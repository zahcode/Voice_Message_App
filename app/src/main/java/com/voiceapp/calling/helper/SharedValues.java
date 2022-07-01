package com.voiceapp.calling.helper;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class SharedValues {

    public static final String PHONE_LENGTH ="phone_length";
    public static final String COUNTRY_CODE ="country_code";
    public static final String MESSAGE_TEXT ="message_text";
    public static final String ACCESS_KEY ="access_key";
    public static final String LANGUAGE ="language";
    public static final String VOICE_TYPE ="voice_type";

    SharedPreferences shared;
    SharedPreferences.Editor editor;
    Context context;

    public SharedValues(Context context) {
        this.context = context;

        shared = PreferenceManager.getDefaultSharedPreferences(context);
        editor = shared.edit();
    }

    // get Values
    public int getPhoneLength(){
        return shared.getInt(PHONE_LENGTH, 10);
    }

    public String getCountryCode(){
        return shared.getString(COUNTRY_CODE, "+2");
    }

    public String getMessageText(){
        return shared.getString(MESSAGE_TEXT, "Hello");
    }

    public String getAccessKey(){
        return shared.getString(ACCESS_KEY, "");
    }

    public String getLanguage(){
        return shared.getString(LANGUAGE, "en-us");
    }

    public String getVoiceType(){
        return shared.getString(VOICE_TYPE, "female");
    }

    // set Values
    public void setPhoneLength(int phoneLength){
        editor.putInt(PHONE_LENGTH, phoneLength);
        editor.apply();
    }

    public void setCountryCode(String countryCode){
        editor.putString(COUNTRY_CODE, countryCode);
        editor.apply();
    }

    public void setMessageText(String messageText){
        editor.putString(MESSAGE_TEXT, messageText);
        editor.apply();
    }

    public void setAccessKey(String accessKey){
        editor.putString(ACCESS_KEY, accessKey);
        editor.apply();
    }

    public void setLanguage(String language){
        editor.putString(LANGUAGE, language);
        editor.apply();
    }

    public void setVoiceType(String voiceType){
        editor.putString(VOICE_TYPE, voiceType);
        editor.apply();
    }
}
