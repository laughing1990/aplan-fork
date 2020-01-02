var vm = new Vue({
    el:'#regulation',
    data:{
        ctx:ctx,
        active:0,
        departName:'唐山市政府',
        approvalBureauData:[
            {
                id:'1',
                itemName:'（唐港城办发﹝2019﹞9号）关于印发《唐山市工程建设项目“多评合一”实施办法》的通知（印发版）',
                url:'mall/file/tangshan/ssxz/approvalBureau/（唐港城办发﹝2019﹞9号）关于印发《唐山市工程建设项目“多评合一”实施办法》的通知（印发版）.pdf'
            },
            {
                id:'2',
                itemName:'（唐港城办发﹝2019﹞10号）关于进一步规范市中心区市政设施建设类审批、工程建设涉及城市绿地树木审批事项合并办理工作的通知（印发稿）',
                url:'mall/file/tangshan/ssxz/approvalBureau/（唐港城办发﹝2019﹞10号）关于进一步规范市中心区市政设施建设类审批、工程建设涉及城市绿地树木审批事项合并办理工作的通知（印发稿）.pdf'
            },
        ],
        governmentData:[
            {
                id:'1',
                itemName:'（唐港城办发﹝2019﹞8号）关于印发《政府投资房屋建筑类项目审批流程图》等11类工程建设项目审批流程图的通知（印发版）终稿',
                url:'mall/file/tangshan/ssxz/government/（唐港城办发﹝2019﹞8号）关于印发《政府投资房屋建筑类项目审批流程图》等11类工程建设项目审批流程图的通知（印发版）终稿.pdf',
            },
        ],
        houseData:[
            {
                id:'1',
                itemName:'（唐港城办发﹝2019﹞13号）关于印发《全面推进工程建设项目验收阶段“多测合一”改革的实施意见》的通知',
                url:'mall/file/tangshan/ssxz/house/（唐港城办发﹝2019﹞13号）关于印发《全面推进工程建设项目验收阶段“多测合一”改革的实施意见》的通知.pdf',
            },
            {
                id:'2',
                itemName:'（唐港城办发﹝2019﹞14号）关于进一步加强工程建设项目施工图技术审查工作的通知',
                url:'mall/file/tangshan/ssxz/house/（唐港城办发﹝2019﹞14号）关于进一步加强工程建设项目施工图技术审查工作的通知.pdf'
            },
            {
                id:'3',
                itemName:'（唐港城办发﹝2019﹞15号）关于进一步规范工程建设项目施工图设计文件联合审查有关工作的通知',
                url:'mall/file/tangshan/ssxz/house/（唐港城办发﹝2019﹞15号）关于进一步规范工程建设项目施工图设计文件联合审查有关工作的通知.pdf'
            },
            {
                id:'4',
                itemName:'（唐港城办发﹝2019﹞16号）关于印发《唐山市工程建设项目施工预许可审批实施细则》的通知',
                url:'mall/file/tangshan/ssxz/approvalBureau/（唐港城办发﹝2019﹞16号）关于印发《唐山市工程建设项目施工预许可审批实施细则》的通知.pdf',
            },
            {
                id:'5',
                itemName:'（唐港城办发﹝2019﹞17号）关于印发《唐山市工程建设项目联合验收工作规程》的通知',
                url:'mall/file/tangshan/ssxz/house/（唐港城办发﹝2019﹞17号）关于印发《唐山市工程建设项目联合验收工作规程》的通知.pdf',
            }
        ],
        plannimgBureauData:[
            {
                id:'1',
                itemName:'（唐港城办发﹝2019﹞11号）关于印发《唐山市建设工程规划设计方案联合审查实施细则》的通知（印发稿）',
                url:'mall/file/tangshan/ssxz/planningBureau/（唐港城办发﹝2019﹞11号）关于印发《唐山市建设工程规划设计方案联合审查实施细则》的通知（印发稿）.pdf'
            },
            {
                id:'2',
                itemName:'（唐港城办发﹝2019﹞12号）关于印发《唐山市告知承诺制办理建设工程规划预许可管理办法（试行）》的通知（印发稿）',
                url:'mall/file/tangshan/ssxz/planningBureau/（唐港城办发﹝2019﹞12号）关于印发《唐山市告知承诺制办理建设工程规划预许可管理办法（试行）》的通知（印发稿）.pdf'
            },
        ],

        tableData:[
            {
                id:'1',
                itemName:'（唐港城办发﹝2019﹞9号）关于印发《唐山市工程建设项目“多评合一”实施办法》的通知（印发版）',
                url:'mall/file/tangshan/ssxz/approvalBureau/（唐港城办发﹝2019﹞9号）关于印发《唐山市工程建设项目“多评合一”实施办法》的通知（印发版）.pdf'
            },
            {
                id:'2',
                itemName:'（唐港城办发﹝2019﹞10号）关于进一步规范市中心区市政设施建设类审批、工程建设涉及城市绿地树木审批事项合并办理工作的通知（印发稿）',
                url:'mall/file/tangshan/ssxz/approvalBureau/（唐港城办发﹝2019﹞10号）关于进一步规范市中心区市政设施建设类审批、工程建设涉及城市绿地树木审批事项合并办理工作的通知（印发稿）.pdf'
            },
        ],
        departmentData:[
            {
                id:1,
                name:'唐山市政府',
            },
            {
                id:2,
                name:'唐山市行政审批局',
            },
            {
                id:3,
                name:'唐山市自然资源和规划局',
            },
            {
                id:4,
                name:'唐山市住建局',
            },
        ]
    },
    methods:{
        changeOrg:function (item,index) {
            var ts = this;

            ts.active = index;
            ts.departName = ts.departmentData[index].name
            switch (index) {
                case 0:
                    ts.tableData = ts.approvalBureauData

                    break;
                case 1:
                    ts.tableData = ts.governmentData

                    break;
                case 2:
                    ts.tableData = ts.houseData

                    break;
                case 3:
                    ts.tableData = ts.plannimgBureauData

                    break;
            }

        }
    }
})