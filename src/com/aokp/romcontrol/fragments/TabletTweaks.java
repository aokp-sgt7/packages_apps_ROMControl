/* -------------------------------------- --- -- -
** :::: AOKP ICS for SGT7 GSM - Tablet Tweaks ::::
** - -- --- --------------------------------------
** 
** Tablet Tweaks for Samsung GalaxyTab GT-P1000 (all variants)
**
** originally coded by sbradymobile of the SGT7 ICS TE4M
** ported to AOKP and ROMControl by stimpz0r
**
*/

package com.aokp.romcontrol.fragments;

import android.app.AlertDialog;
import android.app.FragmentTransaction;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnMultiChoiceClickListener;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemProperties;
import android.preference.CheckBoxPreference;
import android.preference.ListPreference;
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
import com.aokp.romcontrol.util.CMDProcessor;
import com.aokp.romcontrol.util.Helpers;

import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;


public class TabletTweaks extends AOKPPreferenceFragment implements OnPreferenceChangeListener 
{
    static final String TAG = "TabletTweaks";
    private static final String TT_RIGHT_BUTTONS = "tt_right_buttons";
    public static final String TT_ENABLE_HARDWARE_BUTTONS = "tt_enable_hardware_buttons";
    public static final String TT_BACKLIGHT_TIMEOUT = "tt_backlight_timeout";
    private static final String TT_RECENT_THUMBNAILS = "tt_recent_thumbnails";
    private static final String TT_PEEK_NOTIFICATIONS = "tt_peek_notifications";
    private static final String TT_HIDE_STATUSBAR = "tt_hide_statusbar";
    private static final String TT_STORAGE_SWITCH = "tt_storage_switch";
    private static final String TT_STORAGE_AUTOMOUNT = "tt_storage_automount";
    public static final String TT_GPU_OVERCLOCK = "tt_gpu_overclock";
    public static final String TT_WIFI_PM = "tt_wifi_pm";
    public static final String TT_LIVEOC = "tt_liveoc";
    public static final String TT_TOUCHSCREEN_CLOCK = "tt_touchscreen_clock";

    public static final String CAPACITIVE_BUTTONS_ENABLED_FILE = "/sys/devices/platform/s3c2440-i2c.2/i2c-2/2-004a/buttons_enabled";
    private static final String CAPACITIVE_BACKLIGHT_FILE = "/sys/devices/platform/s3c2440-i2c.2/i2c-2/2-004a/leds_timeout";
    private static final String GPU_OVERCLOCK_FILE = "/sys/kernel/pvr_oc/pvr_oc";
    private static final String WIFI_PM_FILE = "/sys/module/bcmdhd/parameters/wifi_pm";
    private static final String LIVEOC_FILE = "/sys/class/misc/liveoc/oc_value";
    private static final String TOUCHSCREEN_CLOCK_FILE = "/sys/devices/platform/s3c2440-i2c.2/i2c-2/2-004a/cpufreq_lock";

    CheckBoxPreference mTTRightButtons;
    CheckBoxPreference mTTEnableHardwareButtons;
    CheckBoxPreference mTTRecentThumbnails;
    CheckBoxPreference mTTPeekNotifications;
    CheckBoxPreference mTTHideStatusbar;
    CheckBoxPreference mTTStorageSwitch;
    CheckBoxPreference mTTStorageAutomount;
    ListPreference mTTBacklightTimeout;
    ListPreference mTTGpuOverclock;
    ListPreference mTTWifiPM;
    ListPreference mTTLiveOC;
    ListPreference mTTTouchscreenClock;
 
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.tablet_tweaks);
        PreferenceScreen prefs = getPreferenceScreen();

        mTTEnableHardwareButtons = (CheckBoxPreference) findPreference(TT_ENABLE_HARDWARE_BUTTONS);
        mTTEnableHardwareButtons.setChecked(Settings.System.getInt(getActivity().getContentResolver(),
                Settings.System.ENABLE_HARDWARE_BUTTONS, 0) == 1);

        mTTRecentThumbnails = (CheckBoxPreference) findPreference(TT_RECENT_THUMBNAILS);
        mTTRecentThumbnails.setChecked(Settings.System.getInt(getActivity().getContentResolver(),
                Settings.System.LARGE_RECENT_THUMBNAILS, 0) == 1);

        mTTRightButtons = (CheckBoxPreference) findPreference(TT_RIGHT_BUTTONS);
        mTTRightButtons.setChecked(Settings.System.getInt(getActivity().getContentResolver(),
                Settings.System.RIGHT_SOFT_BUTTONS, 0) == 1);

        mTTHideStatusbar = (CheckBoxPreference) findPreference(TT_HIDE_STATUSBAR);
        mTTHideStatusbar.setChecked(Settings.System.getInt(getActivity().getContentResolver(),
                Settings.System.HIDE_STATUSBAR, 0) == 1);

        mTTPeekNotifications = (CheckBoxPreference) findPreference(TT_PEEK_NOTIFICATIONS);
        mTTPeekNotifications.setChecked(Settings.System.getInt(getActivity().getContentResolver(),
                Settings.System.SHOW_NOTIFICATION_PEEK, 0) == 1);

        mTTBacklightTimeout = (ListPreference) findPreference(TT_BACKLIGHT_TIMEOUT);
        mTTBacklightTimeout.setOnPreferenceChangeListener(this);
        mTTBacklightTimeout.setValue(Integer.toString(Settings.System.getInt(getActivity().getContentResolver(), 
		Settings.System.BACKLIGHT_TIMEOUT, 0)));
//        updateSummary(mTTBacklightTimeout, Integer.parseInt(mTTBacklightTimeout.getValue()));

        mTTGpuOverclock = (ListPreference) findPreference(TT_GPU_OVERCLOCK);
	mTTGpuOverclock.setOnPreferenceChangeListener(this);
        mTTGpuOverclock.setValue(Integer.toString(Settings.System.getInt(getActivity().getContentResolver(), 
		Settings.System.GPU_OVERCLOCK, 0)));
        updateSummary(mTTGpuOverclock, Integer.parseInt(mTTGpuOverclock.getValue()));

	mTTWifiPM = (ListPreference) findPreference(TT_WIFI_PM);
	mTTWifiPM.setOnPreferenceChangeListener(this);
        mTTWifiPM.setValue(Integer.toString(Settings.System.getInt(getActivity().getContentResolver(), 
		Settings.System.WIFI_PM, 0)));
        updateSummary(mTTWifiPM, Integer.parseInt(mTTWifiPM.getValue()));

        mTTLiveOC = (ListPreference) findPreference(TT_LIVEOC);
	mTTLiveOC.setOnPreferenceChangeListener(this);
        mTTLiveOC.setValue(Integer.toString(Settings.System.getInt(getActivity().getContentResolver(), 
		Settings.System.LIVEOC, 0)));
        updateSummary(mTTLiveOC, Integer.parseInt(mTTLiveOC.getValue()));

        mTTTouchscreenClock = (ListPreference) findPreference(TT_TOUCHSCREEN_CLOCK);
	mTTTouchscreenClock.setOnPreferenceChangeListener(this);
        mTTTouchscreenClock.setValue(Integer.toString(Settings.System.getInt(getActivity().getContentResolver(), 
		Settings.System.TOUCHSCREEN_CLOCK, 0)));
        updateSummary(mTTTouchscreenClock, Integer.parseInt(mTTTouchscreenClock.getValue()));

        mTTStorageSwitch = (CheckBoxPreference) findPreference(TT_STORAGE_SWITCH);
        int i = Integer.parseInt(Helpers.getSystemProp("persist.sys.vold.switchexternal", "0"));
	mTTStorageSwitch.setChecked(i == 0);

        mTTStorageAutomount = (CheckBoxPreference) findPreference(TT_STORAGE_AUTOMOUNT);
        mTTStorageAutomount.setChecked(Settings.System.getInt(getActivity().getContentResolver(),
                Settings.Secure.MOUNT_UMS_AUTOSTART, 0) == 1);

        if (Helpers.fileExists(CAPACITIVE_BUTTONS_ENABLED_FILE)) {
	    mTTEnableHardwareButtons.setEnabled(true);
        }

        if (Helpers.fileExists(CAPACITIVE_BACKLIGHT_FILE)) {
            mTTBacklightTimeout.setEnabled(true);
        }

        if (Helpers.fileExists(GPU_OVERCLOCK_FILE)) {
            mTTGpuOverclock.setEnabled(true);
        }

        if (Helpers.fileExists(WIFI_PM_FILE)) {
            mTTWifiPM.setEnabled(true);
        }

        if (Helpers.fileExists(TOUCHSCREEN_CLOCK_FILE)) {
            mTTTouchscreenClock.setEnabled(true);
        }

        mTTLiveOC.setEnabled(false); // currently disabled in kernel - removed until working again.

        if (Helpers.getSystemProp("ro.vold.switchablepair","").equals("")) {
            ((PreferenceGroup) findPreference("storage")).removePreference(mTTStorageSwitch);
        }

	// not working at the moment, will get this sorted out hopefully soon.
        ((PreferenceGroup) findPreference("storage")).removePreference(mTTStorageSwitch);
        ((PreferenceGroup) findPreference("storage")).removePreference(mTTStorageAutomount);

    }

    @Override
    public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen, Preference preference) {
        if (preference == mTTEnableHardwareButtons) {
            Settings.System.putInt(getActivity().getContentResolver(),
                    Settings.System.ENABLE_HARDWARE_BUTTONS, 
		    ((CheckBoxPreference) preference).isChecked() ? 1 : 0);
	    int val = Settings.System.getInt(getActivity().getContentResolver(), 
		    Settings.System.ENABLE_HARDWARE_BUTTONS, 0); 
	    changeKernelPref(CAPACITIVE_BUTTONS_ENABLED_FILE, val);
	    if (val == 0) {
		mTTBacklightTimeout.setEnabled(false);
	    }
	    else {
		mTTBacklightTimeout.setEnabled(true);
	    }
            return true;
        } else if (preference == mTTRecentThumbnails) {
            Settings.System.putInt(getActivity().getContentResolver(),
                    Settings.System.LARGE_RECENT_THUMBNAILS, 
		    ((CheckBoxPreference) preference).isChecked() ? 1 : 0);
            Helpers.restartSystemUI();
            return true;
        } else if (preference == mTTRightButtons) {
            Settings.System.putInt(getActivity().getContentResolver(),
                    Settings.System.RIGHT_SOFT_BUTTONS, 
		    ((CheckBoxPreference) preference).isChecked() ? 1 : 0);
            Helpers.restartSystemUI();
            return true;
        } else if (preference == mTTHideStatusbar) {
            Settings.System.putInt(getActivity().getContentResolver(),
                    Settings.System.HIDE_STATUSBAR, 
		    ((CheckBoxPreference) preference).isChecked() ? 1 : 0);
            Helpers.restartSystemUI();
            return true;
        } else if (preference == mTTPeekNotifications) {
            Settings.System.putInt(getActivity().getContentResolver(),
                    Settings.System.SHOW_NOTIFICATION_PEEK, 
		    ((CheckBoxPreference) preference).isChecked() ? 1 : 0);
            Helpers.restartSystemUI();
            return true;
        } else if (preference == mTTStorageSwitch) {
            Helpers.setSystemProp("persist.sys.vold.switchexternal", ((CheckBoxPreference) preference).isChecked() ? "1" : "0");
            return true;
        } else if (preference == mTTStorageAutomount) {
            Settings.System.putInt(getActivity().getContentResolver(),
                    Settings.Secure.MOUNT_UMS_AUTOSTART, 
		    ((CheckBoxPreference) preference).isChecked() ? 1 : 0);
            return true;
	}
        return super.onPreferenceTreeClick(preferenceScreen, preference);
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        if (preference == mTTBacklightTimeout) {
            int val = Integer.parseInt((String) newValue);
            Settings.System.putInt(getActivity().getContentResolver(),
                Settings.System.BACKLIGHT_TIMEOUT, val);
	    changeKernelPref(CAPACITIVE_BACKLIGHT_FILE, val);
//            updateSummary(mTTBacklightTimeout, val);
            return true;
        }
        if (preference == mTTGpuOverclock) {
            int val = Integer.parseInt((String) newValue);
            Settings.System.putInt(getActivity().getContentResolver(),
                Settings.System.GPU_OVERCLOCK, val);
	    changeKernelPref(GPU_OVERCLOCK_FILE, val);
            updateSummary(mTTGpuOverclock, val);
            return true;
        }
        if (preference == mTTWifiPM) {
            int val = Integer.parseInt((String) newValue);
            Settings.System.putInt(getActivity().getContentResolver(),
                Settings.System.WIFI_PM, val);
	    changeKernelPref(WIFI_PM_FILE, val);
            updateSummary(mTTWifiPM, val);
            return true;
        }
        if (preference == mTTLiveOC) {
            int val = Integer.parseInt((String) newValue);
            Settings.System.putInt(getActivity().getContentResolver(),
                Settings.System.LIVEOC, val);
	    changeKernelPref(LIVEOC_FILE, val);
            updateSummary(mTTLiveOC, val);
            return true;
        }
        if (preference == mTTTouchscreenClock) {
            int val = Integer.parseInt((String) newValue);
            Settings.System.putInt(getActivity().getContentResolver(),
                Settings.System.TOUCHSCREEN_CLOCK, val);
	    changeKernelPref(TOUCHSCREEN_CLOCK_FILE, val);
            updateSummary(mTTTouchscreenClock, val);
            return true;
        }
        return false;
    }

    public static void changeKernelPref(String file, int value) {
        final CMDProcessor cmd = new CMDProcessor();
	cmd.su.runWaitFor("busybox echo " + value + " > " + file);
    }

    public static void updateSummary(ListPreference preference, int value) {
        final CharSequence[] entries = preference.getEntries();
        final CharSequence[] values = preference.getEntryValues();
        int best = 0;
        for (int i = 0; i < values.length; i++) {
            int summaryValue = Integer.parseInt(values[i].toString());
            if (value >= summaryValue) {
                best = i;
            }
        }
        preference.setSummary(entries[best].toString());
    }
}

