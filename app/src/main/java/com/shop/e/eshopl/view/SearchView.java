package com.shop.e.eshopl.view;

import android.content.Context;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.shop.e.eshopl.R;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by lt on 2017-05-27.
 */

public class SearchView extends LinearLayout implements TextWatcher, TextView.OnEditorActionListener {
    /**
     * 1.输入文本，有文本的时候，有显示清除的按钮
     * 没有文本的时候，不显示清除按钮
     * 需要给EditText设置文本变化的监听
     * 2.点击软键盘的搜索时触发搜索，且隐藏软键盘
     *       EditText.setImeOptions()  设置软键盘的操作
     *       setOnEditorAction() 编辑器动作的一个监听
     *       InputMethodManager.hideSoftInputFromWindow()  隐藏软键盘
     * 3.点击搜索的时候，要执行搜索的事件，比如将输入的内容设置为关键字，在搜索页面进行数据获取
     * 搜索的具体事件，需要让调用者去实现，需要定义一个接口，接口中定义一个方法，让调用者去实现
     */
    @BindView(R.id.button_search)
    ImageButton mButtonSearch;
    @BindView(R.id.edit_query)
    EditText mEditQuery;
    @BindView(R.id.button_clear)
    ImageButton mButtonClear;
    public OnSearchListener mOnSearchListener;
    /**
     * 代码中
     */
    public SearchView(Context context) {
        super(context);
        init(context);
    }
    /**
     * 布局中
     */
    public SearchView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }
    /**
     * 布局中设置了style
     */
    public SearchView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }
    /**
     * 视图初始化
     */
    public void init(Context context) {
        LayoutInflater.from(context).inflate(R.layout.widget_simple_search_view, this);
        ButterKnife.bind(this);
        //设置文本变化的监听
        mEditQuery.addTextChangedListener(this);
        //设置软键盘的样式  回车的图标变成搜索的图标
        mEditQuery.setImeOptions(EditorInfo.IME_ACTION_SEARCH);
        //设置输入的类型：文本类型
        mEditQuery.setInputType(EditorInfo.TYPE_CLASS_TEXT);
        //设置软键盘的事件监听
        mEditQuery.setOnEditorActionListener(this);

    }

    /**
     * 搜索和清除的点击事件
     */
    @OnClick({R.id.button_search, R.id.button_clear})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.button_search:
                //搜索的事件处理
                search();
                break;
            case R.id.button_clear:
                //清除文本
                mEditQuery.setText(null);
                search();
                break;
        }
    }

    //-----------------------文本监听的重写方法----------------------
    /**
     * 文本输入前
     */
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }
    /**
     * 文本输入中
     */
    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }
    /**
     * 文本输入完
     */
    @Override
    public void afterTextChanged(Editable s) {
        //根据文本有没有来决定是否显示清除图标
        String query = mEditQuery.getText().toString();
        int visible = TextUtils.isEmpty(query) ? View.INVISIBLE : VISIBLE;
        mButtonClear.setVisibility(visible);
    }

    //-------------ㄟ( ▔, ▔ )ㄏ 软键盘监听的重写方法-------------------
    /**
     * 输入键盘的操作
     */
    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_SEARCH) {
            //搜索事件的处理
            search();
            return true;
        }
        return false;
    }
    /**
     * 拿到字符串，然后去搜索的方法
     */
    public void search(){
        String query = mEditQuery.getText().toString();
        if(mOnSearchListener!=null){
            mOnSearchListener.search(query);
        }
        closeKeyBoard();
    }

    /**
     * 关闭软键盘
     */
    private void closeKeyBoard() {
        //失去焦点
        mEditQuery.clearFocus();
        //关闭软键盘
        InputMethodManager inputMethodManager= (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(mEditQuery.getWindowToken(),0);
    }

    /**
     * 给控件提供一个设置搜索监听的方法
     */
    public void setOnSearchListener(OnSearchListener onSearchListener){
        mOnSearchListener=onSearchListener;
    }
    /**
     * 利用接口回调，为控件提供一个搜索监听的方法，让调用者实现监听，实现里面的搜索方法
     */
    public interface OnSearchListener{
        //提供一个搜索的方法，调用者去实现
        void search(String query);
    }
}
