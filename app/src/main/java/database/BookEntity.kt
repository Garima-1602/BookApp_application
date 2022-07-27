package database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
//to add different tables or multiple tables we need to create multiple entities
@Entity(tableName="books") //annotation
data class BookEntity (
    @PrimaryKey val book_id:Int,
    @ColumnInfo(name = "book_name")val bookName :String,
    @ColumnInfo(name = "book_author")val bookAuthor:String,
    @ColumnInfo(name = "book_price")val bookPrice:String,
    @ColumnInfo(name = "book_rating")val bookRating:String,
    @ColumnInfo(name = "book_desc")val bookDesc:String,
    @ColumnInfo(name = "book_image")val bookImage:String
        )//each table has a primary key