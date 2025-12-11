package controller;
// pelanggan controler
import dao.PelangganDAO;
import model.Pelanggan;

import javax.swing.table.DefaultTableModel;
import java.util.List;

public class PelangganController {
    private PelangganDAO pelangganDAO;
    private DefaultTableModel tableModel;

    public PelangganController(DefaultTableModel tableModel) {
        this.pelangganDAO = new PelangganDAO();
        this.tableModel = tableModel;
    }

    public void loadData() {
        tableModel.setRowCount(0);
        List<Pelanggan> pelangganList = pelangganDAO.tampilData();
        for (Pelanggan pelanggan : pelangganList) {
            tableModel.addRow(new Object[]{
                    pelanggan.getId_pelanggan(),
                    pelanggan.getNama_pelanggan(),
                    pelanggan.getEmail(),
                    pelanggan.getNo_hp()
            });
        }
    }

    public void tambahPelanggan(String nama, String email, String noHp) {
        Pelanggan pelanggan = new Pelanggan();
        pelanggan.setNama_pelanggan(nama);
        pelanggan.setEmail(email);
        pelanggan.setNo_hp(noHp);
        pelangganDAO.simpan(pelanggan);
        loadData();
    }

    public void ubahPelanggan(int id, String nama, String email, String noHp) {
        Pelanggan pelanggan = new Pelanggan();
        pelanggan.setId_pelanggan(id);
        pelanggan.setNama_pelanggan(nama);
        pelanggan.setEmail(email);
        pelanggan.setNo_hp(noHp);
        pelangganDAO.ubah(pelanggan);
        loadData();
    }

    public void hapusPelanggan(int id) {
        pelangganDAO.hapus(id);
        loadData();
    }
}
