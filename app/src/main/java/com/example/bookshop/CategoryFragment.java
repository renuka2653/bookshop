package com.example.bookshop;

import android.os.Bundle;

import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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


public class CategoryFragment extends Fragment {
    SearchView searchCategory;
    ListView lvShowAllCategory;
    TextView tvNoCategoryAvailable;

   List<POJOGetCategoryDetails> pojoGetCategoryDetails;
   AdapterGetAllCategoryDetails adapterGetAllCategoryDetails;
//multiple data listview design madhe show karte

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_category, container, false);

        searchCategory=view.findViewById(R.id.svCategorySearchCategory);
        pojoGetCategoryDetails = new ArrayList<>();
        lvShowAllCategory=view.findViewById(R.id.lvCategoryFragmentShowMultipaleCategory);
        tvNoCategoryAvailable=view.findViewById(R.id.tvCategoryFragmentNoCategoryAvailable);

        //query = user ne type kelele book
        searchCategory.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchCategory(query);
                return false;
            }

            @Override    //type kelele word store karto
            public boolean onQueryTextChange(String query) {
                searchCategory(query);
                return false;
            }
        });

        getAllCategory();

        return view;
    }

    private void searchCategory(String query) {
        List<POJOGetCategoryDetails> tempCategory=new ArrayList<>();
        tempCategory.clear();

        for (POJOGetCategoryDetails obj:pojoGetCategoryDetails)
        {
            if (obj.getCategoryName().toLowerCase().contains(query.toUpperCase()))
            {
               tempCategory.add(obj);
            }
            else
            {
                tvNoCategoryAvailable.setVisibility(View.VISIBLE);
            }

            adapterGetAllCategoryDetails=new AdapterGetAllCategoryDetails(tempCategory,getActivity());
            lvShowAllCategory.setAdapter(adapterGetAllCategoryDetails);
        }


    }

    private void getAllCategory() {
        //Client _Server Communication means Passing Data over Network
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params=new RequestParams(); // put the Data AsyncHttpClient object name

        client.post(Urls.getAllCategoeyWebServicec,
                params,
                new JsonHttpResponseHandler()//web server kade jo pn response aahe to chake karto
                {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        super.onSuccess(statusCode, headers, response);

                        try {
                            JSONArray jsonArray= response.getJSONArray("getAllCategory");
                            if (jsonArray.isNull(0))
                            {
                                tvNoCategoryAvailable.setVisibility(View.VISIBLE);
                            }

                            for (int i=0;i<jsonArray.length();i++)
                            {
                                JSONObject jsonObject= jsonArray.getJSONObject(i);
                                String strid=jsonObject.getString("id");
                                String strCategoryImage=jsonObject.getString("categoryimage");
                                String strCategoryName = jsonObject.getString("categoryname");

                                pojoGetCategoryDetails.add(new POJOGetCategoryDetails(strid,strCategoryImage,
                                        strCategoryName));
                            }

                             adapterGetAllCategoryDetails=new AdapterGetAllCategoryDetails(pojoGetCategoryDetails,
                                     getActivity());

                            lvShowAllCategory.setAdapter(adapterGetAllCategoryDetails);

                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }


                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        super.onFailure(statusCode, headers, throwable, errorResponse);
                        Toast.makeText(getActivity(), "Server Error", Toast.LENGTH_SHORT).show();
                    }
                }
                );

    }
}