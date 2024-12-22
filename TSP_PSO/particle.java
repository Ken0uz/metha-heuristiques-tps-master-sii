import java.util.ArrayList;

// Définition de la classe particle
public class particle {

    // Attributs pour représenter la route, la position, la vitesse et le meilleur personnel (pBest)
    public Route route;
    public Location location;
    public Velocity velocity;
    public Route pBest;
    public Location locationPBest;

    // Attributs de taille du problème et nombre maximum d'itérations
    public static int problemSize;
    public static int maximumIterations = 100;

    // Constructeur de la classe particle
    public particle(Route route)
    {
        // Initialisation des attributs avec les valeurs passées en paramètre 
        this.route = route;
        this.pBest = route;
        location = new Location(new ArrayList<Double>());
        locationPBest = new Location(new ArrayList<Double>());
        velocity = new Velocity(new ArrayList<Double>());
        problemSize = route.cities.size();
    }

    // Méthode pour rechercher la meilleure solution dans le voisinage
    public void getBest()
    {
        // Initialisation de la variable pour stocker la solution voisine
        Route neighorRoute = null;
        int i = 0 ;
        // Boucle sur un nombre maximal d'itérations
        while (i < maximumIterations)
        {
            // Obtention d'une solution dans le voisinage
            neighorRoute = getNeighborhoodSolution(new Route(this.pBest));
            // Comparaison avec la meilleure solution personnelle
            if(neighorRoute.getFullRouteDistance() < pBest.getFullRouteDistance()){
                // Mise à jour du meilleur personnel si une meilleure solution est trouvée
                pBest = new Route(neighorRoute);
            }
            // Incrémentation du compteur d'itérations
            i++;
        }
    }

    // Méthode pour obtenir une solution dans le voisinage
    public Route getNeighborhoodSolution(Route aRoute)
    {
        // Sélection aléatoire de deux indices différents dans la route
        int random1 = 0 ;
        int random2 = 0 ;
        while(random1==random2){
            random1 = (int) (aRoute.cities.size()* Math.random());
            random2 = (int) (aRoute.cities.size()* Math.random());
        }
        // Échange des villes aux indices sélectionnés pour obtenir une solution voisine
        City city_1 = aRoute.cities.get(random1);
        City city_2 = aRoute.cities.get(random2);
        aRoute.cities.set(random2,city_1);
        aRoute.cities.set(random1,city_2);
        return aRoute;
    }

    // Méthode pour échanger des villes avec la meilleure solution personnelle
    public void swapWithLocation(int coeff)
    {
        // Boucle pour effectuer un certain nombre d'échanges de villes
        for(int i = 0 ; i < coeff ; i++)
        {
            int random1 = 0 ;
            int random2 = 0 ;

            // Sélection aléatoire de deux indices différents dans la meilleure solution personnelle
            while(random1==random2){
                random1 = (int) (pBest.cities.size()* Math.random());
                random2 = (int) (pBest.cities.size()* Math.random());
            }
            // Échange des villes aux indices sélectionnés
            City city_1 = pBest.cities.get(random1);
            City city_2 = pBest.cities.get(random2);
            pBest.cities.set(random2,city_1);
            pBest.cities.set(random1,city_2);
        }
    }
}
