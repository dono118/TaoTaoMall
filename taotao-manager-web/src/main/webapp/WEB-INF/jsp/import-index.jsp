<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>导入数据到索引库</title>
</head>
<body>
	<div>
		<a class="easyui-linkbutton" onclick="importIndex()">导入数据到索引库</a>
	</div>
<script type="text/javascript">  
   function importIndex(){  
       $.post("/index/import",null,function(data){  
            if(data.status == 200){  
                $.messager.alert('提示','导入数据到索引库成功!');  
            }else{  
                $.messager.alert('提示','导入数据到索引库失败!');  
            }  
        });  
   }  
</script>
</body>
</html>