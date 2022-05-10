package com.onlinestore.onlinestore.utility;

import com.onlinestore.onlinestore.constants.TokenOption;

import java.util.Date;

public class DateHelper {

    public static Date getAccessTokenTimeAlive() {
        return new Date(System.currentTimeMillis() + TokenOption.ACCESS_TOKEN_TIME_ALIVE);
    }

    public static Date getRefreshTokenTimeAlive() {
        return new Date(System.currentTimeMillis() + TokenOption.REFRESH_TOKEN_TIME_ALIVE);
    }

    public static Date getCurrentDate() {
        return new Date(System.currentTimeMillis());
    }
}
