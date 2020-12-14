package com.sufang.scanner.adapter;

import android.content.Context;

import com.sufang.scanner.PrintHistory;
import com.sufang.scanner.R;
import com.sufang.scanner.adapter.base.BaseBindingRecyclerAdapter;
import com.sufang.scanner.adapter.base.BindingViewHolder;
import com.sufang.scanner.databinding.HistoryItemBinding;

import java.util.List;

/**
 * @author by admin on 2019/11/30.
 */
public class HistoryAdapter extends BaseBindingRecyclerAdapter<PrintHistory> {

    public HistoryAdapter(Context context, List<PrintHistory> dataList) {
        super(context, dataList, R.layout.history_item);
    }

    @Override
    public void onBindViewHolder(BindingViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        HistoryItemBinding viewHolder = (HistoryItemBinding) holder.binding;

        final PrintHistory bean = datalist.get(position);
        if (bean == null ) {
            return;
        }
//        viewHolder.txtLocation.setText(bean.getLocation());
//        viewHolder.txtFosbId.setText(bean.getCrrId());
//        viewHolder.txtDryStart.setText(bean.getDryStartTime());
//        viewHolder.txtDryEnd.setText(bean.getDryEndTime());
//        viewHolder.txtNextClean.setText(bean.getNextCleanTime());
    }
}
