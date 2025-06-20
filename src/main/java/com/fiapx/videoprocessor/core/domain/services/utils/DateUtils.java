package com.fiapx.videoprocessor.core.domain.services.utils;

import java.sql.Timestamp;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class DateUtils {

    public static String timestampToString(Timestamp timestamp){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS")
                .withZone(ZoneId.systemDefault());
        return formatter.format(timestamp.toInstant());
    }
}
