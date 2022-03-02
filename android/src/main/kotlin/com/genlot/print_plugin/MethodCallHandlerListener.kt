package com.genlot.print_plugin

import io.flutter.plugin.common.BinaryMessenger

/**
 * Created by LeiGuangwu on 2022/3/2.
 */
interface MethodCallHandlerListener {

    fun startService(binaryMessenger: BinaryMessenger)

    fun stopService()
}