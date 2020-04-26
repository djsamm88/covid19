package medantechno.com.covid_19;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.File;
import java.util.List;

/**
 * Created by dinaskominfokab.pakpakbharat on 07/11/18.
 */

public class AdapterDetailMasuk extends BaseAdapter {
    private Activity activity;
    private LayoutInflater inflater;
    private List<ModelData> modelDataList;


    public AdapterDetailMasuk(Activity activity, List<ModelData> modelDataList){
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
        if (convertView == null) convertView = inflater.inflate(R.layout.adapter_data_masuk, null);

        LinearLayout ln = (LinearLayout) convertView.findViewById(R.id.linear_klik);
        TextView nama  = (TextView) convertView.findViewById(R.id.nama);
        TextView jenis = (TextView) convertView.findViewById(R.id.jenis);
        ImageView foto_ktp = (ImageView) convertView.findViewById(R.id.foto_ktp);
        ImageView foto_orang = (ImageView) convertView.findViewById(R.id.foto_orang);
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

        sudah_sinkron.setText(mData.getSudah_sinkron());
        latlng.setText(mData.getLat()+","+mData.getLng());

        File imgFileKtp = new File(mData.getFoto_ktp());
        Bitmap bitmapKtp = BitmapFactory.decodeFile(imgFileKtp.getAbsolutePath());
        foto_ktp.setImageBitmap(bitmapKtp);

        File imgFileOrang = new File(mData.getFoto_orang());
        Bitmap bitmapOrang = BitmapFactory.decodeFile(imgFileOrang.getAbsolutePath());
        foto_orang.setImageBitmap(bitmapOrang);


        /** jika base64
        byte[] decodedStringKtp = Base64.decode(mData.getFoto_ktp(), Base64.DEFAULT);
        final Bitmap ktp = BitmapFactory.decodeByteArray(decodedStringKtp, 0, decodedStringKtp.length);
        //foto_ktp.setImageBitmap(ktp);

        byte[] decodeStringOrang = Base64.decode(mData.getFoto_orang(), Base64.DEFAULT);
        final Bitmap orang = BitmapFactory.decodeByteArray(decodeStringOrang, 0, decodeStringOrang.length);
        //foto_orang.setImageBitmap(orang);
         jika base64**/


        foto_ktp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //showImage(ktp);
                showImage(mData.getFoto_ktp());
            }
        });

        foto_orang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //showImage(orang);
                showImage(mData.getFoto_orang());
            }
        });

        /*
        judul.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(view.getContext(), ViewActivity.class);
                i.putExtra("isi",mEnde.getIsi_clean());
                i.putExtra("judul",mEnde.getJudul());
                i.putExtra("nomor","Buku Ende No."+mEnde.getNomor_ende()+" Buku Logu No."+mEnde.getNomor_logu());
                i.putExtra("gambar","https://ende.sibirong.com/logu/angka/"+mEnde.getNomor_logu()+".jpg");
                i.putExtra("keyword",keyword);
                i.putExtra("id_ende", String.valueOf(mEnde.getId_ende()));
                i.putExtra("ne",mEnde.getNomor_ende());
                i.putExtra("nl",mEnde.getNomor_logu());
                view.getContext().startActivity(i);
                System.out.println(keyword);
            }
        });

         */

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
