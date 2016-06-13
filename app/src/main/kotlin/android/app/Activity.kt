package android.app

import android.os.Bundle
import android.view.LayoutInflater

/**
 * Created by y2k on 13/06/16.
 */

open class Activity {

    lateinit var inflater: LayoutInflater

    open fun onCreate(states: Bundle?) {
        // TODO:
    }

    fun setContentView(viewId: Int) {
        // TODO:
        val view = inflater.inflate(viewId, null)
    }
}