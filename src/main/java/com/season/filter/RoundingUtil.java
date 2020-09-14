package com.season.filter;

import java.math.RoundingMode;
import java.text.DecimalFormat;

public class RoundingUtil {
//    public static void main(String[] args) {
//        System.out.println(check2Num(12.39879D));
//        System.out.println(check4Num(12.39879D));
//        System.out.println(check0Num(12.39879D));
//    }

    /**
     * 截取小数点后两位不四舍五入
     *
     * @return
     */
    public static String check2Num(Double num) {
        if (StringUtil.checkNull(num)) {
            return "0";
        }
        DecimalFormat formater = new DecimalFormat();
        formater.setMaximumFractionDigits(2);
        formater.setGroupingSize(0);
        formater.setRoundingMode(RoundingMode.FLOOR);
        DecimalFormat df2 = new DecimalFormat("###########0.00");
        return df2.format(Double.valueOf(formater.format(num)));
    }

    /**
     * 截取小数点后4位不四舍五入
     *
     * @return
     */
    public static String check4Num(Double num) {
        if (StringUtil.checkNull(num)) {
            return "0";
        }
        DecimalFormat formater = new DecimalFormat();
        formater.setMaximumFractionDigits(4);
        formater.setGroupingSize(0);
        formater.setRoundingMode(RoundingMode.FLOOR);
        DecimalFormat df2 = new DecimalFormat("###########0.0000");
        return df2.format(Double.valueOf(formater.format(num)));
    }


    /**
     * 截取小数点后4位不四舍五入
     *
     * @return
     */
    public static String check0Num(Double num) {
        if (StringUtil.checkNull(num)) {
            return "0";
        }
        DecimalFormat formater = new DecimalFormat();
        formater.setMaximumFractionDigits(0);
        formater.setGroupingSize(0);
        formater.setRoundingMode(RoundingMode.FLOOR);
        DecimalFormat df2 = new DecimalFormat("###########0");
        return df2.format(Double.valueOf(formater.format(num)));
    }
    public static Long check0Numv1(Double num) {
        if (StringUtil.checkNull(num)) {
            return 0L;
        }
        DecimalFormat formater = new DecimalFormat();
        formater.setMaximumFractionDigits(0);
        formater.setGroupingSize(0);
        formater.setRoundingMode(RoundingMode.FLOOR);
        DecimalFormat df2 = new DecimalFormat("###########0");
        return Long.valueOf(df2.format(Double.valueOf(formater.format(num))));
    }
}
