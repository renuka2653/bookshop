package com.example.bookshop.Comman;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatButton;

import com.example.bookshop.R;

public class NetworkChangeLister extends BroadcastReceiver
{

    @Override
    public void onReceive(Context context, Intent intent) {

        if (!NetworkDetails.isConnectedToInternet(context))
        {
            AlertDialog.Builder ad= new AlertDialog.Builder(context);
            View layout_dialog = LayoutInflater.from(context).inflate(R.layout.
                    check_internate_connection_dialog,null);
            //xml file store karte
            //layoutinflater layout la call karte
            //from(javaclass la call karto)
            //inflate (xml file path,null)
            ad.setView(layout_dialog);
            AppCompatButton btnRetry = layout_dialog.findViewById(R.id.btncheckinternateconnection);
            AlertDialog alertDialog = ad.create();
            alertDialog.show();
            alertDialog.setCanceledOnTouchOutside(false);

            btnRetry.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    alertDialog.dismiss();
                    onReceive(context,intent);

                }
            });
        }
        else {
            Toast.makeText(context, "Your Internate is Connected", Toast.LENGTH_SHORT).show();
        }
    }
}


//Activity =>
//Services =>long running operation background
//broadcast receiver => system communication and app cummunicate ex.batary low
//Content provider =>data pass,data share ex.pdf open karne