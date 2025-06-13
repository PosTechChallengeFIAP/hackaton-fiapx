package com.fiapx;

import com.fiapx.core.domain.services.utils.DirectoryManager;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class FiapXApplication {

	public static void main(String[] args) {
		DirectoryManager.createDir("uploads");
		DirectoryManager.createDir("output");

		SpringApplication.run(FiapXApplication.class, args);
	}

}
