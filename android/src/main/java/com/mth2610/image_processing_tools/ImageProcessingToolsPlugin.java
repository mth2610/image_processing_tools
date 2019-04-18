package com.mth2610.image_processing_tools;

import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;
import io.flutter.plugin.common.PluginRegistry.Registrar;

import org.opencv.core.Mat;
//import org.opencv.core.MatOfInt;
//import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.android.Utils;

//import java.io.IOException;
import android.os.Environment;
import java.io.File;
import java.io.FileOutputStream;

import android.graphics.Bitmap;


/** ImageProcessingToolsPlugin */
public class ImageProcessingToolsPlugin implements MethodCallHandler {
  /** Plugin registration. */
  public static void registerWith(Registrar registrar) {
    final MethodChannel channel = new MethodChannel(registrar.messenger(), "image_processing_tools");
    channel.setMethodCallHandler(new ImageProcessingToolsPlugin());
  }

  @Override
  public void onMethodCall(MethodCall call, Result result) {
    if (call.method.equals("getPlatformVersion")) {
      result.success("Android " + android.os.Build.VERSION.RELEASE);
    }  else if (call.method.equals("convertToGray")){
      String filePath = call.argument("filePath");
      convertToGray(filePath, result);
    }
    else {
      result.notImplemented();
    }
  }

  private void convertToGray(final String path, final Result result){
    new Thread(new Runnable() {
      public void run() {
        Mat img;
        Mat grayImage= new Mat();
        File outputFile = null;
        try {
          img=Imgcodecs.imread(path);
          Imgproc.cvtColor(img, grayImage, Imgproc.COLOR_BayerBG2GRAY);

          Bitmap resultBitmap = Bitmap.createBitmap(img.cols(), img.rows(),Bitmap.Config.ARGB_8888);;
          Utils.matToBitmap(grayImage, resultBitmap);

          String root = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).toString()+ "/"+"processed_image";
          File myDir = new File(root);
          myDir.mkdirs();
          String fname = String.valueOf(System.currentTimeMillis()) + ".png";
          outputFile = new File(myDir, fname);
          if (outputFile.exists()) outputFile.delete();
          FileOutputStream out = new FileOutputStream(outputFile);
          resultBitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
          result.success(outputFile);
          out.flush();
          out.close();
        } catch (Exception e) {
          e.printStackTrace();
        }
      }}).start();
  }

}
