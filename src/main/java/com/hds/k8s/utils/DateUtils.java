package com.hds.k8s.utils;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class DateUtils {

    public static String format(OffsetDateTime offsetDateTime) {
        if (Objects.isNull(offsetDateTime)) {
            return "";
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ssXXX");
        return offsetDateTime.format(formatter);
    }
}
