package android.content.res

import android.util.AttributeSet
import android.util.makeXmlPullAttributes
import org.kxml2.io.KXmlParser

/**
 * Created by y2k on 10/06/16.
 */
class ResourceParser(private val resolver: ThemeAttributeResolver) : KXmlParser(), AttributeSet {

    private val attrs = makeXmlPullAttributes(this)

    override fun getAttributeNameResource(index: Int): Int {
        val name = this.getAttributeName(index).replace("android:", "")
        return resolver.getAttributeIndex(name)
    }

    override fun getAttributeResourceValue(index: Int, p1: Int): Int {
        val name = this.getAttributeValue(index).replace("@drawable/", "")
        return resolver.getDrawableIndex(name)
    }

    override fun getAttributeUnsignedIntValue(p0: String?, p1: String?, p2: Int): Int {
        throw UnsupportedOperationException()
    }

    override fun getAttributeUnsignedIntValue(p0: Int, p1: Int): Int {
        throw UnsupportedOperationException()
    }

    override fun getAttributeIntValue(p0: String?, p1: String?, p2: Int): Int {
        throw UnsupportedOperationException()
    }

    override fun getAttributeIntValue(p0: Int, p1: Int): Int {
        throw UnsupportedOperationException()
    }

    override fun getIdAttribute(): String? {
        throw UnsupportedOperationException()
    }

    override fun getIdAttributeResourceValue(p0: Int): Int {
        throw UnsupportedOperationException()
    }

    override fun getAttributeFloatValue(p0: String?, p1: String?, p2: Float): Float {
        throw UnsupportedOperationException()
    }

    override fun getAttributeFloatValue(p0: Int, p1: Float): Float {
        throw UnsupportedOperationException()
    }

    override fun getStyleAttribute(): Int {
        throw UnsupportedOperationException()
    }

    override fun getAttributeListValue(p0: String?, p1: String?, p2: Array<out String>?, p3: Int): Int {
        throw UnsupportedOperationException()
    }

    override fun getAttributeListValue(p0: Int, p1: Array<out String>?, p2: Int): Int {
        throw UnsupportedOperationException()
    }

    override fun getClassAttribute(): String? {
        throw UnsupportedOperationException()
    }

    override fun getAttributeBooleanValue(p0: String?, p1: String?, p2: Boolean): Boolean {
        throw UnsupportedOperationException()
    }

    override fun getAttributeBooleanValue(index: Int, default: Boolean): Boolean {
        return attrs.getAttributeBooleanValue(index, default)
    }

    override fun getAttributeResourceValue(p0: String?, p1: String?, p2: Int): Int {
        throw UnsupportedOperationException()
    }
}