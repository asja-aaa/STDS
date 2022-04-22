package com.asja.finaldesign.config;

import com.uber.h3core.H3Core;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

/**
 * @Description
 * @Author ASJA
 * @Create 2022-04-21 17:53
 */
@Configuration
public class BeanFactory {

    @Bean
    public H3Core getH3Core() throws IOException {
        return H3Core.newInstance();
    }
}
