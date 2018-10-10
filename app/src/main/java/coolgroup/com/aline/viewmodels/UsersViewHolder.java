package coolgroup.com.aline.viewmodels;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import coolgroup.com.aline.R;

public class UsersViewHolder extends RecyclerView.ViewHolder{

    public View mView;


    public UsersViewHolder(@NonNull View itemView) {
        super(itemView);

        mView = itemView;
    }

    public void setName(String name) {

        TextView nameView = (TextView) mView.findViewById(R.id.single_user_name);
        nameView.setText(name);

    }

    public void setStatus(String status) {

        TextView statusView = (TextView) mView.findViewById(R.id.single_user_status);
        statusView.setText(status);

    }
}
