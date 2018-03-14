package com.example.pefpr.kahaniyonkashehar.customData;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.Button;

/**
 * Created by HP on 02-12-2016.
 */

public class TektonProButton extends android.support.v7.widget.AppCompatButton {

    public TektonProButton(Context context) {
        super(context);
        setFont();
    }

    public TektonProButton(Context context, AttributeSet attrs) {
        super(context, attrs);
//        CustomFontHelper.setCustomFont(this, context, attrs);
        setFont();
    }

    public TektonProButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
//        CustomFontHelper.setCustomFont(this, context, attrs);
        setFont();
    }

    private void setFont() {
        Typeface font = Typeface.createFromAsset(getContext().getAssets(),
                "fonts/tektonpro-bold.otf");
        setTypeface(font, Typeface.NORMAL);
    }
}
