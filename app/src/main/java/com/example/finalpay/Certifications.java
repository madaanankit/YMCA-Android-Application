package com.example.finalpay;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
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

public class Certifications extends Fragment implements AdapterView.OnItemClickListener {
    EditText edtCname,edtOrg,edtDate;
    Button btnAdd;
    ListView certList;
    ArrayList<String> listData;
    ArrayAdapter<String> arrayAdapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_certifications, container, false);

        SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences("login", Context.MODE_PRIVATE);
        final String roll=sharedPreferences.getString("roll","");

        listData= new ArrayList<>();
        arrayAdapter = new ArrayAdapter<String>(getActivity(), R.layout.certificate_layout, R.id.cert1, listData);

        certList=v.findViewById(R.id.listCert);
        certList.setAdapter(arrayAdapter);

        edtCname=v.findViewById(R.id.cname);
        edtOrg=v.findViewById(R.id.org);
        edtDate=v.findViewById(R.id.cdate);

        certList=v.findViewById(R.id.listCert);

        btnAdd=v.findViewById(R.id.add);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                add(roll);
            }
        });
        loadCert(roll);

        certList.setOnItemClickListener(this);

        return v;
    }

    private void loadCert(final String roll){

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        final String url ="https://payymca.000webhostapp.com/loadCertification.php";
        StringRequest request = new StringRequest(
                Request.Method.POST,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if(response.equalsIgnoreCase("No Record Found")){
                            Toast.makeText(getActivity(), "Add Certificates", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        try {
                            JSONArray arr= new JSONArray(response);
                            listData.clear();
                            for(int i=0;i<arr.length();i++){
                                JSONObject obj= arr.getJSONObject(i);
                                listData.add(obj.getString("Ctitle"));
                            }
                            arrayAdapter.notifyDataSetChanged();
                        } catch (JSONException j) {
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
                Map<String, String> map= new HashMap<>();
                map.put("Roll_No",roll);
                return map;
            }
        };
        requestQueue.add(request);
    }


    private void add(final String roll){

        final String cName,org,date;

        cName=edtCname.getText().toString();
        org=edtOrg.getText().toString();
        date=edtDate.getText().toString();

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        final String url ="https://payymca.000webhostapp.com/addCertification.php";
        StringRequest request=new StringRequest(
                Request.Method.POST,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(getActivity(), response, Toast.LENGTH_SHORT).show();
                        loadCert(roll);
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
                Map<String, String> map=new HashMap<>();

                map.put("Roll_No",roll);
                map.put("Ctitle",cName);
                map.put("Organization",org);
                map.put("Idate",date);

                return map;
            }
        };
        requestQueue.add(request);

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        //Intent intent = new Intent(getActivity(), CertificationDetails.class);
        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.nav_host_fragment,new CertificationDetails(listData.get(position)));
        ft.commit();
        //startActivity(intent);
        //Toast.makeText(getActivity(), "Item Clicked"+listData.get(position), Toast.LENGTH_SHORT).show();
    }
}