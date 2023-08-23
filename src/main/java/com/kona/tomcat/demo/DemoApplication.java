package com.kona.tomcat.demo;

import com.kona.tomcat.demo.config.AppConfig;
import com.tencent.kona.KonaProvider;
import lombok.val;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Security;

@SpringBootApplication
public class DemoApplication {

	static{
		Security.insertProviderAt(new KonaProvider(),1);
	}

	public static void main(String[] args) {

//		SpringApplication.run(DemoApplication.class, args);
		new SpringApplicationBuilder(AppConfig.class)
				.child(DemoApplication.class)
				.run(args);
	}

	@RestController
	public static class ResponseController {

		@GetMapping("/tomcat")
		public String response() {
			return "This is a testing server on Tencent Kona SM Suite";
		}
	}

}
