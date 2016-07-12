/*
 * Copyright (C) 2013 readyState Software Ltd
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package fc.com.status.bar;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.LinearLayout;

/**
 * Class to manage status and navigation bar tint effects when using KitKat 
 * translucent system UI modes.
 *
 */
public class StatusBarTintManager {

    /**
     * The default system bar tint color value.
     */
    public static final int DEFAULT_TINT_COLOR = 0x99000000;

    private SystemBarConfig mConfig;

    public boolean isStatusBarAvailable() {
        return mStatusBarAvailable;
    }

    private boolean mStatusBarAvailable;
    private boolean mStatusBarTintEnabled;
    private View mStatusBarTintView;
    private boolean isOverlay;

    /**
     * Constructor. Call this in the host activity onCreate method after its
     * content view has been set. You should always create new instances when
     * the host activity is recreated.
     *
     * @param activity The host activity.
     */

    public StatusBarTintManager(Activity activity){
        this(activity, false);
    }

    @TargetApi(19)
    public StatusBarTintManager(Activity activity, boolean isOverlay) {
        this.isOverlay = isOverlay;
        Window window = activity.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        initWindow(window, true);
        View view = ((ViewGroup) window.getDecorView().findViewById(android.R.id.content)).getChildAt(0);
        mConfig = new SystemBarConfig(activity, mStatusBarAvailable);
        if (mStatusBarAvailable) {
            setupStatusBarViewForActivity(activity, view);
        }
    }

    public StatusBarTintManager(View view){
        this(view, false);
    }

    public StatusBarTintManager(View view, boolean isOverlay){
        this.isOverlay = isOverlay;
        Context context = view.getContext();
        if (context instanceof Activity) {
            Activity activity = (Activity) context;
            initWindow(activity.getWindow(), false);
            mConfig = new SystemBarConfig(activity, mStatusBarAvailable);
            if (mStatusBarAvailable) {
                setupStatusBarView(activity, view);
            }
        }
    }

    private void initWindow(Window win, boolean isActivity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (isActivity && !isOverlay && Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                win.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                win.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            } else {
                win.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                mStatusBarAvailable = true;
            }
        }
    }

    /**
     * Enable tinting of the system status bar.
     *
     * If the platform is running Jelly Bean or earlier, or translucent system
     * UI modes have not been enabled in either the theme or via window flags,
     * then this method does nothing.
     *
     * @param enabled True to enable tinting, false to disable it (default).
     */
    public void setStatusBarTintEnabled(boolean enabled) {
        mStatusBarTintEnabled = enabled;
        if (mStatusBarAvailable) {
            mStatusBarTintView.setVisibility(enabled ? View.VISIBLE : View.GONE);
        }
    }

    /**
     * Apply the specified color tint to all system UI bars.
     *
     * @param color The color of the background tint.
     */
    public void setTintColor(int color) {
        setStatusBarTintColor(color);
    }

    /**
     * Apply the specified drawable or color resource to all system UI bars.
     *
     * @param res The identifier of the resource.
     */
    public void setTintResource(int res) {
        setStatusBarTintResource(res);
    }

    /**
     * Apply the specified drawable to all system UI bars.
     *
     * @param drawable The drawable to use as the background, or null to remove it.
     */
    public void setTintDrawable(Drawable drawable) {
        setStatusBarTintDrawable(drawable);
    }

    /**
     * Apply the specified alpha to all system UI bars.
     *
     * @param alpha The alpha to use
     */
    public void setTintAlpha(float alpha) {
        setStatusBarAlpha(alpha);
    }

    /**
     * Apply the specified color tint to the system status bar.
     *
     * @param color The color of the background tint.
     */
    public void setStatusBarTintColor(int color) {
        if (mStatusBarAvailable) {
            mStatusBarTintView.setBackgroundColor(color);
        }
    }

    /**
     * Apply the specified drawable or color resource to the system status bar.
     *
     * @param res The identifier of the resource.
     */
    public void setStatusBarTintResource(int res) {
        if (mStatusBarAvailable) {
            mStatusBarTintView.setBackgroundResource(res);
        }
    }

    /**
     * Apply the specified drawable to the system status bar.
     *
     * @param drawable The drawable to use as the background, or null to remove it.
     */
    @SuppressWarnings("deprecation")
    public void setStatusBarTintDrawable(Drawable drawable) {
        if (mStatusBarAvailable) {
            mStatusBarTintView.setBackgroundDrawable(drawable);
        }
    }

    /**
     * Apply the specified alpha to the system status bar.
     *
     * @param alpha The alpha to use
     */
    @TargetApi(11)
    public void setStatusBarAlpha(float alpha) {
        if (mStatusBarAvailable && Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            mStatusBarTintView.setAlpha(alpha);
        }
    }

    /**
     * Get the system bar configuration.
     *
     * @return The system bar configuration for the current device configuration.
     */
    public SystemBarConfig getConfig() {
        return mConfig;
    }

    /**
     * Is tinting enabled for the system status bar?
     *
     * @return True if enabled, False otherwise.
     */
    public boolean isStatusBarTintEnabled() {
        return mStatusBarTintEnabled;
    }

//    private void setupStatusBarView(Context context, ViewGroup decorViewGroup) {
//        mStatusBarTintView = new View(context);
//        LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, mConfig.getStatusBarHeight());
//        params.gravity = Gravity.TOP;
//        mStatusBarTintView.setLayoutParams(params);
//        mStatusBarTintView.setBackgroundColor(DEFAULT_TINT_COLOR);
//        mStatusBarTintView.setVisibility(View.GONE);
//        decorViewGroup.addView(mStatusBarTintView);
//    }

    private void setupStatusBarViewForActivity(Context context, View view) {
        mStatusBarTintView = new View(context);
        LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, mConfig.getStatusBarHeight());
        mStatusBarTintView.setLayoutParams(params);
        mStatusBarTintView.setBackgroundColor(DEFAULT_TINT_COLOR);
        mStatusBarTintView.setVisibility(View.GONE);

        ViewGroup parent = (ViewGroup) view.getParent();
        if (!isOverlay) {
            view.setFitsSystemWindows(true);
        }
        parent.addView(mStatusBarTintView);
    }

    private void setupStatusBarView(Context context, View view) {
        mStatusBarTintView = new View(context);
        LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, mConfig.getStatusBarHeight());
        mStatusBarTintView.setLayoutParams(params);
        mStatusBarTintView.setBackgroundColor(DEFAULT_TINT_COLOR);
        mStatusBarTintView.setVisibility(View.GONE);

        ViewGroup parent = (ViewGroup) view.getParent();
        ViewGroup viewGroup = null;
        if (isOverlay) {
            FrameLayout frameLayout = new FrameLayout(context);
            frameLayout.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            viewGroup = frameLayout;
        } else {
            LinearLayout linearLayout = new LinearLayout(context);
            linearLayout.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            linearLayout.setOrientation(LinearLayout.VERTICAL);
            viewGroup = linearLayout;
        }

        if (!isOverlay) {
            viewGroup.addView(mStatusBarTintView);
        }

//        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(view.getLayoutParams());
//        layoutParams.weight = 1;
//        view.setLayoutParams(layoutParams);
        if (parent != null) {
            int position = parent.indexOfChild(view);
            parent.removeView(view);
            viewGroup.addView(view);
            parent.addView(viewGroup, position);
        } else {
            viewGroup.addView(view);
        }

        if (isOverlay) {
            viewGroup.addView(mStatusBarTintView);
        }
    }

    public ViewGroup getRootView() {
        if (mStatusBarTintView != null) {
            return (ViewGroup) mStatusBarTintView.getParent();
        }
        return null;
    }

    /**
     * Class which describes system bar sizing and other characteristics for the current
     * device configuration.
     *
     */
    public static class SystemBarConfig {

        private static final String STATUS_BAR_HEIGHT_RES_NAME = "status_bar_height";

        private final boolean mTranslucentStatusBar;
        private final int mStatusBarHeight;
        private final int mActionBarHeight;

        private SystemBarConfig(Context activity, boolean translucentStatusBar) {
            Resources res = activity.getResources();
            mStatusBarHeight = getInternalDimensionSize(res, STATUS_BAR_HEIGHT_RES_NAME);
            mActionBarHeight = getActionBarHeight(activity);
            mTranslucentStatusBar = translucentStatusBar;
        }

        @TargetApi(14)
        private int getActionBarHeight(Context context) {
            int result = 0;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
                TypedValue tv = new TypedValue();
                context.getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true);
                result = TypedValue.complexToDimensionPixelSize(tv.data, context.getResources().getDisplayMetrics());
            }
            return result;
        }

        private int getInternalDimensionSize(Resources res, String key) {
            int result = 0;
            int resourceId = res.getIdentifier(key, "dimen", "android");
            if (resourceId > 0) {
                result = res.getDimensionPixelSize(resourceId);
            }
            return result;
        }

        /**
         * Get the height of the system status bar.
         *
         * @return The height of the status bar (in pixels).
         */
        public int getStatusBarHeight() {
            return mStatusBarHeight;
        }

        /**
         * Get the height of the action bar.
         *
         * @return The height of the action bar (in pixels).
         */
        public int getActionBarHeight() {
            return mActionBarHeight;
        }

        /**
         * Get the layout inset for any system UI that appears at the top of the screen.
         *
         * @param withActionBar True to include the height of the action bar, False otherwise.
         * @return The layout inset (in pixels).
         */
        public int getPixelInsetTop(boolean withActionBar) {
            return (mTranslucentStatusBar ? mStatusBarHeight : 0) + (withActionBar ? mActionBarHeight : 0);
        }
    }

}

