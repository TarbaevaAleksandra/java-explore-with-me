package ru.practicum;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"ru.practicum"})
public class StatsServerApp
{
    public static void main( String[] args )
    {
        SpringApplication.run(StatsServerApp.class, args);
    }
}
