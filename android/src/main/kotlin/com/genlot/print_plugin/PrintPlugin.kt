package com.genlot.print_plugin

import androidx.annotation.NonNull

import io.flutter.embedding.engine.plugins.FlutterPlugin

/**
 * PrintPlugin
 */
class PrintPlugin : FlutterPlugin{
    /// The MethodChannel that will the communication between Flutter and native Android
    ///
    /// This local reference serves to register the plugin with the Flutter Engine and unregister it
    /// when the Flutter Engine is detached from the Activity
    private lateinit var deviceLauncher: DeviceLauncher
    private lateinit var handler: MethodCallHandlerImpl

    override fun onAttachedToEngine(@NonNull flutterPluginBinding: FlutterPlugin.FlutterPluginBinding) {
        deviceLauncher = DeviceLauncher(flutterPluginBinding.applicationContext)
        handler = MethodCallHandlerImpl(deviceLauncher)
        handler.startService(flutterPluginBinding.binaryMessenger)
    }

    override fun onDetachedFromEngine(@NonNull binding: FlutterPlugin.FlutterPluginBinding) {
        handler.stopService()
    }
}
