//首页控制器
app.controller('setController',function($scope,setService,uploadService){

    //用户注册
    $scope.regis=function(){
        setService.regis($scope.regEntity).success(
            function(response){
                alert(response.message);
            }
        );
    }

    $scope.uploadFile = function(){
        // 调用uploadService的方法完成文件的上传
        uploadService.uploadFile().success(function(response){
            if(response.flag){
                alert(response.message);
                $scope.reg_entity.headPic = response.message;
                alert("haha");

            }else{
                alert(response.message);
            }
        });
    }

});