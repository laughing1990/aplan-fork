<%--<!--@Author: ZL-->--%>
<%--<!--@Date:   2019/05/27-->--%>
<%--<!--@Last Modified by:   ZL-->--%>
<%--<!--@Last Modified time: $ $-->--%>
<%--<!DOCTYPE html>--%>
<%--<html lang="en" xmlns:th="http://www.thymeleaf.org">--%>

<%--<head>--%>
    <%--<!--引入公共css-->--%>
    <%--<th:block th:include="common/common :: commonheader('并联申报')" />--%>
    <%--<link rel="stylesheet" href="../../static/apply/css/index.css" th:href="@{/apply/css/index.css}">--%>
<%--</head>--%>

<%--<body class="gray-body">--%>
<%--<div id="approve" v-cloak v-loading="loading">--%>
    <%--<div class="title-nav">--%>
      <%--<span>投资项目申报前，请登陆 <a href="http://tzxm.hbzwfw.gov.cn/sbglweb/gsxxList?xzqh=130000"--%>
                           <%--target="_blank">“河北省投资项目在线审批监管平台”</a> 进行项目登记赋码</span>--%>
    <%--</div>--%>
    <%--<el-form class="search-proj-info" label-width="115px">--%>
        <%--<el-form-item label="项目/工程查询" id="projCodeInfo">--%>
            <%--<el-select v-model="searchKeyword" filterable remote autofocus reserve-keyword ref="searchProjInfo"--%>
                       <%--no-data-text="暂无匹配的项目/工程" placeholder="请输入项目（工程）的编码 / 名称" :remote-method="searchProjInfoByKeyword"--%>
                       <%--:loading="loadingProjInfo">--%>
                <%--<el-option v-for="item in projInfoList" :key="item.projInfoId" @click.native="projNameSel(item)"--%>
                           <%--:label="item.projName+' （'+item.localCode+'）'" :value="item.projInfoId">--%>
                <%--</el-option>--%>
            <%--</el-select>--%>
            <%--<el-button type="primary" icon="icon ag ag-search" @click="linkQuery()">联网查询</el-button>--%>
        <%--</el-form-item>--%>

    <%--</el-form>--%>
    <%--<div class="content">--%>
        <%--<div class="vertical-tab">--%>
            <%--<ul>--%>
                <%--<li class="vertical-label"--%>
                    <%--v-show="index<showVerLen"--%>
                    <%--v-for="(item, index) in verticalTabData"--%>
                    <%--:class="activeTab==index?'active':item.target"--%>
                    <%--data-name="item.target"--%>
                    <%--:key="index"--%>
                    <%--@click="targetCollapse(item.target,index)">--%>
                    <%--<a :href="'#'+item.target">{{item.label}}</a>--%>
                <%--</li>--%>
            <%--</ul>--%>
        <%--</div>--%>

        <%--<el-collapse v-model="activeNames">--%>
            <%--<!--  基本信息  start  -->--%>
            <%--<el-collapse-item class="clearfix" name="1" id="baseInfo">--%>
                <%--<template slot="title">--%>
                    <%--<i class="header-icon icon ag ag-project-info"></i>项目/工程信息--%>
                <%--</template>--%>
                <%--<el-form label-width="176px" ref="projBascInfoShowFrom" :rules="projBascInfoShowFromRules"--%>
                         <%--:model="projBascInfoShow">--%>
                    <%--<input type="hidden" name="projInfoId" :value="projBascInfoShow.projInfoId">--%>
                    <%--<el-form-item label="项目/工程名称" class="proj-code-info" prop="projName">--%>
                        <%--<el-input name="projName" :disabled="linkQuerySucc" v-model="projBascInfoShow.projName"--%>
                                  <%--placeholder="请输入项目/工程名称">--%>
                        <%--</el-input>--%>
                        <%--<el-button @click="showProjTree()">项目工程</el-button>--%>
                    <%--</el-form-item>--%>
                    <%--<div class="clearfix">--%>
                        <%--<el-form-item class="input-inline" label="项目代码" prop="localCode">--%>
                            <%--<el-input name="localCode" :disabled="linkQuerySucc" v-model="projBascInfoShow.localCode"--%>
                                      <%--placeholder="投资平台登记的项目代码">--%>
                            <%--</el-input>--%>
                        <%--</el-form-item>--%>
                        <%--<el-form-item class="input-inline" label="工程代码" prop="gcbm">--%>
                            <%--<el-input v-model="projBascInfoShow.gcbm" placeholder="请输入工程代码"></el-input>--%>
                        <%--</el-form-item>--%>
                        <%--<el-form-item class="input-inline" label="行政区划" prop="regionalism">--%>
                            <%--<el-select filterable v-model="projBascInfoShow.regionalism" placeholder="请选择行政区划">--%>
                                <%--<el-option v-for="item in districtList" :key="item.id" :label="item.regionName"--%>
                                           <%--:value="item.regionId">--%>
                                <%--</el-option>--%>
                            <%--</el-select>--%>
                        <%--</el-form-item>--%>
                        <%--<el-form-item class="input-inline" label="重点项目">--%>
                            <%--<el-select v-model="projBascInfoShow.projLevel">--%>
                                <%--<el-option v-for="item in projLevelList" :key="item.id" :label="item.itemName" :value="item.itemCode">--%>
                                <%--</el-option>--%>
                            <%--</el-select>--%>
                        <%--</el-form-item>--%>
                    <%--</div>--%>
                    <%--<el-form-item label="项目地址" class="proj-addr">--%>
                        <%--<div class="proj-addr-item">--%>
                            <%--<el-input v-model="projBascInfoShow.garden" placeholder="请输入区（园区）"></el-input>--%>
                            <%--<span class="input-label">区（园区）</span>--%>
                        <%--</div>--%>
                        <%--<div class="proj-addr-item">--%>
                            <%--<el-input v-model="projBascInfoShow.street" placeholder="请输入街道"></el-input>--%>
                            <%--<span class="input-label">街道</span>--%>
                        <%--</div>--%>
                        <%--<el-input class="proj-addr-item" v-model="projBascInfoShow.projAddr" placeholder="请输入项目详细地址"></el-input>--%>
                    <%--</el-form-item>--%>
                    <%--<el-form-item label="四至范围" class="proj-addr-scope">--%>
                        <%--<div class="proj-addr-item">--%>
                            <%--<span class="input-label">东至</span>--%>
                            <%--<el-input v-model="projBascInfoShow.eastScope" placeholder="请输入东至范围"></el-input>--%>
                        <%--</div>--%>
                        <%--<div class="proj-addr-item">--%>
                            <%--<span class="input-label">西至</span>--%>
                            <%--<el-input v-model="projBascInfoShow.westScope" placeholder="请输入西至范围"></el-input>--%>
                        <%--</div>--%>
                        <%--<div class="proj-addr-item">--%>
                            <%--<span class="input-label">南至</span>--%>
                            <%--<el-input v-model="projBascInfoShow.southScope" placeholder="请输入南至范围"></el-input>--%>
                        <%--</div>--%>
                        <%--<div class="proj-addr-item">--%>
                            <%--<span class="input-label">北至</span>--%>
                            <%--<el-input v-model="projBascInfoShow.northScope" placeholder="请输入北至范围"></el-input>--%>
                        <%--</div>--%>
                    <%--</el-form-item>--%>
                    <%--<div class="clearfix">--%>
                        <%--<el-form-item class="input-inline" label="立项类型" prop="projType">--%>
                            <%--<el-radio-group v-model="projBascInfoShow.projType" :disabled="linkQuerySucc&&projBascInfoShow.projType!==null">--%>
                                <%--<el-radio v-for="item in projTypeList" :disabled="projSelect" :key="item.id" :label="item.itemCode"--%>
                                          <%--:value="item.itemCode">--%>
                                    <%--{{item.itemName}}--%>
                                <%--</el-radio>--%>
                            <%--</el-radio-group>--%>
                        <%--</el-form-item>--%>
                        <%--<el-form-item class="input-inline" label="立项部门">--%>

                            <%--<el-input v-model="projBascInfoShow.projectDept" placeholder="请输入立项部门"></el-input>--%>
                        <%--</el-form-item>--%>
                        <%--<el-form-item class="input-inline" label="项目类型">--%>
                            <%--<el-select v-model="projBascInfoShow.projApplyType" filterable placeholder="请选择项目类型"--%>
                                       <%--@click.native="getProjThemeIdList" :loading="loadingThemeIdList">--%>
                                <%--<el-option v-for="item in projThemeIdList" :key="item.themeId" :label="item.themeName"--%>
                                           <%--:value="item.themeName">--%>
                                <%--</el-option>--%>
                            <%--</el-select>--%>
                        <%--</el-form-item>--%>
                        <%--<el-form-item class="input-inline" label="投资类型">--%>
                            <%--<el-select v-model="projBascInfoShow.investType">--%>
                                <%--<el-option v-for="item in tzlxList" :key="item.id" :label="item.itemName" :value="item.itemCode">--%>
                                <%--</el-option>--%>
                            <%--</el-select>--%>
                        <%--</el-form-item>--%>
                        <%--<el-form-item class="input-inline" label="行业类别">--%>
                            <%--<el-select v-model="projBascInfoShow.theIndustry">--%>
                                <%--<el-option v-for="item in gbhyList" :key="item.id" :label="item.itemName" :value="item.itemCode">--%>
                                <%--</el-option>--%>
                            <%--</el-select>--%>
                        <%--</el-form-item>--%>
                        <%--<el-form-item label="资金来源" class="input-inline">--%>
                            <%--<el-radio-group v-model="projBascInfoShow.financialSource">--%>
                                <%--<el-radio v-for="item in zjlyList" :key="item.id" :label="item.itemName" :value="item.itemCode">--%>
                                    <%--{{item.itemName}}--%>
                                <%--</el-radio>--%>
                            <%--</el-radio-group>--%>
                        <%--</el-form-item>--%>
                        <%--<el-form-item class="input-inline" label="总投资（万元）" prop="investSum">--%>
                            <%--<el-input v-model="projBascInfoShow.investSum" placeholder="请输入总投资"></el-input>--%>
                        <%--</el-form-item>--%>
                        <%--<el-form-item class="input-inline" label="是否外资">--%>
                            <%--<el-radio-group v-model="projBascInfoShow.isForeign">--%>
                                <%--<el-radio label="0" value="0" key="0">否</el-radio>--%>
                                <%--<el-radio label="1" value="1" key="1">是</el-radio>--%>
                            <%--</el-radio-group>--%>
                        <%--</el-form-item>--%>
                        <%--<el-form-item label="土地来源" class="input-inline">--%>
                            <%--<el-select v-model="projBascInfoShow.landSource">--%>
                                <%--<el-option v-for="item in tdlyList" :key="item.id" :label="item.itemName" :value="item.itemCode">--%>
                                <%--</el-option>--%>
                            <%--</el-select>--%>
                        <%--</el-form-item>--%>
                        <%--<el-form-item class="input-inline" label="用地面积（m&sup2;）" prop="xmYdmj">--%>
                            <%--<el-input v-model="projBascInfoShow.xmYdmj" placeholder="请填写用地面积"></el-input>--%>
                        <%--</el-form-item>--%>

                        <%--<el-form-item label="新增用地面积(公顷)" class="input-inline" prop="xzydmj">--%>
                            <%--<el-input v-model="projBascInfoShow.xzydmj" placeholder="请填写新增用地面积"></el-input>--%>
                        <%--</el-form-item>--%>
                        <%--<el-form-item class="input-inline" label="建设性质">--%>
                            <%--<el-select v-model="projBascInfoShow.projNature">--%>
                                <%--<el-option v-for="item in jsxzList" :key="item.id" :label="item.itemName" :value="item.itemCode">--%>
                                <%--</el-option>--%>
                            <%--</el-select>--%>
                        <%--</el-form-item>--%>
                        <%--<el-form-item label="工程分类" class="input-inline">--%>
                            <%--<el-select v-model="projBascInfoShow.projCategory">--%>
                                <%--<el-option v-for="item in gcflList" :key="item.id" :label="item.itemName" :value="item.itemCode">--%>
                                <%--</el-option>--%>
                            <%--</el-select>--%>
                        <%--</el-form-item>--%>

                        <%--<el-form-item class="input-inline" label="总建筑面积（m&sup2;）" prop="foreignBuildingArea">--%>
                            <%--<el-input v-model="projBascInfoShow.foreignBuildingArea" placeholder="请填写建筑面积"></el-input>--%>
                        <%--</el-form-item>--%>
                        <%--<el-form-item class="input-inline" label="地上（m&sup2;）" prop="aboveGround">--%>
                            <%--<el-input v-model="projBascInfoShow.aboveGround" placeholder="请填写地上面积"></el-input>--%>
                        <%--</el-form-item>--%>
                        <%--<el-form-item class="input-inline" label="地下（m&sup2;）" prop="underGround">--%>
                            <%--<el-input v-model="projBascInfoShow.underGround" placeholder="请填写地下面积"></el-input>--%>
                        <%--</el-form-item>--%>
                    <%--</div>--%>

                    <%--<div class="clearfix" v-if="showMoreBaseInfo">--%>
                        <%--<el-form-item label="建设内容(包括必要性)" class="wt100">--%>
                            <%--<el-input v-model="projBascInfoShow.foreignRemark" type="textarea" placeholder="请填写建设内容"></el-input>--%>
                        <%--</el-form-item>--%>
                        <%--<div class="clearfix">--%>
                            <%--<el-form-item class="input-inline" label="项目开工时间">--%>
                                <%--<el-date-picker v-model="projBascInfoShow.nstartTime" type="date" placeholder="选择项目开工时间"--%>
                                                <%--format="yyyy 年 MM 月 dd 日" value-format="yyyy-MM-dd">--%>
                                <%--</el-date-picker>--%>
                            <%--</el-form-item>--%>
                            <%--<el-form-item class="input-inline" label="项目建成时间">--%>
                                <%--<el-date-picker type="date" placeholder="项目建成时间" format="yyyy 年 MM 月 dd 日" value-format="yyyy-MM-dd"--%>
                                                <%--v-model="projBascInfoShow.endTime">--%>
                                <%--</el-date-picker>--%>
                            <%--</el-form-item>--%>
                        <%--</div>--%>
                    <%--</div>--%>
                <%--</el-form>--%>
                <%--<p class="more-base-info" @click="showMoreBaseInfo=!showMoreBaseInfo">{{showMoreBaseInfo?'隐藏更多':'显示更多'}}<i--%>
                        <%--:class="showMoreBaseInfo?'el-icon-arrow-up':'el-icon-arrow-down'"></i></p>--%>
                <%--<el-button type="primary" icon="el-icon-wallet" class="fr" @click="saveOrUpdateProjFrom(true)">保存</el-button>--%>
            <%--</el-collapse-item>--%>
            <%--<!--  基本信息  end  -->--%>
            <%--<template v-if="showMoreProjInfo">--%>
                <%--<!--<template>-->--%>
                <%--<!--  申办主体信息  start  -->--%>
                <%--<el-collapse-item name="2" id="applyInfo">--%>
                    <%--<template slot="title">--%>
                        <%--<i class="header-icon el-icon-info"></i>申报主体信息--%>
                    <%--</template>--%>
                    <%--<div class="checked-opt">--%>
                        <%--<el-radio-group v-model="applySubjectType" @change="changeApplySubjectSelect">--%>
                            <%--<el-radio-button :label="1" name="applySubject">&nbsp;企业&nbsp;</el-radio-button>--%>
                            <%--<!--                <el-tooltip class="item" effect="dark" content="机关或事业单位" placement="top-start">-->--%>
                            <%--<el-radio-button :label="2" name="applySubject">机关 / 事业单位</el-radio-button>--%>
                            <%--<!--                </el-tooltip>-->--%>
                            <%--<el-radio-button :label="0" name="applySubject">&nbsp;个人&nbsp;</el-radio-button>--%>
                        <%--</el-radio-group>--%>
                        <%--<el-switch class="is-jinban fr" v-show="applySubjectType!=0" v-model="agentChecked" inactive-text="是否经办">--%>
                        <%--</el-switch>--%>
                    <%--</div>--%>
                    <%--<!--个人申报主体 start-->--%>
                    <%--<el-form v-if="applySubjectType==0" ref="applyPerson" id="applyPerson" :model="applyPersonFrom">--%>
                        <%--<el-form label-width="176px" class="clearfix" :model="applyPersonFrom" :rules="rulesApplyPersonForm"--%>
                                 <%--ref="applicantPer">--%>
                            <%--<input type="hidden" v-model="applyPersonFrom.applyLinkmanId">--%>
                            <%--<input type="hidden" v-model="applyPersonFrom.linkLinkmanId">--%>
                            <%--<p class="apply-info-title">申请人信息</p>--%>
                            <%--<!--<el-col :span="24">--%>
                              <%--<el-form-item class="info-header" label="申请人信息">--%>
                              <%--</el-form-item>--%>
                            <%--</el-col>-->--%>
                            <%--<el-col :span="12">--%>
                                <%--<el-form-item label="申请人" prop="applyLinkmanName">--%>
                                    <%--<el-autocomplete :trigger-on-focus="false" :fetch-suggestions="querySearchLinkMan"--%>
                                                     <%--v-model="applyPersonFrom.applyLinkmanName" @select="querySearchApplyLinkMan"--%>
                                                     <%--placeholder="请输入申请人姓名">--%>
                                        <%--<el-button slot="append" @click.native="clearApplyLinkManInfo(true)" title="清空">清空</el-button>--%>
                                    <%--</el-autocomplete>--%>
                                <%--</el-form-item>--%>
                            <%--</el-col>--%>
                            <%--<el-col :span="12">--%>
                                <%--<el-form-item label="申请人身份证号" prop="applyLinkmanIdCard">--%>
                                    <%--<el-input v-model="applyPersonFrom.applyLinkmanIdCard" placeholder="请输入申请人身份证号">--%>
                                    <%--</el-input>--%>
                                <%--</el-form-item>--%>
                            <%--</el-col>--%>
                            <%--<el-col :span="12">--%>
                                <%--<el-form-item label="申请人电话" prop="applyLinkmanTel">--%>
                                    <%--<el-input v-model="applyPersonFrom.applyLinkmanTel" placeholder="请输入申请人电话">--%>
                                    <%--</el-input>--%>
                                <%--</el-form-item>--%>
                            <%--</el-col>--%>
                            <%--<el-col :span="12">--%>
                                <%--<el-form-item label="申请人邮箱">--%>
                                    <%--<el-input v-model="applyPersonFrom.applyLinkmanEmail" id="apply_linkmanEmail"--%>
                                              <%--placeholder="请输入申请人邮箱">--%>
                                    <%--</el-input>--%>
                                <%--</el-form-item>--%>
                            <%--</el-col>--%>
                            <%--<p class="apply-info-title">联系人信息--%>
                                <%--<el-checkbox class="fr" v-model="sameAsApplyLink" @change="copyApplyLinkmanInfo">与申请人信息一致 <span--%>
                                        <%--class="notice">（勾选后将自动获取联系人信息）</span></el-checkbox>--%>
                            <%--</p>--%>

                            <%--<el-col :span="12">--%>
                                <%--<el-form-item label="联系人" prop="linkLinkmanName">--%>
                                    <%--<el-autocomplete :trigger-on-focus="false" :disabled="sameAsApplyLink"--%>
                                                     <%--:fetch-suggestions="querySearchLinkMan" @select="querySearchLinkLinkMan"--%>
                                                     <%--v-model="applyPersonFrom.linkLinkmanName" placeholder="请输入联系人姓名">--%>
                                        <%--<el-button slot="append" @click.native="clearApplyLinkManInfo(false)" title="清空">清空</el-button>--%>
                                    <%--</el-autocomplete>--%>
                                <%--</el-form-item>--%>
                            <%--</el-col>--%>
                            <%--<el-col :span="12">--%>
                                <%--<el-form-item label="联系人身份证号" prop="linkLinkmanIdCard">--%>
                                    <%--<el-input :disabled="sameAsApplyLink" v-model="applyPersonFrom.linkLinkmanIdCard"--%>
                                              <%--placeholder="请输入联系人身份证号">--%>
                                    <%--</el-input>--%>
                                <%--</el-form-item>--%>
                            <%--</el-col>--%>
                            <%--<el-col :span="12">--%>
                                <%--<el-form-item label="联系人电话" prop="linkLinkmanTel">--%>
                                    <%--<el-input :disabled="sameAsApplyLink" v-model="applyPersonFrom.linkLinkmanTel"--%>
                                              <%--placeholder="请输入联系人电话"></el-input>--%>
                                <%--</el-form-item>--%>
                            <%--</el-col>--%>
                            <%--<el-col :span="12">--%>
                                <%--<el-form-item label="联系人邮箱">--%>
                                    <%--<el-input :disabled="sameAsApplyLink" v-model="applyPersonFrom.linkLinkmanEmail"--%>
                                              <%--placeholder="请输入联系人邮箱">--%>
                                    <%--</el-input>--%>
                                <%--</el-form-item>--%>
                            <%--</el-col>--%>
                        <%--</el-form>--%>
                        <%--<el-col :span="24" class="ar">--%>
                            <%--<el-form-item>--%>
                                <%--<el-button type="primary" icon="el-icon-wallet" @click="saveApplyAndLinkmanInfo()">保存</el-button>--%>
                            <%--</el-form-item>--%>
                        <%--</el-col>--%>
                    <%--</el-form>--%>
                    <%--<!--个人申报主体 end-->--%>
                    <%--<!--企业申报主体 start-->--%>
                    <%--<el-form v-if="applySubjectType==1 || applySubjectType==2">--%>
                        <%--<el-form label-width="176px"--%>
                                 <%--class="clearfix"--%>
                                 <%--v-for="(jiansheFromItem,index) in jiansheFrom"--%>
                                 <%--:ref="'jianshe_'+index"--%>
                                 <%--:key="index"--%>
                                 <%--:rules="rulesCompanyForm"--%>
                                 <%--:model="jiansheFromItem">--%>
                            <%--<p v-show="index==0" class="apply-info-title">建设单位信息</p>--%>
                            <%--<!--v-show="agentChecked && (index==0)"-->--%>
                            <%--<input type="hidden" name="applyinstId" :value="jiansheFromItem.applyinstId">--%>
                            <%--<input type="hidden" name="unitInfoId" :value="jiansheFromItem.unitInfoId">--%>
                            <%--<input type="hidden" name="linkmanId" :value="jiansheFromItem.linkmanId">--%>
                            <%--<el-col :span="12" class="input-inline">--%>
                                <%--<el-form-item label="单位类型" prop="unitType">--%>
                                    <%--<el-select v-model="jiansheFromItem.unitType" placeholder="请选择">--%>
                                        <%--<el-option v-for="(item,indexOp) in xmdwlxList"--%>
                                                   <%--:key="indexOp"--%>
                                                   <%--:label="item.itemName"--%>
                                                   <%--:value="item.itemCode"></el-option>--%>
                                    <%--</el-select>--%>
                                <%--</el-form-item>--%>
                            <%--</el-col>--%>
                            <%--<el-col :span="12">--%>
                                <%--<el-form-item label="统一社会信用代码" prop="unifiedSocialCreditCode">--%>
                                    <%--<el-input v-model="jiansheFromItem.unifiedSocialCreditCode" placeholder="请输入统一社会信用代码"></el-input>--%>
                                <%--</el-form-item>--%>
                            <%--</el-col>--%>
                            <%--<el-col :span="12">--%>
                                <%--<el-form-item label="单位名称" prop="applicant">--%>
                                    <%--<el-autocomplete :trigger-on-focus="false" :fetch-suggestions="querySearchJiansheName"--%>
                                                     <%--v-model="jiansheFromItem.applicant" placeholder="请输入单位名称">--%>
                                    <%--</el-autocomplete>--%>
                                <%--</el-form-item>--%>
                            <%--</el-col>--%>
                            <%--<el-col :span="12">--%>
                                <%--<el-form-item label="单位地址" prop="applicantDetailSite">--%>
                                    <%--<el-input v-model="jiansheFromItem.applicantDetailSite" placeholder="请输入法定代表人"></el-input>--%>
                                <%--</el-form-item>--%>
                            <%--</el-col>--%>
                            <%--<el-col :span="12">--%>
                                <%--<el-form-item label="法定代表人" prop="idrepresentative">--%>
                                    <%--<el-input v-model="jiansheFromItem.idrepresentative" placeholder="请输入法定代表人"></el-input>--%>
                                <%--</el-form-item>--%>
                            <%--</el-col>--%>
                            <%--<el-col :span="12">--%>
                                <%--<el-form-item label="法定代表人证件号">--%>
                                    <%--<el-input v-model="jiansheFromItem.idno" placeholder="请输入法定代表人证件号">--%>
                                    <%--</el-input>--%>
                                <%--</el-form-item>--%>
                            <%--</el-col>--%>
                            <%--<el-col :span="12">--%>
                                <%--<el-form-item label="联系人名称" prop="linkmanName" class="sel-btn">--%>
                                    <%--<el-select v-model="jiansheFromItem.linkmanName" remote :loading="loadingLinkMan"--%>
                                               <%--@click.native="getLinkManListByUnitId(jiansheFromItem)" required placeholder="--请选择--">--%>
                                        <%--<el-option class="select-option-ul"--%>
                                                   <%--v-for="(item2, index2) in jiansheFromItem.aeaLinkmanInfoList"--%>
                                                   <%--:key="index2"--%>
                                                   <%--:label="item2.addressName"--%>
                                                   <%--@click.native="selLinkman(item2,index,true)"--%>
                                                   <%--:value="item2.addressId">--%>
                                            <%--<span class="fl">{{ item2.addressName }}</span>--%>
                                            <%--<span class="fr el-icon-delete" @click.stop="delLinkman(item2,jiansheFromItem,index)"></span>--%>
                                        <%--</el-option>--%>
                                    <%--</el-select>--%>
                                    <%--<el-button type="primary" class="add-link-man" icon="el-icon-circle-plus-outline"--%>
                                               <%--@click="addLinkman('',jiansheFromItem);jingbanFlag=false" title="新增"></el-button>--%>
                                    <%--<el-button icon="el-icon-edit-outline" type="primary" class="item-right-btn"--%>
                                               <%--@click="addLinkman(jiansheFromItem,jiansheFromItem);jingbanFlag=false" title="编辑"></el-button>--%>
                                <%--</el-form-item>--%>
                            <%--</el-col>--%>
                            <%--<el-col :span="12">--%>
                                <%--<el-form-item label="联系人证件号" prop="linkmanCertNo">--%>
                                    <%--<el-input v-model="jiansheFromItem.linkmanCertNo" disabled placeholder="请输入联系人证件号">--%>
                                    <%--</el-input>--%>
                                <%--</el-form-item>--%>
                            <%--</el-col>--%>
                            <%--<el-col :span="12">--%>
                                <%--<el-form-item label="联系人手机" prop="linkmanMobilePhone">--%>
                                    <%--<el-input v-model="jiansheFromItem.linkmanMobilePhone" disabled placeholder="请输入联系人手机号"></el-input>--%>
                                <%--</el-form-item>--%>
                            <%--</el-col>--%>
                            <%--<el-col :span="12">--%>
                                <%--<el-form-item label="联系人邮箱">--%>
                                    <%--<el-input :id="'linkmanMail_'+index" v-model="jiansheFromItem.linkmanMail" disabled--%>
                                              <%--placeholder="请输入联系人邮箱"></el-input>--%>
                                <%--</el-form-item>--%>
                            <%--</el-col>--%>
                            <%--<template v-if="showUnitMore.indexOf(index)>-1">--%>
                                <%--<el-col :span="12">--%>
                                    <%--<el-form-item label="工商登记号" prop="induCommRegNum">--%>
                                        <%--<el-input v-model="jiansheFromItem.induCommRegNum" placeholder="请输入工商登记号"></el-input>--%>
                                    <%--</el-form-item>--%>
                                <%--</el-col>--%>
                                <%--<el-col :span="12">--%>
                                    <%--<el-form-item label="组织机构代码" prop="organizationalCode">--%>
                                        <%--<el-input v-model="jiansheFromItem.organizationalCode" placeholder="请输入组织机构代码"></el-input>--%>
                                    <%--</el-form-item>--%>
                                <%--</el-col>--%>
                                <%--<el-col :span="12">--%>
                                    <%--<el-form-item label="行政区（园区）" prop="applicantDistrict">--%>
                                        <%--<el-input v-model="jiansheFromItem.applicantDistrict" placeholder="请输入行政区（园区）"></el-input>--%>
                                    <%--</el-form-item>--%>
                                <%--</el-col>--%>
                                <%--<el-col :span="12">--%>
                                    <%--<el-form-item label="经营范围" prop="managementScope">--%>
                                        <%--<el-input v-model="jiansheFromItem.managementScope" placeholder="请输入经营范围"></el-input>--%>
                                    <%--</el-form-item>--%>
                                <%--</el-col>--%>
                                <%--<el-col :span="12">--%>
                                    <%--<el-form-item label="注册资本" prop="registerCapital">--%>
                                        <%--<el-input v-model="jiansheFromItem.registerCapital" placeholder="请输入注册资本"></el-input>--%>
                                    <%--</el-form-item>--%>
                                <%--</el-col>--%>
                                <%--<el-col :span="12">--%>
                                    <%--<el-form-item label="注册登记机关" prop="registerAuthority">--%>
                                        <%--<el-input v-model="jiansheFromItem.registerAuthority" placeholder="请输入注册登记机关"></el-input>--%>
                                    <%--</el-form-item>--%>
                                <%--</el-col>--%>
                                <%--<el-col :span="12">--%>
                                    <%--<el-form-item label="邮政编码" prop="postalCode">--%>
                                        <%--<el-input v-model="jiansheFromItem.postalCode" placeholder="请输入邮政编码"></el-input>--%>
                                    <%--</el-form-item>--%>
                                <%--</el-col>--%>
                                <%--<el-col :span="12">--%>
                                    <%--<el-form-item label="纳税人识别号" prop="taxpayerNum">--%>
                                        <%--<el-input v-model="jiansheFromItem.taxpayerNum" placeholder="请输入纳税人识别号"></el-input>--%>
                                    <%--</el-form-item>--%>
                                <%--</el-col>--%>
                                <%--<el-col :span="12">--%>
                                    <%--<el-form-item label="信用标记码" prop="creditFlagNum">--%>
                                        <%--<el-input v-model="jiansheFromItem.creditFlagNum" placeholder="请输入信用标记码"></el-input>--%>
                                    <%--</el-form-item>--%>
                                <%--</el-col>--%>
                            <%--</template>--%>
                            <%--<el-col :span="24" class="ar">--%>
                                <%--<p class="more-base-info" @click="setShowUnitMore(index)">{{(showUnitMore.indexOf(index)>-1)?'隐藏更多':'显示更多'}}--%>
                                    <%--<i :class="showMoreBaseInfo?'el-icon-arrow-up':'el-icon-arrow-down'"></i></p>--%>
                                <%--<el-form-item>--%>
                                    <%--<el-button type="primary" class="save-jansheUnit-info" icon="el-icon-wallet"--%>
                                               <%--@click="saveProjinfoids(jiansheFromItem,index,'jianshe_')">保存</el-button>--%>
                                    <%--<el-button type="warning" icon="el-icon-delete"--%>
                                               <%--@click="delUnitInfoProj(jiansheFromItem,index,'jianshe')">删除</el-button>--%>
                                <%--</el-form-item>--%>
                            <%--</el-col>--%>
                        <%--</el-form>--%>
                        <%--<el-col :span="24" class="ar">--%>
                            <%--<el-form-item>--%>
                                <%--<el-button type="primary" size="small" plain @click="addUnitInfoForm(1)">+ 新增建设单位</el-button>--%>
                            <%--</el-form-item>--%>
                        <%--</el-col>--%>
                    <%--</el-form>--%>
                    <%--<!--企业申报主体 end-->--%>
                    <%--<!--经办单位信息 start-->--%>
                    <%--<el-form v-if="applySubjectType!=0&&agentChecked">--%>
                        <%--<el-form label-width="176px"--%>
                                 <%--class="clearfix"--%>
                                 <%--:ref="'jingban_'+index"--%>
                                 <%--:rules="rulesCompanyForm"--%>
                                 <%--:key="index"--%>
                                 <%--v-for="(jingbanUnitsItem,index) in agentUnits"--%>
                                 <%--:model="jingbanUnitsItem">--%>
                            <%--<p v-show="index==0" class="apply-info-title">经办单位信息</p>--%>
                            <%--<input type="hidden" name="linkmanId" :value="jingbanUnitsItem.linkmanId">--%>
                            <%--<input type="hidden" name="unitInfoId" :value="jingbanUnitsItem.unitInfoId">--%>
                            <%--<el-col :span="12" class="input-inline">--%>
                                <%--<el-form-item label="单位类型" prop="unitType">--%>
                                    <%--<el-select v-model="jingbanUnitsItem.unitType" placeholder="请选择">--%>
                                        <%--<el-option v-for="(item,indexOp) in xmdwlxList"--%>
                                                   <%--:key="indexOp"--%>
                                                   <%--:label="item.itemName"--%>
                                                   <%--:value="item.itemCode"></el-option>--%>
                                    <%--</el-select>--%>
                                <%--</el-form-item>--%>
                            <%--</el-col>--%>
                            <%--<el-col :span="12">--%>
                                <%--<el-form-item label="统一社会信用代码" prop="unifiedSocialCreditCode">--%>
                                    <%--<el-input v-model="jingbanUnitsItem.unifiedSocialCreditCode" placeholder="请输入统一社会信用代码"></el-input>--%>
                                <%--</el-form-item>--%>
                            <%--</el-col>--%>
                            <%--<el-col :span="12">--%>
                                <%--<el-form-item label="单位名称" prop="applicant">--%>
                                    <%--<el-autocomplete :trigger-on-focus="false" :fetch-suggestions="querySearchJiansheName"--%>
                                                     <%--v-model="jingbanUnitsItem.applicant" placeholder="请输入单位名称">--%>
                                    <%--</el-autocomplete>--%>
                                <%--</el-form-item>--%>
                            <%--</el-col>--%>
                            <%--<el-col :span="12">--%>
                                <%--<el-form-item label="单位地址" prop="applicantDetailSite">--%>
                                    <%--<el-input v-model="jingbanUnitsItem.applicantDetailSite" placeholder="请输入法定代表人"></el-input>--%>
                                <%--</el-form-item>--%>
                            <%--</el-col>--%>
                            <%--<el-col :span="12">--%>
                                <%--<el-form-item label="法定代表人" prop="idrepresentative">--%>
                                    <%--<el-input v-model="jingbanUnitsItem.idrepresentative" placeholder="请输入法定代表人"></el-input>--%>
                                <%--</el-form-item>--%>
                            <%--</el-col>--%>
                            <%--<el-col :span="12">--%>
                                <%--<el-form-item label="法定代表人证件号">--%>
                                    <%--<el-input v-model="jingbanUnitsItem.idno" placeholder="请输入法定代表人证件号">--%>
                                    <%--</el-input>--%>
                                <%--</el-form-item>--%>
                            <%--</el-col>--%>
                            <%--<el-col :span="12">--%>
                                <%--<el-form-item label="联系人名称" prop="linkmanName" class="sel-btn">--%>
                                    <%--<el-select v-model="jingbanUnitsItem.linkmanName" remote reserve-keyword :loading="loadingLinkMan"--%>
                                               <%--@click.native="getLinkManListByUnitId(jingbanUnitsItem)" required placeholder="--请选择--">--%>
                                        <%--<el-option class="select-option-ul"--%>
                                                   <%--v-for="(item2, index2) in jingbanUnitsItem.aeaLinkmanInfoList"--%>
                                                   <%--:label="item2.linkmanName"--%>
                                                   <%--:key="index2"--%>
                                                   <%--@click.native="selLinkman(item2,index,false)"--%>
                                                   <%--:value="item2.linkmanName">--%>
                                            <%--<span class="fl">{{ item2.linkmanName }}</span>--%>
                                            <%--<span class="fr el-icon-delete" @click.stop="delLinkman(item2,jingbanUnitsItem,index)"></span>--%>
                                        <%--</el-option>--%>
                                    <%--</el-select>--%>
                                    <%--<el-button type="primary" class="add-link-man" icon="el-icon-circle-plus-outline"--%>
                                               <%--@click="addLinkman('',jingbanUnitsItem);jingbanFlag=true" title="新增"></el-button>--%>
                                    <%--<el-button icon="el-icon-edit-outline" type="primary" class="item-right-btn"--%>
                                               <%--@click="addLinkman(jingbanUnitsItem,jingbanUnitsItem);jingbanFlag=true" title="编辑"></el-button>--%>
                                <%--</el-form-item>--%>
                            <%--</el-col>--%>
                            <%--<el-col :span="12">--%>
                                <%--<el-form-item label="联系人证件号" prop="linkmanCertNo">--%>
                                    <%--<el-input v-model="jingbanUnitsItem.linkmanCertNo" disabled placeholder="请输入联系人证件号">--%>
                                    <%--</el-input>--%>
                                <%--</el-form-item>--%>
                            <%--</el-col>--%>
                            <%--<el-col :span="12">--%>
                                <%--<el-form-item label="联系人手机" prop="linkmanMobilePhone">--%>
                                    <%--<el-input v-model="jingbanUnitsItem.linkmanMobilePhone" disabled placeholder="请输入联系人手机"></el-input>--%>
                                <%--</el-form-item>--%>
                            <%--</el-col>--%>
                            <%--<el-col :span="12">--%>
                                <%--<el-form-item label="联系人邮箱">--%>
                                    <%--<el-input v-model="jingbanUnitsItem.linkmanMail" disabled placeholder="请输入联系人邮箱"></el-input>--%>
                                <%--</el-form-item>--%>
                            <%--</el-col>--%>
                            <%--<template v-if="showUnitMore.indexOf(index)>-1">--%>
                                <%--<el-col :span="12">--%>
                                    <%--<el-form-item label="工商登记号" prop="induCommRegNum">--%>
                                        <%--<el-input v-model="jingbanUnitsItem.induCommRegNum" placeholder="请输入工商登记号"></el-input>--%>
                                    <%--</el-form-item>--%>
                                <%--</el-col>--%>
                                <%--<el-col :span="12">--%>
                                    <%--<el-form-item label="组织机构代码" prop="organizationalCode">--%>
                                        <%--<el-input v-model="jingbanUnitsItem.organizationalCode" placeholder="请输入组织机构代码"></el-input>--%>
                                    <%--</el-form-item>--%>
                                <%--</el-col>--%>
                                <%--<el-col :span="12">--%>
                                    <%--<el-form-item label="行政区（园区）" prop="applicantDistrict">--%>
                                        <%--<el-input v-model="jingbanUnitsItem.applicantDistrict" placeholder="请输入行政区（园区）"></el-input>--%>
                                    <%--</el-form-item>--%>
                                <%--</el-col>--%>
                                <%--<el-col :span="12">--%>
                                    <%--<el-form-item label="经营范围" prop="managementScope">--%>
                                        <%--<el-input v-model="jingbanUnitsItem.managementScope" placeholder="请输入经营范围"></el-input>--%>
                                    <%--</el-form-item>--%>
                                <%--</el-col>--%>
                                <%--<el-col :span="12">--%>
                                    <%--<el-form-item label="注册资本" prop="registerCapital">--%>
                                        <%--<el-input v-model="jingbanUnitsItem.registerCapital" placeholder="请输入注册资本"></el-input>--%>
                                    <%--</el-form-item>--%>
                                <%--</el-col>--%>
                                <%--<el-col :span="12">--%>
                                    <%--<el-form-item label="注册登记机关" prop="registerAuthority">--%>
                                        <%--<el-input v-model="jingbanUnitsItem.registerAuthority" placeholder="请输入注册登记机关"></el-input>--%>
                                    <%--</el-form-item>--%>
                                <%--</el-col>--%>
                                <%--<el-col :span="12">--%>
                                    <%--<el-form-item label="邮政编码" prop="postalCode">--%>
                                        <%--<el-input v-model="jingbanUnitsItem.postalCode" placeholder="请输入邮政编码"></el-input>--%>
                                    <%--</el-form-item>--%>
                                <%--</el-col>--%>
                                <%--<el-col :span="12">--%>
                                    <%--<el-form-item label="纳税人识别号" prop="taxpayerNum">--%>
                                        <%--<el-input v-model="jingbanUnitsItem.taxpayerNum" placeholder="请输入纳税人识别号"></el-input>--%>
                                    <%--</el-form-item>--%>
                                <%--</el-col>--%>
                                <%--<el-col :span="12">--%>
                                    <%--<el-form-item label="信用标记码" prop="creditFlagNum">--%>
                                        <%--<el-input v-model="jingbanUnitsItem.creditFlagNum" placeholder="请输入信用标记码"></el-input>--%>
                                    <%--</el-form-item>--%>
                                <%--</el-col>--%>
                            <%--</template>--%>
                            <%--<el-col :span="24" class="ar">--%>
                                <%--<p class="more-base-info" @click="setShowUnitMore(index)">{{(showUnitMore.indexOf(index)>-1)?'隐藏更多':'显示更多'}}--%>
                                    <%--<i :class="showMoreBaseInfo?'el-icon-arrow-up':'el-icon-arrow-down'"></i></p>--%>
                                <%--<el-form-item>--%>
                                    <%--<el-button type="primary" class="save-jinbanUnit-info" icon="el-icon-wallet"--%>
                                               <%--@click="saveProjinfoids(jingbanUnitsItem,index,'jingban_')">保存</el-button>--%>
                                    <%--<el-button type="warning" icon="el-icon-delete"--%>
                                               <%--@click="delUnitInfoProj(jingbanUnitsItem,index,'jingban')">删除</el-button>--%>
                                <%--</el-form-item>--%>
                            <%--</el-col>--%>
                        <%--</el-form>--%>
                        <%--<el-col :span="24" class="ar">--%>
                            <%--<el-form-item>--%>
                                <%--<el-button type="primary" size="small" plain @click="addUnitInfoForm(0)">+ 新增经办单位</el-button>--%>
                            <%--</el-form-item>--%>
                        <%--</el-col>--%>
                    <%--</el-form>--%>

                    <%--<!--经办单位信息 end-->--%>
                <%--</el-collapse-item>--%>
                <%--<!--  申办主体信息  end  -->--%>
                <%--<!--事项一单清 start-->--%>
                <%--<el-collapse-item name="3" id="applyStage">--%>
                    <%--<template slot="title">--%>
                        <%--<i class="header-icon el-icon-info"></i>事项一单清--%>
                    <%--</template>--%>
                    <%--<!--申报阶段 start-->--%>
                    <%--<!--主题类型 start-->--%>
                    <%--<div class="col-status-z" :class="(colStatusActive==0)?'active':''" @click="setColStatusActive(0)">--%>
                        <%--<p class="col-s-title content-center">选主题</p>--%>
                        <%--<div class="theme-type-list" v-show="themeList.length>1">--%>
                <%--<span class="theme-type" style="width: 180px;" v-for="(item, index) in themeList"--%>
                      <%--:class="(themeTypeIndex==index)?'active':''" @click="changeThemeType(item,index,'single')">--%>
                  <%--{{item.themeTypeName}}--%>
                <%--</span>--%>
                        <%--</div>--%>
                        <%--<div class="apply-theme clearfix" v-if="themeTypeIndex==0">--%>
                            <%--<div class="theme-list-item" v-if="selTheme.themeId">--%>
                                <%--<img class="text-left" src="../../apply/imgs/pro_type_4.png">--%>
                                <%--<span class="theme-name ellipsis">{{selTheme.themeName}}</span>--%>
                            <%--</div>--%>
                            <%--<div class="theme-list-item no-sel-theme" v-else>--%>
                                <%--<span @click="showSelThemeDialog"> <i class="el-icon-circle-plus-outline"></i>请选择并联审批主题类型</span>--%>
                            <%--</div>--%>
                            <%--<el-button style="margin-left: 10px;border-color: #169aff;color: #169aff;" circle icon="el-icon-refresh"--%>
                                       <%--v-show="selTheme.themeId" @click="showSelThemeDialog"></el-button>--%>
                            <%--<a :href="ctx+'rest/project/diagram/status?projInfoId='+projInfoId+'&&themeId='+themeId"--%>
                               <%--target="_blank">--%>
                                <%--<el-button style="margin-left: 10px;border-color: #169aff;color: #169aff;" circle icon="el-icon-view">--%>
                                <%--</el-button>--%>
                            <%--</a>--%>

                        <%--</div>--%>
                        <%--<div class="help-line-theme clearfix" v-else>--%>
                            <%--<div class="theme-list-item" :class="selThemeActive1==index?'active':''"--%>
                                 <%--@click="chooseTheme(item,index,'single');" v-for="(item,index) in themeInfoList">--%>
                                <%--<img class="text-left" :src="ctx+'apply/imgs/pro_type_4.png'">--%>
                                <%--<span class="theme-name ellipsisLn">{{item.themeName}}</span>--%>
                            <%--</div>--%>
                        <%--</div>--%>
                    <%--</div>--%>
                    <%--<!--主题类型 end-->--%>
                    <%--<!--申报阶段或辅线服务 start-->--%>
                    <%--<div class="col-status-z" :class="(colStatusActive==1)?'active':''" @click="setColStatusActive(1)"--%>
                         <%--v-show="statusLineList.length>1||statusList.length>0">--%>
                        <%--<p class="col-s-title content-center">{{((statusLineList.length>1)&&(statusList.length>0))?'选阶段或服务':'选阶段'}}--%>
                        <%--</p>--%>
                        <%--<div class="line-type" v-show="statusLineList.length>1">--%>
                            <%--<ul>--%>
                                <%--<li class="fl" v-for="(item,ind) in statusLineList" @click="selStatusLine(item,ind)"--%>
                                    <%--:class="statusLineListActive==ind?'active':''">--%>
                                    <%--{{item.title}}--%>
                                <%--</li>--%>
                            <%--</ul>--%>
                        <%--</div>--%>
                        <%--<div class="status-img clearfix">--%>
                            <%--<div class="status-img-item" v-for="(item,index) in statusList"--%>
                                 <%--:class="statusActiveIndex==index?'active':''"--%>
                                 <%--@click="selStatus(item,index,item.stageId,item.isSelItem)">--%>
                                <%--<div class="stage-img" :class="item.classIcon">--%>
                                    <%--<img :src="ctx+item.bigImgPath" alt="">--%>
                                    <%--<span v-if="item.helperStages.length>0"--%>
                                          <%--class="helper-stages">{{item.helperStages[0].stageName}}</span>--%>
                                <%--</div>--%>
                                <%--<p class="stage-name">{{item.stageName}}</p>--%>
                                <%--<!--                  <span class="applied-stage" v-if="item.appliedStage"-->--%>
                                <%--<!--                    :class="item.appliedStage.businessStatusName=='通过'?'bg-green':item.appliedStage.businessStatusName=='申报失败'?'bg-gray':''">{{item.appliedStage.businessStatusName}}</span>-->--%>
                            <%--</div>--%>
                        <%--</div>--%>
                    <%--</div>--%>
                    <%--<!--申报阶段或辅线服务 end-->--%>
                    <%--<!--办理情形 start-->--%>
                    <%--<div class="col-status-z" :class="(colStatusActive==2)?'active':''" @click="setColStatusActive(2)"--%>
                         <%--v-show="stateList.length>0">--%>
                        <%--<p class="col-s-title content-center">选情形</p>--%>
                        <%--<div class="sel-status" v-if="stateList.length>0">--%>
                            <%--<p class="m-font-gary">请选择情形：</p>--%>
                            <%--<div id="parstatediv">--%>
                                <%--<el-form :model="stateSelVal">--%>
                                    <%--<el-form-item class="sel-status-item" :required="item.mustAnswer==1"--%>
                                                  <%--v-for="(item,index) in stateList" :key="index" :label="item.stateName">--%>
                                        <%--<p class="status-label">--%>
                                            <%--<el-tooltip class="item" effect="light" :content="item.stateMemo?item.stateMemo:'无'"--%>
                                                        <%--placement="right">--%>
                                                <%--<i class="el-icon-question"></i>--%>
                                            <%--</el-tooltip>--%>
                                        <%--</p>--%>
                                        <%--<template v-if="item.answerType=='s'||item.answerType=='y'">--%>
                                            <%--<el-radio-group v-model="item.selectAnswerId">--%>
                                                <%--<el-radio v-for="(itemAns,ind) in item.answerStates"--%>
                                                          <%--:key="ind"--%>
                                                          <%--@change="getStatusStateMats(item,itemAns,itemAns.stageId,index,false)"--%>
                                                          <%--:value="itemAns.parStateId" :label="itemAns.parStateId" name="answer">{{itemAns.stateName}}--%>
                                                <%--</el-radio>--%>
                                            <%--</el-radio-group>--%>
                                        <%--</template>--%>
                                        <%--<template v-else>--%>
                                            <%--<el-checkbox-group v-model="item.selectAnswerId" :required="item.mustAnswer==1">--%>
                                                <%--<el-checkbox v-for="(itemAns,indAns) in item.answerStates" :key="indAns" :value="itemAns.parStateId"--%>
                                                             <%--@change="function(value) {getStatusStateMats(item,itemAns,itemAns.stageId,index,false,value)}"--%>
                                                             <%--:label="itemAns.parStateId" name="answer">{{itemAns.stateName}}</el-checkbox>--%>
                                            <%--</el-checkbox-group>--%>
                                        <%--</template>--%>
                                    <%--</el-form-item>--%>
                                <%--</el-form>--%>
                            <%--</div>--%>
                        <%--</div>--%>
                    <%--</div>--%>
                    <%--<!--办理情形  end-->--%>
                    <%--<!--事项列表 start-->--%>
                    <%--<div class="col-status-z" :class="(colStatusActive==3)?'active':''" @click="setColStatusActive(3)"--%>
                         <%--v-show="parallelItems.length>0||coreItems.length>0">--%>
                        <%--<p class="col-s-title content-center">选要办理的事项清单</p>--%>
                        <%--<div v-show="parallelItems.length>0">--%>
                            <%--<div class="pro-core">--%>
                                <%--<!--                  <img :src="ctx+'apply/imgs/pro-core-icon-y.png'">-->--%>
                                <%--&nbsp;并联审批事项列表--%>
                            <%--</div>--%>
                            <%--<el-table ref="parallelItemsTable" :data="parallelItems" border :expand-row-keys="showParallelItemsKey"--%>
                                      <%--row-key="itemBasicId" tooltip-effect="dark" @select="parallelItemsSelItem"--%>
                                      <%--@select-all="parallelItemsSelAll" :default-sort="{prop: 'sortNo', order: 'ascending'}"--%>
                                      <%--style="width: 100%">--%>
                                <%--<el-table-column type="selection" :selectable='checkboxInit' v-show="isSelItem" align="center"--%>
                                                 <%--width="55">--%>
                                <%--</el-table-column>--%>
                                <%--<el-table-column type="expand" width="5px" v-if="parallelItemsQuestionFlag">--%>
                                    <%--<template slot-scope="scope">--%>
                                        <%--<el-form :model="stateSelVal" class="expand-coreItem">--%>
                                            <%--<el-form-item class="sel-status-item" :required="item.mustAnswer==1"--%>
                                                          <%--v-for="(item,index) in scope.row.questionStates" :key="index" :label="item.stateName">--%>
                                                <%--<p class="status-label">--%>
                                                    <%--<el-tooltip class="item" effect="light" :content="item.stateMemo?item.stateMemo:'无'"--%>
                                                                <%--placement="right">--%>
                                                        <%--<i class="el-icon-question"></i>--%>
                                                    <%--</el-tooltip>--%>
                                                <%--</p>--%>
                                                <%--<template v-if="item.answerType=='s'||item.answerType=='y'">--%>
                                                    <%--<el-radio-group v-model="item.selectAnswerId">--%>
                                                        <%--<el-radio v-for="(itemAns,ind) in item.answerStates"--%>
                                                                  <%--:key="ind"--%>
                                                                  <%--@change="getCoreItemsStateMats(scope.row.questionStates,item,itemAns,index,'parallelItem')"--%>
                                                                  <%--:value="itemAns.itemStateId" :label="itemAns.itemStateId" name="answer">--%>
                                                            <%--{{itemAns.stateName}}</el-radio>--%>
                                                    <%--</el-radio-group>--%>
                                                <%--</template>--%>
                                                <%--<template v-else>--%>
                                                    <%--<el-checkbox-group v-model="item.selectAnswerId" :required="item.mustAnswer==1">--%>
                                                        <%--<el-checkbox v-for="(itemAns,indAns) in item.answerStates" :key="indAns" :value="itemAns.itemStateId"--%>
                                                                     <%--@change="function(value){getCoreItemsStateMats(scope.row.questionStates,item,itemAns,index,'parallelItem',value)}"--%>
                                                                     <%--:label="itemAns.itemStateId" name="answer">{{itemAns.stateName}}</el-checkbox>--%>
                                                    <%--</el-checkbox-group>--%>
                                                <%--</template>--%>
                                            <%--</el-form-item>--%>
                                        <%--</el-form>--%>
                                    <%--</template>--%>
                                <%--</el-table-column>--%>
                                <%--<el-table-column label="事项名称" prop="sortNo" sortable>--%>
                                    <%--<template slot-scope="scope">--%>
                      <%--<span class="is-catalog"--%>
                            <%--:class="scope.row.isCatalog!=1?'is-shishi':''">{{scope.row.isCatalog==1?'标':'实'}}</span>--%>
                                        <%--{{scope.row.itemName}}--%>
                                        <%--<span v-show="scope.row.isDone=='FINISHED'" class="isTag isDone"><i--%>
                                                <%--class="el-icon-success"></i>已办</span>--%>
                                        <%--<span v-show="scope.row.isDone=='HANDLING'" class="isTag isDone">办理中</span>--%>
                                        <%--<span v-show="scope.row.isRecommend==1" class="isTag isRecommend">推荐</span>--%>
                                        <%--<p class="not-region-data" v-if="scope.row.itemHaved">* 该实施事项已存在，请勿重复选择!</p>--%>
                                        <%--<p class="not-region-data" v-if="scope.row.notRegionData">* 该事项无匹配的行政区划及承办单位</p>--%>
                                    <%--</template>--%>
                                <%--</el-table-column>--%>
                                <%--<el-table-column label="行政区划" width="220">--%>
                                    <%--<template slot-scope="scope">--%>
                                        <%--<div v-if="scope.row.isImplement==false">--%>
                                            <%--<el-select :disabled="scope.row.disabled" v-model="scope.row.implementItemVerId"--%>
                                                       <%--placeholder="请选择活动区域">--%>
                                                <%--<el-option v-for="(item,indexOp) in scope.row.carryOutItems"--%>
                                                           <%--:key="indexOp"--%>
                                                           <%--:label="item.regionName"--%>
                                                           <%--@click.native="getItemOrgId(scope.row,item)" :value="item.itemVerId"></el-option>--%>
                                            <%--</el-select>--%>
                                        <%--</div>--%>
                                        <%--<div v-else>--%>
                                            <%--<span>{{scope.row.regionName}}</span>--%>
                                        <%--</div>--%>
                                    <%--</template>--%>
                                <%--</el-table-column>--%>
                                <%--<el-table-column prop="orgName" label="承办单位" width="220">--%>
                                    <%--<template slot-scope="scope">--%>
                                        <%--<div v-if="scope.row.isImplement==false">--%>
                                            <%--<el-select :disabled="scope.row.disabled" v-model="scope.row.implementItemVerId"--%>
                                                       <%--placeholder="请选择活动区域">--%>
                                                <%--<el-option v-for="(item,indexOp) in scope.row.carryOutItems" :label="item.orgName"--%>
                                                           <%--:key="indexOp"--%>
                                                           <%--@click.native="getItemOrgId(scope.row,item)" :value="item.itemVerId"></el-option>--%>
                                            <%--</el-select>--%>
                                        <%--</div>--%>
                                        <%--<div v-else>--%>
                                            <%--<span>{{scope.row.orgName}}</span>--%>
                                        <%--</div>--%>
                                    <%--</template>--%>
                                <%--</el-table-column>--%>
                            <%--</el-table>--%>
                        <%--</div>--%>
                        <%--<div v-show="coreItems.length>0">--%>
                            <%--<div class="pro-core">--%>
                                <%--<!--                  <img :src="ctx+'apply/imgs/pro-core-icon-b.png'">-->--%>
                                <%--&nbsp;并行推进事项列表--%>
                            <%--</div>--%>
                            <%--<el-table ref="coreItemsTable" :data="coreItems" border tooltip-effect="dark"--%>
                                      <%--:default-sort="{prop: 'sortNo', order: 'ascending'}" :expand-row-keys="showCoreItemsKey"--%>
                                      <%--row-key="itemBasicId" @select="coreItemsSelItem" @select-all="coreItemsSelAll" style="width: 100%">--%>
                                <%--<el-table-column type="selection" :selectable='checkboxInit' align="center" width="55">--%>
                                <%--</el-table-column>--%>
                                <%--<el-table-column type="expand" width="5px" v-if="coreItemQuestionFlag">--%>
                                    <%--<template slot-scope="scope">--%>
                                        <%--<el-form :model="stateSelVal" class="expand-coreItem">--%>
                                            <%--<el-form-item class="sel-status-item" :required="item.mustAnswer==1"--%>
                                                          <%--v-for="(item,index) in scope.row.questionStates" :key="index" :label="item.stateName">--%>
                                                <%--<p class="status-label">--%>
                                                    <%--<el-tooltip class="item" effect="light" :content="item.stateMemo?item.stateMemo:'无'"--%>
                                                                <%--placement="right">--%>
                                                        <%--<i class="el-icon-question"></i>--%>
                                                    <%--</el-tooltip>--%>
                                                <%--</p>--%>
                                                <%--<template v-if="item.answerType=='s'||item.answerType=='y'">--%>
                                                    <%--<el-radio-group v-model="item.selectAnswerId">--%>
                                                        <%--<el-radio v-for="(itemAns,ind) in item.answerStates"--%>
                                                                  <%--:key="ind"--%>
                                                                  <%--@change="getCoreItemsStateMats(scope.row.questionStates,item,itemAns,index,'coreItem')"--%>
                                                                  <%--:value="itemAns.itemStateId" :label="itemAns.itemStateId" name="answer">--%>
                                                            <%--{{itemAns.stateName}}</el-radio>--%>
                                                    <%--</el-radio-group>--%>
                                                <%--</template>--%>
                                                <%--<template v-else>--%>
                                                    <%--<el-checkbox-group v-model="item.selectAnswerId" :required="item.mustAnswer==1">--%>
                                                        <%--<el-checkbox v-for="(itemAns,indAns) in item.answerStates" :key="indAns" :value="itemAns.itemStateId"--%>
                                                                     <%--@change="function(value){getCoreItemsStateMats(scope.row.questionStates,item,itemAns,index,'coreItem',value)}"--%>
                                                                     <%--:label="itemAns.itemStateId" name="answer">{{itemAns.stateName}}</el-checkbox>--%>
                                                    <%--</el-checkbox-group>--%>
                                                <%--</template>--%>
                                            <%--</el-form-item>--%>
                                        <%--</el-form>--%>
                                    <%--</template>--%>
                                <%--</el-table-column>--%>
                                <%--<el-table-column label="事项名称" prop="sortNo" sortable>--%>
                                    <%--<template slot-scope="scope">--%>
                      <%--<span class="is-catalog"--%>
                            <%--:class="scope.row.isCatalog!=1?'is-shishi':''">{{scope.row.isCatalog==1?'标':'实'}}</span>--%>
                                        <%--{{scope.row.itemName}}--%>
                                        <%--<span v-show="scope.row.isDone=='FINISHED'" class="isTag isDone"><i--%>
                                                <%--class="el-icon-success"></i>已办</span>--%>
                                        <%--<span v-show="scope.row.isDone=='HANDLING'" class="isTag isDone">办理中</span>--%>
                                        <%--<span v-show="scope.row.isRecommend==1" class="isTag isRecommend">推荐</span>--%>
                                        <%--<p class="not-region-data" v-if="scope.row.itemHaved">* 该实施事项已存在，请勿重复选择!</p>--%>
                                        <%--<p class="not-region-data" v-if="scope.row.notRegionData">* 该事项无匹配的行政区划及承办单位</p>--%>
                                    <%--</template>--%>
                                <%--</el-table-column>--%>
                                <%--<el-table-column label="行政区划" width="220">--%>
                                    <%--<template slot-scope="scope">--%>
                                        <%--<div v-if="scope.row.isImplement==false">--%>
                                            <%--<el-select :disabled="scope.row.disabled" v-model="scope.row.implementItemVerId"--%>
                                                       <%--placeholder="请选择活动区域">--%>
                                                <%--<el-option v-for="(item,indexOp) in scope.row.carryOutItems" :label="item.regionName"--%>
                                                           <%--:key="indexOp"--%>
                                                           <%--@click.native="getItemOrgId(scope.row,item)" :value="item.itemVerId"></el-option>--%>
                                            <%--</el-select>--%>
                                        <%--</div>--%>
                                        <%--<div v-else>--%>
                                            <%--<span>{{scope.row.regionName}}</span>--%>
                                        <%--</div>--%>
                                    <%--</template>--%>
                                <%--</el-table-column>--%>
                                <%--<el-table-column prop="orgName" label="承办单位" width="220">--%>
                                    <%--<template slot-scope="scope">--%>
                                        <%--<div v-if="scope.row.isImplement==false">--%>
                                            <%--<el-select :disabled="scope.row.disabled" v-model="scope.row.implementItemVerId"--%>
                                                       <%--placeholder="请选择活动区域">--%>
                                                <%--<el-option v-for="(item,indexOp) in scope.row.carryOutItems" :label="item.orgName"--%>
                                                           <%--:key="indexOp"--%>
                                                           <%--@click.native="getItemOrgId(scope.row,item)" :value="item.itemVerId"></el-option>--%>
                                            <%--</el-select>--%>
                                        <%--</div>--%>
                                        <%--<div v-else>--%>
                                            <%--<span>{{scope.row.orgName}}</span>--%>
                                        <%--</div>--%>
                                    <%--</template>--%>
                                <%--</el-table-column>--%>
                            <%--</el-table>--%>
                        <%--</div>--%>
                    <%--</div>--%>
                    <%--<!--事项列表 end-->--%>
                <%--</el-collapse-item>--%>
                <%--<!--事项一单清 end-->--%>
                <%--<!--一张表单 start-->--%>
                <%--<el-collapse-item name="4" v-if="oneFormData.length>0" id="oneForm" class="clearfix">--%>
                    <%--<template slot="title">--%>
                        <%--<i class="header-icon el-icon-edit-outline"></i>一张表单--%>
                    <%--</template>--%>
                    <%--<div class="fl" v-for="item in oneFormData" @click="showOneFormDialog">--%>
                        <%--<p class="one-form-name ellipsis">--%>
                            <%--<span class="icon el-icon-edit-outline"></span>--%>
                            <%--{{item.oneformName}}--%>
                            <%--<span v-show="item.filledOut==1" class="isTag isDone"><i class="el-icon-success"></i>已填</span>--%>
                        <%--</p>--%>
                    <%--</div>--%>
                <%--</el-collapse-item>--%>
                <%--<!--一张表单 end-->--%>
                <%--<!--材料一单清 start-->--%>
                <%--<el-collapse-item name="5" id="matsList">--%>
                    <%--<template slot="title">--%>
                        <%--<i class="header-icon el-icon-info"></i>材料一单清--%>
                    <%--</template>--%>
                    <%--<div class="fileDiv" v-loading="loadingFile" element-loading-text="文件上传中"--%>
                         <%--element-loading-spinner="el-icon-loading" element-loading-background="rgba(0, 0, 0, 0.3)"--%>
                         <%--:class="this.AllFileList.length>0?'is-sel-file':''">--%>
                        <%--<el-upload ref="uploadFile" class="upload-flie" :auto-upload="false" drag :show-file-list="false"--%>
                                   <%--:file-list="fileList" :action="ctx+'rest/mats/att/upload/auto'" multiple :http-request="myUploadFile"--%>
                                   <%--:on-change="fileChange">--%>
                            <%--<i slot="trigger" v-show="this.AllFileList.length>0" class="el-icon-plus"></i>--%>
                            <%--<el-button class="submit-file" v-show="this.AllFileList.length>0" size="small" type="primary"--%>
                                       <%--@click="submitUpload">一键自动分拣</el-button>--%>
                            <%--<div class="el-upload__text" v-show="this.AllFileList.length==0">或将文件直接拖入这里，支持上传后自动分拣</div>--%>
                            <%--<el-button v-show="this.AllFileList.length==0" slot="trigger" size="small" type="primary">选择文件--%>
                            <%--</el-button>--%>
                        <%--</el-upload>--%>
                        <%--<div class="flie-list-che file-list-ul" v-show="this.AllFileList.length>0">--%>
                            <%--<el-checkbox-group v-model="checkedSelFlie" class="check-item" @change="handleCheckedCitiesChange">--%>
                                <%--<el-checkbox v-for="(item,index) in AllFileList" :label="item.name" :key="item.uid">--%>
                                    <%--<span><i class="el-icon-document"></i>{{item.name}}</span>--%>
                                <%--</el-checkbox>--%>
                            <%--</el-checkbox-group>--%>
                            <%--<el-checkbox class="check-all fr" v-model="checkAll" @change="handleCheckAllChange">全选</el-checkbox>--%>
                        <%--</div>--%>
                    <%--</div>--%>
                    <%--<el-form :rules="model.rules" :model="model" ref="formTest">--%>
                        <%--<el-table :data="model.matsTableData" row-key="matId" class="mats-table"--%>
                                  <%--:expand-row-keys="showFileListKey" style="width: 100%">--%>
                            <%--<el-table-column align="center" label="序号" width="55">--%>
                                <%--<template slot-scope="scope">--%>
                                    <%--<span>{{scope.$index+1}}</span>--%>
                                    <%--<!--                  <span>{{scope.row.sortNo}}</span>-->--%>
                                <%--</template>--%>
                            <%--</el-table-column>--%>
                            <%--<el-table-column type="expand" v-if="showMatTableExpand" width="20" align="right">--%>
                                <%--<template slot-scope="scope">--%>
                                    <%--<!--                    <span>{{scope.row}}</span>-->--%>
                                    <%--<div class="mat-children" v-if="showMatTableExpand&&scope.row.children.length>0">--%>
                                        <%--<div v-for="item in scope.row.children" class="file-list-tab">--%>
                        <%--<span class="file-name">--%>
                          <%--<span class="icon ag" :class="'ag-filetype-'+getFileType(item.fileName)"></span>--%>
                          <%--{{item.fileName}}--%>
                        <%--</span>--%>
                                            <%--<span class="file-icon-btn">--%>
                          <%--<span title="预览" class="el-icon-search" @click="filePreview(item)"></span>--%>
                          <%--<span title="下载" class="el-icon-download" @click="downOneFile(item)"></span>--%>
                          <%--<span title="删除" class="el-icon-delete" @click="delOneFile(item,scope.row)"></span>--%>
                        <%--</span>--%>
                                        <%--</div>--%>
                                    <%--</div>--%>
                                    <%--<div class="mat-children" v-else>--%>
                                        <%--<div class="file-list-tab">无已上传关联材料</div>--%>
                                    <%--</div>--%>
                                <%--</template>--%>
                            <%--</el-table-column>--%>

                            <%--<el-table-column prop="matName" label="材料名称" min-width="220">--%>
                            <%--</el-table-column>--%>
                            <%--<el-table-column label="收件要点" align="center" width="80">--%>
                                <%--<template slot-scope="scope">--%>
                                    <%--<el-tooltip class="item custor" effect="light" :content="scope.row.reviewKeyPoints"--%>
                                                <%--placement="right">--%>
                                        <%--<span class="el-icon el-icon-search"></span>--%>
                                    <%--</el-tooltip>--%>
                                <%--</template>--%>
                            <%--</el-table-column>--%>
                            <%--<el-table-column align="center" label="要点示例" width="80">--%>
                                <%--<template slot-scope="scope">--%>
                                    <%--<el-popover class="custor" placement="right" width="60" trigger="hover">--%>
                                        <%--<p class="notice-pop" @click="showMatFiles(scope.row.matId,'TEMPLATE_DOC')">空表</p>--%>
                                        <%--<p class="notice-pop" @click="showMatFiles(scope.row.matId,'SAMPLE_DOC')">样表</p>--%>
                                        <%--<p class="notice-pop" @click="showMatFiles(scope.row.matId,'REVIEW_SAMPLE_DOC')">收件样本</p>--%>
                                        <%--<i slot="reference" class="el-icon el-icon-more"></i>--%>
                                    <%--</el-popover>--%>
                                <%--</template>--%>
                            <%--</el-table-column>--%>
                            <%--<el-table-column align="center" label="是否容缺" width="80">--%>
                                <%--<template slot-scope="scope">--%>
                                    <%--<span :class="(scope.row.zcqy!='0')?'text-green':''">{{(scope.row.zcqy=='0')?'否':'是'}}</span>--%>
                                <%--</template>--%>
                            <%--</el-table-column>--%>
                            <%--<el-table-column label="纸质件" align="center">--%>
                                <%--<!--                <el-table-column-->--%>
                                <%--<!--                  label="必须"-->--%>
                                <%--<!--                  align="center"-->--%>
                                <%--<!--                  min-width="55">-->--%>
                                <%--<!--                  <template slot-scope="scope">-->--%>
                                <%--<!--                    <span>{{(scope.row.paperIsRequire==1)?'是':'否'}}</span>-->--%>
                                <%--<!--                  </template>-->--%>
                                <%--<!--                </el-table-column>-->--%>
                                <%--<el-table-column label="原件" align="center">--%>
                                    <%--<el-table-column prop="duePaperCount" align="center" label="份数" width="60">--%>
                                    <%--</el-table-column>--%>
                                    <%--<el-table-column prop="realPaperCount" align="center" label="收/验" width="70">--%>
                                        <%--<template slot-scope="scope">--%>
                                            <%--<el-form-item :prop="'matsTableData.' + scope.$index + '.realPaperCount'"--%>
                                                          <%--:rules='(scope.row.zcqy==0&&scope.row.paperIsRequire==1)?model.rules.realPaperCount:model.rules.realPaperCount1'>--%>

                                                <%--<el-input size="mini" v-model="scope.row.realPaperCount"></el-input>--%>
                                            <%--</el-form-item>--%>
                                        <%--</template>--%>
                                    <%--</el-table-column>--%>
                                    <%--<el-table-column align="center" width="98">--%>
                                        <%--<template slot-scope="scope" slot="header">--%>
                                            <%--<el-checkbox v-model="getPaperAll" @change="function(val) {checkAllMatChange(val,'paper')}">全选--%>
                                            <%--</el-checkbox>--%>
                                        <%--</template>--%>
                                        <%--<template slot-scope="scope">--%>
                                            <%--<el-form-item :prop="'matsTableData.' + scope.$index + '.getPaper'"--%>
                                                          <%--:rules='(scope.row.zcqy==0&&scope.row.paperIsRequire==1)?model.rules.getPaper:[]'>--%>
                                                <%--<el-checkbox v-model="scope.row.getPaper" :key="scope.row.matinstId"--%>
                                                             <%--@change="function(value) { checkedMatChange(value,scope.$index,'paper')}">已收/验</el-checkbox>--%>
                                            <%--</el-form-item>--%>
                                        <%--</template>--%>
                                    <%--</el-table-column>--%>
                                <%--</el-table-column>--%>
                                <%--<el-table-column label="复印件" align="center">--%>
                                    <%--<el-table-column prop="dueCopyCount" align="center" label="份数" width="60">--%>
                                    <%--</el-table-column>--%>
                                    <%--<el-table-column prop="realCopyCount" align="center" label="收/验" width="70">--%>
                                        <%--<template slot-scope="scope">--%>
                                            <%--<el-form-item :prop="'matsTableData.' + scope.$index + '.realCopyCount'"--%>
                                                          <%--:rules='(scope.row.zcqy==0&&scope.row.attIsRequire==1)?model.rules.realCopyCount:model.rules.realCopyCount1'>--%>
                                                <%--<el-input size="mini" v-model="scope.row.realCopyCount"></el-input>--%>
                                            <%--</el-form-item>--%>
                                        <%--</template>--%>
                                    <%--</el-table-column>--%>
                                    <%--<el-table-column align="center" width="98">--%>
                                        <%--<template slot-scope="scope" slot="header">--%>
                                            <%--<el-checkbox v-model="getCopyAll" @change="function(val) { checkAllMatChange(val,'copy')}">全选--%>
                                            <%--</el-checkbox>--%>
                                        <%--</template>--%>
                                        <%--<template slot-scope="scope">--%>
                                            <%--<el-form-item :prop="'matsTableData.' + scope.$index + '.getCopy'"--%>
                                                          <%--:rules='(scope.row.zcqy==0&&scope.row.attIsRequire==1)?model.rules.getCopy:[]'>--%>
                                                <%--<el-checkbox v-model="scope.row.getCopy" :key="scope.row.matinstId"--%>
                                                             <%--@change="function(value) { checkedMatChange(value,scope.$index,'copy')}">已收/验</el-checkbox>--%>
                                            <%--</el-form-item>--%>
                                        <%--</template>--%>
                                    <%--</el-table-column>--%>
                                <%--</el-table-column>--%>
                            <%--</el-table-column>--%>
                            <%--<el-table-column label="电子件" align="center">--%>
                                <%--<el-table-column align="center" label="必须" width="60">--%>
                                    <%--<template slot-scope="scope">--%>
                                        <%--<span>{{(scope.row.attIsRequire==1)?'是':'否'}}</span>--%>
                                    <%--</template>--%>
                                <%--</el-table-column>--%>
                                <%--<el-table-column label="附件" width="60" align="center">--%>
                                    <%--<template slot-scope="scope">--%>
                                        <%--<i class="upload-flie-icon el-icon-upload" @click="showUploadWindow(scope.row)"></i>--%>
                                    <%--</template>--%>
                                <%--</el-table-column>--%>
                            <%--</el-table-column>--%>
                        <%--</el-table>--%>
                    <%--</el-form>--%>
                <%--</el-collapse-item>--%>
                <%--<!--材料一单清 end-->--%>
                <%--<!--办证结果取件方式 start-->--%>
                <%--<el-collapse-item name="6" id="getResult">--%>
                    <%--<template slot="title">--%>
                        <%--<i class="header-icon el-icon-info"></i>办证取件方式--%>
                    <%--</template>--%>
                    <%--<el-form :model="getResultForm" :rules="(getResultForm.receiveMode==1)?rulesResultForm:rulesResultForm1"--%>
                             <%--ref="resultForm" label-width="176px" class="get-result-form">--%>
                        <%--<input type="hidden" value="getResultForm.id">--%>
                        <%--<el-form-item label="领取模式" prop="receiveMode">--%>
                            <%--<el-radio-group v-model="getResultForm.receiveMode" @change="changeReceiveMode">--%>
                                <%--<el-radio :label="0">邮政快递</el-radio>--%>
                                <%--<el-radio :label="1">窗口取证</el-radio>--%>
                            <%--</el-radio-group>--%>
                        <%--</el-form-item>--%>
                        <%--<el-form-item label="快递类型" prop="smsType" v-if="getResultForm.receiveMode==0">--%>
                            <%--<el-radio-group v-model="getResultForm.smsType">--%>
                                <%--<el-radio :label="1">上门取件</el-radio>--%>
                                <%--<el-radio :label="2">普通快递</el-radio>--%>
                            <%--</el-radio-group>--%>
                        <%--</el-form-item>--%>
                        <%--<div class="clearfix">--%>
                            <%--<el-form-item class="input-inline" label="收件人名字" prop="addresseeName">--%>
                                <%--<el-autocomplete :trigger-on-focus="false" :fetch-suggestions="querySearchLinkMan"--%>
                                                 <%--v-model="getResultForm.addresseeName" @select="queryGetResultMan" placeholder="请输入收件人名字">--%>
                                <%--</el-autocomplete>--%>
                                <%--<!--<el-input v-model="getResultForm.addresseeName" placeholder="请输入收件人名字"></el-input>-->--%>
                            <%--</el-form-item>--%>
                            <%--<el-form-item class="input-inline" label="收件人联系方式" prop="addresseePhone">--%>
                                <%--<el-input v-model="getResultForm.addresseePhone" placeholder="请输入收件人联系方式"></el-input>--%>
                            <%--</el-form-item>--%>
                        <%--</div>--%>
                        <%--<el-form-item label="收件人身份证" prop="addresseeIdcard">--%>
                            <%--<el-input v-model="getResultForm.addresseeIdcard" placeholder="请输入收件人身份证"></el-input>--%>
                        <%--</el-form-item>--%>
                        <%--<el-form-item label="收件人所在区域" required v-if="getResultForm.receiveMode==0">--%>
                            <%--<el-select v-model="getResultForm.addresseeProvince" prop="addresseeProvince" placeholder="--请选择省--">--%>
                                <%--<el-option v-for="(item,index) in provinceList" :key="item.code" :label="item.name"--%>
                                           <%--@click.native.stop="queryCityData(index)" :value="item.code">--%>
                                <%--</el-option>--%>
                            <%--</el-select>--%>
                            <%--<el-select v-model="getResultForm.addresseeCity" prop="addresseeCity" placeholder="--请选择市--">--%>
                                <%--<el-option v-for="(item,index) in cityList" :key="item.code" :label="item.name"--%>
                                           <%--@click.native.stop="queryAreaData(index)" :value="item.code">--%>
                                <%--</el-option>--%>
                            <%--</el-select>--%>
                            <%--<el-select v-model="getResultForm.addresseeCounty" prop="addresseeCounty"--%>
                                       <%--@change="$set(getResultForm, getResultForm.addresseeCounty, $event)" placeholder="--请选择区--">--%>
                                <%--<el-option v-for="(item,index) in countyList" :key="item.code" :label="item.name" :value="item.code">--%>
                                <%--</el-option>--%>
                            <%--</el-select>--%>
                        <%--</el-form-item>--%>
                        <%--<el-form-item label="收件人详细地址" prop="addresseeAddr" v-if="getResultForm.receiveMode==0">--%>
                            <%--<el-input type="textarea" v-model="getResultForm.addresseeAddr" placeholder="请输入收件人详细地址"></el-input>--%>
                        <%--</el-form-item>--%>
                    <%--</el-form>--%>
                    <%--<el-button class="fr" type="primary" icon="el-icon-wallet" @click="saveSmsinfo">保存</el-button>--%>
                <%--</el-collapse-item>--%>
                <%--<!--办证结果取件方式 end-->--%>
            <%--</template>--%>
        <%--</el-collapse>--%>
        <%--<div class="right-bottom-btn" v-if="showMoreProjInfo">--%>
            <%--<el-button type="danger" circle @click="showCommentDialog('3')">不予<br />受理</el-button>--%>
            <%--<!--        <el-button type="primary" circle @click="getLackMatsMatmend()" :disabled="isMend">材料<br />补全</el-button>-->--%>
            <%--<el-button type="primary" circle @click="showCommentDialog('5')">材料<br />补全</el-button>--%>
            <%--<el-button type="primary" circle @click="showCommentDialog('0')">发起<br />申报</el-button>--%>
            <%--<el-button type="primary" circle @click="showCommentDialog('1')">打印<br />回执</el-button>--%>

        <%--</div>--%>
        <%--<!--项目树窗口 start-->--%>
        <%--<el-dialog title="查看项目工程结构" :visible.sync="projectTreeInfoModal" class="proj-tree-dialog"--%>
                   <%--@closed="clearTreeChildInfo()" width="750px">--%>
            <%--<div class="project-tree">--%>
                <%--<div class="search-proj" v-if="!addChildProjShow">--%>
                    <%--<el-input suffix-icon="el-icon-search" :inline="true" placeholder="输入关键字" v-model="searchProjfilterText">--%>
                    <%--</el-input>--%>
                    <%--<el-button @click="clearProjfilterText">清空</el-button>--%>
                <%--</div>--%>
                <%--<div class="search-proj" v-else>--%>
                    <%--<el-input :inline="true" placeholder="请输入子项目工程名称" v-model="childProjName">--%>
                    <%--</el-input>--%>
                    <%--<el-input :inline="true" placeholder="请输入子项目工程备注" v-model="childProjText">--%>
                    <%--</el-input>--%>
                    <%--<el-button type="primary" :loading="addChildLoading" @click="saveAddChildProj()">确认</el-button>--%>
                    <%--<el-button type="danger" @click="addChildProjShow=false">取消</el-button>--%>
                <%--</div>--%>

                <%--<el-tree :data="projTree" :props="projTreeDefaultProps" default-expand-all show-checkbox--%>
                         <%--:expand-on-click-node="false" :check-on-click-node="true" highlight-current @node-click="selChildProj"--%>
                         <%--@node-contextmenu="showRightBtnList" :check-strictly="true" :filter-node-method="filterProjNode"--%>
                         <%--ref="projTree">--%>
            <%--<span class="custom-tree-node" slot-scope="scope">--%>
              <%--<span class="tree-icon-name">--%>
                <%--<i--%>
                        <%--:class="scope.data.icon?scope.data.icon:scope.data.children?'el-icon-folder-opened':'el-icon-tickets'"></i>{{ scope.data.name}}--%>
              <%--</span>--%>
            <%--</span>--%>
                <%--</el-tree>--%>
                <%--<div class="tree-right-btn" v-show="treeRightBtnList" :style="{top:treeBtnTop+'px',left:treeBtnLeft+'px'}">--%>
                    <%--<a href="javascript:void(0);" class="list-group-item" @click="addChildChildProj();">--%>
                        <%--<i class="el-icon-circle-plus-outline"></i>新增子工程--%>
                    <%--</a>--%>
                    <%--<a href="javascript:void(0);" class="list-group-item" @click="delChildChildProj();">--%>
                        <%--<i class="el-icon-delete"></i>删除子工程--%>
                    <%--</a>--%>
                <%--</div>--%>
            <%--</div>--%>
        <%--</el-dialog>--%>
        <%--<!--项目树窗口 end-->--%>
        <%--<!--新增编辑联系人 start-->--%>
        <%--<el-dialog :title="addEditManModalTitle" :visible.sync="addEditManModalShow" width="750px"--%>
                   <%--class="dg-modal-add-linkmain-info" @close="resetForm('addEditManform')">--%>
            <%--<el-form :model="addEditManform" ref="addEditManform" label-width="180px" :rules="addLinkManRules">--%>
                <%--<el-form-item label="所在单位：" required>--%>
                    <%--<el-select v-model="addEditManform.unitName" placeholder="请选择单位">--%>
                        <%--<el-option v-for="item in unitInfoIdList" :key="item.unitInfoId" :label="item.applicant"--%>
                                   <%--:value="item.unitInfoId"></el-option>--%>
                    <%--</el-select>--%>
                <%--</el-form-item>--%>
                <%--<el-form-item label="联系人姓名：" prop="linkmanName">--%>
                    <%--<el-input v-model="addEditManform.linkmanName" placeholder="请输入联系人姓名"></el-input>--%>
                <%--</el-form-item>--%>
                <%--<el-form-item label="证件号：" prop="linkmanCertNo">--%>
                    <%--<el-input v-model="addEditManform.linkmanCertNo" placeholder="请输入联系人证件号"></el-input>--%>
                <%--</el-form-item>--%>
                <%--<el-form-item label="手机号码：" prop="linkmanMobilePhone">--%>
                    <%--<el-input v-model="addEditManform.linkmanMobilePhone" placeholder="请输入联系人手机号码"></el-input>--%>
                <%--</el-form-item>--%>
                <%--<el-form-item label="办公电话：">--%>
                    <%--<el-input v-model="addEditManform.linkmanOfficePhon"></el-input>--%>
                <%--</el-form-item>--%>
                <%--<el-form-item label="传真：">--%>
                    <%--<el-input v-model="addEditManform.linkmanFax"></el-input>--%>
                <%--</el-form-item>--%>
                <%--<el-form-item label="电子邮件：">--%>
                    <%--<el-input v-model="addEditManform.linkmanMail"></el-input>--%>
                <%--</el-form-item>--%>
                <%--<el-form-item label="联系人住址：">--%>
                    <%--<el-input v-model="addEditManform.linkmanAddr"></el-input>--%>
                <%--</el-form-item>--%>
                <%--<el-form-item label="备注：">--%>
                    <%--<el-input v-model="addEditManform.linkmanMemo"></el-input>--%>
                <%--</el-form-item>--%>
            <%--</el-form>--%>
            <%--<div slot="footer" class="dialog-footer">--%>
                <%--<el-button @click="addEditManModalShow = false">取 消</el-button>--%>
                <%--<el-button type="primary" @click="saveAeaLinkmanInfo('u')">确 定</el-button>--%>
            <%--</div>--%>
        <%--</el-dialog>--%>
        <%--<!--新增编辑联系人 end-->--%>
        <%--<!--选择主题 start-->--%>
        <%--<el-dialog title="选择主题" :visible.sync="selThemeDialogShow" width="750px">--%>
            <%--<div class="theme-content" :style="{maxHeight:curHeight-380+'px',overflow: 'auto',}">--%>
                <%--<div class="themeTypeList">--%>
            <%--<span class="theme-type" :class="themeDialogIndex==index?'active':''"--%>
                  <%--@click="changeThemeType(item,index,'parallel')"--%>
                  <%--:key="index"--%>
                  <%--v-for="(item,index) in parallelThemeList">--%>
              <%--{{item.themeTypeName}}--%>
            <%--</span>--%>
                <%--</div>--%>
                <%--<div class="pro-type-item"--%>
                     <%--:class="selThemeActive==index?'active':''"--%>
                     <%--v-for="(item,index) in themeInfoListP"--%>
                     <%--:key="index"--%>
                     <%--@click="chooseTheme(item,index,'parallel');">--%>
                    <%--<img class="text-left" src="../../apply/imgs/pro_type_4.png">--%>
                    <%--<span class="theme-name ellipsis">{{item.themeName}}</span>--%>
                <%--</div>--%>
            <%--</div>--%>
        <%--</el-dialog>--%>
        <%--<!--选择主题 end-->--%>
        <%--<!--样本弹窗 start-->--%>
        <%--<el-dialog title="查看样本" :visible.sync="showMatFilesDialogShow" width="750px">--%>
            <%--<div class="mat-file-list" v-html="dialogHtml"></div>--%>
            <%--<div slot="footer" class="dialog-footer">--%>
                <%--<el-button @click="showMatFilesDialogShow = false">关 闭</el-button>--%>
            <%--</div>--%>
        <%--</el-dialog>--%>
        <%--<!--样本弹窗 end-->--%>
        <%--<!--文件上传下载弹窗 start-->--%>
        <%--<el-dialog v-loading="loadingFileWin" element-loading-text="文件上传中"--%>
                   <%--element-loading-spinner="el-icon-loading" element-loading-background="rgba(0, 0, 0, 0.3)"--%>
                   <%--class="upload-flie-dialog" :title="showUploadWindowTitle" :visible.sync="showUploadWindowFlag"--%>
                   <%--@closed="uploadTableData=[]" width="750px">--%>
            <%--<div class="upload-file-content">--%>
                <%--<div class="file-opt-btn">--%>
                    <%--<el-button type="primary" @click="downloadFile()">下载</el-button>--%>
                    <%--<el-upload ref="uploadFileWin" class="upload-s-file" :action="ctx+'rest/mats/att/upload'"--%>
                               <%--:show-file-list="false" :http-request="uploadFileCom" :file-list="fileList">--%>
                        <%--<el-button slot="trigger" size="small" type="primary">上传</el-button>--%>
                    <%--</el-upload>--%>
                    <%--<el-button type="primary" @click="delSelectFileCom()">删除</el-button>--%>
                    <%--<el-button type="primary" @click="openPhotoWindow(selMatRowData,'lt')">良田拍照</el-button>--%>
                    <%--<el-button type="primary" @click="openPhotoWindow(selMatRowData,'fz')">方正拍照</el-button>--%>
                <%--</div>--%>
                <%--<el-table :data="uploadTableData" border @selection-change="selectionFileChange">--%>
                    <%--<el-table-column type="selection" align="center" width="55">--%>
                    <%--</el-table-column>--%>
                    <%--<el-table-column prop="fileName" label="文件名"></el-table-column>--%>
                    <%--<el-table-column label="操作" align="center" width="200">--%>
                        <%--<template slot-scope="scope">--%>
                            <%--<el-button type="primary" size="mini" @click="filePreview(scope.row)">预览</el-button>--%>
                            <%--<el-button type="primary" size="mini" :disabled="getFileType(scope.row.fileName)!=='pdf'" @click="filePreview(scope.row,'pdf')">查看施工图</el-button>--%>
                        <%--</template>--%>
                    <%--</el-table-column>--%>
                <%--</el-table>--%>
            <%--</div>--%>
        <%--</el-dialog>--%>
        <%--<!--文件上传下载弹窗 end-->--%>
        <%--<!--导入电子材料弹窗 start-->--%>
        <%--<el-dialog class="upload-flie-dialog" title="材料附件" :visible.sync="getFileListWindowFlag" width="750px">--%>
            <%--<div class="upload-file-content">--%>
                <%--<div class="file-opt-btn search-proj">--%>
                    <%--<el-input suffix-icon="el-icon-search" :inline="true" placeholder="输入关键字" v-model="searchFileListfilter">--%>
                    <%--</el-input>--%>
                    <%--<el-button @click="searchFileListfilter=''">清空</el-button>--%>
                    <%--<el-button type="primary" @click="fileImport">导入</el-button>--%>
                <%--</div>--%>
                <%--<el-table :data="shareFileList" border ref="shareFileList">--%>
                    <%--<el-table-column type="selection" align="center" width="55">--%>
                    <%--</el-table-column>--%>
                    <%--<el-table-column prop="attName" label="文件名"></el-table-column>--%>
                    <%--<el-table-column label="操作" align="center" width="150">--%>
                        <%--<template slot-scope="scope">--%>
                            <%--<el-button type="primary" size="mini" @click="filePreview(scope.row)">预览</el-button>--%>
                        <%--</template>--%>
                    <%--</el-table-column>--%>
                <%--</el-table>--%>
            <%--</div>--%>
        <%--</el-dialog>--%>
        <%--<!--导入电子材料弹窗 end-->--%>
        <%--<!--申报意见弹窗 start-->--%>
        <%--<el-dialog class="submit-comments" :title="submitCommentsTitle" :visible.sync="submitCommentsFlag" width="750px">--%>
            <%--<div class="comments-content">--%>
                <%--<p class="com-header">{{submitCommentsType=='3'?'不予受理意见':'收件意见'}}</p>--%>
                <%--<el-row>--%>
                    <%--<el-col :span="16">--%>
                        <%--<el-select v-model="selComment" @change="changeUserComment" placeholder="请选择">--%>
                            <%--<el-option--%>
                                    <%--v-for="(item,index) in commentsList"--%>
                                    <%--:key="index"--%>
                                    <%--:label="item.opinionContent"--%>
                                    <%--:value="item.opinionContent">--%>
                            <%--</el-option>--%>
                        <%--</el-select>--%>
                    <%--</el-col>--%>
                    <%--<el-col :span="8">--%>
                        <%--<el-button @click="setUseComment()">设为常用意见</el-button>--%>
                        <%--<el-button @click="mUseComments()">管理</el-button>--%>
                    <%--</el-col>--%>
                <%--</el-row>--%>
                <%--<el-row>--%>
                    <%--<el-col :span="24">--%>
                        <%--<el-input type="textarea" v-model="comments"></el-input>--%>
                    <%--</el-col>--%>
                <%--</el-row>--%>
                <%--<el-row v-if="showMatList">--%>
                    <%--<el-col :span="24">--%>
                        <%--<el-select v-model="selectMat" multiple placeholder="请选择不予受理的材料" @change="getSelectedMat">--%>
                            <%--<el-option v-for="(item, index) in model.matsTableData" :key="item.matId" :label="item.matName"--%>
                                       <%--:value="item.matName">--%>
                                <%--<span class="fl">{{ index+1 }}：</span>--%>
                                <%--<span class="fl">{{ item.matName }}</span>--%>
                            <%--</el-option>--%>
                        <%--</el-select>--%>
                    <%--</el-col>--%>
                <%--</el-row>--%>
            <%--</div>--%>
            <%--<div slot="footer" class="dialog-footer">--%>
                <%--<el-button @click="submitCommentsFlag = false">取 消</el-button>--%>
                <%--<el-button type="primary" @click="startParalleApprove()">发送</el-button>--%>
            <%--</div>--%>
            <%--<el-dialog title="常用意见管理" append-to-body class="edit-comments" :visible.sync="submitCommentsFlag1"--%>
                       <%--width="700px">--%>
                <%--<el-table :data="commentsList">--%>
                    <%--<el-table-column property="opinionContent" label="常用意见"></el-table-column>--%>
                    <%--<el-table-column property="_handle" label="操作" width="150" class="handle-icon">--%>
                        <%--<template slot-scope="scope">--%>
                            <%--<span title="编辑" class="el-icon-edit" @click="editComment(scope.row)"></span>--%>
                            <%--<span title="删除" class="el-icon-delete" @click="deleteUseComment(scope.row.opinionId)"></span>--%>
                        <%--</template>--%>
                    <%--</el-table-column>--%>
                <%--</el-table>--%>
                <%--<el-dialog title="编辑常用意见" append-to-body class="edit-comments" :visible.sync="submitCommentsFlag2"--%>
                           <%--width="600px">--%>
                    <%--<el-form :model="commentInfo" label-width="100px">--%>
                        <%--<el-form-item label="意见">--%>
                            <%--<el-input type="textarea" v-model="commentInfo.comment" autocomplete="off"></el-input>--%>
                        <%--</el-form-item>--%>
                        <%--<el-form-item label="排序号">--%>
                            <%--<el-input type="number" v-model="commentInfo.sort" autocomplete="off"></el-input>--%>
                        <%--</el-form-item>--%>
                    <%--</el-form>--%>
                    <%--<div slot="footer" class="dialog-footer">--%>
                        <%--<el-button @click="submitCommentsFlag2 = false">关闭</el-button>--%>
                        <%--<el-button type="primary" @click="updateComment()">保存</el-button>--%>
                    <%--</div>--%>
                <%--</el-dialog>--%>
            <%--</el-dialog>--%>
        <%--</el-dialog>--%>
        <%--<!--申报意见弹窗 end-->--%>
        <%--<!--回执列表弹窗 start-->--%>
        <%--<el-dialog title="回执列表" @closed="reloadPage()" :visible.sync="receiveModalShow" :close-on-click-modal="false"--%>
                   <%--:close-on-press-escape="false" class="receive-modal" width="80%" :style="{height: curHeight+'px'}">--%>
            <%--<div class="fl">--%>
                <%--<div class="receive-item"--%>
                     <%--:class="receiveActive==ind?'active':''"--%>
                     <%--:key="ind"--%>
                     <%--v-for="(item,ind) in receiveList">--%>
                    <%--<p class="receive-item-header">--%>
                        <%--<span :title="item.name" class="ellipsis">{{item.name}}</span>--%>
                        <%--<i class="fr icon" :class="item.show?'el-icon-arrow-down':'el-icon-arrow-right'" @click="toggleReceiveListShow(item)"></i>--%>
                        <%--<span class="fr" :class="item.isSeries==0?'receive-type-y':'receive-type'">{{item.isSeries==0?'并联':'并行'}}</span>--%>
                    <%--</p>--%>
                    <%--<ul class="receive-list" v-show="item.show">--%>
                        <%--<li :class="receiveItemActive==index?'active':''"--%>
                            <%--:key="index"--%>
                            <%--v-for="(itemBtn,index) in item.receiveList"--%>
                            <%--@click="printReceive1(itemBtn,index,ind)">--%>
                            <%--<!--<span>{{(itemBtn.receiptType==2)?'收件回执':(itemBtn.receiptType==1)?'物料回执':(itemBtn.receiptType==3)?'不受理回执':'其他回执'}}</span>-->--%>
                            <%--<span>{{$options.filters.changeReceiveType(itemBtn.receiptType)}}</span>--%>
                        <%--</li>--%>
                    <%--</ul>--%>
                <%--</div>--%>
            <%--</div>--%>
            <%--<div class="pdf-view-win" :style="{height: (curHeight-144)+'px'}">--%>
                <%--<iframe :src="pdfSrc" width="100%" height="100%"></iframe>--%>
            <%--</div>--%>
        <%--</el-dialog>--%>
        <%--<!--回执列表弹窗 end-->--%>
        <%--<!--分局承办部门组织树窗口 start-->--%>
        <%--<el-dialog title="选择分局机构" :visible.sync="orgTreeInfoModal" class="org-tree-dialog" @closed="clearTreeChildInfo()"--%>
                   <%--width="750px">--%>
            <%--<div class="project-tree">--%>
                <%--<div class="search-proj" v-if="!addChildProjShow">--%>
                    <%--<el-input suffix-icon="el-icon-search" :inline="true" placeholder="输入关键字" v-model="searchOrgfilterText">--%>
                    <%--</el-input>--%>
                    <%--<el-button @click="clearProjfilterText">清空</el-button>--%>
                <%--</div>--%>
                <%--<el-tree :data="orgTree" :props="projTreeDefaultProps" default-expand-all highlight-current--%>
                         <%--@node-click="selChildOrg" :filter-node-method="filterProjNode" ref="orgTree">--%>
            <%--<span class="custom-tree-node" slot-scope="scope">--%>
              <%--<span class="tree-icon-name">--%>
                <%--<i class="el-icon-s-order"></i>{{ scope.data.name}}--%>
              <%--</span>--%>
            <%--</span>--%>
                <%--</el-tree>--%>
            <%--</div>--%>
        <%--</el-dialog>--%>
        <%--<!--分局承办部门组织树窗口 end-->--%>
        <%--<!--  进度条  start-->--%>
        <%--<el-dialog class="show-loading-prog" fullscreen :show-close="false" :close-on-press-escape="false"--%>
                   <%--@closed="uploadPercentage=0"--%>
                   <%--:close-on-click-modal="false" :visible.sync="progressDialogVisible">--%>
            <%--<el-progress type="circle" :width="110" :stroke-width="10" :format="formatUploadPercentage"--%>
                         <%--:percentage="uploadPercentage"></el-progress>--%>
        <%--</el-dialog>--%>
        <%--<!--  一张表单  start-->--%>
        <%--<el-dialog @closed="uploadPercentage=0;progressDialogVisible=false" class="show-one-form" title="一张表单" :close-on-press-escape="false" :close-on-click-modal="false"--%>
                   <%--width="1000px" :visible.sync="oneFormDialogVisible">--%>
            <%--<div id="oneFormContent" :style="{height:(curHeight-180)+'px',overflow: 'auto'}"></div>--%>
        <%--</el-dialog>--%>
        <%--<!--  一张表单  end-->--%>

        <%--<!-- 材料补全dialog -->--%>
        <%--<el-dialog title="材料补全" :visible.sync="isShowMatmend" class="dialog mertial-supplement-dialog" width="880px">--%>
            <%--<!-- 标签（正在补件的显示） -->--%>
            <%--<div class="supplement-tag" v-if="matMendDialogData.correctState == 6 || matMendDialogData.correctState == 7">正在补件中...</div>--%>
            <%--<el-form :inline="true" :model="matMendForm" :rules="supplementFormRules" label-width="140px" class="demo-form-inline supplement-form" ref="matMendForm" v-loading="sloading">--%>
                <%--<el-form-item class="half-item" label="办件流水号">--%>
                    <%--<!-- <el-input placeholder="请输入办件流水号" v-model="matMendForm.applyinstCode" disabled></el-input> -->--%>
                    <%--<span class="color-999">{{matMendForm.applyinstCode || '暂无'}}</span>--%>
                <%--</el-form-item>--%>
                <%--<el-form-item class="half-item" label="项目编号">--%>
                    <%--<!-- <el-input placeholder="请输入项目编号" v-model="matMendForm.localCode" disabled></el-input> -->--%>
                    <%--<span class="color-999">{{matMendForm.localCode || '暂无'}}</span>--%>
                <%--</el-form-item>--%>
                <%--<el-form-item  class="half-item" label="项目名称">--%>
                    <%--<!-- <el-input placeholder="请输入项目名称" v-model="matMendForm.projInfoName" disabled></el-input> -->--%>
                    <%--<span class="color-999">{{matMendForm.projInfoName || '暂无'}}</span>--%>
                <%--</el-form-item>--%>
                <%--<el-form-item  class="half-item" label="客户名称">--%>
                    <%--<!-- <el-input placeholder="请输入客户名称" v-model="matMendForm.owner" disabled></el-input> -->--%>
                    <%--<span class="color-999">{{matMendForm.owner || '暂无'}}</span>--%>
                <%--</el-form-item>--%>
                <%--<el-form-item class="half-item" label="审批事项名称">--%>
                    <%--<!-- <el-input placeholder="请输入审批事项名称" v-model="matMendForm.iteminstName" disabled></el-input> -->--%>
                    <%--<span class="color-999">{{matMendForm.iteminstName || '暂无'}}</span>--%>
                <%--</el-form-item>--%>
                <%--<el-form-item class="half-item" label="负责部门名称">--%>
                    <%--<!-- <el-input placeholder="请输入负责部门名称" v-model="matMendForm.approveOrgName" disabled></el-input> -->--%>
                    <%--<span class="color-999">{{matMendForm.approveOrgName || '暂无'}}</span>--%>
                <%--</el-form-item>--%>
                <%--<el-form-item  class="half-item" label="补全办理时限(天)" prop="correctDueDays">--%>
                    <%--<span class="color-999" v-if="matMendDialogData.correctState == 6 || matMendDialogData.correctState == 7">{{matMendForm.correctDueDays || '暂无'}}</span>--%>
                    <%--<el-input placeholder="请输入补全办理时限" type="number" min="0"  v-else--%>
                              <%--v-model="matMendForm.correctDueDays"></el-input>--%>
                <%--</el-form-item>--%>
                <%--<el-form-item class="fill-item" :label="inSeletcedMaterialPandel? '待补全材料清单':'已收清单列表'">--%>
                    <%--<template v-if="!inSeletcedMaterialPandel">--%>
                        <%--<div class="material-table-header">--%>
                            <%--<span style="width: 126px;">材料名称</span>--%>
                            <%--<span style="width: 61px;">类型</span>--%>
                            <%--<span style="width: 100px;">补全份数</span>--%>
                            <%--<span style="width: 282px;">意见</span>--%>
                            <%--<span style="width: 58px;">操作</span>--%>
                        <%--</div>--%>
                        <%--<div class="material-table-wrap">--%>
                            <%--<div class="material-table"--%>
                                 <%--v-for="(item,index) in tobaMakeupMaterialList2"--%>
                                 <%--:key="item.matId">--%>
                                <%--<div class="tb-title inline-block"><p>{{item.matName}}</p></div>--%>
                                <%--<div class="tb-content inline-block">--%>
                                    <%--<div class="tb-item" v-if="item.isNeedOrign">--%>
                                        <%--<span class="inline-block tip">原件</span>--%>
                                        <%--<div class="inline-block border-left al-center num ">--%>
                                            <%--<span class="color-999" v-if="matMendDialogData.correctState == 6 || matMendDialogData.correctState == 7">{{tobaMakeupMaterialList2[index].paperCount || '0'}}</span>--%>
                                            <%--<el-input class="w98" placeholder="补全份数" type="number" min="0" v-model="tobaMakeupMaterialList2[index].paperCount" v-else></el-input>--%>
                                        <%--</div>--%>
                                        <%--<div class="inline-block border-left al-center opinion">--%>
                                            <%--<span class="color-999" v-if="matMendDialogData.correctState == 6 || matMendDialogData.correctState == 7">{{tobaMakeupMaterialList2[index].paperDueIninstOpinion || '暂无'}}</span>--%>
                                            <%--<el-input class="w98" placeholder="请输入意见" v-model="tobaMakeupMaterialList2[index].paperDueIninstOpinion" v-else></el-input>--%>
                                        <%--</div>--%>
                                        <%--<div class="inline-block border-left al-center delete">--%>
                        <%--<span class="color-999" v-if="matMendDialogData.correctState == 6 || matMendDialogData.correctState == 7">--%>
                            <%--<i class="el-icon-delete color-999" style="cursor: not-allowed;"></i>--%>
                        <%--</span>--%>
                                            <%--<i class="el-icon-delete" @click="delTobaMakeupMaterialMend('isNeedOrign',item,index)" v-else></i>--%>
                                        <%--</div>--%>
                                    <%--</div>--%>
                                    <%--<div class="tb-item" v-if="item.isNeedCopy">--%>
                                        <%--<span class="inline-block tip">复印件</span>--%>
                                        <%--<div class="inline-block border-left al-center num">--%>
                                            <%--<span class="color-999" v-if="matMendDialogData.correctState == 6 || matMendDialogData.correctState == 7">{{tobaMakeupMaterialList2[index].copyCount || '0'}}</span>--%>
                                            <%--<el-input class="w98" placeholder="补全份数" type="number" min="0" v-model="tobaMakeupMaterialList2[index].copyCount" v-else></el-input>--%>
                                        <%--</div>--%>
                                        <%--<div class="inline-block border-left al-center opinion">--%>
                                            <%--<span class="color-999" v-if="matMendDialogData.correctState == 6 || matMendDialogData.correctState == 7">{{tobaMakeupMaterialList2[index].copyDueIninstOpinion || '暂无'}}</span>--%>
                                            <%--<el-input class="w98" placeholder="请输入意见" v-model="tobaMakeupMaterialList2[index].copyDueIninstOpinion" v-else></el-input>--%>
                                        <%--</div>--%>
                                        <%--<div class="inline-block border-left al-center delete">--%>
                        <%--<span class="color-999" v-if="matMendDialogData.correctState == 6 || matMendDialogData.correctState == 7">--%>
                            <%--<i class="el-icon-delete color-999" style="cursor: not-allowed;"></i>--%>
                        <%--</span>--%>
                                            <%--<i class="el-icon-delete" @click="delTobaMakeupMaterialMend('isNeedCopy',item,index)" v-else></i>--%>
                                        <%--</div>--%>
                                    <%--</div>--%>
                                    <%--<div class="tb-item" v-if="item.isNeedElectron">--%>
                                        <%--<span class="inline-block tip">电子件</span>--%>
                                        <%--<div class="inline-block border-left al-center num">--%>
                                            <%--<el-switch v-model="tobaMakeupMaterialList2[index].isNeedAtt" active-color="#13ce66" disabled>--%>
                                            <%--</el-switch>--%>
                                        <%--</div>--%>
                                        <%--<div class="inline-block border-left al-center opinion">--%>
                                            <%--<span class="color-999" v-if="matMendDialogData.correctState == 6 || matMendDialogData.correctState == 7">{{tobaMakeupMaterialList2[index].attDueIninstOpinion || '暂无'}}</span>--%>
                                            <%--<el-input class="w98" placeholder="请输入意见" v-model="tobaMakeupMaterialList2[index].attDueIninstOpinion" v-else></el-input>--%>
                                        <%--</div>--%>
                                        <%--<div class="inline-block border-left al-center delete">--%>
                        <%--<span class="color-999" v-if="matMendDialogData.correctState == 6 || matMendDialogData.correctState == 7">--%>
                            <%--<i class="el-icon-delete color-999" style="cursor: not-allowed;"></i>--%>
                        <%--</span>--%>
                                            <%--<i class="el-icon-delete" @click="delTobaMakeupMaterialMend('isNeedElectron',item,index)" v-else></i>--%>
                                        <%--</div>--%>
                                    <%--</div>--%>
                                <%--</div>--%>
                                <%--<!-- <div class="close-box"> <i class="el-icon-circle-close"></i> </div> -->--%>
                            <%--</div>--%>
                        <%--</div>--%>
                        <%--<div class="show-all" v-if="matMendDialogData.correctState == 6 || matMendDialogData.correctState == 7" style="height: 1px;border-bottom: 1px solid #e5e5e5;"></div>--%>
                        <%--<div class="show-all" @click="inSeletcedMaterialPandel = true;" v-else>选择材料</div>--%>
                    <%--</template>--%>
                    <%--<template v-else>--%>
                        <%--<div class="material-table-header">--%>
                            <%--<span style="width: 126px;">材料名称</span>--%>
                            <%--<span style="width: 101px;">类型</span>--%>
                            <%--<span style="width: 100px;">应收（份）</span>--%>
                            <%--<span style="width: 100px;">已收（份）</span>--%>
                            <%--<span style="width: 200px;">操作</span>--%>
                        <%--</div>--%>
                        <%--<div class="material-table-wrap">--%>
                            <%--<div class="material-table"--%>
                                 <%--v-for="(item,index) in tobeSelectMaterialList2"--%>
                                 <%--:key="item.matId">--%>
                                <%--<div class="tb-title inline-block"><p>{{item.matName}}</p></div>--%>
                                <%--<div class="tb-content inline-block">--%>
                                    <%--<div class="tb-item" v-if="item.paperCount==1">--%>
                                        <%--<span class="inline-block show-span">原件</span>--%>
                                        <%--<span class="inline-block show-span">{{item.paperCount || '0'}}</span>--%>
                                        <%--<span class="inline-block show-span">{{item.realPaperCount || '0'}}</span>--%>
                                        <%--<div class="inline-block border-left al-center check">--%>
                                            <%--<el-checkbox v-model="tobeSelectMaterialList2[index].isNeedOrign" @change="clickTobaSelectMaterial"></el-checkbox>--%>
                                        <%--</div>--%>
                                    <%--</div>--%>
                                    <%--<div class="tb-item" v-if="item.copyCount==1">--%>
                                        <%--<span class="inline-block show-span">复印件</span>--%>
                                        <%--<span class="inline-block show-span">{{item.copyCount || '0'}}</span>--%>
                                        <%--<span class="inline-block show-span">{{item.realCopyCount || '0'}}</span>--%>
                                        <%--<div class="inline-block border-left al-center check">--%>
                                            <%--<el-checkbox v-model="tobeSelectMaterialList2[index].isNeedCopy"  @change="clickTobaSelectMaterial"></el-checkbox>--%>
                                        <%--</div>--%>
                                    <%--</div>--%>
                                    <%--<div class="tb-item" v-if="item.attCount==1">--%>
                                        <%--<span class="inline-block show-span">电子件</span>--%>
                                        <%--<span class="inline-block show-span">必须</span>--%>
                                        <%--<span class="inline-block show-span">{{item.attCount || '0'}}</span>--%>
                                        <%--<div class="inline-block border-left al-center check">--%>
                                            <%--<el-checkbox v-model="tobeSelectMaterialList2[index].isNeedElectron" @change="clickTobaSelectMaterial"></el-checkbox>--%>
                                        <%--</div>--%>
                                    <%--</div>--%>
                                <%--</div>--%>
                            <%--</div>--%>
                        <%--</div>--%>
                        <%--<div class="show-all" @click="inSeletcedMaterialPandel = false; selectMaterialToMakeupList();">确定并返回</div>--%>
                    <%--</template>--%>
                <%--</el-form-item>--%>
                <%--<el-form-item class="fill-item" label="备注">--%>
                    <%--<span class="color-999" v-if="matMendDialogData.correctState == 6 || matMendDialogData.correctState == 7">{{matMendDialogData.correctMemo || '暂无'}}</span>--%>
                    <%--<el-input placeholder="请输入备注" type="textarea" v-model="matMendDialogData.correctMemo" v-else></el-input>--%>
                <%--</el-form-item>--%>
            <%--</el-form>--%>
            <%--<div slot="footer" class="dialog-footer">--%>
                <%--<el-button type="primary" @click="saveMaterialCorrectionMend" :disabled="matMendDialogData.correctState == 6 || matMendDialogData.correctState == 7">补全开始</el-button>--%>
                <%--<el-button @click="isShowMatmend=false;isGetReceiveList()">关闭</el-button>--%>
            <%--</div>--%>
        <%--</el-dialog>--%>
    <%--</div>--%>
<%--</div>--%>
<%--</body>--%>
<%--<th:block th:include="common/common :: commonfooter" />--%>
<%--<th:block th:include="common/common :: gaopaiyi" />--%>
<%--<script th:src="@{/preview/pdfjs/build/pdf.js}"></script>--%>
<%--<script src="../../static/apply/js/index.js" th:src="@{/apply/js/index.js}"></script>--%>

<%--</html>--%>