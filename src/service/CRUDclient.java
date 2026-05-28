package service;

import dao.ClientDAO;
import model.Client;

import java.util.ArrayList;
import java.util.Scanner;

public class CRUDclient {
    private ClientDAO clientDAO;
    private Scanner scanner;

    public CRUDclient() {
        this.clientDAO = new ClientDAO();
        this.scanner = new Scanner(System.in);
    }

    public void mostrarMenu() {
        int opcio = 0;
        do {
            System.out.println("\n=== GESTIÓ DE CLIENTS (CRUD) ===");
            System.out.println("1. Llistar tots els clients");
            System.out.println("2. Afegir un nou client");
            System.out.println("3. Modificar un client existent");
            System.out.println("4. Eliminar un client");
            System.out.println("0. Tornar al menú principal");
            System.out.print("Tria una opció: ");
            
            try {
                opcio = scanner.nextInt();
                scanner.nextLine();

                switch (opcio) {
                    case 1:
                        llistarClients();
                        break;
                    case 2:
                        afegirClient();
                        break;
                    case 3:
                        modificarClient();
                        break;
                    case 4:
                        eliminarClient();
                        break;
                    case 5:
                        System.out.println("Tornant al menú principal...");
                        break;
                    default:
                        System.out.println("Opció no vàlida. Intenta-ho de nou.");
                }
            } catch (Exception e) {
                System.out.println("Error: Introdueix un número vàlid.");
                scanner.nextLine(); // Netejar el buffer
            }
        } while (opcio != 5);
    }

    private void llistarClients() {
        System.out.println("\n--- LLISTAT DE CLIENTS ---");
        ArrayList<Client> llista = clientDAO.obtenirClients();
        
        if (llista.isEmpty()) {
            System.out.println("No hi ha cap client registrat a la base de dades.");
        } else {
            for (Client c : llista) {
                System.out.println("DNI: " + c.getDni() + " | Nom: " + c.getNom() + 
                                   " | Email: " + c.getEmail() + " | Telèfon: " + c.getTelefon());
            }
        }
    }

    private void afegirClient() {
        System.out.println("\n--- AFEGIR NOU CLIENT ---");
        try {
            System.out.print("Introdueix el DNI del client: ");
            String dni = scanner.nextLine();

            System.out.print("Nom i cognoms: ");
            String nom = scanner.nextLine();

            System.out.print("Correu electrònic: ");
            String email = scanner.nextLine();

            System.out.print("Telèfon (només números): ");
            int telefon = scanner.nextInt();
            scanner.nextLine();

            Client nouClient = new Client(dni, nom, email, telefon);

            if (clientDAO.inserirClient(nouClient)) {
                System.out.println("Client afegit correctament a la base de dades!");
            } else {
                System.out.println("Error en desar el client. És possible que aquest DNI ja existeixi.");
            }

        } catch (Exception e) {
            System.out.println("Error en les dades introduïdes. Recorda que el telèfon han de ser números.");
            scanner.nextLine();
        }
    }

    private void modificarClient() {
        System.out.println("\n--- MODIFICAR CLIENT ---");
        try {
            System.out.print("Introdueix el DNI del client que vols modificar: ");
            String dni = scanner.nextLine();

            System.out.println("Introdueix les noves dades (es sobreescriuran les antigues):");
            
            System.out.print("Nou nom: ");
            String nom = scanner.nextLine();

            System.out.print("Nou correu electrònic: ");
            String email = scanner.nextLine();

            System.out.print("Nou telèfon: ");
            int telefon = scanner.nextInt();
            scanner.nextLine();

            Client clientModificat = new Client(dni, nom, email, telefon);

            if (clientDAO.actualitzarClient(clientModificat)) {
                System.out.println("Dades del client actualitzades correctament!");
            } else {
                System.out.println("No s'ha pogut actualitzar. Estàs segur que el DNI '" + dni + "' existeix?");
            }

        } catch (Exception e) {
            System.out.println("Error en les dades introduïdes.");
            scanner.nextLine();
        }
    }

    private void eliminarClient() {
        System.out.println("\n--- ELIMINAR CLIENT ---");
        System.out.print("Introdueix el DNI del client a eliminar: ");
        String dni = scanner.nextLine();
        
        if (dni.equals("000")) {
            System.out.println("Error: No es pot eliminar el Client Genèric (000), el sistema el necessita.");
            return;
        }

        if (clientDAO.eliminarClient(dni)) {
            System.out.println("Client eliminat de la base de dades.");
        } else {
            System.out.println("No s'ha pogut eliminar el client. Comprova que el DNI sigui correcte i que no tingui tiquets de compra associats.");
        }
    }
}