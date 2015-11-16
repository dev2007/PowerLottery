package com.awu.powerlottery.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.TextView;

import com.awu.powerlottery.R;

/**
 * Created by awu on 2015-11-10.
 */
public class ImageTextView extends ImageView {
    private TextView textView;
    private int resourceId;
    private String text;

    public ImageTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ImageTextView(Context context,AttributeSet attrs,int defStyle){
        super(context,attrs,defStyle);
    }

    private void init(Context context,AttributeSet attrs){
        textView = new TextView(context);
        TypedArray typedArray = context.obtainStyledAttributes(attrs,
                R.styleable.ImageText);

        resourceId = typedArray.getResourceId(R.styleable.ImageText_Text, 0);

        try {
            text = typedArray.getResources().getText(resourceId).toString();
        }catch (Exception e){
            text = typedArray.getString(R.styleable.ImageText_Text);
        }finally {
            textView.setText(text);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        textView.draw(canvas);
    }
}
