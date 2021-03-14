import org.hibernate.annotations.OptimisticLockType;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "onegame", uniqueConstraints = {
        @UniqueConstraint(columnNames = "id")})
public class OneGame {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Column(name = "gameId", nullable = false)
    private int gameId;

    @Column(name = "moveId", nullable = false)
    private int moveId;

    @Column(name = "moveString", nullable = false)
    private String moveString;

    public OneGame() {
        super();
    }

    public int getGameId() {
        return gameId;
    }

    public void setGameId(int id) {
        this.gameId = id;
    }

    public int getMoveId() {
        return moveId;
    }

    public void setMoveId(int id) {
        this.moveId = id;
    }

    public String getMoveString() {
        return moveString;
    }

    public void setMoveString(String moveString) {
        this.moveString = moveString;
    }
}
