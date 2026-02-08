package main.java.cmms.database;

public class TestDBConnection {
    public static void main(String[] args) {
        if (DatabaseConnection.testConnection()) {
            System.out.println("Database connection successful!");
        } else {
            System.out.println("Database connection failed!");
        }
    }
}
