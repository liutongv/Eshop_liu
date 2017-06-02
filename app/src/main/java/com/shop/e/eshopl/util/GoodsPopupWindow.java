package com.shop.e.eshopl.util;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.shop.e.eshopl.R;
import com.shop.e.eshopl.base.MyBaseActivity;
import com.shop.e.eshopl.entity.GoodsInfo;
import com.shop.e.eshopl.wrapper.ToastWrapper;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 商品选择的弹窗
 * Created by lt on 2017-06-02.
 */

public class GoodsPopupWindow extends PopupWindow implements PopupWindow.OnDismissListener {
    /**
     * 1.布局的填充：构造方法中完成
     * 2.展示的方法：show方法
     * 3.当我们显示和隐藏的时候，当前Activity的背景透明度进行变化
     * 4.商品数据的展示：需要商品的数据，可以在构造方法中将数据传递过来
     * 5.当我们选择了商品之后，商品的数量需要给使用者传递出去，让具体的事件交给使用者去实现
     */
    MyBaseActivity mActivity;
    GoodsInfo mGoodsInfo;
    private final ViewGroup parent;
    OnConfirmListener onConfirmListener;

    @BindView(R.id.image_goods)
    ImageView mIvGoods;
    @BindView(R.id.text_goods_price)
    TextView mTvPrice;
    @BindView(R.id.text_inventory_value)
    TextView mTvInventoryValue;
    @BindView(R.id.text_number_value)
    TextView mTvNumberValue;
    @BindView(R.id.number_picker)
    SimpleNumberPicker mNumberPicker;

    public GoodsPopupWindow(MyBaseActivity activity, @NonNull GoodsInfo goodsInfo) {

        mActivity = activity;
        mGoodsInfo = goodsInfo;
        //获取顶层视图
        parent = (ViewGroup) activity.getWindow().getDecorView();
        Context context = parent.getContext();
        //布局的填充
        View view = LayoutInflater.from(activity).inflate(R.layout.popup_goods_spec, parent, false);
        //设置布局
        setContentView(view);
        //当前视图显示的宽高
        setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        setHeight(context.getResources().getDimensionPixelSize(R.dimen.size_400));
        //设置背景
        setBackgroundDrawable(new ColorDrawable());
        //设置得到焦点
        setFocusable(true);
        //设置点击窗口外部，PopupWindow消失
        setOutsideTouchable(true);
        //设置消失的监听
        setOnDismissListener(this);
        ButterKnife.bind(this,view);
        initView();
    }

    /**
     * 视图的初始化
     */
    private void initView() {
        //商品的图片、价格、库存，以及选择的商品数量进行设置
        Picasso.with(parent.getContext()).load(mGoodsInfo.getImg().getLarge()).into(mIvGoods);
        mTvPrice.setText(mGoodsInfo.getShopPrice());
        mTvInventoryValue.setText(String.valueOf(mGoodsInfo.getNumber()));
        //商品的数量：通过数量选择器决定
        mTvNumberValue.setText(String.valueOf(mNumberPicker.getCount()));
        mNumberPicker.setOnNumberChangedListener(new SimpleNumberPicker.OnNumberChangedListener() {
           //获取商品的数量
            @Override
            public void onNumberChange(int number) {
                //实时拿到商品的数量
                mTvNumberValue.setText(String.valueOf(number));
            }
        });
    }
    @OnClick(R.id.button_ok)
    public void onClickOk(View v){
        //具体选择了多少商品，是不是需要给使用者传递出去，具体的操作事件我们并不知道
        //具体的事件交给使用者处理
        int count = mNumberPicker.getCount();
        //如果商品的数量为0，不做操作
        if(count==0){
            ToastWrapper.show(R.string.goods_msg_must_choose_number);
            return;
        }
        onConfirmListener.onConfirm(count);
    }

    /**
     * show方法：用于展示PopupWindow
     * 参数中将监听传递过来
     */
    public void show(OnConfirmListener onConfirmListener){
        this.onConfirmListener=onConfirmListener;
        //从哪个方向显示
        showAtLocation(parent, Gravity.BOTTOM,0,0);
        //设置透明度
        backgroundAlpha(0.5f);
    }

    /**
     * 改变透明度：传入的透明度变化 0.5f-1.0f
     */
    private void backgroundAlpha(float bkgAlpha) {
        //设置Activity窗口的透明度
        WindowManager.LayoutParams attributes = mActivity.getWindow().getAttributes();
        attributes.alpha=bkgAlpha;
        mActivity.getWindow().setAttributes(attributes);
    }

    /*-----------------------------弹框消失的时候触发-----------------------------*/

    /**
     * 弹框消失的时候改变透明度
     */
    @Override
    public void onDismiss() {
        backgroundAlpha(1.0f);
    }
    /**
     * 利用接口回调的方式将选择的数量传递出去
     */
    public interface OnConfirmListener{
        void onConfirm(int number);
    }

}
