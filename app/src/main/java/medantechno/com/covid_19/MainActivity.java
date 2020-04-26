package medantechno.com.covid_19;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import butterknife.ButterKnife;

import android.os.Looper;
import android.provider.Settings;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    Button btnFormKeluar,btnFormMasuk,btnLaporanMasuk,btnLaporanKeluar,btnSinkronisasiServer,btnPemantauan,btnVerifikasi,btnSakit,btnSelesai;

    private ProgressDialog pDialog;



    /************************* atribut fused lokasi ***************************/
    private static final String TAG = MainActivity.class.getSimpleName();


    Button btnStartUpdate;

    private String mLastUpdateTime;
    private static final long UPDATE_INTERVAL_IN_MILLISECONDS = 100000;
    private static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS = 50000;
    private static final int REQUEST_CHECK_SETTINGS = 100;


    // bunch of location related apis
    private FusedLocationProviderClient mFusedLocationClient;
    private SettingsClient mSettingsClient;
    private LocationRequest mLocationRequest;
    private LocationSettingsRequest mLocationSettingsRequest;
    private LocationCallback mLocationCallback;
    private Location mCurrentLocation;

    // boolean flag to toggle the ui
    private Boolean mRequestingLocationUpdates;
    /************************* atribut fused lokasi ***************************/


    TextView badgeSakit,badgePemantauan,badgeVerifikasi,badgeODP,badgeOTG,badgePOSITIF_COVID19,badgeSelesai;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        badgeODP = (TextView)findViewById(R.id.badgeODP);
        badgeOTG = (TextView)findViewById(R.id.badgeOTG);
        badgePOSITIF_COVID19 = (TextView)findViewById(R.id.badgePOSITIF_COVID19);

        badgeSakit = (TextView)findViewById(R.id.badgeSakit);
        badgeVerifikasi = (TextView)findViewById(R.id.badgeVerifikasi);
        badgePemantauan = (TextView)findViewById(R.id.badgePemantauan);
        badgeSelesai = (TextView)findViewById(R.id.badgeSelesai);

        badgeSakit.setVisibility(View.INVISIBLE);
        badgeVerifikasi.setVisibility(View.INVISIBLE);
        badgePemantauan.setVisibility(View.INVISIBLE);
        badgeSelesai.setVisibility(View.INVISIBLE);

        btnFormMasuk = (Button)findViewById(R.id.btnFormMasuk);
        //btnFormMasuk.setVisibility(View.INVISIBLE);

        btnFormMasuk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, FormActivityMasuk.class);
                startActivity(i);
            }
        });

        btnFormKeluar = (Button)findViewById(R.id.btnFormKeluar);
        btnFormKeluar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),FormActivityKeluar.class));
            }
        });

        btnLaporanMasuk = (Button)findViewById(R.id.btnLaporan_masuk);
        btnLaporanMasuk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), DetailActivity.class);
                i.putExtra("jenis","masuk");
                startActivity(i);
            }
        });

        btnLaporanKeluar = (Button)findViewById(R.id.btnLaporan_keluar);
        btnLaporanKeluar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), DetailActivity.class);
                i.putExtra("jenis","keluar");
                startActivity(i);
            }
        });

        btnSinkronisasiServer = (Button)findViewById(R.id.btnSinkronisasiServer);
        btnSinkronisasiServer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sinkron();
                startService();

                Intent i = new Intent();
                i.setAction("abc");
                i.putExtra("val","horas");
                sendBroadcast(i);
            }
        });


        btnPemantauan = (Button)findViewById(R.id.btnPemantauan);
        btnPemantauan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(getApplicationContext(),PemantauanActivity.class));
            }
        });


        btnVerifikasi=(Button)findViewById(R.id.btnVerifikasi);
        btnVerifikasi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),VerifikasiActivity.class));
            }
        });

        btnSakit = (Button)findViewById(R.id.btnSakit);
        btnSakit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),SakitActivity.class));
            }
        });

        btnSelesai = (Button)findViewById(R.id.btnSelesai);
        btnSelesai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),SelesaiActivity.class));
            }
        });


        startService();
        badge();

        /************************* atribut fused lokasi ***************************/
        ButterKnife.bind(this);
        // initialize the necessary libraries
        init();
        // restore the values from saved instance state
        restoreValuesFromBundle(savedInstanceState);
        startLocationButtonClick();
        /************************* atribut fused lokasi ***************************/


        /****** ini untuk receiver ****/
        IntentFilter filter = new IntentFilter();
        filter.addAction("LokasiService");
        registerReceiver(receiver, filter);
        /****** ini untuk receiver ****/



        /****** ini untuk receiver Badge ****/
        IntentFilter filterBadge = new IntentFilter();
        filterBadge.addAction("badgeService");
        registerReceiver(receiverBadge, filterBadge);
        /****** ini untuk receiver ****/


    }




    /************************************ badge ************************************/
    private void badge()
    {


        RequestQueue queue = Volley.newRequestQueue(this);
        JsonObjectRequest sptRequest = new JsonObjectRequest(Request.Method.GET,UrlConfig.badge,null,new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    System.out.println("ODP"+response.getString("ODP"));

                    badgeSakit.setText(response.getString("Sakit"));
                    badgePemantauan.setText(response.getString("Pemantauan"));
                    badgeVerifikasi.setText(response.getString("Verifikasi"));
                    if(Integer.parseInt(response.getString("Sakit"))>0)
                    {
                        badgeSakit.setVisibility(View.VISIBLE);
                    }

                    if(Integer.parseInt(response.getString("Pemantauan"))>0)
                    {
                        badgePemantauan.setVisibility(View.VISIBLE);
                    }

                    if(Integer.parseInt(response.getString("Verifikasi"))>0)
                    {
                        badgeVerifikasi.setVisibility(View.VISIBLE);
                    }

                    if(Integer.parseInt(response.getString("Selesai"))>0)
                    {
                        badgeSelesai.setVisibility(View.VISIBLE);
                    }

                    badgePOSITIF_COVID19.setText("COVID19:"+response.getString("POSITIF_COVID19"));
                    badgeODP.setText("ODP:"+response.getString("ODP"));
                    badgeOTG.setText("OTG:"+response.getString("OTG"));
                    badgeSelesai.setText("SELESAI:"+response.getString("Selesai"));


                }catch (Exception e)
                {

                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        queue.add(sptRequest);

    }

    /************************************ badge ************************************/


    private BroadcastReceiver receiverBadge = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {


            badgeSakit.setText(intent.getStringExtra("Sakit"));
            badgePemantauan.setText(intent.getStringExtra("Pemantauan"));
            badgeVerifikasi.setText(intent.getStringExtra("Verifikasi"));

            if(Integer.parseInt(intent.getStringExtra("Sakit"))>0)
            {
                badgeSakit.setVisibility(View.VISIBLE);
            }

            if(Integer.parseInt(intent.getStringExtra("Pemantauan"))>0)
            {
                badgePemantauan.setVisibility(View.VISIBLE);
            }

            if(Integer.parseInt(intent.getStringExtra("Verifikasi"))>0)
            {
                badgeVerifikasi.setVisibility(View.VISIBLE);
            }

            if(Integer.parseInt(intent.getStringExtra("Selesai"))>0)
            {
                badgeSelesai.setVisibility(View.VISIBLE);
            }

            badgeODP.setText("ODP:"+intent.getStringExtra("ODP"));
            badgeOTG.setText("OTG:"+intent.getStringExtra("OTG"));
            badgePOSITIF_COVID19.setText("COVID19:"+intent.getStringExtra("POSITIF_COVID19"));
            badgeSelesai.setText("SELESAI:"+intent.getStringExtra("Selesai"));

        }
    };

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            //Toast.makeText(getApplicationContext(), "received", Toast.LENGTH_SHORT).show();
            Double lat = intent.getDoubleExtra("Latitude",0);
            Double lng = intent.getDoubleExtra("Latitude",0);
            //Toast.makeText(getApplicationContext(), lat+"-"+lng, Toast.LENGTH_SHORT).show();
        }
    };


    public void startService() {
        Intent serviceIntent = new Intent(this, ServiceUpload.class);
        serviceIntent.putExtra("inputExtra", "AutoSync...");

        ContextCompat.startForegroundService(this, serviceIntent);
    }

    public void stopService() {
        Intent serviceIntent = new Intent(this, ServiceUpload.class);
        stopService(serviceIntent);
    }

    private void sinkron()
    {
        showDialog();
        DbTransaksi dbTransaksi = new DbTransaksi(getApplicationContext());
        List<ModelData> transaksis = dbTransaksi.belum_sinkron();

        int i=0;
        for(ModelData trx:transaksis)
        {

            //upload(trx);
            new SerbaGuna(this).upload(trx);

            i++;

            System.out.println(trx.getNama()+"-"+trx.getSudah_sinkron());


        }
        System.out.println(transaksis.size());
        if(i==transaksis.size())
        {
            Toast.makeText(getApplicationContext(),"Akan disinkronkan. "+i+" data.",Toast.LENGTH_SHORT).show();
            hidePDialog();
        }

        if(transaksis.size()==0)
        {
            Toast.makeText(getApplicationContext(),"Belum ada data baru.",Toast.LENGTH_SHORT).show();
            hidePDialog();
        }

    }

    private void upload(final ModelData modelData)
    {
        try {


            JSONObject jsonBody = new JSONObject();
            jsonBody.put("id", modelData.getId());
            jsonBody.put("nama", modelData.getNama());
            jsonBody.put("tempat_lahir", modelData.getTempat_lahir());
            jsonBody.put("tgl_lahir", modelData.getTgl_lahir());
            jsonBody.put("alamat", modelData.getAlamat());
            jsonBody.put("luar_hh", modelData.getLuar_hh());
            jsonBody.put("id_kec", modelData.getId_kec());
            jsonBody.put("id_desa", modelData.getId_desa());
            jsonBody.put("no_hp", modelData.getNo_hp());
            jsonBody.put("suhu_badan", modelData.getSuhu_badan());
            jsonBody.put("keterangan", modelData.getKeterangan());
            jsonBody.put("jenis", modelData.getJenis());
            jsonBody.put("kesehatan", modelData.getKesehatan());
            jsonBody.put("lat", modelData.getLat());
            jsonBody.put("lng", modelData.getLng());
            jsonBody.put("v_kecamatan", modelData.getV_kecamatan());
            jsonBody.put("v_desa", modelData.getV_desa());

            /** disini merubah string ke base64 **/

            String ktp,orang;
            try {
                Bitmap bmKtp = BitmapFactory.decodeFile(modelData.getFoto_ktp());
                ByteArrayOutputStream baosKtp = new ByteArrayOutputStream();
                bmKtp.compress(Bitmap.CompressFormat.JPEG, 60, baosKtp); //bm is the bitmap object
                byte[] byteArrayImageKtp = baosKtp.toByteArray();
                ktp = Base64.encodeToString(byteArrayImageKtp, Base64.DEFAULT);

            }catch (Exception e)
            {
                System.out.println("Tidak ada foto"+ e);
                ktp="";

            }


            try{

                Bitmap bmOrang = BitmapFactory.decodeFile(modelData.getFoto_orang());
                ByteArrayOutputStream basoOrang = new ByteArrayOutputStream();
                bmOrang.compress(Bitmap.CompressFormat.JPEG, 60, basoOrang); //bm is the bitmap object
                byte[] byteArrayImageOrang = basoOrang.toByteArray();
                orang = Base64.encodeToString(byteArrayImageOrang, Base64.DEFAULT);
            }catch (Exception ex)
            {
                System.out.println("Tidak ada foto"+ ex);

                orang="";
            }


            /** disini merubah string ke base64 **/

            jsonBody.put("foto_ktp",ktp);
            jsonBody.put("foto_orang",orang);

            final String requestBody = jsonBody.toString();
            System.out.println(requestBody);

            RequestQueue requestQueue = Volley.newRequestQueue(this);
            String URL = UrlConfig.sinkron;

            StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.i("VOLLEY", response);
                    hidePDialog();
                    Toast.makeText(getApplicationContext(),"Berhasil sinkron ke server.",Toast.LENGTH_SHORT).show();
                    notifnya("Berhasil","Berhasil sinkron ke server.");

                    /** ganti status sinkron **/
                    ModelData mdBaru = new ModelData();
                    mdBaru.setSudah_sinkron("sudah");
                    mdBaru.setId(modelData.getId());
                    new DbTransaksi(getApplicationContext()).updateStatusSinkron(mdBaru);
                    /** ganti status sinkron **/

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("VOLLEY", error.toString());
                    hidePDialog();
                    Toast.makeText(getApplicationContext(),"Nampaknya ada masalah. Pastikan jaringan anda bagus.",Toast.LENGTH_LONG).show();
                    notifnya("Gagal","Nampaknya ada masalah. Pastikan jaringan anda bagus.");
                }
            }) {
                @Override
                public String getBodyContentType() {
                    return "application/json; charset=utf-8";
                }

                @Override
                public byte[] getBody() throws AuthFailureError {
                    try {
                        return requestBody == null ? null : requestBody.getBytes("utf-8");
                    } catch (UnsupportedEncodingException uee) {
                        VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", requestBody, "utf-8");
                        return null;
                    }
                }

            };

            requestQueue.add(stringRequest);

            /** mengatur time out volley***/
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                    50000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            /** mengatur time out volley***/


        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(),"Nampaknya ada masalah. Pastikan jaringan anda bagus."+e.toString(),Toast.LENGTH_LONG).show();
            notifnya("Gagal","Nampaknya ada masalah. Pastikan jaringan anda bagus.");
        }


    }




    public void notifnya(String title,String konten)
    {

        /****************  notif **********/
        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
                notificationIntent, 0);

        Uri uri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this, "1")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setSound(uri)
                .setAutoCancel(true)
                .setContentTitle(title)
                .setContentText(konten)
                .setContentIntent(pendingIntent)
                .setPriority(NotificationCompat.PRIORITY_HIGH);
        Notification notif = mBuilder.build();


        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("1",
                    "sinkronisasi",
                    NotificationManager.IMPORTANCE_DEFAULT);
            mNotificationManager.createNotificationChannel(channel);
        }


        mNotificationManager.notify(1, notif);
        /**************** notif **********/
    }

    private void hidePDialog() {
        if (pDialog != null) {
            pDialog.dismiss();
            pDialog = null;
        }
    }

    private void showDialog()
    {
        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.show();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }


        return super.onOptionsItemSelected(item);
    }









    /************************* atribut fused lokasi ***************************/
    private void init() {
        mFusedLocationClient    = LocationServices.getFusedLocationProviderClient(this);
        mSettingsClient         = LocationServices.getSettingsClient(this);

        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                // location is received
                mCurrentLocation    = locationResult.getLastLocation();
                mLastUpdateTime     = DateFormat.getTimeInstance().format(new Date());

                updateLocationUI();
            }
        };

        mRequestingLocationUpdates = false;

        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS);
        mLocationRequest.setFastestInterval(FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(mLocationRequest);
        mLocationSettingsRequest = builder.build();
    }

    /**
     * Restoring values from saved instance state
     */
    private void restoreValuesFromBundle(Bundle savedInstanceState)
    {
        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey("is_requesting_updates")) {
                mRequestingLocationUpdates = savedInstanceState.getBoolean("is_requesting_updates");
            }

            if (savedInstanceState.containsKey("last_known_location")) {
                mCurrentLocation = savedInstanceState.getParcelable("last_known_location");
            }

            if (savedInstanceState.containsKey("last_updated_on")) {
                mLastUpdateTime = savedInstanceState.getString("last_updated_on");
            }
        }

        updateLocationUI();
    }


    /**
     * Update the UI displaying the location data
     * and toggling the buttons
     */
    private void updateLocationUI() {
        if (mCurrentLocation != null) {
            //txtLocationResult.setText("Lat: " + mCurrentLocation.getLatitude() + ", " + "Lng: " + mCurrentLocation.getLongitude());
            Log.d(TAG,"lat:"+mCurrentLocation.getLatitude());
            Log.d(TAG,"lng:"+mCurrentLocation.getLongitude());
            Log.d(TAG,"Last updated on: " + mLastUpdateTime);

            Date cDate = new Date();
            String fDate = new SimpleDateFormat("yyyy-MM-dd").format(cDate);

            //ModelLokasi(String id_lokasi,String lat,String lng,String jam,String tanggal)
            new DbLokasi(getApplicationContext()).insert(new ModelLokasi("1",String.valueOf(mCurrentLocation.getLatitude()),String.valueOf(mCurrentLocation.getLongitude()),mLastUpdateTime.toString(),fDate));
        }

        //toggleButtons();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean("is_requesting_updates", mRequestingLocationUpdates);
        outState.putParcelable("last_known_location", mCurrentLocation);
        outState.putString("last_updated_on", mLastUpdateTime);

    }


    /**
     * Starting location updates
     * Check whether location settings are satisfied and then
     * location updates will be requested
     */
    private void startLocationUpdates() {
        mSettingsClient
                .checkLocationSettings(mLocationSettingsRequest)
                .addOnSuccessListener(this, new OnSuccessListener<LocationSettingsResponse>() {
                    @SuppressLint("MissingPermission")
                    @Override
                    public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                        Log.i(TAG, "All location settings are satisfied.");

                        //Toast.makeText(getApplicationContext(), "Started location updates!", Toast.LENGTH_SHORT).show();

                        //noinspection MissingPermission
                        mFusedLocationClient.requestLocationUpdates(mLocationRequest,
                                mLocationCallback, Looper.myLooper());

                        updateLocationUI();
                    }
                })
                .addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        int statusCode = ((ApiException) e).getStatusCode();
                        switch (statusCode) {
                            case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                                Log.i(TAG, "Location settings are not satisfied. Attempting to upgrade " +
                                        "location settings ");
                                try {
                                    // Show the dialog by calling startResolutionForResult(), and check the
                                    // result in onActivityResult().
                                    ResolvableApiException rae = (ResolvableApiException) e;
                                    rae.startResolutionForResult(MainActivity.this, REQUEST_CHECK_SETTINGS);
                                } catch (IntentSender.SendIntentException sie) {
                                    Log.i(TAG, "PendingIntent unable to execute request.");
                                }
                                break;
                            case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                                String errorMessage = "Location settings are inadequate, and cannot be " +
                                        "fixed here. Fix in Settings.";
                                Log.e(TAG, errorMessage);

                                Toast.makeText(MainActivity.this, errorMessage, Toast.LENGTH_LONG).show();
                        }

                        updateLocationUI();
                    }
                });
    }

    //@OnClick(R.id.btn_start_location_updates)
    public void startLocationButtonClick()
    {
        // Requesting ACCESS_FINE_LOCATION using Dexter library
        Dexter.withActivity(this)
                .withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {
                        mRequestingLocationUpdates = true;
                        startLocationUpdates();
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {
                        if (response.isPermanentlyDenied()) {
                            // open device settings when the permission is
                            // denied permanently
                            openSettings();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).check();
    }


    public void stopLocationButtonClick()
    {
        mRequestingLocationUpdates = false;
        stopLocationUpdates();
    }

    public void stopLocationUpdates()
    {
        // Removing location updates
        mFusedLocationClient
                .removeLocationUpdates(mLocationCallback)
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        //Toast.makeText(getApplicationContext(), "... Lokasi dihentikan ...", Toast.LENGTH_SHORT).show();
                        //toggleButtons();
                    }
                });
    }


    public void showLastKnownLocation()
    {
        if (mCurrentLocation != null) {
            Toast.makeText(getApplicationContext(), "Lat: " + mCurrentLocation.getLatitude()
                    + ", Lng: " + mCurrentLocation.getLongitude(), Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(getApplicationContext(), "Last known location is not available!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        switch (requestCode) {
            // Check for the integer request code originally supplied to startResolutionForResult().
            case REQUEST_CHECK_SETTINGS:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        Log.e(TAG, "User agreed to make required location settings changes.");
                        // Nothing to do. startLocationupdates() gets called in onResume again.
                        break;
                    case Activity.RESULT_CANCELED:
                        Log.e(TAG, "User chose not to make required location settings changes.");



                        AlertDialog.Builder xx = new AlertDialog.Builder(MainActivity.this);
                        xx.setTitle("Perhatian");
                        xx.setMessage("Anda harus menghidupkan GPS?");
                        xx.setPositiveButton("Oke", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //finish();
                                startLocationButtonClick();
                            }
                        });

                        xx.show();




                        mRequestingLocationUpdates = false;
                        break;
                }
                break;
        }
    }

    private void openSettings() {
        Intent intent = new Intent();
        intent.setAction(
                Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package",
                BuildConfig.APPLICATION_ID, null);
        intent.setData(uri);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    private boolean checkPermissions() {
        int permissionState = ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION);
        return permissionState == PackageManager.PERMISSION_GRANTED;
    }


    @Override
    public void onResume() {
        super.onResume();

        // Resuming location updates depending on button state and
        // allowed permissions
        if (mRequestingLocationUpdates && checkPermissions()) {
            startLocationUpdates();
        }

        updateLocationUI();

        /****** ini untuk receiver ****/
        IntentFilter filter = new IntentFilter();
        filter.addAction("LokasiService");
        registerReceiver(receiver, filter);
        /****** ini untuk receiver ****/


        /****** ini untuk receiver Badge ****/
        IntentFilter filterBadge = new IntentFilter();
        filterBadge.addAction("badgeService");
        registerReceiver(receiverBadge, filterBadge);
        /****** ini untuk receiver ****/
    }



    @Override
    protected void onPause() {
        super.onPause();

        if (mRequestingLocationUpdates)
        {
            // pausing location updates
            stopLocationUpdates();
        }

        /****** ini untuk receiver ****/
        unregisterReceiver(receiver);
        /****** ini untuk receiver ****/
        unregisterReceiver(receiverBadge);
    }

    /************************* atribut fused lokasi ***************************/



}
