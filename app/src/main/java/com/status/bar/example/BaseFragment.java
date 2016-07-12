package com.status.bar.example;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import fc.com.status.bar.StatusBarTintManager;


/**
 * BaseFragment
 * <p/>
 * Created by Eric on 2014-4-26
 */
public abstract class BaseFragment extends Fragment {

    private StatusBarTintManager tintManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(getContentView(), container, false);

        //fragment是放在ViewPage中则一定要在这里初始化status bar
        if (initStatusBar(view) && tintManager.getRootView().getParent() == null) {
            return tintManager.getRootView();
        } else {
            return view;
        }
    }

    protected abstract int getContentView();

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //如果fragment不是放在ViewPage中则可以在这里初始化status bar
//        initStatusBar(view);
    }

    protected final void setStatusBarColor(int color) {
        if (tintManager != null && tintManager.isStatusBarAvailable()) {
            tintManager.setStatusBarTintColor(color);
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                getActivity().getWindow().setStatusBarColor(color);
            }
        }
    }

    protected boolean isCustomStautsBar() {
        return false;
    }

    private boolean initStatusBar(View view) {
        if (isCustomStautsBar()) {
            tintManager = getStatusBarTintManager(view);
            return tintManager != null && tintManager.isStatusBarAvailable();
        }
        return false;
    }

    protected StatusBarTintManager getStatusBarTintManager(View view) {
        StatusBarTintManager tintManager = new StatusBarTintManager(view);
        // enable status bar tint
        tintManager.setStatusBarTintEnabled(true);
        tintManager.setStatusBarTintColor(getResources().getColor(R.color.colorPrimaryDark));
        return tintManager;
    }
}
