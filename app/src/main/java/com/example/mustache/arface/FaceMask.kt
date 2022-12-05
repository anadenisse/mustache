package com.example.mustache.arface

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.mustache.R
import com.example.mustache.rendering.AugmentedFaceNode
import java.util.*

data class FaceMaskElement(val landmark: AugmentedFaceNode.Companion.FaceLandmark,
                            val model: String,
                            val texture: String)

data class FaceMask(val faceTexture: String?,
                    val landmarkMap: ArrayList<FaceMaskElement>?)



