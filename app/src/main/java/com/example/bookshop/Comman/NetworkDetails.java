package com.example.bookshop.Comman;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

//this java class is used to check network connect or not
public class NetworkDetails {
    public static boolean isConnectedToInternet(Context context)
    {
                                                   //type cast
        ConnectivityManager connectivityManager = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connectivityManager != null)
        {
            NetworkInfo[] networkInfos  = connectivityManager.getAllNetworkInfo();
           if (networkInfos != null)
           {
               for (int i= 0; i<networkInfos.length ;i++)
               {
                   if (networkInfos[i].getState()== NetworkInfo.State.CONNECTED)
                   {
                       return  true;
                   }
               }
           }
        }
        return false;
    }
}
