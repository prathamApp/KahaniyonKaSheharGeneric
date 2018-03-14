package com.example.pefpr.kahaniyonkashehar.customData;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by HP on 10-11-2016.
 */

public class TektonProTextview extends android.support.v7.widget.AppCompatTextView{

    public TektonProTextview(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.setTypeface(Typeface.createFromAsset(context.getAssets(), "fonts/tektonpro-bold.otf"));
        this.setTextColor(Color.BLACK);
    }
}
