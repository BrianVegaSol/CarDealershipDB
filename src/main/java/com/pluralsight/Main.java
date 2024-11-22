package com.pluralsight;

import java.sql.*;
import java.util.HashMap;
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
    /*private static <T> HashMap <Integer, T> valuesToUpdate () {
       <T> HashMap <Integer, T> map =  new HashMap<>();
    }*/

    private static void updateVehicleRecord(Connection connection, Scanner scanner) throws SQLException {
        viewVehicleRecords(connection);
        String columnToUpdate = "";
        System.out.print("Enter the VIN of the Vehicle to update: ");
        int VIN = scanner.nextInt();
        scanner.nextLine(); // Consume newline
        System.out.println("""
                What would you like to update?
                1) Year
                2) Make
                3) Model
                4) Vehicle Type
                5) Color
                6) Mileage
                7) Price
                8) Sold (0 for false/ 1 for true)
                """);
        int input = -1;
        input = scanner.nextInt();
        switch (input) {
            case 0 -> {
                return;
            }
            case 1 -> columnToUpdate = "Year";
            case 2 -> columnToUpdate = "Make";
            case 3 -> columnToUpdate = "Model";
            case 4 -> columnToUpdate = "Vehicle Type";
            case 5 -> columnToUpdate = "Color";
            case 6 -> columnToUpdate = "Odometer";
            case 7 -> columnToUpdate = "Price";
            case 8 -> columnToUpdate = "Sold";
            default -> System.out.println("Invalid Input");
        }
        System.out.print("Enter new value: ");
        Object setValue = switch (columnToUpdate) {
            case "Year", "Odometer" -> scanner.nextInt();
            case "Model", "Vehicle Type", "Color" -> scanner.nextLine();
            case "Price" -> scanner.nextDouble();
            case "Sold" -> scanner.nextByte();
            default -> throw new IllegalArgumentException("Invalid Input");
        };

        if (setValue instanceof Byte) {
            if ((byte) setValue > 1 || (byte) setValue < 0) {
                throw new IllegalArgumentException("Invalid Input");
            }
        }

        String sql = "UPDATE [BVS_Table2:Vehicles] SET " + columnToUpdate + " = ?" + " WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {

            switch (setValue) {
                case String s -> stmt.setString(1, s);
                case Integer i -> stmt.setInt(1, i);
                case Double d -> stmt.setDouble(1, d);
                case Byte b -> stmt.setByte(1, b);
                case null, default ->
                        throw new IllegalArgumentException("Unsupported data type for newValue: " + setValue.getClass().getName());
            }

            //stmt.setString(1, newName);
            //stmt.setString(2, newFlavor);
            //stmt.setInt(1, setValue);
            stmt.setInt(2, VIN);
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Vehicle updated successfully!");
            } else {
                System.out.println("No Vehicle found with the given VIN.");
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
