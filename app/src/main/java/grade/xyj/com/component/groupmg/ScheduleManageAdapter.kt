package grade.xyj.com.component.groupmg

import android.graphics.Typeface
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import grade.xyj.com.R
import grade.xyj.com.util.Settings
import grade.xyj.room.entity.GroupEntity

class ScheduleManageAdapter(da: MutableList<GroupEntity>) : BaseQuickAdapter<GroupEntity, BaseViewHolder>(R.layout.item_table_list, da) {
    private val font: Typeface by lazy { Typeface.createFromAsset(mContext.assets, "fonts/iconfont.ttf") }
    var cgId = Settings.groupId
    private val ids = intArrayOf(R.id.ib_edit, R.id.ib_delete)
    override fun convert(helper: BaseViewHolder, item: GroupEntity) {
        helper.run {
            ids.forEach { getView<TextView>(it).typeface = font }
            setVisible(R.id.ib_delete, item.id != cgId)
            setVisible(R.id.tag_text,item.id == cgId)
            setText(R.id.tv_table_name, item.name)
            setImageResource(R.id.iv_pic, R.drawable.main_background_2019)
            addOnClickListener(R.id.ib_edit)
            addOnClickListener(R.id.ib_delete)
            addOnLongClickListener(R.id.ib_delete)
        }

    }

    fun update(id: Long) {
        cgId = id
        notifyDataSetChanged()
    }

}