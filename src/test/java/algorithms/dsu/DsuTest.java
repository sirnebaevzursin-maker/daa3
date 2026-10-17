package algorithms.dsu;

import metrics.Metrics;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DsuTest {

    @Test
    void findAndComponentsMatchReference() {
        Random rnd = new Random(1);
        int n = 500;
        DSU dsu = new DSU(n, new Metrics());
        int[] label = new int[n];
        for (int i = 0; i < n; i++) {
            label[i] = i;
        }
        for (int k = 0; k < 400; k++) {
            int a = rnd.nextInt(n);
            int b = rnd.nextInt(n);
            dsu.union(a, b);
            int la = label[a];
            int lb = label[b];
            if (la != lb) {
                for (int i = 0; i < n; i++) {
                    if (label[i] == lb) {
                        label[i] = la;
                    }
                }
            }
        }
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j += 7) {
                assertEquals(label[i] == label[j], dsu.find(i) == dsu.find(j));
            }
        }
        Set<Integer> sets = new HashSet<>();
        for (int l : label) {
            sets.add(l);
        }
        assertEquals(sets.size(), dsu.components());
    }

    @Test
    void countersCountFindsAndSuccessfulUnions() {
        Metrics m = new Metrics();
        DSU dsu = new DSU(4, m);
        dsu.find(0);
        dsu.find(1);
        dsu.union(0, 1);
        dsu.union(0, 1);
        dsu.union(2, 3);
        assertEquals(2, dsu.findCalls());
        assertEquals(2, dsu.unionCalls());
        assertEquals(4, m.dsuOps);
        assertEquals(2, dsu.components());
    }
}