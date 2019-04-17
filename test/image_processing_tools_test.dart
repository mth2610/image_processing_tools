import 'package:flutter/services.dart';
import 'package:flutter_test/flutter_test.dart';
import 'package:image_processing_tools/image_processing_tools.dart';

void main() {
  const MethodChannel channel = MethodChannel('image_processing_tools');

  setUp(() {
    channel.setMockMethodCallHandler((MethodCall methodCall) async {
      return '42';
    });
  });

  tearDown(() {
    channel.setMockMethodCallHandler(null);
  });

  test('getPlatformVersion', () async {
    expect(await ImageProcessingTools.platformVersion, '42');
  });
}
