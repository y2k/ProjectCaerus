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
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import org.junit.Before
import org.junit.Ignore
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

    val virtScreen = 320 to 480

    lateinit var context: Context
    lateinit var canvas: Canvas

    @Before
    fun setUp() {
        PowerMockito.mockStatic(EmojiFactory::class.java)

        context = PowerMockito.mock(Context::class.java)
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

//        canvas = mock(Canvas::class.java, Answer {
//            println("called = " + it)
//
//            if (it.method.name == "save") 1
//            else null
//        })
//        when_(canvas.save()).then { 1 }
        canvas = AwtCanvas(virtScreen.first, virtScreen.second)
    }

    @Test
    fun test() {
        val button = Button(context)
        button.text = "Hello World"

        button.measure(
            View.MeasureSpec.makeMeasureSpec(virtScreen.first, View.MeasureSpec.EXACTLY),
            View.MeasureSpec.makeMeasureSpec(virtScreen.second, View.MeasureSpec.EXACTLY))
        button.layout(0, 0, virtScreen.first, virtScreen.second)

        button.draw(canvas)
        canvas.setBitmap(null)
    }

    @Test
    fun testLinearLayout() {
        val linearLayout = LinearLayout(context).apply {
            orientation = LinearLayout.VERTICAL

            addView(
                Button(context).apply {
                    text = "Button 1"
                    layoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, 1f)
                })
            addView(
                EditText(context).apply {
                    hint = "Hint for edittext"
                    layoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, 1f)
                })
        }

        linearLayout.measure(
            View.MeasureSpec.makeMeasureSpec(virtScreen.first, View.MeasureSpec.EXACTLY),
            View.MeasureSpec.makeMeasureSpec(virtScreen.second, View.MeasureSpec.EXACTLY))
        linearLayout.layout(0, 0, virtScreen.first, virtScreen.second)
        linearLayout.draw(canvas)
        canvas.setBitmap(null)
    }
}