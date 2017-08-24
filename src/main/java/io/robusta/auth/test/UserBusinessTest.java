package io.robusta.auth.test;

import io.robusta.auth.business.UserBusiness;
import io.robusta.auth.dao.MySQLDatabaseConnection;
import io.robusta.auth.domain.User;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


@RunWith(Arquillian.class)
public class UserBusinessTest {

    @Deployment
    public static JavaArchive createDeployment() {
        return ShrinkWrap.create(JavaArchive.class)
                .addClass(io.robusta.auth.business.UserBusiness.class)
                .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
    }

    MySQLDatabaseConnection databaseConnection = new MySQLDatabaseConnection();
    private Connection connection= databaseConnection.getConnection();
    UserBusiness uBusiness = new UserBusiness();


    @Before
    public void setUp() throws Exception {


        List<User> defaultUsers = new ArrayList<>();
        User default1 = new User("Default 1", "default1@toto.com", "defaultPWD1");
        User default2 = new User("Default 2", "default2@tata.com", "defaultPWD2");
        User default3 = new User("Default 3", "default3@titi.com", "defaultPWD3");

        defaultUsers.add(default1);
        defaultUsers.add(default2);
        defaultUsers.add(default3);

        for(User currentUser : defaultUsers)
            uBusiness.createPasswordUser(currentUser.getEmail(), currentUser.getPassword());



    }

    @Test
    public void createPasswordUser() throws Exception {

        User user = new User("Nicolas", "nicolas@toto.com", "myPassword");
        uBusiness.createPasswordUser(user.getEmail(), user.getPassword());

        try {
            String sql = "SELECT * FROM `users` WHERE name = ?";
            java.sql.PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, user.getUsername());
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next())
                System.out.println("You added a user in the database which name is " + resultSet.getString("name") + " and password is " + resultSet.getString("password"));
            else
                System.out.println("ERROR : no result found");
        } catch (SQLException e) {
            throw new RuntimeException("Impossible de réaliser l(es) opération(s)", e);
        }


    }

    @Test
    public void findByEmail() throws Exception {

        User user = new User("Nicolas", "nicolas@toto.com", "myPassword");
        User foundUser = (User) uBusiness.findByEmail(user.getEmail());

        if(foundUser == user)
            System.out.println("SUCCESS !");
        else
            System.out.println("FAIL !");


    }

    @Test
    public void findByName() throws Exception {

        User user = new User("Nicolas", "nicolas@toto.com", "myPassword");
        User foundUser = (User) uBusiness.findByName(user.getUsername());

        if(foundUser == user)
            System.out.println("SUCCESS !");
        else
            System.out.println("FAIL !");


    }

    @Test
    public void updateUser() throws Exception {
    }

    @Test
    public void deleteUser() throws Exception {
    }


}
