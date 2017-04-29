package com.oroblam;

import com.oroblam.model.Resource;
import com.oroblam.repository.ResourceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Component
public class MonitorService {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private ResourceRepository resourceRepository;

    @Autowired
    private WebComparatorService webComparatorService;

    public List<Resource> run() {
        List<Resource> resources = resourceRepository.getAll();
        List<Resource> updatedResource = new ArrayList<>();
        resources.forEach(resource -> {
            String updatedContent = restTemplate.getForObject(resource.getUrl(),String.class);
            if (webComparatorService.compare(resource.getContent(), updatedContent)){
                //Update detected
                resource.setContent(updatedContent);
                resourceRepository.update(resource);
                updatedResource.add(resource);
            }
        });
        return  updatedResource;
    }
}
