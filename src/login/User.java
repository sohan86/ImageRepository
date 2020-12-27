package login;

import controller.Response;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.*;

public class User {

    private String username;
    private String password;
    private String name;

    public User(String username, String password, String name) {
        this.username = username;
        this.password = password;
        this.name = name;
    }

    public Response createUser(Connection connection) {
        Response resp = new Response();
        if (isNumberInString(this.name)) {
            resp.code = 400;
            resp.body = "Illegal name with numbers";
        } else if (this.name.isBlank()) {
            resp.code = 400;
            resp.body = "User cannot be created, blank name";
        } else if (this.username.isBlank()) {
            resp.code = 400;
            resp.body = "User cannot be created, blank username";
        } else if (this.password.isBlank()) {
            resp.code = 400;
            resp.body = "User cannot be created, blank password";
        } else {
            String query = "insert into users (username, password, full_name)"
                    + "values (?, ?, ?)";
            try {
                PreparedStatement preparedStmt = connection.prepareStatement(query);
                preparedStmt.setString(1, this.username);
                preparedStmt.setString(2, this.password);
                preparedStmt.setString(3, this.name);
                preparedStmt.execute();
                resp.code = 200;
                resp.body = "User created successfully";
            } catch (SQLException e) {
                resp.code = 400;
                if (e instanceof SQLIntegrityConstraintViolationException) {
                    resp.body = "User already created with same username, try another username";
                } else {
                    resp.body = e.getMessage();
                }
            }

        }
        return resp;
    }

    private boolean isNumberInString(String name) {
        char[] chars = name.toCharArray();
        for(char c : chars){
            if(Character.isDigit(c)){
                return true;
            }
        }
        return false;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getName() {
        return name;
    }

    public void setUser(String username, String password, Connection connection, User user) {
        String databaseUsername = "";
        String databasePassword = "";
        String databaseName = "";
        String query = "SELECT * FROM users WHERE username='" + username + "' && password='" + password+ "'";
        try {
            PreparedStatement preparedStmt = connection.prepareStatement(query);
            ResultSet rs = preparedStmt.executeQuery();

            while (rs.next()) {
                databaseUsername = rs.getString("username");
                databasePassword = rs.getString("password");
                databaseName = rs.getString("full_name");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        user.setUsername(databaseUsername);
        user.setPassword(databasePassword);
        user.setName(databaseName);
    }

    public void setUsername (String username) {this.username = username;}

    public void setPassword (String password) {this.password = password;}

    public void setName (String name) {this.name = name;}
}

