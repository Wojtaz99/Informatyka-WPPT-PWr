import org.hibernate.annotations.OptimisticLockType;

import javax.persistence.*;
import java.util.Date;

@Entity
//@org.hibernate.annotations.Entity(optimisticLock = OptimisticLockType.ALL)
@Table(name = "savedgames", uniqueConstraints = {
        @UniqueConstraint(columnNames = "id") })
public class SavedGames {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private int id;

    @Column(name = "gameDate", unique = true)
    private Date date;

    public SavedGames() {
        super();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
