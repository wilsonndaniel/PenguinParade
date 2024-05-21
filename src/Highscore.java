class Highscore implements Comparable<Highscore> {
	private String name;
	private double score;

    public Highscore(String name, double score) {
        this.name = name;
        this.score = score;
    }

    public String getName() {
        return name;
    }

    public double getScore() {
        return score;
    }
    
    public void setName(String s) {
        name = s;
    }

    public void setScore(int s) {
        score = s;
    }
    
    public String toString() {
        return name + ": " + score + "s";
    }
    public String inputToFile() {
    	return name + " " + score;
    }

    //Compare scores in descending order
    public int compareTo(Highscore highscore) {
        return Double.compare(highscore.score, this.score);
    }
}

//class HighscoreComparator implements Comparator<Highscore> {
//    @Override
//    public int compare(Highscore h1, Highscore h2) {
//        // Compare scores in ascending order
//        return Integer.compare(h1.getScore(), h2.getScore());
//    }
//}