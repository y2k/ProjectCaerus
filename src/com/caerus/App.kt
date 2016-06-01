package com.caerus

import android.app.Application
import android.app.attach
import android.app.preInit
import android.os.Bundle
import android.os.Looper
import com.example.helloworld.MainActivity

/**
 * Created by y2k on 5/31/16.
 */
fun main(args: Array<String>) {
    println("Started...")
    startActivity()
    println("Finished success")
}

private fun startActivity() {
//    Looper.prepareMainLooper()
//
//    val app = Application()
//    app.onCreate()

    preInit()

    val activity = MainActivity()
    activity.attach()
    activity.invoke("onCreate", arrayOf(Bundle::class.java), null)
}