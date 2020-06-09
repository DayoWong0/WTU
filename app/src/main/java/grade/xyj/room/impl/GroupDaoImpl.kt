package grade.xyj.room.impl

import grade.xyj.room.DataBaseManager
import grade.xyj.room.dao.GroupDao
import grade.xyj.room.entity.GroupEntity

object GroupDaoImpl : GroupDao {

    private val dao = DataBaseManager.groupDao

    override fun getGroupById(groupId: Long): GroupEntity? =
        dao.getGroupById(groupId)

    override fun getGroupByName(groupName: String): List<GroupEntity> =
        dao.getGroupByName(groupName)

    override fun getAll(): MutableList<GroupEntity> {
        return dao.getAll()
    }

    override fun insert(groupEntity: GroupEntity): Long =
        dao.insert(groupEntity)

    override fun insert(groupEntities: List<GroupEntity>) {
        dao.insert(groupEntities)
    }

    override fun update(groupEntity: GroupEntity) =
        dao.update(groupEntity)

    override fun delete(groupEntity: GroupEntity) =
        dao.delete(groupEntity)

}