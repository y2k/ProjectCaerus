package com.projectcaerus

import android.content.res.attrId
import android.graphics.Color
import android.util.AttributeSet
import android.util.TypedValue
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import java.util.*

/**
 * Created by y2k on 16/06/16.
 */
class DrawableParser(private val public: Document) {

    fun parser(attrs: IntArray, data: IntArray, indexes: ArrayList<Int>, style: Element, set: AttributeSet?) {
        getAttrIndex(attrs, "background")?.let {
            val bgStyle = style.select("item[name=background]").first()
            if (bgStyle == null) {
                val textBg = set?.getAttributeValue(null, "android:background") ?: return@let

                val color = textBg
                    .let { normalize(it) }
                    .let { Color.parseColor(it) }
                data[6 * it] = TypedValue.TYPE_INT_COLOR_ARGB8
                data[6 * it + 1] = color
                indexes.add(it)
            } else {
                val bgName = bgStyle.text().split('/')[1]
                val drawableResId = public.select("public[type=drawable][name=$bgName]").first().attrId()

                data[6 * it] = TypedValue.TYPE_REFERENCE
                data[6 * it + 3] = drawableResId
                indexes.add(it)
            }
        }
    }

    private fun normalize(it: String) = if (it.length >= 5) it else it.replace("[^#]".toRegex(), "$10")

    private fun getAttrIndex(attrs: IntArray, name: String): Int? {
        val index = getResource("attr", name)
        return attrs.indexOf(index).let { if (it >= 0) it else null }
    }

    private fun getResource(type: String, name: String): Int {
        return public.select("public[type='$type'][name=$name]").first().attrId()
    }
}