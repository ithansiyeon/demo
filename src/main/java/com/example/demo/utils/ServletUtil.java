package com.example.demo.utils;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.Enumeration;

@Slf4j
public class ServletUtil {
    public static String getHeaderRefer(HttpServletRequest request) {
        return request.getHeader("referer");
    }

    public static String getHeaderUserAgent(HttpServletRequest request) {
        return request.getHeader("user-agent");
    }

    /**
     * Ajax Request 요청여부를 리턴한다.
     *
     * @param request
     * @return
     */
    public static boolean isAjaxRequest() {
        String header = getRequest().getHeader("X-Requested-With");
        if (StringUtils.isEmpty(header)) {
            return false;
        }
        return header.equals("XMLHttpRequest");
    }

    public static HttpServletRequest getRequest() {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        if (requestAttributes == null) {
            return null;
        }
        return ((ServletRequestAttributes) requestAttributes).getRequest();
    }

    public static HttpServletResponse getResponse() {
        return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
    }

    public static void sendRedirect(String location) throws IOException {
        try {
            getResponse().sendRedirect(location);
        } catch (IOException e) {
            log.error("sendRedirect error", e);
            throw e;
        }
    }

    public static String getRealPath() {
        return getRequest().getSession().getServletContext().getRealPath("/") != null ?getRequest().getSession().getServletContext().getRealPath("/") : null ;
    }

    public static String getRealPath(String addPath) {
        String gepath = getRealPath() != null ? getRealPath() :null;
        return gepath + addPath;
    }

    /**
     * 브라우저 종류를 리턴한다.
     *
     * @return
     */
    public static Browser getBrowser() {
        String header = getRequest().getHeader("User-Agent") != null ? getRequest().getHeader("User-Agent") : null ;
        if(header != null) {
            if (header.indexOf("MSIE") > -1 || header.indexOf("Trident") > -1) {
                return Browser.MSIE != null ? Browser.MSIE : null ;
            } else if (header.indexOf("Chrome") > -1) {
                return Browser.CHROME != null ? Browser.CHROME :null ;
            } else if (header.indexOf("Opera") > -1) {
                return Browser.OPERA != null ? Browser.OPERA : null;
            } else if (header.indexOf("Firefox") > -1) {
                return Browser.FIREFOX != null ? Browser.FIREFOX :null;
            }
        }

        return Browser.ETC != null ? Browser.ETC :null;
    }

    enum Browser {
        MSIE, CHROME, OPERA, FIREFOX, ETC
    }

    /**
     * 로그 제외 여부 반환
     *
     * @param request
     * @return
     */
    public static boolean isIgnoreLogging(HttpServletRequest request) {
        return request.getAttribute("__ignoreLogging") != null && (boolean) request.getAttribute("__ignoreLogging");
    }

    /**
     * 브라우저 종류에 따라 한글을 인코딩하여 리턴한다.
     *
     * @param filename : 파일명
     * @param attachmentPrefix : attachment prefix 사용여부 boolean
     * @param isAddCurrentDatePrefix : 파일명에 다운로드 시간 추가 여부 boolean
     * @return
     * @throws Exception
     */
    public static String getEncodingFilename(String filename, boolean attachmentPrefix, boolean isAddCurrentDatePrefix) throws Exception {
        Browser browser = getBrowser();
        String encodedFilename = new String(filename);
        try {
            if (browser == Browser.MSIE) {
                encodedFilename = URLEncoder.encode(filename, "UTF-8").replaceAll("\\+", "%20");
            } else if (browser == Browser.FIREFOX) {
                encodedFilename = "\"" + new String(filename.getBytes("UTF-8"), "8859_1") + "\"";
            } else if (browser == Browser.OPERA) {
                encodedFilename = "\"" + new String(filename.getBytes("UTF-8"), "8859_1") + "\"";
            } else if (browser == Browser.CHROME) {
                StringBuffer sb = new StringBuffer();
                for (int i = 0; i < filename.length(); i++) {
                    char c = filename.charAt(i);
                    if (c > '~') {
                        sb.append(URLEncoder.encode("" + c, "UTF-8"));
                    } else {
                        sb.append(c);
                    }
                }
                encodedFilename = sb.toString();
            }
            // 파일명에 현재날짜를 추가
            if (isAddCurrentDatePrefix) {
                String name = encodedFilename.substring(0, encodedFilename.lastIndexOf("."));
                String prefix = encodedFilename.substring(encodedFilename.lastIndexOf(".") + 1,
                        encodedFilename.length());
                encodedFilename = name + "_" + CalendarUtil.getCurrentSimpletTimeDate() + "." + prefix;
            }
            if (attachmentPrefix) {
                return "attachment; filename=" + encodedFilename + ";";
            }
        }catch(Exception bfe) {
            log.error("BaseFileException", bfe);
            throw new Exception("파일 이름 인코딩 중 오류가 발생했습니다.");
        }
        return encodedFilename;
    }

    /**
     * request parameter 값들을 log로 출력
     *
     * @param request
     * @throws Exception
     */
    public static void printRequest(HttpServletRequest request) throws Exception {
        Enumeration<String> parameterNames = request.getParameterNames();
        log.debug("@@@ ServletUtil.printRequest START");
        while (parameterNames.hasMoreElements()) {
            String key = parameterNames.nextElement();
            String[] values = request.getParameterValues(key);
            if (values != null) {
                if (values.length == 1) {
                    log.debug("@@@ Util.printRequest -- [" + key + ":" + values[0] + "]");
                } else {
                    log.debug("@@@ Util.printRequest -- [" + key + ":" + Arrays.asList(values).toString() + "]");
                }
            } else {
                log.debug("@@@ Util.printRequest -- [" + key + ":" + values + "]");
            }
        }
        log.debug("@@@ ServletUtil.printRequest END");
    }

    /**
     * request ip 정보를 반환
     *
     * @return
     */
    public static String getIp() {
        // IP 체크
        String ip ="";
        if(getRequest().getHeader("X-FORWARDED-FOR") != null ) {
            ip = getRequest().getHeader("X-FORWARDED-FOR") ;
        }
        if (StringUtil.isEmpty(ip) && ip == null) {
            ip = getRequest().getHeader("Proxy-Xlient-IP");
        }
        if (StringUtil.isEmpty(ip) && ip == null) {
            ip = getRequest().getHeader("WL-Proxy-Client-IP");
        }
        if (StringUtil.isEmpty(ip) && ip == null) {
            ip = getRequest().getRemoteAddr();
        }
        return ip;
    }
}
