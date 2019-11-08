package com.augurit.aplanmis.common.convert;

import com.augurit.agcloud.framework.constant.Status;
import com.augurit.aplanmis.common.constants.ActiveStatus;
import com.augurit.aplanmis.common.constants.DeletedStatus;
import com.augurit.aplanmis.common.domain.AeaItemInout;
import com.augurit.aplanmis.common.domain.AeaItemMat;
import com.augurit.aplanmis.common.utils.NumberUtils;
import com.augurit.aplanmis.common.vo.AeaItemMatKpVo;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.UUID;

/**
 * 与开普事项对接所需材料实体表
 */
public class AeaItemMatConvert implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 获取材料列表转换
     *
     * @param isNew
     * @param vo
     * @return
     */
    public static AeaItemMat useByGetClqdByItemCode(boolean isNew, AeaItemMat mat, AeaItemMatKpVo vo) {

        String matId = null;
        if (isNew) {
            matId = UUID.randomUUID().toString();
        } else {
            matId = vo.getMaterialId();
        }
        mat.setMatName(vo.getMaterialName());
        mat.setMatCode(matId);
        mat.setIsGlobalShare(Status.OFF);
        mat.setIsActive(ActiveStatus.ACTIVE.getValue());
        mat.setIsDeleted(DeletedStatus.NOT_DELETED.getValue());
        return mat;
    }

    public static void convertKpMatToAeaMat(AeaItemMatKpVo entity, AeaItemMat aeaItemMat) {

        aeaItemMat.setMatId(entity.getId()); // 材料id
        aeaItemMat.setMatCode(UUID.randomUUID().toString()); // 材料编号
        String matName = entity.getMaterialName();
        if(StringUtils.isNotBlank(entity.getFilenameAlias())){
            matName = entity.getFilenameAlias();
        }
        aeaItemMat.setMatName(matName); // 材料名称
        aeaItemMat.setIsGlobalShare(Status.ON);                   //默认1
        aeaItemMat.setReceiveMode("0");//接收方式
        aeaItemMat.setZcqy(entity.getZcqy()); // 是否容缺
        aeaItemMat.setPaperMatType(entity.getZzjyqlx()); // 纸质材料类型
        aeaItemMat.setDuePaperType(entity.getYbf()); // 原件验收
        aeaItemMat.setDuePaperCount(NumberUtils.isNumeric(entity.getYfs().trim()) ? Long.parseLong(entity.getYfs().trim()) : 0L);   //原件数
        aeaItemMat.setDueCopyType(entity.getFjbf()); // 复件验收
        aeaItemMat.setDueCopyCount(NumberUtils.isNumeric(entity.getFjfs().trim()) ? Long.parseLong(entity.getFjfs().trim()) : 0L);  //复印件数
        aeaItemMat.setMatFrom(entity.getLyqd());            //材料来源
        aeaItemMat.setPaperIsRequire(entity.getZzjyqbx());  //纸质材料是否必需，0表示容缺，1表示必须
        aeaItemMat.setAttIsRequire(entity.getDzjyqbx()); // 电子材料是否必需
        aeaItemMat.setMatMemo(entity.getBz());              //备注
        aeaItemMat.setIsActive(ActiveStatus.ACTIVE.getValue());                        //是否启用
        aeaItemMat.setIsDeleted(DeletedStatus.NOT_DELETED.getValue());                       //是否删除
        /*
        aeaItemMat.setAttDirId("");                         //File Position ID
        aeaItemMat.setMatHolder("");                        //Material Owner
        aeaItemMat.setIsOfficialDoc("");                    //是否批复文件，0表示否，1表示是
        aeaItemMat.setAttIsRequire("");                     //电子材料是否必需，0表示容缺，1表示必须
        */
        aeaItemMat.setIsCommon(entity.getTycl());           //是否通用材料
        aeaItemMat.setReviewKeyPoints(entity.getSjyd());    //审查要点
        aeaItemMat.setReviewBasis(entity.getSjydyj());      //审查要点依据
//        aeaItemMat.setMatRequire(entity.getSjyd());  // 收件要点
//        aeaItemMat.setMatBasis(entity.getSjydyj()); // 收件依据
        aeaItemMat.setKbslAttachList(entity.getKbslAttachList());
        aeaItemMat.setSjydsAttachList(entity.getSjydsAttachList());
        aeaItemMat.setYbslAttachList(entity.getYbslAttachList());
    }

    public static void initAeaItemInout(AeaItemMatKpVo entity, AeaItemInout inout, String matId) {

        inout.setInoutId(UUID.randomUUID().toString());
        /*  //事项、情形ID在API调用后返回
            if(StringUtils.isNotBlank(entity.getItemcode())){
                inout.setItemId(entity.getItemcode());
            }
            if(StringUtils.isNotBlank(entity.getYwqxId())){
                inout.setItemStateId(entity.getYwqxId());
            }
        */
        inout.setIsOwner("0");      //是否为当前事项直接所有，0表示其他事项所有，1表示当前事项所有
        inout.setIsInput("1");      //是否输入，0表示输出，1表示输入
        inout.setFileType("mat");   //文件类型
        inout.setMatId(matId);
        inout.setIsStateIn("0");    //是否为情形输入，0表示直接的事项输入，1表示关联情形的输入【当IS_INPUT=1】
        inout.setIsDeleted(StringUtils.isNotBlank(entity.getMaterialFzsxsj()) ? DeletedStatus.DELETED.getValue() : DeletedStatus.NOT_DELETED.getValue());//控制删除的字段
    }
}
