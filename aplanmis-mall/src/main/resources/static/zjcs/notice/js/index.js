
var vm = new Vue({
    el: '#app',
    data: {
      loading:false,

      fullHeight:document.documentElement.clientHeight,
      treeLeftHeight:document.documentElement.clientHeight-200,
      noticeList:[{
        name:'关于印发《关于建立唐山市行政审批中介服务超市的工作方案》的通知',
        time:'2018-8-20'
      },{
        name:'市委、市政府优化营商环境的意见',
        time:'2018-8-20'
      },{
        name:'唐山市行政审批中介服务超市进驻承诺书',
        time:'2018-8-20'
      },{
        name:'唐山市行政审批中介服务超市入驻公告',
        time:'2018-8-20'
      }],
      controlList:[{
        name:'唐山市行政审批中介服务超市管理办法（暂行）',
        time:'2018-9-21'
      }],
      type:'message',
      curPage:'1',
      num:0,
      pageSize:10,
      currentPage:1,
      total:0,
      page:1,
      contentWord:[
        {
          title:'关于印发《关于建立唐山市行政审批中介服务超市的工作方案》的通知',
        },
        {
          title:'市委、市政府优化营商环境的意见',
        },
        {
          title:'唐山市行政审批中介服务超市进驻承诺书',
          time:'2018-08-20 15:00:07 ' ,
          subTitle:'唐山市行政审批中介服务超市进驻承诺书',
          constructor:'唐山市行政审批局：',
          word:[{
            text:' __________________（公司全称)，所提供的备案资料及相关证明材料属实，自愿签订本承诺。进驻唐山市行政审批中介服务超市，接受唐山市行政审批局统一管理，严格遵守《唐山市行政审批中介服务超市管理办法》和市民服务中心各项规章制度，并在此作出郑重承诺：'
          },{
            text:'一、严格遵守国家有关法律、法规，认真履行唐山市行政审批中介服务超市的相关规定，组织开展中介服务活动。'
          },{
            text:'二、严格遵循公开、公平、公正和诚实信用的原则，以对委托人高度负责的态度，周到、规范服务，不论项目大小，认真完成中介服务工作。'
          },{
            text:'三、按照招标文件要求收取中介费，坚决杜绝价格垄断、任意抬高或恶意降低收费标准等现象。'
          },{
            text:'四、保证向委托人出具真实、合法、有效的各类书面文件，不伪造变造任何书面文件，出具的任何文件均符合国家法律、法规及国家政策的规定。'
          },{
            text:'五、保证在中介服务过程中不利用行贿、欺骗、弄虚作假等不正当手段承接业务。'
          },{
            text:'六、保证不以任何形式向委托人以外的单位和个人泄露委托事项秘密。'
          },{
            text:'七、保证在履行职责时，与各关系人之间，严格按照国家相关法律法规规范本公司人员的行为。'
          },{
            text:'八、保证不向委托人索要或接受回扣、现金、有价证券、贵重物品及其他任何有可能影响公正、公平、公开的财物或好处，不以任何形式变相收取或间接收受礼品等，包括未来利益。'
          },{
            text:'九、自愿接受监督管理部门的监督检查和管理，主动配合监督检查和处理投诉事项，如实反映情况。'
          },{
            text:'十、自愿接受社会各界人士监督。我公司及工作人员若违反本承诺，愿意接受相应的处罚。并承担不诚信或不廉洁所造成的一切后果；若构成违约，向委托人承担全部违约责任；若造成财产损失，承担全部赔偿责任；若触犯刑律，承担刑事责任。'
          },{
            text:'中介服务机构名称(盖章)：'
          },{
            text:'中介服务机构代表(签字)：'
          },{
            text:'二O一八年七月二十七日'
          }]
        },
        {
          title:'唐山市行政审批中介服务超市入驻公告',
          time:'2018-08-20 14:50:58',
          subTitle:'关于建立唐山市行政审批中介服务超市的招标公告',
          constructor:'根据《中共唐山市委、唐山市人民政府关于进一步优化全市营商环境的意见》（唐发【2018】10号）文件精神，打造“四最唐山”品牌的工作部署，构建我市“合法经营、公平开放、竞争规范、服务高效、收费合理”的中介市场，为项目单位提供更便捷的服务，激发中介市场活力，决定面向社会征集进驻唐山市行政审批中介服务超市的中介机构，现将有关事项公告如下：',
          word:[{
            text:'一、进驻范围'
          },{
            text:'依法设立、具备专业资质(格)条件并能承担相应责任的中介机构有意在唐山市开展中介服务，均可自愿申请进驻唐山市行政审批中介服务超市，在资质（格）许可范围内开展从业活动，并接受各中介监管部门和唐山市行政审批局的监督与管理。'
          },{
            text:'二、超市形式'
          },{
            text:'依托市民服务中心和市政府门户网站建设实体和网上两种中介服务超市。'
          },{
            text:'{C}{C}1、{C}{C}实体中介服务超市。设在市民服务中心A区，本着“既能满足企业群众办事需求，又能充分发挥中介机构作用”的原则，对中介服务事项报名超过3家（包括3家）的，由唐山市行政审批局组织公开招标确定2家进驻唐山市行政审批中介服务实体超市（房地产评估公司为3家）；对中介服务事项报名少于3家的，由唐山市行政审批局组织专家对中介机构资质进行审查，符合进驻条件的直接进驻唐山市行政审批中介服务超市。进驻时间为两年，两年后重新招标。'
          },{
            text:'2、网上虚拟中介服务超市。依托唐山市政府门户网站，设计“唐山市行政审批中介服务超市”栏目，各中介机构可随时通过网上或到行政审批局申报，我局和各审批单位共同进行资格审查（网上虚拟超市不受数量限制，凡是符合进驻标准的中介机构随时申报，随时进驻）。'
          },{
            text:'（一）工程咨询类：项目申请报告编制、节能专项报告编制、项目可行性研究报告编制、项目申请报告、水资源论证报告、水土保持方案报告书、建设项目使用林地可行性报告或林地现状调查表、核准类建设项目申请报告编制。'
          },{
            text:'（二）评估类：土地评估、房地产评估、土地利用总体规划修改调整方案编制、地质灾害评估报告、海洋工程建设项目环境影响报告书、环境影响评价报告、安全评估、防洪影响评价报告'
          },{
            text:'（三）工程测量类：宗海图编制、建设工程竣工规划测量及测量报告编制。'
          },{
            text:'（四）其它类：建筑工程施工图设计审查、人防施工图审查、验资报告编制、海域使用论证、排水许可技术检测、排水水质水量检测、移动式压力容器定期检验、车用气瓶安装监督检验。'
          },{
            text:'四、进驻基本条件'
          },{
            text:'凡具备相关执业资质资格，并符合以下条件的中介机构，均可申请进驻中介超市，进行相关信息登记，依法依规提供相关服务：'
          },{
            text:'（一）经依法登记设立，具有独立法人资格，并具备中介服务项目对应的资质； '
          },{
            text:'（二）能独立承担相应法律责任的法律主体； '
          },{
            text:'（三）有健全的执业规则以及其他相应的管理制度；'
          },{
            text:'（四）无违法违规和三年内不良信用记录；'
          },{
            text:'（五）出具的服务成果文件应具备相应法律效力；'
          },{
            text:'（六）一家中介机构有多项资质的应分类申报；'
          },{
            text:'（七）符合法律、法规规定的其他条件。 '
          },{
            text:'五、申请材料 '
          },{
            text:'（一）《唐山市行政审批中介服务超市进驻申请登记表》（附件1）和《唐山市行政审批中介服务超市进驻承诺书》（附件2）；（可登陆tssxzspj@126.com 密码：2806312邮箱自行下载）'
          },{
            text:'（二）营业执照复印件； '
          },{
            text:'（三）法人代表身份证复印件及业务授权代表身份证复印件； '
          },{
            text:'（四）资质（格）证书复印件； '
          },{
            text:'（五）内部管理制度及获得执业相关的荣誉情况证明材料； '
          },{
            text:'（六）中介机构所在地人民银行或法定征信机构出具的信用报告； '
          },{
            text:'（七）法定代表人委托授权书 '
          },{
            text:'（八）服务质量保证措施。 '
          },{
            text:'以上材料一式两份，并加盖单位公章后到唐山市公共资源交易中心（长宁道与学院路交叉口北行350米路东一层业务大厅）北京典方建设工程咨询有限公司业务窗口报名。 '
          },{
            text:'六、报名时间及方式 '
          },{
            text:'实体超市招标公告发布之日为2018年6月4日至6月8 日；申报网上超市不设时间限制。 '
          },{
            text:'报名采取现场报名，具体联系方式如下 '
          },{
            text:'代理机构：北京典方建设工程咨询有限公司 '
          },{
            text:'代理机构地址：唐山市路北区友谊东辅路梧桐苑209-2-803 '
          },{
            text:'代理机构联系方式：刘若西、李强 0315-2110893、13273579950 '
          },{
            text:'附件1. 唐山市行政审批中介服务超市进驻申请登记表 '
          },{
            text:'2. 唐山市行政审批中介服务超市进驻承诺书 '
          }]
        },{
        title:'唐山市行政审批中介服务超市管理办法（暂行）',
        time:'2018-09-21 16:24:57',
        subTitle:'',
        constructor:'依据《唐山市行政审批中介服务超市招标公告》，结合市民服务中心相关制度和服务规范要求，制定本管理办法。',
        word:[{
          text:'一、进驻唐山市行政审批中介服务实体超市的公司必须与唐山市行政审批局签订进驻协议和承诺书；进驻唐山市行政审批中介服务网络超市的公司必须报送真实有效的资质材料供招标代理机构审核。'
        },{
          text:'二、各进驻公司严格遵守国家有关法律、法规，认真履行唐山市行政审批中介服务超市的相关规定，组织开展中介服务活动。'
        },{
          text:'三、各进驻公司严格遵循公开、公平、公正和诚实信用的原则，以对委托人高度负责的态度，周到、规范服务，不论项目大小，认真完成中介服务工作。'
        },{
          text:'四、市行政审批局有关处室对进驻公司的服务态度、办事效率、服务质量、工作秩序和市民满意度等进行考评，考评结果将作为能否保留在市民服务中心办公资格的重要组成部分'
        },{
          text:'五、各进驻公司要积极配合市行政审批局开展政务服务工作，严格遵守市民服务中心的各项规章制度，明确一名驻厅经理和一名前台工作人员（前台工作人员要相对固定，禁止随意调换），执行中心刷脸考勤制度；工作人员着装要与中心工装保持基本一致，认真学习市民服务中心标准化管理，规范设置广告宣传物品。'
        },{
          text:'六、各进驻公司要严格按照招标文件要求收取中介费，坚决杜绝价格垄断、任意抬高或恶意降低收费标准等现象。'
        },{
          text:'七、各进驻公司必须向委托人出具真实、合法、有效的各类书面文件，不伪造变造任何书面文件，出具的任何文件均符合国家法律、法规及国家政策的规定。'
        },{
          text:'八、各进驻公司在中介服务过程中不得采取行贿、欺骗、弄虚作假等不正当手段承接业务。'
        },{
          text:'九、各进驻公司不得以任何形式向委托人以外的单位和个人泄露委托事项秘密。'
        },{
          text:'十、各进驻公司不得向委托人索要或接受回扣、现金、有价证券、贵重物品及其他任何有可能影响公正、公平、公开的财物或好处，不以任何形式变相收取或间接收受礼品等，包括未来利益。'
        },{
          text:'十一、各进驻公司有下列情况的，取消进驻资格：1.不具备合格资质，在欺骗情况下（以欺诈手段）签订协议者；2.服务质量差，不能履行服务承诺的；3.服务过程中违反职业道德，出现严重失职、营私舞弊、泄露企业商业秘密等情形的；4.受到有关行政主管机关、行业组织处分的；5.一年之内三次查实有服务质量问题投诉的；6.不服从招标人管理的。'
        }]
      }],
      isScroll:false,
      activeName:6,
      // 当前公告类
      categoryId:'',
      // 分类列表
      items:[],
      // 当前选择类名
      categoryName:'',
      // 是否显示分页
      pagination:false,
      //附件列表
      attrList:[]
    },
    created: function () {
        this.getPageAoaNoticeContentCascade();
        this.getListAoaNoticeCategory();
        console.log(localStorage.getItem("access_token"));
        // this.getURLArgs();
    },
    mounted: function () {
      var that = this;
      var url = document.location.toString();
      this.getItemBasicId = url.split("?id=")[1];
      if(this.getItemBasicId){
        this.listClick(this.getItemBasicId);
        this.curPage =2;
      }
      window.addEventListener("resize", function() {
        return (function(){
          window.fullHeight = document.documentElement.clientHeight
          that.fullHeight = window.fullHeight;
          that.treeLeftHeight = that.fullHeight - 100;
        })();
      });
    },
    methods: {

      //获取公告分类
      getListAoaNoticeCategory:function(){
        var _this = this;
        // this.loading=true;
        request('',{url:ctx + 'rest/aoa/notice/category/getListAoaNoticeCategory',type: 'get',
          data: {
            businessFlag : 'supermarket'
          }
        },function (data) {
          _this.categoryName = data.content[0].categoryName;
          _this.items = data.content;
        }, function(msg){
           _this.$message({
            message: '加载失败',
            type: 'error'
          });
          _this.loading=false;
        });
      },
      //获取公告列表
      getPageAoaNoticeContentCascade:function(){
        var _this = this;
        // this.loading=true;
        request('',{url:ctx + 'rest/aoa/notice/content/getPageAoaNoticeContentCascade',type: 'get',
          data: {
            pageSize : this.pageSize,
            pageNum : this.currentPage,
            categoryId : this.categoryId,
            businessFlag : 'supermarket'
          }
        },function (data) {
          _this.noticeList = data.content.rows;
          _this.total = data.content.total;
          if( _this.total<10){
            _this.pagination=false;
          }else{
            _this.pagination=true;
          }
        }, function(msg){
           _this.$message({
            message: '加载失败',
            type: 'error'
          });
          _this.loading=false;
        });
      },
      //下载附件
      download:function(item){
        var _this = this;
        window.open(ctx+"rest/aoa/attachment/downloadByDetailId?detailId="+item.detailId)
      },
      // 格式化时间
      dateFormat: function (val){
          var daterc = val;
          if(daterc!=null){
              var dateMat= new Date(parseInt(daterc));
              return formatDate(dateMat, 'yyyy.MM.dd hh:mm')
          }
      },
      tabToggle:function(e,item){
        this.categoryId = item.categoryId;
        $(e.target).addClass('active').siblings().removeClass('active');
        this.getPageAoaNoticeContentCascade();
        this.categoryName = item.categoryName;
      },
      getURLArgs:function() {
        //解析地址栏中的中文字符
          var search = decodeURI(window.location.search);
          if(search){
            var params = {}
            search = search.substring(1, search.length);
            var group = search.split('?')
            for (var i = 0; i < group.length; i++) {
                var entry = group[i].split('=')
                params[entry[0]] = entry[1]
            }
            this.curPage=2;
            this.num=params.id;
          }

      },
      handleSizeChange:function(val) {
        this.pageSize = val;
        this.getPageAoaNoticeContentCascade();
      },
      handleCurrentChange:function(val) {
        this.currentPage = val;
        this.getPageAoaNoticeContentCascade();
      },
      listClick:function(contentId){
        var _this = this;
        this.loading=true;
        request('',{url:ctx + 'rest/aoa/notice/content/getAoaNoticeContent',type: 'get',
          data: {
            id : contentId
          }
        },function (data) {
          _this.contentWord = data.content;
          _this.loading=false;

        }, function(msg){
           _this.$message({
            message: '加载失败',
            type: 'error'
          });
          _this.loading=false;
        });
      }
    },
    computed:{
      headMsg:function(){
        return {
          "Authorization":"bearer "+localStorage.getItem("access_token")
        }
      }
    },
    watch:{
      filterText1:function(val){

      }
    }
});


