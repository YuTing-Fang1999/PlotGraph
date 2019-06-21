<%@page import="java.util.ArrayList"%>
<%@page import="com.sun.corba.se.impl.orb.ParserTable.TestORBSocketFactory"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import= "plotgraph.*" %>
<%@ page import= "org.json.*" %>
<%@ page import= "java.util.Iterator" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<%
	parseFile f;
ReadFile file;
String N = (String) request.getParameter("N");
if(N==null){
	N="Pub7";
}

// f = new parseFile(N+".txt");
file = new ReadFile(N+".txt");
%>
<title><%=file.getSchoolName()%></title>
<script src="echarts.js"></script>
</head>
<body>
	<% 
	ReadAllFile readAllFile = new ReadAllFile();
// 	ParseAllFile allFile = new ParseAllFile();
	
	JSONArray data = file.getData();
	JSONArray link = file.getLink();
	JSONArray categories = file.getCategories();

// 	JSONArray data = f.getData();
// 	JSONArray link = f.getLink();
// 	JSONArray categories = f.getCategories();
	
	Iterator it1 = readAllFile.getAllPubSchoolName().iterator();
	Iterator it2 = readAllFile.getAllPubSchoolNum().iterator();
	Iterator it3 = readAllFile.getAllPriSchoolName().iterator();
	Iterator it4 = readAllFile.getAllPriSchoolNum().iterator();
	
// 	Iterator it1 = allFile.getAllPubSchoolName().iterator();
// 	Iterator it2 = allFile.getAllPubSchoolNum().iterator();
// 	Iterator it3 = allFile.getAllPriSchoolName().iterator();
// 	Iterator it4 = allFile.getAllPriSchoolNum().iterator();
	
	%>
	<form style="display:inline" method=post action="Graph3.jsp">
		<select name="N"> 
			<%while(it1.hasNext()){%>
			<option value=<%="Pub"+it2.next()%>><%=it1.next()%></option> 
			<%}%> 
		</select>
		<input type="submit" value="查詢國立" name="submit">
	</form>
	<form style="display:inline" method=post action="Graph3.jsp">
		<select name="N"> 
			<%while(it3.hasNext()){%>
			<option value=<%="Pri"+it4.next()%>><%=it3.next()%></option> 
			<%}%> 
		</select>
		<input type="submit" value="查詢私立" name="submit">
	</form>
	<form style="display:inline" method=post action="Graph4.jsp">
		<input type="submit" value="查詢全台資工系" name="submit">
	</form>
	
	<form name="update" action="" method="post">
		<input type="hidden" name="N" value="<%=N%>" />
		<input type="submit" name="updateButton" value="更新此頁資料" onclick="updateData()"/>
		<input type="submit" name="allUpdateButton" value="更新全部資料" onclick="updateAllData()"/>
	</form>
	
	<div id="main" style="width:1300px; height:600px;"></div>
	
	 <script type="text/javascript">
		var DATA = <%=data%>
		var LINK = <%=link%>
		var CATEGORIES = <%=categories%>
		var myChart = echarts.init(document.getElementById('main'));
		var option = {
// 				backgroundColor: '#ccc',	// 背景颜色
			    title: {                    // 图表标题
			        text: "<%=file.getSchoolName()%>",
			        subtext: "<%=file.getDepartment()%>",
			        left : '3%',                    // 标题距离左侧边距
			        top : '3%',                     // 标题距顶部边距
					textStyle : {                     
						fontSize : '30',                    
					},
				    subtextStyle:{//副标题内容的样式
// 						color:'green',//绿色
// 						fontStyle:'normal',//主标题文字字体风格，默认normal，有italic(斜体),oblique(斜体)
// 						fontWeight:"lighter",//可选normal(正常)，bold(加粗)，bolder(加粗)，lighter(变细)，100|200|300|400|500...
// 						fontFamily:"san-serif",//主题文字字体，默认微软雅黑
						fontSize:18//主题文字字体大小，默认为12px
				    },
				},
			    tooltip: {                  // 提示框的配置
			    	confine: true,
			    	triggerOn: 'click',
			        formatter: function(param) {
			            if (param.dataType === 'edge') {
			                return param.data.category
			            }
			            return param.data.plan;
			        }
			    },
			    legend: {
			        type: 'scroll',
			        orient: 'vertical',
			        right: 10,
			        top: 20,
			        bottom: 10,
			    },
			    series: 
			    [{
			        type: "graph",          // 系列类型:关系图***很重要，一定要指名
			        top: '10%',             // 图表距离容器顶部的距离
			        roam: true,             // 是否开启鼠标缩放和平移漫游。默认不开启。如果只想要开启缩放或者平移，可以设置成 'scale' 或者 'move'。设置成 true 为都开启
			        focusNodeAdjacency: true,   // 是否在鼠标移到节点上的时候突出显示节点以及节点的边和邻接节点。[ default: false ]
			                force: {                // 力引导布局相关的配置项，力引导布局是模拟弹簧电荷模型在每两个节点之间添加一个斥力，每条边的两个节点之间添加一个引力，每次迭代节点会在各个斥力和引力的作用下移动位置，多次迭代后节点会静止在一个受力平衡的位置，达到整个模型的能量最小化。
			                                // 力引导布局的结果有良好的对称性和局部聚合性，也比较美观。
			            repulsion: 1000,            // [ default: 50 ]节点之间的斥力因子(关系对象之间的距离)。支持设置成数组表达斥力的范围，此时不同大小的值会线性映射到不同的斥力。值越大则斥力越大
			            edgeLength: [400, 300]      // [ default: 30 ]边的两个节点之间的距离(关系对象连接线两端对象的距离,会根据关系对象值得大小来判断距离的大小)，
			                                        // 这个距离也会受 repulsion。支持设置成数组表达边长的范围，此时不同大小的值会线性映射到不同的长度。值越小则长度越长。如下示例:
			                                        // 值最大的边长度会趋向于 10，值最小的边长度会趋向于 50      edgeLength: [10, 50]
			        },
			        layout: "force",            // 图的布局。[ default: 'none' ]
			                                    // 'none' 不采用任何布局，使用节点中提供的 x， y 作为节点的位置。
			                                    // 'circular' 采用环形布局;'force' 采用力引导布局.
			        // 标记的图形
			        lineStyle: {            // 关系边的公用线条样式。其中 lineStyle.color 支持设置为'source'或者'target'特殊值，此时边会自动取源节点或目标节点的颜色作为自己的颜色。
			            normal: {
			                color: '#000',          // 线的颜色[ default: '#aaa' ]
			                width: 5,               // 线宽[ default: 1 ]
			                type: 'solid',          // 线的类型[ default: solid ]   'dashed'    'dotted'
			                opacity: 0.5,           // 图形透明度。支持从 0 到 1 的数字，为 0 时不绘制该图形。[ default: 0.5 ]
			                curveness: 0            // 边的曲度，支持从 0 到 1 的值，值越大曲度越大。[ default: 0 ]
			            }
			        },
			        label: {                // 关系对象上的标签
			            normal: {
			                show: true,                 // 是否显示标签
			                position: "inside",         // 标签位置:'top''left''right''bottom''inside''insideLeft''insideRight''insideTop''insideBottom''insideTopLeft''insideBottomLeft''insideTopRight''insideBottomRight'
			                textStyle: {                // 文本样式
			                    fontSize: 14
			                }
			            }
			        },
			        edgeLabel: {                // 连接两个关系对象的线上的标签
			            normal: {
			                show: false,
			                textStyle: {                
			                    fontSize: 10
			                },
			                formatter: function(param) {        // 标签内容
			                    return param.data.category;
			                }
			            }
			        },
			        data: DATA,
			        categories: CATEGORIES,
			        links: LINK,
			    }],
			    
			    animationEasingUpdate: "quinticInOut",          // 数据更新动画的缓动效果。[ default: cubicOut ]    "quinticInOut"
			    animationDurationUpdate: 100                    // 数据更新动画的时长。[ default: 300 ]
			};

		myChart.setOption(option);
		
		function updateData(){
			if(confirm("確定要更新此頁資料嗎?"))
			{
				document.update.action="MyServlet";
				document.updateButton.submit();
				document.getElementsByName('update')[0].submit();
			}
		}
		
		function updateAllData(){
			if(confirm("確定要更新全部大學的資料嗎?(大概要花十分鐘)"))
			{
				document.update.action="MyServlet";
				document.updateButton.submit();
				document.getElementsByName('update')[0].submit();
			}
		}
	</script>
</body>
</html>