package controller;

import dao.PelangganDAO;
import dao.ProdukDAO;
import dao.TransaksiDAO;
import model.DetailTransaksi;
import model.Pelanggan;
import model.Produk;
import model.Transaksi;
import view.ViewDetailTransaksi;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TransaksiController {
    private PelangganDAO pelangganDAO;
    private ProdukDAO produkDAO;
    private TransaksiDAO transaksiDAO;
    private List<Produk> produkList;
    private List<Pelanggan> pelangganList;
    private List<DetailTransaksi> keranjang;
    private DefaultComboBoxModel<String> produkComboBoxModel;
    private DefaultComboBoxModel<String> pelangganComboBoxModel;
    private DefaultTableModel keranjangTableModel;
    private DefaultTableModel transaksiTableModel;
    private List<Transaksi> transaksiList;
    private JFrame view;

    public TransaksiController(DefaultComboBoxModel<String> produkComboBoxModel, DefaultComboBoxModel<String> pelangganComboBoxModel, DefaultTableModel keranjangTableModel, DefaultTableModel transaksiTableModel, JFrame view) {
        this.pelangganDAO = new PelangganDAO();
        this.produkDAO = new ProdukDAO();
        this.transaksiDAO = new TransaksiDAO();
        this.produkList = new ArrayList<>();
        this.pelangganList = new ArrayList<>();
        this.transaksiList = new ArrayList<>();
        this.keranjang = new ArrayList<>();
        this.produkComboBoxModel = produkComboBoxModel;
        this.pelangganComboBoxModel = pelangganComboBoxModel;
        this.keranjangTableModel = keranjangTableModel;
        this.transaksiTableModel = transaksiTableModel;
        this.view = view;
    }

    public void loadInitialData() {
        // Load Pelanggan
        pelangganList = pelangganDAO.tampilData();
        pelangganComboBoxModel.removeAllElements();
        for (Pelanggan p : pelangganList) {
            pelangganComboBoxModel.addElement(p.getId_pelanggan() + " - " + p.getNama_pelanggan());
        }

        // Load Produk
        produkList = produkDAO.tampilData();
        produkComboBoxModel.removeAllElements();
        for (Produk p : produkList) {
            produkComboBoxModel.addElement(p.getId_produk() + " - " + p.getNama_produk() + " (Stok: " + p.getStok() + ")");
        }
        
        loadAllTransaksi();
    }

    public void loadAllTransaksi() {
        transaksiList = transaksiDAO.getAllTransaksi();
        transaksiTableModel.setRowCount(0);
        for (Transaksi t : transaksiList) {
            transaksiTableModel.addRow(new Object[]{
                    t.getId_transaksi(),
                    t.getNama_pelanggan(),
                    t.getTanggal(),
                    t.getTotal_harga(),
                    t.getMetode_bayar()
            });
        }
    }
    
    public void showDetailTransaksi(int selectedRow) {
        if (selectedRow >= 0) {
            Transaksi selectedTransaksi = transaksiList.get(selectedRow);
            List<DetailTransaksi> detailItems = transaksiDAO.getDetailTransaksiById(selectedTransaksi.getId_transaksi());

            // Assuming 'view' is the parent JFrame (FormTransaksi)
            ViewDetailTransaksi detailView = new ViewDetailTransaksi(view, selectedTransaksi, detailItems);
            detailView.setVisible(true);
        }
    }

    public void tambahKeKeranjang(int produkIndex, int jumlah) {
        if (produkIndex < 0 || jumlah <= 0) {
            JOptionPane.showMessageDialog(null, "Pilih produk dan masukkan jumlah yang valid.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Produk produkDipilih = produkList.get(produkIndex);

        if (jumlah > produkDipilih.getStok()) {
            JOptionPane.showMessageDialog(null, "Stok tidak mencukupi. Stok tersedia: " + produkDipilih.getStok(), "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        double subtotal = produkDipilih.getHarga() * jumlah;

        DetailTransaksi item = new DetailTransaksi();
        item.setId_produk(produkDipilih.getId_produk());
        item.setNama_produk(produkDipilih.getNama_produk());
        item.setHarga(produkDipilih.getHarga());
        item.setJumlah(jumlah);
        item.setSubtotal(subtotal);

        keranjang.add(item);
        updateKeranjangTable();
    }

    private void updateKeranjangTable() {
        keranjangTableModel.setRowCount(0);
        for (DetailTransaksi item : keranjang) {
            keranjangTableModel.addRow(new Object[]{
                    item.getId_produk(),
                    item.getNama_produk(),
                    item.getHarga(),
                    item.getJumlah(),
                    item.getSubtotal()
            });
        }
    }

    public double getTotalBelanja() {
        double total = 0;
        for (DetailTransaksi item : keranjang) {
            total += item.getSubtotal();
        }
        return total;
    }

    public void simpanTransaksi(int pelangganIndex, String metodeBayar) {
        if (pelangganIndex < 0) {
            JOptionPane.showMessageDialog(null, "Pilih pelanggan.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (keranjang.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Keranjang belanja kosong.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Pelanggan pelangganDipilih = pelangganList.get(pelangganIndex);

        Transaksi transaksi = new Transaksi();
        transaksi.setId_pelanggan(pelangganDipilih.getId_pelanggan());
        transaksi.setTanggal(new Date());
        transaksi.setTotal_harga(getTotalBelanja());
        transaksi.setMetode_bayar(metodeBayar);

        int idTransaksi = transaksiDAO.simpanTransaksi(transaksi, keranjang);

        if (idTransaksi != -1) {
            JOptionPane.showMessageDialog(null, "Transaksi berhasil disimpan dengan ID: " + idTransaksi);
            keranjang.clear();
            updateKeranjangTable();
            loadInitialData(); // Refresh product stock in combobox
        } else {
            JOptionPane.showMessageDialog(null, "Gagal menyimpan transaksi.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
