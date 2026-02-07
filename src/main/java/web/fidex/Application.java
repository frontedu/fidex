package web.fidex;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ImportRuntimeHints;

import web.fidex.config.NativeRuntimeHints;

@SpringBootApplication
@EnableCaching
@ImportRuntimeHints(NativeRuntimeHints.class)
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

}
