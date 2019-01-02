app.controller('seckillGoodsController1' ,function($scope,$controller,goodsService,seckillGoodsService) {

    //查询实体
    $scope.seckilApply=function(){
        var id = $location.search()['id'];
        if(null == id){
            return;
        }
        goodsService.findOne(id).success(
            function(response){
                $scope.entity= response;

                // 调用处理富文本编辑器：
                editor.html($scope.entity.goodsDesc.introduction);

                // 处理图片列表，因为图片信息保存的是JSON的字符串，让前台识别为JSON格式对象
                $scope.entity.goodsDesc.itemImages = JSON.parse( $scope.entity.goodsDesc.itemImages );

                // 处理扩展属性:
                $scope.entity.goodsDesc.customAttributeItems = JSON.parse( $scope.entity.goodsDesc.customAttributeItems );

                // 处理规格
                $scope.entity.goodsDesc.specificationItems = JSON.parse($scope.entity.goodsDesc.specificationItems);

                // 遍历SKU的集合:
                for(var i=0;i<$scope.entity.itemList.length;i++){
                    $scope.entity.itemList[i].spec = JSON.parse( $scope.entity.itemList[i].spec );
                }
            }
        );
    }

    //读取列表数据绑定到表单中
    $scope.findAll=function(){
        seckillGoodsService.findAll().success(
            function(response){
                $scope.list=response;
            }
        );
    }

    //分页
    $scope.findPage=function(page,rows){
        seckillGoodsService.findPage(page,rows).success(
            function(response){
                $scope.list=response.rows;
                $scope.paginationConf.totalItems=response.total;//更新总记录数
            }
        );
    }


    //秒杀商品添加
    $scope.add=function(){
        seckillGoodsService.add( $scope.entity).success(
            function(response){
                if(response.flag){
                    alert(response.message);
                    location.href="seckillgoods.html";
                }else{
                    alert(response.message);
                }
            }
        );
    }
})


$scope.searchEntity={};//定义搜索对象
//搜索
$scope.search=function(page,rows){
    goodsService.search(page,rows,$scope.searchEntity).success(
        function(response){
            $scope.list=response.rows;
            $scope.paginationConf.totalItems=response.total;//更新总记录数
        }
    );
}

// 显示状态
$scope.status = ["未审核","审核通过","审核未通过","关闭"];

// 审核的方法:
$scope.updateStatus = function(status) {
    goodsService.updateStatus($scope.selectIds, status).success(function (response) {
        if (response.flag) {
            $scope.reloadList();//刷新列表
            $scope.selectIds = [];
        } else {
            alert(response.message);
        }
    });
}
