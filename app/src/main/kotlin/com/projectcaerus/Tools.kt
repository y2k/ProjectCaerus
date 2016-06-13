package com.projectcaerus

import android.content.res.ResourceParser
import android.content.res.Resources
import android.graphics.drawable.Drawable
import android.graphics.drawable.StateListDrawable

/**
 * Created by y2k on 13/06/16.
 */

fun createStateListDrawable(resources: Resources, parser: ResourceParser): Drawable {
    return StateListDrawable.createFromXml(resources, parser)
}