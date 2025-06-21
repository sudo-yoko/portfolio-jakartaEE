package com.example;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

/**
 * タイムゾーンのオフセットを含むISO8601形式の日付を扱うユーティリティクラス
 */
public class OffsetDateTimeUtils {
    /**
     * LocalDateTimeの値を、日本時間のISO8601形式の文字列に変換する
     */
    public static String toJapanIsoString(LocalDateTime dateTime) {
        return dateTime.truncatedTo(ChronoUnit.SECONDS) // 秒以下の精度を切り捨て
                .atZone(ZoneId.of("Asia/Tokyo"))
                .format(DateTimeFormatter.ISO_OFFSET_DATE_TIME);
    }
}
