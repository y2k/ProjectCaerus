package com.projectcaerus

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import java.awt.Color
import java.awt.Font
import java.awt.Graphics2D
import java.awt.geom.AffineTransform
import java.awt.image.BufferedImage
import java.io.File
import java.util.*
import javax.imageio.ImageIO

/**
 * Created by y2k on 6/5/16.
 */
class AwtCanvas(val virtWidth: Int, val virtHeight: Int) : Canvas() {

    private val image: BufferedImage
    private val canvas: Graphics2D
    private val states = Stack<AffineTransform>()

    init {
        image = BufferedImage(virtWidth, virtHeight, BufferedImage.TYPE_4BYTE_ABGR)
        canvas = image.createGraphics()
    }

    override fun save(): Int {
        println("save")

        states.push(canvas.transform)
        return states.size
    }

    override fun clipRect(left: Float, top: Float, right: Float, bottom: Float): Boolean {
// FIXME:
//        println("clipRect: left = $left, top = $top, right = $right, bottom = $bottom")
//        canvas.clipRect(left.toInt(), top.toInt(), (right - left).toInt(), (bottom - top).toInt())
        return true
    }

    override fun clipRect(left: Int, top: Int, right: Int, bottom: Int): Boolean {
// FIXME:
//        println("clipRect: left = $left, top = $top, right = $right, bottom = $bottom")
//        canvas.clipRect(left, top, (right - left), (bottom - top))
        return true
    }

    override fun translate(x: Float, y: Float) {
        println("translate: x = $x, y = $y")

        canvas.translate(x.toDouble(), y.toDouble())
    }

    override fun drawText(text: String?, x: Float, y: Float, p3: Paint?) {
        println("drawText: text = '$text', x = $x, y = $y ")

        canvas.paint = Color.red;
        canvas.font = Font("Calibri", Font.PLAIN, 20);

        canvas.drawString(text, x, y + canvas.fontMetrics.height)
    }

    override fun restore() {
        println("restore")

        canvas.transform = states.pop()
    }

    override fun quickReject(left: Float, top: Float, right: Float, bottom: Float, type: EdgeType?): Boolean {
        return false // TODO:
    }

    override fun restoreToCount(index: Int) {
        while (states.size > index) states.pop()
        canvas.transform = states.pop()
    }

    override fun drawBitmap(bitmap: Bitmap, src: Rect?, dst: Rect?, paint: Paint?) {
        val s = src ?: Rect(0, 0, bitmap.image.getWidth(null), bitmap.image.getHeight(null))
        val d = dst ?: Rect(0, 0, virtWidth, virtHeight)
        canvas.drawImage(bitmap.image,
                d.left, d.top, d.right, d.bottom,
                s.left, s.top, s.right, s.bottom,
                null)
    }

    override fun setBitmap(bitmap: Bitmap?) {
        val path = File(File(System.getenv("HOME")), "${System.currentTimeMillis()}.png")
        ImageIO.write(image, "png", path)
    }
}