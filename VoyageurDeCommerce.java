import java.util.Random;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class VoyageurDeCommerce {
    private int nombreDeVilles;
    private int[][] matriceDistances;
    private int taillePopulation; // Taille de la population

    public VoyageurDeCommerce(int nombreDeVilles, int taillePopulation) {
        this.nombreDeVilles = nombreDeVilles;
        this.matriceDistances = genererMatriceDistances();
        this.taillePopulation = taillePopulation;
    }

    // Générer une population initiale de solutions aléatoires
    public List<int[]> genererPopulationInitiale() {
        List<int[]> population = new ArrayList<>();
        for (int i = 0; i < taillePopulation; i++) {
            int[] solution = genererSolutionAleatoire();
            population.add(solution);
        }
        return population;
    }

    // Évaluer chaque solution de la population
    public List<Integer> evaluerPopulation(List<int[]> population) {
        List<Integer> evaluations = new ArrayList<>();
        for (int[] solution : population) {
            int evaluation = calculerCoutSolution(solution);
            evaluations.add(evaluation);
        }
        return evaluations;
    }

    // Sélectionner les 50% meilleures solutions
    public List<int[]> selectionnerMeilleuresSolutions(List<int[]> population, List<Integer> evaluations) {
        List<int[]> meilleuresSolutions = new ArrayList<>();
        // Créer une liste de paires (solution, évaluation)
        List<Pair<int[], Integer>> pairs = new ArrayList<>();
        for (int i = 0; i < population.size(); i++) {
            pairs.add(new Pair<>(population.get(i), evaluations.get(i)));
        }
        // Trier la liste de paires en fonction des évaluations
        Collections.sort(pairs, Comparator.comparing(Pair::getValue));
        // Sélectionner les 50% meilleures solutions
        for (int i = 0; i < population.size() / 2; i++) {
            meilleuresSolutions.add(pairs.get(i).getKey());
        }
        return meilleuresSolutions;
    }


    public List<int[]> selectionnerParents(List<int[]> population, List<Integer> evaluations) {
        // Sélectionner les 50% meilleures solutions
        List<int[]> meilleuresSolutions = selectionnerMeilleuresSolutions(population, evaluations);
    
        // Sélectionner les meilleurs parents parmi les 50% meilleures solutions
        List<int[]> parents = new ArrayList<>();
        for (int i = 0; i < population.size() / 2; i++) {
            int indexMeilleurParent = i; // Prendre les i premières solutions comme parents
            parents.add(meilleuresSolutions.get(indexMeilleurParent));
        }
    
        return parents;
    }

    public List<int[]> croisement(List<int[]> parents) {
        List<int[]> enfants = new ArrayList<>();
        Random random = new Random();
        int nbPointsCroisement = Math.max(1, parents.get(0).length / 10); // Nombre de points de croisement dépendant de la taille du problème
    
        for (int i = 0; i < parents.size() - 1; i += 2) {
            int[] parent1 = parents.get(i);
            int[] parent2 = parents.get(i + 1);
    
            // Générer aléatoirement les positions des points de croisement
            List<Integer> positionsCroisement = new ArrayList<>();
            for (int j = 0; j < nbPointsCroisement; j++) {
                int position = random.nextInt(parent1.length - 1) + 1; // Éviter la première et la dernière ville
                positionsCroisement.add(position);
            }
            Collections.sort(positionsCroisement); // Trier les positions de croisement pour faciliter le croisement
    
            int[] enfant1 = croiser(parent1, parent2, positionsCroisement);
            int[] enfant2 = croiser(parent2, parent1, positionsCroisement);
    
            enfants.add(enfant1);
            enfants.add(enfant2);
        }
    
        return enfants;
    }
    
    // Méthode utilitaire pour croiser deux parents en fonction des positions de croisement
    private int[] croiser(int[] parent1, int[] parent2, List<Integer> positionsCroisement) {
        // prend deux tableaux d'entiers représentant les parents
        // et une liste des positions de croisement.
    
        int[] enfant = new int[parent1.length];
        // Initialisation d'un tableau pour stocker les valeurs de l'enfant,
        // de la même taille que les parents.
    
        int startIndex = 0;
        // Initialisation de l'index de départ pour la copie des valeurs des parents.
    
        for (int i = 0; i < positionsCroisement.size(); i++) {
            // Boucle à travers les positions de croisement.
    
            int endIndex = positionsCroisement.get(i);
            // Récupération de la position de croisement actuelle.
    
            if (i % 2 == 0) {
                // Si l'indice de la position de croisement est pair, copiez à partir de parent1.
                System.arraycopy(parent1, startIndex, enfant, startIndex, endIndex - startIndex);
            } else {
                // Sinon, copiez à partir de parent2.
                System.arraycopy(parent2, startIndex, enfant, startIndex, endIndex - startIndex);
            }
            startIndex = endIndex;
            // Mise à jour de l'index de départ pour la prochaine itération.
        }    
    
        // Copier le reste des villes de l'un des parents
        if (positionsCroisement.size() % 2 == 0) {
            System.arraycopy(parent1, startIndex, enfant, startIndex, parent1.length - startIndex);
        } else {
            System.arraycopy(parent2, startIndex, enfant, startIndex, parent2.length - startIndex);
        }
    
        return enfant;
    }
    
    public List<int[]> mutation(List<int[]> enfants) {
        // Définition de la méthode `mutation` qui prend une liste d'entiers représentant les enfants à muter
        // et retourne une liste d'entiers.
    
        Random random = new Random();
        // Création d'une instance de la classe `Random` pour générer des nombres aléatoires.
    
        for (int[] enfant : enfants) {
            // Boucle à travers chaque enfant dans la liste des enfants.
            int nbPointsMutation = Math.max(1, enfant.length / 20);    //je dois le rajouter comme parametre 
            // Calcul du nombre de points de mutation, en prenant le maximum entre 1 et 5% de la taille du chemin.
    
            for (int i = 0; i < nbPointsMutation; i++) {
                // Boucle pour chaque point de mutation.
    
                int index1 = random.nextInt(enfant.length - 1) + 1;
                int index2 = random.nextInt(enfant.length - 1) + 1;
                // Génération aléatoire de deux indices pour sélectionner deux positions dans le chemin de l'enfant.
    
                // Échanger les positions de deux villes dans le chemin de l'enfant.
                int temp = enfant[index1];
                enfant[index1] = enfant[index2];
                enfant[index2] = temp;
            }
        }
    
        return enfants;
    }
    

    public List<int[]> remplacerPopulation(List<int[]> population, List<Integer> evaluations, List<int[]> enfantsCroises, List<Integer> evaluationsCroises, List<int[]> enfantsMutants, List<Integer> evaluationsMutantes) {
        // Fusionner les enfants croisés et mutants avec la population existante
        population.addAll(enfantsCroises);
        population.addAll(enfantsMutants);
    
        // Fusionner les évaluations correspondantes
        evaluations.addAll(evaluationsCroises);
        evaluations.addAll(evaluationsMutantes);
    
        // Trier la population en fonction des évaluations
        List<Pair<int[], Integer>> pairs = new ArrayList<>();
        for (int i = 0; i < population.size(); i++) {
            pairs.add(new Pair<>(population.get(i), evaluations.get(i)));
        }
        Collections.sort(pairs, Comparator.comparing(Pair::getValue));
    
        // Conserver les N meilleures solutions (population de taille constante)
        int tailleNouvellePopulation = population.size();
        population.clear();
        evaluations.clear();
        for (int i = 0; i < tailleNouvellePopulation; i++) {
            population.add(pairs.get(i).getKey());
            evaluations.add(pairs.get(i).getValue());
        }
    
        return population;
    }

    public int[] meilleurIndividu(List<int[]> population, List<Integer> evaluations) {
        int meilleureEvaluation = Integer.MAX_VALUE;
        int indiceMeilleureSolution = -1;
    
        // Parcourir les évaluations pour trouver la meilleure
        for (int i = 0; i < evaluations.size(); i++) {
            int evaluation = evaluations.get(i);
            if (evaluation < meilleureEvaluation) {
                meilleureEvaluation = evaluation;
                indiceMeilleureSolution = i;
            }
        }
    
        // Renvoyer la meilleure solution correspondant à l'indice trouvé
        return population.get(indiceMeilleureSolution);
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

    public int[] genererSolutionAleatoire() {
        int[] solution = new int[nombreDeVilles + 1]; // Augmenter la taille du tableau de 1
        for (int i = 0; i < nombreDeVilles; i++) {
            solution[i] = i; // Initialiser la solution avec l'ordre des villes de 0 à nombreDeVilles-1
        }
        Random random = new Random();
        // Permuter aléatoirement les villes pour obtenir une solution aléatoire
        for (int i = 0; i < nombreDeVilles; i++) {
            int index = random.nextInt(nombreDeVilles - i) + i;
            int temp = solution[i];
            solution[i] = solution[index];
            solution[index] = temp;
        }
        // Ajouter la première ville à la fin de la solution
        solution[nombreDeVilles] = solution[0];
        return solution;
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
     //=======================================================
    public boolean estSolutionValide(int[] solution) {
        // Vérifie si la solution est valide (commence et termine avec la même ville)
        return solution[0] == solution[solution.length - 1];
    }

    public int[] executerAlgorithmeGenetique(int nombreDeVilles, int taillePopulation, int maxGenerations) {
        // Générer une population initiale de manière aléatoire
        List<int[]> population = genererPopulationInitiale();
    
        // Évaluer la population initiale
        List<Integer> evaluations = evaluerPopulation(population);
    
        // Boucle principale de l'algorithme génétique
        for (int generation = 1; generation <= maxGenerations; generation++) {
            // Sélectionner les parents
            List<int[]> parents = selectionnerParents(population, evaluations);
    
            // Afficher les parents sélectionnés
            System.out.println("Génération " + generation + ": Parents sélectionnés:");
            for (int[] parent : parents) {
                System.out.println(arrayToString(parent));
            }
    
            // Effectuer le croisement (Crossover)
            List<int[]> enfantsCroises = croisement(parents);
    
            // Afficher les enfants après le croisement
            System.out.println("Génération " + generation + ": Enfants après croisement:");
            for (int[] enfant : enfantsCroises) {
                System.out.println(arrayToString(enfant));
            }
    
            // Appliquer la mutation aux enfants croisés
            List<int[]> enfantsMutants = mutation(enfantsCroises);
    
            // Afficher les enfants après la mutation
            System.out.println("Génération " + generation + ": Enfants après mutation:");
            for (int[] enfant : enfantsMutants) {
                System.out.println(arrayToString(enfant));
            }
    
            // Évaluer les enfants croisés et mutants
            List<Integer> evaluationsCroises = evaluerPopulation(enfantsCroises);
            List<Integer> evaluationsMutantes = evaluerPopulation(enfantsMutants);
    
            // Remplacement de la population
            population = remplacerPopulation(population, evaluations, enfantsCroises, evaluationsCroises, enfantsMutants, evaluationsMutantes);
        }
    
        // Retourner le meilleur individu de la population finale
        return meilleurIndividu(population, evaluations);
    }
    

    public static void main(String[] args) {
        int nombreDeVilles = 5;
        int taillePopulation = 10; // Taille de la population
        int maxGenerations = 15; // Nombre maximum de générations

        VoyageurDeCommerce voyageur = new VoyageurDeCommerce(nombreDeVilles, taillePopulation);
        List<int[]> population = voyageur.genererPopulationInitiale();
        List<Integer> evaluations = voyageur.evaluerPopulation(population);
        List<int[]> meilleuresSolutions = voyageur.selectionnerMeilleuresSolutions(population, evaluations);

        // Affichage des solutions et de leurs évaluations
        for (int i = 0; i < population.size(); i++) {
            System.out.println("Solution " + (i + 1) + ": " + arrayToString(population.get(i)) + " - Distance totale: " + evaluations.get(i));
        }

        // Affichage des meilleures solutions avec leurs coûts
        for (int i = 0; i < meilleuresSolutions.size(); i++) {
            int[] meilleureSolution = meilleuresSolutions.get(i);
            int cout = voyageur.calculerCoutSolution(meilleureSolution);
            System.out.println("Meilleure solution " + (i + 1) + ": " + arrayToString(meilleureSolution) + " - Coût: " + cout);
        }

        // Exécution de l'algorithme génétique et récupération de la meilleure solution
        int[] meilleureSolution = voyageur.executerAlgorithmeGenetique(nombreDeVilles, taillePopulation, maxGenerations);
        
        // Affichage de la meilleure solution
        System.out.println("Meilleure solution trouvée : " + arrayToString(meilleureSolution));
    }







    // Méthode utilitaire pour convertir un tableau en chaîne de caractères
    private static String arrayToString(int[] array) {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (int i = 0; i < array.length; i++) {
            sb.append(array[i]);
            if (i < array.length - 1) {
                sb.append(", ");
            }
        }
        sb.append("]");
        return sb.toString();
    }




    public class Pair<K, V> {
        private final K key;
        private final V value;
    
        public Pair(K key, V value) {
            this.key = key;
            this.value = value;
        }
    
        public K getKey() {
            return key;
        }
    
        public V getValue() {
            return value;
        }
    }
    
}
