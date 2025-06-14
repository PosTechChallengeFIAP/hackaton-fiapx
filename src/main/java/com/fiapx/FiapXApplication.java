package com.fiapx;

import com.fiapx.core.domain.services.utils.DirectoryManager;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.env.Environment;

@SpringBootApplication
public class FiapXApplication {

	public static void main(String[] args) {
		Environment env = SpringApplication.run(FiapXApplication.class, args).getEnvironment();
		DirectoryManager.createDir(env.getProperty("spring.application.upload-location"));
		DirectoryManager.createDir(env.getProperty("spring.application.output-location"));
	}

}
