package com.projectcaerus

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.ColorFilter
import android.graphics.drawable.Drawable
import java.io.File
import javax.imageio.ImageIO

/**
 * Created by y2k on 10/06/16.
 */
class NinePatchDrawable(path: File) : Drawable() {

    private val image = ImageIO.read(path)

    override fun draw(canvas: Canvas) {
        canvas.drawBitmap(getAndroidBitmap(), null, bounds, null)
    }

    private fun getAndroidBitmap(): Bitmap {
        throw UnsupportedOperationException("not implemented")
    }

    private fun getBitmap() = null

    override fun setAlpha(alpha: Int) {
        // Ignore
    }

    override fun setColorFilter(colorFilter: ColorFilter?) {
        // Ignore
    }

    override fun getOpacity(): Int {
        return 100;
    }
}