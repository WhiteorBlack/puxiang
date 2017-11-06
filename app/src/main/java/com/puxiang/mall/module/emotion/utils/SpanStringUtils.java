/**
 *
 */
package com.puxiang.mall.module.emotion.utils;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.util.Log;
import android.widget.TextView;

import com.puxiang.mall.MyApplication;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @description :文本中的emojb字符处理为表情图片
 */
public class SpanStringUtils {

    private static String TAG = "SpanStringUtils";

    public static SpannableString getEmotionContent(int emotion_map_type, final TextView tv, String source) {
        SpannableString spannableString = new SpannableString(source);
        Resources res = MyApplication.getContext().getResources();

        String regexEmotion = "\\[em_\\d+_\\d+\\]";
        Pattern patternEmotion = Pattern.compile(regexEmotion);
        Matcher matcherEmotion = patternEmotion.matcher(spannableString);

        while (matcherEmotion.find()) {
            // 获取匹配到的具体字符
            String key = matcherEmotion.group();
            // 匹配字符串的开始位置
            int start = matcherEmotion.start();
            int type = Integer.parseInt(key.substring(key.indexOf("_") + 1, key.lastIndexOf("_")));
            // 利用表情名字获取到对应的图片
            Integer imgRes = EmotionUtils.getImgByName(type, key);
            if (imgRes != null) {
                // 压缩表情图片
                int size = (int) tv.getTextSize() * 13 / 9;
                Log.e(TAG, "imgRes: " + imgRes);
                Bitmap bitmap = BitmapFactory.decodeResource(res, imgRes);
                Bitmap scaleBitmap = Bitmap.createScaledBitmap(bitmap, size, size, true);
                ImageSpan span = new ImageSpan(MyApplication.getContext(), scaleBitmap);
                spannableString.setSpan(span, start, start + key.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }
        return spannableString;
    }
}
