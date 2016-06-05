package android.content.res

import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.util.TypedValue
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

    fun loadDrawable(value: TypedValue, id: Int): Drawable {
        val bgName = public.select("public[type=drawable][id=${id.toHex()}]").first().attr("name")
        println("bgName = $bgName")

        TODO("value = $value, id = $id")
    }

    //        return this.getTheme().obtainStyledAttributes(set, attrs, defStyleAttr, defStyleRes)
    fun obtainStyledAttributes(set: AttributeSet?, attrs: IntArray, defStyleAttr: Int, defStyleRes: Int): TypedArray {
        val styleAttrName = public.select("public[id=${defStyleAttr.toHex()}]").first()
        if (styleAttrName.attr("type") != "attr") throw IllegalStateException()
//        println("Attr name = " + styleAttrName.attr("name"))
        val styleName = styles.select("style[name=Theme] > item[name=${styleAttrName.attr("name")}]").first()
//        println("Style name = " + styleName.text())
        val style = styles.select("style[name=${styleName.text().split('/')[1]}]").first()
//        println("Style = " + style)
        val bgName = style.select("item[name=background]").first().text().split('/')[1]
//        println("bgName = " + bgName)
        val bgIndex = public.select("public[type=drawable][name=$bgName]").first().index()
//        println("bgIndex = " + bgIndex)

        val index = getBackgroundIndex(attrs, "background") ?: return TypedArray(resources, IntArray(100), intArrayOf(0), 0)

        val data = IntArray((index + 1) * 6)
        data[6 * index] = TypedValue.TYPE_REFERENCE
        data[6 * index + 3] = bgIndex

        val result = TypedArray(resources, data, intArrayOf(1, index), 1)
        return result;
    }

    private fun getBackgroundIndex(attrs: IntArray, name: String): Int? {
        val index = public.select("public[type='attr'][name=$name]").first().index()
        return attrs.indexOf(index).let { if (it >= 0) it else null }
    }

    fun Element.index(): Int {
        return attr("id").let { Integer.parseInt(it.substring(2), 16) }
    }

    fun Int.toHex(): String {
        return String.format("0x%08X", this)
    }
}