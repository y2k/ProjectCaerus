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
import android.widget.LinearLayout
import org.junit.Before
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

        canvas = mock(Canvas::class.java, Answer {
            println("called = " + it)

            if (it.method.name == "save") 1
            else null
        })
        when_(canvas.save()).then { 1 }
    }

    @Test
    fun test() {
        val button = Button(context)
        button.text = "Hello World"

        button.measure(
            View.MeasureSpec.makeMeasureSpec(200, View.MeasureSpec.EXACTLY),
            View.MeasureSpec.makeMeasureSpec(200, View.MeasureSpec.EXACTLY))
        button.layout(0, 0, 200, 200)

        button.draw(canvas)
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
                Button(context).apply {
                    text = "Button 2"
                    layoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, 1f)
                })
        }

        linearLayout.measure(
            View.MeasureSpec.makeMeasureSpec(200, View.MeasureSpec.EXACTLY),
            View.MeasureSpec.makeMeasureSpec(200, View.MeasureSpec.EXACTLY))
        linearLayout.layout(0, 0, 200, 200)
        linearLayout.draw(canvas)
    }
}