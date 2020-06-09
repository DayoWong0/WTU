package grade.xyj.com.component.groupmg

import android.app.Dialog
import android.os.Bundle
import android.os.Parcel
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import grade.xyj.com.R
import grade.xyj.com.base.BaseTitleActivity
import grade.xyj.com.classchedule.event.CourseDataChangeEvent
import grade.xyj.com.util.extend.defaultBus
import grade.xyj.com.util.extend.getViewModel
import grade.xyj.com.util.extend.toastInfo
import grade.xyj.com.util.extend.toastSuccess
import grade.xyj.com.widget.AppWidgetUtil
import grade.xyj.room.entity.GroupEntity
import grade.xyj.room.impl.GroupDaoImpl
import org.jetbrains.anko.*
import org.jetbrains.anko.recyclerview.v7.recyclerView

class ScheduleManageActivity : BaseTitleActivity() {

    override val title: String
        get() =  "课表管理"

    private lateinit var viewModel: ScheduleManageViewModel
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ScheduleManageAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = getViewModel()
        recyclerView = UI {
            recyclerView {
                layoutManager = LinearLayoutManager(this@ScheduleManageActivity)
                this@ScheduleManageActivity.adapter =
                    ScheduleManageAdapter(viewModel.groups).apply {
                        addFooterView(createFooterView())
                        setOnItemClickListener { adapter, view, position ->
                            val id = viewModel.groups[position].id

                            if (id == viewModel.cgId)
                                return@setOnItemClickListener
                            else
                                showSwitchDialog(id!!)
                        }
                        setOnItemChildClickListener { adapter, view, position ->
                            when (view.id) {
                                R.id.ib_delete -> toastInfo("长按删除键删除")
                                R.id.ib_edit -> {
                                    ModifyTableNameFragment.newInstance(object :
                                        ModifyTableNameFragment.TableNameChangeListener {
                                        override fun onFinish(editText: EditText, dialog: Dialog) {
                                            val name = editText.text.toString()
                                            if (name.isEmpty()) {
                                                toastInfo("名称不能为空哦")
                                            } else {
                                                val group =
                                                    GroupDaoImpl.getGroupByName(name).firstOrNull()
                                                if (group != null) {
                                                    toastInfo("课表名冲突!")
                                                } else {
                                                    dialog.cancel()
                                                    GroupDaoImpl.update(viewModel.groups[position].apply {
                                                        this.name = name
                                                    })
                                                    toastSuccess("修改成功")
                                                    adapter.notifyDataSetChanged()
                                                }
                                            }
                                        }

                                        override fun writeToParcel(dest: Parcel?, flags: Int) = Unit

                                        override fun describeContents(): Int = 0

                                    }).show(supportFragmentManager, "")
                                }
                            }
                        }
                        setOnItemChildLongClickListener { adapter, _, position ->
                            GroupDaoImpl.delete(viewModel.groups[position])
                            viewModel.groups.removeAt(position)
                            adapter.notifyDataSetChanged()
                            true
                        }
                    }

                adapter = this@ScheduleManageActivity.adapter
            }
        }.view as RecyclerView
        mContentView.addView(recyclerView)
    }

    private fun showSwitchDialog(id: Long) {
        alert("确认要切换到该课表吗?", "提示") {
            yesButton {
                viewModel.setId(id)
                adapter.cgId = id
                adapter.notifyDataSetChanged()

                AppWidgetUtil.updateWidget(this@ScheduleManageActivity)
                defaultBus.post(CourseDataChangeEvent)
                toastSuccess("切换成功")
            }
            cancelButton { }
        }.show()
    }


    private fun createFooterView(): View =
        layoutInflater.inflate(R.layout.item_add_course_btn, null).apply {
            find<TextView>(R.id.tv_add).run {
                text = "添加"
                setOnClickListener {
                    ModifyTableNameFragment.newInstance(object :
                        ModifyTableNameFragment.TableNameChangeListener {
                        override fun onFinish(editText: EditText, dialog: Dialog) {
                            val name = editText.text.toString()
                            if (name.isEmpty()) {
                                toastInfo("名称不能为空哦")
                            } else {
                                val group = GroupDaoImpl.getGroupByName(name).firstOrNull()
                                if (group != null) {
                                    toastInfo("课表名冲突!")
                                } else {
                                    GroupDaoImpl.insert(GroupEntity(name))
                                    toastSuccess("添加成功!")
                                    dialog.cancel()

                                    //刷新
                                    viewModel.groups.run {
                                        clear()
                                        addAll(GroupDaoImpl.getAll())
                                    }
                                    adapter.notifyDataSetChanged()

                                }
                            }
                        }

                        override fun writeToParcel(dest: Parcel?, flags: Int) = Unit

                        override fun describeContents(): Int = 0

                    }).show(supportFragmentManager, "")
                }
            }
        }
}