//控制层
//订单查询 wph
app.controller('ordersController' ,function($scope,$controller,$location,ordersService){

    $controller('baseController',{$scope:$scope});//继承

    //搜索
    $scope.searchEntity = {};
    $scope.search=function(page,rows){
        ordersService.search(page,rows,$scope.searchEntity).success(
            function(response){
                $scope.list=response.rows;
                $scope.paginationConf.totalItems=response.total;//更新总记录数
            }
        );
    }

    //显示状态：1、未付款，2、已付款，3、未发货，4、已发货，5、交易成功，6、交易关闭,7、待评价
    $scope.auditStatus = ["","未付款","已付款","未发货","已发货","交易成功","交易关闭","待评价"];

    //支付类型，1、在线支付，2、货到付款
    $scope.payType = ["","在线支付","货到付款"];
});