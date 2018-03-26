package come.jkt.splash.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import come.jkt.splash.R;

public class MainActivity extends AppBarActivity {

    private long mTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initAppBar(false);
    }

    @Override
    public String setAppBarTitle() {
        return "首页";
    }

    @Override
    public String setAppBarRightTitle() {
        return null;
    }

    @Override
    public void onAppBarBackClick() {
    }

    @Override
    public void onAppBarRightClick() {

    }

    @Override
    public void onBackPressed() {
        long timeMillis = System.currentTimeMillis();
        if (timeMillis - mTime > 2000) {
            Toast.makeText(this, "再点一次退出", Toast.LENGTH_SHORT).show();
            mTime = timeMillis;
            return;
        }
        super.onBackPressed();
    }
}
