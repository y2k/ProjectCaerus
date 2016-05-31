package com.caerus

import android.os.Bundle
import com.example.helloworld.MainActivity

/**
 * Created by y2k on 5/31/16.
 */

fun MainActivity.invoke(methodName: String, argumentTypes: Array<Class<Bundle>>, vararg args: Any?) {
    val method = javaClass.getDeclaredMethod(methodName, *argumentTypes)
    method.isAccessible = true
    method.invoke(this, *args)
}