package dao;

import config.koneksi;
import model.Pelanggan;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PelangganDAO implements DAO<Pelanggan> {
    private Connection conn;

    public PelangganDAO() {
        this.conn = koneksi.getConnection();
    }

    @Override
    public void simpan(Pelanggan pelanggan) {
        String sql = "INSERT INTO pelanggan (nama_pelanggan, email, no_hp) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, pelanggan.getNama_pelanggan());
            stmt.setString(2, pelanggan.getEmail());
            stmt.setString(3, pelanggan.getNo_hp());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void ubah(Pelanggan pelanggan) {
        String sql = "UPDATE pelanggan SET nama_pelanggan = ?, email = ?, no_hp = ? WHERE id_pelanggan = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, pelanggan.getNama_pelanggan());
            stmt.setString(2, pelanggan.getEmail());
            stmt.setString(3, pelanggan.getNo_hp());
            stmt.setInt(4, pelanggan.getId_pelanggan());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void hapus(int id) {
        String sql = "DELETE FROM pelanggan WHERE id_pelanggan = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Pelanggan ambilData(int id) {
        String sql = "SELECT * FROM pelanggan WHERE id_pelanggan = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Pelanggan pelanggan = new Pelanggan();
                pelanggan.setId_pelanggan(rs.getInt("id_pelanggan"));
                pelanggan.setNama_pelanggan(rs.getString("nama_pelanggan"));
                pelanggan.setEmail(rs.getString("email"));
                pelanggan.setNo_hp(rs.getString("no_hp"));
                return pelanggan;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Pelanggan> tampilData() {
        List<Pelanggan> dataPelanggan = new ArrayList<>();
        String sql = "SELECT * FROM pelanggan";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Pelanggan pelanggan = new Pelanggan();
                pelanggan.setId_pelanggan(rs.getInt("id_pelanggan"));
                pelanggan.setNama_pelanggan(rs.getString("nama_pelanggan"));
                pelanggan.setEmail(rs.getString("email"));
                pelanggan.setNo_hp(rs.getString("no_hp"));
                dataPelanggan.add(pelanggan);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return dataPelanggan;
    }
}
