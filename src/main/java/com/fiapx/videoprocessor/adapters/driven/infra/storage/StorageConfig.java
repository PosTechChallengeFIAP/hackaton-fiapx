package com.fiapx.videoprocessor.adapters.driven.infra.storage;

import com.fiapx.videoprocessor.core.domain.services.usecases.CreateInitialDirectoriesUseCase.ICreateInitialDirectoriesUseCase;
import com.fiapx.videoprocessor.core.domain.services.usecases.GetFileUseCase.IGetFileUseCase;
import com.fiapx.videoprocessor.core.domain.services.usecases.SaveFileUseCase.ISaveFileUseCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class StorageConfig {
    @Autowired
    @Qualifier("localSaveFileUseCase")
    private ISaveFileUseCase localSaveFileUseCase;

    @Autowired
    @Qualifier("awsSaveFileUseCase")
    private ISaveFileUseCase awsSaveFileUseCase;

    @Autowired
    @Qualifier("localGetFileUseCase")
    private IGetFileUseCase localGetFileUseCase;

    @Autowired
    @Qualifier("awsGetFileUseCase")
    private IGetFileUseCase awsGetFileUseCase;

    @Autowired
    @Qualifier("awsCreateInitialDirectoriesUseCase")
    private ICreateInitialDirectoriesUseCase awsCreateInitialDirectoriesUseCase;

    @Autowired
    @Qualifier("localCreateInitialDirectoriesUseCase")
    private ICreateInitialDirectoriesUseCase localCreateInitialDirectoriesUseCase;

    @Value("${spring.application.storage}")
    private String storageType;

    @Bean
    @Primary
    public ISaveFileUseCase saveFileUseCase() {
        return switch (EStorageType.fromString(storageType)) {
            case LOCAL -> localSaveFileUseCase;
            case AWS -> awsSaveFileUseCase;
        };
    }

    @Bean
    @Primary
    public IGetFileUseCase getFileUseCase() {
        return switch (EStorageType.fromString(storageType)) {
            case LOCAL -> localGetFileUseCase;
            case AWS -> awsGetFileUseCase;
        };
    }

    @Bean
    @Primary
    public ICreateInitialDirectoriesUseCase createInitialDirectoriesUseCase() {
        return switch (EStorageType.fromString(storageType)) {
            case LOCAL -> localCreateInitialDirectoriesUseCase;
            case AWS -> awsCreateInitialDirectoriesUseCase;
        };
    }
}
