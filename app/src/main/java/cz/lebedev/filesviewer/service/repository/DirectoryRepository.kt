package cz.lebedev.filesviewer.service.repository

import android.content.Context
import android.content.SharedPreferences
import android.os.Environment
import cz.lebedev.filesviewer.Env
import cz.lebedev.filesviewer.isRoot
import cz.lebedev.filesviewer.isUpFileNameFake
import io.reactivex.Observable
import java.io.File
import javax.inject.Inject


class DirectoryRepository @Inject constructor(var sharedPreferences : DirectorySharedPrefs){

      var actuallDirName : String = ""

    fun getFiles(dirName: String = ""): Observable<ArrayList<File>> {

          var dir : File;
          if(!dirName.isEmpty()){
              if(dirName.isUpFileNameFake())
                dir = File(actuallDirName).parentFile
              else
                dir = File(dirName)
              actuallDirName = dir.absolutePath
          }else{
              if(actuallDirName!!.isEmpty())
                  actuallDirName = sharedPreferences.getDefaultFolder()!!
              dir = File(actuallDirName)
          }

          val listFiles = dir.listFiles()

          var size = 0
          if(listFiles !=null){
              size = listFiles.size
          }

          var listFilesTmp :  ArrayList<File>
          if(actuallDirName!!.isRoot()){
              listFilesTmp = ArrayList<File>(size)
          }else{
              listFilesTmp = ArrayList<File>(size + 1)
              val fakefile = File("..")
              listFilesTmp.add(fakefile)
          }
          if(listFiles!=null)
            listFilesTmp.addAll(listFiles)

         return Observable.just(dir)
             .flatMap { Observable.fromArray(listFilesTmp) }
     }




}





