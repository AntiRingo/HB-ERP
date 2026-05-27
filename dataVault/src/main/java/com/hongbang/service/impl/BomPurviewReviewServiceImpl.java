package com.hongbang.service.impl;

import com.hongbang.mapper.*;
import com.hongbang.pojo.*;
import com.hongbang.service.BomPurviewReviewService;
import com.hongbang.util.SqlSessionFactoryUtils;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class BomPurviewReviewServiceImpl implements BomPurviewReviewService {

    //获取工厂
    SqlSessionFactory factory = SqlSessionFactoryUtils.getSqlSessionFactory();
    //查询未审核的数据
    public List<ApprovalRequests> selectNoReview(){
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        BomPurviewReviewMapper mapper = sqlSession.getMapper(BomPurviewReviewMapper.class);
        UserMapper mapper1 = sqlSession.getMapper(UserMapper.class);
        BomPurviewMapper mapper2 = sqlSession.getMapper(BomPurviewMapper.class);
        DepartmentMapper mapper3 = sqlSession.getMapper(DepartmentMapper.class);

        //调用mapper
        //建立新的
        List<ApprovalRequests> approvalRequests = new ArrayList<>();

        //所有的申请单
        List<BomPurviewReview> bomPurviewReviews = mapper.selectNoReview();


        for (int i = 0; i < bomPurviewReviews.size(); i++) {

            List<Members> members = new ArrayList<>();
            BomPurviewReview bomPurviewReview = bomPurviewReviews.get(i);

            //查询
            //获取申请单内容
            List<BomPurview> bomPurviews = mapper.selectNoBomPurview(bomPurviewReview.getId());


            for (int j = 0; j < bomPurviews.size(); j++) {
//                boolean exist = false;
                BomPurview bomPurview = bomPurviews.get(j);

                //查询哪些是新增的
//                int finBomTitleId = bomPurview.getFinBomTitleId();//获取BOM表id
                //根据id查询现在正在使用的用户权限

//                List<Integer> list = mapper2.selectNowUsed(finBomTitleId);
//                System.out.println(list);
//                for (int k = 0; k < list.size(); k++) {
//                    Integer integer = list.get(k);
//                    if (integer==bomPurview.getUserPurview()){
//                        exist=true;
//                        break;
//                    }
//                }
//                if (exist){

                    User user = mapper1.selectById(bomPurview.getUserPurview());

                    String name = user.getName();

                    boolean b1;
                    if (bomPurview.getCanEdit()==1){
                        b1=true;
                    }
                    else {
                        b1=false;
                    }
                    Members members1 = new Members(1,name,true,b1,0);
                    members.add(members1);
//                }


            }
            //查询申请人部门
            User user = mapper1.selectById(bomPurviewReview.getApplicant());
            List<Department> departments = mapper3.selectById(user.getDepartment());



            ApprovalRequests approvalRequests1 = new ApprovalRequests(bomPurviewReview.getId(),bomPurviewReview.gettimes(),user.getName(),departments.get(0).getDepartmentName(),bomPurviewReview.getStatus(),members);

            approvalRequests.add(approvalRequests1);

        }

        sqlSession.close();
        return approvalRequests;
    }

    //通过审核
    public void updateStatus(int status, int id,int bomTitleId){
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        BomPurviewReviewMapper mapper = sqlSession.getMapper(BomPurviewReviewMapper.class);
        BomPurviewMapper mapper1 = sqlSession.getMapper(BomPurviewMapper.class);
        //调用service
        //更新权限申请单状态
        mapper.updateStatus(status,id);
        if (status==1){
            //通过
            //删除现在的正在使用的权限
            mapper1.deletePurview(bomTitleId);
            //更新最新申请的
            mapper1.updateDeleteSign(id,0);
        }
        else if (status==2){
            //拒绝，那就要将申请的内容deleteSign改成1
            mapper1.updateDeleteSign(id,1);
        }

        //提交事务
        sqlSession.commit();
        //释放资源
        sqlSession.close();
    }


    //根据id查询BOM表TitleId
    public BomPurviewReview selectById( int id){
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        BomPurviewReviewMapper mapper = sqlSession.getMapper(BomPurviewReviewMapper.class);
        //调用mapper
        BomPurviewReview bomPurviewReview = mapper.selectById(id);
        //释放资源
        sqlSession.close();
        //返回值
        return bomPurviewReview;
    }






    //    最新的审核作者管理哪些用户的 -------------------------------------------------------------------------------------------------------------------------------------------

    //查询全部的申请单内容
    public List<ApprovalRequests> selectAllAuthorAndUserReview(){
        //（大分类）0删除；1增加；
        //
        //（大分类的小分类）2增加浏览权限；3增加编辑权限；4增加所有权限；5删除浏览权限；6删除编辑权限；7删除所有权限



        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        BomPurviewReviewMapper mapper = sqlSession.getMapper(BomPurviewReviewMapper.class);
        AuthorUserMapper mapper1 = sqlSession.getMapper(AuthorUserMapper.class);

        //调用service
        List<Map<String, Object>> maps = mapper.selectAllAuthorAndUserReview(); //查询全部的申请单
        List<Map<String, Object>> authorUsers = mapper.selectAllAuthor();//查询全部的申请单的申请内容
//新建一个符合前端格式的
        List<ApprovalRequests> approvalRequests = new ArrayList<>();

        for (int i = 0; i < maps.size(); i++) {
            Map<String, Object> map = maps.get(i);
            String id = map.get("id").toString();
            String timestamp = map.get("timestamp").toString();
            String applicant = map.get("name").toString();
            String department = map.get("department").toString();
            String status = map.get("status").toString();
            //新建一个list<members>
            List<Members> members = new ArrayList<>();

            if (authorUsers.size()>0){
                for (int j = 0; j < authorUsers.size(); j++) {
                    Map<String, Object> map1 = authorUsers.get(j);


                    String id1 = map1.get("id").toString();
                    String name = map1.get("name").toString();
                    String type = map1.get("type").toString();



                    boolean can = false;//编辑
                    boolean sel = true;//浏览

                    //（大分类）0删除；1增加；
                    //
                    //（大分类的小分类）2增加浏览权限；3增加编辑权限；4增加所有权限；5删除浏览权限；6删除编辑权限；7删除所有权限

//                    新增：谁是true谁新增；  删除：谁是false谁删除
                    if (Integer.parseInt(type)==2){
                        type = String.valueOf(1);
                        sel = true;
                        can = false;
                    }
                    else if (Integer.parseInt(type)==3){
                        type = String.valueOf(1);
                        sel = false;
                        can = true;
                    }
                    else if (Integer.parseInt(type)==4){
                        type = String.valueOf(1);
                        sel = true;
                        can =true;
                    }
                    else if (Integer.parseInt(type)==5){
                        type = String.valueOf(0);
                        sel = false;
                        can = true;
                    }
                    else if (Integer.parseInt(type)==6){
                        type = String.valueOf(0);
                        sel = true;
                        can = false;
                    }
                    else if (Integer.parseInt(type)==7){
                        type = String.valueOf(0);
                        sel = false;
                        can = false;
                    }






//
//                    if (b && Integer.parseInt(canEdit)==0 && Integer.parseInt(type)==1){
//                    //是否审核结果都相同，都是新增浏览权限
//
//                        sel = true;//新增的
//                        can=false;//不是新增的
//
//
//                    }
//                    else if (b && Integer.parseInt(canEdit)==1 && Integer.parseInt(type)==1){
//                        if (b1){
//                            //存在权限，还未审核
//                            //新增编辑权限（浏览权限已经存在了）
//                            sel = false;
//                            can = true;
//                        }
//                        else {
//                            //存在权限，已经审核，而现正在使用的权限有编辑和浏览权限，无法判断是新增一个还是全部新增，需要加入判断
//
//                        }
//
//                    }
//                    else if (!b && Integer.parseInt(canEdit)==1 && Integer.parseInt(type)==1){
//                        //新增浏览和编辑权限
//                        sel = true;
//                        can = true;
//                    }
//                    else if (b && Integer.parseInt(canEdit)==0 && Integer.parseInt(type)==0){
//                        //删除编辑权限（保留浏览权限）
//                        sel = true;
//                        can = false;
//                    }
//                    else if (Integer.parseInt(type)==3){
//                        //删除全部权限
//                        type = String.valueOf(0);
//                        sel = false;
//                        //查询正在使用的编辑权限
//                        AuthorUser authorUser = mapper1.selectUserByAuthor(Integer.parseInt(authorId), Integer.parseInt(userId));
//                        if (authorUser!=null && authorUser.getCanEdit()==1){
//                            //正在使用的有编辑权限，那就是删除全部权限
//                            can = false;
//                        }
//                        else if (authorUser!=null && authorUser.getCanEdit()==0){
//                            //正在使用的没有编辑权限，那就是删除浏览权限
//                            sel = false;
//                            can = true;
//                        }
//                        else {
//                            //现在没有正在使用的，查询删除的是怎样的，全删、删浏览、删编辑
//
//
//                            //正在使用的没有编辑权限，查询最新的已经删除的权限
//                            List<AuthorUser> authorUserList = mapper1.selectDeleteUserByAuthor(Integer.parseInt(authorId), Integer.parseInt(userId));
//                            if (authorUserList.size()>0){
//                                AuthorUser authorUser1 = authorUserList.get(0);
//                              //删除的，查询最新的一条的type。如果是0那就是只删除编辑权限，如果是3则需要判断是删除了所有权限还是只删除了浏览权限
//
//                                int type1 = authorUser1.getType();
//                                if (type1==0){
//                                    sel = true;
//                                    can = false;
//                                }
//                                else if (type1 == 3){
//                                    //查询上一条的数据
//                                    AuthorUser authorUser2 = authorUserList.get(1);
//                                    int canEdit1 = authorUser2.getCanEdit();
//                                    if (canEdit1==0){
//                                        //只删除了浏览权限
//                                        sel = false;
//                                        can = true;
//                                    }
//                                    else {
//                                        //全删
//                                        sel = false;
//                                        can = false;
//                                    }
//                                }
//
//
////                                int canEdit1 = authorUser1.getCanEdit();
////                                if (canEdit1==1){
////                                    //如果上个编辑权限是存在的，那这次就是删除的
////                                    can = false;
////                                }
//                            }
//                            else {
//                                can = true;
//                            }
//
//                        }
//
//                    }





                    if (Integer.parseInt(id)==Integer.parseInt(id1)){
                        Members members1 = new Members(0,name,sel,can,Integer.parseInt(type));
                        members.add(members1);
                    }
                }
            }

            if (members.size()>0){
                ApprovalRequests approvalRequests1 = new ApprovalRequests(Integer.parseInt(id),timestamp,applicant,department,Integer.parseInt(status),members);
                approvalRequests.add(approvalRequests1);
            }

        }
        sqlSession.close();
        return approvalRequests;

    }

    //通过审核
   public void updateStatusAuthorReview(int status, int appId){
        //获取session
       SqlSession sqlSession = factory.openSession();
       //获取mapper
       BomPurviewReviewMapper mapper = sqlSession.getMapper(BomPurviewReviewMapper.class);
       AuthorUserMapper mapper1 = sqlSession.getMapper(AuthorUserMapper.class);
       BomPurviewMapper mapper2 = sqlSession.getMapper(BomPurviewMapper.class);

       //调用mapper
       //更新作者的管理人员

       if (status==1){
           mapper.updateStatusAuthorReview(1, appId);
           //通过(有可能是通过新增，也有可能是通过删除)
           //查询该申请单的内容
           List<AuthorUser> authorUserList = mapper1.selectByReviewId(appId);

           for (int i = 0; i < authorUserList.size(); i++) {
               AuthorUser authorUser = authorUserList.get(i);
               //（大分类）0删除；1增加；
               //
               //（大分类的小分类）2增加浏览权限；3增加编辑权限；4增加所有权限；5删除浏览权限；6删除编辑权限；7删除所有权限
               if (authorUser.getType()==2 || authorUser.getType()==3 ||authorUser.getType()==4){
                   //先删除正在使用的
                   mapper1.deleteSomeOne(authorUser.getAuthorId(), authorUser.getUserId());
                   //新增
                   mapper1.updateNewAuthorUser(0,authorUser.getAuthorId(),appId);
               }
               else if (authorUser.getType()==5){
//                   System.out.println(authorUser.getId()+"删除浏览权限");
                   //删除
                   mapper1.deleteSomeOne(authorUser.getAuthorId(), authorUser.getUserId());
                   //查询该作者管理的这个人的所有的权限
                   List<BomPurview> bomPurviews = mapper2.selectUserIdByAuthor(authorUser.getUserId(), authorUser.getAuthorId());
                   if (bomPurviews.size()>0){
                       //更新该作者管理这个人的所有权限，全部删除
                       mapper2.deleteAllUserByAuthor(authorUser.getUserId(),authorUser.getAuthorId());
                   }
               }
               else if (authorUser.getType()==6){
//                   System.out.println(authorUser.getId()+"删除编辑权限");
                   //删除
                   mapper1.deleteSomeOne(authorUser.getAuthorId(), authorUser.getUserId());
                   //删除正在使用的后，将现在这个改为正在使用的
                   mapper1.updateById(authorUser.getId());
                   //删除这个人在BOM表上的编辑权限
                   mapper2.deleteAllUserCanEditByAuthor(authorUser.getUserId(),authorUser.getAuthorId());

               }
               else if (authorUser.getType()==7){
//                   System.out.println(authorUser.getId()+"删除所有权限");
                   //删除
                   mapper1.deleteSomeOne(authorUser.getAuthorId(), authorUser.getUserId());
                   //查询该作者管理的这个人的所有的权限
                   List<BomPurview> bomPurviews = mapper2.selectUserIdByAuthor(authorUser.getUserId(), authorUser.getAuthorId());
                   if (bomPurviews.size()>0){
                       //更新该作者管理这个人的所有权限，全部删除
                       mapper2.deleteAllUserByAuthor(authorUser.getUserId(),authorUser.getAuthorId());
                   }
               }


           }
       }
       else if (status==2){
           //拒绝
           mapper.updateStatusAuthorReview(2, appId);

       }
       //提交事务
       sqlSession.commit();
       //释放资源
       sqlSession.close();
   }


}
