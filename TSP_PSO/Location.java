import java.util.ArrayList;

// Définition d'une classe Location
public class Location {

    // Attribut pour stocker les coordonnées
    public ArrayList<Double> locations;

    // Constructeur prenant en paramètre une liste de coordonnées
    public Location(ArrayList<Double> locations)
    {
        // Initialisation de l'attribut locations avec une nouvelle ArrayList<Double>
        this.locations = new ArrayList<Double>();
        // Copie des coordonnées passées en paramètre dans l'attribut locations
        this.locations = locations;
    }

    // Méthode pour obtenir les coordonnées actuelles
    public ArrayList<Double> getLocations() {
        return this.locations;
    }

    // Méthode pour mettre à jour les coordonnées
    public void updateLocation(ArrayList<Double> update)
    {
        // Ajout des nouvelles coordonnées à la liste existante
        this.locations.addAll(update);
    }
}
