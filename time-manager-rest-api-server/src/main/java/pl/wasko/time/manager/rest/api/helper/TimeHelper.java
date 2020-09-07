package pl.wasko.time.manager.rest.api.helper;

import java.util.Calendar;
import java.util.Date;

public class TimeHelper {

    public static long timeAsSeconds(String time) {
        long sign = 1;
        if(time.startsWith("-")) {
            time = time.substring(1);
            sign = -1;
        }

        String[] split = time.split(":");
        return sign * (Long.parseLong(split[0]) * 3600 + Long.parseLong(split[1]) * 60 + Long.parseLong(split[2]));
    }

    public static String secondsAsTime(long seconds) {
        return String.format("%02d:%02d:%02d", seconds / 3600, (seconds % 3600) / 60, (seconds % 60));
    }

    public static Date addToDate(Date date, String time) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.SECOND, (int) timeAsSeconds(time));
        return calendar.getTime();
    }

}
