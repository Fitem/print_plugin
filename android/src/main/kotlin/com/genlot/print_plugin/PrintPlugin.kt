package com.genlot.print_plugin

import android.content.Context
import android.os.RemoteException
import android.text.format.DateUtils
import android.util.Log
import android.widget.Toast
import androidx.annotation.NonNull
import com.szzt.sdk.device.Device
import com.szzt.sdk.device.DeviceManager
import com.szzt.sdk.device.DeviceManager.DeviceManagerListener
import com.szzt.sdk.device.aidl.IPrinterListener
import com.szzt.sdk.device.printer.Printer

import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.MethodCallHandler
import io.flutter.plugin.common.MethodChannel.Result
import java.text.SimpleDateFormat
import java.time.chrono.HijrahChronology.INSTANCE
import java.time.chrono.HijrahDate
import java.util.*

/**
 * PrintPlugin
 */
class PrintPlugin : FlutterPlugin{
    /// The MethodChannel that will the communication between Flutter and native Android
    ///
    /// This local reference serves to register the plugin with the Flutter Engine and unregister it
    /// when the Flutter Engine is detached from the Activity
    private lateinit var printLauncher: PrintLauncher
    private lateinit var handler: MethodCallHandlerImpl

    override fun onAttachedToEngine(@NonNull flutterPluginBinding: FlutterPlugin.FlutterPluginBinding) {
        printLauncher = PrintLauncher(flutterPluginBinding.applicationContext)
        handler = MethodCallHandlerImpl(printLauncher)
        handler.startService(flutterPluginBinding.binaryMessenger)

    }

    override fun onDetachedFromEngine(@NonNull binding: FlutterPlugin.FlutterPluginBinding) {
        handler.stopService()
    }
}
