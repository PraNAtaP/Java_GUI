package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Menu extends JFrame {
    private JFrame frame;
    private JTable table;
    private JButton button;
    
    public Menu() {
        setTitle("Aplikasi Kasir Toko");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1000, 600);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JLabel lblJudul = new JLabel("Menu Utama Aplikasi Kasir Toko" , JLabel.CENTER);
        lblJudul.setFont(new Font("Arial", Font.BOLD, 30));
        add(lblJudul, BorderLayout.NORTH);


        JPanel panelTombol = new JPanel(new GridLayout(2, 2, 10, 10));
        JButton btnProduk = new JButton("Data Produk");
        JButton btnPelanggan = new JButton("Data Pelanggan");
        JButton btnTransaksi = new JButton("Transaksi Penjualan");
        JButton btnRiwayat = new JButton("Riwayat Transaksi");

        panelTombol.add(btnProduk);
        panelTombol.add(btnPelanggan);
        panelTombol.add(btnTransaksi);
        panelTombol.add(btnRiwayat);
        add(panelTombol, BorderLayout.CENTER);

        btnProduk.addActionListener(e -> new FormProduk().setVisible(true));
        btnPelanggan.addActionListener(e -> new FormPelanggan().setVisible(true));
        btnTransaksi.addActionListener(e -> new FormTransaksi().setVisible(true));
        btnRiwayat.addActionListener(e -> new FormRiwayatTransaksi().setVisible(true));
    }
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Menu menu = new Menu();
            menu.setVisible(true);
        });
    }
}
