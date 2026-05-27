


//新信息桌面右下角提示
function  messageNotice(s,tag){
    if (window.Notification && Notification.permission !==  "denied" ) {
        Notification.requestPermission( function (status) {
            var  notice_ =  new  Notification( '新的消息' , {
                body:  s,
                icon: '/dataVault/images/HBlogo.png', // 自定义图标路径
                image: '/dataVault/images/logo2.png', // 可选图片展示
                badge: '/dataVault/images/HBlogo.png', // 应用于小尺寸的通知标记图标的URL
                requireInteraction: true, // 不自动关闭通知
                tag:tag,
                renotify:true,
            });
            notice_.onclick =  function () { //单击消息提示框，进入浏览器页面
                window.focus();
            }
        });
    }
}






// 获取弹窗
var modal = document.getElementById('myModal');

// 打开弹窗的按钮对象
var btn = document.getElementById("login");

// 获取 <span> 元素，用于关闭弹窗
var span = document.querySelector('.close');

// 点击按钮打开弹窗
btn.onclick = function() {
    //判断登录情况，如果登录就提示已登录，未登录就打开登录窗口
    axios({
        method: "post",
        url: "user/ifLogin",
    }).then(function (resp){
        let datas=resp.data;
        if (String(datas)!=="false"){
            alert("您已登录！")
        }else{
            document.getElementById("checkCodeImg").src="checkCodeServlet?"+new Date().getMilliseconds();
            function f2(){
                modal.style.display = "flex";

            }

            function f1(){
                modal.style.opacity="1";

            }
            setTimeout(f2,10);
            setTimeout(f1,20);
        }
    })


}

//加载页面判断是否登录
axios({
    method: "post",
    url: "user/ifLogin",
}).then(function (resp){
    let datas=resp.data;
    if (String(datas)!=="false"){
            document.getElementById("login").innerHTML=""+datas+"";
    }else {
        document.getElementById("login").innerHTML="登录";
    }
})
// 在用户点击其他地方时，关闭弹窗
window.onclick = function(event) {

    if (event.target===modal) {
        function f2(){
            modal.style.display = "none";
        }

        function f1(){
            modal.style.opacity="0";
        }
        setTimeout(f1,10);
        setTimeout(f2,1000);

    }
}

// 点击 <span> (x), 关闭弹窗
span.onclick = function() {
    function f2(){
        modal.style.display = "none";
    }

    function f1(){
        modal.style.opacity="0";
    }
    setTimeout(f1,10);
    setTimeout(f2,1000);
}


function login(){
    let  username = document.getElementById("username").value;

    let  password = document.getElementById("password").value;
    let checkCode=document.getElementById("checkCode").value;


    axios({
        method:"post",
        url:"user/loginVerification?password="+password+"&checkCode="+checkCode,
        data:username,

    }).then(function (resp){
        if (resp.data==="success"){

            modal.style.display = "none";
            document.getElementById("username").value="";
            document.getElementById("password").value="";
            document.getElementById("checkCode").value="";

                document.getElementById("login").innerHTML=""+username+"";
                alert("登录成功！")
        } else if (resp.data==="codefalse"){
            alert("验证码错误，请重新输入！！！")

        } else if(resp.data===false){
            alert("用户名或密码错误，请您重新输入！！！")
        }else if (resp.data==="codenull"){
            alert("验证码已过期，请刷新后再试！")
        }

        else {
            alert("服务器错误，请稍后再试！")
        }
    })
}

document.getElementById("loginBtn").onclick=function (){

    login();


}







var eye = document.getElementById("eye");
var pwd = document.getElementById("password");
var flag = 0;
eye.onclick = function() {
    // 点击一次之后，flag一定要变化
    if (flag === 0) {
        pwd.type = "text";
        eye.src = "./images/open.png";
        flag = 1; //赋值操作
    } else {
        pwd.type = "password";
        eye.src = "./images/close.png";
        flag = 0;
    }
}

//点击更换验证码
document.getElementById("changeImg").onclick = function (){

    document.getElementById("checkCodeImg").src="checkCodeServlet?"+new Date().getMilliseconds();
}


// //点击进入个人中心
// document.getElementById("persional").onclick=function (){
//     //判断登录情况，如果登录就转到个人中心页面，未登录就转到登录页面
//     axios({
//         method: "post",
//         url: "user/ifLogin",
//     }).then(function (resp){
//         let datas=resp.data;
//         if (datas==="success"){
//             window.open("userIndex.html");
//         }else {
//             window.open("userLogin.html");
//
//         }
//     })
//
// }



window.onkeydown = function (event) {

    if (modal.style.display === "flex") {
        /* 解决兼容性问题 */
        event = event || window.event;

        if (event.keyCode === 13) {
            login();
        }
    }
}



