package loshica.quiz;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Set;

public class LeaderboardAdapter extends RecyclerView.Adapter<LeaderboardAdapter.ViewHolder> {

    private final ArrayList<User> users;

    LeaderboardAdapter(Set<User> users) {
        ArrayList<User> list = new ArrayList<>(users);
        Collections.sort(list, (o1, o2) -> o2.score - o1.score);
        this.users = list;
    }

    @NonNull
    @Override
    public LeaderboardAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
            .inflate(R.layout.item_user, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull LeaderboardAdapter.ViewHolder holder, int position) {
        User user = users.toArray(new User[0])[position];
        holder.nameView.setText(user.name);
        holder.scoreView.setText(Integer.toString(user.score));
    }

    @Override
    public int getItemCount() { return users.size(); }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        final TextView nameView, scoreView;

        ViewHolder(View view) {
            super(view);
            nameView = view.findViewById(R.id.user_name);
            scoreView = view.findViewById(R.id.user_score);
        }
    }
}
