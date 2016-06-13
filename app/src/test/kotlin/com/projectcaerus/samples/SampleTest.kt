package com.projectcaerus.samples

import android.app.Activity
import android.content.res.ContextFactory
import com.android.internal.policy.impl.PhoneLayoutInflater
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test
import java.io.File
import java.net.URLClassLoader

/**
 * Created by y2k on 13/06/16.
 */
class SampleTest {

    @Test
    fun test() {
        val path = File("../samples/hello-world/app/build/intermediates/classes/debug")
        assertTrue(path.exists())

        val loader = URLClassLoader(arrayOf(path.toURI().toURL()))
        val context = ContextFactory().makeNoPowerMock(loader)

        val mainClass = loader.loadClass("com.example.helloworld.MainActivity")
        assertNotNull(mainClass)

        val mainActivity = mainClass.newInstance() as Activity
        mainActivity.inflater = PhoneLayoutInflater(context)
        mainActivity.onCreate(null)

        mainActivity.dump(320, 480)
    }
}