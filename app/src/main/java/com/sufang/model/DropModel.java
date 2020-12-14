package com.sufang.model;

import com.bin.david.form.annotation.SmartColumn;
import com.bin.david.form.annotation.SmartTable;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;

import java.io.Serializable;
import java.util.Date;


@SmartTable(name = "Drop Table")
public class DropModel implements Serializable {
    public DropModel(Boolean sel,String column_1,String column_2)
    {
        this.sel= sel;
        this.column_1=column_1;
        this.column_2 = column_2;
    }
    private static final long serialVersionUID = -3098985139095632120L;
    @SmartColumn(id = 1, name = "sel")
    private boolean sel;
    /**
     * Column1
     */
    @SmartColumn(id = 2, name = "Column1")
    private String column_1;
    /**
     * Column2
     */
    @SmartColumn(id = 3, name = "Column2")
    private String column_2;

    public String getColumn_2()
    {
        return column_2;
    }
    public String getColumn_1()
    {
        return column_1;
    }



}
