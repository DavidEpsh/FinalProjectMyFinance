package dsme.myfinance.utils;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class DateUtils {

    public static long getStartOfWeek(){
        GregorianCalendar cal = new GregorianCalendar();

        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.clear(Calendar.MINUTE);
        cal.clear(Calendar.SECOND);
        cal.clear(Calendar.MILLISECOND);
        return cal.getTimeInMillis();
    }

    public static long getStartOfMonth(){
        GregorianCalendar cal = new GregorianCalendar();

        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.clear(Calendar.MINUTE);
        cal.clear(Calendar.SECOND);
        cal.clear(Calendar.MILLISECOND);
        cal.clear(Calendar.DAY_OF_MONTH);
        return cal.getTimeInMillis();
    }

    public static long[] getMonthDateRange(int month, int year){
        GregorianCalendar cal = new GregorianCalendar();
        long startOfMonth, endOfMonth;

        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.clear(Calendar.MINUTE);
        cal.clear(Calendar.SECOND);
        cal.clear(Calendar.MILLISECOND);
        cal.clear(Calendar.DAY_OF_MONTH);
        cal.set(Calendar.MONTH, month);
        cal.set(Calendar.YEAR, year);

        startOfMonth = cal.getTimeInMillis();

        cal.getActualMaximum(Calendar.DAY_OF_MONTH);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.HOUR_OF_DAY, 23);
        endOfMonth = cal.getTimeInMillis();

        return (new long[]{startOfMonth,endOfMonth});
    }

    public static String getNameOfDay(long date) {
        GregorianCalendar cal = new GregorianCalendar();

        cal.setTimeInMillis(date);
        cal.get(Calendar.DAY_OF_WEEK);

        switch (cal.get(Calendar.DAY_OF_WEEK)){
            case 1:
                return "Sun";
            case 2:
                return "Mon";
            case 3:
                return "Tue";
            case 4:
                return "Wed";
            case 5:
                return "Thu";
            case 6:
                return "Fri";
            case 7:
                return "Sat";
            default:
                return "";
        }
    }

    public static int getNumOfDayInMonth(long date){
        GregorianCalendar cal = new GregorianCalendar();

        cal.setTimeInMillis(date);
        return cal.get(Calendar.DAY_OF_MONTH);
    }
}
