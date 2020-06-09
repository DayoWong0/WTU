package grade.xyj.com.component.grade

import android.graphics.Color
import android.widget.ImageView
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.chad.library.adapter.base.entity.AbstractExpandableItem
import com.chad.library.adapter.base.entity.MultiItemEntity
import grade.xyj.com.R
import grade.xyj.com.bean.NGradeBean
import grade.xyj.com.util.Util
import grade.xyj.com.util.extend.getColorByCompat
import org.jetbrains.anko.imageResource

class GradeAdapter :
    BaseMultiItemQuickAdapter<MultiItemEntity, BaseViewHolder>(mutableListOf()) {
    init {
        addItemType(0, R.layout.item_level1)
        addItemType(1, R.layout.item_level2)
    }

    private val passColor by lazy { mContext.getColorByCompat(R.color.dark_text_color) }

    override fun convert(helper: BaseViewHolder, item: MultiItemEntity) {
        helper.run {
            when (item.itemType) {
                0 -> {
                    val ite = item as Level0Item
                    setText(R.id.textView6, ite.name)
                    setTextColor(R.id.textView6, if (ite.isPass) passColor else Color.RED)
                    /*setImageResource(
                        R.id.imageView,
                        if (ite.isExpanded) R.drawable.ic_keyboard_arrow_up_black_24dp else R.drawable.ic_keyboard_arrow_down_black_24dp
                    )*/
                    getView<ImageView>(R.id.imageView).run {
                        imageResource =
                            if (ite.isExpanded) R.drawable.ic_keyboard_arrow_up_black_24dp else R.drawable.ic_keyboard_arrow_down_black_24dp
                        setColorFilter(mContext.getColorByCompat(R.color.dark_text_color))
                    }
                }
                else -> {
                    val ite = item as Level1Item
                    setText(R.id.textView7, ite.info)
                }
            }
        }
    }

}

class Level0Item(bean: NGradeBean) : AbstractExpandableItem<Level1Item>(), MultiItemEntity {

    val name: String = bean.name + ":" + bean.cj
    val isPass = Util.isPass(bean.cj)

    init {
        addSubItem(Level1Item(bean))
    }

    override fun getLevel(): Int = 0
    override fun getItemType(): Int = 0
}

class Level1Item(bean: NGradeBean) : MultiItemEntity {


    val info = buildString {
        append("学期:${bean.xq}   学分:${bean.xf}\n\n")
        append("开课学院:${bean.itemsBean.kkbmmc}\n\n")
        bean.list.forEach {
            append("${it.type}:${it.cj}")
            if (bean.list.last() != it)
                append("\n\n")
            else
                append("\n")
        }
    }

    override fun getItemType(): Int = 1
}