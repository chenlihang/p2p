package cn.wolfcode.p2p.base.util;

import java.util.Calendar;
import java.util.Date;

public abstract class DateUtil {
    //获取当前的最后时间
    public static Date getEndDate(Date d) {
        if (d == null) {
            return null;
        }
        Calendar c = Calendar.getInstance();
        c.setTime(d);
        c.set(Calendar.HOUR,23);
        c.set(Calendar.MINUTE,59);
        c.set(Calendar.SECOND,59);
        return c.getTime();
    }

    /**
     * 获取两个日期之间的秒值
     * @param d1
     * @param d2
     * @return
     */
    public static long getDateBetweenTime(Date d1,Date d2){
        return Math.abs((d1.getTime()-d2.getTime())/1000);
    }
}
