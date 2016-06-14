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

/**
 * Created by y2k on 11/06/16.
 */
class ContextFactory {

    private lateinit var resolver: AppThemeAttributeResolver

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

            when_(resources.getLayout(anyInt())).then {
                resolver.getLayout(it.arguments[0] as Int)
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

                val themeAttributeResolver = ThemeAttributeResolver(this.resources)
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

    fun makeNoPowerMock(loader: ClassLoader): Context {
        val resources = object : MockResources() {

            override fun getAnimation(id: Int): XmlResourceParser? {
                return resolver.getAnimation(id)
            }

            override fun getDrawable(id: Int): Drawable? {
                return resolver.loadDrawable(id)
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

            override fun loadDrawable(value: TypedValue?, id: Int): Drawable? {
                return resolver.loadDrawable(id)
            }
        }

        resolver = AppThemeAttributeResolver(resources, loader)

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