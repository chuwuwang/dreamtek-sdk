// Iimage.baseSDKLibrary
package com.dreamtek.smartpos.deviceservice.aidl.utils;
import android.graphics.Bitmap;

// Declare any non-default types here with import statements

interface IImage {
      byte[] bitmap2Jbig(in Bitmap bitmap);
}
