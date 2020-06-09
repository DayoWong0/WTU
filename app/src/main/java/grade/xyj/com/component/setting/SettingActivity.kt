package grade.xyj.com.component.setting

import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import androidx.recyclerview.widget.LinearLayoutManager
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.lifecycle.lifecycleOwner
import com.afollestad.materialdialogs.list.listItems
import com.afollestad.materialdialogs.list.listItemsSingleChoice
import com.google.android.material.dialog.MaterialDialogs
import grade.xyj.com.base.BaseTitleActivity
import grade.xyj.com.util.Settings
import grade.xyj.com.view.setting.bean.HorizontalItem
import grade.xyj.com.view.setting.bean.SwitchItem
import grade.xyj.com.view.setting.binder.HorizontalItemViewBinder
import grade.xyj.com.view.setting.binder.SwitchItemViewBinder
import me.drakeet.multitype.MultiTypeAdapter
import org.jetbrains.anko.UI
import org.jetbrains.anko.recyclerview.v7.recyclerView

class SettingActivity : BaseTitleActivity() {

    override val title: String
        get() =  "设置"


    private val mAdapter: MultiTypeAdapter = MultiTypeAdapter().apply {
        register(
            SwitchItem::class,
            SwitchItemViewBinder { item, isCheck -> onSwitchItemCheckChange(item, isCheck) })
    }
    private val items = listOf<Any>(
        SwitchItem("将成绩查询设置为首页", !Settings.courseFirst),
        SwitchItem("夜间模式", Settings.nightMode)
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mAdapter.items = items

        mContentView.addView(UI {
            recyclerView {
                layoutManager = LinearLayoutManager(this@SettingActivity)
                adapter = mAdapter
            }
        }.view)
    }

    private fun onSwitchItemCheckChange(item: SwitchItem, isChecked: Boolean) {

        when (item.title) {
            "将成绩查询设置为首页" -> Settings.courseFirst = !isChecked
            "夜间模式"->{
                Settings.nightMode = isChecked
                AppCompatDelegate.setDefaultNightMode(if (isChecked) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO)
            }
        }
        item.checked = isChecked
    }
}