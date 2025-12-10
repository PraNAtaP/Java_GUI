package view;

import controller.TransaksiController;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class FormTransaksi extends JFrame {
    private JComboBox<String> cmbPelanggan, cmbProduk;
    private JTextField txtJumlah;
    private JTable tblKeranjang;
    private DefaultTableModel modelKeranjang;
    private JLabel lblTotal;
    private TransaksiController transaksiController;

    public FormTransaksi() {
        setTitle("Transaksi Penjualan");
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        DefaultComboBoxModel<String> produkComboBoxModel = new DefaultComboBoxModel<>();
        DefaultComboBoxModel<String> pelangganComboBoxModel = new DefaultComboBoxModel<>();
        modelKeranjang = new DefaultTableModel(new String[]{"ID Produk", "Nama Produk", "Harga", "Jumlah", "Subtotal"}, 0);
        
        transaksiController = new TransaksiController(produkComboBoxModel, pelangganComboBoxModel, modelKeranjang, null, this);

        JPanel panelAtas = new JPanel(new GridLayout(2, 2, 5, 5));
        panelAtas.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panelAtas.add(new JLabel("Pilih Pelanggan:"));
        cmbPelanggan = new JComboBox<>(pelangganComboBoxModel);
        panelAtas.add(cmbPelanggan);

        panelAtas.add(new JLabel("Pilih Produk:"));
        cmbProduk = new JComboBox<>(produkComboBoxModel);
        panelAtas.add(cmbProduk);
        add(panelAtas, BorderLayout.NORTH);

        JPanel panelTengah = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelTengah.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
        panelTengah.add(new JLabel("Jumlah:"));
        txtJumlah = new JTextField(5);
        panelTengah.add(txtJumlah);
        JButton btnTambahKeranjang = new JButton("Tambah ke Keranjang");
        panelTengah.add(btnTambahKeranjang);

        JPanel panelKeranjang = new JPanel(new BorderLayout());
        panelKeranjang.add(panelTengah, BorderLayout.NORTH);

        tblKeranjang = new JTable(modelKeranjang);
        panelKeranjang.add(new JScrollPane(tblKeranjang), BorderLayout.CENTER);
        add(panelKeranjang, BorderLayout.CENTER);

        JPanel panelBawah = new JPanel(new BorderLayout());
        panelBawah.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        lblTotal = new JLabel("Total: Rp 0.0");
        lblTotal.setFont(new Font("Arial", Font.BOLD, 20));
        panelBawah.add(lblTotal, BorderLayout.WEST);

        JButton btnSimpan = new JButton("Simpan Transaksi");
        panelBawah.add(btnSimpan, BorderLayout.EAST);
        add(panelBawah, BorderLayout.SOUTH);

        transaksiController.loadInitialDataForCreate();

        btnTambahKeranjang.addActionListener(e -> {
            tambahKeKeranjang();
            updateTotal();
        });

        btnSimpan.addActionListener(e -> simpanTransaksi());
    }

    private void tambahKeKeranjang() {
        try {
            int produkIndex = cmbProduk.getSelectedIndex();
            int jumlah = Integer.parseInt(txtJumlah.getText());
            transaksiController.tambahKeKeranjang(produkIndex, jumlah);
            txtJumlah.setText("");
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Jumlah harus berupa angka.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateTotal() {
        double total = transaksiController.getTotalBelanja();
        lblTotal.setText(String.format("Total: Rp %.2f", total));
    }

    private void simpanTransaksi() {
        int pelangganIndex = cmbPelanggan.getSelectedIndex();
        String[] metodeBayar = {"Tunai", "QRIS", "Transfer Bank"};
        String metode = (String) JOptionPane.showInputDialog(this, "Pilih metode pembayaran:",
                "Metode Pembayaran", JOptionPane.QUESTION_MESSAGE, null, metodeBayar, metodeBayar[0]);

        if (metode != null) {
            transaksiController.simpanTransaksi(pelangganIndex, metode);
            updateTotal(); 
        }
    }
}