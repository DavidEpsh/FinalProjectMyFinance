package dsme.myfinance.utils;

import java.util.ArrayList;
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
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.DAY_OF_MONTH, 1);
        cal.set(Calendar.MONTH, month);
        cal.set(Calendar.YEAR, year);

        startOfMonth = cal.getTimeInMillis();

        cal.set(Calendar.MONTH, month);
        cal.set(Calendar.DAY_OF_MONTH,cal.getActualMaximum(Calendar.DAY_OF_MONTH));
        cal.set(Calendar.MINUTE, cal.getActualMaximum(Calendar.MINUTE));
        cal.set(Calendar.HOUR_OF_DAY, cal.getActualMaximum(Calendar.HOUR_OF_DAY));
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

    public static ArrayList<String> getMonths() {

        ArrayList<String> m = new ArrayList<String>();
        m.add("Jan");
        m.add("Feb");
        m.add("Mar");
        m.add("Apr");
        m.add("May");
        m.add("Jun");
        m.add("Jul");
        m.add("Aug");
        m.add("Sep");
        m.add("Okt");
        m.add("Nov");
        m.add("Dec");

        return m;
    }

    public static String getMonthName(int month) {

        switch (month){
            case 1:
                return "January";
            case 2:
                return "February";
            case 3:
                return "March";
            case 4:
                return "April";
            case 5:
                return "May";
            case 6:
                return "June";
            case 7:
                return "July";
            case 8:
                return "August";
            case 9:
                return "September";
            case 10:
                return "October";
            case 11:
                return "November";
            case 12:
                return "December";
            default:
                return "Monthly";
        }
    }

    public static String getMonthNameShort(int month) {

        switch (month){
            case 1:
                return "Jan";
            case 2:
                return "Feb";
            case 3:
                return "Mar";
            case 4:
                return "Apr";
            case 5:
                return "May";
            case 6:
                return "Jun";
            case 7:
                return "Jul";
            case 8:
                return "Aug";
            case 9:
                return "Sep";
            case 10:
                return "Oct";
            case 11:
                return "Nov";
            case 12:
                return "Dec";
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
