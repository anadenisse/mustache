package com.example.mustache.rendering

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.mustache.R
import com.example.mustache.arface.FaceMaskElement
import com.google.ar.core.AugmentedFace
import com.google.ar.core.Pose
import com.google.ar.core.TrackingState

class AugmentedFaceNode(val augmentedFace: AugmentedFace?, val context: Context) {

    private val augmentedFaceRenderer = AugmentedFaceRenderer()
    private val faceLandmarks = HashMap<FaceLandmark, FaceRegion>()
    private var renderFaceMesh: Boolean = false

    companion object {
        enum class FaceLandmark {
            MUSTACHE,
            GLASSES
        }
    }

    init {
        renderFaceMesh = false
        augmentedFaceRenderer.setMaterialProperties(0.0f, 1.0f, 0.1f, 6.0f)
    }

    fun clearLandmarks() {
        faceLandmarks.clear()
    }

    fun setRegionModel(faceLandmark: FaceLandmark, modelName: String, modelTexture: String) {
        val faceRegion = FaceRegion(faceLandmark)
        faceRegion.setRenderable(context, modelName, modelTexture)
        faceLandmarks[faceLandmark] = faceRegion

    }

    fun setFaceMeshTexture(assetName: String) {
        augmentedFaceRenderer.createOnGlThread(context, assetName)
        renderFaceMesh = true
    }

    fun onDraw(projectionMatrix: FloatArray, viewMatrix: FloatArray, colorCorrectionRgba: FloatArray) {
        augmentedFace?.let { face ->
            if (face.trackingState != TrackingState.TRACKING) {
                return
            }

            if (renderFaceMesh) {
                val modelMatrix = FloatArray(16)
                face.centerPose.toMatrix(modelMatrix, 0)
                augmentedFaceRenderer.draw(
                    projectionMatrix, viewMatrix, modelMatrix, colorCorrectionRgba, face
                )
            }

            for (region in faceLandmarks.values) {
                val objectMatrix = FloatArray(16)
                getRegionPose(region.faceLandmark)?.toMatrix(objectMatrix, 0)
                region.draw(objectMatrix, viewMatrix, projectionMatrix, colorCorrectionRgba)
            }
    }

}

    private fun getRegionPose(faceLandmark: FaceLandmark): Pose? {
        return when (faceLandmark) {
            FaceLandmark.MUSTACHE -> getLandmarkPose(0)
            FaceLandmark.GLASSES -> getLandmarkPose(197)
        }
    }
    private fun getLandmarkPose(vertexIndex: Int) : Pose? {
        val centerPose = augmentedFace?.centerPose
        val buffer = augmentedFace?.meshVertices
        return buffer?.let { vertices ->
            centerPose?.compose(Pose.makeTranslation(vertices.get(vertexIndex * 3),
                vertices.get(vertexIndex * 3 + 1),
                vertices.get(vertexIndex * 3 + 2)))
        }
    }

}