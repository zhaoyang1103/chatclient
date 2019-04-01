package com.example.chartclient.zy_java.util;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by 昭阳 on 2019/4/1.
 */
public class Image_Util {
    private static Context context;

    public static void saveBmp2Gallery(Context mContext, Bitmap bmp, String picName) {
        context = mContext;
        String fileName = null;
        //系统相册目录
        String galleryPath = Environment.getExternalStorageDirectory()
                + File.separator + Environment.DIRECTORY_DCIM
                + File.separator + "Camera" + File.separator;


        // 声明文件对象
        File file = null;
        // 声明输出流
        FileOutputStream outStream = null;

        try {
            // 如果有目标文件，直接获得文件对象，否则创建一个以filename为名称的文件
            file = new File(galleryPath, picName + ".jpg");

            // 获得文件相对路径
            fileName = file.toString();
            // 获得输出流，如果文件中有内容，追加内容
            outStream = new FileOutputStream(fileName);
            if (null != outStream) {
                bmp.compress( Bitmap.CompressFormat.PNG, 90, outStream);
            }

        } catch (Exception e) {
            e.getStackTrace();
        } finally {
            try {
                if (outStream != null) {
                    outStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }


            MediaStore.Images.Media.insertImage(mContext.getContentResolver(),
                    bmp, fileName, null);
            Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            Uri uri = Uri.fromFile(file);
            intent.setData(uri);
            mContext.sendBroadcast(intent);
            Toast.makeText(mContext, "图片保存至"+galleryPath, Toast.LENGTH_SHORT).show();

        }
    }
}