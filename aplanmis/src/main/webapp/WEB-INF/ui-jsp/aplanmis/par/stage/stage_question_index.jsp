<%@ page contentType="text/html;charset=UTF-8" %>

<!DOCTYPE html>
<html style="overflow-x: hidden;height: 100%;
    margin: 0;
    padding: 0;
    border: 0;
    overflow: auto;">
<head>
    <title>常见问题解答</title>
    <%@ include file="/ui-static/agcloud/framework/jsp/agcloud-meta.jsp"%>
    <%@ include file="/ui-static/agcloud/framework/jsp/agcloud-common.jsp"%>
    <%@ include file="/ui-static/agcloud/framework/theme-libs/metronic-v5/template-default.jsp"%>
    <%@ include file="/ui-static/agcloud/framework/jsp/lib-bootstrap-table.jsp"%>
    <script src="${pageContext.request.contextPath}/ui-static/dg_aplanmis/framework/js/jquery.nicescroll.js" type="text/javascript"></script>
    <script type="text/javascript">
        var recordId = '${recordId}';
        var curIsEditable = ${curIsEditable};
    </script>
    <style type="text/css">
        .row{
            margin-left: 0px;
            margin-left: 0px;
        }
        .label{
            text-align: right;
        }
    </style>
</head>
<body>
    <div id="mainContentPanel" class="row" style="width: 100%;height: 99%;padding: 0px 5px 0px 0px;margin: 0px;">
        <div class="col-xl-12" style="padding: 0px 2px 0px 5px;">
            <div class="m-portlet" style="margin-bottom: 0px;width: 100%;height: 100%;">
                <div class="m-portlet__head">
                    <div class="m-portlet__head-caption">
                        <div class="m-portlet__head-title">
                            <span class="m-portlet__head-icon m--hide">
                                <i class="la la-gear"></i>
                            </span>
                            <h3 class="m-portlet__head-text">
                                常见问题解答列表
                            </h3>
                        </div>
                    </div>
                </div>
                <div class="m-portlet__body" style="padding: 10px 0px;">
                    <!-- 按钮区域 -->
                    <div class="m-form m-form--label-align-right m--margin-bottom-5">
                        <div class="row" style="margin: 0px;">
                            <div class="col-md-6"style="text-align: left;">
                                <button type="button" class="btn btn-info"
                                        onclick="addQuestAnswer();">新增</button>
                                <button type="button" class="btn btn-secondary"
                                        onclick="batchDelQuestAnswer();">删除</button>
                                <button type="button" class="btn btn-secondary"
                                        onclick="refreshQuestAnswer();">刷新</button>
                            </div>
                            <div class="col-md-6" style="padding: 0px;">
                                <div class="row" style="margin: 0px;">
                                    <div class="col-3"></div>
                                    <div class="col-6">
                                        <form id="search_que_ans_form" method="post">
                                            <div class="m-input-icon m-input-icon--left">
                                                <input type="text" class="form-control m-input"
                                                       placeholder="请输入关键字..." name="keyword" value=""/>
                                                <span class="m-input-icon__icon m-input-icon__icon--left">
                                                    <span><i class="la la-search"></i></span>
                                                </span>
                                            </div>
                                        </form>
                                    </div>
                                    <div class="col-3">
                                        <button type="button" class="btn  btn-info" onclick="searchQuestAnswer();">查询</button>
                                        <button type="button" class="btn  btn-secondary" onclick="clearQuestAnswer();">清空</button>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                    <!-- 按钮区域end -->
                    <div style="margin: 10px 0px;border-bottom: 1px solid #e8e8e8;"></div>
                    <!-- 列表区域 -->
                    <div class="base" style="padding: 0px 5px;">
                        <table  id="questAnswerTb"
                                data-toggle="table"
                                data-method="post"
                                data-content-type="application/x-www-form-urlencoded; charset=UTF-8"
                                data-click-to-select=true
                                data-pagination-detail-h-align="left"
                                data-pagination-show-page-go="true"
                                data-page-size="10"
                                data-page-list="[10,20,30,50,100]",
                                data-pagination=true
                                data-side-pagination="server"
                                data-pagination-detail-h-align="left"
                                data-query-params="questAnswerParam"
                                data-url="${pageContext.request.contextPath}/aea/par/stage/questions/listQuestAnswers.do">
                            <thead>
                            <tr>
                                <th data-field="#" data-checkbox="true" data-align="center"
                                    data-colspan="1" data-width="10">ID</th>
                                <th data-field="question" data-formatter="showLengthFormatter"
                                    data-align="left" data-colspan="1" data-width="300">问题</th>
                                <th data-field="answer" data-formatter="showLengthFormatter"
                                    data-align="left"  data-colspan="1" data-width="300">答案</th>
                                <th data-field="sortNo" data-align="center"
                                    data-colspan="1" data-width="60">排序</th>
                                <th data-field="" data-formatter="operatorFormatter"
                                    data-align="center" data-colspan="1" data-width="120">操作</th>
                            </tr>
                            </thead>
                        </table>
                    </div>
                    <!-- 列表区域end -->
                </div>
            </div>
        </div>
    </div>

    <!-- 新增/编辑 -->
    <%@include file="aedit_quest_answer.jsp"%>

    <!-- 业务js -->
    <script src="${pageContext.request.contextPath}/ui-static/aplanmis/par/stage/ui-js/stage_question_index.js" type="text/javascript"></script>
</body>
</html>