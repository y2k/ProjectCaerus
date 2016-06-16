package com.projectcaerus

import android.content.res.attrId
import android.util.AttributeSet
import android.util.TypedValue
import org.jsoup.nodes.Document
import java.util.*

/**
 * Created by y2k on 16/06/16.
 */
class TextParser(
    private val public: Document,
    private val stringPool: HashMap<Int, String>) {

    fun parse(attrs: IntArray, data: IntArray, indexes: ArrayList<Int>, set: AttributeSet?, attributeName: String) {
        getAttrIndex(attrs, attributeName)?.let {
            if (set == null) return@let

            val text = set.getAttributeValue(null, "android:$attributeName") ?: ""
            stringPool[text.hashCode()] = text

            data[6 * it] = TypedValue.TYPE_STRING
            data[6 * it + 1] = text.hashCode()

            indexes.add(it)
        }
    }

    private fun getAttrIndex(attrs: IntArray, name: String): Int? {
        val index = getResource("attr", name)
        return attrs.indexOf(index).let { if (it >= 0) it else null }
    }

    private fun getResource(type: String, name: String): Int {
        return public.select("public[type='$type'][name=$name]").first().attrId()
    }
}