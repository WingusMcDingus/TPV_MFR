package json;

import dao.ArticleDAO;
import model.*;
import util.GestorJSON;
import java.util.ArrayList;
import java.util.Scanner;

public class ImportacioArticles {

    private GestorJSON gestorJSON;
    private ArticleDAO articleDAO;
    private Scanner scanner;

    public ImportacioArticles() {
        this.gestorJSON = new GestorJSON();
        this.articleDAO = new ArticleDAO();
        this.scanner = new Scanner(System.in);
    }

    public void executar(String rutaFitxer) {
        // 1. Llegir el JSON
        ArrayList<Article> articles = gestorJSON.llegirArticles(rutaFitxer);

        if (articles.isEmpty()) {
            System.out.println("No s'han pogut llegir articles del fitxer.");
            return;
        }

        // 2. Comptar i mostrar
        int numCamises = 0;
        int numPantalons = 0;
        for (Article a : articles) {
            if (a instanceof Camisa) numCamises++;
            else if (a instanceof Pantalo) numPantalons++;
        }

        System.out.println("\n=== IMPORTACIÓ D'ARTICLES ===");
        System.out.println("Articles trobats al fitxer:");
        System.out.println("  Camises:  " + numCamises);
        System.out.println("  Pantalons: " + numPantalons);
        System.out.println("  TOTAL:    " + articles.size());

        // 3. Preguntar confirmació
        System.out.print("\nVols carregar aquests articles a la base de dades? (s/n): ");
        String resposta = scanner.nextLine().trim().toLowerCase();

        if (!resposta.equals("s")) {
            System.out.println("Importació cancel·lada.");
            return;
        }

        // 4. Inserir o actualitzar
        int afegits = 0;
        int actualitzats = 0;
        ArrayList<Article> articlesExistents = articleDAO.obtenirArticles();

        for (Article article : articles) {
            boolean existeix = false;

            for (Article existent : articlesExistents) {
                if (existent.getId() == article.getId()) {
                    existeix = true;
                    break;
                }
            }

            if (existeix) {
                articleDAO.actualitzarArticle(article);
                actualitzats++;
            } else {
                articleDAO.inserirArticle(article);
                afegits++;
            }
        }

        // 5. Mostrar resultat
        System.out.println("\nImportació finalitzada:");
        System.out.println("  Articles afegits:      " + afegits);
        System.out.println("  Articles actualitzats: " + actualitzats);
    }
}