import java.sql.*;

// Press Shift twice to open the Search Everywhere dialog and type `show whitespaces`,
// then press Enter. You can now see whitespace characters in your code.
public class Main {
    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/db_nations";
        String user = "root";
        String password = "root";


        try (Connection con = DriverManager.getConnection(url, user, password)) {
            String sql = "select c.name, c.country_id, r.name, c2.name " +
                    "from countries c " +
                    "join regions r on c.region_id = r.region_id " +
                    "join continents c2 on c2.continent_id = r.continent_id " +
                    "order by c.name";
            // la connection prepara uno statement sql
            try (PreparedStatement preparedStatement = con.prepareStatement(sql)){

                // eseguo il prepared statement
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    while (resultSet.next()){
                    String countryName = resultSet.getString(1);
                    int countryId = resultSet.getInt(2);
                    String regionName = resultSet.getString(3);
                    String continentName = resultSet.getString(4);
                    System.out.println( countryName + " " + countryId + " " + regionName + " " + continentName);
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}