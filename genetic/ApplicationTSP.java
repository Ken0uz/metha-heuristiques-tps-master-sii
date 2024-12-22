import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

// Classe principale de l'application TSP (Problème du Voyageur de Commerce)
public class ApplicationTSP {
    // Définition des paramètres de l'algorithme génétique
    static int tailleSolution = 7; // Taille de la solution (nombre de villes à visiter)
    static int taillePopulation = 50; // Taille de la population initiale
    static int generations = 250; // Nombre de générations
    static double tauxMutation = 0.1; // Taux de mutation
    static double tauxCrossover = 0.7; // Taux de crossover
    static int siteInitial = 5; // Ville de départ





/*     static int[][] genererGrapheAleatoire(int tailleSolution) {
        int[][] graphe = new int[tailleSolution][tailleSolution];
        Random random = new Random();
    
        // Remplissage du graphe avec des distances aléatoires entre les villes
        for (int i = 0; i < tailleSolution; i++) {
            for (int j = 0; j < tailleSolution; j++) {
                if (i == j) {
                    graphe[i][j] = 0; // Distance de chaque ville à elle-même est 0
                } else {
                    // Génération aléatoire de distances entre 1 et 100
                    graphe[i][j] = random.nextInt(100) + 1;
                }
            }
        }
    
        return graphe;
    }
    */ 


    // Définition du graphe de distances entre les villes
    static int[][] graphe = {
            {0, 10, 15, 20, 25, 30, 35},
            {10, 0, 10, 20, 25, 30, 35},
            {15, 10, 0, 10, 20, 25, 30},
            {20, 20, 10, 0, 10, 20, 25},
            {25, 25, 20, 10, 0, 10, 20},
            {30, 30, 25, 20, 10, 0, 10},
            {35, 35, 30, 25, 20, 10, 0}
    };



    


    public static void main(String[] args) throws Exception {
        // Génération de la population initiale
        List<Individu> population = genererPopulation(taillePopulation, tailleSolution, siteInitial);

        // Boucle principale sur les générations
        for (int generation = 0; generation < generations; generation++) {
            // Création de la population de la génération suivante
            List<Individu> populationGenSuiv = new ArrayList<>();
            List<Individu> populationFusionnee = new ArrayList<>();

            // Opérateur de crossover sur la population actuelle
            for (int i = 0; i < taillePopulation; i += 2) {
                if (Math.random() < tauxCrossover) {
                    List<Individu> resultatsCrossover = crossover(population.get(i), population.get(i + 1), siteInitial, tailleSolution);
                    populationGenSuiv.add(resultatsCrossover.get(0));
                    populationGenSuiv.add(resultatsCrossover.get(1));
                }
            }

            // Opérateur de mutation sur la population actuelle
            for (Individu individu : population) {
                if (Math.random() < tauxMutation) {
                    Individu individuMutant = muter(individu);
                    populationGenSuiv.add(individuMutant);
                }
            }
            
            // Fusion de la population actuelle avec celle de la génération suivante
            populationFusionnee.addAll(population);
            populationFusionnee.addAll(populationFusionnee);

            // Évaluation et sélection des individus pour la génération suivante
            evaluer(populationFusionnee, taillePopulation);
            population.clear();
            population.addAll(populationFusionnee);
        }

        // Évaluation finale de la population et affichage de la meilleure solution trouvée
        evaluer(population, 1);
        System.out.println("Meilleure solution trouvée : " + population.get(0).solution);
        System.out.println("Distance totale parcourue : " + population.get(0).qualite);
    }

    // Méthode pour évaluer la qualité de la population et sélectionner les meilleurs individus
    public static void evaluer(List<Individu> population, int limite) {
        for (Individu individu : population) {
            individu.qualite = calculerQualite(individu);
        }
        Collections.sort(population, (a, b) -> -Integer.compare(b.qualite, a.qualite));

        
        // si on depasse la taille on conserve les meilleurs individus
        if (population.size() > limite) {
            population.subList(limite, population.size()).clear();
        }
    }

    // Méthode pour générer la population initiale
    public static List<Individu> genererPopulation(int nombreIndividus, int tailleSolution, int depart) {
        List<Individu> individus = new ArrayList<>();
        List<List<Integer>> solutions = new ArrayList<>();

        for (int i = 0; i < nombreIndividus; i++) {
            List<Integer> solutionAleatoire = genererSolutionAleatoire(tailleSolution, depart, solutions);
            solutions.add(solutionAleatoire);
            Individu individu = new Individu(solutionAleatoire, 0);
            individu.qualite = calculerQualite(individu);
            individus.add(individu);
        }
        return individus;
    }

    // Méthode pour générer une solution aléatoire ne visitant chaque ville qu'une fois ( nous donne donc une solution valide)
    public static List<Integer> genererSolutionAleatoire(int n, int depart, List<List<Integer>> solutions) {
        List<Integer> solution = new ArrayList<>();
        solution.add(depart);
        
        while (true) {
            List<Integer> solutionTemp = new ArrayList<>();
            solutionTemp.add(depart);
            List<Integer> villesRestantes = new ArrayList<>();
            for (int i = 0; i < n; i++) {
                if (i != depart) {
                    villesRestantes.add(i);
                }
            }
            Collections.shuffle(villesRestantes);
            for (int i = 0; i < n - 1; i++) {
                solutionTemp.add(villesRestantes.get(i));
            }
            solutionTemp.add(depart);

            // pour ne pas avoir tikrar ( des solutions pareilles)
            boolean solutionExiste = false;
            for (List<Integer> solutionExistante : solutions) {
                if (solutionExistante.equals(solutionTemp)) {
                    solutionExiste = true;
                    break;
                }
            }
            if (!solutionExiste) {
                solution = solutionTemp;
                break;
            }
        }
        return solution;
    }

    // Méthode pour calculer la qualité d'un individu (distance totale parcourue)
    private static int calculerQualite(Individu individu) {
        int qualite = 0;
        int limite = tailleSolution - 1;
        for (int i = 0; i < limite; i++) {
            qualite += graphe[individu.solution.get(i)][individu.solution.get(i + 1)];
        }
        qualite += graphe[individu.solution.get(limite)][individu.solution.get(0)];
        return qualite;
    }

    // Méthode pour muter un individu en échangeant aléatoirement deux villes dans la solution
    public static Individu muter(Individu individu) {
        Individu z = new Individu(new ArrayList<>(individu.solution), individu.qualite);
        int taille = z.solution.size();
        int nb = (int) (Math.random() * 5);
        for (int i = 0; i < nb; i++) {
            int a = (int) (Math.random() * taille);
            int b = (int) (Math.random() * taille);
            int temp = z.solution.get(a);
            z.solution.set(a, z.solution.get(b));
            z.solution.set(b, temp);
        }
        return z;
    }

    // Méthode pour réaliser l'opération de crossover entre deux individus
    public static List<Individu> crossover(Individu soli, Individu solj, int depart, int tailleSolution) {
        Individu i = new Individu(new ArrayList<>(soli.solution), soli.qualite);
        Individu j = new Individu(new ArrayList<>(solj.solution), solj.qualite);
        int taille = i.solution.size();
        int a = (int) (Math.random() * taille);
        int min1 = a;
        for (int ii = 0; ii < min1; ii++) {
            int temp = j.solution.get(ii);
            j.solution.set(ii, i.solution.get(ii));
            i.solution.set(ii, temp);
        }
        corrigerDescendance(i.solution, depart, tailleSolution);
        corrigerDescendance(j.solution, depart, tailleSolution);
        return new ArrayList<>(List.of(i, j));
    }

    // Méthode pour corriger les solutions des individus après crossover
    public static void corrigerDescendance(List<Integer> descendance, int depart, int tailleSolution) {
        // Création d'un ensemble pour stocker les villes déjà visitées
        Set<Integer> villesVisitees = new HashSet<>();
        // Liste pour stocker les villes répétées dans la solution
        List<Integer> villesRepetees = new ArrayList<>();
        
        // Parcours de chaque ville dans la solution
        for (int ville : descendance) {
            // Vérification si la ville a déjà été visitée et si ce n'est pas la ville de départ
            if (!villesVisitees.add(ville) && ville != depart) {
                // Si oui, ajout de la ville à la liste des villes répétées
                villesRepetees.add(ville);
            }
        }
        
        // Correction des villes répétées
        for (int villeRepetee : villesRepetees) {
            // Recherche d'une nouvelle ville non visitée pour remplacer la ville répétée
            int nouvelleVille = trouverNouvelleVille(descendance, villesVisitees, tailleSolution);
            // Remplacement de la ville répétée par la nouvelle ville
            descendance.set(descendance.indexOf(villeRepetee), nouvelleVille);
            // Ajout de la nouvelle ville à l'ensemble des villes visitées
            villesVisitees.add(nouvelleVille);
        }
    }
    

    // Méthode pour trouver une nouvelle ville non visitée
    public static int trouverNouvelleVille(List<Integer> descendance, Set<Integer> villesVisitees, int tailleSolution) {
        for (int ville = 0; ville <= tailleSolution; ville++) {
            if (!villesVisitees.contains(ville)) {
                return ville;
            }
        }
        return 0;
    }
}

// Classe représentant un individu dans la population
class Individu {
    List<Integer> solution; // Séquence de villes visitées
    int qualite; // Qualité de la solution (distance totale parcourue)

    // Constructeur
    public Individu(List<Integer> solution, int qualite) {
        this.solution = solution;
        this.qualite = qualite;
    }
}
