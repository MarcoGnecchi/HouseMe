package com.oroblam.controller;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

import java.net.URI;
import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.client.ExpectedCount;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

import com.oroblam.model.Resource;
import com.oroblam.repository.AddResourceException;
import com.oroblam.repository.ResourceRepository;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@RunWith(SpringRunner.class)
public class ResourceControllerTest {

    @Autowired
    private TestRestTemplate testRestTemplate;

    @MockBean
    private ResourceRepository resourceRepository;

    private HttpHeaders httpHeaders;

    @Autowired
    private RestTemplate restTemplate;

    @Before
    public void setUp() {
        httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
    }

    @Test
    public void shouldReturnTheStatusOfTheProcess() throws Exception {
        String response = testRestTemplate.getForObject("/api/status", String.class);
        assertThat(response, equalTo("OK"));
    }

    @Test
    public void shouldAddANewURLToTheResourcesList() throws AddResourceException {
        Resource resource = new Resource("http://www.test.co.uk");
        HttpEntity<Resource> request = new HttpEntity<>(resource, httpHeaders);
        URI location = testRestTemplate.postForLocation("/api/monitor", request, Resource.class);
        assertThat(location, notNullValue());
        verify(resourceRepository, times(1)).add(resource);
    }

    @Test
    public void shouldFetchTheContentOfTheResource() {
        Resource resource = new Resource("http://www.test.co.uk");
        HttpEntity<Resource> request = new HttpEntity<>(resource, httpHeaders);
        URI location = testRestTemplate.postForLocation("/api/monitor", request, Resource.class);
        MockRestServiceServer mockServer = MockRestServiceServer.createServer(restTemplate);

        // Two call are expected, once at the moment of creation of resource, and when the resource is checked
        mockServer.expect(ExpectedCount.times(1), requestTo("http://www.test.co.uk"))
                .andRespond(withSuccess("FooBar!!", MediaType.TEXT_PLAIN));

        mockServer.verify();
    }
}