import java.util.Comparator;
//Compare scores from bottom to top
public class HighscoreScoreComparator implements Comparator<Highscore> {
    public int compare(Highscore highscoreA, Highscore highscoreB) {
        return Double.compare(highscoreA.getScore(), highscoreB.getScore());
    }
}