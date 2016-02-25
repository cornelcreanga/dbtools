package com.ccreanga.util;

import java.text.DecimalFormat;

public class FormatUtil {

    public static DecimalFormat decimalFormatter(){
        DecimalFormat df = new DecimalFormat("#########.###");
        df.setGroupingUsed(true);
        df.setGroupingSize(3);
        return df;
    }

    public static String readableSize(long size) {
        if(size <= 0) return "0";
        final String[] units = new String[] { "B", "kB", "MB", "GB", "TB" };
        int digitGroups = (int) (Math.log10(size)/Math.log10(1024));
        return new DecimalFormat("#,##0.#").format(size/Math.pow(1024, digitGroups)) + " " + units[digitGroups];
    }

    public static String toMbytes(long number){
        return decimalFormatter().format((double)number/(1024*1024));
    }

    public static String toMillions(long number){
        return decimalFormatter().format((double)number/(1000*1000));
    }

}
