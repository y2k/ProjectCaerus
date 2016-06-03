package android.app

import android.content.Intent
import android.content.pm.ActivityInfo
import android.content.pm.ApplicationInfo
import kotlin.concurrent.thread

/**
 * Created by y2k on 5/31/16.
 */

fun preInit(callback: () -> Unit) {
    thread {
        Thread.sleep(500)
        ActivityThread.sMainThreadHandler.post(callback)
    }
    ActivityThread.main(emptyArray())
}

fun Activity.attach() {
    ContextImpl()
    val pckInfo = LoadedApk(
        null,
        ApplicationInfo().apply {
            // TODO
        },
        null,
        javaClass.classLoader,
        false,
        false)

    val context = ContextImpl()
    context.init(
        pckInfo,
        null,
        ActivityThread.currentActivityThread())

    attach(
        context,
        null,
        null,
        null,
        null,
        Intent(context, javaClass),
        ActivityInfo(),
        null,
        null,
        null,
        null,
        null)
}