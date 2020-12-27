package controller;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.sql.*;

public class RepoFunction implements IRepoFunction{

    @Override
    public Response addSingleImage(InputStream path, String username, Permission permission,
                                   String[] characteristics, Connection connection) {
        Response resp = new Response();
        String query = "insert into images (username, access, img)"
                + "values (?, ?, ?)";
        try {
            PreparedStatement preparedStmt = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            preparedStmt.setString(1, username);
            preparedStmt.setString(2, String.valueOf(permission));
            preparedStmt.setBlob(3, path);
            preparedStmt.executeUpdate();
            addCharacteristics(preparedStmt, characteristics, connection);
            resp.code = 200;
            resp.body = "Image Added Successfully";
        } catch (SQLException e) {
            resp.code = 400;
            resp.body = e.getMessage();
        }
        return resp;
    }

    private void addCharacteristics(PreparedStatement preparedStmt, String[] characteristics, Connection connection) throws SQLException {
        ResultSet generatedKeys = preparedStmt.getGeneratedKeys();
        int imageId = 0;
        if (generatedKeys.next()) {
            imageId = generatedKeys.getInt(1);
        } else {
            throw new SQLException("Creating characteristics failed, no ID obtained.");

        }
        String query = "Insert into characteristics (image_id, characteristic)" +
                "values (?, ?)";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        for (int i = 0; i < characteristics.length; i++) {
            preparedStatement.setInt(1, imageId);
            preparedStatement.setString(2, characteristics[i]);
            preparedStatement.execute();
        }
    }

    @Override
    public Response addMultipleImages(InputStream[] paths, String username, Permission permission,
                                      String[] characteristics, Connection connection) {
        Response finalResp = new Response();
        Response initResp = new Response();
        for (InputStream path : paths) {
            initResp = addSingleImage(path, username, permission, characteristics, connection);
            if (initResp.code == 400) {
                finalResp.code = initResp.code;
                finalResp.body = initResp.body;
                return finalResp;
            }
        }
        finalResp.code = initResp.code;
        finalResp.body = "Images Added Successfully";
        return finalResp;
    }

}
