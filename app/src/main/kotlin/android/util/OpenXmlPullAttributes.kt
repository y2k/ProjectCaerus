package android.util

import org.xmlpull.v1.XmlPullParser

/**
 * Created by y2k on 10/06/16.
 */
fun makeXmlPullAttributes(parser: XmlPullParser): AttributeSet {
    return XmlPullAttributes(parser)
}