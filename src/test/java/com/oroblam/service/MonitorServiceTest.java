package com.oroblam.service;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

import com.oroblam.service.MonitorService;
import com.oroblam.service.WebComparatorService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.client.ExpectedCount;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

import com.oroblam.model.Resource;
import com.oroblam.repository.ResourceRepository;

import java.util.Arrays;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MonitorServiceTest {

    private static final String NEW_BODY_CONTENT = "new body content";
    private static final String OLD_BODY_CONTENT = "old body content";
    private static final String TEST_URL = "http://www.test.co.uk";

    @MockBean
    private WebComparatorService webComparatorService;
    @MockBean
    private ResourceRepository resourceRepository;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private MonitorService monitorService;

    private MockRestServiceServer server;

    @Before
    public void setUp(){
        // Set up the resource
        Resource resourceToCheck = new Resource();
        resourceToCheck.setUrl(TEST_URL);
        resourceToCheck.setContent(OLD_BODY_CONTENT);
        when(resourceRepository.getAll()).thenReturn(Arrays.asList(resourceToCheck));

        // Set up mock server to respond with new content
        server = MockRestServiceServer.bindTo(restTemplate).build();
        server.expect(ExpectedCount.once(), requestTo(resourceToCheck.getUrl())).andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess(NEW_BODY_CONTENT, MediaType.TEXT_HTML));

    }

    @Test
    public void shouldCompareAResourceWithNewVersion() {
        monitorService.run();
        verify(webComparatorService, times(1)).isUpdated(OLD_BODY_CONTENT, NEW_BODY_CONTENT);
    }

    @Test
    public void shouldSaveTheNewVersionWhenThereAreChanges() {
        ArgumentCaptor<Resource> argument = ArgumentCaptor.forClass(Resource.class);
        when(webComparatorService.isUpdated(OLD_BODY_CONTENT, NEW_BODY_CONTENT)).thenReturn(true);
        monitorService.run();
        verify(resourceRepository).update(argument.capture());
        assertThat(argument.getValue().getContent(), is(equalTo(NEW_BODY_CONTENT)));
    }

    @Test
    public void shouldHandleMultipleResources() {
        server.expect(ExpectedCount.times(2), requestTo(TEST_URL)).andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess(NEW_BODY_CONTENT, MediaType.TEXT_HTML));
        Resource resource1 = new Resource(TEST_URL);
        Resource resource2 = new Resource(TEST_URL);
        List<Resource> resources = Arrays.asList(resource1,resource2);
        when(resourceRepository.getAll()).thenReturn(resources);
        monitorService.run();
        verify(webComparatorService, atLeast(2)).isUpdated(any(),any());
    }

    @Test
    public void shouldReturnUpdatedResources(){
        when(webComparatorService.isUpdated(OLD_BODY_CONTENT, NEW_BODY_CONTENT)).thenReturn(true);
        List<Resource> updatedResources = monitorService.run();
        assertThat(updatedResources.size(), is(1));
        assertThat(updatedResources.get(0).getContent(), is(equalTo(NEW_BODY_CONTENT)));
    }


}
