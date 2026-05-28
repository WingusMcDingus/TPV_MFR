import dao.ArticleDAO;
import dao.ClientDAO;
import model.Camisa;
import model.Client;
import model.Pantalo;
import model.Article;

import java.util.ArrayList;

public class Test {

    public static void executarTests() {
        System.out.println("\n=============================================");
        System.out.println("🧪 INICIANT BATERIA DE TESTS (SPRINT 2) 🧪");
        System.out.println("=============================================");

        testClients();
        testArticles();
        testTiquets();

        System.out.println("\n=============================================");
        System.out.println("🏁 FI DE LA BATERIA DE TESTS 🏁");
        System.out.println("=============================================");
    }

    private static void testClients() {
        System.out.println("\n🔹 [TEST] Capa de CLIENTS (Model + DAO + CRUD)...");
        ClientDAO clientDAO = new ClientDAO();
        
        // 1. Test de Model (Create)
        Client clientProva = new Client("99999999T", "Client de Prova", "test@botiga.com", 600111222);
        System.out.println("  -> [OK] Model Client instanciat correctament.");

        // 2. Test de DAO - Inserir (Create)
        boolean inserit = clientDAO.inserirClient(clientProva);
        if (inserit) {
            System.out.println("  ✅ [OK] Client inserit correctament a MySQL.");
        } else {
            System.out.println("  ❌ [ERROR] Fallada en inserir client a MySQL.");
            return;
        }

        // 3. Test de DAO - Obtenir (Read)
        ArrayList<Client> llista = clientDAO.obtenirClients();
        boolean trobat = false;
        for (Client c : llista) {
            if (c.getDni().equals("99999999T")) {
                trobat = true;
                break;
            }
        }
        if (trobat) {
            System.out.println("  ✅ [OK] Client de prova recuperat i llegit correctament de MySQL.");
        } else {
            System.out.println("  ❌ [ERROR] No s'ha trobat el client de prova al fer SELECT.");
        }

        // 4. Test de DAO - Actualitzar (Update)
        clientProva.setNom("Client Prova MODIFICAT");
        boolean actualitzat = clientDAO.actualitzarClient(clientProva);
        if (actualitzat) {
            System.out.println("  ✅ [OK] Client modificat correctament a MySQL.");
        } else {
            System.out.println("  ❌ [ERROR] Fallada en actualitzar el client.");
        }

        // 5. Test de DAO - Eliminar (Delete)
        boolean eliminat = clientDAO.eliminarClient("99999999T");
        if (eliminat) {
            System.out.println("  ✅ [OK] Client de prova eliminat (neteja de BD correcta).");
        } else {
            System.out.println("  ❌ [ERROR] No s'ha pogut esborrar el client de prova.");
        }
    }

    private static void testArticles() {
        System.out.println("\n🔹 [TEST] Capa d'ARTICLES (Herència Model + DAO + CRUD)...");
        ArticleDAO articleDAO = new ArticleDAO();

        // 1. Test de Models amb Herència (Camisa i Pantaló)
        Camisa camisaProva = new Camisa(998, "Camisa de Test", "camisa", 20.0, 21, 10, 42, 14);
        Pantalo pantaloProva = new Pantalo(999, "Pantalo de Test", "pantaló", 30.0, 4, 5, 34, 32);
        System.out.println("  -> [OK] Models de Camisa (ID 998) i Pantaló (ID 999) instanciats.");

        // 2. Test de DAO - Inserir Camisa
        if (articleDAO.inserirArticle(camisaProva)) {
            System.out.println("  ✅ [OK] Camisa de prova guardada a MySQL.");
        } else {
            System.out.println("  ❌ [ERROR] Fallada en guardar Camisa.");
        }

        // 3. Test de DAO - Inserir Pantaló
        if (articleDAO.inserirArticle(pantaloProva)) {
            System.out.println("  ✅ [OK] Pantaló de prova guardat a MySQL.");
        } else {
            System.out.println("  ❌ [ERROR] Fallada en guardar Pantaló.");
        }

        // 4. Test de DAO - Llegir i detectar instàncies (Polimorfisme)
        ArrayList<Article> llista = articleDAO.obtenirArticles();
        int comptadorTests = 0;
        for (Article a : llista) {
            if (a.getId() == 998 && a instanceof Camisa) {
                comptadorTests++;
            }
            if (a.getId() == 999 && a instanceof Pantalo) {
                comptadorTests++;
            }
        }
        if (comptadorTests == 2) {
            System.out.println("  ✅ [OK] Polimorfisme correcte: MySQL ha retornat els articles transformats en Camisa i Pantalo en memòria.");
        } else {
            System.out.println("  ❌ [ERROR] Fallada en recuperar o identificar els tipus d'articles.");
        }

        // 5. Test de DAO - Eliminar (Neteja)
        boolean delCamisa = articleDAO.eliminarArticle(998);
        boolean delPantalo = articleDAO.eliminarArticle(999);
        if (delCamisa && delPantalo) {
            System.out.println("  ✅ [OK] Articles de prova eliminats del magatzem correctament.");
        } else {
            System.out.println("  ❌ [ERROR] Fallada en la neteja d'articles de prova.");
        }
    }
    private static void testTiquets() {
        System.out.println("\n🔹 [TEST] Capa de TIQUETS (Capçalera + Línies + DAO)...");
        dao.TiquetDAO tiquetDAO = new dao.TiquetDAO();
        ClientDAO clientDAO = new ClientDAO();
        ArticleDAO articleDAO = new ArticleDAO();

        // 1. PREPARACIÓ: Creem un Client i un Article falsos perquè MySQL no es queixi (Foreign Keys)
        Client clientDummy = new Client("77777777V", "Client Compra", "compra@botiga.com", 611222333);
        clientDAO.inserirClient(clientDummy);
        
        Camisa articleDummy = new Camisa(777, "Camisa Tiquet", "camisa", 50.0, 21, 10, 40, 15);
        articleDAO.inserirArticle(articleDummy);
        System.out.println("  -> [OK] Dades prèvies (Client 77777777V i Article 777) injectades temporalment.");

        // 2. CREATE: Creem el Tiquet i l'inserim
        model.Tiquet nouTiquet = new model.Tiquet(0, "2026-05-28", "77777777V");
        model.LiniaTiquet linia = new model.LiniaTiquet(0, 777, 2, 100.0, 21, 121.0);
        nouTiquet.afegirLinia(linia);
        
        int idGenerat = tiquetDAO.inserirTiquet(nouTiquet);
        if (idGenerat > 0) {
            System.out.println("  ✅ [OK] Capçalera del tiquet guardada a MySQL (ID Autogenerat: " + idGenerat + ").");
            
            // Inserim la línia associant-la a l'ID del tiquet
            model.LiniaTiquet liniaFinal = new model.LiniaTiquet(idGenerat, linia.getIdArticle(), linia.getQuantitat(), linia.getPreuBase(), linia.getIva(), linia.getPreuFinal());
            if (tiquetDAO.inserirLinia(liniaFinal)) {
                System.out.println("  ✅ [OK] Línia del tiquet inserida i associada correctament.");
            } else {
                System.out.println("  ❌ [ERROR] Fallada en inserir la línia.");
            }
        } else {
            System.out.println("  ❌ [ERROR] Fallada en inserir la capçalera del tiquet.");
        }

        // 3. READ: Llegim el tiquet de la BD per comprovar que s'ha desat
        ArrayList<model.Tiquet> tiquets = tiquetDAO.obtenirTiquetsByClient("77777777V");
        if (!tiquets.isEmpty()) {
            System.out.println("  ✅ [OK] El tiquet de prova s'ha recuperat correctament fent SELECT.");
        } else {
            System.out.println("  ❌ [ERROR] Fallada en recuperar el tiquet.");
        }

        // 4. DELETE (Neteja): Esborrem les línies i el tiquet (amb SQL directe perquè el DAO no té mètode d'esborrar tiquets)
        try (java.sql.Connection conn = database.ConnexioBD.connectar();
             java.sql.Statement stmt = conn.createStatement()) {
            stmt.executeUpdate("DELETE FROM linies_factura WHERE id_tiquet = " + idGenerat);
            stmt.executeUpdate("DELETE FROM tiquets WHERE id = " + idGenerat);
            System.out.println("  ✅ [OK] Tiquet i línies esborrats correctament per mantenir la BD neta.");
        } catch (Exception e) {
            System.out.println("  ❌ [ERROR] No s'han pogut netejar les dades del tiquet: " + e.getMessage());
        }

        // Netejem finalment l'Article i el Client falsos
        articleDAO.eliminarArticle(777);
        clientDAO.eliminarClient("77777777V");
    }
}