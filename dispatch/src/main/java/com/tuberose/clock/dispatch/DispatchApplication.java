package com.tuberose.clock.dispatch;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.env.Environment;

@SpringBootApplication
@Slf4j
@ComponentScan("com.tuberose.clock")
public class DispatchApplication {

    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(DispatchApplication.class);
        Environment env = application.run(args).getEnvironment();
        log.info("dispatch module is running.\thttp://127.0.0.1:{}", env.getProperty("server.port"));
    }

}
