package com.example.finalpay;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
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

import java.util.HashMap;
import java.util.Map;

public class OpportunityDetails extends Fragment {


    // TODO: Rename and change types of parameters
    TextView profName,salary,vacancies,compName,courseRequired;
    Button btnApply;
    String id;
    String ugcourse,pgcourse;


    public OpportunityDetails(String id) {
        // Required empty public constructor
        this.id=id;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_opportunity_details, container, false);

        SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences("login", Context.MODE_PRIVATE);
        final String roll=sharedPreferences.getString("roll","");

        profName=v.findViewById(R.id.profName);
        salary=v.findViewById(R.id.salary);
        vacancies=v.findViewById(R.id.vacancies);
        compName=v.findViewById(R.id.compName);
        courseRequired=v.findViewById(R.id.whoCanApply);

        btnApply=v.findViewById(R.id.btnApply);
        btnApply.setEnabled(false);
        btnApply.setText("Not Eligible");

        loadDetails(roll);
        checkApply(roll);

        btnApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                applyCourse(roll);
                btnApply.setEnabled(false);
            }
        });
        return v;
    }

    private void applyCourse(final String roll){
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        final String url = "https://payymca.000webhostapp.com/setApplication.php";
        StringRequest request = new StringRequest(
                Request.Method.POST,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(getActivity(), response, Toast.LENGTH_SHORT).show();
                        if(response.equalsIgnoreCase("Applied")){
                            btnApply.setText("Already Applied");
                        }else{
                            btnApply.setEnabled(true);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_SHORT).show();
                        btnApply.setEnabled(true);
                    }
                }
        ){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("Roll_No",roll);
                map.put("Id",id);
                return map;
            }
        };
        requestQueue.add(request);
    }

    private void checkApply(final String roll){
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        final String url = "https://payymca.000webhostapp.com/fetchCourse.php";
        StringRequest request = new StringRequest(
                Request.Method.POST,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray arr = new JSONArray(response);
                            JSONObject obj = arr.getJSONObject(0);
                            ugcourse = obj.getString("ugcourse");
                            pgcourse = obj.getString("pgcourse");
                            String[] parts = courseRequired.getText().toString().split(",");
                            for(int i=0;i<parts.length;i++) {

                                if (ugcourse.equalsIgnoreCase(parts[i].trim()) || pgcourse.equalsIgnoreCase(parts[i].trim()))
                                {
                                    btnApply.setText("Apply");
                                    checkAlreadyApplied(roll);
                                    return;
                                }
                            }
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
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("Roll_No",roll);
                return map;
            }
        };
        requestQueue.add(request);

    }

    private void checkAlreadyApplied(final String roll){
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        final String url ="https://payymca.000webhostapp.com/checkAlreadyApplied.php";
        StringRequest request = new StringRequest(
                Request.Method.POST,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.equalsIgnoreCase("Can Apply")) {
                            btnApply.setEnabled(true);
                        }
                        else{
                            btnApply.setText("Already Applied");
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
                map.put("Id",id);
                return map;
            }
        };
        requestQueue.add(request);
    }

    private void loadDetails(final String roll){
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        final String url ="https://payymca.000webhostapp.com/fetchOpportunity.php";
        StringRequest request = new StringRequest(
                Request.Method.POST,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            JSONArray arr= new JSONArray(response);
                            for(int i=0;i<arr.length();i++){
                                JSONObject obj = arr.getJSONObject(i);
                                profName.setText(obj.getString("Profile"));
                                salary.setText("Salary:"+obj.getString("package"));
                                vacancies.setText("Vacancies:"+obj.getString("Vacancies"));
                                compName.setText(obj.getString("CompanyName"));
                                courseRequired.setText(obj.getString("Course_required"));

                            }

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
                Map<String, String> map= new HashMap<>();
                map.put("Id",id);
                return map;
            }
        };
        requestQueue.add(request);
    }
}