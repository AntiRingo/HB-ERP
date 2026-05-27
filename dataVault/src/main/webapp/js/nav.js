
(function () {
    const scaleKey = "dataVault.largeFontScale";
    const legacyScaleKey = "dataVault.uiScale";

    function roundToStep(value, step) {
        return Math.round(value / step) * step;
    }

    function clamp(value, min, max) {
        return Math.min(max, Math.max(min, value));
    }

    function getStoredScale() {
        try {
            const raw = localStorage.getItem(scaleKey);
            const parsed = Number(raw);
            if (Number.isFinite(parsed)) return clamp(roundToStep(parsed, 0.1), 1, 2);

            const legacyRaw = localStorage.getItem(legacyScaleKey);
            const legacyParsed = Number(legacyRaw);
            if (Number.isFinite(legacyParsed)) return clamp(roundToStep(legacyParsed, 0.1), 1, 2);
        } catch (e) {}

        return 1;
    }

    function storeScale(scale) {
        try {
            localStorage.setItem(scaleKey, String(scale));
        } catch (e) {}
    }

    function applyScale(scale) {
        document.documentElement.style.zoom = String(scale);
    }

    function ensurePopupDom() {
        if (document.getElementById("fontScalePopup")) return;
        const toggleButton = document.getElementById("fontModeToggle");
        if (!toggleButton || !toggleButton.parentElement) return;

        const popup = document.createElement("div");
        popup.id = "fontScalePopup";
        popup.style.display = "none";
        popup.innerHTML =
            '<div id="fontScalePopupTitle">' +
            '<span>放大倍数</span>' +
            '<span id="fontScalePopupValue">1.0倍</span>' +
            "</div>" +
            '<input id="fontScaleSlider" type="range" min="1" max="2" step="0.1" value="1" aria-label="放大倍数">';
        toggleButton.parentElement.appendChild(popup);
    }

    function setPopupVisible(visible) {
        const popup = document.getElementById("fontScalePopup");
        if (!popup) return;
        popup.style.display = visible ? "" : "none";
    }

    function updatePopup(scale) {
        const slider = document.getElementById("fontScaleSlider");
        const valueEl = document.getElementById("fontScalePopupValue");
        if (slider) slider.value = String(scale);
        if (valueEl) valueEl.textContent = scale.toFixed(1) + "倍";
    }

    function updateButton(scale) {
        const toggleButton = document.getElementById("fontModeToggle");
        if (!toggleButton) return;
        toggleButton.textContent = "放大字体";
        toggleButton.title = "放大字体：调整放大倍数（1.0-2.0）";
    }

    ensurePopupDom();

    const initialScale = getStoredScale();
    applyScale(initialScale);
    updateButton(initialScale);
    updatePopup(initialScale);

    const toggleButton = document.getElementById("fontModeToggle");
    if (!toggleButton) return;

    toggleButton.addEventListener("click", function (e) {
        e.stopPropagation();
        const currentScale = getStoredScale();
        if (currentScale <= 1) {
            const defaultScale = 1.5;
            storeScale(defaultScale);
            applyScale(defaultScale);
            updateButton(defaultScale);
            updatePopup(defaultScale);
        } else {
            updateButton(currentScale);
            updatePopup(currentScale);
        }
        setPopupVisible(true);
    });

    const slider = document.getElementById("fontScaleSlider");
    if (slider) {
        slider.addEventListener("input", function (e) {
            e.stopPropagation();
            const raw = Number(slider.value);
            const scale = clamp(roundToStep(raw, 0.1), 1, 2);
            storeScale(scale);
            applyScale(scale);
            updateButton(scale);
            updatePopup(scale);
        });
    }

    document.addEventListener("click", function (e) {
        const popup = document.getElementById("fontScalePopup");
        const toggle = document.getElementById("fontModeToggle");
        if (!popup || !toggle) return;
        if (popup.contains(e.target) || toggle.contains(e.target)) return;
        setPopupVisible(false);
    });
})();

//查询一级分类(零件仓库)
axios({
    method:"post",
    url:"sort/selectOne"
}).then(async function (resp){
    let oneDatas=resp.data;
    for (let k = 0; k < oneDatas.length; k++) {
        //查询所有二级分类
       await axios({
            method:"post",
            url:"sort/selectOtherLevel",
            data:oneDatas[k].id
        }).then( async function (resp)
        {
            let datas=resp.data;
            let formdata ="<h4 style='padding-left: 5px'>"+oneDatas[k].name+"</h4>";
            for (let i = 0; i < datas.length; i++) {
                formdata+=' <div class="twoSort"><input value="'+datas[i].id+'" class="twoId" style="display: none"><input value="'+datas[i].name+'" class="twoName" style="display: none"><div><a href="productResult.html?id='+datas[i].id+'&level=2">'+datas[i].name+'</a></div><i class="twoSortBefore"></i></div>'
            }
            document.getElementById("two").innerHTML+=formdata;



        })

    }
    let twosort = document.querySelectorAll(".twoSort");
    let twoId = document.querySelectorAll(".twoId");
    let twoName = document.querySelectorAll(".twoName");
    for (let i = 0; i < twosort.length; i++) {
        twosort[i].onmouseenter =async function (){
            document.getElementById("three").style.display="";
            document.getElementById("twoName").innerHTML=twoName[i].value;
            //查询该分类下的三级分类
            await axios({
                method:"post",
                url:"sort/selectOtherLevel",
                data:twoId[i].value
            }).then(function (resp){
                let threeDatas=resp.data;
                let formdata="";
                for (let j = 0; j < threeDatas.length; j++) {
                    formdata+=' <div class="threeDiv"><a href="productResult.html?id='+threeDatas[j].id+'&level=3">'+threeDatas[j].name+'</a></div>'

                }

                document.getElementById("threeSort").innerHTML=formdata;
            })
        }

    }
})


//查询一级分类(产品仓库)
axios({
    method:"post",
    url:"finSort/selectOne"
}).then(async function (resp){
    let oneDatas=resp.data;
    for (let k = 0; k < oneDatas.length; k++) {
        //查询所有二级分类
        await axios({
            method:"post",
            url:"finSort/selectOtherLevel",
            data:oneDatas[k].id
        }).then( async function (resp)
        {
            let datas=resp.data;
            let formdata ="<h4 style='padding-left: 5px'>"+oneDatas[k].finSortName+"</h4>";
            for (let i = 0; i < datas.length; i++) {
                formdata+=' <div class="twoSort2"><input value="'+datas[i].id+'" class="twoId2" style="display: none"><input value="'+datas[i].finSortName+'" class="twoName2" style="display: none"><div><a href="finproductResult.html?id='+datas[i].id+'&level=2&sort=1">'+datas[i].finSortName+'</a></div><i class="twoSortBefore"></i></div>'
            }
            document.getElementById("two2").innerHTML+=formdata;



        })

    }
    let twosort = document.querySelectorAll(".twoSort2");
    let twoId = document.querySelectorAll(".twoId2");
    let twoName = document.querySelectorAll(".twoName2");
    for (let i = 0; i < twosort.length; i++) {
        twosort[i].onmouseenter =async function (){
            document.getElementById("three2").style.display="";
            document.getElementById("twoName2").innerHTML=twoName[i].value;
            //查询该分类下的三级分类
            await axios({
                method:"post",
                url:"finSort/selectOtherLevel",
                data:twoId[i].value
            }).then(function (resp){
                let threeDatas=resp.data;
                let formdata="";
                for (let j = 0; j < threeDatas.length; j++) {
                    formdata+=' <div class="threeDiv2"><a href="finproductResult.html?id='+threeDatas[j].id+'&level=3&sort=1">'+threeDatas[j].finSortName+'</a></div>'

                }

                document.getElementById("threeSort2").innerHTML=formdata;
            })
        }

    }
})




//监听窗口宽度变化
window.onresize=function (){
    if (Number(document.documentElement.clientWidth)<=900){
        document.getElementById("sw").style.display="";

    }else {
        document.getElementById("sw").style.display="flex"
    }

}

//打开页面时获取窗口宽度然后改变样式
if (Number(document.documentElement.clientWidth)<=900){
    document.getElementById("sw").style.display="";

}else {
    document.getElementById("sw").style.display="flex"
}


//点击出现所有产品
// document.getElementById("allSort").onclick=function (){
//     let display = document.getElementById("AllSortDiv").style;
//     if (display.opacity==="0"){
//         display.opacity="1";
//         display.pointerEvents = "all";
//         axios({
//             method:"post",
//             url:"sort/selectHaveProduct"
//         }).then(function (resp){
//             let datas=resp.data;
//             let formdata="";
//             for (let i = 0; i < datas.length; i++) {
//                 formdata+="<div class=\"sort\" title='双击进入分类，单击确定搜索范围'>"+datas[i].name+"</div>"
//             }
//             document.getElementById("allSortContent").innerHTML=' <div id="all">全部</div>'
//             document.getElementById("allSortContent").innerHTML+=formdata;
//             let sorts = document.querySelectorAll(".sort");
//             // 初始化变量
//             let clickTimeout = null;
//             for (let i = 0; i < sorts.length; i++) {
//                 sorts[i].onclick=function (){
//                     // 清除可能存在的延时
//                     clearTimeout(clickTimeout);
//
//                     // 设置延时执行单击事件
//                     clickTimeout = setTimeout(function() {
//                         document.getElementById("sortName").innerHTML=''+datas[i].name+'';
//                         document.getElementById("sortVault").innerHTML = ''+datas[i].vault+''
//                         document.getElementById("allSort").value=datas[i].id;
//                         document.getElementById("AllSortDiv").style.opacity="0";
//                         display.pointerEvents = "none";
//                     }, 250); // 250ms延时以检测可能的双击
//
//
//
//                 }
//
//                 sorts[i].ondblclick=function (){
//                     // 取消等待的单击事件
//                     clearTimeout(clickTimeout);
//                     const frame = window.parent.document.getElementById('iframepage');
//                     frame.src ="productResult.html?id="+datas[i].id+"&level=4"
//                 }
//             }
//
//             document.getElementById("all").onclick = function (){
//                 document.getElementById("sortName").innerHTML='全部<i class="allSortBefore"></i>';
//                 document.getElementById("allSort").value=0;
//                 document.getElementById("AllSortDiv").style.opacity="0";
//                 display.pointerEvents = "none";
//
//             }
//         })
//
//     }else {
//         display.opacity="0";
//         display.pointerEvents="none"
//         document.getElementById("allSortContent").innerHTML="";
//     }
// }


let allSort = document.getElementById("allSort");

allSort.onmouseenter=function (){
    let display = document.getElementById("AllSortDiv").style;
    if (display.opacity==="0"){
        display.opacity="1";
        display.pointerEvents = "all";
        axios({
            method:"post",
            url:"sort/selectHaveProduct"
        }).then(function (resp){
            let datas=resp.data;
            let formdata="";
            for (let i = 0; i < datas.length; i++) {
                formdata+="<div class=\"sort\" title='双击进入分类，单击确定搜索范围'>"+datas[i].name+"</div>"
            }
            document.getElementById("allSortContent").innerHTML=' <div id="all">全部</div>'
            document.getElementById("allSortContent").innerHTML+=formdata;
            let sorts = document.querySelectorAll(".sort");
            // 初始化变量
            let clickTimeout = null;
            for (let i = 0; i < sorts.length; i++) {
                sorts[i].onclick=function (){
                    // 清除可能存在的延时
                    clearTimeout(clickTimeout);

                    // 设置延时执行单击事件
                    clickTimeout = setTimeout(function() {
                        document.getElementById("sortName").innerHTML=''+datas[i].name+'';
                        document.getElementById("sortVault").innerHTML = ''+datas[i].vault+''
                        document.getElementById("allSort").value=datas[i].id;
                        document.getElementById("AllSortDiv").style.opacity="0";
                        display.pointerEvents = "none";
                    }, 250); // 250ms延时以检测可能的双击



                }

                sorts[i].ondblclick=function (){
                    // 取消等待的单击事件
                    clearTimeout(clickTimeout);
                    const frame = window.parent.document.getElementById('iframepage');
                    frame.src ="productResult.html?id="+datas[i].id+"&level=4"
                }
            }

            document.getElementById("all").onclick = function (){
                document.getElementById("sortName").innerHTML='全部<i class="allSortBefore"></i>';
                document.getElementById("allSort").value=0;
                document.getElementById("AllSortDiv").style.opacity="0";
                display.pointerEvents = "none";

            }
        })

    }
    document.getElementById("AllSortDiv").onmouseleave = function (){
        display.opacity="0";
    }

}




// let one = document.getElementById("one");
// let one2 = document.getElementById("one2");
// let two = document.getElementById("two");
// let two2 = document.getElementById("two2");
// let three = document.getElementById("three");
// let three2 = document.getElementById("three2");

document.getElementById("product").onmouseenter=function (){
    document.getElementById("two").style.display="block";
    document.getElementById("two2").style.display="none";
    document.getElementById("three2").style.display="none";

}
document.getElementById("one").onmouseleave = function (){

    document.getElementById("two").style.display="none";
    document.getElementById("three").style.display="none";
}
document.getElementById("three").onmouseleave = function (){
    document.getElementById("three").style.display="none"
}



document.getElementById("product2").onmouseenter=function (){
    document.getElementById("two2").style.display="block";
    document.getElementById("two").style.display="none";
    document.getElementById("three").style.display="none";

}
document.getElementById("one2").onmouseleave = function (){
    document.getElementById("two2").style.display="none";
    document.getElementById("three2").style.display="none";
}
document.getElementById("three2").onmouseleave = function (){
    document.getElementById("three2").style.display="none"
}
