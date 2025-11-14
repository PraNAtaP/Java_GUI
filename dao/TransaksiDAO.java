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
        String sqlDetail = "INSERT INTO detail_transaksi (id_transaksi, id_pelanggan, jumlah) VALUES (?, ?, ?)";
        String sqlUpdateStok = "UPDATE produk SET stok = stok - ? WHERE id_produk = ?";
        
        int idTransaksi = -1;

        try {
            // Start transaction
            conn.setAutoCommit(false);

            // Insert into transaksi table
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

            // Insert into detail_transaksi table
            try (PreparedStatement stmtDetail = conn.prepareStatement(sqlDetail)) {
                for (DetailTransaksi detail : detailList) {
                    stmtDetail.setInt(1, idTransaksi);
                    stmtDetail.setInt(2, transaksi.getId_pelanggan());
                    stmtDetail.setInt(3, detail.getJumlah());
                    stmtDetail.addBatch();
                }
                stmtDetail.executeBatch();
            }

            // Update product stock
            try (PreparedStatement stmtUpdateStok = conn.prepareStatement(sqlUpdateStok)) {
                for (DetailTransaksi detail : detailList) {
                    stmtUpdateStok.setInt(1, detail.getJumlah());
                    stmtUpdateStok.setInt(2, detail.getId_produk());
                    stmtUpdateStok.addBatch();
                }
                stmtUpdateStok.executeBatch();
            }

            // Commit transaction
            conn.commit();

        } catch (SQLException e) {
            e.printStackTrace();
            try {
                // Rollback transaction on error
                conn.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            return -1; // Return -1 on failure
        } finally {
            try {
                conn.setAutoCommit(true);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        
        return idTransaksi;
    }
}