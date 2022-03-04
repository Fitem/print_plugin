import 'dart:async';

import 'package:flutter/services.dart';

class PrintPlugin {
  static const MethodChannel _channel = MethodChannel('print_plugin');

  static Future<String?> get platformVersion async {
    final String? version = await _channel.invokeMethod('getPlatformVersion');
    return version;
  }

  static Future<bool> printTicket(String betNumber) async {
    return await _channel.invokeMethod("printTicket", <String, Object>{
      'betNumber': betNumber,
    });
  }

  static Future<bool> scanner() async {
    return await _channel.invokeMethod("scanner");
  }
}
