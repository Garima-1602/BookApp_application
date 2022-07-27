package util

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo

class ConnectionManager {
    fun checkConnectivity(context: Context):Boolean{
        //this gives us information about currently active network
    val connectivityManager=context.getSystemService(Context.CONNECTIVITY_SERVICE)as ConnectivityManager
        val activeNetwork:NetworkInfo?=connectivityManager.activeNetworkInfo
        if(activeNetwork?.isConnected!=null)
        {
            return activeNetwork.isConnected
        }
        else
        {
            return false
        }
    }
}
//FOR INTERNET CONNCETION
//GIVE PERMISIIONS IN MANIFEST FILE
//create class connection manager
//create function which return boolean for checking connectivty and state
//tocall that func craete button in dashboard fragment.xml
//initiliase button and create alert dialog box