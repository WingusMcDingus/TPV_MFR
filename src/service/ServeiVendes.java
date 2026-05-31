package service;

import dao.TiquetDAO;
import dao.ArticleDAO;
import model.Tiquet;
import model.LiniaTiquet;
import model.Article;
import database.ConnexioBD;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Scanner;

public class ServeiVendes {
    private TiquetDAO tiquetDAO;
    private ArticleDAO articleDAO;
    private Scanner scanner;

    public ServeiVendes() {
        this.tiquetDAO = new TiquetDAO();
        this.articleDAO = new ArticleDAO();
        this.scanner = new Scanner(System.in);
    }

    /**
     * 1. REALITZAR VENDA (Opció 4 del Main)
     */
    public void realitzarVenda() {
        System.out.println("\n--- 🛒 NOVA VENDA (TPV) ---");
        System.out.print("Introdueix el DNI del client (Buits -> Client Genèric 000): ");
        String dni = scanner.nextLine().trim();
        if (dni.isEmpty()) {
            dni = "000";
        }

        // Creem el tiquet provisionalment (MySQL autogenerarà l'ID real)
        String dataAvui = LocalDate.now().toString();
        Tiquet tiquet = new Tiquet(0, dataAvui, dni);
        
        boolean afegintArticles = true;

        while (afegintArticles) {
            System.out.print("\nID de l'article a vendre: ");
            int idArticle = scanner.nextInt();
            
            // Busquem l'article a la base de dades
            Article article = articleDAO.obtenirArticlePerId(idArticle);
            if (article == null) {
                System.out.println("Aquest article no existeix a la Base de Dades.");
                scanner.nextLine(); 
                continue;
            }

            System.out.print("Quantitat: ");
            int quantitat = scanner.nextInt();
            scanner.nextLine(); // Neteja buffer

            // Validem si hi ha stock suficient
            if (article.getStock() < quantitat) {
                System.out.println("Stock insuficient! Disponibles: " + article.getStock());
                continue;
            }

            // Càlculs de preus utilitzant els getters de l'Article
            double preuUnitari = article.getPreu_base();
            int ivaPercentatge = article.getIva();

            double totalBaseLinia = preuUnitari * quantitat;
            double totalIvaLinia = totalBaseLinia * (ivaPercentatge / 100.0);
            double totalFinalLinia = totalBaseLinia + totalIvaLinia;

            // Creem la línia de tiquet
            LiniaTiquet linia = new LiniaTiquet(0, idArticle, quantitat, totalBaseLinia, ivaPercentatge, totalFinalLinia);
            
            // Afegim la línia i actualitzem els totals del tiquet utilitzant els seus setters oficials
            tiquet.getLinies().add(linia);
            tiquet.setTotalBase(tiquet.getTotalBase() + totalBaseLinia);
            tiquet.setTotalIva(tiquet.getTotalIva() + totalIvaLinia);
            tiquet.setTotalFinal(tiquet.getTotalFinal() + totalFinalLinia);
            
            System.out.println("Afegit: " + quantitat + "x " + article.getNom());

            System.out.print("Vols afegir un altre article? (s/n): ");
            String resposta = scanner.nextLine().trim().toLowerCase();
            if (!resposta.equals("s")) {
                afegintArticles = false;
            }
        }

        // Si no s'ha processat cap article, cancel·lem
        if (tiquet.getLinies().isEmpty()) {
            System.out.println("Venda cancel·lada de forma buida.");
            return;
        }

        // Guardem a la base de dades
        System.out.println("\nProcessant transacció a la BD...");
        int idTiquetGenerat = tiquetDAO.inserirTiquet(tiquet);
        
        if (idTiquetGenerat > 0) {
            // Guardem les línies de la factura i restem l'stock de la botiga
            for (int i = 0; i < tiquet.getLinies().size(); i++) {
                LiniaTiquet l = tiquet.getLinies().get(i);
                l.setIdTiquet(idTiquetGenerat); 
                tiquetDAO.inserirLinia(l);

                // Actualització d'stock d'articles
                Article art = articleDAO.obtenirArticlePerId(l.getIdArticle());
                int nouStock = art.getStock() - l.getQuantitat();
                articleDAO.actualitzarStock(art.getId(), nouStock);
            }

            System.out.println("Tiquet desat correctament!");
            imprimirRebut(idTiquetGenerat, tiquet);
        } else {
            System.out.println("Error a la base de dades en generar la capçalera del tiquet.");
        }
    }

    // 2. CONSULTAR VENDES PER CLIENT (Opció 5 del Main)
    public void consultarVendesPerClient() {
        System.out.print("\nIntrodueix el DNI del client: ");
        String dni = scanner.nextLine().trim();
        ArrayList<Tiquet> tiquets = tiquetDAO.obtenirTiquetsByClient(dni);

        if (tiquets.isEmpty()) {
            System.out.println("No s'ha trobat cap tiquet associat a aquest DNI.");
        } else {
            System.out.println("\n--- HISTORIAL DE TIQUETS DEL CLIENT ---");
            for (int i = 0; i < tiquets.size(); i++) {
                Tiquet t = tiquets.get(i);
                System.out.printf("Tiquet ID: %-5d | Data: %-10s | Total Base: %-7.2f€ | Total Final: %.2f€\n", 
                    t.getId(), t.getData(), t.getTotalBase(), t.getTotalFinal());
            }
        }
    }

    // CONSULTAR VENDES PER ARTICLE (Opció 6 del Main)
    public void consultarVendesPerArticle() {
        System.out.print("\nIntrodueix l'ID de l'article a consultar: ");
        int id = scanner.nextInt();
        scanner.nextLine();
        
        ArrayList<LiniaTiquet> linies = tiquetDAO.obtenirLiniesByArticle(id);
        if (linies.isEmpty()) {
            System.out.println("Aquest article no s'ha venut en cap ocasió.");
        } else {
            System.out.println("\n--- HISTORIAL DE VENDES DE L'ARTICLE ---");
            for (int i = 0; i < linies.size(); i++) {
                LiniaTiquet l = linies.get(i);
                System.out.printf("Al Tiquet ID: %-5d | Unitats Venudes: %-4d | Total Línia: %.2f€\n", 
                    l.getIdTiquet(), l.getQuantitat(), l.getPreuFinal());
            }
        }
    }

    // CÀLCUL BENEFICIS TOTALS (Opció 7 del Main)

    public void calcularBeneficis() {
        System.out.println("\n--- BALANÇ ECONÒMIC DE BENEFICIS ---");
        String sql = "SELECT SUM(total_base) AS base, SUM(total_iva) AS iva, SUM(total_final) AS total FROM tiquets";
        
        try (Connection conn = ConnexioBD.connectar();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
             
            if (rs.next()) {
                double base = rs.getDouble("base");
                double iva = rs.getDouble("iva");
                double total = rs.getDouble("total");

                System.out.printf("💰 Facturació Total Net (Base): %.2f €\n", base);
                System.out.printf("🏛️ IVA total recaptat:          %.2f €\n", iva);
                System.out.println("-----------------------------------------");
                System.out.printf("🚀 BRUT ACUMULAT A CAIXA:       %.2f €\n", total);
            }
        } catch (SQLException e) {
            System.out.println("Error calculant beneficis de la taula tiquets:");
            e.printStackTrace();
        }
    }


    //  RECOMPRA AUTOMÀTICA (Opció 8 del Main)
    public void recompraAutomatica() {
        System.out.println("\n--- 🤖 CONTROL D'STOCK / RECOMPRA AUTOMÀTICA ---");
        System.out.println("Buscant articles amb stock crític (menys de 5 unitats)...");
        
        ArrayList<Article> llistaArticles = articleDAO.obtenirArticles(); 
        int comptadorRestocs = 0;

        for (int i = 0; i < llistaArticles.size(); i++) {
            Article art = llistaArticles.get(i);
            if (art.getStock() < 5) {
                int quantitatAComprar = 25 - art.getStock();
                int nouStock = 25;
                
                boolean exit = articleDAO.actualitzarStock(art.getId(), nouStock);
                if (exit) {
                    System.out.printf("[RESTOCK] S'han comprat %d unitats de '%s' (ID: %d). Nou stock: %d\n", 
                        quantitatAComprar, art.getNom(), art.getId(), nouStock);
                    comptadorRestocs++;
                }
            }
        }

        if (comptadorRestocs == 0) {
            System.out.println("Tots els articles disposen d'un stock saludable. No cal fer recompra.");
        } else {
            System.out.println("Recompra automàtica finalitzada. Premses reabastides amb èxit.");
        }
    }

    // Mètode privat auxiliar per pintar el tiquet elegantment per consola
    private void imprimirRebut(int id, Tiquet t) {
        System.out.println("\n=========================================");
        System.out.println("             REBUT DE COMPRA             ");
        System.out.println("=========================================");
        System.out.println("Factura Nº: " + id);
        System.out.println("Data: " + t.getData());
        System.out.println("Client DNI: " + t.getDniClient());
        System.out.println("-----------------------------------------");
        System.out.printf("%-10s %-8s %-10s %-10s\n", "ART. ID", "CANT.", "BASE", "TOTAL");
        System.out.println("-----------------------------------------");
        for (int i = 0; i < t.getLinies().size(); i++) {
            LiniaTiquet l = t.getLinies().get(i);
            System.out.printf("%-10d %-8d %-10.2f %-10.2f\n", 
                l.getIdArticle(), l.getQuantitat(), l.getPreuBase(), l.getPreuFinal());
        }
        System.out.println("-----------------------------------------");
        System.out.printf("TOTAL NET:   %.2f €\n", t.getTotalBase());
        System.out.printf("TOTAL IVA:   %.2f €\n", t.getTotalIva());
        System.out.printf("TOTAL FINAL: %.2f €\n", t.getTotalFinal());
        System.out.println("=========================================");
        System.out.println("       Gràcies per la seva compra!       \n");
    }
}