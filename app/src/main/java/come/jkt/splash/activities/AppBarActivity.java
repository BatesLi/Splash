package come.jkt.splash.activities;

import android.graphics.Color;
import android.os.Build;
import android.support.annotation.ColorRes;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;


import come.jkt.splash.R;
import come.jkt.splash.util.DensityUtil;
import come.jkt.splash.util.TypeUtil;

/**
 * Created by Allen at 2017/7/13 15:05
 */
public abstract class AppBarActivity extends AppCompatActivity {

    protected void initAppBar() {
        initAppBar(true, false, -1, -1);
    }

    protected void initAppBar(boolean isBack) {
        initAppBar(isBack, false, -1, -1);
    }

    protected void initAppBar(boolean isBack, boolean isRightText) {
        initAppBar(isBack, isRightText, -1, -1);
    }

    protected void initAppBar(boolean isBack, boolean isRightText, @ColorRes int bgColor, @ColorRes int textColor) {
        RelativeLayout layout = (RelativeLayout) findViewById(R.id.common_appbar_rl);
        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.common_appbar_ll);
        if (layout == null) {
            return;
        }
        invadeStatusBar();
        if (bgColor != -1) {
            linearLayout.setBackgroundResource(bgColor);
        }
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) layout.getLayoutParams();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            layoutParams.setMargins(0, DensityUtil.dp2px(this, 18), 0, 0);
        } else {
            layoutParams.setMargins(0, 0, 0, 0);
        }
        layout.setLayoutParams(layoutParams);
        ImageView iconIV = (ImageView) findViewById(R.id.common_appbar_iv);
        if (!isBack) {
            iconIV.setVisibility(View.GONE);
        } else {
            iconIV.setVisibility(View.VISIBLE);
            iconIV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onAppBarBackClick();

                }
            });
        }
        TextView centerTV = (TextView) findViewById(R.id.common_appbar_center_tv);
        if (textColor != -1) {
            centerTV.setTextColor(getResources().getColor(textColor));
        }
        centerTV.setText(TypeUtil.isBlank(setAppBarTitle()) ? "" : setAppBarTitle());
        TextView rightTV = (TextView) findViewById(R.id.common_appbar_right_tv);
        if (!isRightText) {
            rightTV.setVisibility(View.GONE);
        } else {
            rightTV.setVisibility(View.VISIBLE);
            if (textColor != -1) {
                centerTV.setTextColor(getResources().getColor(textColor));
            }
            rightTV.setText(TypeUtil.isBlank(setAppBarRightTitle()) ? "" : setAppBarRightTitle());
            rightTV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onAppBarRightClick();
                }
            });
        }
    }


    public abstract String setAppBarTitle();

    public abstract String setAppBarRightTitle();

    public abstract void onAppBarBackClick();

    public abstract void onAppBarRightClick();

    protected void invadeStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
//            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
//            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);

            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
//            window.setNavigationBarColor(Color.TRANSPARENT);
        }
    }
}

