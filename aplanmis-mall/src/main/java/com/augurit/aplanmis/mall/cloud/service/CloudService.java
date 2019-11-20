package com.augurit.aplanmis.mall.cloud.service;

import com.augurit.agcloud.bsc.domain.BscAttForm;
import com.augurit.aplanmis.mall.cloud.vo.AttAndDirVo;
import com.augurit.aplanmis.mall.cloud.vo.AttAndDirWithChildVo;
import com.augurit.aplanmis.mall.cloud.vo.CloudDirListVo;
import com.github.pagehelper.PageInfo;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface CloudService {

    /**
     * 查询云盘文件根目录及文件列表
     * @param unitInfoId
     * @param userInfoId
     * @return
     */
    AttAndDirVo getMyCloudRootDirAndAttList(String unitInfoId, String userInfoId)throws Exception;



    /**
     * 根据root文件夹查询子文件夹及文件
     * @param dirId
     * @return
     */
    AttAndDirVo getMyCloudChildDirAndAttList(String dirId)throws Exception;

    /**
     * 根据用户信息查询文件夹Tree
     * @param unitInfoId
     * @param userInfoId
     * @return
     */
    List<CloudDirListVo> getDirTreeByUser(String unitInfoId, String userInfoId)throws Exception;

    /**
     * 根据文件夹查询文件列表
     * @param dirID
     * @return
     */
    PageInfo<BscAttForm> getAttsByDirId(String dirID, int pageNum, int pageSize,String keyword,boolean bool)throws Exception;


    void deleteDirAndFiles(String dirId, String recordId) throws Exception;

    void moveFilesFromDir(String currentDirId, String targetDirId, String[] detailIds, String recordId) throws Exception;

    /**
     * 上传云盘文件
     * @param dirId
     * @param request
     * @throws Exception
     */
    void uploadCloudFiles(String dirId, HttpServletRequest request) throws Exception;

    /**
     * 根目录、文件及子目录、文件列表
     * @param request
     * @return
     */
    AttAndDirWithChildVo getMyCloudDirAndAttList(HttpServletRequest request)throws Exception;


    /**
     * 模糊查询用户文件
     * @param keyword
     * @return
     */
    List<BscAttForm> listAttLinkAndDetailList(String keyword,HttpServletRequest request);

    /**
     * 重命名云盘文件名
     * @param detailId
     * @param attName
     * @throws Exception
     */
    void renameAttName(String detailId,String attName)throws Exception;

}
