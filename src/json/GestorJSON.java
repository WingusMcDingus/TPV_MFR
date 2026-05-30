package json;

import model.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import java.io.FileReader;
import java.util.ArrayList;
//no puedo solucionar esta mierda al estar conectado remoto y no ser el host, tendras que mirar como 
//hacer que detecte el puto json simple o ponerlo tu srry :)
public class GestorJSON {

    public ArrayList<Article> llegirArticles(String rutaFitxer) {
        ArrayList<Article> articles = new ArrayList<>();
        JSONParser parser = new JSONParser();

        try {
            JSONArray jsonArray = (JSONArray) parser.parse(new FileReader(rutaFitxer));

            for (Object o : jsonArray) {
                JSONObject obj = (JSONObject) o;

                int id = ((Number) obj.get("id")).intValue();
                String nom = (String) obj.get("nom");
                String familia = (String) obj.get("familia");
                double preuBase = ((Number) obj.get("preu_base")).doubleValue();
                int iva = ((Number) obj.get("iva")).intValue();
                int stock = ((Number) obj.get("stock")).intValue();

                if (familia.equalsIgnoreCase("camisa")) {
                    int tallaColl = ((Number) obj.get("talla_coll")).intValue();
                    int ampladaPit = ((Number) obj.get("amplada_pit")).intValue();
                    articles.add(new Camisa(id, nom, familia, preuBase, iva, stock, tallaColl, ampladaPit));

                } else if (familia.equalsIgnoreCase("pantaló")) {
                    int tallaCintura = ((Number) obj.get("talla_cintura")).intValue();
                    int llargada = ((Number) obj.get("llargada_camal")).intValue();
                    articles.add(new Pantalo(id, nom, familia, preuBase, iva, stock, tallaCintura, llargada));
                }
            }

        } catch (Exception e) {
            System.out.println("Error llegint el fitxer JSON: " + e.getMessage());
        }

        return articles;
    }
}