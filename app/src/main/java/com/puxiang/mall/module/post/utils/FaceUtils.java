package com.puxiang.mall.module.post.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ImageSpan;
import android.widget.TextView;

import com.puxiang.mall.MyApplication;
import com.puxiang.mall.module.emotion.utils.EmotionUtils;
import com.puxiang.mall.module.post.viewmodel.emojie.AnimatedGifDrawable;
import com.puxiang.mall.module.post.viewmodel.emojie.AnimatedImageSpan;

import java.lang.ref.WeakReference;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.reactivex.Observable;

public class FaceUtils {

    public static boolean isRun = true;

    public static void resetGifRun() {
        isRun = false;
        Observable.timer(1, TimeUnit.SECONDS)
                .subscribe(aLong -> isRun = true);
    }

    /**
     * check the cacke first
     *
     * @param message
     * @param tv
     * @return
     */
    public static SpannableString convertNormalStringToSpannableString(String message, final TextView tv) {

        SpannableString value = SpannableString.valueOf(message);

        String regexEmotion = "\\[em_\\d+_\\d+\\]";

        Pattern patternEmotion = Pattern.compile(regexEmotion);
        Matcher localMatcher = patternEmotion.matcher(message);
        while (localMatcher.find()) {
            // 获取匹配到的具体字符
            String key = localMatcher.group();
            int start = localMatcher.start();
            // 匹配字符串的开始位置
            //  Log.e("111", "convertNormalStringToSpannableString: " + key);
            int type = Integer.parseInt(key.substring(key.indexOf("_") + 1, key.lastIndexOf("_")));

            // 利用表情名字获取到对应的图片
            Integer face = EmotionUtils.getImgByName(type, key);

            if (-1 != face) {//wrapping with weakReference
                int lx = AnimatedGifDrawable.PNG;
                int size = (int) tv.getTextSize() * 13 / 7;
                if (type == 1 || type == 2 || type == 3 || type == 4 || type == 6) {
                    lx = AnimatedGifDrawable.GIF;
                    size = (int) tv.getTextSize() * 13 / 3;
                }
                WeakReference localImageSpanWr;
                if (lx == AnimatedGifDrawable.GIF) {
                    AnimatedGifDrawable gifDrawable = new AnimatedGifDrawable(lx, face, tv::postInvalidate, size);
                    localImageSpanWr = new WeakReference(new AnimatedImageSpan(gifDrawable));
                } else {
                    Bitmap bitmap = BitmapFactory.decodeResource(MyApplication.getContext().getResources(), face);
                    Bitmap scaleBitmap = Bitmap.createScaledBitmap(bitmap, size, size, true);
                    localImageSpanWr = new WeakReference(new ImageSpan(MyApplication.getContext(), scaleBitmap));
                }
                value.setSpan(localImageSpanWr.get(), start, start + key.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }
        return value;
    }

//    /**
//     * check the cacke first
//     *
//     * @param message
//     * @param tv
//     * @return
//     */
//    private SpannableString convertNormalStringToSpannableString(int id, String message, final TextView tv) {
//
//        /*SpannableString ss = sa.get(id);
//        if (ss != null) {
//            return ss;
//        }
//*/
//        SpannableString value = SpannableString.valueOf(message);
//        Resources res = MyApplication.getContext().getResources();
//        String regexEmotion = "\\[([\u4e00-\u9fa5\\w])+\\]";
//        Pattern patternEmotion = Pattern.compile(regexEmotion);
//        Matcher localMatcher = patternEmotion.matcher(message);
//        //Matcher localMatcher = EMOTION_URL.matcher(value);
//        while (localMatcher.find()) {
////            String str2 = localMatcher.group(0);
//           /* int k = localMatcher.start();
//            int m = localMatcher.end();
//            Log.e(TAG, "convertNormalStringToSpannableString: " + k + "---" + m);
//            if (m - k < 8) {*/
//            // 获取匹配到的具体字符
//            String key = localMatcher.group();
//            int start = localMatcher.start();
//            // 匹配字符串的开始位置
//        /*    Log.e(TAG, "emotion_map_type: " + localMatcher);
//            Log.e(TAG, "key: " + key);*/
//            if (!key.contains("_")) {
//                continue;
//            }
//            int type = Integer.parseInt(key.substring(key.indexOf("_") + 1, key.lastIndexOf("_")));
//            //   Log.e(TAG, "type: " + type);
//            // 利用表情名字获取到对应的图片
//            Integer face = EmotionUtils.getImgByName(type, key);
//            //   int face = fm.getFaceId(str2);
//
//            if (-1 != face) {//wrapping with weakReference
//                int lx = AnimatedGifDrawable.PNG;
//                int size = (int) tv.getTextSize() * 13 / 7;
//                if (type == 1 || type == 2 || type == 3 || type == 4 || type == 5 || type == 6) {
//                    if (type != 5) {
//                        lx = AnimatedGifDrawable.GIF;
//                    }
//                    size = (int) tv.getTextSize() * 13 / 3;
//                }
//
//                AnimatedGifDrawable gifDrawable = new AnimatedGifDrawable(lx, face, new AnimatedGifDrawable
//                        .UpdateListener() {
//                    @Override
//                    public void update() {//update the textview
//                        tv.postInvalidate();
//                    }
//                }, size);
//                AnimatedImageSpan imageSpan = new AnimatedImageSpan(gifDrawable);
//                WeakReference<AnimatedImageSpan> localImageSpanRef = new WeakReference<AnimatedImageSpan>(
//                        imageSpan);
//                value.setSpan(localImageSpanRef.get(), start, start + key.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
//            }
//        }
//        return value;
//    }
}
