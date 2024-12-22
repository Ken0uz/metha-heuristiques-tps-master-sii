import java.util.List;

public class Individual {
    List<Integer> solution;
    int quality;

    public Individual(List<Integer> solution, int quality) {
        this.solution = solution;
        this.quality = quality;
    }
}