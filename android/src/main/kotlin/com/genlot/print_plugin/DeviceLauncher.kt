package com.genlot.print_plugin

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.szzt.sdk.device.Device
import com.szzt.sdk.device.DeviceManager
import com.szzt.sdk.device.barcode.CameraScan
import com.szzt.sdk.device.printer.Printer

/**
 * 打印机管理
 * Created by LeiGuangwu on 2022/3/2.
 */
class DeviceLauncher(var applicationContext: Context) {

    init {
        initDeviceManager()
    }

    //证通设备管理器
    private lateinit var mDeviceManager: DeviceManager
    private var isConnect = false

    /**
     * 初始化设备管理器
     */
    private fun initDeviceManager() {
        Log.d("Plugin", "initDeviceManage()")
        mDeviceManager = DeviceManager.createInstance(applicationContext)
        mDeviceManager.start(deviceManagerListener)
        mDeviceManager.systemManager
    }

    /**
     * 获取打印机
     */
    fun getPrinter(): Printer? {
        val printers = mDeviceManager.getDeviceByType(Device.TYPE_PRINTER)
        return if (printers != null) {
            printers[0] as Printer
        } else null
    }

    /**
     * 获取扫码器
     */
    fun getCameraScan(): CameraScan? {
        val barcode = mDeviceManager.getDeviceByType(Device.TYPE_CAMERA_SCAN)
        return if (barcode != null) barcode[0] as CameraScan else null
    }

    /**
     * 设备监听
     */
    private var deviceManagerListener: DeviceManager.DeviceManagerListener = object :
        DeviceManager.DeviceManagerListener {
        override fun serviceEventNotify(event: Int): Int {
            when (event) {
                DeviceManager.DeviceManagerListener.EVENT_SERVICE_CONNECTED -> {
                    isConnect = true
                    //mLocalBroadcastManager.sendBroadcast(mServiceConnectedIntent);
                }
                DeviceManager.DeviceManagerListener.EVENT_SERVICE_VERSION_NOT_COMPATABLE -> {
                    Toast.makeText(
                        applicationContext,
                        "SDK Version is not compatable!!!", Toast.LENGTH_SHORT
                    ).show()
                }
                DeviceManager.DeviceManagerListener.EVENT_SERVICE_DISCONNECTED -> {
                    isConnect = false
                    mDeviceManager.start(this)
                }
            }
            return 0
        }

        override fun deviceEventNotify(device: Device, event: Int): Int {
            return 0
        }
    }
}