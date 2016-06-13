package android.content.res

import android.content.Context
import android.emoji.EmojiFactory
import android.view.View
import com.android.internal.policy.impl.PhoneLayoutInflater
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.powermock.core.classloader.annotations.PrepareForTest
import org.powermock.modules.junit4.PowerMockRunner

/**
 * Created by y2k on 11/06/16.
 */
@RunWith(PowerMockRunner::class)
@PrepareForTest(Resources.Theme::class, EmojiFactory::class, Resources.Theme::class)
class LayoutInflateTest {

    lateinit var context: Context

    @Before
    fun setUp() {
        context = ContextFactory().makeMock()
    }

    @Test
    fun test() {
        val inflater = PhoneLayoutInflater(context)

        val view = inflater.inflate(0x01090058, null)
        assertNotNull(view)

        val screenSize = 320 to 480
        view.measure(
                View.MeasureSpec.makeMeasureSpec(screenSize.first, View.MeasureSpec.EXACTLY),
                View.MeasureSpec.makeMeasureSpec(screenSize.second, View.MeasureSpec.EXACTLY))
        view.layout(0, 0, screenSize.first, screenSize.second)
    }
}