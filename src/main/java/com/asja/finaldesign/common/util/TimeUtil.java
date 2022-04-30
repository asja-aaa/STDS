package com.asja.finaldesign.common.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.format.datetime.DateFormatter;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * @Description
 * @Author ASJA
 * @Create 2022-04-29 14:27
 */

@Slf4j
public class TimeUtil {

    public static final String TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

    public static final long TIME_2014_10_1_00_00_00 = 1412121600000L;

    public static final long TIME_30_min = 1800000L;

    public static final SimpleDateFormat SIMPLE_DATE_FORMAT = new SimpleDateFormat(TIME_FORMAT);

    public static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern(TIME_FORMAT);



    public static LocalDateTime timeStrToLocalDateTime(String timeStr){
        return LocalDateTime.parse(timeStr,DATE_TIME_FORMATTER);
    }
}
