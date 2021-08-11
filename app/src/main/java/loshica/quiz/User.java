package loshica.quiz;

import androidx.annotation.NonNull;

public class User {

    public String name;
    public int score;

    public User(String name, int score) {
        this.name = name;
        this.score = score;
    }

    // TODO: only for development
    @NonNull
    @Override
    public String toString() {
        return "[name: " + name + "; score: " + score + "]";
    }
}
