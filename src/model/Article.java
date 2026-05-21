package model;

public abstract class Article {
    protected int id;
    protected String nom;
    protected String familia; 
    protected double preu_base;
    protected int iva;
    protected int stock;

    public Article (int id, String nom, String familia, double preu_base, int iva, int stock) {
        this.id = id;
        setNom(nom);
        this.familia = familia;
        setPreu_base(preu_base);
        setIva(iva);
        setStock(stock);
    }

    public int getId() {
        return id;
    }

    public String getNom() {
        return nom;
    } 

    public void setNom(String nom) {
        if (nom.isEmpty()) {
            System.out.println("Error, el nom no pot quedar buit.");

        } else {
            this.nom = nom;
        }
    }

    public String getFamilia() {
        return familia;
    }
    
    public double getPreu_base() {
        return preu_base;
    } 

    public void setPreu_base(double preu_base) {
        if (preu_base < 0) {
            System.out.println("Error, el preu base no pot ser negatiu");

        } else {
            this.preu_base = preu_base;
        }
        
    }

    public int getIva() {
        return iva;
    }

    public void setIva(int iva) {
        if (iva < 4 || iva > 21) {
            System.out.println("Error, l'iva ha d'estar entre 4 i 21%");

        } else {
            this.iva = iva;
        }
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        if (stock < 0) {
            System.out.println("Error, l'Stock no pot ser negatiu");

        } else {
            this.stock = stock;
        }
    }

    public abstract double calcularPreuFinal();
    
    public String toString() {
        return "ID: " + id + "\nNom: " + nom + "\nFamilia: " + familia + "\nPreu base: " + preu_base + "IVA: " + iva + "\nStock: " + stock;
    }
}