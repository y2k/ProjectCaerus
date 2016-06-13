package android.content.res

import android.graphics.drawable.Drawable
import android.graphics.drawable.StateListDrawable
import android.util.AttributeSet
import android.util.TypedValue
import android.view.ViewGroup
import com.projectcaerus.NinePatchDrawable
import com.projectcaerus.createStateListDrawable
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import org.jsoup.parser.Parser
import java.io.File

/**
 * Created by y2k on 6/5/16.
 */

class ThemeAttributeResolver(private val resources: Resources) {

    private val public: Document
    private val styles: Document

    init {
        public = Jsoup.parse(File("../res/values/public.xml").readText(), "", Parser.xmlParser())
        styles = Jsoup.parse(File("../res/values/styles.xml").readText(), "", Parser.xmlParser())
    }

    fun getLayout(id: Int): XmlResourceParser {
        val layoutName = public.select("public[type=layout][id=${id.toHex()}]").first().attr("name")
        val pathToResource = File(File("../res/layout"), "$layoutName.xml")

        val parser = ResourceParser(this)
        parser.setInput(pathToResource.bufferedReader())
        return parser
    }

    fun loadDrawable(id: Int): Drawable {
        val bgName = public.select("public[type=drawable][id=${id.toHex()}]").first().attr("name")
        println("bgName = $bgName")

        val pathToResource = getPathToResource(bgName)
        return when {
            pathToResource.extension == "xml" -> {
                pathToResource.bufferedReader().use {
                    val parser = ResourceParser(this)
                    parser.setInput(it)
                    createStateListDrawable(resources, parser)
                }
            }
            pathToResource.name.endsWith(".9.png") -> {
                return NinePatchDrawable(pathToResource)
            }
            else -> throw UnsupportedOperationException("Path = $pathToResource")
        }
    }

    private fun getPathToResource(resName: String): File {
        return File("../res")
                .listFiles { s -> s.isDirectory }
                .flatMap { it.listFiles().toList() }
                .first { it.nameWithoutExtension.replace(".9", "") == resName }
    }

    fun obtainStyledAttributes(set: AttributeSet?, attrs: IntArray, defStyleAttr: Int, defStyleRes: Int): TypedArray {
        if (set != null && set.getAttributeValue(null, "android:layout_width") != null) {
            val wIndex = getAttrIndex(attrs, "layout_width") ?: return makeEmptyTypedArray(resources)
            val hIndex = getAttrIndex(attrs, "layout_height") ?: return makeEmptyTypedArray(resources)
            val data = IntArray((Math.max(wIndex, hIndex) + 1) * 6)
            loadDimension(set, data, "layout_width", wIndex)
            loadDimension(set, data, "layout_height", hIndex)

            return TypedArray(resources, data, intArrayOf(1, wIndex, hIndex), 2);
        }

        if (defStyleAttr == 0) return makeEmptyTypedArray(resources)

        val styleAttrName = public.select("public[id=${defStyleAttr.toHex()}]").first()
        if (styleAttrName.attr("type") != "attr") throw IllegalStateException()
        val styleName = getThemeAttribute(styleAttrName.attr("name"))
        val style = styles.select("style[name=${styleName.split('/')[1]}]").first()

        val bgName = style.select("item[name=background]").first().text().split('/')[1]
        val bgIndex = public.select("public[type=drawable][name=$bgName]").first().attrId()

        val index = getAttrIndex(attrs, "background") ?: return makeEmptyTypedArray(resources)

        val data = IntArray((index + 1) * 6)
        data[6 * index] = TypedValue.TYPE_REFERENCE
        data[6 * index + 3] = bgIndex

        val result = TypedArray(resources, data, intArrayOf(1, index), 1)
        return result;
    }

    fun loadDimension(set: AttributeSet, target: IntArray, dimName: String, index: Int) {
        val sizeText = set.getAttributeValue(null, "android:$dimName")
        val size = parserStringToDimension(sizeText)

        if (size >= 0) {
            target[6 * 0] = TypedValue.TYPE_DIMENSION
            target[6 * 0 + 1] = (26 shl 8) + (TypedValue.COMPLEX_UNIT_DIP) + (0 shl 4)
        } else {
            target[6 * index] = TypedValue.TYPE_FIRST_INT
            target[6 * index + 1] = size
        }
    }

    private fun parserStringToDimension(sizeText: String): Int {
        val dipRegex = "(\\d+)(sp|dip|dp)".toRegex()
        val themeAttrRegex = "\\?(.+)".toRegex()
        val size = when {
            sizeText in listOf("fill_parent", "match_parent") -> ViewGroup.LayoutParams.MATCH_PARENT
            sizeText == "wrap_content" -> ViewGroup.LayoutParams.WRAP_CONTENT
            sizeText.matches(dipRegex) -> dipRegex.find(sizeText)!!.groupValues[1].toInt()
            sizeText.matches(themeAttrRegex) -> getThemeDimensionAttribute(themeAttrRegex.find(sizeText)!!.groupValues[1])
            else -> throw IllegalStateException(sizeText)
        }
        return size
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

    fun getAttributeIndex(name: String): Int {
        return getResource("attr", name)
    }

    fun getDrawableIndex(name: String): Int {
        return getResource("drawable", name)
    }

    private fun getResource(type: String, name: String): Int {
        return public.select("public[type='$type'][name=$name]").first().attrId()
    }
}

fun makeEmptyTypedArray(resources: Resources) = TypedArray(resources, IntArray(100), intArrayOf(0), 0)

fun Element.attrId(): Int {
    return attr("id").let { Integer.parseInt(it.substring(2), 16) }
}

fun Int.toHex(): String {
    return String.format("0x%08X", this)
}
