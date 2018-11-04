package cz.lebedev.filesviewer.callback

import java.io.File

interface FileOperationCallBack{
    fun open(file: File)
    fun delete(file: File) : Boolean
}