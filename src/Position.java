class Position {
    int x;
    int y;
    Position prev;
    Position next;

    Position(int x, int y) {
        this.x = x;
        this.y = y;
        prev = null;
        next = null;
    }
}