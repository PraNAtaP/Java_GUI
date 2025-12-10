package dao;

import config.koneksi;
import model.DetailTransaksi;
import model.Transaksi;

import java.sql.*;

public class TransaksiDAO {
    private Connection conn;

    public TransaksiDAO() {
        this.conn = koneksi.getConnection();
    }

    public int simpanTransaksi(Transaksi transaksi, java.util.List<DetailTransaksi> detailList) {
        String sqlTransaksi = "INSERT INTO transaksi (id_pelanggan, tanggal, total_harga, metode_bayar) VALUES (?, ?, ?, ?)";
        String sqlDetail = "INSERT INTO detail_transaksi (id_transaksi, id_produk, jumlah) VALUES (?, ?, ?)";
        String sqlUpdateStok = "UPDATE produk SET stok = stok - ? WHERE id_produk = ?";
        
        int idTransaksi = -1;

        try {
            conn.setAutoCommit(false);

            try (PreparedStatement stmtTransaksi = conn.prepareStatement(sqlTransaksi, Statement.RETURN_GENERATED_KEYS)) {
                stmtTransaksi.setInt(1, transaksi.getId_pelanggan());
                stmtTransaksi.setDate(2, new java.sql.Date(transaksi.getTanggal().getTime()));
                stmtTransaksi.setDouble(3, transaksi.getTotal_harga());
                stmtTransaksi.setString(4, transaksi.getMetode_bayar());
                stmtTransaksi.executeUpdate();

                ResultSet generatedKeys = stmtTransaksi.getGeneratedKeys();
                if (generatedKeys.next()) {
                    idTransaksi = generatedKeys.getInt(1);
                } else {
                    throw new SQLException("Gagal membuat transaksi, tidak ada ID yang didapat.");
                }
            }

            try (PreparedStatement stmtDetail = conn.prepareStatement(sqlDetail)) {
                for (DetailTransaksi detail : detailList) {
                    stmtDetail.setInt(1, idTransaksi);
                    stmtDetail.setInt(2, detail.getId_produk());
                    stmtDetail.setInt(3, detail.getJumlah());
                    stmtDetail.addBatch();
                }
                stmtDetail.executeBatch();
            }

            try (PreparedStatement stmtUpdateStok = conn.prepareStatement(sqlUpdateStok)) {
                for (DetailTransaksi detail : detailList) {
                    stmtUpdateStok.setInt(1, detail.getJumlah());
                    stmtUpdateStok.setInt(2, detail.getId_produk());
                    stmtUpdateStok.addBatch();
                }
                stmtUpdateStok.executeBatch();
            }

            conn.commit();

        } catch (SQLException e) {
            e.printStackTrace();
            try {
                conn.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            return -1; 
        } finally {
            try {
                conn.setAutoCommit(true);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        
        return idTransaksi;
    }

    public java.util.List<Transaksi> getAllTransaksi() {
        java.util.List<Transaksi> transaksiList = new java.util.ArrayList<>();
        String sql = "SELECT t.id_transaksi, t.id_pelanggan, p.nama_pelanggan, t.tanggal, t.total_harga, t.metode_bayar " +
                     "FROM transaksi t JOIN pelanggan p ON t.id_pelanggan = p.id_pelanggan ORDER BY t.id_transaksi DESC";
        try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Transaksi t = new Transaksi();
                t.setId_transaksi(rs.getInt("id_transaksi"));
                t.setId_pelanggan(rs.getInt("id_pelanggan"));
                t.setNama_pelanggan(rs.getString("nama_pelanggan"));
                t.setTanggal(rs.getDate("tanggal"));
                t.setTotal_harga(rs.getDouble("total_harga"));
                t.setMetode_bayar(rs.getString("metode_bayar"));
                transaksiList.add(t);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return transaksiList;
    }

    public java.util.List<DetailTransaksi> getDetailTransaksiById(int idTransaksi) {
        java.util.List<DetailTransaksi> detailList = new java.util.ArrayList<>();
        String sql = "SELECT dt.id_detail_transaksi, dt.id_produk, p.nama_produk, p.harga, dt.jumlah " +
                     "FROM detail_transaksi dt JOIN produk p ON dt.id_produk = p.id_produk " +
                     "WHERE dt.id_transaksi = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idTransaksi);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                DetailTransaksi dt = new DetailTransaksi();
                dt.setId_detail_transaksi(rs.getInt("id_detail_transaksi"));
                dt.setId_produk(rs.getInt("id_produk"));
                dt.setNama_produk(rs.getString("nama_produk"));
                dt.setHarga(rs.getDouble("harga"));
                dt.setJumlah(rs.getInt("jumlah"));
                dt.setSubtotal(rs.getDouble("harga") * rs.getInt("jumlah"));
                detailList.add(dt);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return detailList;
    }
}