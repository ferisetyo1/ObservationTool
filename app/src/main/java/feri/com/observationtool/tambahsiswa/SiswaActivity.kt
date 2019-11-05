package feri.com.observationtool.tambahsiswa

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import feri.com.observationtool.R
import feri.com.observationtool.databinding.ActivitySiswaBinding

class SiswaActivity : AppCompatActivity() {

    private lateinit var viewBinding: ActivitySiswaBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding =
            DataBindingUtil.setContentView<ActivitySiswaBinding>(this, R.layout.activity_siswa)
                .apply {
                    vm = ViewModelProviders.of(this@SiswaActivity).get(SiswaViewModel::class.java)
                    vm!!.siswaModel.set(SiswaModel(intent.getIntExtra("absen", 0),""))

                    action = object : SiswaAction {

                        override fun OnClickTambah() {
                            var returnintent = Intent()
                            returnintent.putExtra("dataSiswa", vm?.siswaModel?.get())
                            setResult(Activity.RESULT_OK, returnintent)
                            finish()
                        }

                    }
                }
    }
}
