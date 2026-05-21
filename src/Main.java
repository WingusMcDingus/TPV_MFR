import database.ConnexioBD;
import java.sql.Connection;
import java.sql.SQLException;

public class Main {
    public static void main(String[] args) {
        System.out.println("--- DIAGNÒSTIC DE LA CONNEXIÓ ---");
        
        try (Connection conn = ConnexioBD.connectar()) {
            if (conn != null && !conn.isClosed()) {
                System.out.println("✅ ÈXIT: El programa s'ha connectat correctament a MySQL.");
                
                // 1. Mostra la URL completa que està fent servir Java
                System.out.println("🔗 URL de connexió: " + conn.getMetaData().getURL());
                
                // 2. Mostra el catàleg (la Base de Dades) activa
                System.out.println("🗄️ Base de dades activa actualment: " + conn.getCatalog());
                
                // 3. Mostra l'usuari amb el qual t'has connectat
                System.out.println("👤 Usuari de la BD: " + conn.getMetaData().getUserName());
                
            } else {
                System.out.println("❌ ERROR: La connexió ha retornat un objecte buit (null) o tancat.");
            }
        } catch (SQLException e) {
            System.out.println("❌ ERROR CRÍTIC: Ha fallat la comunicació amb MySQL.");
            System.out.println("Motiu de l'error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}