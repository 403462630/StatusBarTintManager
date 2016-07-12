package com.status.bar.example;


import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;

import fc.com.status.bar.StatusBarTintManager;


/**
 * Created by Administrator on 2016/5/18.
 */
public class BaseActivity extends AppCompatActivity{
    private StatusBarTintManager tintManager;

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        initStatusBar();
    }

    @Override
    public void setContentView(View view) {
        super.setContentView(view);
        initStatusBar();
    }

    @Override
    public void setContentView(View view, ViewGroup.LayoutParams params) {
        super.setContentView(view, params);
        initStatusBar();
    }

    protected boolean isCustomStautsBar() {
        return true;
    }

    private void initStatusBar() {
        if (isCustomStautsBar()) {
            tintManager = getStatusBarTintManager();
        }
    }

    protected StatusBarTintManager getStatusBarTintManager() {
        StatusBarTintManager tintManager = new StatusBarTintManager(this);
        // enable status bar tint
        tintManager.setStatusBarTintEnabled(true);
        tintManager.setStatusBarTintColor(getResources().getColor(R.color.colorPrimaryDark));
        return tintManager;
    }

    protected void setStatusBarColor(int color) {
        if (tintManager != null && tintManager.isStatusBarAvailable()) {
            tintManager.setStatusBarTintColor(color);
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                getWindow().setStatusBarColor(color);
            }
        }
    }
}
