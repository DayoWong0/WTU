package grade.xyj.room.dao

import androidx.room.*
import grade.xyj.room.entity.GroupEntity

@Dao
interface GroupDao {

    @Query("select * from tb_group where id = :groupId")
    fun getGroupById(groupId: Long): GroupEntity?

    @Query("select * from tb_group where name = :groupName")
    fun getGroupByName(groupName: String): List<GroupEntity>

    @Query("select * from tb_group")
    fun getAll(): MutableList<GroupEntity>

    @Insert
    fun insert(groupEntity: GroupEntity): Long

    @Insert
    fun insert(groupEntities: List<GroupEntity>)

    @Update
    fun update(groupEntity: GroupEntity)

    @Delete
    fun delete(groupEntity: GroupEntity)
}