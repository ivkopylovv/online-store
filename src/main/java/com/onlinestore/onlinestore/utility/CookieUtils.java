package com.onlinestore.onlinestore.utility;

import javax.servlet.http.Cookie;

public class CookieUtils {
    public static Cookie getCookie(String key, String value) {
        Cookie cookie = new Cookie(key, value);

        cookie.setHttpOnly(false);
        cookie.setSecure(false);

        return cookie;
    }
}
