package database

import androidx.room.Database
import androidx.room.RoomDatabase
//all the functions performed on data are performed by dao interface
//there is no default implementation of abstract class
@Database(entities=[BookEntity::class],version=1)
abstract class BookDatabase :RoomDatabase(){
    abstract fun bookDao():BookDao
}