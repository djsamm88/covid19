package medantechno.com.covid_19;

public class ModelData {

    private String
            id,
            nama,
            tempat_lahir,
            tgl_lahir,
            alamat,
            luar_hh,
            id_kec,
            id_desa,
            dusun,
            no_hp,
            suhu_badan,
            keterangan,
            foto_ktp,
            foto_orang,
            jenis,
            kesehatan,
            lat,
            lng,
            tgl_update,
            v_kecamatan,
            v_desa,
            sudah_sinkron,
            status,
            catatan_medis,
            status_akhir;

    public ModelData()
    {

    }


    public ModelData(
                String id,
                String nama,
                String tempat_lahir,
                String tgl_lahir,
                String alamat,
                String luar_hh,
                String id_kec,
                String id_desa,
                String dusun,
                String no_hp,
                String suhu_badan,
                String keterangan,
                String foto_ktp,
                String foto_orang,
                String jenis,
                String kesehatan,
                String lat,
                String lng,
                String tgl_update,
                String v_desa,
                String v_kecamatan,
                String sudah_sinkron,
                String status,
                String catatan_medis,
                String status_akhir
                )
    {
            this.id=id;
            this.nama=nama;
            this.tempat_lahir=tempat_lahir;
            this.tgl_lahir=tgl_lahir;
            this.alamat=alamat;
            this.luar_hh=luar_hh;
            this.id_kec=id_kec;
            this.id_desa=id_desa;
            this.dusun=dusun;
            this.no_hp=no_hp;
            this.suhu_badan=suhu_badan;
            this.keterangan=keterangan;
            this.foto_ktp=foto_ktp;
            this.foto_orang=foto_orang;
            this.jenis=jenis;
            this.kesehatan=kesehatan;
            this.lat=lat;
            this.lng=lng;
            this.tgl_update=tgl_update;
            this.v_desa=v_desa;
            this.v_kecamatan=v_kecamatan;
            this.sudah_sinkron=sudah_sinkron;
            this.status=status;
            this.catatan_medis=catatan_medis;
            this.status_akhir=status_akhir;


    }

    public String getStatus_akhir() {
        return status_akhir;
    }

    public void setStatus_akhir(String status_akhir) {
        this.status_akhir = status_akhir;
    }

    public String getCatatan_medis() {
        return catatan_medis;
    }

    public void setCatatan_medis(String catatan_medis) {
        this.catatan_medis = catatan_medis;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSudah_sinkron() {
        return sudah_sinkron;
    }

    public void setSudah_sinkron(String sudah_sinkron) {
        this.sudah_sinkron = sudah_sinkron;
    }

    public String getV_desa() {
        return v_desa;
    }

    public String getV_kecamatan() {
        return v_kecamatan;
    }

    public void setV_desa(String v_desa) {
        this.v_desa = v_desa;
    }

    public void setV_kecamatan(String v_kecamatan) {
        this.v_kecamatan = v_kecamatan;
    }

    public String getTgl_update() {
        return tgl_update;
    }

    public void setTgl_update(String tgl_update) {
        this.tgl_update = tgl_update;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAlamat() {
        return alamat;
    }

    public String getId_kec() {
        return id_kec;
    }

    public String getLuar_hh() {
        return luar_hh;
    }

    public String getNama() {
        return nama;
    }

    public String getTempat_lahir() {
        return tempat_lahir;
    }

    public String getTgl_lahir() {
        return tgl_lahir;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }

    public void setId_kec(String id_kec) {
        this.id_kec = id_kec;
    }

    public void setLuar_hh(String luar_hh) {
        this.luar_hh = luar_hh;
    }

    public String getId_desa() {
        return id_desa;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public void setTempat_lahir(String tempat_lahir) {
        this.tempat_lahir = tempat_lahir;
    }

    public void setId_desa(String id_desa) {
        this.id_desa = id_desa;
    }

    public void setTgl_lahir(String tgl_lahir) {
        this.tgl_lahir = tgl_lahir;
    }

    public void setJenis(String jenis) {
        this.jenis = jenis;
    }

    public String getJenis() {
        return jenis;
    }

    public String getDusun() {
        return dusun;
    }

    public String getFoto_ktp() {
        return foto_ktp;
    }

    public String getFoto_orang() {
        return foto_orang;
    }

    public String getKesehatan() {
        return kesehatan;
    }

    public String getKeterangan() {
        return keterangan;
    }

    public String getLat() {
        return lat;
    }

    public String getLng() {
        return lng;
    }

    public String getNo_hp() {
        return no_hp;
    }

    public String getSuhu_badan() {
        return suhu_badan;
    }

    public void setDusun(String dusun) {
        this.dusun = dusun;
    }

    public void setFoto_ktp(String foto_ktp) {
        this.foto_ktp = foto_ktp;
    }

    public void setFoto_orang(String foto_orang) {
        this.foto_orang = foto_orang;
    }

    public void setKesehatan(String kesehatan) {
        this.kesehatan = kesehatan;
    }

    public void setKeterangan(String keterangan) {
        this.keterangan = keterangan;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public void setNo_hp(String no_hp) {
        this.no_hp = no_hp;
    }

    public void setSuhu_badan(String suhu_badan) {
        this.suhu_badan = suhu_badan;
    }


}
