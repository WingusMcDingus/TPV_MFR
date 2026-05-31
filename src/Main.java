import java.util.Scanner;
import java.util.InputMismatchException;
import json.ImportacioArticles;
import service.CRUDarticle;
import service.CRUDclient;
import service.ServeiVendes;

public class Main {
    Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        Main p = new Main();
        p.principal();
    }

    public void principal() {
        int opcioMenu = 0;

        ImportacioArticles importacio = new ImportacioArticles();
        CRUDarticle crudArticle = new CRUDarticle();
        CRUDclient crudClient = new CRUDclient();
        ServeiVendes serveiVendes = new ServeiVendes();

        do {
            missatgeInicial();
            opcioMenu = llegirEnter(1, 9);

            switch (opcioMenu) {
                case 1: 
                importacio.executar("src/json/PE11_articles.json"); 
                break;
                case 2: 
                crudArticle.mostrarMenu(); 
                break;
                case 3: 
                crudClient.mostrarMenu(); 
                break;
                case 4: 
                serveiVendes.realitzarVenda(); 
                break;
                case 5: 
                serveiVendes.consultarVendesPerClient(); 
                break;
                case 6: 
                serveiVendes.consultarVendesPerArticle(); 
                break;
                case 7: 
                serveiVendes.calcularBeneficis(); 
                break;
                case 8: 
                serveiVendes.recompraAutomatica(); 
                break;
                case 9: 
                System.out.println("Fins aviat!"); 
                break;
            }

        } while (opcioMenu != 9);

        sc.close();
    }

    public void missatgeInicial() {
        System.out.println("\n=== TPV - BOTIGA DE ROBA ===");
        System.out.println("Si us plau, escull una opció:");
        System.out.println("1. Importació d'articles");
        System.out.println("2. Gestió d'articles");
        System.out.println("3. Gestió de clients");
        System.out.println("4. TPV");
        System.out.println("5. Consultes vendes per client");
        System.out.println("6. Consultes vendes per article");
        System.out.println("7. Càlcul beneficis totals");
        System.out.println("8. Recompra automàtica articles");
        System.out.println("9. Sortir");
        System.out.print("Tria una opció: ");
    }

    public int llegirEnter(int rangMin, int rangMax) {
        int enter = 0;

        do {
            try {
                enter = sc.nextInt();
                sc.nextLine();

                if (enter < rangMin || enter > rangMax) {
                    System.out.println("Opció no vàlida. Introdueix un número entre " + rangMin + " i " + rangMax + ".");
                }

            } catch (InputMismatchException e) {
                System.out.println("Error: introdueix un número vàlid.");
                sc.nextLine();
            }

        } while (enter < rangMin || enter > rangMax);

        return enter;
    }
}