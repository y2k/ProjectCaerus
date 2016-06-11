package android.content.res

import android.content.Context
import android.content.pm.ApplicationInfo
import android.emoji.EmojiFactory
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
}