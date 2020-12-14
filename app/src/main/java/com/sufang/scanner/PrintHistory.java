package com.sufang.scanner;


import com.bin.david.form.annotation.SmartColumn;
import com.bin.david.form.annotation.SmartTable;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;

import java.util.Date;

import org.greenrobot.greendao.annotation.Keep;
import org.greenrobot.greendao.annotation.Generated;


@Entity(indexes = {
        @Index(value = "localId")
})
@SmartTable(name = "Parts")
public class PrintHistory {

    /**
     * android本地保存主键
     */
    @Id
    private Long localId;
    @SmartColumn(id = 1, name = "sel")
    private boolean sel;
    /**
     * ID
     */
    private int id;
    /**
     * 单号+项次
     */
    @SmartColumn(id = 2, name = "单号")
    private String order_no_;
    /**
     * 单号
     */
    private String order_no;
    /**
     * 项次
     */
    private String no;
    /**
     * Parts ID
     */
    @SmartColumn(id = 3, name = "Pallet No")
    private String pallet_no;
    @SmartColumn(id = 4, name = "Pallet Key")
    private String pallet_key;
    /**
     * Parts Code
     */
    @SmartColumn(id = 4, name = "Code")
    private String code;
    /**
     * Parts Code
     */
    @SmartColumn(id = 4, name = "Name")
    private String name;
    /**
     * Lot ID
     */
    @SmartColumn(id = 5, name = "Lot ID")
    private String lot_id;


    /**
     * ERP Lot ID
     */
    @SmartColumn(id = 5, name = "ERP Lot ID")
    private String erp_lot_id;


    /**
     * MES Lot ID
     */
    @SmartColumn(id = 5, name = "MES Lot ID")
    private String mes_lot_id;
    /**
     * delivery_no
     */
    @SmartColumn(id = 6, name = "Delivery No")
    private String delivery_no;
    /**
     * 规格
     */
    @SmartColumn(id = 7, name = "Specification")
    private String specification;
    /**
     * 出厂日期
     */
    @SmartColumn(id = 8, name = "出厂日期")
    private String ouput_date;
    /**
     * 收货日期
     */
    @SmartColumn(id = 9, name = "收货日期")
    private String input_date;
    /**
     * 到期日期
     */
    @SmartColumn(id = 10, name = "到期日期")
    private String expire_date;
    /**
     * 领料日期
     */
    @SmartColumn(id = 11, name = "领料日期")
    private String picking_date;
    /**
     * 装载日期
     */
    @SmartColumn(id = 12, name = "装载日期")
    private String mount_date;
    /**
     * 卸载日期
     */
    @SmartColumn(id = 13, name = "卸载日期")
    private String unmount_date;
    /**
     * SN
     */
    @SmartColumn(id = 14, name = "SN")
    private String SN;
    /**
     * 数量
     */
    @SmartColumn(id = 15, name = "QTY")
    private String qty;
    /**
     * 创建日期
     */
    @SmartColumn(id = 16, name = "Creat Date")
    private Date createTime;


    public PrintHistory(Boolean sel, String pallet_no, String delivery_no,
                        String ouput_date, String expire_date, String picking_date, Date createTime) {
        this.sel = sel;
        this.pallet_no = pallet_no;
        this.delivery_no = delivery_no;
        this.ouput_date = ouput_date;
        this.expire_date = expire_date;
        this.picking_date = picking_date;
        this.createTime = createTime;
    }


    public PrintHistory() {
    }


    @Keep()
    public PrintHistory(Long localId, boolean sel, int id, String order_no_, String order_no, String no,
                        String pallet_no, String part_code, String lot_id, String delivery_no, String specification,
                        String ouput_date, String input_date, String expire_date, String picking_date,
                        String mount_date, String unmount_date, String SN, String qty, Date createTime) {
        this.localId = localId;
        this.sel = sel;
        this.id = id;
        this.order_no_ = order_no_;
        this.order_no = order_no;
        this.no = no;
        this.pallet_no = pallet_no;
        this.code = part_code;
        this.lot_id = lot_id;
        this.delivery_no = delivery_no;
        this.specification = specification;
        this.ouput_date = ouput_date;
        this.input_date = input_date;
        this.expire_date = expire_date;
        this.picking_date = picking_date;
        this.mount_date = mount_date;
        this.unmount_date = unmount_date;
        this.SN = SN;
        this.qty = qty;
        this.createTime = createTime;
    }


    @Keep()
    public PrintHistory(Long localId, boolean sel, int id, String order_no_, String order_no, String no,
                        String pallet_no, String pallet_key, String part_code, String lot_id, String delivery_no,
                        String specification, String ouput_date, String input_date, String expire_date,
                        String picking_date, String mount_date, String unmount_date, String SN, String qty,
                        Date createTime) {
        this.localId = localId;
        this.sel = sel;
        this.id = id;
        this.order_no_ = order_no_;
        this.order_no = order_no;
        this.no = no;
        this.pallet_no = pallet_no;
        this.pallet_key = pallet_key;
        this.code = part_code;
        this.lot_id = lot_id;
        this.delivery_no = delivery_no;
        this.specification = specification;
        this.ouput_date = ouput_date;
        this.input_date = input_date;
        this.expire_date = expire_date;
        this.picking_date = picking_date;
        this.mount_date = mount_date;
        this.unmount_date = unmount_date;
        this.SN = SN;
        this.qty = qty;
        this.createTime = createTime;
    }


    @Generated(hash = 1046475803)
    public PrintHistory(Long localId, boolean sel, int id, String order_no_, String order_no, String no, String pallet_no,
            String pallet_key, String code, String name, String lot_id, String erp_lot_id, String mes_lot_id,
            String delivery_no, String specification, String ouput_date, String input_date, String expire_date,
            String picking_date, String mount_date, String unmount_date, String SN, String qty, Date createTime) {
        this.localId = localId;
        this.sel = sel;
        this.id = id;
        this.order_no_ = order_no_;
        this.order_no = order_no;
        this.no = no;
        this.pallet_no = pallet_no;
        this.pallet_key = pallet_key;
        this.code = code;
        this.name = name;
        this.lot_id = lot_id;
        this.erp_lot_id = erp_lot_id;
        this.mes_lot_id = mes_lot_id;
        this.delivery_no = delivery_no;
        this.specification = specification;
        this.ouput_date = ouput_date;
        this.input_date = input_date;
        this.expire_date = expire_date;
        this.picking_date = picking_date;
        this.mount_date = mount_date;
        this.unmount_date = unmount_date;
        this.SN = SN;
        this.qty = qty;
        this.createTime = createTime;
    }


    public Boolean getSel() {
        return sel;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getOrder_no_() {
        return order_no + "#" + no;
    }

    public String getOrder_no() {
        return order_no;
    }

    public void setOrder_no(String order_no) {
        this.order_no = order_no;
        this.order_no_ = order_no + "#" + no;
    }

    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
        this.order_no_ = order_no + "#" + no;
    }

    public String getPallet_no() {
        return pallet_no;
    }

    public void setPallet_no(String pallet_no) {
        this.pallet_no = pallet_no;
    }

    public String getPallet_key() {
        return pallet_key;
    }

    public void setPallet_key(String pallet_key) {
        this.pallet_key = pallet_key;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLot_id() {
        return lot_id;
    }

    public void setLot_id(String lot_id) {
        this.lot_id = lot_id;
    }

    public String getMes_lot_id() {
        return mes_lot_id;
    }

    public void setMes_lot_id(String mes_lot_id) {
        this.mes_lot_id = mes_lot_id;
    }
    public String getErp_lot_id() {
        return erp_lot_id;
    }

    public void setErp_lot_id(String erp_lot_id) {
        this.erp_lot_id = erp_lot_id;
    }

    public void setSpecification(String specification) {
        this.specification = specification;
    }

    public String getSpecification() {
        return specification;
    }

    public String getDelivery_no() {
        return delivery_no;
    }

    public void setDelivery_no(String delivery_no) {
        this.delivery_no = delivery_no;
    }

    public String getOuput_date() {
        return ouput_date;
    }

    public void setOuput_date(String ouput_date) {
        this.ouput_date = ouput_date;
    }

    public String getInput_date() {
        return input_date;
    }

    public void setInput_date(String input_date) {
        this.input_date = input_date;
    }

    public String getExpire_date() {
        return expire_date;
    }

    public void setExpire_date(String expire_date) {
        this.expire_date = expire_date;
    }

    public String getPicking_date() {
        return picking_date;
    }

    public void setPicking_date(String picking_date) {
        this.picking_date = picking_date;
    }

    public String getUnMount_Date() {
        return unmount_date;
    }

    public void setUnMount_Date(String UnMount_Date) {
        this.unmount_date = UnMount_Date;
    }

    public String getMount_Date() {
        return mount_date;
    }

    public void setMount_Date(String Mount_Date) {
        this.mount_date = Mount_Date;
    }

    public String getSN() {
        return SN;
    }

    public void setSN(String SN) {
        this.SN = SN;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }


    public Long getLocalId() {
        return this.localId;
    }


    public void setLocalId(Long localId) {
        this.localId = localId;
    }


    public void setSel(boolean sel) {
        this.sel = sel;
    }


    public void setOrder_no_(String order_no_) {
        this.order_no_ = order_no_;
    }

    public String getMount_date() {
        return this.mount_date;
    }


    public void setMount_date(String mount_date) {
        this.mount_date = mount_date;
    }


    public String getUnmount_date() {
        return this.unmount_date;
    }


    public void setUnmount_date(String unmount_date) {
        this.unmount_date = unmount_date;
    }


    public String getQty() {
        return this.qty;
    }
}
