
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
import com.aokp.romcontrol.util.Helpers;

import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;


public class TabletTweaks extends AOKPPreferenceFragment implements OnPreferenceChangeListener 
{
/*
    private static final String TABLET_TWEAKS_HIDE_HOME = "tablet_tweaks_hide_home";
    private static final String TABLET_TWEAKS_HIDE_RECENT = "tablet_tweaks_hide_recent";
    private static final String TABLET_TWEAKS_HIDE_BACK = "tablet_tweaks_hide_back";
    private static final String TABLET_TWEAKS_HIDE_MENU = "tablet_tweaks_hide_menu";
    private static final String TABLET_TWEAKS_FORCE_MENU = "tablet_tweaks_force_menu";
*/
    private static final String TABLET_TWEAKS_RIGHT_BUTTONS = "tablet_tweaks_right_buttons";
    public static final String TABLET_TWEAKS_DISABLE_HARDWARE_BUTTONS = "tablet_tweaks_disable_hardware_buttons";
    private static final String TABLET_TWEAKS_RECENT_THUMBNAILS = "tablet_tweaks_recent_thumbnails";
    private static final String TABLET_TWEAKS_PEEK_NOTIFICATIONS = "tablet_tweaks_peek_notifications";
    private static final String TABLET_TWEAKS_HIDE_STATUSBAR = "tablet_tweaks_hide_statusbar";
    private static final String TABLET_TWEAKS_STORAGE_SWITCH = "tablet_tweaks_storage_switch";
    private static final String TABLET_TWEAKS_STORAGE_AUTOMOUNT = "tablet_tweaks_storage_automount";

    public static final String COMMAND_SHELL = "/system/bin/sh";
    public static final String ECHO_COMMAND = "echo ";
    public static final String BUTTONS_ENABLED_PATH =
            "/sys/devices/platform/s3c2440-i2c.2/i2c-2/2-004a/buttons_enabled";
    public static final String BUTTONS_ENABLED_COMMAND =
            " > /sys/devices/platform/s3c2440-i2c.2/i2c-2/2-004a/buttons_enabled";

/*
    CheckBoxPreference mTabletTweaksHideHome;
    CheckBoxPreference mTabletTweaksHideRecent;
    CheckBoxPreference mTabletTweaksHideBack;
    CheckBoxPreference mTabletTweaksHideMenu;
    CheckBoxPreference mTabletTweaksForceMenu;
*/
    CheckBoxPreference mTabletTweaksRightButtons;
    CheckBoxPreference mTabletTweaksDisableHardwareButtons;
    CheckBoxPreference mTabletTweaksRecentThumbnails;
    CheckBoxPreference mTabletTweaksPeekNotifications;
    CheckBoxPreference mTabletTweaksHideStatusbar;
    CheckBoxPreference mTabletTweaksStorageSwitch;
    CheckBoxPreference mTabletTweaksStorageAutomount;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.tablet_tweaks);
        PreferenceScreen prefs = getPreferenceScreen();

/*
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
        mTabletTweaksForceMenu = (CheckBoxPreference) findPreference(TABLET_TWEAKS_FORCE_MENU);
        mTabletTweaksForceMenu.setChecked(Settings.System.getInt(getActivity().getContentResolver(),
                Settings.System.FORCE_SOFT_MENU_BUTTON, 0) == 1);
*/
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
        mTabletTweaksStorageSwitch = (CheckBoxPreference) findPreference(TABLET_TWEAKS_STORAGE_SWITCH);
	mTabletTweaksStorageSwitch.setChecked((SystemProperties.getInt("persist.sys.vold.switchexternal", 0) == 0));
        mTabletTweaksStorageAutomount = (CheckBoxPreference) findPreference(TABLET_TWEAKS_STORAGE_AUTOMOUNT);
        mTabletTweaksStorageAutomount.setChecked(Settings.System.getInt(getActivity().getContentResolver(),
                Settings.Secure.MOUNT_UMS_AUTOSTART, 0) == 1);

        File file = new File(BUTTONS_ENABLED_PATH);
        if (!file.exists()) {
            prefSet.removePreference(findPreference("capacitive"));
        }

        if (SystemProperties.get("ro.vold.switchablepair","").equals("")) {
            ((PreferenceGroup) findPreference("storage")).removePreference(mTabletTweaksStorageSwitch);
        }

	// not working at the moment, will get this sorted out hopefully soon.
        ((PreferenceGroup) findPreference("storage")).removePreference(mTabletTweaksStorageSwitch);
        ((PreferenceGroup) findPreference("storage")).removePreference(mTabletTweaksStorageAutomount);

    }

    @Override
    public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen,
            Preference preference) {
/*
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
        } else if (preference == mTabletTweaksForceMenu) {
            Settings.System.putInt(getActivity().getContentResolver(),
                    Settings.System.FORCE_SOFT_MENU_BUTTON, 
		    ((CheckBoxPreference) preference).isChecked() ? 1 : 0);
            return true;
        } else if (preference == mTabletTweaksDisableHardwareButtons) {
*/
        if (preference == mTabletTweaksDisableHardwareButtons) {
            try {

                String[] cmds = {COMMAND_SHELL, "-c",
                        ECHO_COMMAND + (((CheckBoxPreference) preference).isChecked() ? "0" : "1") +
                        BUTTONS_ENABLED_COMMAND};
                Runtime.getRuntime().exec(cmds);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return true;
        } else if (preference == mTabletTweaksRecentThumbnails) {
            Settings.System.putInt(getActivity().getContentResolver(),
                    Settings.System.LARGE_RECENT_THUMBNAILS, 
		    ((CheckBoxPreference) preference).isChecked() ? 1 : 0);
            Helpers.restartSystemUI();
            return true;
        } else if (preference == mTabletTweaksRightButtons) {
            Settings.System.putInt(getActivity().getContentResolver(),
                    Settings.System.RIGHT_SOFT_BUTTONS, 
		    ((CheckBoxPreference) preference).isChecked() ? 1 : 0);
            Helpers.restartSystemUI();
            return true;
        } else if (preference == mTabletTweaksHideStatusbar) {
            Settings.System.putInt(getActivity().getContentResolver(),
                    Settings.System.HIDE_STATUSBAR, 
		    ((CheckBoxPreference) preference).isChecked() ? 1 : 0);
            Helpers.restartSystemUI();
            return true;
        } else if (preference == mTabletTweaksPeekNotifications) {
            Settings.System.putInt(getActivity().getContentResolver(),
                    Settings.System.SHOW_NOTIFICATION_PEEK, 
		    ((CheckBoxPreference) preference).isChecked() ? 1 : 0);
            Helpers.restartSystemUI();
            return true;
        } else if (preference == mTabletTweaksStorageSwitch) {
            SystemProperties.set("persist.sys.vold.switchexternal", ((CheckBoxPreference) preference).isChecked() ? "1" : "0");
            return true;
        } else if (preference == mTabletTweaksStorageAutomount) {
            Settings.System.putInt(getActivity().getContentResolver(),
                    Settings.Secure.MOUNT_UMS_AUTOSTART, 
		    ((CheckBoxPreference) preference).isChecked() ? 1 : 0);
            return true;
	}
        return super.onPreferenceTreeClick(preferenceScreen, preference);
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        return false;
    }
}

