package com.fyp.discussx.utils;

import android.os.SystemClock;
import android.view.View;

/**
 * Created by IMCKY on 15-Nov-17.
 */
public abstract class OnSingleClickListener implements View.OnClickListener {

    private boolean clickable = true;

    @Override
    public final void onClick(View v) {
        if (clickable) {
            clickable = false;
            onOneClick(v);
            //reset();
        }
    }


    public abstract void onOneClick(View v);

    public void reset() {
        clickable = true;
    }

}
