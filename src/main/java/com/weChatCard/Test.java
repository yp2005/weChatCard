package com.weChatCard;

import java.util.Calendar;

public class Test {
    public static void main(String[] args) {
        Calendar calendar = Calendar.getInstance();
        System.out.println(calendar.get(Calendar.YEAR) + " "  + calendar.get(Calendar.MONTH) + " " + calendar.get(Calendar.DAY_OF_MONTH));
    }
}
