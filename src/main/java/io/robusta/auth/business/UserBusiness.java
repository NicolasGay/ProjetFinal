package io.robusta.auth.business;


import io.robusta.auth.dao.MySQLDatabaseConnection;
import io.robusta.auth.domain.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserBusiness {
    private Connection connection;

    public UserBusiness(MySQLDatabaseConnection connection) {
        this.connection = connection.getConnection();
    }

    public User findByEmail(String email) {
        try {
            String sql = "SELECT * FROM `users` WHERE `email` = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, email);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next())
                return userFactory(resultSet);
            else
                return null;
        } catch (SQLException e) {
            throw new RuntimeException("Impossible de réaliser l(es) opération(s)", e);
        }
    }

    public User findByName(String name) {
        try {
            String sql = "SELECT * FROM `users` WHERE `name` = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, name);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next())
                return userFactory(resultSet);
            else
                return null;
        } catch (SQLException e) {
            throw new RuntimeException("Impossible de réaliser l(es) opération(s)", e);
        }


    }

    public List<User> findAll() {

        List<User> foundUsers = new ArrayList<>();

        try {
                String sql = "SELECT * FROM `users`";
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next())
                foundUsers.add(userFactory(resultSet));

            return foundUsers;

        } catch (SQLException e) {
            throw new RuntimeException("Impossible de réaliser l(es) opération(s)", e);
        }

    }


    public void createUser(User user) {

        try {
            String sql = "INSERT INTO `users` (`name`,`email`,`password`) VALUES (?,?,?)";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, user.getName());
            statement.setString(2, user.getEmail());
            statement.setString(3, user.getPassword());

            statement.executeUpdate();


        } catch (SQLException e) {
            throw new RuntimeException("Impossible d'ajouter votre compte", e);
        }


    }

    public void updateUser(User user) {
        if (user.getName() == null)
            throw new RuntimeException("Impossible d'effacer un user sans nom !!");

        try {
            String sql = "UPDATE `users` SET `name` = ?,  `password`= ? WHERE `email` = ? ";
            PreparedStatement statement = connection.prepareStatement(sql);

            statement.setString(1, user.getName());
            statement.setString(2, user.getPassword());
            statement.setString(3, user.getEmail());

            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Impossible de mettre à jour l'utilisateur", e);
        }
    }

    public void deleteUser(User user) {
        try {
            String sql = "DELETE FROM `users` WHERE `email` = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, user.getEmail());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Impossible de retirer cet utilisateur", e);
        }

    }

    private User userFactory(ResultSet resultSet) throws SQLException {

        User user = new User();

        String email = resultSet.getString("email");
        String name = resultSet.getString("name");
        String password = resultSet.getString("password");

        user.setEmail(email);
        user.setName(name);
        user.setPassword(password);

        return user;
    }


}