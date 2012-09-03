
package com.aokp.romcontrol.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceScreen;

import com.aokp.romcontrol.AOKPPreferenceFragment;
import com.aokp.romcontrol.R;

public class About extends AOKPPreferenceFragment {

    public static final String TAG = "About";


    Preference mSGT7About;
    Preference mSGT7SiteUrl;
    Preference mSGT7XDAUrl;
    Preference mSGT7Facebook;
    Preference mSGT7Twitter;
    Preference mSGT7GooglePlus;
    Preference mSGT7SourceUrl;
    Preference mSiteUrl;
    Preference mSourceUrl;
    Preference mReviewUrl;
    Preference mIrcUrl;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.prefs_about);
        mSGT7About = findPreference("about_aokp_sgt7");
        mSGT7SiteUrl = findPreference("aokp_sgt7_website");
        mSGT7XDAUrl = findPreference("aokp_sgt7_XDA");
        mSGT7Facebook = findPreference("aokp_sgt7_fb");
        mSGT7Twitter = findPreference("aokp_sgt7_twitter");
        mSGT7GooglePlus = findPreference("aokp_sgt7_gplus");
        mSGT7SourceUrl = findPreference("aokp_sgt7_source");
        mSiteUrl = findPreference("aokp_website");
        mSourceUrl = findPreference("aokp_source");
        mReviewUrl = findPreference("aokp_review");
        mIrcUrl = findPreference("aokp_irc");

    }

    @Override
    public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen, Preference preference) {
	if (preference == mSGT7About) {	
                Resources res = getActivity().getResources();
                String detailsMessage = res.getString(R.string.about_aokp_sgt7_details);
                String ok = res.getString(R.string.ok);

                new AlertDialog.Builder(getActivity())
			.setTitle(res.getString(R.string.about_aokp_sgt7))
                        .setMessage(detailsMessage)
                        .setPositiveButton(ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        })
                        .create()
                        .show();
                return true;
        } else if (preference == mSGT7SiteUrl) {
            launchUrl("http://aokp.sgt7.net/");
        } else if (preference == mSGT7XDAUrl) {
            launchUrl("http://forum.xda-developers.com/showthread.php?t=1523174");
        } else if (preference == mSGT7SourceUrl) {
            launchUrl("http://github.com/aokp-sgt7");
        } else if (preference == mSGT7Facebook) {
            launchUrl("http://facebook.com/aokp-sgt7");
        } else if (preference == mSGT7Twitter) {
            launchUrl("http://twitter.com/aokpsgt7");
        } else if (preference == mSGT7GooglePlus) {
            launchUrl("http://gplus.to/aokpsgt7");
        } else if (preference == mSiteUrl) {
            launchUrl("http://aokp.co/");
        } else if (preference == mSourceUrl) {
            launchUrl("http://github.com/aokp");
	} else if (preference == mReviewUrl) {
            launchUrl("http://gerrit.aokp.co");
        } else if (preference == mIrcUrl) {
            launchUrl("http://webchat.freenode.net/?channels=teamkang");
        }
        return super.onPreferenceTreeClick(preferenceScreen, preference);
    }

    private void launchUrl(String url) {
        Uri uriUrl = Uri.parse(url);
        Intent donate = new Intent(Intent.ACTION_VIEW, uriUrl);
        getActivity().startActivity(donate);
    }
}
