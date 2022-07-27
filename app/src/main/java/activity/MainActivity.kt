package activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.FrameLayout
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.example.bookapp.*
import com.google.android.material.navigation.NavigationView as NavigationView


class MainActivity : AppCompatActivity() {
    lateinit var drawerLayout:DrawerLayout
    lateinit var coordinateLayout:CoordinatorLayout
    lateinit var toolbar:Toolbar
    lateinit var frameLayout:FrameLayout
    lateinit var navigationView: NavigationView
      var previousMenuItem :MenuItem? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        drawerLayout=findViewById(R.id.drawerLayout)
        coordinateLayout=findViewById(R.id.coordinateLayout)
        toolbar=findViewById(R.id.toolbar)
        frameLayout=findViewById(R.id.frameLayout)
        navigationView=findViewById(R.id.navigationView)
        setUpToolBar()
        openDashboard()

//make a toggle  and place it in action bar which is action bar toggle
        val actionBarDrawerToggle= ActionBarDrawerToggle(
            this@MainActivity,
            drawerLayout,
            R.string.open_drawer,
            R.string.close_drawer
        )
        //make homebutton functional
        //to make hamsburger clickable
        //to add clicklistener to toggle
        drawerLayout.addDrawerListener(actionBarDrawerToggle)
        //to change hamsburger icon to back icon and vice versa
        //sync the toggle
        actionBarDrawerToggle.syncState()
        //to add clicklistener to items of navigation bar



        navigationView.setNavigationItemSelectedListener{
            //code for checking and unchecking of menu item
            if(previousMenuItem!=null)
            {
                previousMenuItem?.isChecked=false
            }
            else
            {
                it.isCheckable=true
                it.isChecked=true
                previousMenuItem=it

            }
            when(it.itemId)

            {
                R.id.dashboard ->{
                    openDashboard()
                         drawerLayout.closeDrawers()

                }
                R.id.favourite ->{
                    supportFragmentManager.beginTransaction()
                        //here dashboardfragment replace the blank frame
                        .replace(R.id.frameLayout, fragment.FavouriteFragment())
                        //.addToBackStack("Favourite")
                        .commit() //to make change and start
                    drawerLayout.closeDrawers()
                    supportActionBar?.title="Favourite"
                }
                R.id.profile ->
                {
                   supportFragmentManager.beginTransaction()
                       //here dashboardfragment replace the blank frame
                       .replace(R.id.frameLayout, fragment.ProfileFragment())
                       //.addToBackStack("Profile")
                       .commit() //to make change and start
                    drawerLayout.closeDrawers()
                    supportActionBar?.title="Profile"
                }
                R.id.About ->
                {
                    supportFragmentManager.beginTransaction()
                        //here dashboardfragment replace the blank frame
                        .replace(R.id.frameLayout, fragment.AboutFragment())
                        //.addToBackStack("About")
                        .commit() //to make change and start
                    drawerLayout.closeDrawers()
                    supportActionBar?.title="About"
                }

            }

                return@setNavigationItemSelectedListener true
    }

    }
    fun setUpToolBar()
    {
        //setsupportactionbar is a method to set up action bar

        setSupportActionBar(toolbar)
        supportActionBar?.title="Book Hub"
        //enble homebutton
        supportActionBar?.setHomeButtonEnabled(true)
        //to display homebutton
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }
//to add action  click listener to action bar
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id=item.itemId //extracting id of item and store in id
        if(id==android.R.id.home)
        {
            //use open drawer to start from left
            drawerLayout.openDrawer(GravityCompat.START)
        }
        return super.onOptionsItemSelected(item)

    }
    //to make dashboard as default
    fun openDashboard()
    {
        val fragment= fragment.DashboardFragment()
        val transaction=supportFragmentManager.beginTransaction()
        transaction.replace(R.id.frameLayout,fragment)
        transaction.commit()
        supportActionBar?.title="Dashboard"
        navigationView.setCheckedItem(R.id.dashboard)

    }

    override fun onBackPressed() {
        val frag =supportFragmentManager.findFragmentById(R.id.frameLayout)
        when(frag) {
            !is fragment.DashboardFragment ->openDashboard()
            else->super.onBackPressed()
        }

    }

}
//steps to add hamsburger icon
//enable homebutton
//make toggle and place it in action bar
//add click listener on method using addDrawerListerner methos
//sync the toggle with navigation drawer
//use onOptionsItemSelected to add click listener to action bar
//use drawermethod to make drawer open from left side of app