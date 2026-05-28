package service;

import dao.ArticleDAO;
import model.Article;
import model.Camisa;
import model.Pantalo;

import java.util.ArrayList;
import java.util.Scanner;

public class CRUDarticle {
    private ArticleDAO articleDAO;
    private Scanner scanner;

    public CRUDarticle() {
        this.articleDAO = new ArticleDAO();
        this.scanner = new Scanner(System.in);
    }

    public void mostrarMenu() {
        int opcio = 0;
        do {
            System.out.println("\n=== GESTIÓ D'ARTICLES (CRUD) ===");
            System.out.println("1. Llistar tots els articles");
            System.out.println("2. Afegir un nou article");
            System.out.println("3. Modificar un article existent");
            System.out.println("4. Eliminar un article");
            System.out.println("5. Tornar al menú principal");
            System.out.print("Tria una opció: ");
            
            try {
                opcio = scanner.nextInt();
                scanner.nextLine();

                switch (opcio) {
                    case 1:
                        llistarArticles();
                        break;
                    case 2:
                        afegirArticle();
                        break;
                    case 3:
                        modificarArticle();
                        break;
                    case 4:
                        eliminarArticle();
                        break;
                    case 5:
                        System.out.println("Tornant al menú principal...");
                        break;
                    default:
                        System.out.println("Opció no vàlida. Intenta-ho de nou.");
                }
            } catch (Exception e) {
                System.out.println("Error: Introdueix un número vàlid.");
                scanner.nextLine();
            }
        } while (opcio != 5);
    }

    // READ (Llistar)
    private void llistarArticles() {
        System.out.println("\n--- LLISTAT D'ARTICLES ---");
        ArrayList<Article> llista = articleDAO.obtenirArticles();
        
        if (llista.isEmpty()) {
            System.out.println("El magatzem està buit.");
        } else {
            for (Article a : llista) {
                System.out.print("ID: " + a.getId() + " | Nom: " + a.getNom() + " | Preu: " + a.getPreu_base() + "€ | Stock: " + a.getStock());
                
                if (a instanceof Camisa) {
                    Camisa c = (Camisa) a;
                    System.out.println(" [CAMISA -> Coll: " + c.getTallaColl() + ", Pit: " + c.getAmpladaPit() + "]");
                } else if (a instanceof Pantalo) {
                    Pantalo p = (Pantalo) a;
                    System.out.println(" [PANTALÓ -> Cintura: " + p.getTallaCintura() + ", Llargada: " + p.getLlargada() + "]");
                }
            }
        }
    }

    // CREATE (Afegir)
    private void afegirArticle() {
        System.out.println("\n--- AFEGIR NOU ARTICLE ---");
        try {
            System.out.print("Introdueix l'ID de l'article: ");
            int id = scanner.nextInt();
            scanner.nextLine();

            System.out.print("Nom de l'article: ");
            String nom = scanner.nextLine();

            System.out.print("Quin tipus és? (1 per Camisa / 2 per Pantaló): ");
            int tipus = scanner.nextInt();

            System.out.print("Preu base (sense IVA): ");
            double preu = scanner.nextDouble();

            System.out.print("IVA (ex: 21): ");
            int iva = scanner.nextInt();

            System.out.print("Stock inicial: ");
            int stock = scanner.nextInt();

            Article nouArticle = null;

            if (tipus == 1) { // És Camisa
                System.out.print("Talla del coll (36-52): ");
                int coll = scanner.nextInt();
                System.out.print("Amplada del pit (10-15): ");
                int pit = scanner.nextInt();
                
                nouArticle = new Camisa(id, nom, "camisa", preu, iva, stock, coll, pit);
                
            } else if (tipus == 2) { // És Pantaló
                System.out.print("Talla de cintura (24-56): ");
                int cintura = scanner.nextInt();
                System.out.print("Llargada del camal (32-46): ");
                int llargada = scanner.nextInt();
                
                nouArticle = new Pantalo(id, nom, "pantaló", preu, iva, stock, cintura, llargada);
            } else {
                System.out.println("Tipus d'article desconegut. Operació cancel·lada.");
                return;
            }

            if (articleDAO.inserirArticle(nouArticle)) {
                System.out.println("Article afegit correctament a la base de dades!");
            } else {
                System.out.println("Error en desar l'article. Comprova que l'ID no estigui repetit.");
            }

        } catch (Exception e) {
            System.out.println("Error en les dades introduïdes. Operació cancel·lada.");
            scanner.nextLine();
        }
    }

    // UPDATE (Modificar)
    private void modificarArticle() {
        System.out.println("\n--- MODIFICAR ARTICLE ---");
        System.out.println("! Nota: Introdueix totes les dades de nou per a l'ID seleccionat.");
        afegirArticle(); 
    }

    private void modificarArticleComplet() {
        System.out.println("\n--- MODIFICAR ARTICLE ---");
        try {
            System.out.print("Introdueix l'ID de l'article a modificar: ");
            int id = scanner.nextInt();
            scanner.nextLine();

            System.out.print("Nou nom: ");
            String nom = scanner.nextLine();

            System.out.print("Quin tipus és? (1 per Camisa / 2 per Pantaló): ");
            int tipus = scanner.nextInt();

            System.out.print("Nou preu base: ");
            double preu = scanner.nextDouble();

            System.out.print("Nou IVA: ");
            int iva = scanner.nextInt();

            System.out.print("Nou Stock: ");
            int stock = scanner.nextInt();

            Article articleModificat = null;

            if (tipus == 1) {
                System.out.print("Nova talla del coll: ");
                int coll = scanner.nextInt();
                System.out.print("Nova amplada del pit: ");
                int pit = scanner.nextInt();
                articleModificat = new Camisa(id, nom, "camisa", preu, iva, stock, coll, pit);
            } else if (tipus == 2) {
                System.out.print("Nova talla de cintura: ");
                int cintura = scanner.nextInt();
                System.out.print("Nova llargada: ");
                int llargada = scanner.nextInt();
                articleModificat = new Pantalo(id, nom, "pantaló", preu, iva, stock, cintura, llargada);
            }

            if (articleModificat != null && articleDAO.actualitzarArticle(articleModificat)) {
                System.out.println("Article modificat correctament!");
            } else {
                System.out.println("No s'ha pogut modificar (l'ID existeix?).");
            }

        } catch (Exception e) {
            System.out.println("Error en les dades introduïdes.");
            scanner.nextLine();
        }
    }

    // DELETE (Eliminar)
    private void eliminarArticle() {
        System.out.println("\n--- ELIMINAR ARTICLE ---");
        System.out.print("Introdueix l'ID de l'article a eliminar: ");
        try {
            int id = scanner.nextInt();
            
            if (articleDAO.eliminarArticle(id)) {
                System.out.println("Article eliminat de la base de dades.");
            } else {
                System.out.println("No s'ha pogut eliminar l'article. Comprova que no pertanyi a cap tiquet.");
            }
        } catch (Exception e) {
            System.out.println("Si us plau, introdueix un número d'ID vàlid.");
            scanner.nextLine();
        }
    }
}