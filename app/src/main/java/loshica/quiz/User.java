package loshica.quiz;

public class User {

    private String name;
    private String score;

    public User(String name, int score) {
        this.name = name;
        this.score = Integer.toString(score);
    }

    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

    public String getScore() { return score; }

    public void setScore(int score) { this.score = Integer.toString(score); }
}
