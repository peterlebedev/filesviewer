package cz.lebedev.filesviewer.view.adapters

import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.view.ActionMode
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import cz.lebedev.filesviewer.R
import cz.lebedev.filesviewer.callback.FileClickCallback
import cz.lebedev.filesviewer.callback.FileOperationCallBack
import cz.lebedev.filesviewer.databinding.FileItemBinding
import cz.lebedev.filesviewer.isUpFileNameFake
import java.io.File
import java.util.ArrayList


class DirectoryAdapter(val fileOperationCallBack: FileOperationCallBack) :
    RecyclerView.Adapter<DirectoryAdapter.FileViewHolder>() {

    public var multiSelect = false
    var data: ArrayList<File>? = null
    val selectedItems = ArrayList<File>()
    val actionModeCallbacks = object : androidx.appcompat.view.ActionMode.Callback {

        override fun onActionItemClicked(mode: ActionMode?, item: MenuItem?): Boolean {
            for (file in selectedItems) {
                  if(fileOperationCallBack.delete(file))
                    data?.remove(file)
            }
            mode?.finish()
            return true
        }

        override fun onCreateActionMode(mode: ActionMode?, menu: Menu?): Boolean {
            multiSelect = true
            menu?.add("Delete")
            return true
        }

        override fun onPrepareActionMode(mode: ActionMode?, menu: Menu?): Boolean {
            return false
        }

        override fun onDestroyActionMode(mode: ActionMode?) {
            multiSelect = false
            selectedItems.clear()
            notifyDataSetChanged()
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FileViewHolder {
        val binding = DataBindingUtil.inflate<FileItemBinding>(
            LayoutInflater.from(parent.context),
            R.layout.file_item,
            parent,
            false
        )
        return FileViewHolder(binding)
    }

    override fun getItemCount(): Int {
        if (data == null)
            return 0
        return data!!.size
    }

    override fun onBindViewHolder(holder: FileViewHolder, position: Int) {
        holder.update(data!!.get(position))
    }

    fun setItems(items: ArrayList<File>?) {
        if (data == null) {
            data = items
            notifyItemRangeInserted(0, data!!.size)
        } else {
            val calculateDiff = DiffUtil.calculateDiff(object : DiffUtil.Callback() {
                override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                    return data!!.get(oldItemPosition).name == items!!.get(newItemPosition).name
                }

                override fun getOldListSize(): Int {
                    return data!!.size
                }

                override fun getNewListSize(): Int {
                    return items!!.size
                }

                override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                    return data!!.get(oldItemPosition).name == items!!.get(newItemPosition).name
                }

            })
            data = items
            calculateDiff.dispatchUpdatesTo(this)
        }
    }


    inner class FileViewHolder(var binding: FileItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun update(file: File) {
            binding.callback = fileClickCallback
            binding.file = file
            if (selectedItems.contains(file)) {
                binding.isselected = true
            } else {
                binding.isselected = false
            }
            binding.executePendingBindings()
            binding.invalidateAll()
        }

        fun selectItem(file: File) {
            if(file.name.isUpFileNameFake() || file.isDirectory)
                return
            if (multiSelect) {
                if (selectedItems.contains(file)) {
                    selectedItems.remove(file)
                    binding.isselected = false
                } else {
                    selectedItems.add(file)
                    binding.isselected = true
                }
            }
        }

        val fileClickCallback = object : FileClickCallback {
            override fun onLongClick(view: View, file: File): Boolean {
                if(file.name.isUpFileNameFake() || file.isDirectory)
                    return true
                (view.getContext() as AppCompatActivity).startSupportActionMode(actionModeCallbacks)
                selectItem(file)
                return true
            }

            override fun onClick(file: File) {
                if (multiSelect) {
                    selectItem(file)
                } else {
                    fileOperationCallBack.open(file)
                }
            }
        }


    }
}
