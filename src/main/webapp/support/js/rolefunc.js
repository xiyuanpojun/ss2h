$(function () {
    layui.use('element', function () {
        var element = layui.element;
    });
    layui.use('form', function () {
    	checkLogin();
        var form =layui.form;
        //增加
        form.on('submit(btn)', function (data) {
        	checkLogin();
        	var rol_id="";
        	var funcidlist="";
        	$("input:radio[name='roleid']:checked").each(function(){//遍历name=funcid的单选按钮radio
        		rol_id=$(this).val();
        		});
        	$("input:checkbox[name='funcid']:checked").each(function(){//遍历name=funcid的多选框checkbox
        		funcidlist=funcidlist+","+$(this).val();
        		});
        	  $.ajax({
                  url: ctx + "/rolfc/rolfc_add",
                  type: "POST",
                  data:{
        		    'tRoleFuncEntity.roleid':rol_id,
        		    'tRoleFuncEntity.funcId':funcidlist
        	       },
                  dataType: "json",
                  beforeSend: function (xhr) {
                      $("#btn").attr('disabled', "true");
                  },
                  success: function (data) {
                      var error = parseInt(data.error);
                      if (error === 0) {
                          $("#btn").next().click();
                      }
                      layer.msg(data.message);
                      getfunc(rol_id);
                  },
                  error: function () {
                      layer.msg("连接服务器失败");
                  },
                  complete: function () {
                      $("#btn").removeAttr("disabled");
                  }
              });
        
            return false;
        });
    });
   //获取多选框数据 
    function getfunc(data){
    	 checkLogin();
    	 $("#funclist").html("");
    	  $.ajax({
			   url:ctx + "/rolfc/rolfc_findnofuncbyrole",
			   type:"POST",
			   dataType:"json",
			   data:{
				   'role':data
			   },
			   success:function(data){
				$.each(data.tfunclist,function(i,item){
					var checkbox='<input type="checkbox" name="funcid" value="'+item.fId+'" title="'+item.fName+'">';
   				$("#funclist").append(checkbox);
   				layui.use('form',function(){
   			    	 var form = layui.form;
   			         form.render();
   				});
     		
				});
				},
				error:function(data){
					layer.msg(data.message);
				}
			   
		   });
    }
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
  //对功能信息修改表单form监听
    layui.use('form', function () {
    	
    	 checkLogin();
        var form = layui.form;
        //修改
        form.on('submit(uBtn)', function (data) {
        	layer.msg("得到了");
           // update(data.field);
            return false;
        });
    });
    layui.use('form',function(){
  
    	var form = layui.form;
    	form.on('radio(radio1)', function(data){
    		  getfunc(data.value);
    		  return false;
    		});  
    });
    
    layui.use('form', function () {
    	 checkLogin();
        var form = layui.form;
        //监听角色
        form.on('select(searchChange)', function (data) {
            var role = data.value;
            init(role);
            return false;
        });
    });
    function init(role) {
    
        layui.use('table', function () {
            var table = layui.table;
            var tableOptions = {
                url: '${ctx}/rolfc/rolfc_findAll',
                method: 'POST',
                id: 'listReload',
                where: {
                    'role': role
                },
                page: true,
                request: {pageName: 'current', limitName: 'currentTotal'},
                response: {
                    statusName: 'error',
                    statusCode: 0,
                    msgName: 'message',
                    countName: 'total',
                    dataName: 'rolefunclist'
                }
            };
            table.init('table', tableOptions);
            //监听工具条
            table.on('tool(table)', function (obj) {
                var data = obj.data;
                if (obj.event === 'detail') {
                    layer.msg('ID：' + data.id + ' 的查看操作');
                } else if (obj.event === 'del') {
                        $.ajax({
                            url: ctx + "/rolfc/rolfc_delete",
                            type: "POST",
                            data: {
                            	 'tRoleFuncEntity.roleid':data.role_id,
                            	 'tRoleFuncEntity.orderNum':data.order,
                     		     'tRoleFuncEntity.funcId':data.func_id
                            },
                            dataType: "json",
                            beforeSend: function (xhr) {
            	                $(".delete").attr('disabled', "true");
            	            },
                            success: function (data) {
                                var error = parseInt(data.error);
                                if (error === 0) {
                                    obj.del();
                                }
                                layer.msg(data.message);
                                init($("#role").val());
                            },
                            error: function () {
                                layer.alert("连接服务器失败");
                            },
                            complete: function () {
                                layer.close(index);
                                $(".delete").removeAttr("disabled");
                            }
                        });
                
                }
                else if (obj.event === 'edit') {
                    //获取信息
                    $.ajax({
                        url: ctx + "/rolfc/rolfc_findOne",
                        type: "POST",
                        data: {
                        	 'tRoleFuncEntity.roleid':data.role_id,
                        	 'tRoleFuncEntity.orderNum':data.order,
                 		     'tRoleFuncEntity.funcId':data.func_id
                        },
                        dataType: "json",
                        success: function (data) {
                            if (data.error === 0) {
                                //设置值
                                var roleid2 = data.tRoleFuncEntity.roleid;
                                var order2 = data.tRoleFuncEntity.orderNum;
                                var func_id2 = data.tRoleFuncEntity.orderNum;
                                $("#role_id2").val(roleid2);
                                $("#order2").val(order2);
                                $("#func_id2").val(func_id2);
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
                                layer.title('更新信息-角色-功能id:' + role_id2, updateLayer);
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
    //点击角色功能列表刷新
   $("#roel_func").click(function(){
	   init($("#role").val()); 
   });
   //点击添加角色功能 刷新
   $("#addrolfc").click(function(){
	   getfunc($(".roleid").val());
	   
   });
   //点击重置
   $("#reset").click(function(){
	   getfunc("1");
   });
    //打开页面时加载一次数据
    init($("#role").val());
});