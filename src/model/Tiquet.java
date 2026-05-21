package model;
import java.util.ArrayList;

public class Tiquet {
    private int id;
    private String data;
    private String dni_client;
    private double total_base;
    private double total_iva;
    private double total_final;
    private ArrayList<LiniaTiquet> linies;

    public Tiquet(int id, String data, String dni_client) {
        this.id = id;
        this.data = data;
        this.dni_client = dni_client;
        this.total_base = 0;
        this.total_iva = 0;
        this.total_final = 0;
        this.linies = new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public String getData() {
        return data;
    }

    public String getDniClient() {
        return dni_client;
    }

    public double getTotalBase() {
        return total_base;
    }

    public double getTotalIva() {
        return total_iva;
    }

    public double getTotalFinal() {
        return total_final;
    }

    public ArrayList<LiniaTiquet> getLinies() {
        return linies;
    }
    
    public void afegirLinia(LiniaTiquet linia) {
        linies.add(linia);
        total_base += linia.getTotalBase();
        total_iva += linia.getTotalIva(); 
        total_final += linia.getTotalFinal(); 
    }
    
}