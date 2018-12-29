// 定义服务层:
app.service("orderService",function($http){
    this.findAll = function(){
        return $http.get("../order/findAll.do");
    }

    this.findPage = function(page,rows){
        return $http.get("../order/findPage.do?pageNum="+page+"&pageSize="+rows);
    }

    this.add = function(entity){
        return $http.post("../order/add.do",entity);
    }

    this.update=function(entity){
        return $http.post("../order/update.do",entity);
    }

    this.findOne=function(orderId){
        return $http.get("../order/findOne.do?orderId="+orderId);
    }

    this.search = function(page,rows,searchEntity){
        return $http.post("../order/search.do?pageNum="+page+"&pageSize="+rows,searchEntity);
    }

    this.selectOptionList = function(){
        return $http.get("../order/selectOptionList.do");
    }
});