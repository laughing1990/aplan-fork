
function getDataNullIsZero(val){
    if(val==undefined || val=="undefined" || val==null){
        return 0;
    }else{
        return val;
    }
}
function insertEnter(str,n){
    var len = str.length;
    var strTemp = '';
    if(len > n){
        strTemp = str.substring(0,n);
        str = str.substring(n,len);
        return strTemp+'\n'+insertEnter(str,n);
    }else{
        return str;
    }
}
function xmlxbjs(legendData,xAxisData,seriesDatas) {
    var objId = "xiang_muban_jian";
    var colorData=['#5d91dd','#6bc0d5','#00C062','#ff6b6b'];
    var barWidth = "20";
    var chartsParams={objId:objId,legendData:legendData,xAxisData:xAxisData,seriesDatas:seriesDatas,colorData:colorData,barWidth:barWidth};
    doBarCharts(chartsParams,function () {

    });
}


function mybjs(legendData,xAxisData,seriesDatas) {
    var objId = "mei_yue_ban_jian";
    var colorData=['#5d91dd','#6bc0d5','#00C062','#ff6b6b'];
    var barWidth = "10";
    var chartsParams={objId:objId,legendData:legendData,xAxisData:xAxisData,seriesDatas:seriesDatas,colorData:colorData,barWidth:barWidth};
    doBarCharts(chartsParams,function () {

    });
}


function doBarCharts(chartsParams,onclickFunction){
    var legendData=chartsParams.legendData;
    var xAxisData=chartsParams.xAxisData;
    var seriesDatas=chartsParams.seriesDatas;
    var objId=chartsParams.objId;
    var seriesObj=[],zearVall=[];
    var barWidth=chartsParams.barWidth;
    var xAxisFontSize=14;
    var legendFontSize=15;
    var gridBbottom="5%";
    var tooltipFontSize=18;
    var colorData=[];
    if(!chartsParams.colorData  || chartsParams.colorData.length<=0){
        if(legendData.length==1){
            colorData=['#5d91dd'];
        }else if(legendData.length==2){
            colorData=['#5d91dd','#6bc0d5'];
        }else if(legendData.length==3){
            colorData=['#5d91dd','#6bc0d5','#9d66e8'];
        }else if(legendData.length==4){
            colorData=['#5d91dd','#6bc0d5','#9d66e8','#ff6b6b'];
        }
    }else{
        colorData=chartsParams.colorData;
    }
    if(chartsParams.barWidth){
        barWidth=chartsParams.barWidth;
    }
    if(chartsParams.xAxisFontSize){
        xAxisFontSize=chartsParams.xAxisFontSize;
    }
    var vall=null;
    for(var i=0;i<legendData.length;i++){
        zearVall.push(0);
    }
    for(var i=0;i<seriesDatas.length;i++){
        if(seriesDatas==null || seriesDatas.length<=i-1 || seriesDatas[i]==undefined || seriesDatas[i]==null){
            vall=zearVall;
        }else{
            vall=seriesDatas[i];
        }
        var itemStyle = {
            emphasis: {
               barBorderRadius: 7
            },
           normal: {
               barBorderRadius: 7
           }
        }
        seriesObj.push({name: legendData[i],type: 'bar',data:vall,barWidth: barWidth,itemStyle:itemStyle,label: {normal: {show: true, position: 'top',textStyle: {color: colorData[i], fontSize: xAxisFontSize}}}});
    }
    var pillar1 = echarts.init(document.getElementById(objId));
    var option = {
        color: colorData,
        tooltip : {
            trigger: 'axis',
            textStyle: {
                fontSize: tooltipFontSize
            },
            axisPointer : {            // 坐标轴指示器，坐标轴触发有效
                type : 'shadow'        // 默认为直线，可选为：'line' | 'shadow'
            }
        },
        legend: {
            top: '2%',
            data: legendData,
            textStyle: {
                color: "#abcaf3",
                fontSize: legendFontSize
            },
            itemWidth: 18,
            itemHeight: 18,
            itemGap: 30
        },
        grid: {top: '28%',left: '4%', right: '4%',bottom: gridBbottom,containLabel: true},
        xAxis : [
            {
                type : 'category',
                data : xAxisData,
                axisTick: {
                    show: false
                },
                splitLine: {
                    show: false
                },
                axisLabel: {
                    interval:0,
                    textStyle: {
                        fontSize: xAxisFontSize,
                        color:'#fff'
                    },
                    formatter:function(params) {
                        return insertEnter(params,8);
                    }
                },
                axisLine: {
                    lineStyle: {
                        color: '#575962',
                        width: '1'
                    }
                }
            }
        ],
        yAxis : [
            {
                type : 'value',
                min: 0,

                interval: 20,
                axisTick: {
                    show: true
                },
                splitLine: {
                    show: true
                },
                splitArea: {
                    show: false
                },
                axisLabel: {
                    show: true
                },
                axisLine: {
                    show: true,
                    lineStyle: {
                        color: '#575962',
                        width: '1'
                    }
                }
            }
        ],
        series : seriesObj
    };
    // 使用刚指定的配置项和数据显示图表。
    pillar1.setOption(option);
    if(onclickFunction && onclickFunction!=null && onclickFunction instanceof Function){
        pillar1.on('click', function(param){
            onclickFunction(param);
        });
    }else{
        pillar1.on('click', function(){});
    }
}
