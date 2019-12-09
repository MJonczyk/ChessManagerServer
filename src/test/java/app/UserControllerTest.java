package app;

import static org.assertj.core.api.Assertions.assertThat;

import app.controller.controller.UserController;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserControllerTest {

    @Autowired
    private UserController userController;

    @Test
    public void testAdduser() throws Exception {
        assertThat(userController).isNotNull();
    }

    @Test
    public void testSignIn() throws Exception {
        assertThat(userController).isNotNull();
    }

    @Test
    public void testGetUsersRole() throws Exception {
        assertThat(userController).isNotNull();
    }

    @Test
    public void testDeleteUser() throws Exception {
        assertThat(userController).isNotNull();
    }

}
