package com.learn.shruti.workforceanalysis;

import android.support.v7.view.menu.MenuView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.learn.shruti.workforceanalysis.Model.Employee;
import com.learn.shruti.workforceanalysis.Model.Review;

import java.util.List;

/**
 * Created by Shruti on 22/07/2017.
 */
public class EmployeeAdapter extends RecyclerView.Adapter<EmployeeAdapter.EmployeeViewHolder>  {

    private List<Employee> empList;

    public EmployeeAdapter(List<Employee> empList) {
        this.empList = empList;
    }

    @Override
    public EmployeeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new EmployeeViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_employee_item, parent,false));
    }

    @Override
    public void onBindViewHolder(EmployeeViewHolder holder, int position) {

        Employee e = empList.get(position);
        holder.empname.setText(e.empName);
        holder.empemail.setText(e.empEmail);
        holder.phone.setText(e.Phone.toString());
        holder.empid.setText(e.employeeID);
        holder.desig.setText(e.designation);
    }

    @Override
    public int getItemCount() {
        return empList == null ? 0 : empList.size();
    }

    class EmployeeViewHolder extends RecyclerView.ViewHolder{

        TextView empname;
        TextView empemail;
        TextView empid;
        TextView phone;
        TextView desig;

        public EmployeeViewHolder(View itemView) {
            super(itemView);

            empname = (TextView) itemView.findViewById(R.id.empnamerecycle);
            empemail = (TextView) itemView.findViewById(R.id.empemailrecycle);
            empid = (TextView) itemView.findViewById(R.id.empidrecycle);
            phone = (TextView) itemView.findViewById(R.id.empcontactrecycle);
            desig = (TextView) itemView.findViewById(R.id.empdesigrecycle);
        }
    }
}
