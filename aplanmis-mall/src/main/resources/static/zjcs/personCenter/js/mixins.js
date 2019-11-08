// add(0) --补零
function add0(m) {
    return m < 10 ? '0' + m : m
};

var mixins = {
    data: function () {
        return {
            // 页面loading
            mloading: false,

            // 页面app高度和内容content高度
            appH: '',
            contentH: '',
            // 是否展示默认的头像
            showNormalPhoto: false,
            // 头像的url前缀
            imgctx: ctx,

            // 业主相关
            // 项目详情相关
            // 当前展示项目详情行
            curShowProjectDetailRow: {},
        }
    },
    created: function () {

    },
    methods: {
        // 编辑页的展开一项
        expandProjectInfoItem: function ($event) {
            var _mod = $($event.target);
            _mod.toggleClass('active');
        },
        // 返回顶部
        backToTop: function () {
            $('body').animate({
                scrollTop: 0
            }, 1000);
        },
        // 模块主体的最小高
        mContH: function () {
            var ts = this;
            var _minH = $(window).height() < 500 ? 500 : $(window).height();
            return _minH + 'px';
        },
        // 加载头像失败
        errorLoadPhoto: function () {
            this.showNormalPhoto = true;
        },

        // 业主个人中心相关
        // 业主-项目列表到详情操作
        // 展示详情
        showProjectDetail: function (row) {
            var ts = this;
            // 获取当前模块id
            var id = $(".box").attr("id");

            ts.isProjectServiceDetail = true; //项目详情容器为true
            ts.curShowProjectDetailRow = row;
            var url = "";
            if (document.location.protocol == "file:") {
                url = './components/owner/detailAndContract.html';
            } else {
                url = ctx + '/supermarket/main/detailAndContract.html';
            }

            $.get(url, function (result) {
                $('.project-detail-pandel-'+id).html(result);
            });
        },
        // 返回列表-提供给项目详情模块内部使用
        returnProjectList: function () {
            var ts = this;
            ts.showList=0;
            $('.project-detail').html('');
            ts.isProjectServiceDetail = false; //项目详情容器为false
            ts.curShowProjectDetailRow = {};
        },
    },
    filters: {
        dateFormat: function (date, format) {
            if (!date) return '暂无';
            var time = new Date(date);
            var y = time.getFullYear();
            var m = time.getMonth() + 1;
            var d = time.getDate();
            var h = time.getHours();
            var mm = time.getMinutes();
            var s = time.getSeconds();
            if (format == 'y-m-d') {
                return y + '-' + add0(m) + '-' + add0(d)
            }
            return y + '-' + add0(m) + '-' + add0(d) + ' ' + add0(h) + ':' + add0(mm) + ':' + add0(s);
        },
    },
    directives: {
        // el-select的加载更多
        loadmore: {
            bind: function (el, binding) {
                // 获取element-ui定义好的scroll盒子
                var SELECTWRAP_DOM = el.querySelector('.el-select-dropdown .el-select-dropdown__wrap')
                SELECTWRAP_DOM.addEventListener('scroll', function () {

                    var CONDITION = this.scrollHeight - this.scrollTop <= this.clientHeight
                    if (CONDITION) {
                        binding.value()
                    }
                })
            }
        }
    },
};