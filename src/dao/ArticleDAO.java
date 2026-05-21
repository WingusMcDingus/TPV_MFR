package dao;

import database.ConnexioBD;
import model.Article;
import model.Camisa;
import model.Pantalo;

import java.sql.*;
import java.util.ArrayList;

public class ArticleDAO {
    // INSERT
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
        
            // Lògica segons si és Camisa o Pantaló
            if (article instanceof Camisa) {
                Camisa c = (Camisa) article;
                ps.setInt(7, c.getTallaColl());
                ps.setInt(8, c.getAmpladaPit());
                ps.setNull(9, java.sql.Types.INTEGER); // No té cintura
                ps.setNull(10, java.sql.Types.INTEGER); // No té camal
            } else if (article instanceof Pantalo) {
                Pantalo p = (Pantalo) article;
                ps.setNull(7, java.sql.Types.INTEGER); // No té coll
                ps.setNull(8, java.sql.Types.INTEGER); // No té pit
                ps.setInt(9, p.getTallaCintura());
                ps.setInt(10, p.getLlargada());
            }
        
            int filesAfectades = ps.executeUpdate();
            return filesAfectades > 0;
        
        } catch (SQLException e) {
        System.out.println("Error inserint article");
        e.printStackTrace();
        }
        return false;
    }
    // SELECT ALL
    public ArrayList<Article> obtenirArticles() {
        ArrayList<Article> articles = new ArrayList<>();
        String sql = "SELECT * FROM articles";
        try (Connection conn = ConnexioBD.connectar();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                int id = rs.getInt("id");
                String nom = rs.getString("nom");
                String familia = rs.getString("familia");
                double preu_base = rs.getDouble("preu_base");
                int iva = rs.getInt("iva");
                int stock = rs.getInt("stock");

                Article article;
                if (familia.equalsIgnoreCase("camisa")) {
                    article = new Camisa(id, nom, familia, preu_base, iva, stock, 0, 0);
                } else if (familia.equalsIgnoreCase("pantalo")) {
                    article = new Pantalo(id, nom, familia, preu_base, iva, stock, 0, 0);
                } else {
                    continue; // Si la familia no es reconocida, se omite el artículo
                }
                articles.add(article);
            }
        } catch (SQLException e) {
            System.out.println("Error obtenint articles");
            e.printStackTrace();
        }
        return articles;
    }
    // UPDATE
    public boolean actualitzarArticle(Article article) {
        String sql = "UPDATE articles SET nom = ?, familia = ?, preu_base = ?, iva = ?, stock = ? WHERE id = ?";
        try (Connection conn = ConnexioBD.connectar();
            PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, article.getNom());
            ps.setString(2, article.getFamilia());
            ps.setDouble(3, article.getPreu_base());
            ps.setInt(4, article.getIva());
            ps.setInt(5, article.getStock());
            ps.setInt(6, article.getId());
            int filesAfectades = ps.executeUpdate();
            return filesAfectades > 0;

        } catch (SQLException e) {
            System.out.println("Error actualitzant article");
            e.printStackTrace();
        }
        return false;
    }

    // DELETE
    public boolean eliminarArticle(int id) {
        String sql = "DELETE FROM articles WHERE id = ?";
        try (Connection conn = ConnexioBD.connectar();
            PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            int filesAfectades = ps.executeUpdate();
            return filesAfectades > 0;

        } catch (SQLException e) {
            System.out.println("Error eliminant article");
            e.printStackTrace();
        }
        return false;
    }
}