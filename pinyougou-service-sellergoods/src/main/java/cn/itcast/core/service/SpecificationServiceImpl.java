package cn.itcast.core.service;

import cn.itcast.core.dao.specification.SpecificationDao;
import cn.itcast.core.dao.specification.SpecificationOptionDao;
import cn.itcast.core.pojo.specification.Specification;
import cn.itcast.core.pojo.specification.SpecificationOption;
import cn.itcast.core.pojo.specification.SpecificationOptionQuery;
import cn.itcast.core.pojo.specification.SpecificationQuery;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import entity.PageResult;
import entity.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import pojogroup.SpecificationVo;

import java.util.List;
import java.util.Map;

/**
 * 规格管理ZQ
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
        //条件查询
        SpecificationQuery query = new SpecificationQuery();
        query.createCriteria().andstatusEqualTo("0");
        Page<Specification> p =null;
        if (specification.getId()!=(long)1){
             p = (Page<Specification>) specificationDao.selectByExample(query);
        }else {
            //查询 分页
            p = (Page<Specification>) specificationDao.selectByExample(null);
        }
        return new PageResult(p.getTotal(),p.getResult());
    }

    @Override
    public void add(SpecificationVo vo) {
        //规格表  返回值 ID
        vo.getSpecification().setStatus("0");
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

    /**
     * ZQ 12.28
     * @param ids
     */
    @Override
    public void commit(Long[] ids) {
        for (Long id : ids) {
            Specification specification = specificationDao.selectByPrimaryKey(id);
            if ("0".equals(specification.getStatus())){
                specification.setStatus("1");
                specificationDao.updateByPrimaryKeySelective(specification);
                return;
            }
            if ("1".equals(specification.getStatus())){
                specification.setStatus("2");
                specificationDao.updateByPrimaryKeySelective(specification);
            }
        }

    }


}
