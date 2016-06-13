package android.app

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import com.projectcaerus.AwtCanvas

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

    fun dump(width: Int, height: Int) {
        val screen = width to height
        view.measure(
                View.MeasureSpec.makeMeasureSpec(screen.first, View.MeasureSpec.EXACTLY),
                View.MeasureSpec.makeMeasureSpec(screen.second, View.MeasureSpec.EXACTLY))
        view.layout(0, 0, screen.first, screen.second)

        val canvas = AwtCanvas(width, height)
        view.draw(canvas)
        canvas.setBitmap(null)
    }
}