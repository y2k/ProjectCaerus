package com.projectcaerus

import android.content.res.attrId
import android.util.TypedValue
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import java.util.*

/**
 * Created by y2k on 16/06/16.
 */
class DrawableParser(private val public: Document) {

    fun parser(data: IntArray, indexes: ArrayList<Int>, it: Int, style: Element) {
        val bgStyle = style.select("item[name=background]").first()
        if (bgStyle != null) {
            val bgName = bgStyle.text().split('/')[1]
            val drawableResId = public.select("public[type=drawable][name=$bgName]").first().attrId()

            data[6 * it] = TypedValue.TYPE_REFERENCE
            data[6 * it + 3] = drawableResId
            indexes.add(it)
        }
    }
}