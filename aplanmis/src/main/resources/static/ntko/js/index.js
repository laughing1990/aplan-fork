var TANGER_OCX_OBJ;//控件对象
function init(){
    TANGER_OCX_OBJ=document.getElementById("TANGER_OCX");
    //判断chrome的版本是否大于54，若大于54，使用代理解决问题
    //本段代码一般放在TANGER_OCX_OBJ = document.getElementById("TANGER_OCX");代码后面。
    var brower;
    var regStr_Chrome = /chrome\/[\d.]+/gi;
    var agent = navigator.userAgent.toLowerCase();
    if( agent.indexOf("chrome") > 0 ){
        brower = agent.match( regStr_Chrome );
    }
    var verinfo = ( brower + "" ).replace(/[^0-9.]/ig,"");
    var uVerinfo = parseFloat( verinfo );
    if( uVerinfo > 54 ){
        TANGER_OCX_OBJ = new Proxy( TANGER_OCX_OBJ, {
            set: function (target, key, value, proxy) {
                var func = target["setattribute"];
                var args = new Array();
                args[0] = key;
                args[1] = value;
                return ( Reflect.apply(func, target, args) );
            },
            get: function(target, key, proxy) {
                var value = target[key];
                if(typeof value == 'function'){ // 为函数
                    return function() {
                        return Reflect.apply(value, target, arguments);
                    };
                }
                else
                    return( value );
            }
        });
    }
    if(pluginUrl == '') pluginUrl = ctx + "/static/ntko/plugin/ntkooledocall.cab";
    TANGER_OCX_OBJ.AddDocTypePlugin(".pdf","PDF.NtkoDocument","4.0.0.7",pluginUrl,51,true);//加载pdf插件
    initCustomMenus();           //初始化自定义菜单
    unShowFullScreenButton();     //取消全屏
}

//打开指定url的文档
// Word文档：						“Word.Document”
// PowerPoint幻灯片：				“PowerPoint.Show”
// Excel工作表：					“Excel.Sheet”
// Excel图表： 					"Excel.Chart"
// Visio画图： 					"Visio.Drawing"
// MS Project项目：				"MSProject.Project"
// WPS2003文档：					"WPSFile.4.8001"
// WPS2005及以上文档：			"WPS.Document"
// 金山电子表2003：				"ET.Sheet.1.80.01.2001"
// 金山电子表2005及以上：		"ET.WorkBook"
// 金山电子表2013(V9版本)：		"KET.WorkBook"
// 永中OFFICE文档：				"EIOffice.Document"
function openWord(wordUrl) {
    // var wordUrl =  ctx + "/ui-static/aplanmis/ntko/9-国家工程建设项目审批管理系统数据共享交换标准_V1.0_20180806.doc";
    // TANGER_OCX_OBJ.ToolBars = false;            //不显示工具栏
    // TANGER_OCX_OBJ.Statusbar = false;           //不显示底部的状态栏
    // TANGER_OCX_OBJ.IsShowFileErrorMsg = false;  //不显示错误提示弹窗
    // TANGER_OCX_OBJ.OpenFromURL(wordUrl);          //同步打开
    TANGER_OCX_OBJ.BeginOpenFromURL(wordUrl);      //异步打开
}
//自定义菜单
function initCustomMenus() {
    var b, c, d, a = TANGER_OCX_OBJ;
    for (b = 0; 1 > b; b++)
        for (a.AddCustomMenu2(b, "自定义菜单(&" + b + ")"), c = 0; 1 > c; c++)
            for (a.AddCustomMenuItem2(b, c, -1, !0, "自定义菜单组", !1), d = 0; 3 > d; d++)
    0 == d && a.AddCustomMenuItem2(b, c, d, !1, "插入图片", !1, 100 * b + 20 * c + d),
    1 == d && a.AddCustomMenuItem2(b, c, d, !1, "打印预览", !1, 100 * b + 20 * c + d),
    2 == d && a.AddCustomMenuItem2(b, c, d, !1, "自定义菜单3", !1, 100 * b + 20 * c + d)
}

//新建一个word
function createNewWord() {
    TANGER_OCX_OBJ.CreateNew("word.document");
    TANGER_OCX_OBJ.WebFileName = '新建word文档.doc';         //当点击保存按钮时显示的默认名称
    // TANGER_OCX_OBJ.ActiveDocument.Application.Selection.TypeText("快乐无处不在！");
    // TANGER_OCX_OBJ.IsNoCopy = true;                  //禁止拷贝控件中的内容       mark 这是一个坑
}

//打开指定url的pdf
function openPdf(pdfUrl) {
    // var pdfUrl= ctx + "/ui-static/aplanmis/ntko/附件：《学分制管理规范》.pdf";
    TANGER_OCX_OBJ.BeginOpenFromURL(pdfUrl);
}
//保存
function save() {
    if(!TANGER_OCX_bDocOpen) {
        console.log('没有打开的文档！');
        return false;
    }
    // var path = 'D:\\test.doc';
    // TANGER_OCX_OBJ.SaveTolocal(path);//保存文档到本地
    // if(0 == TANGER_OCX_OBJ.status){
    //     console.log('保存成功');
    // }
    TANGER_OCX_OBJ.ShowDialog(3);//弹出另存为窗口  0：新建对象 1：打开 2：保存 3：另存为 4：打印	5：页面设置	6：文件属性
}
//保存为pdf
function objSaveAsPDFFile(path) {
    // var path = 'D:\\test.pdf';
    if (TANGER_OCX_bDocOpen) {
        try {
            //word转pdf
            1 == opendoctype ? TANGER_OCX_OBJ.ActiveDocument.ExportAsFixedFormat(path, 17, !1, 0, 0, 1, 1, 0, !0, !0, 0, !0, !0, !1) :
                //excel转pdf
            2 == opendoctype && TANGER_OCX_OBJ.ActiveDocument.ExportAsFixedFormat(0, path, 0, !0, !1, 1, 1, !1);
            console.log("PDF保存成功" + path);
        } catch (b) {
            //判断本机是否安装了PDFCreator转换器,true表示安装，false表示没有安装
            TANGER_OCX_OBJ.IsPDFCreatorInstalled() ?
                (TANGER_OCX_OBJ.SaveAsPDFFile(path, !1), console.log(TANGER_OCX_OBJ.StatusMessage)) : console.log("请安装PDFCreator");
        }
    }else {
        console.log("没有文档处于编辑状态");
    }
}
//打印预览，pdf不支持？
function objprintpreview() {
    TANGER_OCX_bDocOpen ? TANGER_OCX_OBJ.PrintPreview() : console.log("无文档打开不能执行打印预览！");
}
//关闭打印预览
function objexitprintpreview() {
    TANGER_OCX_OBJ.ExitPrintPreview();
}
//关闭文档，关闭前检查文档是否已经修改。只是无法检查是否已经保存过...保存之后继续修改...
function officeclose() {
    0 == TANGER_OCX_OBJ.ActiveDocument.Saved ? (console.log("你正在编辑的文档还没有保存，你可以先保存后再关闭。") ,
        TANGER_OCX_OBJ.ShowDialog(3),
        TANGER_OCX_OBJ.Close(),
        TANGER_OCX_bDocOpen = !1,
        console.log("文档已经关闭")) : (TANGER_OCX_OBJ.Close(), TANGER_OCX_bDocOpen = !1, console.log("文档已经关闭"))
}

//打印文档
function objprint() {
    TANGER_OCX_bDocOpen ? TANGER_OCX_OBJ.PrintOut(true) : console.log("无文档打开不能执行打印！")
}
//文档内部滚动条是否显示
function ScrollBar() {
    TANGER_OCX_OBJ.ActiveDocument.ActiveWindow.DisplayHorizontalScrollBar = !TANGER_OCX_OBJ.ActiveDocument.ActiveWindow.DisplayHorizontalScrollBar,
        TANGER_OCX_OBJ.ActiveDocument.ActiveWindow.DisplayVerticalScrollBar = !TANGER_OCX_OBJ.ActiveDocument.ActiveWindow.DisplayVerticalScrollBar
}
//文档控件底部状态栏是否显示
function objStatusbar() {
    TANGER_OCX_OBJ.statusbar = !TANGER_OCX_OBJ.statusbar
}
//文档控件顶部标题栏是否显示
function objTitleBar() {
    TANGER_OCX_OBJ.Titlebar = !TANGER_OCX_OBJ.Titlebar
}
//文档控件标题栏下面的菜单栏是否显示
function objMenubar() {
    TANGER_OCX_OBJ.Menubar = !TANGER_OCX_OBJ.Menubar
}
//菜单栏下面的自定义工具栏是否显示。
function objCustomToolBar() {
    TANGER_OCX_OBJ.CustomToolBar = !TANGER_OCX_OBJ.CustomToolBar
}
//文档内部工具栏是否显示
function officeToolBar() {
    TANGER_OCX_OBJ.toolbars = !TANGER_OCX_OBJ.toolbars
}
//该方法用来将一个从URL下载的word文档插入到当前word文档的光标处。适用于word文档
function addtemplatefile(url) {
    // var url = 'templatefile/test.docx';
    TANGER_OCX_bDocOpen && 1 == opendoctype ? (TANGER_OCX_OBJ.ActiveDocument.Application.Selection.TypeParagraph(), TANGER_OCX_OBJ.AddTemplateFromURL(url)) :  console.log("没有文档处于编辑状态或者不是WORD文档")
}
//打开文件选择窗口，从本地增加插入模板到当前光标处。适用于word文档
function addtemplatelocalfile() {
    TANGER_OCX_bDocOpen && 1 == opendoctype ? (TANGER_OCX_OBJ.ActiveDocument.Application.Selection.TypeParagraph(), TANGER_OCX_OBJ.AddTemplateFromLocal("", !0, !1)) :  console.log("没有文档处于编辑状态或者不是WORD文档")
}
//切换文档显示,不好用。每次打开都要重新加载文档数据
// function objectDisplay(a) {
//     var b = document.getElementById("ocxobject");
//     a ? (b.style.display = "block", a = !1, TANGER_OCX_OBJ.PutBase64Value(docdata)) : (docdata = TANGER_OCX_OBJ.GetBase64Value(), b.style.display = "none", a = !0);
// }
//插入本地图片到光标位置，支持多张。
function insertpicforlocal() {
    TANGER_OCX_bDocOpen ? (TANGER_OCX_OBJ.AddMultiPicFromLocal(), 0 == TANGER_OCX_OBJ.statuscode && console.log("插入图片成功！")) : console.log("没有文档处于编辑状态！")
}
//不允许全屏。谷歌全屏状态下，编辑文档，点击鼠标右键显示菜单时会退出全屏。体验不好
function unShowFullScreenButton() {
    TANGER_OCX_OBJ.IsShowFullScreenButton = false;//不显示全屏按钮
    TANGER_OCX_OBJ.IsEnableDoubleClickFSM = false;//不允许双击切换
}



