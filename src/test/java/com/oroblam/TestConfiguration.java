package com.oroblam;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.subethamail.wiser.Wiser;

/**
 * Created by magnecch on 04/06/2017.
 */
@Configuration
public class TestConfiguration {

    @Bean
    public Wiser getWiser() {
        Wiser wiser = new Wiser();
        wiser.setPort(10000);
        wiser.setHostname("localhost");
        return wiser;
    }

}
