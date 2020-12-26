package login;

import controller.Response;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class User {

    private String username;
    private String password;
    private String name;

    public User(String username, String password, String name) {
        this.username = username;
        this.password = password;
        this.name = name;
    }

    public Response createUser() {
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
        } else if (isIdentical(this.username)) {
            resp.code = 400;
            resp.body = "User cannot be created, identical username";
        } else {
            File userFile = new File("src/data/" + this.username);
            try {
                boolean boolUser = userFile.mkdir();
                if (boolUser) {
                    File imagesFile = new File(userFile + "/images");
                    File userDetails = new File(userFile + "/details.txt");
                    try {
                        boolean boolImages = imagesFile.mkdir();
                        boolean boolDetails = userDetails.createNewFile();
                        if (boolImages & boolDetails) {
                            resp.code = 200;
                            resp.body = "User successfully created";
                            writeDetails(userFile+"/details.txt", this.username, this.password, this.name);
                        } else {
                            resp.code = 400;
                            resp.body = "User file could not be created";
                        }
                    } catch (Exception e) {
                        resp.code = 200;
                        resp.body = e.getMessage();
                    }
                } else {
                    resp.code = 400;
                    resp.body = "User file could not be created";
                }
            } catch (Exception e) {
                resp.code = 200;
                resp.body = e.getMessage();
            }
        }
        return resp;
    }

    private void writeDetails(String path, String username, String password, String name) throws IOException {
        FileWriter writer = new FileWriter(path);
        writer.write(name);
        writer.write("\r\n");
        writer.write(username);
        writer.write("\r\n");
        writer.write(password);
        writer.close();
    }

    private boolean isIdentical(String username) {
        File dir = new File("src/data");
        for (File subFile : dir.listFiles()) {
            if (subFile.getName().equals(username)) {
                return true;
            }
        }
        return false;
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

}

