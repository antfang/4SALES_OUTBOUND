package com.sufang.dailog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.bin.david.form.core.SmartTable;
import com.bin.david.form.core.TableConfig;
import com.bin.david.form.data.column.Column;
import com.bin.david.form.data.format.draw.ImageResDrawFormat;
import com.bin.david.form.data.format.sequence.BaseSequenceFormat;
import com.bin.david.form.data.table.TableData;
import com.bin.david.form.listener.OnColumnItemClickListener;
import com.sufang.model.DropModel;
import com.sufang.scanner.PrintHistory;
import com.sufang.scanner.R;
import com.sufang.scanner.databinding.ActivityDeliveryBinding;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2018/1/31.
 */

public class TableDialog extends Dialog {
    private Button yes, no;//确定按钮
    private TextView titleTv;//消息标题文本
    private MySmartTable table_smart;//表格
    private String titleStr;//从外界设置的title文本
    private String messageStr;//从外界设置的消息文本
    //确定文本和取消文本的显示内容
    private String yesStr, noStr;

    private onNoOnclickListener noOnclickListener;//取消按钮被点击了的监听器
    private onYesOnclickListener yesOnclickListener;//确定按钮被点击了的监听器

    /**
     * 设置取消按钮的显示内容和监听
     *
     * @param str
     * @param onNoOnclickListener
     */
    public void setNoOnclickListener(String str, onNoOnclickListener onNoOnclickListener) {
        if (str != null) {
            noStr = str;
        }
        this.noOnclickListener = onNoOnclickListener;
    }

    /**
     * 设置确定按钮的显示内容和监听
     *
     * @param str
     * @param onYesOnclickListener
     */
    public void setYesOnclickListener(String str, onYesOnclickListener onYesOnclickListener) {
        if (str != null) {
            yesStr = str;
        }
        this.yesOnclickListener = onYesOnclickListener;
    }

    public TableDialog(Context context) {
        super(context, R.style.Dialog_Msg);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.table_dailog);
        //按空白处不能取消动画
        setCanceledOnTouchOutside(false);
        //初始化界面控件
        initView();
        //初始化界面数据
        initData();
        //初始化界面控件的事件
        initEvent();

    }

    /**
     * 初始化界面的确定和取消监听器
     */
    private void initEvent() {
        //设置确定按钮被点击后，向外界提供监听
        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (yesOnclickListener != null) {
                    yesOnclickListener.onYesClick("ok");
                }
            }
        });

        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (noOnclickListener != null) {
                    noOnclickListener.onNoClick();
                }
            }
        });
    }

    List<String> name_list=null;
    List<String> field_list=null;
    List<PrintHistory> data_list = null;
    /**
     * 初始化界面控件的显示数据
     */
    private void initData() {
        //如果用户自定了title和message
        if (titleStr != null) {
            titleTv.setText(titleStr);
        }
        if (messageStr != null) {
//            messageTv.setText(messageStr);
        }
        //如果设置按钮的文字
        if (yesStr != null) {
            yes.setText(yesStr);
        }
        initTable_1();

    }
    private void initTable_1() {
        List<Column> column_list = new ArrayList<>();
        int i = 0 ;
        for(String name :name_list)
        {
            Column<Object> column = new Column<>(name,field_list.get(i++));
            column_list.add(column);
        }
        if(data_list==null){
            data_list= new ArrayList<>();
        }
        TableData<PrintHistory> tableData = new TableData<PrintHistory>("Table",data_list,column_list);
        table_smart.setTableData(tableData);
        TableConfig config = table_smart.getConfig();
        config.setShowTableTitle(false);
        config.setShowXSequence(false);
        tableData.setYSequenceFormat(new BaseSequenceFormat(){
            @Override
            public String format(Integer integer) {
                return String.valueOf(integer-1);
            }
        });
    }
    /**
     * 初始化界面控件
     */
    private void initView() {
        yes = (Button) findViewById(R.id.table_yes);
        no = (Button) findViewById(R.id.table_no);
        titleTv = (TextView) findViewById(R.id.table_title_1);
        table_smart = (MySmartTable) findViewById(R.id.table_smart_1);
        setfocuse();
    }

    /**
     * 从外界Activity为Dialog设置标题
     *
     * @param title
     */
    public void setTitle(String title) {
        titleStr = title;
    }
    public void setData(List<String> name, List<String> field,List<PrintHistory> data){
        name_list = name;
        field_list = field;
        data_list = data;
    }

    public void setfocuse(){

    }

    /**
     * 从外界Activity为Dialog设置dialog的message
     *
     * @param message
     */
    public void setMessage(String message) {
        messageStr = message;
    }

    /**
     * 设置确定按钮和取消被点击的接口
     */
    public interface onYesOnclickListener {
        public void onYesClick(String phone);
    }

    public interface onNoOnclickListener {
        public void onNoClick();
    }

    @Override
    public void show() {
        super.show();
        /**
         * 设置宽度全屏，要设置在show的后面
         */
        WindowManager.LayoutParams layoutParams = getWindow().getAttributes();
        layoutParams.width= ViewGroup.LayoutParams.MATCH_PARENT;
        layoutParams.height= ViewGroup.LayoutParams.MATCH_PARENT;
        getWindow().getDecorView().setPadding(0, 0, 0, 0);
        getWindow().setAttributes(layoutParams);
    }
}
