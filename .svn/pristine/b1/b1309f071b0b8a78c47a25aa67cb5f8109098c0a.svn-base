package com.puxiang.mall.module.post.viewmodel.emojie;

import android.graphics.Bitmap;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import com.puxiang.mall.MyApplication;

import java.io.InputStream;


/**
 * @author tracyZhang  https://github.com/TracyZhangLei
 * @since 2014-4-4
 */

public class AnimatedGifDrawable extends AnimationDrawable {

    private int mCurrentIndex = 0;
    private UpdateListener mListener;
    public static final int GIF = 300;
    public static final int PNG = 301;

    public AnimatedGifDrawable(int type, int res, UpdateListener listener, int multiple) {
        mListener = listener;
        if (type == GIF) {
            InputStream resource = MyApplication.getContext().getResources().openRawResource(res);
            GifDecoder decoder = new GifDecoder();
            decoder.read(resource);
            // Iterate through the gif frames, add each as animation frame
            for (int i = 0; i < decoder.getFrameCount(); i++) {
                Bitmap bitmap = decoder.getFrame(i);
                BitmapDrawable drawable = new BitmapDrawable(bitmap);
                // Explicitly set the bounds in order for the frames to display
                drawable.setBounds(0, 0, multiple, multiple);
                addFrame(drawable, decoder.getDelay(i));
                if (i == 0) {
                    // Also set the bounds for this container drawable
                    setBounds(0, 0, multiple, multiple);
                }
            }
/*
            for (int i = 0; i < decoder.getFrameCount(); i++) {
                Bitmap bitmap = decoder.getFrame(i);
                BitmapDrawable drawable = new BitmapDrawable(bitmap);
                // Explicitly set the bounds in order for the frames to display
                drawable.setBounds(0, 0, bitmap.getWidth() * multiple, bitmap.getHeight() * multiple);
                addFrame(drawable, decoder.getDelay(i));
                if (i == 0) {
                    // Also set the bounds for this container drawable
                    setBounds(0, 0, bitmap.getWidth() * multiple, bitmap.getHeight() * multiple);
                }
            }
*/
        }
//        if (type == PNG) {
//            /*Bitmap bitmap = BitmapFactory.decodeResource(MyApplication.getContext().getResources(), res);
//            BitmapDrawable drawable = new BitmapDrawable(bitmap);*/
//            Drawable drawable = AppUtil.getDrawable(res);
//            setBounds(0, 0, multiple, multiple);
//            addFrame(drawable, 0);
//        }
    }

    /**
     * Naive method to proceed to next frame. Also notifies listener.
     */
    public void nextFrame() {
        mCurrentIndex = (mCurrentIndex + 1) % getNumberOfFrames();
        if (mListener != null) mListener.update();
    }

    /**
     * Return display duration for current frame
     */
    public int getFrameDuration() {
        return getDuration(mCurrentIndex);
    }

    /**
     * Return drawable for current frame
     */
    public Drawable getDrawable() {
        return getFrame(mCurrentIndex);
    }

    /**
     * Interface to notify listener to update/redraw
     * Can't figure out how to invalidate the drawable (or span in which it sits) itself to force redraw
     */
    public interface UpdateListener {
        void update();
    }

}
