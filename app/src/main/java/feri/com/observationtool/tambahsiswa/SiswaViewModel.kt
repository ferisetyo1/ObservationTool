package feri.com.observationtool.tambahsiswa

import android.app.Application
import androidx.databinding.ObservableField
import androidx.lifecycle.AndroidViewModel

class SiswaViewModel(application: Application) : AndroidViewModel(application) {
    var siswaModel: ObservableField<SiswaModel> = ObservableField()
}