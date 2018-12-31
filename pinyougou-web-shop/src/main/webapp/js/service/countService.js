//服务层
//订单统计 wph
app.service('countService',function($http){
    //搜索
    this.search=function(page,rows,searchEntity){
        return $http.post('../orders/count.do?page='+page+"&rows="+rows,searchEntity);
    }
});