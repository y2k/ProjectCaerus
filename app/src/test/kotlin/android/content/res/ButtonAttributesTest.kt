package android.content.res

import android.content.Context
import android.emoji.EmojiFactory
import android.graphics.Canvas
import android.view.View
import android.widget.Button
import com.projectcaerus.AwtCanvas
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
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
        context = ContextFactory().make()
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
}