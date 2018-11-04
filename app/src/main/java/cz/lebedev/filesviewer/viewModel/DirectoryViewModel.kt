package cz.lebedev.filesviewer.viewModel

import android.app.Application
import android.content.SharedPreferences
import android.os.Environment
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import cz.lebedev.filesviewer.Env
import cz.lebedev.filesviewer.service.repository.DirectoryRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import java.io.File
import javax.inject.Inject

class DirectoryViewModel : AndroidViewModel {

    val observableLiveData: MutableLiveData<ArrayList<File>> = MutableLiveData()
    val errorLiveData: MutableLiveData<String> = MutableLiveData()

    var directoryRepository: DirectoryRepository
    lateinit var disposableObserver: DisposableObserver<ArrayList<File>>


    @Inject constructor (application: Application, directoryRepository: DirectoryRepository) : super(application) {
        this.directoryRepository = directoryRepository
        application.applicationContext
        loadFiles()
    }

    fun getObservableLD() : LiveData<ArrayList<File>> {
        return observableLiveData
    }

    fun getObservableErrorLD() : LiveData<String> {
        return errorLiveData
    }

    fun loadFiles(f: File? = null) {
        disposableObserver = object : DisposableObserver<ArrayList<File>>(){
            override fun onComplete() {}

            override fun onNext(t: ArrayList<File>) {
                observableLiveData.postValue(t)
            }

            override fun onError(e: Throwable) {
                errorLiveData.postValue(e.toString() + ":" +e.message)
            }

        }

        var path = ""
        if(f!=null)
            path = f.absolutePath


        directoryRepository.getFiles(path).subscribeOn(Schedulers.io())
            .subscribeOn(AndroidSchedulers.mainThread())
            .subscribe(disposableObserver)


    }

    fun refresh(){
        loadFiles()
    }

    fun disposeElements(){
        if(!disposableObserver.isDisposed) disposableObserver.dispose()
    }

    fun getActualFolder() : String{
        return directoryRepository.actuallDirName
    }




  }