package come.jkt.splash.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;


import come.jkt.splash.R;
import come.jkt.splash.util.ConstantUtil;
import come.jkt.splash.widgets.WCClient;

/**
 * Created by 天哥哥
 */
public class ADActivity extends AppBarActivity implements WCClient.WbeClientInterface {
    private WebView mWV;
    private String mUrl;
    private ProgressBar mPB;
    private String mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initIntent();
        initViews();
        initAppBar(true);
        initObjects();
        initWork();
        initState();

    }

    private void initIntent() {
        Intent intent = getIntent();
        if (intent == null) {
            return;
        }
        mUrl = intent.getStringExtra(ConstantUtil.Intent.AD_CONTENT_URL);
        mTitle = intent.getStringExtra(ConstantUtil.Intent.AD_TITLE);
    }

    private void initViews() {
        setContentView(R.layout.activity_ad);
        mWV = (WebView) findViewById(R.id.ad_wv);
        mPB = (ProgressBar) findViewById(R.id.ad_pb);

    }

    private void initObjects() {
        initDB();
    }

    private void initDB() {

    }

    private void initWork() {
        setLayout();
        setAdapter();
        wvWork();
    }

    private void wvWork() {
        mWV.setWebViewClient(new WebViewClient());
        mWV.setWebChromeClient(new WCClient(this));
        WebSettings settings = mWV.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setDomStorageEnabled(true);
        settings.setSupportZoom(true);
        mWV.loadUrl(mUrl);

    }

    private void setAdapter() {

    }

    private void setLayout() {

    }

    private void initState() {

    }


    @Override
    public String setAppBarTitle() {
        return mTitle;
    }

    @Override
    public String setAppBarRightTitle() {
        return null;
    }

    @Override
    public void onAppBarBackClick() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    @Override
    public void onAppBarRightClick() {
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (mWV != null && mWV.canGoBack()) {
            mWV.goBack();
            return true;
        }
        //toast(博客介绍使用,实际开发中应该删除)
        showToast("关闭广告页,进入首页面");
        onAppBarBackClick();
        return true;
    }

    @Override
    public void getData(WebView webView, int progress) {
        mPB.setProgress(progress);
        if (progress == 100) {
            mPB.setVisibility(View.GONE);
        } else {
            mPB.setVisibility(View.VISIBLE);
        }
    }
    private void showToast(String str) {
        Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
    }

}
