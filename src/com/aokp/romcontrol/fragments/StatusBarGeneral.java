
package com.aokp.romcontrol.fragments;

import android.content.Context;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceScreen;
import android.provider.Settings;
import android.util.Log;

import com.aokp.romcontrol.AOKPPreferenceFragment;
import com.aokp.romcontrol.R;
import com.aokp.romcontrol.util.Helpers;
import com.aokp.romcontrol.widgets.SeekBarPreference;

public class StatusBarGeneral extends AOKPPreferenceFragment implements
        OnPreferenceChangeListener {

    private static final String PREF_SETTINGS_BUTTON_BEHAVIOR = "settings_behavior";
    private static final String PREF_AUTO_HIDE_TOGGLES = "auto_hide_toggles";
    private static final String PREF_BRIGHTNESS_TOGGLE = "status_bar_brightness_toggle";
    private static final String PREF_ICON_TRANSPARENCY = "icon_transparency";
    private static final String PREF_ADB_ICON = "adb_icon";
    private static final String PREF_TRANSPARENCY = "status_bar_transparency";
    private static final String PREF_LAYOUT = "status_bar_layout";
    private static final String PREF_FONTSIZE = "status_bar_fontsize";
    private static final String PREF_STATUS_BAR_NOTIF_COUNT = "status_bar_notif_count";
    private static final String TT_RIGHT_BUTTONS = "tt_right_buttons";
    private static final String TT_PEEK_NOTIFICATIONS = "tt_peek_notifications";
    private static final String TT_HIDE_STATUSBAR = "tt_hide_statusbar";


    CheckBoxPreference mDefaultSettingsButtonBehavior;
    CheckBoxPreference mAutoHideToggles;
    CheckBoxPreference mStatusBarBrightnessToggle;
    CheckBoxPreference mTTRightButtons;
    CheckBoxPreference mTTPeekNotifications;
    CheckBoxPreference mTTHideStatusbar;
    SeekBarPreference mIconAlpha;
    CheckBoxPreference mAdbIcon;
    CheckBoxPreference mStatusBarNotifCount;
    ListPreference mTransparency;
    ListPreference mLayout;
    ListPreference mFontsize;

    Context mContext;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mContext = getActivity().getApplicationContext();

        // Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.prefs_statusbar_general);

        mDefaultSettingsButtonBehavior = (CheckBoxPreference) findPreference(PREF_SETTINGS_BUTTON_BEHAVIOR);
        mDefaultSettingsButtonBehavior.setChecked(Settings.System.getInt(mContext
                .getContentResolver(), Settings.System.STATUSBAR_SETTINGS_BEHAVIOR,
                0) == 1);

        mAutoHideToggles = (CheckBoxPreference) findPreference(PREF_AUTO_HIDE_TOGGLES);
        mAutoHideToggles.setChecked(Settings.System.getInt(mContext
                .getContentResolver(), Settings.System.STATUSBAR_QUICKTOGGLES_AUTOHIDE,
                0) == 1);

        mStatusBarBrightnessToggle = (CheckBoxPreference) findPreference(PREF_BRIGHTNESS_TOGGLE);
        mStatusBarBrightnessToggle.setChecked(Settings.System.getInt(mContext
                .getContentResolver(), Settings.System.STATUS_BAR_BRIGHTNESS_TOGGLE,
                1) == 1);

        float defaultAlpha = Settings.System.getFloat(mContext
                .getContentResolver(), Settings.System.STATUS_BAR_ICON_TRANSPARENCY,
                0.9f);
        mIconAlpha = (SeekBarPreference) findPreference(PREF_ICON_TRANSPARENCY);
        mIconAlpha.setInitValue((int) (defaultAlpha * 100));
        mIconAlpha.setOnPreferenceChangeListener(this);

        mAdbIcon = (CheckBoxPreference) findPreference(PREF_ADB_ICON);
        mAdbIcon.setChecked(Settings.Secure.getInt(getActivity().getContentResolver(),
                Settings.Secure.ADB_ICON, 1) == 1);
        
        mTransparency = (ListPreference) findPreference(PREF_TRANSPARENCY);
        mTransparency.setOnPreferenceChangeListener(this);
        mTransparency.setValue(Integer.toString(Settings.System.getInt(getActivity()
                .getContentResolver(), Settings.System.STATUS_BAR_TRANSPARENCY,
                100)));

        mLayout = (ListPreference) findPreference(PREF_LAYOUT);
        mLayout.setOnPreferenceChangeListener(this);
        mLayout.setValue(Integer.toString(Settings.System.getInt(getActivity()
                .getContentResolver(), Settings.System.STATUS_BAR_LAYOUT,
                0)));

        mStatusBarNotifCount = (CheckBoxPreference) findPreference(PREF_STATUS_BAR_NOTIF_COUNT);
        mStatusBarNotifCount.setChecked(Settings.System.getInt(mContext
                .getContentResolver(), Settings.System.STATUS_BAR_NOTIF_COUNT,
                0) == 1);

        mFontsize = (ListPreference) findPreference(PREF_FONTSIZE);
        mFontsize.setOnPreferenceChangeListener(this);
        mFontsize.setValue(Integer.toString(Settings.System.getInt(getActivity()
                .getContentResolver(), Settings.System.STATUSBAR_FONT_SIZE,
                16)));

        mTTRightButtons = (CheckBoxPreference) findPreference(TT_RIGHT_BUTTONS);
        mTTRightButtons.setChecked(Settings.System.getInt(getActivity().getContentResolver(),
                Settings.System.RIGHT_SOFT_BUTTONS, 0) == 1);

        mTTHideStatusbar = (CheckBoxPreference) findPreference(TT_HIDE_STATUSBAR);
        mTTHideStatusbar.setChecked(Settings.System.getInt(getActivity().getContentResolver(),
                Settings.System.HIDE_STATUSBAR, 0) == 1);

        mTTPeekNotifications = (CheckBoxPreference) findPreference(TT_PEEK_NOTIFICATIONS);
        mTTPeekNotifications.setChecked(Settings.System.getInt(getActivity().getContentResolver(),
                Settings.System.SHOW_NOTIFICATION_PEEK, 0) == 1);


        if (mTablet) {
            PreferenceScreen prefs = getPreferenceScreen();
            prefs.removePreference(mStatusBarBrightnessToggle);
            prefs.removePreference(mAutoHideToggles);
            prefs.removePreference(mDefaultSettingsButtonBehavior);
            prefs.removePreference(mTransparency);
            prefs.removePreference(mLayout);
            prefs.removePreference(mIconAlpha); // broken.
        }
    }

    @Override
    public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen,
            Preference preference) {
        if (preference == mDefaultSettingsButtonBehavior) {

            Settings.System.putInt(mContext.getContentResolver(),
                    Settings.System.STATUSBAR_SETTINGS_BEHAVIOR,
                    ((CheckBoxPreference) preference).isChecked() ? 1 : 0);
            return true;

        } else if (preference == mAutoHideToggles) {

            Settings.System.putInt(mContext.getContentResolver(),
                    Settings.System.STATUSBAR_QUICKTOGGLES_AUTOHIDE,
                    ((CheckBoxPreference) preference).isChecked() ? 1 : 0);
            return true;

        } else if (preference == mStatusBarBrightnessToggle) {

            Settings.System.putInt(mContext.getContentResolver(),
                    Settings.System.STATUS_BAR_BRIGHTNESS_TOGGLE,
                    ((CheckBoxPreference) preference).isChecked() ? 1 : 0);
            return true;

        } else if (preference == mAdbIcon) {

            boolean checked = ((CheckBoxPreference) preference).isChecked();
            Settings.Secure.putInt(getActivity().getContentResolver(),
                    Settings.Secure.ADB_ICON, checked ? 1 : 0);
            return true;
            
        } else if (preference == mStatusBarNotifCount) {
            Settings.System.putInt(mContext.getContentResolver(),
                    Settings.System.STATUS_BAR_NOTIF_COUNT,
                    ((CheckBoxPreference) preference).isChecked() ? 1 : 0);
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
        } 

        return super.onPreferenceTreeClick(preferenceScreen, preference);

    }
    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        mContext = getActivity().getApplicationContext();
	
       if (preference == mIconAlpha) {
            float val = Float.parseFloat((String) newValue);
            Settings.System.putFloat(mContext.getContentResolver(),
                    Settings.System.STATUS_BAR_ICON_TRANSPARENCY,
                    val / 100);
            return true;
        } else if (preference == mTransparency) {
            int val = Integer.parseInt((String) newValue);
            Settings.System.putInt(getActivity().getContentResolver(),
                    Settings.System.STATUS_BAR_TRANSPARENCY, val);
            Helpers.restartSystemUI();
            return true;
        } else if (preference == mLayout) {
            int val = Integer.parseInt((String) newValue);
            Settings.System.putInt(getActivity().getContentResolver(),
                    Settings.System.STATUS_BAR_LAYOUT, val);
            Helpers.restartSystemUI();
            return true;
        } else if (preference == mFontsize) {
            int val = Integer.parseInt((String) newValue);
            Settings.System.putInt(getActivity().getContentResolver(),
                    Settings.System.STATUSBAR_FONT_SIZE, val);
            Helpers.restartSystemUI();
            return true;
        }
        return false;
    }
}
