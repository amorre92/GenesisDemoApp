package amorre.genesis.demo;

import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

/**
 * @author Anthony Morre
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = DemoApplicationTestConfiguration.class)
@WebAppConfiguration
public abstract class AbstractIntegrationTest {

}
