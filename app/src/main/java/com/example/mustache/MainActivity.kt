package com.example.mustache

import android.app.Activity
import android.app.Instrumentation
import android.content.Intent
import android.graphics.Bitmap
import android.media.Image
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.DisplayMetrics
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.graphics.convertTo
import androidx.core.graphics.createBitmap
import androidx.fragment.app.Fragment
import com.example.mustache.arface.FaceMask
import com.example.mustache.arface.FaceMaskElement
import com.example.mustache.rendering.AugmentedFaceFragment
import com.example.mustache.rendering.AugmentedFaceListener
import com.example.mustache.rendering.AugmentedFaceNode
import com.google.ar.core.Frame
import com.google.ar.core.ImageFormat
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_augmented_face.*
import java.util.*

class MainActivity : AppCompatActivity(), AugmentedFaceListener {

    private var faceMasks = ArrayList<FaceMask>()
    private var currentMaskIndex: Int = 0
    private var updateMask: Boolean = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        (face_view as AugmentedFaceFragment).setAugmentedFaceListener(this)
        initMasks()
        change_btn.setOnClickListener { nextMask() }

        /* val shoot_pic = findViewById<ImageButton>(R.id.shoot_pic) */

    }

    /* AR Code Below */
    override fun onFaceAdded(face: AugmentedFaceNode) {
        changeMask(face)
    }

    override fun onFaceUpdate(face: AugmentedFaceNode) {
        if (updateMask) {
            changeMask(face)
            updateMask = false
        }
    }

    private fun initMasks() {

        val lightbrownlandmarks = ArrayList<FaceMaskElement>()
        lightbrownlandmarks.add(
            FaceMaskElement(
                AugmentedFaceNode.Companion.FaceLandmark.MUSTACHE,
                "models/mustache_lightbrown.obj",
                "models/cafeclaro_color.png"
            )
        )
        faceMasks.add(FaceMask("models/invisiblemask.png", lightbrownlandmarks))

        val brownlandmarks = ArrayList<FaceMaskElement>()
        brownlandmarks.add(
            FaceMaskElement(
                AugmentedFaceNode.Companion.FaceLandmark.MUSTACHE,
                "models/mustache_darkbrown.obj",
                "models/brown_color.png"
            )
        )

        faceMasks.add(FaceMask("models/invisiblemask.png", brownlandmarks))
    }

    private fun nextMask() {
        currentMaskIndex++
        if (currentMaskIndex == faceMasks.size) {
            currentMaskIndex = 0
        }
        updateMask = true
    }

    private fun changeMask(face: AugmentedFaceNode) {
        val currentMask = faceMasks[currentMaskIndex]

        currentMask.faceTexture?.let {
            face.setFaceMeshTexture(it)
        }

        face.clearLandmarks()
        currentMask.landmarkMap?.let { landmarks ->
            for (landmark in landmarks) {
                face.setRegionModel(landmark.landmark, landmark.model, landmark.texture)
            }
        }
    }
/** convertir displaymetrics(unit) en int y continuar tratando de tomar la foto con ar on
    fun requestpicture() {
        createBitmap(width = gettoInt(displayMetricsW()) , height = displayMetricsH(), Bitmap.Config = Bitmap.Config.ARGB_8888)
    }

    fun displayMetricsH(){
        var displayMetrics: DisplayMetrics = DisplayMetrics()
        var windowManager = getWindowManager().defaultDisplay.getMetrics(displayMetrics)
            var displayheight: Int = displayMetrics.heightPixels
    }
 fun toInt = Int

    fun displayMetricsW(){
        var displayMetrics: DisplayMetrics = DisplayMetrics()
        var windowManager = getWindowManager().defaultDisplay.getMetrics(displayMetrics)
        var displayWidth: Int = displayMetrics.widthPixels
    }

    final CaptureRequest.Builder captureBuilder =
    mCameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_STILL_CAPTURE);
    captureBuilder.addTarget(mPreviewReader.getSurface());
    captureBuilder.set(CaptureRequest.CONTROL_AE_MODE, CaptureRequest.CONTROL_AE_MODE_ON);
    captureBuilder.set(CaptureRequest.JPEG_ORIENTATION, getOrientation(mOrientation));
    Log.d(TAG, "Capture request created.");
    mCaptureSession.capture(captureBuilder.build(), mCaptureCallback, mBackgroundHandler); */



}
