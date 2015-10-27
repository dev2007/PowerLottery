package com.awu.powerlottery.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.awu.powerlottery.R;

/**
 * Created by awu on 2015-10-27.
 */
public class ImageText extends FrameLayout {
    private String text = "";
    private int resourceId;
    private ImageView imageView;
    private TextView textView;

    public ImageText(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.imagetext, this);

        initView(context, attrs);
    }


    private void initView(Context context, AttributeSet attrs){
        imageView = (ImageView)findViewById(R.id.iv);
        textView = (TextView)findViewById(R.id.tv);

        TypedArray typedArray = context.obtainStyledAttributes(attrs,
                R.styleable.ImageText);

        for (int i= 0;i < typedArray.getIndexCount();i++){
            int attr = typedArray.getIndex(i);
            switch (attr){
                case R.styleable.ImageText_Text:
                    resourceId = typedArray.getResourceId(R.styleable.ImageText_Text, 0);

                    try {
                        text = typedArray.getResources().getText(resourceId).toString();
                    }catch (Exception e){
                        text = typedArray.getString(R.styleable.ImageText_Text);
                    }finally {
                        textView.setText(text);
                    }
                    break;
                case R.styleable.ImageText_Src:
                    resourceId = typedArray.getResourceId(R.styleable.ImageText_Src,0);
                    if(resourceId > 0)
                        imageView.setImageResource(resourceId);
                default:
                    break;
            }
        }
        typedArray.recycle();
    }

    public void setText(CharSequence text){
        textView.setText(text);
    }
}
