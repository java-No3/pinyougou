app.service('setService',function($http){
    //个人信息注册 wph
    this.regis=function(reg_entity){
        return  $http.post('../user/regis.do',reg_entity);
    }

    //个人信息回显 wph
    this.loadInfo=function(){
        return  $http.get('../user/loadInfo.do');
    }

});