package service;

import dao.ArticleDAO;
import dao.TiquetDAO;
import model.Article;
import model.Tiquet;
import model.LiniaTiquet;

import java.util.ArrayList;
import java.util.Scanner;
import java.time.LocalDate;

public class ServeiVendes {
    private TiquetDAO tiquetDAO;
    private ArticleDAO articleDAO;
    private Scanner scanner;

    public ServeiVendes() {
        this.tiquetDAO = new TiquetDAO();
        this.articleDAO = new ArticleDAO();
        this.scanner = new Scanner(System.in);
    }

    public void mostrarMenu() {
        int opcio = 0;
        do {
            System.out.println("\n=== GESTIÓ DE VENDES ===");
            System.out.println("1. Realitzar una venda");
            System.out.println("2. Consultar vendes per client");
            System.out.println("3. Consultar vendes per article");
            System.out.println("4. Tornar al menú principal");
            System.out.print("Tria una opció: ");
            
            try {
                opcio = scanner.nextInt();
                scanner.nextLine();

                switch (opcio) {
                    case 1:
                        realitzarVenda();
                        break;
                    case 2:
                        consultarVendesPerClient();
                        break;
                    case 3:
                         consultarVendesPerArticle();
                        break;
                    case 4:
                        System.out.println("Tornant al menú principal...");
                        break;
                    default:
                        System.out.println("Opció no vàlida. Intenta-ho de nou.");
                }
            } catch (Exception e) {
                System.out.println("Error: Introdueix un número vàlid.");
                scanner.nextLine();
            }
        } while (opcio != 4);
    }
    private void realitzarVenda() {
        System.out.println("\n--- REALITZANT VENDA ---");
        System.out.print("Introdueix el DNI del client: ");
        String dniClient = scanner.nextLine();
        String dataAvui = LocalDate.now().toString();
        Tiquet tiquet = new Tiquet(0, dataAvui, dniClient);
        ArrayList<LiniaTiquet> liniesVenda = new ArrayList<>();
        boolean afegintArticles = true;

        while (afegintArticles) {
            System.out.print("Introdueix l'ID de l'article (0 per acabar): ");
            int idArticle = scanner.nextInt();
            scanner.nextLine();

            // busquem l'article al seu DAO per veure si existeix i obtenir el preu
            ArrayList<Article> articles = articleDAO.obtenirArticles();
            Article article = null;
            if (articles != null) {
                for (Article a : articles) {
                    if (a.getId() == idArticle) {
                        article = a;
                        break;
                    }
                }
            }
            if (article == null) {
                System.out.println("Article no trobat. Intenta-ho de nou.");
                continue;
            }
            
            System.out.print("Introdueix la quantitat: ");
            int quantitat = scanner.nextInt();
            scanner.nextLine();
            if (article.getStock() < quantitat) {
                System.out.println("No hi ha prou stock. Stock actual: " + article.getStock());
                continue;
            }
            // calcular totals de la linia
            double preuBase = article.getPreu_base();
            int ivaPercentatge = 21;
            if (article.getFamilia().equalsIgnoreCase("camisa")) {
                ivaPercentatge = 21;
            }
            if (article.getFamilia().equalsIgnoreCase("pantaló")) {
                ivaPercentatge = 4;
            }

            double totalBaseLinia = preuBase * quantitat;
            double totalIvaLinia = totalBaseLinia * ivaPercentatge / 100;
            double totalFinalLinia = totalBaseLinia + totalIvaLinia;

            LiniaTiquet linia = new LiniaTiquet(0, idArticle, quantitat, preuBase, ivaPercentatge, totalFinalLinia);
            liniesVenda.add(linia);
            System.out.print("Article afegit a la venda. ");
            System.out.print("Vols afegir més articles? (s/n): ");
            String resposta = scanner.nextLine().toLowerCase();
            if (!resposta.equals("s")) {
                afegintArticles = false;
            }
        }
        
        if (liniesVenda.isEmpty()) {
            System.out.println("No s'ha afegit cap article a la venda. Operació cancel·lada.");
            return;
        }

        double totalBaseTiquet = 0;
        double totalIvaTiquet = 0;
        double totalFinalTiquet = 0;

        for(int i = 0; i < liniesVenda.size(); i++) {
            LiniaTiquet li = liniesVenda.get(i);
            totalBaseTiquet += li.getPreuBase() * li.getQuantitat();
            totalIvaTiquet += (li.getPreuBase() * li.getQuantitat()) * li.getIva() / 100;
            totalFinalTiquet += li.getPreuFinal();
        }
        
        tiquet.setTotalBase(totalBaseTiquet);
        tiquet.setTotalIva(totalIvaTiquet);
        tiquet.setTotalFinal(totalFinalTiquet);

        int idTiquetGenerat = tiquetDAO.inserirTiquet(tiquet);
        if (idTiquetGenerat > 0) {
            // Si la capçalera s'ha guardat, guardem les línies una a una
            for(int i = 0; i < liniesVenda.size(); i++) {
                LiniaTiquet li = liniesVenda.get(i);
                li.setIdTiquet(idTiquetGenerat);

                ArrayList<Article> articles = articleDAO.obtenirArticles();
            }

        }
    }
    private void consultarVendesPerClient() {

    }
    private void consultarVendesPerArticle() {

    }
}

