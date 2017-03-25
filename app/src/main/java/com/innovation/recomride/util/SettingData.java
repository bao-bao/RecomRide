package com.innovation.recomride.util;

/**
 * Created by AMXPC on 2017/3/26.
 */

public class SettingData {
    public static final int SAFE = 1;
    public static final int SATISFIED = 2;
    public static final int COMFORT = 3;
    private static int a = SAFE;

    public static int getSetting() {
        return a;
    }

    public static void setSetting(int a) {
        SettingData.a = a;
    }
}
