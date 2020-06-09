package grade.xyj.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import grade.xyj.room.entity.QuestionEntity

@Dao
interface QuestionkDao {

    @Insert
    fun insert(questionEntity: QuestionEntity)

    @Query("select * from tb_question where SUB = :sub and TOP like :top")
    fun query(sub:String,top:String):List<QuestionEntity>
}