package com.genlot.print_plugin

import android.os.Bundle
import android.os.RemoteException
import com.szzt.sdk.device.aidl.IPrinterListener
import com.szzt.sdk.device.barcode.CameraScan
import com.szzt.sdk.device.barcode.CameraScan.CameraListener
import com.szzt.sdk.device.printer.Printer
import io.flutter.plugin.common.BinaryMessenger
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import java.text.SimpleDateFormat
import java.util.*

/**
 * 插件方法监听
 * Created by LeiGuangwu on 2022/3/2.
 */
class MethodCallHandlerImpl(var deviceLauncher: DeviceLauncher) : MethodChannel.MethodCallHandler,
    MethodCallHandlerListener {

    private lateinit var channel: MethodChannel

    override fun onMethodCall(call: MethodCall, result: MethodChannel.Result) {
        when (call.method) {
            "getPlatformVersion" -> {
                result.success("Android ${android.os.Build.VERSION.RELEASE}")
            }
            "printTicket" -> {
                printTicket(call, result)
            }
            "scanner" -> {
                scanner(call, result)
            }
            else -> {
                result.notImplemented()
            }
        }
    }

    override fun startService(binaryMessenger: BinaryMessenger) {
        channel = MethodChannel(binaryMessenger, "print_plugin")
        channel.setMethodCallHandler(this)
    }


    override fun stopService() {
        channel.setMethodCallHandler(null)
    }

    /**
     * 扫码
     */
    private fun scanner(call: MethodCall, result: MethodChannel.Result) {
        try {
            val scan = deviceLauncher.getCameraScan()
            val bundle = Bundle()
            bundle.putInt(CameraScan.BARCODE_CAMERA_TYPE, CameraScan.CarmeraType.TYPE_BACK_FACING)
            bundle.putBoolean(CameraScan.BARCODE_BEEP, true)
            scan?.setConfig(bundle)
            scan?.scan(30000, CameraListener { i, data ->
                printCash(call, result)
            })
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * 打印票
     */
    private fun printTicket(call: MethodCall, result: MethodChannel.Result) {
        var betNumber: String = call.argument("betNumber") ?: "A123456"
        betNumber = if (betNumber.length < 7) "A123456" else betNumber
        val printer = deviceLauncher.getPrinter()
        printer?.open()

        // SINGAPORE
//        printer?.addStr("SINGAPORE\n", Printer.Font.FONT_2, false, Printer.Align.CENTER)
        // SWEEP
        printer?.addStr("SWEEP\n", Printer.Font.FONT_3, false, Printer.Align.CENTER)

        printer?.addStr("\n", Printer.Font.FONT_2, false, Printer.Align.LEFT)

        // A.123456   $3   QP
        printer?.addStr(
            betNumber.substring(0, 1) + "." + betNumber.substring(1) + "\n",
            Printer.Font.FONT_3,
            false,
            Printer.Align.LEFT
        )

        printer?.addStr("\n", Printer.Font.FONT_2, false, Printer.Align.LEFT)

        // PRICE:$3.00
        printer?.addStr("PRICE:$3.00", Printer.Font.FONT_3, false, Printer.Align.LEFT)
        // DRAW:
        printer?.addStr(
            "DRAW:${
                SimpleDateFormat(
                    "E dd/MM/yyyy    1000/18\n",
                    Locale.US
                ).format(Date(System.currentTimeMillis() + 24 * 60 * 60 * 1000))
            }",
            Printer.Font.FONT_2,
            false,
            Printer.Align.LEFT
        )
        printer?.addStr(
            "${SimpleDateFormat("E dd/MM/yyyy", Locale.US).format(Date())}     00888802-51\n",
            Printer.Font.FONT_2,
            false,
            Printer.Align.LEFT
        )
        printer?.addStr(
            "733-39700993-118208    02-6163\n",
            Printer.Font.FONT_2,
            false,
            Printer.Align.LEFT
        )

        printer?.addQrCode("1231231231231")
        printer?.addStr("\n\n\n", Printer.Font.FONT_2, false, Printer.Align.LEFT)
        printer?.start(object : IPrinterListener.Stub() {
            @Throws(RemoteException::class)
            override fun PrinterNotify(retCode: Int) {
            }
        })

        printer?.close()
        result.success(true)
    }

    /**
     * 打印兑奖单
     */
    private fun printCash(call: MethodCall, result: MethodChannel.Result) {
        val printer = deviceLauncher.getPrinter()
        printer?.open()

        // SINGAPORE
//        printer?.addStr("SINGAPORE\n", Printer.Font.FONT_2, false, Printer.Align.CENTER)
        // SWEEP
        printer?.addStr("==Cash Voucher==\n", Printer.Font.FONT_3, false, Printer.Align.CENTER)

        printer?.addStr("\n", Printer.Font.FONT_2, false, Printer.Align.LEFT)

        // Amount
        printer?.addStr("Amount: 100.00\n", Printer.Font.FONT_2, false, Printer.Align.CENTER)

        printer?.addStr("\n", Printer.Font.FONT_2, false, Printer.Align.LEFT)

        // Issue
        printer?.addStr("Issue: 02-6163\n", Printer.Font.FONT_2, false, Printer.Align.LEFT)
        printer?.addStr("Sale StationID: 1001001\n", Printer.Font.FONT_2, false, Printer.Align.LEFT)
        printer?.addStr("Cash StationID: 1001001\n", Printer.Font.FONT_2, false, Printer.Align.LEFT)
        printer?.addStr(
            "Cash Time: ${
                SimpleDateFormat(
                    "E dd/MM/yyyy\n",
                    Locale.US
                ).format(Date())
            }\n", Printer.Font.FONT_2, false, Printer.Align.LEFT
        )

        printer?.addStr("\n\n", Printer.Font.FONT_2, false, Printer.Align.LEFT)
        printer?.start(object : IPrinterListener.Stub() {
            @Throws(RemoteException::class)
            override fun PrinterNotify(retCode: Int) {
            }
        })

        printer?.close()
        result.success(true)
    }
}