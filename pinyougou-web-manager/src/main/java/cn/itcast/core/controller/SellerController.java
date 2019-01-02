package cn.itcast.core.controller;

import cn.itcast.common.utils.ExportExcelUtils;
import cn.itcast.core.pojo.seller.Seller;
import cn.itcast.core.service.SellerService;
import com.alibaba.dubbo.config.annotation.Reference;

import entity.PageResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * szj
 * 商家管理
 */
@RestController
@RequestMapping("/seller")
public class SellerController {

    @Reference
    private SellerService sellerService;

    @RequestMapping("/search")
    public PageResult search(Integer page , Integer rows , @RequestBody(required = false) Seller seller){
        return sellerService.search(page,rows,seller);
    }

    @RequestMapping("/findOne")
    public Seller findOne (String id){
        return sellerService.findOne(id);
    }

    // 导出买家信息到excel szj
    @RequestMapping("/exportSellerInfoExcel")
    public void exportSellerInfoExcel (HttpServletRequest request, HttpServletResponse response){
        // 获取需要导出的数据
        List<Seller> sellerList = sellerService.findAll();
        //定义导出excel的名字
        String excelName="卖家统计表";
        // 获取需要转出的excle表头的map字段
        LinkedHashMap<String, String> fieldMap =new LinkedHashMap<String, String>() ;
        fieldMap.put("sellerId", "用户ID");
        fieldMap.put("name", "公司名");
        fieldMap.put("nickName", "店铺名称");
        fieldMap.put("linkmanName", "联系人姓名");
        fieldMap.put("telephone", "公司电话");
        fieldMap.put("status", "状态");
        //导出用户相关信息
        new ExportExcelUtils().export(excelName, sellerList, fieldMap, response);
    }
}
