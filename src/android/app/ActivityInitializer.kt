package android.app

import android.content.Intent
import android.content.pm.ActivityInfo
import com.android.server.am.ActivityManagerService

/**
 * Created by y2k on 5/31/16.
 */

fun preInit() {
//    ActivityManagerService.main(0)

    ActivityThread.main(emptyArray())

//    ActivityManagerService.getDefault()
    ActivityManagerService.self()
}

fun Activity.attach() {
    val systemContext = ContextImpl()
    val pckInfo = LoadedApk(
        null,
        null,
        systemContext,
        null)

    val context = ContextImpl()
    context.init(
        pckInfo,
        null,
        null)

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