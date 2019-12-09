package app;

import app.controller.controller.GameController;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class GameControllerTest {
    @Autowired
    private GameController gameController;

    @Test
    public void testGetAll() throws Exception {
        assertThat(gameController).isNotNull();
    }

    @Test
    public void testGetOne() throws Exception {
        assertThat(gameController).isNotNull();
    }

    @Test
    public void testDelete() throws Exception {
        assertThat(gameController).isNotNull();
    }
}
