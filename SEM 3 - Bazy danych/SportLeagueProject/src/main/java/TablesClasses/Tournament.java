package TablesClasses;

import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import org.hibernate.annotations.OptimisticLockType;
import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "turniej", schema = "sportleague", uniqueConstraints = {
        @UniqueConstraint(columnNames = "idTurnieju")})
public class Tournament {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idTurnieju", unique = true, nullable = false)
    private int tournamentId;
    @Column(name = "nazwaTurnieju", nullable = false)
    private String tournamentName;
    @Column(name = "dataTurnieju", nullable = false)
    private java.sql.Date tournamentDate;

    public java.sql.Date getTournamentDate() {
        return tournamentDate;
    }

    public void setTournamentDate(java.sql.Date tournamentDate) {
        this.tournamentDate = tournamentDate;
    }

    @Column(name = "miejsce", nullable = false)
    private String location;
    @Column(name = "dywizjaTurnieju", nullable = false)
    private String division;
    @Column(name = "idOrganizatora", nullable = false)
    private int organizerId;

    public int getOrganizerId() {
        return organizerId;
    }

    public void setOrganizerId(int organizerId) {
        this.organizerId = organizerId;
    }

    public String getDivision() {
        return division;
    }

    public void setDivision(String division) {
        this.division = division;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }



    public String getTournamentName() {
        return tournamentName;
    }

    public void setTournamentName(String tournamentName) {
        this.tournamentName = tournamentName;
    }

    public int getTournamentId() {
        return tournamentId;
    }

    public void setTournamentId(int tournamentId) {
        this.tournamentId = tournamentId;
    }
}
