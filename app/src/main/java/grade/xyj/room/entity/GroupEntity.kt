package grade.xyj.room.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tb_group")
class GroupEntity(
    var name: String,
    @PrimaryKey(autoGenerate = true)
    val id: Long? = null
)