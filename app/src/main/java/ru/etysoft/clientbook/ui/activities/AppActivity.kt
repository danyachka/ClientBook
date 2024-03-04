package ru.etysoft.clientbook.ui.activities

import android.animation.ValueAnimator
import android.view.View
import android.view.animation.Animation
import android.view.animation.DecelerateInterpolator
import android.view.animation.ScaleAnimation
import androidx.appcompat.app.AppCompatActivity
import java.util.concurrent.Executors

open class AppActivity: AppCompatActivity() {

    companion object {
        private val staticThreadPoolExecutor = Executors.newFixedThreadPool(8)
    }

    fun runBackground(runnable: Runnable?) {
        staticThreadPoolExecutor.execute(runnable)
    }

    fun performScaleAnimation(fromScale: Float, toScale: Float, view: View): ScaleAnimation? {
        val scaleOut = ScaleAnimation(fromScale, toScale,
                fromScale, toScale, Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f)
        scaleOut.duration = 200
        scaleOut.interpolator = DecelerateInterpolator(2f)
        scaleOut.fillAfter = true
        view.startAnimation(scaleOut)
        return scaleOut
    }

    fun performHeightAnimation(fromHeight: Int, toHeight: Int, view: View): ValueAnimator? {
        val animator = ValueAnimator.ofInt(fromHeight, toHeight)
        animator.addUpdateListener { valueAnimator ->
            val value = valueAnimator.animatedValue as Int
            val params = view.layoutParams
            params.height = value
            view.layoutParams = params
        }
        animator.interpolator = DecelerateInterpolator(2f)
        animator.duration = 150
        animator.start()
        return animator
    }

}