package com.hongbang.service.impl;

import com.hongbang.mapper.AuthorUserMapper;
import com.hongbang.mapper.BomPurviewReviewMapper;
import com.hongbang.mapper.DepartmentMapper;
import com.hongbang.mapper.UserMapper;
import com.hongbang.pojo.*;
import com.hongbang.service.AuthorUserService;
import com.hongbang.util.SqlSessionFactoryUtils;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AuthorUserServiceImpl implements AuthorUserService {
    //获取工厂
    SqlSessionFactory factory = SqlSessionFactoryUtils.getSqlSessionFactory();


    //查询自己管理的人员
    public List<UserPickerData> selectByAuthor(int authorId){
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        AuthorUserMapper mapper = sqlSession.getMapper(AuthorUserMapper.class);
        //获取mapper
        DepartmentMapper mapper1 = sqlSession.getMapper(DepartmentMapper.class);
        UserMapper mapper2 = sqlSession.getMapper(UserMapper.class);
        List<UserPickerData> userPickerData = new ArrayList<>();

        //查询有几个部门
        List<Department> list = mapper1.selectBomDepartment();
        for (int i = 0; i < list.size(); i++) {
            //查询该部门下所有的用户信息
            List<User> users1 = mapper2.selectDepartmentUser(list.get(i).getId());
            List<Members>members = new ArrayList<>();
            for (int j = 0; j < users1.size(); j++) {
                int id = users1.get(j).getId();
                //查询作者管理的用户

                //查询该作者管理的人员
                List<Map<String,Object>> users = mapper.selectByAuthor(authorId);
                boolean a=false;
                for (int k = 0; k < users.size(); k++) {
                    String id1 = users.get(k).get("id").toString();
                    String can_edit = users.get(k).get("can_edit").toString();
                    boolean aa = false;
                    if (Integer.parseInt(can_edit)==1){
                        aa=true;
                    }

                    if (Integer.parseInt(id1)==id){
                        Members members1 = new Members(Integer.parseInt(users.get(k).get("id").toString()),users.get(k).get("name").toString(),true,aa,0);
                        members.add(members1);
                        a=true;
                        break;
                    }
                }
                if (!a){
                    Members members1 = new Members(users1.get(j).getId(),users1.get(j).getName(),false,false,0);
                    members.add(members1);
                }
            }

            List<Department> departments = mapper1.selectById(list.get(i).getId());
            UserPickerData userPickerData1 = new UserPickerData(list.get(i).getId(),departments.get(0).getDepartmentName(),false,members);
            if (members.size()>0){
                userPickerData.add(userPickerData1);
            }

        }
        //释放资源
        sqlSession.close();
        //返回值
        return userPickerData;

    }





    //添加新的未审核的管理人员审核信息
   public void addAuthorUser(AuthorUserReview authorUserReview, List<AuthorUser> authorUsers){

        //（大分类）0删除；1增加；
       //
       //（大分类的小分类）2增加浏览权限；3增加编辑权限；4增加所有权限；5删除浏览权限；6删除编辑权限；7删除所有权限


        //获取session
       SqlSession sqlSession = factory.openSession();
       //获取mapper
       AuthorUserMapper mapper = sqlSession.getMapper(AuthorUserMapper.class);
       BomPurviewReviewMapper mapper1 = sqlSession.getMapper(BomPurviewReviewMapper.class);
       List<AuthorUser> authorUserList = new ArrayList<>();

       //调用mapper
       mapper1.addAuthorUserReviewApp(authorUserReview);
       if (authorUsers.size()>0){

           int id = authorUserReview.getId();
           //查询申请人
           //查询现在登录的人的id
           int applicant = authorUserReview.getApplicant();
           //查询现在正在管理的人
           List<AuthorUser> authorUsers1 = mapper.selectNowAuthor(applicant);

           for (int i = 0; i < authorUsers.size(); i++) {

               //查询是否存在新增的管理的人
               boolean a= false;
               boolean b =false;
               int userId = authorUsers.get(i).getUserId();//新申请的人的id
               int canEdit = authorUsers.get(i).getCanEdit();
               for (int j = 0; j < authorUsers1.size(); j++) {
                   int userId1 = authorUsers1.get(j).getUserId();//正在管理的人的id
                   if (userId==userId1){
                       a=true;//用户已经存在了
                       //用户已经存在了，即浏览权限已经存在，查询编辑权限
                       int canEdit1 = authorUsers1.get(j).getCanEdit();
                       if (canEdit1==canEdit){
                           //编辑权限也相同，这个是真的相同
                           b=true;//编写权限也相同
                           break;
                       }


                   }

               }
//               !a表示用户不存在，a&&!b表示人用户存在，浏览权限存在编写权限不相同
               if (!a ||(a&&!b && canEdit ==1)){

                   //是新增的
                   authorUsers.get(i).setType(1);
                   //设置申请Id
                   authorUsers.get(i).setReviewAppId(id);
                   //查询是新增的什么
                    if (!a){
                        //用户不存在
                        if (authorUsers.get(i).getCanEdit()==0){
                            //只新增浏览权限
                            AuthorUser authorUser = new AuthorUser(authorUsers.get(i).getId(),authorUsers.get(i).getAuthorId(),authorUsers.get(i).getUserId(),2,id,2,authorUsers.get(i).getCanEdit());
                            authorUserList.add(authorUser);
                        }
                        else {
                            //新增浏览和编辑权限
                            AuthorUser authorUser = new AuthorUser(authorUsers.get(i).getId(),authorUsers.get(i).getAuthorId(),authorUsers.get(i).getUserId(),2,id,4,authorUsers.get(i).getCanEdit());
                            authorUserList.add(authorUser);
                        }

                    }
                    else {
                        //用户存在（只新增编辑权限）
                        if (authorUsers.get(i).getCanEdit()==1){
                            AuthorUser authorUser = new AuthorUser(authorUsers.get(i).getId(),authorUsers.get(i).getAuthorId(),authorUsers.get(i).getUserId(),2,id,3,authorUsers.get(i).getCanEdit());
                            authorUserList.add(authorUser);
                        }
                    }





               }




           }
           //查询是否存在删除的（原来以管理的存在，但是新申请的不存在）

           for (int i = 0; i < authorUsers1.size(); i++) {
               int userId = authorUsers1.get(i).getUserId();
               int canEdit = authorUsers1.get(i).getCanEdit();
               boolean a = false;
               boolean b = false;
               for (int j = 0; j < authorUsers.size(); j++) {
                   int userId1 = authorUsers.get(j).getUserId();
                   int canEdit1 = authorUsers.get(j).getCanEdit();
                   if (userId==userId1){
                       //用户存在，比较编辑权限是否相同
                       a=true;
                       if (canEdit==canEdit1){
                           b=true;
                           break;
                       }

                   }
               }
               if (!a||(a&&!b && canEdit ==1)){

                   authorUsers1.get(i).setType(0);
                   authorUsers1.get(i).setDeleteSign(2);
                   AuthorUser authorUser1 = authorUsers1.get(i);

                   if (!a){
                       //删除整个人 type为3
                       //查询正在使用的权限

                       int canEdit1 = authorUser1.getCanEdit();
                       if (canEdit1==1){
                           //原来存在编辑权限，那就是全部删除
                           AuthorUser authorUser = new AuthorUser(authorUser1.getId(),authorUser1.getAuthorId(),authorUser1.getUserId(),2,authorUserReview.getId(),7,0);
                           authorUserList.add(authorUser);
                       }
                       else {
                           //原来不存在编辑权限，那就是只删除浏览权限
                           AuthorUser authorUser = new AuthorUser(authorUser1.getId(),authorUser1.getAuthorId(),authorUser1.getUserId(),2,authorUserReview.getId(),5,0);
                           authorUserList.add(authorUser);
                       }

                   }
                   else {


                            //只删除编辑权限
                           AuthorUser authorUser = new AuthorUser(authorUser1.getId(),authorUser1.getAuthorId(),authorUser1.getUserId(),2,authorUserReview.getId(),6,0);
                           authorUserList.add(authorUser);

//                       else if (authorUser1.getCanEdit()==1){
//                           AuthorUser authorUser = new AuthorUser(authorUser1.getId(),authorUser1.getAuthorId(),authorUser1.getUserId(),2,authorUserReview.getId(),0,0);
//                           authorUserList.add(authorUser);
//                       }
                   }

               }
           }


       }
       else {
           //提交的没有选中的，那就删除所有的,需要生成删除申请
//           mapper.updateNowAuthorUser(authorUserReview.getApplicant());

           //查询现在管理的所有用户
           List<AuthorUser> authorUserList1 = mapper.selectNowAuthor(authorUserReview.getApplicant());
           for (int i = 0; i < authorUserList1.size(); i++) {
               //生成新的申请内容

               int canEdit = authorUserList1.get(i).getCanEdit();
               if (canEdit==0){

                   //原来没有编辑权限，那就是只删除浏览权限
                   AuthorUser authorUser = new AuthorUser(0,authorUserReview.getApplicant(),authorUserList1.get(i).getUserId(),2,authorUserReview.getId(),5,0);
                   authorUserList.add(authorUser);
               }
               else {
                   //原来存在编辑权限，删除浏览权限和编辑权限
                   AuthorUser authorUser = new AuthorUser(0,authorUserReview.getApplicant(),authorUserList1.get(i).getUserId(),2,authorUserReview.getId(),7,0);
                   authorUserList.add(authorUser);
               }


           }
       }


       //添加
       if (authorUserList.size()>0){
           mapper.addAuthorUser(authorUserList);

           //提交事务
           sqlSession.commit();
       }

       //释放资源
       sqlSession.close();



   }


    //查询某个作者可以控制的人分为几个部门
    public List<Department> selectAllDepart(int authorId){
        //获取session
        SqlSession sqlSession = factory.openSession();
        AuthorUserMapper mapper = sqlSession.getMapper(AuthorUserMapper.class);
        List<Department> list = mapper.selectAllDepart(authorId);
        sqlSession.close();
        return list;
    }

    //查询该作者已经管理的人员

    public List<AuthorUser> selectNowAuthor(int authorId){
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        AuthorUserMapper mapper = sqlSession.getMapper(AuthorUserMapper.class);
        //调用mapper
        List<AuthorUser> authorUserList = mapper.selectNowAuthor(authorId);
        //释放资源
        sqlSession.close();
        //返回值
        return authorUserList;
    }
}
