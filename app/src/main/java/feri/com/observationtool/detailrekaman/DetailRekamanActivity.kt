package feri.com.observationtool.detailrekaman

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import feri.com.observationtool.R
import feri.com.observationtool.databinding.ActivityDetailRekamanBinding
import feri.com.observationtool.detailrekaman.action.DetailRekamanAction
import feri.com.observationtool.detailrekaman.viewmodel.DetailRekamanViewModel
import feri.com.observationtool.mulairekaman.MulaiRekamanActivity

class DetailRekamanActivity : AppCompatActivity() {

    private lateinit var viewBinding: ActivityDetailRekamanBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding=DataBindingUtil.setContentView<ActivityDetailRekamanBinding>(this,R.layout.activity_detail_rekaman)
        viewBinding.vm=ViewModelProviders.of(this).get(DetailRekamanViewModel::class.java)
        viewBinding.action=object:DetailRekamanAction{
            override fun onClickBack() {
                onBackPressed()
            }

            override fun onClickLanjut() {
                Log.d("judul",viewBinding.vm?.judul?.get().toString())
                Log.d("mapel",resources.getStringArray(R.array.list_matapelajaran)[viewBinding.vm?.mapel_position?.get()!!])
                Log.d("lokasi",viewBinding.vm?.lokasi?.get().toString())
                Log.d("tanggal",viewBinding.vm?.tanggal?.get().toString())
                startActivity(Intent(this@DetailRekamanActivity, MulaiRekamanActivity::class.java))
            }
        }
    }

}
