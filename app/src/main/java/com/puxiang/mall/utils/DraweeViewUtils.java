package com.puxiang.mall.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.facebook.binaryresource.BinaryResource;
import com.facebook.binaryresource.FileBinaryResource;
import com.facebook.cache.common.CacheKey;
import com.facebook.common.util.UriUtil;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.backends.pipeline.PipelineDraweeController;
import com.facebook.drawee.controller.BaseControllerListener;
import com.facebook.drawee.controller.ControllerListener;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.cache.DefaultCacheKeyFactory;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.core.ImagePipelineFactory;
import com.facebook.imagepipeline.image.ImageInfo;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.puxiang.mall.MyApplication;
import com.puxiang.mall.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;

import static android.content.ContentValues.TAG;

public class DraweeViewUtils {

    public final static int IMG_SIZE_HEAD_PIC = MyApplication.widthPixels / 8;
    public final static int IMG_SIZE_POST_LIST = MyApplication.widthPixels / 4;
    public final static int IMG_SIZE_USER_HEAD_PIC = MyApplication.widthPixels / 6;
    public final static int IMG_SIZE_PRODUCT = MyApplication.widthPixels / 3;
    public final static int IMG_SIZE_MATCH = MyApplication.widthPixels;

    public static final int TYPE_DEFAULT = 0;
    public static final int TYPE_RESIZE = 1;
    public static final int TYPE_IMG_AR = 2;
    public static final int TYPE_GIF_AR = 3;
    public static final int TYPE_URI_AR = 4;
    public static final int TYPE_URI_OR_URL = 5;

    private DraweeViewUtils() {
    }

    private static DraweeViewUtils bean;

    public static DraweeViewUtils getInstance() {
        if (bean == null) {
            bean = new DraweeViewUtils();
        }
        return bean;
    }

    //    }
    public void loadImg(final SimpleDraweeView sdv, String url, int w, int h) {
        if (sdv == null || StringUtil.isEmpty(url)) {
            return;
        }
        ImageRequest request = ImageRequestBuilder
                .newBuilderWithSource(Uri.parse(url))//url
                .setProgressiveRenderingEnabled(true)
                .setResizeOptions(new ResizeOptions(w, h))//图像质量，可以缩小大图片体积，提升UI的流畅程度
                .build();


        DraweeController controller = Fresco
                .newDraweeControllerBuilder()
                .setImageRequest(request)
                .setAutoPlayAnimations(true)
                // .setControllerListener(controllerListener)
                .setOldController(sdv.getController())
                .build();
        sdv.setController(controller);
    }


//    private static final int resizeHeight = MyApplication.heightPixels / 3;
//    private static final int resizeWidth = (int) ((MyApplication.widthPixels - 48) / (float)
// MyApplication
// .widthPixels * 720);
//
//    public void loadResizeImg(final SimpleDraweeView sdv, String url) {
//        if (sdv == null || StringUtil.isEmpty(url)) {
//            return;
//        }
//        int i = url.lastIndexOf("?");
//        final boolean b = i != -1;
//        int w = 0;
//        int h = 0;
//
//        if (b) {
//            int width;
//            int height;
//            String size = url.substring(i + 1);
//            String[] xes = size.split("x");
//            Integer imageInfoWidth = Integer.valueOf(xes[0]);
//            Integer imageInfoHeight = Integer.valueOf(xes[1]);
//
//            Logger.i("loadResizeImg ", "imageInfoWidth:" + imageInfoWidth + "\n imageInfoHeight
// : " + imageInfoHeight);
//            Logger.i("loadResizeImg ", "resizeWidth:" + resizeWidth + "\n resizeHeight : " +
// resizeHeight);
////            int width = imageInfoWidth >= 672 ? ViewGroup.LayoutParams.MATCH_PARENT :
/// imageInfoWidth;
//
//            float ratio = imageInfoWidth / (float) imageInfoHeight;
//            if (imageInfoHeight >= resizeHeight && ratio <= 1) {
//                width = ViewGroup.LayoutParams.WRAP_CONTENT;
//                height = resizeHeight;
//                h = resizeHeight;
//                w = (int) (h * ratio);
//            } else if (imageInfoWidth >= resizeWidth) {
//                w = resizeWidth;
//                h = (int) (w / ratio);
//                width = ViewGroup.LayoutParams.MATCH_PARENT;
//                height = ViewGroup.LayoutParams.WRAP_CONTENT;
//            } else {
//                w = imageInfoWidth;
//                h = (int) (w / ratio);
//                width = imageInfoWidth;
//                height = ViewGroup.LayoutParams.WRAP_CONTENT;
//            }
//            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(width, height);
//            params.topMargin = 36;
//            sdv.setLayoutParams(params);
//            sdv.setAspectRatio(ratio);
//            AutoUtils.auto(sdv);
//        }
//        ControllerListener controllerListener = new BaseControllerListener<ImageInfo>() {
//            @Override
//            public void onFinalImageSet(
//                    String id,
//                    @Nullable ImageInfo imageInfo,
//                    @Nullable Animatable anim) {
//                if (!b) {
//                    //加载成功,此处可以获得bitmap的宽高等信息
//                    int imageInfoWidth = imageInfo != null ? imageInfo.getWidth() : 1;
//                    int imageInfoHeight = imageInfo != null ? imageInfo.getHeight() : 1;
////                    int width = imageInfoWidth >= 672 ? ViewGroup.LayoutParams.MATCH_PARENT :
/// imageInfoWidth;
//                    //           int height = imageInfo != null ? imageInfo.getHeight() : 0;
//                    //        Log.e("11111", "onFinalImageSet: " + width + "==" + height);
//                    int width;
//                    int height;
//                    float ratio = imageInfoWidth / (float) imageInfoHeight;
//                    if (imageInfoHeight >= 800 && ratio < 1) {
//                        width = ViewGroup.LayoutParams.WRAP_CONTENT;
//                        height = 800;
//                    } else if (imageInfoWidth >= 672) {
//                        width = ViewGroup.LayoutParams.MATCH_PARENT;
//                        height = ViewGroup.LayoutParams.WRAP_CONTENT;
//                    } else {
//                        width = imageInfoWidth;
//                        height = ViewGroup.LayoutParams.WRAP_CONTENT;
//                    }
//                    FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(width, height);
////                    FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(width,
/// ViewGroup.LayoutParams.WRAP_CONTENT);
//                    params.topMargin = 36;
//                    sdv.setLayoutParams(params);
//                    sdv.setAspectRatio(imageInfoWidth / (float) imageInfoHeight);
//                    AutoUtils.auto(sdv);
//                    //    Log.e("2222", "sdv size: " + sdv.getWidth() + "----" + sdv.getHeight());
//                }
//            }
//        };
//
//        ImageRequestBuilder builder = ImageRequestBuilder
//                .newBuilderWithSource(Uri.parse(url))//url
//                .setProgressiveRenderingEnabled(true)
//                .setAutoRotateEnabled(false);
//
//        if (w < 0 && h > 0) {
//            builder.setResizeOptions(new ResizeOptions(w, h));//图像质量，可以缩小大图片体积，提升UI的流畅程度
//        }
//
//        AbstractDraweeController build = Fresco.newDraweeControllerBuilder()
//                .setImageRequest(builder.build())
////                .setUri(url)
//                .setControllerListener(controllerListener)
//                .setOldController(sdv.getController())
//                .setAutoPlayAnimations(true)
//                .build();
//        sdv.setController(build);
//    }

    private static final int resize = 672;

    /**
     * 根据URL后缀压缩图片
     *
     * @param sdv
     * @param url
     */
    public void loadResizeImg(final SimpleDraweeView sdv, String url) {
        if (sdv == null || StringUtil.isEmpty(url)) {
            return;
        }
        int i = url.lastIndexOf("?");
        final boolean b = i != -1;
        int w = 0;
        int h = 0;

        if (b) {
            int width;
            int height;
            String size = url.substring(i + 1);
            String[] xes = size.split("x");
            Integer imageInfoWidth = Integer.valueOf(xes[0]);
            Integer imageInfoHeight = Integer.valueOf(xes[1]);

            float ratio = imageInfoWidth / (float) imageInfoHeight;
            if (ratio < 1) {
                if (imageInfoWidth >= resize / 2) {
                    width = resize / 2;
                    int mh = (int) (width / ratio);
                    height = mh > 400 ? 400 : mh;
                } else {
                    width = imageInfoWidth;
                    int mh = (int) (width / ratio);
                    height = mh > 400 ? 400 : mh;
                }
                w = width;
                h = height;
            } else {
                if (imageInfoHeight >= 300) {
                    height = 300;
                    int mw = (int) (height * ratio);
                    if (mw > resize) {
                        width = ViewGroup.LayoutParams.MATCH_PARENT;
                        height = (int) (resize / ratio);
                        w = resize;
                    } else {
                        w = width = mw;
                    }
                    h = height;
                } else {
                    if (imageInfoWidth >= resize) {
                        width = ViewGroup.LayoutParams.MATCH_PARENT;
                        h = height = (int) (resize / ratio);
                        w = resize;
                    } else {
                        w = width = imageInfoWidth;
                        h = height = imageInfoHeight;
                    }
                }
            }

            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(width, height);
            params.topMargin = 36;
            sdv.setLayoutParams(params);
            sdv.setAspectRatio(ratio);
            AutoUtils.auto(sdv);
        }
        ControllerListener controllerListener = new BaseControllerListener<ImageInfo>() {
            @Override
            public void onFinalImageSet(
                    String id,
                    @Nullable ImageInfo imageInfo,
                    @Nullable Animatable anim) {
                if (!b) {
                    int imageInfoWidth = imageInfo.getWidth();
                    int imageInfoHeight = imageInfo.getHeight();
                    float ratio = imageInfoWidth / (float) imageInfoHeight;
                    int width;
                    int height;
                    if (ratio < 1) {
                        if (imageInfoWidth >= resize / 2) {
                            width = resize / 2;
                            int mh = (int) (width / ratio);
                            height = mh > 400 ? 400 : mh;
                        } else {
                            width = imageInfoWidth;
                            int mh = (int) (width / ratio);
                            height = mh > 400 ? 400 : mh;
                        }
                    } else {
                        if (imageInfoHeight >= 300) {
                            height = 300;
                            int mw = (int) (height * ratio);
                            if (mw > resize) {
                                width = ViewGroup.LayoutParams.MATCH_PARENT;
                                height = (int) (resize / ratio);
                            } else {
                                width = mw;
                            }
                        } else {
                            if (imageInfoWidth >= resize) {
                                width = ViewGroup.LayoutParams.MATCH_PARENT;
                                height = (int) (resize / ratio);
                            } else {
                                width = imageInfoWidth;
                                height = imageInfoHeight;
                            }
                        }
                    }

                    FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(width, height);
                    params.topMargin = 36;
                    sdv.setLayoutParams(params);
                    sdv.setAspectRatio(ratio);
                    AutoUtils.auto(sdv);
                }
            }
        };

        ImageRequestBuilder builder = ImageRequestBuilder
                .newBuilderWithSource(Uri.parse(url))//url
                .setProgressiveRenderingEnabled(true)
                .setAutoRotateEnabled(false);

        if (w > 0 && h > 0) {
            builder.setResizeOptions(new ResizeOptions(w, h));//图像质量，可以缩小大图片体积，提升UI的流畅程度
            //   builder.setResizeOptions(new ResizeOptions((int) (w * resizeRatio), (int) (h *
            // resizeRatio)));//图像质量，可以缩小大图片体积，提升UI的流畅程度
        }

        DraweeController build = Fresco.newDraweeControllerBuilder()
                .setImageRequest(builder.build())
//                .setUri(url)
                .setControllerListener(controllerListener)
                .setOldController(sdv.getController())
                .setAutoPlayAnimations(true)
                .build();
        sdv.setController(build);
    }

    public void loadImg(final SimpleDraweeView sdv, String url) {

        if (sdv == null || StringUtil.isEmpty(url)) {
            return;
        }
        ImageRequest request = ImageRequestBuilder
                .newBuilderWithSource(Uri.parse(url))//url
                .setProgressiveRenderingEnabled(true)
                //图像质量，可以缩小大图片体积，提升UI的流畅程度
                .build();


        DraweeController controller = Fresco
                .newDraweeControllerBuilder()
                .setImageRequest(request)
                .setAutoPlayAnimations(true)
                .setOldController(sdv.getController())
                .build();
        sdv.setController(controller);
    }

    private final static int MAX_INFLATE_SIZE =
            (int) ((MyApplication.widthPixels - (MyApplication.widthPixels / 720) * 24) * 0.8);

    /**
     * 宽度充满，动态设置高度比例
     *
     * @param sdv
     * @param url
     */
    public void loadImgAR(final SimpleDraweeView sdv, String url) {
        if (sdv == null || StringUtil.isEmpty(url)) {
            return;
        }
        int i = url.lastIndexOf("?");
        final boolean b = i != -1;
        float ratio = 0;
        if (b) {
            String size = url.substring(i + 1);
            String[] xes = size.split("x");
            ratio = Integer.valueOf(xes[0]) / (float) Integer.valueOf(xes[1]);
            sdv.setAspectRatio(ratio);
        }

        ControllerListener controllerListener = new BaseControllerListener<ImageInfo>() {
            @Override
            public void onFinalImageSet(
                    String id,
                    @Nullable ImageInfo imageInfo,
                    @Nullable Animatable anim) {
                if (!b) {
                    //加载成功,此处可以获得bitmap的宽高等信息
                    float ratio = imageInfo.getWidth() / (float) imageInfo.getHeight();
                    sdv.setAspectRatio(ratio);
                }
            }
        };

        ImageRequest request = getImageMaxSizeRequest(url, ratio);
        DraweeController controller = Fresco
                .newDraweeControllerBuilder()
                .setImageRequest(request)
                .setAutoPlayAnimations(true)
                .setControllerListener(controllerListener)
                .setOldController(sdv.getController())
                .build();
        sdv.setController(controller);
    }

    /**
     * 根据屏幕最大显示像素，动态压缩展示
     *
     * @param url   图片地址
     * @param ratio 宽高比
     * @return 图片请求体
     */
    private ImageRequest getImageMaxSizeRequest(String url, float ratio) {
        ImageRequestBuilder requestBuilder = ImageRequestBuilder
                .newBuilderWithSource(Uri.parse(url))
                .setProgressiveRenderingEnabled(true);
        if (ratio != 0) {
            requestBuilder.setResizeOptions(
                    new ResizeOptions(MAX_INFLATE_SIZE, ((int) (MAX_INFLATE_SIZE * ratio))));
        }
        return requestBuilder.build();
    }

    /**
     * 加载GIF,宽度充满，动态设置高度比例
     *
     * @param sdv
     * @param url
     */
    public void loadGifAR(final SimpleDraweeView sdv, String url) {
        if (sdv == null || StringUtil.isEmpty(url)) {
            return;
        }
        ControllerListener controllerListener = new BaseControllerListener<ImageInfo>() {
            @Override
            public void onFinalImageSet(
                    String id,
                    @Nullable ImageInfo imageInfo,
                    @Nullable Animatable anim) {
                //加载成功,此处可以获得bitmap的宽高等信息
                float ratio = imageInfo.getWidth() / (float) imageInfo.getHeight();
                sdv.setAspectRatio(ratio);
                ViewGroup group = (ViewGroup) sdv.getParent();
                group.removeView(group.findViewById(R.id.fl_loading));
                Log.e(TAG, "removeView: ");
            }
        };

        ImageRequest request = ImageRequestBuilder
                .newBuilderWithSource(Uri.parse(url))//url
                .setProgressiveRenderingEnabled(true)
                // .setResizeOptions(new ResizeOptions(sdv.getWidth(), sdv.getHeight()))
                // 图像质量，可以缩小大图片体积，提升UI的流畅程度
                .build();


        DraweeController controller = Fresco
                .newDraweeControllerBuilder()
                .setImageRequest(request)
                .setAutoPlayAnimations(true)
                .setControllerListener(controllerListener)
                .setOldController(sdv.getController())
                .build();
        sdv.setController(controller);
    }

    /**
     * 加载本地图片资源
     *
     * @param sdv
     * @param path 图片文件路径
     * @param w
     * @param h
     */
    public void loadImgUri(final SimpleDraweeView sdv, String path, int w, int h) {
        if (sdv == null || StringUtil.isEmpty(path)) {
            Log.e("22", "sdv: =null");
            return;
        }
        Uri uri = new Uri.Builder()
                .scheme(UriUtil.LOCAL_FILE_SCHEME)
                .path(path)
                .build();
        ImageRequest request = ImageRequestBuilder
                .newBuilderWithSource(uri)//url
                .setProgressiveRenderingEnabled(true)
                .setResizeOptions(new ResizeOptions(w, h))//图像质量，可以缩小大图片体积，提升UI的流畅程度
                .build();

        PipelineDraweeController controller = (PipelineDraweeController) Fresco
                .newDraweeControllerBuilder()
                .setImageRequest(request)
                .setAutoPlayAnimations(true)
                // .setControllerListener(controllerListener)
                .setOldController(sdv.getController())
                .build();
        sdv.setController(controller);
    }

    public static String getResUri(@DrawableRes int resId) {
        StringBuilder builder = new StringBuilder("res://");
        builder.append(MyApplication.getContext().getPackageName())
                .append("/")
                .append(resId);
        return builder.toString();
    }

//    public void loadImgUri(final SimpleDraweeView sdv, Uri uri, int w, int h) {
//
//        if (sdv == null) {
//            return;
//        }
//        ImageRequest request = ImageRequestBuilder
//                .newBuilderWithSource(uri)//url
//                .setProgressiveRenderingEnabled(true)
//                .setResizeOptions(new ResizeOptions(w, h))//图像质量，可以缩小大图片体积，提升UI的流畅程度
//                .build();
//
//        PipelineDraweeController controller = (PipelineDraweeController) Fresco
// .newDraweeControllerBuilder()
//                .setImageRequest(request)
//                .setAutoPlayAnimations(true)
//                // .setControllerListener(controllerListener)
//                .setOldController(sdv.getController())
//                .build();
//        sdv.setController(controller);
//    }
//
//    public void loadImgUri(final SimpleDraweeView sdv, int resourcesId) {
//
//        if (sdv == null || resourcesId < 0) {
//            return;
//        }
//        Uri uri = Uri.parse("res://" + MyApplication.getContext().getPackageName() + "/" +
// resourcesId);
//        ImageRequest request = ImageRequestBuilder
//                .newBuilderWithSource(uri)//url
//                .setProgressiveRenderingEnabled(true)
////                .setResizeOptions(new ResizeOptions(sdv.getWidth(), sdv.getHeight()))
/// //图像质量，可以缩小大图片体积，提升UI的流畅程度
//                .build();
//
//        PipelineDraweeController controller = (PipelineDraweeController) Fresco
// .newDraweeControllerBuilder()
//                .setImageRequest(request)
//                .setAutoPlayAnimations(true)
//                // .setControllerListener(controllerListener)
//                .setOldController(sdv.getController())
//                .build();
//        sdv.setController(controller);
//    }

    public Drawable getCacheDrawable(String url) {
        BinaryResource resource = getBinaryResource(url);
        if (resource == null) {
            return MyApplication.getContext().getResources().getDrawable(R.mipmap.ic_launcher);
        } else {
            final File file = ((FileBinaryResource) resource).getFile();
            Bitmap bitmap = BitmapFactory.decodeFile(file.getPath());
            return new BitmapDrawable(bitmap);
        }
    }

    public File saveCacheDrawable(String url) {
        Log.e(TAG, "saveCacheDrawable: " + url);
        BinaryResource resource = getBinaryResource(url);
        final File oldFile = ((FileBinaryResource) resource).getFile();
        int i = url.lastIndexOf(".");
        String type = url.substring(i, i + 4);

        File appDir = new File(Environment.getExternalStorageDirectory(), "somic");
        if (!appDir.exists()) {
            appDir.mkdir();
        }

        String oldFileName = oldFile.getName();
        Log.e(TAG, "oldFileName: " + oldFileName);
        String name = oldFileName.substring(0, oldFileName.lastIndexOf("."));
        String fileName = name + type;
        File newFile = new File(appDir, fileName);

        if (newFile.exists()) {
            return newFile;
        }

        return copyFile(oldFile, newFile);
    }

    private BinaryResource getBinaryResource(String url) {
        ImageRequest imageRequest = ImageRequestBuilder.newBuilderWithSource(Uri.parse(url))
                .build();
        CacheKey cacheKey = DefaultCacheKeyFactory.getInstance()
                .getEncodedCacheKey(imageRequest, MyApplication.getContext());
        return ImagePipelineFactory.getInstance().getMainFileCache()
                .getResource(cacheKey);
    }

//    public File sendCacheFile(String url) {
//        ImageRequest imageRequest = ImageRequestBuilder.newBuilderWithSource(Uri.parse(url))
// .build();
//        CacheKey cacheKey = DefaultCacheKeyFactory.getInstance()
//                .getEncodedCacheKey(imageRequest, MyApplication.getContext());
//        BinaryResource resource = ImagePipelineFactory.getInstance()
//                .getMainDiskStorageCache().getResource(cacheKey);
//        final File file = ((FileBinaryResource) resource).getFile();
//
//        return file;
//    }

    private static String path = Environment.getExternalStorageDirectory().getAbsolutePath();

    public File copyFile(File oldFile, File newFile) {
        try {

            int byteSum = 0;
            int byteRead = 0;
            if (oldFile.exists()) { //文件不存在时
                InputStream inStream = new FileInputStream(oldFile); //读入原文件
                FileOutputStream fs = new FileOutputStream(newFile);
                byte[] buffer = new byte[1444];
                int length;
                while ((byteRead = inStream.read(buffer)) != -1) {
                    byteSum += byteRead; //字节数 文件大小
                    System.out.println(byteSum);
                    fs.write(buffer, 0, byteRead);
                }
                inStream.close();
                return newFile;
            }
        } catch (Exception e) {
            System.out.println("复制单个文件操作出错");
            e.printStackTrace();
        }
        return null;
    }

    public static Uri getUriPath(String path) {
        if (TextUtils.isEmpty(path)) {
            return null;
        }
        Uri uri = new Uri.Builder().scheme(UriUtil.LOCAL_FILE_SCHEME).path(path).build();
        return uri;
    }

}
