package grade.xyj.com.component.grade

import android.view.View
import android.widget.ArrayAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import grade.xyj.com.R
import grade.xyj.com.util.ShapeUtils
import grade.xyj.com.util.TimeUtils
import grade.xyj.com.util.extend.showProgressDialog
import org.jetbrains.anko.*
import org.jetbrains.anko.recyclerview.v7.recyclerView
import org.jetbrains.anko.sdk27.coroutines.onClick

object GradeUI{
    fun getView(fragment: GradeBaseFragment):View = with(fragment.context!!){
        val array = TimeUtils.getCurrentSchoolYearAndSemester()
        val yearArray = mutableListOf<String>().apply { for (i in array[0] downTo array[0] - 4) add(i.toString()) }
        val xqArray = listOf("全部","1","2")

        fun getXq(): String = when (fragment.xqSpinner.selectedItem.toString()) {
            "全部" -> ""
            "1" -> "3"
            else -> "12"
        }


        verticalLayout {
            linearLayout {
                fragment.xnSpinner = spinner {
                    adapter = ArrayAdapter(this@with, R.layout.item_spinner_qbank, R.id.qbank_tv, yearArray)
                }.lparams(dip(0), wrapContent){
                    weight = 1f
                }
                fragment.xqSpinner = spinner {
                    adapter = ArrayAdapter(this@with, R.layout.item_spinner_qbank, R.id.qbank_tv, xqArray)
                    setSelection(TimeUtils.getCurrentSchoolYearAndSemester()[1])
                }.lparams(dip(0), wrapContent){
                    weight = 1f
                }
                fragment.queryButton = button("查询") {
                    onClick {
                        isEnabled = false
                        fragment.dialog = fragment.showProgressDialog("查询中...")
                        fragment.viewModel.getGrades(getXq(),fragment.xnSpinner.selectedItem.toString())
                    }
                }.lparams(dip(0), matchParent){
                    weight = 1f
                }
            }
            fragment.recyclerView = recyclerView {
                layoutManager = LinearLayoutManager(context)
            }.lparams(matchParent, matchParent)
        }
    }




}