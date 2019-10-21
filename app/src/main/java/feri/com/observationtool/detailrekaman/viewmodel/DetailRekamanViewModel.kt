package feri.com.observationtool.detailrekaman.viewmodel

import android.app.Application
import androidx.databinding.ObservableField
import androidx.lifecycle.AndroidViewModel

class DetailRekamanViewModel(application: Application) : AndroidViewModel(application) {
    var judul: ObservableField<String>? = null
    var mapel_position: ObservableField<Int>? = null
    var lokasi: ObservableField<String>? = null
    var tanggal: ObservableField<String>? = null

    init {
        judul = ObservableField("")
        mapel_position = ObservableField(0)
        lokasi = ObservableField("")
        tanggal = ObservableField("15-10-2019")
    }

}