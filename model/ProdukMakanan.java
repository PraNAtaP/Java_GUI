package model;

import java.util.Date;

public class ProdukMakanan extends Produk {
    private Date tgl_kadaluarsa;

    public Date getTgl_kadaluarsa() {
        return tgl_kadaluarsa;
    }

    public void setTgl_kadaluarsa(Date tgl_kadaluarsa) {
        this.tgl_kadaluarsa = tgl_kadaluarsa;
    }

    @Override
    public String tampilInfo() {
        return super.tampilInfo() + ", Tanggal Kadaluarsa: " + tgl_kadaluarsa;
    }
}
