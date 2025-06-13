package com.fiapx.core.domain.services.utils;

public class CheckFileExtension {

    public static boolean hasValidExtension(String fileName){
        String[] validExtensions = {".mp4", ".avi", ".mov", ".mkv", ".wmv", ".flv", ".webm"};
        for(String extension : validExtensions){
            if(fileName.endsWith(extension)) return true;
        }
        return false;
    }
}
