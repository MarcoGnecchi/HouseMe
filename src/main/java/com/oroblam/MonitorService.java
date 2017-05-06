package com.oroblam;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.oroblam.model.Resource;
import com.oroblam.repository.ResourceRepository;

@Component
public class MonitorService {

    public static final String HTTP = "http://";
    private Logger log = LoggerFactory.getLogger(MonitorService.class);

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private ResourceRepository resourceRepository;

    @Autowired
    private WebComparatorService webComparatorService;

    public List<Resource> run() {
        List<Resource> resources = resourceRepository.getAll();
        log.info("Found {} resources to check", resources.size());
        List<Resource> updatedResource = resources.stream()
                .filter(resource -> updateContent(resource))
                .collect(Collectors.toList());
        return  updatedResource;
    }

    /**
     * Check whether the resource has been updated, if so, it is persisted
     * @param resource
     * @return
     */
    private boolean updateContent(Resource resource) {
        // Validate URL
        if (!resource.getUrl().startsWith(HTTP)) {
            resource.setUrl(HTTP + resource.getUrl());
        }
        try {
            String updatedContent = restTemplate.getForObject(resource.getUrl(), String.class);
            if (webComparatorService.compare(resource.getContent(), updatedContent)) {
                // Update detected
                log.info("Update detected: {}", resource.getUrl());
                resource.setContent(updatedContent);
                resourceRepository.update(resource);
                return true;
            } else {
                log.info("Update NOT detected: {}", resource.getUrl());
            }
        } catch (RestClientException e) {
            log.error("Error while pulling new resource content: {}", resource.getUrl(), e);
        }
        return false;
    }
}
