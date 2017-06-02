package com.shop.e.eshopl.activity;

import android.animation.Animator;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import com.shop.e.eshopl.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * 加载界面
 * Created by lt on 2017-05-19.
 */

public class LoadingActivity extends Activity implements Animator.AnimatorListener {
    @BindView(R.id.iv_loading)
    ImageView mIvLoading;
    private Unbinder munBinder;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);
        munBinder=ButterKnife.bind(this);
        show();
    }

    /**
     * 设置动画
     */
    public void show(){
        mIvLoading.setAlpha(0.1f);
        mIvLoading.animate()
                  .alpha(1.0f)   //透明度
                  .setDuration(2000)  //设置动画持续的时间
                  .setListener(this)  //绑定监听
                  .start();//开始动画
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        munBinder.unbind();
    }

    /**
     * 动画开始
     * @param animation
     */
    @Override
    public void onAnimationStart(Animator animation) {


    }

    /**
     * 动画结束
     * @param animation
     */
    @Override
    public void onAnimationEnd(Animator animation) {
        Intent intent=new Intent(this,MainActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.push_right_in,R.anim.push_right_out);
        finish();
    }

    /**
     * 动画取消
     * @param animation
     */
    @Override
    public void onAnimationCancel(Animator animation) {

    }

    /**
     * 动画重复播放
     * @param animation
     */
    @Override
    public void onAnimationRepeat(Animator animation) {

    }
}
