package medantechno.com.covid_19;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

public class DetailActivity extends AppCompatActivity {
    private AdapterDetailMasuk adapterDetailMasuk;
    private AdapterDetailKeluar adapterDetailKeluar;
    private List<ModelData> modelDataList = new ArrayList<>();
    private ListView listView;
    private ProgressDialog pDialog;
    SwipeRefreshLayout swipe;
    String jenis;
    TextView v_judul;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail);

        Intent i= getIntent();
        Bundle j = i.getExtras();
        jenis = j.getString("jenis");

        v_judul = (TextView)findViewById(R.id.v_judul);
        v_judul.setText("Data "+jenis+" tersimpan.");

        listView = (ListView) findViewById(R.id.list);
        listData(jenis);

        swipe = findViewById(R.id.swipe);
        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                listData(jenis);
                swipe.setRefreshing(false);
            }
        });



    }


    private void listData(String jenis)
    {

        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.show();

        if(jenis.equals("masuk"))
        {
            adapterDetailMasuk = new AdapterDetailMasuk(this,modelDataList);
            listView.setAdapter(adapterDetailMasuk);
            adapterDetailMasuk.notifyDataSetChanged();
        }

        if(jenis.equals("keluar"))
        {
            adapterDetailKeluar = new AdapterDetailKeluar(this,modelDataList);
            listView.setAdapter(adapterDetailKeluar);
            adapterDetailKeluar.notifyDataSetChanged();
        }



        hidePDialog();

        DbTransaksi dbTransaksi = new DbTransaksi(getApplicationContext());
        List<ModelData> transaksis = dbTransaksi.by_jenis(jenis);
        modelDataList.clear();

        for(ModelData trx:transaksis)
        {
            ModelData modelData = new ModelData();
            //int id,String jenis,double jumlah,String tanggal,String ket

            modelData.setJenis(trx.getJenis());
            modelData.setId(trx.getId());
            modelData.setFoto_ktp(trx.getFoto_ktp());
            modelData.setFoto_orang(trx.getFoto_orang());
            modelData.setJenis(trx.getJenis());
            modelData.setNama(trx.getNama());
            modelData.setTgl_update(trx.getTgl_update());
            modelData.setV_desa(trx.getV_desa());
            modelData.setV_kecamatan(trx.getV_kecamatan());
            modelData.setDusun(trx.getDusun());
            modelData.setAlamat(trx.getAlamat());
            modelData.setTgl_lahir(trx.getTgl_lahir());
            modelData.setTempat_lahir(trx.getTempat_lahir());
            modelData.setSuhu_badan(trx.getSuhu_badan());
            modelData.setNo_hp(trx.getNo_hp());
            modelData.setKeterangan(trx.getKeterangan());
            modelData.setTgl_lahir(trx.getTgl_lahir());
            modelData.setLuar_hh(trx.getLuar_hh());
            modelData.setSudah_sinkron(trx.getSudah_sinkron());
            modelData.setLat(trx.getLat());
            modelData.setLng(trx.getLng());


            System.out.println(trx.getSudah_sinkron());
            modelDataList.add(modelData);

        }

        if(transaksis.size()==0)
        {
            Toast.makeText(getApplicationContext(),"Belum ada transaksi",Toast.LENGTH_LONG).show();
        }

        if(jenis.equals("masuk")) {
            adapterDetailMasuk.notifyDataSetChanged();
        }

        if(jenis.equals("keluar")) {
            adapterDetailKeluar.notifyDataSetChanged();
        }

    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        hidePDialog();
    }

    private void hidePDialog() {
        if (pDialog != null) {
            pDialog.dismiss();
            pDialog = null;
        }
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




}
