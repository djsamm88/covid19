package medantechno.com.covid_19;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.chrisbanes.photoview.PhotoView;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.List;

/**
 * Created by dinaskominfokab.pakpakbharat on 07/11/18.
 */

public class AdapterPemantauan extends BaseAdapter {
    private Activity activity;
    private LayoutInflater inflater;
    private List<ModelData> modelDataList;


    public AdapterPemantauan(Activity activity, List<ModelData> modelDataList){
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
        if (convertView == null) convertView = inflater.inflate(R.layout.adapter_pemantauan, null);

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


        try {
            Picasso.with(activity).load(mData.getFoto_ktp()).into(foto_ktp);
            Picasso.with(activity).load(mData.getFoto_orang()).into(foto_orang);
        }catch (Exception e)
        {
            Log.d("load gambar","Masalah");
        }


        CheckBox cb_demam,cb_pilek,cb_batuk,cb_sakit_tenggorokan,cb_sesak_nafas;
        Button simpanPemantauan;
        EditText catatan_medis;
        cb_demam = (CheckBox)convertView.findViewById(R.id.cb_demam);
        cb_pilek = (CheckBox)convertView.findViewById(R.id.cb_pilek);
        cb_batuk = (CheckBox)convertView.findViewById(R.id.cb_batuk);
        cb_sakit_tenggorokan=(CheckBox)convertView.findViewById(R.id.cb_sakit_tenggorokan);
        cb_sesak_nafas=(CheckBox)convertView.findViewById(R.id.cb_sesak_nafas);

        catatan_medis = (EditText)convertView.findViewById(R.id.catatan_medis);

        catatan_medis.setText(mData.getCatatan_medis());


        simpanPemantauan = (Button)convertView.findViewById(R.id.simpanPemantauan);
        simpanPemantauan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println(cb_demam.isChecked());
                String kesehatan="";
                boolean sakit = false;
                if(cb_demam.isChecked())
                {
                    kesehatan +="Demam,";
                    sakit =true;
                }
                if(cb_batuk.isChecked())
                {
                    kesehatan +="Batuk,";
                    sakit=true;
                }
                if(cb_pilek.isChecked())
                {
                    kesehatan +="Pilek,";
                    sakit=true;
                }
                if(cb_sakit_tenggorokan.isChecked())
                {
                    kesehatan +="Sakit Tenggorokan,";
                    sakit=true;
                }
                if(cb_sesak_nafas.isChecked())
                {
                    kesehatan +="Sesak Nafas,";
                    sakit=true;
                }

                String kesehatan_ok;
                try {
                     kesehatan_ok = kesehatan.substring(0, kesehatan.length() - 1);
                }catch (Exception e)
                {
                    kesehatan_ok ="";
                }
                String catatan_medis_ok = catatan_medis.getText().toString();
                String id =mData.getId();
                String status="";
                if(sakit)
                {
                     status = "Sakit";
                }else{
                     status = "Selesai";
                }

                ModelData modelData = new ModelData();
                modelData.setKesehatan(kesehatan_ok);
                modelData.setCatatan_medis(catatan_medis_ok);
                modelData.setId(id);
                modelData.setStatus(status);
                modelData.setSudah_sinkron("belum");
                new DbTransaksi(activity).updateStatus(modelData);

                Toast.makeText(view.getContext(), "Status Orang Berhasil diUpdate", Toast.LENGTH_SHORT).show();
                activity.finish();


                System.out.println(kesehatan_ok+"--"+catatan_medis_ok);
            }
        });

        return convertView;

    }


    public void showImage(/*Bitmap imageUri*/String imageUri) {
        final Dialog builder = new Dialog(activity);
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

        //imageView.setImageBitmap(imageUri);
        File imgFile = new File(imageUri);
        Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
        imageView.setImageBitmap(myBitmap);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                builder.cancel();
            }
        });

        builder.addContentView(imageView, new RelativeLayout.LayoutParams(
                700,
                1100));
        builder.show();
    }
}
