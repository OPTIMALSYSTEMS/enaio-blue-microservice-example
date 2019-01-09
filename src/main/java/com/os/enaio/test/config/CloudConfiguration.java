package com.os.enaio.test.config;

import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

/**
 * Created by cschulze on 18.11.2016.
 */
@Configuration
@Profile("cloud")
@EnableDiscoveryClient
public class CloudConfiguration {
}
