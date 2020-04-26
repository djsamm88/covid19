package medantechno.com.covid_19;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;


public class DbTransaksi extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 10;

    private static final String DATABASE_NAME = "db_covid_keluar_masuk.db";

    private static final String table = "tbl_data";

    private static final String id = "id";
    private static final String nama = "nama";
    private static final String tempat_lahir = "tempat_lahir";
    private static final String tgl_lahir = "tgl_lahir";
    private static final String alamat = "alamat";
    private static final String luar_hh = "luar_hh";
    private static final String id_kec = "id_kec";
    private static final String id_desa = "id_desa";
    private static final String dusun = "dusun";
    private static final String no_hp = "no_hp";
    private static final String suhu_badan = "suhu_badan";
    private static final String keterangan = "keterangan";
    private static final String foto_ktp = "foto_ktp";
    private static final String foto_orang = "foto_orang";
    private static final String jenis = "jenis";
    private static final String kesehatan = "kesehatan";
    private static final String lat = "lat";
    private static final String lng = "lng";
    private static final String tgl_update = "tgl_update";
    private static final String v_kecamatan = "v_kecamatan";
    private static final String v_desa = "v_desa";
    private static final String sudah_sinkron = "sudah_sinkron";
    private static final String status = "status";

    private static final String catatan_medis = "catatan_medis";
    private static final String status_akhir = "status_akhir";




    public DbTransaksi(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    // membuat Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CONTACTS_TABLE = "CREATE TABLE  "+table+" ("+id+" TEXT PRIMARY KEY ,"+nama+" TEXT,"+tempat_lahir+" TEXT,"+tgl_lahir+" TEXT,"+alamat+" TEXT,"+luar_hh+" TEXT,"+id_kec+" TEXT, "+id_desa+" TEXT,"+dusun+" TEXT, "+no_hp+" TEXT ,"+suhu_badan+" TEXT, "+keterangan+" TEXT,"+foto_ktp+" TEXT, "+foto_orang+" TEXT,"+jenis+" TEXT,"+kesehatan+" TEXT,"+lat+" TEXT,"+lng+" TEXT, "+tgl_update+" TEXT, "+v_desa+" TEXT, "+v_kecamatan+" TEXT, "+sudah_sinkron+" TEXT,"+status+" TEXT,"+catatan_medis+" TEXT,"+status_akhir+" TEXT)";
        db.execSQL(CREATE_CONTACTS_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS "+table);

        // Create tables again
        onCreate(db);
    }



    public void insert(ModelData modelData)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        // keterangan,foto_ktp,foto_orang,jenis,kesehatan,lat,lng;
        values.put(id,modelData.getId());
        values.put(nama,modelData.getNama());
        values.put(tempat_lahir,modelData.getTempat_lahir());
        values.put(alamat,modelData.getAlamat());
        values.put(luar_hh,modelData.getLuar_hh());
        values.put(id_kec,modelData.getId_kec());
        values.put(id_desa,modelData.getId_desa());
        values.put(dusun,modelData.getDusun());
        values.put(no_hp,modelData.getNo_hp());
        values.put(suhu_badan,modelData.getSuhu_badan());
        values.put(keterangan,modelData.getKeterangan());
        values.put(foto_ktp,modelData.getFoto_ktp());
        values.put(foto_orang,modelData.getFoto_orang());
        values.put(jenis,modelData.getJenis());
        values.put(kesehatan,modelData.getKesehatan());
        values.put(lat,modelData.getLat());
        values.put(lng,modelData.getLng());
        values.put(v_desa,modelData.getV_desa());
        values.put(v_kecamatan,modelData.getV_kecamatan());
        values.put(tgl_update,modelData.getTgl_update());
        values.put(tgl_lahir,modelData.getTgl_lahir());
        values.put(sudah_sinkron,modelData.getSudah_sinkron());
        values.put(status,modelData.getStatus());
        values.put(catatan_medis,modelData.getCatatan_medis());
        values.put(status_akhir,modelData.getStatus_akhir());


        //db.insertOrThrow(table, null, values);
        System.out.println(modelData.getV_kecamatan());
        db.insert(table,null,values);
        db.close();

        System.out.println(modelData.getId());
    }



    public void update(ModelData modelData)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(id,modelData.getId());
        values.put(nama,modelData.getNama());
        values.put(tempat_lahir,modelData.getTempat_lahir());
        values.put(alamat,modelData.getAlamat());
        values.put(luar_hh,modelData.getLuar_hh());
        values.put(id_kec,modelData.getId_kec());
        values.put(id_desa,modelData.getId_desa());
        values.put(dusun,modelData.getDusun());
        values.put(no_hp,modelData.getNo_hp());
        values.put(suhu_badan,modelData.getSuhu_badan());
        values.put(keterangan,modelData.getKeterangan());
        values.put(foto_ktp,modelData.getFoto_ktp());
        values.put(foto_orang,modelData.getFoto_orang());
        values.put(jenis,modelData.getJenis());
        values.put(kesehatan,modelData.getKesehatan());
        values.put(lat,modelData.getLat());
        values.put(lng,modelData.getLng());
        values.put(tgl_update,modelData.getTgl_update());
        values.put(v_desa,modelData.getV_desa());
        values.put(v_kecamatan,modelData.getV_kecamatan());
        values.put(tgl_lahir,modelData.getTgl_lahir());
        values.put(sudah_sinkron,modelData.getSudah_sinkron());
        values.put(status,modelData.getStatus());
        values.put(catatan_medis,modelData.getCatatan_medis());
        values.put(status_akhir,modelData.getStatus_akhir());





        db.update(table, values, id+"=?",new String[]{String.valueOf(modelData.getId())});
        //db.insert(table,null,values);
        db.close();
    }



    public void updateStatusSinkron(ModelData modelData)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(id,modelData.getId());
        values.put(sudah_sinkron,modelData.getSudah_sinkron());
        db.update(table, values, id+"=?",new String[]{String.valueOf(modelData.getId())});
        //db.insert(table,null,values);
        db.close();
    }


    public void updateStatus(ModelData modelData)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(id,modelData.getId());
        values.put(status,modelData.getStatus());
        values.put(kesehatan,modelData.getKesehatan());
        values.put(catatan_medis,modelData.getCatatan_medis());
        values.put(sudah_sinkron,modelData.getSudah_sinkron());
        db.update(table, values, id+"=?",new String[]{String.valueOf(modelData.getId())});
        //db.insert(table,null,values);
        db.close();
    }



    public void updateStatusAkhir(ModelData modelData)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(id,modelData.getId());
        values.put(status_akhir,modelData.getStatus_akhir());
        values.put(sudah_sinkron,modelData.getSudah_sinkron());
        db.update(table, values, id+"=?",new String[]{String.valueOf(modelData.getId())});
        //db.insert(table,null,values);
        db.close();
    }

    public List<ModelData> getAll() {
        List<ModelData> semuanya = new ArrayList<ModelData>();
        String selectQuery = "SELECT  * FROM " + table + " ORDER BY DATETIME("+tgl_update+") DESC";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);


        if (cursor.moveToFirst()) {
            do {
                ModelData modelData = new ModelData();
                modelData.setId(cursor.getString(cursor.getColumnIndex(id)));
                modelData.setNama(cursor.getString(cursor.getColumnIndex(nama)));
                modelData.setTempat_lahir(cursor.getString(cursor.getColumnIndex(tempat_lahir)));
                modelData.setTgl_lahir(cursor.getString(cursor.getColumnIndex(tgl_lahir)));
                modelData.setAlamat(cursor.getString(cursor.getColumnIndex(alamat)));
                modelData.setLuar_hh(cursor.getString(cursor.getColumnIndex(luar_hh)));
                modelData.setId_kec(cursor.getString(cursor.getColumnIndex(id_kec)));
                modelData.setId_desa(cursor.getString(cursor.getColumnIndex(id_desa)));
                modelData.setDusun(cursor.getString(cursor.getColumnIndex(dusun)));
                modelData.setNo_hp(cursor.getString(cursor.getColumnIndex(no_hp)));
                modelData.setSuhu_badan(cursor.getString(cursor.getColumnIndex(suhu_badan)));
                modelData.setKeterangan(cursor.getString(cursor.getColumnIndex(keterangan)));
                modelData.setKesehatan(cursor.getString(cursor.getColumnIndex(kesehatan)));
                modelData.setFoto_ktp(cursor.getString(cursor.getColumnIndex(foto_ktp)));
                modelData.setFoto_orang(cursor.getString(cursor.getColumnIndex(foto_orang)));
                modelData.setJenis(cursor.getString(cursor.getColumnIndex(jenis)));
                modelData.setLat(cursor.getString(cursor.getColumnIndex(lat)));
                modelData.setLng(cursor.getString(cursor.getColumnIndex(lng)));
                modelData.setTgl_update(cursor.getString(cursor.getColumnIndex(tgl_update)));
                modelData.setV_desa(cursor.getString(cursor.getColumnIndex(v_desa)));
                modelData.setV_kecamatan(cursor.getString(cursor.getColumnIndex(v_kecamatan)));
                modelData.setSudah_sinkron(cursor.getString(cursor.getColumnIndex(sudah_sinkron)));
                modelData.setLat(cursor.getString(cursor.getColumnIndex(lat)));
                modelData.setLng(cursor.getString(cursor.getColumnIndex(lng)));
                modelData.setStatus(cursor.getString(cursor.getColumnIndex(status)));
                modelData.setCatatan_medis(cursor.getString(cursor.getColumnIndex(catatan_medis)));
                modelData.setStatus_akhir(cursor.getString(cursor.getColumnIndex(status_akhir)));

                System.out.println("cursor17:"+modelData.getV_kecamatan());
                semuanya.add(modelData);
            } while (cursor.moveToNext());
        }

        return semuanya;
    }

    public List<ModelData> cari(String key) {
        List<ModelData> semuanya = new ArrayList<ModelData>();

        String selectQuery;

        selectQuery = "SELECT  * FROM " + table + " WHERE "+nama+" LIKE '%"+key+"%' ORDER BY DATE("+tgl_update+") DESC";



        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                ModelData modelData = new ModelData();
                modelData.setId(cursor.getString(cursor.getColumnIndex(id)));
                modelData.setNama(cursor.getString(cursor.getColumnIndex(nama)));
                modelData.setTempat_lahir(cursor.getString(cursor.getColumnIndex(tempat_lahir)));
                modelData.setTgl_lahir(cursor.getString(cursor.getColumnIndex(tgl_lahir)));
                modelData.setAlamat(cursor.getString(cursor.getColumnIndex(alamat)));
                modelData.setLuar_hh(cursor.getString(cursor.getColumnIndex(luar_hh)));
                modelData.setId_kec(cursor.getString(cursor.getColumnIndex(id_kec)));
                modelData.setId_desa(cursor.getString(cursor.getColumnIndex(id_desa)));
                modelData.setDusun(cursor.getString(cursor.getColumnIndex(dusun)));
                modelData.setNo_hp(cursor.getString(cursor.getColumnIndex(no_hp)));
                modelData.setSuhu_badan(cursor.getString(cursor.getColumnIndex(suhu_badan)));
                modelData.setKeterangan(cursor.getString(cursor.getColumnIndex(keterangan)));
                modelData.setKesehatan(cursor.getString(cursor.getColumnIndex(kesehatan)));
                modelData.setFoto_ktp(cursor.getString(cursor.getColumnIndex(foto_ktp)));
                modelData.setFoto_orang(cursor.getString(cursor.getColumnIndex(foto_orang)));
                modelData.setJenis(cursor.getString(cursor.getColumnIndex(jenis)));
                modelData.setLat(cursor.getString(cursor.getColumnIndex(lat)));
                modelData.setLng(cursor.getString(cursor.getColumnIndex(lng)));
                modelData.setTgl_update(cursor.getString(cursor.getColumnIndex(tgl_update)));
                modelData.setV_desa(cursor.getString(cursor.getColumnIndex(v_desa)));
                modelData.setV_kecamatan(cursor.getString(cursor.getColumnIndex(v_kecamatan)));
                modelData.setSudah_sinkron(cursor.getString(cursor.getColumnIndex(sudah_sinkron)));
                modelData.setLat(cursor.getString(cursor.getColumnIndex(lat)));
                modelData.setLng(cursor.getString(cursor.getColumnIndex(lng)));
                modelData.setStatus(cursor.getString(cursor.getColumnIndex(status)));
                modelData.setCatatan_medis(cursor.getString(cursor.getColumnIndex(catatan_medis)));
                modelData.setStatus_akhir(cursor.getString(cursor.getColumnIndex(status_akhir)));

                semuanya.add(modelData);
            } while (cursor.moveToNext());
        }

        return semuanya;
    }



    public List<ModelData> by_jenis(String key) {
        List<ModelData> semuanya = new ArrayList<ModelData>();

        String selectQuery;

        selectQuery = "SELECT  * FROM " + table + " WHERE "+jenis+" LIKE '%"+key+"%' AND status ='Verifikasi' ORDER BY DATETIME("+tgl_update+") DESC";



        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                ModelData modelData = new ModelData();
                modelData.setId(cursor.getString(cursor.getColumnIndex(id)));
                modelData.setNama(cursor.getString(cursor.getColumnIndex(nama)));
                modelData.setTempat_lahir(cursor.getString(cursor.getColumnIndex(tempat_lahir)));
                modelData.setTgl_lahir(cursor.getString(cursor.getColumnIndex(tgl_lahir)));
                modelData.setAlamat(cursor.getString(cursor.getColumnIndex(alamat)));
                modelData.setLuar_hh(cursor.getString(cursor.getColumnIndex(luar_hh)));
                modelData.setId_kec(cursor.getString(cursor.getColumnIndex(id_kec)));
                modelData.setId_desa(cursor.getString(cursor.getColumnIndex(id_desa)));
                modelData.setDusun(cursor.getString(cursor.getColumnIndex(dusun)));
                modelData.setNo_hp(cursor.getString(cursor.getColumnIndex(no_hp)));
                modelData.setSuhu_badan(cursor.getString(cursor.getColumnIndex(suhu_badan)));
                modelData.setKeterangan(cursor.getString(cursor.getColumnIndex(keterangan)));
                modelData.setKesehatan(cursor.getString(cursor.getColumnIndex(kesehatan)));
                modelData.setFoto_ktp(cursor.getString(cursor.getColumnIndex(foto_ktp)));
                modelData.setFoto_orang(cursor.getString(cursor.getColumnIndex(foto_orang)));
                modelData.setJenis(cursor.getString(cursor.getColumnIndex(jenis)));
                modelData.setLat(cursor.getString(cursor.getColumnIndex(lat)));
                modelData.setLng(cursor.getString(cursor.getColumnIndex(lng)));
                modelData.setTgl_update(cursor.getString(cursor.getColumnIndex(tgl_update)));
                modelData.setV_desa(cursor.getString(cursor.getColumnIndex(v_desa)));
                modelData.setV_kecamatan(cursor.getString(cursor.getColumnIndex(v_kecamatan)));
                modelData.setSudah_sinkron(cursor.getString(cursor.getColumnIndex(sudah_sinkron)));
                modelData.setLat(cursor.getString(cursor.getColumnIndex(lat)));
                modelData.setLng(cursor.getString(cursor.getColumnIndex(lng)));
                modelData.setStatus(cursor.getString(cursor.getColumnIndex(status)));
                modelData.setCatatan_medis(cursor.getString(cursor.getColumnIndex(catatan_medis)));
                modelData.setStatus_akhir(cursor.getString(cursor.getColumnIndex(status_akhir)));

                semuanya.add(modelData);
            } while (cursor.moveToNext());
        }

        return semuanya;
    }



    public List<ModelData> by_jenis_bukan_baru(String key) {
        List<ModelData> semuanya = new ArrayList<ModelData>();

        String selectQuery;

        selectQuery = "SELECT  * FROM " + table + " WHERE "+jenis+" LIKE '%"+key+"%' AND status IS NOT NULL ORDER BY DATETIME("+tgl_update+") DESC";



        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                ModelData modelData = new ModelData();
                modelData.setId(cursor.getString(cursor.getColumnIndex(id)));
                modelData.setNama(cursor.getString(cursor.getColumnIndex(nama)));
                modelData.setTempat_lahir(cursor.getString(cursor.getColumnIndex(tempat_lahir)));
                modelData.setTgl_lahir(cursor.getString(cursor.getColumnIndex(tgl_lahir)));
                modelData.setAlamat(cursor.getString(cursor.getColumnIndex(alamat)));
                modelData.setLuar_hh(cursor.getString(cursor.getColumnIndex(luar_hh)));
                modelData.setId_kec(cursor.getString(cursor.getColumnIndex(id_kec)));
                modelData.setId_desa(cursor.getString(cursor.getColumnIndex(id_desa)));
                modelData.setDusun(cursor.getString(cursor.getColumnIndex(dusun)));
                modelData.setNo_hp(cursor.getString(cursor.getColumnIndex(no_hp)));
                modelData.setSuhu_badan(cursor.getString(cursor.getColumnIndex(suhu_badan)));
                modelData.setKeterangan(cursor.getString(cursor.getColumnIndex(keterangan)));
                modelData.setKesehatan(cursor.getString(cursor.getColumnIndex(kesehatan)));
                modelData.setFoto_ktp(cursor.getString(cursor.getColumnIndex(foto_ktp)));
                modelData.setFoto_orang(cursor.getString(cursor.getColumnIndex(foto_orang)));
                modelData.setJenis(cursor.getString(cursor.getColumnIndex(jenis)));
                modelData.setLat(cursor.getString(cursor.getColumnIndex(lat)));
                modelData.setLng(cursor.getString(cursor.getColumnIndex(lng)));
                modelData.setTgl_update(cursor.getString(cursor.getColumnIndex(tgl_update)));
                modelData.setV_desa(cursor.getString(cursor.getColumnIndex(v_desa)));
                modelData.setV_kecamatan(cursor.getString(cursor.getColumnIndex(v_kecamatan)));
                modelData.setSudah_sinkron(cursor.getString(cursor.getColumnIndex(sudah_sinkron)));
                modelData.setLat(cursor.getString(cursor.getColumnIndex(lat)));
                modelData.setLng(cursor.getString(cursor.getColumnIndex(lng)));
                modelData.setStatus(cursor.getString(cursor.getColumnIndex(status)));
                modelData.setCatatan_medis(cursor.getString(cursor.getColumnIndex(catatan_medis)));
                modelData.setStatus_akhir(cursor.getString(cursor.getColumnIndex(status_akhir)));

                semuanya.add(modelData);
            } while (cursor.moveToNext());
        }

        return semuanya;
    }




    public List<ModelData> by_id(String id) {
        List<ModelData> semuanya = new ArrayList<ModelData>();

        String selectQuery;

        selectQuery = "SELECT  * FROM " + table + " WHERE "+id+" = '%"+id+"%' ORDER BY DATETIME("+tgl_update+") DESC LIMIT 1";



        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                ModelData modelData = new ModelData();
                modelData.setId(cursor.getString(cursor.getColumnIndex(id)));
                modelData.setNama(cursor.getString(cursor.getColumnIndex(nama)));
                modelData.setTempat_lahir(cursor.getString(cursor.getColumnIndex(tempat_lahir)));
                modelData.setTgl_lahir(cursor.getString(cursor.getColumnIndex(tgl_lahir)));
                modelData.setAlamat(cursor.getString(cursor.getColumnIndex(alamat)));
                modelData.setLuar_hh(cursor.getString(cursor.getColumnIndex(luar_hh)));
                modelData.setId_kec(cursor.getString(cursor.getColumnIndex(id_kec)));
                modelData.setId_desa(cursor.getString(cursor.getColumnIndex(id_desa)));
                modelData.setDusun(cursor.getString(cursor.getColumnIndex(dusun)));
                modelData.setNo_hp(cursor.getString(cursor.getColumnIndex(no_hp)));
                modelData.setSuhu_badan(cursor.getString(cursor.getColumnIndex(suhu_badan)));
                modelData.setKeterangan(cursor.getString(cursor.getColumnIndex(keterangan)));
                modelData.setKesehatan(cursor.getString(cursor.getColumnIndex(kesehatan)));
                modelData.setFoto_ktp(cursor.getString(cursor.getColumnIndex(foto_ktp)));
                modelData.setFoto_orang(cursor.getString(cursor.getColumnIndex(foto_orang)));
                modelData.setJenis(cursor.getString(cursor.getColumnIndex(jenis)));
                modelData.setLat(cursor.getString(cursor.getColumnIndex(lat)));
                modelData.setLng(cursor.getString(cursor.getColumnIndex(lng)));
                modelData.setTgl_update(cursor.getString(cursor.getColumnIndex(tgl_update)));
                modelData.setV_desa(cursor.getString(cursor.getColumnIndex(v_desa)));
                modelData.setV_kecamatan(cursor.getString(cursor.getColumnIndex(v_kecamatan)));
                modelData.setSudah_sinkron(cursor.getString(cursor.getColumnIndex(sudah_sinkron)));
                modelData.setStatus(cursor.getString(cursor.getColumnIndex(status)));
                modelData.setCatatan_medis(cursor.getString(cursor.getColumnIndex(catatan_medis)));
                modelData.setStatus_akhir(cursor.getString(cursor.getColumnIndex(status_akhir)));
                semuanya.add(modelData);
            } while (cursor.moveToNext());
        }

        return semuanya;
    }



    public List<ModelData> belum_sinkron() {
        List<ModelData> semuanya = new ArrayList<ModelData>();

        String selectQuery;

        selectQuery = "SELECT  * FROM " + table + " WHERE "+sudah_sinkron+" = '' OR "+sudah_sinkron+"= 'belum' OR "+sudah_sinkron+" IS NULL ORDER BY DATETIME("+tgl_update+") DESC";



        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                ModelData modelData = new ModelData();
                modelData.setId(cursor.getString(cursor.getColumnIndex(id)));
                modelData.setNama(cursor.getString(cursor.getColumnIndex(nama)));
                modelData.setTempat_lahir(cursor.getString(cursor.getColumnIndex(tempat_lahir)));
                modelData.setTgl_lahir(cursor.getString(cursor.getColumnIndex(tgl_lahir)));
                modelData.setAlamat(cursor.getString(cursor.getColumnIndex(alamat)));
                modelData.setLuar_hh(cursor.getString(cursor.getColumnIndex(luar_hh)));
                modelData.setId_kec(cursor.getString(cursor.getColumnIndex(id_kec)));
                modelData.setId_desa(cursor.getString(cursor.getColumnIndex(id_desa)));
                modelData.setDusun(cursor.getString(cursor.getColumnIndex(dusun)));
                modelData.setNo_hp(cursor.getString(cursor.getColumnIndex(no_hp)));
                modelData.setSuhu_badan(cursor.getString(cursor.getColumnIndex(suhu_badan)));
                modelData.setKeterangan(cursor.getString(cursor.getColumnIndex(keterangan)));
                modelData.setFoto_ktp(cursor.getString(cursor.getColumnIndex(foto_ktp)));
                modelData.setFoto_orang(cursor.getString(cursor.getColumnIndex(foto_orang)));
                modelData.setJenis(cursor.getString(cursor.getColumnIndex(jenis)));
                modelData.setLat(cursor.getString(cursor.getColumnIndex(lat)));
                modelData.setLng(cursor.getString(cursor.getColumnIndex(lng)));
                modelData.setTgl_update(cursor.getString(cursor.getColumnIndex(tgl_update)));
                modelData.setV_desa(cursor.getString(cursor.getColumnIndex(v_desa)));
                modelData.setKesehatan(cursor.getString(cursor.getColumnIndex(kesehatan)));
                modelData.setV_kecamatan(cursor.getString(cursor.getColumnIndex(v_kecamatan)));
                modelData.setSudah_sinkron(cursor.getString(cursor.getColumnIndex(sudah_sinkron)));
                modelData.setStatus(cursor.getString(cursor.getColumnIndex(status)));
                modelData.setCatatan_medis(cursor.getString(cursor.getColumnIndex(catatan_medis)));
                modelData.setStatus_akhir(cursor.getString(cursor.getColumnIndex(status_akhir)));

                semuanya.add(modelData);
            } while (cursor.moveToNext());
        }

        return semuanya;
    }




    public List<ModelData> by_status(String key) {
        List<ModelData> semuanya = new ArrayList<ModelData>();

        String selectQuery;

        selectQuery = "SELECT  * FROM " + table + " WHERE "+status+" = '"+key+"' ORDER BY DATETIME("+tgl_update+") DESC";



        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                ModelData modelData = new ModelData();
                modelData.setId(cursor.getString(cursor.getColumnIndex(id)));
                modelData.setNama(cursor.getString(cursor.getColumnIndex(nama)));
                modelData.setTempat_lahir(cursor.getString(cursor.getColumnIndex(tempat_lahir)));
                modelData.setTgl_lahir(cursor.getString(cursor.getColumnIndex(tgl_lahir)));
                modelData.setAlamat(cursor.getString(cursor.getColumnIndex(alamat)));
                modelData.setLuar_hh(cursor.getString(cursor.getColumnIndex(luar_hh)));
                modelData.setId_kec(cursor.getString(cursor.getColumnIndex(id_kec)));
                modelData.setId_desa(cursor.getString(cursor.getColumnIndex(id_desa)));
                modelData.setDusun(cursor.getString(cursor.getColumnIndex(dusun)));
                modelData.setNo_hp(cursor.getString(cursor.getColumnIndex(no_hp)));
                modelData.setSuhu_badan(cursor.getString(cursor.getColumnIndex(suhu_badan)));
                modelData.setKeterangan(cursor.getString(cursor.getColumnIndex(keterangan)));
                modelData.setFoto_ktp(cursor.getString(cursor.getColumnIndex(foto_ktp)));
                modelData.setFoto_orang(cursor.getString(cursor.getColumnIndex(foto_orang)));
                modelData.setKesehatan(cursor.getString(cursor.getColumnIndex(kesehatan)));
                modelData.setJenis(cursor.getString(cursor.getColumnIndex(jenis)));
                modelData.setLat(cursor.getString(cursor.getColumnIndex(lat)));
                modelData.setLng(cursor.getString(cursor.getColumnIndex(lng)));
                modelData.setTgl_update(cursor.getString(cursor.getColumnIndex(tgl_update)));
                modelData.setV_desa(cursor.getString(cursor.getColumnIndex(v_desa)));
                modelData.setV_kecamatan(cursor.getString(cursor.getColumnIndex(v_kecamatan)));
                modelData.setSudah_sinkron(cursor.getString(cursor.getColumnIndex(sudah_sinkron)));
                modelData.setStatus(cursor.getString(cursor.getColumnIndex(status)));
                modelData.setCatatan_medis(cursor.getString(cursor.getColumnIndex(catatan_medis)));
                modelData.setStatus_akhir(cursor.getString(cursor.getColumnIndex(status_akhir)));

                semuanya.add(modelData);
            } while (cursor.moveToNext());
        }

        return semuanya;
    }




    public void delete(String idnya)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from "+table+" where "+id+"='"+idnya+"'");

    }


    public static boolean isNumeric(String str)
    {
        try
        {
            double d = Double.parseDouble(str);
        }
        catch(NumberFormatException nfe)
        {
            return false;
        }
        return true;
    }



}
