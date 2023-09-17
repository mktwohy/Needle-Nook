package com.mktwohy.needlenook

import androidx.room.Dao
import androidx.room.Database
import androidx.room.Delete
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.RoomDatabase
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Database(entities = [Project::class], version = 1)
abstract class ProjectDatabase : RoomDatabase() {
    abstract val projectDao: ProjectDao
}

@Dao
interface ProjectDao {
    @Insert
    suspend fun insertProject(project: Project)

    @Update
    suspend fun updateProject(project: Project)

    @Delete
    suspend fun deleteProject(project: Project)

    @Query("SELECT * FROM project")
    fun getProjects(): Flow<List<Project>>

    @Query("SELECT * FROM project ORDER BY name ASC")
    fun getProjectsOrderedByName(): Flow<List<Project>>
}

// TODO add date created
@Entity
data class Project(
    val name: String,
    val stitchCount: Int = 0,
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0
)