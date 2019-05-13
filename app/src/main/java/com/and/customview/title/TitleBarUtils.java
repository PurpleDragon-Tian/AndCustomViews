package com.and.customview.title;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.ColorRes;
import androidx.annotation.DrawableRes;
import androidx.annotation.StringRes;

import com.and.customview.R;
import com.and.customview.util.ScreenUtils;


/***
 * 标题栏工具
 *
 * @author
 */
public class TitleBarUtils {

    private View rootView;
    private Context mContext;

    private TextView left, left1, right, right1, title;
    private LinearLayout customTitle;
    public RelativeLayout titleLayout;


    public static TitleBarUtils newInstance(Activity activity) {
        return new TitleBarUtils(activity.getWindow().getDecorView().getRootView(), activity);
    }


    private TitleBarUtils(View view, Context context) {
        rootView = view;
        mContext = context;
        init();
    }

    private TitleBarUtils(int resLayout, Context context) {
        rootView = View.inflate(mContext, resLayout, null);
        mContext = context;
        init();
    }

    private void init() {
        titleLayout = (RelativeLayout) rootView.findViewById(R.id.title_layout);

        left = (TextView) rootView.findViewById(R.id.title_left);
        left1 = (TextView) rootView.findViewById(R.id.title_left1);
        right = (TextView) rootView.findViewById(R.id.title_right);
        right1 = (TextView) rootView.findViewById(R.id.title_right1);
        title = (TextView) rootView.findViewById(R.id.title_title);
        customTitle = (LinearLayout) rootView.findViewById(R.id.title_custom_layout);

        left.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (mContext instanceof Activity) {
                    ((Activity) mContext).finish();
                }
            }
        });


    }


    /***
     * 左边两个按钮处理
     *
     * @param leftButton 从左到右从0开始
     */
    public void setLeftHandler(ILeftButton leftButton) {
        if (null != leftButton)
            leftButton.invokeLeft(left, left1);
    }

    /***
     * 右边两个按钮处理
     *
     * @param rightButton 从右到左从0开始
     */
    public void setRightHandler(IRightButton rightButton) {
        if (null != rightButton)
            rightButton.invokeRight(right, right1);
    }

    public void setTitle(@StringRes int resId){
        title.setText(resId);
    }

    public void setTitle(String titleStr) {
        title.setText(titleStr);
    }

    public int getStatusHeight(Context context) {

        int statusHeight = -1;
        try {
            Class clazz = Class.forName("com.android.internal.R$dimen");
            Object object = clazz.newInstance();
            int height = Integer.parseInt(clazz.getField("status_bar_height")
                    .get(object).toString());
            statusHeight = context.getResources().getDimensionPixelSize(height);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return statusHeight;
    }

    public void setCustomTitleView(View v) {
        if (customTitle != null) {
            customTitle.removeAllViews();
            customTitle.addView(v);
            customTitle.setVisibility(View.VISIBLE);
        } else {
            throw new RuntimeException("customTitle is null");
        }
    }

    public void setVisibility(int visibility){
        titleLayout.setVisibility(visibility);
    }

    public TitleBarUtils setLeftIcon(Context mContext, @DrawableRes int resId) {
        Drawable leftDrawable = mContext.getResources().getDrawable(resId);
        leftDrawable.setBounds(0, 0 , ScreenUtils.dp2px(20, mContext), ScreenUtils.dp2px(20, mContext));
        left.setCompoundDrawables(leftDrawable, null, null, null);
        return this;
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public TitleBarUtils setBackground(Drawable background){
        titleLayout.setBackground(background);
        return this;
    }

    public void setBackgroundColor(int color) {
        titleLayout.setBackgroundColor(color);
    }

    public void setBackgroundResource(int resid) {
        titleLayout.setBackgroundResource(resid);
    }


    /**
     * 如果不需要有特殊操作.直接调用这个方法,xml布局必须要include title_bar_layout
     *****/
    public void setTextTitle(String title) {
        setTextTitle(title, null, null);
    }

    public void setTextTitle(int titleId) {
        setTextTitle(mContext.getResources().getString(titleId));
    }

    public void setTextTitle(String title, ILeftButton leftButton,
                             IRightButton rightbutton) {
        setTitleBarListener(leftButton, rightbutton);
        setTitle(title);

    }

    private void setTitleBarListener(ILeftButton leftButton,
                                     IRightButton rightbutton) {
        if (leftButton != null)
            setLeftHandler(leftButton);
        if (rightbutton != null)
            setRightHandler(rightbutton);
    }

    public void setTextTitle(int titleId, ILeftButton leftButton,
                             IRightButton rightbutton) {
        setTitleBarListener(leftButton, rightbutton);
        setTitle(mContext.getResources().getString(titleId));

        // barUtils.setPadingTop();

    }

    public TitleBarUtils setTitleColor(Context mContext, @ColorRes int resId) {
        title.setTextColor(mContext.getResources().getColor(resId));
        return this;
    }
    public TitleBarUtils setTitleColor(int color) {
        title.setTextColor(color);
        return this;
    }

    public void setCustomTitle(View title) {
        setCustomTitle(title);
    }


    public void setTitleBarBackground(Drawable background){
       setBackground(background);
    }

    public void setTitleBarBackgroundColor(int color) {
        setBackgroundColor(color);
    }

    public void setTitleBarBackgroundResource(int resid) {
        setBackgroundResource(resid);
    }

    public void setTitleVisibility(int visibility) {
        setVisibility(visibility);
    }


}
