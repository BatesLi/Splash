package come.jkt.splash.service;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;


import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import come.jkt.splash.util.FileUtil;
import come.jkt.splash.util.SPManager;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * helper methods.
 */
public class ADIntentService extends IntentService {
    public static final String ACTION_DOWNLOAD_AD = "com.jkt.update.action.downloadAD";

    public ADIntentService() {
        super("ADIntentService");
    }

    /**
     * Starts this service to perform action Foo with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */


    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            //单一action  无需action判断
            Log.i("theadintent", "start ......");
            adWork(intent);
//            final String action = intent.getAction();
//            switch (action) {
//                case ADIntentService.ACTION_DOWNLOAD_AD:
//            adWork(intent);
//                    break;
//                default:
//                    break;
//        }
        }
    }

    private void adWork(Intent intent) {
        File file = adIo(intent);
        if (file == null || file.length() <= 0) {
            return;
        }
        SPManager.setADFilePath(this, file.getAbsolutePath());
    }


    private File adIo(Intent intent) {
        File updateFile = FileUtil.getDiskCacheDir(getApplicationContext(), intent.getStringExtra("name") + System.currentTimeMillis() + ".apk");
        try {
            URL url = new URL(intent.getStringExtra("downUrl"));
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestProperty("Accept-Encoding", "identity");
            conn.connect();
            int length = conn.getContentLength();
            InputStream inputStream = conn.getInputStream();
            FileOutputStream fos = new FileOutputStream(updateFile, true);
            int oldProgress = 0;
            byte buf[] = new byte[1024 * 8];
            while (true) {
                int num = inputStream.read(buf);
                if (num <= 0) {
                    break;
                }
                fos.write(buf, 0, num);
                fos.flush();
            }
            fos.flush();
            fos.close();
            inputStream.close();
        } catch (Exception e) {
            Log.i("updateException", e.toString());
            return null;
        }
        return updateFile;
    }


}
