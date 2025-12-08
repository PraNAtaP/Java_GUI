package view;

import model.DetailTransaksi;
import model.Transaksi;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.List;

public class ViewDetailTransaksi extends JDialog {
    private JLabel lblIdTransaksi, lblNamaPelanggan, lblTanggal;
    private JTable tblDetail;
    private DefaultTableModel modelDetail;

    public ViewDetailTransaksi(JFrame parent, Transaksi transaksi, List<DetailTransaksi> detailItems) {
        super(parent, "Detail Transaksi", true);
        setSize(600, 400);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout(10, 10));

        // Top Panel for Transaction Info
        JPanel panelInfo = new JPanel(new GridLayout(3, 2, 5, 5));
        panelInfo.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        panelInfo.add(new JLabel("ID Transaksi:"));
        lblIdTransaksi = new JLabel();
        panelInfo.add(lblIdTransaksi);

        panelInfo.add(new JLabel("Nama Pelanggan:"));
        lblNamaPelanggan = new JLabel();
        panelInfo.add(lblNamaPelanggan);

        panelInfo.add(new JLabel("Tanggal:"));
        lblTanggal = new JLabel();
        panelInfo.add(lblTanggal);

        add(panelInfo, BorderLayout.NORTH);

        // Center Panel for Details Table
        modelDetail = new DefaultTableModel(new String[]{"Nama Produk", "Harga", "Jumlah", "Subtotal"}, 0);
        tblDetail = new JTable(modelDetail);
        add(new JScrollPane(tblDetail), BorderLayout.CENTER);

        // Populate data
        populateData(transaksi, detailItems);
    }

    private void populateData(Transaksi transaksi, List<DetailTransaksi> detailItems) {
        lblIdTransaksi.setText(String.valueOf(transaksi.getId_transaksi()));
        lblNamaPelanggan.setText(transaksi.getNama_pelanggan()); // Assuming this is set in the Transaksi object
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        lblTanggal.setText(sdf.format(transaksi.getTanggal()));

        modelDetail.setRowCount(0);
        for (DetailTransaksi item : detailItems) {
            modelDetail.addRow(new Object[]{
                    item.getNama_produk(),
                    item.getHarga(),
                    item.getJumlah(),
                    item.getSubtotal()
            });
        }
    }
}
