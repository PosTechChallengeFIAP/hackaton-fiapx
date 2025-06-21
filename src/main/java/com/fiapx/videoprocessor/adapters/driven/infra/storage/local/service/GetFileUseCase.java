package com.fiapx.videoprocessor.adapters.driven.infra.storage.local.service;

import com.fiapx.videoprocessor.core.domain.services.usecases.GetFileUseCase.IGetFileUseCase;
import org.springframework.stereotype.Service;

import java.io.File;
import java.nio.file.Paths;

@Service("localGetFileUseCase")
public class GetFileUseCase implements IGetFileUseCase {
    @Override
    public File execute(String location, String fileName) {
        return new File(Paths.get(location, fileName).toString());
    }
}
