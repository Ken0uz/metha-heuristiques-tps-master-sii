import java.util.*;
import java.time.Duration;
import java.time.Instant;

public class VoyageurCommerceDFS {

    static int[][] matriceAdjacence; // Matrice d'adjacence représentant le graphe
    static int nombreNoeuds;
    static int coutMin = Integer.MAX_VALUE;
    static List<Integer> cheminOptimal = new ArrayList<>(nombreNoeuds + 1);

    // Méthode pour générer des distances aléatoires entre les nœuds
    static int[][] genererDistancesAleatoires(int n) {
        Random random = new Random();
        int[][] distances = new int[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) { // Assurer la symétrie en ne générant que la moitié de la matrice
                int distance = random.nextInt(101); // Générer une distance aléatoire entre 0 et 100 (inclus)
                distances[i][j] = distance;
                distances[j][i] = distance; // Assurer la symétrie
            }
        }
        return distances;
    }

    public static void main(String[] args) {
        // Initialiser le graphe et d'autres variables
        Scanner scanner = new Scanner(System.in);
        System.out.print("Entrez le nombre de nœuds : ");
        nombreNoeuds = scanner.nextInt();
        matriceAdjacence = genererDistancesAleatoires(nombreNoeuds);

        System.out.print("Entrez le nœud de départ (entre 0 et " + (nombreNoeuds - 1) + ") : ");
        int noeudDepart = scanner.nextInt();

        Instant start = Instant.now(); // Mesurer le temps d'exécution
        dfs(noeudDepart);
        Instant end = Instant.now();
        Duration timeElapsed = Duration.between(start, end);
        System.out.println("\n\n\n");
        System.out.println("Coût minimal: " + coutMin);
        System.out.println("Chemin optimal: " + cheminOptimal + ", Coût: " + coutMin);
        System.out.println("Temps d'exécution: " + timeElapsed.toMillis() + " millisecondes");
        scanner.close();
    }

    // Méthode de recherche DFS
    static void dfs(int noeudDepart) {
        Stack<Integer> pile = new Stack<>();
        Stack<List<Integer>> chemins = new Stack<>();
        pile.push(noeudDepart); // Commencer la recherche à partir du nœud de départ
        chemins.push(new ArrayList<>(Arrays.asList(noeudDepart))); 

        while (!pile.isEmpty()) { // Tant qu'il reste des nœuds à explorer
            // Récupérer le dernier nœud ajouté à la pile
            int noeudCourant = pile.pop(); 
            List<Integer> cheminCourant = chemins.pop(); // Récupérer le chemin correspondant à ce nœud

            // Vérifier si le chemin courant est un chemin complet
            if (cheminCourant.size() == nombreNoeuds) {
                int coutCourant = calculerCout(cheminCourant); 
                coutCourant += matriceAdjacence[noeudCourant][noeudDepart]; // Ajouter le coût du retour au nœud de départ

                // Si le coût du chemin courant est inférieur au coût minimal actuel
                if (coutCourant < coutMin && matriceAdjacence[noeudCourant][noeudDepart] != 0) {
                    coutMin = coutCourant; // Mettre à jour le coût minimal
                    cheminOptimal.clear(); // CO précédent
                    cheminOptimal.addAll(cheminCourant); // Ajouter le chemin courant au chemin optimal

                    // Ajouter le retour au nœud de départ au chemin optimal
                    cheminOptimal.add(noeudDepart); 
                    System.out.println("Chemin optimal trouvé: " + cheminOptimal + ", Coût: " + coutMin);
                }
                continue; // Passer à la prochaine itération de la boucle
            }

            // Parcourir tous les nœuds adjacents
            for (int i = 0; i < nombreNoeuds; i++) { 
                // Vérifier si le nœud i n'est pas déjà visité et s'il existe une arête du nœud courant à i
                if (!cheminCourant.contains(i) && matriceAdjacence[noeudCourant][i] != 0) { 
                    List<Integer> nouveauChemin = new ArrayList<>(cheminCourant); // Créer un nouveau chemin basé sur le chemin courant
                    nouveauChemin.add(i); // Ajouter le nœud i au nouveau chemin
                    pile.push(i); // Ajouter le nœud i à la pile pour l'exploration ultérieure
                    chemins.push(nouveauChemin); // Ajouter le nouveau chemin à la pile de chemins
                }
            }
        }
    }


    // Méthode pour calculer le coût d'un chemin
    static int calculerCout(List<Integer> chemin) {
        int cout = 0;
        for (int i = 0; i < chemin.size() - 1; i++) {
            cout += matriceAdjacence[chemin.get(i)][chemin.get(i + 1)];
        }
        return cout;
    }
}
