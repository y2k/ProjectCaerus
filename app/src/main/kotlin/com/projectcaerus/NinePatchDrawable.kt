package com.projectcaerus

import android.graphics.*
import android.graphics.drawable.Drawable
import java.io.File
import java.util.*
import javax.imageio.ImageIO

/**
 * Created by y2k on 10/06/16.
 */
class NinePatchDrawable(path: File) : Drawable() {

    private val bitmap: Bitmap

    private val hResize: Pair<Int, Int>
    private val vResize: Pair<Int, Int>

    private var srcRect: List<Rect>

    init {
        val image = ImageIO.read(path)

        val hParts = getStretchParts(image.getRGB(1, 0, image.width - 2, 1, null, 0, image.width - 2))
        val vParts = getStretchParts(image.getRGB(0, 1, 1, image.height - 2, null, 0, 1))
        hResize = hParts[0] to hParts[2]
        vResize = vParts[0] to vParts[2]

        bitmap = Bitmap(image.getSubimage(1, 1, image.width - 2, image.height - 2))

        srcRect = listOf(
                Rect(0, 0, hResize.first, vResize.first),
                Rect(hResize.first, 0, bitmap.width - vResize.second, vResize.first),
                Rect(bitmap.width - vResize.second, 0, bitmap.width, vResize.first),

                Rect(0, vResize.first, hResize.first, bitmap.height - vResize.second),
                Rect(hResize.first, vResize.first, bitmap.width - hResize.second, bitmap.height - vResize.second),
                Rect(bitmap.width - hResize.second, vResize.first, bitmap.width, bitmap.height - vResize.second),

                Rect(0, bitmap.height - vResize.second, hResize.first, bitmap.height),
                Rect(hResize.first, bitmap.height - vResize.second, bitmap.width - hResize.second, bitmap.height),
                Rect(bitmap.width - hResize.second, bitmap.height - vResize.second, bitmap.width, bitmap.height)
        )
    }

    private fun getStretchParts(colors: IntArray): List<Int> {
        val result = ArrayList<Int>()
        result.add(1)
        colors.map { it >= 0 }.reduce { l, r ->
            if (l != r) result.add(1)
            else result[result.size - 1] = result.last() + 1
            r
        }
        return result
    }

    override fun draw(canvas: Canvas) {
        listOf(
                Rect(bounds.left, bounds.top, bounds.left + hResize.first, bounds.top + vResize.first),
                Rect(bounds.left + hResize.first, bounds.top, bounds.right - vResize.second, bounds.top + vResize.first),
                Rect(bounds.right - hResize.second, bounds.top, bounds.right, bounds.top + vResize.first),

                Rect(bounds.left, bounds.top + vResize.first, hResize.first, bounds.bottom - vResize.second),
                Rect(bounds.left + hResize.first, bounds.top + vResize.first, bounds.right - hResize.second, bounds.bottom - vResize.second),
                Rect(bounds.right - hResize.second, bounds.top + vResize.first, bounds.right, bounds.bottom - vResize.second),

                Rect(bounds.left, bounds.bottom - vResize.second, bounds.left + hResize.first, bounds.bottom),
                Rect(bounds.left + hResize.first, bounds.bottom - vResize.second, bounds.right - hResize.second, bounds.bottom),
                Rect(bounds.right - hResize.second, bounds.bottom - vResize.second, bounds.right, bounds.bottom)
        ).forEachIndexed { i, rect ->
            canvas.drawBitmap(bitmap, srcRect[i], rect, null)
        }
    }

    override fun setAlpha(alpha: Int) {
        // Ignore
    }

    override fun setColorFilter(colorFilter: ColorFilter?) {
        // Ignore
    }

    override fun getOpacity(): Int {
        return -1;
    }
}