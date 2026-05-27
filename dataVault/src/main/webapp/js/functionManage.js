function height(){
    //设置iframe高度

    let iframe = window.parent.document.getElementById("iframepage");
    iframe.style.height=window.parent.innerHeight+"px";

}
height();

//查询所有的功能模块、
axios({
    method:"post",
    url:"function/selectAllFunction"
}).then(function (resp){
    let  functionData=resp.data;
    let formData="";
    for (let i = 0; i < functionData.length; i++) {
        formData+='         <div style="width: 100%;height: auto;display: flex;text-align: center;">\n' +

            '                                                            <div style="width: 75%;margin: auto;  border:white solid 1px;height: auto" ><b>'+functionData[i].functionName+'</b></div>\n' +
            '                                                            <div style="width: 25%;margin: auto;  border:white solid 1px;height: auto" ><a class="checkFunction" href="javascript:void(0)">查看</a>&nbsp;<a class="AddFunction" href="javascript:void(0)">新增</a></div>\n' +
            '                                                        </div>'
    }
    document.getElementById("content").innerHTML=formData;

    //给查看按钮添加方法
    let checkFunctions = document.querySelectorAll(".checkFunction");
    for (let i = 0; i < checkFunctions.length; i++) {
        checkFunctions[i].onclick=function (){
            //打开窗口
            wd.style.display="block";
            document.getElementById("functionTwoWindow").style.display="";
            axios({
                method:"post",
                url:"function/selectFunctionTwo",
                data:functionData[i].id
            }).then(function (resp){
                let data=resp.data;
                let formdata=""
                for (let j = 0; j < data.length; j++) {
                    formdata+='         <div style="width: 100%;height: auto;display: flex;text-align: center;">\n' +

                        '                                                            <div style="width:100%;margin: auto;  border:white solid 1px;height: auto" ><b>'+data[j].name+'</b></div>\n' +
                        '                                                        </div>'
                }

                document.getElementById("functionTwoContent").innerHTML=formdata;
            })
        }
    }

    //给新增按钮添加方法
    let add = document.querySelectorAll(".addFunction");
    for (let i = 0; i < add.length; i++) {
        add[i].onclick=function (){

        }
    }
})










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



