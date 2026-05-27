

function height(){
    //设置iframe高度

    let iframe = window.parent.document.getElementById("iframepage");
    iframe.style.height=window.parent.innerHeight+"px";

}
height();
//获取部门信息
//查询所有部门信息显示到部门选择框中




    //查询自己的基本信息
    axios({
        method:"post",
        url:"user/userSelectPersonal"
    }).then(function (resp){
        let datas=resp.data;
        document.getElementById("name").value=datas[0].name;
        let sex = datas[0].sex;
        let sexs = document.querySelectorAll(".sex");
        if (sex==="男"){
            sexs[0].checked=true;
        }else if (sex==="女"){
            sexs[1].checked=true;
        }
        document.getElementById("age").value=datas[0].age;


        //根据部门ID查询部门
        axios({
            method:"post",
            url:"department/selectById",
            data:datas[0].department
        }).then(function (resp){
            let datas=resp.data;
            let formdata="";
            formdata+='<option class="option" value='+datas[0].id+'>'+datas[0].departmentName+'</option>';
            document.getElementById("departmentSelect").innerHTML=formdata;
        })

    })



//确认修改
document.getElementById("submit").onclick=function (){
    if (window.confirm("您确定要更新信息吗？")){
        //获取信息
        //检测格式
        let name = document.getElementById("name").value;
        let age = document.getElementById("age").value;
        //检测名字
        if (name===""){
            alert("请输入姓名！")
        }else if (age.length===0){
            alert("请输入年龄！")
        }else{
            //获取信息
            var formdata={
                id:"",
                name:"",
                sex:"",
                age:"",
            }
            //名字
            formdata.name=document.getElementById("name").value;
            //性别
            let sex = document.querySelectorAll(".sex");

            if (sex[0].checked===true){
                formdata.sex="男"
            }else if (sex[1].checked===true){
                formdata.sex="女"
            }

            //年龄
          formdata.age =  document.getElementById("age").value;

            axios({
                method:"post",
                url:"user/userUpdatePersonal",
                data:formdata
            }).then(function (resp){
                if (resp.data==="success"){
                    alert("更新成功！");
                    location.reload();
                }else {
                    alert("更新失败，请联系相关人员！")
                }
            })
        }
        return true;
    }else{
        return false;
    }
}



