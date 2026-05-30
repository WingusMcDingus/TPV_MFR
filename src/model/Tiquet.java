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

    // Constructor 1: Per a nous tiquets (comença a 0)
    public Tiquet(int id, String data, String dni_client) {
        this.id = id;
        this.data = data;
        this.dni_client = dni_client;
        this.total_base = 0;
        this.total_iva = 0;
        this.total_final = 0;
        this.linies = new ArrayList<>();
    }

    // Constructor 2: Recomanat per quan llegim dades ja guardades de la BD
    public Tiquet(int id, String data, String dni_client, double total_base, double total_iva, double total_final) {
        this.id = id;
        this.data = data;
        this.dni_client = dni_client;
        this.total_base = total_base;
        this.total_iva = total_iva;
        this.total_final = total_final;
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
    public void setTotalBase(double totalBase) { 
        this.total_base = totalBase; 
    }
    public void setTotalIva(double totalIva) { 
        this.total_iva = totalIva; 
    }
    public void setTotalFinal(double totalFinal) { 
        this.total_final = totalFinal; 
    }
    public ArrayList<LiniaTiquet> getLinies() { 
        return linies; 
    }
    
    public void afegirLinia(LiniaTiquet linia) {
        linies.add(linia);
        this.total_base += linia.getPreuBase();
        this.total_iva += (linia.getPreuFinal() - linia.getPreuBase());
        this.total_final += linia.getPreuFinal(); 
    }
}