package grade.xyj.room.impl

import grade.xyj.room.DataBaseManager
import grade.xyj.room.dao.QuestionkDao
import grade.xyj.room.entity.QuestionEntity

object QuestionDaoImpl :QuestionkDao{

    private val dao = DataBaseManager.questionDao

    override fun insert(questionEntity: QuestionEntity) {
        dao.insert(questionEntity)
    }

    override fun query(sub: String, top: String):List<QuestionEntity>{
        return dao.query(sub, top)
    }

}