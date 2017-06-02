package com.shop.e.eshopl.util;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.shop.e.eshopl.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 商品数量选择器
 * Created by lt on 2017-06-01.
 * <p>
 * 1.视图方面：包括两个+ - 的图标，展示数量的文本
 * 2.+ -有点击事件，数量进行相应的增减变化
 * 3.对外提供一个数量变化的监听，将变化后的商品数量提供出去
 * <p>
 * 在这个自定义控件中，学习自定义属性
 */

public class SimpleNumberPicker extends RelativeLayout {

    @BindView(R.id.image_minus)
    ImageView imageMinus;
    @BindView(R.id.text_number)
    TextView textNumber;
    @BindView(R.id.image_plus)
    ImageView imagePlus;
    private int min=0;
    public OnNumberChangedListener onNumberChangedListener;
    /**
     * 代码中使用
     *
     * @param context
     */
    public SimpleNumberPicker(Context context) {
        super(context);
        init(context,null);
    }

    /**
     * 布局中使用
     *
     * @param context
     * @param attrs
     */
    public SimpleNumberPicker(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context,attrs);
    }

    /**
     * 布局中使用，且设置了Style样式
     * @param context
     * @param attrs
     * @param defStyleAttr
     */
    public SimpleNumberPicker(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context,attrs);
    }

    /**
     * 填充布局
     */
    public void init(Context context,AttributeSet attrs) {
        LayoutInflater.from(context).inflate(R.layout.widget_simple_number_picker, this,true);
        ButterKnife.bind(this);
        /**
         *    自定义属性的流程
         * 1.需要创建属性集以及属性值:在res和values下创建一个attrs.xml文件
         *    <attr name="min_number" format="integer"/>
         * 2.布局中作为一个属性使用
         * 3.获取设置的属性值
         */
        if(attrs==null) return;
        //属性集
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.SimpleNumberPicker);
        //获取定义的属性的值
        min = typedArray.getInteger(R.styleable.SimpleNumberPicker_min_number, 0);
        typedArray.recycle(); //清空 重复循环使用TypedArray

        //处理视图
        setNumber(min);
        imageMinus.setImageResource(R.mipmap.btn_minus_gray);
    }
    /**
     * 提供一个设置数量的方法
     * @param number
     */
    public void setNumber(int number) {
        if(number<min){
            throw new IllegalArgumentException("Min Number is:"+min+"while Number is:"+number);
        }
        textNumber.setText(String.valueOf(number));
    }

    /**
     * 获取数量的方法
     */
    public int getCount(){
        String s = textNumber.getText().toString();
        return Integer.parseInt(s);
    }
    @OnClick({R.id.image_minus, R.id.image_plus})
    public void onViewClicked(View view) {
        if(view.getId()==R.id.image_minus){
            //点击减少
            if(getCount()==min){
                //不能再减少了
                return;
            }
            setNumber(getCount()-1);
        }else{
            //点击增加
            setNumber(getCount()+1);
        }
        //减少图标的样式
        if(getCount()==min){
            imageMinus.setImageResource(R.mipmap.btn_minus_gray);
        }else{
            imageMinus.setImageResource(R.mipmap.btn_minus);
        }
        //选择的数量发生变化以后，要实时告诉别人，将数量传递出去
        if(onNumberChangedListener!=null){
            onNumberChangedListener.onNumberChange(getCount());
        }
    }
    /**
     * 对外提供一个接口，写传递数量变化的方法
     */
    public interface OnNumberChangedListener{
        void onNumberChange(int number);
    }
    /**
     * 提供一个设置监听的方法
     */
    public void setOnNumberChangedListener(OnNumberChangedListener onNumberChangedListener){
        this.onNumberChangedListener=onNumberChangedListener;
    }


}
