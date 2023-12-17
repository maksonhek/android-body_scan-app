package com.Maksim.androidbodydetectionup.camerascreen

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Paint.ANTI_ALIAS_FLAG
import android.graphics.Point
import android.graphics.PointF
import android.util.AttributeSet
import android.util.Size
import android.view.View
import com.google.mlkit.vision.common.PointF3D
import com.google.mlkit.vision.pose.Pose
import com.google.mlkit.vision.pose.PoseLandmark


class SkeletonView(
    context: Context,
    attributes: AttributeSet
): View(context,attributes) {
    private val mainPaint = Paint(ANTI_ALIAS_FLAG)
    private var sizeImage:Size=Size(0,0)
    private var viewSize:Size=Size(0,0)
    private var poseDetector:Pose? = null
    init {
        mainPaint.color = Color.WHITE
        mainPaint.strokeWidth = 40.0F
        mainPaint.style = Paint.Style.FILL
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        viewSize=Size(w,h)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        drawLandmarks(canvas)
        drawLines(canvas)
    }

    fun parameters(pose: Pose,size:Size){
        sizeImage=size
        poseDetector=pose
        invalidate()
    }

    private fun drawLandmarks(canvas: Canvas){
       drawLandmark(canvas,poseDetector?.getPoseLandmark(PoseLandmark.NOSE))
       drawLandmark(canvas,poseDetector?.getPoseLandmark(PoseLandmark.RIGHT_EYE))
    }
    private fun drawLandmark(canvas: Canvas,poseLandmark: PoseLandmark?){
        if(poseLandmark!=null){
            val newPoint = convertPosition(poseLandmark.position3D)
            canvas.drawCircle(newPoint.x,newPoint.y,15F,mainPaint)
        }
    }
    private fun drawLines(canvas:Canvas){
        val firstLandmark = poseDetector?.getPoseLandmark(PoseLandmark.LEFT_ELBOW)
        val secondLandmark = poseDetector?.getPoseLandmark(PoseLandmark.LEFT_WRIST)
        drawLine(canvas,firstLandmark,secondLandmark)
    }

    private fun drawLine(canvas: Canvas,firstLandmark:PoseLandmark?,secondLandmark: PoseLandmark?){
        if(firstLandmark != null && secondLandmark !=null){
            val firstPoint: PointF =convertPosition(firstLandmark.position3D)
            val secondPoint: PointF =convertPosition((secondLandmark.position3D))
            canvas.drawLine(firstPoint.x,firstPoint.y,secondPoint.x,secondPoint.y,mainPaint)
        }
    }



    private fun convertPosition(startingPoint:PointF3D):PointF{
        val x1 = startingPoint.x
        val y1 = startingPoint.y
        val w1 = sizeImage.width
        val h1 = sizeImage.height
        val w2 = viewSize.width
        val h2 =viewSize.height

        val x2 = x1*w2/w1
        val y2 = y1*h2/h1
        return PointF(x2,y2)
    }
}