package com.oroblam;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@RunWith(SpringRunner.class)
public class ControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void testRunChecks() throws Exception {
        String body = restTemplate.getForObject("/status", String.class);
        assertThat(body).isEqualTo("OK");
    }
}