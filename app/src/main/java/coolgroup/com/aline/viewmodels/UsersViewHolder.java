package coolgroup.com.aline.viewmodels;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import coolgroup.com.aline.R;
import de.hdodenhof.circleimageview.CircleImageView;

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

    public void setImage(String thumb_image, Context ctx){

        CircleImageView userImageView = (CircleImageView) mView.findViewById(R.id.single_user_image);
        Picasso.with(ctx).load(thumb_image).placeholder(R.drawable.avatar_male).into(userImageView);

    }

}
