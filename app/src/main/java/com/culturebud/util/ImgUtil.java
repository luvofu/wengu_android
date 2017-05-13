package com.culturebud.util;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.support.annotation.RequiresApi;

import com.culturebud.BaseApp;
import com.culturebud.R;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by XieWei on 2016/11/10.
 */

public class ImgUtil {

    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    public static Bitmap blurBitmap(Bitmap bitmap) {

        //Let's create an empty bitmap with the same size of the bitmap we want to blur
        int y = bitmap.getHeight() / 3;
        int h = bitmap.getHeight() / 3;
        final Bitmap portionToBlur = Bitmap.createBitmap(bitmap, 0, y, bitmap.getWidth(), h);
        final Bitmap blurredBitmap = portionToBlur.copy(Bitmap.Config.ARGB_8888, true);
        //Instantiate a new Renderscript
        RenderScript rs = RenderScript.create(BaseApp.getInstance());

        //Create an Intrinsic Blur Script using the Renderscript
        ScriptIntrinsicBlur blurScript = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs));

        //Create the Allocations (in/out) with the Renderscript and the in/out bitmaps
        Allocation allIn = Allocation.createFromBitmap(rs, portionToBlur);
        Allocation allOut = Allocation.createFromBitmap(rs, blurredBitmap);

        //Set the radius of the blur
        blurScript.setRadius(25F);

        //Perform the Renderscript
        blurScript.setInput(allIn);
        blurScript.forEach(allOut);
        allOut.copyTo(blurredBitmap);
        new Canvas(blurredBitmap).drawColor(BaseApp.getInstance().getResources()
                .getColor(R.color.blur_scrim));

        final Bitmap newBitmap = portionToBlur.copy(Bitmap.Config.ARGB_8888, true);
        final Canvas canvas = new Canvas(newBitmap);
        canvas.drawBitmap(blurredBitmap, 0, h, new Paint());
        //Copy the final bitmap created by the out Allocation to the outBitmap
        //allOut.copyTo(outBitmap);

        //recycle the original bitmap
        bitmap.recycle();

        //After finishing everything, we destroy the Renderscript.
        rs.destroy();

        return newBitmap;
    }

    public static Bitmap doBlur(Bitmap sentBitmap, int radius, boolean canReuseInBitmap) {
        Bitmap bitmap;
        if (canReuseInBitmap) {
            //可复用视图
            bitmap = sentBitmap;
        } else {
            bitmap = sentBitmap.copy(sentBitmap.getConfig(), true);
        }

        if (radius < 0) {
            return (null);
        }

        int w = bitmap.getWidth();
        int h = bitmap.getHeight();

        int[] pix = new int[w * h];
        bitmap.getPixels(pix, 0, w, 0, 0, w, h);

        int wm = w - 1;
        int hm = h - 1;
        int wh = w * h;
        int div = radius + radius + 1;

        int r[] = new int[wh];
        int g[] = new int[wh];
        int b[] = new int[wh];
        int rsum, gsum, bsum, x, y, i, p, yp, yi, yw;
        int vmin[] = new int[Math.max(w, h)];

        int divsum = (div + 1) >> 1;
        divsum *= divsum;
        int dv[] = new int[256 * divsum];
        for (i = 0; i < 256 * divsum; i++) {
            dv[i] = (i / divsum);
        }

        yw = yi = 0;

        int[][] stack = new int[div][3];
        int stackpointer;
        int stackstart;
        int[] sir;
        int rbs;
        int r1 = radius + 1;
        int routsum, goutsum, boutsum;
        int rinsum, ginsum, binsum;

        for (y = 0; y < h; y++) {
            rinsum = ginsum = binsum = routsum = goutsum = boutsum = rsum = gsum = bsum = 0;
            for (i = -radius; i <= radius; i++) {
                p = pix[yi + Math.min(wm, Math.max(i, 0))];
                sir = stack[i + radius];
                sir[0] = (p & 0xff0000) >> 16;
                sir[1] = (p & 0x00ff00) >> 8;
                sir[2] = (p & 0x0000ff);
                rbs = r1 - Math.abs(i);
                rsum += sir[0] * rbs;
                gsum += sir[1] * rbs;
                bsum += sir[2] * rbs;
                if (i > 0) {
                    rinsum += sir[0];
                    ginsum += sir[1];
                    binsum += sir[2];
                } else {
                    routsum += sir[0];
                    goutsum += sir[1];
                    boutsum += sir[2];
                }
            }
            stackpointer = radius;

            for (x = 0; x < w; x++) {

                r[yi] = dv[rsum];
                g[yi] = dv[gsum];
                b[yi] = dv[bsum];

                rsum -= routsum;
                gsum -= goutsum;
                bsum -= boutsum;

                stackstart = stackpointer - radius + div;
                sir = stack[stackstart % div];

                routsum -= sir[0];
                goutsum -= sir[1];
                boutsum -= sir[2];

                if (y == 0) {
                    vmin[x] = Math.min(x + radius + 1, wm);
                }
                p = pix[yw + vmin[x]];

                sir[0] = (p & 0xff0000) >> 16;
                sir[1] = (p & 0x00ff00) >> 8;
                sir[2] = (p & 0x0000ff);

                rinsum += sir[0];
                ginsum += sir[1];
                binsum += sir[2];

                rsum += rinsum;
                gsum += ginsum;
                bsum += binsum;

                stackpointer = (stackpointer + 1) % div;
                sir = stack[(stackpointer) % div];

                routsum += sir[0];
                goutsum += sir[1];
                boutsum += sir[2];

                rinsum -= sir[0];
                ginsum -= sir[1];
                binsum -= sir[2];

                yi++;
            }
            yw += w;
        }
        for (x = 0; x < w; x++) {
            rinsum = ginsum = binsum = routsum = goutsum = boutsum = rsum = gsum = bsum = 0;
            yp = -radius * w;
            for (i = -radius; i <= radius; i++) {
                yi = Math.max(0, yp) + x;

                sir = stack[i + radius];

                sir[0] = r[yi];
                sir[1] = g[yi];
                sir[2] = b[yi];

                rbs = r1 - Math.abs(i);

                rsum += r[yi] * rbs;
                gsum += g[yi] * rbs;
                bsum += b[yi] * rbs;

                if (i > 0) {
                    rinsum += sir[0];
                    ginsum += sir[1];
                    binsum += sir[2];
                } else {
                    routsum += sir[0];
                    goutsum += sir[1];
                    boutsum += sir[2];
                }

                if (i < hm) {
                    yp += w;
                }
            }
            yi = x;
            stackpointer = radius;
            for (y = 0; y < h; y++) {
                // Preserve alpha channel: ( 0xff000000 & pix[yi] )
                pix[yi] = (0xff000000 & pix[yi]) | (dv[rsum] << 16)
                        | (dv[gsum] << 8) | dv[bsum];

                rsum -= routsum;
                gsum -= goutsum;
                bsum -= boutsum;

                stackstart = stackpointer - radius + div;
                sir = stack[stackstart % div];

                routsum -= sir[0];
                goutsum -= sir[1];
                boutsum -= sir[2];

                if (x == 0) {
                    vmin[y] = Math.min(y + r1, hm) * w;
                }
                p = x + vmin[y];

                sir[0] = r[p];
                sir[1] = g[p];
                sir[2] = b[p];

                rinsum += sir[0];
                ginsum += sir[1];
                binsum += sir[2];

                rsum += rinsum;
                gsum += ginsum;
                bsum += binsum;

                stackpointer = (stackpointer + 1) % div;
                sir = stack[stackpointer];

                routsum += sir[0];
                goutsum += sir[1];
                boutsum += sir[2];

                rinsum -= sir[0];
                ginsum -= sir[1];
                binsum -= sir[2];

                yi += w;
            }
        }

        bitmap.setPixels(pix, 0, w, 0, 0, w, h);
        bitmap.setHasAlpha(true);

        return (bitmap);
    }

    /**
     * blurImageAmeliorate:模糊效果
     * http://blog.csdn.net/sjf0115/article/details/7266998
     *
     * @param bmp
     * @return
     */
    public static Bitmap blurImageAmeliorate(Bitmap bmp) {
        long start = System.currentTimeMillis();
        // 高斯矩阵
        int[] gauss = new int[]{1, 2, 1, 2, 4, 2, 1, 2, 1};

        int width = bmp.getWidth();
        int height = bmp.getHeight();
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);

        int pixR;
        int pixG;
        int pixB;

        int pixColor;

        int newR = 0;
        int newG = 0;
        int newB = 0;

        int delta = 35; // 值越小图片会越亮，越大则越暗

        int idx = 0;
        int[] pixels = new int[width * height];
        bmp.getPixels(pixels, 0, width, 0, 0, width, height);
        for (int i = 1, length = height - 1; i < length; i++) {
            for (int k = 1, len = width - 1; k < len; k++) {
                idx = 0;
                for (int m = -1; m <= 1; m++) {
                    for (int n = -1; n <= 1; n++) {
                        pixColor = pixels[(i + m) * width + k + n];
                        pixR = Color.red(pixColor);
                        pixG = Color.green(pixColor);
                        pixB = Color.blue(pixColor);

                        newR = newR + pixR * gauss[idx];
                        newG = newG + pixG * gauss[idx];
                        newB = newB + pixB * gauss[idx];
                        idx++;
                    }
                }

                newR /= delta;
                newG /= delta;
                newB /= delta;

                newR = Math.min(255, Math.max(0, newR));
                newG = Math.min(255, Math.max(0, newG));
                newB = Math.min(255, Math.max(0, newB));

                pixels[i * width + k] = Color.argb(255, newR, newG, newB);

                newR = 0;
                newG = 0;
                newB = 0;
            }
        }

        bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
        return bitmap;
    }

    public static void cropImageUri(Activity activity, Uri uri, int aspectX, int aspectY, int outputX, int outputY, int
            requestCode) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", aspectX);
        intent.putExtra("aspectY", aspectY);
        intent.putExtra("outputX", outputX);
        intent.putExtra("outputY", outputY);
        intent.putExtra("scale", false);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        intent.putExtra("return-data", false);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("noFaceDetection", true); // no face detection
        activity.startActivityForResult(intent, requestCode);
    }

    public static void cropImageUri(Activity activity, Uri uri, Uri output, int aspectX, int aspectY, int outputX,
                                    int outputY, int requestCode) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", aspectX);
        intent.putExtra("aspectY", aspectY);
        intent.putExtra("outputX", outputX);
        intent.putExtra("outputY", outputY);
        intent.putExtra("scale", false);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, output);
        intent.putExtra("return-data", false);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("noFaceDetection", true); // no face detection
        activity.startActivityForResult(intent, requestCode);
    }

    public static void compressImage(Uri imgUri, String imgCachePath, Boolean isJPEG) {
        try {

//
//            int maxKBNum = 120;
//
//
//            int options = 100;
//            ByteArrayOutputStream output = new ByteArrayOutputStream();
//            // Store the bitmap into output stream(no compress)
//            bitmap.compress(Bitmap.CompressFormat.JPEG, options, output);
//            // Compress by loop
//            while (output.toByteArray().length / 1024 > maxKBNum && options > 30) {
//                // Clean up os
//                output.reset();
//                // interval 10
//                options -= 5;
//                bitmap.compress(Bitmap.CompressFormat.JPEG, options, output);
//            }


//            // 首先进行一次大范围的压缩
//
//            ByteArrayOutputStream output = new ByteArrayOutputStream();
//            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, output);
//            float zoom = (float)Math.sqrt(maxKBNum * 1024 / (float)output.toByteArray().length); //获取缩放比例
//
//            // 设置矩阵数据
//            Matrix matrix = new Matrix();
//            matrix.setScale(zoom, zoom);
//
//            // 根据矩阵数据进行新bitmap的创建
//            Bitmap resultBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, false);
//            output.reset();
//            resultBitmap.compress(Bitmap.CompressFormat.JPEG, 100, output);
//
//            // 如果进行了上面的压缩后，依旧大于32K，就进行小范围的微调压缩
//            while(output.toByteArray().length > maxKBNum * 1024){
//                matrix.setScale(0.9f, 0.9f);//每次缩小 1/10
//
//                resultBitmap = Bitmap.createBitmap(
//                        resultBitmap, 0, 0,
//                        resultBitmap.getWidth(), resultBitmap.getHeight(), matrix, false);
//
//                output.reset();
//                resultBitmap.compress(Bitmap.CompressFormat.JPEG, 100, output);
//            }

//            FileOutputStream fos = new FileOutputStream(imgCachePath);
//            fos.write(os.toByteArray());
//            fos.flush();
//            fos.close();

//            FileOutputStream fos = new FileOutputStream(imgCachePath);
//            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);



            InputStream is = BaseApp.getInstance().getContentResolver().openInputStream(imgUri);
            Bitmap bitmap = BitmapFactory.decodeStream(is);

            Bitmap.CompressFormat format = isJPEG ? Bitmap.CompressFormat.JPEG : Bitmap.CompressFormat.PNG;
            int maxKBNum = 120;

            int options = 100;
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            // Store the bitmap into output stream(no compress)
            bitmap.compress(format, options, output);



            float maxWidth = 1080;

            float zoom = maxWidth / bitmap.getWidth() ; //获取缩放比例
            if (zoom > 1) {
                //说明截取后的图比较小，不需要压缩.
                zoom = 1;
            }

            // 设置矩阵数据
            Matrix matrix = new Matrix();
            matrix.setScale(zoom, zoom);

            // 根据矩阵数据进行新bitmap的创建
            bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);

            // Compress by loop
            while (output.toByteArray().length / 1024 > maxKBNum && options > 22) {
                // Clean up os
                output.reset();
                // interval 10
                options -= 6;
                bitmap.compress(format, options, output);
            }

            FileOutputStream fos = new FileOutputStream(imgCachePath);
            fos.write(output.toByteArray());
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
