package com.augurit.aplanmis.common.mapper;

import base.BaseTest;
import com.augurit.agcloud.framework.security.SecurityContext;
import com.augurit.aplanmis.common.domain.AeaParentProj;
import com.augurit.aplanmis.common.domain.AeaProjInfo;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@DisplayName("测试项目")
public class AeaProjInfoMapperTest extends BaseTest {
    @Autowired
    private AeaProjInfoMapper aeaProjInfoMapper;

    @Test
    @DisplayName("插入项目信息")
    public void createProjInfo() {
        for (int i = 0; i < 2; i++) {
            try {
                aeaProjInfoMapper.insertAeaProjInfo(createAeaProjInfo());
                List<AeaProjInfo> projInfos = aeaProjInfoMapper.listAeaProjInfo(new AeaProjInfo());
                Assertions.assertNotNull(projInfos);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /*@DisplayName("insertAndListParentProj")
    @Test
    public void insertParentProj() throws Exception {
        AeaProjInfo aeaProjInfo = createAeaProjInfo();
        aeaProjInfoMapper.insertAeaProjInfo(aeaProjInfo);
        //测试查询项目
        AeaProjInfo projInfoById = aeaProjInfoMapper.getOnlyAeaProjInfoById(aeaProjInfo.getProjInfoId());
        Assertions.assertNotNull(projInfoById);

        AeaProjInfo childProjInfo = createChildProjInfo(projInfoById);
        //插入父项目
        aeaProjInfoMapper.insertAeaProjInfo(childProjInfo);
        List<AeaProjInfo> projInfos = aeaProjInfoMapper.listAeaProjInfo(new AeaProjInfo());
        Assertions.assertEquals(11, projInfos.size());

        //插入父子项目表
        AeaParentProj aeaParentProj = createAeaParentProj(projInfos);
        aeaProjInfoMapper.insertAeaParentProj(aeaParentProj);
        AeaParentProj aeaParentProjById = aeaProjInfoMapper.getAeaParentProjById(aeaParentProj.getNodeProjId());
        Assertions.assertNotNull(aeaParentProjById);
        //Assertions.assertEquals(PublishStatus.PUBLISHED, aeaParentProjById.getVerStatus());

    }
*/
    /*@Test
    @DisplayName("testTreeSearch")
    public void testParentProjTree() {
        List<AeaParentProj> aeaParentProjs = aeaProjInfoMapper.listAllTreeByChildId("211-2");
        Assertions.assertNotNull(aeaParentProjs);
    }*/

    /* --------------------------------测试节点查询 ------------------------------------------------------*/
    /*@DisplayName("1-根据子节点查询子节点所有的父节点（不包括子节点下的子节点）")
    @Test
    public void testlistAllParentOfChildProjTree() {
        List<AeaParentProj> aeaParentProjs = aeaProjInfoMapper.listAllParentOfChildProjTree("f084121a-3a1f-46f2-b2e5-466c973284b6");//3及节点
        Assertions.assertEquals(3, aeaParentProjs.size());
        List<AeaParentProj> aeaParentProjs1 = aeaProjInfoMapper.listAllParentOfChildProjTree("510c149a-ad6f-4b98-9409-14ee6f1908aa");//ROOT节点
        Assertions.assertEquals(0, aeaParentProjs1.size());
    }

    @DisplayName("2-查询节点下的所有子节点")
    @Test
    public void listAllChildOfNodeTree() {
        List<AeaParentProj> aeaParentProjs = aeaProjInfoMapper.listAllChildOfNodeTree("f084121a-3a1f-46f2-b2e5-466c973284b6");
        Assertions.assertEquals(1, aeaParentProjs.size());
        List<AeaParentProj> projs = aeaProjInfoMapper.listAllChildOfNodeTree("510c149a-ad6f-4b98-9409-14ee6f1908aa");
        Assertions.assertEquals(7, projs.size());
        List<AeaParentProj> projs1 = aeaProjInfoMapper.listAllChildOfNodeTree("3269d548-134f-430e-8a28-5d883119e202");
        Assertions.assertEquals(5, projs1.size());
    }*/

//    @DisplayName("3-listAllTreeByChildId")
//    @Test
//    public void listAllTreeByChildId() {
//        List<AeaParentProj> proj1 = aeaProjInfoMapper.listAllTreeByChildId("510c149a-ad6f-4b98-9409-14ee6f1908aa");
//        List<AeaParentProj> proj2 = aeaProjInfoMapper.listAllTreeByChildId("3269d548-134f-430e-8a28-5d883119e202");
//        List<AeaParentProj> proj3 = aeaProjInfoMapper.listAllTreeByChildId("f084121a-3a1f-46f2-b2e5-466c973284b6");
//        Assertions.assertEquals(0, proj1.size());
//        Assertions.assertEquals(6, proj2.size());
//        Assertions.assertEquals(6, proj3.size());
//
//    }
//
//    @DisplayName("4-根据节点查询节点所有父子节点的AeaProjInfo 包含ROOT节点； 如果传入rootId，只能查到root节点，请使用5查询")
//    @Test
//    public void listAllNodeByChildNode() {
//        List<AeaProjInfo> proj1 = aeaProjInfoMapper.listAllNodeByChildNode("510c149a-ad6f-4b98-9409-14ee6f1908aa");//rootid
//        List<AeaProjInfo> proj2 = aeaProjInfoMapper.listAllNodeByChildNode("3269d548-134f-430e-8a28-5d883119e202");
//        List<AeaProjInfo> proj4 = aeaProjInfoMapper.listAllNodeByChildNode("d4878ac7-adce-4051-85a2-6cc48ebd8733");
//
//        List<AeaProjInfo> proj3 = aeaProjInfoMapper.listAllNodeByChildNode("f084121a-3a1f-46f2-b2e5-466c973284b6");
//        Assertions.assertEquals(1, proj1.size());
//        Assertions.assertEquals(7, proj2.size());
//        Assertions.assertEquals(7, proj4.size());
//        Assertions.assertEquals(7, proj3.size());
//    }
//
//    @DisplayName("5 根据节点查询所有节点下的子节点的 AeaProjInfo")
//    @Test
//    public void listAllChildNodeOfNode() {
//        List<AeaProjInfo> proj1 = aeaProjInfoMapper.listAllChildNodeOfNode("510c149a-ad6f-4b98-9409-14ee6f1908aa");//rootid
//        List<AeaProjInfo> proj2 = aeaProjInfoMapper.listAllChildNodeOfNode("3269d548-134f-430e-8a28-5d883119e202");
//        List<AeaProjInfo> proj3 = aeaProjInfoMapper.listAllChildNodeOfNode("f084121a-3a1f-46f2-b2e5-466c973284b6");
//        Assertions.assertEquals(8, proj1.size());
//        Assertions.assertEquals(6, proj2.size());
//        Assertions.assertEquals(2, proj3.size());
//    }
//
//    @DisplayName("6 根据子节点查询所有的父节点AeaProjInfo")
//    @Test
//    public void listAllParentOfNode() {
//        List<AeaProjInfo> proj1 = aeaProjInfoMapper.listAllParentOfNode("510c149a-ad6f-4b98-9409-14ee6f1908aa");//rootid
//        List<AeaProjInfo> proj2 = aeaProjInfoMapper.listAllParentOfNode("3269d548-134f-430e-8a28-5d883119e202");
//        List<AeaProjInfo> proj3 = aeaProjInfoMapper.listAllParentOfNode("f084121a-3a1f-46f2-b2e5-466c973284b6");
//        Assertions.assertEquals(1, proj1.size());
//        Assertions.assertEquals(2, proj2.size());
//        Assertions.assertEquals(4, proj3.size());
//    }
//
//    @DisplayName("9 根据applyintId 查询项目列表 多项目申报")
//    @Test
//    public void listProjByApplyinstId() {
//        List<AeaProjInfo> projInfos = aeaProjInfoMapper.listProjByApplyinstId("1e9a6cea-78b0-4a0c-a494-bb81bf21af52");
//        Assertions.assertEquals(4, projInfos.size());
//    }
//
//    @DisplayName("10-根据子节点查找root项目信息")
//    @Test
//    public void getRootByChildNode() {
//
//        AeaProjInfo proj1 = aeaProjInfoMapper.getRootByChildNode("510c149a-ad6f-4b98-9409-14ee6f1908aa");//rootid
//        AeaProjInfo proj2 = aeaProjInfoMapper.getRootByChildNode("3269d548-134f-430e-8a28-5d883119e202");
//        AeaProjInfo proj3 = aeaProjInfoMapper.getRootByChildNode("f084121a-3a1f-46f2-b2e5-466c973284b6");
//        Assertions.assertNotNull(proj1);
//        Assertions.assertNotNull(proj2);
//        Assertions.assertNotNull(proj3);
//    }

    /*-------------------------------------------测试删除-----------------------------------------------------------------*/
   /* @Test
    @DisplayName("8-测试条件删除关联表")
    public void delParentProjByCondition() {
        AeaParentProj aeaParentProj = new AeaParentProj();
        aeaParentProj.setChildProjId("2-1-1");
        Integer integer = aeaProjInfoMapper.delParentProjByCondition(aeaParentProj);
        Assertions.assertEquals(1, (int) integer);
        // byParentid
        aeaParentProj = new AeaParentProj();
        aeaParentProj.setParentProjId("121");
        integer = aeaProjInfoMapper.delParentProjByCondition(aeaParentProj);
        Assertions.assertEquals(1, (int) integer);
        //byNoDEiD
        aeaParentProj = new AeaParentProj();
        aeaParentProj.setNodeProjId("12");
        integer = aeaProjInfoMapper.delParentProjByCondition(aeaParentProj);
        Assertions.assertEquals(1, (int) integer);
        //byAll

        aeaParentProj = new AeaParentProj();
        aeaParentProj.setNodeProjId("6");
        aeaParentProj.setParentProjId("0");
        aeaParentProj.setChildProjId("2");
        integer = aeaProjInfoMapper.delParentProjByCondition(aeaParentProj);
        Assertions.assertEquals(1, (int) integer);
    }

    @Test
    @DisplayName("12-根据childId 更新projSeq")
    public void test3() {
        //String delProjId="3269d548-134f-430e-8a28-5d883119e202";
        String delProjId = "d4878ac7-adce-4051-85a2-6cc48ebd8733";
        //select  DISTINCT app.*
        //from AEA_PARENT_PROJ app start with app.PARENT_PROJ_ID ='d4878ac7-adce-4051-85a2-6cc48ebd8733'
        //    connect by app.PARENT_PROJ_ID=prior app.Child_Proj_Id;
        List<AeaParentProj> allChildOfDelNode = aeaProjInfoMapper.listAllChildOfNodeTree(delProjId);
        int num = 0;
        for (AeaParentProj parentProj : allChildOfDelNode) {
            String projSeq = parentProj.getProjSeq();
            if (StringUtils.isNotBlank(projSeq)) {
                String newSeq = Arrays.stream(projSeq.split(",")).filter(s -> !s.equals(delProjId)).collect(Collectors.joining(","));
                parentProj.setProjSeq(newSeq);
                int i = aeaProjInfoMapper.updateProjSeqByChildId(parentProj);
                num += i;
            }
        }
        Assertions.assertEquals(4, num);

    }

    @Test
    @DisplayName("3&&11-listAeaParentProj")
    public void listAeaParentProj() {
        String delProjId = "d4878ac7-adce-4051-85a2-6cc48ebd8733";
        AeaParentProj tempAeaParentProj = new AeaParentProj();
        tempAeaParentProj.setChildProjId(delProjId);
        //int i = aeaProjInfoMapper.delParentProjByCondition(tempAeaParentProj);
        //System.out.println(i);
        //将删除项目下的子项目挂载到父项目下---即将父ID为删除项目id  改为删除项目的父项目ID
        //1 根据删除项目ID查询子项目
        tempAeaParentProj.setChildProjId(null);
        tempAeaParentProj.setParentProjId(delProjId);
        List<AeaParentProj> aeaParentProjs = aeaProjInfoMapper.listAeaParentProj(tempAeaParentProj);
        System.out.println(aeaParentProjs.size());

        String[] childProjIds = null;

        if (aeaParentProjs.size() > 0) {
            childProjIds = aeaParentProjs.parallelStream().map(AeaParentProj::getChildProjId).filter(x -> x != null).toArray(String[]::new);

        }
        //2 更新父子关系
        int i1 = aeaProjInfoMapper.updateParentProjByChildProjIds("3269d548-134f-430e-8a28-5d883119e202", childProjIds);
        System.out.println(i1);
    }

    @Test
    @DisplayName("根据ids查询项目集合")
    public void listAeaProjInfoByIds() {
        List<AeaProjInfo> projInfos = aeaProjInfoMapper.listAeaProjInfo(new AeaProjInfo());
        System.out.println(projInfos.size());
        String[] strings = projInfos.parallelStream().map(AeaProjInfo::getProjInfoId).toArray(String[]::new);
        System.out.println(strings.length);
        List<AeaProjInfo> infos = aeaProjInfoMapper.listAeaProjInfoByIds(strings);
        String projIds = infos.parallelStream().map(AeaProjInfo::getProjInfoId).collect(Collectors.joining(","));
        String projNames = infos.parallelStream().map(AeaProjInfo::getProjName).collect(Collectors.joining(","));
        System.out.println(infos.size());
        System.out.println(projIds);
        System.out.println(projNames);
        Assertions.assertEquals(projInfos.size(), infos.size());
    }

    @Test
    @DisplayName("13-根据子节点名称或localCode 模糊查询查询root节点AeaProjInfo")
    public void listRootByChildNode() {

        List<AeaProjInfo> projInfos = aeaProjInfoMapper.listRootByChildNode("梅州市明鑫物流园");
        projInfos.parallelStream().filter(s -> "r".equals(s.getProjFlag()) || StringUtils.isBlank(s.getProjFlag())).collect(Collectors.toList());

        System.out.println();
    }

    @Test
    @DisplayName("14 查询所有数项目 子项目记录数")
    public void getAllBootstrapTreeGrid() {

        List<AeaProjInfo> allBootstrapTreeGrid = aeaProjInfoMapper.getAllBootstrapTreeGrid();
        System.out.println(allBootstrapTreeGrid.size());
    }

    @Test
    @DisplayName("16 根据rootids 查询所有的项目树")
    public void getBootstrapTreeGridByRootIds() {

        List<AeaProjInfo> allBootstrapTreeGrid = aeaProjInfoMapper.getBootstrapTreeGridByRootIds(new String[]{"5d5ada52-c032-4876-aff4-7a6250c74c12", "e05a38ac-2f00-4f4f-8102-55b1f944a08c"});
        System.out.println(allBootstrapTreeGrid.size());
    }

    @Test
    @DisplayName("17 查询所有的root数量")
    public void getAllRootProj() {
        List<AeaProjInfo> allRootProj = aeaProjInfoMapper.getAllRootProj(null);
        System.out.println(allRootProj.size());

    }


    @Test
    public void Test2() {
        String s = "sjadlajoajojo";
        String[] split = s.split("#");
        System.out.println(split.length);
    }*/

    //根据父项目信息创建子项目
    private AeaProjInfo createChildProjInfo(AeaProjInfo projInfoById) {
        AeaProjInfo aeaProjInfo = new AeaProjInfo();
        if (projInfoById != null) {
            BeanUtils.copyProperties(projInfoById, aeaProjInfo);
        }
        aeaProjInfo.setProjInfoId(UUID.randomUUID().toString());
        aeaProjInfo.setProjName("我是子项目，复制来自父项目");
        return aeaProjInfo;
    }

    public AeaParentProj createAeaParentProj(List<AeaProjInfo> aeaProjInfo) {
        AeaParentProj aeaParentProj = new AeaParentProj();
        aeaParentProj.setNodeProjId(UUID.randomUUID().toString());
        aeaParentProj.setChildProjId(aeaProjInfo.get(0).getProjInfoId());
        aeaParentProj.setParentProjId(aeaProjInfo.get(1).getProjInfoId());
        aeaParentProj.setCreater("xiaohutu");
        aeaParentProj.setCreateTime(new Date());
        return aeaParentProj;

    }

    public AeaProjInfo createAeaProjInfo() {
        int i = (int) (Math.random() * 10);
        AeaProjInfo aeaProjInfo = new AeaProjInfo();
        aeaProjInfo.setProjInfoId(UUID.randomUUID().toString());
        aeaProjInfo.setLocalCode(new SimpleDateFormat("yyyMMddHHmmssSSS").format(new Date()));
        aeaProjInfo.setProjType("QYTZL");
        aeaProjInfo.setProjName("项目名称" + i);
        aeaProjInfo.setMainClass("" + i);
        aeaProjInfo.setCentralCode("" + i);
        aeaProjInfo.setIsJgxm("0");
        aeaProjInfo.setProjUrgency("1");
        aeaProjInfo.setProjAddr("广州市天河区" + i);
        aeaProjInfo.setProjCreateUserCode("" + i);
        aeaProjInfo.setProjCreateUser("xiaohutu" + i);
        aeaProjInfo.setProjCreateMobile("10086-" + i);
        aeaProjInfo.setRootOrgId(SecurityContext.getCurrentOrgId());
        return aeaProjInfo;
    }

    @Test
    public void getUuid() {
        String s = UUID.randomUUID().toString();
        //createAeaProjInfo();
        System.out.println(s);
    }
}
