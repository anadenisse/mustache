package com.example.mustache

import android.app.Activity
import android.app.Instrumentation
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.media.Image
import android.opengl.GLSurfaceView
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.HandlerThread
import android.provider.MediaStore
import android.util.DisplayMetrics
import android.util.Log
import android.view.PixelCopy
import android.view.SurfaceView
import android.view.View
import android.view.WindowManager
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
import java.util.logging.Handler

/*import android.R*/

class MainActivity : AppCompatActivity(), AugmentedFaceListener {

    private var faceMasks = ArrayList<FaceMask>()
    private var currentMaskIndex: Int = 0
    private var updateMask: Boolean = false
    private var surfaceView: GLSurfaceView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        (face_view as AugmentedFaceFragment).setAugmentedFaceListener(this)
        initMasks()
        change_btn.setOnClickListener { nextMask() }
        /* shoot_pic.setOnClickListener {
            getBitmapFromView()
            usePixelCopy(surfaceView: SurfaceView; callback: (Bitmap?) -> Unit))
        }  */


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

        val test = ArrayList<FaceMaskElement>()
        test.add(
            FaceMaskElement(
                AugmentedFaceNode.Companion.FaceLandmark.GLASSES,
                "models/ar.obj",
                "models/brown_color.png"
            )
        )
        faceMasks.add(FaceMask("models/invisiblemask.png", test))
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

    fun getBitmapFromView(view: View): Bitmap? {
        var bitmap =
            Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        view.draw(canvas)
        return bitmap
    }

    fun usePixelCopy(surfaceView: SurfaceView, callback: (Bitmap?) -> Unit) {
        val bitmap: Bitmap = Bitmap.createBitmap(
            surfaceView.width,
            surfaceView.height,
            Bitmap.Config.ARGB_8888
        );
        try {
            // Create a handler thread to offload the processing of the image.
            val handlerThread = HandlerThread("PixelCopier");
            handlerThread.start();
            PixelCopy.request(
                surfaceView, bitmap,
                PixelCopy.OnPixelCopyFinishedListener { copyResult ->
                    if (copyResult == PixelCopy.SUCCESS) {
                        callback(bitmap)
                    }
                    handlerThread.quitSafely();
                },
                android.os.Handler(handlerThread.looper)
            )
        } catch (e: IllegalArgumentException) {
            callback(null)
            // PixelCopy may throw IllegalArgumentException, make sure to handle it
            e.printStackTrace()
        }

    }
}


/*

    surfaceView

    convertir displaymetrics(unit) en int y continuar tratando de tomar la foto con ar on */

fun requestpicture() {
    var displayMetrics: DisplayMetrics = DisplayMetrics()
    var displayheight: Int = displayMetrics.heightPixels
    var displayWidth: Int = displayMetrics.widthPixels
    val ARGB_8888: Bitmap.Config = Bitmap.Config.ARGB_8888
    createBitmap(width = displayWidth, height = displayheight, Bitmap.Config.ARGB_8888)
}
/**


 */

