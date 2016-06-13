package android.content.res

import android.content.Context
import android.emoji.EmojiFactory
import android.util.DisplayMetrics
import android.util.TypedValue
import android.view.ViewGroup
import com.projectcaerus.setFinalField
import com.projectcaerus.when_
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.powermock.api.mockito.PowerMockito
import org.powermock.core.classloader.annotations.PrepareForTest
import org.powermock.modules.junit4.PowerMockRunner

/**
 * Created by y2k on 13/06/16.
 */

@RunWith(PowerMockRunner::class)
@PrepareForTest(Resources.Theme::class, EmojiFactory::class)
class TypedArrayTest {

    lateinit var resources: Resources

    @Before
    fun setUp() {
        PowerMockito.mockStatic(EmojiFactory::class.java)

        resources = Mockito.mock(Resources::class.java)
        setFinalField(resources, Resources::class.java.getDeclaredField("mTmpValue"), TypedValue())
        setFinalField(resources,
                Resources::class.java.getDeclaredField("mMetrics"),
                DisplayMetrics().apply { density = 1f })
    }

    @Test
    fun dipMetricTest() {
        val data = IntArray((0 + 1) * 6)
        data[6 * 0] = TypedValue.TYPE_DIMENSION
        data[6 * 0 + 1] = (26 shl 8) + (TypedValue.COMPLEX_UNIT_DIP) + (0 shl 4)
        val array = TypedArray(resources, data, intArrayOf(1, 0), 1)

        val actual = array.getLayoutDimension(0, "layout_width")
        Assert.assertEquals(26, actual)
    }

    @Test
    fun test2() {
        val data = IntArray((0 + 1) * 6)
        data[6 * 0] = TypedValue.TYPE_FIRST_INT
        data[6 * 0 + 1] = ViewGroup.LayoutParams.WRAP_CONTENT
        val array = TypedArray(null, data, intArrayOf(1, 0), 1)

        val actual = array.getLayoutDimension(0, "layout_width")
        Assert.assertEquals(ViewGroup.LayoutParams.WRAP_CONTENT, actual)
    }

    @Test
    fun test() {
        val data = IntArray((0 + 1) * 6)
        data[6 * 0] = TypedValue.TYPE_FIRST_INT
        data[6 * 0 + 1] = ViewGroup.LayoutParams.MATCH_PARENT
        val array = TypedArray(null, data, intArrayOf(1, 0), 1)

        val actual = array.getLayoutDimension(0, "layout_width")
        Assert.assertEquals(ViewGroup.LayoutParams.MATCH_PARENT, actual)

//        val a = c.obtainStyledAttributes(attrs, R.styleable.ViewGroup_MarginLayout)
//        val array = ThemeAttributeResolver(Resources())

//        val context = ContextFactory().make()
//        context.obtainStyledAttributes(attrs, R.styleable.ViewGroup_MarginLayout)

//        array.getLayoutDimension(0, "layout_width")
    }
}