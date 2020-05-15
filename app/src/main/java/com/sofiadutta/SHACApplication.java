package com.sofiadutta;

import android.app.Application;

public class SHACApplication extends Application {
    private static final String ADULT_FAMILY_MEMBER = "Adult/Family_Member";
    private static final String CHILD_FAMILY_MEMBER = "Child/Family_Member";
    private static final String NON_FAMILY_MEMBER = "Stranger";
    private static final String USER_INFO = "UserInfo";

    public static String getAdultFamilyMember() {
        return ADULT_FAMILY_MEMBER;
    }

    public static String getChildFamilyMember() {
        return CHILD_FAMILY_MEMBER;
    }

    public static String getNonFamilyMember() {
        return NON_FAMILY_MEMBER;
    }

    public static String getUserInfo() {
        return USER_INFO;
    }

    public static String getKasaInfoObject() {
        return KASA_INFO_OBJECT;
    }

    private static final String KASA_INFO_OBJECT = "KasaInfoObject";
}
