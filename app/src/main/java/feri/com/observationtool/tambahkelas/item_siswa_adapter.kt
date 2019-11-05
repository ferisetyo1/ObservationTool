package feri.com.observationtool.tambahkelas

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import feri.com.observationtool.R
import feri.com.observationtool.databinding.ItemSiswaBinding
import feri.com.observationtool.tambahsiswa.SiswaModel

class item_siswa_adapter(private var listSiswa: MutableList<SiswaModel>) :
    RecyclerView.Adapter<item_siswa_adapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemSiswaBinding: ItemSiswaBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.item_siswa,
            parent,
            false
        )
        return ViewHolder(itemSiswaBinding)
    }

    override fun getItemCount(): Int {
        return listSiswa.size
    }

    fun addItem(siswaModel: SiswaModel) {
        listSiswa.add(siswaModel)
        notifyDataSetChanged()
    }

    fun replaceData(listSiswa: MutableList<SiswaModel>){
        setList(listSiswa)
    }

    fun setList(listSiswa: MutableList<SiswaModel>){
        this.listSiswa=listSiswa
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindRow(listSiswa[position])
    }

    class ViewHolder(binding: ItemSiswaBinding) : RecyclerView.ViewHolder(binding.root) {
        val itemSiswaBinding = binding
        fun bindRow(siswaModel: SiswaModel) {
            itemSiswaBinding.datas=siswaModel
            itemSiswaBinding.executePendingBindings()
        }
    }
}