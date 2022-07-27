package fragment

import adapter.DashboardRecyclerAdapter
import android.app.Activity
import android.app.AlertDialog
import android.app.DownloadManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.Volley
import com.example.bookapp.R
import model.Book
import org.json.JSONException
import util.ConnectionManager
import com.android.volley.toolbox.JsonObjectRequest as JsonObjectRequest1

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [DashboardFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class DashboardFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }
       lateinit var Recyclerview:RecyclerView
       lateinit var layoutManager: RecyclerView.LayoutManager

       lateinit var progressLayout:RelativeLayout
       lateinit var progressBar:ProgressBar

    lateinit var recyclerAdapter:DashboardRecyclerAdapter
    val bookInfoList = arrayListOf<Book>()
    /*
    val bookInfoList= arrayListOf<Book>(
        Book("P.S. I love You", "Cecelia Ahern", "Rs. 299", "4.5", R.drawable.ps_ily),
        Book("The Great Gatsby", "F. Scott Fitzgerald", "Rs. 399", "4.1", R.drawable.great_gatsby),
        Book("Anna Karenina", "Leo Tolstoy", "Rs. 199", "4.3", R.drawable.anna_kare),
        Book("Madame Bovary", "Gustave Flaubert", "Rs. 500", "4.0", R.drawable.madame),
        Book("War and Peace", "Leo Tolstoy", "Rs. 249", "4.8", R.drawable.war_and_peace),
        Book("Lolita", "Vladimir Nabokov", "Rs. 349", "3.9", R.drawable.lolita),
        Book("Middlemarch", "George Eliot", "Rs. 599", "4.2", R.drawable.middlemarch),
        Book("The Adventures of Huckleberry Finn", "Mark Twain", "Rs. 699", "4.5", R.drawable.adventures_finn),
        Book("Moby-Dick", "Herman Melville", "Rs. 499", "4.5", R.drawable.moby_dick),
        Book("The Lord of the Rings", "J.R.R Tolkien", "Rs. 749", "5.0", R.drawable.lord_of_rings)

    ) */
    override fun onCreateView(//here we use layoutinflater instead of setcontentview
        //layoutinflater converts the complete layout into view and then we return in the method
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view=inflater.inflate(R.layout.fragment_dashboard, container, false)

        progressLayout=view.findViewById(R.id.progressLayout)
        progressBar=view.findViewById(R.id.progressBar)
        progressLayout.visibility=View.VISIBLE

        Recyclerview=view.findViewById(R.id.Recyclerview)
        layoutManager=LinearLayoutManager(activity)

          val queue= Volley.newRequestQueue(activity as Context) //to manage queue of request
        val url="http://13.235.250.119/v1/book/fetch_books/"
        if(ConnectionManager().checkConnectivity(activity as Context))
        {
            val jsonObjectRequest=object: JsonObjectRequest1(Request.Method.GET,url,null, Response.Listener{
                val success=it.getBoolean("success")
                try {
                    progressLayout.visibility=View.GONE //hide progress layout
                    //handling json exception which occur due to some server services
                    if (success) {
                        val data = it.getJSONArray("data")
                        for (i in 0 until data.length()) {
                            val bookJsonObject = data.getJSONObject(i) //extract object
                            val bookObject = Book(
                                bookJsonObject.getString("book_id"),
                                bookJsonObject.getString("name"),
                                bookJsonObject.getString("author"),
                                bookJsonObject.getString("rating"),
                                bookJsonObject.getString("price"),
                                bookJsonObject.getString("image")

                            )
                            bookInfoList.add(bookObject)
                            recyclerAdapter =
                                DashboardRecyclerAdapter(activity as Context, bookInfoList)
                            Recyclerview.adapter = recyclerAdapter
                            Recyclerview.layoutManager = layoutManager
                            /*Recyclerview.addItemDecoration(
                                DividerItemDecoration(
                                    Recyclerview.context,
                                    (layoutManager as LinearLayoutManager).orientation
                                )
                            )*/
                        }

                    } else {
                        Toast.makeText(activity as Context, "Error occured", Toast.LENGTH_SHORT)
                            .show()
                    }
                }
                catch(e:JSONException)
                {
                    Toast.makeText(activity as Context, "Some error has Occured", Toast.LENGTH_SHORT)
                        .show()

                }


            },Response.ErrorListener {
                //println("Error is $it ")
                Toast.makeText(activity as Context, "Volley error  Occured", Toast.LENGTH_SHORT)
                    .show()
            })
            {
                //headers tell us the type of content sent to and received from API
                override fun getHeaders(): MutableMap<String, String> {
                    val headers =HashMap<String, String>()
                    headers["content->type"]="application/json"
                    headers["token"]="2334db3a8fd667"

                    return headers
                }
            }
            queue.add(jsonObjectRequest)
        }
        else
        {
            val dialog=AlertDialog.Builder(activity as Context)
            dialog.setTitle("Error")
            dialog.setMessage("Internet Connection not Found")
            dialog.setPositiveButton("Open Settings"){text,listener->
                val settingsIntent= Intent(Settings.ACTION_WIRELESS_SETTINGS)
                startActivity(settingsIntent)
                activity?.finish()
            }
            dialog.setNegativeButton("Exit"){text,listener->
                ActivityCompat.finishAffinity(activity as Activity)//closes all running instances of app
            }
            dialog.create()
            dialog.show()

        }

        return view //this view is parent view of fragment
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment DashboardFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            DashboardFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}