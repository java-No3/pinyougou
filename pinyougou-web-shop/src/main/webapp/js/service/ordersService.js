//服务层
//订单查询 wph
app.service('ordersService',function($http){
    //搜索
    this.search=function(page,rows,searchEntity){
        return $http.post('../orders/search.do?page='+page+"&rows="+rows, searchEntity);
    }
});