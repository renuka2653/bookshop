package com.example.bookshop;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.bookshop.Comman.Urls;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class CategoryWiseBookActivity extends AppCompatActivity {
    SearchView searchCategoryWiseBook;
    ListView lvCategoryWiseBook;
    TextView tvNoCategoryBookAvailable;
    String strCategoryName;
    List<POJOCategoryWiseBook> pojoCategoryWiseBookList;
    AdapterCategoryWiseBook adapterCategoryWiseBook;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_category_wise_book);
        searchCategoryWiseBook = findViewById(R.id.svCategoryWiseBookSearchBook);
        lvCategoryWiseBook = findViewById(R.id.lvCategoryWiseListBook);
        tvNoCategoryBookAvailable = findViewById(R.id.tvCategoryWiseBookNoBookAvailable);

        pojoCategoryWiseBookList=new ArrayList<>();

        strCategoryName = getIntent().getStringExtra("categoryname");

        getCategoryWiseBookList();

        searchCategoryWiseBook.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchBookByCategory(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                searchBookByCategory(query);
                return false;
            }
        });

    }

    private void searchBookByCategory(String query) {
        List<POJOCategoryWiseBook> tempList = new ArrayList<>();
        tempList.clear();

        for (POJOCategoryWiseBook obj:pojoCategoryWiseBookList) {
            if (obj.getCategoryname().toUpperCase().contains(query.toUpperCase()) ||
                    obj.getBookname().toUpperCase().contains(query.toUpperCase()) ||
                    obj.getShopname().toUpperCase().contains(query.toUpperCase()) ||
                    obj.getBookprice().toUpperCase().contains(query.toUpperCase()) ||
                    obj.getBooklanguage().toUpperCase().contains(query.toUpperCase())) {
                tempList.add(obj);
            }
        }
            adapterCategoryWiseBook = new AdapterCategoryWiseBook(tempList,this);
            lvCategoryWiseBook.setAdapter(adapterCategoryWiseBook);



    }

    private void getCategoryWiseBookList() {
        //client-server Communication karte
        AsyncHttpClient client=new AsyncHttpClient();
        RequestParams params=new RequestParams();//put the data

        params.put("categoryname",strCategoryName);

        client.post(Urls.getCategoryWiseBook,
                params,new JsonHttpResponseHandler()
                {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        super.onSuccess(statusCode, headers, response);
                        try {
                            JSONArray jsonArray = response.getJSONArray("getCategoryWiseBook");
                            if (jsonArray.isNull(0)) {
                                lvCategoryWiseBook.setVisibility(View.GONE);
                                tvNoCategoryBookAvailable.setVisibility(View.VISIBLE);
                            }

                            for (int i=0;i<jsonArray.length();i++)
                            {
                                JSONObject jsonObject=jsonArray.getJSONObject(i);
                                String strId=jsonObject.getString("id");
                                String strCategoryName=jsonObject.getString("categoryname");
                                String strShopName=jsonObject.getString("shopname");
                                String strBookLanguage=jsonObject.getString("booklanguage");
                                String strBookImage=jsonObject.getString("bookimage");
                                String strBookName=jsonObject.getString("bookname");
                                String strBookPrice=jsonObject.getString("bookprice");
                                String strBookOffer=jsonObject.getString("bookoffer");

                                pojoCategoryWiseBookList.add(new POJOCategoryWiseBook(strId,strCategoryName,
                                        strShopName,strBookLanguage,strBookImage,strBookName,strBookPrice,strBookOffer));


                            }

                            adapterCategoryWiseBook=new AdapterCategoryWiseBook(pojoCategoryWiseBookList,
                                    CategoryWiseBookActivity.this);

                            lvCategoryWiseBook.setAdapter(adapterCategoryWiseBook);



                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        super.onFailure(statusCode, headers, throwable, errorResponse);
                        Toast.makeText(CategoryWiseBookActivity.this, "Server Error",
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }
}