package com.example.finalpay;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Applications extends Fragment {

    ListView listView;
    SimpleAdapter simpleAdapter;
    ArrayList<HashMap<String, String>> applications;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_applications, container, false);

        SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences("login", Context.MODE_PRIVATE);
        final String roll=sharedPreferences.getString("roll","");

        applications=new ArrayList<>();
        listView=v.findViewById(R.id.list_applications);
        simpleAdapter=new SimpleAdapter(getActivity(),applications,R.layout.applications_layout,new String[]{"CompanyName","Profile","Status"},
                new int[]{R.id.layoutCompName,R.id.layoutProfName,R.id.status});

        listView.setAdapter(simpleAdapter);

        loadData(roll);

        return v;
    }
    private void loadData(final String roll){
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        final String url ="https://payymca.000webhostapp.com/fetchApplications.php";
        StringRequest request = new StringRequest(
                Request.Method.POST,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if(response.equalsIgnoreCase("No Record Found")){
                            Toast.makeText(getActivity(), "No Applications Found", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        try{
                            JSONArray arr = new JSONArray(response);
                            for(int i=0;i<arr.length();i++){
                                JSONObject obj = arr.getJSONObject(i);
                                HashMap<String, String> map = new HashMap<>();
                                map.put("CompanyName",obj.getString("CompanyName"));
                                map.put("Profile",obj.getString("Profile"));
                                map.put("Status",obj.getString("Status"));
                                applications.add(map);
                            }
                            simpleAdapter.notifyDataSetChanged();
                        }
                        catch (JSONException j){
                            Toast.makeText(getActivity(), j.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
        ){
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> map = new HashMap<>();
                map.put("Roll_No",roll);
                return map;
            }
        };
        requestQueue.add(request);
    }
}