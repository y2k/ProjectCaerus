package android.content.res

import java.io.File

/**
 * Created by y2k on 13/06/16.
 */
class AppThemeAttributeResolver(
        private val resources: Resources,
        private val classLoader: ClassLoader) :
        ThemeAttributeResolver(resources) {

    override fun getPathToResource(id: Int, type: String): File {
        val resType = classLoader.loadClass("com.example.helloworld.R$$type")
        val filed = resType.fields.firstOrNull { it.getInt(null) == id } ?: return super.getPathToResource(id, type)

        val resDir = File("../samples/hello-world/app/src/main/res/$type")
        val path = resDir.listFiles().first { it.nameWithoutExtension == filed.name }
        return path
    }
}