//控制层
//订单统计 wph
app.controller('countController' ,function($scope,$controller,$location,countService){

    $controller('baseController',{$scope:$scope});//继承

    //搜索
    $scope.searchEntity = {};
    $scope.search=function(page,rows){
        //起始时间
        // var oTimer1 = document.getElementById("timer1");
        // $scope.searchEntity.startTime = new Date(oTimer1.value).getTime();
        //终止时间
        // var oTimer2 = document.getElementById("timer2");
        // $scope.searchEntity.endTime = new Date(oTimer2.value).getTime();


        countService.search(page,rows,$scope.searchEntity).success(
            function(response){
                $scope.list=response.rows;
                $scope.paginationConf.totalItems=response.total;//更新总记录数
            }
        );
    }

});