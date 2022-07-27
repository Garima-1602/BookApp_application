package activity

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.room.Room
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.bookapp.R
import com.squareup.picasso.Picasso
import database.BookDatabase
import database.BookEntity
import org.json.JSONException
import org.json.JSONObject
import util.ConnectionManager
import java.util.HashMap

class DescriptionActivity : AppCompatActivity() {
    lateinit var imagebook:ImageView
    lateinit var txtBookName:TextView
    lateinit var txtBookAuthor:TextView
    lateinit var txtBookPrice:TextView
    lateinit var txtBookRating:TextView
    lateinit var txtDescription:TextView
    lateinit var progresslayout:RelativeLayout
    lateinit var progbar: ProgressBar
    lateinit var btnfav:Button
    lateinit var toolbar:Toolbar
    var bookId:String?="100"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_description)
        imagebook=findViewById(R.id.imagebook)
        txtBookName=findViewById(R.id.txtBookName)
        txtBookAuthor=findViewById(R.id.txtBookAuthor)
        txtBookPrice=findViewById(R.id.txtBookPrice)
        txtBookRating=findViewById(R.id.txtBookRating)
        txtDescription=findViewById(R.id.txtDescription)
        progresslayout=findViewById(R.id.progresslayout)
        progresslayout.visibility= View.VISIBLE
        progbar=findViewById(R.id.progbar)
        progbar.visibility=View.VISIBLE
        toolbar=findViewById(R.id.toolbar)
        btnfav=findViewById(R.id.btnfav)
        setSupportActionBar(toolbar)
        supportActionBar?.title="Book Details"

        if(intent!=null)
        {
           bookId= intent.getStringExtra("book_id")
        }
        else
        {
            finish()
            Toast.makeText(this@DescriptionActivity,"Some unexpected error has occured",Toast.LENGTH_SHORT).show()
        }
        if(bookId=="100")
        {
            finish()
            Toast.makeText(this@DescriptionActivity,"Some unexpected error has occured",Toast.LENGTH_SHORT).show()
        }
        val queue= Volley.newRequestQueue(this@DescriptionActivity)
        val url="http://13.235.250.119/v1/book/get_book/"
        val jsonParams=JSONObject()
        jsonParams.put("book_id",bookId)
        if(ConnectionManager().checkConnectivity(this@DescriptionActivity)){
            val jsonRequest=object:JsonObjectRequest(Method.POST,url,jsonParams,Response.Listener {
                try{
                    val success=it.getBoolean("success")
                    if(success) {
                        val bookJsonObject = it.getJSONObject("book_data")
                        progresslayout.visibility = View.GONE
                        val bookImageUrl = bookJsonObject.getString("image")
                        Picasso.get().load(bookJsonObject.getString("image"))
                            .error(R.drawable.default_book_cover).into(imagebook)
                        txtBookName.text = bookJsonObject.getString("name")
                        txtBookAuthor.text = bookJsonObject.getString("author")
                        txtBookPrice.text = bookJsonObject.getString("price")
                        txtBookRating.text = bookJsonObject.getString("rating")
                        txtDescription.text = bookJsonObject.getString("description")
                        val bookEntity = BookEntity(
                            bookId?.toInt() as Int,
                            txtBookName.text.toString(),
                            txtBookAuthor.text.toString(),
                            txtBookPrice.text.toString(),
                            txtBookRating.text.toString(),
                            txtDescription.text.toString(),
                            bookImageUrl
                        )
                        val checkFav = DBAsyncTask(applicationContext, bookEntity, 1).execute()
                        val isFav = checkFav.get() //to check book is added to fav
                        if (isFav) {
                            btnfav.text = "Remove From Favourites"
                            val favColor =
                                ContextCompat.getColor(applicationContext, R.color.colorAccent)
                            btnfav.setBackgroundColor(favColor)
                        } else
                        {
                            btnfav.text = "Add to Favourites"
                            val nofavColor =
                                ContextCompat.getColor(applicationContext, R.color.colorPrimary)
                            btnfav.setBackgroundColor(nofavColor)
                        }
                        btnfav.setOnClickListener(){
                            //check if book is present in db or not
                            if(!DBAsyncTask(applicationContext, bookEntity, 1).execute().get())
                            //this block will exceute when the book is not favourite
                            {
                                  //to save into favourite we will execute mode 2
                                Log.d("bookss",bookEntity.toString())
                                val async=DBAsyncTask(applicationContext, bookEntity, 2).execute()
                                val result=async.get()
                                if(result)
                                {
                                    Toast.makeText(
                                        this@DescriptionActivity,
                                        "Book added to favourites",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    btnfav.text="Remove from favourites"
                                    val favColor =
                                        ContextCompat.getColor(applicationContext, R.color.colorAccent)
                                    btnfav.setBackgroundColor(favColor)
                                }
                                else
                                {
                                    Toast.makeText(
                                        this@DescriptionActivity,
                                        "Some error has occured",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }

                            }
                            else
                            {
                                val async=DBAsyncTask(applicationContext, bookEntity, 3).execute()
                                val result=async.get()
                                if(result)
                                {
                                    Toast.makeText(
                                        this@DescriptionActivity,
                                        "Book removed from favourites",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    btnfav.text="Add to favourites"
                                    val nofavColor =
                                        ContextCompat.getColor(applicationContext, R.color.colorPrimary)
                                    btnfav.setBackgroundColor(nofavColor)
                                }
                                else
                                {
                                    Toast.makeText(
                                        this@DescriptionActivity,
                                        "Some error has occured",
                                        Toast.LENGTH_SHORT
                                    ).show()

                                }
                            }
                        }

                    } else {
                        Toast.makeText(
                            this@DescriptionActivity,
                            "Some unexpected error has occured",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }catch(e:JSONException){
                    Toast.makeText(this@DescriptionActivity,"Some unexpected error has occured",Toast.LENGTH_SHORT).show()
                }
            },Response.ErrorListener {
                Toast.makeText(this@DescriptionActivity,"Volley error occured $it",Toast.LENGTH_SHORT).show()
            })
            {
                override fun getHeaders(): MutableMap<String, String> {
                    val headers=HashMap<String,String>()
                    headers["Content-type"]="application/json"
                    headers["token"]="2334db3a8fd667"
                    return headers
                }
            }
            queue.add(jsonRequest)
        }
        else
        {
            val dialog= AlertDialog.Builder(this@DescriptionActivity)
            dialog.setTitle("Error")
            dialog.setMessage("Internet Connection not Found")
            dialog.setPositiveButton("Open Settings"){text,listener->
                val settingsIntent= Intent(Settings.ACTION_WIRELESS_SETTINGS)
                startActivity(settingsIntent)
                finish()
            }
            dialog.setNegativeButton("Exit"){text,listener->
                ActivityCompat.finishAffinity(this@DescriptionActivity)//closes all running instances of app
            }
            dialog.create()
            dialog.show()
        }

    }
    //to perform some data operations this class need context
    class DBAsyncTask(val context: Context,val bookEntity: BookEntity,val mode:Int): AsyncTask<Void, Void, Boolean>(){
        /*
        1>check DB if the book is fav or not
        2>Save the book into DB as fav
        3>Remove the fav book
         */
        val db= Room.databaseBuilder(context,BookDatabase::class.java,"books-db").build()
        override fun doInBackground(vararg p0: Void?): Boolean {
            Log.d("books",bookEntity.toString())
            when(mode){
                1->{
                            //check db if the book is fav or not
                    val book:BookEntity?=db.bookDao().getBookById(bookEntity.book_id.toString())
                    db.close()
                    return book!=null
                }
                2->{
                    //save the book into db as fav
                    db.bookDao().insertBook(bookEntity)
                    db.close()
                    val db= Room.databaseBuilder(context,BookDatabase::class.java,"books-db").build()
                    var d= db.bookDao().getAllBooks()
                    Log.d("datass", d.toString())
                    return true

                }
                3->{
                          //remove the fav book
                    db.bookDao().deleteBook(bookEntity)
                    db.close()
                    return true
                }
            }
           return false
        }

    }
}