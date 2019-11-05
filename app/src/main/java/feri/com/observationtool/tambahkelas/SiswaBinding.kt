package feri.com.observationtool.tambahkelas

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import feri.com.observationtool.tambahsiswa.SiswaModel

object SiswaBinding {
    @BindingAdapter("app:listsiswa")
    @JvmStatic
    fun setListSiswa(recyclerView: RecyclerView, listSiswa: MutableList<SiswaModel>) {
        with(recyclerView.adapter as item_siswa_adapter){
            replaceData(listSiswa)
        }
    }
}