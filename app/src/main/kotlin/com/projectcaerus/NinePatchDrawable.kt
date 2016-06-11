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

    private val image = Bitmap(ImageIO.read(path))

    override fun draw(canvas: Canvas) {
        canvas.drawBitmap(image, null, bounds, null)
    }
    
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