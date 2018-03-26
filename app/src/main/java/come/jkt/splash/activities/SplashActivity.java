package come.jkt.splash.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import come.jkt.splash.bean.ADBean;
import come.jkt.splash.service.ADIntentService;
import come.jkt.splash.util.ConstantUtil;
import come.jkt.splash.util.DensityUtil;
import come.jkt.splash.R;
import come.jkt.splash.util.SPManager;
import come.jkt.splash.util.TypeUtil;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class SplashActivity extends AppCompatActivity implements View.OnClickListener {

    private CountDownTimer mDownTimer;
    private boolean mIsStopTimer;
    private TextView mVersionTV;
    private ADBean mAdBean;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        invadeStatusBar();
        addVersionView();
        initNet();
    }

    private void initNet() {
        //此处应该是一个数据获取，请求后端广告接口拿到图片url。
        //应该设置连接和读写超时时间。一旦超时，直接进入主页面。不会让用户过久等待
        //我这边模拟个网络操作，拿到url
        Request request = new Request.Builder().url("https://www.baidu.com").build();
        OkHttpClient client = new OkHttpClient.Builder()
                //一秒钟连接超时、读超时、写超时（避免过长事件启动页面等待）
                .connectTimeout(1000, TimeUnit.MILLISECONDS)
                .readTimeout(1000, TimeUnit.MILLISECONDS)
                .writeTimeout(1000, TimeUnit.MILLISECONDS)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                //失败相应 包括超时等等，直接进入首页
                startActivity(MainActivity.class);
                finish();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                //成功响应 应该从里面拿到数据，我采用模拟图片url
                //广告图片url
                String imgUrl = "http://img.zcool.cn/community/01e51e581074cda84a0d304ff02d18.png@1280w_1l_2o_100sh.png";
                //广告标题
                String title = "百度首页";
                //广告内容地址（webView呈现）
                String contentUrl = "http://www.baidu.com";
                //最好将ad广告对象封装起来，结构有广告图片url,广告标题，广告内容url；
                mAdBean = new ADBean(title, imgUrl, contentUrl);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adWork(mAdBean);
                    }
                });
            }
        });

    }

    private void adWork(ADBean bean) {
        // 如果图片已经下载到了本地，那么就加载该图片。如果没有的话，就下载并进入首页
        String adFilePath = SPManager.getADFilePath(this);
        //本地没有图片url对应的本地路径（之前没有下载该图片）
        if (TypeUtil.isBlank(adFilePath)) {
            //图片下载后路径会存入sp。之后通过sp拿取数据。
            // 不过建议，本地数据库存多个图片（可以切换显示广告），多图片优先级跟你们后端商定.
            //开启intentService下载图片.并存入本地.用sp来获取图片本地路径
            Intent intent = new Intent(this, ADIntentService.class);
            intent.putExtra("downUrl", bean.getImgUrl());
            startService(intent);
            //同时进入首页
            startActivity(MainActivity.class);
            //toast(博客介绍使用,实际开发中应该删除)
            showToast("本地无图片资源,启动service下载图片");
            finish();
            return;
        }
        //之前已经下载了该图片，显示广告图片，点击打开新的activity，加载广告内容
        initViews(adFilePath);

    }


    private void initViews(String adValue) {
        setContentView(R.layout.ad_layout);
        final View layout = findViewById(R.id.splash_rl);
        ImageView imageView = (ImageView) findViewById(R.id.splash_iv);
        final Button button = (Button) findViewById(R.id.splash_bn);
        if (!TypeUtil.isBlank(adValue)) {
            Glide.with(this).load(adValue).placeholder(R.drawable.splash).crossFade().into(new GlideDrawableImageViewTarget(imageView) {
                @Override
                public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> animation) {
                    super.onResourceReady(resource, animation);
                    layout.setVisibility(View.VISIBLE);
                    button.setVisibility(View.VISIBLE);
                }
            });
        }
        //显示广告图片，开始倒计时
        mDownTimer = new CountDownTimer(4000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                button.setText("跳过 " + millisUntilFinished / 1000 + "s");
            }

            @Override
            public void onFinish() {
                button.setText("跳过 " + 0 + "s");
                if (!mIsStopTimer) {
                    startActivity(MainActivity.class);
                    finish();
                }
            }
        };
        mDownTimer.start();
        //注意，如果点击进入广告内容activity，中断mDownTimer的计时，防止首页打开
        button.setOnClickListener(this);
        layout.setOnClickListener(this);
    }

    private void addVersionView() {
        mVersionTV = new TextView(this);
        mVersionTV.setTextColor(getResources().getColor(R.color.colorWhite));
        mVersionTV.setTextSize(14);
        mVersionTV.setText("v 1.0.0");
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams
                (FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
        params.gravity = Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL;
        mVersionTV.setPadding(0, 0, 0, DensityUtil.dp2px(this, 20));
        addContentView(mVersionTV, params);
    }

    public void removeVersionView() {
        if (TypeUtil.isNull(mVersionTV)) {
            return;
        }
        mVersionTV.setVisibility(View.GONE);
    }

    protected void invadeStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.splash_bn:
                //toast(博客介绍使用,实际开发中应该删除)
                showToast("点击跳过");
                stopDownTimer();
                startActivity(MainActivity.class);
                finish();
                break;
            case R.id.splash_rl:
                //toast(博客介绍使用,实际开发中应该删除)
                showToast("点击广告图片,进入详情");
                stopDownTimer();
                Intent intent = new Intent(this, ADActivity.class);
                intent.putExtra(ConstantUtil.Intent.AD_TITLE, mAdBean.getTitle());
                intent.putExtra(ConstantUtil.Intent.AD_CONTENT_URL, mAdBean.getContentUrl());
                startActivity(intent);
                finish();
                break;
        }
    }

    private void stopDownTimer() {
        if (mDownTimer != null) {
            mDownTimer.cancel();
        }
        mIsStopTimer = true;
    }

    private void startActivity(Class tClass) {
        startActivity(new Intent(SplashActivity.this, tClass));
    }

    private void showToast(String str) {
        Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
    }
}
