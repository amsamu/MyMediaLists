package com.amsamu.mymedialists;

import static com.amsamu.mymedialists.util.SharedMethods.openLinkInBrowser;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.amsamu.mymedialists.databinding.ActivityAboutBinding;

public class AboutActivity extends AppCompatActivity {

    ActivityAboutBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAboutBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.topAppBar.setNavigationOnClickListener(v -> finish());

        binding.appVersion.setText("v" + BuildConfig.VERSION_NAME);

        setUpClickableElements();
    }

    public void setUpClickableElements() {
        // APP LINKS
        binding.appAuthorLayout.setOnClickListener(v -> {
            openLinkInBrowser(this, R.string.app_developer_link);
        });

        binding.appLicenseLayout.setOnClickListener(v -> {
            openLinkInBrowser(this, R.string.app_license_link);
        });

        binding.appSourceCodeLayout.setOnClickListener(v -> {
            openLinkInBrowser(this, R.string.app_source_code_link);
        });

        binding.appPrivacyPolicyLayout.setOnClickListener(v -> {
            openLinkInBrowser(this, R.string.app_privacy_policy_link);
        });

        // ICONS
        binding.appIconCreditsLayout.setOnClickListener(v -> {
            openLinkInBrowser(this, R.string.app_icon_credits_link);
        });

        // THIRD PARTY LICENSES
        binding.thirdPartyGsonLayout.setOnClickListener(v -> {
            openLinkInBrowser(this, R.string.gson_license_link);
        });
    }

}