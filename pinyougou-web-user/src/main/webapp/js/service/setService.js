//服务层
app.service('setService',function($http){
    //用户注册
    this.regis=function(reg_entity){
        return  $http.post('../user/regis.do',reg_entity);
    }

});