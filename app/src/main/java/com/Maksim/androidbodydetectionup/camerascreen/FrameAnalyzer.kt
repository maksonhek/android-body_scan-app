package com.Maksim.androidbodydetectionup.camerascreen

import android.util.Size
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.pose.PoseDetection
import com.google.mlkit.vision.pose.defaults.PoseDetectorOptions
import java.lang.Integer.max
import java.lang.Integer.min

@ExperimentalGetImage class FrameAnalyzer(skeletonView: SkeletonView): ImageAnalysis.Analyzer {
    val options = PoseDetectorOptions.Builder()
        .setDetectorMode(PoseDetectorOptions.STREAM_MODE)
        .build()
    val detector = PoseDetection.getClient(options)
    val skeletonSecondView = skeletonView
    override fun analyze(image: ImageProxy) {
        val mediaImage = image.image
        if (mediaImage != null){
            var imageForDetector  = InputImage.fromMediaImage(mediaImage,image.imageInfo.rotationDegrees)
        detector.process(imageForDetector)
            .addOnSuccessListener {detectetPose ->
                val minSide = min(image.height,image.width)
                val maxSide = max(image.height,image.width)
                val targetSie = Size(minSide,maxSide)
                skeletonSecondView.parameters(detectetPose,targetSie)
                image.close()
        }
            .addOnFailureListener{
                println("error error error error error errrrrrrororeoreoreoroeeroeroreooer")

                image.close()
            }
        }
    }
}