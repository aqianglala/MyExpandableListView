package com.example.zy1584.myexpandableview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by zy1584 on 2017-6-28.
 */

public class MyAdapter extends BaseAdapter {
    private ArrayList<ItemBean> mData;
    private LayoutInflater inflater;
    public MyAdapter(ArrayList<ItemBean> data, Context context) {
        mData = data;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object getItem(int i) {
        return mData.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup viewGroup) {
        final ViewHolder holder;
        if (convertView == null){
            convertView = inflater.inflate(R.layout.item_list, viewGroup, false);
            holder = new ViewHolder();
            holder.tv_name = convertView.findViewById(R.id.tv_name);
            holder.btn_install = convertView.findViewById(R.id.btn_install);
            holder.ll_container = convertView.findViewById(R.id.ll_container);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        final ItemBean item = (ItemBean) getItem(position);
        holder.tv_name.setText(item.getName());
        if (mListener != null){
            holder.btn_install.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mListener.onClick(position, item, holder);
                }
            });
        }
        holder.ll_container.setVisibility(View.GONE);
        return convertView;
    }

    class ViewHolder{
        TextView tv_name;
        Button btn_install;
        LinearLayout ll_container;
    }
    private OnInstallButtonClickListener mListener;
    interface OnInstallButtonClickListener{
        void onClick(int position, ItemBean itemBean, ViewHolder holder);
    }

    public void setOnButtonClickListener(OnInstallButtonClickListener listener){
        mListener = listener;
    }
}
