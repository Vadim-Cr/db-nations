package org.learning.java.sql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class ProvaCostanza {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        // Parametri di connessione
        String url = "jdbc:mysql://localhost:3306/db_airport";
        String user = "root";
        String password = "rootpassword";

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        // provo ad aprire una connection con try-with-resources
        try (Connection connection = DriverManager.getConnection(url, user, password)) {
            // dentro al try ho la connection aperta
            // System.out.println(connection.getCatalog());
            System.out.print("Departure airport: ");
            String userAirport = scanner.nextLine();
            System.out.print("Year: ");
            int userYear = Integer.parseInt(scanner.nextLine());

            String query =
                    "SELECT number, departure_datetime , arrival_datetime , da.name as departure_airport, aa.name as arrival_airport "
                            + "FROM flights f "
                            + "JOIN airports da ON f.departure_airport_id = da.id "
                            + "JOIN airports aa ON f.arrival_airport_id = aa.id "
                            + "WHERE YEAR(departure_datetime) = ? "
                            + "AND da.name = ?;";

            // la connection prepara uno statement sql
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                // facciamo il binding dei parametri
                // il primo parametro è un intero
                preparedStatement.setInt(1, userYear);
                // il secondo parametro è una stringa
                preparedStatement.setString(2, userAirport);

                // eseguo il prepared statement
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    // itero sul result set
                    while (resultSet.next()) {
                        // ad ogni iterazione resultSet si sposta e punta alla riga successiva
                        String flightNumber = resultSet.getString("number");
                        LocalDateTime departureDateTime = resultSet.getTimestamp("departure_datetime")
                                .toLocalDateTime();
                        LocalDateTime arrivalDateTime = resultSet.getTimestamp("arrival_datetime")
                                .toLocalDateTime();
                        String departureAirport = resultSet.getString("departure_airport");
                        String arrivalAirport = resultSet.getString("arrival_airport");
                        System.out.println(flightNumber + " " + departureDateTime.format(formatter) + " "
                                + departureAirport + " " + arrivalDateTime.format(formatter) + " " +
                                arrivalAirport);
                    }
                } catch (SQLException e) {
                    System.out.println("Unable to execute query");
                    e.printStackTrace();
                }
            } catch (SQLException e) {
                System.out.println("Unable to prepare statement");
                e.printStackTrace();
            }

        } catch (SQLException e) {
            System.out.println("Unable to open connection");
            e.printStackTrace();
        }
        scanner.close();
    }
}