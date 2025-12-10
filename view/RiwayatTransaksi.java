package view;

import controller.TransaksiController;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class RiwayatTransaksi extends JFrame {
    private JTable tblTransaksi;
    private DefaultTableModel modelTransaksi;
    private TransaksiController transaksiController;

    public RiwayatTransaksi() {
        setTitle("Riwayat Transaksi");
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());
        
        modelTransaksi = new DefaultTableModel(new String[]{"ID Transaksi", "Pelanggan", "Tanggal", "Total", "Metode Bayar"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; 
            }
        };
    
        transaksiController = new TransaksiController(null, null, null, modelTransaksi, this);
        
        JPanel panelList = new JPanel(new BorderLayout());
        panelList.setBorder(BorderFactory.createTitledBorder("Riwayat Transaksi"));
        tblTransaksi = new JTable(modelTransaksi);
        panelList.add(new JScrollPane(tblTransaksi), BorderLayout.CENTER);
        add(panelList, BorderLayout.CENTER);

        transaksiController.loadAllTransaksi();

        tblTransaksi.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) { 
                    int selectedRow = tblTransaksi.getSelectedRow();
                    transaksiController.showDetailTransaksi(selectedRow);
                }
            }
        });
    }
}
