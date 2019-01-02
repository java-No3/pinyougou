app.service('setService',function($http){
    //个人信息注册 wph
    this.regis=function(reg_entity){
        return  $http.post('../user/regis.do',reg_entity);
    }

});