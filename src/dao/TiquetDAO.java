package dao;

import database.ConnexioBD;
import model.Tiquet;
import model.LiniaTiquet;

import java.sql.*;
import java.util.ArrayList;

public class TiquetDAO {

    // INSERIR TIQUET (I obté l'ID Autogenerat de tornada)
    public int inserirTiquet(Tiquet tiquet) {
        // Canviat 'data' per 'data_compra' segons Script SQL
        String sql = "INSERT INTO tiquets (data_compra, dni_client, total_base, total_iva, total_final) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = ConnexioBD.connectar();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            ps.setString(1, tiquet.getData()); // Format esperat: "YYYY-MM-DD"
            ps.setString(2, tiquet.getDniClient());
            ps.setDouble(3, tiquet.getTotalBase());
            ps.setDouble(4, tiquet.getTotalIva());
            ps.setDouble(5, tiquet.getTotalFinal());
            
            int filesAfectades = ps.executeUpdate();
            
            if (filesAfectades > 0) {
                // Recuperem l'ID que la base de dades li ha assignat automàticament
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        return rs.getInt(1); // Retorna el número d'ID generat
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println("Error inserint capçalera del tiquet:");
            e.printStackTrace();
        }
        return -1; // Si falla retorna -1
    }

    // INSERIR LÍNIA DE FACTURA
    public boolean inserirLinia(LiniaTiquet linia) {
        // Canviada la taula a 'linies_factura'
        String sql = "INSERT INTO linies_factura (id_tiquet, id_article, quantitat, preu_base, iva, preu_final) VALUES (?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = ConnexioBD.connectar();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, linia.getIdTiquet());
            ps.setInt(2, linia.getIdArticle());
            ps.setInt(3, linia.getQuantitat());
            ps.setDouble(4, linia.getPreuBase());
            ps.setInt(5, linia.getIva());
            ps.setDouble(6, linia.getPreuFinal());
            
            int filesAfectades = ps.executeUpdate();
            return filesAfectades > 0;
            
        } catch (SQLException e) {
            System.out.println("Error inserint linia de factura:");
            e.printStackTrace();
        }
        return false;
    }

    // OBTENIR TIQUETS D'UN CLIENT
    public ArrayList<Tiquet> obtenirTiquetsByClient(String dniClient) {
        ArrayList<Tiquet> tiquets = new ArrayList<>();
        String sql = "SELECT * FROM tiquets WHERE dni_client = ?";
        
        try (Connection conn = ConnexioBD.connectar();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, dniClient);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    // Fem servir el Constructor 2 per no perdre els totals reals de la BD
                    Tiquet t = new Tiquet(
                        rs.getInt("id"),
                        rs.getString("data_compra"), // data_compra corregit
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

    // OBTENIR LÍNIES PER ARTICLE
    public ArrayList<LiniaTiquet> obtenirLiniesByArticle(int idArticle) {
        ArrayList<LiniaTiquet> linies = new ArrayList<>();
        // Taula corregida a 'linies_factura'
        String sql = "SELECT * FROM linies_factura WHERE id_article = ?";

        try (Connection conn = ConnexioBD.connectar();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, idArticle);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    // Mapeig corregit amb els camps reals de la taula linies_factura
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