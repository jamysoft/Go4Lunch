package com.openclassrooms.go4lunch.ui;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.openclassrooms.go4lunch.R;

import java.util.Objects;

public class WebViewActivity extends AppCompatActivity {
WebView mWebView;
TextView mTextView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
        setSupportActionBar(findViewById(R.id.toolbar));
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        mWebView =findViewById(R.id.mywebview);
        mTextView=findViewById(R.id.nositemessage);
        displayMyWebView();

    }

    private void displayMyWebView() {
        WebSettings webSettings= mWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        mWebView.setWebViewClient(new Callback());
        String url = getIntent().getStringExtra("Website");
        if (url != null){
            mTextView.setVisibility(View.INVISIBLE);
            mWebView.getSettings().setJavaScriptEnabled(true);
            mWebView.getSettings().setLoadsImagesAutomatically(true);
            mWebView.loadUrl(url);
            mWebView.setWebViewClient(new WebViewClient());

        }else{
            mTextView.setText(" NO AVAILABLE WEB SITE FOR THIS RESTAURANT");
            mTextView.setVisibility(View.VISIBLE);
        }
    }

    private class Callback extends WebViewClient {
        @Override
        public boolean shouldOverrideKeyEvent(WebView view, KeyEvent event) {
            return super.shouldOverrideKeyEvent(view, event);
        }
    }
}