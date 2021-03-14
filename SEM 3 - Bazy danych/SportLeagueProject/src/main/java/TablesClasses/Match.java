package TablesClasses;

import javax.persistence.*;

@Entity
@Table(name = "mecz")
public class Match {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idMeczu", unique = true, nullable = false)
    private int idMeczu;

    @Column(name = "idDruzynyPierwszej", nullable = false)
    private int idDruzynyPierwszej;

    @Column(name = "idDruzynyDrugiej", nullable = false)
    private int idDruzynyDrugiej;

    @Column(name = "idTurnieju", nullable = false)
    private int idTurnieju;

    @Column(name = "punktyDruzynyPierwszej")
    private int punktyDruzynyPierwszej;

    @Column(name = "punktyDruzynyDrugiej")
    private int punktyDruzynyDrugiej;
    @Column(name = "czyZakonczony", nullable = false)
    private boolean status;

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public int getIdMeczu() { return idMeczu; }

    public void setIdMeczu(int idMeczu) { this.idMeczu = idMeczu; }

    public int getIdTurnieju() { return idTurnieju; }

    public void setIdTurnieju(int idTurnieju) {
        this.idTurnieju = idTurnieju;
    }

    public int getPunktyDruzynyDrugiej() {
        return punktyDruzynyDrugiej;
    }

    public void setPunktyDruzynyDrugiej(int punktyDruzynyDrugiej) {
        this.punktyDruzynyDrugiej = punktyDruzynyDrugiej;
    }

    public int getPunktyDruzynyPierwszej() {
        return punktyDruzynyPierwszej;
    }

    public void setPunktyDruzynyPierwszej(int punktyDruzynyPierwszej) {
        this.punktyDruzynyPierwszej = punktyDruzynyPierwszej;
    }

    public int getIdDruzynyPierwszej() {
        return idDruzynyPierwszej;
    }

    public void setIdDruzynyPierwszej(int idDruzynyPierwszej) {
        this.idDruzynyPierwszej = idDruzynyPierwszej;
    }

    public int getIdDruzynyDrugiej() {
        return idDruzynyDrugiej;
    }

    public void setIdDruzynyDrugiej(int idDruzynyDrugiej) {
        this.idDruzynyDrugiej = idDruzynyDrugiej;
    }
}
