package com.shop.e.eshopl.util;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

/**
 * Created by lt on 2017-05-24.
 */

public class CarouselLayout extends RelativeLayout{
    /**
     * 一般在代码中使用
     * @param context
     */
    public CarouselLayout(Context context) {
        super(context);
    }

    /**
     * 布局中使用
     * @param context
     * @param attrs
     */
    public CarouselLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * 布局中使用，并设置style
     * @param context
     * @param attrs
     * @param defStyleAttr
     */
    public CarouselLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     * 布局填充
     */
    public void init(Context mContext){

    }

}
