package android.content.res

import android.content.Context
import android.content.pm.ApplicationInfo
import android.emoji.EmojiFactory
import android.graphics.Canvas
import android.util.AttributeSet
import android.util.DisplayMetrics
import android.util.TypedValue
import android.view.View
import android.widget.Button
import com.projectcaerus.IOCanvas
import com.projectcaerus.setFinalField
import com.projectcaerus.when_
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.*
import org.mockito.stubbing.Answer
import org.powermock.api.mockito.PowerMockito
import org.powermock.core.classloader.annotations.PrepareForTest
import org.powermock.modules.junit4.PowerMockRunner

/**
 * Created by y2k on 6/5/16.
 */
@RunWith(PowerMockRunner::class)
@PrepareForTest(Resources.Theme::class, Context::class, EmojiFactory::class)
class ButtonAttributesTest {

    val virtScreen = 320 to 480

    lateinit var context: Context
    lateinit var canvas: Canvas

    @Before
    fun setUp() {
        PowerMockito.mockStatic(EmojiFactory::class.java)

        context = PowerMockito.mock(Context::class.java)
        when_(context.resources).then {
            val resources = mock(Resources::class.java)
            setFinalField(resources, Resources::class.java.getDeclaredField("mTmpValue"), TypedValue())

            when_(resources.displayMetrics).then {
                DisplayMetrics()
            }
            when_(resources.compatibilityInfo).then {
                CompatibilityInfo(ApplicationInfo())
            }
            val resolver = ThemeAttributeResolver(resources)
            when_(resources.loadDrawable(any(), anyInt())).then {
                resolver.loadDrawable(it.arguments[1] as Int)
            }
            when_(resources.obtainAttributes(any(), any())).then {
                resolver.obtainStyledAttributes(
                        it.arguments[0] as AttributeSet?,
                        it.arguments[1] as IntArray, 0, 0)
            }
            when_(resources.getDrawable(anyInt())).then {
                resolver.loadDrawable(it.arguments[0] as Int)
            }
            resources
        }

        val themeAttributeResolver = ThemeAttributeResolver(context.resources)
        when_(context.obtainStyledAttributes(any(), any(), anyInt(), anyInt())).then {
            themeAttributeResolver.obtainStyledAttributes(
                    it.arguments[0] as AttributeSet?,
                    it.arguments[1] as IntArray,
                    it.arguments[2] as Int,
                    it.arguments[3] as Int
            )
        }

//        canvas = mock(Canvas::class.java, Answer {
//            println("called = " + it)
//
//            if (it.method.name == "save") 1
//            else null
//        })
//        when_(canvas.save()).then { 1 }
        canvas = IOCanvas(virtScreen.first, virtScreen.second)
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
}