package com.example.roomassignment.Model;

import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.roomassignment.Model.CallBack.UpdateUserClickListner;
import com.example.roomassignment.Model.MyRoom.UserEntity;
import com.example.roomassignment.R;
import com.example.roomassignment.View.HomeActivity;

import java.util.ArrayList;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>{

    Context context;
    ArrayList<UserEntity> arr;
    UpdateUserClickListner updateUserClickListner;
    public RecyclerViewAdapter(HomeActivity homeActivity, ArrayList<UserEntity> arr){
        this.context = homeActivity;
        this.arr = arr;
        this.updateUserClickListner=homeActivity;
    }

    public void insertSingleItem(UserEntity userEntity) {
        arr.add(userEntity);
        int insertIndex = arr.size();
        notifyItemInserted(insertIndex);
    }

    /// for update item in recyclerview and use notifyItemChanged for notify to the recyclerview that something has been changed


    public void updateItem(int position, UserEntity userEntity) {
        arr.set(position, userEntity);
        notifyItemChanged(position);
    }

    public void addAtPostions(int pos,UserEntity userEntity){
        arr.add(pos,userEntity);
        notifyDataSetChanged();
    }

    /// Remove item from recyclerview and notifyItemRemoved for notify to the recyclerview that an item has been changed
    public void removeItem(int position) {
        arr.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, arr.size());
    }

    public UserEntity getUser(int pos){
       return arr.get(pos);
    }


   // Binds the given View to the position. The View can be a View previously retrieved via onCreateViewHolder it whould be iterate for all item
    @NonNull
    @Override
    public RecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view=layoutInflater.inflate(R.layout.cart_item_layout,parent,false);
        return new ViewHolder(view);
    }


    //will be used to display items of the adapter using onBindViewHolder
    @Override
    public void onBindViewHolder(@NonNull RecyclerViewAdapter.ViewHolder holder, int position) {
       UserEntity userEntity=arr.get(position);
       holder.user_name.setText(userEntity.getName());
       holder.user_desc.setText(userEntity.getDesc());
       holder.user_id.setText(String.valueOf(userEntity.getId()));
       String imageStringUri=userEntity.getUser_image();
       if(userEntity.getTime()){
           holder.timeDate.setVisibility(View.VISIBLE);
           holder.timeDate.setText(userEntity.getDate() + " " + userEntity.getAtime());
       }else{
           holder.timeDate.setVisibility(View.GONE);
       }

       if(imageStringUri!=null && !imageStringUri.isEmpty()){
           Uri uri=Uri.parse(imageStringUri);
           Glide.with(context).load(uri).apply(RequestOptions.circleCropTransform()).into(holder.user_image);
       }

        Button edit=holder.edit;
        Button del=holder.delete;

        holder.cardv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateUserClickListner.onEdit(arr.get(position),position);
            }
        });
        del.setOnClickListener(new View.OnClickListener(){
            /// creating custom dialog box for confirmation
            @Override
            public void onClick(View v){
                AlertDialog.Builder builder=new AlertDialog.Builder(context);
                builder.setMessage("Do you want to Delete?")
                        .setCancelable(false)
                        .setPositiveButton("yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                UserEntity userEntity1=arr.get(position);
                                updateUserClickListner.onDelete(userEntity1,position);
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                AlertDialog alertDialog=builder.create();
                alertDialog.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return arr.size();
    }


    // it wholud be initialize the layout views so that we can access easly in onBindViewHolder method
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView user_name;
        TextView user_desc;
        TextView user_id;
        RelativeLayout cardv;
        ImageView user_image;
        Button edit;
        Button delete;
        TextView timeDate;
        public ViewHolder(@NonNull View itemView){
            super(itemView);
            user_id=itemView.findViewById(R.id.userid);
            user_name=itemView.findViewById(R.id.username);
            user_desc=itemView.findViewById(R.id.userdesc);
            user_image=itemView.findViewById(R.id.cartimage);
            edit=itemView.findViewById(R.id.edit);
            delete=itemView.findViewById(R.id.delete);
            cardv = itemView.findViewById(R.id.cardv);
            timeDate = itemView.findViewById(R.id.timeanddate);
        }
    }
}
