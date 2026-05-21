package model;

public class Camisa extends Article {
    private int tallaColl;
    private int ampladaPit;
    public Camisa(int id, String nom, String familia, double preu_base, int iva, int stock, int tallaColl, int ampladaPit) {
        super(id, nom, familia, preu_base, iva, stock);

        this.tallaColl = tallaColl;
        this.ampladaPit = ampladaPit;
    }
    public int getTallaColl() {
        return tallaColl;
    }
    public void setTallaColl(int tallaColl) {
        if (tallaColl >= 36 && tallaColl <= 52) {
            this.tallaColl = tallaColl;
        } else {
            System.out.println("Error, la talla de coll ha de ser entre 36 y 52.");
        }
    }
    public int getAmpladaPit() {
        return ampladaPit;
    }
    public void setAmpladaPit(int ampladaPit) {
        if (ampladaPit >= 10 && ampladaPit <= 15) {
            this.ampladaPit = ampladaPit;
        } else {
            System.out.println("Error, la amplada de pit ha de ser entre 10 y 15.");
        }
    }

    // sobreescriure metode
    @Override
    public double calcularPreuFinal() {
        return preu_base * 0.35 + tallaColl * 0.3;
    }
    // toString
    @Override
    public String toString() {
        return "Camisa{" + ", tallaColl=" + tallaColl + ", ampladaPit=" + ampladaPit + '}';
    }


}
