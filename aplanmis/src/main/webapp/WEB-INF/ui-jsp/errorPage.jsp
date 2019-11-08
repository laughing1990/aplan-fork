<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
<div>
    <p>请求信息</p>
    <a>${url}</a>
</div>
<br>
    <p>错误码：</p>
    <a>${result.errorCode}</a><br>
    <p>错误信息：</p><br>
<a>${result.errorMsg}</a>
</body>
</html>
