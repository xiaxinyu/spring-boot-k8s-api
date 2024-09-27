package com.hds.k8s.utils;

import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class MapUtils {

    public static String format(Map<String, String> map) {
        if (Objects.isNull(map)) {
            return "";
        }
        return map.entrySet().stream()
                .map(entry -> entry.getKey() + "=" + entry.getValue())
                .collect(Collectors.joining(","));
    }

}
