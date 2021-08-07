package loshica.quiz;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Set;

public class LeaderboardAdapter extends RecyclerView.Adapter<LeaderboardAdapter.ViewHolder> {

    private final Set<User> users;

    LeaderboardAdapter(Set<User> users) { this.users = users; }

    @NonNull
    @Override
    public LeaderboardAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
            .inflate(R.layout.item_user, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LeaderboardAdapter.ViewHolder holder, int position) {
        User user = users.toArray(new User[0])[position];
        holder.nameView.setText(user.getName());
        holder.scoreView.setText(user.getScore());
    }

    @Override
    public int getItemCount() { return users.size(); }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        final TextView nameView, scoreView;

        ViewHolder(View view) {
            super(view);
            nameView = (TextView) view.findViewById(R.id.user_name);
            scoreView = (TextView) view.findViewById(R.id.user_score);
        }
    }
}
