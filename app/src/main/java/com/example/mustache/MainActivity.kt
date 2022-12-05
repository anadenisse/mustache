package com.example.mustache

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.mustache.arface.FaceMask
import com.example.mustache.arface.FaceMaskElement
import com.example.mustache.rendering.AugmentedFaceFragment
import com.example.mustache.rendering.AugmentedFaceListener
import com.example.mustache.rendering.AugmentedFaceNode
import kotlinx.android.synthetic.main.activity_main.*
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
    }

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
        val redlandmarks = ArrayList<FaceMaskElement>()
        redlandmarks.add(
            FaceMaskElement(
                AugmentedFaceNode.Companion.FaceLandmark.MUSTACHE,
                "models/mustache_one.obj",
                "models/red_color.png"
            )
        )
        faceMasks.add(FaceMask("models/cafeclaro_color.png", redlandmarks))

        val blondelandmarks = ArrayList<FaceMaskElement>()
        blondelandmarks.add(
            FaceMaskElement(
                AugmentedFaceNode.Companion.FaceLandmark.MUSTACHE,
                "models/mustache_two.obj",
                "models/blonde_color.png"
            )
        )
        faceMasks.add(FaceMask("models/cafeclaro_color.png", blondelandmarks))

        val lightbrownlandmarks = ArrayList<FaceMaskElement>()
        lightbrownlandmarks.add(
            FaceMaskElement(
                AugmentedFaceNode.Companion.FaceLandmark.MUSTACHE,
                "models/mustache_three.obj",
                "models/cafeclaro_color.png"
            )
        )
        faceMasks.add(FaceMask("models/cafeclaro_color.png", lightbrownlandmarks))

        val brownlandmarks = ArrayList<FaceMaskElement>()
        brownlandmarks.add(
            FaceMaskElement(
                AugmentedFaceNode.Companion.FaceLandmark.MUSTACHE,
                "models/mustache_four.obj",
                "models/brown_color.png"
            )
        )

        faceMasks.add(FaceMask("models/cafeclaro_color.png", brownlandmarks))
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
}
