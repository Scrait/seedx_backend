package ru.scrait.seedx.utils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DateUtil {
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public static int[] formatDate(LocalDate date) {
        return date != null ? new int[]{ date.getYear(), date.getMonth().getValue(), date.getDayOfMonth() } : null;
    }
}
