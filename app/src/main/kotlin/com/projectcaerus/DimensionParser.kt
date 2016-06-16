package com.projectcaerus

import android.content.res.attrId
import android.util.AttributeSet
import android.util.TypedValue
import android.view.ViewGroup
import org.jsoup.nodes.Document
import java.io.File

/**
 * Created by y2k on 16/06/16.
 */
class DimensionParser(
    private val styles: Document,
    private val public: Document,
    private val resDirectory: File) {

    fun parse2(attrs: IntArray, indexes: MutableList<Int>, set: AttributeSet?, data: IntArray, dimenName: String) {
        getAttrIndex(attrs, dimenName)?.let {
            if (set != null && parse(set, data, dimenName, it)) indexes.add(it)
        }
    }


    fun parse(set: AttributeSet, target: IntArray, dimName: String, index: Int): Boolean {
        val sizeText = set.getAttributeValue(null, "android:$dimName") ?: return false
        val size = parserStringToDimension(sizeText)

        if (size >= 0) {
            target[6 * index] = TypedValue.TYPE_DIMENSION
            target[6 * index + 1] = (26 shl 8) + (TypedValue.COMPLEX_UNIT_DIP) + (0 shl 4)
        } else {
            target[6 * index] = TypedValue.TYPE_FIRST_INT
            target[6 * index + 1] = size
        }

        return true
    }

    private fun parserStringToDimension(sizeText: String): Int {
        val dipRegex = "([\\d\\.]+)(sp|dip|dp)".toRegex()
        val themeAttrRegex = "\\?(.+)".toRegex()
        val resDimenRegex = "@dimen/(.+)".toRegex()
        val size = when {
            sizeText in listOf("fill_parent", "match_parent") -> ViewGroup.LayoutParams.MATCH_PARENT
            sizeText == "wrap_content" -> ViewGroup.LayoutParams.WRAP_CONTENT
            sizeText.matches(dipRegex) -> dipRegex.find(sizeText)!!.groupValues[1].toFloat().toInt()
            sizeText.matches(themeAttrRegex) -> getThemeDimensionAttribute(themeAttrRegex.find(sizeText)!!.groupValues[1])
            sizeText.matches(resDimenRegex) -> getDimenResource(resDimenRegex.find(sizeText)!!.groupValues[1])
            else -> throw IllegalStateException("Not found action for pattern: $sizeText")
        }
        return size
    }

    private fun getDimenResource(dimenName: String): Int {
        val dimenRegex = "<dimen name=\"$dimenName\">(.+?)</dimen>".toRegex()
        val dimenText = File(resDirectory, "values")
            .listFiles().asSequence()
            .flatMap { it.readLines().asSequence() }
            .map { dimenRegex.find(it)?.groupValues?.get(1) }
            .filterNotNull()
            .first()
        return parserStringToDimension(dimenText)
    }

    private fun getThemeDimensionAttribute(attributeName: String): Int {
        return parserStringToDimension(getThemeAttribute(attributeName))
    }

    private fun getThemeAttribute(attributeName: String): String {
        return styles.select("style[name=Theme] > item[name=$attributeName]").first().text()
    }

    private fun getAttrIndex(attrs: IntArray, name: String): Int? {
        val index = getResource("attr", name)
        return attrs.indexOf(index).let { if (it >= 0) it else null }
    }

    private fun getResource(type: String, name: String): Int {
        return public.select("public[type='$type'][name=$name]").first().attrId()
    }
}