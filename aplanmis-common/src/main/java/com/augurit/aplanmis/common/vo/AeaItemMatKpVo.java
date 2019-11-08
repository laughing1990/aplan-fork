package com.augurit.aplanmis.common.vo;

import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 与开普事项对接所需实体表
 */
public class AeaItemMatKpVo implements Serializable {

    /**
     * Those field provide by document only and api not provide which from Kampo Co.
     */
    private String bz;                  //备注
    private String filenameAlias;       //材料别名
    private String itemcode;            //事项编码
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private String materialFzsxsj;      //材料标准废置时间(Delete Time)
    private String materialFzyy;        //材料标准废置依据
    private String sjydslIds;           //收件要点示例文件集合
    private String sjydyj;              //收件要点依据
    private String ywqxId;              //业务情形编码

    private String fjbf;                //复件验收
    private String fjfs;                //复件份数
    private String id;                  //材料要素ID
    private String lyqd;                //来源渠道
    private String materialId;          //材料唯一化库ID
    private String materialName;        //材料标准名称
    private String sjyd;                //收件要点
    private String sort;                //排序
    private String status;              //状态
    private String tycl;                //是否是通用材料
    private String ybf;                 //原件验收
    private String yfs;                 //原件份数
    private String zcqy;                //是否支持容缺
    private String zzjyqbx;             //纸质件是否必须(1:必须)
    private String zzjyqlx;             //纸质件类型
    private String dzjyqbx;  // 电子材料是否必须

    private List<AeaItemMatAttr> kbslAttachList; // 模板附件
    private List<AeaItemMatAttr> sjydsAttachList; // 审查样本
    private List<AeaItemMatAttr> ybslAttachList; // 材料样例文

    private String createDeptid; // 创建部门id
    private String createUserid; // 创建用户id
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime; // 创建时间
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime; // 更新时间

    public String getBz() {
        return bz;
    }

    public void setBz(String bz) {
        this.bz = bz;
    }

    public String getFilenameAlias() {
        return filenameAlias;
    }

    public void setFilenameAlias(String filenameAlias) {
        this.filenameAlias = filenameAlias;
    }

    public String getItemcode() {
        return itemcode;
    }

    public void setItemcode(String itemcode) {
        this.itemcode = itemcode;
    }

    public String getMaterialFzsxsj() {
        return materialFzsxsj;
    }

    public void setMaterialFzsxsj(String materialFzsxsj) {
        this.materialFzsxsj = materialFzsxsj;
    }

    public String getMaterialFzyy() {
        return materialFzyy;
    }

    public void setMaterialFzyy(String materialFzyy) {
        this.materialFzyy = materialFzyy;
    }

    public String getSjydslIds() {
        return sjydslIds;
    }

    public void setSjydslIds(String sjydslIds) {
        this.sjydslIds = sjydslIds;
    }

    public String getSjydyj() {
        return sjydyj;
    }

    public void setSjydyj(String sjydyj) {
        this.sjydyj = sjydyj;
    }

    public String getYwqxId() {
        return ywqxId;
    }

    public void setYwqxId(String ywqxId) {
        this.ywqxId = ywqxId;
    }

    public String getFjbf() {
        return fjbf;
    }

    public void setFjbf(String fjbf) {
        this.fjbf = fjbf;
    }

    public String getFjfs() {
        return fjfs;
    }

    public void setFjfs(String fjfs) {
        this.fjfs = fjfs;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLyqd() {
        return lyqd;
    }

    public void setLyqd(String lyqd) {
        this.lyqd = lyqd;
    }

    public String getMaterialId() {
        return materialId;
    }

    public void setMaterialId(String materialId) {
        this.materialId = materialId;
    }

    public String getMaterialName() {
        return materialName;
    }

    public void setMaterialName(String materialName) {
        this.materialName = materialName;
    }

    public String getSjyd() {
        return sjyd;
    }

    public void setSjyd(String sjyd) {
        this.sjyd = sjyd;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTycl() {
        return tycl;
    }

    public void setTycl(String tycl) {
        this.tycl = tycl;
    }

    public String getYbf() {
        return ybf;
    }

    public void setYbf(String ybf) {
        this.ybf = ybf;
    }

    public String getYfs() {
        return yfs;
    }

    public void setYfs(String yfs) {
        this.yfs = yfs;
    }

    public String getZcqy() {
        return zcqy;
    }

    public void setZcqy(String zcqy) {
        this.zcqy = zcqy;
    }

    public String getZzjyqbx() {
        return zzjyqbx;
    }

    public void setZzjyqbx(String zzjyqbx) {
        this.zzjyqbx = zzjyqbx;
    }

    public String getZzjyqlx() {
        return zzjyqlx;
    }

    public void setZzjyqlx(String zzjyqlx) {
        this.zzjyqlx = zzjyqlx;
    }

    public String getDzjyqbx() {
        return dzjyqbx;
    }

    public void setDzjyqbx(String dzjyqbx) {
        this.dzjyqbx = dzjyqbx;
    }

    public String getCreateDeptid() {
        return createDeptid;
    }

    public void setCreateDeptid(String createDeptid) {
        this.createDeptid = createDeptid;
    }

    public String getCreateUserid() {
        return createUserid;
    }

    public void setCreateUserid(String createUserid) {
        this.createUserid = createUserid;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public List<AeaItemMatAttr> getKbslAttachList() {
        return kbslAttachList;
    }

    public void setKbslAttachList(List<AeaItemMatAttr> kbslAttachList) {
        this.kbslAttachList = kbslAttachList;
    }

    public List<AeaItemMatAttr> getSjydsAttachList() {
        return sjydsAttachList;
    }

    public void setSjydsAttachList(List<AeaItemMatAttr> sjydsAttachList) {
        this.sjydsAttachList = sjydsAttachList;
    }

    public List<AeaItemMatAttr> getYbslAttachList() {
        return ybslAttachList;
    }

    public void setYbslAttachList(List<AeaItemMatAttr> ybslAttachList) {
        this.ybslAttachList = ybslAttachList;
    }
}
