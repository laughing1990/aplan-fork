package com.augurit.aplanmis.supermarket.utils;

import com.augurit.agcloud.framework.constant.Status;
import com.augurit.agcloud.framework.util.StringUtils;
import com.augurit.aplanmis.common.vo.LoginInfoVo;

public class LoginUtil {
    /**
     * 获取当前登录人名
     *
     * @param loginInfoVo 登录信息
     * @return string
     */
    public static String getCreater(LoginInfoVo loginInfoVo) throws Exception {
        validLoginStatus(loginInfoVo);
        String isPersonAccount = loginInfoVo.getIsPersonAccount();
        if (StringUtils.isNotBlank(isPersonAccount) && "1".equals(isPersonAccount)) {
            //个人账号
            return loginInfoVo.getPersonName();
        } else {
            return loginInfoVo.getUnitName();
        }
    }

    /**
     * 是否业主单位登录
     *
     * @param loginInfoVo 登录信息
     * @return boolean
     * @throws Exception e
     */
    public static boolean isOwnerUnit(LoginInfoVo loginInfoVo) throws Exception {
        validLoginStatus(loginInfoVo);
        return StringUtils.isBlank(loginInfoVo.getUnitId()) || !Status.ON.equals(loginInfoVo.getIsOwner());
    }


    public static void validLoginStatus(LoginInfoVo loginInfoVo) throws Exception {
        if (null == loginInfoVo) throw new Exception("not login or login time out");
    }


}
