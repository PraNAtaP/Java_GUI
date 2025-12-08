package view;

import controller.TransaksiController;
import model.Transaksi;
import model.DetailTransaksi;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class FormRiwayatTransaksi extends JFrame {
    private JTable tblTransaksi;
    private DefaultTableModel modelTransaksi;
    private TransaksiController transaksiController; // This might need its own controller later

    public FormRiwayatTransaksi() {
        setTitle("Riwayat Transaksi");
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // Initialize Controller and Model
        modelTransaksi = new DefaultTableModel(new String[]{"ID Transaksi", "Pelanggan", "Tanggal", "Total", "Metode Bayar"}, 0);
        
        // We are re-using TransaksiController for now, but a dedicated RiwayatController would be better.
        // We pass null for the models that are not used here.
        transaksiController = new TransaksiController(null, null, null, modelTransaksi, this);

        // --- Panel to display the list of transactions ---
        JPanel panelList = new JPanel(new BorderLayout());
        panelList.setBorder(BorderFactory.createTitledBorder("Riwayat Transaksi"));
        tblTransaksi = new JTable(modelTransaksi);
        panelList.add(new JScrollPane(tblTransaksi), BorderLayout.CENTER);
        add(panelList, BorderLayout.CENTER);

        // Load initial data
        transaksiController.loadAllTransaksi();

        // Event Listeners
        tblTransaksi.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) { // Double-click
                    int selectedRow = tblTransaksi.getSelectedRow();
                    transaksiController.showDetailTransaksi(selectedRow);
                }
            }
        });
    }
}
