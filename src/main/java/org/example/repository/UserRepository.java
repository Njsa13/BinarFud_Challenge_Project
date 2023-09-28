package org.example.repository;

import java.util.List;

public class UserRepository {
    private DatabaseConfig databaseConfig = new DatabaseConfig();

    public List<String> getIdByUsernameAndPassword(String username, String password) {
        String query = String.format("SELECT id_user FROM user WHERE username = '%s' AND password = '%s';", username, password);
        return databaseConfig.getData(query, "id_user");
    }

    public List<String> getIdByUsername(String username) {
        String query = String.format("SELECT id_user FROM user WHERE username = '%s';", username);
        return databaseConfig.getData(query, "id_user");
    }

    public boolean addUsernameAndPassword(String username, String password) {
        String query = "INSERT INTO user(username, password) VALUES(?, ?);";
        Object[] parameters = new Object[2];
        parameters[0] = username;
        parameters[1] = password;
        return databaseConfig.setData(query, parameters);
    }
}
