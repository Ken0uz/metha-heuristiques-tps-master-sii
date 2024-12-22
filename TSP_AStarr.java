import java.util.*;

public class TSP_AStarr {
    static int[][] graph; // Matrice d'adjacence représentant les distances entre les villes
    static int numNodes; // Nombre de villes
    static int minCost = Integer.MAX_VALUE; // Coût minimum trouvé
    static List<Integer> minTour; // Chemin correspondant au coût minimum

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

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
        System.out.print("Entrez le nombre de villes : ");
        numNodes = scanner.nextInt();
        graph = generateRandomDistances(numNodes);
        
        System.out.print("Entrez la ville de départ (0 à " + (numNodes - 1) + ") : ");
        int startNode = scanner.nextInt();

        minTour = new ArrayList<>(numNodes + 1);
        aStar(startNode);

        System.out.println("\n\n");
        System.out.println("Coût minimum : " + minCost);
        System.out.println("Chemin : " + minTour + ", Coût : " + minCost);
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
                    System.out.println("Chemin : " + minTour + ", Coût : " + minCost);
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

    // Calculer le coût total du chemin
    static int calculateCost(List<Integer> path) {
        int cost = 0;
        for (int i = 0; i < path.size() - 1; i++) {
            cost += graph[path.get(i)][path.get(i + 1)];
        }
        return cost;
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
