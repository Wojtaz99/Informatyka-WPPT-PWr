/** Klasa para */
class Pair {
    /** Wspolrzedna x */
    private Integer x;
    /** Wspolrzedna y */
    private Integer y;

    /** Konstruktor pary
     * @param x wspolrzedna x
     * @param y wspolrzedna y
     */
    Pair(Integer x, Integer y) {
        this.x = x;
        this.y = y;
    }

    /** Zwroc x
     * @return wspolrzedna x
     */
    Integer getKey() {
        return x;
    }

    /** Zwroc y
     * @return wspolrzedna y
     */
    Integer getValue() {
        return y;
    }

}
