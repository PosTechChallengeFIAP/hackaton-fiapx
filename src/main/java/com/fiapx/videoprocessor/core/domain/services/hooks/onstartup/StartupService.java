package com.fiapx.videoprocessor.core.domain.services.hooks.onstartup;

import com.fiapx.videoprocessor.core.domain.services.usecases.CreateInitialDirectoriesUseCase.ICreateInitialDirectoriesUseCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

@Service
public class StartupService {

    @Autowired
    private ICreateInitialDirectoriesUseCase createInitialDirectoriesUseCase;

    @EventListener(ApplicationReadyEvent.class)
    public void onApplicationReady() {
        createInitialDirectoriesUseCase.execute();
    }
}