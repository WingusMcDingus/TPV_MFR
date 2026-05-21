package dao;

import database.ConnexioBD;
import model.Article;
import model.Camisa;
import model.Pantalo;

import java.sql.*;
import java.util.ArrayList;

public class ArticleDAO {

    // ==========================================
    // INSERT
    // ==========================================
    public boolean inserirArticle(Article article) {
        // Hem afegit l'id, id_familia i els 4 camps de talles
        String sql = "INSERT INTO articles (id, nom, id_familia, preu_base, iva, stock, talla_coll, amplada_pit, talla_cintura, llargada_camal) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
    
        try (Connection conn = ConnexioBD.connectar();
             PreparedStatement ps = conn.prepareStatement(sql)) {
        
            ps.setInt(1, article.getId());
            ps.setString(2, article.getNom());
        
            // Traduïm la familia a id_familia (1: Camisa, 2: Pantaló)
            if (article.getFamilia().equalsIgnoreCase("camisa") || article instanceof Camisa) {
                ps.setInt(3, 1);
            } else {
                ps.setInt(3, 2);
            }
            
            ps.setDouble(4, article.getPreu_base());
            ps.setInt(5, article.getIva());
            ps.setInt(6, article.getStock());
        
            // Lògica segons si és Camisa o Pantaló per establir les talles o valors NULL
            if (article instanceof Camisa) {
                Camisa c = (Camisa) article;
                ps.setInt(7, c.getTallaColl());
                ps.setInt(8, c.getAmpladaPit());
                ps.setNull(9, java.sql.Types.INTEGER);
                ps.setNull(10, java.sql.Types.INTEGER);
            } else if (article instanceof Pantalo) {
                Pantalo p = (Pantalo) article;
                ps.setNull(7, java.sql.Types.INTEGER);
                ps.setNull(8, java.sql.Types.INTEGER);
                ps.setInt(9, p.getTallaCintura());
                ps.setInt(10, p.getLlargada());
            }
        
            int filesAfectades = ps.executeUpdate();
            return filesAfectades > 0;
            
        } catch (SQLException e) {
            System.out.println("Error inserint article:");
            e.printStackTrace();
        }
        return false;
    }

    // ==========================================
    // SELECT ALL
    // ==========================================
    public ArrayList<Article> obtenirArticles() {
        ArrayList<Article> articles = new ArrayList<>();
        String sql = "SELECT * FROM articles";
        
        try (Connection conn = ConnexioBD.connectar();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
             
            while (rs.next()) {
                int id = rs.getInt("id");
                String nom = rs.getString("nom");
                int idFamilia = rs.getInt("id_familia"); // Llegim la FK en lloc de l'ENUM
                double preuBase = rs.getDouble("preu_base");
                int iva = rs.getInt("iva");
                int stock = rs.getInt("stock");
                
                // Instanciem Camisa o Pantaló segons el número de la família (1 o 2)
                if (idFamilia == 1) { // És una camisa
                    int tallaColl = rs.getInt("talla_coll");
                    int ampladaPit = rs.getInt("amplada_pit");
                    
                    Camisa c = new Camisa(id, nom, "camisa", preuBase, iva, stock, tallaColl, ampladaPit);
                    articles.add(c);
                    
                } else if (idFamilia == 2) { // És un pantaló
                    int tallaCintura = rs.getInt("talla_cintura");
                    int llargadaCamal = rs.getInt("llargada_camal");
                    
                    Pantalo p = new Pantalo(id, nom, "pantaló", preuBase, iva, stock, tallaCintura, llargadaCamal);
                    articles.add(p);
                }
            }
        } catch (SQLException e) {
            System.out.println("Error obtenint articles:");
            e.printStackTrace();
        }
        return articles;
    }

    // ==========================================
    // UPDATE
    // ==========================================
    public boolean actualitzarArticle(Article article) {
        // Incloem l'id_familia i les columnes de les talles a la modificació
        String sql = "UPDATE articles SET nom = ?, id_familia = ?, preu_base = ?, iva = ?, stock = ?, talla_coll = ?, amplada_pit = ?, talla_cintura = ?, llargada_camal = ? WHERE id = ?";
        
        try (Connection conn = ConnexioBD.connectar();
             PreparedStatement ps = conn.prepareStatement(sql)) {
             
            ps.setString(1, article.getNom());
            
            // Traduïm la família al número id_familia
            if (article instanceof Camisa) {
                ps.setInt(2, 1);
            } else {
                ps.setInt(2, 2);
            }
            
            ps.setDouble(3, article.getPreu_base());
            ps.setInt(4, article.getIva());
            ps.setInt(5, article.getStock());
            
            // Comprovem quin tipus d'article és per actualitzar les talles pertinents
            if (article instanceof Camisa) {
                Camisa c = (Camisa) article;
                ps.setInt(6, c.getTallaColl());
                ps.setInt(7, c.getAmpladaPit());
                ps.setNull(8, java.sql.Types.INTEGER);
                ps.setNull(9, java.sql.Types.INTEGER);
            } else if (article instanceof Pantalo) {
                Pantalo p = (Pantalo) article;
                ps.setNull(6, java.sql.Types.INTEGER);
                ps.setNull(7, java.sql.Types.INTEGER);
                ps.setInt(8, p.getTallaCintura());
                ps.setInt(9, p.getLlargada()); 
            }
            
            // Condició del WHERE (id = ?)
            ps.setInt(10, article.getId());
            
            int filesAfectades = ps.executeUpdate();
            return filesAfectades > 0;

        } catch (SQLException e) {
            System.out.println("Error actualitzant article:");
            e.printStackTrace();
        }
        return false;
    }

    // ==========================================
    // DELETE
    // ==========================================
    public boolean eliminarArticle(int id) {
        String sql = "DELETE FROM articles WHERE id = ?";
        
        try (Connection conn = ConnexioBD.connectar();
             PreparedStatement ps = conn.prepareStatement(sql)) {
             
            ps.setInt(1, id);
            int filesAfectades = ps.executeUpdate();
            return filesAfectades > 0;

        } catch (SQLException e) {
            System.out.println("Error eliminant article:");
            e.printStackTrace();
        }
        return false;
    }
}