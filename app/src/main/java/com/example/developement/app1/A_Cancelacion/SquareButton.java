package com.example.developement.app1.A_Cancelacion;

import android.content.Context;
import android.support.v7.widget.AppCompatButton;
import android.util.AttributeSet;

/**
 * Created by webdev_t5 on 13/10/15.
 */
public class SquareButton extends AppCompatButton {

    public SquareButton(Context context) {
        super(context);
    }

    public SquareButton(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public SquareButton(Context context, AttributeSet attributeSet, int defStyle) {
        super(context, attributeSet, defStyle);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        double height = getMeasuredWidth()*0.8155;
        setMeasuredDimension(getMeasuredWidth(), (int) height);
    }
}
