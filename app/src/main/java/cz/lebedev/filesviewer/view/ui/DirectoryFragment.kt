package cz.lebedev.filesviewer.view.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import cz.lebedev.filesviewer.R
import cz.lebedev.filesviewer.databinding.DirectoryFragmentBinding
import cz.lebedev.filesviewer.viewModel.DirectoryViewModel
import cz.lebedev.filesviewer.view.adapters.DirectoryAdapter
import dagger.android.support.AndroidSupportInjection
import java.io.File
import javax.inject.Inject
import android.content.Intent
import android.content.res.Configuration
import android.net.Uri
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import cz.lebedev.filesviewer.callback.FileOperationCallBack
import cz.lebedev.filesviewer.getMimeType
import cz.lebedev.filesviewer.isUpFileNameFake

class DirectoryFragment : Fragment() {

    private lateinit var binding: DirectoryFragmentBinding
    private lateinit var directoryAdapter : DirectoryAdapter

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var directoryViewModel: DirectoryViewModel


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding  = DataBindingUtil.inflate<DirectoryFragmentBinding>(LayoutInflater.from(container!!.context), R.layout.directory_fragment, container, false)

        directoryAdapter = DirectoryAdapter(fileOperationCallBack,context)
        binding.myrecycler.adapter = directoryAdapter
        binding.isLoading = true
        return binding.root
    }

    override fun onAttach(context: Context?) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        directoryViewModel = ViewModelProviders.of(this,
            viewModelFactory).get(DirectoryViewModel::class.java)

        directoryViewModel.getObservableLD().observe(this, Observer {
            directoryAdapter.setItems(it)
            binding.isLoading = false
            (activity as AppCompatActivity).getSupportActionBar()?.setTitle(directoryViewModel.getActualFolder());  // provide compatibility to all the versions
        })

        directoryViewModel.getObservableErrorLD().observe(this, Observer {
            binding.errorMessage = it
        })
        super.onActivityCreated(savedInstanceState)
    }

    fun refresh() {
        directoryViewModel.refresh()
        super.onDestroy()
    }


    override fun onDestroy() {
        directoryViewModel.disposeElements()
        super.onDestroy()
    }

    override fun onConfigurationChanged(newConfig: Configuration?) {
        super.onConfigurationChanged(newConfig)
        // Checks the orientation of the screen
        if (newConfig?.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            binding.myrecycler.layoutManager = GridLayoutManager(context,2)
        } else if (newConfig?.orientation == Configuration.ORIENTATION_PORTRAIT){
            binding.myrecycler.layoutManager = LinearLayoutManager(context)
        }
    }

    fun getActuallDir(): String {
       return directoryViewModel.getActualFolder()
    }

    val fileOperationCallBack  = object : FileOperationCallBack {
        override fun delete(file: File): Boolean {
            val deleted = file.delete()
            if(deleted ){
                return true
            }else{
                Toast.makeText(context,file.name+ getString(R.string.cannot_be_del),Toast.LENGTH_LONG).show()
                return false
            }
        }

        override fun open(file: File) {
            if(file.isFile && !file.name.isUpFileNameFake()){
                try{
                    val intent = Intent()
                    intent.action = android.content.Intent.ACTION_VIEW
                    intent.setDataAndType(Uri.fromFile(file), file.getMimeType())
                    startActivity(intent)
                }catch(e:Exception){
                    Toast.makeText(context,getString(R.string.cannot_open),Toast.LENGTH_LONG).show()
                }
            }else{
                directoryViewModel.loadFiles(file)
            }
        }

    }



    companion object {
        val TAG : String = "DirectoryFragment"
    }
}


