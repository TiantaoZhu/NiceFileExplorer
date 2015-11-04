package com.drslark.nicefileexplore.widget;

import com.drslark.nicefileexplore.R;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Created by zhutiantao on 2015/11/2.
 */
public class GeneralViewHolder extends RecyclerView.ViewHolder {
    public ImageView file_icon;
    public TextView file_name;
    public TextView file_desc;
    public CheckBox file_check;
    public RelativeLayout file_container;

    public GeneralViewHolder(View itemView) {
        super(itemView);
        file_check = (CheckBox) itemView.findViewById(R.id.file_check);
        file_desc = (TextView) itemView.findViewById(R.id.file_desc);
        file_name = (TextView) itemView.findViewById(R.id.file_name);
        file_icon = (ImageView) itemView.findViewById(R.id.file_icon);
        file_container = (RelativeLayout) itemView.findViewById(R.id.general_item_view);
    }
}
