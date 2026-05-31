package json;

import model.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.io.FileInputStream;
import java.util.ArrayList;

public class GestorJSON {

    public ArrayList<Article> llegirArticles(String rutaFitxer) {
        ArrayList<Article> articles = new ArrayList<>();
        JSONParser parser = new JSONParser();

        try {
            JSONArray jsonArray = (JSONArray) parser.parse(new InputStreamReader(
                new FileInputStream(rutaFitxer), "UTF-8"));

            for (int i = 0; i < jsonArray.size(); i++) {
                JSONObject obj = (JSONObject) jsonArray.get(i);

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

    public void escriureRecompra(String rutaFitxer, ArrayList<Article> articles, ArrayList<Integer> quantitats) {
        JSONArray jsonArray = new JSONArray();

        for (int i = 0; i < articles.size(); i++) {
            JSONObject obj = new JSONObject();
            obj.put("id", articles.get(i).getId());
            obj.put("nom", articles.get(i).getNom());
            obj.put("quantitat", quantitats.get(i));
            jsonArray.add(obj);
        }

        try {
            FileWriter fw = new FileWriter(rutaFitxer);
            fw.write(jsonArray.toJSONString());
            fw.close();
            System.out.println("Fitxer de recompra generat correctament.");
        } catch (Exception e) {
            System.out.println("Error escrivint el fitxer JSON: " + e.getMessage());
        }
    }
}