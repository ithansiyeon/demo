package com.example.demo.utils;

import lombok.extern.slf4j.Slf4j;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;

@Slf4j
public class CalendarUtil {

    private static final String DEFAULT_SIMPLE_FORMAT = "yyyyMMdd";
    private static final String DEFAULT_TIME_FORMAT = "HH:mm";
    private static final String DEFAULT_SIMPLE_TIME_HOUR_FORMAT = "yyyyMMddHH";
    private static final String DEFAULT_SIMPLE_TIME_MINUTE_FORMAT = "yyyyMMddHHmm";
    private static final String DEFAULT_SIMPLE_TIME_FORMAT = "yyyyMMddHHmmss";
    private static final String DEFAULT_FULL_FORMAT = "yyyy-MM-dd HH:mm:ss";
    private static final String DEFAULT_FORMAT = "yyyy-MM-dd";
    private static final String DEFAULT_YEAR_FORMAT = "yyyy";
    private static final String DEFAULT_MONTH_FORMAT = "MM";
    private static final String DEFAULT_FORMAT_DAY_WEEK = "yyyy-MM-dd(E)";
    private static final String LIMIT_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

    public static boolean isValidDate(String dateString) {
        try {
            new SimpleDateFormat(DEFAULT_FORMAT).parse(dateString);
            return true;
        } catch (ParseException e) {
            return false;
        }
    }

    public static boolean isValidTime(String dateString) {
        try {
            new SimpleDateFormat(DEFAULT_TIME_FORMAT).parse(dateString);
            return true;
        } catch (ParseException e) {
            return false;
        }
    }

    public static boolean isDateFormat(String value) throws Exception {
        if (StringUtil.isEmpty(value)) {
            return false;
        }
        SimpleDateFormat dateFormatParser = new SimpleDateFormat(DEFAULT_FORMAT, Locale.KOREA);
        dateFormatParser.setLenient(false);
        try {
            dateFormatParser.parse(value);
            return true;
        }catch ( ParseException pe) {
            throw pe;
        }catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static int getCurrentDateTime() {
        Calendar cal = Calendar.getInstance();
        return (int) cal.getTime().getTime();
    }

    public static String getCurrentDate() {
        return toDateformat(Calendar.getInstance(), DEFAULT_FORMAT);
    }

    public static String getCurrentSimpleDate() {
        return toDateformat(Calendar.getInstance(), DEFAULT_SIMPLE_FORMAT);
    }

    public static String getCurrentSimpletTimeDate() {
        return toDateformat(Calendar.getInstance(), DEFAULT_SIMPLE_TIME_FORMAT);
    }

    public static String getCurrentSimpletTimeMinuteDate() {
        return toDateformat(Calendar.getInstance(), DEFAULT_SIMPLE_TIME_MINUTE_FORMAT);
    }

    public static String getCurrentSimpletTimeHourDate() {
        return toDateformat(Calendar.getInstance(), DEFAULT_SIMPLE_TIME_HOUR_FORMAT);
    }

    public static String getCurrentFullFormat() {
        return toDateformat(Calendar.getInstance(), DEFAULT_FULL_FORMAT);
    }

    public static String getCurrentYear() {
        return CalendarUtil.toDateformat(Calendar.getInstance(), DEFAULT_YEAR_FORMAT);
    }

    public static String getCurrentMonth() {
        return CalendarUtil.toDateformat(Calendar.getInstance(), DEFAULT_MONTH_FORMAT);
    }

    public static String getAddMonthDate(int add) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, add);
        return toDateformat(calendar, DEFAULT_FORMAT);
    }

    public static String getNextYear(int year) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.add(Calendar.YEAR, +1);
        return toDateformat(calendar, DEFAULT_YEAR_FORMAT);
    }

    public static String getPreviousYear(int year) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.add(Calendar.YEAR, -1);
        return toDateformat(calendar, DEFAULT_YEAR_FORMAT);
    }

    public static String toDateformat(Calendar calendar, String format) {
        SimpleDateFormat formatter = new SimpleDateFormat(format);
        return formatter.format(calendar.getTime());
    }

    public static String toDateformat(Date date, String format) {
        if (date == null) {
            return null;
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return toDateformat(cal, format);
    }

    public static String toDefaultDateformat(Date date) {
        return toDateformat(date, DEFAULT_FORMAT);
    }

    public static String toDefaultTimeformat(Date date) {
        return toDateformat(date, DEFAULT_TIME_FORMAT);
    }

    public static String toDefaultFullformat(Date date) {
        return toDateformat(date, DEFAULT_FULL_FORMAT);
    }

    public static String toDefaultDayWeekformat(String s) {
        Calendar calendar = Calendar.getInstance();
        setCalendarDate(calendar, s);
        return toDateformat(calendar, DEFAULT_FORMAT_DAY_WEEK);
    }

    public static List<String> getDiffDateList(String start, String end) {
        List<String> list = new ArrayList<>();
        Calendar startCal = Calendar.getInstance();
        Calendar endCal = Calendar.getInstance();
        setCalendarDate(startCal, start);
        setCalendarDate(endCal, end);
        long diffDate = getDiffDate(startCal, endCal);
        for (int i = 0; i <= diffDate; i++) {
            list.add(toDateformat(startCal, DEFAULT_FORMAT));
            startCal.add(Calendar.DATE, +1);
        }
        return list;
    }

    public static void setCalendarDate(Calendar calendar, String date) {
        if (date.length() == 10) {
            calendar.set(Calendar.YEAR, Integer.parseInt(date.substring(0, 4)));
            calendar.set(Calendar.MONTH, Integer.parseInt(date.substring(5, 7)) - 1);
            calendar.set(Calendar.DATE, Integer.parseInt(date.substring(8, 10)));
        } else if (date.length() == 8) {
            calendar.set(Calendar.YEAR, Integer.parseInt(date.substring(0, 4)));
            calendar.set(Calendar.MONTH, Integer.parseInt(date.substring(4, 6)) - 1);
            calendar.set(Calendar.DATE, Integer.parseInt(date.substring(6, 8)));
        }
    }

    public static long getDiffDate(Calendar startCal, Calendar endCal) {
        long diff = endCal.getTimeInMillis() - startCal.getTimeInMillis();
        long diffDays = diff / (24 * 60 * 60 * 1000);
        return diffDays;
    }

    public static Date toDefaultDate(String date) throws Exception {
        try {
            return new SimpleDateFormat(DEFAULT_FORMAT).parse(date);
        }catch(ParseException pe) {
            throw pe;
        }catch (Exception e) {
            log.debug("toDefaultDate", e);
        }
        return null;
    }

    public static Date toSimpleDate(String date)throws Exception {
        try {
            return new SimpleDateFormat(DEFAULT_SIMPLE_FORMAT).parse(date);
        }catch(ParseException pe) {
            throw pe;
        }catch (Exception e) {
            log.debug("toSimpleDate", e);
        }
        return null;
    }

    /**
     * start 날짜가 end 날짜보다 이전 날짜인지 비교
     * @param start
     * @param end
     * @return
     */
    public static boolean isBefore(String start, String end) {
        if (start.equals(end)) {
            return true;
        }
        LocalDateTime startDateTime = LocalDateTime.of(Integer.parseInt(start.substring(0, 4)), Integer.parseInt(start.substring(4, 6)), Integer.parseInt(start.substring(6, 8)), 0, 0, 0);
        LocalDateTime endDateTime = LocalDateTime.of(Integer.parseInt(end.substring(0, 4)), Integer.parseInt(end.substring(4, 6)), Integer.parseInt(end.substring(6, 8)), 0, 0, 0);
        if (startDateTime.isBefore(endDateTime))
            return true;
        return false;
    }

    /**
     * start 날짜가 end 날짜보다 이후 날짜인지 비교
     * @param start
     * @param end
     * @return
     */
    public static boolean isAfter(String start, String end) {
        String[] starts = start.split("-");
        String[] ends = end.split("-");
        LocalDateTime startDateTime = LocalDateTime.of(Integer.parseInt(starts[0]), Integer.parseInt(starts[1]), Integer.parseInt(starts[2]), 0, 0, 0);
        LocalDateTime endDateTime = LocalDateTime.of(Integer.parseInt(ends[0]), Integer.parseInt(ends[1]), Integer.parseInt(ends[2]), 0, 0, 0);
        if (startDateTime.isAfter(endDateTime))
            return true;
        return false;
    }

    /**
     * date 날짜가 현재 날짜보다 이전 날짜인지 비교
     * @param date
     * @return
     */
    public static boolean isSimpleCurrentBefore(String date) {
        if(date != null) {
            Calendar endDate  = Calendar.getInstance() != null ? Calendar.getInstance() : null;
            return isBefore(date, CalendarUtil.toDateformat( endDate , DEFAULT_SIMPLE_FORMAT));
        }else {
            return false;
        }
    }

    /**
     * dateTime과 현재시간을 계산하여 초로 환산하여 리턴한다.
     * @param dateTime
     * @return
     */
    public static int calculateSecondByCurrentTime(long dateTime) {
        return (int) ((Calendar.getInstance().getTimeInMillis() - dateTime) / 1000);
    }

    /**
     * dateTime과 현재시간을 계산하여 일로 리턴한다.
     * @param dateTime
     * @return
     */
    public static int calculateDayByCurrentTime(long dateTime) {
        return calculateSecondByCurrentTime(dateTime) / (24 * 60 * 60);
    }

    /**
     * dateTime과 현재시간을 계산하여 일로 리턴한다.
     * @param time
     * @return
     */
    public static int calculateDayByCurrentTime(Date time) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(time);
        return calculateDayByCurrentTime(calendar.getTimeInMillis());
    }

    /**
     * limitDate가 현재시간기준으로 넘어가면 true
     * @param limitDate
     * @return
     */
    public static boolean isTimeLimit(String limitDate) throws Exception{
        Calendar calendar = CalendarUtil.getInstance(limitDate, LIMIT_DATE_FORMAT);
        int second = CalendarUtil.calculateSecondByCurrentTime(calendar.getTimeInMillis());
        return second >= 0;
    }

    public static Calendar getInstance(String date, String format) throws Exception {
        Calendar calendar = Calendar.getInstance();
        try {
            calendar.setTime(new SimpleDateFormat(format).parse(date));
        }catch(ParseException pe) {
            throw pe;
        }catch(Exception e) {
            e.printStackTrace();
        }
        return calendar;
    }

}
