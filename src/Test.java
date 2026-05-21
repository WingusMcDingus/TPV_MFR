import dao.TiquetDAO;
import database.ConnexioBD;
import model.Tiquet;
import model.LiniaTiquet;
import java.sql.Connection;
import java.sql.Statement;
import java.util.ArrayList;

public class Test {
    public static void main(String[] args) {
        TiquetDAO tiquetDAO = new TiquetDAO();

        System.out.println("=== PREPARANT ENTORN DE TEST ===");
        prepararDadesDeProva();

        System.out.println("\n=== 1. CREANT TIQUET EN MEMÒRIA ===");
        // Creem un tiquet nou pel client '000' (Client genèric que ja ve de l'script SQL)
        Tiquet nouTiquet = new Tiquet(0, "2026-05-21", "000");

        // Simulem que comprem 2 unitats de l'article 1 (Preu base: 10.00€, IVA 21% -> Final: 12.10€ per unitat)
        // Per a 2 unitats -> Base: 20.00€, Final: 24.20€
        LiniaTiquet linia1 = new LiniaTiquet(0, 1, 2, 20.00, 21, 24.20);
        
        // Simulem que comprem 1 unitat de l'article 2 (Preu base: 15.00€, IVA 4% -> Final: 15.60€)
        LiniaTiquet linia2 = new LiniaTiquet(0, 2, 1, 15.00, 4, 15.60);

        // Afegim les línies al tiquet perquè recalculi automàticament els totals a Java
        nouTiquet.afegirLinia(linia1);
        nouTiquet.afegirLinia(linia2);

        System.out.println("Totals calculats a Java:");
        System.out.println(" -> Total Base: " + nouTiquet.getTotalBase() + "€");
        System.out.println(" -> Total Final: " + nouTiquet.getTotalFinal() + "€");

        System.out.println("\n=== 2. GUARDANT TIQUET A LA BASE DE DADES ===");
        // Guardem primer la capçalera i obtenim l'ID definit per l'AUTO_INCREMENT de la BD
        int idGenerat = tiquetDAO.inserirTiquet(nouTiquet);
        
        if (idGenerat > 0) {
            System.out.println("✅ Capçalera del Tiquet guardada correctament! ID Assignat per MySQL: " + idGenerat);
            
            // Ara que sabem l'ID real, el col·loquem a les línies i les inserim
            LiniaTiquet l1Corregida = new LiniaTiquet(idGenerat, linia1.getIdArticle(), linia1.getQuantitat(), linia1.getPreuBase(), linia1.getIva(), linia1.getPreuFinal());
            LiniaTiquet l2Corregida = new LiniaTiquet(idGenerat, linia2.getIdArticle(), linia2.getQuantitat(), linia2.getPreuBase(), linia2.getIva(), linia2.getPreuFinal());

            boolean l1Guardada = tiquetDAO.inserirLinia(l1Corregida);
            boolean l2Guardada = tiquetDAO.inserirLinia(l2Corregida);

            if (l1Guardada && l2Guardada) {
                System.out.println("✅ Totes les línies de la factura s'han inserit perfectament.");
            } else {
                System.out.println("❌ Ha fallat la inserció d'alguna línia.");
            }
        } else {
            System.out.println("❌ Ha fallat la inserció de la capçalera del tiquet.");
        }

        System.out.println("\n=== 3. PROVANT SELECTS (RECUPERACIÓ DE DADES) ===");
        System.out.println("Buscant tiquets del client '000'...");
        ArrayList<Tiquet> tiquetsDelClient = tiquetDAO.obtenirTiquetsByClient("000");
        
        for (Tiquet t : tiquetsDelClient) {
            System.out.println("-> Tiquet Trobat ID [" + t.getId() + "] | Data: " + t.getData() + " | Import final: " + t.getTotalFinal() + "€");
        }

        System.out.println("\nBuscant línies de venda de l'article 1...");
        ArrayList<LiniaTiquet> liniesDeLArticle = tiquetDAO.obtenirLiniesByArticle(1);
        for (LiniaTiquet l : liniesDeLArticle) {
            System.out.println("-> Línia trobada associada al Tiquet ID [" + l.getIdTiquet() + "] | Quantitat venuda: " + l.getQuantitat() + " | Preu final línia: " + l.getPreuFinal() + "€");
        }
        System.out.println("\n=== FI DEL TEST ===");
    }

    // Mètode auxiliar per injectar articles de prova si la BD està buida, evitant errors d'integritat referencial
    // Mètode auxiliar corregit amb la nova estructura de la taula articles (id_familia)
    private static void prepararDadesDeProva() {
        String sqlArticle1 = "INSERT IGNORE INTO articles (id, nom, id_familia, preu_base, iva, stock, talla_coll, amplada_pit, talla_cintura, llargada_camal) " +
                         "VALUES (1, 'Camisa Oficial', 1, 10.00, 21, 50, 40, 12, NULL, NULL)";
                         
        String sqlArticle2 = "INSERT IGNORE INTO articles (id, nom, id_familia, preu_base, iva, stock, talla_coll, amplada_pit, talla_cintura, llargada_camal) " +
                         "VALUES (2, 'Pantaló Oficial', 2, 15.00, 4, 30, NULL, NULL, 32, 34)";
    
        try (Connection conn = ConnexioBD.connectar();
         Statement stmt = conn.createStatement()) {
        stmt.executeUpdate(sqlArticle1);
        stmt.executeUpdate(sqlArticle2);
        System.out.println("ℹ️ Articles de prova (ID 1 i ID 2) verificats/inserits correctament a la BD.");
        } catch (Exception e) {
        System.out.println("❌ Error preparant dades de prova: " + e.getMessage());
        }
    }
}