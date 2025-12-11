package model;
//Produk Model
public class Produk {
    private int id_produk;
    private String nama_produk;
    private double harga;
    private int stok;

    public int getId_produk() {
        return id_produk;
    }

    public void setId_produk(int id_produk) {
        this.id_produk = id_produk;
    }

    public String getNama_produk() {
        return nama_produk;
    }

    public void setNama_produk(String nama_produk) {
        this.nama_produk = nama_produk;
    }

    public double getHarga() {
        return harga;
    }

    public void setHarga(double harga) {
        this.harga = harga;
    }

    public int getStok() {
        return stok;
    }

    public void setStok(int stok) {
        this.stok = stok;
    }

    public String tampilInfo() {
        return "ID: " + id_produk + ", Nama: " + nama_produk + ", Harga: " + harga + ", Stok: " + stok;
    }
}
