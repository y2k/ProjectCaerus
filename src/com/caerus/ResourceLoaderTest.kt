package com.caerus

import android.util.TypedValue
import org.junit.Assert.*

/**
 * Created by y2k on 6/4/16.
 */
class ResourceLoaderTest {

    @org.junit.Test
    fun loadResourceValue() {
        ResourceLoader.loadResourceValue(17367128, TypedValue(), true)
    }
}