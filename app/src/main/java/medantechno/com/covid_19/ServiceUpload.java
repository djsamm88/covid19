package medantechno.com.covid_19;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Executor;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;

public class ServiceUpload extends Service {
    public static final String CHANNEL_ID = "covid19";


    /*** lokasi ***/
    public static final String BROADCAST_ACTION = "LokasiService";
    private static final int TWO_MINUTES = 1000 * 60 * 2;
    public LocationManager locationManager;
    public ServiceUpload.MyLocationListener listener;
    public Location previousBestLocation = null;
    Intent intent,intentBadge;
    public static final String BROADCAST_BADGE = "badgeService";

    int counter = 0;
    /*** lokasi **/



    @Override
    public void onCreate() {
        super.onCreate();
        intent = new Intent(BROADCAST_ACTION);
        intentBadge = new Intent(BROADCAST_BADGE);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String input = intent.getStringExtra("inputExtra");
        createNotificationChannel();
        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,
                0, notificationIntent, 0);

        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("Sinkron Service")
                .setContentText(input)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentIntent(pendingIntent)
                .build();

        startForeground(1, notification);

        //do heavy work on a background thread
        Toast.makeText(getApplicationContext(),"Hello",Toast.LENGTH_SHORT);



        /******* auto refresh **********/

        final Handler handler = new Handler();
        Runnable refresh = new Runnable() {
            @Override
            public void run() {
                System.out.println("delay");
                /******* action disini nanti************/

                sinkron();
                /******* action disini nanti************/
                handler.postDelayed(this, 10000);//10 detik
            }
        };
        handler.postDelayed(refresh, 10000);//10 detik

        /******* auto refresh **********/


        panggilByStatus("Selesai");
        panggilByStatus("Sakit");
        panggilByStatus("Pemantauan");
        panggilByStatus("Verifikasi");
        sinkron();


        /******* auto refresh **********/

        final Handler handlerP = new Handler();
        Runnable refreshP = new Runnable() {
            @Override
            public void run() {
                System.out.println("delay");
                /******* action disini nanti************/
                panggilByStatus("Selesai");
                panggilByStatus("Sakit");
                panggilByStatus("Pemantauan");
                panggilByStatus("Verifikasi");
                /******* action disini nanti************/
                handlerP.postDelayed(this, 40000);//30 detik
            }
        };
        handlerP.postDelayed(refreshP, 40000);//30 detik

        /******* auto refresh **********/



        /******* auto refresh **********/

        final Handler handlerBadge = new Handler();
        Runnable refreshBadge = new Runnable() {
            @Override
            public void run() {
                System.out.println("delay");
                /******* action disini nanti************/
                badge();
                /******* action disini nanti************/
                handlerBadge.postDelayed(this, 10000);//30 detik
            }
        };
        handlerBadge.postDelayed(refreshBadge, 10000);//30 detik

        /******* auto refresh **********/




        //stopSelf();

        /************************************ lokasi ************************************/
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        listener = new ServiceUpload.MyLocationListener();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

        }
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 4000, 0, (LocationListener) listener);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 4000, 0, listener);
        /************************************ lokasi ************************************/

        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.v("STOP_SERVICE", "DONE");
        locationManager.removeUpdates(listener);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel serviceChannel = new NotificationChannel(
                    CHANNEL_ID,
                    "Foreground Service Channel",
                    NotificationManager.IMPORTANCE_DEFAULT
            );

            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(serviceChannel);
        }
    }


    /************************************ lokasi ************************************/

    protected boolean isBetterLocation(Location location, Location currentBestLocation) {
        if (currentBestLocation == null) {
            // A new location is always better than no location
            return true;
        }

        // Check whether the new location fix is newer or older
        long timeDelta = location.getTime() - currentBestLocation.getTime();
        boolean isSignificantlyNewer = timeDelta > TWO_MINUTES;
        boolean isSignificantlyOlder = timeDelta < -TWO_MINUTES;
        boolean isNewer = timeDelta > 0;

        // If it's been more than two minutes since the current location, use the new location
        // because the user has likely moved
        if (isSignificantlyNewer) {
            return true;
            // If the new location is more than two minutes older, it must be worse
        } else if (isSignificantlyOlder) {
            return false;
        }

        // Check whether the new location fix is more or less accurate
        int accuracyDelta = (int) (location.getAccuracy() - currentBestLocation.getAccuracy());
        boolean isLessAccurate = accuracyDelta > 0;
        boolean isMoreAccurate = accuracyDelta < 0;
        boolean isSignificantlyLessAccurate = accuracyDelta > 200;

        // Check if the old and new location are from the same provider
        boolean isFromSameProvider = isSameProvider(location.getProvider(),
                currentBestLocation.getProvider());

        // Determine location quality using a combination of timeliness and accuracy
        if (isMoreAccurate) {
            return true;
        } else if (isNewer && !isLessAccurate) {
            return true;
        } else if (isNewer && !isSignificantlyLessAccurate && isFromSameProvider) {
            return true;
        }
        return false;
    }

    /**
     * Checks whether two providers are the same
     */
    private boolean isSameProvider(String provider1, String provider2) {
        if (provider1 == null) {
            return provider2 == null;
        }
        return provider1.equals(provider2);
    }



    public static Thread performOnBackgroundThread(final Runnable runnable) {
        final Thread t = new Thread() {
            @Override
            public void run() {
                try {
                    runnable.run();
                } finally {

                }
            }
        };
        t.start();
        return t;
    }

    public class MyLocationListener implements LocationListener {

        public void onLocationChanged(final Location loc) {
            Log.i("*****", "Location changed");
            if (isBetterLocation(loc, previousBestLocation)) {
                loc.getLatitude();
                loc.getLongitude();
                intent.putExtra("Latitude", loc.getLatitude());
                intent.putExtra("Longitude", loc.getLongitude());
                intent.putExtra("Provider", loc.getProvider());
                sendBroadcast(intent);
                System.out.println("lat service="+loc.getLatitude());
                System.out.println("lng service="+loc.getLongitude());


                Date cDate = new Date();
                String fDate = new SimpleDateFormat("yyyy-MM-dd").format(cDate);
                String mLastUpdate = DateFormat.getTimeInstance().format(new Date());
                try {
                    new DbLokasi(getApplicationContext()).insert(new ModelLokasi("1", String.valueOf(loc.getLatitude()), String.valueOf(loc.getLongitude()), mLastUpdate, fDate));
                }catch (Exception e)
                {

                }
            }
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        public void onProviderDisabled(String provider) {
            Toast.makeText(getApplicationContext(), "Gps Disabled", Toast.LENGTH_SHORT).show();
        }


        public void onProviderEnabled(String provider) {
            Toast.makeText(getApplicationContext(), "Gps Enabled", Toast.LENGTH_SHORT).show();
        }


    }

    /************************************ lokasi ************************************/



    /************************************ sinkron upload ************************************/
    private void sinkron()
    {
        DbTransaksi dbTransaksi = new DbTransaksi(getApplicationContext());
        List<ModelData> transaksis = dbTransaksi.belum_sinkron();

        int i=0;
        for(ModelData trx:transaksis)
        {

            upload(trx);

            i++;

            System.out.println(trx.getNama()+"-"+trx.getSudah_sinkron());


        }
        System.out.println(transaksis.size());
        if(i==transaksis.size())
        {

        }

        if(transaksis.size()==0)
        {

        }

    }


    public void upload(final ModelData modelData)
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
            jsonBody.put("tgl_update", modelData.getTgl_update());
            jsonBody.put("dusun", modelData.getDusun());
            jsonBody.put("status", modelData.getStatus());
            jsonBody.put("status_akhir", modelData.getStatus_akhir());
            jsonBody.put("catatan_medis", modelData.getCatatan_medis());



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
            if(modelData.getStatus().equals("Verifikasi"))
            {
                jsonBody.put("foto_ktp",ktp);
                jsonBody.put("foto_orang",orang);
            }else{
                jsonBody.put("foto_ktp","");
                jsonBody.put("foto_orang","");
            }


            final String requestBody = jsonBody.toString();
            System.out.println(requestBody);

            RequestQueue requestQueue = Volley.newRequestQueue(this);
            String URL = UrlConfig.sinkron;

            StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.i("VOLLEY", response);


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

        }


    }

    /************************************ sinkron upload ************************************/



    /************************************ sinkron panggil ************************************/
    private void panggilByStatus(String statusnya)
    {


        RequestQueue queue = Volley.newRequestQueue(this);
        JsonArrayRequest sptRequest = new JsonArrayRequest(UrlConfig.panggil_by_status+statusnya,new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {


                // Parsing json
                for (int i = 0; i < response.length(); i++) {
                    try {

                        JSONObject obj = response.getJSONObject(i);

                        Log.d("panggil_by_status:", obj.getString("id")+"-"+obj.getString("status"));

                        /** ganti status dari online **/
                        ModelData mdBaru = new ModelData();
                        mdBaru.setStatus(obj.getString("status"));
                        mdBaru.setId(obj.getString("id"));
                        mdBaru.setNama(obj.getString("nama"));
                        mdBaru.setTempat_lahir(obj.getString("tempat_lahir"));
                        mdBaru.setTgl_lahir(obj.getString("tgl_lahir"));
                        mdBaru.setAlamat(obj.getString("alamat"));
                        mdBaru.setId_kec(obj.getString("id_kec"));
                        mdBaru.setId_desa(obj.getString("id_desa"));
                        mdBaru.setDusun(obj.getString("dusun"));
                        mdBaru.setNo_hp(obj.getString("no_hp"));
                        mdBaru.setSuhu_badan(obj.getString("suhu_badan"));
                        mdBaru.setKeterangan(obj.getString("keterangan"));
                        mdBaru.setKesehatan(obj.getString("kesehatan"));
                        mdBaru.setV_kecamatan(obj.getString("v_kecamatan"));
                        mdBaru.setV_desa(obj.getString("v_desa"));
                        mdBaru.setStatus(statusnya);
                        mdBaru.setFoto_orang(UrlConfig.base_url+obj.getString("foto_orang"));
                        mdBaru.setFoto_ktp(UrlConfig.base_url+obj.getString("foto_ktp"));
                        mdBaru.setTgl_update(obj.getString("tgl_update"));
                        mdBaru.setSudah_sinkron("sudah");
                        mdBaru.setLuar_hh(obj.getString("luar_hh"));
                        mdBaru.setJenis(obj.getString("jenis"));
                        mdBaru.setLat(obj.getString("lat"));
                        mdBaru.setLng(obj.getString("lng"));
                        mdBaru.setStatus_akhir(obj.getString("status_akhir"));
                        mdBaru.setCatatan_medis(obj.getString("catatan_medis"));


                        try{
                            new DbTransaksi(getApplicationContext()).insert(mdBaru);

                        }catch (Exception e)
                        {


                        }

                        try{
                            new DbTransaksi(getApplicationContext()).update(mdBaru);

                        }catch (Exception x)
                        {

                        }
                        notifSound();

                        /** ganti status dari online **/

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }



            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        queue.add(sptRequest);

    }

    /************************************ sinkron panggil ************************************/




    /************************************ badge ************************************/
    private void badge()
    {


        RequestQueue queue = Volley.newRequestQueue(this);
        JsonObjectRequest sptRequest = new JsonObjectRequest(Request.Method.GET,UrlConfig.badge,null,new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {

                    String masuk = response.getString("masuk");
                    String keluar = response.getString("keluar");
                    String ODP = response.getString("ODP");
                    String OTG = response.getString("OTG");
                    String POSITIF_COVID19 = response.getString("POSITIF_COVID19");
                    String Verifikasi = response.getString("Verifikasi");
                    String Pemantauan = response.getString("Pemantauan");
                    String Sakit = response.getString("Sakit");
                    String Selesai = response.getString("Selesai");


                    intentBadge.putExtra("masuk", masuk);
                    intentBadge.putExtra("keluar", keluar);
                    intentBadge.putExtra("ODP", ODP);
                    intentBadge.putExtra("OTG", OTG);
                    intentBadge.putExtra("POSITIF_COVID19", POSITIF_COVID19);
                    intentBadge.putExtra("Pemantauan", Pemantauan);
                    intentBadge.putExtra("Verifikasi", Verifikasi);
                    intentBadge.putExtra("Sakit", Sakit);
                    intentBadge.putExtra("Selesai", Selesai);
                    sendBroadcast(intentBadge);


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



    private void notifSound()
    {
        try {
            Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), notification);
            r.play();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
