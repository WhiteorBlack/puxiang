package com.puxiang.mall.utils;

import android.content.Context;
import android.graphics.Bitmap;

import com.orhanobut.logger.Logger;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;


public class FileUtil {
    public static boolean isFileExists(String path) {
        if (path == null) return false;
        File file = new File(path);
        return file.exists() && file.isFile();
    }

    /**
     * 创建文件
     *
     * @param path 要创建的文件路径
     * @return 如果创建成功或文件已存在则返回对应的文件对象，否则如果指定路径已存在
     * 相同名称的目录或者出错，则返回null
     */
    public static File createFile(String path) throws RuntimeException {
        try {
            File file = new File(path);
            if (!file.exists()) {
                File parentFlie = file.getParentFile();
                if (!parentFlie.exists()) {
                    parentFlie.mkdirs();
                }
                file.createNewFile();
            }
            return file.exists() && file.isFile() ? file : null;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 创建目录
     *
     * @param path 要创建的文件路径
     * @return 如果创建成功则返回创建的目录文件对象，如果指定路径
     * 已存在相同名称的文件或者创建目录失败，则返回null
     */
    public static File createDirectory(String path) {
        File file = new File(path);
        if (!file.exists()) file.mkdirs();
        //可能创建失败，所以再检查1次是否存在
        return file.exists() && file.isDirectory() ? file : null;
    }

    public static File writeBytesToFile(byte[] bytes, String path, boolean append) {
        File file = createFile(path);
        if (file == null) {
            return null;
        }
        try {
            BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(file, append));
            out.write(bytes, 0, bytes.length);
            out.flush();
            out.close();
            return file;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 删除文件,此方法会迭代删除所有子目录及其文件
     * <p>
     * 注意：如果在删除子目录或子文件时出错，那么已经删除的文件不能恢复，建议删除前先备份
     *
     * @param file 需要删除的文件对象
     * @return 如果删除成功则返回true，否则返回false
     */
    public static boolean delFile(File file) {
        if (file == null || !file.exists()) {
            return true;
        }

        if (file.isFile()) {
            return file.delete();
        }

        File[] childFiles = file.listFiles();
        if (childFiles.length == 0) {
            return file.delete();//说明是空目录，可以直接删除
        }

        //删除子目录
        for (File childFile : childFiles) {
            boolean isSuccess = delFile(childFile);
            if (!isSuccess) return false;
        }
        return file.delete();
    }

    public static boolean delFile(String filePath) {
        if (StringUtil.isEmpty(filePath)) {
            return true;
        }

        if (isFileExists(filePath)) {
            return delFile(createFile(filePath));
        }
        return true;
    }

    public static void unZip(File zipFile, File targetDirectory) throws IOException {
        ZipInputStream zis = new ZipInputStream(new BufferedInputStream(new FileInputStream(zipFile)));
        try {
            ZipEntry ze;
            int count;
            byte[] buffer = new byte[8192];
            while ((ze = zis.getNextEntry()) != null) {
                File file = new File(targetDirectory, ze.getName());
                File dir = ze.isDirectory() ? file : file.getParentFile();
                if (!dir.isDirectory() && !dir.mkdirs()) {
                    throw new FileNotFoundException("Failed to ensure directory: " + dir.getAbsolutePath());
                }
                if (ze.isDirectory()) {
                    continue;
                }
                FileOutputStream fout = new FileOutputStream(file);
                try {
                    while ((count = zis.read(buffer)) != -1) {
                        fout.write(buffer, 0, count);
                    }
                } finally {
                    fout.close();
                }
            }
        } finally {
            zis.close();
        }
    }

    /**
     * 根据URL获取保存文件的名称
     */
    public static String getSaveFileName(String urlStr) {
        if (StringUtil.isEmpty(urlStr)) {
            return null;
        }
        urlStr = urlStr.substring(urlStr.lastIndexOf("/") + 1, urlStr.length());
        return urlStr;
    }

    /**
     * 保存图片到本地
     *
     * @param bitmap
     * @param filePath 需要保存的完整路径
     */
    public static File saveImage(Bitmap bitmap, String filePath) {
        if (StringUtil.isEmpty(filePath)) {
            return null;
        }
        BufferedOutputStream out = null;
        File file = null;
        try {
            file = createFile(filePath);
            out = new BufferedOutputStream(new FileOutputStream(file));
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
        } catch (Exception e) {
            Logger.e("save bitmap to File " + filePath + " error");
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (Exception e) {
            }
        }
        return file;
    }

    /**
     * 读取文本数据
     *
     * @param context  程序上下文
     * @param fileName 文件名
     * @return String, 读取到的文本内容，失败返回null
     */
    public static String readAssets(Context context, String fileName) {
        InputStream is = null;
        String content = null;
        try {
            is = context.getAssets().open(fileName);
            if (is != null) {

                byte[] buffer = new byte[1024];
                ByteArrayOutputStream arrayOutputStream = new ByteArrayOutputStream();
                while (true) {
                    int readLength = is.read(buffer);
                    if (readLength == -1) break;
                    arrayOutputStream.write(buffer, 0, readLength);
                }
                is.close();
                arrayOutputStream.close();
                content = new String(arrayOutputStream.toByteArray());

            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
            content = null;
        } finally {
            try {
                if (is != null) is.close();
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
        }
        return content;
    }

    /**
     * 移动文件
     *
     * @param srcFileName 源文件完整路径
     * @param destDirName 目的目录完整路径
     * @return 文件移动成功返回true，否则返回false
     */
    public static boolean moveFile(String srcFileName, String destDirName) {
        File srcFile = new File(srcFileName);
        if (!srcFile.exists() || !srcFile.isFile()) {
            return false;
        }
        File destDir = new File(destDirName);
        if (!destDir.exists()) {
            destDir.mkdirs();
        }
        return srcFile.renameTo(new File(destDirName + File.separator + srcFile.getName()));
    }

    /**
     * 移动目录
     *
     * @param srcDirName  源目录完整路径
     * @param destDirName 目的目录完整路径
     * @return 目录移动成功返回true，否则返回false
     */
    public static boolean moveDirectory(String srcDirName, String destDirName) {
        File srcDir = new File(srcDirName);
        if (!srcDir.exists() || !srcDir.isDirectory()) {
            return false;
        }
        File destDir = new File(destDirName);
        if (!destDir.exists()) {
            destDir.mkdirs();
        }
        File[] sourceFiles = srcDir.listFiles();
        for (File sourceFile : sourceFiles) {
            if (sourceFile.isFile()) {
                moveFile(sourceFile.getAbsolutePath(), destDir.getAbsolutePath());
            } else if (sourceFile.isDirectory()) {
                moveDirectory(sourceFile.getAbsolutePath(), destDir.getAbsolutePath() + File.separator + sourceFile.getName());
            }
        }
        return srcDir.delete();
    }
}
