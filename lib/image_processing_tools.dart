import 'dart:async';

import 'package:flutter/services.dart';

class ImageProcessingTools {
  static const MethodChannel _channel =
      const MethodChannel('image_processing_tools');

  static Future<String> get platformVersion async {
    final String version = await _channel.invokeMethod('getPlatformVersion');
    return version;
  }
}
