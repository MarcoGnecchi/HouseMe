package com.oroblam.controller;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

import com.oroblam.service.MonitorService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = RANDOM_PORT)
public class ExecutorControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @MockBean
    private MonitorService monitorService;

    @Test
    public void shouldCheckChangesOnResources() {
        ResponseEntity<String> response = restTemplate.getForEntity("/api/check", String.class);
        assertThat(response.getStatusCode(), equalTo(HttpStatus.OK));
        verify(monitorService, times(1)).run();
    }
}
