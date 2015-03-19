package com.takumibaba.gyazoroid;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PixelFormat;
import android.net.Uri;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

/**
 * Created by takumi on 2015/03/16.
 */
public class GyazoService extends IntentService{
    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    private static final String TAG = "GyazoService";
    private Handler mHandler = null;
    public GyazoService(){
        super(TAG);
    }
    public GyazoService(String name) {
        super(name);
    }
    private Context mContext;
    private Uri mUri;
    private String api = "http://upload.gyazo.com/api/upload";
    private String mAccessToken = "";

    @Override
    protected void onHandleIntent(final Intent intent) {
        mHandler = new Handler();
        mContext = getApplicationContext();
        mUri = (Uri) intent.getParcelableExtra("data");
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        mAccessToken = pref.getString("gyazo_access_token", "");
        boolean isAlwaysUpload = pref.getBoolean("is_always_upload", false);
        if(isAlwaysUpload == true){
            upload();
        }else{
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    final WindowManager manager= (WindowManager) getSystemService(WINDOW_SERVICE);
                    WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                            WindowManager.LayoutParams.WRAP_CONTENT,
                            WindowManager.LayoutParams.WRAP_CONTENT,
                            WindowManager.LayoutParams.TYPE_PHONE,
                            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                            PixelFormat.TRANSLUCENT);
                    params.gravity = Gravity.CENTER;

//                    final Button button = new Button(mContext);
                    final ImageButton button = new ImageButton(mContext);
                    button.setImageResource(R.drawable.gyazo);
//                    button.setImageAlpha(80);
                    button.setOnClickListener(clickListener);

                    manager.addView(button.getRootView(), params);
                    TimerTask task = new TimerTask() {
                        @Override
                        public void run() {
                            manager.removeView(button);
                        }
                    };
                    Timer timer = new Timer();
                    GyazoReceiver.completeWakefulIntent(intent);
                    timer.schedule(task, TimeUnit.SECONDS.toMillis(5));
                }
            });
        }


    }

    private void upload(){
        AsyncHttpClient client = new AsyncHttpClient();
        InputStream is = null;
        RequestParams params = null;
        try {
            is = mContext.getContentResolver().openInputStream(mUri);
            params = new RequestParams();
            params.put("imagedata", is);
            params.put("access_token", mAccessToken);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        client.post(api, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                Log.d(TAG, "onSuccess" + String.valueOf(statusCode));
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.d(TAG, "onFailure" + String.valueOf(statusCode));
            }
        });
    }

    private View.OnClickListener clickListener = new View.OnClickListener(){

        @Override
        public void onClick(View v) {
            upload();
        }
    };
}
