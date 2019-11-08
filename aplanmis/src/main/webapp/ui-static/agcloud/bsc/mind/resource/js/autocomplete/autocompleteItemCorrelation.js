/**
A jQuery plugin for search hints

Author: Yu Qiang

 PS:查询条件名称与字段名称不符，
 	查询以wordName查询
 	结果以assistantName代替原wordName
*/

(function($) {
	$.fn.autocomplete = function(params) {
		//Selections
		var currentSelection = -1;
		var currentProposals = [];
		
		//Default parameters
		params = $.extend({
			type: 0,										//值为1时从服务器获取数据
			hints: [],
			async: {url: '',param: {}},						//值为1时给的获取服务器数据的url
			wordName: '',									//查找的字段的名称--参数名
			assistantName: false,							//查找的字段的名称--字段名
			assistantNameReal: false,						//辅助显示的字段的名称
            assistantNameReal2: false,						//辅助显示的字段的名称
			keepValue: false,								//当没有查找到数据时，输入的值是否保留
			detail: false,									//查到的数据详细功能是否开启
			ignoreWs: true,									//忽略空格（默认忽略）
			showCount: 5,									//每页显示的条数
			showIcon: false,								//是否显示图标
			showLc: false,									//分页组件后面的组件，默认没有
			showLrc: false,									//右下角组件 默认不显示
			defaultIcon: 'static/sldj/images/touxiang.png',	//默认头像地址
			interval: 1000,									//输入完毕时的间隔
			isEmptyStrQuery: false,							//当输入的值为空时，是否查询(type=1时有效果)
			placeholder: $(this).attr('placeholder'),
			width: 200,
			height: 46,
			name: $(this).attr('name'),
			value: $(this).attr("bva"),
			intervalHeight: 2,
			showButton: true,
			buttonText: '查询',

			/**
			 * 当结果集为空时的函数
			 * @param page_element 显示结果的html元素
			 * @param input	搜索框jquery元素
			 * @param params 全局参数
			 */
			onResultIsEmpty: function(page_element,input,params){
				var msg_element = $('<span></span>').html('未查找到关键词<label style="color:red;">“'+input.val()+'”</label>的相关数据');
				page_element.append(msg_element);
				if(params.showLrc){
					var showLrc_element = $('<span></span>')
						.addClass('pull-right')
						.append(params.showLrc);
					page_element.append(showLrc_element);
				}
			},

			/**
			 * 当li选中时的事件
			 * @param text
			 * @param li
			 */
			onSubmit: function(text,data,obj){

			},


			/**
			 * 当搜索框是去焦点的时候
			 */
			onBlur: function(){


			},

			/**
			 * 当搜索框值改变的时候
			 */
			onChange: function(){

			}
		}, params);
		
		//Build messagess
		this.each(function() {
			//Container
			var searchContainer = $('<div></div>')
				.addClass('autocomplete-container')
				.css('height', params.height * 2);	

			//Text input		
			var input = $('<input type="text" autocomplete="off">')
				.attr('placeholder', params.placeholder)
				.attr('name', params.name)
				.val(params.value)
				.addClass('autocomplete-input')
				.css({
					'width' : params.width,
					'height' : params.height
				});
			
			if(params.showButton){
				input.css('border-radius', '3px 0 0 3px');
			}

			params.async.param = {
				wordName: params.wordName,
                limit: params.showCount
			};

			//Proposals
			var proposals = $('<div></div>')
				.addClass('proposal-box')
				.css('width', params.width + 18)
				.css('top', input.height() + params.intervalHeight);
			var proposalList = $('<ul></ul>')
				.addClass('proposal-list');
			proposals.append(proposalList);
			
			input.keydown(function(e) {
				switch(e.which) {
					case 38: // Up arrow
					e.preventDefault();
					$('ul.proposal-list li').removeClass('selected');
					if((currentSelection - 1) >= 0){
						currentSelection--;
						$( "ul.proposal-list li:eq(" + currentSelection + ")" )
							.addClass('selected');
					} else {
						currentSelection = -1;
					}
					break;
					case 40: // Down arrow
					e.preventDefault();
					if((currentSelection + 1) < currentProposals.length){
						$('ul.proposal-list li').removeClass('selected');
						currentSelection++;
						$( "ul.proposal-list li:eq(" + currentSelection + ")" )
							.addClass('selected');
					}
					break;
					case 13: // Enter
						if(currentSelection > -1){
							var text = $( "ul.proposal-list li:eq(" + currentSelection + ")").find('.proposal');
							input.val(text.find("span:first").text()).addClass('active');
							proposalList.empty();
							var data = JSON.parse(text.attr('value'));
							params.onSubmit(input.val(),data,input);
							input.parents(".form-control").css({'z-index':888});
							input.blur()
						}
						break;
					case 27: // Esc button
						currentSelection = -1;
						proposalList.empty();
						input.val('');
						break;
					default:
						break;
				}
			});

			/**
			 * 第一页
			 * @param page
			 */
			var firstPage = function(page){
				if(page.currentPage == 1){
					return true;
				}else {
					params.async.param.page = 1;
					proposalList.empty();
					asyncFunction();
				}
			}

			/**
			 * 上一页
			 * @param page
			 */
			var prevPage = function(page){
				if(page.currentPage == 1){
					return true;
				}else{
					params.async.param.page = page.currentPage - 1;
					proposalList.empty();
					asyncFunction();
				}
			}

			/**
			 * 下一页
			 * @param page
			 */
			var nextPage = function(page){
				if(page.currentPage == page.totalPage){
					return true;
				}else{
					params.async.param.page = page.currentPage + 1;
					proposalList.empty();
					asyncFunction();
				}
			}

			/**
			 * 最后一页
			 * @param page
			 */
			var finallyPage = function(page){
				if(page.currentPage == page.totalPage){
					return true;
				}else {
					params.async.param.page = page.totalPage;
					proposalList.empty();
					asyncFunction();
				}
			}

			function pageCount (totalnum,limit){
                return totalnum > 0 ? ((totalnum < limit) ? 1 : ((totalnum % limit) ? (parseInt(totalnum / limit) + 1) : (totalnum / limit))) : 0;
            }

			function transformPager(response){
				if(response.page){
					return response.page;
				}else{
					var page = {};
                    if(response.totalCount > 0){
                    	page.currentPage = response.start;
                    	page.totalResult = response.totalCount;
                        page.totalPage = pageCount(response.totalCount, params.showCount);
                    }
                    return page;
				}
			}

			/**
			 * 异步查询数据处理函数
			 */
			function asyncFunction(){
                globalAsyncPost(params.async.url,params.async.param,function(response){
					if(response.code == '1'){
						params.hints = response.data;
						var page = transformPager(response);
						//显示分页组件
						var page_element = $('<li class="proptotal"></li>');
						//有数据时把数据显示到li中
						if(page.totalPage > 0){
							for(var i = 0; i < params.hints.length; i++){
								var hints_data = params.hints[i];
								//将要查询的字段放在检索显示li中
								//将相应的name存到全局数组中
								currentProposals.push(hints_data[params.assistantName]);
								var element = $('<li></li>');
								var swn = hints_data[params.assistantName];
								swn = swn.length > 40 ? swn.substr(0, 40) + '...' : swn;
								var element_xx = $('<span></span>')
									.html('<span style="overflow-x: hidden;" title="' + hints_data[params.assistantName] + '">'+swn+'</span>')
									.attr('value',JSON.stringify(hints_data))
									.addClass('proposal')
									.click(function(){
										input.val($(this).find("span:first").attr('title')).addClass('active');
										proposalList.empty();
										var data = JSON.parse($(this).attr('value'));
										params.onSubmit(input.val(),data,input);
										input.parents(".form-control").css({'z-index':888});
									})
									.mouseenter(function() {
										$(this).addClass('selected');
									})
									.mouseleave(function() {
										$(this).removeClass('selected');
									});
                                if(params.assistantNameReal){
                                    var pname = hints_data[params.assistantNameReal] ? hints_data[params.assistantNameReal] : '无'
                                    element_xx.append('<span style="color: lightgrey;margin-left: 20px;" title="' + pname + '">' + pname + '</span>');
                                }
                                if(params.assistantNameReal2){
                                    var pname = hints_data[params.assistantNameReal2] ? hints_data[params.assistantNameReal2] : '无'
                                    element_xx.append('<span style="color: lightgrey;margin-left: 20px;" title="' + pname + '">' + pname + '</span>');
                                }
								if(params.showIcon){
									//如果头像为空的话，默认一个图片
									var img_url = hints_data[params.showIcon] || params.defaultIcon;
									img_url = img_url ? getRootPath() + img_url : params.defaultIcon;
									element_xx.prepend('<img style="padding-right:10px;width:42px;height:32px;" src="'
									+ img_url
									+ '" onerror="this.src=\''
									+ params.defaultIcon
									+'\'"/>');
								}
								element.append(element_xx)
								if(params.detail){
									var detail_dom = $('<span></span>')
										.addClass('pull-right')
										.append(params.detail.clone(true))
									element.append(detail_dom);
								}
								proposalList.append(element);
							}

							//如果页数大于1，则显示下一页上一页按钮
							if(page.totalPage > 1){
								var pnbtn_element = $('<span style="padding-right:20px;"></span>')
									.html('<div class="layui-btn-group"></div>');
								var firstPageButton = $('<button type="button" class="layui-btn layui-btn-primary layui-btn-xs">第一页</button>')
									.click(function(){
										firstPage(page);
									});
								pnbtn_element.append(firstPageButton);
								var prevButton = $('<button type="button" class="layui-btn layui-btn-primary layui-btn-xs">上一页</button>')
									.click(function(){
										prevPage(page);
									});
								pnbtn_element.append(prevButton);
								var nextButton = $('<button type="button" class="layui-btn layui-btn-primary layui-btn-xs">下一页</button>')
									.click(function(){
										nextPage(page);
									});
								pnbtn_element.append(nextButton);
								var finallyPageButton = $('<button type="button" class="layui-btn layui-btn-primary layui-btn-xs">最后一页</button>')
									.click(function(){
										finallyPage(page);
									});
								pnbtn_element.append(finallyPageButton);
								page_element.append(pnbtn_element);
							}
							var total_element = $('<span style="cursor: default"></span>')
								.html('第<label style="color:red;">'+page.currentPage
								+ '</label>页/共<label style="color:red;">' + page.totalPage + '</label>页,共<label style="color:red;">' + page.totalResult + '</label>条数据&nbsp;');
							page_element.append(total_element);
							if(params.showLc){
								page_element.append(params.showLc);
							}
							if(params.showLrc){
								var showLrc_element = $('<span></span>')
									.addClass('pull-right')
									.append(params.showLrc);
								page_element.append(showLrc_element);
							}
							input.blur();
						}else{
							params.onResultIsEmpty(page_element,input,params);
							input.removeClass('active');
						}
						if(proposalList.find('.proptotal').length == 0){
							proposalList.append(page_element);
						}
						//第一次点击初始化
						input.parents(".form-control").css({'z-index':999});
						input.focus();
					}else{
						console.error('检索服务出现异常，请联系管理员):');
					}
				});
			}

			var autocomplete_timer;
			//change paste keyup 不要用这个事件，这个在值改变的时候也会触发，导致两次事件冒泡
			input.bind("keyup", function(e){
				if(!params.async){
					return true;
				}
				//如果定时器存在，则清空之前的
				if(autocomplete_timer){
					window.clearTimeout(autocomplete_timer);
				}
				autocomplete_timer = window.setTimeout(function(){
					if(e.which != 27
							&& e.which != 38 && e.which != 40){
						currentProposals = [];
						currentSelection = -1;
						proposalList.empty();
						if(1 == params.type){ //url获取数据
							//当空字符串异步查询时，判断
							if(params.isEmptyStrQuery && $.trim(input.val()).length == 0) return true;
							params.async.param.word = input.val();
                            params.async.param[params.async.param.wordName] = input.val();
							if(params.ignoreWs){
								params.async.param.word = trim(input.val());
                                params.async.param[params.async.param.wordName] = trim(input.val());
							}
							asyncFunction();
						}else{
							if(input.val() != ''){
								var word = "^" + input.val() + ".*";
								//proposalList.empty();
								for(var test in params.hints){
									if(params.hints[test].match(word)){
										currentProposals.push(params.hints[test]);
										var element = $('<li></li>')
											.html(params.hints[test])
											.addClass('proposal')
											.click(function(){
												input.val($(this).html()).addClass('active');
												proposalList.empty();
												params.onSubmit(input.val(),null,input);
												input.parents(".form-control").css({'z-index':888});
											})
											.mouseenter(function() {
												$(this).addClass('selected');
											})
											.mouseleave(function() {
												$(this).removeClass('selected');
											});
										proposalList.append(element);
									}
								}

								var page_element = $('<li class="proptotal"></li>');
								//显示分页组件
								if(currentProposals.length > 0){
									var total_element = $('<span class="pull-right"></span>')
										.html('共查找到<label style="color:red;">' + currentProposals.length + '</label>条数据&nbsp;');
									page_element.append(total_element);
									var pnbtn_element = $('<span class="pull-left"></span>')
										.html('<div class="btn-group">'
										+ '<button type="button" class="btn btn-white btn-sm" onclick=""><i class="ace-icon fa fa-angle-left"></i></button>'
										+ '<button type="button" class="btn btn-white btn-sm"><i class="ace-icon fa fa-angle-right"></i></button>'
										+ '</div>');
									page_element.append(pnbtn_element);
									input.blur();
								}else{
									params.onResultIsEmpty(page_element,input,params);
									input.removeClass('active');
								}
								if(proposalList.find('.proptotal').length == 0){
									proposalList.append(page_element);
								}
								//第一次点击初始化
								input.parents(".form-control").css({'z-index':999});
							}else{
								input.parents(".form-control").css({'z-index':888});
								input.removeClass('active');
							}
							input.focus();
						}
					}
				},params.interval)
			});

			var autocomplete_blur_timer;
			input.blur(function(e){
				if(autocomplete_blur_timer){
					window.clearTimeout(autocomplete_blur_timer);
				}
				var interval = 200;
				autocomplete_blur_timer = window.setTimeout(function(){
					currentSelection = -1;
					if(!input.hasClass('active') && currentProposals.length == 0){
						if(params.keepValue){

						}else{
							input.val("");
						}
						input.parents(".form-control").css({'z-index':888});
					}
					if(currentProposals.length == 0){
						proposalList.empty();
						input.parents(".form-control").css({'z-index':888});
					}
					//proposalList.empty();
					params.onBlur();
				},interval);
			});

			input.change(function(e){
				currentSelection = -1;
				//proposalList.empty();
				params.onChange();
			});
			
			searchContainer.append(input);
			searchContainer.append(proposals);		
			
			if(params.showButton){
				//Search button
				var button = $('<div></div>')
					.addClass('autocomplete-button')
					.html(params.buttonText)
					.css({
						'height': params.height + 2,
						'line-height': params.height + 2 + 'px'
					})
					.click(function(){
						proposalList.empty();
						params.onSubmit(input.val(),null,input);
					});
				searchContainer.append(button);	
			}
	
			$(this).append(searchContainer).css({'z-index':888});
			
			if(params.showButton){
				//Width fix
				searchContainer.css('width', params.width + button.width() + 50);
			}
		});

		return this;
	};
    function trim(str){
        return str.replace(/(^\s+)|(\s+$)/g,"").replace(/\s/g,"");
    }
})(jQuery);