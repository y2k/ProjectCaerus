package android.content.res

import android.content.Context
import android.emoji.EmojiFactory
import android.graphics.Canvas
import android.view.View
import android.widget.EditText
import com.projectcaerus.AwtCanvas
import com.projectcaerus.Size
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
        val editText = EditText(context)
        editText.hint = "Hint Hello World"

        editText.measure(
                View.MeasureSpec.makeMeasureSpec(screen.width, View.MeasureSpec.EXACTLY),
                View.MeasureSpec.makeMeasureSpec(screen.height, View.MeasureSpec.EXACTLY))
        editText.layout(0, 0, screen.width, screen.height)

        editText.draw(canvas)
        canvas.setBitmap(null)
    }
}