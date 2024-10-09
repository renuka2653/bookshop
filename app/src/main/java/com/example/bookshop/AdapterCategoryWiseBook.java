package com.example.bookshop;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

public class AdapterCategoryWiseBook extends BaseAdapter {
    // BaseAdapter =multiple desing handle karte XML chi and data pass karto ->AdapterCategoryWiseBook
    //AdapterCategoryWiseBook= ha data pass karto list View la

    List<POJOCategoryWiseBook> list;
    Activity activity;

    public AdapterCategoryWiseBook(List<POJOCategoryWiseBook> pojoCategoryWiseBookList, Activity activity) {
        this.list = pojoCategoryWiseBookList;
        this.activity = activity;
    }

    @Override
    public int getCount() {
        //POJO class  chi Size return karte
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        //item chi position return karte

        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        //item ID chi position return karte
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
       final ViewHolder holder;
       final LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

       if (view == null)
       {
           holder=new ViewHolder();
           view= inflater.inflate(R.layout.activity_category_wise_book,null);
           holder.ivcatgoryWiseBookImage=view.findViewById(R.id.ivCategoryWiseBookImage);
           holder.tvShopName=view.findViewById(R.id.tvCategoryWiseBookShopName);
           holder.tvbookCatgory=view.findViewById(R.id.tvCategoryWiseBookBookCategory);
           holder.tvShopRating=view.findViewById(R.id.tvCategoryWiseBookShopRetaning);
           holder.tvBookName=view.findViewById(R.id.tvCategoryWiseBookBookName);
           holder.tvBookPrice=view.findViewById(R.id.tvCategoryWiseBookBookPrice);
           holder.tvBookOffer=view.findViewById(R.id.tvCategoryWiseBookBookOffer);

           view.setTag(holder);
       }
       else
       {
           holder=(ViewHolder) view.getTag();
       }

       final POJOCategoryWiseBook obj = list.get(position);
       holder.tvShopName.setText(obj.getShopname());
       holder.tvBookName.setText(obj.getBookname());
       holder.tvbookCatgory.setText(obj.getCategoryname());
       holder.tvBookPrice.setText(obj.getBookprice());
       holder.tvBookOffer.setText(obj.getBookprice());

        Glide.with(activity)
                .load("http://192.168.160.60:80/onlinebookshopdbAPI/images/"+
                        obj.getBookimage())
                .skipMemoryCache(true)
                .error(R.drawable.imagenotfound)
                .into(holder.ivcatgoryWiseBookImage);


        return view;
    }



  class ViewHolder {
        ImageView ivcatgoryWiseBookImage;
        TextView tvShopName,tvShopRating,tvBookName,tvbookCatgory,tvBookPrice,tvBookOffer;


  }


}
