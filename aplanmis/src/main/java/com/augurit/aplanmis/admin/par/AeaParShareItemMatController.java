package com.augurit.aplanmis.admin.par;

import com.augurit.agcloud.framework.exception.InvalidParameterException;
import com.augurit.agcloud.framework.ui.pager.EasyuiPageInfo;
import com.augurit.agcloud.framework.ui.result.ResultForm;
import com.augurit.agcloud.framework.ui.ztree.ZtreeNode;
import com.augurit.agcloud.framework.util.StringUtils;
import com.augurit.aplanmis.common.domain.AeaItemInout;
import com.augurit.aplanmis.common.domain.AeaParShareMat;
import com.augurit.aplanmis.common.service.admin.par.AeaParShareMatService;
import com.github.pagehelper.Page;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 材料共享-Controller 页面控制转发类
 */
@RestController
@RequestMapping("/aea/share/mat")
public class AeaParShareItemMatController {
    private static Logger logger = LoggerFactory.getLogger(AeaParShareItemMatController.class);

    @Autowired
    private AeaParShareMatService aeaParShareMatService;

    /**
     * 获取事项输出材料列表
     *
     * @param aeaItemInout
     * @param page
     * @return
     */
    @RequestMapping("/listOutItemMat.do")
    public EasyuiPageInfo<AeaItemInout> listOutItemMat(AeaItemInout aeaItemInout, Page page) {

        logger.debug("分页查询，过滤条件为{}，对象为{}", aeaItemInout);
        return aeaParShareMatService.listOutItemMat(aeaItemInout, page);
    }

    /**
     * 获取事项输入材料列表
     *
     * @param aeaItemInout
     * @param page
     * @return
     */
    @RequestMapping("/listInItemMat.do")
    public EasyuiPageInfo<AeaItemInout> listInItemMat(AeaItemInout aeaItemInout, Page page) {

        logger.debug("分页查询，过滤条件为{}，对象为{}", aeaItemInout);
        return aeaParShareMatService.listInItemMat(aeaItemInout, page);
    }

    /**
     * 获取事项输出树
     *
     * @param themeVerId
     * @return
     */
    @RequestMapping("/listOutItemTreeByThemeVerId")
    public List<ZtreeNode> listOutItemTreeByThemeVerId(String themeVerId){

        if(StringUtils.isBlank(themeVerId)){
            throw new InvalidParameterException("无法获取主题版本号!");
        }
        return aeaParShareMatService.listOutItemTreeByThemeVerId(themeVerId);
    }

    /**
     * 获取事项输入树
     *
     * @param themeVerId
     * @return
     */
    @RequestMapping("/listInItemTreeByThemeVerId.do")
    public List<ZtreeNode> listInItemTreeByThemeVerId(String themeVerId){

        if(StringUtils.isBlank(themeVerId)){
            throw new InvalidParameterException("无法获取主题版本号!");
        }
        return aeaParShareMatService.listInItemTreeByThemeVerId(themeVerId);
    }

    @RequestMapping("/saveSharemat.do")
    public ResultForm saveSharemat(AeaParShareMat aeaParShareMat) throws Exception {

        if(aeaParShareMat!=null){
            if(StringUtils.isBlank(aeaParShareMat.getThemeVerId())){
                throw new InvalidParameterException("传递themeVerId参数!");
            }
            if(StringUtils.isBlank(aeaParShareMat.getOutItemVerId())){
                throw new InvalidParameterException("传递outItemVerId参数为空!");
            }
            if(StringUtils.isBlank(aeaParShareMat.getOutInoutId())){
                throw new InvalidParameterException("传递outInoutId参数为空!");
            }
            if(StringUtils.isBlank(aeaParShareMat.getInItemVerId())){
                throw new InvalidParameterException("传递inItemVerId参数为空!");
            }
            if(StringUtils.isBlank(aeaParShareMat.getInItemStateVerId())){
                throw new InvalidParameterException("传递inItemStateVerId参数为空!");
            }
//            if(aeaParShareMat.getInInoutIds()==null){
//                throw new InvalidParameterException("传递inInoutIds参数为空!");
//            }
//            if(aeaParShareMat.getInInoutIds()!=null&&aeaParShareMat.getInInoutIds().length==0){
//                throw new InvalidParameterException("传递inInoutIds参数为空!");
//            }
            aeaParShareMatService.saveSharemat(aeaParShareMat);
            return new ResultForm(true);
        }else{
            throw new InvalidParameterException("未传递参数!");
        }
    }

    @RequestMapping("/inOutCheckboxList.do")
    public List<AeaParShareMat> inOutCheckboxList(String themeVerId, String outInoutId){

        if(StringUtils.isBlank(themeVerId)){
            throw new InvalidParameterException("参数themeVerId为空!");
        }
        if(StringUtils.isBlank(outInoutId)){
            throw new InvalidParameterException("参数outInoutId为空!");
        }
        return aeaParShareMatService.inOutCheckboxList(themeVerId, outInoutId);
    }
}
