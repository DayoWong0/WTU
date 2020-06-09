package grade.xyj.com.view.setting.binder

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.Typeface
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.blankj.utilcode.util.BarUtils
import grade.xyj.com.R
import grade.xyj.com.view.setting.bean.CategoryItem
import me.drakeet.multitype.ItemViewBinder
import org.jetbrains.anko.*

class CategoryItemViewBinder:ItemViewBinder<CategoryItem,CategoryItemViewBinder.ViewHolder>(){
    val color = R.color.red
    override fun onBindViewHolder(holder: ViewHolder, item: CategoryItem) {
        holder.tvCategory.text = item.name
        if (item.hasMarginTop) {
            holder.vTop.visibility = View.VISIBLE
        } else {
            holder.vTop.visibility = View.GONE
        }
    }

    @SuppressLint("ResourceType")
    override fun onCreateViewHolder(inflater: LayoutInflater, parent: ViewGroup): ViewHolder {
        val view = AnkoContext.create(parent.context).apply {
            verticalLayout {
                id = R.id.anko_layout
                view {
                    id = R.id.anko_view
                }.lparams(matchParent, BarUtils.getStatusBarHeight() + dip(48))

                linearLayout {
                    setPadding(dip(16), dip(2), dip(16), dip(2))
                    backgroundColorResource = color
                    textView {
                        id = R.id.anko_text_view
                        textColor = Color.WHITE
                        textSize = 12f
                        lines = 1
                        gravity = Gravity.CENTER_VERTICAL
                        typeface = Typeface.DEFAULT_BOLD
                    }.lparams(wrapContent, wrapContent)

                }.lparams(wrapContent, wrapContent) {
                    topMargin = dip(16)
                }
            }
        }.view
        return ViewHolder(view)
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tvCategory: TextView = itemView.find(R.id.anko_text_view)
        var vTop: View = itemView.find(R.id.anko_view)
    }
}