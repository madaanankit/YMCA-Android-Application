package com.example.finalpay;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

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

public class Opportunities extends Fragment {
    ListView listoppo;
    SimpleAdapter simpleAdapter;
    ArrayList<HashMap<String, String>> data;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_opportunities, container, false);

        listoppo = v.findViewById(R.id.listoppo);

        data = new ArrayList<>();

        simpleAdapter = new SimpleAdapter(getActivity(),data,R.layout.opportunity_layout,new String[]{"CompanyName","Profile"},
                new int[]{R.id.orgName,R.id.oppProfile});

        listoppo.setAdapter(simpleAdapter);

        showOpportunities();

        listoppo.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                HashMap<String, String> map = data.get(position);
                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.nav_host_fragment,new OpportunityDetails(map.get("Id")));
                ft.commit();
            }
        });
        return v;
    }

    private void showOpportunities(){
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        final String url ="https://payymca.000webhostapp.com/fetchOpportunities.php";
        StringRequest request=new StringRequest(
                Request.Method.POST,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //Toast.makeText(getActivity(), response, Toast.LENGTH_SHORT).show();
                        try{
                            JSONArray arr = new JSONArray(response);
                            for(int i=0;i<arr.length();i++) {
                                JSONObject obj = arr.getJSONObject(i);
                                HashMap<String, String> map = new HashMap<>();
                                map.put("CompanyName",obj.getString("CompanyName"));
                                map.put("Profile",obj.getString("Profile"));
                                map.put("Id",obj.getString("Vacancy_id"));
                                data.add(map);
                            }
                            simpleAdapter.notifyDataSetChanged();
                        }
                        catch(JSONException j){
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
        );
        requestQueue.add(request);
    }
}