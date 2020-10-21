package jdbcDemo;

import java.security.cert.CertificateRevokedException;
import java.sql.*;
import java.util.Properties;
import java.util.Scanner;

public class Main {
    public static String DB_DRIVER = "com.mysql.cj.jdbc.Driver";
    public static String DB_URL = "jdbc:mysql://root:9Mx4uokkk@localhost:3306/soft_uni";

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter DB username (<Enter> for 'root'): ");
        String username = sc.nextLine().trim();
        username = username.length() > 0 ? username : "root";

        System.out.println("Enter DB password (<Enter> for default): ");
        String password = sc.nextLine().trim();
        password = password.length() > 0 ? password : "9Mx4uokkk";

        // 1. Load DB driver
        try {
            Class.forName(DB_DRIVER);
        } catch (ClassNotFoundException e) {
            System.err.printf("Database driver: '%s' not found!%n", DB_DRIVER);
            System.exit(0);
        }

        System.out.println("DB driver loaded successfully.");

        System.out.println("Enter minimum salary (<Enter> for '40000'): ");
        String salary = sc.nextLine().trim();
        salary = salary.length() > 0 ? salary : "40000";
        double salaryAsDouble = 40000;
        try {
            salaryAsDouble = Double.parseDouble(salary);
        }   catch (NumberFormatException ex) {
            System.err.println("Invalid number");
        }

        // 2. Connect to DB
        // try {
        //     DriverManager.getConnection(DB_URL);
        // } catch (SQLException throwables) {
        //     System.err.printf("Cannot connect to DB: %s%n", DB_URL);
        //     System.exit(0);
        // }
        //
        // System.out.println("Connection created successfully: %s%n");


        //Essential Part!
        Properties props = new Properties();
        props.setProperty("user", username);
        props.setProperty("password", password);


        // try with recourses block, Connection class implements AutoCloseable interface
        // we do not need to close it manually
        try (Connection con = DriverManager.getConnection(DB_URL, props)) {
            System.out.printf("DB connection successful: %s%n", DB_URL);

            //3. Create Prepared Statement
            PreparedStatement ps = con.prepareStatement("SELECT * FROM employees WHERE SALARY > ?");

            // 4. Execute prepared statement with parameter
            ps.setDouble(1, salaryAsDouble);
            ResultSet rs = ps.executeQuery();

            //5. Print
            while (rs.next()) {
                System.out.printf("| %10d | %-15.15s | %-15.15s | %10.2f |%n",
                        rs.getLong("employee_id"),
                        rs.getString("first_name"),
                        rs.getString("last_name"),
                        rs.getDouble("salary")
                        );
            }

        //  6. Close connection
        //  try {
        //      con.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}
