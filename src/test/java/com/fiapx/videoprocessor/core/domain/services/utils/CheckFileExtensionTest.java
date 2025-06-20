package com.fiapx.videoprocessor.core.domain.services.utils;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class CheckFileExtensionTest {

    @Test
    void executeTrueTest(){
        String fileName = "Arquivo.mp4";

        Assertions.assertEquals(true, CheckFileExtension.hasValidExtension(fileName));
    }

    @Test
    void executeFalseTest(){
        String fileName = "Arquivo.y";

        Assertions.assertEquals(false, CheckFileExtension.hasValidExtension(fileName));
    }
}
