
package com.aokp.romcontrol.fragments;

import android.app.AlertDialog;
import android.app.FragmentTransaction;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnMultiChoiceClickListener;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemProperties;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceGroup;
import android.preference.PreferenceScreen;
import android.provider.Settings;
import android.provider.Settings.SettingNotFoundException;
import android.os.PowerManager;
import android.util.Log;

import com.aokp.romcontrol.R;
import com.aokp.romcontrol.AOKPPreferenceFragment;

import java.io.DataOutputStream;
import java.io.IOException;

public class TabletTweaks extends AOKPPreferenceFragment implements OnPreferenceChangeListener 
{
    private static final String TABLET_TWEAKS_HIDE_HOME = "tablet_tweaks_hide_home";
    private static final String TABLET_TWEAKS_HIDE_RECENT = "tablet_tweaks_hide_recent";
    private static final String TABLET_TWEAKS_HIDE_BACK = "tablet_tweaks_hide_back";
    private static final String TABLET_TWEAKS_HIDE_MENU = "tablet_tweaks_hide_menu";
    private static final String TABLET_TWEAKS_RIGHT_BUTTONS = "tablet_tweaks_right_buttons";
    public static final String TABLET_TWEAKS_DISABLE_HARDWARE_BUTTONS = "tablet_tweaks_disable_hardware_buttons";
    private static final String TABLET_TWEAKS_RECENT_THUMBNAILS = "tablet_tweaks_recent_thumbnails";
    private static final String TABLET_TWEAKS_PEEK_NOTIFICATIONS = "tablet_tweaks_peek_notifications";
    private static final String TABLET_TWEAKS_HIDE_STATUSBAR = "tablet_tweaks_hide_statusbar";

    public static final String BUTTONS_ENABLED_COMMAND = "echo ";
    public static final String BUTTONS_ENABLED_PATH =
            " > /sys/devices/platform/s3c2440-i2c.2/i2c-2/2-004a/buttons_enabled";
    public static final String BUTTONS_ENABLED_SHELL = "/system/bin/sh";

    CheckBoxPreference mTabletTweaksHideHome;
    CheckBoxPreference mTabletTweaksHideRecent;
    CheckBoxPreference mTabletTweaksHideBack;
    CheckBoxPreference mTabletTweaksHideMenu;
    CheckBoxPreference mTabletTweaksRightButtons;
    CheckBoxPreference mTabletTweaksDisableHardwareButtons;
    CheckBoxPreference mTabletTweaksRecentThumbnails;
    CheckBoxPreference mTabletTweaksPeekNotifications;
    CheckBoxPreference mTabletTweaksHideStatusbar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.tablet_tweaks);
        PreferenceScreen prefs = getPreferenceScreen();

        mTabletTweaksHideBack = (CheckBoxPreference) findPreference(TABLET_TWEAKS_HIDE_BACK);
        mTabletTweaksHideBack.setChecked(Settings.System.getInt(getActivity().getContentResolver(),
                Settings.System.HIDE_SOFT_BACK_BUTTON, 0) == 1);
        mTabletTweaksHideHome = (CheckBoxPreference) findPreference(TABLET_TWEAKS_HIDE_HOME);
        mTabletTweaksHideHome.setChecked(Settings.System.getInt(getActivity().getContentResolver(),
                Settings.System.HIDE_SOFT_HOME_BUTTON, 0) == 1);
        mTabletTweaksHideRecent = (CheckBoxPreference) findPreference(TABLET_TWEAKS_HIDE_RECENT);
        mTabletTweaksHideRecent.setChecked(Settings.System.getInt(getActivity().getContentResolver(),
                Settings.System.HIDE_SOFT_RECENT_BUTTON, 0) == 1);
       mTabletTweaksHideMenu = (CheckBoxPreference) findPreference(TABLET_TWEAKS_HIDE_MENU);
        mTabletTweaksHideMenu.setChecked(Settings.System.getInt(getActivity().getContentResolver(),
                Settings.System.HIDE_SOFT_MENU_BUTTON, 0) == 1);
        mTabletTweaksDisableHardwareButtons = (CheckBoxPreference) findPreference(TABLET_TWEAKS_DISABLE_HARDWARE_BUTTONS);
        mTabletTweaksRecentThumbnails = (CheckBoxPreference) findPreference(TABLET_TWEAKS_RECENT_THUMBNAILS);
        mTabletTweaksRecentThumbnails.setChecked(Settings.System.getInt(getActivity().getContentResolver(),
                Settings.System.LARGE_RECENT_THUMBNAILS, 0) == 1);
        mTabletTweaksRightButtons = (CheckBoxPreference) findPreference(TABLET_TWEAKS_RIGHT_BUTTONS);
        mTabletTweaksRightButtons.setChecked(Settings.System.getInt(getActivity().getContentResolver(),
                Settings.System.RIGHT_SOFT_BUTTONS, 0) == 1);
        mTabletTweaksHideStatusbar = (CheckBoxPreference) findPreference(TABLET_TWEAKS_HIDE_STATUSBAR);
        mTabletTweaksHideStatusbar.setChecked(Settings.System.getInt(getActivity().getContentResolver(),
                Settings.System.HIDE_STATUSBAR, 0) == 1);
        mTabletTweaksPeekNotifications = (CheckBoxPreference) findPreference(TABLET_TWEAKS_PEEK_NOTIFICATIONS);
        mTabletTweaksPeekNotifications.setChecked(Settings.System.getInt(getActivity().getContentResolver(),
                Settings.System.SHOW_NOTIFICATION_PEEK, 0) == 1);

        ((PreferenceGroup) findPreference("statusbar")).removePreference(mTabletTweaksRightButtons);
//	prefs.removePreference(mTabletTweaksRightButtons);
    }

    @Override
    public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen,
            Preference preference) {

        if (preference == mTabletTweaksHideHome) {
            Settings.System.putInt(getActivity().getContentResolver(),
                    Settings.System.HIDE_SOFT_HOME_BUTTON, 
		    ((CheckBoxPreference) preference).isChecked() ? 1 : 0);
            return true;
        } else if (preference == mTabletTweaksHideRecent) {
            Settings.System.putInt(getActivity().getContentResolver(),
                    Settings.System.HIDE_SOFT_RECENT_BUTTON, 
		    ((CheckBoxPreference) preference).isChecked() ? 1 : 0);
            return true;
        } else if (preference == mTabletTweaksHideBack) {
            Settings.System.putInt(getActivity().getContentResolver(),
                    Settings.System.HIDE_SOFT_BACK_BUTTON, 
		    ((CheckBoxPreference) preference).isChecked() ? 1 : 0);
            return true;
        } else if (preference == mTabletTweaksHideMenu) {
            Settings.System.putInt(getActivity().getContentResolver(),
                    Settings.System.HIDE_SOFT_MENU_BUTTON, 
		    ((CheckBoxPreference) preference).isChecked() ? 1 : 0);
            return true;
        } else if (preference == mTabletTweaksDisableHardwareButtons) {
            try {
                String[] cmds = {BUTTONS_ENABLED_SHELL, "-c",
                        BUTTONS_ENABLED_COMMAND + (((CheckBoxPreference) preference).isChecked() ? "0" : "1") + BUTTONS_ENABLED_PATH};
                Runtime.getRuntime().exec(cmds);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return true;
        } else if (preference == mTabletTweaksRecentThumbnails) {
            Settings.System.putInt(getActivity().getContentResolver(),
                    Settings.System.LARGE_RECENT_THUMBNAILS, 
		    ((CheckBoxPreference) preference).isChecked() ? 1 : 0);
            restartSystemUI();
            return true;
        } else if (preference == mTabletTweaksRightButtons) {
            Settings.System.putInt(getActivity().getContentResolver(),
                    Settings.System.RIGHT_SOFT_BUTTONS, 
		    ((CheckBoxPreference) preference).isChecked() ? 1 : 0);
            restartSystemUI();
            return true;
        } else if (preference == mTabletTweaksHideStatusbar) {
            Settings.System.putInt(getActivity().getContentResolver(),
                    Settings.System.HIDE_STATUSBAR, 
		    ((CheckBoxPreference) preference).isChecked() ? 1 : 0);
            restartSystemUI();
            return true;
        } else if (preference == mTabletTweaksPeekNotifications) {
            Settings.System.putInt(getActivity().getContentResolver(),
                    Settings.System.SHOW_NOTIFICATION_PEEK, 
		    ((CheckBoxPreference) preference).isChecked() ? 1 : 0);
            restartSystemUI();
            return true;
	}
        return super.onPreferenceTreeClick(preferenceScreen, preference);
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        return false;
    }

    private void restartSystemUI() {
        try {
            Runtime.getRuntime().exec("pkill -TERM -f  com.android.systemui");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

