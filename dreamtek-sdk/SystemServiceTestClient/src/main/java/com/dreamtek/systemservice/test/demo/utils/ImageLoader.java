package com.dreamtek.systemservice.test.demo.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import java.io.File;

public class ImageLoader {

    public Bitmap getBitmapFromSdCard(String fileName) {
        String sdCardPath = Environment.getExternalStorageDirectory().getPath() + "/Download/" + fileName;
        return getBitmapFromPath(sdCardPath);
    }

    private Bitmap getBitmapFromPath(String path) {
        if (path == null || path.isEmpty()) {
            return null;
        }

        Bitmap bitmap = BitmapFactory.decodeFile(path);
        return bitmap;
    }

    public Bitmap getBitmapFromUsb(String fileName) {
        File usbDrive = findUsbDrive();
        if (usbDrive != null) {
            String usbPath = usbDrive.getPath() + "/" + fileName;
            return getBitmapFromPath(usbPath);
        }
        return null;
    }

    private File findUsbDrive() {
        File[] externalDirs = getExternalStorageDirectories();
        for (File dir : externalDirs) {
            if (dir.isDirectory() && dir.getName().toLowerCase().contains("usb")) {
                return dir;
            }
        }
        return null; // 没有找到U盘
    }

    private File[] getExternalStorageDirectories() {
        File storageDir = new File("/storage");
        return storageDir.listFiles();
    }

}

