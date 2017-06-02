package com.shop.e.eshopl.util;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.shop.e.eshopl.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 测试展示
 * Created by Administrator on 2017-05-19.
 */

public class TestFragment extends Fragment {
    public static final String ARG_TEXT = "arg_text";
    @BindView(R.id.text)


    TextView textView;
    @BindView(R.id.number_picker)
    SimpleNumberPicker numberPicker;

    // 不建议在构造方法中传递数据，官方推荐的方式是采用setArguments()的方法传递数据
    public static TestFragment newInstance(String text) {
        TestFragment fragment = new TestFragment();
        Bundle bundle = new Bundle();
        bundle.putString(ARG_TEXT, text);
        fragment.setArguments(bundle);//传递数据
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_test, container, false);
        ButterKnife.bind(this, view);
        //切换不同Fragment显示不同文本
//        textView.setText(getArgText());
        textView.setText(String.valueOf(numberPicker.getCount()));
        numberPicker.setOnNumberChangedListener(new SimpleNumberPicker.OnNumberChangedListener() {
            @Override
            public void onNumberChange(int number) {
                textView.setText(String.valueOf(number));
            }
        });
        return view;

    }

    //获取传递的数据
    public String getArgText() {
        return getArguments().getString(ARG_TEXT);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
