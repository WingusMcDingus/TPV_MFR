package dao;

import database.ConnexioBD;
import model.Client;

import java.sql.*;
import java.util.ArrayList;

public class ClientDAO {
    public boolean inserirClient(Client client) {
        String sql = "INSERT INTO clients (dni, nom, email, telefon) VALUES (?, ?, ?, ?)";
        
        try (Connection conn = ConnexioBD.connectar();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, client.getDni());
            ps.setString(2, client.getNom());
            ps.setString(3, client.getEmail());
            ps.setString(4, String.valueOf(client.getTelefon())); 

            int filesAfectades = ps.executeUpdate();
            return filesAfectades > 0;

        } catch (SQLException e) {
            System.out.println("Error inserint client: " + e.getMessage());
        }
        return false;
    }

    public ArrayList<Client> obtenirClients() {
        ArrayList<Client> clients = new ArrayList<>();
        String sql = "SELECT * FROM clients";
        
        try (Connection conn = ConnexioBD.connectar();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Client c = new Client(
                    rs.getString("dni"),
                    rs.getString("nom"),
                    rs.getString("email"),
                    rs.getInt("telefon") 
                );
                clients.add(c);
            }
        } catch (SQLException e) {
            System.out.println("Error obtenint clients: " + e.getMessage());
        }
        return clients;
    }

    public boolean actualitzarClient(Client client) {
        String sql = "UPDATE clients SET nom = ?, email = ?, telefon = ? WHERE dni = ?";
        
        try (Connection conn = ConnexioBD.connectar();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, client.getNom());
            ps.setString(2, client.getEmail());
            ps.setString(3, String.valueOf(client.getTelefon()));
            ps.setString(4, client.getDni());

            int filesAfectades = ps.executeUpdate();
            return filesAfectades > 0;

        } catch (SQLException e) {
            System.out.println("Error actualitzant client: " + e.getMessage());
        }
        return false;
    }

    public boolean eliminarClient(String dni) {
        String sql = "DELETE FROM clients WHERE dni = ?";
        
        try (Connection conn = ConnexioBD.connectar();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, dni);
            int filesAfectades = ps.executeUpdate();
            return filesAfectades > 0;

        } catch (SQLException e) {
            System.out.println("Error eliminant client: " + e.getMessage());
        }
        return false;
    }
}