package service;

import db.DBConnection;
import model.Account;
import model.Transaction;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LedgerService {
    public void runDemo() {
        try (Connection conn = DBConnection.getConnection()) {
            setupDatabase(conn);
            createAccounts(conn);
            transfer(conn, 1, 2, 100.0); // Bug: No transaction/rollback
            transfer(conn, 2, 1, 50.0);  // Bug: Dirty read possible
            printAccounts(conn);
            printTransactions(conn);
        } catch (SQLException e) {
            // Bug: Swallowed exception
        }
        // Bug: Connection leak if exception occurs before closing
    }

    private void setupDatabase(Connection conn) throws SQLException {
        Statement stmt = conn.createStatement();
        stmt.execute("CREATE TABLE accounts (id INT PRIMARY KEY, name VARCHAR(100), balance DOUBLE)");
        stmt.execute("CREATE TABLE transactions (id INT AUTO_INCREMENT PRIMARY KEY, from_id INT, to_id INT, amount DOUBLE, timestamp VARCHAR(100))");
        stmt.close();
    }

    private void createAccounts(Connection conn) throws SQLException {
        PreparedStatement ps = conn.prepareStatement("INSERT INTO accounts (id, name, balance) VALUES (?, ?, ?)");
        ps.setInt(1, 1);
        ps.setString(2, "Alice");
        ps.setDouble(3, 500.0);
        ps.executeUpdate();
        ps.setInt(1, 2);
        ps.setString(2, "Bob");
        ps.setDouble(3, 300.0);
        ps.executeUpdate();
        ps.close();
    }

    private void transfer(Connection conn, int fromId, int toId, double amount) throws SQLException {
        // Bug: No transaction management
        Statement stmt = conn.createStatement();
        stmt.executeUpdate("UPDATE accounts SET balance = balance - " + amount + " WHERE id = " + fromId);
        stmt.executeUpdate("UPDATE accounts SET balance = balance + " + amount + " WHERE id = " + toId);
        PreparedStatement ps = conn.prepareStatement("INSERT INTO transactions (from_id, to_id, amount, timestamp) VALUES (?, ?, ?, CURRENT_TIMESTAMP())");
        ps.setInt(1, fromId);
        ps.setInt(2, toId);
        ps.setDouble(3, amount);
        ps.executeUpdate();
        ps.close();
        stmt.close();
    }

    private void printAccounts(Connection conn) throws SQLException {
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM accounts");
        System.out.println("Accounts:");
        while (rs.next()) {
            System.out.println(rs.getInt("id") + ": " + rs.getString("name") + " - " + rs.getDouble("balance"));
        }
        rs.close();
        stmt.close();
    }

    private void printTransactions(Connection conn) throws SQLException {
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM transactions");
        System.out.println("Transactions:");
        while (rs.next()) {
            System.out.println(rs.getInt("id") + ": " + rs.getInt("from_id") + " -> " + rs.getInt("to_id") + " : " + rs.getDouble("amount") + " @ " + rs.getString("timestamp"));
        }
        rs.close();
        stmt.close();
    }
}
