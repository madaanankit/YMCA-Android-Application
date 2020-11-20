package com.example.finalpay;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
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

public class StudentRegister extends AppCompatActivity {
    Button btnReg,btnClr;
    EditText edtRno,edtPass;
    TextView tvForgot;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_register);
        btnReg = findViewById(R.id.btnlogin);
        btnClr = findViewById(R.id.btnClear);
        edtRno = findViewById(R.id.edtRNo);

        edtPass = findViewById(R.id.edtPass);

        tvForgot =  findViewById(R.id.forgot);
        tvForgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(StudentRegister.this, "Contact Admin for New Password", Toast.LENGTH_LONG).show();
            }
        });

        btnReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkuser();

            }
        });
        btnClr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clear();
            }
        });

    }
    private void checkuser(){
        final String pass,rno;
        rno=edtRno.getText().toString();
        pass=edtPass.getText().toString();
        if(rno.isEmpty() || pass.isEmpty()){
            Toast.makeText(this, "Field Empty", Toast.LENGTH_SHORT).show();
            return;
        }
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        final String url ="https://payymca.000webhostapp.com/login.php";
        StringRequest request= new StringRequest(
                Request.Method.POST,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(StudentRegister.this, response, Toast.LENGTH_SHORT).show();
                        if(response.equals("Login Successfull")){
                            SharedPreferences sharedPreferences = getSharedPreferences("login", MODE_PRIVATE);
                            SharedPreferences.Editor myEdit = sharedPreferences.edit();
                            myEdit.putString("roll", rno);
                            myEdit.putString("pass", pass);
                            myEdit.commit();
                            Intent intent = new Intent(StudentRegister.this, HomeScreen.class);
                            startActivity(intent);
                            finish();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(StudentRegister.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
        ){
            @Override
            protected Map<String, String> getParams(){
                Map<String, String> map = new HashMap<>();
                map.put("Password",pass);
                map.put("Roll_No",rno);
                return map;
            }
        };
        requestQueue.add(request);
    }
    private void clear(){
        String a,b;
        a=edtRno.getText().toString();
        b=edtPass.getText().toString();
        if(a.isEmpty() && b.isEmpty()){
            Toast.makeText(this, "Field Already Empty", Toast.LENGTH_SHORT).show();
            return;
        }
        edtRno.setText("");
        edtPass.setText("");
        Toast.makeText(this, "Text Cleared", Toast.LENGTH_SHORT).show();
    }
}