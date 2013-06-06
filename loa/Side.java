package loa;

/** Indicates a piece or player color.
 *  @author Weier Wan */
enum Side {
    /** The names of the two sides. */
    BLACK, WHITE;

    /** Return the opposing color. */
    Side opponent() {
        return this == BLACK ? WHITE : BLACK;
    }

    @Override
    public String toString() {
        return super.toString().toLowerCase();
    }
}
