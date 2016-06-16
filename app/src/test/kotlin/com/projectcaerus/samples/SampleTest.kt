package com.projectcaerus.samples

import android.app.Activity
import android.content.res.ContextFactory
import android.graphics.Canvas
import com.android.internal.policy.impl.PhoneLayoutInflater
import com.projectcaerus.Size
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.mockito.Mockito
import org.mockito.stubbing.Answer
import java.io.File
import java.net.URLClassLoader

/**
 * Created by y2k on 13/06/16.
 */
class SampleTest {

    @Test
    fun test() {
        val appPath = File("../samples/hello-world/app")
        val resPath = File(appPath, "src/main/res")
        val classPath = File(appPath, "build/intermediates/classes/debug")
        assertTrue(classPath.exists())

        val loader = URLClassLoader(arrayOf(classPath.toURI().toURL()))
        val context = ContextFactory().makeNoPowerMock(loader, resPath)

        val mainClass = loader.loadClass("com.example.helloworld.MainActivity")
        assertNotNull(mainClass)

        val mainActivity = mainClass.newInstance() as Activity
        mainActivity.inflater = PhoneLayoutInflater(context)
        mainActivity.onCreate(null)

        mainActivity.dump(Size(320, 480))
//        mainActivity.dumpTo(Size(320, 480), mockCanvas())
    }

    private fun mockCanvas(): Canvas {
        return Mockito.mock(Canvas::class.java, Answer {
            println("called = " + it)
            if (it.method.name == "save") 1 else null
        })
    }
}