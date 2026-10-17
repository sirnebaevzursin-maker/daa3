package benchmark;

public class ReferenceValues {
    private static final long[][] TABLE = {
            {1000, 4000, 147970L, 27, 396274L},
            {1000, 20000, 30363L, 1, 252411L},
            {3162, 12648, 468911L, 137, 1271911L},
            {3162, 63240, 97589L, 1, 723157L},
            {10000, 40000, 1486139L, 373, 3941828L},
            {10000, 200000, 309550L, 1, 2381342L},
            {31623, 126492, 4715515L, 1188, 12551891L},
            {31623, 632460, 968517L, 1, 7376612L},
            {100000, 400000, 15012094L, 3923, 40096539L},
            {100000, 2000000, 3056681L, 1, 23512087L}
    };

    private static long[] row(int n, int m) {
        for (long[] r : TABLE) {
            if (r[0] == n && r[1] == m) {
                return r;
            }
        }
        throw new IllegalArgumentException("no reference for " + n + "/" + m);
    }

    public static long mst(int n, int m) {
        return row(n, m)[2];
    }

    public static long scc(int n, int m) {
        return row(n, m)[3];
    }

    public static long dagSum(int n, int m) {
        return row(n, m)[4];
    }
}