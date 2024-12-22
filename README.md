# Méta-Heuristiques - Master SII

This repository contains the work for the Méta-Heuristiques (Metaheuristics) module in the Master SII program, focusing on the resolution of the "Voyageur de Commerce" (Traveling Salesman Problem). The project is divided into several TPs (Travaux Pratiques), each implementing a different algorithm for solving the problem, such as DFS, A*, Genetic Algorithm, and PSO. The detailed task descriptions are provided in `.doc` files included in the repository.

## Repository Structure

- **TP1: Modélisation du problème du Voyageur de Commerce**
  - Java file :  `VoyageurDeCommerce.java`.
  - Tasks include generating instances of the problem, modeling a solution, checking the validity of a solution, and evaluating a solution.

- **TP2: Recherche Exacte - DFS** BFS**
  - Java files : `VoyageurCommerceDFS.java`, `ProblemeVoyageurCommerceBFS.java`
  - Implement the Depth First Search (DFS) algorithm for solving the Traveling Salesman Problem.
  - Implement the Breadth First Search (BFS) algorithm for solving the Traveling Salesman Problem.
  - Test the algorithm with different problem sizes.

- **TP3: Recherche Heuristique - A* (A étoile)**
  - Java files : `TSP_AStar.java` , `TSP_AStarr.java`
  - Implement the A* algorithm for solving the Traveling Salesman Problem.
  - Propose a heuristic and a cost function for the problem.
  - Test the algorithm with different problem sizes.

- **TP4: Algorithme Génétique**
  - Implement a random solution generator, fitness function, crossover operator, and mutation operator.
  - Implement the Genetic Algorithm for solving the Traveling Salesman Problem.
  - Test different parameter values and problem sizes.

- **TP5: Algorithme PSO (Particle Swarm Optimization)**
  - Implement the PSO algorithm for solving the Traveling Salesman Problem.
  - Tasks include implementing the velocity update, solution update, pBest, and gBest functions.
  - Test different parameter values and problem sizes.

## Files Description
The detailed task descriptions for each TP are provided in `.doc` files located in the respective TP folders.

## Requirements

To run the Java files and code, ensure you have the following installed:

- Java Development Kit (JDK)
- Any IDE (e.g., IntelliJ IDEA, Eclipse) or command-line tools for compiling and running Java code.

## How to Use

1. Clone this repository to your local machine:
   ```bash
   git clone https://github.com/Ken0uz/metha-heuristiques-tps-master-sii.git

2. Navigate to the desired TP folder
3. Compile and run the Java files using your IDE or the command line:
   ```bash
   javac VoyageurCommerceDFS.java
   java VoyageurCommerceDFS
4. Follow the steps and explanations in the .doc files for each TP.
   

