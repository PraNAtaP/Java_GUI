package dao;
//produk DAO
import config.koneksi;
import model.Produk;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProdukDAO implements DAO<Produk> {
    private Connection conn;

    public ProdukDAO() {
        this.conn = koneksi.getConnection();
    }

    @Override
    public void simpan(Produk produk) {
        String sql = "INSERT INTO produk (nama_produk, harga, stok) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, produk.getNama_produk());
            stmt.setDouble(2, produk.getHarga());
            stmt.setInt(3, produk.getStok());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void ubah(Produk produk) {
        String sql = "UPDATE produk SET nama_produk = ?, harga = ?, stok = ? WHERE id_produk = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, produk.getNama_produk());
            stmt.setDouble(2, produk.getHarga());
            stmt.setInt(3, produk.getStok());
            stmt.setInt(4, produk.getId_produk());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void hapus(int id) {
        String sql = "DELETE FROM produk WHERE id_produk = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Produk ambilData(int id) {
        String sql = "SELECT * FROM produk WHERE id_produk = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Produk produk = new Produk();
                produk.setId_produk(rs.getInt("id_produk"));
                produk.setNama_produk(rs.getString("nama_produk"));
                produk.setHarga(rs.getDouble("harga"));
                produk.setStok(rs.getInt("stok"));
                return produk;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Produk> tampilData() {
        List<Produk> dataProduk = new ArrayList<>();
        String sql = "SELECT * FROM produk";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Produk produk = new Produk();
                produk.setId_produk(rs.getInt("id_produk"));
                produk.setNama_produk(rs.getString("nama_produk"));
                produk.setHarga(rs.getDouble("harga"));
                produk.setStok(rs.getInt("stok"));
                dataProduk.add(produk);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return dataProduk;
    }
}
