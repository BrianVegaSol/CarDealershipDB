package com.pluralsight;

import java.sql.*;
import java.util.Scanner;

public class Main {
    // Azure SQL Connection Details
    private static final String DB_URL = "jdbc:sqlserver://skills4it.database.windows.net:1433;database=courses;encrypt=true;trustServerCertificate=false;hostNameInCertificate=*.database.windows.net;";
    private static final String USER = "user25";
    private static final String PASSWORD = "goldenphoenix2024!";

    public static void main(String[] args) {
        try (Connection connection = DriverManager.getConnection(DB_URL, USER, PASSWORD)) {
            System.out.println("Connected to Azure SQL Database!");

            Scanner scanner = new Scanner(System.in);
            while (true) {
                System.out.println("\nCar Dealership CRUD Application");
                System.out.println("1. Add Vehicle");
                System.out.println("2. View Vehicle Records");
                System.out.println("3. Update a Cookie");
                System.out.println("4. Delete a Cookie");
                System.out.println("5. Exit");
                System.out.print("Choose an option: ");

                int choice = scanner.nextInt();
                scanner.nextLine(); // Consume newline

                switch (choice) {
                    case 1 -> addVehicle(connection, scanner);
                    case 2 -> viewVehicleRecords(connection);
                    case 3 -> updateVehicleRecord(connection, scanner);
                    case 4 -> deleteCookie(connection, scanner);
                    case 5 -> {
                        System.out.println("Exiting application...");
                        return;
                    }
                    default -> System.out.println("Invalid choice, please try again.");
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    // CREATE operation
    private static void addVehicle(Connection connection, Scanner scanner) throws SQLException {
       /* System.out.print("Enter VIN: ");
        int VIN = scanner.nextInt();
        scanner.nextLine(); // Consume newline*/

        System.out.print("Enter Year: ");
        int year = scanner.nextInt();
        scanner.nextLine();

        System.out.print("Enter Make: ");
        String make = scanner.nextLine();

        System.out.print("Enter Model: ");
        String model = scanner.nextLine();

        System.out.print("Enter Vehicle Type: ");
        String vehicleType = scanner.nextLine();

        System.out.print("Enter Color: ");
        String color = scanner.nextLine();

        System.out.print("Enter Mileage: ");
        int odometer = scanner.nextInt();

        System.out.print("Enter Price: ");
        double price = scanner.nextDouble();

        System.out.print("Has the Vehicle Been Sold?\n1)Yes\n0)No ");
        byte sold = scanner.nextByte();

        String sql = "INSERT INTO [BVS_Table2:Vehicles] ([Year], [Make], " +
                "[Model], [VehicleType], [Color], [Odometer], [Price], [Sold]) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            //stmt.setInt(1, VIN);
            stmt.setInt(1, year);
            stmt.setString(2, make);
            stmt.setString(3, model);
            stmt.setString(4, vehicleType);
            stmt.setString(5, color);
            stmt.setInt(6, odometer);
            stmt.setDouble(7, price);
            stmt.setByte(8, sold);
            stmt.executeUpdate();
            System.out.println("Vehicle added successfully!");
        }
    }

    // READ operation
    private static void viewVehicleRecords(Connection connection) throws SQLException {
        String sql = "SELECT * FROM [BVS_Table2:Vehicles]";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            System.out.println("\nVehicle Records:");
            while (rs.next()) {
                System.out.println("Year: " + rs.getInt("Year") +
                        "\nMake: " + rs.getString("Make") +
                        "\nModel: " + rs.getString("Model") +
                "\nVehicle Type: " + rs.getString("VehicleType") +
                "\nColor: " + rs.getString("Color") +
                        "\nMileage: " + rs.getInt("Odometer") +
                                "\nPrice: $" + rs.getDouble("Price") +
                        "\nisSold: " + rs.getByte("Sold") + "\n\n"
                );
            }
        }
    }

    // UPDATE operation
    private static void updateVehicleRecord(Connection connection, Scanner scanner) throws SQLException {
        System.out.print("Enter the ID of the cookie to update: ");
        int id = scanner.nextInt();
        scanner.nextLine(); // Consume newline
        //What would you like to update?
        System.out.print("Enter new name: ");
        String newName = scanner.nextLine();
        System.out.print("Enter new flavor: ");
        String newFlavor = scanner.nextLine();

        String sql = "UPDATE Cookies.Cookie SET name = ?, flavor = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, newName);
            stmt.setString(2, newFlavor);
            stmt.setInt(3, id);
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Cookie updated successfully!");
            } else {
                System.out.println("No cookie found with the given ID.");
            }
        }
    }

    // DELETE operation
    private static void deleteCookie(Connection connection, Scanner scanner) throws SQLException {
        System.out.print("Enter the ID of the cookie to delete: ");
        int id = scanner.nextInt();

        String sql = "DELETE FROM Cookies.Cookie WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Cookie deleted successfully!");
            } else {
                System.out.println("No cookie found with the given ID.");
            }
        }
    }
}
