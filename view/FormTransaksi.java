package view;

import controller.TransaksiController;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class FormTransaksi extends JFrame {
    private JComboBox<String> cmbPelanggan, cmbProduk;
    private JTextField txtJumlah;
    private JTable tblKeranjang, tblTransaksi;
    private DefaultTableModel modelKeranjang, modelTransaksi;
    private JLabel lblTotal;
    private TransaksiController transaksiController;

    public FormTransaksi() {
        setTitle("Transaksi Penjualan");
        setSize(800, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        // --- Bagian Atas: Form untuk membuat transaksi baru ---
        JPanel panelPembuatan = new JPanel(new BorderLayout(10, 10));
        panelPembuatan.setBorder(BorderFactory.createTitledBorder("Buat Transaksi Baru"));

        // Initialize Controller and Models
        DefaultComboBoxModel<String> produkComboBoxModel = new DefaultComboBoxModel<>();
        DefaultComboBoxModel<String> pelangganComboBoxModel = new DefaultComboBoxModel<>();
        modelKeranjang = new DefaultTableModel(new String[]{"ID Produk", "Nama Produk", "Harga", "Jumlah", "Subtotal"}, 0);
        modelTransaksi = new DefaultTableModel(new String[]{"ID Transaksi", "Pelanggan", "Tanggal", "Total", "Metode Bayar"}, 0);

        transaksiController = new TransaksiController(produkComboBoxModel, pelangganComboBoxModel, modelKeranjang, modelTransaksi, this);

        // Top Panel: Pelanggan and Produk Selection
        JPanel panelAtas = new JPanel(new GridLayout(2, 2, 5, 5));
        panelAtas.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panelAtas.add(new JLabel("Pilih Pelanggan:"));
        cmbPelanggan = new JComboBox<>(pelangganComboBoxModel);
        panelAtas.add(cmbPelanggan);

        panelAtas.add(new JLabel("Pilih Produk:"));
        cmbProduk = new JComboBox<>(produkComboBoxModel);
        panelAtas.add(cmbProduk);
        panelPembuatan.add(panelAtas, BorderLayout.NORTH);

        // Center Panel: Add to Cart
        JPanel panelTengah = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelTengah.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
        panelTengah.add(new JLabel("Jumlah:"));
        txtJumlah = new JTextField(5);
        panelTengah.add(txtJumlah);
        JButton btnTambahKeranjang = new JButton("Tambah ke Keranjang");
        panelTengah.add(btnTambahKeranjang);

        JPanel panelKeranjang = new JPanel(new BorderLayout());
        panelKeranjang.add(panelTengah, BorderLayout.NORTH);

        // Cart Table
        tblKeranjang = new JTable(modelKeranjang);
        panelKeranjang.add(new JScrollPane(tblKeranjang), BorderLayout.CENTER);
        panelPembuatan.add(panelKeranjang, BorderLayout.CENTER);

        // Bottom Panel: Total and Save Button
        JPanel panelBawah = new JPanel(new BorderLayout());
        panelBawah.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        lblTotal = new JLabel("Total: Rp 0.0");
        lblTotal.setFont(new Font("Arial", Font.BOLD, 20));
        panelBawah.add(lblTotal, BorderLayout.WEST);

        JButton btnSimpan = new JButton("Simpan Transaksi");
        panelBawah.add(btnSimpan, BorderLayout.EAST);
        panelPembuatan.add(panelBawah, BorderLayout.SOUTH);

        // --- Bagian Bawah: Tabel untuk menampilkan daftar transaksi ---
        JPanel panelList = new JPanel(new BorderLayout());
        panelList.setBorder(BorderFactory.createTitledBorder("Riwayat Transaksi"));
        tblTransaksi = new JTable(modelTransaksi);
        panelList.add(new JScrollPane(tblTransaksi), BorderLayout.CENTER);

        // --- Split Pane untuk menggabungkan keduanya ---
        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, panelPembuatan, panelList);
        splitPane.setDividerLocation(350);
        add(splitPane);


        // Load initial data
        transaksiController.loadInitialData();

        // Event Listeners
        btnTambahKeranjang.addActionListener(e -> {
            tambahKeKeranjang();
            updateTotal();
        });

        btnSimpan.addActionListener(e -> simpanTransaksi());

        tblTransaksi.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) { // Double-click
                    int selectedRow = tblTransaksi.getSelectedRow();
                    transaksiController.showDetailTransaksi(selectedRow);
                }
            }
        });
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
        String[] metodeBayar = {"Tunai", "Kartu Kredit", "Transfer Bank"};
        String metode = (String) JOptionPane.showInputDialog(this, "Pilih metode pembayaran:",
                "Metode Pembayaran", JOptionPane.QUESTION_MESSAGE, null, metodeBayar, metodeBayar[0]);

        if (metode != null) {
            transaksiController.simpanTransaksi(pelangganIndex, metode);
            updateTotal(); // Reset total label
            transaksiController.loadAllTransaksi(); // Refresh transaction list
        }
    }
}