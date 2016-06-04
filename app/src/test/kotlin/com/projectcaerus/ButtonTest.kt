package com.projectcaerus

import android.content.Context
import android.content.res.Resources
import android.content.res.TypedArray
import android.util.DisplayMetrics
import android.widget.Button
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.AdditionalMatchers
import org.mockito.Mockito
import org.mockito.Mockito.*
import org.powermock.core.classloader.annotations.PrepareForTest
import org.powermock.modules.junit4.PowerMockRunner

//
// Created by y2k on 6/4/2016.
//

@RunWith(PowerMockRunner::class)
@PrepareForTest(Resources.Theme::class)
class ButtonTest {

    @Test
    fun test() {
        // TODO:

        val context = mock(Context::class.java)
        when_(context.resources).then {
            val resources = mock(Resources::class.java)
            when_(resources.displayMetrics).then {
                DisplayMetrics()
            }
            resources
        }
        when_(context.theme).then {
            val theme = mock(Resources.Theme::class.java)
//            when_(theme.obtainStyledAttributes(any(), any(), any(), any())).then {
//                mock(TypedArray::class.java)
//            }
            theme
        }
        when_(context.obtainStyledAttributes(any(), any(IntArray::class.java), any(), any())).then {
            mock(TypedArray::class.java)
        }
//        Mockito.doAnswer {
//            mock(TypedArray::class.java)
//        }.`when`(context)
//            .obtainStyledAttributes(any(),any(),any(),any())

        Button(context)

    }
}