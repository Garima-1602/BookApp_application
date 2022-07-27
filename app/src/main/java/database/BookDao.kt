package database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface BookDao {
    //to store books in favourite fragment
    @Insert
    fun insertBook(bookEntity: BookEntity)

    @Delete
    fun deleteBook(bookEntity: BookEntity)

    @Query("Select * FROM books")
    fun getAllBooks():List<BookEntity>

    @Query("Select * from books WHERE book_id=:bookId")
    fun getBookById(bookId:String):BookEntity

}