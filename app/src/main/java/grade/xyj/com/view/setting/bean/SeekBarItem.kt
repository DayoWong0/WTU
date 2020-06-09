package grade.xyj.com.view.setting.bean

data class SeekBarItem(
        val title: String,
        var valueInt: Int,
        val min: Int,
        val max: Int,
        val unit: String)