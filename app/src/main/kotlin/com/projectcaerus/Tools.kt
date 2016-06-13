package com.projectcaerus

import android.content.res.ResourceParser
import android.content.res.Resources
import android.graphics.drawable.Drawable
import android.graphics.drawable.StateListDrawable
import kotlin.reflect.KClass

/**
 * Created by y2k on 13/06/16.
 */

fun createStateListDrawable(resources: Resources, parser: ResourceParser): Drawable {
    return StateListDrawable.createFromXml(resources, parser)
}

fun Any.invoke(methodName: String, argsTypes: List<KClass<*>>, vararg args: Any?) {
    val method = javaClass.getDeclaredMethod(methodName, *(argsTypes.map { it.java }.toTypedArray<Class<*>>()))
    method.isAccessible = true
    method.invoke(this, *args)
}

data class Size(val width: Int, val height: Int)