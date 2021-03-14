import java.util.ArrayList;
import java.util.Iterator;

import static java.lang.Math.abs;
/** Klasa Lancuch kamieni */
class StoneChain {
    /** Wszytskie kamienie nalezace do chaina */
    ArrayList<Pair> stoneChain = new ArrayList<>();
    /** Wszystkie oddechy */
    ArrayList<Pair> liberties = new ArrayList<>();
    /** Id gracza ktory jest wlascicielem */
    int owner;

    /** Konstruktor kiedy tworze pierwszy
     * @param owner wlasciciel chaina
     * @param x wspolrzedna x kamienia
     * @param y wspolrzedna y kamienia
     */
    StoneChain (int owner, int x, int y) {
        stoneChain.add(new Pair(x, y));
        findLiberties(findNeighbours(x, y), liberties);
        this.owner = owner;
    }

    /** Konstruktor przy fakeStoneChainList
     * @param owner id wlasciciela
     */
    StoneChain (int owner) {
        this.owner = owner;
    }

    /** Znajduje wszystkich sasiadow pola
     * @param x wspolrzedna x pola ktorego szukamy sasiada
     * @param y wspolrzedna y pola ktorego szukamy sasiada
     * @return lista sasiadow
     */
     ArrayList<Pair> findNeighbours (int x, int y) {
        ArrayList<Pair> neighbours = new ArrayList<>();
        if ((x-1) >= 0) {
            neighbours.add(new Pair(x-1, y));
        }
        if ((y-1) >= 0 ) {
            neighbours.add(new Pair(x, y-1));
        }
        if ((x+1) < GameServer.gameServer.gameHandler.boardSize ) {
            neighbours.add(new Pair(x+1, y));
        }
        if ((y+1) < GameServer.gameServer.gameHandler.boardSize ) {
            neighbours.add(new Pair(x, y+1));
        }
        return neighbours;
    }

    /** Znajduje oddechy
     * @param neighbours
     * @param liberties
     */
    private void findLiberties(ArrayList<Pair> neighbours, ArrayList<Pair> liberties) {
        for (Pair pair: neighbours) {
            if (GameServer.gameServer.gameHandler.stoneLogicTable[pair.getKey()][pair.getValue()] == 0) {
                if (!listContains((pair), liberties)) {
                    liberties.add(new Pair(pair.getKey(), pair.getValue()));
                }
            }
        }
    }

    /** Czy pole jest czescia lancucha
     * @param x wspolrzedna x pola
     * @param y wspolrzedna y pola
     * @return tak lub nie
     */
    boolean isPartOfThisChain(int x, int y) {
        for (Pair field: stoneChain) {
            if ((field.getKey() == x && abs(field.getValue() - y) == 1) ||  (field.getValue() == y && abs(field.getKey() - x) == 1)) {
                if (!listContains(new Pair(x,y), stoneChain)) {
                    findLiberties(findNeighbours(x, y), liberties);
                    removeLiberty(x, y);
                    System.out.println("Found chain of: " + x + y);
                    return true;
                }
            }
        }
        return false;
    }

    /** Usun konkretny oddech
     * @param x wspolrzedna x oddechu
     * @param y wspolrzedna y oddechu
     */
    void removeLiberty (int x, int y) {
        /* dobrze ale dlugo, uzywamy remove iteratora
        for (Iterator<Pair> it = liberties.iterator(); it.hasNext();) {
            Pair pair = it.next();
            if (pair.getKey() == x && pair.getValue() == y) {
                it.remove();
            }
        }*/
        liberties.removeIf(pair -> pair.getKey() == x && pair.getValue() == y);
    }

    /** Polacz chainy
     * @param toMerge
     */
    void mergeChains(StoneChain toMerge) {
        for (Pair pair: toMerge.stoneChain) {
            this.stoneChain.add(pair);
        }
        for (Pair pair: toMerge.liberties) {
            this.liberties.add(pair);
        }
    }

    /** Zwraca oddechy sasiadom po uduszeniu */
    void restoreLibertiesToNeighbours() {
        for (Pair pair: stoneChain) {
            System.out.println("Restoring Liberties to neighbours of:" + pair.getKey() + pair.getValue());
            for (Pair neighbour: findNeighbours(pair.getKey(), pair.getValue())) {
                System.out.println("Neigbour: " + neighbour.getKey() + neighbour.getValue());
                if (!listContains(neighbour, stoneChain) && GameServer.gameServer.gameHandler.stoneLogicTable[neighbour.getKey()][neighbour.getValue()] != owner && GameServer.gameServer.gameHandler.stoneLogicTable[neighbour.getKey()][neighbour.getValue()] != 0) {
                    System.out.println("jestem w ifie w restoreliberties");
                    GameServer.gameServer.gameHandler.findStonesChain(neighbour).liberties.add(pair);
                }
            }
        }
    }

    /** Czy lista zawiera jakies pole
     * @param checkPair pole
     * @param list lista
     * @return tak lub nie
     */
    protected boolean listContains(Pair checkPair, ArrayList<Pair> list) {
        for (Pair pair: list) {
            if (pair.getKey() == checkPair.getKey() && pair.getValue() == checkPair.getValue()) {
                return true;
            }
        }
        return false;
    }
}
