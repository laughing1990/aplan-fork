package com.augurit.aplanmis.common.service.admin.item.impl;

import com.augurit.agcloud.framework.util.CollectionUtils;
import com.augurit.aplanmis.common.constants.PublishStatus;
import com.augurit.aplanmis.common.domain.AeaItemStateVer;
import com.augurit.aplanmis.common.mapper.AeaItemStateVerMapper;
import com.augurit.aplanmis.common.qo.state.AeaItemStateVersionQo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
@Slf4j
public class AeaItemStateVersionAdminService {

    private AeaItemStateVerMapper aeaItemStateVersionMapper;

    public AeaItemStateVer getSpecificVersion(StateVersionStrategy stateVersionStrategy, String itemVerId, String rootOrgId) {

        Assert.notNull(itemVerId, "itemVerId is null");

        log.info("获取情形版本： itemVerId: {}", itemVerId);
        AeaItemStateVersionQo qo = AeaItemStateVersionQo.createQuery().itemVerIdEq(itemVerId).rootOrgIdEq(rootOrgId);
        List<AeaItemStateVer> versions = aeaItemStateVersionMapper.listAeaItemStateVersionByQueryCriteria(qo);
        return stateVersionStrategy.getSpecificVersion(versions);
    }

    public abstract static class StateVersionStrategy {

        /*获取初始版本*/
        public static final StateVersionStrategy INIT_VERSION = new StateVersionStrategy() {
            @Override
            AeaItemStateVer getSpecificVersion(List<AeaItemStateVer> versions) {
                List<AeaItemStateVer> vers = versions.stream().filter(AeaItemStateVer::isInitVer).collect(Collectors.toList());
                return CollectionUtils.isNotEmpty(vers) ? vers.get(0) : null;
            }
        };
        /*获取最新未发布版本*/
        public static final StateVersionStrategy UN_PUBLISHED = new StateVersionStrategy() {
            @Override
            AeaItemStateVer getSpecificVersion(List<AeaItemStateVer> versions) {
                List<AeaItemStateVer> vers = versions.stream().filter(AeaItemStateVer::isUnPublishedVer).collect(Collectors.toList());
                return CollectionUtils.isNotEmpty(vers) ? vers.get(0) : null;
            }
        };
        /*获取最新已发布版*/
        public static final StateVersionStrategy PUBLISHED = new StateVersionStrategy() {
            @Override
            AeaItemStateVer getSpecificVersion(List<AeaItemStateVer> versions) {
                List<AeaItemStateVer> vers = versions.stream().filter(AeaItemStateVer::isPublishedVer).collect(Collectors.toList());
                Assert.isTrue(vers.size() <= 1, "同一时刻发现多个已发布版本， 请联系管理员检查数据");
                return CollectionUtils.isNotEmpty(vers) ? vers.get(0) : null;
            }
        };
        /*最新试运行版*/
        public static final StateVersionStrategy LATEST_TEST_VERSION = new StateVersionStrategy() {
            @Override
            AeaItemStateVer getSpecificVersion(List<AeaItemStateVer> versions) {
                List<AeaItemStateVer> vers = versions.stream()
                        .filter(AeaItemStateVer::isTestRunVer)
                        .sorted((a, b) -> a.getCreateTime().before(b.getCreateTime()) ? -1 : 1)
                        .collect(Collectors.toList());
                return CollectionUtils.isNotEmpty(vers) ? vers.get(0) : null;
            }
        };
        /*获取最大版本*/
        public static final StateVersionStrategy MAX_VERSION = new StateVersionStrategy() {
            @Override
            AeaItemStateVer getSpecificVersion(List<AeaItemStateVer> versions) {
                List<AeaItemStateVer> vers = versions.stream()
                        .sorted((a, b) -> a.getVerNum() < b.getVerNum() ? 1 : -1)
                        .collect(Collectors.toList());
                return CollectionUtils.isNotEmpty(vers) ? vers.get(0) : null;
            }
        };

        /*先获取已发布版本，不存在再获取最大版本*/
        public static final StateVersionStrategy PUBLISH_FIRST_THEN_MAX = new StateVersionStrategy() {
            @Override
            AeaItemStateVer getSpecificVersion(List<AeaItemStateVer> versions) {
                AeaItemStateVer specificVersion = PUBLISHED.getSpecificVersion(versions);
                if (specificVersion == null) {
                    specificVersion = MAX_VERSION.getSpecificVersion(versions);
                }
                return specificVersion;
            }
        };

        abstract AeaItemStateVer getSpecificVersion(List<AeaItemStateVer> versions);
    }

    public Double getNextVersion(String itemVerId, String rootOrgId) {

        Optional<Double> maxVerNum = aeaItemStateVersionMapper.listAeaItemStateVersionByQueryCriteria(AeaItemStateVersionQo.createQuery()
                .itemVerIdEq(itemVerId)
                .rootOrgIdEq(rootOrgId)
                .isDeletedEq(null)
        ).stream()
                .max((v1, v2) -> v1.getVerNum() > v2.getVerNum() ? 1 : -1)
                .map(AeaItemStateVer::getVerNum);
        return maxVerNum.orElse(0.01D) + 1;
    }

    public void deprecateAllTestRunVersion(String stateVerId, String itemVerId, String rootOrgId) {

        aeaItemStateVersionMapper.listAeaItemStateVersionByQueryCriteria(AeaItemStateVersionQo.createQuery()
                .itemVerIdEq(itemVerId)
                .itemStateVerIdNotEq(stateVerId)
                .rootOrgIdEq(rootOrgId)
                .verStatusEq(PublishStatus.TEST_RUN)
        ).stream().forEach(v -> {
            v.deprecate();
            aeaItemStateVersionMapper.updateAeaItemStateVer(v);
        });
    }

    public void deprecateAllPublishedVersion(String stateVerId, String itemVerId, String rootOrgId) {

        aeaItemStateVersionMapper.listAeaItemStateVersionByQueryCriteria(AeaItemStateVersionQo.createQuery()
                .itemVerIdEq(itemVerId)
                .itemStateVerIdNotEq(stateVerId)
                .rootOrgIdEq(rootOrgId)
                .verStatusEq(PublishStatus.PUBLISHED)
        ).stream().forEach(v -> {
            v.deprecate();
            aeaItemStateVersionMapper.updateAeaItemStateVer(v);
        });
    }

    @Autowired
    public void setAeaItemStateVersionMapper(AeaItemStateVerMapper aeaItemStateVersionMapper) {
        this.aeaItemStateVersionMapper = aeaItemStateVersionMapper;
    }
}

