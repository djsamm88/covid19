package medantechno.com.covid_19;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;

import androidx.core.app.NotificationCompat;

public class SerbaGuna {

    private Activity activity;
    private ProgressDialog pDialog;
    public SerbaGuna(Activity activity){
        this.activity=activity;
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

            RequestQueue requestQueue = Volley.newRequestQueue(activity);
            String URL = UrlConfig.sinkron;

            StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.i("VOLLEY", response);
                    hidePDialog();
                    Toast.makeText(activity,"Berhasil sinkron ke server.",Toast.LENGTH_SHORT).show();
                    notifnya("Berhasil","Berhasil sinkron ke server.");

                    /** ganti status sinkron **/
                    ModelData mdBaru = new ModelData();
                    mdBaru.setSudah_sinkron("sudah");
                    mdBaru.setId(modelData.getId());
                    new DbTransaksi(activity).updateStatusSinkron(mdBaru);
                    /** ganti status sinkron **/

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("VOLLEY", error.toString());
                    hidePDialog();
                    Toast.makeText(activity,"Nampaknya ada masalah. Pastikan jaringan anda bagus.",Toast.LENGTH_LONG).show();
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
            Toast.makeText(activity,"Nampaknya ada masalah. Pastikan jaringan anda bagus."+e.toString(),Toast.LENGTH_LONG).show();
            notifnya("Gagal","Nampaknya ada masalah. Pastikan jaringan anda bagus.");
        }


    }



    public void notifnya(String title,String konten)
    {

        /****************  notif **********/
        Intent notificationIntent = new Intent(activity, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(activity, 0,
                notificationIntent, 0);

        Uri uri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(activity, "1")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setSound(uri)
                .setAutoCancel(true)
                .setContentTitle(title)
                .setContentText(konten)
                .setContentIntent(pendingIntent)
                .setPriority(NotificationCompat.PRIORITY_HIGH);
        Notification notif = mBuilder.build();


        NotificationManager mNotificationManager = (NotificationManager) activity.getSystemService(Context.NOTIFICATION_SERVICE);

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
        pDialog = new ProgressDialog(activity);
        pDialog.setMessage("Loading...");
        pDialog.show();

    }
}
