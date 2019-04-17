#import "ImageProcessingToolsPlugin.h"
#import <image_processing_tools/image_processing_tools-Swift.h>

@implementation ImageProcessingToolsPlugin
+ (void)registerWithRegistrar:(NSObject<FlutterPluginRegistrar>*)registrar {
  [SwiftImageProcessingToolsPlugin registerWithRegistrar:registrar];
}
@end
