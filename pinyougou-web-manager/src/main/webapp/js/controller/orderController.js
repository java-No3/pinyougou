// 定义控制器:
app.controller("orderController",function($scope,$controller,$http,orderService){
    // AngularJS中的继承:伪继承
    $controller('baseController',{$scope:$scope});

    // 查询所有的订单列表的方法:
    $scope.findAll = function(){
        // 向后台发送请求:
        orderService.findAll().success(function(response){
            $scope.list = response;
        });
    }

    // 分页查询
    $scope.findPage = function(page,rows){
        // 向后台发送请求获取数据:
        orderService.findPage(page,rows).success(function(response){
            $scope.paginationConf.totalItems = response.total;
            $scope.list = response.rows;
        });
    }

    // 保存订单的方法:
    $scope.save = function(){
        // 区分是保存还是修改
        var object;
        if($scope.entity.orderId != null){
            // 更新
            object = orderService.update($scope.entity);
        }else{
            // 保存
            object = orderService.add($scope.entity);
        }
        object.success(function(response){
            // {flag:true,message:xxx}
            // 判断保存是否成功:
            if(response.flag){
                // 保存成功
                alert(response.message);
                $scope.reloadList();
            }else{
                // 保存失败
                alert(response.message);
            }
        });
    }

    // 查询一个:
    $scope.findById = function(orderId){
        orderService.findOne(orderId).success(function(response){
            // {id:xx,name:yy,firstChar:zz}
            $scope.entity = response;
        });
    }

    $scope.searchEntity={};

    // 假设定义一个查询的实体：searchEntity
    $scope.search = function(page,rows){
        // 向后台发送请求获取数据:
        orderService.search(page,rows,$scope.searchEntity).success(function(response){
            $scope.paginationConf.totalItems = response.total;
            $scope.list = response.rows;
        });
    }

    // 导出excel  szj
    $scope.exportExcel=function(){

        orderService.exportExcel().success(
            function(response){


            }
        );
    }



});
