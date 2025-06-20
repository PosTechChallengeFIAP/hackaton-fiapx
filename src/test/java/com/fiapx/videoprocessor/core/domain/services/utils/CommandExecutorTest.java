package com.fiapx.videoprocessor.core.domain.services.utils;

import com.fiapx.videoprocessor.core.application.exceptions.CommandExecutorException;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
public class CommandExecutorTest {

    @Test
    void executeCommand_shouldReturnOutput_whenValidCommand() {
        String output = CommandExecutor.executeCommand(Arrays.asList("cmd", "/c", "echo", "hello"));

        assertTrue(output.contains("hello"));
    }

    @Test
    void executeCommand_shouldThrowException_whenInvalidCommand() {
        // Comando propositalmente inválido
        Exception exception = assertThrows(CommandExecutorException.class, () ->
                CommandExecutor.executeCommand(Arrays.asList("comando_que_nao_existe"))
        );

        // Verifica se a mensagem contém o prefixo padrão de erro
        assertTrue(exception.getMessage().contains("FFMPEG Error"));
    }
}
