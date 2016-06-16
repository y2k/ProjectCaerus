package android.content.res

import android.content.Context
import android.content.pm.ApplicationInfo
import android.emoji.EmojiFactory
import android.graphics.drawable.Drawable
import android.test.mock.MockContext
import android.test.mock.MockResources
import android.util.AttributeSet
import android.util.DisplayMetrics
import android.util.TypedValue
import com.projectcaerus.setFinalField
import com.projectcaerus.when_
import org.mockito.Mockito.*
import org.powermock.api.mockito.PowerMockito
import java.io.File

/**
 * Created by y2k on 11/06/16.
 */
class ContextFactory {

    private val resDirectory = File("../res")
    private lateinit var resolver: ThemeAttributeResolver

    fun make(): Context {
        PowerMockito.mockStatic(EmojiFactory::class.java)

        val context = PowerMockito.mock(Context::class.java)
        when_(context.resources).then {
            val resources = mock(Resources::class.java)
            setFinalField(resources, Resources::class.java.getDeclaredField("mTmpValue"), TypedValue())

            when_(resources.displayMetrics).then {
                DisplayMetrics()
            }
            when_(resources.compatibilityInfo).then {
                CompatibilityInfo(ApplicationInfo())
            }

            val resolver = ThemeAttributeResolver(resDirectory, resources)
            when_(resources.loadDrawable(any(), anyInt())).then {
                resolver.loadDrawable(it.arguments[0] as TypedValue, it.arguments[1] as Int)
            }
            when_(resources.obtainAttributes(any(), any())).then {
                resolver.obtainStyledAttributes(
                    it.arguments[0] as AttributeSet?,
                    it.arguments[1] as IntArray, 0, 0)
            }
            when_(resources.getDrawable(anyInt())).then {
                resolver.loadDrawable(it.arguments[0] as TypedValue, it.arguments[0] as Int)
            }

            when_(resources.getLayout(anyInt())).then {
                resolver.getLayout(it.arguments[0] as Int)
            }

            resources
        }

        val themeAttributeResolver = ThemeAttributeResolver(resDirectory, context.resources)
        when_(context.obtainStyledAttributes(any(), any(), anyInt(), anyInt())).then {
            themeAttributeResolver.obtainStyledAttributes(
                it.arguments[0] as AttributeSet?,
                it.arguments[1] as IntArray,
                it.arguments[2] as Int,
                it.arguments[3] as Int
            )
        }

        return context
    }

    fun makeMock(): Context {
        PowerMockito.mockStatic(EmojiFactory::class.java)

        val context = object : MockContext() {

            override fun getResources(): Resources {
                val resources = mock(Resources::class.java)
                setFinalField(resources, Resources::class.java.getDeclaredField("mTmpValue"), TypedValue())

                when_(resources.displayMetrics).then {
                    DisplayMetrics().apply { density = 1f }
                }
                setFinalField(resources,
                    Resources::class.java.getDeclaredField("mMetrics"),
                    DisplayMetrics().apply { density = 1f })

                when_(resources.compatibilityInfo).then {
                    CompatibilityInfo(ApplicationInfo())
                }

                val resolver = ThemeAttributeResolver(resDirectory, resources)
                when_(resources.loadDrawable(any(), anyInt())).then {
                    resolver.loadDrawable(it.arguments[0] as TypedValue, it.arguments[1] as Int)
                }
                when_(resources.obtainAttributes(any(), any())).then {
                    resolver.obtainStyledAttributes(
                        it.arguments[0] as AttributeSet?,
                        it.arguments[1] as IntArray, 0, 0)
                }
                when_(resources.getDrawable(anyInt())).then {
                    resolver.loadDrawable(it.arguments[0] as TypedValue, it.arguments[0] as Int)
                }

                when_(resources.getLayout(anyInt())).then {
                    resolver.getLayout(it.arguments[0] as Int)
                }

                when_(resources.getAnimation(anyInt())).then {
                    resolver.getAnimation(it.arguments[0] as Int)
                }

                return resources
            }

            override fun getTheme(): Resources.Theme {
                val theme = PowerMockito.mock(Resources.Theme::class.java)

                val themeAttributeResolver = ThemeAttributeResolver(resDirectory, this.resources)
                when_(theme.obtainStyledAttributes(any(), any(), anyInt(), anyInt())).then {
                    themeAttributeResolver.obtainStyledAttributes(
                        it.arguments[0] as AttributeSet?,
                        it.arguments[1] as IntArray,
                        it.arguments[2] as Int,
                        it.arguments[3] as Int
                    )
                }
                return theme
            }

            override fun getClassLoader(): ClassLoader? {
                return javaClass.classLoader
            }
        }
        return context
    }

    fun makeNoPowerMock(loader: ClassLoader? = null, resPath: File? = null): Context {
        val assertManager = object : AssetManager() {

            override fun getPooledString(block: Int, id: Int): CharSequence? {
                return resolver.getPooledString(id)
            }
        }

        val resources = object : MockResources(assertManager) {

            override fun obtainAttributes(set: AttributeSet?, attrs: IntArray): TypedArray? {
                return resolver.obtainStyledAttributes(set, attrs, 0, 0)
            }

            override fun getAnimation(id: Int): XmlResourceParser? {
                return resolver.getAnimation(id)
            }

            override fun getDrawable(id: Int): Drawable? {
                return resolver.loadDrawable(TypedValue().apply { type = TypedValue.TYPE_REFERENCE }, id)
            }

            override fun getCompatibilityInfo(): CompatibilityInfo? {
                return CompatibilityInfo(ApplicationInfo())
            }

            override fun getLayout(id: Int): XmlResourceParser? {
                return resolver.getLayout(id)
            }

            override fun getDisplayMetrics(): DisplayMetrics? {
                return DisplayMetrics().apply {
                    setToDefaults()
                    widthPixels = 320
                    heightPixels = 480
                }
            }

            override fun loadDrawable(value: TypedValue, id: Int): Drawable? {
                return resolver.loadDrawable(value, id)
            }
        }

        resolver = if (loader == null) ThemeAttributeResolver(resDirectory, resources) else AppThemeAttributeResolver(resPath!!, resources, loader)

        val context = object : MockContext() {

            override fun obtainStyledAttributes(set: AttributeSet?, attrs: IntArray, defStyleAttr: Int, defStyleRes: Int): TypedArray? {
                return resolver.obtainStyledAttributes(set, attrs, defStyleAttr, defStyleRes)
            }

            override fun obtainStyledAttributes(set: AttributeSet?, attrs: IntArray): TypedArray? {
                return resolver.obtainStyledAttributes(set, attrs, 0, 0)
            }

            override fun getResources(): Resources {
                return resources
            }

            override fun getClassLoader(): ClassLoader? {
                return javaClass.classLoader
            }
        }
        return context
    }
}