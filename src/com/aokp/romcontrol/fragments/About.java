
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
    Preference mSGT7Features;
    Preference mSGT7SiteUrl;
    Preference mSGT7XDAUrl;
    Preference mSGT7SourceUrl;
    Preference mSGT7TE4MSourceUrl;
    Preference mSiteUrl;
    Preference mSourceUrl;
    Preference mReviewUrl;
    Preference mIrcUrl;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.prefs_about);
        mSGT7About = findPreference("about_aokp_sgt7");
        mSGT7Features = findPreference("aokp_sgt7_features");
        mSGT7SiteUrl = findPreference("aokp_sgt7_website");
        mSGT7XDAUrl = findPreference("aokp_sgt7_XDA");
        mSGT7SourceUrl = findPreference("aokp_sgt7_source");
        mSGT7TE4MSourceUrl = findPreference("aokp_sgt7_TE4M");
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
	} else if (preference == mSGT7Features) {	
                Resources res = getActivity().getResources();
                String detailsMessage = res.getString(R.string.aokp_sgt7_features_details);
                String ok = res.getString(R.string.ok);

                new AlertDialog.Builder(getActivity())
			.setTitle(res.getString(R.string.aokp_sgt7_features))
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
            launchUrl("http://coming.very.soon/");
        } else if (preference == mSGT7XDAUrl) {
            launchUrl("http://forum.xda-developers.com/showthread.php?t=1523174");
        } else if (preference == mSGT7SourceUrl) {
            launchUrl("http://github.com/aokp-sgt7");
        } else if (preference == mSGT7TE4MSourceUrl) {
            launchUrl("http://github.com/sgt7");
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
