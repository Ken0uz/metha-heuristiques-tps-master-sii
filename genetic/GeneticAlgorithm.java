import java.util.*;

public class GeneticAlgorithm {

    // Génération d'une solution aléatoire
    public List<Integer> generateRandomSolution(int numOfItems) {
        Random random = new Random();
        List<Integer> solution = new ArrayList<>();
        for (int i = 0; i < numOfItems; i++) {
            solution.add(random.nextInt(2)); // 1 si l'objet est pris, 0 sinon
        }
        return solution;
    }

    // Évaluation d'une solution (Fitness Function)
    public int evaluateSolution(List<Integer> solution, int[] values, int[] weights, int capacity) {
        int totalValue = 0;
        int totalWeight = 0;
        for (int i = 0; i < solution.size(); i++) {
            if (solution.get(i) == 1) {
                totalValue += values[i];
                totalWeight += weights[i];
            }
        }
        if (totalWeight > capacity) {
            return 0; // Pénalité si la solution dépasse la capacité du sac à dos
        }
        return totalValue;
    }

    // Opérateur de croisement (Crossover)
    public List<Integer> crossover(List<Integer> parent1, List<Integer> parent2) {
        Random random = new Random();
        int crossoverPoint = random.nextInt(parent1.size());
        List<Integer> child = new ArrayList<>(parent1.subList(0, crossoverPoint));
        child.addAll(parent2.subList(crossoverPoint, parent2.size()));
        return child;
    }

    // Opérateur de mutation
    public void mutate(List<Integer> solution, double mutationRate) {
        Random random = new Random();
        for (int i = 0; i < solution.size(); i++) {
            if (random.nextDouble() < mutationRate) {
                solution.set(i, 1 - solution.get(i)); // Inversion de 0 à 1 ou de 1 à 0
            }
        }
    }

    // Remplacement des individus les moins performants
    public void replace(List<List<Integer>> population, List<List<Integer>> children) {
        // Remplacer les individus les moins performants par les enfants
        // Par exemple, on pourrait trier la population par ordre décroissant de performance et remplacer les derniers individus par les enfants
        // Mais cela dépendra de votre stratégie de remplacement spécifique
    }

    // Algorithme génétique principal
    public List<Integer> geneticAlgorithm(int numOfItems, int[] values, int[] weights, int capacity, int populationSize, int maxGenerations, double mutationRate) {
        // Génération initiale de la population
        List<List<Integer>> population = new ArrayList<>();
        for (int i = 0; i < populationSize; i++) {
            population.add(generateRandomSolution(numOfItems));
        }

        // Évolution de la population sur un certain nombre de générations
        for (int generation = 0; generation < maxGenerations; generation++) {
            List<List<Integer>> children = new ArrayList<>();
            for (int i = 0; i < populationSize / 2; i++) {
                // Sélection des parents (à implémenter)
                List<Integer> parent1 = population.get(0); // Exemple : sélection du premier individu pour simplifier
                List<Integer> parent2 = population.get(1); // Exemple : sélection du deuxième individu pour simplifier

                // Croisement des parents pour produire des enfants
                List<Integer> child = crossover(parent1, parent2);
                mutate(child, mutationRate); // Mutation de l'enfant
                children.add(child);
            }
            replace(population, children); // Remplacement des individus les moins performants
        }

        // Sélection du meilleur individu après toutes les générations
        // (à implémenter apres selon des criteres de sélection)
        List<Integer> bestSolution = population.get(0); // Exemple : sélection du premier individu pour simplifier
        return bestSolution;
    }

    public static void main(String[] args) {
        GeneticAlgorithm ga = new GeneticAlgorithm();

        // Exemple d'utilisation
        int numOfItems = 10;
        int[] values = {10, 5, 8, 2, 3, 9, 4, 7, 6, 1};
        int[] weights = {5, 3, 7, 2, 4, 6, 3, 5, 2, 1};
        int capacity = 15;
        int populationSize = 50;
        int maxGenerations = 100;
        double mutationRate = 0.1;

        List<Integer> bestSolution = ga.geneticAlgorithm(numOfItems, values, weights, capacity, populationSize, maxGenerations, mutationRate);
        System.out.println("Meilleure solution trouvée : " + bestSolution);
    }
}
