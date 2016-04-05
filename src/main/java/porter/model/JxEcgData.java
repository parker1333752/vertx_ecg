package porter.model;

import java.text.DateFormat;

/**
 * Created by parker on 2015/10/20.
 */
public class JxEcgData {
    private Short date;
    private Integer ecg;
    private Integer hs;
    private Short accx;
    private Short accy;
    private Short accz;
    private Short omegax;
    private Short omegay;
    private Short omegaz;

    public Short getDate() {
        return date;
    }

    public void setDate(Short date) {
        this.date = date;
    }

    public Integer getEcg() {
        return ecg;
    }

    public void setEcg(Integer ecg) {
        this.ecg = ecg;
    }

    public Integer getHs() {
        return hs;
    }

    public void setHs(Integer hs) {
        this.hs = hs;
    }

    public Short getAccx() {
        return accx;
    }

    public void setAccx(Short accx) {
        this.accx = accx;
    }

    public Short getAccy() {
        return accy;
    }

    public void setAccy(Short accy) {
        this.accy = accy;
    }

    public Short getAccz() {
        return accz;
    }

    public void setAccz(Short accz) {
        this.accz = accz;
    }

    public Short getOmegax() {
        return omegax;
    }

    public void setOmegax(Short omegax) {
        this.omegax = omegax;
    }

    public Short getOmegay() {
        return omegay;
    }

    public void setOmegay(Short omegay) {
        this.omegay = omegay;
    }

    public Short getOmegaz() {
        return omegaz;
    }

    public void setOmegaz(Short omegaz) {
        this.omegaz = omegaz;
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append(date);s.append(", ");
        s.append(ecg); s.append(", ");
        s.append(hs); s.append(", ");
        s.append(accx);s.append(", ");
        s.append(accy);s.append(", ");
        s.append(accz);s.append(", ");
        s.append(omegax);s.append(", ");
        s.append(omegay);s.append(", ");
        s.append(omegaz);s.append(", ");
        return s.toString();
    }
}
