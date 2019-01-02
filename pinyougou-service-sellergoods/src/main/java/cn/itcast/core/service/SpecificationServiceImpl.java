package cn.itcast.core.service;

import cn.itcast.core.dao.specification.SpecificationDao;
import cn.itcast.core.dao.specification.SpecificationOptionDao;
import cn.itcast.core.pojo.good.Brand;
import cn.itcast.core.pojo.specification.Specification;
import cn.itcast.core.pojo.specification.SpecificationOption;
import cn.itcast.core.pojo.specification.SpecificationOptionQuery;
import cn.itcast.core.pojo.specification.SpecificationQuery;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import entity.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import pojogroup.SpecificationVo;

import java.util.List;
import java.util.Map;

/**
 * 规格管理
 */
@Service
@Transactional
public class SpecificationServiceImpl implements  SpecificationService {


    @Autowired
    private SpecificationDao specificationDao;
    @Autowired
    private SpecificationOptionDao specificationOptionDao;
    @Override
    public PageResult search(Integer page, Integer rows, Specification specification) {
        //分页插件
        PageHelper.startPage(page,rows);
        //查询 分页
        Page<Specification> p = (Page<Specification>) specificationDao.selectByExample(null);

        return new PageResult(p.getTotal(),p.getResult());
    }

    @Override
    public void add(SpecificationVo vo) {
        //规格表  返回值 ID
        specificationDao.insertSelective(vo.getSpecification());
        //规格选项表结果集
        List<SpecificationOption> specificationOptionList = vo.getSpecificationOptionList();
        for (SpecificationOption specificationOption : specificationOptionList) {
            //外键
            specificationOption.setSpecId(vo.getSpecification().getId());
            //保存
            specificationOptionDao.insertSelective(specificationOption);
        }

    }

    @Override
    public SpecificationVo findOne(Long id) {

        SpecificationVo vo = new SpecificationVo();
        //规格对象
        Specification specification = specificationDao.selectByPrimaryKey(id);
        vo.setSpecification(specification);
        //规格选项对象结果集
        SpecificationOptionQuery query = new SpecificationOptionQuery();
        query.createCriteria().andSpecIdEqualTo(id);
        //排序query
        query.setOrderByClause("orders desc");



        List<SpecificationOption> specificationOptions = specificationOptionDao.selectByExample(query);
        vo.setSpecificationOptionList(specificationOptions);
        return vo;
    }

    @Override
    public void update(SpecificationVo vo) {

        //规格修改
        specificationDao.updateByPrimaryKeySelective(vo.getSpecification());
        //规格选项
        //1:先删除  外键
        SpecificationOptionQuery query = new SpecificationOptionQuery();
        query.createCriteria().andSpecIdEqualTo(vo.getSpecification().getId());
        specificationOptionDao.deleteByExample(query);
        //2:再添加
        List<SpecificationOption> specificationOptionList = vo.getSpecificationOptionList();
        for (SpecificationOption specificationOption : specificationOptionList) {
            //外键
            specificationOption.setSpecId(vo.getSpecification().getId());
            //保存
            specificationOptionDao.insertSelective(specificationOption);
        }





    }

    @Override
    public List<Map> selectOptionList() {
        return specificationDao.selectOptionList();
    }

    // excel导入数据库 szj
    @Override
    public void importSpecification(String[] string) throws Exception{
//        if (null != string && 1 < string.length){
        String name = string[0];

       //  String firstChar = string[1];
        Specification specification = new Specification();
        specification.setSpecName(name);
        specificationDao.insert(specification);

    }

    @Override
    public void importSpecificationList(String[] string) throws Exception {
        String name = string[0];
        String name2 = string[1];
        String orders = string[2];
        SpecificationOption specificationOption = new SpecificationOption();
        specificationOption.setOptionName(name2);
        SpecificationQuery query = new SpecificationQuery();
        SpecificationQuery.Criteria criteria = query.createCriteria();
        criteria.andSpecNameEqualTo(name);
        List<Specification> specifications = specificationDao.selectByExample(query);
        Specification specification1 = specifications.get(0);
        Long id = specification1.getId();
        specificationOption.setSpecId(id);
        specificationOption.setOrders(Integer.parseInt(orders));
        specificationOptionDao.insert(specificationOption);
    }
//    }
}
