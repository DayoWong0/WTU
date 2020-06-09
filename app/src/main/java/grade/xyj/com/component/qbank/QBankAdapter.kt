package grade.xyj.com.component.qbank

import android.view.View
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.haozhang.lib.SlantedTextView
import grade.xyj.com.R
import grade.xyj.com.util.extend.getColorByCompat
import grade.xyj.room.entity.QuestionEntity
import org.jetbrains.anko.textColorResource

class QBankAdapter :BaseQuickAdapter<QuestionEntity, BaseViewHolder>(R.layout.item_qbank, null){
    override fun convert(helper: BaseViewHolder, item: QuestionEntity) {
        helper.run {
            getView<SlantedTextView>(R.id.textView5).run {
                text = when(item.TYPE){"ddx"->"多选";"dx"->"单选";else->"判断" }
                this.setSlantedBackgroundColor(context.getColorByCompat(when(item.TYPE){"ddx"->R.color.blue;"dx"->R.color.pink;else->R.color.green }))
            }
            setText(R.id.textView8,item.TOP)
            getView<TextView>(R.id.textView9).run {
                if(item.TYPE != "pd") visibility = View.GONE
                else{
                    visibility = View.VISIBLE
                    text = if(item.RES == "Y") "正确" else "错误"
                    textColorResource = if(item.RES == "Y") R.color.md_green_600 else R.color.md_red_700
                }
            }
        }
    }
}