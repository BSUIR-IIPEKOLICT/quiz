package loshica.quiz;

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

public class LeaderboardAdapter extends RecyclerView.Adapter<LeaderboardAdapter.ViewHolder> {

    private final ArrayList<Player> players;

    LeaderboardAdapter(Set<Player> players) {
        ArrayList<Player> list = new ArrayList<>(players);
        Collections.sort(list, (o1, o2) -> o2.score - o1.score);
        this.players = list;
    }

    @NonNull
    @Override
    public LeaderboardAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
            .inflate(R.layout.item_player, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull LeaderboardAdapter.ViewHolder holder, int position) {
        Player player = players.toArray(new Player[0])[position];
        holder.nameView.setText(player.name);
        holder.scoreView.setText(Integer.toString(player.score));
    }

    @Override
    public int getItemCount() { return players.size(); }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        final TextView nameView, scoreView;

        ViewHolder(View view) {
            super(view);
            nameView = view.findViewById(R.id.player_name);
            scoreView = view.findViewById(R.id.player_score);
        }
    }
}
