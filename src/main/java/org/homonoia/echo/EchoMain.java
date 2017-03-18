package org.homonoia.echo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Copyright (c) 2015-2016 Homonoia Studios.
 *
 * @author alexparlett
 * @since 16/03/2017
 */
@SpringBootApplication
@EnableScheduling
@EnableAsync
public class EchoMain {
    public static void main(String[] args) {
        SpringApplication.run(EchoMain.class, args);
    }
}
