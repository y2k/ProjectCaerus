package android.content.res

import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.util.TypedValue
import android.view.ViewGroup
import android.widget.LinearLayout
import com.projectcaerus.DrawableParser
import com.projectcaerus.NinePatchDrawable
import com.projectcaerus.createStateListDrawable
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import org.jsoup.parser.Parser
import java.io.File
import java.util.*

/**
 * Created by y2k on 6/5/16.
 */

open class ThemeAttributeResolver(
    private val resDirectory: File,
    private val resources: Resources) {

    private val public = Jsoup.parse(File("../res/values/public.xml").readText(), "", Parser.xmlParser())
    private val styles = Jsoup.parse(File("../res/values/styles.xml").readText(), "", Parser.xmlParser())
    private val stringPool = HashMap<Int, String>()

    private val drawableParser = DrawableParser(public)

    fun getPooledString(id: Int): CharSequence? {
        return stringPool[id]
    }

    fun getAnimation(id: Int): XmlResourceParser {
        return getXmlResourceById(id, "anim")
    }

    fun getLayout(id: Int): XmlResourceParser {
        return getXmlResourceById(id, "layout")
    }

    private fun getXmlResourceById(id: Int, type: String): ResourceParser {
        val pathToResource = getPathToResource(id, type)
        val parser = ResourceParser(this)
        parser.setInput(pathToResource.bufferedReader())
        return parser
    }

    protected open fun getPathToResource(id: Int, type: String): File {
        val resName = public.select("public[type=$type][id=${id.toHex()}]").first().attr("name")
        return File(File("../res/$type"), "$resName.xml")
    }

    fun loadDrawable(value: TypedValue, id: Int): Drawable {
        if (value.type in TypedValue.TYPE_FIRST_COLOR_INT..TypedValue.TYPE_LAST_COLOR_INT) {
            return ColorDrawable(value.data)
        }

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
        val data = IntArray(attrs.size * 6)
        val style = getStyle(defStyleAttr)
        val indexes = ArrayList<Int>()

        fun loadDimension(dimenName: String) {
            getAttrIndex(attrs, dimenName)?.let {
                if (set != null && loadDimension(set, data, dimenName, it)) indexes.add(it)
            }
        }

        loadDimension("layout_margin")
        loadDimension("layout_width")
        loadDimension("layout_height")
        loadDimension("padding")
        loadDimension("paddingLeft")
        loadDimension("paddingRight")
        loadDimension("paddingTop")
        loadDimension("paddingBottom")

        getAttrIndex(attrs, "text")?.let {
            if (set == null) return@let

            val text = set.getAttributeValue(null, "android:text") ?: ""
            stringPool[text.hashCode()] = text

            data[6 * it] = TypedValue.TYPE_STRING
            data[6 * it + 1] = text.hashCode()

            indexes.add(it)
        }

        getAttrIndex(attrs, "hint")?.let {
            if (set == null) return@let

            val text = set.getAttributeValue(null, "android:hint") ?: ""
            stringPool[text.hashCode()] = text

            data[6 * it] = TypedValue.TYPE_STRING
            data[6 * it + 1] = text.hashCode()

            indexes.add(it)
        }

        getAttrIndex(attrs, "orientation")?.let {
            val textOrientation = set?.getAttributeValue(null, "android:orientation") ?: return@let

            val orientation = when (textOrientation) {
                "horizontal" -> LinearLayout.HORIZONTAL
                "vertical" -> LinearLayout.VERTICAL
                else -> throw IllegalStateException("Orientation: $textOrientation")
            }

            data[6 * it] = TypedValue.TYPE_FIRST_INT
            data[6 * it + 1] = orientation

            indexes.add(it)
        }

        getAttrIndex(attrs, "background")?.let {
            drawableParser.parser(data, indexes, it, style)
        }

        indexes.add(0, indexes.size)
        return TypedArray(resources, data, indexes.toIntArray(), attrs.size);
    }

    private fun getStyle(defStyleAttr: Int): Element {
        if (defStyleAttr == 0) return Document("")

        val styleAttrName = public.select("public[id=${defStyleAttr.toHex()}]").first()
        if (styleAttrName.attr("type") != "attr") throw IllegalStateException()
        val styleName = getThemeAttribute(styleAttrName.attr("name"))
        return styles.select("style[name=${styleName.split('/')[1]}]").first()
    }

    fun loadDimension(set: AttributeSet, target: IntArray, dimName: String, index: Int): Boolean {
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

fun Element.attrId(): Int {
    return attr("id").let { Integer.parseInt(it.substring(2), 16) }
}

fun Int.toHex(): String {
    return String.format("0x%08X", this)
}
