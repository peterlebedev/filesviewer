package cz.lebedev.filesviewer

import android.os.Environment
import java.io.File
import android.webkit.MimeTypeMap


class Env{
    companion object {
        fun getRootDir() : String{
           return Environment.getExternalStorageDirectory().absolutePath
        }
    }
}

fun String.isUpFileNameFake() : Boolean {
    if (this == "/.." || this == "..")
        return true
    return false
}


fun String.isRoot() : Boolean {
    return (this == "/"+ Env.getRootDir() || this == Env.getRootDir())
}

fun File.getMimeType(): String? {
    val url =  absolutePath
    val parts = url.split("\\.".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
    val extension = parts[parts.size - 1]
    var type: String? = null
    val mime = MimeTypeMap.getSingleton()
    type = mime.getMimeTypeFromExtension(extension)
    return type
}


