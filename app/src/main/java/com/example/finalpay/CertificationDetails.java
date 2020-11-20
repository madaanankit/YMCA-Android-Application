package com.example.finalpay;

import android.content.Context;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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


public class CertificationDetails extends Fragment {
    EditText edtCertOrgName,edtCertName,edtCertIssueDate;
    Button btnUpdate,btnDelete;
    String title;
    TextView goback;
    public CertificationDetails(String title){
        this.title=title;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v =inflater.inflate(R.layout.fragment_certification_details, container, false);

        edtCertName=v.findViewById(R.id.edtcertname);
        edtCertOrgName=v.findViewById(R.id.edtorganization);
        edtCertIssueDate=v.findViewById(R.id.edtissuedate);

        SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences("login", Context.MODE_PRIVATE);
        final String roll=sharedPreferences.getString("roll","");

        btnDelete=v.findViewById(R.id.btndeletecertificate);
        btnUpdate=v.findViewById(R.id.btnupdatecertificate);

        goback=v.findViewById(R.id.goback);

        loadCertificate(roll);

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateCertificate(roll);
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteCertificate(roll);
            }
        });

        goback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.nav_host_fragment,new Certifications());
                ft.commit();
            }
        });
        //Toast.makeText(getActivity(), title, Toast.LENGTH_SHORT).show();
        return v;
    }

    private void deleteCertificate(final String roll){
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        final String url ="https://payymca.000webhostapp.com/deleteCertificate.php";

        StringRequest request= new StringRequest(
                Request.Method.POST,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(getActivity(), response, Toast.LENGTH_SHORT).show();
                        if (response.equals("Deleted")){
                            FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                            ft.replace(R.id.nav_host_fragment,new Certifications());
                            ft.commit();
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
                Map<String, String> map=new HashMap<>();
                map.put("Roll_No",roll);
                map.put("title",title);
                return map;
            }
        };
        requestQueue.add(request);

    }

    private void loadCertificate(final String roll){

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        final String url ="https://payymca.000webhostapp.com/fetchCertificate.php";
        StringRequest request= new StringRequest(
                Request.Method.POST,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray arr = new JSONArray(response);
                            JSONObject obj= (JSONObject) arr.get(0);
                            edtCertName.setText(obj.getString("Ctitle"));
                            edtCertOrgName.setText(obj.getString("Organization"));
                            edtCertIssueDate.setText(obj.getString("Idate"));
                        }catch (JSONException j){
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
                Map<String, String> map=new HashMap<>();
                map.put("Roll_No",roll);
                map.put("title",title);
                return map;
            }
        };
        requestQueue.add(request);
    }

    private void updateCertificate(final String roll){
        final String certName,certOrganization,certIssueDate;

        certName=edtCertName.getText().toString();
        certOrganization=edtCertOrgName.getText().toString();
        certIssueDate=edtCertIssueDate.getText().toString();

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        final String url ="https://payymca.000webhostapp.com/updateCertificate.php";
        StringRequest request= new StringRequest(
                Request.Method.POST,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(getActivity(), response, Toast.LENGTH_SHORT).show();
                        if(response.equals("Updated")){
                            title=certName;
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
                Map<String, String> map= new HashMap<>();
                map.put("Roll_No",roll);
                map.put("title",certName);
                map.put("oldTitle",title);
                map.put("Organization",certOrganization);
                map.put("IssueDate",certIssueDate);
                return map;
            }
        };


        requestQueue.add(request);

    }
}