package login;

import controller.Response;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Login {

    public Response login (String username, String password) {
        Response resp = new Response();
        File dir = new File("src/data");
        if (dir.list().length == 0) {
            resp.code = 400;
            resp.body = "No users created";
        } else {
            for (File subFile : dir.listFiles()) {
                if (subFile.getName().equals(username)) {
                    String path = "src/data/" + username + "/details.txt";
                    try {
                        String realPassword = Files.readAllLines(Paths.get(path)).get(2);
                        if (password.equals(realPassword)) {
                            resp.code = 202;
                            resp.body = "Login Successful";
                        } else {
                            resp.code = 400;
                            resp.body = "Password does not match";
                        }
                    } catch (IOException e) {
                        resp.code = 400;
                        resp.body = e.getMessage();
                    }
                } else {
                    resp.code = 400;
                    resp.body = "No user with this username";
                }
            }
        }
        return resp;
    }

}
