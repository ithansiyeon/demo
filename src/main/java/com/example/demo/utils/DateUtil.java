package com.example.demo.utils;

import lombok.extern.slf4j.Slf4j;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

@Slf4j
public class DateUtil {

    /*****************************************************
     * 날짜 데이타 구분자에 맞는 날짜 형식으로 변환
     * @param delimiter 날짜 토큰
     * @param rtnFormmat 날짜 포맷 구분자
     * @return String 변환 날짜 데이타
     *****************************************************/
    public static String getCurrentDate(String delimiter, String rtnFormmat) {

        // 응답받을 출력 시간
        String szReturn = "";

        try {
            // 캘린더 인스턴스
            Calendar currentWhat = Calendar.getInstance();

            // 현재 년도
            int currentYear = currentWhat.get(Calendar.YEAR);
            // 현재 월
            int currentMonth = currentWhat.get(Calendar.MONTH) + 1;
            // 현재 일
            int currentDay = currentWhat.get(Calendar.DAY_OF_MONTH);
            // 현재 시간(시)
            int currentHour = currentWhat.get(Calendar.HOUR_OF_DAY);
            // 현재 시간(분)
            int currentMinute = currentWhat.get(Calendar.MINUTE);
            // 현재 시간(초)
            int currentSecond = currentWhat.get(Calendar.SECOND);
            // 현재 시간(밀리세컨드)
            int currentMilecond = currentWhat.get(Calendar.MILLISECOND);

            // 현재 년도 4자리 스트링으로 변환
            String yearToday = StringUtil.padLeftwithZero(currentYear, 4);
            // 현재 월 2자리 스트링으로 변환
            String monthToday = StringUtil.padLeftwithZero(currentMonth, 2);
            // 현재 일 2자리 스트링으로 변환
            String dayToday = StringUtil.padLeftwithZero(currentDay, 2);
            // 현재 시간(시) 2자리 스트링으로 변환
            String hourToday = StringUtil.padLeftwithZero(currentHour, 2);
            // 현재 시간(분) 2자리 스트링으로 변환
            String minuteToday = StringUtil.padLeftwithZero(currentMinute, 2);
            // 현재 시간(초) 2자리 스트링으로 변환
            String secondToday = StringUtil.padLeftwithZero(currentSecond, 2);
            // 현재 시간(밀리세컨드) 3자리 스트링으로 변환
            String milecondToday = StringUtil.padLeftwithZero(currentMilecond, 3);

            if (rtnFormmat.equals("YYYY/MM/DD HH:MI:SS")) {
                szReturn = yearToday + "/" + monthToday + "/" + dayToday
                        + " " + hourToday + ":" + minuteToday + ":" + secondToday;
            }else if (rtnFormmat.equals("YYYY.MM.DD HH:MI:SS")) {
                szReturn = yearToday + "." + monthToday + "." + dayToday
                        + " " + hourToday + ":" + minuteToday + ":" + secondToday;
            } else if (rtnFormmat.equals("YYYY-MM-DD")) {
                szReturn = yearToday + "-" + monthToday + "-" + dayToday;
            } else if (rtnFormmat.equals("YYYYMMDD-HHMISS")) {
                szReturn = yearToday + monthToday + dayToday
                        + "-" + hourToday + minuteToday + secondToday;
            } else if (rtnFormmat.equals("YYYYMMDDHHMISS")) {
                szReturn = yearToday + monthToday + dayToday
                        + hourToday + minuteToday + secondToday;
            } else if (rtnFormmat.equals("YYYYMMDDHHMISSMS")) {
                szReturn = yearToday + monthToday + dayToday
                        + hourToday + minuteToday + secondToday + milecondToday.substring(0,2);
            } else if (rtnFormmat.equals("YYYYMMDD")) {
                szReturn = yearToday + monthToday + dayToday;
            } else if (rtnFormmat.equals("HH:MI:SS")) {
                szReturn = hourToday + ":" + minuteToday + ":" + secondToday;
            } else if (rtnFormmat.equals("HHMISS")) {
                szReturn = hourToday + minuteToday + secondToday;
            } else if (rtnFormmat.equals("YYYY")) {
                szReturn = yearToday;
            } else if (rtnFormmat.equals("MM")) {
                szReturn = monthToday;
            } else if (rtnFormmat.equals("DD")) {
                szReturn = dayToday;
            } else if (rtnFormmat.equals("HHMI")) {
                szReturn = hourToday + minuteToday;
            } else if (rtnFormmat.equals("HH:MI")) {
                szReturn = hourToday + ":" + minuteToday;
            } else if (rtnFormmat.equals("MS")) {
                szReturn = milecondToday.substring(0,2);
            } else if (rtnFormmat.equals("dafault")) {
                szReturn = yearToday + delimiter + monthToday + delimiter + dayToday;
            } else {
                szReturn = yearToday + delimiter + monthToday + delimiter + dayToday;
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }

        return szReturn;
    }


    /*****************************************************
     * 특정일자가 시작일자와 종료일자 사이에 있는지 반환
     * @param standardDate 기준날짜
     * @param compareDate 비교날짜
     * @return int  standardDate = compareDate (0 리턴)
     * 				standardDate > compareDate (0보다 큰값)
     * 				standardDate < compareDate (0보다 작은값)
     *****************************************************/
    public static boolean getDateCompare(String startDate, String endDate, String compareDate) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        boolean compare = false;
        try {
            Date day1 = dateFormat.parse(startDate);
            Date day2 = dateFormat.parse(endDate);
            Date day3 = dateFormat.parse(compareDate);

            compare = day3.after(day1) && day3.before(day2);
        } catch (ParseException e) {
            log.error(e.getMessage());
        }

        return compare;
    }
}