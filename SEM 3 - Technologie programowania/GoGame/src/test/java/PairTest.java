import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PairTest {

    Pair pair;

    @BeforeEach
    void setUp() {
        pair = new Pair(2,3);
    }

    @Test
    void getKey() {
        assertEquals(2, pair.getKey());
    }

    @Test
    void getValue() {
        assertEquals(3, pair.getValue());
    }
}