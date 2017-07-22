package com.learn.shruti.workforceanalysis;

import android.support.v7.view.menu.MenuView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.learn.shruti.workforceanalysis.Model.Review;

import java.util.List;

/**
 * Created by Shruti on 22/07/2017.
 */
public class FeedbackAdapter extends RecyclerView.Adapter<FeedbackAdapter.FeedbackViewHolder>  {

    private List<Review> reviewList;

    public FeedbackAdapter(List<Review> reviewList) {
        this.reviewList = reviewList;
    }

    @Override
    public FeedbackViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new FeedbackViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_user_item, parent,false));
    }

    @Override
    public void onBindViewHolder(FeedbackViewHolder holder, int position) {

        Review r = reviewList.get(position);
        holder.empname.setText(r.employeeName);
        holder.empemail.setText(r.employeeEmail);
        holder.comment.setText(r.comments);
        holder.reviewDate.setText(r.dateOfReview);
    }

    @Override
    public int getItemCount() {
        return reviewList == null ? 0 : reviewList.size();
    }

    class FeedbackViewHolder extends RecyclerView.ViewHolder{

        TextView empname;
        TextView empemail;
        TextView reviewDate;
        TextView comment;

        public FeedbackViewHolder(View itemView) {
            super(itemView);

            empname = (TextView) itemView.findViewById(R.id.tvempname);
            empemail = (TextView) itemView.findViewById(R.id.tvempemail);
            reviewDate = (TextView) itemView.findViewById(R.id.tvdate);
            comment = (TextView) itemView.findViewById(R.id.tvmsg);
        }
    }
}
