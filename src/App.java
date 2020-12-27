import controller.Permission;
import controller.RepoFunction;
import controller.Response;
import login.Login;
import login.User;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class App {

    private static User user = new User("", "", "");

    public static void main(String[] arg) {
        Connection connection = connectDatabase();
        Scanner scanner = new Scanner(System.in);
        selectLoginOptions(connection, scanner);
    }

    private static void selectLoginOptions(Connection connection, Scanner scanner) {
        Response resp;
        System.out.println("Input [1] to login, [2] to create a new user or [3] to quit");
        int option = scanner.nextInt();
        if (option == 1) {
            resp = login(connection, scanner);
            if (resp.code == 202) {
                System.out.println(resp.body);
                selectUserOptions(connection, scanner);

            } else {
                System.out.println(resp.body);
                selectLoginOptions(connection, scanner);
            }
        }
        else if (option == 2) {
            resp = createUser(connection, scanner);
            System.out.println(resp.body);
            selectLoginOptions(connection, scanner);
        } else if (option == 3) {
            quit(connection);
        } else {
            System.out.println("Invalid option. Please try again.");
            selectLoginOptions(connection, scanner);
        }
    }

    private static void quit(Connection connection) {
        System.out.println("Thank you! Shutting down...");
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static Response login(Connection connection, Scanner scanner) {
        System.out.println("Input your username");
        String username = scanner.next();
        System.out.println("Input your password");
        String password = scanner.next();
        Login login = new Login();
        Response resp = login.login(username, password, connection);
        if (resp.code == 202) {
            user.setUser(username, password, connection, user);
        }
        return resp;
    }

    private static Response createUser(Connection connection, Scanner scanner) {
        System.out.println("Input your name");
        String name = scanner.next();
        System.out.println("Input your username");
        String username = scanner.next();
        System.out.println("Input your password");
        String password = scanner.next();
        User user = new User(username, password, name);
        Response resp = user.createUser(connection);
        return resp;
    }

    private static void selectUserOptions(Connection connection, Scanner scanner) {
        Response resp;
        System.out.println("Input [1] to add images, [2] to search for images, [3] to delete images, [4] to logout or [5] to quit");
        int option = scanner.nextInt();
        if (option == 1) {
            resp = addImages(connection, scanner);
            System.out.println(resp.body);
            selectUserOptions(connection, scanner);
        } else if (option == 4) {
            Login login = new Login();
            login.logout(user);
            selectLoginOptions(connection, scanner);
        } else if (option == 5) {
            quit(connection);
        }
    }

    private static Response addImages(Connection connection, Scanner scanner) {
        Response resp = new Response();
        System.out.println("Input [1] to add a single image or [2] to add multiple images");
        int option = scanner.nextInt();
        if (option == 1) {
            resp = addSingleImage(connection, scanner);
        } else if (option == 2) {
            resp = addMultipleImages(connection, scanner);
        }
        return resp;
    }

    private static Response addSingleImage(Connection connection, Scanner scanner) {
        RepoFunction rf = new RepoFunction();
        Response resp = new Response();
        System.out.println("Enter the file path of your image");
        String path = scanner.next();
        System.out.println("Input [1] for private or [2] for public access");
        int accessOption = scanner.nextInt();
        Permission access;
        if (accessOption == 1) {
            access = Permission.PRIVATE;
        } else {
            access = Permission.PUBLIC;
        }
        System.out.println("Enter the tags for the picture (separated by commas and no spaces)");
        String characteristics = scanner.next();
        String[] tags = characteristics.split(",");
        try {
            InputStream in = new FileInputStream(path);
            resp = rf.addSingleImage(in, user.getUsername(), access, tags, connection);
        } catch (FileNotFoundException e) {
           resp.code = 400;
           resp.body = "File does not exist";
        }
        return resp;
    }

    private static Response addMultipleImages(Connection connection, Scanner scanner) {
        RepoFunction rf = new RepoFunction();
        Response resp = new Response();
        List<String> paths = getFilePaths(scanner);
        System.out.println("Input [1] for private or [2] for public access");
        int accessOption = scanner.nextInt();
        Permission access;
        if (accessOption == 1) {
            access = Permission.PRIVATE;
        } else {
            access = Permission.PUBLIC;
        }
        System.out.println("Enter the tags for the picture (separated by commas and no spaces)");
        String characteristics = scanner.next();
        String[] tags = characteristics.split(",");
        InputStream[] ins = new InputStream[paths.size()];
        for (int i = 0; i < ins.length; i++) {
            try {
                InputStream in = new FileInputStream(paths.get(i));
                ins[i] = in;
            } catch (FileNotFoundException e) {
                resp.code = 400;
                resp.body = "File does not exist";
            }
        }
        resp = rf.addMultipleImages(ins, user.getUsername(), access, tags, connection);
        return resp;
    }

    private static List<String> getFilePaths(Scanner scanner) {
        List<String> paths = new ArrayList<String>();
        System.out.println("Enter the file path of your first image");
        paths.add(scanner.next());
        boolean boolCont = true;
        while (boolCont) {
            System.out.println("Enter the path of your next image");
            paths.add(scanner.next());
            boolCont = isBoolCont(scanner, true);
        }
        return paths;
    }

    private static boolean isBoolCont(Scanner scanner, boolean boolCont) {
        System.out.println("Do you want to add another image? [y] or [n]");
        String boolAnother = scanner.next();
        if (boolAnother.equals("n")) {
            boolCont = false;
        } else if (!boolAnother.equals("y") && !boolAnother.equals("n")) {
            isBoolCont(scanner, boolCont);
        }
        return boolCont;
    }

    public static Connection connectDatabase() {
        Connection connection = null;
        String url = "jdbc:mysql://localhost:3306/ImageRepository";
        String username = "root";
        String password = "Gdqmlp6704*";
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(url, username, password);
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return connection;
    }

}
