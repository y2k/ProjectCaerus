package com.projectcaerus

import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.res.CompatibilityInfo
import android.content.res.Resources
import android.content.res.TypedArray
import android.util.DisplayMetrics
import android.widget.Button
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.*
import org.powermock.api.mockito.PowerMockito
import org.powermock.core.classloader.annotations.PrepareForTest
import org.powermock.modules.junit4.PowerMockRunner

//
// Created by y2k on 6/4/2016.
//

@RunWith(PowerMockRunner::class)
@PrepareForTest(Resources.Theme::class, Context::class)
class ButtonTest {

    @Test
    fun test() {
        val context = PowerMockito.mock(Context::class.java)
        when_(context.resources).then {
            val resources = mock(Resources::class.java)
            when_(resources.displayMetrics).then {
                DisplayMetrics()
            }
            when_(resources.compatibilityInfo).then {
                CompatibilityInfo(ApplicationInfo())
            }
            resources
        }
        when_(context.obtainStyledAttributes(any(), any(), anyInt(), anyInt())).then {
            mock(TypedArray::class.java)
        }

        val button = Button(context)
    }
}