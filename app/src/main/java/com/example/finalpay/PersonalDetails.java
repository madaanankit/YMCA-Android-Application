package com.example.finalpay;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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


public class PersonalDetails extends Fragment {
    EditText edtName;
    EditText edtRno,edtAddress,edtCity,edtState,edtEmail,edtCno,edtFather,edtDBO,edtGender;
    boolean isnew = false;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v= inflater.inflate(R.layout.fragment_personal_details, container, false);

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("login", Context.MODE_PRIVATE);
        final String roll=sharedPreferences.getString("roll","");

        edtRno=v.findViewById(R.id.edtpdRno);
        edtName=v.findViewById(R.id.edtFName);
        edtAddress=v.findViewById(R.id.edtaddress);
        edtCity=v.findViewById(R.id.edtcity);
        edtState=v.findViewById(R.id.edtstate);
        edtEmail=v.findViewById(R.id.edtemail);
        edtCno=v.findViewById(R.id.edtCno);
        edtFather=v.findViewById(R.id.edtFatherName);
        edtDBO=v.findViewById(R.id.edtdob);
        edtGender=v.findViewById(R.id.edtGender);
        Button btnSubmit = v.findViewById(R.id.btnpdsubmit);

        fetch(roll);
        btnSubmit.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                update(roll);
            }
        });
        return v;
    }

    private void update(final String roll){
        final String Fname,Gender,Address,City,State,Email,Contact,FatherName,Dob;

        Fname=edtName.getText().toString();
        Gender=edtGender.getText().toString();
        Address=edtAddress.getText().toString();
        City=edtCity.getText().toString();
        State=edtState.getText().toString();
        Email=edtEmail.getText().toString();
        Contact=edtCno.getText().toString();
        FatherName=edtFather.getText().toString();
        Dob=edtDBO.getText().toString();

        if(Fname.isEmpty() ||Gender.isEmpty() ||Address.isEmpty() ||City.isEmpty() ||State.isEmpty() ||Email.isEmpty() ||Contact.isEmpty() ||
                FatherName.isEmpty() ||Dob.isEmpty()){
            Toast.makeText(getActivity(), "Field Empty!!", Toast.LENGTH_LONG).show();
            return;
        }

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        String url ="https://payymca.000webhostapp.com/updatepd.php";
        if(isnew){
            url ="https://payymca.000webhostapp.com/insertPD.php";
        }
        StringRequest request = new StringRequest(
                Request.Method.POST,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(getActivity(), response, Toast.LENGTH_SHORT).show();
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
            protected Map<String, String> getParams(){
                Map<String, String> map=new HashMap<>();
                map.put("Name",Fname);
                map.put("Gender",Gender.equals("Male")?"0":"1");
                map.put("Address",Address);
                map.put("City",City);
                map.put("State",State);
                map.put("Email",Email);
                map.put("Contactno",Contact);
                map.put("FMName",FatherName);
                map.put("Dateofbirth",Dob);
                map.put("Roll_No",roll);
                return map;
            }
        };
        requestQueue.add(request);
    }

    private void fetch(final String roll){
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        final String url ="https://payymca.000webhostapp.com/test.php";
        StringRequest request=new StringRequest(
                Request.Method.POST,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if(response.equalsIgnoreCase("No records found")){
                            Toast.makeText(getActivity(), "Enter Profile Details", Toast.LENGTH_LONG).show();
                            edtRno.setText(roll);
                            isnew = true;
                            return;
                        }
                        try{
                            JSONArray arr=new JSONArray(response);

                                JSONObject obj= arr.getJSONObject(0);
                                edtName.setText(obj.getString("Name"));
                                edtAddress.setText(obj.getString("Address"));
                                edtCity.setText(obj.getString("City"));
                                edtState.setText(obj.getString("State"));
                                edtEmail.setText(obj.getString("Email"));
                                edtCno.setText(obj.getString("Contactno"));
                                edtFather.setText(obj.getString("FMName"));
                                edtDBO.setText(obj.getString("Dateofbirth"));
                                String gender=obj.getString("Gender");
                                edtRno.setText(obj.getString("Roll_no"));
                                edtGender.setText(gender.equals("0")?"Male":"Female");
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
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map=new HashMap<>();
                map.put("Roll_No",roll);
                return map;
            }
        };
        requestQueue.add(request);
    }
}