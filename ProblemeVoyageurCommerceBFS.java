import java.util.*;

public class ProblemeVoyageurCommerceBFS {
    // Matrice d'adjacence représentant le graphe
    static int[][] matriceDistances; 
    static int nombreNoeuds ;
    static int coutMin = Integer.MAX_VALUE;
    static List<Integer> cheminOptimal = new ArrayList<>(nombreNoeuds + 1); // Le chemin optimal trouvé

    // Méthode pour générer des distances aléatoires entre les nœuds
    static int[][] genererDistancesAleatoires(int n) {
        Random random = new Random();
        int[][] distances = new int[n][n];
        for (int i = 0; i < n; i++) {
            // Assurer la symétrie en ne générant que la moitié de la matrice
            for (int j = i + 1; j < n; j++) { 
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
        matriceDistances = genererDistancesAleatoires(nombreNoeuds);

        System.out.print("Entrez le nœud de départ (entre 0 et " + (nombreNoeuds - 1) + ") : ");
        int noeudDepart = scanner.nextInt();

        // Lancer la recherche en largeur depuis le nœud de départ spécifié
        rechercheLargeur(noeudDepart);
        //System.out.println("Temps d'exécution: " + timeElapsed.toMillis() + " millisecondes");
        scanner.close();
    }

    // Méthode de recherche en largeur (BFS)
    static void rechercheLargeur(int noeudDepart) {
        Queue<Integer> fileAttente = new LinkedList<>();
        Queue<List<Integer>> chemins = new LinkedList<>();
        fileAttente.offer(noeudDepart);
        chemins.offer(new ArrayList<>(Arrays.asList(noeudDepart)));

        // Boucle principale pour explorer les nœuds
        while (!fileAttente.isEmpty()) { 
            int noeudCourant = fileAttente.poll();
            List<Integer> cheminCourant = chemins.poll();

            // Vérifier si le chemin actuel est un chemin complet
            if (cheminCourant.size() == nombreNoeuds && matriceDistances[noeudCourant][noeudDepart] != 0) { 

                // Si oui, calculer le coût du chemin et comparer au coût minimal actuel
                int coutCourant = calculerCout(cheminCourant);
                coutCourant += matriceDistances[noeudCourant][noeudDepart]; // Revenir au nœud de départ
                if (coutCourant < coutMin) { // Si le coût du chemin est inférieur au coût minimal actuel
                    coutMin = coutCourant;
                    cheminOptimal.clear();
                    cheminOptimal.addAll(cheminCourant);

                    //hna on ajoute le noeud de depart
                    cheminOptimal.add(noeudDepart);
                    System.out.println("Chemin optimal trouvé: " + cheminOptimal + ", Coût: " + coutMin);
                }
                continue;
            }

            // Affichage des chemins intermédiaires explorés
            //System.out.println("Chemin exploré : " + cheminCourant + ", Coût : " + calculerCout(cheminCourant));
            //System.out.println("---------------------------------------------");


            // Parcourir tous les nœuds adjacents non visités
            for (int i = 0; i < nombreNoeuds; i++) {
                if (!cheminCourant.contains(i) && matriceDistances[noeudCourant][i] != 0) { 
                    // Si le nœud n'est pas déjà dans le chemin et s'il existe une arête
                    // Ajouter le nœud à la file d'attente et le chemin à la liste des chemins
                    List<Integer> nouveauChemin = new ArrayList<>(cheminCourant);
                    nouveauChemin.add(i);

                    //hna c'est pour ajouter
                    fileAttente.offer(i);
                    chemins.offer(nouveauChemin);
                }
            }
        }
    }

    // Méthode pour calculer le coût d'un chemin
    static int calculerCout(List<Integer> chemin) {
        int cout = 0;
        for (int i = 0; i < chemin.size() - 1; i++) {
            cout += matriceDistances[chemin.get(i)][chemin.get(i + 1)];
        }
        return cout;
    }
}
