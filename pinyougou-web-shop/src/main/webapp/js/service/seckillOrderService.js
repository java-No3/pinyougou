app.service('seckillOrderService',function($http){

    //读取列表数据绑定到表单中
    this.findAll=function(){
        return $http.get('../orderQT/findAll.do');
    }
    //分页
    this.findPage=function(page,rows){
        return $http.get('../orderQT/findPage.do?page='+page+'&rows='+rows);
    }

    //秒杀订单查询
    this.search=function(page,rows,searchEntity){
        return $http.post('../orderQT/search.do?page='+page+"&rows="+rows, searchEntity);
    }
})