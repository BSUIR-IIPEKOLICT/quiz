package com.example.quiz.view;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Set;

import com.example.quiz.R;
import com.example.quiz.model.Player;

public class LeaderboardAdapter extends RecyclerView.Adapter<LeaderboardAdapter.ViewHolder> {

    private final ArrayList<Player> players;

    public LeaderboardAdapter(Set<Player> players) {
        ArrayList<Player> list = new ArrayList<>(players); // перезасовываем пришедший сет в массив
        Collections.sort(list, (o1, o2) -> o2.score - o1.score); // сортировка по очкам по убыванию
        this.players = list;
    }

    @NonNull
    @Override
    public LeaderboardAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
            .inflate(R.layout.item_player, parent, false); // layout элемента списка
        return new ViewHolder(view);
    }

    // метод, вызываемый при создании нового элемента recycler view
    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull LeaderboardAdapter.ViewHolder holder, int position) {
        Player player = players.toArray(new Player[0])[position]; // получаем конкретного игрока
        holder.nameView.setText(player.name); // записываем в вьюшку для имени его имя
        holder.scoreView.setText(Integer.toString(player.score)); // то же самое для очков
    }

    // метода, задающий размер списка (равен длине пришедшего массива/сета)
    @Override
    public int getItemCount() { return players.size(); }

    // метод для организации макета элементов списка
    public static class ViewHolder extends RecyclerView.ViewHolder {
        final TextView nameView, scoreView;

        ViewHolder(View view) {
            super(view);
            nameView = view.findViewById(R.id.player_name); // вьюшка для имени игрока
            scoreView = view.findViewById(R.id.player_score); // вьюшка для очков игрока
        }
    }
}
