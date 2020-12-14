package com.sufang.scanner.adapter.base;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sufang.scanner.callback.OnItemClickListener;
import com.sufang.scanner.callback.OnItemLongClickListener;
import com.sufang.util.CommonUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * @author admin
 */
public class BaseBindingRecyclerAdapter<T> extends RecyclerView.Adapter<BindingViewHolder> {

    public Context context;
    public List<T> datalist;
    public LayoutInflater inflater;
    private int resourceId;
    private OnItemClickListener itemClick;
    private OnItemLongClickListener itemLongClick;

    public BaseBindingRecyclerAdapter(Context context, int rid) {
        this(context, new ArrayList<T>(), rid);
    }
    public BaseBindingRecyclerAdapter(Context context, List<T> datalist, int rid) {
        this.context = context;
        this.datalist = datalist;
        inflater = LayoutInflater.from(context);
        resourceId = rid;
    }

    public void setData(int page, List<T> data) {
        if (page == 1) {
            setData(data);
        } else {
            addData(data);
        }
    }

    public void setData(List<T> datalist) {
        this.datalist = datalist;
        notifyDataSetChanged();
    }
    public void addData(List<T> datalist) {
        if (this.datalist != null && !CommonUtil.isNull(datalist)) {
            this.datalist.addAll(datalist);
            notifyDataSetChanged();
        }
    }
    public List<T> getData() {
        return this.datalist;
    }

    public void clear() {
        this.datalist=new ArrayList<>();
        notifyDataSetChanged();
    }

    @Override
    public BindingViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewDataBinding binding = DataBindingUtil.inflate(inflater, resourceId, parent, false);
        return new BindingViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(BindingViewHolder holder, final int position) {
        if(null != itemClick){
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    itemClick.onItemClick(position);
                }
            });
        }
        if(null != itemLongClick){
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    itemLongClick.onItemLongClick(position);
                    return true;//false会同时触发click
                }
            });
        }
    }

    public void setOnItemClick(OnItemClickListener itemClick1) {
        itemClick = itemClick1;
    }
    public void setOnItemLongClick(OnItemLongClickListener itemClick1) {
        itemLongClick = itemClick1;
    }

    public T getItem(int position) {
        if(0 == getItemCount()) {
            return null;
        }
        return datalist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        if(CommonUtil.isEmpty(datalist)) {
            return 0;
        }
        return datalist.size();
    }

}