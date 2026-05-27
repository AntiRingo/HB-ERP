//查询权限
let userId = parent.document.getElementById("userId").value;
async function userFunction(userId){
    let data;
    await axios({
        method:"post",
        url:"userFunction/selectUserFunctionTwoByUserId",
        data:userId
    }).then(function (resp){
        data= resp.data;
    })
    return data;
}

//添加部门信息权限
async function addDepartmentQx(){
    let promise = userFunction(userId);
    let promiseBoolean=false;
    await promise.then( async function (resp) {

        for (let j = 0; j < resp.length; j++) {
            if (resp[j].function_two_id === 32 && resp[j].open_status === 1) {
                promiseBoolean = true;

                break;
            }
        }

    })
    return promiseBoolean;
}
//修改部门信息权限
async function updateDepartmentQx(){
    let promise = userFunction(userId);
    let promiseBoolean=false;
    await promise.then( async function (resp) {

        for (let j = 0; j < resp.length; j++) {
            if (resp[j].function_two_id === 33 && resp[j].open_status === 1) {
                promiseBoolean = true;

                break;
            }
        }

    })
    return promiseBoolean;
}
//删除部门信息权限
async function deleteDepartmentQx(){
    let promise = userFunction(userId);
    let promiseBoolean=false;
    await promise.then( async function (resp) {

        for (let j = 0; j < resp.length; j++) {
            if (resp[j].function_two_id === 34 && resp[j].open_status === 1) {
                promiseBoolean = true;

                break;
            }
        }

    })
    return promiseBoolean;
}

function height(){
    //设置iframe高度

    let iframe = window.parent.document.getElementById("iframepage");
    iframe.style.height=window.parent.innerHeight+"px";

}
height();

// 在用户点击其他地方时，关闭弹窗
let wd = document.querySelector(".window");
window.onclick = function(event) {
    if (event.target === wd) {
        wd.style.display = "none";
    }
}

//点击close也可以关闭弹窗
document.querySelector(".window-content-close").onclick=function (){
    wd.style.display="none";
}

//点击添加打开窗口
document.getElementById("addDepartment").onclick=function (){
    let booleanPromise = addDepartmentQx();
    booleanPromise.then(function (resp){
        if (resp===true){
            document.querySelector(".window").style.display="block";
            document.getElementById("addContent").style.display="block";
            document.getElementById("updateContent").style.display="none";
        }
        else {
            alert("您暂未获得添加权限！")
        }
    })

}


//获取所有的部门数据
function selectAll(){
    axios({
        method:"post",
        url:"department/selectAll"
    }).then(function (resp){
        let datas=resp.data;
        let formdata="";
        for (let i = 0; i < datas.length; i++) {
            formdata+='   <div style="width: 100%;height: auto;display: flex;text-align: center;">\n' +
                '                                            <div style="width: 70%;margin: auto; border: white solid 1px"><b>'+datas[i].departmentName+'</b></div>\n' +
                '                                            <div style="width: 30%;margin: auto  ; border: white solid 1px"><a class="updateDepartment" href="javascript:void(0)">修改</a>&nbsp;<a class="deleteDepartment"  href="javascript:void(0)">删除</a></div>\n' +
                '\n' +
                '                                        </div>'
        }

        document.getElementById("content").innerHTML=formdata;

        //删除部门
        let deletes = document.querySelectorAll(".deleteDepartment");
        for (let i = 0; i < deletes.length; i++) {
            deletes[i].onclick=function (){
                let booleanPromise = deleteDepartmentQx();
                booleanPromise.then(function (resp){
                    if (resp===true){
                        if (window.confirm("您确定要删除这个部门吗？")){
                            //查询该部门下是否有成员！
                            axios({
                                method:"post",
                                url:"user/selectIfUserDepartment",
                                data:datas[i].id
                            }).then(function (resp){
                                if (resp.data===false){
                                    axios({
                                        method:"post",
                                        url:"department/delete",
                                        data: datas[i].id
                                    }).then(function (resp){
                                        if (resp.data==="success"){
                                            alert("删除成功！");
                                            location.reload();
                                        }else {
                                            alert("删除失败，请联系相关人员！")
                                        }
                                    })
                                }else {
                                    alert("该部门下有账号存在，不允许删除！")
                                }
                            })



                            return true;
                        }else {
                            return false;
                        }
                    }
                    else {
                        alert("您暂未获取删除权限！")
                    }
                })

            }
        }

        //修改部门
        let updates = document.querySelectorAll(".updateDepartment");
        for (let i = 0; i < updates.length; i++) {
            updates[i].onclick=function (){
                let booleanPromise = updateDepartmentQx();
                booleanPromise.then(function (resp){
                    if (resp===true){
                        //打开窗口，显示修改内容
                        document.querySelector(".window").style.display="block";
                        document.getElementById("addContent").style.display="none";
                        document.getElementById("updateContent").style.display="block";

                        //获取内容
                        axios({
                            method:"post",
                            url:"department/selectById",
                            data:datas[i].id
                        }).then(function (resp)
                        {
                            let datas=resp.data;
                            document.getElementById("updateName").value=datas[0].departmentName;

                            document.getElementById("update").onclick=function (){
                                if (window.confirm("您确定要更新这个部门信息吗？")){
                                    let formdata={
                                        id:"",
                                        departmentName:""
                                    }
                                    formdata.id=datas[0].id;
                                    formdata.departmentName=document.getElementById("updateName").value;
                                    //查询部门名称是否重复
                                    axios({
                                        method:"post",
                                        url:"department/updateIfExist",
                                        data:formdata
                                    }).then(function (resp){
                                        if (resp.data===false){
                                            axios({
                                                method:"post",
                                                url:"department/update",
                                                data:formdata
                                            }).then(function (resp){
                                                let datas=resp.data;
                                                if (datas==="success"){
                                                    alert("修改成功！");
                                                    location.reload();
                                                }else {
                                                    alert("修改失败，请联系相关人员！")
                                                }
                                            })

                                        }else {
                                            alert("部门名称重复，请核对后再进行操作！")

                                        }
                                    })


                                    return true;
                                }else {
                                    return false;
                                }
                            }
                        })
                    }
                    else {
                        alert("您暂未获得修改权限！")
                    }
                })


            }
        }
    })
}

selectAll();

//添加部门

document.getElementById("add").onclick=function (){
    if (window.confirm("您确定要添加这个部门吗？")){
        let formdata={
            id:"",
            departmentName:"",
        }

        //获取数据
        formdata.departmentName =document.getElementById("name").value;

        //查询添加的部门名称是否重复
        axios({
            method:"post",
            url:"department/addIfExist",
            data:formdata.departmentName
        }).then(function (resp){
            if (resp.data===false){
                //调用axios
                axios({
                    method: "post",
                    url: "department/add",
                    data:formdata,
                }).then(function (resp){
                    let datas=resp.data;
                    if (datas==="success"){
                        alert("添加成功");
                        document.getElementById("name").value="";
                        location.reload();
                        document.querySelector(".window").style.display="none";
                    }else {
                        alert("添加失败，请联系相关人员！")
                    }
                })
            }else {
                alert("部门名称重复，请核对后再进行操作！")
            }
        })



        return true;
    }else {
        return false;
    }


}

