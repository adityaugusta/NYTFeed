package com.aditya.ctrl.nytfeed.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.aditya.ctrl.nytfeed.R;

public class DetailActivity extends AppCompatActivity {
    private static String url;
    private String source;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Intent intent = getIntent();
        if (intent != null) {
            url = intent.getStringExtra("URL");
            source = intent.getStringExtra("SOURCE");
        }
        getSupportActionBar().setTitle(source);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new DetailFragment())
                    .commit();
        }
    }

    public static class DetailFragment extends Fragment {
        public DetailFragment() {}
        private android.webkit.WebView webView1;
        private ProgressBar pbar;
        private String htmlContentInStringFormat;

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_detail, container, false);

            pbar = (ProgressBar) rootView.findViewById(R.id.loading);

            if (savedInstanceState != null) {
                ((android.webkit.WebView) rootView.findViewById(R.id.webView1)).restoreState(savedInstanceState);
            }

            else {
                webView1 = (android.webkit.WebView) rootView.findViewById(R.id.webView1);
                webView1.getSettings().setJavaScriptEnabled(true);
                webView1.setWebViewClient(new WebViewClient());
                webView1.setWebChromeClient(new WebChromeClient() {
                    public void onProgressChanged(android.webkit.WebView view, int progress) {
                        if (progress < 100 && pbar.getVisibility() == ProgressBar.GONE) {
                            pbar.setVisibility(ProgressBar.VISIBLE);
                        }
                        pbar.setProgress(progress);
                        if (progress == 100) {
                            pbar.setVisibility(ProgressBar.GONE);
                        }
                    }
                });
                webView1.loadUrl(url);
            }

            return rootView;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                super.onBackPressed();
                break;
        }
        return true;
    }

}