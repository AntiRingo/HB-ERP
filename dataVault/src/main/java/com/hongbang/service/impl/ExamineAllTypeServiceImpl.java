package com.hongbang.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.hongbang.mapper.ExamineAllTypeMapper;
import com.hongbang.mapper.ExamineConditionContentMapper;
import com.hongbang.pojo.ExamineAllType;
import com.hongbang.pojo.ExamineConditionContent;
import com.hongbang.service.ExamineAllTypeService;
import com.hongbang.util.SqlSessionFactoryUtils;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import java.util.List;

public class ExamineAllTypeServiceImpl implements ExamineAllTypeService {
    //获取工厂
    SqlSessionFactory factory = SqlSessionFactoryUtils.getSqlSessionFactory();

    //根据出入库类型查询审核步骤
    public List<ExamineAllType> selectStep(int typeId){
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        ExamineAllTypeMapper mapper = sqlSession.getMapper(ExamineAllTypeMapper.class);
        //调用mapper
        List<ExamineAllType> examineAllTypes = mapper.selectStep(typeId);
        //释放资源
        sqlSession.close();
        //返回值
        return examineAllTypes;
    }

    //添加步骤
    public void add(String s){


        List<List> lists = JSONArray.parseArray(s, List.class);
        List list = lists.get(0);
        List list1 = lists.get(1);
        String s1 = JSON.toJSONString(list);
        String s2 = JSON.toJSONString(list1);
        List<ExamineAllType> examineAllTypes = JSONArray.parseArray(s1, ExamineAllType.class);
        List<ExamineConditionContent> examineConditionContents = JSONArray.parseArray(s2, ExamineConditionContent.class);
        ExamineAllType examineAllType = examineAllTypes.get(0);

        //调用mapper
        //获取session
        SqlSession sqlSession = factory.openSession();

        //获取mapper
        ExamineAllTypeMapper mapper = sqlSession.getMapper(ExamineAllTypeMapper.class);
        //获取步骤
        int typeId = examineAllType.getTypeId();
        int i = mapper.selectExistCount(typeId);
        int x= i+1;
        examineAllType.setStep(x);
        mapper.add(examineAllType);
        int id = examineAllType.getId();



        ExamineConditionContentMapper mapper1 = sqlSession.getMapper(ExamineConditionContentMapper.class);

        //获取现在的条件进行添加
        if (examineConditionContents.size()>0){
            for (int j = 0; j < examineConditionContents.size(); j++) {
                ExamineConditionContent examineConditionContent = examineConditionContents.get(j);
                examineConditionContent.setExamineStepId(id);
            }
            mapper1.addAll(examineConditionContents);
        }

        //提交数据
        sqlSession.commit();
        //释放资源
        sqlSession.close();

    }

    //删除
    public void delete(int id){
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        ExamineAllTypeMapper mapper = sqlSession.getMapper(ExamineAllTypeMapper.class);
        //调用mapper
        ExamineAllType examineAllType = mapper.selectById(id);
        int step = examineAllType.getStep();
        int typeId = examineAllType.getTypeId();
        mapper.updateStep(step,typeId);
        mapper.delete(id);
        //提交事务
        sqlSession.commit();
        //释放资源
        sqlSession.close();
    }

    //修改
    public void update(String s){
        List<List> lists = JSONArray.parseArray(s, List.class);
        List list = lists.get(0);
        List list1 = lists.get(1);
        String s1 = JSON.toJSONString(list);
        String s2 = JSON.toJSONString(list1);
        List<ExamineAllType> examineAllTypes = JSONArray.parseArray(s1, ExamineAllType.class);
        List<ExamineConditionContent> examineConditionContents = JSONArray.parseArray(s2, ExamineConditionContent.class);
        ExamineAllType examineAllType = examineAllTypes.get(0);
        //调用mapper
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        ExamineAllTypeMapper mapper = sqlSession.getMapper(ExamineAllTypeMapper.class);
        //调用mapper
        //先删除原来的步骤，然后再添加新的步骤
        int id = examineAllType.getId();
        mapper.delete(id);
        //添加新的步骤
        mapper.add(examineAllType);
        //获取新添加的步骤id
        int id1 = examineAllType.getId();
        //删除原来的审核条件数据
        ExamineConditionContentMapper mapper1 = sqlSession.getMapper(ExamineConditionContentMapper.class);
        mapper1.deleteByStep(id);

        //获取现在的条件进行添加
        if (examineConditionContents.size()>0){
            for (int i = 0; i < examineConditionContents.size(); i++) {
                ExamineConditionContent examineConditionContent = examineConditionContents.get(i);
                examineConditionContent.setExamineStepId(id1);
            }

            mapper1.addAll(examineConditionContents);
        }

        //提交数据
        sqlSession.commit();
        //释放资源
        sqlSession.close();

    }

    //查询审核步骤中是否已经存在该部门了
   public boolean selectDepartExist(ExamineAllType examineAllType){
        //获取session
       SqlSession sqlSession = factory.openSession();
       //获取mapper
       ExamineAllTypeMapper mapper = sqlSession.getMapper(ExamineAllTypeMapper.class);
       //调用mapper
       boolean b = mapper.selectDepartExist(examineAllType);
       //释放资源
       sqlSession.close();
       //返回值
       return b;
   }

    //查询审核步骤中是否已经存在该部门了(修改时使用)
   public boolean selectDepartExistUpdate(ExamineAllType examineAllType){
        //获取session
       SqlSession sqlSession = factory.openSession();
       //获取mapper
       ExamineAllTypeMapper mapper = sqlSession.getMapper(ExamineAllTypeMapper.class);
       //调用mapper
       boolean b = mapper.selectDepartExistUpdate(examineAllType);
       //释放资源
       sqlSession.close();
       //返回值
       return b;
   }


    //查看该类型的审核流程有几步
    public int selectExistCount(int typeId){
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        ExamineAllTypeMapper mapper = sqlSession.getMapper(ExamineAllTypeMapper.class);
        //调用mapper
        int i = mapper.selectExistCount(typeId);
        //释放资源
        sqlSession.close();
        //返回值
        return i;
    }

    //删除步骤后改类型大于这个步骤的-1
   public void updateStep( int step, int typeId){
        //获取session
       SqlSession sqlSession = factory.openSession();
       //获取mapper
       ExamineAllTypeMapper mapper = sqlSession.getMapper(ExamineAllTypeMapper.class);
       //调用mapper
       mapper.updateStep(step,typeId);
       //提交事务
       sqlSession.commit();
       //释放资源
       sqlSession.close();
   }

    //插入步骤，大于等于该步骤的都+1
   public void updateStepAdd( int step, int typeId){
       //获取session
       SqlSession sqlSession = factory.openSession();
       //获取mapper
       ExamineAllTypeMapper mapper = sqlSession.getMapper(ExamineAllTypeMapper.class);
       //调用mapper
       mapper.updateStepAdd(step,typeId);
       //提交事务
       sqlSession.commit();
       //释放资源
       sqlSession.close();
   }

    //根据id查询
    public ExamineAllType selectById(int id){
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        ExamineAllTypeMapper mapper = sqlSession.getMapper(ExamineAllTypeMapper.class);
        //调用mapper
        ExamineAllType examineAllType = mapper.selectById(id);
        //释放资源
        sqlSession.close();
        //返回数据
        return examineAllType;
    }

    //插入
    public void insert(String s){


        List<List> lists = JSONArray.parseArray(s, List.class);
        List list = lists.get(0);
        List list1 = lists.get(1);
        String s1 = JSON.toJSONString(list);
        String s2 = JSON.toJSONString(list1);
        List<ExamineAllType> examineAllTypes = JSONArray.parseArray(s1, ExamineAllType.class);
        List<ExamineConditionContent> examineConditionContents = JSONArray.parseArray(s2, ExamineConditionContent.class);
        ExamineAllType examineAllType = examineAllTypes.get(0);

        //调用mapper
        //获取session
        SqlSession sqlSession = factory.openSession();

        //获取mapper
        ExamineAllTypeMapper mapper = sqlSession.getMapper(ExamineAllTypeMapper.class);

        int step = examineAllType.getStep();
        int step1 = 0;
        if (step==1){
            step1=1;
        }
        else if (step>1){
            step1 = step;
        }

        examineAllType.setStep(step1);
        //给大于等于step的+1
        int typeId = examineAllType.getTypeId();
        mapper.updateStepAdd(step,typeId);

        //插入
        mapper.insert(examineAllType);
        int id = examineAllType.getId();

        ExamineConditionContentMapper mapper1 = sqlSession.getMapper(ExamineConditionContentMapper.class);

        //获取现在的条件进行添加
        if (examineConditionContents.size()>0){
            for (int j = 0; j < examineConditionContents.size(); j++) {
                ExamineConditionContent examineConditionContent = examineConditionContents.get(j);
                examineConditionContent.setExamineStepId(id);
            }
            mapper1.addAll(examineConditionContents);
        }

        //提交数据
        sqlSession.commit();
        //释放资源
        sqlSession.close();

    }
}
