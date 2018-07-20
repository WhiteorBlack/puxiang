package com.puxiang.mall.utils.fullhtml;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.Html;
import android.text.Layout;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.AlignmentSpan;
import android.text.style.CharacterStyle;
import android.text.style.ForegroundColorSpan;
import android.text.style.URLSpan;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import com.puxiang.mall.utils.ActivityUtil;
import com.puxiang.mall.utils.StringUtil;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * TextViewForFullHtml是一个增强的TextView,修改了TextView.fromHtml不能设置
 * 字体大小的问题(原生的只能设置small\normal\big),同时,兼容了ActionScript脚本
 */
public class TextViewForFullHtml extends android.support.v7.widget.AppCompatTextView {


    private static Context sContext;

    public TextViewForFullHtml(Context context) {
        super(context);
        sContext = context;
    }

    public TextViewForFullHtml(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TextViewForFullHtml(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private void txtViewSetText(TextView view, CharSequence text, BufferType type) {
        CharSequence t = text;
        if (t instanceof Spannable) {
            int end = text.length();
            Spannable sp = (Spannable) t;
            URLSpan[] urls = sp.getSpans(0, end, URLSpan.class);
//            AlignmentSpan[] spans = sp.getSpans(0, end, AlignmentSpan.class);
//            Log.e("111", "txtViewSetText: " + spans.length);
//            String toHtml = FullHtml.toHtml(sp);
//            Log.e("111", "toHtml: " + toHtml);
            SpannableStringBuilder style = new SpannableStringBuilder(text);
            if (urls.length > 0) {
                view.setMovementMethod(LinkMovementMethod.getInstance());
                for (URLSpan url : urls) {
                    HJURLSpan myURLSpan = new HJURLSpan(view, url.getURL());
                    style.clearSpans();
                    style.setSpan(myURLSpan, sp.getSpanStart(url),
                            sp.getSpanEnd(url),
                            Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
                }
            }
            view.setText(style, type);
        }
    }

    @Override
    public void setText(CharSequence text, BufferType type) {
        super.setText(text, type);
    }



    /**
     * 定义空格回车换行符
     */
    private static final String REGEX_SPACE = "\\s*|\t|\r|\n";

    public void loadContent(String content) {
        if (TextUtils.isEmpty(content)){
            return;
        }
        content = content.replace("<p", "<d");
        content = content.replace("</p>", "</d><br/>");
        //  content = ActionscriptTextUtils.parseFontHTML(content);

//        String reg = ">\\s+([^\\s<]*)\\s+<";
//        String str = content.replaceAll(reg, ">$1<");
//        Log.e("111", "loadContent: " + str);
        String reg1 = "^>\\s*|\\t|\\r|\\n$<";
        content = content.replaceAll(reg1, "");
        //  Log.e("111", "loadContent: " + str);

        String p_regex = "<d style=\"text-align:center;\">(.*?)</d>";
        Pattern p2 = Pattern.compile(p_regex);
        Matcher m2 = p2.matcher(content);
        // String c_str = "left";
        String key;
        SpannableStringBuilder builder = new SpannableStringBuilder();

        int end = 0;
        while (m2.find()) {
            int start = m2.start();
            key = m2.group(1).replaceAll(" ", "");
            String sub = content.substring(end, start);
            end = m2.end();
            if (!StringUtil.isEmpty(sub)) {
                Spanned spanned = Html.fromHtml(sub);
                builder.append(spanned);
            }
            Spanned spanned = Html.fromHtml(key);
            AlignmentSpan.Standard standard = new AlignmentSpan.Standard(Layout.Alignment.ALIGN_CENTER);
            SpannableString spannableString = new SpannableString(spanned);
            spannableString.setSpan(standard, 0, spanned.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            builder.append(spannableString);
        }
        if (end < content.length()) {
            String sub = content.substring(end, content.length());
            if (!StringUtil.isEmpty(sub)) {
                Spanned spanned = Html.fromHtml(sub);
                builder.append(spanned);
            }
        }
        txtViewSetText(TextViewForFullHtml.this, builder, BufferType.SPANNABLE);
    }

    public SpannableStringBuilder highlight(String text, int color1, int color2, int fontSize) {
        SpannableStringBuilder spannable = new SpannableStringBuilder(text);//用于可变字符串
        CharacterStyle span_0 = null, span_1 = null, span_2;
        int end = text.indexOf("\n");
        if (end == -1) {//如果没有换行符就使用第一种颜色显示
            span_0 = new ForegroundColorSpan(color1);
            spannable.setSpan(span_0, 0, text.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        } else {
            span_0 = new ForegroundColorSpan(color1);
            span_1 = new ForegroundColorSpan(color2);
            spannable.setSpan(span_0, 0, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            spannable.setSpan(span_1, end + 1, text.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

            span_2 = new AbsoluteSizeSpan(fontSize);//字体大小
            spannable.setSpan(span_2, end + 1, text.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        return spannable;
    }

    @SuppressLint("ParcelCreator")
    private static class HJURLSpan extends URLSpan {
        private String mUrl;
        private View mView;

        HJURLSpan(View view, String url) {
            super(url);
            mView = view;
            mUrl = url;
        }

        @Override
        public void onClick(View widget) {
            ActivityUtil.openChrome(widget.getContext(), mUrl);
            // Toast.makeText(sContext, mUrl, Toast.LENGTH_SHORT).show();
        }
    }
}
