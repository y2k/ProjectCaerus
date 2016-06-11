package android.content.res

import android.content.Context
import android.emoji.EmojiFactory
import android.graphics.Canvas
import android.view.View
import android.widget.EditText
import com.projectcaerus.AwtCanvas
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.powermock.core.classloader.annotations.PrepareForTest
import org.powermock.modules.junit4.PowerMockRunner

/**
 * Created by y2k on 11/06/16.
 */
@RunWith(PowerMockRunner::class)
@PrepareForTest(Resources.Theme::class, Context::class, EmojiFactory::class)
class EditTextAttributeTest {

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
        val editText = EditText(context)
        editText.hint = "Hint Hello World"

        editText.measure(
                View.MeasureSpec.makeMeasureSpec(virtScreen.first, View.MeasureSpec.EXACTLY),
                View.MeasureSpec.makeMeasureSpec(virtScreen.second, View.MeasureSpec.EXACTLY))
        editText.layout(0, 0, virtScreen.first, virtScreen.second)

        editText.draw(canvas)
        canvas.setBitmap(null)
    }
}