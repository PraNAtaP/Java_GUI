package view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class FormProduk extends JFrame {
    private JTextField txtNama, txtHarga, txtStok;
    private JTable tblProduk;
    private DefaultTableModel model;

    public FormProduk() {
        setTitle("Kelola Produk");
        setSize(600, 400);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JPanel panelInput = new JPanel(new GridLayout(4, 2, 5, 5));
        panelInput.add(new JLabel("Nama Produk:"));
        txtNama = new JTextField();
        panelInput.add(txtNama);

        panelInput.add(new JLabel("Harga:"));
        txtHarga = new JTextField();
        panelInput.add(txtHarga);

        panelInput.add(new JLabel("Stok:"));
        txtStok = new JTextField();
        panelInput.add(txtStok);

        add(panelInput, BorderLayout.NORTH);

        JPanel panelButton = new JPanel();
        JButton btnTambah = new JButton("Tambah");
        JButton btnEdit = new JButton("Edit");
        JButton btnHapus = new JButton("Hapus");
        JButton btnRefresh = new JButton("Refresh");

        panelButton.add(btnTambah);
        panelButton.add(btnEdit);
        panelButton.add(btnHapus);
        panelButton.add(btnRefresh);
        add(panelButton, BorderLayout.CENTER);

        model = new DefaultTableModel(new String[]{"ID", "Nama", "Harga", "Stok"}, 0);
        tblProduk = new JTable(model);
        add(new JScrollPane(tblProduk), BorderLayout.SOUTH);
    }
}
