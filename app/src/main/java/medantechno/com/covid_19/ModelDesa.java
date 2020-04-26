package medantechno.com.covid_19;

public class ModelDesa {
    public ModelDesa(){

    }

    public String id_desa,desa,id_kec,kec;

    public ModelDesa(String id_desa, String desa, String id_kec , String kec)
    {
        this.id_kec=id_kec;
        this.kec=kec;
        this.id_desa=desa;
        this.desa=desa;
    }

    public void setId_kec(String id_kec) {
        this.id_kec = id_kec;
    }

    public String getId_kec() {
        return id_kec;
    }

    public void setKec(String kec) {
        this.kec = kec;
    }

    public String getKec() {
        return kec;
    }

    public String toString()
    {
        return( desa );
    }

    public void setId_desa(String id_desa) {
        this.id_desa = id_desa;
    }

    public String getId_desa() {
        return id_desa;
    }

    public String getDesa() {
        return desa;
    }

    public void setDesa(String desa) {
        this.desa = desa;
    }


}
