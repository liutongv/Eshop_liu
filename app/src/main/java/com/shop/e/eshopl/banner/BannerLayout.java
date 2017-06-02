package com.shop.e.eshopl.banner;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.widget.RelativeLayout;

import com.shop.e.eshopl.R;

import java.lang.ref.WeakReference;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.relex.circleindicator.CircleIndicator;

/**
 * 利用组合控件方法自定义，直接填充一个布局
 * 1. 自动轮播：Timer+handler
 * 2. 数据可以随意设置(适配器方面的处理)
 * 3. 自动和手动的冲突
 */
public class BannerLayout extends RelativeLayout {

    @BindView(R.id.pager_banner)
    ViewPager mPagerBanner;
    @BindView(R.id.indicator)
    CircleIndicator mIndicator;
    private static final long duration = 4000;
    private Timer mCycleTimer;
    private TimerTask mCycleTask;
    private CyclingHandler mCyclingHandler;
    private long mResumeCycleTime;

    // 一般是只在代码中使用
    public BannerLayout(Context context) {
        super(context);
        init(context);
    }

    // 布局中使用，未设置style
    public BannerLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    // 布局中使用并且设置了style样式
    public BannerLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    // 布局的填充和视图的初始化
    private void init(Context context) {
        // 布局中的根标签为<merge> 一定要设置viewGroup和attachToRoot=true
        LayoutInflater.from(context).inflate(R.layout.widget_banner_layout, this, true);
        ButterKnife.bind(this);
        mCyclingHandler = new CyclingHandler(this);
    }

    // 在视图上显示出来的时候处理自动轮播
    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();

        // 1. 创建一个计时器
        mCycleTimer = new Timer();

        // 创建一个计时任务：定期发送一些事件,通过Handler来实现，并且处理
        // 通过Handler发送消息
        mCycleTask = new TimerTask() {
            @Override
            public void run() {
                // 通过Handler发送消息
                mCyclingHandler.sendEmptyMessage(0);
            }
        };

        // 2. 执行一个计时任务：任务、延时时间、循环间隔的时间
        mCycleTimer.schedule(mCycleTask,duration,duration);
    }


    // 视图不展示的时候取消
    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();

        // 取消开始的计时任务
        mCycleTimer.cancel();
        mCycleTask.cancel();
        mCycleTask = null;
        mCycleTimer = null;
    }

    // 获取触摸的时间
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        mResumeCycleTime = System.currentTimeMillis() + duration;
        return super.dispatchTouchEvent(ev);
    }

    // 切换到下一页的方法
    public void moveToNextPosition(){
        // 看有没有设置适配器
        if (mPagerBanner.getAdapter()==null){
            throw new IllegalStateException("you need set a banner adapter");
        }
        // 看适配器里面有没有数据
        int count = mPagerBanner.getAdapter().getCount();
        if (count==0) return;

        // 看是不是在最后一条
        if (mPagerBanner.getCurrentItem()==count-1){
            // 切换到0，不设置平滑滚动
            mPagerBanner.setCurrentItem(0,false);
        }else {
            mPagerBanner.setCurrentItem(mPagerBanner.getCurrentItem()+1,true);
        }
    }

    // 给当前的控件提供一个设置适配器的方法
    public void setAdapter(BannerAdapter adapter){
        mPagerBanner.setAdapter(adapter);
        mIndicator.setViewPager(mPagerBanner);// 绑定圆点指示器和ViewPager
        adapter.registerDataSetObserver(mIndicator.getDataSetObserver());
    }

    // 为了防止内部类持有外部类引用而造成内存泄漏，采取静态内部类+弱引用的方式
    private static class CyclingHandler extends Handler{

        // 弱引用
        private WeakReference<BannerLayout> mBannerReference;

        public CyclingHandler(BannerLayout bannerLayout) {
            mBannerReference = new WeakReference<BannerLayout>(bannerLayout);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            // 接收到消息，ViewPager切换到下一页
            if(mBannerReference==null) return;
            BannerLayout bannerLayout = mBannerReference.get();
            if (bannerLayout==null)return;

            // 触摸之后时间还没过四秒，不去轮播
            if (System.currentTimeMillis()<bannerLayout.mResumeCycleTime){
                return;
            }

            // 切换到下一页
            bannerLayout.moveToNextPosition();
        }
    }
}
