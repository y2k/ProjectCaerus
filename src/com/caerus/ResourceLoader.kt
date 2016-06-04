package com.caerus

import android.util.TypedValue
import org.jsoup.Jsoup
import java.io.File

/**
 * Created by y2k on 6/4/16.
 */
object ResourceLoader {

    private val resources = Jsoup.parse(File("res/values/public.xml"), "UTF-8")

    fun loadResourceValue(ident: Int, outValue: TypedValue, resolve: Boolean): Int {
        val resId = "0x" + String.format("%08X", ident)
        val rec = resources.select("public[id=$resId]").first()
        outValue.string = rec.attr("name")
        outValue.type = when (rec.attr("type")) {
            "layout" -> TypedValue.TYPE_STRING
            else -> throw UnsupportedOperationException("type = " + rec.attr("type"))
        }
        return 1
    }
}