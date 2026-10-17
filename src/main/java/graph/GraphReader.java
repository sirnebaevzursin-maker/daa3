package graph;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class GraphReader {

    public static Graph read(String path) throws IOException {
        try (BufferedReader br = Files.newBufferedReader(Path.of(path))) {
            String[] head = br.readLine().trim().split("\\s+");
            int n = Integer.parseInt(head[0]);
            int m = Integer.parseInt(head[1]);
            boolean directed = head[2].equalsIgnoreCase("directed");
            Graph g = new Graph(n, directed);
            int read = 0;
            String line;
            while (read < m && (line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) {
                    continue;
                }
                String[] p = line.split("\\s+");
                g.addEdge(Integer.parseInt(p[0]), Integer.parseInt(p[1]), Integer.parseInt(p[2]));
                read++;
            }
            return g;
        }
    }
}