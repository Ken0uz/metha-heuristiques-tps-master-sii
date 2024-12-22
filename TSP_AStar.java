import java.awt.Dimension;
import java.util.*;
import javax.swing.JFrame;
import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;
import org.jgrapht.graph.DefaultWeightedEdge;
import com.mxgraph.layout.mxCircleLayout;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.util.mxConstants;
import com.mxgraph.view.mxEdgeStyle;
import org.jgrapht.ext.JGraphXAdapter;
import com.mxgraph.model.mxCell;
import com.mxgraph.model.mxICell;
import com.mxgraph.view.mxCellState;
import com.mxgraph.view.mxGraph;
import com.mxgraph.view.mxGraph.mxICellVisitor;

public class TSP_AStar {
    static int[][] graph; // Matrice d'adjacence représentant les distances entre les villes
    static int numNodes; // Nombre de villes
    static int minCost = Integer.MAX_VALUE; // Coût minimum trouvé
    static List<Integer> minTour; // Chemin correspondant au coût minimum

    // Classe interne représentant un nœud dans l'algorithme A*
    static class Node {
        List<Integer> path; // Chemin parcouru jusqu'à ce nœud
        int cost; // Coût actuel du chemin
        int estimatedCost; // Estimation du coût restant

        Node(List<Integer> path, int cost, int estimatedCost) {
            this.path = path;
            this.cost = cost;
            this.estimatedCost = estimatedCost;
        }
    }

    // Générer des distances aléatoires entre les villes
    static int[][] generateRandomDistances(int n) {
        Random random = new Random();
        int[][] dist = new int[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                int distance = random.nextInt(101); // Distance aléatoire entre 0 et 100
                dist[i][j] = distance;
                dist[j][i] = distance; // Assurer la symétrie
            }
        }
        return dist;
    }

    // Fonction pour afficher le graphe avec les distances et les poids sur les arêtes
    public static void visualizeGraph(int[][] graph) {
        Graph<Integer, DefaultWeightedEdge> g = new SimpleDirectedWeightedGraph<>(DefaultWeightedEdge.class);

        // Ajouter les nœuds
        for (int i = 0; i < graph.length; i++) {
            g.addVertex(i);
        }

        // Ajouter les arêtes avec les poids
        for (int i = 0; i < graph.length; i++) {
            for (int j = i + 1; j < graph[i].length; j++) {
                if (graph[i][j] != 0) {
                    DefaultWeightedEdge edge = g.addEdge(i, j);
                    // Ajouter le poids à l'arête
                    g.setEdgeWeight(edge, graph[i][j]);
                }
            }
        }

        // Créer une adaptation du graphe JGraphT pour l'affichage avec JGraphX
        JGraphXAdapter<Integer, DefaultWeightedEdge> jgxAdapter = new JGraphXAdapter<>(g);

        // Appliquer un layout pour disposer les nœuds de manière esthétique
        mxCircleLayout layout = new mxCircleLayout(jgxAdapter);
        layout.execute(jgxAdapter.getDefaultParent());

        // Créer un composant graphique pour afficher le graphe
        mxGraphComponent graphComponent = new mxGraphComponent(jgxAdapter);

        // Créer une fenêtre pour afficher le graphe
        JFrame frame = new JFrame("Graph Visualization");
        frame.getContentPane().add(graphComponent);
        frame.setPreferredSize(new Dimension(800, 600));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);

        // Ajouter les étiquettes de poids sur les arêtes
        for (DefaultWeightedEdge edge : g.edgeSet()) {
            Integer source = g.getEdgeSource(edge);
            Integer target = g.getEdgeTarget(edge);
            Object[] cells = jgxAdapter.getEdgesBetween(source, target);
            if (cells != null && cells.length > 0) {
                mxCell cell = (mxCell) cells[0];
                double weight = g.getEdgeWeight(edge);
                cell.setValue(Double.toString(weight));
            }
        }
    }


    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Entrez le nombre de villes : ");
        numNodes = scanner.nextInt();
        graph = generateRandomDistances(numNodes);

        System.out.println("Matrice d'adjacence du graphe généré :");
        printGraph(graph);

        System.out.print("Entrez la ville de départ (0 à " + (numNodes - 1) + ") : ");
        int startNode = scanner.nextInt();

        minTour = new ArrayList<>(numNodes + 1);
        aStar(startNode);

        // Affichage du résultat final
        System.out.println("\n\n");
        System.out.println("Coût minimum : " + minCost);
        System.out.println("Chemin : " + minTour + ", Coût : " + minCost);

        // Affichage du graphe
        visualizeGraph(graph);
    }

    // Algorithme A*
    static void aStar(int startNode) {
        PriorityQueue<Node> pq = new PriorityQueue<>(Comparator.comparingInt(n -> n.cost + n.estimatedCost));
        pq.add(new Node(new ArrayList<>(Collections.singletonList(startNode)), 0, estimateCost(Collections.singletonList(startNode))));

        while (!pq.isEmpty()) {
            Node currentNode = pq.poll();
            List<Integer> currentPath = currentNode.path;

            if (currentPath.size() == numNodes) { // Si toutes les villes ont été visitées
                int currentCost = currentNode.cost + graph[currentNode.path.get(numNodes - 1)][startNode];
                if (currentCost < minCost && graph[currentNode.path.get(numNodes - 1)][startNode] != 0) {
                    minCost = currentCost;
                    minTour.clear();
                    minTour.addAll(currentPath);
                    minTour.add(startNode);
                    System.out.println("Nouveau chemin optimal trouvé : " + minTour + ", Coût : " + minCost);
                }
                continue;
            }

            int lastVisited = currentPath.get(currentPath.size() - 1);
            for (int i = 0; i < numNodes; i++) {
                if (!currentPath.contains(i) && graph[lastVisited][i] != 0) { // Si la ville i n'est pas visitée et il y a un bord direct depuis la dernière ville visitée jusqu'à i
                    List<Integer> newPath = new ArrayList<>(currentPath);
                    newPath.add(i);
                    int newCost = currentNode.cost + graph[lastVisited][i];
                    int estimatedCost = estimateCost(newPath);
                    pq.add(new Node(newPath, newCost, estimatedCost));
                }
            }
        }
    }

    // Fonction pour afficher la matrice d'adjacence du graphe
    static void printGraph(int[][] graph) {
        for (int i = 0; i < graph.length; i++) {
            for (int j = 0; j < graph[i].length; j++) {
                System.out.print(graph[i][j] + "\t");
            }
            System.out.println();
        }
    }

    // Estimer le coût restant à parcourir
    static int estimateCost(List<Integer> path) {
        int estimatedCost = 0;
        boolean[] visited = new boolean[numNodes];
        for (int i = 0; i < path.size(); i++) {
            visited[path.get(i)] = true;
        }

        for (int i = 0; i < numNodes; i++) {
            if (!visited[i]) { // Si la ville i n'a pas été visitée
                int minDistance = Integer.MAX_VALUE;
                for (int j = 0; j < numNodes; j++) {
                    if (visited[j] && graph[j][i] < minDistance) {
                        minDistance = graph[j][i];
                    }
                }
                estimatedCost += minDistance;
            }
        }
        return estimatedCost;
    }
}
