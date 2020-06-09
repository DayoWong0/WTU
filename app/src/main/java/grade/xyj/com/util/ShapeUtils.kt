package grade.xyj.com.util

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.Drawable
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import com.noober.background.drawable.DrawableCreator
import grade.xyj.com.App
import grade.xyj.com.util.extend.getColorByCompat
import org.jetbrains.anko.dip

object ShapeUtils {
    fun getDrawable(@DrawableRes id:Int) = ContextCompat.getDrawable(App.instance, id)

    fun radius(@ColorRes colorId:Int, dpValue:Int,context: Context):Drawable = DrawableCreator.Builder()
            .setSolidColor(context.getColorByCompat(colorId))
            .setCornersRadius(dip(dpValue))
            .build()

    fun radius(color:String, dpValue:Int):Drawable = DrawableCreator.Builder()
            .setSolidColor(Color.parseColor(color))
            .setCornersRadius(dip(dpValue))
            .build()

    fun courseItemBg(bgColor:Int, lineColor:Int, lineWidth:Int, alphaValue:Int):Drawable = DrawableCreator.Builder()
            .setSolidColor(bgColor)
            .setStrokeColor(lineColor)
            .setStrokeWidth(lineWidth.toFloat())
            .setCornersRadius(dip(4))
            .build().apply { this.alpha = alphaValue }

    private fun dip(dp:Int) = App.instance.dip(dp).toFloat()
}