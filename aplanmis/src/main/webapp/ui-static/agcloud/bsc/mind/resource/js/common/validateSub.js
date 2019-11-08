/**
 * 通用验证实现，依赖global.js
 */
if(typeof layui != 'undefined')
{//layui
	$(function()
	{
		layui.form.verify
		({//更多验证规则
			idCard : function(value,item)
			{//身份证号
				if(value) 
				{
					if(value.length == 15)
					{
						if(!/^[1-9]\d{7}((0\d)|(1[0-2]))(([0|1|2]\d)|3[0-1])\d{3}$/i.test(value)) return '请输入15或18位正确的身份证号';
					}
					else if(value.length == 18)
					{
						if(!/^[1-9]\d{5}[1-9]\d{3}((0\d)|(1[0-2]))(([0|1|2]\d)|3[0-1])\d{3}[0-9A-Z]$/i.test(value)) return '请输入15或18位正确的身份证号'; 
					}
					else
						return '请输入15或18位正确的身份证号';
				}
			},
            zzjgdm : function(orgCode,item)
            {   //组织机构代码
                var ret = orgcodevalidate(orgCode);
                if(!ret)
                {
                    return "组织机构代码格式不正确，组织机构代码为8位数字或者拉丁字母+“-”+1位校验码，并且字母必须大写！";
                }
            },
            taxCode : function(taxpayerId,item)
            {   //组织机构代码
                //（15位包括地区编码6位+组织机构代码9位）
                //纳税人识别号校验是否合法
                if($.trim(taxpayerId) == ''){
                    return "请输入纳税人识别号 ！";
                }else if($.trim(taxpayerId) != ''){
                    var addressCode = taxpayerId.substring(0,6);
                    // 校验地址码
                    var check = checkAddressCode(addressCode);
                    if(!check) {
                        return "请输入正确的纳税人识别号 (地址码)！";
                    }else{
                        // 校验组织机构代码
                        var orgCode = taxpayerId.substring(6,9);
                        check = orgcodevalidate(orgCode);
                        if(!check){
                            return "请输入正确的纳税人识别号(组织机构代码) ！";
                        }
                    }
                }
            },
            busCode : function(value,item)
            {//工商注册号
                var ret=false;
                if(value.length===15){//15位
                    var sum=0;
                    var s=[];
                    var p=[];
                    var a=[];
                    var m=10;
                    p[0]=m;
                    for(var i=0;i<value.length;i++){
                        a[i]=parseInt(value.substring(i,i+1),m);
                        s[i]=(p[i]%(m+1))+a[i];
                        if(0==s[i]%m){
                            p[i+1]=10*2;
                        }else{
                            p[i+1]=(s[i]%m)*2;
                        }
                    }
                    if(1==(s[14]%m)){//营业执照编号正确!
                        ret=true;
                    }else{//营业执照编号错误!
                        ret=false;
                    }
                }else{//营业执照格式不对，必须是15位数的！
                    ret=false;
                }
                if(!ret)
                {
                    return '请输入正确的工商注册号';
                }
            },
            shtyxydm : function(value,item)
            {   //社会统一信用代码
                var patrn = /^[0-9A-Z]+$/;
                //18位校验及大写校验
                if ((value.length != 18) || (patrn.test(value) == false))
                {
                    return "请输入有效的统一社会信用编码";
                }
                else
                {
                    var Ancode;//信用代码/税号的每一个值
                    var Ancodevalue;//信用代码/税号每一个值的权重
                    var total = 0;
                    var weightedfactors = [1, 3, 9, 27, 19, 26, 16, 17, 20, 29, 25, 13, 8, 24, 10, 30, 28];//加权因子
                    var str = '0123456789ABCDEFGHJKLMNPQRTUWXY';
                    //不用I、O、S、V、Z
                    for (var i = 0; i < value.length - 1; i++)
                    {
                        Ancode = value.substring(i, i + 1);
                        Ancodevalue = str.indexOf(Ancode);
                        total = total + Ancodevalue * weightedfactors[i];
                        //权重与加权因子相乘之和
                    }
                    var logiccheckcode = 31 - total % 31;
                    if (logiccheckcode == 31)
                    {
                        logiccheckcode = 0;
                    }
                    var Str = "0,1,2,3,4,5,6,7,8,9,A,B,C,D,E,F,G,H,J,K,L,M,N,P,Q,R,T,U,W,X,Y";
                    var Array_Str = Str.split(',');
                    logiccheckcode = Array_Str[logiccheckcode];

                    var checkcode = value.substring(17, 18);
                    if (logiccheckcode != checkcode)
                    {
                        return "请输入有效的统一社会信用编码";
                    }
                }
            },
			length : function(value,item)
			{//长度范围，例：lay-verify-length="[1,3]"
				if(value)
				{
					var params = eval($(item).attr('lay-verify-length'));
					if(Array.isArray(params) && params.length == 2)
					{
						var min = params[0],max = params[1];
						if(value && (value.length < min || value.length > max)) return '长度应为' + min + '到' + max + '个字符';
					}
				}
			},
			maxLength : function(value,item)
			{//最大长度，例：lay-verify-maxLength="[50]"
				if(value)
				{
					var params = eval($(item).attr('lay-verify-maxLength'));
					if(Array.isArray(params) && params.length == 1)
					{
						var max = params[0];
						if(value && value.length > max) return '最大长度' + max + '个字符';
					}
				}
			},
			reg : function(value,item)
			{//正则表达式，例：lay-verify-reg="['/^[0-9a-zA-Z_\u4e00-\u9fa5]+$/','只接受数字、字母、中文、下划线']"
				if(value)
				{
					var params = eval($(item).attr('lay-verify-reg'));
					if(Array.isArray(params) && params.length == 2)
					{
						var reg = params[0],msg = params[1];
						if(value && !eval(reg).test(value)) return msg;
					}
				}
			},
            posInt : function(value,item)
            {//正则表达式，例：lay-verify-reg="['/^[0-9a-zA-Z_\u4e00-\u9fa5]+$/','只接受数字、字母、中文、下划线']"
                if(value)
                {
                    console.log(value)
                    var reg =/^(0|[1-9][0-9]*)$/,msg = '请输入正整数';
                    if(!reg.test(value)){
                        return msg
                    };
                }
            },
            number : function(value,item)
            {//正则表达式，例：lay-verify-reg="['/^[0-9a-zA-Z_\u4e00-\u9fa5]+$/','只接受数字、字母、中文、下划线']"
                if(value)
                {
                    var reg = /^\d+(\.\d+)?$/,msg = '请输入数字';
                    if(!reg.test(value)){
                        return msg
                    };
                }
            },
			phone : function(value,item)
            {//正则表达式，例：lay-verify-reg="['/^[0-9a-zA-Z_\u4e00-\u9fa5]+$/','只接受数字、字母、中文、下划线']"
                if(value)
                {
                    //
                    var reg = /(^(\d{3,4}-)?\d{7,8})$|(1[0-9][0-9]{9})$/,msg = '请输入正确的号码格式';
                    if(!reg.test(value)){
                        return msg
                    };
                }
            },
            mobile : function(value,item)
            {//正则表达式，例：lay-verify-reg="['/^[0-9a-zA-Z_\u4e00-\u9fa5]+$/','只接受数字、字母、中文、下划线']"
                if(value)
                {

                    var reg =/^0?(13[0-9]|15[012356789]|17[013678]|18[0-9]|14[57])[0-9]{8}$/,msg = '请输入正确的号码格式';
                    if(!reg.test(value)){
                        return msg
                    };
                }
            },
            telphone : function(value,item)
            {//正则表达式，例：lay-verify-reg="['/^[0-9a-zA-Z_\u4e00-\u9fa5]+$/','只接受数字、字母、中文、下划线']"
                if(value)
                {

                    var reg = /^((0\d{2,3})-)(\d{7,8})(-(\d{3,}))?$/,msg = '请输入正确的号码格式，格式为区号-电话号码';
                    if(!reg.test(value)){
                        return msg
                    };
                }
            },
            email : function(value,item)
            {//正则表达式，例：lay-verify-reg="['/^[0-9a-zA-Z_\u4e00-\u9fa5]+$/','只接受数字、字母、中文、下划线']"
                if(value)
                {

                    var reg = /^([a-zA-Z0-9_-])+@([a-zA-Z0-9_-])+(.[a-zA-Z0-9_-])+/,msg = '请输入正确的邮箱地址';
                    if(!reg.test(value)){
                        return msg
                    };
                }
            },
            date : function(value,item)
            {//正则表达式，例：lay-verify-reg="['/^[0-9a-zA-Z_\u4e00-\u9fa5]+$/','只接受数字、字母、中文、下划线']"
                if(value)
                {

                    var reg = /^(\d{4})-(\d{2})-(\d{2})$/,msg = '请输入正确的日期';
                    if(!reg.test(value)){
                        return msg
                    };
                }
            },
			unique : function(value,item)
			{//服务端验证唯一性，例：lay-verify-unique="['user!checkUniqueUser.action','userCode','userCode']"
				if(value)
				{
					var params = eval($(item).attr('lay-verify-unique'));
					
					if(Array.isArray(params) && params.length == 3)
					{
						var url = params[0],postName = params[1],inputId = params[2];
						if(value)
						{
							if(inputId
								&& document.getElementById(inputId)
								&& document.getElementById(inputId).defaultValue == value) return;
							else
							{
								var data = {};
								if(typeof postName == 'string')
								{
									data[postName] = value;
								}

                                var isValid = '';
                                if(typeof globalSyncPost == 'function')
                                {
                                    globalSyncPost(url, data, function (result) {
                                        isValid = result.data;
                                    });
                                    if (isValid != 'true') return '已存在相同数据，请检查';
                                }
                                else
                                    return '本功能需要global.js支持';
                            }
						}
					}
				}
			},
            postCode : function(value,item)
			{//正则表达式,例：lay-verify-reg="['/^[1-9]\d{5}(?!\d)$/','检查中国邮政编码']"
                if(value)
                {
                    var reg = /^[1-9]\d{5}(?!\d)$/,msg = '请输入正确的邮政编码';
                    if(!reg.test(value)){
                        return msg
                    };
                }
			}
		});

        function msg(msg,title,timeout)
        {
            if(title)
            {
                layer.open
                ({
                    type		: 1,
                    offset		: 'rb',//右下
                    title		: (title?title:'提示'),
                    content		: '<div style="padding:20px 20px;">'+ msg +'</div>',
                    shade		: 0,
                    time		: (timeout?timeout:3000),
                    anim 		: 2//从最底部往上滑入
                });
            }
            else
                layer.msg(msg,{time:(timeout?timeout:3000)});
        }

		layui.form.render();
		layui.form.on('submit(submit)', function(data)
		{
			var f = $('form:first');
			if(typeof validateFun == 'function')
			{//额外验证函数
				var extraResult = validateFun(data);
				if(extraResult == false) return false;
			}
			var mask = layui.layer.msg('处理中，请稍侯...',
			{
				icon		: 16,
				shade		: [0.5, '#f5f5f5'],
				scrollbar	: false,
				time		: 100 * 1000,
				success		: function()
				{
                    var action = data.form.action;
                    function resFun(response){
                        if(response.code == 1)
                        {
                            top.msg('操作成功');
                            if(typeof successfn === 'function'){
                            	successfn(response.data)
							}else{
                                hideData();
							}
                        }
                        else if(response.msg)
                            top.msg(response.msg);
                        else
                            top.msg('操作失败');

                        layui.layer.close(layui.layer.index);
                        layui.layer.close(mask);
					}
					if(f.hasClass('isFormData')){ //如果form中有数组提交表单，序列化表单
                        var datas = $(data.form).serialize();
                        globalAsyncPost(action,datas,function(response){resFun(response);});
                    }
                    else if(f.hasClass('syncFrom')){
                        var datas = data.field;
                        globalPost(action,datas,false,'application/json',function(response){resFun(response);});
                    }
                    else{
                        var datas = data.field;
                        globalAsyncPostJson(action,datas,function(response){resFun(response);});
					}
				}
			});
			return false;
		});
	})
}


function orgcodevalidate(orgCode){
    var ret=false;
    var codeVal = ["0","1","2","3","4","5","6","7","8","9","A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z"];
    var intVal =    [0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30,31,32,33,34,35];
    var crcs =[3,7,9,10,5,8,4,2];
    if(!(""==orgCode) && orgCode.length==10){
        var sum=0;
        for(var i=0;i<8;i++){
            var codeI=orgCode.substring(i,i+1);
            var valI=-1;
            for(var j=0;j<codeVal.length;j++){
                if(codeI==codeVal[j]){
                    valI=intVal[j];
                    break;
                }
            }
            sum+=valI*crcs[i];
        }
        var crc=11- (sum%11);

        switch (crc){
            case 10:{
                crc="X";
                break;
            }default:{
            break;
        }
        }
        if(crc==orgCode.substring(9)){
            ret=true;
        }else{
            ret=false;
        }
    }else if(""==orgCode){
        ret=false;
    }else{
        ret=false;
    }

    return ret;
}

function checkAddressCode(addressCode) {
    var provinceAndCitys = {
        11: "北京", 12: "天津", 13: "河北", 14: "山西", 15: "内蒙古", 21: "辽宁", 22: "吉林", 23: "黑龙江",
        31: "上海", 32: "江苏", 33: "浙江", 34: "安徽", 35: "福建", 36: "江西", 37: "山东", 41: "河南", 42: "湖北", 43: "湖南", 44: "广东",
        45: "广西", 46: "海南", 50: "重庆", 51: "四川", 52: "贵州", 53: "云南", 54: "西藏", 61: "陕西", 62: "甘肃", 63: "青海", 64: "宁夏",
        65: "新疆", 71: "台湾", 81: "香港", 82: "澳门", 91: "国外"
    };
    var check = /^[1-9]\d{5}$/.test(addressCode);
    if (!check) return false;
    if (provinceAndCitys[parseInt(addressCode.substring(0, 2))]) {
        return true;
    } else {
        return false;
    }
}