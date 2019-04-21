import 'dart:async';
import 'dart:io';

import 'package:flutter/services.dart';

class ImageProcessingTools {
  static const MethodChannel _channel =
      const MethodChannel('image_processing_tools');

  static Future<String> get platformVersion async {
    final String version = await _channel.invokeMethod('getPlatformVersion');
    return version;
  }

  static Future<String> detectCircle(String inputFilePath, String outputFilePath, minCircleRadius) async {
    try{
      final String processedFile = await _channel.invokeMethod(
        'detectCircle',
        {
          'inputFilePath': inputFilePath,
          'outputFilePath': outputFilePath,
          'minCircleRadius': minCircleRadius,
        }
      );
      print(processedFile);
      return processedFile;
    } catch (e) {
      print(e);
      return null;
    }
  }

  static Future<String> toCartoon(String inputFilePath, String outputFilePath) async {
    try{
      final String processedFile = await _channel.invokeMethod(
        'toCartoon',
        {
          'inputFilePath': inputFilePath,
          'outputFilePath': outputFilePath,
        }
      );
      print(processedFile);
      return processedFile;
    } catch (e) {
      print(e);
      return null;
    }
  }

  static Future<String> toPencilSketch(String inputFilePath, String outputFilePath, bool isGrey) async {
    try{
      final String processedFile = await _channel.invokeMethod(
        'toPencilSketch',
        {
          'inputFilePath': inputFilePath,
          'outputFilePath': outputFilePath,
          'isGrey': isGrey,
        }
      );
      print(processedFile);
      return processedFile;
    } catch (e) {
      print(e);
      return null;
    }
  }

static Future<String> toOilPaiting(String inputFilePath, String outputFilePath) async {
    try{
      final String processedFile = await _channel.invokeMethod(
        'toOilPaiting',
        {
          'inputFilePath': inputFilePath,
          'outputFilePath': outputFilePath,
        }
      );
      print(processedFile);
      return processedFile;
    } catch (e) {
      print(e);
      return null;
    }
  }
}
