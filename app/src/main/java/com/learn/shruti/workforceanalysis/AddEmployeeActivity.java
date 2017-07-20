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

        //Get Firebase auth instance
        auth = FirebaseAuth.getInstance();


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

            String phone = empNumber.getText().toString();

            if(TextUtils.isEmpty(phone))
            {
                Toast.makeText(getApplicationContext(), "Enter Phone Number!", Toast.LENGTH_SHORT).show();
                return;
            }



                Employee newEmp = new Employee(idtext,eName,desig,email,Long.valueOf(phone),pass);

            addEmploy(newEmp);



            }
        });
    }


    private void addEmploy(Employee newEmp)
    {
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("users");


        //Toast.makeText(this,mDatabase.toString(),Toast.LENGTH_SHORT).show();
// pushing user to 'users' node using the userId
        mDatabase.child(newEmp.employeeID).setValue(newEmp);
        auth.createUserWithEmailAndPassword(newEmp.empEmail, newEmp.Password);
        startActivity(new Intent(AddEmployeeActivity.this,DashBoardActivity.class));
    }
}