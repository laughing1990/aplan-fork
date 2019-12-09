var myCertificateLibs = (function (window) {

    var vm = new Vue({
        data:{
            tableLoading:false,
            c:'c',
            u:'u',
            p:'p',
            cPageNum:1,
            cPageSize:10,
            uPageNum:1,
            uPageSize:10,
            pPageNum:1,
            pPageSize:10,
            keyword:''
        },
        create:function(){
            this.getCerList(this.c,this.cPageNum,this.cPageSize);
         /*   this.getCerList(this.u,this.);
            this.getCerList(this.p);*/
        },
        methods:{
            // 获取系统证照实例列表
            getCerList:function(certHolder,pageNum,pageSize){
                var ts = this;
                ts.tableLoading = true;
                request('', {
                    url: ctx + '/aea/cert/getCertintListByCertHolder',
                    type: 'get',
                    data:{
                        certHolder:certHolder,// 证照所属，c表示企业，u表示个人，p表示项目
                        pageNum:pageNum,
                        pageSize:pageSize,
                        keyword:ts.keyword,
                    }
                }, function (res) {
                    ts.tableLoading = false;
                    if (res) {
                        ts.matFileList = res.list;
                        ts.total = res.total;
                    }
                }, function () {
                    ts.tableLoading = false;
                });
            },
        }
    })
})(typeof window === "undefined"?this:window);