package grade.xyj.com.update

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class UpdateInfo(
    @SerializedName("version")
    val version: Int,
    @SerializedName("url")
    val url: String,
    @SerializedName("info")
    val info: String,
    @SerializedName("force")
    val force: Boolean
)