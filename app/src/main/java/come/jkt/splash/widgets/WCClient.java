package come.jkt.splash.widgets;

import android.graphics.Bitmap;
import android.util.Log;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

/**
 * Created by Allen at 2017/6/22 16:18
 */
public class WCClient extends WebChromeClient {
    private WbeClientInterface wbeClientInterface;

    public WCClient(WbeClientInterface wbeClientInterface) {
        this.wbeClientInterface = wbeClientInterface;
    }

    @Override
    public void onProgressChanged(WebView view, int newProgress) {
        super.onProgressChanged(view, newProgress);
        Log.i("webviewmessage","WebChromeClient:onProgressChanged"+newProgress);
        Log.i("aboutThread","onProgressChanged    "+Thread.currentThread().getName()+ "   "+newProgress);
        wbeClientInterface.getData(view,newProgress);
    }

    @Override
    public void onReceivedTitle(WebView view, String title) {
        super.onReceivedTitle(view, title);
        Log.i("webviewmessage","WebChromeClient:onReceivedTitle"+title);

    }

    @Override
    public void onReceivedIcon(WebView view, Bitmap icon) {
        super.onReceivedIcon(view, icon);
        Log.i("webviewmessage","WebChromeClient:onReceivedIcon"+icon);
    }

    @Override
    public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
        Log.i("webviewmessage","WebChromeClient:onJsAlert");
        Log.i("aboutThread","onJsAlert    "+Thread.currentThread().getName());
        return super.onJsAlert(view, url, message, result);
    }

    @Override
    public boolean onJsPrompt(WebView view, String url, String message, String defaultValue, JsPromptResult result) {
        Log.i("webviewmessage","WebChromeClient:onJsPrompt");
        Log.i("aboutThread","onJsPrompt    "+Thread.currentThread().getName());
        return super.onJsPrompt(view, url, message, defaultValue, result);
    }

    @Override
    public boolean onJsConfirm(WebView view, String url, String message, JsResult result) {
        Log.i("webviewmessage","WebChromeClient:onJsConfirm");
        Log.i("aboutThread","onJsConfirm    "+Thread.currentThread().getName());
        return super.onJsConfirm(view, url, message, result);
    }
    public interface WbeClientInterface{
        void getData(WebView webView, int progress);
    }
}
