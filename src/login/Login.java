package login;

import controller.Response;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Login {

    public Response login (String username, String password, Connection connection) {
        String databaseUsername = "";
        String databasePassword = "";
        Response resp = new Response();
        String query = "SELECT * FROM users WHERE username='" + username + "' && password='" + password+ "'";

        try {
            PreparedStatement preparedStmt = connection.prepareStatement(query);
            ResultSet rs = preparedStmt.executeQuery();

            while (rs.next()) {
                databaseUsername = rs.getString("username");
                databasePassword = rs.getString("password");

            }
            if (username.equals(databaseUsername) && password.equals(databasePassword)) {
                resp.code = 202;
                resp.body = "Logged In Successfully";
            } else {
                resp.code = 400;
                resp.body = "Invalid Username or Password";
            }
        } catch (SQLException e) {
            resp.code = 400;
            resp.body = e.getMessage();
        }
        return resp;
    }

    public void logout(User user) {
        user.setUsername("");
        user.setPassword("");
        user.setName("");
    }
}
