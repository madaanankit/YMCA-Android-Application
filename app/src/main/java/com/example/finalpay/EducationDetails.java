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

public class EducationDetails extends Fragment {
    EditText edt10,edt10pass,edt12,edt12pass,edt12Stream,edtUgcourse,edtUgmarks,edtUgpass,edtPgcourse,edtPgpass,edtPgmarks,edtBack,edtGapreason,edtGap;
    Button btnSubmit;
    boolean isnew = false;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v =inflater.inflate(R.layout.fragment_education_details, container, false);

        edt10=v.findViewById(R.id.edt10marks);
        edt10pass=v.findViewById(R.id.edt10passyear);
        edt12=v.findViewById(R.id.edt12marks);
        edt12pass=v.findViewById(R.id.edt12passyear);
        edt12Stream=v.findViewById(R.id.edt12stream);
        edtUgcourse=v.findViewById(R.id.edtugcourse);
        edtUgmarks=v.findViewById(R.id.edtgradmarks);
        edtUgpass=v.findViewById(R.id.edtgradpassyear);
        edtPgcourse=v.findViewById(R.id.edtpgcourse);
        edtPgpass=v.findViewById(R.id.edtpgpassyear);
        edtPgmarks=v.findViewById(R.id.pgcgpa);
        edtBack=v.findViewById(R.id.edtbacklogs);
        edtGapreason=v.findViewById(R.id.edtgapreason);
        edtGap=v.findViewById(R.id.edtgap);

        btnSubmit=v.findViewById(R.id.btnsbmt);

        SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences("login", Context.MODE_PRIVATE);
        final String roll=sharedPreferences.getString("roll","");

        loadEducation(roll);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                update(roll);
            }
        });

        return v;
    }

    private void update(final String roll){
        final String tenth,tPass,twelve,twPass,twStream,ugCourse,ugMarks,ugPass,pgCourse,pgPass,pgMarks,back,gapReason,gap;

        tenth=edt10.getText().toString();
        tPass=edt10pass.getText().toString();
        twelve=edt12.getText().toString();
        twPass=edt12pass.getText().toString();
        twStream=edt12Stream.getText().toString();
        ugCourse=edtUgcourse.getText().toString();
        ugMarks=edtUgmarks.getText().toString();
        ugPass=edtUgpass.getText().toString();
        pgCourse=edtPgcourse.getText().toString();
        pgPass=edtPgpass.getText().toString();
        pgMarks=edtPgmarks.getText().toString();
        back=edtBack.getText().toString();
        gapReason=edtGapreason.getText().toString();
        gap=edtGap.getText().toString();

        if(tenth.isEmpty() || tPass.isEmpty() || twelve.isEmpty() || twPass.isEmpty() || twStream.isEmpty() || ugCourse.isEmpty() ||
                ugMarks.isEmpty() || ugPass.isEmpty() || pgCourse.isEmpty() || pgPass.isEmpty() || pgMarks.isEmpty() ||
                back.isEmpty() || gap.isEmpty()){

            Toast.makeText(getActivity(), "Field Empty!!", Toast.LENGTH_LONG).show();
            return;
        }

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        String url ="https://payymca.000webhostapp.com/updateeducation.php";
        if(isnew){
            url ="https://payymca.000webhostapp.com/insertED.php";
        }
        StringRequest request= new StringRequest(
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
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map=new HashMap<>();

                map.put("marks10",tenth);
                map.put("passyear10",tPass);
                map.put("marks12",twelve);
                map.put("passyear12",twPass);
                map.put("stream12",twStream);
                map.put("ugcourse",ugCourse);
                map.put("ugmarks",ugMarks);
                map.put("ugpass",ugPass);
                map.put("pgcourse",pgCourse);
                map.put("pgpass",pgPass);
                map.put("pgmarks",pgMarks);
                map.put("backlogs",back);
                map.put("gapreason",gapReason);
                map.put("gapyear",gap);
                map.put("Roll_No",roll);

                return map;
            }
        };
        requestQueue.add(request);

    }
    private void loadEducation(final String roll){
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        final String url ="https://payymca.000webhostapp.com/edudetails.php";
        StringRequest request=new StringRequest(
                Request.Method.POST,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if(response.equalsIgnoreCase("Record not Found")){
                            Toast.makeText(getActivity(), "Enter Education Details", Toast.LENGTH_LONG).show();
                            isnew = true;
                            return;
                        }
                        try{
                            JSONArray arr=new JSONArray(response);
                            for(int i=0;i<arr.length();i++) {
                                JSONObject obj= arr.getJSONObject(i);
                                edt10.setText(obj.getString("marks10"));
                                edt10pass.setText(obj.getString("passyear10"));

                                edt12.setText(obj.getString("marks12"));
                                edt12pass.setText(obj.getString("passyear12"));
                                edt12Stream.setText(obj.getString("stream12"));

                                edtUgcourse.setText(obj.getString("ugcourse"));
                                edtUgmarks.setText(obj.getString("ugmarks"));
                                edtUgpass.setText(obj.getString("ugpass"));

                                edtPgcourse.setText(obj.getString("pgcourse"));
                                edtPgpass.setText(obj.getString("pgpass"));
                                edtPgmarks.setText(obj.getString("pgmarks"));

                                edtBack.setText(obj.getString("backlogs"));
                                edtGapreason.setText(obj.getString("gapreason"));
                                edtGap.setText(obj.getString("gapyear"));
                            }
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
            protected Map<String, String> getParams(){
                Map<String, String> map=new HashMap<>();
                map.put("Roll_No",roll);
                return map;
            }
        };
        requestQueue.add(request);
    }
}