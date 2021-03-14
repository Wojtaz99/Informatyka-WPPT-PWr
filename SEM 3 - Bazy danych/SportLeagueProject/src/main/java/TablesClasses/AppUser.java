package TablesClasses;

import javax.persistence.*;

@Entity
@Table(name = "uzytkownikaplikacji", uniqueConstraints = {
        @UniqueConstraint(columnNames = "idUzytkownika"),
        @UniqueConstraint(columnNames = "login"),
        @UniqueConstraint(columnNames = "pesel")
})
public class AppUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idUzytkownika", unique = true, nullable = false)
    private int idUzytkownika;

    @Column(name = "haslo", nullable = false)
    private String haslo;

    @Column(name = "imie", nullable = false)
    private String imie;

    @Column(name = "nazwisko", nullable = false)
    private String nazwisko;

    @Column(name = "pesel", unique = true, nullable = false)
    private String pesel;

    @Column(name = "poziomUprawnien", nullable = false)
    private int poziomUprawnien;

    @Column(name = "login", unique = true, nullable = false)
    private String login;

    @Column(name = "czyZatwierdzony", nullable = false)
    private boolean czyZatwierdzony;

    public boolean isCzyZatwierdzony() {
        return czyZatwierdzony;
    }

    public void setCzyZatwierdzony(boolean czyZatwierdzony) {
        this.czyZatwierdzony = czyZatwierdzony;
    }

    public int getPoziomUprawnien() {
        return poziomUprawnien;
    }

    public void setPoziomUprawnien(int poziomUprawnien) {
        this.poziomUprawnien = poziomUprawnien;
    }

    public String getPesel() {
        return pesel;
    }

    public void setPesel(String pesel) {
        this.pesel = pesel;
    }

    public String getNazwisko() {
        return nazwisko;
    }

    public void setNazwisko(String nazwisko) {
        this.nazwisko = nazwisko;
    }

    public String getImie() {
        return imie;
    }

    public void setImie(String imie) {
        this.imie = imie;
    }

    public String getHaslo() {
        return haslo;
    }

    public void setHaslo(String haslo) {
        this.haslo = haslo;
    }

    public int getIdUzytkownika() {
        return idUzytkownika;
    }

    public void setIdUzytkownika(int idUzytkownika) {
        this.idUzytkownika = idUzytkownika;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }


}
