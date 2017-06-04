package com.oroblam.integration;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.client.ExpectedCount;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;
import org.subethamail.wiser.Wiser;

import com.oroblam.model.Resource;
import com.oroblam.repository.AddResourceException;
import com.oroblam.repository.ResourceRepository;
import com.oroblam.service.NotificationService;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = RANDOM_PORT)
@TestPropertySource(locations = "classpath:test.properties")
public class IntegrationTests {

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Value("${working.directory}")
    private String workingDirectory = "";

    // class that will we used to call external services
    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private Wiser wiser;

    @Autowired
    private ResourceRepository resourceRepository;

    @Autowired
    private NotificationService notificationService;

    private HttpHeaders httpHeaders;

    @Before
    public void setUp() {

        httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
    }

    @After
    public void tearDown() {

    }

    @Test
    public void shouldAddANewURLToTheResourcesList() throws AddResourceException {
        MockRestServiceServer mockServer = MockRestServiceServer.createServer(restTemplate);
        mockServer.expect(ExpectedCount.once(), requestTo("http://www.test.co.uk")).andRespond(withSuccess());
        Resource resource = new Resource("http://www.test.co.uk");
        HttpEntity<Resource> request = new HttpEntity<>(resource, httpHeaders);
        URI location = testRestTemplate.postForLocation("/api/monitor", request, Resource.class);
        assertThat(location, notNullValue());
        // Check file creation
        Path filePath = Paths.get(workingDirectory, String.valueOf(resource.hashCode()) + ".json");
        assertTrue("File does not exist:" + filePath, Files.exists(filePath));
        mockServer.verify();
    }

    @Test
    public void shouldDetectChangeWhenAResourceIsUpdatedAndNotify() throws Exception {
        //start mail server
        wiser.start();

        // Push resource
        Resource resource = new Resource("http://www.test.co.uk");
        HttpEntity<Resource> request = new HttpEntity<>(resource, httpHeaders);

        // prepare server
        MockRestServiceServer mockServer = MockRestServiceServer.createServer(restTemplate);
        mockServer.expect(ExpectedCount.once(), requestTo("http://www.test.co.uk"))
                .andRespond(withSuccess("Hello world", MediaType.TEXT_PLAIN));

        // add resource
        URI location = testRestTemplate.postForLocation("/api/monitor", request, Resource.class);

        mockServer.reset();

        // prepare server
        mockServer.expect(ExpectedCount.once(), requestTo("http://www.test.co.uk"))
                .andRespond(withSuccess("Hello again world!", MediaType.TEXT_PLAIN));

        // Trigger checks
        testRestTemplate.getForEntity("/api/check", String.class);

        // Check notification
        assertThat(wiser.getMessages().size(), is(1));

        wiser.stop();
    }
}
