package utils;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.TimeZone;

public class TimeUtils {

    public static LocalDateTime timestampToLocalDate(Long timestamp) {
        return LocalDateTime.ofInstant(
                Instant.ofEpochMilli(timestamp),
                TimeZone.getDefault().toZoneId());
    }
}
