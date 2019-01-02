app.controller('setController',function($scope,setService,uploadService){

    //个人信息注册 wph
    $scope.reg_entity = {};
    $scope.pro_entity = [];
    $scope.cit_entity = [];
    $scope.dis_entity = [];


    //个人信息注册 wph
    $scope.regis=function(){
        $scope.reg_entity.locus = $scope.pro_entity + $scope.cit_entity + $scope.dis_entity;
        $scope.reg_entity.birthday = $scope.year_entity + "-" + $scope.mon_entity + "-" + $scope.day_entity;
        setService.regis($scope.reg_entity).success(
            function(response){
                alert(response.message);
            }
        );
    }


    //信息注册之图片上传 wph
    $scope.uploadFile = function(){
        // 调用uploadService的方法完成文件的上传
        uploadService.uploadFile().success(function(response){
            if(response.flag){
                $scope.reg_entity.headPic = response.message;
            }else{
                alert(response.message);
            }
        });
    }

});