package com.example.jijin.qrlogin;

import android.app.ProgressDialog;
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

public class signup extends AppCompatActivity {
   public EditText etusername,etpassword,etname;
   private Button btnsignup;
    private TextView tvback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        etusername = (EditText) findViewById(R.id.etusername);
        etname = (EditText) findViewById(R.id.etname);
        etpassword = (EditText) findViewById(R.id.etpassword);
        btnsignup = (Button) findViewById(R.id.btnsignup);
        tvback=(TextView)findViewById(R.id.tvback);



        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference table_user = database.getReference("att");

        btnsignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validate()) {
                    final ProgressDialog mDialog = new ProgressDialog(signup.this);
                    mDialog.setMessage("please wait....");
                    mDialog.show();

                    table_user.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            //check if already user signup
                            if (dataSnapshot.child(etusername.getText().toString()).exists()) {
                                mDialog.dismiss();
                                Toast.makeText(signup.this, "already registered", Toast.LENGTH_SHORT).show();

                            } else {
                                mDialog.dismiss();
                                User user = new User(etname.getText().toString(), etpassword.getText().toString());
                                table_user.child(etusername.getText().toString()).setValue(user);
                                Toast.makeText(signup.this, "sigup successfull", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
            }
        });
    }

    private Boolean validate()
    {
        Boolean result=false;
        String name=etusername.getText().toString();
        String password=etpassword.getText().toString();
        String email=etname.getText().toString();

        if(name.isEmpty() || password.isEmpty() || email.isEmpty()){
            Toast.makeText(this,"Enter all the details",Toast.LENGTH_SHORT).show();

        }else{
            result=true;
        }


        return result;
    }

}  
