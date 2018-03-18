package com.example.jijin.qrlogin;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import static com.example.jijin.qrlogin.R.id.etname;
import static com.example.jijin.qrlogin.R.id.etusername;

public class scan extends AppCompatActivity {
    private Button scan_Btn;
    public EditText etusername, etpassword, etname;


    //locatoin
//    private Button button;
//    private LocationManager locationManager;
//    private LocationListener locationListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);
        etusername = (EditText) findViewById(R.id.etusername);
        etname = (EditText) findViewById(R.id.etname);
        etpassword = (EditText) findViewById(R.id.etpassword);

        //location
//        button = (Button) findViewById(R.id.bloc);
//
//                   locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
//            locationListener = new LocationListener() {
//                @Override
//                public void onLocationChanged(Location location) {
//
//                }
//
//            @Override
//            public void onStatusChanged(String provider, int status, Bundle extras) {
//
//            }
//
//            @Override
//            public void onProviderEnabled(String provider) {
//
//            }
//
//            @Override
//            public void onProviderDisabled(String provider) {
//
//            }
//        };
//
//        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//
//            return;
//        }else
//        {}
//        locationManager.requestLocationUpdates("gps", 5000, 0, locationListener);
//
















////////////////////////////////////////////////////////////////////
        final Activity activity = this;
        scan_Btn = (Button) findViewById(R.id.scab_btn);


        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference table_useratt = database.getReference("log");


        scan_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                com.google.zxing.integration.android.IntentIntegrator integrator = new com.google.zxing.integration.android.IntentIntegrator(activity);
                integrator.setDesiredBarcodeFormats(com.google.zxing.integration.android.IntentIntegrator.QR_CODE_TYPES);
                integrator.setPrompt("Scan");
                integrator.setBeepEnabled(false);
                integrator.setCameraId(0);
                integrator.setBarcodeImageEnabled(false);
                integrator.initiateScan();

                final ProgressDialog mDialog = new ProgressDialog(scan.this);
                mDialog.setMessage("please wait....");
                mDialog.show();

            }
        });
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() == null) {
                Toast.makeText(this, "you cancelled the scanning", Toast.LENGTH_SHORT).show();
            } else {




                if (result.getContents().equals("hi")) {




                    final ProgressDialog mDialog = new ProgressDialog(scan.this);
                    mDialog.setMessage("please wait....");
                    mDialog.show();

                    final FirebaseDatabase database = FirebaseDatabase.getInstance();
                    final DatabaseReference table_useratt = database.getReference("log");

                    table_useratt.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            //check if already user signup
//                            if(dataSnapshot.child(etusername.getText().toString()).exists()){
//                                mDialog.dismiss();
//                                Toast.makeText(scan.this,"already registered",Toast.LENGTH_SHORT).show();
//
//                            }else
                            {
                                mDialog.dismiss();
                                User user =new User(etname.getText().toString(),etpassword.getText().toString());
                                table_useratt.child(etusername.getText().toString()).setValue(user);
                                Toast.makeText(scan.this,"successfull",Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                }
                Toast.makeText(this, result.getContents(), Toast.LENGTH_LONG).show();

            }

        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}
