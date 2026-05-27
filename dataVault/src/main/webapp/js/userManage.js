function height(){
    //设置iframe高度

    let iframe = window.parent.document.getElementById("iframepage");
    iframe.style.height=window.parent.innerHeight+"px";

}
height();

//设置现在的时间，，，显示使用
function fDate( t1, format = "YYYY-MM-DD HH:II "){
    let minutes = t1.getMinutes();
    if (minutes<10){
        const config = {
            YYYY:t1.getFullYear(),
            MM:t1.getMonth()+1,
            DD:t1.getDate(),
            HH:t1.getHours(),
            II:"0"+t1.getMinutes(),
            // SS:t1.getSeconds(),
        };
        for( const key in config){

            format = format.replace(key,config[key])


        }
        return format;
    }
    else {
        const config = {
            YYYY:t1.getFullYear(),
            MM:t1.getMonth()+1,
            DD:t1.getDate(),
            HH:t1.getHours(),
            II:t1.getMinutes(),
            // SS:t1.getSeconds(),
        };
        for( const key in config){

            format = format.replace(key,config[key])


        }
        return format;
    }



}

//设置现在的时间
function fDate1( t1, format = "YYYY-MM-DD HH:II:SS "){
    const config = {
        YYYY:t1.getFullYear(),
        MM:t1.getMonth()+1,
        DD:t1.getDate(),
        HH:t1.getHours(),
        II:t1.getMinutes(),
        SS:t1.getSeconds(),
    };

    for( const key in config){

        format = format.replace(key,config[key])


    }
    return format;
}

async function levelTwoExist(department) {
    let b;
    //查询是否存在
    await axios({
        method: "post",
        url: "user/selectLevelTwoExist",
        data: department
    }).then(function (resp) {
        b=resp.data
    })

    return b;
}

async function updateLevelTwoExist(department,id) {
    let b;
    //查询是否存在
    await axios({
        method: "post",
        url: "user/updateLevelTwoExist?id="+id+"",
        data: department
    }).then(function (resp) {
        b=resp.data
    })

    return b;
}

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

//添加用户权限
async function addUserQx(){
    let promise = userFunction(userId);
    let promiseBoolean=false;
    await promise.then( async function (resp) {

        for (let j = 0; j < resp.length; j++) {
            if (resp[j].function_two_id === 29 && resp[j].open_status === 1) {
                promiseBoolean = true;

                break;
            }
        }

    })
    return promiseBoolean;
}
//修改用户权限
async function updateUserQx(){
    let promise = userFunction(userId);
    let promiseBoolean=false;
    await promise.then( async function (resp) {

        for (let j = 0; j < resp.length; j++) {
            if (resp[j].function_two_id === 30 && resp[j].open_status === 1) {
                promiseBoolean = true;

                break;
            }
        }

    })
    return promiseBoolean;
}
//删除用户权限
async function deleteUserQx(){
    let promise = userFunction(userId);
    let promiseBoolean=false;
    await promise.then( async function (resp) {

        for (let j = 0; j < resp.length; j++) {
            if (resp[j].function_two_id === 31 && resp[j].open_status === 1) {
                promiseBoolean = true;

                break;
            }
        }

    })
    return promiseBoolean;
}


//展开功能
function f2(oneData,twoData,threeData){

    for (let i = 0; i < twoData.length; i++) {
        let twos = document.getElementById("upthreefunction"+twoData[i].id+"");
        let nodeListOf = twos.querySelectorAll("input");

       if (Number(nodeListOf.length)===0){
            document.getElementById("upTwo"+twoData[i].id+"").className="accordion2";
            twos.className="";

       }
    }
    var acc = document.querySelectorAll(".accordion");
    var acc1 = document.querySelectorAll(".accordion1");
    var accc =document.querySelectorAll(".panel");
    var accc1 =document.querySelectorAll(".panel1");



    for (let i = 0; i < acc.length; i++) {
        acc[i].addEventListener("click", function() {

            acc[i].classList.toggle("active");

            if (accc[i].style.maxHeight) {

                accc[i].style.maxHeight = null;

                getComputedStyle(document.querySelector(".accordion"),":after").getPropertyValue('color')

            } else {

                accc[i].style.maxHeight =accc[i].scrollHeight + "px";
            }
            for (let j = 0; j < acc1.length; j++) {



                acc1[j].onclick=function() {
                    acc1[j].classList.toggle("active");

                    if (accc1[j].style.maxHeight) {

                        accc1[j].style.maxHeight = null;
                        getComputedStyle(document.querySelector(".accordion"),":after").getPropertyValue('color')

                    } else {

                        accc[i].style.maxHeight =accc[i].scrollHeight + accc1[j].scrollHeight+"px";
                        accc1[j].style.maxHeight =accc1[j].scrollHeight + "px";
                    }
                }
            }
        });
    }

}
function yijidisplayupdate(oneData){

    //一级目录显示状态
    for (let j = 0; j < oneData.length; j++) {
        let s = document.getElementById("uptwoFunction"+oneData[j].id+"");
        if (s){
            let ss = s.querySelectorAll('input');



            let kk = true;
            let mm=0;
            for (let k = 0; k < ss.length; k++) {
                if (ss[k].checked===false){
                    kk=false;
                }else {
                    mm++;
                }
            }

            if (kk===true){
                document.getElementById("upmodule"+oneData[j].id+"").checked= true;
                document.getElementById("upmodule"+oneData[j].id+"").indeterminate = false;
            }else {
                if (mm>0){
                    document.getElementById("upmodule"+oneData[j].id+"").checked=false;
                    document.getElementById("upmodule"+oneData[j].id+"").indeterminate = true;
                }
                else {
                    document.getElementById("upmodule"+oneData[j].id+"").checked=false;
                    document.getElementById("upmodule"+oneData[j].id+"").indeterminate = false;
                }

            }
        }

    }


}
function erjidisplayupdate(twoData){

    //二级目录显示状态
    for (let j = 0; j < twoData.length; j++) {
        let s = document.getElementById("upthreefunction"+twoData[j].id+"");
            if (s){
                let ss = s.querySelectorAll('input');



                let kk = true;
                let mm=0;
                if (ss.length>0){
                    for (let k = 0; k < ss.length; k++) {
                        if (ss[k].checked===false && ss[k].indeterminate===false){
                            kk=false;
                        }else {
                            mm++;
                        }
                    }
                }
                else {
                    kk=false;
                }


                if (kk===true){
                    document.getElementById("upfunction2"+twoData[j].id+"").checked= true;
                    document.getElementById("upfunction2"+twoData[j].id+"").indeterminate = false;

                }else {
                    if (mm>0){
                        document.getElementById("upfunction2"+twoData[j].id+"").checked=false;
                        document.getElementById("upfunction2"+twoData[j].id+"").indeterminate = true;
                    }

                }
            }

    }
}

function yijidisplay(oneData){
    //一级目录显示状态
    for (let j = 0; j < oneData.length; j++) {
        let s = document.getElementById("twoFunction"+oneData[j].id+"");
        let ss = s.querySelectorAll('input');


        let kk = true;
        let mm=0;
        for (let k = 0; k < ss.length; k++) {
            if (ss[k].checked===false){
                kk=false;
            }else {
                mm++;
            }
        }
        if (kk===true){
            document.getElementById("module"+oneData[j].id+"").checked= true;
            document.getElementById("module"+oneData[j].id+"").indeterminate = false;
        }else {
            if (mm>0){
                document.getElementById("module"+oneData[j].id+"").checked=false;
                document.getElementById("module"+oneData[j].id+"").indeterminate = true;
            }else {
                document.getElementById("module"+oneData[j].id+"").checked=false;
                document.getElementById("module"+oneData[j].id+"").indeterminate = false;
            }

        }
    }


}
function erjidisplay(twoData){
    //二级目录显示状态
    for (let j = 0; j < twoData[j].length; j++) {
        let s = document.getElementById("threefunction1"+twoData[j].id+"");
        let ss = s.querySelectorAll('input');


        let kk = true;
        let mm=0;
        for (let k = 0; k < ss.length; k++) {
            if (ss[k].checked===false){
                kk=false;
            }else {
                mm++;
            }
        }
        if (kk===true){
            document.getElementById("function2"+twoData[j].id+"").checked= true;
            document.getElementById("function2"+twoData[j].id+"").indeterminate = false;
        }else {
            if (mm>0){
                document.getElementById("function2"+twoData[j].id+"").checked=false;
                document.getElementById("function2"+twoData[j].id+"").indeterminate = true;
            }

        }
    }
}

function onetwothree(oneData,twoData,threeData){
    //一级菜单全选
    for (let i = 0; i < oneData.length; i++){
        document.getElementById("module"+oneData[i].id+"").onclick=function (){
            let button = document.getElementById("module"+oneData[i].id+"");
            let ss = document.getElementById("twoFunction"+oneData[i].id+"");
            if (button.checked===true){
                let s = ss.querySelectorAll('input');
                for (let j = 0; j < s.length; j++) {
                    s[j].checked=true;
                    s[j].indeterminate=false;
                }
            }else {
                let s = ss.querySelectorAll('input');
                for (let j = 0; j < s.length; j++) {
                    s[j].checked=false;
                    s[j].indeterminate=false;
                }
            }
        }
        document.getElementById("upmodule"+oneData[i].id+"").onclick=function (){
            let button = document.getElementById("upmodule"+oneData[i].id+"");
            let ss = document.getElementById("uptwoFunction"+oneData[i].id+"");
            if (button.checked===true){
                let s = ss.querySelectorAll('input');
                for (let j = 0; j < s.length; j++) {
                    s[j].checked=true;
                    s[j].indeterminate=false;
                }
            }else {
                let s = ss.querySelectorAll('input');
                for (let j = 0; j < s.length; j++) {
                    s[j].checked=false;
                    s[j].indeterminate=false;
                }
            }
        }

    }

    function erji(i){
        //判断是否全选了
        let n=twoData[i].moduleId;
        let ss=true;
        let nn=0;
        for (let j = 0; j < twoData.length; j++) {
            if (Number(n)===Number(twoData[j].moduleId)){
                //判断是否是选中状态
                let ff = document.getElementById("function2"+twoData[j].id+"");
                if (ff.checked===false && ff.indeterminate===false){
                    ss=false;
                }else {
                    nn++;
                    let elementById = document.getElementById("threefunction"+twoData[j].id+"");
                    let s = elementById.querySelectorAll('input');



                    for (let k = 0; k < s.length; k++) {
                        s[k].checked=true;

                    }
                }
            }
        }

        if (ss===true){
         //全选中的时候
            document.getElementById("module"+twoData[i].moduleId+"").indeterminate=false;
            document.getElementById("module"+twoData[i].moduleId+"").checked=true;
            let elementById = document.getElementById("threefunction"+twoData[i].id+"");

            let elementsByTagNameNS = elementById.querySelectorAll(".threeInput");
            for (let j = 0; j < elementsByTagNameNS.length; j++) {
                elementsByTagNameNS[j].checked=true;
            }
        }else {
            document.getElementById("module"+twoData[i].moduleId+"").checked=false;
            if (nn>0){
                document.getElementById("module"+twoData[i].moduleId+"").indeterminate=true;

            }else {
                document.getElementById("module"+twoData[i].moduleId+"").indeterminate=false;
                let elementById = document.getElementById("threefunction"+twoData[i].id+"");

                let elementsByTagNameNS = elementById.querySelectorAll(".threeInput");
                for (let j = 0; j < elementsByTagNameNS.length; j++) {
                    elementsByTagNameNS[j].checked=false;
                }
            }

        }
    }

    function uperji(i){
        //判断是否全选了
        let n=twoData[i].moduleId;
        let ss=true;
        let nn=0;
        for (let j = 0; j < twoData.length; j++) {
            if (Number(n)===Number(twoData[j].moduleId)){
                //判断是否是选中状态
                let ff = document.getElementById("upfunction2"+twoData[j].id+"");
                if ((ff.checked===false&&ff.indeterminate===false)||(ff.checked===false && ff.indeterminate===true)){
                    ss=false;
                    if (ff.checked===false && ff.indeterminate===true){
                        nn++;
                    }
                }else {
                    nn++;
                }
            }
        }


        if (ss===true){
            document.getElementById("upmodule"+twoData[i].moduleId+"").indeterminate=false;
            document.getElementById("upmodule"+twoData[i].moduleId+"").checked=true;
            let elementById = document.getElementById("upthreefunction"+twoData[i].id+"");

            let elementsByTagNameNS = elementById.querySelectorAll(".threeInput");
            for (let j = 0; j < elementsByTagNameNS.length; j++) {
                elementsByTagNameNS[j].checked=true;
            }
        }else {

            document.getElementById("upmodule"+twoData[i].moduleId+"").checked=false;
            if (nn>0){
                document.getElementById("upmodule"+twoData[i].moduleId+"").indeterminate=true;

            }else {
                document.getElementById("upmodule"+twoData[i].moduleId+"").indeterminate=false;

            }
            let elementById = document.getElementById("upthreefunction"+twoData[i].id+"");

            let elementsByTagNameNS = elementById.querySelectorAll(".threeInput");
            for (let j = 0; j < elementsByTagNameNS.length; j++) {
                elementsByTagNameNS[j].checked=true;
            }
        }
    }
    //二级菜单
    for (let i = 0; i < twoData.length; i++){

        document.getElementById("function2"+twoData[i].id+"").onclick=function (){


            if (document.getElementById("function2"+twoData[i].id+"").checked===true){

                erji(i);
                let elementById = document.getElementById("threefunction"+twoData[i].id+"");

                let elementsByTagNameNS = elementById.querySelectorAll(".threeInput");
                for (let j = 0; j < elementsByTagNameNS.length; j++) {
                    elementsByTagNameNS[j].checked=true;
                }
            }else {
                erji(i);
                let elementById = document.getElementById("threefunction"+twoData[i].id+"");

                let elementsByTagNameNS = elementById.querySelectorAll(".threeInput");
                for (let j = 0; j < elementsByTagNameNS.length; j++) {
                    elementsByTagNameNS[j].checked=false;
                }

            }
        }

        document.getElementById("upfunction2"+twoData[i].id+"").onclick=function (){


            if (document.getElementById("upfunction2"+twoData[i].id+"").checked===true){

                uperji(i);
                let elementById = document.getElementById("upthreefunction"+twoData[i].id+"");

                let elementsByTagNameNS = elementById.querySelectorAll(".threeInput");
                for (let j = 0; j < elementsByTagNameNS.length; j++) {
                    elementsByTagNameNS[j].checked=true;
                }

            }else {

                uperji(i);
                let elementById = document.getElementById("upthreefunction"+twoData[i].id+"");

                let elementsByTagNameNS = elementById.querySelectorAll(".threeInput");
                for (let j = 0; j < elementsByTagNameNS.length; j++) {
                    elementsByTagNameNS[j].checked=false;
                }

            }
        }
    }


    function sanji(i){
        let n = threeData[i].functionId;
        let ss=true;
        let nn=0;
        for (let j = 0; j < threeData.length; j++) {
            if (n===threeData[j].functionId){
                //判断是否是选中状态
                let ff = document.getElementById("function3"+threeData[j].id+"");
                if (ff.checked===false){
                    ss=false;
                }else {
                    nn++;
                }
            }
        }

        if (ss===true){
            document.getElementById("function2"+threeData[i].functionId+"").indeterminate=false;
            document.getElementById("function2"+threeData[i].functionId+"").checked=true;
            yijidisplay(oneData)

        }else {
            document.getElementById("function2"+threeData[i].functionId+"").checked=false;
            if (nn>0){
                document.getElementById("function2"+threeData[i].functionId+"").indeterminate=true;
                yijidisplay(oneData)


            }else {
                document.getElementById("function2"+threeData[i].functionId+"").indeterminate=false;
                yijidisplay(oneData)

            }
        }
    }

    function upsanji(i){
        let n = threeData[i].functionId;
        let ss=true;
        let nn=0;
        for (let j = 0; j < threeData.length; j++) {
            if (n===threeData[j].functionId){
                //判断是否是选中状态
                let ff = document.getElementById("upfunction3"+threeData[j].id+"");
                if (ff.checked===false){
                    ss=false;
                }else {
                    nn++;
                    erjidisplayupdate(twoData);
                }
            }
        }

        if (ss===true){
            document.getElementById("upfunction2"+threeData[i].functionId+"").indeterminate=false;
            document.getElementById("upfunction2"+threeData[i].functionId+"").checked=true;
            //一级目录显示状态
            yijidisplayupdate(oneData)
        }else {
            document.getElementById("upfunction2"+threeData[i].functionId+"").checked=false;
            if (nn>0){
                document.getElementById("upfunction2"+threeData[i].functionId+"").indeterminate=true;

                //一级目录显示状态
                yijidisplayupdate(oneData)
            }else {
                document.getElementById("upfunction2"+threeData[i].functionId+"").indeterminate=false;
                //一级目录显示状态
                yijidisplayupdate(oneData)
            }
        }
    }
    //三级菜单
    for (let i = 0; i < threeData.length; i++) {
        document.getElementById("function3"+threeData[i].id+"").onclick=function (){
            if (document.getElementById("function3"+threeData[i].id+"").checked===true){

                sanji(i);
            }else {
                sanji(i);

            }
        }

        document.getElementById("upfunction3"+threeData[i].id+"").onclick=function (){
            if (document.getElementById("upfunction3"+threeData[i].id+"").checked===true){

                upsanji(i);
            }else {
                upsanji(i);

            }
        }
    }
}

//重置密码
function resetPassword(datas){
    let reset = document.querySelectorAll(".reset");
    for (let i = 0; i < reset.length; i++) {
        reset[i].onclick=function (){
            if (window.confirm("您确定要重置该用户的密码？")){
                axios({
                    method:"post",
                    url:"user/resetPassWord",
                    data:datas[i].id
                }).then(function (resp){
                    let result = resp.data;
                    if (result==="success"){
                        alert("密码重置为：hb8888")
                    }
                    else {
                        alert("密码重置失败，请联系相关人员！")
                    }
                })
                return true;
            }
            else {
                return false;
            }
        }
    }

}

async function allModule(level, departId, userLevel,department) {
    let oneData;
    let twoData;
    let threeData;
    async function module(level,departId) {
        //查询所有的功能模块(一级)
        await axios({
            method: "post",
            // url:"user/selectAllModule"
            url: "departmentFunction/selectModule?level=" + level + "&departId=" + departId + ""
        }).then(async function (resp) {
            oneData = resp.data;
            console.log(oneData)
            let one = "";
            let upone = "";
            for (let i = 0; i < oneData.length; i++) {


                one += '    <div  style="padding: 1px;margin: 5px auto">\n' +
                    '                <div style="display: flex">\n' +
                    '                    <input  id="module' + oneData[i].id + '" type="checkbox" style="width: 20px;height: 20px;margin-top: 5px"><label for="module' + oneData[i].id + '" class="checkbox"></label>\n' +
                    '                    <div class="accordion" id="addOne' + oneData[i].id + '" >\n' +
                    '                     ' + oneData[i].name + '\n' +
                    '                    </div>\n' +
                    '                </div>\n' +
                    '\n' +
                    '                <div class="panel"  id="twoFunction' + oneData[i].id + '" >\n' +

                    '                </div>\n' +
                    '            </div>'
                upone += '    <div  style="padding: 1px;margin: 5px auto">\n' +
                    '                <div style="display: flex">\n' +
                    '                    <input id="upmodule' + oneData[i].id + '" type="checkbox" style="width: 20px;height: 20px;margin-top: 5px"><label for="upmodule' + oneData[i].id + '" class="checkbox"></label>\n' +
                    '                    <div class="accordion" id="upOne' + oneData[i].id + '">\n' +
                    '                     ' + oneData[i].name + '\n' +
                    '                    </div>\n' +
                    '                </div>\n' +
                    '\n' +
                    '                <div class="panel"  id="uptwoFunction' + oneData[i].id + '" >\n' +

                    '                </div>\n' +
                    '            </div>'
            }
            document.getElementById("functionData1").innerHTML = one;
            document.getElementById("upfunctionData").innerHTML = upone;

            //查询所有的功能模块(二级)
            await axios({
                method: "post",
                // url:"function/selectAllFunction"
                url: "departmentFunction/selectFunction?level=" + level + "&departId=" + departId + ""
            }).then(async function (resp) {
                twoData = resp.data;
                for (let i = 0; i < twoData.length; i++) {


                    document.getElementById("twoFunction" + twoData[i].moduleId + "").innerHTML += '<div>\n' +
                        '                        <div style="display: flex">\n' +
                        '                            <input id="function2' + twoData[i].id + '" type="checkbox" style="width: 20px;height: 20px; margin-left: 50px"><label style="margin-left: 50px" for="function2' + twoData[i].id + '" class="checkbox"></label>\n' +
                        '                            <div id="addTwo' + twoData[i].id + '" class="accordion1">' + twoData[i].functionName + '</div>\n' +
                        '                        </div>\n' +
                        '                        <div id="threefunction' + twoData[i].id + '" class="panel1">\n' +

                        '                        </div>\n' +
                        '                    </div>\n'
                    document.getElementById("uptwoFunction" + twoData[i].moduleId + "").innerHTML += '<div>\n' +
                        '                        <div style="display: flex">\n' +
                        '                            <input id="upfunction2' + twoData[i].id + '" type="checkbox" style="width: 20px;height: 20px; margin-left: 50px"><label style="margin-left: 50px" for="upfunction2' + twoData[i].id + '" class="checkbox"></label>\n' +
                        '                            <div id="upTwo' + twoData[i].id + '" class="accordion1">' + twoData[i].functionName + '</div>\n' +
                        '                        </div>\n' +
                        '                        <div id="upthreefunction' + twoData[i].id + '" class="panel1">\n' +

                        '                        </div>\n' +
                        '                    </div>\n'
                }

                //查询所有的功能(三级)


                await axios({
                    method: "post",
                    // url:"user/selectAllFunctionThree",
                    url: "departmentFunction/selectFunctionTwo?level=" + level + "&departId=" + departId + "",
                }).then(async function (resp) {
                    threeData = resp.data;
                    for (let j = 0; j < threeData.length; j++) {

                        document.getElementById("threefunction" + threeData[j].functionId + "").innerHTML += '<div>\n' +
                            '                        <div style="display: flex">\n' +
                            '                            <input class="threeInput" id="function3' + threeData[j].id + '" type="checkbox" style="width: 20px;height: 20px; margin-left: 50px"><label style="margin-left: 100px" for="function3' + threeData[j].id + '" class="checkbox"></label>\n' +
                            '                            <div class="accordion2">' + threeData[j].name + '</div>\n' +
                            '                        </div>\n' +
                            '                    </div>\n'

                        document.getElementById("upthreefunction" + threeData[j].functionId + "").innerHTML += '<div>\n' +
                            '                        <div style="display: flex">\n' +
                            '                            <input class="threeInput"  id="upfunction3' + threeData[j].id + '" type="checkbox" style="width: 20px;height: 20px; margin-left: 50px"><label style="margin-left: 100px" for="upfunction3' + threeData[j].id + '" class="checkbox"></label>\n' +
                            '                            <div class="accordion2">' + threeData[j].name + '</div>\n' +
                            '                        </div>\n' +
                            '                    </div>\n'
                    }

                    f2(oneData, twoData, threeData);

                    onetwothree(oneData, twoData, threeData);



                })


            })
        })
    }

    async function zhixing(level,departId) {
        let add = document.getElementById("addUserForm");
        let update = document.getElementById("updateUserForm");
        let zjladd = document.getElementById("zjladd");
        let zjlupdate = document.getElementById("zjlupdate");

        if ((Number(departId)===30 && add.style.display==="block") || (Number(departId)===30 && update.style.display==="block"))
        {
            if (Number(departId)===30 && add.style.display==="block"){
                //解除添加总经理职位限制
                zjladd.disabled=false;
            }
            else if (Number(departId)===30 && update.style.display==="block"){
                //解除修改总经理职位限制
                zjlupdate.disabled=false;
            }

        }
        else {
            if (add.style.display==="block"){
                zjladd.disabled=true;
                let select = document.getElementById("levelSelect");
                let options = select.querySelectorAll("option");
                let selectedIndex = select.selectedIndex;
                let value = select.options[selectedIndex].value;
                if (Number(value)===2){
                    //选中的是总经理级别
                    for (let i = 0; i < options.length; i++) {
                        let value1 = options[i].value;
                        if (Number(value1)===3 && Number(departId) !==30){
                            options[i].selected=true;
                            //获取部门
                            let select = document.getElementById("departmentSelect");
                            //2.获取索引
                            var index = select.selectedIndex;
                            //3.获取部门id
                        let value2 = select.options[index].value;
                            await module(3, value2)
                        }
                    }
                }

            }
            else if (update.style.display==="block")
                if (zjlupdate){
                    zjlupdate.disabled=true;

                    let select = document.getElementById("upLevelSelect");
                    if (select){
                        let options = select.querySelectorAll("option");
                        let selectedIndex = select.selectedIndex;
                        let value = select.options[selectedIndex].value;
                        if (Number(value)===2){
                            //选中的是总经理级别
                            for (let i = 0; i < options.length; i++) {
                                let value1 = options[i].value;
                                if (Number(value1)===3 && Number(departId) !==30){
                                    options[i].selected=true;
                                    //获取部门
                                    let select = document.getElementById("updateDepartmentSelect");
                                    //2.获取索引
                                    var index1 = select.selectedIndex;
                                    //3.获取部门id
                                    let value2 = select.options[index1].value;
                                    await module(3, value2)
                                }
                            }
                        }
                    }
                }





            }


            }

        await module(level, departId);




    await zhixing(level, departId);
    if (Number(userLevel) === 2) {
        //登录的是总经理级别的账号
        //查询除了自己以外的用户信息
        axios({
            method: "post",
            url: "user/userNoMe"
        }).then(function (resp)
        {
            let datas = resp.data;
            let formdata = ""
            if (datas.length > 0) {
                for (let i = 0; i < datas.length; i++) {
                    formdata += ' <div style="width: 100%;height: auto;display: flex;">\n' +
                        '                                <div style="width: 30%;border:white solid 1px;display: flex" ><div style="width: fit-content;height: auto;align-self:center;margin: auton 0">' + datas[i].userName + '</div></div>\n' +
                        '                                <div style="width: 20%;border:white solid 1px;display: flex" ><div style="width: fit-content;height: auto;align-self:center;margin: auton 0">' + datas[i].name + '</div></div>\n' +
                        '                                <div style="width: 10%;border:white solid 1px;display: flex" ><div style="width: fit-content;height: auto;align-self:center;margin: auton 0">' + datas[i].age + '</div></div>\n' +
                        '                                <div style="width: 15%;border:white solid 1px;display: flex" ><div style="width: fit-content;height: auto;align-self:center;margin: auton 0">' + datas[i].departmentName + '</div></div>\n' +
                        '                                <div style="width: 25%;border:white solid 1px;display: flex" ><div style="width: fit-content;height: auto;align-self:center;margin: auton 0"><a class="updateUser" href="javascript:void(0)">修改</a>&nbsp;<a class="deleteUser"  href="javascript:void(0)">删除</a>&nbsp;<a class="reset"  href="javascript:void(0)">重置密码</a></div></div>\n' +
                        '                            </div>'
                }
                document.getElementById("content").innerHTML = formdata;
//修改用户信息
                let update = document.querySelectorAll(".updateUser");
                for (let i = 0; i < update.length; i++) {

                    update[i].onclick = function () {
                        let booleanPromise = updateUserQx();
                        booleanPromise.then(async function (resp) {
                            if (resp === true) {
                                await zhixing(datas[i].level, datas[i].department)
                                wd.style.display = "block";
                                document.getElementById("addUserForm").style.display = "none";
                                document.getElementById("updateUserForm").style.display = "block";
                                document.getElementById("updateTemporaryUserForm").style.display = "none";
                                //查询所有部门信息显示到部门选择框中
                                await axios({
                                    method: "post",
                                    url: "department/selectAll"
                                }).then(function (resp) {
                                    let datas = resp.data;
                                    let formdata = "";
                                    for (let i = 0; i < datas.length; i++) {
                                        formdata += '<option class="updateOption" value=' + datas[i].id + '>' + datas[i].departmentName + '</option>'
                                    }
                                    document.getElementById("updateDepartmentSelect").innerHTML = formdata;

                                })


                                //查询用户个人信息
                                await axios({
                                    method: "post",
                                    url: "user/selectById",
                                    data: datas[i].id
                                }).then(async function (resp) {
                                    let datas = resp.data;
                                    document.getElementById("updateExitTime").value=datas.exitTime;
                                    //姓名
                                    document.getElementById("updateName").value = datas.name;
                                    //性别
                                    let sex = document.querySelectorAll(".updateSex");
                                    if (datas.sex === "男") {
                                        sex[0].checked = true
                                    } else if (datas.sex === "女") {
                                        sex[1].checked = true
                                    }
                                    //年龄
                                    document.getElementById("updateAge").value = datas.age;
                                    //用户名
                                    document.getElementById("updateUsername").value = datas.userName;
                                    //部门
                                    let options = document.querySelectorAll(".updateOption");
                                    for (let j = 0; j < options.length; j++) {
                                        if (Number(datas.department) === Number(options[j].value)) {
                                            options[j].selected = true;
                                        }
                                    }
                                    //账号等级
                                    let updateOptions = document.querySelectorAll(".updateLevelOption");
                                    for (let j = 0; j < updateOptions.length; j++) {
                                        if (Number(datas.level) === Number(updateOptions[j].value)) {
                                            updateOptions[j].selected = true;
                                        }
                                    }

                                    await module(datas.level, datas.department)
                                    //权限
                                    await axios({
                                        method: "post",
                                        url: "userFunction/selectByUserId",
                                        data: datas.id
                                    }).then(async function (resp) {
                                        let updatefunction = resp.data;
                                        for (let j = 0; j < updatefunction.length; j++) {
                                            if (Number(updatefunction[j].open) === 1 && document.getElementById("upfunction2" + updatefunction[j].functionId + "")) {
                                                document.getElementById("upfunction2" + updatefunction[j].functionId + "").checked = true;
                                            }
                                        }
                                        //查询具体权限
                                        await axios({
                                            method: "post",
                                            url: "userFunction/selectUserFunctionTwoByUserId",
                                            data: datas.id
                                        }).then(async function (resp) {
                                            let updatefunction2 = resp.data;
                                            for (let j = 0; j < updatefunction2.length; j++) {
                                                if (Number(updatefunction2[j].open_status) === 1 && document.getElementById("upfunction3" + updatefunction2[j].function_two_id + "")) {
                                                    document.getElementById("upfunction3" + updatefunction2[j].function_two_id + "").checked = true;

                                                }
                                            }

                                            //一级目录显示状态
                                            await yijidisplayupdate(oneData);
                                            //二级目录显示状态
                                            await erjidisplayupdate(twoData);

                                            //
                                            //等级
                                            let uplevel = document.getElementById("upLevelSelect");
                                            uplevel.onchange=function (){
                                                let selectedIndex = uplevel.selectedIndex;
                                                let changeLevel = uplevel.options[selectedIndex].value

                                                //部门
                                                let selects = document.getElementById("updateDepartmentSelect");
                                                let index = selects.selectedIndex;
                                                let changeDepartment = selects.options[index].value;
                                                module(changeLevel,changeDepartment)
                                            }

                                            //部门
                                            let selects = document.getElementById("updateDepartmentSelect");
                                            selects.onchange=function (){
                                                let uplevel = document.getElementById("upLevelSelect");
                                                let selectedIndex = uplevel.selectedIndex;
                                                let changeLevel = uplevel.options[selectedIndex].value

                                                //部门
                                                let index = selects.selectedIndex;
                                                let changeDepartment = selects.options[index].value;
                                                module(changeLevel,changeDepartment)
                                            }

                                        })


                                    })

                                    //最大保留时间
                                    document.getElementById("updateExitTime").value=datas.exitTime;

                                    //点击确认修改按钮
                                    document.getElementById("updateSubmit").onclick = function () {
                                        if (window.confirm("您确定要更新该用户的信息吗？")) {
                                            //检测格式
                                            let name = document.getElementById("updateName").value;
                                            let username = document.getElementById("updateUsername").value;
                                            let age = document.getElementById("updateAge").value;
                                            let updateExit = document.getElementById("updateExitTime").value;
                                            //检测名字
                                            if (name === "") {
                                                alert("请输入姓名！")
                                            } else if (age.length === 0) {
                                                alert("请输入年龄！")
                                            } else if (updateExit.length === 0) {
                                                alert("请输入账号登录后最大保持时间！")
                                            } else {
                                                //检测用户名
                                                if (/^[a-zA-Z0-9_-]{4,16}$/.test(username) === false) {
                                                    alert("您输入的用户名不合法！，请您重新输入");
                                                } else {

                                                    //检测用户名是否重复
                                                    axios({
                                                        method: "post",
                                                        url: "user/selectUserExistUpdate?id=" + datas.id + "",
                                                        data: username,
                                                    }).then(async function (resp) {
                                                        if (resp.data === true) {
                                                            alert("用户名已存在，请重新输入！")
                                                        } else {
                                                            //获取数据
                                                            let formdata = {
                                                                id: "",
                                                                userName: "",
                                                                department: "",
                                                                name: "",
                                                                age: "",
                                                                sex: "",
                                                                level: "",
                                                                exitTime: updateExit
                                                            }
                                                            //id
                                                            formdata.id = datas.id;
                                                            //userName
                                                            formdata.userName = document.getElementById("updateUsername").value;

                                                            //等级
                                                            let uplevel = document.getElementById("upLevelSelect");
                                                            let selectedIndex = uplevel.selectedIndex;
                                                            formdata.level = uplevel.options[selectedIndex].value

                                                            //部门
                                                            let selects = document.getElementById("updateDepartmentSelect");
                                                            let index = selects.selectedIndex;
                                                            formdata.department = selects.options[index].value;
                                                            //姓名
                                                            formdata.name = document.getElementById("updateName").value;
                                                            //年龄
                                                            formdata.age = document.getElementById("updateAge").value;
                                                            //性别
                                                            let sex = document.querySelectorAll(".updateSex");
                                                            if (sex[0].checked === true) {
                                                                formdata.sex = "男"
                                                            } else if (sex[1].checked === true) {
                                                                formdata.sex = "女"
                                                            }

                                                            let b = true;
                                                            if (Number(formdata.level) === Number(3)) {

                                                                let c = await updateLevelTwoExist(formdata.department,formdata.id);
                                                                if (c === true) {
                                                                    b = false;
                                                                }
                                                            }
                                                            if (b === false) {
                                                                alert("该部门已经存在一个部长级别的账号了！")
                                                            }
                                                            else {
                                                                axios({
                                                                    method: "post",
                                                                    url: "user/adminUpdateUser",
                                                                    data: formdata,
                                                                }).then(function (resp)
                                                                {
                                                                    if (resp.data === "success") {
                                                                        //更新权限信息
                                                                        let formdata1 = {
                                                                            id: "",
                                                                            userId: datas.id,
                                                                            functionId: "",
                                                                            open: "",
                                                                        }
                                                                        let formdata2 = {
                                                                            id: "",
                                                                            userId: datas.id,
                                                                            functionTwoId: "",
                                                                            openStatus: "",
                                                                        }
                                                                        let list = [twoData.length];
                                                                        let list1 = [threeData.length];
                                                                        for (let i = 0; i < twoData.length; i++) {
                                                                            let button = document.getElementById("upfunction2" + twoData[i].id + "");
                                                                            if (button) {
                                                                                formdata1.functionId = twoData[i].id;
                                                                                if (button.checked === true || button.indeterminate === true) {
                                                                                    formdata1.open = 1;
                                                                                } else if (button.checked === false && button.indeterminate === false) {
                                                                                    formdata1.open = 0;
                                                                                }
                                                                            }

                                                                            list[i] = formdata1;
                                                                            formdata1 = {
                                                                                id: "",
                                                                                userId: datas.id,
                                                                                functionId: "",
                                                                                open: "",
                                                                            }
                                                                        }
                                                                        for (let j = 0; j < threeData.length; j++) {
                                                                            let button = document.getElementById("upfunction3" + threeData[j].id + "");

                                                                            if (button) {
                                                                                formdata2.functionTwoId = threeData[j].id;
                                                                                if (button.checked === true) {
                                                                                    formdata2.openStatus = 1;
                                                                                } else {
                                                                                    formdata2.openStatus = 0;
                                                                                }
                                                                            }

                                                                            list1[j] = formdata2;
                                                                            formdata2 = {
                                                                                id: "",
                                                                                userId: datas.id,
                                                                                functionTwoId: "",
                                                                                openStatus: "",
                                                                            }
                                                                        }
                                                                        let list2 = [1];
                                                                        list2[0] = list;
                                                                        list2[1] = list1;
                                                                        axios({
                                                                            method: "post",
                                                                            url: "userFunction/updateAll?level=" + formdata.level + "",
                                                                            data: list2
                                                                        }).then(function (resp) {
                                                                            if (resp.data === "success") {
                                                                                alert("用户信息修改成功！");
                                                                                location.reload();
                                                                            } else {
                                                                                alert("用户权限信息修改失败，请联系相关人员")
                                                                            }
                                                                        })

                                                                    } else {
                                                                        alert("用户信息修改失败，请联系相关人员")
                                                                    }
                                                                })
                                                            }



                                                        }
                                                    })


                                                }
                                            }


                                            return true;
                                        } else {
                                            return false;
                                        }
                                    }
                                })
                            } else {
                                alert("您暂未获得修改用户信息的权限！")
                            }
                        })


                    }
                }
                //删除用户信息
                let deletes = document.querySelectorAll(".deleteUser");
                for (let i = 0; i < deletes.length; i++) {
                    deletes[i].onclick = function () {
                        let booleanPromise = deleteUserQx();
                        booleanPromise.then(function (resp) {
                            if (resp === true) {
                                if (window.confirm("您确定要删除这个用户吗？")) {
                                    axios({
                                        method: "post",
                                        url: "user/delete",
                                        data: datas[i].id
                                    }).then(function (resp) {
                                        if (resp.data === "success") {
                                            alert("删除成功！");
                                            location.reload();

                                        } else {
                                            alert("删除失败，请联系相关人员！")
                                        }
                                    })
                                    return true;
                                } else {
                                    return false;
                                }
                            } else {
                                alert("您暂未获得删除用户的权限！")
                            }
                        })

                    }
                }

                //重置密码
                resetPassword(datas);


            } else {
                document.getElementById("content").innerHTML = '<div style="width: 100%;height: auto;color: red;text-align: center">暂未添加用户信息！</div>'
            }


        })
    }
    else if (Number(userLevel) === 3) {
        document.getElementById("upqxa").innerHTML="";
        document.getElementById("updateDepartmentSelect").disabled=true;
        // 登录的是部长级别的账号
        //部长查询自己部门的人员信息
        axios({
            method: "post",
            url: "user/managerSelectUser?department=" +department+ "",
            data: userId
        }).then(function (resp) {
            let datas = resp.data;
            let formdata = ""
            if (datas.length > 0) {
                for (let i = 0; i < datas.length; i++) {
                    formdata += ' <div style="width: 100%;height: auto;display: flex;">\n' +
                        '                                <div style="width: 30%;border:white solid 1px;display: flex" ><div style="width: fit-content;height: auto;align-self:center;margin: auto 0">' + datas[i].userName + '</div></div>\n' +
                        '                                <div style="width: 20%;border:white solid 1px;display: flex" ><div style="width: fit-content;height: auto;align-self:center;margin: auto 0">' + datas[i].name + '</div></div>\n' +
                        '                                <div style="width: 10%;border:white solid 1px;display: flex" ><div style="width: fit-content;height: auto;align-self:center;margin: auto 0">' + datas[i].age + '</div></div>\n' +
                        '                                <div style="width: 15%;border:white solid 1px;display: flex" ><div style="width: fit-content;height: auto;align-self:center;margin: auto 0">' + datas[i].departmentName + '</div></div>\n' +
                        '                                <div style="width: 25%;border:white solid 1px;display: flex" ><div style="width: fit-content;height: auto;align-self:center;margin: auto 0"><a class="updateUser" href="javascript:void(0)">修改</a>&nbsp;<a style="pointer-events:none;color: grey" class="deleteUser"  href="javascript:void(0)">删除</a>&nbsp;<a class="reset"  href="javascript:void(0)">重置密码</a></div></div>\n' +
                        '                            </div>'
                }
                document.getElementById("content").innerHTML = formdata;
//修改用户信息
                let update = document.querySelectorAll(".updateUser");
                for (let i = 0; i < update.length; i++) {

                    update[i].onclick = function () {
                        let booleanPromise = updateUserQx();
                        booleanPromise.then(async function (resp) {
                            if (resp === true) {


                                // await zhixing(datas[i].level, datas[i].department)

                                wd.style.display = "block";
                                document.getElementById("addUserForm").style.display = "none";
                                document.getElementById("updateUserForm").style.display = "block";
                                document.getElementById("updateTemporaryUserForm").style.display = "none";
                                //查询所有部门信息显示到部门选择框中
                                axios({
                                    method: "post",
                                    url: "department/selectAll"
                                }).then(function (resp) {
                                    let datas = resp.data;
                                    let formdata = "";
                                    for (let i = 0; i < datas.length; i++) {
                                        formdata += '<option class="updateOption" value=' + datas[i].id + '>' + datas[i].departmentName + '</option>'
                                    }
                                    document.getElementById("updateDepartmentSelect").innerHTML = formdata;

                                })


                                //查询用户个人信息
                                axios({
                                    method: "post",
                                    url: "user/selectById",
                                    data: datas[i].id
                                }).then(async function (resp) {
                                    let datas = resp.data;
                                    document.getElementById("updateExitTime").value=datas.exitTime;
                                    //姓名
                                    document.getElementById("updateName").value = datas.name;
                                    //性别
                                    let sex = document.querySelectorAll(".updateSex");
                                    if (datas.sex === "男") {
                                        sex[0].checked = true
                                    } else if (datas.sex === "女") {
                                        sex[1].checked = true
                                    }
                                    //年龄
                                    document.getElementById("updateAge").value = datas.age;
                                    //用户名
                                    document.getElementById("updateUsername").value = datas.userName;
                                    //部门
                                    let options = document.querySelectorAll(".updateOption");
                                    for (let j = 0; j < options.length; j++) {
                                        if (Number(datas.department) === Number(options[j].value)) {
                                            options[j].selected = true;
                                        }
                                    }
                                    //账号等级
                                    // let updateOptions = document.querySelectorAll(".updateLevelOption");
                                    // for (let j = 0; j < updateOptions.length; j++) {
                                    //     if (Number(datas.level) === Number(updateOptions[j].value)) {
                                    //         updateOptions[j].selected = true;
                                    //     }
                                    // }
                                    // await module(datas.level, datas.department)
                                    // //权限
                                    // axios({
                                    //     method: "post",
                                    //     url: "userFunction/selectByUserId",
                                    //     data: datas.id
                                    // }).then(function (resp)
                                    // {
                                    //     let updatefunction = resp.data;
                                    //     for (let j = 0; j < updatefunction.length; j++) {
                                    //         if (Number(updatefunction[j].open) === 1 && document.getElementById("upfunction2" + updatefunction[j].functionId + "")) {
                                    //             document.getElementById("upfunction2" + updatefunction[j].functionId + "").checked = true;
                                    //         }
                                    //     }
                                    //     //查询具体权限
                                    //     axios({
                                    //         method: "post",
                                    //         url: "userFunction/selectUserFunctionTwoByUserId",
                                    //         data: datas.id
                                    //     }).then(async function (resp) {
                                    //         let updatefunction2 = resp.data;
                                    //         for (let j = 0; j < updatefunction2.length; j++) {
                                    //             if (Number(updatefunction2[j].open_status) === 1 && document.getElementById("upfunction3" + updatefunction2[j].function_two_id + "")) {
                                    //                 document.getElementById("upfunction3" + updatefunction2[j].function_two_id + "").checked = true;
                                    //
                                    //             }
                                    //         }
                                    //
                                    //         //一级目录显示状态
                                    //         await yijidisplayupdate(oneData);
                                    //         //二级目录显示状态
                                    //         await erjidisplayupdate(twoData);
                                    //
                                    //         //
                                    //
                                    //
                                    //         // //部门
                                    //         // let selects = document.getElementById("updateDepartmentSelect");
                                    //         // selects.onchange = function () {
                                    //         //
                                    //         //
                                    //         //     //部门
                                    //         //     let index = selects.selectedIndex;
                                    //         //     let changeDepartment = selects.options[index].value;
                                    //         //     zhixing(3, changeDepartment)
                                    //         // }
                                    //
                                    //
                                    //     })
                                    //
                                    //
                                    // })


                                    //点击确认修改按钮
                                    document.getElementById("updateSubmit").onclick = function () {
                                        if (window.confirm("您确定要更新该用户的信息吗？")) {
                                            //检测格式
                                            let name = document.getElementById("updateName").value;
                                            let username = document.getElementById("updateUsername").value;
                                            let age = document.getElementById("updateAge").value;
                                            let updateExit = document.getElementById("updateExitTime").value;
                                            //检测名字
                                            if (name === "") {
                                                alert("请输入姓名！")
                                            } else if (age.length === 0) {
                                                alert("请输入年龄！")
                                            } else if (updateExit.length === 0) {
                                                alert("请输入账号登录后最大保持时间！")
                                            } else {
                                                //检测用户名
                                                if (/^[a-zA-Z0-9_-]{4,16}$/.test(username) === false) {
                                                    alert("您输入的用户名不合法！，请您重新输入");
                                                } else {

                                                    //检测用户名是否重复
                                                    axios({
                                                        method: "post",
                                                        url: "user/selectUserExistUpdate?id=" + datas.id + "",
                                                        data: username,
                                                    }).then(function (resp) {
                                                        if (resp.data === true) {
                                                            alert("用户名已存在，请重新输入！")
                                                        } else {
                                                            //获取数据
                                                            let formdata = {
                                                                id: "",
                                                                userName: "",
                                                                department: "",
                                                                name: "",
                                                                age: "",
                                                                sex: "",
                                                                level: "",
                                                                exitTime: updateExit
                                                            }
                                                            //id
                                                            formdata.id = datas.id;
                                                            //userName
                                                            formdata.userName = document.getElementById("updateUsername").value;

                                                            //等级
                                                            // let uplevel = document.getElementById("upLevelSelect");
                                                            // let selectedIndex = uplevel.selectedIndex;
                                                            formdata.level = 4;

                                                            //部门

                                                            formdata.department = department;
                                                            //姓名
                                                            formdata.name = document.getElementById("updateName").value;
                                                            //年龄
                                                            formdata.age = document.getElementById("updateAge").value;
                                                            //性别
                                                            let sex = document.querySelectorAll(".updateSex");
                                                            if (sex[0].checked === true) {
                                                                formdata.sex = "男"
                                                            } else if (sex[1].checked === true) {
                                                                formdata.sex = "女"
                                                            }


                                                            axios({
                                                                method: "post",
                                                                url: "user/adminUpdateUser",
                                                                data: formdata,
                                                            }).then(function (resp) {
                                                                if (resp.data==="success"){
                                                                    alert("更新成功！");
                                                                    location.reload();
                                                                }
                                                                else {
                                                                    alert("更新失败，请联系相关人员！")
                                                                }

                                                                // if (resp.data === "success") {
                                                                //     //更新权限信息
                                                                //     let formdata1 = {
                                                                //         id: "",
                                                                //         userId: datas.id,
                                                                //         functionId: "",
                                                                //         open: "",
                                                                //     }
                                                                //     let formdata2 = {
                                                                //         id: "",
                                                                //         userId: datas.id,
                                                                //         functionTwoId: "",
                                                                //         openStatus: "",
                                                                //     }
                                                                //     let list = [twoData.length];
                                                                //     let list1 = [threeData.length];
                                                                //     for (let i = 0; i < twoData.length; i++) {
                                                                //         let button = document.getElementById("upfunction2" + twoData[i].id + "");
                                                                //         if (button) {
                                                                //             formdata1.functionId = twoData[i].id;
                                                                //             if (button.checked === true || button.indeterminate === true) {
                                                                //                 formdata1.open = 1;
                                                                //             } else if (button.checked === false && button.indeterminate === false) {
                                                                //                 formdata1.open = 0;
                                                                //             }
                                                                //
                                                                //         }
                                                                //
                                                                //         list[i] = formdata1;
                                                                //         formdata1 = {
                                                                //             id: "",
                                                                //             userId: datas.id,
                                                                //             functionId: "",
                                                                //             open: "",
                                                                //         }
                                                                //     }
                                                                //     for (let j = 0; j < threeData.length; j++) {
                                                                //         let button = document.getElementById("upfunction3" + threeData[j].id + "");
                                                                //         if (button) {
                                                                //             formdata2.functionTwoId = threeData[j].id;
                                                                //             if (button.checked === true) {
                                                                //                 formdata2.openStatus = 1;
                                                                //             } else {
                                                                //                 formdata2.openStatus = 0;
                                                                //             }
                                                                //         }
                                                                //
                                                                //         list1[j] = formdata2;
                                                                //         formdata2 = {
                                                                //             id: "",
                                                                //             userId: datas.id,
                                                                //             functionTwoId: "",
                                                                //             openStatus: "",
                                                                //         }
                                                                //     }
                                                                //     let list2 = [1];
                                                                //     list2[0] = list;
                                                                //     list2[1] = list1;
                                                                //     axios({
                                                                //         method: "post",
                                                                //         url: "userFunction/updateAll?level=" + formdata.level + "",
                                                                //         data: list2
                                                                //     }).then(function (resp) {
                                                                //         if (resp.data === "success") {
                                                                //             alert("用户信息修改成功！");
                                                                //             location.reload();
                                                                //         } else {
                                                                //             alert("用户权限信息修改失败，请联系相关人员")
                                                                //         }
                                                                //     })
                                                                //
                                                                // }
                                                                // else {
                                                                //     alert("用户信息修改失败，请联系相关人员")
                                                                // }
                                                            })

                                                        }
                                                    })


                                                }
                                            }


                                            return true;
                                        } else {
                                            return false;
                                        }
                                    }
                                })
                            } else {
                                alert("您暂未获得修改用户信息的权限！")
                            }
                        })


                    }
                }
                //删除用户信息
                let deletes = document.querySelectorAll(".deleteUser");
                for (let i = 0; i < deletes.length; i++) {
                    deletes[i].onclick = function () {
                        let booleanPromise = deleteUserQx();
                        booleanPromise.then(function (resp) {
                            if (resp === true) {
                                if (window.confirm("您确定要删除这个用户吗？")) {
                                    axios({
                                        method: "post",
                                        url: "user/delete",
                                        data: datas[i].id
                                    }).then(function (resp) {
                                        if (resp.data === "success") {
                                            alert("删除成功！");
                                            location.reload();

                                        } else {
                                            alert("删除失败，请联系相关人员！")
                                        }
                                    })
                                    return true;
                                } else {
                                    return false;
                                }
                            } else {
                                alert("您暂未获得删除用户的权限！")
                            }
                        })

                    }
                }

                //重置密码
                resetPassword(datas);


            } else {
                document.getElementById("content").innerHTML = '<div style="width: 100%;height: auto;color: red;text-align: center">暂未添加用户信息！</div>'
            }
        })


    }
    else if (Number(userLevel)===1){
        //管理员级别
        //查询除了自己以外的用户信息
        axios({
            method: "post",
            url: "user/userNoMe"
        }).then(function (resp)
        {
            let datas = resp.data;
            let formdata = ""
            if (datas.length > 0) {
                for (let i = 0; i < datas.length; i++) {
                    formdata += ' <div style="width: 100%;height: auto;display: flex;">\n' +
                        '                                <div style="width: 30%;border:white solid 1px;display: flex" ><div style="width: fit-content;height: auto;align-self:center;margin: auto 0">' + datas[i].userName + '</div></div>\n' +
                        '                                <div style="width: 20%;border:white solid 1px;display: flex" ><div style="width: fit-content;height: auto;align-self:center;margin: auto 0">' + datas[i].name + '</div></div>\n' +
                        '                                <div style="width: 10%;border:white solid 1px;display: flex" ><div style="width: fit-content;height: auto;align-self:center;margin: auto 0">' + datas[i].age + '</div></div>\n' +
                        '                                <div style="width: 15%;border:white solid 1px;display: flex" ><div style="width: fit-content;height: auto;align-self:center;margin: auto 0">' + datas[i].departmentName + '</div></div>\n' +
                        '                                <div style="width: 25%;border:white solid 1px;display: flex" ><div style="width: fit-content;height: auto;align-self:center;margin: auto 0"><a class="updateUser" href="javascript:void(0)">修改</a>&nbsp;<a class="deleteUser"  href="javascript:void(0)">删除</a>&nbsp;<a class="reset"  href="javascript:void(0)">重置密码</a></div></div>\n' +
                        '                            </div>'
                }
                document.getElementById("content").innerHTML = formdata;
//修改用户信息
                let update = document.querySelectorAll(".updateUser");
                for (let i = 0; i < update.length; i++) {

                    update[i].onclick = function () {
                        let booleanPromise = updateUserQx();
                        booleanPromise.then(async function (resp) {
                            if (resp === true) {
                                await zhixing(datas[i].level, datas[i].department)
                                wd.style.display = "block";
                                document.getElementById("addUserForm").style.display = "none";
                                document.getElementById("updateUserForm").style.display = "block";
                                document.getElementById("updateTemporaryUserForm").style.display = "none";
                                //查询所有部门信息显示到部门选择框中
                                await axios({
                                    method: "post",
                                    url: "department/selectAll"
                                }).then(function (resp) {
                                    let datas = resp.data;
                                    let formdata = "";
                                    for (let i = 0; i < datas.length; i++) {
                                        formdata += '<option class="updateOption" value=' + datas[i].id + '>' + datas[i].departmentName + '</option>'
                                    }
                                    document.getElementById("updateDepartmentSelect").innerHTML = formdata;

                                })


                                //查询用户个人信息
                                await axios({
                                    method: "post",
                                    url: "user/selectById",
                                    data: datas[i].id
                                }).then(async function (resp) {
                                    let datas = resp.data;

                                    //姓名
                                    document.getElementById("updateName").value = datas.name;
                                    //性别
                                    let sex = document.querySelectorAll(".updateSex");
                                    if (datas.sex === "男") {
                                        sex[0].checked = true
                                    } else if (datas.sex === "女") {
                                        sex[1].checked = true
                                    }
                                    //年龄
                                    document.getElementById("updateAge").value = datas.age;
                                    //用户名
                                    document.getElementById("updateUsername").value = datas.userName;
                                    //部门
                                    let options = document.querySelectorAll(".updateOption");
                                    for (let j = 0; j < options.length; j++) {
                                        if (Number(datas.department) === Number(options[j].value)) {
                                            options[j].selected = true;
                                        }
                                    }
                                    //账号等级
                                    let updateOptions = document.querySelectorAll(".updateLevelOption");
                                    for (let j = 0; j < updateOptions.length; j++) {
                                        if (Number(datas.level) === Number(updateOptions[j].value)) {
                                            updateOptions[j].selected = true;
                                        }
                                    }
                                    await module(datas.level, datas.department)
                                    //权限
                                    await axios({
                                        method: "post",
                                        url: "userFunction/selectByUserId",
                                        data: datas.id
                                    }).then(async function (resp) {
                                        let updatefunction = resp.data;
                                        for (let j = 0; j < updatefunction.length; j++) {
                                            if (Number(updatefunction[j].open) === 1 && document.getElementById("upfunction2" + updatefunction[j].functionId + "")) {
                                                document.getElementById("upfunction2" + updatefunction[j].functionId + "").checked = true;
                                            }
                                        }
                                        //查询具体权限
                                        await axios({
                                            method: "post",
                                            url: "userFunction/selectUserFunctionTwoByUserId",
                                            data: datas.id
                                        }).then(async function (resp) {
                                            let updatefunction2 = resp.data;
                                            for (let j = 0; j < updatefunction2.length; j++) {
                                                if (Number(updatefunction2[j].open_status) === 1 && document.getElementById("upfunction3" + updatefunction2[j].function_two_id + "")) {
                                                    document.getElementById("upfunction3" + updatefunction2[j].function_two_id + "").checked = true;

                                                }
                                            }
                                            // await module(datas.level, datas.department)
                                            //一级目录显示状态
                                            await yijidisplayupdate(oneData);
                                            //二级目录显示状态
                                            await erjidisplayupdate(twoData);

                                            //
                                            //等级
                                            let uplevel = document.getElementById("upLevelSelect");
                                            uplevel.onchange=function (){

                                                let selectedIndex = uplevel.selectedIndex;
                                                let changeLevel = uplevel.options[selectedIndex].value

                                                //部门
                                                let selects = document.getElementById("updateDepartmentSelect");
                                                let index = selects.selectedIndex;
                                                let changeDepartment = selects.options[index].value;
                                                module(changeLevel,changeDepartment)
                                            }

                                            //部门
                                            let selects = document.getElementById("updateDepartmentSelect");
                                            selects.onchange=function (){

                                                let uplevel = document.getElementById("upLevelSelect");
                                                let selectedIndex = uplevel.selectedIndex;
                                                let changeLevel = uplevel.options[selectedIndex].value

                                                //部门
                                                let index = selects.selectedIndex;
                                                let changeDepartment = selects.options[index].value;
                                                module(changeLevel,changeDepartment)
                                            }

                                        })


                                    })

                                    //最大保留时间
                                    document.getElementById("updateExitTime").value=datas.exitTime;

                                    //点击确认修改按钮
                                    document.getElementById("updateSubmit").onclick = function () {
                                        if (window.confirm("您确定要更新该用户的信息吗？")) {
                                            //检测格式
                                            let name = document.getElementById("updateName").value;
                                            let username = document.getElementById("updateUsername").value;
                                            let age = document.getElementById("updateAge").value;
                                            let updateExit = document.getElementById("updateExitTime").value;
                                            //检测名字
                                            if (name === "") {
                                                alert("请输入姓名！")
                                            } else if (age.length === 0) {
                                                alert("请输入年龄！")
                                            } else if (updateExit.length === 0) {
                                                alert("请输入账号登录后最大保持时间！")
                                            } else {
                                                //检测用户名
                                                if (/^[a-zA-Z0-9_-]{4,16}$/.test(username) === false) {
                                                    alert("您输入的用户名不合法！，请您重新输入");
                                                } else {

                                                    //检测用户名是否重复
                                                    axios({
                                                        method: "post",
                                                        url: "user/selectUserExistUpdate?id=" + datas.id + "",
                                                        data: username,
                                                    }).then(async function (resp) {
                                                        if (resp.data === true) {
                                                            alert("用户名已存在，请重新输入！")
                                                        } else {
                                                            //获取数据
                                                            let formdata = {
                                                                id: "",
                                                                userName: "",
                                                                department: "",
                                                                name: "",
                                                                age: "",
                                                                sex: "",
                                                                level: "",
                                                                exitTime: updateExit
                                                            }
                                                            //id
                                                            formdata.id = datas.id;
                                                            //userName
                                                            formdata.userName = document.getElementById("updateUsername").value;

                                                            //等级
                                                            let uplevel = document.getElementById("upLevelSelect");
                                                            let selectedIndex = uplevel.selectedIndex;
                                                            formdata.level = uplevel.options[selectedIndex].value

                                                            //部门
                                                            let selects = document.getElementById("updateDepartmentSelect");
                                                            let index = selects.selectedIndex;
                                                            formdata.department = selects.options[index].value;
                                                            //姓名
                                                            formdata.name = document.getElementById("updateName").value;
                                                            //年龄
                                                            formdata.age = document.getElementById("updateAge").value;
                                                            //性别
                                                            let sex = document.querySelectorAll(".updateSex");
                                                            if (sex[0].checked === true) {
                                                                formdata.sex = "男"
                                                            } else if (sex[1].checked === true) {
                                                                formdata.sex = "女"
                                                            }

                                                            let b = true;
                                                            if (Number(formdata.level) === Number(3)) {

                                                                let c = await updateLevelTwoExist(formdata.department,formdata.id);
                                                                if (c === true) {
                                                                    b = false;
                                                                }
                                                            }
                                                            if (b === false) {
                                                                alert("该部门已经存在一个部长级别的账号了！")
                                                            }
                                                            else {
                                                                axios({
                                                                    method: "post",
                                                                    url: "user/adminUpdateUser",
                                                                    data: formdata,
                                                                }).then(function (resp)
                                                                {
                                                                    if (resp.data === "success") {
                                                                        //更新权限信息
                                                                        let formdata1 = {
                                                                            id: "",
                                                                            userId: datas.id,
                                                                            functionId: "",
                                                                            open: "",
                                                                        }
                                                                        let formdata2 = {
                                                                            id: "",
                                                                            userId: datas.id,
                                                                            functionTwoId: "",
                                                                            openStatus: "",
                                                                        }
                                                                        let list = [twoData.length];
                                                                        let list1 = [threeData.length];
                                                                        for (let i = 0; i < twoData.length; i++) {
                                                                            let button = document.getElementById("upfunction2" + twoData[i].id + "");
                                                                            if (button) {
                                                                                formdata1.functionId = twoData[i].id;
                                                                                if (button.checked === true || button.indeterminate === true) {
                                                                                    formdata1.open = 1;
                                                                                } else if (button.checked === false && button.indeterminate === false) {
                                                                                    formdata1.open = 0;
                                                                                }
                                                                            }

                                                                            list[i] = formdata1;
                                                                            formdata1 = {
                                                                                id: "",
                                                                                userId: datas.id,
                                                                                functionId: "",
                                                                                open: "",
                                                                            }
                                                                        }
                                                                        for (let j = 0; j < threeData.length; j++) {
                                                                            let button = document.getElementById("upfunction3" + threeData[j].id + "");

                                                                            if (button) {
                                                                                formdata2.functionTwoId = threeData[j].id;
                                                                                if (button.checked === true) {
                                                                                    formdata2.openStatus = 1;
                                                                                } else {
                                                                                    formdata2.openStatus = 0;
                                                                                }
                                                                            }

                                                                            list1[j] = formdata2;
                                                                            formdata2 = {
                                                                                id: "",
                                                                                userId: datas.id,
                                                                                functionTwoId: "",
                                                                                openStatus: "",
                                                                            }
                                                                        }
                                                                        let list2 = [1];
                                                                        list2[0] = list;
                                                                        list2[1] = list1;
                                                                        axios({
                                                                            method: "post",
                                                                            url: "userFunction/updateAll?level=" + formdata.level + "",
                                                                            data: list2
                                                                        }).then(function (resp) {
                                                                            if (resp.data === "success") {
                                                                                alert("用户信息修改成功！");
                                                                                location.reload();
                                                                            } else {
                                                                                alert("用户权限信息修改失败，请联系相关人员")
                                                                            }
                                                                        })

                                                                    } else {
                                                                        alert("用户信息修改失败，请联系相关人员")
                                                                    }
                                                                })
                                                            }



                                                        }
                                                    })


                                                }
                                            }


                                            return true;
                                        } else {
                                            return false;
                                        }
                                    }
                                })
                            } else {
                                alert("您暂未获得修改用户信息的权限！")
                            }
                        })


                    }
                }
                //删除用户信息
                let deletes = document.querySelectorAll(".deleteUser");
                for (let i = 0; i < deletes.length; i++) {
                    deletes[i].onclick = function () {
                        let booleanPromise = deleteUserQx();
                        booleanPromise.then(function (resp) {
                            if (resp === true) {
                                if (window.confirm("您确定要删除这个用户吗？")) {
                                    axios({
                                        method: "post",
                                        url: "user/delete",
                                        data: datas[i].id
                                    }).then(function (resp) {
                                        if (resp.data === "success") {
                                            alert("删除成功！");
                                            location.reload();

                                        } else {
                                            alert("删除失败，请联系相关人员！")
                                        }
                                    })
                                    return true;
                                } else {
                                    return false;
                                }
                            } else {
                                alert("您暂未获得删除用户的权限！")
                            }
                        })

                    }
                }
                //重置密码
                resetPassword(datas);

            } else {
                document.getElementById("content").innerHTML = '<div style="width: 100%;height: auto;color: red;text-align: center">暂未添加用户信息！</div>'
            }


        })
    }

    //新增用户
    document.getElementById("submit").onclick = function () {
        //检测格式
        let name = document.getElementById("name").value;
        let username = document.getElementById("username").value;
        let password = document.getElementById("password").value;
        let age = document.getElementById("age").value;
        let passwordAgain = document.getElementById("passwordAgain").value;
        // let exitTime = document.getElementById("ExitTime").value;
        //检测名字
        if (name === "") {
            alert("请输入姓名！")
        } else if (age.length === 0) {
            alert("请输入年龄！")
        } else {
            //检测用户名
            if (/^[a-zA-Z0-9_-]{4,16}$/.test(username) === false) {
                alert("您输入的用户名不合法！，请您重新输入");
            } else {
                //检测密码   /^\S*(?=\S{6,})(?=\S*\d)(?=\S*[A-Z])(?=\S*[a-z])\S*$/
                if (/^[a-zA-Z0-9]{6,20}$/.test(password) === false) {
                    alert("您输入的密码格式错误，请您重新输入")
                } else {
                    if (password!==passwordAgain){
                        alert("您输入的两次密码不一致！")
                    }
                    else {
                        //检测用户名是否重复
                        axios({
                            method: "post",
                            url: "user/selectUserExist",
                            data: username,
                        }).then(async function (resp)
                        {
                            if (resp.data === true) {
                                alert("用户名已存在，请重新输入！")
                            } else {
                                var formdata = {
                                    id: "",
                                    userName: "",
                                    passWord: "",
                                    department: "",
                                    name: "",
                                    age: "",
                                    sex: "",
                                    level: "",
                                    exitTime: 30
                                }
                                formdata.name = document.getElementById("name").value;
                                //性别
                                let sex = document.querySelectorAll(".sex");

                                if (sex[0].checked === true) {
                                    formdata.sex = "男"
                                } else if (sex[1].checked === true) {
                                    formdata.sex = "女"
                                }
                                //用户名密码
                                formdata.userName = document.getElementById("username").value;
                                formdata.passWord = document.getElementById("password").value;

                                //年龄
                                formdata.age = document.getElementById("age").value;

                                //所属部门
                                //1.获取select对象
                                let select = document.getElementById("departmentSelect");
                                //2.获取索引
                                var index = select.selectedIndex;
                                //3.获取部门id
                                formdata.department = select.options[index].value;

                                //获取账号等级
                                //1.获取select对象
                                let levelSelect = document.getElementById("levelSelect");
                                //2.获取索引
                                let selectedIndex = levelSelect.selectedIndex;
                                //3.获取等级
                                formdata.level = levelSelect.options[selectedIndex].value;

                                let all = [];

                                let b = true ;
                                if (Number(formdata.level) === Number(3)) {
                                    console.log("aaa")
                                    let c = await levelTwoExist(formdata.department);
                                    if (c===true){
                                        b = false;
                                    }
                                }
                                if (b===false){
                                    alert("该部门已经存在一个部长级别的账号了！")
                                }
                                else {
                                    axios({
                                        method: "post",
                                        url: "user/add",
                                        data: formdata,
                                    }).then(function (resp)
                                    {
                                        let id = resp.data;
                                        if (id) {
                                            //用户添加成功，现在要获取功能信息
                                            let formdata = {
                                                id: "",
                                                userId: id,
                                                functionId: "",
                                                open: "",
                                            }
                                            var list = [twoData.length];
                                            for (let i = 0; i < twoData.length; i++) {
                                                let button = document.getElementById("function2" + twoData[i].id + "");
                                                formdata.functionId = twoData[i].id;
                                                if (button.checked === true) {
                                                    formdata.open = 1;
                                                } else {
                                                    formdata.open = 0;
                                                }
                                                list[i] = formdata;
                                                formdata = {
                                                    id: "",
                                                    userId: id,
                                                    functionId: "",
                                                    open: "",
                                                }
                                            }

                                            let formdata1 = {
                                                id: "",
                                                functionTwoId: "",
                                                userId: id,
                                                openStatus: ""
                                            }
                                            var list1 = [threeData.length];
                                            for (let i = 0; i < threeData.length; i++) {
                                                let button = document.getElementById("function3" + threeData[i].id + "");
                                                formdata1.functionTwoId = threeData[i].id;
                                                if (button.checked === true) {
                                                    formdata1.openStatus = 1;
                                                } else {
                                                    formdata1.openStatus = 0;
                                                }
                                                list1[i] = formdata1;
                                                formdata1 = {
                                                    id: "",
                                                    functionTwoId: "",
                                                    userId: id,
                                                    openStatus: ""
                                                }
                                            }
                                            all[0] = list;
                                            all[1] = list1;
                                            //得到了所有的功能信息，进行功能信息添加操作
                                            axios({
                                                method: "post",
                                                url: "userFunction/addAll",
                                                data: all
                                            }).then(function (resp) {
                                                if (resp.data === "success") {
                                                    alert("用户信息添加成功！");
                                                    location.reload();
                                                } else {
                                                    //删除刚添加的基本信息
                                                    axios({
                                                        method: "post",
                                                        url: "user/delete",
                                                        data: id
                                                    }).then(function (resp) {
                                                        if (resp.data === "success") {
                                                            alert("用户信息添加失败，请联系相关人员！")
                                                        } else {
                                                            alert("系统错误，添加失败，请联系相关人员！")
                                                        }
                                                    })

                                                }
                                            })


                                        } else {
                                            alert("用户信息添加失败，请联系相关人员")
                                        }
                                    })

                                }


                            }
                        })
                    }


                }
            }
        }


    }
    //新增临时用户
    document.getElementById("submitTemporary").onclick = function () {

        //检测格式
        let username = document.getElementById("username").value;
        let password = document.getElementById("password").value;
        //检测名字

        //检测用户名
        if (/^[a-zA-Z0-9_-]{4,16}$/.test(username) === false) {
            alert("您输入的用户名不合法！，请您重新输入");
        } else {
            //检测密码
            if (/^[a-zA-Z0-9]{6,20}$/.test(password) === false) {
                alert("您输入的密码格式错误，请您重新输入")
            } else {
                //检测有效期
                let value = document.getElementById("overTime").value;
                if (value.length > 0) {
                    //检测用户名是否重复
                    axios({
                        method: "post",
                        url: "temporaryUser/selectExistUserName",
                        data: username,
                    }).then(function (resp) {
                        if (resp.data === "success") {
                            alert("用户名已存在，请重新输入！")
                        } else {
                            var formdata = {
                                id: "",
                                userName: "",
                                passWord: "",
                                department: "",
                                createTime: "",
                                overTime: "",
                                userId: ""
                            }


                            //用户名密码
                            formdata.userName = document.getElementById("username").value;
                            formdata.passWord = document.getElementById("password").value;


                            //所属部门
                            //1.获取select对象
                            let select = document.getElementById("departmentSelect");
                            //2.获取索引
                            var index = select.selectedIndex;
                            //3.获取部门id
                            formdata.department = select.options[index].value;
                            var oneDay = 24 * 60 * 60 * 1000; // 每天毫秒数

                            //获取创建时间
                            formdata.createTime = fDate1(new Date());
                            formdata.overTime = fDate1(new Date(new Date().getTime() + value * oneDay))
                            formdata.userId = window.parent.document.getElementById("userId").value;


                            axios({
                                method: "post",
                                url: "temporaryUser/addTemporaryUser",
                                data: formdata,
                            }).then(function (resp) {
                                let id = resp.data;
                                if (id === "success") {
                                    alert("添加成功！");
                                    window.parent.document.getElementById("iframepage").contentWindow.location.reload();
                                } else {
                                    alert("用户信息添加失败，请联系相关人员")
                                }
                            })

                        }
                    })

                } else {
                    alert("有效期未填写！")
                }

            }
        }
    }

}


//查询用户的级别
axios({
    method:"post",
    url:"user/selectName"
}).then(function (resp){
    let id = resp.data.id;
    let department=resp.data.department;
    axios({
        method:"post",
        url:"user/selectLevelById",
        data:id
    }).then(async function (resp) {
        let level = resp.data;
        if (Number(level) === 2) {
            //总经理级别

            //查询所有部门信息显示到部门选择框中
            axios({
                method: "post",
                url: "department/selectAllTwo"
            }).then(function (resp) {
                let datas = resp.data;
                let formdata = "";
                for (let i = 0; i < datas.length; i++) {
                    formdata += '<option value=' + datas[i].id + '>' + datas[i].departmentName + '</option>'
                }
                document.getElementById("departmentSelect").innerHTML = formdata;

            })


            //设置账号等级的功能
            document.getElementById("setLevel").innerHTML = ' <div style="width: 20%;height: auto;display: flex"><div style="width: fit-content;height:fit-content;margin: auto;align-self: center">账号等级管理:</div></div>\n' +
                '                        <div style="width: 80%;height: auto;">\n' +
                '                            <div style="width: 100%;height: fit-content;margin: auto;" >\n' +
                '                                <select id="levelSelect" style="width:90%">\n' +
                '                                    <option value="3">员工级别</option>\n' +
                '                                    <option value="2" selected>部长级别</option>\n' +
                // '                                    <option value="1">总经理级别</option>\n' +
                '                                </select>\n' +
                '                            </div>\n' +
                '                            \n' +
                '                        </div>'
            document.getElementById("setUpdateLevel").innerHTML = ' <div style="width: 20%;height: auto;display: flex"><div style="width: fit-content;height:fit-content;margin: auto;align-self: center">账号等级管理:</div></div>\n' +
                '                        <div style="width: 80%;height: auto;">\n' +
                '                            <div style="width: 100%;height: fit-content;margin: auto;" >\n' +
                '                                <select id="upLevelSelect" style="width:90%">\n' +
                '                                    <option class="updateLevelOption" value="3">员工级别</option>\n' +
                '                                    <option class="updateLevelOption" value="2">部长级别</option>\n' +
                // '                                    <option class="updateLevelOption" value="1">总经理级别</option>\n' +
                '                                </select>\n' +
                '                            </div>\n' +
                '                            \n' +
                '                        </div>'


            await allModule(3, 1, 2);


        }
        else if (Number(level) === 3) {
            document.getElementById("updateUsername").disabled = true;
            // document.getElementById("addUser").style.display = "none";
            // document.getElementById("submit").disabled = true;
            document.getElementById("qxa").style.display = "none";
            //部长级别
            //查询所有部门信息显示到部门选择框中
            axios({
                method: "post",
                url: "department/selectById",
                data: department
            }).then(function (resp) {
                let datas = resp.data;
                let formdata = "";
                for (let i = 0; i < datas.length; i++) {
                    formdata += '<option value=' + datas[i].id + '>' + datas[i].departmentName + '</option>'
                }
                document.getElementById("departmentSelect").innerHTML = formdata;

            })

           await allModule(4, 1, 3, department);


        }
        else if (Number(level) === 4) {
            //员工级别
            document.getElementById("addUser").disabled = true;
            document.getElementById("addTemporaryUser").disabled = true;
            document.getElementById("updateTemporaryUser").disabled = true;
            alert("您的账号级别不够，无法访问此区域！")
        }
        else if (Number(level)===1){
            //管理员级别
            //查询所有部门信息显示到部门选择框中
            axios({
                method: "post",
                url: "department/selectAll"
            }).then(function (resp) {
                let datas = resp.data;
                let formdata = "";
                for (let i = 0; i < datas.length; i++) {
                    formdata += '<option value=' + datas[i].id + '>' + datas[i].departmentName + '</option>'
                }
                document.getElementById("departmentSelect").innerHTML = formdata;

            })


            //设置账号等级的功能
            document.getElementById("setLevel").innerHTML = ' <div style="width: 20%;height: auto;display: flex"><div style="width: fit-content;height:fit-content;margin: auto;align-self: center">账号等级管理:</div></div>\n' +
                '                        <div style="width: 80%;height: auto;">\n' +
                '                            <div style="width: 100%;height: fit-content;margin: auto;" >\n' +
                '                                <select id="levelSelect" style="width:90%">\n' +
                '                                    <option value="4">员工级别</option>\n' +
                '                                    <option value="3" selected>部长级别</option>\n' +
                '                                    <option id="zjladd" disabled value="2">总经理级别</option>\n' +
                '                                </select>\n' +
                '                            </div>\n' +
                '                            \n' +
                '                        </div>'

            document.getElementById("setUpdateLevel").innerHTML = ' <div style="width: 20%;height: auto;display: flex"><div style="width: fit-content;height:fit-content;margin: auto;align-self: center">账号等级管理:</div></div>\n' +
                '                        <div style="width: 80%;height: auto;">\n' +
                '                            <div style="width: 100%;height: fit-content;margin: auto;" >\n' +
                '                                <select id="upLevelSelect" style="width:90%">\n' +
                '                                    <option class="updateLevelOption" value="4">员工级别</option>\n' +
                '                                    <option class="updateLevelOption" value="3">部长级别</option>\n' +
                '                                    <option id="zjlupdate" disabled class="updateLevelOption" value="2">总经理级别</option>\n' +
                '                                </select>\n' +
                '                            </div>\n' +
                '                            \n' +
                '                        </div>'


            await allModule(2, 1, 1);
        }


        //点击添加新用户打开窗口
        document.getElementById("addUser").onclick = function () {
            let booleanPromise = addUserQx();
            booleanPromise.then(async function (resp) {
                if (resp === true) {
                    wd.style.display = "block";
                    document.getElementById("addUserForm").style.display = "block";
                    document.getElementById("updateUserForm").style.display = "none";
                    document.getElementById("submitTemporary").style.display = "none";
                    document.getElementById("submit").style.display = "";
                    document.getElementById("qxa").style.display = "flex";
                    document.getElementById("setLevel").style.display = "flex";
                    document.getElementById("nameFlex").style.display = "flex";
                    document.getElementById("sexFlex").style.display = "flex";
                    document.getElementById("ageFlex").style.display = "flex";
                    document.getElementById("overTimeFlex").style.display = "none";
                    document.getElementById("updateTemporaryUserForm").style.display = "none";

                    //获取已选中的level和departId

                    let departmentSelect = document.getElementById("departmentSelect");
                    let levelSelect = document.getElementById("levelSelect");
                    let selectedIndex1 = departmentSelect.selectedIndex;

                    let depaertId = departmentSelect.options[selectedIndex1].value;

                    if (Number(level) === 2 ||Number(level)===1 ) {
                        let selectedIndex2 = levelSelect.selectedIndex;
                        let level1 = levelSelect.options[selectedIndex2].value;
                        await allModule(level1, depaertId, 2);

                        //筛选
                        departmentSelect.onchange = async function () {
                            let selectedIndex2 = departmentSelect.selectedIndex;
                            let depaertId1 = departmentSelect.options[selectedIndex2].value;
                            let selectedIndex3 = levelSelect.selectedIndex;
                            let level1 = levelSelect.options[selectedIndex3].value;
                            await allModule(level1, depaertId1, 2);
                        }
                        levelSelect.onchange = async function () {
                            let selectedIndex2 = departmentSelect.selectedIndex;
                            let depaertId1 = departmentSelect.options[selectedIndex2].value;
                            let selectedIndex3 = levelSelect.selectedIndex;
                            let level1 = levelSelect.options[selectedIndex3].value;
                            await allModule(level1, depaertId1, 2);
                        }
                    } else if (Number(level) === 3) {
                        await allModule(4, depaertId, 3);
                    }


                } else {
                    alert("您未获得添加用户的权限！")
                }
            })


        }


    })
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




//添加临时用户
document.getElementById("addTemporaryUser").onclick=function (){
    let booleanPromise = addUserQx();
    booleanPromise.then(function (resp){
        if (resp===true){
            wd.style.display="block";
            document.getElementById("addUserForm").style.display="block";
            document.getElementById("updateUserForm").style.display="none";
            document.getElementById("submit").style.display="none";
            document.getElementById("submitTemporary").style.display="";
            document.getElementById("qxa").style.display="none";
            document.getElementById("setLevel").style.display="none";
            document.getElementById("nameFlex").style.display="none";
            document.getElementById("sexFlex").style.display="none";
            document.getElementById("ageFlex").style.display="none";
            document.getElementById("overTimeFlex").style.display="flex";
            document.getElementById("updateTemporaryUserForm").style.display="none";
        }
        else {
            alert("您未获得添加用户的权限！")
        }
    })


}

//修改临时用户信息
document.getElementById("updateTemporaryUser").onclick= function (){
    let booleanPromise = updateUserQx();
    booleanPromise.then(function (resp){
        if (resp===true){
            wd.style.display="block";
            document.getElementById("addUserForm").style.display="none";
            document.getElementById("updateTemporaryUserForm").style.display="none";
            document.getElementById("updateUserForm").style.display="none";
            document.getElementById("updateTemporaryUserForm").style.display="block";
            //查询临时账户信息
            axios({
                method:"post",
                url:"temporaryUser/selectAllTemporaryUser"
            }).then(function (resp){
                let datas=resp.data;
                let formdata="";
                if (datas.length>0){
                    for (let i = 0; i < datas.length; i++) {
                        formdata+=' <div style="width: 100%;height: auto;display: flex;text-align: center;">\n' +
                            '                            <div style="width: 20%;border:white solid 1px  ;display: flex" ><div style="margin: auto;align-self: center">'+datas[i].user_name+'</div></div>\n' +
                            '                            <div style="width: 20%;border:white solid 1px  ;display: flex" ><div style="margin: auto;align-self: center">'+datas[i].createUser+'</div></div>\n' +
                            '                            <div style="width: 15%;border:white solid 1px;display: flex" >  <div style="margin: auto;align-self: center">'+datas[i].departmentName+'</div></div>\n' +
                            '                            <div style="width: 20%;border:white solid 1px;display: flex" >  <div style="margin: auto;align-self: center">'+fDate(new Date(datas[i].over_time))+'</div></div>\n' +
                            '                            <div style="width: 25%;border:white solid 1px;display: flex"  > <div style="margin: auto;align-self: center"><button class="deleteTemporary">删除账号</button> <button class="addDays">重置时间</button></div></div>\n' +
                            '                        </div>'
                    }
                    document.getElementById("temporaryContent").innerHTML=formdata;
                    //删除
                    let deletes = document.querySelectorAll(".deleteTemporary");
                    for (let i = 0; i < deletes.length; i++) {
                        deletes[i].onclick=function (){
                            if (window.confirm("您确定要删除这个临时账号吗？")){
                                axios({
                                    method:"post",
                                    url:"temporaryUser/delete",
                                    data:datas[i].id
                                }).then(function (resp){
                                    if (resp.data==="success"){
                                        alert("删除成功！")
                                        window.parent.document.getElementById("iframepage").contentWindow.location.reload();

                                    }
                                    else {
                                        alert("删除失败！请联系相关人员！")
                                    }
                                })
                                return true
                            }
                            else {
                                return false;
                            }
                        }
                    }

                    //重置时间
                    let addDays = document.querySelectorAll(".addDays");
                    for (let i = 0; i < addDays.length; i++) {
                        addDays[i].onclick=function (){
                            var days = prompt("请输入要修改的有效期天数！例：1 (从当前时间开始计算)");
                            if (days!==null){
                                if (Number.isInteger(Number(days))){
                                    if (Number(days)>=1 && Number(days)<=10){
                                        //拿重置后的时间跟现在的时间做比较
                                        var oneDay = 24 * 60 * 60 * 1000; // 每天毫秒数


                                      //获取现在的时间戳
                                        let nowDate = new Date().getTime();
                                        //获取修改后的时间戳
                                        let overTime =  new Date( days * oneDay).getTime();


                                            //更新过期时间
                                            let times = fDate1(new Date(overTime+nowDate));
                                            axios({
                                                method:"post",
                                                url:"temporaryUser/updateOverTime?id="+datas[i].id+"",
                                                data:times
                                            }).then(function (resp){
                                                if (resp.data==="success"){
                                                    alert("时间重置成功！");
                                                    window.parent.document.getElementById("iframepage").contentWindow.location.reload();
                                                }
                                                else {
                                                    alert("时间重置失败，请联系相关人员！")
                                                }
                                            })


                                    }
                                    else {
                                        alert("允许修改的范围是1~10天！")
                                    }

                                }
                                else {
                                    alert("请输入数字！")
                                }
                            }



                        }
                    }
                }

            })









        }
        else {
            alert("您暂未获得修改用户的权限！")
        }
    })


}

