package io.robusta.auth.test;

import com.mysql.jdbc.PreparedStatement;
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

import java.sql.ResultSet;
import java.sql.SQLException;


@RunWith(Arquillian.class)
public class UserBusinessTest {

    @Deployment
    public static JavaArchive createDeployment() {
        return ShrinkWrap.create(JavaArchive.class)
                .addClass(io.robusta.auth.business.UserBusiness.class)
                .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
    }

    @Before
    public void setUp() throws Exception {


    }

    @Test
    public void createPasswordUser() throws Exception {
        MySQLDatabaseConnection databaseConnection = new MySQLDatabaseConnection();
        UserBusiness uBusiness = new UserBusiness(databaseConnection);
        User user = new User("Nicolas", "nicolas@toto.com", "myPassword");
        uBusiness.createPasswordUser(user.getEmail(), user.getPassword());

        try {
            String sql = "SELECT * FROM `users` WHERE name = ?";
            PreparedStatement statement = uBusiness.connection.prepareStatement(sql);
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


    }

    @Test
    public void findByName() throws Exception {
    }

    @Test
    public void updateUser() throws Exception {
    }

    @Test
    public void deleteUser() throws Exception {
    }


}
