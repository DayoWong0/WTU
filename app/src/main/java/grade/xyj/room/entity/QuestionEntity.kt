package grade.xyj.room.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tb_question")
class QuestionEntity (
    var SUB:String,
    var TYPE:String,
    var TOP:String,
    var OPTIONS:String,
    var RES:String,
    @PrimaryKey
    var _id:Long? = null
)