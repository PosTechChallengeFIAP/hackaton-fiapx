package com.fiapx.videoprocessor;

import com.fiapx.videoprocessor.adapters.driven.infra.storage.EStorageType;
import com.fiapx.videoprocessor.core.domain.services.utils.DirectoryManager;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.env.Environment;

@SpringBootApplication
public class FiapXApplication {

	public static void main(String[] args) {
		SpringApplication.run(FiapXApplication.class, args);
	}

}
