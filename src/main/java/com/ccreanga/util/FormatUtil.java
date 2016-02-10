package com.ccreanga.util;

import java.text.DecimalFormat;

public class FormatUtil {

    public static DecimalFormat decimalFormatter(){
        DecimalFormat df = new DecimalFormat("###.###");
        df.setGroupingUsed(true);
        df.setGroupingSize(3);
        return df;
    }

}
