package dao;

import database.ConnexioBD;
import model.Tiquet;
import model.LiniaTiquet;

import java.sql.*;
import java.util.ArrayList;

public class TiquetDAO {
    public int inserirTiquet(Tiquet tiquet) {
        String sql = "INSERT INTO tiquets (data_compra, dni_client, total_base, total_iva, total_final) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = ConnexioBD.connectar();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            ps.setString(1, tiquet.getData());
            ps.setString(2, tiquet.getDniClient());
            ps.setDouble(3, tiquet.getTotalBase());
            ps.setDouble(4, tiquet.getTotalIva());
            ps.setDouble(5, tiquet.getTotalFinal());
            
            int filesAfectades = ps.executeUpdate();
            
            if (filesAfectades > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        return rs.getInt(1);
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println("Error inserint capçalera del tiquet:");
            e.printStackTrace();
        }
        return -1;
    }

    public boolean inserirLinia(LiniaTiquet linia) {
        String sql = "INSERT INTO linies_tiquet (id_tiquet, id_article, quantitat, preu_base, iva, preu_final) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = ConnexioBD.connectar();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, linia.getIdTiquet());
            ps.setInt(2, linia.getIdArticle());
            ps.setInt(3, linia.getQuantitat());
            ps.setDouble(4, linia.getPreuBase());
            ps.setDouble(5, linia.getIva());
            ps.setDouble(6, linia.getPreuFinal());
            
            int filesAfectades = ps.executeUpdate();
            return filesAfectades > 0;
        } catch (SQLException e) {
            System.out.println("Error inserint linia de tiquet");
            e.printStackTrace();
        }
        return false;
    }

    public ArrayList<Tiquet> obtenirTiquetsByClient(String dniClient) {
        ArrayList<Tiquet> tiquets = new ArrayList<>();
        String sql = "SELECT * FROM tiquets WHERE dni_client = ?";
        
        try (Connection conn = ConnexioBD.connectar();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, dniClient);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Tiquet t = new Tiquet(
                        rs.getInt("id"),
                        rs.getString("data_compra"),
                        rs.getString("dni_client"),
                        rs.getDouble("total_base"),
                        rs.getDouble("total_iva"),
                        rs.getDouble("total_final")
                    );
                    tiquets.add(t);
                }
            }
        } catch (SQLException e) {
            System.out.println("Error obtenint tiquets per client:");
            e.printStackTrace();
        }
        return tiquets;
    }

    public ArrayList<LiniaTiquet> obtenirLiniesByArticle(int idArticle) {
        ArrayList<LiniaTiquet> linies = new ArrayList<>();
        String sql = "SELECT * FROM linies_factura WHERE id_article = ?";

        try (Connection conn = ConnexioBD.connectar();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, idArticle);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    LiniaTiquet l = new LiniaTiquet(
                        rs.getInt("id_tiquet"),
                        rs.getInt("id_article"),
                        rs.getInt("quantitat"),
                        rs.getDouble("preu_base"),
                        rs.getInt("iva"),
                        rs.getDouble("preu_final")
                    );
                    linies.add(l);
                }
            }
        } catch (SQLException e) {
            System.out.println("Error obtenint linies per article:");
            e.printStackTrace();
        }
        return linies;
    }
}