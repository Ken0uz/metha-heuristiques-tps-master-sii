import java.util.Random;

public class VoyageurDeCommerce {
    private int nombreDeVilles;
    private int[][] matriceDistances;

    public VoyageurDeCommerce(int nombreDeVilles) {
        this.nombreDeVilles = nombreDeVilles;
        this.matriceDistances = genererMatriceDistances();
    }

    private int[][] genererMatriceDistances() {
        Random random = new Random();
        int[][] matrice = new int[nombreDeVilles][nombreDeVilles];
        for (int i = 0; i < nombreDeVilles; i++) {
            for (int j = 0; j < nombreDeVilles; j++) {
                if (i == j) {
                    matrice[i][j] = 0; // Distance de la ville à elle-même est 0
                } else {
                    matrice[i][j] = 1 + random.nextInt(100); // Distances aléatoires entre 1 et 100
                }
            }
        }
        return matrice;
    }

    public int calculerCoutSolution(int[] solution) {
        int coutTotal = 0;
        for (int i = 0; i < solution.length - 1; i++) {
            int villeDepart = solution[i];
            int villeArrivee = solution[i + 1];
            coutTotal += matriceDistances[villeDepart][villeArrivee];
        }
        // Ajouter le coût du retour à la ville de départ
        coutTotal += matriceDistances[solution[solution.length - 1]][solution[0]];
        return coutTotal;
    }

    public boolean estSolutionValide(int[] solution) {
        // Vérifie si la solution est valide (commence et termine avec la même ville)
        return solution[0] == solution[solution.length - 1];
    }

    public static void main(String[] args) {
        int nombreDeVilles = 5; // Vous pouvez demander à l'utilisateur d'entrer le nombre de villes
        VoyageurDeCommerce voyageur = new VoyageurDeCommerce(nombreDeVilles);
        System.out.println("Matrice de distances :");
        for (int[] row : voyageur.matriceDistances) {
            for (int distance : row) {
                System.out.print(distance + " ");
            }
            System.out.println();
        }
        int[] solutionTest = {0, 1, 2, 3, 0}; // Exemple de solution
        System.out.println("Cout de la solution test : " + voyageur.calculerCoutSolution(solutionTest));
        System.out.println("Est-ce que la solution test est valide ? " + voyageur.estSolutionValide(solutionTest));
    }
}
