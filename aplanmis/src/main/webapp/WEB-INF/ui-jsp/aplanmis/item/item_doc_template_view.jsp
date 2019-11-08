<%@ page contentType="text/html;charset=UTF-8" %>

<!DOCTYPE html>
<html>
<head>
    <title>${fileTypeName}</title>
</head>
<body>
   <img src="${pageContext.request.contextPath}/aea/item/basic/getItemDocOrTemplateFile.do?itemBasicId=${itemBasicId}&fileType=${fileType}" alt="${fileTypeName}"/>
</body>
</html>