package feri.com.observationtool.mulairekaman

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.media.MediaRecorder
import android.os.Bundle
import android.os.Handler
import android.os.SystemClock
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Chronometer
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import feri.com.observationtool.R
import feri.com.observationtool.tambahcatatan.CatatanActivity
import java.io.File
import java.io.IOException

private const val LOG_TAG = "AudioRecordTest"
private const val REQUEST_RECORD_AUDIO_PERMISSION = 200
private const val REQUEST_CODE_CATATAN=201

class MulaiRekamanActivity : AppCompatActivity(), View.OnClickListener {

    private var chronometer: Chronometer? = null
    private var fileName: String = ""
    private lateinit var btn_record: Button
    private lateinit var btn_catatan: Button
    private var startRecording: Boolean? = true
    private var recorder: MediaRecorder? = null
    private lateinit var runnable:Runnable
    private lateinit var myhandler:Handler

    // Requesting permission to RECORD_AUDIO
    private var permissionToRecordAccepted = false
    private var permissions: Array<String> = arrayOf(Manifest.permission.RECORD_AUDIO)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mulai_rekaman)

        ActivityCompat.requestPermissions(this, permissions, REQUEST_RECORD_AUDIO_PERMISSION)

        chronometer = findViewById(R.id.chronometer)
        btn_record = findViewById(R.id.btn_record)
        btn_catatan = findViewById(R.id.catatan)
        val file = File(externalCacheDir,
            "audio_${System.currentTimeMillis()}.3gp")
        fileName = file.absolutePath
        Log.d(LOG_TAG, fileName)

        btn_record.setOnClickListener(this)
        btn_catatan.setOnClickListener(this)

        myhandler= Handler()
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.btn_record -> {
                onRecord(startRecording!!)
                when (startRecording) {
                    true -> {
                        chronometer!!.base = SystemClock.elapsedRealtime()
                        chronometer!!.start()
                        btn_record.text = "berhenti"
                    }
                    false -> {
                        chronometer!!.stop()
                        btn_record.text = "mulai"
                    }
                }
                startRecording = !startRecording!!
            }
            R.id.catatan -> {
                var time=SystemClock.elapsedRealtime() - chronometer!!.base
                var intent= Intent(this, CatatanActivity::class.java)
                intent.putExtra("waktu",time)
                startActivityForResult(intent,REQUEST_CODE_CATATAN)
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        permissionToRecordAccepted = if (requestCode == REQUEST_RECORD_AUDIO_PERMISSION) {
            grantResults[0] == PackageManager.PERMISSION_GRANTED
        } else {
            false
        }
        if (!permissionToRecordAccepted) {
            Toast.makeText(this,"permission required!!",Toast.LENGTH_LONG).show()
            finish()
        }
    }

    private fun onRecord(start: Boolean) = if (start) {
        runnable=object : Runnable{
            override fun run() {
                startRecording()
            }
        }
        myhandler.postDelayed(runnable,1000)
    } else {
        myhandler.removeCallbacks(runnable)
        stopRecording()
    }

    private fun startRecording() {

        recorder = MediaRecorder().apply {
            setAudioSource(MediaRecorder.AudioSource.MIC)
            setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
            setOutputFile(fileName)
            setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)

            try {
                prepare()
            } catch (e: IOException) {
                Log.e(LOG_TAG, "prepare() failed")
            }

            start()
        }
    }

    private fun stopRecording() {
        recorder?.apply {
            stop()
            release()
        }
        recorder = null
    }

    override fun onDestroy() {
        super.onDestroy()
        recorder?.release()
        recorder = null
        myhandler.removeCallbacks(runnable)
    }
}
