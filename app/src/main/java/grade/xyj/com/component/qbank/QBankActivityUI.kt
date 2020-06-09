package grade.xyj.com.component.qbank

import android.graphics.Color
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import androidx.recyclerview.widget.LinearLayoutManager
import com.blankj.utilcode.util.BarUtils
import grade.xyj.com.R
import grade.xyj.com.util.ShapeUtils
import grade.xyj.room.entity.QuestionEntity
import org.jetbrains.anko.*
import org.jetbrains.anko.recyclerview.v7.recyclerView
import org.jetbrains.anko.sdk27.coroutines.onClick
import org.jetbrains.anko.sdk27.coroutines.onItemSelectedListener
import org.jetbrains.anko.sdk27.coroutines.textChangedListener

class QBankActivityUI : AnkoComponent<WorkActivity> {
    lateinit var keyEdit:EditText
    lateinit var spinner:Spinner
    override fun createView(ui: AnkoContext<WorkActivity>): View = ui.apply {
        verticalLayout {
            linearLayout {
                gravity = Gravity.CENTER_VERTICAL
                id = R.id.anko_layout
                topPadding = BarUtils.getStatusBarHeight()
                backgroundColor = Color.WHITE
                val outValue = TypedValue()
                context.theme.resolveAttribute(R.attr.selectableItemBackgroundBorderless, outValue, true)

                imageButton(R.drawable.ic_arrow_back_black_24dp) {
                    backgroundResource = outValue.resourceId
                    setColorFilter(Color.BLACK)
                    padding = dip(8)
                    onClick {
                        owner.onBackPressed()
                    }
                }.lparams(wrapContent, dip(48))

                spinner = spinner {
                    this.adapter = ArrayAdapter<String>(owner, R.layout.item_spinner_qbank, R.id.qbank_tv, arrayOf("毛概", "近代史", "马克思", "思修"))
                    onItemSelectedListener {
                        onItemSelected { _, _, _, _ ->
                            owner.viewModel.query(getSub(selectedItem.toString()),keyEdit.text.toString())
                        }
                    }
                }
                linearLayout {
                    background = ShapeUtils.radius("#eeeeee",18)
                    imageView {
                        imageResource = R.drawable.ic_search_white_20dp
                        setColorFilter(Color.BLACK)
                    }.lparams {
                        gravity = Gravity.CENTER
                        marginStart = dip(10)
                    }
                    keyEdit = editText {
                        background = null
                        textSize = 14f
                        hint = "在这里输入关键字"
                        textChangedListener {
                            afterTextChanged {
                                owner.viewModel.query(getSub(spinner.selectedItem.toString()),text.toString())
                            }
                        }
                    }.lparams(matchParent, wrapContent ){
                        marginStart = dip(10)
                        gravity = Gravity.CENTER
                    }
                }.lparams(matchParent, dip(38)) {
                    marginEnd = dip(88)
                }


            }.lparams(matchParent, wrapContent)
            owner.recyclerView = recyclerView {
                owner.adapter = QBankAdapter().apply {
                    setOnItemClickListener { adapter, _, position ->
                        (adapter.data[position] as QuestionEntity).run {
                            if(TYPE != "pd") owner.showQbankDialog(this)
                        }
                    }
                }
                layoutManager = LinearLayoutManager(owner)
                adapter = owner.adapter
            }.lparams(matchParent, matchParent)
        }
    }.view

    private fun getSub(t:String):String = when(t){
        "毛概"->"mg"
        "近代史"->"jds"
        "马克思"->"mks"
        else->"sx"
    }
}