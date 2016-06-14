package android.app

import android.graphics.Canvas
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
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