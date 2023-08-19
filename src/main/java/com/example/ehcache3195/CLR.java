package com.example.ehcache3195;

import jakarta.annotation.PostConstruct;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

/**
 * @author Moritz Halbritter
 */
@Component
class CLR {
    private final Globals properties;
    private final ApplicationContext context;

    CLR(Globals properties, ApplicationContext context) {
        this.properties = properties;
        this.context = context;
    }

    @PostConstruct
    public void init() {
        System.out.printf("CLR, properties.getHome(): %s\n", this.properties.getHome());
        System.out.printf("CLR, MH_HOME: %s\n", System.getenv("MH_HOME"));
        System.out.printf("CLR, properties.dispatcher.enabled: %s\n", properties.dispatcher.enabled);
    }

/*
    @Override
    public void run(String... args) throws Exception {
        System.out.println(this.properties.getHome());
        System.out.println(System.getenv("MH_HOME"));
        System.exit(SpringApplication.exit(context, ()->0));
    }
*/
}
