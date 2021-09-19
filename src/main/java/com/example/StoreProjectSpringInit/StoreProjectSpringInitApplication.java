package com.example.StoreProjectSpringInit;

import com.example.StoreProjectSpringInit.core.Store;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import javax.xml.crypto.Data;
import java.io.IOException;
import java.util.Date;

@SpringBootApplication
public class StoreProjectSpringInitApplication {
	public static void main(String[] args) {
		SpringApplication.run(StoreProjectSpringInitApplication.class, args);
	}

}

