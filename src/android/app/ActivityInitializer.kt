package android.app

import android.content.Intent
import android.content.pm.ActivityInfo

/**
 * Created by y2k on 5/31/16.
 */

fun Activity.attach() {
    val context = ContextImpl()
    this.attach(
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