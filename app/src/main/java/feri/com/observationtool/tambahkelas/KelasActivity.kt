package feri.com.observationtool.tambahkelas

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import feri.com.observationtool.R
import feri.com.observationtool.databinding.ActivityKelasBinding
import feri.com.observationtool.tambahsiswa.SiswaActivity
import feri.com.observationtool.tambahsiswa.SiswaModel

private const val REQUEST_TAMBAH_SISWA = 200

class KelasActivity : AppCompatActivity() {

    private lateinit var viewBinding: ActivityKelasBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding =
            DataBindingUtil.setContentView<ActivityKelasBinding>(this, R.layout.activity_kelas)
                .apply {
                    vm = ViewModelProviders.of(this@KelasActivity).get(KelasViewModel::class.java)
                    action = object : KelasAction {
                        override fun OnClickSimpan() {
                            vm!!.tambahkelas()
                        }

                        override fun OnClickTambahSiswa() {
                            var intent=Intent(
                                this@KelasActivity,
                                SiswaActivity::class.java
                            )
                            intent.putExtra("absen",vm!!.rvSiswa.size+1)
                            startActivityForResult(intent
                                , REQUEST_TAMBAH_SISWA
                            )
                        }

                        override fun OnClickTambahBentuk() {
                            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                        }

                    }
                }

        setupRecycler()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_TAMBAH_SISWA) {
            viewBinding.recyclerView.apply {
                (adapter as item_siswa_adapter).addItem(data?.getSerializableExtra("dataSiswa") as SiswaModel)
                Log.d("data",(data?.getSerializableExtra("dataSiswa") as SiswaModel).toString())
            }
        }

    }

    fun setupRecycler(){
        viewBinding.recyclerView.apply {
            adapter=item_siswa_adapter(viewBinding.vm!!.rvSiswa)
        }
    }
}
