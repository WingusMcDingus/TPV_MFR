public class Main {
    public static void main(String[] args) {
        
        // 🧪 Executem el test automàtic total de la capa backend
        Test.executarTests();
        
        // Després d'executar els tests, si tot surt verd, pots deixar 
        // el menú principal obert per provar-ho manualment:
        /*
        service.CRUDarticle crudArticle = new service.CRUDarticle();
        crudArticle.mostrarMenu();
        */
    }
}