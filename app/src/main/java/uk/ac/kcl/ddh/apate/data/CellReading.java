package uk.ac.kcl.ddh.apate.data;

/**
 * Created by augeas on 31/05/17.
 */

import org.greenrobot.greendao.annotation.Convert;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;
import org.greenrobot.greendao.annotation.NotNull;
import java.util.Date;

@Entity
public class CellReading {

    @Id(autoincrement = true)
    @Index(unique = true)
    private Long id;

    @NotNull
    private String mcc;
    private String mnc;
    private String lac;
    private String cid;

    @NotNull
    @Index
    private java.util.Date timestamp;

    public void setTimestamp(String timestamp) {
        this.timestamp = new java.util.Date(Long.parseLong(timestamp));
    }

    @Generated(hash = 175791424)
    public CellReading(Long id, @NotNull String mcc, String mnc, String lac,
            String cid, @NotNull java.util.Date timestamp) {
        this.id = id;
        this.mcc = mcc;
        this.mnc = mnc;
        this.lac = lac;
        this.cid = cid;
        this.timestamp = timestamp;
    }

    @Generated(hash = 502185028)
    public CellReading() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMcc() {
        return this.mcc;
    }

    public void setMcc(String mcc) {
        this.mcc = mcc;
    }

    public String getMnc() {
        return this.mnc;
    }

    public void setMnc(String mnc) {
        this.mnc = mnc;
    }

    public String getLac() {
        return this.lac;
    }

    public void setLac(String lac) {
        this.lac = lac;
    }

    public String getCid() {
        return this.cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public java.util.Date getTimestamp() {
        return this.timestamp;
    }

    public void setTimestamp(java.util.Date timestamp) {
        this.timestamp = timestamp;
    }

}


