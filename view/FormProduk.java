package view;

import controller.ProdukController;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class FormProduk extends JFrame {
    private JTextField txtNama, txtHarga, txtStok;
    private JTable tblProduk;
    private DefaultTableModel model;
    private ProdukController produkController;
    private JTextField txtId; 

    public FormProduk() {
        setTitle("Kelola Produk");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        model = new DefaultTableModel(new String[]{"ID", "Nama", "Harga", "Stok"}, 0);
        produkController = new ProdukController(model);

        JPanel panelInput = new JPanel(new GridLayout(4, 2, 5, 5));
        panelInput.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        txtId = new JTextField(); 
        
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

        tblProduk = new JTable(model);
        add(new JScrollPane(tblProduk), BorderLayout.CENTER);
        
        JPanel panelButton = new JPanel();
        JButton btnTambah = new JButton("Tambah");
        JButton btnEdit = new JButton("Edit");
        JButton btnHapus = new JButton("Hapus");
        JButton btnRefresh = new JButton("Refresh");

        panelButton.add(btnTambah);
        panelButton.add(btnEdit);
        panelButton.add(btnHapus);
        panelButton.add(btnRefresh);
        add(panelButton, BorderLayout.SOUTH);

        produkController.loadData();
        
        pack();
        setLocationRelativeTo(null);
        
        btnTambah.addActionListener(e -> tambahProduk());
        btnEdit.addActionListener(e -> ubahProduk());
        btnHapus.addActionListener(e -> hapusProduk());
        btnRefresh.addActionListener(e -> produkController.loadData());

        tblProduk.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int selectedRow = tblProduk.getSelectedRow();
                if (selectedRow != -1) {
                    txtId.setText(model.getValueAt(selectedRow, 0).toString());
                    txtNama.setText(model.getValueAt(selectedRow, 1).toString());
                    txtHarga.setText(model.getValueAt(selectedRow, 2).toString());
                    txtStok.setText(model.getValueAt(selectedRow, 3).toString());
                }
            }
        });
    }

    private void tambahProduk() {
        if (!validateInput()) return;
        String nama = txtNama.getText();
        double harga = Double.parseDouble(txtHarga.getText());
        int stok = Integer.parseInt(txtStok.getText());
        produkController.tambahProduk(nama, harga, stok);
        clearFields();
    }

    private void ubahProduk() {
        if (txtId.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Pilih produk yang akan diubah.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (!validateInput()) return;
        int id = Integer.parseInt(txtId.getText());
        String nama = txtNama.getText();
        double harga = Double.parseDouble(txtHarga.getText());
        int stok = Integer.parseInt(txtStok.getText());
        produkController.ubahProduk(id, nama, harga, stok);
        clearFields();
    }

    private void hapusProduk() {
        if (txtId.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Pilih produk yang akan dihapus.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        int id = Integer.parseInt(txtId.getText());
        int confirm = JOptionPane.showConfirmDialog(this, "Anda yakin ingin menghapus produk ini?", "Konfirmasi", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            produkController.hapusProduk(id);
            clearFields();
        }
    }

    private boolean validateInput() {
        if (txtNama.getText().trim().isEmpty() || txtHarga.getText().trim().isEmpty() || txtStok.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Semua field harus diisi.", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        try {
            double harga = Double.parseDouble(txtHarga.getText());
            int stok = Integer.parseInt(txtStok.getText());
            if (harga < 0 || stok < 0) {
                JOptionPane.showMessageDialog(this, "Harga dan stok tidak boleh negatif.", "Error", JOptionPane.ERROR_MESSAGE);
                return false;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Harga dan stok harus berupa angka.", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }

    private void clearFields() {
        txtId.setText("");
        txtNama.setText("");
        txtHarga.setText("");
        txtStok.setText("");
        tblProduk.clearSelection();
    }
}