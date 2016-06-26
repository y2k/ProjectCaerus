package android.content.res

import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.util.TypedValue
import android.widget.LinearLayout
import com.projectcaerus.*
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
    private val dimensionParser = DimensionParser(styles, public, resDirectory)
    private val textParser = TextParser(public, stringPool)

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

        dimensionParser.parse2(attrs, indexes, set, data, "layout_margin")
        dimensionParser.parse2(attrs, indexes, set, data, "layout_width")
        dimensionParser.parse2(attrs, indexes, set, data, "layout_height")
        dimensionParser.parse2(attrs, indexes, set, data, "padding")
        dimensionParser.parse2(attrs, indexes, set, data, "paddingLeft")
        dimensionParser.parse2(attrs, indexes, set, data, "paddingRight")
        dimensionParser.parse2(attrs, indexes, set, data, "paddingTop")
        dimensionParser.parse2(attrs, indexes, set, data, "paddingBottom")

        textParser.parse(attrs, data, indexes, set, "text")
        textParser.parse(attrs, data, indexes, set, "hint")
        drawableParser.parser(attrs, data, indexes, style, set)

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

    private fun getThemeAttribute(attributeName: String): String {
        return styles.select("style[name=Theme] > item[name=$attributeName]").first().text()
    }

    fun getAttributeIndex(name: String): Int {
        return getResource("attr", name)
    }

    fun getDrawableIndex(name: String): Int {
        return getResource("drawable", name)
    }

    private fun getAttrIndex(attrs: IntArray, name: String): Int? {
        val index = getResource("attr", name)
        return attrs.indexOf(index).let { if (it >= 0) it else null }
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
