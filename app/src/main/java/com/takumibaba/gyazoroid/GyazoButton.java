package com.takumibaba.gyazoroid;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.Button;

/**
 * Created by takumi on 2015/03/16.
 */
public class GyazoButton extends Button{
    private Context mContext;
    private Uri mUri;
    private String mAccessToken = "";
    public GyazoButton(Context c){
        super(c);
    }

    private OnClickListener onClick = new OnClickListener() {
        @Override
        public void onClick(View v) {
            Log.d("GyazoButton", "hogggggg");
        }
    };
}
