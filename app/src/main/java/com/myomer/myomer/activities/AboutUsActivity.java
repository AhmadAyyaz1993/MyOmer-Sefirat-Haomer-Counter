package com.sefirah.myomer.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.sefirah.myomer.R;

public class AboutUsActivity extends AppCompatActivity {
    WebView wvAboutUs;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_about_us);
        wvAboutUs = (WebView) findViewById(R.id.wvAboutUs);
        WebSettings webSetting = wvAboutUs.getSettings();
        webSetting.setBuiltInZoomControls(true);
        webSetting.setJavaScriptEnabled(true);

        wvAboutUs.setWebViewClient(new WebViewClient());
        wvAboutUs.loadUrl("file:///android_asset/www/about_us.html");

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        AboutUsActivity.this.finish();
    }

    private class WebViewClient extends android.webkit.WebViewClient
    {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url)
        {
            return super.shouldOverrideUrlLoading(view, url);
        }
    }
}
