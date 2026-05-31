package dao;

import database.ConnexioBD;
import model.Article;
import model.Camisa;
import model.Pantalo;

import java.sql.*;
import java.util.ArrayList;

public class ArticleDAO {
    public boolean inserirArticle(Article article) {
        String sql = "INSERT INTO articles (id, nom, id_familia, preu_base, iva, stock, talla_coll, amplada_pit, talla_cintura, llargada_camal) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
    
        try (Connection conn = ConnexioBD.connectar();
             PreparedStatement ps = conn.prepareStatement(sql)) {
        
            ps.setInt(1, article.getId());
            ps.setString(2, article.getNom());
        
            if (article.getFamilia().equalsIgnoreCase("camisa") || article instanceof Camisa) {
                ps.setInt(3, 1);
            } else {
                ps.setInt(3, 2);
            }
            
            ps.setDouble(4, article.getPreu_base());
            ps.setInt(5, article.getIva());
            ps.setInt(6, article.getStock());
        
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

    public ArrayList<Article> obtenirArticles() {
        ArrayList<Article> articles = new ArrayList<>();
        String sql = "SELECT * FROM articles";
        
        try (Connection conn = ConnexioBD.connectar();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
             
            while (rs.next()) {
                int id = rs.getInt("id");
                String nom = rs.getString("nom");
                int idFamilia = rs.getInt("id_familia");
                double preuBase = rs.getDouble("preu_base");
                int iva = rs.getInt("iva");
                int stock = rs.getInt("stock");
                
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

    public Article obtenirArticlePerId(int id) {
        String sql = "SELECT * FROM articles WHERE id = ?";
        try (Connection conn = ConnexioBD.connectar();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    int idFamilia = rs.getInt("id_familia");
                    if (idFamilia == 1) { // Camisa
                        return new Camisa(
                            rs.getInt("id"), rs.getString("nom"), "camisa", 
                            rs.getDouble("preu_base"), rs.getInt("iva"), 
                            rs.getInt("stock"), rs.getInt("talla_coll"), rs.getInt("amplada_pit")
                        );
                    } else if (idFamilia == 2) { // Pantaló
                        return new Pantalo(
                            rs.getInt("id"), rs.getString("nom"), "pantaló", 
                            rs.getDouble("preu_base"), rs.getInt("iva"), 
                            rs.getInt("stock"), rs.getInt("talla_cintura"), rs.getInt("llargada_camal")
                        );
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println("Error obtenint article per ID:");
            e.printStackTrace();
        }
        return null; // Retorna null si no troba l'article
    }

    public boolean actualitzarStock(int id, int nouStock) {
        String sql = "UPDATE articles SET stock = ? WHERE id = ?";
        try (Connection conn = ConnexioBD.connectar();
             PreparedStatement ps = conn.prepareStatement(sql)) {
             
            ps.setInt(1, nouStock);
            ps.setInt(2, id);
            return ps.executeUpdate() > 0;
            
        } catch (SQLException e) {
            System.out.println("Error actualitzant stock:");
            e.printStackTrace();
        }
        return false;
    }
    public boolean actualitzarArticle(Article article) {
        String sql = "UPDATE articles SET nom = ?, id_familia = ?, preu_base = ?, iva = ?, stock = ?, talla_coll = ?, amplada_pit = ?, talla_cintura = ?, llargada_camal = ? WHERE id = ?";
        
        try (Connection conn = ConnexioBD.connectar();
             PreparedStatement ps = conn.prepareStatement(sql)) {
             
            ps.setString(1, article.getNom());
        
            if (article.getFamilia().equalsIgnoreCase("camisa") || article instanceof Camisa) {
                ps.setInt(2, 1);
            } else {
                ps.setInt(2, 2);
            }
            
            ps.setDouble(3, article.getPreu_base());
            ps.setInt(4, article.getIva());
            ps.setInt(5, article.getStock());
        
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
            
            ps.setInt(10, article.getId());
            return ps.executeUpdate() > 0;
            
        } catch (SQLException e) {
            System.out.println("Error actualitzant article:");
            e.printStackTrace();
        }
        return false;
    }
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