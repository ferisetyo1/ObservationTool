package feri.com.observationtool.tambahcatatan

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import feri.com.observationtool.R
import feri.com.observationtool.inputgambarvideo.CameraActivity
import kotlinx.android.synthetic.main.activity_catatan.*
import java.util.concurrent.TimeUnit

private const val REQUEST_CODE_PHOTO = 200
class CatatanActivity : AppCompatActivity() , View.OnClickListener{
    override fun onClick(v: View?) {
        when(v!!.id){
            R.id.btn_tambah->{

            }
            R.id.btn_batal->{

            }
            R.id.tambahFoto->{
                startActivityForResult(Intent(this,CameraActivity::class.java), REQUEST_CODE_PHOTO)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_catatan)
        var intent=intent

        var millis=intent.getLongExtra("waktu",0)
        val hms = String.format(
            "%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(millis),
            TimeUnit.MILLISECONDS.toMinutes(millis) % TimeUnit.HOURS.toMinutes(1),
            TimeUnit.MILLISECONDS.toSeconds(millis) % TimeUnit.MINUTES.toSeconds(1)
        )
        waktu.text=hms
    }
}
