package amorre.genesis.demo;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import javax.annotation.PostConstruct;

/**
 * Abstract integration test mainly aimed at testing controllers. What it provides:
 * <ul>
 * <li>Spring initialization, within a testing context
 * <li>Transactional behaviour by default
 * <li>An accessible jackson object mapper via the {@link #objectMapper()} method
 * </ul>
 *
 * @author Anthony Morre
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {DemoApplicationTestConfiguration.class})
@WebAppConfiguration
@Transactional
public abstract class AbstractControllerTest {
    private MockMvc mvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private ObjectMapper objectMapper;


    @PostConstruct
    public void initialize() {
        this.mvc = MockMvcBuilders
                .webAppContextSetup(this.webApplicationContext)
                .build();
    }

    public MockMvc mockMvc() {
        return this.mvc;
    }

    public ObjectMapper objectMapper() {
        return objectMapper;
    }

}
