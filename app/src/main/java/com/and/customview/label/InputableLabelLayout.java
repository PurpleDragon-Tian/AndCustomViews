package com.and.customview.label;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.and.customview.R;
import com.google.android.flexbox.AlignContent;
import com.google.android.flexbox.AlignItems;
import com.google.android.flexbox.AlignSelf;
import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexWrap;
import com.google.android.flexbox.FlexboxLayout;
import com.google.android.flexbox.JustifyContent;

import java.lang.reflect.Field;
import java.util.ArrayList;

/**
 * @author Tianzilong
 * @date 19-5-9 下午3:48
 */
public class InputableLabelLayout extends FlexboxLayout {

    private Context mContext;
    private float[] cornerArray;
    private String mHintTxt = "";
    private Drawable mLabelDrawble;
    private Drawable mDividerDrawable;
    private EditText mEditText;
    private Drawable mCursorDrawable;
    private String mLabelTextColor = "#ff5b3d";
    private int mMinEditTextWid = DEFAULT_EDIT_TEXT_WIDTH;
    private static final int DEFAULT_EDIT_TEXT_WIDTH = 10;
    private ArrayList<String> lableTxtList = new ArrayList<>();



    public InputableLabelLayout(Context context) {
        this(context, null);
    }

    public InputableLabelLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public InputableLabelLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        // Load attributes
        final TypedArray a = getContext().obtainStyledAttributes(
                attrs, R.styleable.InputableLabelLayout, defStyleAttr, 0);
        if (a.hasValue(R.styleable.InputableLabelLayout_flexDividerDrawable)) {
            mDividerDrawable = a.getDrawable(R.styleable.InputableLabelLayout_flexDividerDrawable);
        }
        if (a.hasValue(R.styleable.InputableLabelLayout_labelbg)) {
            mLabelDrawble = a.getDrawable(R.styleable.InputableLabelLayout_labelbg);
        }
        if (a.hasValue(R.styleable.InputableLabelLayout_inputHint)) {
            mHintTxt = a.getString(R.styleable.InputableLabelLayout_inputHint);
        }
        if (a.hasValue(R.styleable.InputableLabelLayout_cursorDrawable)) {
            mCursorDrawable = a.getDrawable(R.styleable.InputableLabelLayout_cursorDrawable);
        }
        a.recycle();
        cornerArray = new float[]{
                dip2px(mContext, 5),
                dip2px(mContext, 5),
                dip2px(mContext, 5),
                dip2px(mContext, 5),
                dip2px(mContext, 5),
                dip2px(mContext, 5),
                dip2px(mContext, 5),
                dip2px(mContext, 5)};
        init(context);
    }

    /**
     * 初始化FlexboxLayout
     *
     * @param context
     */
    private void init(Context context) {
        initFlexboxLayout();
        initEditText(context);
    }


    private void initEditText(final Context context) {
        mEditText = new EditText(context);
        mEditText.setHint(mHintTxt);
        mEditText.setMaxLines(1);
        mEditText.setBackground(null);
        setCursorStyle(mEditText);
        mEditText.setMinWidth(dip2px(context, getAdjustEditTextWidth(context)));
//        android:textCursorDrawable="@drawable/drawable_cursor"
        ViewGroup.LayoutParams editParam = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, dip2px(context, 40));
        mEditText.setLayoutParams(editParam);
        //监听回车（换行）按键
        mEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                String keyWord = mEditText.getText().toString().trim();
                if (actionId == EditorInfo.IME_ACTION_UNSPECIFIED || (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                    if (TextUtils.isEmpty(keyWord)) {
                        return true;
                    }
                    //因为在点击键盘按键的时候按下和抬起都会调用一次onEditorAction， 所以要区分一下，保证只在某一次执行时触发我们的代码
                    switch (event.getAction()) {
                        case KeyEvent.ACTION_UP:
                            createNewLabel(keyWord);
                            mEditText.setText("");
                            break;
                    }

                }
                return true;
            }
        });
        //监听删除按键
        mEditText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_DEL) {
                    switch (event.getAction()) {
                        //一定要用action_down,用action_up会有问题。在删除输入框中最后一个字的时候会导致最后一个标签自动被选中，
                        case KeyEvent.ACTION_DOWN:
                            String keyWord = mEditText.getText().toString().trim();
                            if (TextUtils.isEmpty(keyWord)) {
                                int childCount = getChildCount();
                                if (childCount >= 2) {
                                    View view = getChildAt(childCount - 2);
                                    if (!view.isSelected()) {
                                        mLabelDrawble.setBounds(new Rect(0, 0, view.getWidth(), view.getHeight()));
                                        view.setSelected(true);
                                    } else {
                                        lableTxtList.remove(indexOfChild(view));
                                        removeView(view);
                                    }
                                }
                                return true;
                            } else {
                                return false;
                            }
                    }
                }
                return false;
            }
        });
        addView(mEditText);
        LayoutParams flexParam = (LayoutParams) mEditText.getLayoutParams();
        flexParam.setFlexGrow(1);
        flexParam.setAlignSelf(AlignItems.FLEX_START);
    }

    private void setCursorStyle(EditText mEditText) {
        try {
            Field fCursorDrawableRes = TextView.class.getDeclaredField("mCursorDrawableRes");
            fCursorDrawableRes.setAccessible(true);
            Field fEditor = TextView.class.getDeclaredField("mEditor");
            fEditor.setAccessible(true);
            Object editor = fEditor.get(mEditText);
            Class<?> clazz = editor.getClass();
            Field fCursorDrawable = clazz.getDeclaredField("mCursorDrawable");
            fCursorDrawable.setAccessible(true);
            Drawable[] drawables = new Drawable[1];
            drawables[0] = mCursorDrawable ;
            drawables[0].setColorFilter(0xff00ff00, PorterDuff.Mode.SRC_IN);
            fCursorDrawable.set(editor, drawables);
        } catch (Exception ignored) {
        }
    }

    /**
     * android:layout_width="wrap_content"
     * android:layout_height="40dp"
     * android:paddingLeft="15dp"
     * android:paddingRight="15dp"
     * app:layout_alignSelf="flex_start"
     * android:text="短篇小说"
     * android:gravity="center"
     * android:textColor="@color/text_color"
     * android:background="@drawable/label_bg_shape"
     *
     * @param keyWord
     */
    private void createNewLabel(String keyWord) {
        TextView label = new TextView(mContext);
        label.setText(keyWord);
        label.setTextColor(Color.parseColor(mLabelTextColor));
        label.setMaxEms(7);
        label.setSelected(false);
        label.setSingleLine(true);

        Drawable bg = getNewDrawable(mLabelDrawble);
        label.setBackground(bg);
//        label.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.bg_custom_label));
        label.setGravity(Gravity.CENTER);
        label.setPadding(dip2px(mContext, 15), 0, dip2px(mContext, 15), 0);
        label.setEllipsize(TextUtils.TruncateAt.END);
        addView(label, getChildCount() - 1);
        ViewGroup.LayoutParams params = label.getLayoutParams();
        if (params instanceof LayoutParams) {
            LayoutParams flexparams = (LayoutParams) params;
            flexparams.width = LayoutParams.WRAP_CONTENT;
            flexparams.height = dip2px(mContext, 40);
            flexparams.setAlignSelf(AlignSelf.FLEX_START);
            label.setLayoutParams(params);
            mLabelDrawble.setBounds(new Rect(0, 0, label.getWidth(), label.getHeight()));
        }
        lableTxtList.add(keyWord);
    }

    /**
     * drawable 复制
     *
     * @param drawable
     * @return
     */
    public  Drawable getNewDrawable(Drawable drawable) {
        return drawable.getConstantState().newDrawable();
    }

    public void addLabels(ArrayList<String> labelTexts) {
        if (labelTexts == null || labelTexts.size() == 0) {
            return;
        }
        for (int i = 0; i < labelTexts.size(); i++) {
            createNewLabel(labelTexts.get(i));
            lableTxtList.add(labelTexts.get(i));
        }
    }

    public void addLabels(String data) {
        if (TextUtils.isEmpty(data)) {
            return;
        }
        createNewLabel(data);
        lableTxtList.add(data);
    }
    private void setHint(Context context) {

    }


    private int getAdjustEditTextWidth(Context context) {
        return mMinEditTextWid <= 10 ? dip2px(context, DEFAULT_EDIT_TEXT_WIDTH) : dip2px(context, mMinEditTextWid);
    }

    @Override
    public void setOnClickListener(@Nullable View.OnClickListener l) {
        super.setOnClickListener(l);
        if (mEditText != null) {
            if (!mEditText.isFocused()) {
                mEditText.requestFocus();
            } else {
                // 弹出软键盘
                showIME(mContext, this);
            }
        }
    }

    /**
     * app:flexWrap="wrap"
     * app:alignItems="center"
     * app:alignContent="flex_start"
     * app:flexDirection="row"
     * app:justifyContent="flex_start"
     * app:showDivider="beginning|middle"
     * app:dividerDrawable="@drawable/divider_shape"
     */
    private void initFlexboxLayout() {
        //是否换
        setFlexWrap(FlexWrap.WRAP);
        //子view对齐方式
        setAlignItems(AlignItems.CENTER);
        //属性控制多根轴线的对齐方式(也就是控制多行，如果子元素只有一行，则不起作用)
        setAlignContent(AlignContent.FLEX_START);
        //子view排列方向
        setFlexDirection(FlexDirection.ROW);
        //属性控制元素主轴方向上的对齐方式
        setJustifyContent(JustifyContent.FLEX_START);
        //显示divider的位置
        setShowDivider(SHOW_DIVIDER_BEGINNING | SHOW_DIVIDER_MIDDLE);
        //设置dividerDrawable
        setDividerDrawable(mDividerDrawable);

    }


    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    public static void showIME(Context context, View v) {
        InputMethodManager imm = (InputMethodManager) context
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        // 得到InputMethodManager的实例
        if (!imm.isActive()) {
            //如果开启
            imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT,
                    InputMethodManager.HIDE_NOT_ALWAYS);
            //关闭软键盘，开启方法相同，这个方法是切换开启与关闭状态的
        }
        imm.showSoftInput(v, 0);

    }

    public ArrayList<String> getLableTxtList() {
        return lableTxtList;
    }
}
