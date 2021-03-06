package com.projectcaerus

import android.graphics.*
import android.graphics.Canvas
import android.graphics.Paint
import java.awt.*
import java.awt.Color
import java.awt.geom.AffineTransform
import java.awt.image.BufferedImage
import java.io.File
import java.util.*
import javax.imageio.ImageIO

/**
 * Created by y2k on 6/5/16.
 */
class AwtCanvas(val size: Size) : Canvas() {

    private val image: BufferedImage
    private val canvas: Graphics2D
    private val states = Stack<StackFrame>()
    private val empty = Rectangle(0, 0, size.width, size.height)

    private var currentClip: Shape = empty

    init {
        image = BufferedImage(size.width, size.height, BufferedImage.TYPE_4BYTE_ABGR)
        canvas = image.createGraphics()
        canvas.clip = currentClip
    }

    override fun getClipBounds(bounds: Rect): Boolean {
        bounds.right = size.width
        bounds.bottom = size.height
        return true
    }

    override fun save(): Int {
        println("save")

        states.push(StackFrame(canvas.transform, canvas.clip))
        return states.size
    }

    override fun clipRect(left: Float, top: Float, right: Float, bottom: Float): Boolean {
        currentClip = Rectangle(left.toInt(), top.toInt(), (right - left).toInt(), (bottom - top).toInt())
        return true
    }

    override fun clipRect(left: Int, top: Int, right: Int, bottom: Int): Boolean {
        currentClip = Rectangle(left, top, right - left, bottom - top)
        return true
    }

    override fun translate(x: Float, y: Float) {
        println("translate: x = $x, y = $y")

        canvas.translate(x.toDouble(), y.toDouble())
    }

    override fun drawText(text: CharSequence?, start: Int, end: Int, x: Float, y: Float, paint: Paint?) {
        drawText("" + text?.subSequence(start, end), x, y, paint)
    }

    override fun drawText(text: String?, x: Float, y: Float, Paint: Paint?) {
        println("drawText: text = '$text', x = $x, y = $y ")

        canvas.paint = Color.red;
        canvas.font = Font("Calibri", Font.PLAIN, 20);

        canvas.drawString(text, x, y + canvas.fontMetrics.height)
    }

    override fun restore() {
        println("restore")

        states.pop().apply {
            currentClip = clip
            canvas.transform = transform
        }
    }

    override fun quickReject(left: Float, top: Float, right: Float, bottom: Float, type: EdgeType?): Boolean {
        return false // TODO:
    }

    override fun restoreToCount(index: Int) {
        while (states.size > index) states.pop()
        canvas.transform = states.pop().transform
    }

    override fun drawBitmap(bitmap: Bitmap, src: Rect?, dst: Rect?, paint: Paint?) {
        val s = src ?: Rect(0, 0, bitmap.image.getWidth(null), bitmap.image.getHeight(null))
        val d = dst ?: Rect(0, 0, size.width, size.height)
        canvas.drawImage(bitmap.image,
            d.left, d.top, d.right, d.bottom,
            s.left, s.top, s.right, s.bottom,
            null)
    }

    override fun setBitmap(bitmap: Bitmap?) {
        val path = File(File(System.getenv("HOME")), "${System.currentTimeMillis()}.png")
        ImageIO.write(image, "png", path)
    }

    override fun drawColor(color: Int) {
        canvas.paint = Color(color, true)

        canvas.clip = currentClip
        canvas.fillRect(0, 0, size.width, size.height)
        canvas.clip = empty
    }

    data class StackFrame(val transform: AffineTransform, val clip: Shape)
}