package com.example.voting;

import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.voting.database.Poll;

public class PollAdapter extends androidx.recyclerview.widget.ListAdapter<Poll, PollAdapter.ViewHolder> {

    // creating a variable for on item click listener.
    private OnItemClickListener listener;

    private int position;

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
    // creating a constructor class for our adapter class.
    public PollAdapter() {
        super(DIFF_CALLBACK);
    }

    // creating a call back for item of recycler view.
    private static final DiffUtil.ItemCallback<Poll> DIFF_CALLBACK = new DiffUtil.ItemCallback<Poll>() {
        @Override
        public boolean areItemsTheSame(Poll oldItem, Poll newItem) {
            return oldItem.getId() == newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(Poll oldItem, Poll newItem) {
            // below line is to check the course name, description and course duration.
            return oldItem.getTitle().equals(newItem.getTitle()) &&
                    oldItem.getDesc().equals(newItem.getDesc());
        }
    };

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // below line is use to inflate our layout
        // file for each item of our recycler view.
        View item = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.poll_item, parent, false);
        return new ViewHolder(item);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // below line of code is use to set data to
        // each item of our recycler view.
        Poll model = getPollAt(position);
        holder.pollTitle.setText(model.getTitle());
        holder.pollDesc.setText(model.getDesc());
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                setPosition(holder.getAdapterPosition());
                return false;
            }
        });
    }

    // creating a method to get course modal for a specific position.
    public Poll getPollAt(int position) {
        return getItem(position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {
        // view holder class to create a variable for each view.
        TextView pollTitle, pollDesc;


        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo)
        {
            menu.add(Menu.NONE, R.id.delete_menu,
                    Menu.NONE, "Delete");
        }
        ViewHolder(@NonNull View itemView) {
            super(itemView);
            // initializing each view of our recycler view.
            pollTitle = itemView.findViewById(R.id.title_txt_view);
            pollDesc = itemView.findViewById(R.id.body_txt_view);
            itemView.setOnCreateContextMenuListener(this);

            // adding on click listener for each item of recycler view.
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // inside on click listener we are passing
                    // position to our item of recycler view.
                    int position = getAdapterPosition();
                    if (listener != null && position != RecyclerView.NO_POSITION) {
                        listener.onItemClick(getItem(position));
                    }
                }
            });
        }

    }

    @Override
    public void onViewRecycled(ViewHolder holder) {
        holder.itemView.setOnLongClickListener(null);
        super.onViewRecycled(holder);
    }

    public interface OnItemClickListener {
        void onItemClick(Poll model);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }
}