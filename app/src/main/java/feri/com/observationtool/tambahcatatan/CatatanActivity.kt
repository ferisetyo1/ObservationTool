package feri.com.observationtool.tambahcatatan

import android.Manifest
import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.FileProvider
import feri.com.observationtool.R
import feri.com.observationtool.data.Catatan
import feri.com.observationtool.util.ConvertTime
import kotlinx.android.synthetic.main.activity_catatan.*
import kotlinx.android.synthetic.main.dialog_video_foto.*
import java.io.File
import java.io.IOException


private const val REQUEST_VIDEO_CAPTURE = 201
private const val REQUEST_IMAGE_CAPTURE = 202
private const val REQUEST_PERMISSION = 203
private var permissions: Array<String> = arrayOf(
    Manifest.permission.CAMERA,
    Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE
)

class CatatanActivity : AppCompatActivity(), View.OnClickListener {

    private var time_milis: Long? = 0
    var currentpath: String = ""

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.btn_tambah -> {
                var returnintent = Intent()
                returnintent.putExtra(
                    "data_catatan", Catatan(
                        nama_catatan.text.toString().trim(),
                        tipe_kegiatan.selectedItemPosition,
                        deskripsi_catatan.text.toString().trim(),
                        time_milis,
                        this.currentpath
                    )
                )
                setResult(Activity.RESULT_OK,returnintent)
                finish()
            }
            R.id.btn_batal -> {
                finish()
            }
            R.id.tambahFoto -> {
                var dialog = Dialog(this)
                dialog.setContentView(R.layout.dialog_video_foto)
                dialog.btn_foto.setOnClickListener {
                    dispatchTakePictureIntent()
                    dialog.dismiss()
                    true
                }
                dialog.btn_video.setOnClickListener {
                    dispatchTakeVideoIntent()
                    dialog.dismiss()
                    true
                }
                dialog.show()
            }
            R.id.pv_video -> {
                when (pv_video.isPlaying) {
                    false -> pv_video.start()
                    true -> {
                        pv_video.stopPlayback()
                        pv_video.resume()
                    }
                }
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        var permissionToRecordAccepted = if (requestCode == REQUEST_PERMISSION) {
            grantResults[0] == PackageManager.PERMISSION_GRANTED
        } else {
            false
        }
        if (!permissionToRecordAccepted) {
            Toast.makeText(this, "permission required!!", Toast.LENGTH_LONG).show()
            finish()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_catatan)
        ActivityCompat.requestPermissions(this, permissions, REQUEST_PERMISSION)
        var intent = intent

        time_milis = intent.getLongExtra("waktu", 0)
        waktu.text = ConvertTime().longtoDate(time_milis!!)
    }

    private fun dispatchTakePictureIntent() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            // Ensure that there's a camera activity to handle the intent
            takePictureIntent.resolveActivity(packageManager)?.also {
                // Create the File where the photo should go
                val photoFile: File? = try {
                    createFile("gambar", ".jpg")
                } catch (ex: IOException) {
                    null
                }
                // Continue only if the File was successfully created
                photoFile?.also {
                    val fileUri = FileProvider.getUriForFile(
                        this,
                        "feri.com.observationtool.fileprovider",
                        it
                    )
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri)
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
                }
            }
        }
    }

    private fun dispatchTakeVideoIntent() {
        Intent(MediaStore.ACTION_VIDEO_CAPTURE).also { takeVideoIntent ->
            takeVideoIntent.resolveActivity(packageManager)?.also {
                val videoFile: File? = try {
                    createFile("video", ".mp4")
                } catch (ex: IOException) {
                    null
                }
                videoFile?.also {
                    val fileUri = FileProvider.getUriForFile(
                        this,
                        "feri.com.observationtool.fileprovider",
                        it
                    )
                    takeVideoIntent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri)
                    startActivityForResult(takeVideoIntent, REQUEST_VIDEO_CAPTURE)
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode !== Activity.RESULT_CANCELED) {
            if (requestCode === REQUEST_IMAGE_CAPTURE) {
                val myBitmap = BitmapFactory.decodeFile(currentpath)
                pv_image.setImageBitmap(myBitmap)
                pv_video.visibility = View.GONE
                pv_image.visibility = View.VISIBLE
            } else if (requestCode === REQUEST_VIDEO_CAPTURE) {
                pv_video.setVideoPath(currentpath)
                pv_video.start()
                pv_image.visibility = View.INVISIBLE
                pv_video.visibility = View.VISIBLE
            }
        }
    }

    @Throws(IOException::class)
    private fun createFile(prefix: String, ext: String): File {
        // Create an image file name
        return File.createTempFile(
            "${prefix}_${System.currentTimeMillis()}", /* prefix */
            "${ext}", /* suffix */
            externalCacheDir /* directory */
        ).apply {
            currentpath = absolutePath
        }
    }
}
