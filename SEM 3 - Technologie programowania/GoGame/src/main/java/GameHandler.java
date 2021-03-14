import java.util.ArrayList;
import java.util.Iterator;

/** Glowna klasa obslugujaca sie logike */
class GameHandler {
    /** Id gracza czyja tura */
    private int whoseTurn;
    /** Logiczna tablica zawierajaca stan planszy */
    int[][] stoneLogicTable;
    /** Rozmiar planszy */
    int boardSize;
    /** Wspolrzedna x ruchu */
    private int moveX;
    /** Wspolrzedna y ruchu */
    private int moveY;
    /** Czy ruch jest dozwolony */
    private boolean isMoveAllowed;
    /** Ostatni zabity kamien */
    private Pair lastKilled;
    /** Ten co zabil ostatni kamien */
    private Pair killer;
    /** */
    private Pair toKill;
    /** Licznik rund */
    private int roundCounter = 0;
    /** Lista lancuchow */
    private ArrayList<StoneChain> stoneChainList = new ArrayList<>();
    /** Podrobka listy lancuchu uzywana do sprawdzania samobojczych ruchow */
    private ArrayList<StoneChain> fakeStoneChainList;
    /** List uzywana do symulacji czy bot ma ruchy */
    private ArrayList<StoneChain> fakeStoneChainList2;
    /** terytorium danego gracza */
    int[] territory = new int[3];
    /** łańcuchy pustych pól */
    private  ArrayList<StoneChain> territoryList;

    /** Konstrukor
     * @param boardSize rozmiar planszy
     */
    GameHandler(int boardSize) {
        this.boardSize = boardSize;
    }

    /** Obliczanie terytorium */
    void territory (int[][] table) {
        territoryList = new ArrayList<>();
        whoseTurn = 0;
        stoneLogicTable = table;
        territory[1] = 0;
        territory[2] = 0;
        /** szukanie łańcuchów pustych pól */
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                if (stoneLogicTable[i][j] == 0) {
                    isPartOfChain(territoryList, i, j);
                }
            }
        }
        /** usuwanie z listy terytoriów które do nikogo nie należą */
        for (Iterator<StoneChain> it = territoryList.iterator(); it.hasNext();) {
            boolean rm = false;
            StoneChain chain = it.next();
            int territoryOwner = 0;
            for (Pair pair: chain.stoneChain) {
                ArrayList<Pair> neighbours = chain.findNeighbours(pair.getKey(), pair.getValue());
                for (Pair neighbour: neighbours) {
                    if (stoneLogicTable[neighbour.getKey()][neighbour.getValue()] != 0) {
                        if (territoryOwner == 0) {
                            chain.owner = stoneLogicTable[neighbour.getKey()][neighbour.getValue()];
                            territoryOwner = stoneLogicTable[neighbour.getKey()][neighbour.getValue()];
                        }
                        else if (territoryOwner != stoneLogicTable[neighbour.getKey()][neighbour.getValue()]) {
                            rm = true;
                        }
                    }
                }
            }
            if (rm) it.remove();
        }
        /* System.out.println("Lista łańcuchów terytoriów po usunięciu tych które do nikogo nie należa:"); {
            for (StoneChain chain: territoryList) {
                System.out.println("Łańcuch użytkownika: " + chain.owner);
                for (Pair pair: chain.stoneChain) {
                    System.out.println("x:" + pair.getKey() + "y:" + pair.getValue());
                }
            }
        } */
        /** Obliczanie terytorium */
        for (StoneChain chain: territoryList) {
            territory[chain.owner] += chain.stoneChain.size();
        }
        System.out.println("terytorium 1:" + territory[1]);
        System.out.println("terytorium 2:" + territory[2]);
        GameServer.gameServer.setTerritory(territory[1], territory[2]);
    }
    /** Ruch gracza
     * @param moveX wspolrzedna x ruchu gracza
     * @param moveY wspolrzedna y ruchu gracza
     * @param whoseTurn id gracza ktorego jest tura
     * @param table stan planszy
     * @return id gracza czyjego bedzie tura
     */
    int move (int moveX, int moveY, int whoseTurn, int[][] table) {
        this.moveX = moveX;
        this.moveY = moveY;
        this.whoseTurn = whoseTurn;
        stoneLogicTable = table;
        isMoveAllowed = true;

        if(GameServer.gameServer.bot && whoseTurn == 2) {
            if (!isAnyMoveLeft()) {
                return 3;
            }
        }

        isFieldEmpty();

        if(isMoveAllowed) {
            fakeStoneChainList = new ArrayList<>();
            for (StoneChain chain: stoneChainList) {
                StoneChain newChain = new StoneChain(chain.owner);
                for (Pair pair: chain.stoneChain) {
                    newChain.stoneChain.add(new Pair(pair.getKey(), pair.getValue()));
                }
                for (Pair pair: chain.liberties) {
                    newChain.liberties.add(new Pair(pair.getKey(), pair.getValue()));
                }
                fakeStoneChainList.add(newChain);
            }

           // System.out.println("fejkowa tura");
            if (isLibertyLeft(isPartOfChain(fakeStoneChainList, moveX, moveY)) || doesItKill(fakeStoneChainList )) {
                if (!isKo(moveX, moveY)) {
                    // System.out.println("normalna tura");
                    isPartOfChain(stoneChainList, moveX, moveY); // dwa razy robi nowy lancuch tutaj
                    stoneLogicTable[moveX][moveY] = whoseTurn;
                    /* System.out.println("Przypisuje graczowi " + whoseTurn);
                    System.out.println("przed zabiciem"); */
                    removeDead(stoneChainList);
                    //System.out.println("po zabiciu");
                    roundCounter ++;
                    if (whoseTurn == 1) whoseTurn = 2;
                    else whoseTurn = 1;
                }
            }
        }

        GameServer.gameServer.setTable(stoneLogicTable);
        return whoseTurn;
    }

    /** Sprawdza czy to sytuacja ko
     * @return tak lub nie
     */
    private boolean isKo(int moveX, int moveY) {
        if (toKill != null && killer != null && lastKilled != null && roundCounter == 1) {
            return toKill.getKey() == killer.getKey() && toKill.getValue() == killer.getValue() && lastKilled.getKey() == moveX && lastKilled.getValue() == moveY;
        }
        return false;
    }

    /** Czy pole jest puste? Ustawia czy ruch jest mozliwy */
    private void isFieldEmpty() {
        if (stoneLogicTable[moveX][moveY] != 0) {
            isMoveAllowed = false;
        }
    }

    /** Sprawdza do jakiego łańcucha będzie należał nowy kamięń */
    private StoneChain isPartOfChain(ArrayList<StoneChain> list, int moveX, int moveY) {
        StoneChain lastFoundIn = null;
        for (Iterator<StoneChain> it = list.iterator(); it.hasNext();) {
            StoneChain stoneChain = it.next();
            if (stoneChain.owner == whoseTurn) {
                if (stoneChain.isPartOfThisChain(moveX, moveY)) {
                    if (lastFoundIn != null) {
                       /* System.out.println("Lacze lancuch:");
                        for (Pair pair: lastFoundIn.stoneChain) {
                            System.out.println("Para: " + pair.getKey() + pair.getValue());
                        }
                        System.out.println("Lista oddechów: ");
                        for (Pair pair: lastFoundIn.liberties) {
                            System.out.println("Oddech: " +  pair.getKey() + pair.getValue());
                        }
                        System.out.println("z łańcuchem:");
                        for (Pair pair: stoneChain.stoneChain) {
                            System.out.println("Para: " + pair.getKey() + pair.getValue());
                        }
                        System.out.println("Lista oddechów: ");
                        for (Pair pair: stoneChain.liberties) {
                            System.out.println("Oddech: " +  pair.getKey() + pair.getValue());
                        } */
                        lastFoundIn.mergeChains(stoneChain);
                        //System.out.println("polaczylem łancuchy");
                        it.remove();
                        /*System.out.println("Połączony łańcuch: ");
                        for (Pair pair: lastFoundIn.stoneChain) {
                            System.out.println("Para: " + pair.getKey() + pair.getValue());
                        }
                        System.out.println("Lista oddechów: ");
                        for (Pair pair: lastFoundIn.liberties) {
                            System.out.println("Oddech: " +  pair.getKey() + pair.getValue());
                        } */
                    }
                    else {
                        stoneChain.stoneChain.add(new Pair(moveX,moveY));
                        lastFoundIn = stoneChain;
                    }
                }
            }
            if (stoneChain.owner != whoseTurn) {
                stoneChain.removeLiberty(moveX, moveY);
            }
        }
        if (lastFoundIn == null) {
            System.out.println("Nie znalazlem łancucha dla:" + moveX + moveY + "tworze nowy");
            lastFoundIn = new StoneChain(whoseTurn, moveX, moveY);
            list.add(lastFoundIn);
        }
        return lastFoundIn;
    }

    /** Sprawdza, czy zostaly jakies oddechy
     * @param chain lancuch w ktorym sprawdzamy oddechy
     * @return tak lub nie
     */
    boolean isLibertyLeft(StoneChain chain) {
        return chain.liberties.size() > 0;
    }

    /** Sprawdza czy ruch udusi jakieś kamienie
     * @return tak lub nie
     * */
    private boolean doesItKill(ArrayList<StoneChain> fakeList) {
        for (StoneChain chain: fakeList) {
            if (chain.owner != whoseTurn && chain.liberties.isEmpty()) {
                toKill = chain.stoneChain.get(0);
                return true;
            }
        }
        return false;
    }

    /** Usuwa z planszy martwe kamienie
     * @param stoneChainList lista wszystkich lancuchow do sprawdzenia
     */
    private void removeDead (ArrayList<StoneChain> stoneChainList) {
       for (Iterator<StoneChain> it = stoneChainList.iterator(); it.hasNext();) {
            StoneChain chain = it.next();
            if (chain.owner != whoseTurn && chain.liberties.isEmpty()) {
                for(Pair pair: chain.stoneChain) {
                    if (stoneLogicTable[pair.getKey()][pair.getValue()] !=0) {
                        GameServer.gameServer.captives[whoseTurn-1]++;
                    }
                    stoneLogicTable[pair.getKey()][pair.getValue()] = 0;
                    lastKilled = new Pair(pair.getKey(), pair.getValue());
                    killer = new Pair(moveX, moveY);
                    roundCounter = 0;
                }
                it.remove();
                chain.restoreLibertiesToNeighbours();
            }
        }
    }

    /** Szuka lancucha dla danego kamienia
     * @param pair kamien
     * @return znaleziony lancuch lub null
     */
    StoneChain findStonesChain (Pair pair) {
        System.out.println("szukam chaina");
        for (StoneChain chain: stoneChainList) {
            System.out.println("Lancuch: " );
            for (Pair stone: chain.stoneChain) {
                System.out.println("Para: " + stone.getKey() + stone.getValue());
                if (stone.getKey() == pair.getKey() && stone.getValue() == pair.getValue()) {
                    System.out.println("znalazlem chaina");
                    return chain;
                }
            }
        }
        return null;
    }

    /** Sprawdza czy bot ma jeszcze hjakiś możliwy ruch */
    private boolean isAnyMoveLeft() {
        for (int i=0; i<boardSize; i++) {
            for (int j=0; j<boardSize; j++) {
                if (stoneLogicTable[i][j] == 0) {
                    fakeStoneChainList2 = new ArrayList<>();
                    for (StoneChain chain: stoneChainList) {
                        StoneChain newChain = new StoneChain(chain.owner);
                        for (Pair pair: chain.stoneChain) {
                            newChain.stoneChain.add(new Pair(pair.getKey(), pair.getValue()));
                        }
                        for (Pair pair: chain.liberties) {
                            newChain.liberties.add(new Pair(pair.getKey(), pair.getValue()));
                        }
                        fakeStoneChainList2.add(newChain);
                    }
                    if ((isLibertyLeft(isPartOfChain(fakeStoneChainList2, i, j)) || doesItKill(fakeStoneChainList2)) && !isKo(i,j)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
