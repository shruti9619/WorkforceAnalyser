package com.learn.shruti.workforceanalysis;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.learn.shruti.workforceanalysis.Model.AnonymousComplaint;
import com.learn.shruti.workforceanalysis.Model.Employee;

import java.util.List;

/**
 * Created by Shruti on 31/08/2017.
 */
public class SafeComplaintAdapter extends RecyclerView.Adapter<SafeComplaintAdapter.AnonymousComplaintViewHolder>  {

    private List<AnonymousComplaint> compList;

    public SafeComplaintAdapter(List<AnonymousComplaint> compList) {
        this.compList = compList;
    }

    @Override
    public AnonymousComplaintViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new AnonymousComplaintViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_safe_complaint, parent,false));
    }

    @Override
    public void onBindViewHolder(AnonymousComplaintViewHolder holder, int position) {

        AnonymousComplaint c = compList.get(position);
        holder.issuename.setText(c.Issue);
        holder.issuedesc.setText(c.details);
        holder.issuedate.setText(c.dateOfIssue);
    }

    @Override
    public int getItemCount() {
        return compList == null ? 0 : compList.size();
    }

    class AnonymousComplaintViewHolder extends RecyclerView.ViewHolder{

        TextView issuename;
        TextView issuedesc;
        TextView issuedate;


        public AnonymousComplaintViewHolder(View itemView) {
            super(itemView);

            issuename = (TextView) itemView.findViewById(R.id.issuerecycle);
            issuedesc = (TextView) itemView.findViewById(R.id.issuedescrecycle);
            issuedate = (TextView) itemView.findViewById(R.id.issuedaterecycle);

        }
    }
}
