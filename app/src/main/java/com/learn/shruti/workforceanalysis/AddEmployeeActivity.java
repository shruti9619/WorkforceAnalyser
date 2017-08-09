package com.learn.shruti.workforceanalysis;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.learn.shruti.workforceanalysis.Model.Employee;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.quickstart.database.models.Post;
//import com.google.firebase.quickstart.database.models.User;


public class AddEmployeeActivity extends AppCompatActivity {

    Button btn_add_emp;
    EditText empnametext, empdesigtext, empemailtext, empidtext, emppasstext, empNumber;
    EditText empjoinndate;
    private DatabaseReference mDatabase;
    private FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_add_employee);



        btn_add_emp = (Button)findViewById(R.id.addempbutton);
        empnametext = (EditText)findViewById(R.id.empnametext);
        empdesigtext = (EditText)findViewById(R.id.empdesigtext);
        empemailtext = (EditText)findViewById(R.id.empemailtext);
        empidtext = (EditText)findViewById(R.id.empidtext);
        emppasstext = (EditText)findViewById(R.id.emppasstext);
        empNumber = (EditText)findViewById(R.id.empnumtext);

        Bundle bundle = getIntent().getExtras();

        if (bundle != null) {
            empnametext.setText(bundle.getString("empname"));
            empdesigtext.setText(bundle.getString("empdesig"));
            emppasstext.setText(bundle.getString("emppass"));
            empNumber.setText(String.valueOf(bundle.getLong("empphone")));
            empemailtext.setText(bundle.getString("empemail"));
            empidtext.setText(bundle.getString("empid"));
            // id is fixed cant be changed
            empidtext.setEnabled(false);
            empemailtext.setEnabled(false);
        }

        //Get Firebase auth instance
        auth = FirebaseAuth.getInstance();

        // add employee details after validations
        btn_add_emp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            String eName = empnametext.getText().toString();
            if(TextUtils.isEmpty(eName))
            {
                Toast.makeText(getApplicationContext(), "Enter Name!", Toast.LENGTH_SHORT).show();
                return;
            }

            String desig = empdesigtext.getText().toString();

            if(TextUtils.isEmpty(desig))
            {
                Toast.makeText(getApplicationContext(), "Enter Designation!", Toast.LENGTH_SHORT).show();
                return;
            }

            String email = empemailtext.getText().toString();

            if(TextUtils.isEmpty(email))
            {
                Toast.makeText(getApplicationContext(), "Enter Email!", Toast.LENGTH_SHORT).show();
                return;
            }

            String idtext = empidtext.getText().toString();

            if(TextUtils.isEmpty(idtext))
            {
                Toast.makeText(getApplicationContext(), "Enter Employee ID!", Toast.LENGTH_SHORT).show();
                return;
            }

            String pass = emppasstext.getText().toString();

            if(TextUtils.isEmpty(pass))
            {
                Toast.makeText(getApplicationContext(), "Enter Password!", Toast.LENGTH_SHORT).show();
                return;
            }

            if(pass.length() < 6)
            {
                Toast.makeText(getApplicationContext(), "Password too short, enter minimum 6 characters!", Toast.LENGTH_SHORT).show();
                return;
            }

            String phone = empNumber.getText().toString();

            if(TextUtils.isEmpty(phone))
            {
                Toast.makeText(getApplicationContext(), "Enter Phone Number!", Toast.LENGTH_SHORT).show();
                return;
            }



                Employee newEmp = new Employee(idtext,eName,desig,email,Long.valueOf(phone),MD5Hasher.md5hash(pass));

                addEmploy(newEmp, pass);



            }
        });
    }


    // method to fetch db reference from firebase and create new employee in db without immediate login
    private void addEmploy(Employee newEmp,String pass)
    {
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("users");
        mDatabase.child(newEmp.employeeID).setValue(newEmp);
        auth.createUserWithEmailAndPassword(newEmp.empEmail, MD5Hasher.md5hash(pass));
        startActivity(new Intent(AddEmployeeActivity.this,DashBoardActivity.class));
    }
}
