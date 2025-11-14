package view;

import controller.PelangganController;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class FormPelanggan extends JFrame {
    private JTextField txtNama, txtEmail, txtNoHp;
    private JTable tblPelanggan;
    private DefaultTableModel model;
    private PelangganController pelangganController;
    private JTextField txtId;

    public FormPelanggan() {
        setTitle("Kelola Pelanggan");
        setSize(600, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        model = new DefaultTableModel(new String[]{"ID", "Nama", "Email", "No. HP"}, 0);
        pelangganController = new PelangganController(model);

        JPanel panelInput = new JPanel(new GridLayout(4, 2, 5, 5));
        panelInput.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        txtId = new JTextField(); 

        panelInput.add(new JLabel("Nama Pelanggan:"));
        txtNama = new JTextField();
        panelInput.add(txtNama);

        panelInput.add(new JLabel("Email:"));
        txtEmail = new JTextField();
        panelInput.add(txtEmail);

        panelInput.add(new JLabel("No. HP:"));
        txtNoHp = new JTextField();
        panelInput.add(txtNoHp);

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

        tblPelanggan = new JTable(model);
        add(new JScrollPane(tblPelanggan), BorderLayout.SOUTH);

        // Load initial data
        pelangganController.loadData();

        // Event Listeners
        btnTambah.addActionListener(e -> tambahPelanggan());
        btnEdit.addActionListener(e -> ubahPelanggan());
        btnHapus.addActionListener(e -> hapusPelanggan());
        btnRefresh.addActionListener(e -> pelangganController.loadData());

        tblPelanggan.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int selectedRow = tblPelanggan.getSelectedRow();
                if (selectedRow != -1) {
                    txtId.setText(model.getValueAt(selectedRow, 0).toString());
                    txtNama.setText(model.getValueAt(selectedRow, 1).toString());
                    txtEmail.setText(model.getValueAt(selectedRow, 2).toString());
                    txtNoHp.setText(model.getValueAt(selectedRow, 3).toString());
                }
            }
        });
    }

    private void tambahPelanggan() {
        if (!validateInput()) return;
        String nama = txtNama.getText();
        String email = txtEmail.getText();
        String noHp = txtNoHp.getText();
        pelangganController.tambahPelanggan(nama, email, noHp);
        clearFields();
    }

    private void ubahPelanggan() {
        if (txtId.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Pilih pelanggan yang akan diubah.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (!validateInput()) return;
        int id = Integer.parseInt(txtId.getText());
        String nama = txtNama.getText();
        String email = txtEmail.getText();
        String noHp = txtNoHp.getText();
        pelangganController.ubahPelanggan(id, nama, email, noHp);
        clearFields();
    }

    private void hapusPelanggan() {
        if (txtId.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Pilih pelanggan yang akan dihapus.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        int id = Integer.parseInt(txtId.getText());
        int confirm = JOptionPane.showConfirmDialog(this, "Anda yakin ingin menghapus pelanggan ini?", "Konfirmasi", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            pelangganController.hapusPelanggan(id);
            clearFields();
        }
    }

    private boolean validateInput() {
        if (txtNama.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nama pelanggan tidak boleh kosong.", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        // Basic email validation
        if (!txtEmail.getText().trim().matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
            JOptionPane.showMessageDialog(this, "Format email tidak valid.", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        // Basic phone number validation
        if (!txtNoHp.getText().trim().matches("^\\+?[0-9. ()-]{7,25}$")) {
            JOptionPane.showMessageDialog(this, "Format nomor HP tidak valid.", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }

    private void clearFields() {
        txtId.setText("");
        txtNama.setText("");
        txtEmail.setText("");
        txtNoHp.setText("");
        tblPelanggan.clearSelection();
    }
}