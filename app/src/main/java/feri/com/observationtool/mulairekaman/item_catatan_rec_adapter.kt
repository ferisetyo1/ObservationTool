package feri.com.observationtool.mulairekaman

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import feri.com.observationtool.R
import feri.com.observationtool.data.Catatan
import feri.com.observationtool.util.ConvertTime
import kotlinx.android.synthetic.main.item_catatan_rec.view.*

class item_catatan_rec_adapter(private val context: Context?, private val listCatatan : ArrayList<Catatan> ) : RecyclerView.Adapter<item_catatan_rec_adapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_catatan_rec, parent,false))
    }

    override fun getItemCount(): Int {
        return listCatatan.size
    }

    fun addItem(catatan: Catatan){
        listCatatan.add(catatan)
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindViewHolder(listCatatan[position],context,position)
    }

    class ViewHolder(itemView : View) :RecyclerView.ViewHolder(itemView) {
        fun bindViewHolder(
            catatan: Catatan,
            context: Context?,
            position: Int
        ){
            itemView.number.text=(position+1).toString()
            itemView.tipe_kegiatan.text=context?.resources!!.getStringArray(R.array.tipe_kegiatan)[catatan.tipe_kegiatan!!]
            itemView.judul_catatan.text=catatan.judul_catatan
            itemView.time.text=ConvertTime().longtoDate(catatan.time!!)
        }
    }
}