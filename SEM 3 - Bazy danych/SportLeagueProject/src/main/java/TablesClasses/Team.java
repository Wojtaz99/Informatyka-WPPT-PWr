package TablesClasses;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import org.hibernate.annotations.OptimisticLockType;
import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "druzyna", schema = "sportleague", uniqueConstraints = {
        @UniqueConstraint(columnNames = "idDruzyny")})
public class Team {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idDruzyny", unique = true, nullable = false)
    private int teamId;
    @Column(name = "zwyciestwa", nullable = false)
    private int wins;
    @Column(name = "remisy", nullable = false)
    private int draws;
    @Column(name = "porazki", nullable = false)
    private int losts;
    @Column(name = "zdobytePunkty", nullable = false)
    private int scoredPoints;
    @Column(name = "idUzytkownikaKapitana", nullable = false)
    private int capitanUserId;

    public int getCapitanUserId() {
        return capitanUserId;
    }

    public void setCapitanUserId(int capitanUserId) {
        this.capitanUserId = capitanUserId;
    }

    public int getScoredPoints() {
        return scoredPoints;
    }

    public void setScoredPoints(int scoredPoints) {
        this.scoredPoints = scoredPoints;
    }

    public int getLosts() {
        return losts;
    }

    public void setLosts(int losts) {
        this.losts = losts;
    }

    public int getDraws() {
        return draws;
    }

    public void setDraws(int draws) {
        this.draws = draws;
    }

    public int getWins() {
        return wins;
    }

    public void setWins(int wins) {
        this.wins = wins;
    }

    public int getTeamId() {
        return teamId;
    }

    public void setTeamId(int id) {
       teamId = id;
    }

    @Column(name = "nazwaDruzyny", nullable = false)
    private String teamName;

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String name) {
        teamName = name;
    }

    @Column(name = "miasto", nullable = false)
    private String city;

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    @Column(name = "rokZalozenia", nullable = false)
    private int foundationYear;

    public int getFoundationYear() {
        return foundationYear;
    }

    public void setFoundationYear(int year) {
        foundationYear = year;
    }

    @Column(name = "dywizja", nullable = false)
    private String division;

    public String getDivision() {
        return division;
    }

    public void setDivision(String division) {
        this.division = division;
    }

    public Team() {
        super();
    }

}

