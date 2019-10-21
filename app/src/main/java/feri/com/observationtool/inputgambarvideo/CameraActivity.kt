package feri.com.observationtool.inputgambarvideo

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.graphics.Matrix
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.util.Log
import android.util.Rational
import android.util.Size
import android.view.Surface
import android.view.TextureView
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.*
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import feri.com.observationtool.R
import feri.com.observationtool.util.LuminosityAnalyzer
import kotlinx.android.synthetic.main.activity_camera.*
import java.io.File

private const val REQUEST_CODE_PERMISSIONS = 10

// This is an array of all the permission specified in the manifest
private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)

class CameraActivity : AppCompatActivity(), LifecycleOwner {

    private lateinit var viewFinder: TextureView
    private lateinit var filename: String
    private var isrecord=false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camera)

        viewFinder = findViewById(R.id.view_finder)

        // Request camera permissions
        if (allPermissionsGranted()) {
            viewFinder.post { startCamera() }
        } else {
            ActivityCompat.requestPermissions(
                this, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS
            )
        }

        // Every time the provided texture view changes, recompute layout
        viewFinder.addOnLayoutChangeListener { _, _, _, _, _, _, _, _, _ ->
            updateTransform()
        }
    }

    @SuppressLint("RestrictedApi")
    private fun startCamera() {

        // Create configuration object for the viewfinder use case
        val previewConfig = PreviewConfig.Builder().apply {
            setTargetAspectRatio(Rational(1, 1))
            setTargetResolution(Size(640, 640))
        }.build()

        // Build the viewfinder use case
        val preview = Preview(previewConfig)

        // Every time the viewfinder is updated, recompute layout
        preview.setOnPreviewOutputUpdateListener {

            // To update the SurfaceTexture, we have to remove it and re-add it
            val parent = viewFinder.parent as ViewGroup
            parent.removeView(viewFinder)
            parent.addView(viewFinder, 0)

            viewFinder.surfaceTexture = it.surfaceTexture
            updateTransform()
        }

        // Create configuration object for the viewfinder use case
        // Add this before CameraX.bindToLifecycle

        // Create configuration object for the image capture use case
        val imageCaptureConfig = ImageCaptureConfig.Builder()
            .apply {
                setTargetAspectRatio(Rational(1, 1))
                // We don't set a resolution for image capture; instead, we
                // select a capture mode which will infer the appropriate
                // resolution based on aspect ration and requested mode
                setCaptureMode(ImageCapture.CaptureMode.MIN_LATENCY)
            }.build()

        // Build the image capture use case and attach button click listener
        val imageCapture = ImageCapture(imageCaptureConfig)

        val analyzerConfig = ImageAnalysisConfig.Builder().apply {
            // Use a worker thread for image analysis to prevent glitches
            val analyzerThread = HandlerThread(
                "LuminosityAnalysis"
            ).apply { start() }
            setCallbackHandler(Handler(analyzerThread.looper))
            // In our analysis, we care more about the latest image than
            // analyzing *every* image
            setImageReaderMode(
                ImageAnalysis.ImageReaderMode.ACQUIRE_LATEST_IMAGE
            )
        }.build()

        // Build the image analysis use case and instantiate our analyzer
        val analyzerUseCase = ImageAnalysis(analyzerConfig).apply {
            analyzer = LuminosityAnalyzer()
        }

        val videoCaptureConfig = VideoCaptureConfig.Builder().apply {
            //            setLensFacing(CameraX.LensFacing.BACK)
            setTargetAspectRatio(Rational(1, 1))
            setTargetRotation(viewFinder.display.rotation)
            setTargetResolution(Size(640,640))
        }.build()

        val videoCapture = VideoCapture(videoCaptureConfig)

        capture_button.setOnClickListener {
            if(!switch_mode.isChecked){
                val file = File(
                    externalCacheDir,
                    "${System.currentTimeMillis()}.jpg"
                )
                imageCapture.takePicture(file,
                    object : ImageCapture.OnImageSavedListener {
                        override fun onError(
                            error: ImageCapture.ImageCaptureError,
                            message: String, exc: Throwable?
                        ) {
                            val msg = "Photo capture failed: $message"
                            Toast.makeText(baseContext, msg, Toast.LENGTH_SHORT).show()
                            Log.e("CameraXApp", msg)
                            exc?.printStackTrace()
                        }

                        override fun onImageSaved(file: File) {
                            val msg = "Photo capture succeeded: ${file.absolutePath}"
                            filename = file.absolutePath
                            Toast.makeText(baseContext, msg, Toast.LENGTH_SHORT).show()
                            Log.d("CameraXApp", msg)
                        }
                    })
            }else {
                val file = File(
                    externalCacheDir,
                    "${System.currentTimeMillis()}.mp4"
                )
                if (!isrecord) {
                    videoCapture?.startRecording(file, object : VideoCapture.OnVideoSavedListener {
                        override fun onVideoSaved(file: File) {
                            Log.i(javaClass.simpleName, "Video File : $file")
                        }

                        override fun onError(
                            videoCaptureError: VideoCapture.VideoCaptureError,
                            message: String,
                            cause: Throwable?
                        ) {
                            Log.i(javaClass.simpleName, "Video Error: $message")
                        }
                    })
                    capture_button.background=ContextCompat.getDrawable(this,R.drawable.ic_stop_black_24dp)
                }else{
                    capture_button.background=ContextCompat.getDrawable(this,R.drawable.ic_fiber_manual_record_black_24dp)
                    videoCapture?.stopRecording()
                }
                isrecord=!isrecord
            }
        }


        // Bind use cases to lifecycle
        // If Android Studio complains about "this" being not a LifecycleOwner
        // try rebuilding the project or updating the appcompat dependency to
        // version 1.1.0 or higher.
        CameraX.bindToLifecycle(this,preview,imageCapture,analyzerUseCase)
        switch_mode.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked){
                switch_mode.text="video"
                CameraX.unbind(imageCapture)
                CameraX.unbind(analyzerUseCase)
                updateTransform()
                CameraX.bindToLifecycle(this,videoCapture)
                capture_button.background=ContextCompat.getDrawable(this,R.drawable.ic_fiber_manual_record_black_24dp)
            }
            else {
                switch_mode.text="foto"
                CameraX.unbind(videoCapture)
                updateTransform()
                CameraX.bindToLifecycle(this,imageCapture,analyzerUseCase)
                capture_button.background=ContextCompat.getDrawable(this,android.R.drawable.ic_menu_camera)
            }

        }


    }


    private fun updateTransform() {
        val matrix = Matrix()

        // Compute the center of the view finder
        val centerX = viewFinder.width / 2f
        val centerY = viewFinder.height / 2f

        // Correct preview output to account for display rotation
        val rotationDegrees = when (viewFinder.display.rotation) {
            Surface.ROTATION_0 -> 0
            Surface.ROTATION_90 -> 90
            Surface.ROTATION_180 -> 180
            Surface.ROTATION_270 -> 270
            else -> return
        }
        matrix.postRotate(-rotationDegrees.toFloat(), centerX, centerY)

        // Finally, apply transformations to our TextureView
        viewFinder.setTransform(matrix)
    }


    /**
     * Process result from permission request dialog box, has the request
     * been granted? If yes, start Camera. Otherwise display a toast
     */
    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>, grantResults: IntArray
    ) {
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (allPermissionsGranted()) {
                viewFinder.post { startCamera() }
            } else {
                Toast.makeText(
                    this,
                    "Permissions not granted by the user.",
                    Toast.LENGTH_SHORT
                ).show()
                finish()
            }
        }
    }

    /**
     * Check if all permission specified in the manifest have been granted
     */
    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(
            baseContext, it
        ) == PackageManager.PERMISSION_GRANTED
    }


}
