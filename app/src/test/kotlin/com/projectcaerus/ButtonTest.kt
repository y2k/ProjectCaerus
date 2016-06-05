package com.projectcaerus

import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.res.CompatibilityInfo
import android.content.res.Resources
import android.content.res.TypedArray
import android.emoji.EmojiFactory
import android.graphics.Canvas
import android.util.DisplayMetrics
import android.view.View
import android.widget.Button
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.*
import org.mockito.stubbing.Answer
import org.powermock.api.mockito.PowerMockito
import org.powermock.core.classloader.annotations.PrepareForTest
import org.powermock.modules.junit4.PowerMockRunner

//
// Created by y2k on 6/4/2016.
//

@RunWith(PowerMockRunner::class)
@PrepareForTest(Resources.Theme::class, Context::class, EmojiFactory::class)
class ButtonTest {

    @Test
    fun test() {
        PowerMockito.mockStatic(EmojiFactory::class.java)

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

        val canvas = mock(Canvas::class.java, Answer {
            println("called = " + it)

            if (it.method.name == "save") 1
            else null
        })
        when_(canvas.save()).then { 1 }

        val button = Button(context)
        button.text = "Hello World"

        button.measure(
            View.MeasureSpec.makeMeasureSpec(200, View.MeasureSpec.EXACTLY),
            View.MeasureSpec.makeMeasureSpec(200, View.MeasureSpec.EXACTLY))
        button.layout(0, 0, 200, 200)

        button.draw(canvas)
    }
}