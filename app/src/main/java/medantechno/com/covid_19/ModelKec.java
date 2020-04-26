package medantechno.com.covid_19;

public class ModelKec {
    public ModelKec(){

    }

    public String id_kec,kec;

    public ModelKec(String id_kec,String kec)
    {
        this.id_kec=id_kec;
        this.kec=kec;
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
        return( kec );
    }
}
