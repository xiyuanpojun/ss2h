$(function (){
	layui.use("element",function(){
	  var element = layui.element;
	});
var flag=0;
//对输入值 进行监听
  layui.use("form",function(){
	//对输入的功能id进行监听
	    $("#afId").bind("input propertychange",function(){
	    	var afId=$("#afId").val();
	    	if(!isNaN(afId)){
	    		flag=1;
	    		$.ajax({
		    		url:ctx+'/func/func_checkfId',
		    		type:"POST",
		    		data:{
		    			'fId':$("#afId").val()
		    		},
		    	   success:function(data){
                       if($("#afId").val()==null||$("#afId").val()==""){
                           $("#checkfId").html("");
                           flag=3;
                       }
                       else {
                           if (data.message == "1") {
                               $("#checkfId").html("功能id可用");
                               $("#checkfId").attr("style", "color:green");
                               flag = 1;
                           }
                           else if (data.message = "0") {
                               $("#checkfId").html("功能id不可用");
                               $("#checkfId").attr("style", "color:red");
                               flag = 2;
                           }
                       }
		    	    }
		    	});
	    		 
	  		 }
	    	
	    	else{
	    		 $("#checkfId").html("请输入 数字");
		          $("#checkfId").attr("style", "color:red");
		          flag=2;
	    	}
	    	
	    	 return false;
	    });
  });
  
//对新增功能表单form监听
  layui.use('form',function(){
	  var fId=$("#afId").val();
	  var fName=$("#afName").val();
	  var fUrl=$("#afUrl").val()
	  checkLogin();
	  var form=layui.form;
	  if(fId==null||fId==""||fName==null||fName==""||fUrl==null||fUrl==""){
		  flag==0;
	  }
	  form.on('submit(btn)',function(data){
		  if(flag==1){
	      $.ajax({
	    	  url:ctx+"/func/func_add",
	    	  type:"POST",
	    	  data:data.field,
	    	  data_type:"json",
	    	  beforeSend: function (xhr) {
	                $("#btn").attr('disabled', "true");
	            },
	    	  success:function(data){
	    		   layer.msg(data.message);
	    		   $("#checkfId").html("");
	    		   $('#funcform')[0].reset();
	    		  
	    		
	    	  },
	    	  error:function(){
	    		  layer.alert("连接服务器失败");
	    	  },
	    	  complete:function(){
	    	   $("#btn").removeAttr("disabled"); 
	    	  }
	    	 
	      });
	     
		  }
     else if(flag==1){
    	  layer.msg("功能id不可用请重新填写");  
    	  }
     else{
    	 layer.msg("存在 空 值请重新 填写 ");  
     }
	      return false;
	  });
  });
 //对功能信息修改表单form监听
    layui.use('form', function () {
    	 checkLogin();
        var form = layui.form;
        //修改
        form.on('submit(uBtn)', function (data) {
            update(data.field);
            return false;
        });
    });
  $("#uCan").click(function () {
        layer.closeAll();
    });
    //修改弹出层
    var updateLayer;
    //执行修改功能的方法
    function update(data) {
        $.ajax({
            url: ctx + "/func/func_update",
            type: "POST",
            data: data,
            dataType: "json",
            success: function (data) {
                var error = parseInt(data.error);
                if (error === 0) {
                    layer.close(updateLayer);
                    init();
                }
                layer.msg(data.message);
            },
            error: function () {
                layer.alert("连接服务器失败");
            }
        })
    }
//初始化方法 
    function init() {
        layui.use('table', function () {
            var table = layui.table;
            var tableOptions = {
                url: ctx+'/func/func_findFuncAll',
                method: 'POST',
                id: 'listReload',
                page: true,
                request: {pageName: 'current', limitName: 'currentTotal'},
                response: {
                    statusName: 'error',
                    statusCode: 0,
                    msgName: 'message',
                    countName: 'total',
                    dataName: 'funclist'
                }
            };
            table.init('table', tableOptions);
           
            //监听工具条
            table.on('tool(table)', function (obj) {
                var data = obj.data;//obj代表所选中的行
                
                if (obj.event === 'detail') {
                    layer.msg('ID：' + data.fId + ' 的查看操作');
                } else if (obj.event === 'del') {
                    
                        $.ajax({
                            url: ctx + "/func/func_delete",
                            type: "POST",
                            data: {
                            	 'funcentity.fId': data.fId
                            },
                            dataType: "json",
                            success: function (data) {
                                var error = parseInt(data.error);
                                if (error === 0) {
                                    obj.del();
                                    layer.msg(data.message);
                                    init();
                                }
                              
                            },
                            error: function () {
                                layer.alert("连接服务器失败");
                            },
                            complete: function () {
                                layer.close(index);
                            }
                        });
                
                } else if (obj.event === 'edit') {
                    //获取信息
                    $.ajax({
                        url: ctx + "/func/func_findOne",
                        type: "POST",
                        data: {
                            'funcentity.fId': data.fId
                        },
                        dataType: "json",
                        success: function (data) {
                            if (data.error === 0) {
                                //设置值
                                var fId = data.func.fId;
                                var fName = data.func.fName;
                                var fUrl = data.func.fUrl;
                                $("#fId").val(fId);
                                $("#fName").val(fName);
                                $("#fUrl").val(fUrl);
                                //重新渲染
                                layui.use('form', function () {
                                    var form = layui.form;
                                    form.render();//更新全部 ；
                                });
                                updateLayer = layer.open({
                                    type: 1,
                                    content: $('#update'),
                                    offset: 'auto',
                                    maxWidth: 500
                                });
                                layer.title('更新信息-功能id:' + fId, updateLayer);
                            } else {
                                layer.alert(data.message);
                            }
                        },
                        error: function () {
                            layer.alert("连接服务器失败");
                        }
                    });
                }
            });
        });
    }
  //点击功能列表
    $("#funclistmenu").click(function(){
  	   init();
    });
    //点击添加功能重置 checkId
    $("#addfunc").click(function(){
      $('#funcform')[0].reset();
      $("#checkfId").html("");
    });
    //打开页面时加载一次数据
    init();
    
});