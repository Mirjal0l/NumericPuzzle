package com.example.numericpuzzle.cache;

import android.content.Context;
import android.content.SharedPreferences;

public class SettingsPreference {
    private static SettingsPreference settingsPreference;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;


    private SettingsPreference(Context context) {
        sharedPreferences = context.getSharedPreferences("settings", Context.MODE_PRIVATE);
    }

    public static SettingsPreference getSettingsPreference() {
        return settingsPreference;
    }

    public static void init(Context context) {
        if (settingsPreference == null) {
            settingsPreference = new SettingsPreference(context);
        }
    }

    public boolean setScore(int steps, String time) {
        editor = sharedPreferences.edit();
        editor.putString("time", time);
        editor.putInt("step", steps);
        return editor.commit();
    }

    public String getTime() {
        return sharedPreferences.getString("time", "00:00");
    }

    public int getSteps() {
        return sharedPreferences.getInt("step", 0);
    }

    public void clearCache() {
        editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }
}
