package com.status.bar.example;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import fc.com.status.bar.StatusBarTintManager;

/**
 * Created by Administrator on 2016/7/12 0012.
 */
public class Fragment3 extends BaseFragment {

    @Override
    protected int getContentView() {
        return R.layout.fragment_3;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view.findViewById(R.id.bt_change).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            setStatusBarColor(Color.argb(33, (int)(Math.random() * 255), (int)(Math.random() * 255), (int)(Math.random() * 255)));
            }
        });
    }

    @Override
    protected boolean isCustomStautsBar() {
        return true;
    }

    @Override
    protected StatusBarTintManager getStatusBarTintManager(View view) {
        StatusBarTintManager tintManager = new StatusBarTintManager(view, true);
        // enable status bar tint
        tintManager.setStatusBarTintEnabled(true);
        tintManager.setStatusBarTintColor(Color.parseColor("#33000000"));
        return tintManager;
    }
}
