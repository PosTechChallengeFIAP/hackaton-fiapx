package com.fiapx.videoprocessor.core.domain.services.utils;
import com.fiapx.videoprocessor.core.application.exceptions.CommandExecutorException;
import lombok.Getter;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;

@Getter
public class CommandExecutor {

    public static String executeCommand(List<String> args) {
        String output = "";

        try {
            Process process = Runtime.getRuntime().exec(joinArgs(args));
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

    private static String joinArgs(List<String> args){
        if(args.isEmpty()) return "";
        String joined = args.get(0);

        for(int i = 1;i < args.size();i++){
            joined += " " + args.get(i);
        }

        return joined;
    }
}