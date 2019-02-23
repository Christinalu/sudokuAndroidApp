package com.omicron.android.cmpt276_1191e1_omicron;

import android.content.Context;
import android.graphics.Rect;
import android.support.v7.widget.AppCompatTextView;
import android.widget.TextView;

public class TextViewMany extends TextView
{
    /*
     * This class extends Text View and attempts to fix marquee resetting each time a new word is inserted by btn press
     */

    public TextViewMany(Context context) {
        super(context);
    }

    @Override
    protected void onFocusChanged(boolean focused, int direction, Rect previouslyFocusedRect) {
        if(focused)
            super.onFocusChanged(focused, direction, previouslyFocusedRect);
    }

    @Override
    public void onWindowFocusChanged(boolean focused) {
        if(focused)
            super.onWindowFocusChanged(focused);
    }


    @Override
    public boolean isFocused() {
        return true;
    }
}
