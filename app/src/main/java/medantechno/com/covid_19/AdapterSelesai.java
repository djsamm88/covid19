package medantechno.com.covid_19;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.github.chrisbanes.photoview.PhotoView;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by dinaskominfokab.pakpakbharat on 07/11/18.
 */

public class AdapterSelesai extends BaseAdapter {
    private Activity activity;
    private LayoutInflater inflater;
    private List<ModelData> modelDataList;


    public AdapterSelesai(Activity activity, List<ModelData> modelDataList){
        this.activity=activity;
        this.modelDataList=modelDataList;

    }


    @Override
    public int getCount() {
        return modelDataList.size();
    }

    @Override
    public Object getItem(int location) {
        return modelDataList.get(location);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, final ViewGroup parent) {
        if (inflater == null) inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) convertView = inflater.inflate(R.layout.adapter_selesai, null);

        LinearLayout ln = (LinearLayout) convertView.findViewById(R.id.linear_klik);
        TextView nama  = (TextView) convertView.findViewById(R.id.nama);
        TextView jenis = (TextView) convertView.findViewById(R.id.jenis);
        PhotoView foto_ktp = (PhotoView) convertView.findViewById(R.id.foto_ktp);
        PhotoView foto_orang = (PhotoView) convertView.findViewById(R.id.foto_orang);
        TextView tgl_update =(TextView)convertView.findViewById(R.id.tgl_update);

        TextView no_hp =(TextView)convertView.findViewById(R.id.no_hp);
        TextView ttl =(TextView)convertView.findViewById(R.id.ttl);
        TextView dari =(TextView)convertView.findViewById(R.id.luar_hh);
        TextView suhu_badan =(TextView)convertView.findViewById(R.id.suhu_badan);
        TextView keterangan =(TextView)convertView.findViewById(R.id.keterangan);
        TextView alamat =(TextView)convertView.findViewById(R.id.alamat);
        TextView v_lokasi =(TextView)convertView.findViewById(R.id.v_lokasi);

        TextView sudah_sinkron = (TextView)convertView.findViewById(R.id.sudah_sinkron);
        TextView latlng = (TextView)convertView.findViewById(R.id.latlng);

        TextView kesehatan = (TextView)convertView.findViewById(R.id.kesehatan);
        TextView catatan_medis = (TextView)convertView.findViewById(R.id.catatan_medis);


        final ModelData mData = modelDataList.get(position);

        nama.setText(mData.getNama());
        jenis.setText(mData.getJenis());
        tgl_update.setText(mData.getTgl_update());
        no_hp.setText(mData.getNo_hp());
        ttl.setText(mData.getTempat_lahir()+", "+mData.getTgl_lahir());
        dari.setText(mData.getLuar_hh());
        suhu_badan.setText(mData.getSuhu_badan());
        keterangan.setText(mData.getKeterangan());
        alamat.setText(mData.getAlamat());
        v_lokasi.setText(mData.getDusun()+", "+mData.getV_desa()+", "+mData.getV_kecamatan());

        sudah_sinkron.setText(mData.getSudah_sinkron()+"-"+mData.getStatus());
        latlng.setText(mData.getLat()+","+mData.getLng());


        kesehatan.setText(mData.getKesehatan());
        catatan_medis.setText(mData.getCatatan_medis());




        foto_ktp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showImage(mData.getFoto_ktp());
            }
        });


        foto_orang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showImage(mData.getFoto_orang());
            }
        });

        try {
            Picasso.with(activity).load(mData.getFoto_ktp()).into(foto_ktp);
            Picasso.with(activity).load(mData.getFoto_orang()).into(foto_orang);
        }catch (Exception e)
        {
            Log.d("load gambar","Masalah");
        }


        TextView sinnerTindakLanjut;
        sinnerTindakLanjut =(TextView) convertView.findViewById(R.id.sinnerTindakLanjut);
        sinnerTindakLanjut.setText(mData.getStatus_akhir());


        return convertView;

    }


    public void showImage(String url) {
        Dialog builder = new Dialog(activity);
        builder.requestWindowFeature(Window.FEATURE_NO_TITLE);
        builder.getWindow().setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.TRANSPARENT));
        builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                //nothing;

            }
        });

        ImageView imageView = new ImageView(activity);

        try {
            Picasso.with(activity)
                    .load(url)
                    .into(imageView);
        }catch (Exception e){

        }

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                builder.cancel();
            }
        });
/*** mengambil ukuran layar hp ****/
        DisplayMetrics displayMetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = displayMetrics.widthPixels;
        int height = displayMetrics.heightPixels;
        /*** mengambil ukuran layar hp ****/

        builder.addContentView(imageView, new RelativeLayout.LayoutParams(
                width,
                height));

        builder.show();
    }
}
