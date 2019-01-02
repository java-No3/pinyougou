//控制层
//订单统计 wph
app.controller('countController' ,function($scope,$controller,$location,countService){

    $controller('baseController',{$scope:$scope});//继承

    $scope.searchMap={'page':1,'rows':5,'startTime':'','endTime':'','title':'','sellerId':'','sortField':'','sort':''};

    //搜索
    $scope.search=function(page,rows){
        if (null == $scope.searchMap.sortField) {
            $scope.searchMap.page = page;
            $scope.searchMap.rows = rows;
        }

        countService.search($scope.searchMap).success(
            $scope.searchMap.page = page;
            $scope.searchMap.rows = rows;
            function(response){
                $scope.list=response.rows;
                $scope.paginationConf.totalItems=response.total;//更新总记录数
            }
        );
    }

    //排序查询
    $scope.sortSearch=function(sortField,sort){
        $scope.searchMap.sortField=sortField;
        $scope.searchMap.sort=sort;

        $scope.search();//查询
    }

});