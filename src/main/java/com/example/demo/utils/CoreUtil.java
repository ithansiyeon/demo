package com.example.demo.utils;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.SecureRandom;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
public class CoreUtil {
    /** Random 객체 */
    private static SecureRandom random =  new SecureRandom();

    /** AGENT 명 */
    private static String userAgent = "User-Agent";

    /*****************************************************
     * 배열 의 해당값 포함 여부
     * @param array 체크할 문자배열
     * @param value 체크 값
     * @return boolean true/false
     *****************************************************/
    public static boolean arrayContains(Object[] array, Object value) {
        // 문자열 포함 여부
        boolean isContains = false;

        if (isArray(array)) {
            for (Object obj : array) {
                isContains = obj.equals(value);

                if (isContains) {
                    break;
                }
            }
        }

        return isContains;
    }

    /*****************************************************
     * DB에 들어간 Html 문자열을 Html Tag 형태로 변환
     * @param str 변환 할 대상 문자열
     * @return String 변환 완료된 문자열
     *****************************************************/
    public static String convertDBToHtml( String str ) {
        // 변환 문자열
        String strTemp = "";

        if( str == null ) {
            str = "";
        }

        strTemp = str.replaceAll( "&lt;", "<" );
        strTemp = strTemp.replaceAll( "&gt;", ">" );
        strTemp = strTemp.replaceAll( "&nbsp;", " " );
        strTemp = strTemp.replaceAll( "<p> </p>", "<p>&nbsp;</p>" );
        strTemp = strTemp.replaceAll( "<div> </div>", "<div>&nbsp;</div>" );
        strTemp = strTemp.replaceAll( "&amp;", "&" );
        strTemp = strTemp.replaceAll( "&quot;", "\"" );
        strTemp = strTemp.replaceAll( "&#34;", "\"" );
        strTemp = strTemp.replaceAll( "&#39;", "\'" );
        strTemp = strTemp.replaceAll( "&#37;", "%" );

        // 스크립트 정규식
        Pattern scripts = Pattern.compile("<script([^'\"]|\"[^\"]*\"|'[^']*')*?</script>",Pattern.DOTALL);
        // 스타일 정규식
        Pattern style = Pattern.compile("<style[^>]*>.*</style>",Pattern.DOTALL);

        // Matcher 객체
        Matcher m;

        m = scripts.matcher(strTemp);
        strTemp = m.replaceAll("");

        m = style.matcher(strTemp);
        strTemp = m.replaceAll("");

        return strTemp;
    }

    /*****************************************************
     * 태그 및 엔터 제거 전 객체 여부 체크
     * @param obj 변환 할 객체
     * @return String 변환 완료된 문자열
     *****************************************************/
    public static String convertHtmlTags(Object obj) {
        if ( obj == null ) {
            return "";
        }

        return convertHtmlTags(obj.toString());
    }

    /*****************************************************
     * 태그 및 엔터 제거 전 객체 여부 체크
     * @param str 변환 할 문자열
     * @return String 변환 완료된 문자열
     *****************************************************/
    public static String convertHtmlTags(String str) {
        str = str.replaceAll("<[^>]*>", "");	// 정규식 태그삭제
        str = str.replaceAll("\r\n", " ");	  // 엔터제거

        return str;
    }

    /*****************************************************
     * double을 원하는 형태의 포맷으로 변환(String)으로 반환 i == 1 : 소숫점이하 한자리 i == 2 : 컴마로 구분된 숫자 i == 3 : 붙어있는 숫자
     * @param d double 데이타
     * @param i 구분 숫자
     * @return String 변환 완료된 문자열
     *****************************************************/
    public static String doubleToStr(double d, int i) {
        // DecimalFormat 객체
        DecimalFormat decimalformat = null;
        // 변환 문자열
        String s = "";

        if (i == 1) {
            decimalformat = new DecimalFormat("######0.#");
        } else if (i == 2) {
            decimalformat = new DecimalFormat("#,###,##0");
        } else if (i == 3) {
            decimalformat = new DecimalFormat("######0");
        } else {
            decimalformat = new DecimalFormat("#,###,##0.#####");
        }

        s = decimalformat.format(d);
        return s;
    }

    /*****************************************************
     * 원화 표시
     * @param strVal 변환 할 문자열
     * @return String 변환 완료된 문자열
     *****************************************************/
    public static String getComma(String strVal) {
        strVal = StringUtil.nvl(strVal, "0");

        DecimalFormat formatter = new DecimalFormat("#,##0");

        return formatter.format(Integer.parseInt(strVal));
    }

    /*****************************************************
     * 문자열 디코딩
     * @param strIn 변환 할 문자열
     * @return String 변환 완료된 문자열
     *****************************************************/
    public static String setDSDecode(String strIn) {
        // 변환 문자열
        StringBuilder retStr = new StringBuilder();
        for (int i = 0; i < (strIn.length()); i++) {
            retStr.append((char) ((strIn.charAt(i)) - (i % 2) - 1));
        }

        if (retStr.toString().length() < 2 || !retStr.toString().substring(retStr.toString().length() - 6).equals("PASSWD")) {
            return "";
        } else {
            return retStr.toString().substring(0, retStr.toString().length() - 6);

        }
    }

    /*****************************************************
     * 문자열 인코딩
     * @param strIn 변환 할 문자열
     * @return String 변환 완료된 문자열
     *****************************************************/
    public static String setDSEncode(String strIn) {
        strIn = strIn + "PASSWD";

        // 변환 문자열
        StringBuilder retStr = new StringBuilder();

        for (int i = 0; i < strIn.length(); i++) {
            retStr.append((char) (strIn.charAt(i) + (i % 2) + 1));
        }

        return retStr.toString();
    }

    /*****************************************************
     * 쿠키 정보 가지고 오기
     * @param request Request 객체
     * @param cookieName 쿠키명
     * @return String 쿠키 정보
     *****************************************************/
    public static String getCookieObject(HttpServletRequest request, String cookieName) throws UnsupportedEncodingException {
        // 쿠키 목록
        Cookie[] cookies = request.getCookies();

        if (cookies == null)
            return null;

        // 쿠키정보 문자열
        String value = null;

        for (int i = 0; i < cookies.length; i++) {
            if (cookieName.equals(cookies[i].getName())) {
                value = cookies[i].getValue();
                if ("".equals(value))
                    value = null;
                break;
            }
        }

        return (value == null ? null : URLDecoder.decode(value, "euc-kr"));
    }

    /*****************************************************
     * 쿠키 정보 설정
     * @param response Response 객체
     * @param name 쿠키명
     * @param value 쿠키값
     * @param iMinute 쿠키저장시간
     *****************************************************/
    public static void setCookieObject(HttpServletResponse response, String name, String value, int iMinute)
            throws UnsupportedEncodingException {
        // Cookie 객체
        Cookie cookie = new Cookie(name, URLEncoder.encode(value, "euc-kr"));

        cookie.setMaxAge(60 * iMinute);
        cookie.setPath("/");
        cookie.setSecure(true);
        response.addCookie(cookie);
    }

    /*****************************************************
     * 파일명에서 확장자 축출
     * @param strFileName 파일명
     * @return String 파일확장자
     *****************************************************/
    public static String getFileExt(String strFileName) {
        if (strFileName != null && !"".equals(strFileName)) {
            return strFileName.substring(strFileName.lastIndexOf('.') + 1, strFileName.length());
        } else {
            return "";
        }
    }

    /*****************************************************
     * 파일사이즈 정보로 변환
     * @param obj 변환 할 객체
     * @return String 변환 완료된 문자열
     *****************************************************/
    public static String getFileSize(Object obj) {
        // 변환 문자
        String strVal = "";

        if ( obj == null ) {
            return "";
        }

        // 파일사이즈
        Long nSize = Long.parseLong(obj.toString());

        nSize /= 1000; // Byte를 KB로 환산

        if (nSize < 1)
            nSize = 1L;

        if ( nSize < 1000 ) {
            strVal = String.format("%dKB", nSize);
        } else {
            strVal = String.format("%.2fMB", nSize/1000.0);
        }

        return strVal;
    }

    /*****************************************************
     * 랜덤숫자 축출
     * @param limit 랜덤숫자 제한 자릿수
     * @return int 변환 완료된 랜덤숫자
     *****************************************************/
    public static int getRandomInt(int limit) {
        // 랜덤 숫자
        int number = random.nextInt();
        number = (number >>> 16) & 0xffff;
        number /= (0xffff / limit);

        return number;
    }

    /*****************************************************
     * 고유번호 UUID 조회
     * @return String 고유번호 UUID
     *****************************************************/
    public static String getUniqueId() {
        return UUID.randomUUID().toString();
    }

    /*****************************************************
     * 난수 조회
     * @return String 난수
     *****************************************************/
    public static String getUniqueValue() {
        // 난수
        String strVal="";

        strVal = DateUtil.getCurrentDate("", "YYYYMMDDHHMISS");
        Random rand =  new SecureRandom();
        strVal += Long.toString(rand.nextLong());

        return strVal;
    }

    /*****************************************************
     * 배열 여부 검증
     * @param obj 배열 객체
     * @return boolean true/false
     *****************************************************/
    public static boolean isArray(Object obj) {
        return (obj != null && obj.getClass().isArray());
    }

    /*****************************************************
     * 객체 비워있는지 여부 검증
     * @param obj 객체
     * @return boolean true/false
     *****************************************************/
    @SuppressWarnings("rawtypes")
    public static boolean isEmpty(Object obj) {
        if (obj == null) {
            return true;
        } else if (obj instanceof Map) {
            return ((Map) obj).isEmpty();
        } else if (obj instanceof Collection) {
            return ((Collection) obj).isEmpty();
        } else if (obj.getClass().isArray()) {
            return (Array.getLength(obj) == 0);
        } else if (obj instanceof String) {
            return (((String) obj).trim().length() == 0);
        } else if (obj instanceof File) {
            return (!((File) obj).exists());
        }

        return false;
    }

    public static boolean isNotEmpty(Object obj) {
        return !isEmpty(obj);
    }

    /*****************************************************
     * 배열이 비거나 배열 안의 모든 값이 비었는지를 검증
     * @param objs 배열 객체
     * @return boolean true/false
     *****************************************************/
    public static boolean isEmptyAll(Object... objs) {
        if (isNotEmpty(objs)) {
            for (Object obj : objs) {
                if (isNotEmpty(obj)) return false;
            }
        }

        return true;
    }

    /*****************************************************
     * 배열이 비어있지 않고 배열 안의 값이 empty가 아님을 검증
     * @param objs 배열 객체
     * @return boolean true/false
     *****************************************************/
    public static boolean isNotEmptyAll(Object... objs) {
        boolean result = true;

        if (isNotEmpty(objs)) {
            for (Object obj : objs) {
                if (isEmpty(obj)) {
                    result = false;
                    break;
                }
            }
        } else {
            result = false;
        }

        return result;
    }

    /*****************************************************
     * Map안의 값들이 비었는지를 검증
     * @param map Map 객체
     * @return boolean true/false
     *****************************************************/
    public static boolean isEmptyMapValue(Map<String, Object> map) {
        // 검증 여부
        boolean isEmpty = true;

        for (Object obj : map.values()) {
            if (CoreUtil.isNotEmpty(obj)) {
                isEmpty = false;
            }
        }

        return isEmpty;
    }


    /*****************************************************
     * 임시비밀번호 생성
     * @param nLen 비밀번호 자릿수
     * @return String 임시비밀번호
     *****************************************************/
    public static String shufflePasswd(int nLen) {
        // 임시비밀번호 문자 배열
        char[] charSet = new char[]{
                '0','1','2','3','4','5','6','7','8','9'
                ,'a','b','c','d','e','f','g','h','i','j','k','l','m'
                ,'n','o','p','q','r','s','t','u','v','w','x','y','z'};
        // 인덱스
        int idx = 0;
        // 임시비밀번호
        StringBuilder sb = new StringBuilder();

        for(int i=0; i<nLen; i++) {
            idx = charSet.length*(new SecureRandom().nextInt());
            sb.append(charSet[idx]);
        }

        return sb.toString();
    }


    /*****************************************************
     * 접속한 웹브라우저명 조회
     * @return
     *****************************************************/
    public static String getWebBrowzNm() {
        HttpServletRequest req = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        String agent = req.getHeader(userAgent);

        String webBrowzNm = "";

        if(agent != null) {
            if (agent.indexOf("Firefox") > -1) {
                webBrowzNm = "Firefox";
            } else if (agent.indexOf("Chrome") > -1) {
                webBrowzNm = "Chrome";
            } else if (agent.indexOf("Opera") > -1) {
                webBrowzNm = "Opera";
            } else if (agent.indexOf("Safari") > -1) {
                webBrowzNm = "Safari";
            } else {
                webBrowzNm = "MSIE";
            }
        }

        return webBrowzNm;
    }

    /*****************************************************
     * 웹뷰 or 웹브라우저 접속 확인
     * @return 웹뷰(APP), 웹브라우져(MW)
     *****************************************************/
    public static String getWebType() {
        HttpServletRequest req = ((ServletRequestAttributes)RequestContextHolder.currentRequestAttributes()).getRequest();
        String agent = req.getHeader(userAgent);

        String webBrowzType = "";
        
        if(agent != null) {
            if (agent.indexOf("CHICOR_Mobile") > -1) {
                webBrowzType = "APP";
            } else if (agent.indexOf("Mobile") > -1) {
                webBrowzType = "MW";
            } else {
                webBrowzType = "PC";
            }
        }

        return webBrowzType;
    }

    /*****************************************************
     * 접속한 OS명 조회
     * @return
     *****************************************************/
    public static String getOsNm() {
        HttpServletRequest req = ((ServletRequestAttributes)RequestContextHolder.currentRequestAttributes()).getRequest();
        String agent = req.getHeader(userAgent);
        String osKindMemo = "";

        if(agent != null) {
            if(agent.indexOf("Mobi") > -1) {		// 모바일의 경우
                String[] mobileos = {"iPhone", "iPod", "Android", "BlackBerry", "Windows CE", "Nokia", "Webos", "Opera Mini", "SonyEricsson", "Opera Mobi", "IEMobile"};
                for(int i = 0; i < mobileos.length; i++) {
                    if (agent.indexOf(mobileos[i]) > -1) {
                        osKindMemo = mobileos[i];
                    }
                }
            } else {
                if(agent.indexOf("NT 6.0") != -1) osKindMemo = "Windows Vista/Server 2008";
                else if(agent.indexOf("NT 5.2") != -1) osKindMemo = "Windows Server 2003";
                else if(agent.indexOf("NT 5.1") != -1) osKindMemo = "Windows XP";
                else if(agent.indexOf("NT 5.0") != -1) osKindMemo = "Windows 2000";
                else if(agent.indexOf("NT") != -1) osKindMemo = "Windows NT";
                else if(agent.indexOf("9x 4.90") != -1) osKindMemo = "Windows Me";
                else if(agent.indexOf("98") != -1) osKindMemo = "Windows 98";
                else if(agent.indexOf("95") != -1) osKindMemo = "Windows 95";
                else if(agent.indexOf("Win16") != -1) osKindMemo = "Windows 3.x";
                else if(agent.indexOf("Windows") != -1) osKindMemo = "Windows";
                else if(agent.indexOf("Linux") != -1) osKindMemo = "Linux";
                else if(agent.indexOf("Mac") != -1) osKindMemo = "Mac";
            }
        }

        return osKindMemo;
    }

    /*****************************************************
     * 배열에 값이 들어있을경우 해당 인덱스 반환
     * @param array 배열
     * @param value 값
     * @return 배열 의 해당값 포함여부
     *****************************************************/
    public static int indexOfContainsValue(Object[] array, Object value) {
        for (int i = 0, size = (isArray(array) ? array.length : 0); i < size; i++) {
            if (StringUtil.nvl(array[i]).equals(StringUtil.nvl(value))) {
                return i;
            }
        }

        return -1;
    }

    /*****************************************************
     * 이전날짜 이후날짜 비교
     * @param strPrevDate : 이전날짜
     * @param strNextDate : 이후날짜
     * @return :안맞으면 :true 맞으면:false
     *****************************************************/
    public static boolean isDateCompare(String strPrevDate, String strNextDate) {
        DateFormat sdFormat = new SimpleDateFormat("yyyyMMdd");

        int iComPare = 0;
        Date datePrev;
        Date dateNextDate;
        try {
            datePrev = sdFormat.parse(strPrevDate);
            dateNextDate = sdFormat.parse(strNextDate);
            iComPare = datePrev.compareTo(dateNextDate); //크면 양수, 작으면 음수 같으면 0
        } catch (ParseException e) {
            log.error("ParseException =====> ", e);
        }

        return iComPare > 0;
    }

    /*****************************************************
     * Object를 Map으로 변환 처리
     * @param obj Object
     * @return Map<String, Object> 변환 된 Map
     *****************************************************/
    public static Map<String, Object> objectToMap(Object obj) {
        try {
            Field[] fields = obj.getClass().getDeclaredFields();
            Map <String, Object>resultMap = new HashMap<>();
            for(int i=0; i<=fields.length-1;i++){
                fields[i].setAccessible(true);
                resultMap.put(fields[i].getName(), fields[i].get(obj));
            }
            return resultMap;
        } catch (IllegalArgumentException|IllegalAccessException e) {
            log.error(e.getMessage());
        }

        return null;
    }

    /*****************************************************
     * 클라이언트IP를 리턴처리
     * @param HttpServletRequest request
     * @return String 클라이언트ip
     *****************************************************/
    public static String getClientIP(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }

}
