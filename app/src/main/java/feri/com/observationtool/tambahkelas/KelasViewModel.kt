package feri.com.observationtool.tambahkelas

import android.app.Application
import android.util.Log
import androidx.databinding.ObservableArrayList
import androidx.databinding.ObservableField
import androidx.databinding.ObservableList
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import feri.com.observationtool.api.ApiConfig
import feri.com.observationtool.api.dao.ResultStringDao
import feri.com.observationtool.tambahsiswa.SiswaModel
import feri.com.observationtool.util.SingleLiveEvent
import okhttp3.MediaType
import okhttp3.RequestBody
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class KelasViewModel(application: Application) : AndroidViewModel(application),
    Callback<ResultStringDao> {
    var rvSiswa: ObservableList<SiswaModel> = ObservableArrayList()
    var kelasModel: ObservableField<KelasModel>
    var progressDialog: SingleLiveEvent<Boolean>? = SingleLiveEvent()
    var resultStringDao: MutableLiveData<ResultStringDao> = MutableLiveData()

    init {
        kelasModel = ObservableField(KelasModel())
    }

    fun tambahkelas() {
        progressDialog?.value = true
        var jsonObject = JSONObject()
        jsonObject.put("user_id", "201910281545176lGhm4")
        jsonObject.put("nama_kelas", kelasModel.get()?.nama_kelas)
        jsonObject.put("nama_sekolah", kelasModel.get()?.nama_sekolah)
        jsonObject.put("bentuk_kelas", "akwkakawkwa.jpg")
        var jsonArray = JSONArray()
        for (siswaModel in rvSiswa) {
            var SiswaJson = JSONObject()
            SiswaJson.put("siswa_nama", siswaModel.nama_siswa)
            SiswaJson.put("siswa_noabsen", siswaModel.no_absen)
            jsonArray.put(SiswaJson)
        }
        jsonObject.put("list_siswa", jsonArray)
        Log.d("print", jsonObject.toString())
        ApiConfig.getApi().tambahkelas(
            RequestBody.create(
                MediaType.parse("application/json; charset=utf-8"),
                jsonObject.toString()
            )
        ).enqueue(this)
    }

    override fun onFailure(call: Call<ResultStringDao>, t: Throwable) {
        t.printStackTrace()
    }

    override fun onResponse(call: Call<ResultStringDao>, response: Response<ResultStringDao>) {
        progressDialog?.value = false
        resultStringDao?.value = response.body()
    }
}
