package android.app

import android.graphics.Canvas
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.projectcaerus.AwtCanvas
import com.projectcaerus.Size

/**
 * Created by y2k on 13/06/16.
 */

open class Activity {

    lateinit var inflater: LayoutInflater

    private lateinit var view: View

    open fun onCreate(states: Bundle?) {
        // TODO:
    }

    fun setContentView(viewId: Int) {
        view = inflater.inflate(viewId, null)
    }

    fun dump(screen: Size) {
        dumpTo(screen, AwtCanvas(screen))

        val g = view as ViewGroup
        g.let {
            println("$view -> $it [(${it.left}, ${it.top}), (${it.width}, ${it.height})]")
        }
        (0..g.childCount - 1).map { g.getChildAt(it) }.forEach {
            println("$view -> $it [(${it.left}, ${it.top}), (${it.width}, ${it.height})]")
        }
    }

    fun dumpTo(screen: Size, canvas: Canvas) {
        view.measure(
                View.MeasureSpec.makeMeasureSpec(screen.width, View.MeasureSpec.EXACTLY),
                View.MeasureSpec.makeMeasureSpec(screen.height, View.MeasureSpec.EXACTLY))
        view.layout(0, 0, screen.width, screen.height)

        view.draw(canvas)
        canvas.setBitmap(null)
    }
}