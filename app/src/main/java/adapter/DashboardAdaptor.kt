package adapter

import activity.DescriptionActivity
import android.content.Context
import android.content.Intent
import android.os.Parcel
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.bookapp.R
import com.squareup.picasso.Picasso
import model.Book

//construction two primary constructors
//adaptor class has viewholder inside it and viewholder is also a class
class DashboardRecyclerAdapter(val context: Context,val itemList:ArrayList<Book>) :RecyclerView.Adapter<DashboardRecyclerAdapter.DashboardViewHolder>(){
    class DashboardViewHolder (view: View):RecyclerView.ViewHolder(view) {

        val txtBookName:TextView=view.findViewById(R.id.txtBookname)
        val txtBookAuthor:TextView=view.findViewById(R.id.txtBookAuthor)
        val txtBookRating:TextView=view.findViewById(R.id.txtBookRating)
        val txtBookPrice:TextView=view.findViewById(R.id.txtBookPrice)
        val imgBook: ImageView =view.findViewById(R.id.imgBook)
        val l1content:LinearLayout=view.findViewById(R.id.l1content)


    }
//these methods are responsible for connceting the adaptor to the list
    override fun onCreateViewHolder(//responsible for creating 10 viewHolders
        parent: ViewGroup,
        viewType: Int
    ): DashboardRecyclerAdapter.DashboardViewHolder {
      val view=LayoutInflater.from(parent.context).inflate(R.layout.recycler_dashboard_single,parent,false)
    return DashboardViewHolder(view)
    }

//this method is responsible for recycling and reusing of viewholder
    override fun onBindViewHolder(
        holder: DashboardRecyclerAdapter.DashboardViewHolder,
        position: Int
    ) {
        val book=itemList[position]
    holder.txtBookName.text=book.bookName
    holder.txtBookAuthor.text=book.bookAuthor
    holder.txtBookPrice.text=book.bookPrice
    holder.txtBookRating.text=book.bookRating
    //holder.imgBook.setImageResource(book.bookImage)
    //when image not properly fetch through picassio then default image will be taken
    Picasso.get().load(book.bookImage).error(R.drawable.default_book_cover).into(holder.imgBook);
    holder.l1content.setOnClickListener(){
        val intent= Intent(context,DescriptionActivity::class.java)
        intent.putExtra("book_id",book.bookId)
            //everymethod related to current activity can be taken using context in adapter
        context.startActivity(intent)
    }

    }

    //store number of count of list statically we know that
    override fun getItemCount(): Int {
       return itemList.size
    }
}