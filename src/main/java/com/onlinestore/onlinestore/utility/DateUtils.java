package com.onlinestore.onlinestore.utility;

import java.util.Date;

public class DateUtils {
    public static Long getCurrentDateWithOffset(Long offset) {
        return new Date().getTime() + offset;
    }
}
