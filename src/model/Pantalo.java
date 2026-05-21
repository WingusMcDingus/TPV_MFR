package model;

public class Pantalo extends Article {
    private int tallaCintura;
    private int llargada;
    public Pantalo(int id, String nom, String familia, double preu_base, int iva, int stock, int tallaCintura, int llargada) {
        super(id, nom, familia, preu_base, iva, stock);

        this.tallaCintura = tallaCintura;
        this.llargada = llargada;
    }
    public int getTallaCintura() {
        return tallaCintura;
    }
    public void setTallaCintura(int tallaCintura) {
        if (tallaCintura >= 24 && tallaCintura <= 56) {
            this.tallaCintura = tallaCintura;
        } else {
            System.out.println("Error: la talla de cintura ha de ser entre 24 y 56.");
        }
    }
    public int getLlargada() {
        return llargada;
    }
    public void setLlargada(int llargada) {
        if (llargada >= 32 && llargada <= 46) {
            this.llargada = llargada;
        } else {
            System.out.println("Error: la llargada ha de ser entre 32 y 46.");
        }
    }
    // sobreescriure metode 
    @Override
    public double calcularPreuFinal() {
        return preu_base * 0.30 + llargada * 0.2;
    }
    // toString
    @Override
    public String toString() {
        return "Pantalo{" + ", tallaCintura=" + tallaCintura + ", llargada=" + llargada + '}';
    }
}
