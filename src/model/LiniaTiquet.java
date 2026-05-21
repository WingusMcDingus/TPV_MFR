package model;

public class LiniaTiquet {
    private int idTiquet;
    private int idArticle; 
    private int quantitat;  
    private double preuBase;
    private int iva;
    private double preuFinal;

    // Constructor corregit
    public LiniaTiquet(int idTiquet, int idArticle, int quantitat, double preuBase, int iva, double preuFinal) {
        this.idTiquet = idTiquet;
        this.idArticle = idArticle;
        this.quantitat = quantitat;
        this.preuBase = preuBase;
        this.iva = iva;
        this.preuFinal = preuFinal;
    }

    public int getIdTiquet() { 
        return idTiquet; 
    }
    public int getIdArticle() { 
        return idArticle; 
    }
    public int getQuantitat() { 
        return quantitat; 
    }
    public double getPreuBase() { 
        return preuBase; 
    }
    public int getIva() { 
        return iva; 
    }
    public double getPreuFinal() { 
        return preuFinal; 
    }
}