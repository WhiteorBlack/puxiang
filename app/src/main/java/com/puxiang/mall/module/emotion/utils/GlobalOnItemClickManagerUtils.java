package com.puxiang.mall.module.emotion.utils;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.puxiang.mall.module.emotion.viewmodel.adapter.EmotionViewsAdapter;

import java.lang.ref.WeakReference;


/**
 * Description:点击表情的全局监听管理类
 */
public class GlobalOnItemClickManagerUtils {

    private static GlobalOnItemClickManagerUtils instance;
    // private EditText mEditText;//输入框
    private String TAG = "GlobalOnItemClickManagerUtils";
    private EditText editText;

    private GlobalOnItemClickManagerUtils() {
    }

    public static GlobalOnItemClickManagerUtils getInstance() {
        // mContext = context;
        if (instance == null) {
            synchronized (GlobalOnItemClickManagerUtils.class) {
                if (instance == null) {
                    instance = new GlobalOnItemClickManagerUtils();
                }
            }
        }
        return instance;
    }

    public void attachToEditText(EditText editText) {
        this.editText = editText;
    }

    public void detachToEditText() {
        WeakReference<View> weakReference = new WeakReference<View>(editText);
        this.editText = null;
    }

    public RecyclerView.OnItemTouchListener getOnItemClickListener(final int emotion_map_type) {
        return new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {

                if (adapter instanceof EmotionViewsAdapter) {
                    // 点击的是表情
                    EmotionViewsAdapter emotionViewsAdapter = (EmotionViewsAdapter) adapter;
//                    if (position == emotionGvAdapter.getCount() - 1) {
//                        // 如果点击了最后一个回退按钮,则调用删除键事件
//                        mEditText.dispatchKeyEvent(new KeyEvent(
//                                KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_DEL));
//                    } else {
                    // 如果点击了表情,则添加到输入框中
                    String emotionName = emotionViewsAdapter.getItem(position);

                    int type = Integer.parseInt(
                            emotionName.substring(emotionName.indexOf("_") + 1
                                    , emotionName.lastIndexOf("_")));

                    // 获取当前光标位置,在指定位置上添加表情图片文本
                    EditText mEditText = editText;
                    if (type != 0) {
                        mEditText.setText("");
                    }

                    int curPosition = mEditText.getSelectionStart();
                    StringBuilder sb = new StringBuilder(mEditText.getText().toString());
                    sb.insert(curPosition, emotionName);

                    // 特殊文字处理,将表情等转换一下
                    mEditText.setText(SpanStringUtils.getEmotionContent(emotion_map_type, mEditText, sb.toString()));
                    // 将光标设置到新增完表情的右侧
                    mEditText.setSelection(curPosition + emotionName.length());
                }
            }
        };
    }

}
