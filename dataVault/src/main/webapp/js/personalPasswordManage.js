

function height(){

    let iframe = window.parent.document.getElementById("iframepage");
    if (iframe){
        let num= window.parent.innerHeight;
        if (Number(num)>Number(60)){
            let s = Number(num)-Number(60)
            iframe.style.height=s+"px";
        }

    }


}
height();



//点击修改
document.getElementById("submit").onclick=function (){
    if (window.confirm("您确定要修改密码吗？")){
        //判断信息是否为空
        let lengths = document.querySelectorAll(".length");
        let lengthTrue= true;
        for (let i = 0; i < lengths.length; i++) {
            let length = lengths[i].value.length;
            if (length===0){
                lengthTrue=false;
            }
        }
        if (lengthTrue===false){
            alert("所填写的信息不能为空！")
        }else{
                //获取id
            axios({
                method:"post",
                url:"user/selectName",
            }).then(function (resp){
                if (resp.data.id){
                    let username = resp.data.userName;
                    //检测密码格式
                    let password = document.getElementById("password").value;
                    let password1 = document.getElementById("password1").value;
                    let password2 = document.getElementById("password2").value;


                    ///^\S*(?=\S{6,})(?=\S*\d)(?=\S*[A-Z])(?=\S*[a-z])\S*$/
                    if (/^[a-zA-Z0-9_-]{6,20}$/.test(password)===false){
                        alert("您输入的原密码格式错误，请您重新输入！")
                    }else if (/^[a-zA-Z0-9_-]{6,20}$/.test(password1)===false){
                        alert("您输入的修改后的密码格式错误，请您重新输入！")
                    }else if (/^[a-zA-Z0-9_-]{6,20}$/.test(password2)===false){
                        alert("您再次输入修改后的密码格式错误，请您重新输入！")
                    }else {
                        //检测两次输入的密码是否一致
                        if (password1!==password2){
                            alert("您两次输入的密码不一致！")
                        }else {
                            //检测输入的用户名密码是否与登录的用户名密码一致
                            axios({
                                method:"post",
                                url:"user/isRight?username="+username+"&password="+password+""
                            }).then(function (resp){
                                if (resp.data==="success"){
                                    //验证用户名密码成功，执行修改操作
                                    axios({
                                        method: "post",
                                        url: "user/updatePassword",
                                        data:password1
                                    }).then(function (resp){
                                        if (resp.data==="success"){
                                            alert("密码修改成功，请重新登录！");
                                            location.reload();
                                        }else {
                                            alert("密码修改失败，请联系相关人员！")
                                        }
                                    })
                                }else {
                                    alert("您输入的用户名或密码不正确，请核对后在进行操作！")
                                }
                            })
                        }
                    }
                }else {
                    alert("登录过期，请重新登录！")
                }
            })


        }

        return true;
    }else {
        return false;
    }





}