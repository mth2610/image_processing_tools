package com.mth2610.image_processing_tools;

import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;
import io.flutter.plugin.common.PluginRegistry.Registrar;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.photo.Photo;
import org.opencv.core.TermCriteria;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.core.CvType;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import android.util.Log;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/** ImageProcessingToolsPlugin */
public class ImageProcessingToolsPlugin implements MethodCallHandler {
  static {
    System.loadLibrary("opencv_java4");
  }
  /** Plugin registration. */
  public static void registerWith(Registrar registrar) {
    final MethodChannel channel = new MethodChannel(registrar.messenger(), "image_processing_tools");
    channel.setMethodCallHandler(new ImageProcessingToolsPlugin());
  }

  @Override
  public void onMethodCall(MethodCall call, Result result) {
    if (call.method.equals("getPlatformVersion")) {
      result.success("Android " + android.os.Build.VERSION.RELEASE);
    }  else if (call.method.equals("detectCircle")){
      String inputFilePath = call.argument("inputFilePath");
      String outputFilePath = call.argument("outputFilePath");
      int minCircleRadius = call.argument("minCircleRadius");
      //detectCircle(inputFilePath, outputFilePath,minCircleRadius, result);
      //convertToGrey(inputFilePath, outputFilePath, result);
      //toOilPaiting(inputFilePath, outputFilePath, result);
      //toCartoon(inputFilePath, outputFilePath, result);
      toStylization(inputFilePath, outputFilePath, result);
    } else if (call.method.equals("toOilPaiting")) {
      String inputFilePath = call.argument("inputFilePath");
      String outputFilePath = call.argument("outputFilePath");
      toOilPaiting(inputFilePath, outputFilePath, result);
    } else if (call.method.equals("toCartoon")){
      String inputFilePath = call.argument("inputFilePath");
      String outputFilePath = call.argument("outputFilePath");
      toCartoon(inputFilePath, outputFilePath, result);
    } else if (call.method.equals("toPencilSketch")){
      String inputFilePath = call.argument("inputFilePath");
      String outputFilePath = call.argument("outputFilePath");
      boolean isGrey = call.argument("isGrey");
      toPencilSketch(inputFilePath, outputFilePath, isGrey, result);
    }
    else {
      result.notImplemented();
    }
  }

  private void detectCircle(final String inputPath, final String outputPath, final int minCircleRadius, final Result result){

    new Thread(new Runnable() {
      Mat img;
      Mat coverted32FMat = new Mat();
      File outputFile = null;
      public void run() {
        try {
          img = Imgcodecs.imread(inputPath, Imgcodecs.IMREAD_COLOR);
//          img.convertTo(coverted32FMat, 5);
          Mat grayImage = new Mat();
          Imgproc.cvtColor(img, grayImage, Imgproc.COLOR_BGR2GRAY);
          Imgproc.medianBlur(grayImage, grayImage, 5);
          Mat circles = new Mat();
          Imgproc.HoughCircles(grayImage, circles, Imgproc.HOUGH_GRADIENT, 1.0,
                    (double)grayImage.rows()/16, // change this value to detect circles with different distances to each other
                    200.0, 100.0, 100,  0);

          Log.i("number_circles", String.valueOf(circles.cols()));


          for (int x = 0; x < circles.cols(); x++) {
              double[] c = circles.get(0, x);
              Point center = new Point(Math.round(c[0]), Math.round(c[1]));
              // circle center100
              Imgproc.circle(img, center, 1, new Scalar(0,100,100), 3, 8, 0 );
              // circle outline
              int radius = (int) Math.round(c[2]);
              Imgproc.circle(img, center, radius, new Scalar(255,0,255), 3, 8, 0 );
          }

          Log.i("number_rows", String.valueOf(grayImage.rows()));
          Log.i("number_cols", String.valueOf(grayImage.cols()));

          String outputFileName = String.valueOf(System.currentTimeMillis()) + ".png";
          Imgcodecs.imwrite(outputPath+"/"+outputFileName, img);
          result.success(outputPath+"/"+outputFileName);

        } catch (Exception e) {
          result.error("error", e.getMessage(), null);
        }
      }}).start();
  }

  private void convertToGrey(final String inputPath, final String outputPath, final Result result){
    new Thread(new Runnable() {
      Mat img;
      File outputFile = null;
      public void run() {
        try {
          img = Imgcodecs.imread(inputPath, Imgcodecs.IMREAD_COLOR);
          Mat grayImage = new Mat();
          Imgproc.cvtColor(img, grayImage, Imgproc.COLOR_BGR2GRAY);
          String outputFileName = String.valueOf(System.currentTimeMillis()) + ".png";
          Imgcodecs.imwrite(outputPath+"/"+outputFileName, grayImage);
          result.success(outputPath+"/"+outputFileName);
        } catch (Exception e) {
          result.error("error", e.getMessage(), null);
        }
      }}).start();
  }

  private void toOilPaiting(final String inputPath, final String outputPath, final Result result){
    new Thread(new Runnable() {
      Mat img;
      File outputFile = null;

      public void run() {
        try {
          img = Imgcodecs.imread(inputPath, Imgcodecs.IMREAD_COLOR);

          Log.i("input rows", String.valueOf(img.rows()));
          Log.i("input cols", String.valueOf(img.cols()));
          Log.i("input chanels", String.valueOf(img.channels()));

          Mat grayImage = new Mat();
          Mat dxField = new Mat ();
          Mat dyField = new Mat ();
//          Mat magnitudeImage = new Mat();

          int strokeScale = 3*(int)Math.ceil(Math.max(img.rows(), img.cols())/1000.0);
          int gradientSmoothingRadius = Math.max(img.rows(), img.cols())/50;

          // GaussianBlur parameter
          int s = gradientSmoothingRadius*2 + 1;
          Size gauSize = new Size(s,s);
          Imgproc.cvtColor(img, grayImage, Imgproc.COLOR_BGR2GRAY);

          Imgproc.Scharr(grayImage, dxField, CvType.CV_32F, 1, 0);
          Core.divide(dxField, Scalar.all((float)15.36), dxField);
          Imgproc.GaussianBlur(dxField, dxField, gauSize, 0);

          Imgproc.Scharr(grayImage, dyField, CvType.CV_32F, 0, 1);
          Core.divide(dyField, Scalar.all((float)15.36), dyField);
          Imgproc.GaussianBlur(dyField, dyField, gauSize, 0);

//          Core.add(dxField.mul(dxField),dyField.mul(dyField), magnitudeImage);
//          Core.sqrt(magnitudeImage, magnitudeImage);

//          Mat labels = new Mat();
//          TermCriteria criteria = new TermCriteria(TermCriteria.COUNT, 100, 1);
//          Mat centers = new Mat();
//          Mat img32f = img;
////          img32f.reshape(1, img.cols() * img.rows());
//          img32f.convertTo(img32f, CvType.CV_32F, 1.0 / 255.0);
//          Core.kmeans(img32f, 20, labels, criteria, 1, Core.KMEANS_PP_CENTERS, centers);

          List randomXList = new ArrayList();;
          List randomYList = new ArrayList();;
          Mat res = new Mat();
          img.convertTo(res, CvType.CV_8U);
          Imgproc.medianBlur(res, res, 21);

          int scale = 4;

            for(int rc=0; rc < img.rows()-scale; rc += scale){
              for(int cc=0; cc < img.cols()-scale; cc += scale){

                int randomX = (int) Math.round((Math.random()*2-1)*scale/2 + rc);
                int randomY = (int) Math.round((Math.random()*2-1)*scale/2 + cc);

//                randomXList.add(randomX);
//                randomYList.add(randomY);

                Scalar color = new Scalar(img.get(randomX, randomY)[0], img.get(randomX, randomY)[1], img.get(randomX, randomY)[2]);
                double angle = Math.toDegrees(Math.atan2(dyField.get(randomX, randomY)[0], dxField.get(randomX, randomY)[0]))+90;

                double magnitude = Math.hypot(dxField.get(randomX, randomY)[0], dxField.get(randomX, randomY)[0]);
                int length = (int) Math.round(strokeScale + strokeScale *Math.sqrt(magnitude));
                Log.i("strokeScale", String.valueOf(strokeScale));
                Log.i("length", String.valueOf(length));
                Log.i("angle", String.valueOf(angle));
                Log.i("magnitude", String.valueOf(magnitude));

                Imgproc.ellipse(res, new Point(randomY, randomX), new Size (length, strokeScale), angle, 0, 360, color, -1, Imgproc.LINE_AA);
              }
            }


//          Log.i("kmeans", String.valueOf(labels.rows()));
//          Log.i("kmeans", String.valueOf(labels.cols()));
//          Log.i("kmeans", String.valueOf(labels.channels()));
//
//          Log.i("kmeans", String.valueOf(centers.rows()));
//          Log.i("kmeans", String.valueOf(centers.cols()));
//          Log.i("kmeans", String.valueOf(centers.channels()));

          String outputFileName = String.valueOf(System.currentTimeMillis()) + ".png";
          Imgcodecs.imwrite(outputPath+"/"+outputFileName, res);
          result.success(outputPath+"/"+outputFileName);
        } catch (Exception e) {
          result.error("error", e.getMessage(), null);
        }
      }}).start();
  }

  // http://www.askaswiss.com/2016/01/how-to-create-cartoon-effect-opencv-python.html
  private void toCartoon(final String inputPath, final String outputPath, final Result result){
    new Thread(new Runnable() {
      Mat imgColor = new Mat();
      Mat img_rgb = new Mat();
      Mat imgBilateralFilter = new Mat();
      File outputFile = null;
      public void run() {
        try {
          int numDown = 2;       // number of downsampling steps
          int numBilateral = 5;  // number of bilateral filtering steps

          imgColor = Imgcodecs.imread(inputPath, Imgcodecs.IMREAD_UNCHANGED);
          imgColor.convertTo(imgColor, 0);
          img_rgb = imgColor;

          Mat grayImage = new Mat();
          Imgproc.cvtColor(img_rgb, grayImage, Imgproc.COLOR_BGR2GRAY);

          for(int i = 0; i<= numDown; i++){
            Imgproc.pyrDown(imgColor, imgColor);
          }

          Imgproc.bilateralFilter(imgColor, imgBilateralFilter, 9, 9 ,7);

          for(int i = 0; i<= numBilateral-1; i++){
            Imgproc.bilateralFilter(imgBilateralFilter, imgColor, 9, 9 ,7);
            Imgproc.bilateralFilter(imgColor, imgBilateralFilter, 9, 9 ,7);
          }

          for(int i = 0; i<= numDown; i++){
            Imgproc.pyrUp(imgColor, imgColor);
          }

          Mat imgBlur = new Mat();

          Imgproc.medianBlur(grayImage, imgBlur, 5);
          Mat imageEdge = new Mat();
          Imgproc.adaptiveThreshold(grayImage, imageEdge, 255, Imgproc.ADAPTIVE_THRESH_MEAN_C, Imgproc.THRESH_BINARY, 15, 5);
          Imgproc.cvtColor(imageEdge, imageEdge, Imgproc.COLOR_GRAY2RGB);
          // resize imgColor
          Imgproc.resize(imgColor, imgColor, imageEdge.size());
          Mat imgCartoon = new Mat();

          Core.bitwise_and(imgColor, imageEdge, imgCartoon);

          String outputFileName = String.valueOf(System.currentTimeMillis()) + ".png";
          Imgcodecs.imwrite(outputPath+"/"+outputFileName, imgCartoon);
          result.success(outputPath+"/"+outputFileName);
        } catch (Exception e) {
          result.error("error", e.getMessage(), null);
        }
      }}).start();
  }

  // http://www.askaswiss.com/2016/01/how-to-create-cartoon-effect-opencv-python.html
  private void toStylization(final String inputPath, final String outputPath, final Result result){
    new Thread(new Runnable() {
      Mat inputImage = new Mat();
      Mat outputImage = new Mat();
      File outputFile = null;
      public void run() {
        try {
          inputImage = Imgcodecs.imread(inputPath, Imgcodecs.IMREAD_UNCHANGED);
          Photo.stylization(inputImage, outputImage, 60, (float) 0.07);
          String outputFileName = String.valueOf(System.currentTimeMillis()) + ".png";
          Imgcodecs.imwrite(outputPath+"/"+outputFileName, outputImage);
          result.success(outputPath+"/"+outputFileName);
        } catch (Exception e) {
          result.error("error", e.getMessage(), null);
        }
      }}).start();
  }

  // http://www.askaswiss.com/2016/01/how-to-create-cartoon-effect-opencv-python.html
  private void toPencilSketch(final String inputPath, final String outputPath, final boolean isGrey, final Result result){
    new Thread(new Runnable() {
      Mat inputImage = new Mat();
      Mat outputImage = new Mat();
      Mat outputGreyImage = new Mat();
      public void run() {
        try {
          inputImage = Imgcodecs.imread(inputPath, Imgcodecs.IMREAD_UNCHANGED);
          Photo.pencilSketch(inputImage, outputGreyImage, outputImage);
          String outputFileName = String.valueOf(System.currentTimeMillis()) + ".png";

          if(isGrey){
            Imgcodecs.imwrite(outputPath+"/"+outputFileName, outputGreyImage);
          } else {
            Imgcodecs.imwrite(outputPath+"/"+outputFileName, outputImage);
          }

          result.success(outputPath+"/"+outputFileName);
        } catch (Exception e) {
          result.error("error", e.getMessage(), null);
        }
      }}).start();
  }

}
