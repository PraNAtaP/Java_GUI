package controller;
//Produk Controller
import dao.ProdukDAO;
import model.Produk;

import javax.swing.table.DefaultTableModel;
import java.util.List;

public class ProdukController {
    private ProdukDAO produkDAO;
    private DefaultTableModel tableModel;

    public ProdukController(DefaultTableModel tableModel) {
        this.produkDAO = new ProdukDAO();
        this.tableModel = tableModel;
    }

    public void loadData() {
        tableModel.setRowCount(0);
        List<Produk> produkList = produkDAO.tampilData();
        for (Produk produk : produkList) {
            tableModel.addRow(new Object[]{
                    produk.getId_produk(),
                    produk.getNama_produk(),
                    produk.getHarga(),
                    produk.getStok()
            });
        }
    }

    public void tambahProduk(String nama, double harga, int stok) {
        Produk produk = new Produk();
        produk.setNama_produk(nama);
        produk.setHarga(harga);
        produk.setStok(stok);
        produkDAO.simpan(produk);
        loadData();
    }

    public void ubahProduk(int id, String nama, double harga, int stok) {
        Produk produk = new Produk();
        produk.setId_produk(id);
        produk.setNama_produk(nama);
        produk.setHarga(harga);
        produk.setStok(stok);
        produkDAO.ubah(produk);
        loadData();
    }

    public void hapusProduk(int id) {
        produkDAO.hapus(id);
        loadData();
    }
}
