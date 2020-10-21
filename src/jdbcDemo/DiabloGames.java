package jdbcDemo;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;
import java.util.Properties;
import java.util.Scanner;

public class DiabloGames {
    public static void main(String[] args) {
        // 1. Read params from external property file
        Properties props = new Properties();
        String path = DiabloGames.class.getClassLoader()
                .getResource("jdbc.properties")
                .getPath();

        try {
            props.load(new FileInputStream(path));
        } catch (IOException e) {
            e.printStackTrace();
        }

        //TODO: add meaningful defaults

        Scanner sc = new Scanner(System.in);
        String username = sc.nextLine().trim();
        // 2. try with resources - Connection, PreparedStatement
        try (Connection con = DriverManager.getConnection(
                props.getProperty("db.url"),
                props.getProperty("db.user"),
                props.getProperty("db.password"));
             PreparedStatement ps = con.prepareStatement(
                     props.getProperty("sql.games"))) {
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();

            // 3. Print results
            while (rs.next()) {
                if (rs.getLong("id") == 0) {
                    System.out.println("No such user");
                } else {
                    System.out.printf("| %10d | %-15.15s | %-15.15s | %10d |%n",
                            rs.getLong("id"),
                            rs.getString("first_name"),
                            rs.getString("last_name"),
                            rs.getInt("count")
                    );
                }
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

    }
}
