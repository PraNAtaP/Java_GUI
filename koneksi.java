import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class koneksi {
    private String url = "jdbc:mysql://localhost:3306/pbo_gui_kasir_toko";
    private String USER = "root";
    private String PASS = "";

    private Connection conn;

    public Connection getConnection() {
        if (conn == null) {
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
                conn = DriverManager.getConnection(url, USER, PASS);
                System.out.println("Koneksi Berhasil");
            } catch (ClassNotFoundException e) {
                System.out.println("Driver tidak ditemukan: " + e.getMessage());
            } catch (SQLException e) {
                System.out.println("Koneksi Gagal: " + e.getMessage());
            }
        }
        return conn;
    }

}
