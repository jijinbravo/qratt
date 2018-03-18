package com.example.jijin.qrlogin;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    private int counter=5;
    Button btnsignin,btnsignup;
    TextView txtslogan,info;
    EditText etusername,etpassword;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        btnsignin=(Button)findViewById(R.id.btnsignin);
        btnsignup=(Button)findViewById(R.id.btnsignup);
        txtslogan=(TextView)findViewById(R.id.txtslogan);
        etusername=(EditText)findViewById(R.id.etusername);
        etpassword=(EditText)findViewById(R.id.etpassword);
        info = (TextView) findViewById(R.id.tvinfo);
        info.setText("no of attempts remaining:5");


        FirebaseDatabase database=FirebaseDatabase.getInstance();
     final DatabaseReference table_user=database.getReference("att");

         btnsignup.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 Intent intent = new Intent(MainActivity.this, signup.class);
                 startActivity(intent);

             }
         });

         btnsignin.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {

                 final ProgressDialog mDialog =new ProgressDialog(MainActivity.this);
                 mDialog.setMessage("please wait....");
                 mDialog.show();
                 table_user.addValueEventListener(new ValueEventListener() {
                     @Override
                     public void onDataChange(DataSnapshot dataSnapshot) {

                         //to check user not in database
                         if(dataSnapshot.child(etusername.getText().toString()).exists()){

                             //get user info
                         mDialog.dismiss();

                      User user =dataSnapshot.child(etusername.getText().toString()).getValue(User.class);
//                        if(User.getPassword().equals(etpassword.getText().toString()))
                             if(user.getPassword().equals(etpassword.getText().toString()))
                         {
                             Toast.makeText(MainActivity.this,"sign is successfully..",Toast.LENGTH_SHORT).show();
                             Intent inten = new Intent(MainActivity.this, scan.class);
                             startActivity(inten);

                         }else
                         {
                            Toast.makeText(MainActivity.this,"sign is failed..",Toast.LENGTH_SHORT).show();
                             counter--;
                             info.setText("no of attempts remaining:"+String.valueOf(counter));
                             if (counter == 0) {
                                 btnsignin.setEnabled(false);
                             }

                         }
                       }else
                         {
                             mDialog.dismiss();
                             Toast.makeText(MainActivity.this,"user not exist in database..",Toast.LENGTH_SHORT).show();
                         }
                     }

                     @Override
                     public void onCancelled(DatabaseError databaseError) {

                     }
                 });

             }
         });
    }
}
