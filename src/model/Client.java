package model;

public class Client {
    private String dni;
    private String nom;
    private String email;
    private int telefon;

    public Client (String dni, String nom, String email, int telefon) {
        this.dni = dni;
        this.nom = nom;
        this.email = email;
        this.telefon = telefon;
    }

    public String getDni() {
        return dni;
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

    public String getEmail () {
        return email;
    }

    public void setEmail(String email) {
        if (email.isEmpty()) {
            System.out.println("Error, el email no pot quedar buit");

        } else {
            this.email = email;
        }
    }

    public int getTelefon() {
        return telefon;
    }

    public void setTelefon (int telefon) {
        if (telefon == 0) {
            System.out.println("Error, el telefon no pot quedar buit.");

        } else {
            this.telefon = telefon;
        }
    }

    public String toString() {
        return "DNI: " + dni + "\nNom: " + nom + "\nEmail: " + email + "\nTelefon: " + telefon;
    }
}