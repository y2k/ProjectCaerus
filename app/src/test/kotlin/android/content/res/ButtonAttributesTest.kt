package android.content.res

import android.content.Context
import android.emoji.EmojiFactory
import android.graphics.Canvas
import android.view.View
import android.widget.Button
import com.projectcaerus.AwtCanvas
import com.projectcaerus.Size
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

    val screen = Size(320, 480)

    lateinit var context: Context
    lateinit var canvas: Canvas

    @Before
    fun setUp() {
        context = ContextFactory().make()
        canvas = AwtCanvas(screen)
    }

    @Test
    fun test() {
        val button = Button(context)
        button.text = "Hello World"

        button.measure(
                View.MeasureSpec.makeMeasureSpec(screen.width, View.MeasureSpec.EXACTLY),
                View.MeasureSpec.makeMeasureSpec(screen.height, View.MeasureSpec.EXACTLY))
        button.layout(0, 0, screen.width, screen.height)

        button.draw(canvas)
        canvas.setBitmap(null)
    }
}