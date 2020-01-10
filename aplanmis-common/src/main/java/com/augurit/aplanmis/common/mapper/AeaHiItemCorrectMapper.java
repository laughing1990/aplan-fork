package com.augurit.aplanmis.common.mapper;

import com.augurit.aplanmis.common.domain.AeaHiItemCorrect;
import com.augurit.aplanmis.common.dto.MatCorrectAndSuppleDto;
import com.augurit.aplanmis.common.vo.MatCorrectConfirmVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Mapper
@Repository
public interface AeaHiItemCorrectMapper {

    void insertAeaHiItemCorrect(AeaHiItemCorrect aeaHiItemCorrect) throws Exception;

    void updateAeaHiItemCorrect(AeaHiItemCorrect aeaHiItemCorrect) throws Exception;

    void deleteAeaHiItemCorrect(@Param("id") String id) throws Exception;

    List<AeaHiItemCorrect> listAeaHiItemCorrect(AeaHiItemCorrect aeaHiItemCorrect) throws Exception;

    AeaHiItemCorrect getAeaHiItemCorrectById(@Param("id") String id) throws Exception;

    List<MatCorrectConfirmVo> searchStayMatCorrectListByKeyword(@Param("keyword") String keyword, @Param("currentOrgId") String currentOrgId, @Param("currentUserId") String currentUserId);

    List<MatCorrectConfirmVo> searchStayMatCorrectListByCurrentUser(@Param("unitInfoId") String unitInfoId, @Param("userId") String userId, @Param("keyword") String keyword);

    /**
     * 材料补齐补正dao
     *
     * @param unitInfoId
     * @param userId
     * @param keyword
     * @return
     */
    List<MatCorrectAndSuppleDto> searchMatSupportAndCompletByUser(@Param("unitInfoId") String unitInfoId, @Param("userInfoId") String userId, @Param("keyword") String keyword);

    /**
     * 查询待补齐的申报实例
     *
     * @param applyinstIds 需要查询的申报实例id
     */
    List<String> listCorrectingApplyinstId(@Param("applyinstIds") Set<String> applyinstIds);
}
