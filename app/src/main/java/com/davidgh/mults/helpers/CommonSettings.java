package com.davidgh.mults.helpers;

/**
 * Created by davidgh on 2/24/18.
 */

public class CommonSettings {

    // Google Console APIs developer key
    // Replace this key with your's
    public static final String DEVELOPER_KEY = "AIzaSyA1UUkA-SKgEdfXnOE99IfFTMBwbxGoB_k";

    // YouTube video id
    public static final String YOUTUBE_VIDEO_CODE = "_oEA18Y8gM0";


    public static final String API_ALL_MULTS = "http://lxsimple.000webhostapp.com/api/mults";
    public static final String API_ALL_DETAILS = "http://lxsimple.000webhostapp.com/api/details";

    public static String getMultHeader(int i){
        switch(i){
            // {now, popular, comming soon}

            case 0:
                return "Now";
            case 1:
                return "Popular";
            case 2:
                return "Comming Soon";
            default:
                return "Unknown Films";
        }
    }
}
