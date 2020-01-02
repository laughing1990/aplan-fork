//******************************************************************************************************************************************************
/*
谷歌浏览器事件接管
*/
//1.打开文档回调
// function OnComplete(type,code,html) {
//     console.log("BeginOpenFromURL成功回调");
// }

//2.在谷歌或者火狐下面，和服务器交互是异步的方式。SaveTourl执行完后，文档数据的保存操作可能并没执行完。所以引入了保存完成后触发的事件。
function ntkosavetourl(type,code,html) {
    //alert(type);
}

//3.文档打开完毕时回调，ntkoondocumentopened 先于 OnComplete
var TANGER_OCX_bDocOpen = !1;//标记文档是否打开，默认false
var opendoctype;              //文档类型
function ntkoondocumentopened(str,doc) {
    TANGER_OCX_bDocOpen = !0;//true
    //当前打开的文档类型 0=没有文档；100 =其他文档类型；1=word；2=Excel.Sheet或者 Excel.Chart ；3=PowerPoint.Show；4= Visio.Drawing； 5=MSProject.Project；6= WPS Doc；7=Kingsoft.Sheet
    opendoctype = TANGER_OCX_OBJ.DocType;

    TANGER_OCX_OBJ.activeDocument.saved=true;//saved属性用来判断文档是否被修改过,文档打开的时候设置成ture,当文档被修改,自动被设置为false,该属性由office提供.
    //获取文档控件中打开的文档的文档类型 TANGER_OCX_OBJ.doctype
    console.log("ondocumentopened成功回调");
}

//4.该事件在用户单击文件菜单按钮时发生
// function FileCommand(Item, IsCancel){    //文件菜单命令索引、是否取消命令
//     console.log(Item);
//     if (Item == 2){                       //关闭文档
//         TANGER_OCX_bDocOpen = !1;
//         opendoctype = 0;
//     }else
//     if (Item == 3){                       //保存
//         // TANGER_OCX_OBJ.CancelLastCommand = true;//取消操作
//         // TANGER_OCX_OBJ.ShowDialog(3);           //弹出另存为窗口
//     }
// }

//5.该事件在用户单击自定义主菜单中的项目时执行
// function CustomMenuCmd(menuPos,submenuPos,subsubmenuPos,menuCaption,menuID){
//     if(menuCaption == '插入图片'){
//         insertpicforlocal();
//     }else
//     if(menuCaption == '打印预览'){
//         objprintpreview();
//     }
//     console.log("第" + menuPos +","+ submenuPos +","+ subsubmenuPos +"个菜单项,menuID="+menuID+",菜单标题为\""+menuCaption+"\"的命令被执行.");
// }

//6.该事件在WORD、WPS右键事件发生之前激活
// function wordBeforeRightClick(Selection, IsCancel){
//     console.log('谷歌右键...')
// }

//7.将文件保存成HTML文件到URL触发
function ntkopublishashtml(type,code,html){

}

//8.将文档控件中的文档转换为PDF文件并保存到URL触发
function ntkopublishaspdf(type,code,html){

}

//9.将文件保存成其他文件到URL触发
function ntkosaveasotherurl(type,code,html){

}


//*****************************************************************************************************************************************************
