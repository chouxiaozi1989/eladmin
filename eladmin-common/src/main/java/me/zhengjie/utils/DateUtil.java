/*
 * Copyright 2019-2020 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package me.zhengjie.utils;

import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.ISODateTimeFormat;
import org.joda.time.tz.FixedDateTimeZone;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;

/**
 * @author: liaojinlong
 * @date: 2020/6/11 16:28
 * @apiNote: JDK 8  新日期类 格式化与字符串转换 工具类
 */
public class DateUtil {

    public static final DateTimeFormatter DFY_MD_HMS = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    public static final DateTimeFormatter DFY_MD = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    /**
     * LocalDateTime 转时间戳
     *
     * @param localDateTime /
     * @return /
     */
    public static Long getTimeStamp(LocalDateTime localDateTime) {
        return localDateTime.atZone(ZoneId.systemDefault()).toEpochSecond();
    }

    /**
     * 时间戳转LocalDateTime
     *
     * @param timeStamp /
     * @return /
     */
    public static LocalDateTime fromTimeStamp(Long timeStamp) {
        return LocalDateTime.ofEpochSecond(timeStamp, 0, OffsetDateTime.now().getOffset());
    }

    /**
     * LocalDateTime 转 Date
     * Jdk8 后 不推荐使用 {@link Date} Date
     *
     * @param localDateTime /
     * @return /
     */
    public static Date toDate(LocalDateTime localDateTime) {
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }

    /**
     * LocalDate 转 Date
     * Jdk8 后 不推荐使用 {@link Date} Date
     *
     * @param localDate /
     * @return /
     */
    public static Date toDate(LocalDate localDate) {
        return toDate(localDate.atTime(LocalTime.now(ZoneId.systemDefault())));
    }


    /**
     * Date转 LocalDateTime
     * Jdk8 后 不推荐使用 {@link Date} Date
     *
     * @param date /
     * @return /
     */
    public static LocalDateTime toLocalDateTime(Date date) {
        return LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
    }

    /**
     * 日期 格式化
     *
     * @param localDateTime /
     * @param patten /
     * @return /
     */
    public static String localDateTimeFormat(LocalDateTime localDateTime, String patten) {
        DateTimeFormatter df = DateTimeFormatter.ofPattern(patten);
        return df.format(localDateTime);
    }

    /**
     * 日期 格式化
     *
     * @param localDateTime /
     * @param df /
     * @return /
     */
    public static String localDateTimeFormat(LocalDateTime localDateTime, DateTimeFormatter df) {
        return df.format(localDateTime);
    }

    /**
     * 日期格式化 yyyy-MM-dd HH:mm:ss
     *
     * @param localDateTime /
     * @return /
     */
    public static String localDateTimeFormatyMdHms(LocalDateTime localDateTime) {
        return DFY_MD_HMS.format(localDateTime);
    }

    /**
     * 日期格式化 yyyy-MM-dd
     *
     * @param localDateTime /
     * @return /
     */
    public String localDateTimeFormatyMd(LocalDateTime localDateTime) {
        return DFY_MD.format(localDateTime);
    }

    /**
     * 字符串转 LocalDateTime ，字符串格式 yyyy-MM-dd
     *
     * @param localDateTime /
     * @return /
     */
    public static LocalDateTime parseLocalDateTimeFormat(String localDateTime, String pattern) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(pattern);
        return LocalDateTime.from(dateTimeFormatter.parse(localDateTime));
    }

    /**
     * 字符串转 LocalDateTime ，字符串格式 yyyy-MM-dd
     *
     * @param localDateTime /
     * @return /
     */
    public static LocalDateTime parseLocalDateTimeFormat(String localDateTime, DateTimeFormatter dateTimeFormatter) {
        return LocalDateTime.from(dateTimeFormatter.parse(localDateTime));
    }

    /**
     * 字符串转 LocalDateTime ，字符串格式 yyyy-MM-dd HH:mm:ss
     *
     * @param localDateTime /
     * @return /
     */
    public static LocalDateTime parseLocalDateTimeFormatyMdHms(String localDateTime) {
        return LocalDateTime.from(DFY_MD_HMS.parse(localDateTime));
    }

    private static final DateTimeZone GMT = new FixedDateTimeZone("GMT", "GMT", 0, 0);
    private static final long MILLI_SECONDS_OF_365_DAYS = 365L*24*60*60*1000;
    /** RFC 822 format */
    protected static final org.joda.time.format.DateTimeFormatter rfc822DateFormat = DateTimeFormat.forPattern("EEE, dd MMM yyyy HH:mm:ss 'GMT'").withLocale(Locale.US).withZone(GMT);

    /** ISO 8601 format */
    protected static final org.joda.time.format.DateTimeFormatter iso8601DateFormat = ISODateTimeFormat.dateTime().withZone(GMT);

    /** Alternate ISO 8601 format without fractional seconds */
    protected static final org.joda.time.format.DateTimeFormatter alternateIso8601DateFormat = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss'Z'").withZone(GMT);

    /**
     * Formats the specified date as an RFC 822 string.
     *
     * @param date The date to format.
     *
     * @return The RFC 822 string representing the specified date.
     */
    public static String formatRFC822Date(Date date) {
        return rfc822DateFormat.print(date.getTime());
    }

    /**
     * Parses the specified date string as an RFC 822 date and returns the Date object.
     *
     * @param dateString The date string to parse.
     *
     * @return The parsed Date object.
     */
    public static Date parseRFC822Date(String dateString) {
        return new Date(rfc822DateFormat.parseMillis(dateString));
    }

    /**
     * Formats the specified date as an ISO 8601 string.
     *
     * @param date The date to format.
     *
     * @return The ISO 8601 string representing the specified date.
     */
    public static String formatISO8601Date(Date date) {
        return iso8601DateFormat.print(date.getTime());
    }

    /**
     * Parses the specified date string as an ISO 8601 date and returns the Date
     * object.
     *
     * @param dateString
     *            The date string to parse.
     *
     * @return The parsed Date object.
     */
    public static Date parseISO8601Date(String dateString) {

        // For EC2 Spot Fleet.
        if (dateString.endsWith("+0000")) {
            dateString = dateString
                    .substring(0, dateString.length() - 5)
                    .concat("Z");
        }

        String temp = tempDateStringForJodaTime(dateString);
        try {
            if (temp.equals(dateString)) {
                // Normal case: nothing special here
                return new Date(iso8601DateFormat.parseMillis(dateString));
            }
            // Handling edge case:
            // Joda-time can only handle up to year 292278993 but we are given
            // 292278994;  So we parse the date string by first adjusting
            // the year to 292278993. Then we add 1 year back afterwards.
            final long milliLess365Days = iso8601DateFormat.parseMillis(temp);
            final long milli = milliLess365Days + MILLI_SECONDS_OF_365_DAYS;
            if (milli < 0) { // overflow!
                // re-parse the original date string using JodaTime so as to
                // throw  an exception with a consistent message
                return new Date(iso8601DateFormat.parseMillis(dateString));
            }
            return new Date(milli);
        } catch (IllegalArgumentException e) {
            try {
                return new Date(alternateIso8601DateFormat.parseMillis(dateString));
                // If the first ISO 8601 parser didn't work, try the alternate
                // version which doesn't include fractional seconds
            } catch(Exception oops) {
                // no the alternative route doesn't work; let's bubble up the original exception
                throw e;
            }
        }
    }

    /**
     * Returns a date string with the prefix temporarily substituted, if
     * applicable, so that JodaTime can handle it.  Otherwise, if not applicable,
     * the original date string is returned.
     */
    private static String tempDateStringForJodaTime(String dateString) {
        final String fromPrefix = "292278994-";
        final String toPrefix   = "292278993-";
        return dateString.startsWith(fromPrefix)
                ? toPrefix + dateString.substring(fromPrefix.length())
                : dateString;
    }
}
