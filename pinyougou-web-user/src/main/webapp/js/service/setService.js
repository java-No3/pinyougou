//服务层
app.service('setService',function($http){
    //用户注册
    this.regis=function(regEntity){
        return  $http.post('../user/regis.do',regEntity );
    }

});