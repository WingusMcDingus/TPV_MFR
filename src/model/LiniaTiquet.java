package model;

public class LiniaTiquet {
    private int idTiquet;
    public String data;
    public String dniClient;
    public double total_base;
    public double total_iva;
    public double total_final;

    public LiniaTiquet(int idTiquet, String data, String dniClient, double total_base, double total_iva, double total_final) {
        this.idTiquet = idTiquet;
        this.data = data;
        this.dniClient = dniClient;
        this.total_base = total_base;
        this.total_iva = total_iva;
        this.total_final = total_final;
    }

    public int getIdTiquet() {
        return idTiquet;
    }

    public String getData() {
        return data;
    }

    public String getDniClient() {
        return dniClient;
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
}