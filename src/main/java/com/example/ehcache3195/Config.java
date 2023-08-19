package com.example.ehcache3195;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author Sergio Lissner
 * Date: 6/5/2023
 * Time: 10:04 AM
 */
@Configuration
@RequiredArgsConstructor
@EnableConfigurationProperties(Globals.class)
public class Config {

    private final Globals globals;

}
