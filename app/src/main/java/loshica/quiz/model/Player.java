package loshica.quiz.model;

import androidx.annotation.NonNull;

import org.bson.types.ObjectId;

public class Player {

    public ObjectId _id;
    public String name;
    public int score;

    public Player(String name, int score) {
        this._id = new ObjectId();
        this.name = name;
        this.score = score;
    }

    public Player(String name, int score, ObjectId _id) {
        this._id = _id;
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
