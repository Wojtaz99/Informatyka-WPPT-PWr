import org.junit.Before;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.ArrayList;

class StoneChainTest {

    @Test
    void listContains() {
        ArrayList<Pair> list = new ArrayList<>();
        StoneChain stoneChain = new StoneChain(1);
        list.add(new Pair(1,2));
        list.add(new Pair(10, 9));
        list.add(new Pair (6, 0));

        assertTrue(stoneChain.listContains(new Pair(6,0), list));
        assertFalse(stoneChain.listContains(new Pair(1, 9), list));
    }

}
