package com.fiapx.core.domain.services.utils;
import com.fiapx.core.application.exceptions.CommandExecutorException;
import lombok.Getter;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;

@Getter
public class CommandExecutor {

    public static String executeCommand(List<String> args) {
        String output = "";

        try {
            ProcessBuilder builder = new ProcessBuilder(args); // Windows example
            Process process = builder.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

            String line;
            while ((line = reader.readLine()) != null) {
                output += "\n" + line;
            }

            process.waitFor();
        } catch (Exception e) {
            throw new CommandExecutorException("FFMPEG Error: " + output);
        }

        return output;
    }
}