package com.hongbang.service.impl;

import com.hongbang.mapper.UserMapper;
import com.hongbang.pojo.ApplicationForm;
import com.hongbang.pojo.Function;
import com.hongbang.pojo.FunctionTwo;
import com.hongbang.pojo.Module;
import com.hongbang.pojo.User;
import com.hongbang.service.UserService;
import com.hongbang.util.SqlSessionFactoryUtils;
import org.apache.ibatis.exceptions.PersistenceException;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import java.util.List;
import java.util.Map;

public class UserServiceImpl implements UserService {

    //获取工厂
    SqlSessionFactory factory = SqlSessionFactoryUtils.getSqlSessionFactory();

    //添加用户
    @Override
    public void add(User user){
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        UserMapper mapper = sqlSession.getMapper(UserMapper.class);
        //调用mapper
        mapper.add(user);
        //提交事务
        sqlSession.commit();
        //释放资源
        sqlSession.close();
    }

    //修改用户权限
//    public void updatePurview(User user){
//        //获取session
//        SqlSession sqlSession = factory.openSession();
//        //获取mapper
//        UserMapper mapper = sqlSession.getMapper(UserMapper.class);
//        //调用mapper
//        mapper.updatePurview(user);
//        //提交事务
//        sqlSession.commit();
//        //释放资源
//        sqlSession.close();
//    }

    //查询所有用户信息
    @Override
    public List<User>selectAll(){
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        UserMapper mapper = sqlSession.getMapper(UserMapper.class);
        //调用mapper
        List<User> users = mapper.selectAll();
        //释放资源
        sqlSession.close();
        //返回值
        return users;
    }


    //删除用户信息
    @Override
    public void delete(int id){
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        UserMapper mapper = sqlSession.getMapper(UserMapper.class);
        //调用mapper
        mapper.delete(id);
        //提交事务
        sqlSession.commit();
        //释放资源
        sqlSession.close();
    }

    //根据id查询用户信息
    @Override
    public User selectById(int id){
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        UserMapper mapper = sqlSession.getMapper(UserMapper.class);
        //调用mapper
        User users = mapper.selectById(id);
        //释放资源
        sqlSession.close();
        //返回值
        return users;
    }


    /*
     * 登录方法
     * */
    @Override
    public User login(String username, String password){
        //2、获取SqlSession
        SqlSession sqlSession = factory.openSession();
        //3、通过SqlSession获取对应的UserMapper
        UserMapper mapper = sqlSession.getMapper(UserMapper.class);
        //4、通过UserMapper调用根据用户名密码查询的方法
        User user = mapper.select(username, password);
        //5、释放资源
        sqlSession.close();
        return user;
    }

    //最高权限人员查询用户信息除了自己的
    @Override
    public List<Map<String,Object>>userNoMe(int id,int level){
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        UserMapper mapper = sqlSession.getMapper(UserMapper.class);
        //调用mapper
        List<Map<String, Object>> maps = mapper.UserNoME(id,level);

        //释放资源
        sqlSession.close();
        //返回值
        return maps;
    }

    //新增用户的时候检测登录名是否重复
    @Override
    public boolean selectUserExist(String username){
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        UserMapper mapper = sqlSession.getMapper(UserMapper.class);
        //调用mapper
        boolean b = mapper.selectUserExist(username);
        //释放资源
        sqlSession.close();
        return b;
    }

    //超级管理员修改用户信息时验证用户名是否重复，自身除外
    @Override
    public boolean selectUserExistUpdate(int id, String userName){
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        UserMapper mapper = sqlSession.getMapper(UserMapper.class);
        //调用mapper
        boolean b = mapper.selectUserExistUpdate(id, userName);
        //释放资源
        sqlSession.close();
        return b;
    }


    //管理员修改用户信息
    @Override
    public void adminUpdateUser(User user){
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        UserMapper mapper = sqlSession.getMapper(UserMapper.class);
        //调用mapper
        mapper.adminUpdateUser(user);
        //提交事务
        sqlSession.commit();
        //释放资源
        sqlSession.close();
    }

    //用户查询自己的信息：姓名、年龄、性别、部门
    @Override
    public List<Map<String,Object>>userSelectPersonal(int id){
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        UserMapper mapper = sqlSession.getMapper(UserMapper.class);
        //调用mapper
        List<Map<String,Object>> users = mapper.userSelectPersonal(id);
        //释放资源
        sqlSession.close();
        //返回值
        return users;
    }

    //用户更新个人基本信息：姓名、年龄、性别
   public void userUpdatePersonal(User user){
       //获取session
       SqlSession sqlSession = factory.openSession();
       //获取mapper
       UserMapper mapper = sqlSession.getMapper(UserMapper.class);
       //调用mapper
       mapper.userUpdatePersonal(user);
       //提交事务
       sqlSession.commit();
       //释放资源
       sqlSession.close();
   }

   //根据id修改密码
    public void updatePassword(String passWord,int id){
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        UserMapper mapper = sqlSession.getMapper(UserMapper.class);
        //调用mapper
        mapper.updatePassword(passWord, id);
        //提交事务
        sqlSession.commit();
        //释放资源
        sqlSession.close();
    }

    //根据ID修改二级密码
    public void updateSecondaryPassword(String secondaryPassword,int id){
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        UserMapper mapper = sqlSession.getMapper(UserMapper.class);
        //调用mapper
        mapper.updateSecondaryPassword(secondaryPassword,id);
        //提交事务
        sqlSession.commit();
        //释放资源
        sqlSession.close();
    }


    //部门管理人查询自己部门的信息
   public List<Map<String,Object>> managerSelectUser(int department,int id){
       //获取session
       SqlSession sqlSession = factory.openSession();
       //获取mapper
       UserMapper mapper = sqlSession.getMapper(UserMapper.class);
       //调用mapper
       List<Map<String, Object>> maps = mapper.managerSelectUser(department, id);
       //释放资源
       sqlSession.close();
       //返回值
       return maps;
   }

    //根据userid查询该用户所拥有的模块
   public List<Module>selectModuleByUserId(int userId){
       //获取session
       SqlSession sqlSession = factory.openSession();
       //获取mapper
       UserMapper mapper = sqlSession.getMapper(UserMapper.class);
       //调用mapper
       List<Module> modules = mapper.selectModuleByUserId(userId);
       //释放资源
       sqlSession.close();
       //返回值
       return modules;
   }

    //根据userID查询该用户所拥有的功能
   public List<Function> selectUserFunctionByUserId(int userId){
       //获取session
       SqlSession sqlSession = factory.openSession();
       //获取mapper
       UserMapper mapper = sqlSession.getMapper(UserMapper.class);
       //调用mapper
       List<Function> userFunctions = mapper.selectUserFunctionByUserId(userId);
       //释放资源
       sqlSession.close();
       //返回值
       return userFunctions;
   }


    //根据用户ID查询用户的级别
    public int selectLevelById(int id){
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        UserMapper mapper = sqlSession.getMapper(UserMapper.class);
        //调用mapper
        int i = mapper.selectLevelById(id);
        //释放资源
        sqlSession.close();
        //返回值
        return i;
    }

    //查询所有的功能。一级
    public List<Module>selectAllModule(){
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        UserMapper mapper = sqlSession.getMapper(UserMapper.class);
        //调用mapper
        List<Module> modules = mapper.selectAllModule();
        //释放资源
        sqlSession.close();
        //返回值
        return modules;
    }


    //查询所有的功能。三级
   public List<FunctionTwo>selectAllFunctionThree(){
       //获取session
       SqlSession sqlSession = factory.openSession();
       //获取mapper
       UserMapper mapper = sqlSession.getMapper(UserMapper.class);
       //调用mapper
       List<FunctionTwo> functionTwos = mapper.selectAllFunctionThree();
       //释放资源
       sqlSession.close();
       //返回值
       return functionTwos;

   }


    //部长查询所有的功能，包括自己部门的所有功能和通用功能（一级），根据部门ID查询
   public List<Module>selectBZModule(int depart_id){
       //获取session
       SqlSession sqlSession = factory.openSession();
       //获取mapper
       UserMapper mapper = sqlSession.getMapper(UserMapper.class);
       //调用mapper
       List<Module> modules = mapper.selectBZModule(depart_id);
       //释放资源
       sqlSession.close();
       //返回值
       return modules;
   }

    //部长查询所有的功能，包括自己部门的所有功能和通用功能（二级），根据部门ID查询
   public List<Function>selectBZFunction(int departId){
       //获取session
       SqlSession sqlSession = factory.openSession();
       //获取mapper
       UserMapper mapper = sqlSession.getMapper(UserMapper.class);
       //调用mapper
       List<Function> functions = mapper.selectBZFunction(departId);
       //释放资源
       sqlSession.close();
       //返回值
       return functions;
   }

    //部长查询所有的功能，包括自己部门的所有功能和通用功能（三级），根据部门ID查询
    public List<FunctionTwo>selectBZFunctionTwo(int depart_id){
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        UserMapper mapper = sqlSession.getMapper(UserMapper.class);
        //调用mapper
        List<FunctionTwo> functionTwos = mapper.selectBZFunctionTwo(depart_id);
        //释放资源
        sqlSession.close();
        //返回值
        return functionTwos;
    }

    //查询该部门下是否有人员存在
    public boolean selectIfUserDepartment(int department){
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        UserMapper mapper = sqlSession.getMapper(UserMapper.class);
        //调用mapper
        boolean b = mapper.selectIfUserDepartment(department);
        //释放资源
        sqlSession.close();
        //返回值
        return b;
    }


    //心跳使用

   public int heart(){
       //获取session
       SqlSession sqlSession = factory.openSession();
       //获取mapper
       UserMapper mapper = sqlSession.getMapper(UserMapper.class);
       //调用mapper
       int heart = mapper.heart();
       System.out.println(heart);
       //释放资源
       sqlSession.close();
       //返回值
       return heart;

   }


    //根据用户ID查询保留时间
   public Map<String,Object> selectExitTime(int id){
        //获取session
       SqlSession sqlSession = factory.openSession();
       //获取mapper
       UserMapper mapper = sqlSession.getMapper(UserMapper.class);
       //调用mapper
       Map<String, Object> map = mapper.selectExitTime(id);
       //释放资源
       sqlSession.close();
       //返回值
       return map;
   }


    //查询该部门下是否已经存在部长级别的账号了
   public boolean selectLevelTwoExist(int department){
        //获取session
       SqlSession sqlSession = factory.openSession();
       //获取mapper
       UserMapper mapper = sqlSession.getMapper(UserMapper.class);
       //调用mapper
       boolean b = mapper.selectLevelTwoExist(department);
       //释放资源
       sqlSession.close();
       //返回值
       return b;
   }

    //查询该部门下是否已经存在部长级别的账号了(更新时使用)
    public boolean updateLevelTwoExist(int department,int id){
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        UserMapper mapper = sqlSession.getMapper(UserMapper.class);
        //调用mapper
        boolean b = mapper.updateLevelTwoExist(department,id);
        //释放资源
        sqlSession.close();
        //返回值
        return b;
    }

    //根据用户id查询用户名，名称，部门
    public List<Map<String,Object>> selectDepartById (int id){
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        UserMapper mapper = sqlSession.getMapper(UserMapper.class);
        //调用mapper
        List<Map<String, Object>> maps = mapper.selectDepartById(id);
        //释放资源
        sqlSession.close();
        //返回值
        return maps;
    }


    //重置密码为默认状态
   public void resetPassWord(int id){
        //获取session
       SqlSession sqlSession = factory.openSession();
       //获取mapper
       UserMapper mapper = sqlSession.getMapper(UserMapper.class);
       //调用mapper
       mapper.resetPassWord(id);
       //提交事务
       sqlSession.commit();
       //释放资源
       sqlSession.close();
   }


    //根据用户ID以及用户输入的二级密码来判断二级密码是否正确
   public boolean secondaryPasswordExamine(int id,String secondaryPassword){
        //获取session
       SqlSession sqlSession = factory.openSession();
       //获取mapper
       UserMapper mapper = sqlSession.getMapper(UserMapper.class);
       //调用mapper
       boolean b = mapper.secondaryPasswordExamine(id, secondaryPassword);
       //释放资源
       sqlSession.close();
       //返回值
       return b;
   }

    //测试30秒是否有用
    public void executeLongQuery(){

        try (SqlSession session = factory.openSession()) {
            UserMapper mapper = session.getMapper(UserMapper.class);
            mapper.executeLongQuery(); // 触发超时
        } catch (PersistenceException e) {
            // 捕获 MyBatis 抛出的超时异常
            System.err.println("捕获到 MyBatis 异常: " + e.getMessage());
            e.printStackTrace();
        }
    }

    //根据多个用户ID查询
    public List<Map<String,Object>> selectDepartByIds (List<ApplicationForm> applicationForms){
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        UserMapper mapper = sqlSession.getMapper(UserMapper.class);
        //调用mapper
        List<Map<String, Object>> maps = mapper.selectDepartByIds(applicationForms);
        //释放资源
        sqlSession.close();
        //返回值
        return maps;
    }


    //查询部门下所有的人员信息
    public List<User>selectDepartmentUser(int department){
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        UserMapper mapper = sqlSession.getMapper(UserMapper.class);
        //调用mapper
        List<User> users = mapper.selectDepartmentUser(department);
        //释放资源
        sqlSession.close();
        //返回值
        return users;
    }


    // 查询用户信息（根据您的表结构）
   public Map<String, Object> selectUserInfo(int id){
       //获取session
       SqlSession sqlSession = factory.openSession();
       //获取mapper
       UserMapper mapper = sqlSession.getMapper(UserMapper.class);
       //调用mapper
       Map<String, Object> map = mapper.selectUserInfo(id);
       //释放资源
       sqlSession.close();
       //返回值
       return map;
   }


    //查询所以的用户信息
   public List<Map<String, Object>> selectAllUser(){
       //获取session
       SqlSession sqlSession = factory.openSession();
       //获取mapper
       UserMapper mapper = sqlSession.getMapper(UserMapper.class);
       //调用mapper
       List<Map<String, Object>> users = mapper.selectAllUser();
       //释放资源
       sqlSession.close();
       //返回值
       return users;
   }
}
