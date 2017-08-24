package io.robusta.auth.test;

import io.robusta.auth.business.UserBusiness;
import io.robusta.auth.dao.MySQLDatabaseConnection;
import io.robusta.auth.domain.User;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;



public class UserBusinessTest {

    @Deployment
    public static JavaArchive createDeployment() {
        return ShrinkWrap.create(JavaArchive.class)
                .addClass(io.robusta.auth.dao.MySQLDatabaseConnection.class)
                .addClass(io.robusta.auth.domain.User.class)
                .addClass(io.robusta.auth.domain.UserInterface.class)
                .addClass(io.robusta.auth.business.UserBusiness.class)
                .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
    }

    MySQLDatabaseConnection databaseConnection = new MySQLDatabaseConnection();
    private Connection connection= databaseConnection.getConnection();
    UserBusiness uBusiness = new UserBusiness(databaseConnection);


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
            uBusiness.createUser(currentUser);

    }

    @After
    public void tearDown() throws Exception {
        String sql = "DELETE FROM `users` ";
        java.sql.PreparedStatement statement = connection.prepareStatement(sql);
        statement.executeUpdate();
    }


    @Test
    public void findAllTest() throws Exception{
        List<User> allUsers = uBusiness.findAll();
        assertEquals(3, allUsers.size());
    }

    @Test
    public void createUserTest() throws Exception {

        assertEquals(3,uBusiness.findAll().size());

        User user = new User("Nicolas", "nicolas@toto.com", "myPassword");
        uBusiness.createUser(user);

        assertEquals(4,uBusiness.findAll().size());


    }

    @Test
    public void findByEmailTest() throws Exception {

        User user = new User("Nicolas", "nicolas@toto.com", "myPassword");
        uBusiness.createUser(user);
        User foundUser = uBusiness.findByEmail(user.getEmail());
        assertEquals("Nicolas", foundUser.getName());
        assertEquals("nicolas@toto.com", foundUser.getEmail());
        assertEquals("myPassword", foundUser.getPassword());
    }

    @Test
    public void findByNameTest() throws Exception {

        User user = new User("Nicolas", "nicolas@toto.com", "myPassword");
        uBusiness.createUser(user);
        User foundUser = uBusiness.findByName(user.getName());
        assertEquals("Nicolas", foundUser.getName());
        assertEquals("nicolas@toto.com", foundUser.getEmail());
        assertEquals("myPassword", foundUser.getPassword());

    }

    @Test
    public void updateUserTest() throws Exception {
        User userToUpdate = uBusiness.findByName("Default 3");
        userToUpdate.setName("default3UPDATED");
        uBusiness.updateUser(userToUpdate);
        assertEquals("default3UPDATED", uBusiness.findByEmail("default3@titi.com").getName());

        userToUpdate.setPassword("newPassword");
        uBusiness.updateUser(userToUpdate);
        assertEquals("newPassword", uBusiness.findByName("default3UPDATED").getPassword());

    }

    @Test
    public void deleteUserTest() throws Exception {

        List<User> allUsers = uBusiness.findAll();
        assertEquals(3, allUsers.size());

        User userToDelete = uBusiness.findByName("Default 3");
        uBusiness.deleteUser(userToDelete);
        allUsers = uBusiness.findAll();
        assertEquals(2, allUsers.size());
    }


}
