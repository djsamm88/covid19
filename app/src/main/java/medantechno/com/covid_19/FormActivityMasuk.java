package medantechno.com.covid_19;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

public class FormActivityMasuk extends AppCompatActivity {
    ImageView foto_ktp,foto_orang;
    Calendar myCalendar;
    DatePickerDialog.OnDateSetListener dateTanggal;
    Spinner spinnerKecamatan,spinnerDesa;
    private static final int STORAGE_PERMISSION_CODE = 123;

    EditText t4_foto_orang,t4_foto_ktp,nama,tempat_lahir,tgl_lahir,alamat,luar_hh,dusun,no_hp,suhu_badan,keterangan;

    String id_desa,id_kecamatan,v_desa,v_kecamatan,v_tgl_lahir;
    Button btnSimpan;

    String lat,lng;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.form_masuk);


        final String id = UUID.randomUUID().toString();

        btnSimpan = (Button)findViewById(R.id.btnSimpan);
        foto_ktp = (ImageView)findViewById(R.id.foto_ktp);
        foto_orang = (ImageView)findViewById(R.id.foto_orang);
        t4_foto_ktp = (EditText)findViewById(R.id.t4_foto_ktp);
        t4_foto_orang = (EditText)findViewById(R.id.t4_foto_orang);

        nama = (EditText)findViewById(R.id.nama);
        tempat_lahir = (EditText)findViewById(R.id.tempat_lahir);
        alamat = (EditText)findViewById(R.id.alamat);
        luar_hh = (EditText)findViewById(R.id.luar_hh);
        dusun = (EditText)findViewById(R.id.dusun);
        no_hp = (EditText)findViewById(R.id.no_hp);
        suhu_badan = (EditText)findViewById(R.id.suhu_badan);
        keterangan = (EditText)findViewById(R.id.keterangan);


        /****** ini untuk receiver ****/
        IntentFilter filter = new IntentFilter();
        filter.addAction("LokasiService");
        registerReceiver(receiver, filter);
        /****** ini untuk receiver ****/


        spinnerKecamatan = (Spinner)findViewById(R.id.spinnerKecamatan);
        spinnerDesa = (Spinner)findViewById(R.id.spinnerDesa);

        foto_ktp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dispatchTakePictureIntent(0);
            }
        });

        foto_orang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dispatchTakePictureIntent(1);
            }
        });

        /****datepicker tgl lahir****/

        tgl_lahir = (EditText)findViewById(R.id.tgl_lahir);
        myCalendar = Calendar.getInstance();
        dateTanggal = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                String myFormat = "yyyy-MM-dd"; //In which you need put here
                Locale localeID = new Locale("in", "ID");
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, localeID);
                tgl_lahir.setText(sdf.format(myCalendar.getTime()));
                v_tgl_lahir = sdf.format(myCalendar.getTime());

            }

        };
        tgl_lahir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(FormActivityMasuk.this, dateTanggal, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        /****datepicker tgl lahir*****/

        /**spinner Kec**/

        List<String> kecamatans = new ArrayList<String>();
        try {
            JSONArray arr = new JSONArray(loadJSONFromAsset("data_kecamatan.json"));
            for(int i=0;i<arr.length();i++)
            {


                String id_kec = arr.getJSONObject(i).getString("id");
                String kecamatan = arr.getJSONObject(i).getString("kecamatan");

                kecamatans.add(id_kec+"#"+kecamatan);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        ArrayAdapter arrayAdapterJenis= new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, kecamatans);
        arrayAdapterJenis.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerKecamatan.setAdapter(arrayAdapterJenis);
        //int posisiKec = arrayAdapterJenis.getPosition("1#Dolok Sanggul");
        spinnerKecamatan.setSelection(0);

        spinnerKecamatan.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                id_kecamatan = spinnerKecamatan.getSelectedItem().toString().split("#")[0];
                v_kecamatan = spinnerKecamatan.getSelectedItem().toString().split("#")[1];
                id_desa = ambilDesa(id_kecamatan);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        id_kecamatan = spinnerKecamatan.getSelectedItem().toString().split("#")[0];
        v_kecamatan = spinnerKecamatan.getSelectedItem().toString().split("#")[1];
        id_desa = ambilDesa(id_kecamatan);
        /**spinner Kec**/





        /** datetime sekarang **/
        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        final String tgl_updatenya = df.format(c);
        /** datetime sekarang **/

        System.out.println("desa="+v_desa+"kec="+v_kecamatan+"tgl="+tgl_updatenya);

        btnSimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                ModelData modelData = new ModelData();

                modelData.setId_desa(id_desa);
                modelData.setId_kec(id_kecamatan);
                modelData.setId(id);
                modelData.setJenis("masuk");
                modelData.setFoto_ktp(t4_foto_ktp.getText().toString());
                modelData.setFoto_orang(t4_foto_orang.getText().toString());
                modelData.setAlamat(alamat.getText().toString());
                modelData.setDusun(dusun.getText().toString());
                modelData.setNama(nama.getText().toString());
                modelData.setKeterangan(keterangan.getText().toString());
                modelData.setLuar_hh(luar_hh.getText().toString());
                modelData.setNo_hp(no_hp.getText().toString());
                modelData.setSuhu_badan(suhu_badan.getText().toString());
                modelData.setTempat_lahir(tempat_lahir.getText().toString());
                modelData.setTgl_lahir(tgl_lahir.getText().toString());
                //modelData.setTgl_lahir(v_tgl_lahir);
                modelData.setTgl_update(tgl_updatenya);
                modelData.setV_kecamatan(v_kecamatan);
                modelData.setV_desa(v_desa);
                modelData.setSudah_sinkron("belum");

                modelData.setLat(lat);
                modelData.setLng(lng);
                modelData.setStatus("Verifikasi");

                Log.d("tgl_lahir:",modelData.getTgl_lahir());
                try {
                    new DbTransaksi(getApplicationContext()).insert(modelData);
                    //startActivity(new Intent(getApplicationContext(),FormActivityMasuk.class));

                    finish();
                    Toast.makeText(getApplicationContext(),"Berhasil menambah data.",Toast.LENGTH_SHORT).show();
                }catch (Exception e)
                {
                    System.out.println(e);
                }

            }
        });
    }



    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            //Toast.makeText(getApplicationContext(), "received", Toast.LENGTH_SHORT).show();
            Double mlat = intent.getDoubleExtra("Latitude",0);
            Double mlng = intent.getDoubleExtra("Latitude",0);
            lat = String.valueOf(mlat);
            lng = String.valueOf(mlng);
            //Toast.makeText(getApplicationContext(), lat+"-"+lng, Toast.LENGTH_SHORT).show();
        }
    };


    @Override
    public void onResume() {
        super.onResume();

        /****** ini untuk receiver ****/
        IntentFilter filter = new IntentFilter();
        filter.addAction("LokasiService");
        registerReceiver(receiver, filter);
        /****** ini untuk receiver ****/
    }

    @Override
    protected void onPause() {
        super.onPause();

        /****** ini untuk receiver ****/
        unregisterReceiver(receiver);
        /****** ini untuk receiver ****/

    }


    private String ambilDesa(String kec)
    {


        /**spinner desa**/


        List<String> desas = new ArrayList<String>();
        desas.clear();
        try {
            JSONArray arrDesa = new JSONArray(loadJSONFromAsset("data_desa.json"));
            for(int ii=0;ii<arrDesa.length();ii++)
            {

                String id_kec = arrDesa.getJSONObject(ii).getString("id_kecamatan");
                String kecamatan = arrDesa.getJSONObject(ii).getString("kecamatan");
                String id_desa = arrDesa.getJSONObject(ii).getString("id_desa");
                String desa = arrDesa.getJSONObject(ii).getString("nama_desa");


                if(arrDesa.getJSONObject(ii).getString("id_kecamatan").equals(kec))
                {
                    desas.add(id_desa+"#"+desa);
                }




            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        ArrayAdapter adapterDesa = new ArrayAdapter<String>(FormActivityMasuk.this, android.R.layout.simple_spinner_item, desas);
        adapterDesa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDesa.setAdapter(adapterDesa);
        spinnerDesa.setSelection(0);

        spinnerDesa.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                id_desa = spinnerDesa.getSelectedItem().toString().split("#")[0];
                v_desa = spinnerDesa.getSelectedItem().toString().split("#")[1];
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });



        /**spinner desa**/
        id_desa = spinnerDesa.getSelectedItem().toString().split("#")[0];
        v_desa = spinnerDesa.getSelectedItem().toString().split("#")[1];
        return id_desa;

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


    public String loadJSONFromAsset(String filenya) {
        String json = null;
        try {
            InputStream is = getApplicationContext().getAssets().open(filenya);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }





    private void dispatchTakePictureIntent(int requestCode) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File

            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "medantechno.com.covid_19.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, requestCode);

            }
        }
    }



    String currentPhotoPath;

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {

            /*
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            vImg.setImageBitmap(imageBitmap);
            */

            try {


                File imgFile = new File(currentPhotoPath);

                if (imgFile.exists()) {

                    Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());

                    /** mengecilkan Bitmap **/
                    Bitmap converetdImage = getResizedBitmap(myBitmap, 400);
                    /** mengecilkan Bitmap **/

                    /** membuat watermark **/
                    String timeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date());
                    Bitmap mark = mark(converetdImage, timeStamp);
                    /** membuat watermark **/

                    /** menyimpan Bitmap jadi file **/
                    File file = new File(imgFile.getAbsolutePath());
                    OutputStream os = new BufferedOutputStream(new FileOutputStream(file));
                    mark.compress(Bitmap.CompressFormat.JPEG, 100, os);
                    os.close();
                    /** menyimpan Bitmap jadi file **/

                    //vImg.setImageBitmap(mark);
                    //foto_ktp.setImageBitmap(mark);

                    System.out.println(currentPhotoPath);
                    //t4_foto_ktp.setText(currentPhotoPath);

                    galleryAddPic();


                    //mInputPhotonya1.setText(mCurrentPhotoPath);

                    /** image to base64 ***/
                    Bitmap bm = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    bm.compress(Bitmap.CompressFormat.JPEG, 60, baos); //bm is the bitmap object
                    byte[] byteArrayImage = baos.toByteArray();
                    /** image to base64 ***/

                    String encodedImage = Base64.encodeToString(byteArrayImage, Base64.DEFAULT);
                    //alamat.setText(encodedImage);
                    //System.out.println(encodedImage);

                    /** base64 to image **/
                    byte[] decodedString = Base64.decode(encodedImage, Base64.DEFAULT);
                    Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                    //foto_ktp.setImageBitmap(decodedByte);
                    /** base64 to image **/

                    if(requestCode == 0)
                    {
                        foto_ktp.setImageBitmap(mark);
                        t4_foto_ktp.setText(currentPhotoPath);
                    }

                    if(requestCode==1)
                    {
                        foto_orang.setImageBitmap(mark);
                        t4_foto_orang.setText(currentPhotoPath);
                    }

                } else {
                    Log.d("xxx", "tidaak ada");
                }

            }catch (Exception e)
            {
                Toast.makeText(getApplicationContext(),"Ada kesalahan. Ulangi lagi.",Toast.LENGTH_LONG).show();



            }
        }
    }

    private void galleryAddPic() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(currentPhotoPath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        this.sendBroadcast(mediaScanIntent);
    }


    public Bitmap getResizedBitmap(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float)width / (float) height;
        if (bitmapRatio > 1) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(image, width, height, true);
    }
    //calling the method:
    //Bitmap converetdImage = getResizedBitmap(photo, 500);


    public static Bitmap mark(Bitmap src, String watermark) {
        int w = src.getWidth();
        int h = src.getHeight();
        Bitmap result = Bitmap.createBitmap(w, h, src.getConfig());
        Canvas canvas = new Canvas(result);
        canvas.drawBitmap(src, 0, 0, null);
        Paint paint = new Paint();
        paint.setColor(Color.RED);
        paint.setTextSize(12);
        paint.setAntiAlias(true);

        canvas.drawText(watermark, 20, 25, paint);

        return result;
    }

    //Requesting permission
    private void requestStoragePermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
            return;

        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            //If the user has denied the permission previously your code will come to this block
            //Here you can explain why you need this permission
            //Explain here why you need this permission
        }
        //And finally ask for the permission
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
    }


    //This method will be called when the user will tap on allow or deny
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        //Checking the request code of our request
        if (requestCode == STORAGE_PERMISSION_CODE) {

            //If permission is granted
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //Displaying a toast
                Toast.makeText(this, "Permission granted now you can read the storage", Toast.LENGTH_LONG).show();
            } else {
                //Displaying another toast if permission is not granted
                Toast.makeText(this, "Oops you just denied the permission", Toast.LENGTH_LONG).show();
            }
        }
    }


}
