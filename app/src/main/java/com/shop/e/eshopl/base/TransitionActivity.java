package com.shop.e.eshopl.base;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.shop.e.eshopl.R;

/**
 * 设置具有转场效果的父类
 * Created by Administrator on 2017-05-23.
 */

public class TransitionActivity extends AppCompatActivity{

    //处理返回箭头的事件
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home){
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void startActivity(Intent intent) {
        super.startActivity(intent);
        //设置转场效果

    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode) {
        super.startActivityForResult(intent, requestCode);
        //设置转场效果
    }

    @Override
    public void finish() {
        super.finish();
        //设置转场效果
    }

    /**
     * 设置没有动画效果的
     */
    public void setNoAnimation(){
        super.finish();
    }
    /**
     * 设置跳转的动画
     */
    public void setTransitionAnimation(boolean isNewActivity){
        if(isNewActivity){   //新的页面从右边进入
            overridePendingTransition(R.anim.push_right_in,R.anim.push_right_out);
        }else{   //回到上个界面
            overridePendingTransition(R.anim.push_left_in,R.anim.push_left_out);
        }
    }
}
