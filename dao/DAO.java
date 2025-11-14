package dao;

import java.util.List;

public interface DAO<T> {
    void simpan(T object);
    void ubah(T object);
    void hapus(int id);
    T ambilData(int id);
    List<T> tampilData();
}
