document.getElementById("applicantTitle").innerHTML=' <th class="applicationSticky">名称</th>\n' +
    '                <th class="applicationSticky">物料号</th>\n' +
    '                <th class="applicationSticky">库存</th>\n' +
    '                <th class="applicationSticky">申请数量</th>\n'+
    '                <th class="applicationSticky">操作</th>'

function isPositiveNumber(s) {
    const trimmed = s.trim();
    // 排除前后空格、空字符串或纯空格
    if (trimmed === '' || trimmed !== s) return false;
    // 正则表达式验证格式
    const regex = /^[+]?(?:\d+\.?\d*|\.\d+)(?:[eE][+-]?\d+)?$/;
    if (!regex.test(trimmed)) return false;
    // 转换为数值并验证范围
    const num = Number(trimmed);
    return num > 0 && isFinite(num);
}


//获取BOM表名称
async function bomTitleName(id) {
    let name = "";
    await axios({
        method: "post",
        url: "finBomTitle/selectById",
        data: id
    }).then(function (resp){
        let data=resp.data;

            if (id!==0){
                name= '('+data.title+')';
            }


    })
return name;
}

// 打开弹窗
function openModal() {
    document.getElementById("customModal").style.display = "block";
}



//根据申请人id查询是否存在未签字的申请单号
async function selectWqzById (id){
    let s='';
    await axios({
        method:"post",
        url:"log/selectIfQzByUserId",
        data:id
    }).then(function (resp){
        s = resp.data;
    })

    return s;
}




function  height(){

    let iframe = window.parent.document.getElementById("iframepage");
    if (iframe){
        iframe.style.height=96+"vh";
        // let num= window.parent.innerHeight;
        // if (Number(num)>Number(60)){
        //     let s = Number(num)-Number(60)
        //     iframe.style.height=s+"px";
        // }

    }


}
height();
function xs(n1,n2){
    return n1*n2;
}

async function ifActualNumber(applicationId) {
    let b;
    await axios({
        method: "post",
        url: "applicationContent/selectIfActualNumber",
        data: applicationId
    }).then(function (resp) {
        b = resp.data;
    })
    return b;
}

//计算价格
async function howMuch(id,sum) {

    //计算该产品中的零件价格数据
    await axios({
        method: "post",
        url: "finBom/selectPriceLj",
        data: id
    }).then(function (resp) {
        sum = Number(resp.data) + Number(sum) ;
    })

    //查询是否存在产品
    await axios({
        method: "post",
        url: "finBom/selectFinFromBom",
        data: id
    }).then(async function (resp) {
        let data = resp.data;
        for (let i = 0; i < data.length; i++) {
            let number = await howMuch(data[i].productId,0);

            sum = Number(sum) + Number(number)*Number(data[i].number);
        }
    })
    return sum;

}

//更新产品价格
async function updatePrice( id){

    let price = await howMuch(id, 0);
    //更新数据库
    await axios({
        method: "post",
        url: "finProduct/updatePrice?price=" + price + "&finProductId=" + id + ""
    }).then(function (resp) {
        if (resp.data!=="success"){
            alert("系统错误，请联系相关人员！")
        }
    })
}

//加入申请单
function addForm(products){
    let checkboxs = document.querySelectorAll(".productCheckbox");
    for (let i = 0; i < checkboxs.length; i++) {
        checkboxs[i].onclick=async function () {
            if (checkboxs[i].checked === true) {
                //查询是否是采购入库
                let outBoundTypeButton = document.getElementById("outBoundType");

                let index1 = outBoundTypeButton.selectedIndex;
                let s="";
                if (outBoundTypeButton.querySelectorAll(".type").length>0){
                    if (Number(outBoundTypeButton.options[index1].value)===Number(9)){
                        s='<td class="applicationSticky"><input class="rkPrice"  value="'+products[i].brand+'"  type="number" min="0" oninput="if (value<0) value=\'\';if (String(value)===\'\') value=\'\';" style="width: 50%"></td>'

                    }
                }


                let ina = 0;
                let formdata;
                if (Number(products[i].vault) === 1) {
                    ina =1;
                    //选择整机还是零件
                    const a_modal = document.getElementById('a-modal');
                    const a_completeOption = document.getElementById('a-completeOption');
                    const a_partsOption = document.getElementById('a-partsOption');

                    // 打开弹窗

                        a_modal.style.display = 'flex';


                    // 关闭弹窗并执行操作
                    function closeModal() {
                        a_modal.style.display = 'none';
                    }

                    // 整机选项
                    a_completeOption.onclick=async function () {

                        closeModal();
                        //查询该产品的价格信息
                        await updatePrice(products[i].id);
                        formdata = '<tr class="deleteDiv" >\n' +
                            '                <td style="display: none" ><input class="applicationProductId" value="' + products[i].id + '"> <input class="applicationVault" value="' + products[i].vault + '"><input class="surplus" value="' + products[i].number + '"> <input class="priceSign" value="' + products[i].brand + '">  </td>\n' +
                            '                <td class="applicationSticky">' + products[i].name + '</td>\n' +
                            '                <td class="applicationSticky">' + products[i].materialNumber + '</td>\n' +
                            '                <td class="applicationSticky">' + products[i].number + '</td>\n' +
                            '                <td class="applicationSticky"><input class="idValueNumber" value="" type="text" style="width: 50%" onInput="value=value.replace(/^(0+)|[^\\d]+/g,\'\')"></td>\n' +
                            ''+s+''+
                            '                <td class="applicationSticky"><a class="deleteApplication" href="javascript:void(0)">删除</a><input type="checkbox" class="zkbox" title="点击展开会申请该成品BOM表中的零件"><span>展开</span></td>\n' +
                            '            </tr>'

                        //加进去之前获取输入框中的申请数量,在添加内容后然后再赋值，避免innHTML重置input中的内容
                        let value = [];
                        let numbers = document.querySelectorAll(".idValueNumber");
                        for (let j = 0; j < numbers.length; j++) {
                            value[j] = numbers[j].value;
                        }

                        document.getElementById("applicationTableBody").innerHTML += formdata;
                        let numbers1 = document.querySelectorAll(".idValueNumber");
                        for (let j = 0; j < value.length; j++) {
                            numbers1[j].value = value[j];
                        }

                        let number = document.getElementById("applicationProductNumber").innerHTML;
                        document.getElementById("applicationProductNumber").innerHTML = Number(number) + Number(1);
                        //将数组存在本地
                        deletesq();
                        saveLocation();
                        inputNumberLocation();

                    };

                    // 零件选项
                    a_partsOption.onclick=async function () {
                        closeModal();

                        // 这里可以执行零件配置的相关操作
                        //将这个BOM表中的零件信息加入到申请列表中去

                        await axios({
                            method: "post",
                            url: "finBomTitle/selectBomTitle",
                            data: products[i].id
                        }).then(async function (resp) {
                            let datas = resp.data;
                            let bomTitleId = 0;
                            if (datas.length === 1) {
                                bomTitleId = datas[0].id;


                                await axios({
                                    method: "post",
                                    url: "finBom/selectByPage",
                                    data: bomTitleId
                                }).then(function (resp) {
                                    let rows = resp.data.rows;
                                    for (let j = 0; j < rows.length; j++) {

                                            formdata += '<tr class="deleteDiv" >\n' +
                                                '                <td style="display: none" ><input class="applicationProductId" value="' + rows[j].productId + '"> <input class="applicationVault" value="' + rows[j].vault + '"><input class="surplus" value="' + rows[j].number + '"><input class="priceSign" value="' + rows[j].brand + '"> </td>\n' +
                                                '                <td class="applicationSticky">' + rows[j].name + '</td>\n' +
                                                '                <td class="applicationSticky">' + rows[j].material_number + '</td>\n' +
                                                '                <td class="applicationSticky">' + rows[j].number + '</td>\n' +
                                                '                <td class="applicationSticky"><input class="idValueNumber" value="" type="text" style="width: 50%" onInput="value=value.replace(/^(0+)|[^\\d]+/g,\'\')"></td>\n' +
                                                ''+s+''+
                                                '                <td class="applicationSticky"><a class="deleteApplication" href="javascript:void(0)">删除</a></td>\n' +
                                                '            </tr>'


                                    }


                                    //加进去之前获取输入框中的申请数量,在添加内容后然后再赋值，避免innHTML重置input中的内容
                                    let value = [];
                                    let numbers = document.querySelectorAll(".idValueNumber");
                                    for (let j = 0; j < numbers.length; j++) {
                                        value[j] = numbers[j].value;
                                    }

                                    document.getElementById("applicationTableBody").innerHTML += formdata;
                                    let numbers1 = document.querySelectorAll(".idValueNumber");
                                    for (let j = 0; j < value.length; j++) {
                                        numbers1[j].value = value[j];
                                    }

                                    let number = document.getElementById("applicationProductNumber").innerHTML;
                                    document.getElementById("applicationProductNumber").innerHTML = Number(number) + Number(rows.length);
                                    //将数组存在本地
                                    deletesq();
                                    saveLocation();
                                    inputNumberLocation();

                                })
                                document.getElementById("customModal").style.display = "none";

                            }
                            else if (datas.length > 1) {
                                //存在多张BOM表
                                //查询多张BOM表的信息，并提供给用户选择
                                document.getElementById("mySelect").innerHTML = "";
                                let formData = "";
                                for (let j = 0; j < datas.length; j++) {
                                    formData += ' <option value=' + datas[j].id + '>' + datas[j].title + '</option>'

                                }
                                document.getElementById("mySelect").innerHTML = formData;
                                openModal();
                                // 确认选择
                                document.getElementById("selectEnter").onclick = async function () {
                                    const select = document.getElementById("mySelect");
                                    bomTitleId = select.value;
                                    formdata="";
                                    await axios({
                                        method: "post",
                                        url: "finBom/selectByPage",
                                        data: bomTitleId
                                    }).then(function (resp) {
                                        let rows = resp.data.rows;
                                        for (let j = 0; j < rows.length; j++) {
                                            formdata += '<tr class="deleteDiv" >\n' +
                                                '                <td style="display: none" ><input class="applicationProductId" value="' + rows[j].productId + '"> <input class="applicationVault" value="' + rows[j].vault + '"><input class="surplus" value="' + rows[j].number + '"> <input class="priceSign" value="' + rows[j].brand + '"> </td>\n' +
                                                '                <td class="applicationSticky">' + rows[j].name + '</td>\n' +
                                                '                <td class="applicationSticky">' + rows[j].material_number + '</td>\n' +
                                                '                <td class="applicationSticky">' + rows[j].number + '</td>\n' +
                                                '                <td class="applicationSticky"><input class="idValueNumber" value="" type="text" style="width: 50%" onInput="value=value.replace(/^(0+)|[^\\d]+/g,\'\')"></td>\n' +
                                                ''+s+''+
                                                '                <td class="applicationSticky"><a class="deleteApplication" href="javascript:void(0)">删除</a></td>\n' +
                                                '            </tr>'
                                        }



                                        //加进去之前获取输入框中的申请数量,在添加内容后然后再赋值，避免innHTML重置input中的内容
                                        let value = [];
                                        let numbers = document.querySelectorAll(".idValueNumber");
                                        for (let j = 0; j < numbers.length; j++) {
                                            value[j] = numbers[j].value;
                                        }

                                        document.getElementById("applicationTableBody").innerHTML += formdata;
                                        let numbers1 = document.querySelectorAll(".idValueNumber");
                                        for (let j = 0; j < value.length; j++) {
                                            numbers1[j].value = value[j];
                                        }

                                        let number = document.getElementById("applicationProductNumber").innerHTML;
                                        document.getElementById("applicationProductNumber").innerHTML = Number(number) + Number(rows.length);
                                        //将数组存在本地
                                        deletesq();
                                        saveLocation();
                                        inputNumberLocation();

                                    })
                                    document.getElementById("customModal").style.display = "none";

                                }
                                //取消选择
                                document.getElementById("selectCancel").onclick = function () {
                                    document.getElementById("customModal").style.display = "none";
                                    checkboxs[i].checked = false;
                                }

                            }
                            checkboxs[i].checked=false;

                        })
                    };

                    // 点击弹窗外部关闭
                    a_modal.addEventListener('click', (e) => {
                        if (e.target === a_modal) {
                            a_modal.style.display = 'none';
                            checkboxs[i].checked=false;
                        }
                    });


                }
                else if (Number(products[i].vault) === 0) {

                    formdata = '<tr class="deleteDiv" >\n' +
                        '                <td style="display: none" ><input class="applicationProductId" value="' + products[i].id + '"> <input class="applicationVault" value="' + products[i].vault + '"><input class="surplus" value="' + products[i].number + '"> <input class="priceSign" value="' + products[i].brand + '"></td>\n' +
                        '                <td class="applicationSticky">' + products[i].name + '</td>\n' +
                        '                <td class="applicationSticky">' + products[i].materialNumber + '</td>\n' +
                        '                <td class="applicationSticky">' + products[i].number + '</td>\n' +
                        '                <td class="applicationSticky"><input class="idValueNumber" value="" type="text" style="width: 50%" pattern="^(0|[1-9]\\d*)(\\.\\d{2})?$" oninput="this.value = this.value.replace(/[^0-9.]/g, \'\').replace(/(\\..*)\\./g, \'$1\').replace(/^0+(?=\\d)/, \'\').replace(/^\\./, \'0.\').replace(/(\\.\\d{2}).*/, \'$1\').replace(/^(0|[1-9]\\d*)(\\.\\d{0,2})?.*/, \'$1$2\')" title="请输入正数，最多两位小数（如123.45）"></td>\n' +
                        ''+s+''+
                        '                <td class="applicationSticky"><a class="deleteApplication" href="javascript:void(0)">删除</a></td>\n' +
                        '            </tr>'
                }

        if (Number(ina)===Number(0)){
             //加进去之前获取输入框中的申请数量,在添加内容后然后再赋值，避免innHTML重置input中的内容
             let value = [];
             let numbers = document.querySelectorAll(".idValueNumber");
             for (let j = 0; j < numbers.length; j++) {
                 value[j] = numbers[j].value;
             }

             document.getElementById("applicationTableBody").innerHTML += formdata;
             let numbers1 = document.querySelectorAll(".idValueNumber");
             for (let j = 0; j < value.length; j++) {
                 numbers1[j].value = value[j];
             }

             let number = document.getElementById("applicationProductNumber").innerHTML;
             document.getElementById("applicationProductNumber").innerHTML = Number(number) + Number(1);
             console.log("cccc")
             //将数组存在本地
             deletesq();
             saveLocation();
             inputNumberLocation();
        }



            }
            else {

                let idValue = document.querySelectorAll(".idValueNumber");
                for (let j = 0; j < idValue.length; j++) {
                    let parentElement = idValue[j].parentElement.parentElement;
                    //获取申请单内的物料id和物料仓库
                        let id = parentElement.querySelector(".applicationProductId");
                        let vault = parentElement.querySelector(".applicationVault");
                        if (Number(vault.value)===Number(0)){
                            //电子仓库
                            if (Number(id.value) === Number(products[i].id) && Number(vault.value) ===Number(products[i].vault)){
                                parentElement.remove();
                                //数量减一
                                let innerHTML = document.getElementById("applicationProductNumber").innerHTML;
                                document.getElementById("applicationProductNumber").innerHTML = Number(innerHTML) - Number(1);
                                deletesq();
                                saveLocation();
                                inputNumberLocation();
                            }
                        }
                        else {
                            //成品仓库，判断有没有展开，如果没有展开那就删除，如果有展开那就一起删除
                            let zkboxs = parentElement.querySelector(".zkbox");
                            if (zkboxs.checked===true){
                                if ((Number(id.value) === Number(products[i].id)) && (Number(vault.value)===Number(products[i].vault))){
                                    //展开
                                    parentElement.remove();
                                    //获取展开的部分
                                    document.getElementById("zk"+id.value+"").remove();
                                    //数量减一
                                    let innerHTML = document.getElementById("applicationProductNumber").innerHTML;
                                    document.getElementById("applicationProductNumber").innerHTML = Number(innerHTML) - Number(1);
                                    deletesq();
                                    saveLocation();
                                    inputNumberLocation();
                                }

                            }
                            else {
                                //关闭
                                if ((Number(id.value) === Number(products[i].id)) && (Number(vault.value)===Number(products[i].vault))){
                                    parentElement.remove();
                                    //数量减一
                                    let innerHTML = document.getElementById("applicationProductNumber").innerHTML;
                                    document.getElementById("applicationProductNumber").innerHTML = Number(innerHTML) - Number(1);
                                    deletesq();
                                    saveLocation();
                                    inputNumberLocation();
                                }
                            }

                        }
                }




                let divs = document.querySelectorAll(".deleteDiv");
                for (let j = 0; j < divs.length; j++) {
                    let element = divs[j].querySelector(".applicationProductId");
                    let value = element.value;
                    let vault = divs[j].querySelector(".applicationVault");
                    if (Number(value) === Number(products[i].id) && Number(vault.value) ===Number(products[i].vault)) {
                        divs[j].remove();


                    }

                }
            }

        }

    }
}




//从申请单中加入申请单
function formAddForm(products){
    checkBoxDisplay();
    let checkboxs = document.querySelectorAll(".insertForm");
    for (let i = 0; i < checkboxs.length; i++) {

        checkboxs[i].onclick=async function () {

            //查询是否已经添加到申请单中了

            if (checkboxs[i].checked === true) {

                //查询是否是采购入库
                let outBoundTypeButton = document.getElementById("outBoundType");

                let index1 = outBoundTypeButton.selectedIndex;
                let s="";
                if (outBoundTypeButton.querySelectorAll(".type").length>0){
                    if (Number(outBoundTypeButton.options[index1].value)===Number(9)){
                        s='<td class="applicationSticky"><input class="rkPrice" value="'+products[i].brand+'"  type="number" min="0" oninput="if (value<0) value=\'\';if (String(value)===\'\') value=\'\';" style="width: 50%"></td>'

                    }
                }


                let ina = 0;
                let formdata;
                if (Number(products[i].vault) === 1) {
                    ina =1;
                    //选择整机还是零件
                    const a_modal = document.getElementById('a-modal');
                    const a_completeOption = document.getElementById('a-completeOption');
                    const a_partsOption = document.getElementById('a-partsOption');

                    // 打开弹窗

                    a_modal.style.display = 'flex';


                    // 关闭弹窗并执行操作
                    function closeModal() {
                        a_modal.style.display = 'none';
                    }

                    // 整机选项
                    a_completeOption.onclick=async function () {

                        closeModal();
                        //查询该产品的价格信息
                        await updatePrice(products[i].fin_product_id);
                        formdata = '<tr class="deleteDiv" >\n' +
                            '                <td style="display: none" ><input class="applicationProductId" value="' + products[i].product_id + '"> <input class="applicationVault" value="' + products[i].vault + '"><input class="surplus" value="' + products[i].number + '"><input class="priceSign" value="' + products[i].brand + '"> </td>\n' +
                            '                <td class="applicationSticky">' + products[i].name + '</td>\n' +
                            '                <td class="applicationSticky">' + products[i].material_umber + '</td>\n' +
                            '                <td class="applicationSticky">' + products[i].number + '</td>\n' +
                            '                <td class="applicationSticky"><input class="idValueNumber" value="" type="text" style="width: 50%" onInput="value=value.replace(/^(0+)|[^\\d]+/g,\'\')"></td>\n' +
                           ''+s+''+
                            '                <td class="applicationSticky"><a class="deleteApplication" href="javascript:void(0)">删除</a><input type="checkbox" class="zkbox" title="点击展开会申请该成品BOM表中的零件"><span>展开</span></td>\n' +
                            '            </tr>'

                        //加进去之前获取输入框中的申请数量,在添加内容后然后再赋值，避免innHTML重置input中的内容
                        let value = [];
                        let numbers = document.querySelectorAll(".idValueNumber");
                        for (let j = 0; j < numbers.length; j++) {
                            value[j] = numbers[j].value;
                        }

                        document.getElementById("applicationTableBody").innerHTML += formdata;
                        let numbers1 = document.querySelectorAll(".idValueNumber");
                        for (let j = 0; j < value.length; j++) {
                            numbers1[j].value = value[j];
                        }

                        let number = document.getElementById("applicationProductNumber").innerHTML;
                        document.getElementById("applicationProductNumber").innerHTML = Number(number) + Number(1);
                        //将数组存在本地
                        deletesq();
                        saveLocation();
                        inputNumberLocation();

                    };

                    // 零件选项
                    a_partsOption.onclick=async function () {
                        closeModal();

                        // 这里可以执行零件配置的相关操作
                        //将这个BOM表中的零件信息加入到申请列表中去

                        await axios({
                            method: "post",
                            url: "finBomTitle/selectBomTitle",
                            data: products[i].product_id
                        }).then(async function (resp) {
                            let datas = resp.data;
                            let bomTitleId = 0;
                            if (datas.length === 1) {
                                bomTitleId = datas[0].id;


                                await axios({
                                    method: "post",
                                    url: "finBom/selectByPage",
                                    data: bomTitleId
                                }).then(function (resp) {
                                    let rows = resp.data.rows;
                                    for (let j = 0; j < rows.length; j++) {

                                        formdata += '<tr class="deleteDiv" >\n' +
                                            '                <td style="display: none" ><input class="applicationProductId" value="' + rows[j].productId + '"> <input class="applicationVault" value="' + rows[j].vault + '"><input class="surplus" value="' + rows[j].number + '"> <input class="priceSign" value="' + rows[j].brand + '"></td>\n' +
                                            '                <td class="applicationSticky">' + rows[j].name + '</td>\n' +
                                            '                <td class="applicationSticky">' + rows[j].material_number + '</td>\n' +
                                            '                <td class="applicationSticky">' + rows[j].number + '</td>\n' +
                                            '                <td class="applicationSticky"><input class="idValueNumber" value="" type="text" style="width: 50%" onInput="value=value.replace(/^(0+)|[^\\d]+/g,\'\')"></td>\n' +
                                            ''+s+''+
                                            '                <td class="applicationSticky"><a class="deleteApplication" href="javascript:void(0)">删除</a></td>\n' +
                                            '            </tr>'


                                    }


                                    //加进去之前获取输入框中的申请数量,在添加内容后然后再赋值，避免innHTML重置input中的内容
                                    let value = [];
                                    let numbers = document.querySelectorAll(".idValueNumber");
                                    for (let j = 0; j < numbers.length; j++) {
                                        value[j] = numbers[j].value;
                                    }

                                    document.getElementById("applicationTableBody").innerHTML += formdata;
                                    let numbers1 = document.querySelectorAll(".idValueNumber");
                                    for (let j = 0; j < value.length; j++) {
                                        numbers1[j].value = value[j];
                                    }

                                    let number = document.getElementById("applicationProductNumber").innerHTML;
                                    document.getElementById("applicationProductNumber").innerHTML = Number(number) + Number(rows.length);
                                    //将数组存在本地
                                    deletesq();
                                    saveLocation();
                                    inputNumberLocation();

                                })
                                document.getElementById("customModal").style.display = "none";

                            }
                            else if (datas.length > 1) {
                                //存在多张BOM表
                                //查询多张BOM表的信息，并提供给用户选择
                                document.getElementById("mySelect").innerHTML = "";
                                let formData = "";
                                for (let j = 0; j < datas.length; j++) {
                                    formData += ' <option value=' + datas[j].id + '>' + datas[j].title + '</option>'

                                }
                                document.getElementById("mySelect").innerHTML = formData;
                                openModal();
                                // 确认选择
                                document.getElementById("selectEnter").onclick = async function () {
                                    const select = document.getElementById("mySelect");
                                    bomTitleId = select.value;
                                    formdata="";
                                    await axios({
                                        method: "post",
                                        url: "finBom/selectByPage",
                                        data: bomTitleId
                                    }).then(function (resp) {
                                        let rows = resp.data.rows;
                                        for (let j = 0; j < rows.length; j++) {
                                            formdata += '<tr class="deleteDiv" >\n' +
                                                '                <td style="display: none" ><input class="applicationProductId" value="' + rows[j].productId + '"> <input class="applicationVault" value="' + rows[j].vault + '"><input class="surplus" value="' + rows[j].number + '"><input class="priceSign" value="' + rows[j].brand + '"> </td>\n' +
                                                '                <td class="applicationSticky">' + rows[j].name + '</td>\n' +
                                                '                <td class="applicationSticky">' + rows[j].material_number + '</td>\n' +
                                                '                <td class="applicationSticky">' + rows[j].number + '</td>\n' +
                                                '                <td class="applicationSticky"><input class="idValueNumber" value="" type="text" style="width: 50%" onInput="value=value.replace(/^(0+)|[^\\d]+/g,\'\')"></td>\n' +
                                                ''+s+''+
                                                '                <td class="applicationSticky"><a class="deleteApplication" href="javascript:void(0)">删除</a></td>\n' +
                                                '            </tr>'
                                        }



                                        //加进去之前获取输入框中的申请数量,在添加内容后然后再赋值，避免innHTML重置input中的内容
                                        let value = [];
                                        let numbers = document.querySelectorAll(".idValueNumber");
                                        for (let j = 0; j < numbers.length; j++) {
                                            value[j] = numbers[j].value;
                                        }

                                        document.getElementById("applicationTableBody").innerHTML += formdata;
                                        let numbers1 = document.querySelectorAll(".idValueNumber");
                                        for (let j = 0; j < value.length; j++) {
                                            numbers1[j].value = value[j];
                                        }

                                        let number = document.getElementById("applicationProductNumber").innerHTML;
                                        document.getElementById("applicationProductNumber").innerHTML = Number(number) + Number(rows.length);
                                        //将数组存在本地
                                        deletesq();
                                        saveLocation();
                                        inputNumberLocation();

                                    })
                                    document.getElementById("customModal").style.display = "none";

                                }
                                //取消选择
                                document.getElementById("selectCancel").onclick = function () {
                                    document.getElementById("customModal").style.display = "none";
                                    checkboxs[i].checked = false;
                                }

                            }
                            checkboxs[i].checked=false;

                        })
                    };

                    // 点击弹窗外部关闭
                    a_modal.addEventListener('click', (e) => {
                        if (e.target === a_modal) {
                            a_modal.style.display = 'none';
                            checkboxs[i].checked=false;
                        }
                    });


                }
                else if (Number(products[i].vault) === 0) {

                    formdata = '<tr class="deleteDiv" >\n' +
                        '                <td style="display: none" ><input class="applicationProductId" value="' + products[i].product_id + '"> <input class="applicationVault" value="' + products[i].vault + '"><input class="surplus" value="' + products[i].number + '"> <input class="priceSign" value="' + products[i].brand + '"></td>\n' +
                        '                <td class="applicationSticky">' + products[i].name + '</td>\n' +
                        '                <td class="applicationSticky">' + products[i].material_number + '</td>\n' +
                        '                <td class="applicationSticky">' + products[i].number + '</td>\n' +
                        '                <td class="applicationSticky"><input class="idValueNumber" value="" type="text" style="width: 50%" pattern="^(0|[1-9]\\d*)(\\.\\d{2})?$" oninput="this.value = this.value.replace(/[^0-9.]/g, \'\').replace(/(\\..*)\\./g, \'$1\').replace(/^0+(?=\\d)/, \'\').replace(/^\\./, \'0.\').replace(/(\\.\\d{2}).*/, \'$1\').replace(/^(0|[1-9]\\d*)(\\.\\d{0,2})?.*/, \'$1$2\')" title="请输入正数，最多两位小数（如123.45）"></td>\n' +
                        ''+s+''+
                        '                <td class="applicationSticky"><a class="deleteApplication" href="javascript:void(0)">删除</a></td>\n' +
                        '            </tr>'
                }

                if (Number(ina)===Number(0)){
                    //加进去之前获取输入框中的申请数量,在添加内容后然后再赋值，避免innHTML重置input中的内容
                    let value = [];
                    let numbers = document.querySelectorAll(".idValueNumber");
                    for (let j = 0; j < numbers.length; j++) {
                        value[j] = numbers[j].value;
                    }

                    document.getElementById("applicationTableBody").innerHTML += formdata;
                    let numbers1 = document.querySelectorAll(".idValueNumber");
                    for (let j = 0; j < value.length; j++) {
                        numbers1[j].value = value[j];
                    }

                    let number = document.getElementById("applicationProductNumber").innerHTML;
                    document.getElementById("applicationProductNumber").innerHTML = Number(number) + Number(1);
                    //将数组存在本地
                    deletesq();
                    saveLocation();
                    inputNumberLocation();
                }



            }
            else {
                let idValue = document.querySelectorAll(".idValueNumber");
                for (let j = 0; j < idValue.length; j++) {
                    let parentElement = idValue[j].parentElement.parentElement;
                    let id = parentElement.querySelector(".applicationProductId");
                    let vault = parentElement.querySelector(".applicationVault");
                    if (Number(vault.value)===Number(0)){
                        //电子仓库
                        if (Number(id.value) === Number(products[i].product_id)){
                            parentElement.remove();
                            //数量减一
                            let innerHTML = document.getElementById("applicationProductNumber").innerHTML;
                            document.getElementById("applicationProductNumber").innerHTML = Number(innerHTML) - Number(1);
                            deletesq();
                            saveLocation();
                            inputNumberLocation();
                        }
                    }
                    else {
                        //成品仓库，判断有没有展开，如果没有展开那就删除，如果有展开那就一起删除
                        let zkboxs = parentElement.querySelector(".zkbox");
                        if (zkboxs.checked===true){
                            if ((Number(id.value) === Number(products[i].product_id)) && (Number(vault.value)===Number(products[i].vault))){
                                //展开
                                parentElement.remove();
                                //获取展开的部分
                                document.getElementById("zk"+id.value+"").remove();
                                //数量减一
                                let innerHTML = document.getElementById("applicationProductNumber").innerHTML;
                                document.getElementById("applicationProductNumber").innerHTML = Number(innerHTML) - Number(1);
                                deletesq();
                                saveLocation();
                                inputNumberLocation();
                            }

                        }
                        else {
                            //关闭
                            if ((Number(id.value) === Number(products[i].product_id)) && (Number(vault.value)===Number(products[i].vault))){
                                parentElement.remove();
                                //数量减一
                                let innerHTML = document.getElementById("applicationProductNumber").innerHTML;
                                document.getElementById("applicationProductNumber").innerHTML = Number(innerHTML) - Number(1);
                                deletesq();
                                saveLocation();
                                inputNumberLocation();
                            }
                        }

                    }
                }




                let divs = document.querySelectorAll(".deleteDiv");
                for (let j = 0; j < divs.length; j++) {
                    let element = divs[j].querySelector(".applicationProductId");
                    let value = element.value;
                    if (Number(value) === Number(products[i].product_id)) {
                        divs[j].remove();


                    }

                }
            }

        }

    }
}

//创建一个保存函数
var save = function (arr) {

    localStorage.my=JSON.stringify(arr)
}


function readInformation(data,dj){
    console.log(data)
    let outBoundTypeButton = document.getElementById("outBoundType");

        let index1 = outBoundTypeButton.selectedIndex;


       // if ((index1>=0&&Number(outBoundTypeButton.options[index1].value)!==Number(9)) ||index1 === -1){
           //不是采购入库
           //根据数据查询
           axios({
               method:"post",
               url:"product/selectProductByLocation",
               data:data
           }).then(function (resp)
           {
               let products=resp.data;
               let formdata="";

               for (let i = 0; i < products.length; i++) {
                   if (products[i]!==null){

                       let s="";

                       if (data[i].price !==0){

                           s='<td class="applicationSticky"><input class="rkPrice" value="' + data[i].price + '"  type="number" min="0" oninput="if (value<0) value=\'\';if (String(value)===\'\') value=\'\';" style="width: 50%"></td>'
                       }
                       if (Number(products[i].vault)===1){
                           formdata+='<tr class="deleteDiv" >\n' +
                               '                <td style="display: none" ><input class="applicationProductId" value="'+products[i].id+'"> <input class="applicationVault" value="'+products[i].vault+'"><input class="surplus" value="'+products[i].number+'"><input class="priceSign" value="' + products[i].brand + '"> </td>\n' +
                               '                <td class="applicationSticky">'+products[i].name+'<span class="bomTitle"></span></td>\n' +
                               '                <td class="applicationSticky">'+products[i].materialNumber+'</td>\n' +
                               '                <td class="applicationSticky">'+products[i].number+'(单位:'+products[i].unit+')</td>\n' +
                               '                <td class="applicationSticky"><input class="idValueNumber" value="" type="text" style="width: 50%" onInput="value=value.replace(/^(0+)|[^\\d]+/g,\'\')"></td>\n' +
                               ''+s+''+
                               '                <td class="applicationSticky"><a class="deleteApplication" href="javascript:void(0)">删除</a><input type="checkbox" class="zkbox" title="点击展开会申请该成品BOM表中的零件"><span>展开</span></td>\n' +

                               '            </tr>'
                       }
                       else if (Number(products[i].vault)===0){
                           formdata+='<tr class="deleteDiv" >\n' +
                               '                <td style="display: none" ><input class="bomTitleId" value=""><input class="applicationProductId" value="'+products[i].id+'"> <input class="applicationVault" value="'+products[i].vault+'"><input class="surplus" value="'+products[i].number+'"> <input class="priceSign" value="' + products[i].brand + '"></td>\n' +
                               '                <td class="applicationSticky">'+products[i].name+'</td>\n' +
                               '                <td class="applicationSticky">'+products[i].materialNumber+'</td>\n' +
                               '                <td class="applicationSticky">'+products[i].number+'(单位:'+products[i].unit+')</td>\n' +
                               '                <td class="applicationSticky"><input class="idValueNumber" value="" type="text" style="width: 50%" pattern="^(0|[1-9]\\d*)(\\.\\d{2})?$" oninput="this.value = this.value.replace(/[^0-9.]/g, \'\').replace(/(\\..*)\\./g, \'$1\').replace(/^0+(?=\\d)/, \'\').replace(/^\\./, \'0.\').replace(/(\\.\\d{2}).*/, \'$1\').replace(/^(0|[1-9]\\d*)(\\.\\d{0,2})?.*/, \'$1$2\')" title="请输入正数，最多两位小数（如123.45）"></td>\n' +
                               ''+s+''+
                               '                <td class="applicationSticky"><a class="deleteApplication" href="javascript:void(0)">删除</a></td>\n' +
                               '            </tr>'
                       }

                   }

               }


               if (Number(dj)===Number(1)){
                   document.getElementById("applicationTableBody").innerHTML+=formdata;
               }
               else {
                   document.getElementById("applicationTableBody").innerHTML=formdata;
               }

               document.getElementById("applicationProductNumber").innerHTML=products.length;
               //填写申请的数量
               let numbers = document.querySelectorAll(".idValueNumber");
               for (let i = 0; i < numbers.length; i++) {
                   numbers[i].value=data[i].number;
               }

               deletesq();
               inputNumberLocation();

               //展开按钮
               let zkboxs = document.querySelectorAll(".zkbox");
               for (let i = 0; i < zkboxs.length; i++) {
                   zkboxs[i].onclick=async function () {

                       //查询数量是否已经填写
                       let a=false;

                       //查询需要展开的BOM表中的信息
                       let parentElement = zkboxs[i].parentElement.parentElement;
                       let idvalue = parentElement.querySelector(".idValueNumber");
                       if (idvalue){
                           let value = idvalue.value;
                           if (value.length>0){
                               a=true;
                           }
                       }
                       if (a===false){
                           alert("请先填写申请数量!");
                           zkboxs[i].checked=false;
                       }
                       else {
                           //获取需要展开的产品的ID
                           let id = parentElement.querySelector(".applicationProductId").value;
                           if (zkboxs[i].checked === true) {
                               async function oneZk() {
                                   //展开
                                   // idvalue.disabled=true;
                                   parentElement.className = 'zked';
                                   //获取申请数量
                                   let value = idvalue.value;
                                   //获取titleId
                                   let id1=0;
                                   async function zkImportant(id, id1) {
                                       //查询该产品的BOM表信息
                                       await axios({
                                           method: "post",
                                           url: "finBom/selectByPage",
                                           data: id1
                                       }).then(function (resp) {
                                           let rows = resp.data.rows;
                                           if (rows.length > 0) {
                                               let a = '<tr id=zk' + id + ' ><td colspan="5"><table id=zktable' + id + ' border="0" cellspacing="1" bgcolor="#333333" width="100%" >' +
                                                   '</table></td></tr>'
                                               parentElement.insertAdjacentHTML("afterend", a);
                                               let inTable = document.getElementById("zktable" + id + "");
                                               let s = ''
                                               for (let i = 0; i < rows.length; i++) {
                                                   if (Number(rows[i].vault) === 1) {
                                                       s += '<tr class="deleteDiv" >\n' +
                                                           '<td class=zk' + id + ' style="display: none"><input class="bomTitleId" value="' + id1 + '"><input class="inBomId" value="' + rows[i].id + '"><input class="finProductId" value="' + id + '"><input class="lastLevel" value="0"><input class="finProductNumber" value="' + value + '"></td>\n' +
                                                           '                <td style="display: none" ><input class="yssl" value="' + rows[i].number + '"><input class="applicationProductId" value="' + rows[i].productId + '"> <input class="applicationVault" value="' + rows[i].vault + '"><input class="surplus" value="' + rows[i].number + '"><input class="priceSign" value="' +rows[i].brand + '"> </td>\n' +
                                                           '                <td class="applicationSticky">' + rows[i].name + '<span class="bomTitle"></span></td>\n' +
                                                           '                <td class="applicationSticky">' + rows[i].material_number + '</td>\n' +
                                                           '                <td class="zksl" id=sl' + rows[i].id + '>' + xs(Number(rows[i].number), Number(value)) + '</td>\n' +
                                                           '                <td class="applicationSticky"><input type="checkbox" class="inzkbox" title="点击展开会申请该成品BOM表中的零件"><span>展开</span></td>\n' +
                                                           '            </tr>'
                                                   } else {
                                                       s += '<tr class="deleteDiv" >\n' +
                                                           '<td class=zk' + id + ' style="display: none"><input class="bomTitleId" value="' + id1 + '"><input class="inBomId" value="' + rows[i].id + '"><input class="finProductId" value="' + id + '"><input class="lastLevel" value="0"><input class="finProductNumber" value="' + value + '"></td>\n' +
                                                           '                <td style="display: none" ><input class="yssl" value="' + rows[i].number + '"><input class="applicationProductId" value="' + rows[i].productId + '"> <input class="applicationVault" value="' + rows[i].vault + '"><input class="surplus" value="' + rows[i].number + '"> <input class="priceSign" value="' + rows[i].brand + '"></td>\n' +
                                                           '                <td class="applicationSticky">' + rows[i].name + '</td>\n' +
                                                           '                <td class="applicationSticky">' + rows[i].material_number + '</td>\n' +
                                                           '                <td class="zksl" id=sl' + rows[i].id + '>' + xs(Number(rows[i].number), Number(value)) + '</td>\n' +
                                                           '                <td class="applicationSticky">备注：'+rows[i].notes+'</td>\n' +
                                                           '            </tr>'
                                                   }

                                               }
                                               inTable.insertAdjacentHTML("afterbegin", s);

                                               idvalue.onchange = function () {
                                                   let value1 = idvalue.value;
                                                   if (value1.length === 0 ) {
                                                       alert("请注意您填写的申请数量是否符合要求！")
                                                       //查询是否是展开状态
                                                       let element = idvalue.parentElement.parentElement.querySelector(".zkbox");
                                                       if (element.checked === true) {
                                                           //选中，展开
                                                           idvalue.value = value;
                                                       } else {
                                                           //未选中，关闭

                                                       }

                                                   } else {

                                                       document.getElementById("zk" + id + "").remove();
                                                       oneZk();
                                                   }


                                               }


                                               //获取展开后的BOM表里面是否存在可展开的申请单,（注意，这里的类名和一开始的类名不一样）
                                               function zkFunction() {
                                                   let inzkbox = document.querySelectorAll(".inzkbox");
                                                   for (let j = 0; j < inzkbox.length; j++) {
                                                       inzkbox[j].onclick = async function () {
                                                           //查询需要展开的BOM表中的信息
                                                           let parentElement = inzkbox[j].parentElement.parentElement;
                                                           //获取需要展开的产品的ID
                                                           let id = parentElement.querySelector(".applicationProductId").value;
                                                           //获取需要展开的产品属于哪个产品
                                                           let belongId = parentElement.querySelector(".finProductId").value;
                                                           //获取在BOM表中的ID
                                                           let inBomId = parentElement.querySelector(".inBomId").value;
                                                           //在展开的BOM表中展开存在多张BOM表
                                                           async function inZkImportant(id, id1,value) {
                                                               //查询该产品的BOM表信息
                                                               await axios({
                                                                   method: "post",
                                                                   url: "finBom/selectByPage",
                                                                   data: id1
                                                               }).then(function (resp) {
                                                                   let rows = resp.data.rows;
                                                                   if (rows.length > 0) {
                                                                       let a = '<tr id=zk' + inBomId + ' ><td colspan="5"><table id=zktable' + inBomId + ' border="0" cellspacing="1" bgcolor="#333333" width="100%" >' +
                                                                           '</table></td></tr>'
                                                                       parentElement.insertAdjacentHTML("afterend", a);
                                                                       let inTable = document.getElementById("zktable" + inBomId + "");
                                                                       let s = ''
                                                                       for (let k = 0; k < rows.length; k++) {
                                                                           if (Number(rows[k].vault) === 1) {
                                                                               s += '<tr class="deleteDiv" >\n' +
                                                                                   '<td class=zk' + inBomId + ' style="display: none"><input class="bomTitleId" value="' + id1 + '"><input class="inBomId" value="' + rows[k].id + '"><input class="finProductId" value="' + id + '"><input class="lastLevel" value="' + belongId + '"><input class="finProductNumber" value="' + value + '"></td>\n' +
                                                                                   '                <td style="display: none" ><input class="yssl" value="' + rows[k].number + '"><input class="applicationProductId" value="' + rows[k].productId + '"> <input class="applicationVault" value="' + rows[k].vault + '"><input class="surplus" value="' + rows[k].number + '"><input class="priceSign" value="' + rows[k].brand + '"> </td>\n' +
                                                                                   '                <td class="applicationSticky">' + rows[k].name + '</td>\n' +
                                                                                   '                <td class="applicationSticky">' + rows[k].material_number + '</td>\n' +
                                                                                   '                <td class="zksl" id=sl' + rows[k].id + '>' + xs(Number(rows[k].number), Number(value)) + '</td>\n' +
                                                                                   '                <td class="applicationSticky"><input type="checkbox" class="inzkbox" title="点击展开会申请该成品BOM表中的零件"><span>展开</span></td>\n' +
                                                                                   '            </tr>'
                                                                           } else {
                                                                               s += '<tr class="deleteDiv" >\n' +
                                                                                   '<td class=zk' + inBomId + ' style="display: none"><input class="bomTitleId" value="' + id1 + '"><input class="inBomId" value="' + rows[k].id + '"><input class="finProductId" value="' + id + '"><input class="lastLevel" value="' + belongId + '"><input class="finProductNumber" value="' + value + '"></td>\n' +
                                                                                   '                <td style="display: none" ><input class="yssl" value="' + rows[k].number + '"><input class="applicationProductId" value="' + rows[k].productId + '"> <input class="applicationVault" value="' + rows[k].vault + '"><input class="surplus" value="' + rows[k].number + '"><input class="priceSign" value="' + rows[k].brand + '"> </td>\n' +
                                                                                   '                <td class="applicationSticky">' + rows[k].name + '</td>\n' +
                                                                                   '                <td class="applicationSticky">' + rows[k].material_number + '</td>\n' +
                                                                                   '                <td class="zksl" id=sl' + rows[k].id + '>' + xs(Number(rows[k].number), Number(value)) + '</td>\n' +
                                                                                   '                <td class="applicationSticky">备注：'+rows[k].notes+'</td>\n' +
                                                                                   '            </tr>'
                                                                           }
                                                                       }

                                                                       //添加产品信息
                                                                       inTable.insertAdjacentHTML("afterbegin", s);
                                                                       zkFunction();
                                                                   }
                                                               })
                                                           }
                                                           if (inzkbox[j].checked === true) {
                                                               //展开
                                                               //获取数量
                                                               let value = document.getElementById("sl" + inBomId + "").innerHTML;
                                                               parentElement.className = 'zked';

                                                               //获取titleId
                                                               await axios({
                                                                   method: "post",
                                                                   url: "finBomTitle/selectBomTitle",
                                                                   data: id
                                                               }).then(async function (resp) {
                                                                   let datas = resp.data;
                                                                   if (datas.length===1){
                                                                       let id1 = datas[0].id;
                                                                       await inZkImportant(id, id1,value);
                                                                   }
                                                                   else if (datas.length>1){
                                                                       //存在多张BOM表
                                                                       //查询多张BOM表的信息，并提供给用户选择
                                                                       document.getElementById("mySelect").innerHTML="";
                                                                       let formData="";
                                                                       for (let j = 0; j < datas.length; j++) {
                                                                           formData+= ' <option value='+datas[j].id+'>'+datas[j].title+'</option>'

                                                                       }
                                                                       document.getElementById("mySelect").innerHTML=formData;
                                                                       openModal();
                                                                       // 确认选择
                                                                       document.getElementById("selectEnter").onclick=async function () {
                                                                           const select = document.getElementById("mySelect");
                                                                           const selectedValue = select.value;
                                                                           const selectedText = select.options[select.selectedIndex].text;
                                                                           parentElement.querySelector(".bomTitle").innerHTML='('+selectedText+')';

                                                                           id1 = selectedValue;
                                                                           await inZkImportant(id, id1,value);
                                                                           document.getElementById("customModal").style.display = "none";

                                                                       }
                                                                       //取消选择
                                                                       document.getElementById("selectCancel").onclick=function (){
                                                                           document.getElementById("customModal").style.display = "none";
                                                                           inzkbox[j].checked =false;
                                                                       }
                                                                   }



                                                               })
                                                           } else {
                                                               //关闭
                                                               parentElement.className = 'deleteDiv';
                                                               parentElement.querySelector(".bomTitle").innerHTML="";
                                                               document.getElementById("zk" + inBomId + "").remove();

                                                           }


                                                       }
                                                   }
                                               }

                                               zkFunction();
                                           }

                                       })
                                   }
                                   await axios({
                                       method: "post",
                                       url: "finBomTitle/selectBomTitle",
                                       data: id
                                   }).then(async function (resp) {
                                       let datas = resp.data;
                                       if (datas.length === 1) {

                                           id1 = datas[0].id;
                                           await zkImportant(id,id1);

                                       }
                                       else if (datas.length>1){
                                           //存在多张BOM表
                                           //查询多张BOM表的信息，并提供给用户选择
                                           document.getElementById("mySelect").innerHTML="";
                                           let formData="";
                                           for (let j = 0; j < datas.length; j++) {
                                               formData+= ' <option value='+datas[j].id+'>'+datas[j].title+'</option>'

                                           }
                                           document.getElementById("mySelect").innerHTML=formData;
                                           openModal();
                                           // 确认选择
                                           document.getElementById("selectEnter").onclick=async function () {
                                               const select = document.getElementById("mySelect");
                                               const selectedValue = select.value;
                                               const selectedText = select.options[select.selectedIndex].text;
                                               parentElement.querySelector(".bomTitle").innerHTML='('+selectedText+')';

                                               id1 = selectedValue;
                                               await zkImportant(id, id1);
                                               document.getElementById("customModal").style.display = "none";

                                           }
                                           //取消选择
                                           document.getElementById("selectCancel").onclick=function (){
                                               document.getElementById("customModal").style.display = "none";
                                               zkboxs[i].checked =false;
                                           }

                                       }




                                   })
                               }

                               await oneZk();


                           }
                           else {
                               //关闭
                               parentElement.className='deleteDiv';
                               parentElement.querySelector(".bomTitle").innerHTML="";
                               document.getElementById("zk"+id+"").remove();
                               idvalue.disabled=false;


                           }
                       }

                   }


               }

               saveLocation();
               checkBoxDisplay()


           })
       // }



}



//读取函数
var read=function(){
    if (localStorage.my!==undefined){
        let data = JSON.parse(localStorage.my);//把本地存储的my转成数组
        //根据数据查询
       readInformation(data);
    }

}

//收集数据储存在本地
function saveLocation(){

    // let ids = document.querySelectorAll(".applicationProductId");
    // let vaults = document.querySelectorAll(".applicationVault");
    let numbers = document.querySelectorAll(".idValueNumber");



    if (numbers.length>0){
        let data=[numbers.length];
        let formdata={
            id:"",//物料id
            vault:"",//属于哪个仓库
            number:"",//申请数量
            price:"",

        }

        for (let i = 0; i < numbers.length; i++) {

            function qq(){
                if (numbers[i].parentElement.parentElement.querySelector(".rkPrice")){
                    return numbers[i].parentElement.parentElement.querySelector(".rkPrice").value
                }
                else {
                    return data[i]?.price ?? 0;
                }
            }

            formdata={
                id:numbers[i].parentElement.parentElement.querySelector(".applicationProductId").value,
                vault:numbers[i].parentElement.parentElement.querySelector(".applicationVault").value,
                number:numbers[i].value,
                price:qq(),

            }

            // let inBomId = deleteDiv[i].querySelector(".inBomId");
            // let finProductId = deleteDiv[i].querySelector(".finProductId");
            // let lastLevel = deleteDiv[i].querySelector(".lastLevel");
            //
            //
            // if (inBomId && finProductId && lastLevel){
            //
            //     formdata.number=document.getElementById("sl"+inBomId.value+"").innerHTML;
            //
            // }
            // else {
            //     formdata.number=deleteDiv[i].querySelector(".idValueNumber").value;
            // }



            data[i]=formdata;

            formdata={
                id:"",//物料id
                vault:"",//属于哪个仓库
                number:"",//申请数量
                price: "",

            }
        }
        console.log(data)
        save(data);
    }
    else {
        localStorage.removeItem("my")
    }

    // let elementById = document.getElementById("invoice");
    // if (elementById && elementById.checked===true){
    //
    // }
    // else {
    //     alert("aaaa")
    //     read();
    // }




}


document.getElementById("applicationTableBody").onmousedown=function (){
    saveLocation();
}

//删除申请单上的信息
function deletesq(){
    let deletes = document.querySelectorAll(".deleteApplication");
    let ids = document.querySelectorAll(".applicationProductId");
    for (let j = 0; j < deletes.length; j++) {
        deletes[j].onclick=function (){
            if (window.confirm("您确定要将该项从申请表中移除吗？")){
                let id = ids[j].value;
                let id1 = document.getElementById("productData"+id+ids[j].vault+"");
                if (id1){
                    let element = id1.querySelector(".productCheckbox");
                    element.checked=false;
                }
                //删除容器
                let parentElement = deletes[j].parentElement.parentElement;
                let value = parentElement.querySelector(".applicationProductId").value;
                let elementById = document.getElementById("zk"+value+"");
                parentElement.remove();
                if (elementById){
                    elementById.remove();
                }

                //数量减一
                let innerHTML = document.getElementById("applicationProductNumber").innerHTML;
                document.getElementById("applicationProductNumber").innerHTML=Number(innerHTML)-Number(1);
                //选择框变成未选择状态
                deletesq();
                saveLocation();
                inputNumberLocation();
                checkBoxDisplay();


                return true;
            }
            else {
                return false;
            }

        }
    }
}

//查询申请单上的数据，然后显示页面中的按钮是选中状态还是未选中状态
function checkBoxDisplay(){
    //获取申请单的数据
    let deletes = document.querySelectorAll(".deleteDiv");

    if (deletes.length>0){
        //浏览页面的加入按钮
        let checkbox = document.querySelectorAll(".productCheckbox");
        for (let i = 0; i < checkbox.length; i++) {
            checkbox[i].checked=false;
        }
        for (let i = 0; i < deletes.length; i++) {
            let value = deletes[i].querySelector(".applicationProductId").value;
            let vault = deletes[i].querySelector(".applicationVault").value;
            let elementById = document.getElementById("productData"+value+vault+"");
            if (elementById){
                let element = elementById.querySelector(".productCheckbox");

                element.checked=true;
            }
        }



//已经提交的申请单页面的按钮
        let insertForms = document.querySelectorAll(".insertForm");

            for (let i = 0; i < insertForms.length; i++) {
                insertForms[i].checked=false;
            }

            for (let i = 0; i < deletes.length; i++) {
                let value = deletes[i].querySelector(".applicationProductId").value;
                let vault = deletes[i].querySelector(".applicationVault").value;
                let elementById = document.getElementById("formProductData"+value+vault+"");
                if (elementById){


                    elementById.checked=true;
                }
            }



    }
    else {
        let checkbox = document.querySelectorAll(".productCheckbox");
        for (let i = 0; i < checkbox.length; i++) {
            checkbox[i].checked=false;
        }
    }







}

//输入申请数量后保存数据到本地
function inputNumberLocation(){
    let numbers = document.querySelectorAll(".idValueNumber");

    for (let i = 0; i < numbers.length; i++) {

        numbers[i].onchange=function (){
            //申请数量
            let number = numbers[i].value;
            //验证申请数量是否大于0
            if (number.length===0||Number(number)===0||isPositiveNumber(number)===false){
                numbers[i].value="";
                alert("请注意您填写的申请数量是否符合要求！")
            }
            else {
                saveLocation();
            }



        }
    }
}







//查询权限

async function userFunction(){
    let data;
    await axios({
        method:"post",
        url:"user/selectName"
    }).then( async function (resp){
        let userId=resp.data.id;

        await axios({
            method:"post",
            url:"userFunction/selectUserFunctionTwoByUserId",
            data:userId
        }).then(function (resp){
            data= resp.data;
        })

    })
    return data;
}


//申请入库的权限
async function appInBoundQx(){
    let promise = userFunction();
    let promiseBoolean=false;
    await promise.then( async function (resp) {

        for (let j = 0; j < resp.length; j++) {
            if (resp[j].function_two_id === 42 && resp[j].open_status === 1) {
                promiseBoolean = true;

                break;
            }
        }

    })
    return promiseBoolean;
}

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

//将图片转化为base64格式
function getBase64(file) {
    return new Promise(function (resolve, reject) {
        const reader = new FileReader()
        let imgResult = ''
        reader.readAsDataURL(file)
        reader.onload = function () {
            imgResult = reader.result
        }
        reader.onerror = function (error) {
            reject(error)
        }
        reader.onloadend = function () {
            resolve(imgResult)
        }
    })
}
function openSubmit(){

    //查询出库类型
    function ckSort(){
        axios({
            method:"post",
            url:"outBoundType/selectOutBound"
        }).then(function (resp){
            let datas=resp.data;
            let formdata='<option value="0">请选择类型</option>';

            for (let i = 0; i < datas.length; i++) {
                formdata+='<option class="type" value="'+datas[i].id+'">'+datas[i].type+'</option>'
            }


            // if (document.getElementById("outBoundType").querySelectorAll(".type").length===0){
            document.getElementById("outBoundType").innerHTML=formdata;
            // }


            let outBoundTypeButton = document.getElementById("outBoundType");
            outBoundTypeButton.onchange=function (){
                let index1 = outBoundTypeButton.selectedIndex;
                let value = outBoundTypeButton.options[index1].value;
                if (Number(value)===1 || Number(value)===2 || Number(value)===3 || Number(value)===4){
                    document.getElementById("dataitem").style.display="";
                }
                else {
                    document.getElementById("dataitem").style.display="none";
                }
            }


        })
    }




    //查询机型
    axios({
        method:"post",
        url:"model/selectModel"
    }).then(function (resp){
        let datas=resp.data;
        let formdata="";
        for (let i = 0; i < datas.length; i++) {
            formdata+='<option class="model" value="'+datas[i].modelName+'"></option>'
        }
        document.getElementById("model").innerHTML=formdata;
        document.getElementById("dataitem").style.display="none";
    })


    // ckSort();

    //筛选框发生变化进行筛选
    //出库还是入库筛选
    let outBoundButton = document.getElementById("outBound");
    function ckOrRk(){
        let index = outBoundButton.selectedIndex;
        let formGroup = document.querySelector(".form-group");

        if (Number(outBoundButton.options[index].value) === 1) {
            formGroup.style.display="none";
            //出库，查询出库类型
            ckSort();
            document.getElementById("robotBox").style.display="inline-block";

            let invoiceBox = document.getElementById("invoiceBox");
            if (invoiceBox){
                invoiceBox.remove();
            }
            let allPics = document.getElementById("allPic");
            if (allPics){
                allPics.remove();
            }


            let title = document.getElementById("applicantTitle");
            let app = title.querySelectorAll(".applicationSticky");
            for (let i = 0; i < app.length; i++) {
                let innerHTML = app[i].innerHTML;
                if (innerHTML==="预估价格(元)" || innerHTML==="发票价格(元)"){
                    app[i].remove();
                }
            }
            let price = document.querySelectorAll(".rkPrice");
            for (let i = 0; i < price.length; i++) {
                price[i].parentElement.remove();
            }
        }
        else if (Number(outBoundButton.options[index].value) === 0) {
            formGroup.style.display="none";
            document.getElementById("robotBox").style.display="none";
            document.getElementById("robot").checked=false;
            //入库，查询入库类型
            axios({
                method:"post",
                url:"outBoundType/selectInBound"
            }).then(function (resp){
                let datas=resp.data;
                let formdata='<option value="0">请选择类型</option>';
                for (let i = 0; i < datas.length; i++) {
                    formdata+='<option class="type" value="'+datas[i].id+'">'+datas[i].type+'</option>'
                }
                // if (document.getElementById("outBoundType").querySelectorAll(".type").length===0){
                    document.getElementById("outBoundType").innerHTML=formdata;
                // }


                //采购入库
                function cgrk(){
                    //采购入库
                    //获取选择框
                    let outBoundType = document.getElementById("outBoundType");
                    let selectedIndex = outBoundType.selectedIndex;
                    let value = outBoundType.options[selectedIndex].value;
                   let title = document.getElementById("applicantTitle");
                    if (Number(value)===Number(9)){
                        //采购入库
                        let app = title.querySelectorAll(".applicationSticky");
                        for (let i = 0; i < app.length; i++) {
                            let innerHTML = app[i].innerHTML;
                            if (innerHTML==="操作" && app[i-1].innerHTML !=="预估价格(元)"){
                                // let s ='<td class="applicationSticky"><input class="rkPrice" value="" type="number" style="width: 50%" onInput="value=value.replace(/^(0+)|[^\\d]+/g,\'\')"></td>'
                                let s ='<th class="applicationSticky">预估价格(元)</th>'

                                // 在元素前面插入HTML
                                app[i].insertAdjacentHTML("beforebegin", s);
                            }
                        }
                        let deletes = document.querySelectorAll(".deleteApplication");

                        for (let i = 0; i < deletes.length; i++) {
                            let parentElement = deletes[i].parentElement;

                            let element = parentElement.querySelector(".rkPrice");
                            if (element===null){
                                let element1 = parentElement.parentElement.querySelector(".priceSign");
                                let ss ='<td class="applicationSticky"><input class="rkPrice" value="'+element1.value+'" type="number" min="0" oninput="if (value<0) value=\'\';if (String(value)===\'\') value=\'\';" style="width: 50%"></td>'

                                parentElement.insertAdjacentHTML("beforebegin",ss)
                            }

                        }




                        //添加是否有发票按钮
                        let fp='<div id="invoiceBox">  发票<label for="invoice"></label><input  title="是否有发票" type="checkbox" id="invoice" ></div>'
                        let elementById = document.getElementById("invoiceBox");
                        if (elementById===null){
                            document.getElementById("outBoundType").insertAdjacentHTML("afterend",fp);
                        }

                        //判断是否有发票
                        let invoice = document.getElementById("invoice");
                        invoice.onchange=function (){
                            if (invoice.checked===true){
                                //采购入库
                                let app = title.querySelectorAll(".applicationSticky");
                                for (let i = 0; i < app.length; i++) {
                                    let innerHTML = app[i].innerHTML;
                                    if (innerHTML==="预估价格(元)"){
                                        // let s ='<td class="applicationSticky"><input class="rkPrice" value="" type="number" style="width: 50%" onInput="value=value.replace(/^(0+)|[^\\d]+/g,\'\')"></td>'
                                        app[i].innerHTML="发票价格(元)"

                                    }
                                }
                                let deletes = document.querySelectorAll(".deleteApplication");
                                let ss ='<td class="applicationSticky"><input class="rkPrice" value="" type="number" min="0" oninput="if (value<0) value=\'\';if (String(value)===\'\') value=\'\';" style="width: 50%"></td>'

                                for (let i = 0; i < deletes.length; i++) {
                                    let parentElement = deletes[i].parentElement;
                                    parentElement.insertAdjacentHTML("beforebegin",ss)
                                }
                                //有发票
                                let upFp = '<div id="allPic" style="width: 100%;height: auto;display: flex;flex-wrap: wrap"><div id="upFp" style="text-align: center;display: inline-block;margin-left: 10px">\n' +
                                    '        <input type="hidden" name="id"  value="">\n' +
                                    '        <input style="width: 223px;height: 24px;display: none" id="file0" type="file" >\n' +
                                    '        <div class="newUploads" style="width: 50px;height: 50px;font-size: 50px;line-height: 50px;border-style: dotted;border-width: 0.5px; cursor: pointer;" title="点击上传图片">+</div>\n' +
                                    '    </div></div>'

                                document.getElementById("notes").insertAdjacentHTML("afterend",upFp);



                                //上传图片
                                let uploads = document.querySelectorAll(".newUploads");

                                for (let i = 0; i < uploads.length; i++) {
                                    uploads[i].onclick=function (){
                                        document.getElementById('file'+i+'').click();
                                    }

                                }

                                //自动上传
                                for (let i = 0; i < uploads.length; i++) {
                                    document.getElementById('file'+i+'').onchange=async function () {
                                        let files = document.getElementById('file' + i + '').files[0];

                                        const baseResult = await getBase64(files)
                                         let pic='<div class="responsive">\n' +
                                        '  <div class="img">\n' +
                                        '    <a class="ylPic" style="cursor: pointer">\n' +
                                        '      <img src='+baseResult+' alt="发票" title="点击预览发票" >\n' +
                                        '    </a>\n' +
                                        '    <div class="desc" title="点击删除图片" ></div>\n' +
                                        '  </div>\n' +
                                        '</div>'
                                       document.getElementById("upFp").insertAdjacentHTML('afterend',pic);
                                        let ylPic = document.querySelectorAll(".ylPic");
                                        for (let j = 0; j < ylPic.length; j++) {
                                            ylPic[j].onclick=function (){
                                                let src = ylPic[j].querySelector("img").src;
                                                const img = new Image();
                                                img.src = src;
                                                const newWin = window.open("", "_blank");
                                                newWin.document.write(img.outerHTML);
                                                newWin.document.title = "预览图";
                                                newWin.document.close();

                                            }
                                        }
                                        document.getElementById("file"+i+"").value="";
                                        let deletePic = document.querySelectorAll(".desc");
                                        let pics = document.querySelectorAll(".responsive");
                                        for (let j = 0; j < deletePic.length; j++) {
                                            deletePic[j].onclick=function (){
                                                if (window.confirm("确定删除这张图片信息？")){
                                                    pics[j].remove();
                                                    return true;
                                                }
                                                else {
                                                    return false;
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                            else {
                                let app = title.querySelectorAll(".applicationSticky");
                                for (let i = 0; i < app.length; i++) {
                                    let innerHTML = app[i].innerHTML;
                                    if (innerHTML==="发票价格(元)"){
                                        app[i].innerHTML="预估价格(元)"
                                    }
                                }
                                // let price = document.querySelectorAll(".rkPrice");
                                // for (let i = 0; i < price.length; i++) {
                                //     price[i].parentElement.remove();
                                // }
                                //没有发票
                                document.getElementById("upFp").remove();
                                document.getElementById("allPic").remove();
                            }
                        }





                    }
                    else {

                        let app = title.querySelectorAll(".applicationSticky");
                        for (let i = 0; i < app.length; i++) {
                            let innerHTML = app[i].innerHTML;
                            if (innerHTML==="预估价格(元)" || innerHTML==="发票价格(元)"){
                                app[i].remove();
                            }
                        }
                        let price = document.querySelectorAll(".rkPrice");
                        for (let i = 0; i < price.length; i++) {
                            price[i].parentElement.remove();

                        }
                        let invoiceBox = document.getElementById("invoiceBox");
                        if (invoiceBox){
                            invoiceBox.remove();
                        }
                        let allPics = document.getElementById("allPic");
                        if (allPics){
                            allPics.remove();
                        }

                    }
                }
                cgrk();
                document.getElementById("outBoundType").onchange=function (){
                    cgrk();
                }
            })

            document.getElementById("dataitem").style.display='none';
        }
        else if (Number(outBoundButton.options[index].value)===2){
            formGroup.style.display="inline-block";
            //采购
            document.getElementById("robotBox").style.display="none";
            document.getElementById("robot").checked=false;
            // let innerHTML1 = document.getElementById("outBoundType").innerHTML;
            // if (innerHTML1===""){
                axios({
                    method:"post",
                    url:"outBoundType/selectCgBound"
                }).then(function (resp){
                    let datas=resp.data;
                    let formdata='<option value="0">请选择类型</option>';
                    for (let i = 0; i < datas.length; i++) {
                        formdata+='<option class="type" value="'+datas[i].id+'">'+datas[i].type+'</option>'
                    }
                    document.getElementById("outBoundType").innerHTML=formdata;

                })
            // }

        }

    }
    ckOrRk();
    outBoundButton.onchange = function (){
    ckOrRk();
    }

    //测试输入的机型是否存在
    async function modelExist(name) {
        let b = false;
        await axios({
            method: "post",
            url: "model/selectModelExist",
            data: name
        }).then(function (resp) {
            b = resp.data;
        })
        return b;
    }

    //提交申请单
    document.getElementById("submit").onclick=function (){


        //1.检测是否登录
        axios({
            method:"post",
            url:"user/selectLoginId"
        }).then(async function (resp) {
            let data = resp.data;//登录账号的id
            if (data !== "fail") {
                //根据用户ID查询是否存在未签字的申请单
                let s = await selectWqzById(data);
                if (s===false){
                    //2.已经登录，去查询申请的数量是否为空
                    let number = document.querySelectorAll(".idValueNumber");
                    if (number.length > 0) {
                        let numberBoolean = true;
                        for (let i = 0; i < number.length; i++) {

                            if (number[i].value.length === 0) {
                                numberBoolean = false;
                                break;
                            }
                        }

                        if (numberBoolean === true) {
                            //3.获取申请单数据
                            //生成申请单号
                            let all = [];
                            let appFormData = [{
                                id: "",
                                orderNumber: "",
                                date: "",
                                sort: "",
                                userId: data,
                                completeStatus: "3",
                                sortTwo: "",
                                reason:"",
                                circulation:"",
                                notes:document.getElementById("notes").value,
                                robot:document.getElementById("robot").checked
                            }]

                            //获取是出库还是入库
                            let outBoundButton = document.getElementById("outBound");
                            let index = outBoundButton.selectedIndex;
                            let value = outBoundButton.options[index].value;
                            let rk = true;
                            if (Number(value) === 1) {
                                //出库

                                appFormData[0].sort = value;
                            } else if (Number(value) === 0) {
                                //入库

                                appFormData[0].sort = value;
                                //检测是否具有申请入库的权限
                                rk=await appInBoundQx();
                            }
                            else if (Number(value)===2){
                                let level = document.getElementById('urgencyLevel').value;
                                //采购，检测是否具有申请采购的权限
                                appFormData[0].sort = value;
                                appFormData[0].robot=level;
                            }
                            if (rk===false){
                                alert("您暂未获得申请入库的权限！")
                            }
                            else {
                                //查看类型
                                let outBoundTypeButton = document.getElementById("outBoundType");
                                let index1 = outBoundTypeButton.selectedIndex;
                                let value1 = outBoundTypeButton.options[index1].value;
                                let type=true;
                                if (Number(value1) !== 0) {
                                    appFormData[0].sortTwo = value1;

                                    //获取申请物料信息
                                    let applicationContent=[];
                                    let deletes = document.querySelectorAll(".deleteDiv");



                                    for (let i = 0; i < deletes.length; i++) {
                                        let products = deletes[i].querySelector(".applicationProductId");
                                        // let appNumbers = document.querySelectorAll(".idValueNumber");
                                        let vaults = deletes[i].querySelector(".applicationVault");
                                        let inBomId = deletes[i].querySelector(".inBomId");
                                        let finProductId = deletes[i].querySelector(".finProductId");
                                        let lastLevel = deletes[i].querySelector(".lastLevel");
                                        let finProductNumber = deletes[i].querySelector(".finProductNumber");
                                        let bomTitleId = deletes[i].querySelector(".bomTitleId");

                                        let formdata={
                                            id:"",
                                            applicationId:"",
                                            productId:products.value,
                                            appNumber:"",
                                            actual:"0",
                                            vault:vaults.value,
                                            appPrice:"",
                                            finProductId:"",
                                            finProductNumber:"",
                                            lastLevel:"",
                                            bomTitleId:""
                                        }
                                        if (inBomId && finProductId && lastLevel){
                                            formdata.finProductId=finProductId.value;
                                            formdata.lastLevel=lastLevel.value;
                                            formdata.appNumber=document.getElementById("sl"+inBomId.value+"").innerHTML;
                                            formdata.finProductNumber=finProductNumber.value;
                                            formdata.bomTitleId=bomTitleId.value;
                                        }
                                        else {
                                            formdata.appNumber=deletes[i].querySelector(".idValueNumber").value;
                                        }

                                        //&& document.getElementById("invoice").checked === true
                                        if (Number(value1)===9 ){
                                            //采购入库
                                            let rkPrice = document.querySelectorAll(".rkPrice");
                                            let value3 = rkPrice[i].value;
                                            if (value3.trim().length>0){
                                                formdata.appPrice=value3;
                                            }
                                            else {
                                                type=false;
                                                alert("请将入库价格填写完整！");
                                            }
                                            if (type===false){
                                                break;
                                            }

                                        }

                                        applicationContent[i]=formdata;
                                        formdata={
                                            id:"",
                                            applicationId:"",
                                            productId:"",
                                            appNumber:"",
                                            actual:"",
                                            vault:"",
                                            appPrice:"",
                                            finProductId:"",
                                            finProductNumber:"",
                                            lastLevel:"",
                                            bomTitleId:""
                                        }
                                        if (type===false){
                                            break;
                                        }
                                    }
                                    all[0]=appFormData;
                                    all[1]=applicationContent;

                                    // }

                                    //查看用途
                                    let model = document.getElementById("dataitem");
                                    if ((Number(value1)===1 ||Number(value1)===2 ||Number(value1)===3 ||Number(value1)===4)&& model.style.display!=="none"){
                                        let value2 = document.getElementById("dataitem").value;
                                        // let index2 = model.selectedIndex;
                                        // let value2 = model.options[index2].value;
                                        if (value2.length!==Number(0)){
                                            let b = await modelExist(value2);
                                            if (b===true){
                                                appFormData[0].reason=value2;
                                            }
                                            else {
                                                type=false;
                                                alert("请选择下拉框中的型号！")
                                            }

                                        }
                                        else {
                                            alert("请输入机型！")
                                            type=false;
                                        }
                                    }

                                }
                                else {
                                    type=false;
                                    alert("请选择类型！")
                                }


                                //查看发票
                                if (Number(value1)===9 && type===true){

                                    //查看是否有发票
                                    let invoice  = document.getElementById("invoice");
                                    let ylPc = document.querySelectorAll(".ylPic");

                                    if (invoice.checked===true){
                                        //已选择发票按钮，查询是否有发票

                                        if (ylPc.length===0){
                                            type=false;
                                            alert("请上传发票！")
                                        }
                                        else {
                                            //存在发票
                                            let invoiceData=[];
                                            for (let i = 0; i < ylPc.length; i++) {
                                                let formdata={
                                                    id:"",
                                                    appId:"",
                                                    invoiceStr:ylPc[i].querySelector("img").src
                                                }
                                                invoiceData[i] = formdata;
                                                formdata={
                                                    id:"",
                                                    appId:"",
                                                    invoiceStr:""
                                                }

                                            }
                                            all[2]=invoiceData;

                                        }
                                    }
                                }


                                if (type===true){
                                    //数量不为空
                                    if (window.confirm("您确定要提交申请信息吗？")) {
                                        document.getElementById("submit").disabled=true;

                                        //提交申请单信息
                                        axios({
                                            method: "post",
                                            url: "applicationContent/addAll",
                                            data: all
                                        }).then(function (resp) {
                                            if (resp.data === "success") {
                                                alert("申请单信息提交成功！");
                                                localStorage.removeItem("my");
                                                location.reload();
                                            } else {
                                                alert("申请单信息提交失败，请联系相关人员！")
                                                document.getElementById("submit").disabled=false;

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
                        else {
                            alert("检测到存在申请数量未填写！")
                        }
                    } else {
                        alert("未检测到需要提交的内容！")

                    }
                }
                else {
                    alert("您存在未签字的申请单，请签字后再继续！")
                }



            } else {
                alert("请登录后再进行操作")

            }

        })
    }
}

document.getElementById("applicationButton").onclick=function (){

    document.querySelector(".window").style.display="block";
    document.getElementById("addOrder").style.display="block"
    document.getElementById("myOrder").style.display="none";
    document.getElementById("myOrderContent").style.display="none";
    document.getElementById("qzContent").style.display="none";
    //获取是否已经存在类别了，如果存在就不用再重新获取了
let elementNodeListOf = document.getElementById("outBoundType").querySelectorAll(".type");
if (elementNodeListOf.length===0){
    openSubmit();

}
read();


}
//禁止页面进行拖动，在移动端使用，签字完成后要移除此监听方法，要不然会导致无法滑动侧边栏
var banRemove=function (e) {
    e.preventDefault();
}

function canvas(){
    // 获取画布
    var test = document.getElementById("myCanvas");

    test.height=Number(test.parentElement.offsetHeight) * 0.99;
    test.width=Number(test.parentElement.offsetWidth) * 0.99;

    // 检验支持性
    if(test.getContext){
        // 获取画笔
        var ctx = test.getContext("2d");
    }
    // 为画布绑定鼠标按下事件
    test.onmousedown = function(event){
        // 设置事件兼容
        event = event || window.event;
        //获取鼠标偏移量
        let offsetLeft = test.getBoundingClientRect().left;// 计算左边偏移量
        let offsetTop  = test.getBoundingClientRect().top;// 计算顶部偏移量

        var X = event.clientX - offsetLeft;
        var Y = event.clientY - offsetTop;



        // 清空路径容器
        ctx.beginPath();
        // 画笔抬起
        ctx.moveTo(X,Y);
        // 绑定鼠标移动事件
        document.onmousemove = function(event){
            event = event || window.event;
            let offsetLeft = test.getBoundingClientRect().left;// 计算左边偏移量
            let offsetTop = test.getBoundingClientRect().top;// 计算顶部偏移量

            // var X = event.clientX - offsetLeft;
            // var Y = event.clientY - offsetTop;
            // 获取鼠标移动偏移量
            var proX = event.clientX - offsetLeft;
            var proY = event.clientY - offsetTop;
            // 压栈
            ctx.save();
            // 设置样式
            ctx.strokeStyle = "skyblue";
            // 划线
            ctx.lineTo(proX,proY);
            ctx.stroke();
            ctx.restore();
        };
        // 为画布绑定鼠标抬起事件
        document.onmouseup = function(event){
            event = event || window.event;
            // 取消鼠标移动事件
            document.onmousemove = null;
            // 取消鼠标抬起事件
            document.onmouseup = null;
        };
        // // 设置下载
        // document.getElementById("saveSign").onclick=function(){
        //     let link = document.createElement('a');
        //     link.href = test.toDataURL('image/png');
        //     link.download = "sign.png";
        //     link.click();
        // }

        //设置清空画布
        document.getElementById("clearSign").onclick=function(){
            var cxt=document.getElementById("myCanvas").getContext("2d");
            cxt.clearRect(0,0,test.width,test.height);

        }


        return false;
    };



}

function canvasPhone(){
    //阻止页面拖动
    document.body.addEventListener('touchmove', banRemove
        , { passive: false });

    var canvas = document.getElementById("myCanvas");
    canvas.height=Number(canvas.parentElement.offsetHeight) * 0.99;
    canvas.width=Number(canvas.parentElement.offsetWidth) * 0.99;
    var ctx = canvas.getContext("2d");

//页面样式
//           canvas.width = window.screen.width - 42;//左边距20，右边为对齐左边留20，边框左右各1
//           canvas.height = window.screen.height - 130;//顶边距20，底边距20，边框左右各1，按钮组68，按钮组底边距20

    // var l = canvas.offsetLeft;
    // var t = canvas.offsetTop;

    let l = canvas.getBoundingClientRect().left;// 计算左边偏移量
    let t = canvas.getBoundingClientRect().top;// 计算顶部偏移量

    // alert(l)
    // alert(t)


    canvas.ontouchstart = function (e) {
        ctx.beginPath();
        ctx.moveTo(e.touches[0].pageX - l, e.touches[0].pageY - t);
    }

    canvas.ontouchmove = function (e) {
        ctx.lineTo(e.touches[0].pageX - l, e.touches[0].pageY - t);
        ctx.stroke();
    }

    canvas.ontouchend = function (e) {
        ctx.closePath();
    }
    //设置清空画布
    document.getElementById("clearSign").onclick=function(){
        var cxt=document.getElementById("myCanvas").getContext("2d");
        cxt.clearRect(0,0, canvas.width, canvas.height);

    }
}

//申请单已经发生过出入库后撤销申请单需要执行的操作
function thrk(sort,sortTwo,contentData,appFormId){


    //sort为申请类型：出库或入库

    //1.检测是否登录
    axios({
        method:"post",
        url:"user/selectLoginId"
    }).then(function (resp)
    {
        let userdata=resp.data;//登录账号的id
        if (userdata!=="fail") {




            //3.获取申请单数据
            //生成申请单号
            let all=[];
            let appFormData=[{
                id:"",
                orderNumber:"",
                date:fDate(new Date()),
                sort:sort,
                userId:userdata,
                completeStatus:"3",
                sortTwo:sortTwo
            }]
            if (sort===0){
                //入库
                appFormData[0].orderNumber='RK_'+''+new Date().getTime()+''
            }else if (sort===1){
                //出库
                appFormData[0].orderNumber='CK_'+''+new Date().getTime()+''
            }
            //获取申请物料信息
            let applicationContent=[];
            //查询该申请单的数据

            for (let i = 0; i < contentData.length; i++) {
                let formdata={
                    id:"",
                    applicationId:"",
                    productId:contentData[i].product_id,
                    appNumber:contentData[i].actual_number,
                    actual:"0",
                    vault:contentData[i].vault
                }
                applicationContent[i]=formdata;
                formdata={
                    id:"",
                    applicationId:"",
                    productId:"",
                    appNumber:"",
                    actual:"",
                    vault:""
                }
            }

            all[0]=appFormData;
            all[1]=applicationContent;
            all[2]=[{appFormId}];

            //提交申请单信息
            axios({
                method:"post",
                url:"applicationContent/addAll",
                data:all
            }).then(function (resp){
                if (resp.data==="success"){
                    axios({
                        method: "post",
                        url: "applicationForm/updateRevoke",
                        data:appFormId
                    }).then(function (resp) {
                        if (resp.data === "success") {
                            alert("该申请已撤销！");
                            location.reload();
                        } else {
                            alert("申请撤销失败，请联系相关人员！")
                        }
                    })

                }
                else {
                    alert("申请撤销失败，请联系相关人员！")
                }

            })












        }




        else {
            alert("请登录后再进行操作")

        }

    })
}

//部分退料
function  bfProductBack(sort,sortTwo,contentData,appFormId){
    //sort为申请类型：出库或入库

    //1.检测是否登录
    axios({
        method:"post",
        url:"user/selectLoginId"
    }).then(function (resp)
    {
        let userdata=resp.data;//登录账号的id
        if (userdata!=="fail") {
            //3.获取申请单数据
            //生成申请单号
            let all=[];
            let appFormData=[{
                id:"",
                orderNumber:"",
                date:fDate(new Date()),
                sort:sort,
                userId:userdata,
                completeStatus:"3",
                sortTwo:sortTwo
            }]
            if (sort===0){
                //入库
                appFormData[0].orderNumber='RK_'+''+new Date().getTime()+''
            }else if (sort===1){
                //出库
                appFormData[0].orderNumber='CK_'+''+new Date().getTime()+''
            }
            //获取申请物料信息
            let applicationContent=[];
            //查询该申请单的数据

            for (let i = 0; i < contentData.length; i++) {
                let formdata={
                    id:"",
                    applicationId:"",
                    productId:contentData[i].productId,
                    appNumber:contentData[i].appNumber,
                    actual:"0",
                    vault:contentData[i].vault
                }
                applicationContent[i]=formdata;
                formdata={
                    id:"",
                    applicationId:"",
                    productId:"",
                    appNumber:"",
                    actual:"",
                    vault:""
                }
            }

            all[0]=appFormData;
            all[1]=applicationContent;
            all[2]=[{appFormId}];
            //提交申请单信息
            axios({
                method:"post",
                url:"applicationContent/addAll",
                data:all
            }).then(function (resp){
                if (resp.data==="success"){
                  alert("该申请已提交！")
                    location.reload();

                }
                else {
                    alert("申请提交失败，请联系相关人员！")
                }

            })












        }




        else {
            alert("请登录后再进行操作")

        }

    })
}


//点击查看审核流程
function shLc(data,appFormId){
    let wdSmallOne = document.querySelector(".window-small-one");
    document.querySelector(".window-small-content-close-one").onclick=function (){
        wdSmallOne.style.display="none";
    }
    wdSmallOne.style.display="block";
    let status = document.querySelector(".shStatus");
    status.innerHTML="";


    axios({
        method:"post",
        url:"applicationForm/selectPriceAndCount",
        data:data
    }).then(async function (resp)
    {
        let datas = resp.data;
        for (let i = 0; i < datas.length; i++) {
            //查询申申请人部门
            await axios({
                method:"post",
                url:"user/selectDepartById",
                data:data[i].userId
            }).then(async function (resp) {
                let departName = resp.data[0];
                let minister = datas[i].minister//部长是否筛选

                if ( Number(datas[i].id)===Number(appFormId) ){

                    //不需要总经理审核
                    if (Number(minister) === 0) {
                        status.innerHTML = '<div>'+departName.departmentName+':<span class="wsh">未审核</span></div><div class="lc"></div>'
                    } else if (Number(minister) === 1) {
                        status.innerHTML = '<div>'+departName.departmentName+':<span class="tg">通过</span></div><div class="lc"></div>'
                    } else if (Number(minister) === 2) {
                        status.innerHTML = '<div>'+departName.departmentName+':<span class="wtg">未通过</span></div><div style="color: red;font-weight: bolder;margin: 10px auto" >未通过原因:<span>'+data[i].refuse+'</span></div><div class="lc"></div>'



                    }

                    //查询审核流程
                    await axios({
                        method: "post",
                        url: "applicationForm/selectLc",
                        data: datas[i].id
                    }).then(function (resp)
                    {
                        let lcDatas=resp.data;
                        let lc = document.querySelector(".lc");
                        for (let j = 0; j < lcDatas.length; j++) {

                            if (Number(lcDatas[j].result)===0){
                                lc.innerHTML+="<div>"+lcDatas[j].departmentName+":<span class=\"wsh\">未审核</span></div>"
                            }
                            else if (Number(lcDatas[j].result)===1){
                                lc.innerHTML+="<div>"+lcDatas[j].departmentName+":<span class=\"tg\">通过</span></div>"
                            }
                            else if (Number(lcDatas[j].result)===2){
                                lc.innerHTML+="<div>"+lcDatas[j].departmentName+":<span class=\"wtg\">未通过</span></div>"
                                lc.innerHTML+="<div style='color: red;font-weight: bolder;margin: 10px auto' >未通过原因:<span>"+data[i].refuse+"</span></div>"
                            }




                        }


                    })
                }

            })



        }
    })


}

//根据用户ID查询部门
async function selectDepart(id) {
    let s;
    //查询申申请人部门
    await axios({
        method: "post",
        url: "user/selectDepartById",
        data: id
    }).then(function (resp) {
        s=resp.data[0].departmentName;

        })
    return s;
}

//获取筛选状态
function examineStatus(data){
    let status = document.querySelectorAll(".completeStatus");
    let depart = document.querySelectorAll(".examineDepart");

    axios({
        method:"post",
        url:"applicationForm/selectPriceAndCount",
        data:data
    }).then(async function (resp)
    {
        let datas=resp.data;

        for (let i = 0; i < datas.length; i++) {

            let minister = datas[i].minister//部长是否筛选

            let appFormId = datas[i].id;
            let newVar = await selectDepart(data[i].userId);
            if (Number(minister)===0){
                //部长未审核
                depart[i].innerHTML = '<div>'+newVar+'</div>'
                status[i].innerHTML = '<div><span class="wsh">未审核</span></div>'
            }
            else if (Number(minister)===1){

                //部长通过审核
                //先查询是否该总经理审核，如果不该总经理审核则去查改谁审核
                for (let j = 0; j < data.length; j++) {

                    let id = data[j].id;
                    let circulation = data[j].circulation;//该哪个部门审核了
                    if (Number(id)===Number(appFormId)){


                            //不该总经理审核，去查询该谁审核
                            await axios({
                                method: "post",
                                url: "applicationForm/selectLc",
                                data: datas[i].id
                            }).then(function (resp)
                            {
                                let lcDatas=resp.data;
                                if (lcDatas.length>0){

                                    for (let k = 0; k < lcDatas.length; k++) {
                                        let department = lcDatas[k].department_id;

                                        if (Number(circulation)===Number(department)){
                                            //找到了该审核的部门
                                            if (Number(lcDatas[k].result)===0){
                                                depart[i].innerHTML +=""+lcDatas[k].departmentName+""
                                                status[i].innerHTML +="<span class=\"wsh\">未审核</span>"
                                            }
                                            else if (Number(lcDatas[k].result)===1){
                                                depart[i].innerHTML +=""+lcDatas[k].departmentName+""
                                                status[i].innerHTML +="<span class=\"tg\">通过</span>"
                                            }
                                            else if (Number(lcDatas[k].result)===2){
                                                depart[i].innerHTML +=""+lcDatas[k].departmentName+""
                                                status[i].innerHTML +="<span class=\"wtg\">未通过</span>"
                                            }
                                        }

                                    }
                                }
                                else {
                                    depart[i].innerHTML = '<div>'+newVar+'</div>'
                                    status[i].innerHTML = '<div><span class="tg">通过</span></div>'
                                }

                            })

                    }

                }

            }
            else {
                //部长未通过审核
                depart[i].innerHTML = '<div>'+newVar+'</div>'
                status[i].innerHTML = '<div><span class="wtg">未通过</span></div>'
            }
        }
    })


    for (let i = 0; i < status.length; i++) {
        status[i].onclick=function (){
            shLc(data,data[i].id);
        }
    }




}
// 基本计算
function calculatePercentage(part, total, decimalPlaces = 2) {
    if (total === 0) return '0%';
    const percentage = (part / total) * 100;
    return percentage.toFixed(decimalPlaces) + '%';
}
//登录人查询自己的申请单
function selectMyApp(currentpage,userId){

    let screenData = {
        betweenDate: document.getElementById("between").value,
        andDate: document.getElementById("and").value,
        userId: userId,
    }
    //根据用户id查询自己的申请单内容
    axios({
        method:"post",
        url:"applicationForm/selectByUserId?currentPage="+currentpage+"",
        data:screenData
    }).then(async function (resp)
    {
        let data = resp.data.rows;
        let formdata = "";
        if (data.length > 0) {
            let totalCount = resp.data.totalCount;


            //改变当前页页码
            document.getElementById("currentPage1").innerHTML = currentpage;
            //计算共有多少页
            document.getElementById("pageSum1").innerHTML = Math.ceil(Number(totalCount) / 15);
            document.getElementById("totalCount1").innerHTML = totalCount;
            for (let i = 0; i < data.length; i++) {
                //判断是否存在需要申请人签字的申请单
                await axios({
                    method:"post",
                    url:"log/selectWqz",
                    data:data[i].id
                }).then(function (resp){
                    let boolean = resp.data;
                    if (boolean===true && Number(data[i].completeStatus)!==2 && Number(data[i].sort)===Number(1)){
                        //需要签字，但是状态不是已撤销的才可以签字
                        formdata += '<tr bgcolor="white">\n' +
                            '                <td class="applicationSticky">' + data[i].orderNumber + '</td>\n' +
                            '                <td class="applicationSticky">' + fDate(new Date(data[i].date)) + '</td>\n' +
                            '                <td class="applicationSticky"><span class="mySort"></span></td>\n' +
                            '                <td class="applicationSticky"><span class="myCompleteStatus"></span></td>\n' +
                            '                <td class="applicationSticky"><span class="examineDepart"></span></td>\n' +
                            '                <td class="applicationSticky"><span class="completeStatus"></span></td>\n' +
                            '                <td class="applicationSticky"><a class="checkMyApp" href="javascript:void(0)">查看</a><a  class="goqz"  href="javascript:void(0)">签字</a></td>\n' +
                            '            </tr>'
                    }
                    else {
                        formdata += '<tr bgcolor="white">\n' +
                            '                <td class="applicationSticky">' + data[i].orderNumber + '</td>\n' +
                            '                <td class="applicationSticky">' + fDate(new Date(data[i].date)) + '</td>\n' +
                            '                <td class="applicationSticky"><span class="mySort"></span></td>\n' +
                            '                <td class="applicationSticky"><span class="myCompleteStatus"></span></td>\n' +
                            '                <td class="applicationSticky"><span class="examineDepart"></span></td>\n' +
                            '                <td class="applicationSticky"><span class="completeStatus"></span></td>\n' +
                            '                <td class="applicationSticky"><a class="checkMyApp" href="javascript:void(0)">查看</a><div style="display: none" class="goqz"></div></td>\n' +
                            '            </tr>'
                    }
                })




            }
            document.getElementById("myTableBody").innerHTML = formdata;
            //审核状态填写
            examineStatus(data);
            let sort = document.querySelectorAll(".mySort");
            let status = document.querySelectorAll(".myCompleteStatus");
            for (let i = 0; i < sort.length; i++) {
                let completeStatus = data[i].completeStatus;

                //查询分类
                axios({
                    method:"post",
                    url:"outBoundType/selectAllType"
                }).then(function (resp){
                    let types=resp.data;
                    for (let i = 0; i < sort.length; i++) {
                        for (let j = 0; j < types.length; j++) {
                            if (Number(data[i].sortTwo)===Number(types[j].id)){
                                //生产出库
                                sort[i].innerHTML=types[j].type;
                            }
                        }


                    }
                })

                if (Number(completeStatus) === 1) {
                    status[i].style.color = "green";
                    status[i].innerHTML = "已完成"
                } else if (Number(completeStatus) === 0) {
                    if(Number(data[i].sort)===1){
                        //出库
                        status[i].innerHTML = "取货中"
                    }
                    else if (data[i].sort===0){
                        //入库
                        status[i].innerHTML = "验货中"
                    }
                    else if (data[i].sort ===2){
                        //采购
                        status[i].innerHTML="请购中";
                    }
                    status[i].style.color = "red";

                } else if (Number(completeStatus) === 2) {
                    status[i].innerHTML = "已撤销";
                    status[i].style.color = "pink";
                } else if (Number(completeStatus) === 3) {
                    status[i].innerHTML = "待查看";
                    status[i].style.color = "#a1a126";
                } else if (Number(completeStatus) === 4) {
                    status[i].innerHTML = "已出库";
                    status[i].style.color = "orange";
                } else if (Number(completeStatus) === 5) {
                    status[i].innerHTML = "待处理";
                    status[i].style.color = "blue";
                } else if (Number(completeStatus) === 6) {
                    status[i].style.color = "purple";
                    status[i].innerHTML = "待签字"
                } else if (Number(completeStatus) === 7) {
                    status[i].style.color = "yellow";
                    status[i].innerHTML = "撤销中"
                }


            }

            let check = document.querySelectorAll(".checkMyApp");
            for (let i = 0; i < check.length; i++) {
                check[i].onclick = async function () {
                    let contentData2=[];
                    let contentData3=[];
                    let readData=[];
                    let readDataIn={
                        id:"",//物料id
                        vault:"",//属于哪个仓库
                        number:"",//申请数量
                        price:"",
                    }
                    if (Number(data[i].sort)===4){
                        axios({
                            method:"post",
                            url:"applicationContent/selectZjAndAppContent",
                            data:data[i].id
                        }).then(function (resp){
                            let contentData = resp.data;
                            let formdata = "";
                            for (let j = 0; j < contentData.length; j++) {
                                readDataIn={
                                    id:contentData[j].product_id,//物料id
                                    vault:contentData[j].vault ,//属于哪个仓库
                                    number:contentData[j].app_number,//申请数量
                                }
                                readData.push(readDataIn);
                                contentData2.push(contentData[j])
                                contentData3.push(contentData[j])
                                var buttonsDisabled = Number(data[i].completeStatus)===1;

// 根据pass值确定默认选中的选项
                                var passChecked = contentData[j].pass === 1 ? 'checked' : '';
                                var failChecked = contentData[j].pass === 2 ? 'checked' : '';

// 构建单选框组
                                var radioGroup = '<div style="display: flex; align-items: center; gap: 10px;">' +
                                    '<label style="display: flex; align-items: center; gap: 3px; cursor: ' + (buttonsDisabled ? 'not-allowed' : 'pointer') + '">' +
                                    '<input class="pass" type="radio" name="passStatus_' + contentData[j].product_id +contentData[j].vault+ '" value="1" ' +
                                    passChecked + (buttonsDisabled ? ' disabled' : '') + ' style="cursor: ' + (buttonsDisabled ? 'not-allowed' : 'pointer') + '">' +
                                    '合格' +
                                    '</label>' +

                                    '<label style="display: flex; align-items: center; gap: 3px; cursor: ' + (buttonsDisabled ? 'not-allowed' : 'pointer') + '">' +
                                    '<input class="fail" type="radio" name="passStatus_' + contentData[j].product_id +contentData[j].vault+ '" value="2" ' +
                                    failChecked + (buttonsDisabled ? ' disabled' : '') + ' style="cursor: ' + (buttonsDisabled ? 'not-allowed' : 'pointer') + '">' +
                                    '不合格' +
                                    '</label>' +
                                    '</div>';
                                if (contentData[j].vault===0){
                                    formdata+= '  <tr  class=zd bgcolor="white" >\n' +
                                        '                    <td class="applicationSticky" ><input class="bfBack" disabled type="checkbox"><input id="formProductData'+ contentData[j].product_id+''+contentData[j].vault +'" class="insertForm" type="checkbox" title="点击重新加入申请单"></td>\n' +
                                        '                    <td class="applicationSticky" ><input style="display: none" class="myProductId" value="' + contentData[j].product_id + '"><b>' + contentData[j].material_number + '</b></td>\n' +
                                        '                    <td class="applicationSticky" ><input style="display: none" class="myVault" value="' + contentData[j].vault + '"><b>' + contentData[j].name + '</b></td>\n' +
                                        '                    <td class="applicationSticky" ><span>单位:('+contentData[j].unit+')</span><input disabled class="myAppNumber" value="' + contentData[j].app_number + '"  ><input disabled class="backNumber" style="display: none;" value="" placeholder="请输入数量" pattern="^(0|[1-9]\\d*)(\\.\\d{2})?$" oninput="this.value = this.value.replace(/[^0-9.]/g, \'\').replace(/(\\..*)\\./g, \'$1\').replace(/^0+(?=\\d)/, \'\').replace(/^\\./, \'0.\').replace(/(\\.\\d{2}).*/, \'$1\').replace(/^(0|[1-9]\\d*)(\\.\\d{0,2})?.*/, \'$1$2\')" title="请输入正数，最多两位小数（如123.45）"></td>\n' +
                                        '                    <td class="applicationSticky" ><b class="myActualNumber">' + contentData[j].actual_number + '</b></td>\n' +
                                        '                    <td class="applicationSticky" ><b >' + contentData[j].pass_rate+ '</b> (合格率：'+calculatePercentage(contentData[j].pass_rate,contentData[j].actual_number)+')</td>\n' +
                                        '                    <td class="applicationSticky" ><b >' + contentData[j].notes+ '</b></td>\n' +
                                        '                    <td class="applicationSticky" ><b >' + radioGroup+ '</b></td>\n' +
                                        '                </tr>'
                                }
                                else if (contentData[j].vault===1){
                                    formdata+= '  <tr  class=zd bgcolor="white" >\n' +
                                        '                    <td class="applicationSticky" ><input class="bfBack" disabled type="checkbox"><input  id="formProductData'+ contentData[j].product_id+''+contentData[j].vault +'"  class="insertForm" type="checkbox" title="点击重新加入申请单"></td>\n' +
                                        '                    <td class="applicationSticky" ><input style="display: none" class="myProductId" value="' + contentData[j].product_id + '"><b>' + contentData[j].material_number + '</b></td>\n' +
                                        '                    <td class="applicationSticky" ><input style="display: none" class="myVault" value="' + contentData[j].vault + '"><b>' + contentData[j].name + '</b></td>\n' +
                                        '                    <td class="applicationSticky" ><b class="myActualNumber">' + contentData[j].actual_number + '</b></td>\n' +
                                        '                    <td class="applicationSticky" ><b >数量：' + contentData[j].pass_rate+ '</b> (合格率：'+calculatePercentage(contentData[j].pass_rate,contentData[j].actual_number)+')</td>\n' +
                                        '                    <td class="applicationSticky" ><b >' + contentData[j].notes+ '</b></td>\n' +
                                        '                    <td class="applicationSticky" ><b >' + radioGroup+ '</b></td>\n' +
                                        '                </tr>'
                                }

                                readDataIn={
                                    id:"",//物料id
                                    vault:"",//属于哪个仓库
                                    number:"",//申请数量
                                }
                            }
                            document.getElementById("myOrderContentTableBody").innerHTML =formdata;

                        })
                    }
                    else {


                        //根据ID查询申请的内容
                        await axios({
                            method:"post",
                            url:"applicationContent/selectOneLevel",
                            data:data[i].id
                        }).then(async function (resp)
                        {
                            let contentData = resp.data;
                            let formdata = "";
                            for (let j = 0; j < contentData.length; j++) {
                                readDataIn={
                                    id:contentData[j].product_id,//物料id
                                    vault:contentData[j].vault ,//属于哪个仓库
                                    number:contentData[j].app_number,//申请数量
                                }
                                readData.push(readDataIn);
                                contentData2.push(contentData[j])
                                contentData3.push(contentData[j])
                                if (contentData[j].vault===0){
                                    formdata+= '  <tr  class=zd bgcolor="white" >\n' +
                                        '                    <td class="applicationSticky" ><input class="bfBack" disabled type="checkbox"><input id="formProductData'+ contentData[j].product_id+''+contentData[j].vault +'" class="insertForm" type="checkbox" title="点击重新加入申请单"></td>\n' +
                                        '                    <td class="applicationSticky" ><input style="display: none" class="myProductId" value="' + contentData[j].product_id + '"><b>' + contentData[j].material_number + '</b></td>\n' +
                                        '                    <td class="applicationSticky" ><input style="display: none" class="myVault" value="' + contentData[j].vault + '"><b>' + contentData[j].name + '</b></td>\n' +
                                        '                    <td class="applicationSticky" ><span>单位:('+contentData[j].unit+')</span><input disabled class="myAppNumber" value="' + contentData[j].app_number + '"  ><input disabled class="backNumber" style="display: none;" value="" placeholder="请输入数量" pattern="^(0|[1-9]\\d*)(\\.\\d{2})?$" oninput="this.value = this.value.replace(/[^0-9.]/g, \'\').replace(/(\\..*)\\./g, \'$1\').replace(/^0+(?=\\d)/, \'\').replace(/^\\./, \'0.\').replace(/(\\.\\d{2}).*/, \'$1\').replace(/^(0|[1-9]\\d*)(\\.\\d{0,2})?.*/, \'$1$2\')" title="请输入正数，最多两位小数（如123.45）"></td>\n' +
                                        '                    <td class="applicationSticky" ><b class="myNumber">' + contentData[j].number + '</b></td>\n' +
                                        '                    <td class="applicationSticky" ><b class="myActualNumber">' + contentData[j].actual_number + '</b></td>\n' +
                                        '                </tr>'
                                }
                                else if (contentData[j].vault===1){
                                    formdata+= '  <tr  class=zd bgcolor="white" >\n' +
                                        '                    <td class="applicationSticky" ><input class="bfBack" disabled type="checkbox"><input  id="formProductData'+ contentData[j].product_id+''+contentData[j].vault +'"  class="insertForm" type="checkbox" title="点击重新加入申请单"></td>\n' +
                                        '                    <td class="applicationSticky" ><input style="display: none" class="myProductId" value="' + contentData[j].product_id + '"><b>' + contentData[j].material_number + '</b></td>\n' +
                                        '                    <td class="applicationSticky" ><input style="display: none" class="myVault" value="' + contentData[j].vault + '"><b>' + contentData[j].name + '</b></td>\n' +
                                        '                    <td class="applicationSticky" ><span>单位:('+contentData[j].unit+')</span><input disabled class="myAppNumber" value="' + contentData[j].app_number + '" onInput="value=value.replace(/^(0+)|[^\\d]+/g,\'\')"><input disabled class="backNumber" style="display: none;" value="" placeholder="请输入数量" onInput="value=value.replace(/^(0+)|[^\\d]+/g,\'\')"></td>\n' +
                                        '                    <td class="applicationSticky" ><b class="myNumber">' + contentData[j].number + '</b></td>\n' +
                                        '                    <td class="applicationSticky" ><b class="myActualNumber">' + contentData[j].actual_number + '</b></td>\n' +
                                        '                </tr>'
                                }

                                readDataIn={
                                    id:"",//物料id
                                    vault:"",//属于哪个仓库
                                    number:"",//申请数量
                                }
                            }
                            document.getElementById("myOrderContentTableBody").innerHTML =formdata;



                            if (Number(data[i].sortTwo)===9){
                                //插入输入框
                                let deletes = document.querySelectorAll(".myNumber");

                                console.log(deletes)
                                for (let j = 0; j < deletes.length; j++) {
                                    let ss ='<td class="applicationSticky"><input class="fpPrice" value="' + contentData[j].app_price + '"  type="number" min="0" oninput="if (value<0) value=\'\';if (String(value)===\'\') value=\'\';" style="width: 50%"></td>'

                                    let parentElement = deletes[j].parentElement;
                                    parentElement.insertAdjacentHTML("beforebegin",ss)


                                }


                            }
                            else {
                                let element = document.querySelectorAll(".fpPrice");
                                if (element.length>0){
                                    for (let j = 0; j < element.length; j++) {
                                        element[j].parentElement.remove();
                                    }
                                }
                            }





                            //查询被展开的第一级
                            await  axios({
                                method:"post",
                                url:"applicationContent/selectOneLevelZk",
                                data:data[i].id
                            }).then(async function (resp) {
                                let contentData = resp.data;
                                let formdata = "";
                                for (let j = 0; j < contentData.length; j++) {
                                    readDataIn = {
                                        id: contentData[j].fin_product_id,//物料id
                                        vault: 1,//属于哪个仓库
                                        number: contentData[j].fin_product_number,//申请数量
                                    }
                                    readData.push(readDataIn)
                                    //获取BOM表名称
                                    let s = await bomTitleName(contentData[j].bomTitleId);
                                    formdata += '  <tr  class=zd bgcolor="white" >\n' +
                                        '                    <td class="applicationSticky" ></td>\n' +
                                        '                    <td class="applicationSticky" ><input style="display: none" class="myProductId" value="' + contentData[j].fin_product_id + '"><b>' + contentData[j].material_number + '</b></td>\n' +
                                        '                    <td class="applicationSticky" ><input style="display: none" class="myVault" value="' + 1 + '"><b>' + contentData[j].name + s+'</b></td>\n' +
                                        '                    <td class="applicationSticky" ><span>单位:('+contentData[j].unit+')</span><input disabled class="myAppNumber" value="' + contentData[j].fin_product_number + '" onInput="value=value.replace(/^(0+)|[^\\d]+/g,\'\')"></td>\n' +
                                        '                    <td class="applicationSticky" colspan="3" ><input style="cursor: pointer" class=myzk' + contentData[j].fin_product_id + contentData[j].last_level + ' type="checkbox" >展开</td>\n' +
                                        '                </tr>'
                                    readDataIn = {
                                        id: "",//物料id
                                        vault: "",//属于哪个仓库
                                        number: "",//申请数量
                                    }
                                }
                                document.getElementById("myOrderContentTableBody").innerHTML += formdata;
                                await formAddForm(contentData2)

                                async function selectNotes(bomTitleId,productId,vault){
                                    let s;
                                    await  axios({
                                        method:"post",
                                        url:"finBom/selectProductById?bomTitleId="+bomTitleId+"&productId="+productId+"&vault="+vault+"",
                                    }).then(function (resp){
                                        let datas= resp.data;
                                        s=datas.partNumber;
                                    })
                                    return s;
                                }

                                //给展开按钮绑定方法
                                async function zkMyOrderFunction(contentData) {
                                    for (let j = 0; j < contentData.length; j++) {
                                        let zkboxs = document.querySelector(".myzk" + contentData[j].fin_product_id + contentData[j].last_level + "");
                                        if (zkboxs) {
                                            zkboxs.checked = true
                                            //查询该分类下的产品(不可以展开的)
                                            await axios({
                                                method: "post",
                                                url: "applicationContent/selectProductByIdZkContent?applicationId=" + data[i].id + "&lastLevel=" + contentData[j].last_level + "",
                                                data: contentData[j].fin_product_id
                                            }).then(async function (resp) {
                                                let contentData1 = resp.data;
                                                let a = '<tr id=myzk' + contentData[j].fin_product_id + contentData[j].last_level + ' ><td colspan="7"><table id=myzktable' + contentData[j].fin_product_id + contentData[j].last_level + ' border="0" cellspacing="1" bgcolor="#333333" width="100%" ></table></td></tr>'
                                                zkboxs.parentElement.parentElement.insertAdjacentHTML("afterend", a);
                                                let formdata = "";
                                                console.log(contentData1)
                                                for (let k = 0; k < contentData1.length; k++) {
                                                    let newVar = await selectNotes(contentData1[k].bom_title_id,contentData1[k].product_id,contentData1[k].vault);
                                                    console.log(newVar)
                                                    contentData2.push(contentData1[k]);
                                                    formdata += '  <tr  class="zd" bgcolor="white" >\n' +
                                                        '                    <td rowspan="2" class="applicationSticky" ><input class="bfBack" disabled type="checkbox"><input  id="formProductData'+ contentData1[k].product_id+''+contentData1[k].vault +'"  class="insertForm" type="checkbox" title="点击重新加入申请单"></td>\n' +
                                                        '                    <td class="applicationSticky" ><input style="display: none" class="myProductId" value="' + contentData1[k].product_id + '"><b>' + contentData1[k].material_number + '</b></td>\n' +
                                                        '                    <td class="applicationSticky" ><input style="display: none" class="myVault" value="' + contentData1[k].vault + '"><b>' + contentData1[k].name + '</b></td>\n' +
                                                        '                    <td class="applicationSticky" ><span>单位:(' + contentData[j].unit + ')</span><input disabled class="myAppNumber" value="' + contentData1[k].app_number + '" onInput="value=value.replace(/^(0+)|[^\\d]+/g,\'\')"><input disabled class="backNumber" style="display: none;" value="" placeholder="请输入数量" onInput="value=value.replace(/^(0+)|[^\\d]+/g,\'\')"></td>\n' +
                                                        '                    <td class="applicationSticky" ><b class="myNumber">' + contentData1[k].number + '</b></td>\n' +
                                                        '                    <td class="applicationSticky" ><b class="myActualNumber">' + contentData1[k].actual_number + '</b></td>\n' +
                                                        '                </tr><tr class="zd"><td class="applicationSticky" colspan="7">' + newVar + '</td></tr>'
                                                }
                                                document.getElementById("myzktable" + contentData[j].fin_product_id + contentData[j].last_level + "").insertAdjacentHTML("beforeend", formdata);
                                                await formAddForm(contentData2);
                                                if (Number(data[i].sortTwo)===9){
                                                    //插入输入框
                                                    let deletes = document.getElementById("myzktable" + contentData[j].fin_product_id + contentData[j].last_level + "").querySelectorAll(".myNumber");

                                                    for (let j = 0; j < deletes.length; j++) {

                                                        let ss ='<td class="applicationSticky"><input class="fpPrice" value="' + contentData1[j].app_price + '"  type="number" min="0" oninput="if (value<0) value=\'\';if (String(value)===\'\') value=\'\';" style="width: 50%"></td>'

                                                        let parentElement = deletes[j].parentElement;
                                                        parentElement.insertAdjacentHTML("beforebegin",ss);

                                                    }
                                                    let prices = document.querySelectorAll(".fpPrice");
                                                    for (let k = 0; k < prices.length; k++) {
                                                        prices[k].onchange=function (){
                                                            readData[k].price=prices[k].value;
                                                        }
                                                    }

                                                }
                                                else {
                                                    let element = document.querySelectorAll(".fpPrice");
                                                    if (element.length>0){
                                                        for (let j = 0; j < element.length; j++) {
                                                            element[j].parentElement.remove();
                                                        }
                                                    }
                                                }
                                            })
                                            //查询可以展开的
                                            await axios({
                                                method: "post",
                                                url: "applicationContent/selectCanZk?applicationId=" + data[i].id + "",
                                                data: contentData[j].fin_product_id
                                            }).then(async function (resp) {
                                                let contentData1 = resp.data;
                                                let formdata = "";

                                                for (let k = 0; k < contentData1.length; k++) {
                                                    //获取BOM表名称
                                                    let s = await bomTitleName(contentData1[k].bomTitleId);
                                                    formdata += '  <tr  class=zd bgcolor="white" >\n' +
                                                        '                    <td class="applicationSticky" ></td>\n' +
                                                        '                    <td class="applicationSticky" ><input style="display: none" class="myProductId" value="' + contentData1[k].product_id + '"><b>' + contentData1[k].material_number + '</b></td>\n' +
                                                        '                    <td class="applicationSticky" ><input style="display: none" class="myVault" value="' + contentData1[k].vault + '"><b>' + contentData1[k].name + s+'</b></td>\n' +
                                                        '                    <td class="applicationSticky" ><span>单位:('+contentData[j].unit+')</span><input disabled class="myAppNumber" value="' + contentData1[k].fin_product_number + '" pattern="^(0|[1-9]\\d*)(\\.\\d{2})?$" oninput="this.value = this.value.replace(/[^0-9.]/g, \'\').replace(/(\\..*)\\./g, \'$1\').replace(/^0+(?=\\d)/, \'\').replace(/^\\./, \'0.\').replace(/(\\.\\d{2}).*/, \'$1\').replace(/^(0|[1-9]\\d*)(\\.\\d{0,2})?.*/, \'$1$2\')" title="请输入正数，最多两位小数（如123.45）"></td>\n' +
                                                        '                    <td class="applicationSticky" colspan="3" ><input style="cursor: pointer" class=myzk' + contentData1[k].fin_product_id + contentData1[j].last_level + ' type="checkbox" >展开</td>\n' +
                                                        '                </tr>'
                                                }
                                                document.getElementById("myzktable" + contentData[j].fin_product_id + contentData[j].last_level + "").insertAdjacentHTML("beforeend", formdata);
                                                await zkMyOrderFunction(contentData1);
                                            })
                                            zkboxs.onclick = async function () {
                                                if (zkboxs.checked === true) {
                                                    document.getElementById("myzk" + contentData[j].fin_product_id + contentData[j].last_level + "").style.display = "";

                                                } else {
                                                    document.getElementById("myzk" + contentData[j].fin_product_id + contentData[j].last_level + "").style.display = "none";
                                                }

                                            }
                                        }

                                    }
                                }

                                await zkMyOrderFunction(contentData);

                            })


                            //将申请单中的信息变成要修改的申请单中的（覆盖）
                            async function fg() {


                                document.getElementById("applicationTableBody").innerHTML = "";
                                await readInformation(readData);

                                console.log(readData)

                                document.getElementById("applicationProductNumber").innerHTML = readData.length;



                                //隐藏当前窗口，显示申请单窗口
                                document.getElementById("addOrder").style.display = "block";
                                document.getElementById("myOrderContent").style.display = "none";
                                document.getElementById("qzContent").style.display="none";

                                if (Number(data[i].sortTwo)===9){
                                    //采购入库
                                    let app = title.querySelectorAll(".applicationSticky");
                                    for (let i = 0; i < app.length; i++) {
                                        let innerHTML = app[i].innerHTML;
                                        if (innerHTML==="操作"){
                                            // let s ='<td class="applicationSticky"><input class="rkPrice" value="" type="number" style="width: 50%" onInput="value=value.replace(/^(0+)|[^\\d]+/g,\'\')"></td>'
                                            let s ='<th class="applicationSticky">预估价格(元)</th>'

                                            // 在元素前面插入HTML
                                            app[i].insertAdjacentHTML("beforebegin", s);
                                        }
                                    }
                                    document.getElementById("robotBox").style.display="none";
                                    await saveLocation();
                                    await checkBoxDisplay();
                                    await deletesq();
                                }
                            }

                            //两个清单的内容都要（叠加）
                            function dj() {
                                //叠加，先查询是否有展开的信息
                                let zked = document.querySelectorAll(".zked");
                                if (zked.length>0){
                                    alert("申请单中有展开的数据，请关闭后再试！")
                                }
                                else {
                                    //将申请单的内容放到重新提交的申请单上去


                                    //检测原申请单中是否有跟要修改的申请单中重复的信息
                                    let deleteDiv = document.querySelectorAll(".deleteDiv");
                                    //物料ID

                                    for (let j = 0; j < deleteDiv.length; j++) {
                                        //获取ID和仓库,申请数量
                                        let productId2 = deleteDiv[j].querySelector(".applicationProductId").value;
                                        let vault2=  deleteDiv[j].querySelector(".applicationVault").value;
                                        let yNumber =  deleteDiv[j].querySelector(".idValueNumber").value;
                                        let c=false;
                                        for (let k = 0; k < readData.length; k++) {
                                            //readData是要修改的申请单上的内容
                                            //获取ID和仓库,数量
                                            let id = readData[k].id;
                                            let vault1 = readData[k].vault;
                                            let number = readData[k].number;
                                            if (Number(id)===Number(productId2) && Number(vault1)===Number(vault2)){
                                                //是同一种物料
                                                readData[j].number=Number(number)+Number(yNumber);

                                                c=true;
                                            }

                                        }
                                        if (c===false){
                                            //不存在，加入readData
                                            let a={
                                                id:productId2,
                                                vault:vault2,
                                                number:yNumber
                                            }
                                            readData.push(a)
                                        }


                                    }



                                    document.getElementById("applicationTableBody").innerHTML = "";

                                    readInformation(readData);

                                    document.getElementById("applicationProductNumber").innerHTML = readData.length;


                                    saveLocation();
                                    checkBoxDisplay();
                                    deletesq();
                                    //隐藏当前窗口，显示申请单窗口
                                    document.getElementById("addOrder").style.display = "block";
                                    document.getElementById("myOrderContent").style.display = "none";
                                    document.getElementById("qzContent").style.display="none";
                                }


                            }

                            if (Number(data[i].completeStatus) === Number(2)) {
                                //如果是撤销状态,隐藏撤销按钮，隐藏退料按钮，显示重新修改按钮
                                document.getElementById("return").style.display="none";
                                document.getElementById("revoke").style.display = "none";
                                document.getElementById("reloadOrder").style.display = "";
                                //给修改申请添加方法
                                document.getElementById("reloadOrder").onclick = function () {
                                    if (window.confirm("您确定要修改申请单吗？")) {
                                        //检测申请单中是否已经存在信息
                                        let infor = document.querySelectorAll(".deleteDiv");
                                        if (Number(data[i].sortTwo)===9){
                                            //获取数据
                                            let prices = document.querySelectorAll(".fpPrice");
                                            for (let k = 0; k < prices.length; k++) {

                                                readData[k].price=prices[k].value;

                                            }

                                            if (window.confirm("点击确定将覆盖原申请单中的信息！")) {
                                                fg();
                                                document.getElementById("outBound").selectedIndex=1;
                                                let outBoundTypeButton = document.getElementById("outBoundType");
                                                //入库，查询入库类型
                                                axios({
                                                    method:"post",
                                                    url:"outBoundType/selectInBound"
                                                }).then(function (resp){
                                                    let datas=resp.data;
                                                    let formdata='<option value="0">请选择类型</option>';
                                                    for (let i = 0; i < datas.length; i++) {
                                                        formdata+='<option class="type" value="'+datas[i].id+'">'+datas[i].type+'</option>'
                                                    }
                                                    document.getElementById("outBoundType").innerHTML=formdata;
                                                    outBoundTypeButton.selectedIndex=2;
                                                    //采购入库
                                                    function cgrk(){
                                                        //采购入库
                                                        //获取选择框
                                                        let outBoundType = document.getElementById("outBoundType");
                                                        let selectedIndex = outBoundType.selectedIndex;
                                                        let value = outBoundType.options[selectedIndex].value;
                                                        let title = document.getElementById("applicantTitle");
                                                        if (Number(value)===Number(9)){
                                                            //采购入库
                                                            let app = title.querySelectorAll(".applicationSticky");
                                                            for (let i = 0; i < app.length; i++) {
                                                                let innerHTML = app[i].innerHTML;
                                                                if (innerHTML==="操作"){
                                                                    // let s ='<td class="applicationSticky"><input class="rkPrice" value="" type="number" style="width: 50%" onInput="value=value.replace(/^(0+)|[^\\d]+/g,\'\')"></td>'
                                                                    let s ='<th class="applicationSticky">预估价格(元)</th>'

                                                                    // 在元素前面插入HTML
                                                                    app[i].insertAdjacentHTML("beforebegin", s);
                                                                }
                                                            }
                                                            // let deletes = document.querySelectorAll(".deleteApplication");
                                                            // let ss ='<td class="applicationSticky"><input class="rkPrice" value="" type="number" min="0" oninput="if (value<0) value=\'\';if (String(value)===\'\') value=\'\';" style="width: 50%"></td>'
                                                            //
                                                            // for (let i = 0; i < deletes.length; i++) {
                                                            //     let parentElement = deletes[i].parentElement;
                                                            //     parentElement.insertAdjacentHTML("beforebegin",ss)
                                                            // }


                                                            //添加是否有发票按钮
                                                            let fp='<div id="invoiceBox">  发票<label for="invoice"></label><input  title="是否有发票" type="checkbox" id="invoice" ></div>'
                                                            document.getElementById("outBoundType").insertAdjacentHTML("afterend",fp);
                                                            //判断是否有发票
                                                            let invoice = document.getElementById("invoice");
                                                            invoice.onchange=function (){
                                                                if (invoice.checked===true){
                                                                    //采购入库
                                                                    let app = title.querySelectorAll(".applicationSticky");
                                                                    for (let i = 0; i < app.length; i++) {
                                                                        let innerHTML = app[i].innerHTML;
                                                                        if (innerHTML==="预估价格(元)"){
                                                                            // let s ='<td class="applicationSticky"><input class="rkPrice" value="" type="number" style="width: 50%" onInput="value=value.replace(/^(0+)|[^\\d]+/g,\'\')"></td>'
                                                                            app[i].innerHTML="发票价格(元)"

                                                                        }
                                                                    }
                                                                    // let deletes = document.querySelectorAll(".deleteApplication");
                                                                    // let ss ='<td class="applicationSticky"><input class="rkPrice" value="" type="number" min="0" oninput="if (value<0) value=\'\';if (String(value)===\'\') value=\'\';" style="width: 50%"></td>'
                                                                    //
                                                                    // for (let i = 0; i < deletes.length; i++) {
                                                                    //     let parentElement = deletes[i].parentElement;
                                                                    //     parentElement.insertAdjacentHTML("beforebegin",ss)
                                                                    // }
                                                                    //有发票
                                                                    let upFp = '<div id="allPic" style="width: 100%;height: auto;display: flex;flex-wrap: wrap"><div id="upFp" style="text-align: center;display: inline-block;margin-left: 10px">\n' +
                                                                        '        <input type="hidden" name="id"  value="">\n' +
                                                                        '        <input style="width: 223px;height: 24px;display: none" id="file0" type="file" >\n' +
                                                                        '        <div class="newUploads" style="width: 50px;height: 50px;font-size: 50px;line-height: 50px;border-style: dotted;border-width: 0.5px; cursor: pointer;" title="点击上传图片">+</div>\n' +
                                                                        '    </div></div>'

                                                                    document.getElementById("notes").insertAdjacentHTML("afterend",upFp);

                                                                    document.getElementById("robotBox").style.display="none"

                                                                    //上传图片
                                                                    let uploads = document.querySelectorAll(".newUploads");

                                                                    for (let i = 0; i < uploads.length; i++) {
                                                                        uploads[i].onclick=function (){
                                                                            document.getElementById('file'+i+'').click();
                                                                        }

                                                                    }

                                                                    //自动上传
                                                                    for (let i = 0; i < uploads.length; i++) {
                                                                        document.getElementById('file'+i+'').onchange=async function () {
                                                                            let files = document.getElementById('file' + i + '').files[0];

                                                                            const baseResult = await getBase64(files)
                                                                            let pic='<div class="responsive">\n' +
                                                                                '  <div class="img">\n' +
                                                                                '    <a class="ylPic" style="cursor: pointer">\n' +
                                                                                '      <img src='+baseResult+' alt="发票" title="点击预览发票" >\n' +
                                                                                '    </a>\n' +
                                                                                '    <div class="desc" title="点击删除图片" ></div>\n' +
                                                                                '  </div>\n' +
                                                                                '</div>'
                                                                            document.getElementById("upFp").insertAdjacentHTML('afterend',pic);
                                                                            let ylPic = document.querySelectorAll(".ylPic");
                                                                            for (let j = 0; j < ylPic.length; j++) {
                                                                                ylPic[j].onclick=function (){
                                                                                    let src = ylPic[j].querySelector("img").src;
                                                                                    const img = new Image();
                                                                                    img.src = src;
                                                                                    const newWin = window.open("", "_blank");
                                                                                    newWin.document.write(img.outerHTML);
                                                                                    newWin.document.title = "预览图";
                                                                                    newWin.document.close();

                                                                                }
                                                                            }
                                                                            document.getElementById("file"+i+"").value="";
                                                                            let deletePic = document.querySelectorAll(".desc");
                                                                            let pics = document.querySelectorAll(".responsive");
                                                                            for (let j = 0; j < deletePic.length; j++) {
                                                                                deletePic[j].onclick=function (){
                                                                                    if (window.confirm("确定删除这张图片信息？")){
                                                                                        pics[j].remove();
                                                                                        return true;
                                                                                    }
                                                                                    else {
                                                                                        return false;
                                                                                    }
                                                                                }
                                                                            }
                                                                        }
                                                                    }
                                                                }
                                                                else {
                                                                    let app = title.querySelectorAll(".applicationSticky");
                                                                    for (let i = 0; i < app.length; i++) {
                                                                        let innerHTML = app[i].innerHTML;
                                                                        if (innerHTML==="发票价格(元)"){
                                                                            app[i].innerHTML="预估价格(元)"
                                                                        }
                                                                    }
                                                                    // let price = document.querySelectorAll(".rkPrice");
                                                                    // for (let i = 0; i < price.length; i++) {
                                                                    //     price[i].parentElement.remove();
                                                                    // }
                                                                    //没有发票
                                                                    document.getElementById("upFp").remove();
                                                                    document.getElementById("allPic").remove();
                                                                }
                                                            }





                                                        }
                                                        else {

                                                            let app = title.querySelectorAll(".applicationSticky");
                                                            for (let i = 0; i < app.length; i++) {
                                                                let innerHTML = app[i].innerHTML;
                                                                if (innerHTML==="预估价格(元)" || innerHTML==="发票价格(元)"){
                                                                    app[i].remove();
                                                                }
                                                            }
                                                            let price = document.querySelectorAll(".rkPrice");
                                                            for (let i = 0; i < price.length; i++) {
                                                                price[i].parentElement.remove();

                                                            }
                                                            let invoiceBox = document.getElementById("invoiceBox");
                                                            if (invoiceBox){
                                                                invoiceBox.remove();
                                                            }
                                                            let allPics = document.getElementById("allPic");
                                                            if (allPics){
                                                                allPics.remove();
                                                            }

                                                        }
                                                    }
                                                    cgrk();
                                                    document.getElementById("outBoundType").onchange=function (){
                                                        cgrk();
                                                    }
                                                })



                                                return
                                            }
                                        }
                                        else {
                                            if (infor.length > 0) {
                                                //申请单中有信息，询问用户是否覆盖信息
                                                if (window.confirm("点击确定将覆盖原申请单中的信息！")) {
                                                    fg();
                                                    return
                                                } else {
                                                    dj();
                                                    return false;
                                                }

                                            } else {
                                                fg();
                                            }
                                        }



                                        return true;
                                    } else {
                                        return false;
                                    }
                                }

                            }
                            else {
                                //不是撤销的状态，查询是否有已经出入库的物料信息
                                document.getElementById("reloadOrder").style.display = "none";
                                let newVar = await ifActualNumber(data[i].id);

                                //查看是否存在出入库的物料
                                if (newVar === true) {

                                    //存在已经出入库的物料
                                    document.getElementById("revoke").onclick=function (){

                                        // if (Number(data[i].sortTwo)===8||Number(data[i].sortTwo)===10){
                                        //     alert("这已经是撤销后重新生成的申请单了！")
                                        // }
                                        // else {
                                        if (Number(data[i].sort)===0){
                                            //入库
                                            if (window.confirm("点击确定将生成出库申请单，并需要将已出库的物料归还！")){
                                                //退货出库
                                                thrk(1,10,contentData,data[i].id);

                                                return true;
                                            }
                                            else {
                                                return false;

                                            }
                                        }
                                        else if (Number(data[i].sort)===1){
                                            //出库
                                            if (window.confirm("点击确定将生成退货入库申请单，并需要将已出库的物料归还！")){
                                                //退货入库
                                                thrk(0,8,contentData,data[i].id);

                                                return true;
                                            }
                                            else {
                                                return false;

                                            }
                                        }
                                        else if (Number(data[i].sort===2)){
                                            //采购
                                        }
                                        // }


                                    }



                                    if (data[i].sort!==2){
                                        //显示退料按钮
                                        document.getElementById("return").style.display="";
                                    }

                                    //显示撤销按钮
                                    document.getElementById("revoke").style.display="";
                                    //隐藏归还按钮
                                    document.getElementById("giveBack").style.display = "none";
                                    //解禁单选框

                                    let bfBackBox = document.querySelectorAll(".bfBack");
                                    let backNumbers = document.querySelectorAll(".backNumber");

                                    for (let j = 0; j < backNumbers.length; j++) {
                                        bfBackBox[j].disabled=false;

                                        bfBackBox[j].onchange=function (){
                                            if (bfBackBox[j].checked===true){
                                                backNumbers.disabled=false;
                                                backNumbers[j].style.display="";
                                                backNumbers[j].disabled=false;
                                            }
                                            else {
                                                backNumbers.disabled=false;
                                                backNumbers[j].style.display="none";
                                                backNumbers[j].disabled=true;
                                            }
                                        }
                                    }

                                    //退料
                                    document.getElementById("return").onclick=function (){

                                        let allDatas=[];
                                        let formdata={
                                            id:"",
                                            applicationId:"",
                                            productId:"",
                                            appNumber:"",
                                            actual:"0",
                                            vault:""
                                        }
                                        let yes = true;
                                        let count = 0;
                                        for (let j = 0; j < bfBackBox.length; j++) {
                                            if (bfBackBox[j].checked===true){
                                                count++;
                                                //选中了单选框，查看退货框中是否有信息，以及退货的数量是否大于已经取货的数量
                                                let trim = backNumbers[j].value.trim();
                                                if (trim.length>0 && Number(trim)<=Number(contentData2[j].actual_number) ){
                                                    formdata.productId=contentData2[j].product_id;
                                                    formdata.appNumber=trim;
                                                    formdata.vault = contentData2[j].vault;
                                                    allDatas.push(formdata);
                                                    formdata={
                                                        id:"",
                                                        applicationId:"",
                                                        productId:"",
                                                        appNumber:"",
                                                        actual:"0",
                                                        vault:""
                                                    }
                                                }
                                                else {
                                                    if (trim.length===0){
                                                        alert("有数据未填写完整！")
                                                    }else if ( Number(trim)>Number(contentData2[j].actual_number)){
                                                        alert("数据不能大于实际数量！")
                                                    }
                                                    yes = false;
                                                    break;
                                                }
                                            }

                                        }
                                        if (yes===true && Number(count) >0){
                                            //判断是出库还是入库
                                            if (Number(data[i].sort)===1){
                                                //是出库单，退料应该是入库
                                                bfProductBack(0,8,allDatas,data[i].id);
                                            }
                                            else if (Number(data[i].sort)===0){
                                                //是入库单。退料应该是出库
                                                bfProductBack(1,10,allDatas,data[i].id);
                                            }

                                        }
                                        else if (Number(count)===0){
                                            alert("还未选择！")
                                        }
                                    }


                                    //检测是否是借用的单子
                                    if (Number(data[i].sortTwo) ===12){
                                        //归还
                                        //显示归还按钮
                                        document.getElementById("giveBack").style.display="";
                                        //隐藏退料按钮
                                        document.getElementById("return").style.display="none";
                                        document.getElementById("giveBack").onclick=function (){

                                            let allDatas=[];
                                            let formdata={
                                                id:"",
                                                applicationId:"",
                                                productId:"",
                                                appNumber:"",
                                                actual:"0",
                                                vault:""
                                            }
                                            let yes = true;
                                            let count = 0;
                                            for (let j = 0; j < bfBackBox.length; j++) {
                                                if (bfBackBox[j].checked===true){
                                                    count++;
                                                    //选中了单选框，查看退货框中是否有信息，以及退货的数量是否大于已经取货的数量
                                                    let trim = backNumbers[j].value.trim();
                                                    if (trim.length>0 && Number(trim)<=Number(contentData2[j].actual_number) ){
                                                        formdata.productId=contentData2[j].product_id;
                                                        formdata.appNumber=trim;
                                                        formdata.vault = contentData2[j].vault;
                                                        allDatas.push(formdata);
                                                        formdata={
                                                            id:"",
                                                            applicationId:"",
                                                            productId:"",
                                                            appNumber:"",
                                                            actual:"0",
                                                            vault:""
                                                        }
                                                    }
                                                    else {
                                                        if (trim.length===0){
                                                            alert("有数据未填写完整！")
                                                        }else if ( Number(trim)>Number(contentData2[j].actual_number)){
                                                            alert("数据不能大于实际数量！")
                                                        }
                                                        yes = false;
                                                        break;
                                                    }
                                                }

                                            }
                                            if (yes===true && Number(count) >0){
                                                bfProductBack(0,13,allDatas,data[i].id);
                                            }
                                            else if (Number(count)===0){
                                                alert("还未选择！")
                                            }
                                        }
                                    }


                                }
                                else {
                                    //不存在已经出入库的物料
                                    document.getElementById("return").style.display="none";
                                    document.getElementById("revoke").style.display = "";
                                    //给撤销按钮绑定撤销方法
                                    document.getElementById("revoke").onclick = function () {

                                        // if (Number(data[i].sortTwo)===8||Number(data[i].sortTwo)===10){
                                        //     alert("这已经是撤销后重新生成的申请单了！")
                                        // }
                                        // else {
                                        if (window.confirm("您确定要撤销该申请单吗？")) {
                                            axios({
                                                method: "post",
                                                url: "applicationForm/updateRevoke",
                                                data: data[i].id
                                            }).then(function (resp) {
                                                if (resp.data === "success") {
                                                    alert("该申请已撤销！");
                                                    location.reload();
                                                } else {
                                                    alert("申请撤销失败，请联系相关人员！")
                                                }
                                            })
                                            return true;
                                        } else {
                                            return false;
                                        }
                                        // }


                                    }
                                }


                            }

                            openSubmit();

                            //判断是否是归还，退货入库，退货出库
                            if (Number(data[i].sortTwo) ===8 || Number(data[i].sortTwo)===10 || Number(data[i].sortTwo)===13||Number(data[i].sortTwo)===9){
                                //设置显示原来申请单单号的地方
                                axios({
                                    method:"post",
                                    url:"relationship/selectOldByNew",
                                    data:data[i].id
                                }).then(function (resp){
                                    document.getElementById("oldOrderNumber").style.display="";
                                    document.getElementById("oldOrderNumber").innerHTML="";
                                    document.getElementById("oldOrderNumber").innerHTML="(原申请单："+resp.data[0].orderNumber+")"
                                })
                            }
                            else {
                                //隐藏原来显示单号的地方
                                document.getElementById("oldOrderNumber").style.display="none";
                                document.getElementById("oldOrderNumber").innerHTML="";
                            }


                            //返回方法
                            document.getElementById("comeback").onclick = function () {
                                document.getElementById("myOrder").style.display = "block";
                                document.getElementById("myOrderContent").style.display = "none";
                                document.getElementById("qzContent").style.display="none";
                            }
                        })
                    }



                    if (data[i].notes){
                        // document.getElementById("notes").style.display="";
                        document.getElementById("checkNotes").value ="备注信息："+data[i].notes;
                    }
                    else {
                        // document.getElementById("notes").style.display="none";
                        document.getElementById("checkNotes").value ="备注信息：无";
                    }
                    document.getElementById("myOrder").style.display = "none";
                    document.getElementById("myOrderContent").style.display = "block";
                    document.getElementById("qzContent").style.display="none";
                    if (Number(data[i].sort) === 0) {
                        document.getElementById("MyRobotBox").style.display="none";
                        //入库
                        document.getElementById("myOrderContentTitle").innerHTML =
                            '  <th  class="applicationSticky">选择</th>\n' +
                            '  <th class="applicationSticky">物料号</th>\n' +
                            '  <th class="applicationSticky">物料名称</th>\n' +
                            '  <th class="applicationSticky">申请数量</th>\n' +
                            '  <th class="applicationSticky">仓库剩余量</th>\n' +
                            '  <th class="applicationSticky">已入库数量</th>'

                        //查看是否是采购入库
                        if (Number(data[i].sortTwo)===9){
                            let title = document.getElementById("myOrderContentTitle");
                            let app = title.querySelectorAll(".applicationSticky");
                            for (let i = 0; i < app.length; i++) {
                                let innerHTML = app[i].innerHTML;
                                if (innerHTML === "仓库剩余量") {
                                    // let s ='<td class="applicationSticky"><input class="rkPrice" value="" type="number" style="width: 50%" onInput="value=value.replace(/^(0+)|[^\\d]+/g,\'\')"></td>'
                                    let s = '<th class="applicationSticky">申请价格(元)</th>'

                                    // 在元素前面插入HTML
                                    app[i].insertAdjacentHTML("beforebegin", s);
                                }
                            }
                            //查询发票是否已经完成
                            await  axios({
                                method:"post",
                                url:"applicationForm/selectInvoiceSign",
                                data:data[i].id
                            }).then(function (resp){
                                if (resp.data===true){
                                    //完成
                                    let elementById = document.getElementById("addFp");
                                    if (elementById){
                                        elementById.remove();
                                    }
                                    let title = document.getElementById("myOrderContentTitle");
                                    let app = title.querySelectorAll(".applicationSticky");
                                    for (let i = 0; i < app.length; i++) {
                                        let innerHTML = app[i].innerHTML;
                                        if (innerHTML === "申请价格(元)") {
                                            // // let s ='<td class="applicationSticky"><input class="rkPrice" value="" type="number" style="width: 50%" onInput="value=value.replace(/^(0+)|[^\\d]+/g,\'\')"></td>'
                                            // let s = '<th class="applicationSticky">申请价格(元)</th>'
                                            //
                                            // // 在元素前面插入HTML
                                            // app[i].insertAdjacentHTML("beforebegin", s);
                                            app[i].remove()
                                        }
                                    }
                                }
                                else {
                                    let add = document.getElementById("addFp");
                                    if (add===null) {
                                        //未完成
                                        let button = '<button style="padding: 5px;margin: auto 2%" id="addFp">新增发票</button>'
                                        document.getElementById("comeback").insertAdjacentHTML("afterend", button);




                                    }


                                }
                            })


                            //查询是否有发票信息
                            await  axios({
                                method:"post",
                                url:"invoice/selectFp",
                                data:data[i].id
                            }).then(function (resp)
                            {
                                let datas=resp.data;
                                if (datas.length>0){
                                    let deleteAllPics = document.getElementById("allPics");
                                    if (deleteAllPics){
                                        deleteAllPics.remove();
                                    }

                                }
                                else {
                                    let deleteAllPics = document.getElementById("allPics");
                                    if (deleteAllPics){
                                        deleteAllPics.remove();
                                    }
                                }

                                //添加一个存放发票信息的框
                                //有发票
                                let allpics = document.getElementById("allPics");
                                if (allpics===null){
                                    let upFp = '<div id="allPics" style="width: 100%;height: auto;display: flex;flex-wrap: wrap"><div id="upFps" style="text-align: center;display: inline-block;margin-left: 10px">\n' +
                                        '        <input type="hidden" name="id"  value="">\n' +
                                        '        <input style="width: 223px;height: 24px;display: none" id="file0" type="file" >\n' +
                                        '        <div class="uploads" style="width: 50px;height: 50px;font-size: 50px;line-height: 50px;border-style: dotted;border-width: 0.5px; cursor: pointer;" title="点击上传图片">+</div>\n' +
                                        '    </div></div>'
                                    document.getElementById("checkNotes").insertAdjacentHTML("afterend",upFp);
                                }

                                let pic=''
                                for (let j = 0; j < datas.length; j++) {
                                    pic+='<div class="responsive">\n' +
                                        '  <div class="img">\n' +
                                        '    <a class="ylPic" style="cursor: pointer">\n' +
                                        '      <img src='+datas[j].invoiceStr+'   title="点击预览发票"   alt="发票">\n' +
                                        '    </a>\n' +
                                        '  </div>\n' +
                                        '</div>'

                                }

                                document.getElementById("upFps").insertAdjacentHTML('afterend',pic);
                                let ylPic = document.querySelectorAll(".ylPic");
                                for (let j = 0; j < ylPic.length; j++) {
                                    ylPic[j].onclick=function (){
                                        let src = ylPic[j].querySelector("img").src;
                                        const img = new Image();
                                        img.src = src;
                                        const newWin = window.open("", "_blank");
                                        newWin.document.write(img.outerHTML);
                                        newWin.document.title = "预览图";
                                        newWin.document.close();

                                    }
                                }

                                //上传图片
                                let uploads = document.querySelectorAll(".uploads");

                                for (let i = 0; i < uploads.length; i++) {
                                    uploads[i].onclick=function (){
                                        document.getElementById('file'+i+'').click();
                                    }

                                }

                                //自动上传
                                for (let i = 0; i < uploads.length; i++) {
                                    document.getElementById('file'+i+'').onchange=async function () {
                                        let files = document.getElementById('file' + i + '').files[0];

                                        const baseResult = await getBase64(files)
                                        let pic='<div class="responsive">\n' +
                                            '  <div class="img">\n' +
                                            '    <a class="ylPicAdd" style="cursor: pointer">\n' +
                                            '      <img src='+baseResult+' alt="发票" title="点击预览发票" >\n' +
                                            '    </a>\n' +
                                            '    <div class="desc" title="点击删除图片" ></div>\n' +
                                            '  </div>\n' +
                                            '</div>'
                                        document.getElementById("upFps").insertAdjacentHTML('afterend',pic);
                                        let ylPic = document.querySelectorAll(".ylPic");
                                        for (let j = 0; j < ylPic.length; j++) {
                                            ylPic[j].onclick=function (){
                                                let src = ylPic[j].querySelector("img").src;
                                                const img = new Image();
                                                img.src = src;
                                                const newWin = window.open("", "_blank");
                                                newWin.document.write(img.outerHTML);
                                                newWin.document.title = "预览图";
                                                newWin.document.close();

                                            }
                                        }
                                        document.getElementById("file"+i+"").value="";
                                        let deletePic = document.querySelectorAll(".desc");
                                        let pics = document.querySelectorAll(".responsive");
                                        for (let j = 0; j < deletePic.length; j++) {
                                            deletePic[j].onclick=function (){
                                                if (window.confirm("确定删除这张图片信息？")){
                                                    pics[j].remove();
                                                    return true;
                                                }
                                                else {
                                                    return false;
                                                }
                                            }
                                        }
                                    }
                                }


                                document.getElementById("addFp").onclick=function (){
                                    let add = document.querySelectorAll(".ylPicAdd");
                                    if (add.length>0){
                                        //获取所有的发票信息
                                        //存在发票

                                        //查询发票价格是否为空
                                        let fpPrice = document.querySelectorAll(".fpPrice");
                                        let a =true;
                                        for (let j = 0; j < fpPrice.length; j++) {
                                            let value = fpPrice[j].value;
                                            if (value.length===0||Number(value)===Number(0)){
                                                a=false;
                                                break;
                                            }
                                        }
                                        if (a===true){
                                            let allData=[];
                                            let rkFormData=[];
                                            //获取修改后的价格
                                            let rkForm={
                                                id:"",
                                                appFormId:data[i].id,
                                                actualPrice:"",
                                                priceSort:1,
                                                productId:"",
                                                vault:"",
                                                rkTime:"",
                                                rkSort:9
                                            }
                                            for (let j = 0; j < contentData3.length; j++) {
                                                rkForm={
                                                    id:"",
                                                    appFormId:data[i].id,
                                                    actualPrice:fpPrice[j].value,
                                                    priceSort:1,
                                                    productId:contentData3[j].product_id,
                                                    vault:contentData3[j].vault,
                                                    rkTime:"",
                                                    rkSort:9
                                                }
                                                rkFormData[j]=rkForm;
                                                rkForm={
                                                    id:"",
                                                    appFormId:data[i].id,
                                                    actualPrice:"",
                                                    priceSort:1,
                                                    productId:"",
                                                    vault:"",
                                                    rkTime:"",
                                                    rkSort:9
                                                }
                                            }
                                            let ylPc = document.querySelectorAll(".ylPicAdd");

                                            let invoiceData=[];
                                            for (let j = 0; j < ylPc.length; j++) {
                                                let formdata={
                                                    id:"",
                                                    appId:data[i].id,
                                                    invoiceStr:ylPc[j].querySelector("img").src,
                                                    status:0
                                                }
                                                invoiceData[j] = formdata;
                                                formdata={
                                                    id:"",
                                                    appId:"",
                                                    invoiceStr:"",
                                                    status:""
                                                }
                                            }


                                            allData[0]=rkFormData;
                                            allData[1]=invoiceData;

                                            axios({
                                                method:"post",
                                                url:"invoice/insertAllInvoice",
                                                data:allData
                                            }).then(function (resp){
                                                if (resp.data==="success"){
                                                    alert("发票信息更新成功！");
                                                    location.reload();
                                                }
                                                else {
                                                    alert("发票信息更新失败，请联系相关人员！")
                                                }
                                            })
                                        }
                                        else {
                                            alert("请将发票价格以正确的格式填写！")
                                        }


                                    }
                                    else {
                                        alert("未检测到新增的发票信息！")
                                    }
                                }
                            })
                        }
                        else {
                            //删除存放发票的框
                            let allPic = document.getElementById("allPics");
                            if (allPic){
                                allPic.remove();
                            }
                            let addFp = document.getElementById("addFp");
                            if (addFp){
                                addFp.remove();
                            }
                        }

                    }
                    else if (Number(data[i].sort) === 1) {
                        //出库
                        //删除存放发票的框
                        let allPic = document.getElementById("allPics");
                        if (allPic){
                            allPic.remove();
                        }
                        let addFp = document.getElementById("addFp");
                        if (addFp){
                            addFp.remove();
                        }
                        document.getElementById("MyRobotBox").style.display="inline-block";
                        if (Number(data[i].robot)===Number(1)){
                            document.getElementById("myRobot").checked=true;
                        }
                        else {
                            document.getElementById("myRobot").checked=false;
                        }
                        //出库
                        document.getElementById("myOrderContentTitle").innerHTML =
                            '  <th class="applicationSticky">选择</th>\n' +
                            '  <th class="applicationSticky">物料号</th>\n' +
                            '  <th class="applicationSticky">物料名称</th>\n' +
                            '  <th class="applicationSticky">申请数量</th>\n' +
                            '  <th class="applicationSticky">仓库剩余量</th>\n' +
                            '  <th class="applicationSticky">已出库数量</th>'
                    }
                    else if (Number(data[i].sort)===2){
                        //采购
                        document.getElementById("myOrderContentTitle").innerHTML =
                            '  <th class="applicationSticky">选择</th>\n' +
                            '  <th class="applicationSticky">物料号</th>\n' +
                            '  <th class="applicationSticky">物料名称</th>\n' +
                            '  <th class="applicationSticky">采购数量</th>\n' +
                            '  <th class="applicationSticky">仓库剩余量</th>\n' +
                            '  <th class="applicationSticky">已入库数量</th>'
                    }
                    else if (Number(data[i].sort)===4){
                        //采购
                        document.getElementById("myOrderContentTitle").innerHTML =
                            '  <th class="applicationSticky">选择</th>\n' +
                            '  <th class="applicationSticky">物料号</th>\n' +
                            '  <th class="applicationSticky">物料名称</th>\n' +
                            '  <th class="applicationSticky">申请数量</th>\n' +
                            '  <th class="applicationSticky">质检数量</th>\n' +
                            '  <th class="applicationSticky">合格数量</th>\n' +
                            '  <th class="applicationSticky">备注</th>\n'+
                            '  <th class="applicationSticky">操作</th>\n'
                    }







                }
            }
            function toDataURL(mime) {
                var canvas = document.getElementById("myCanvas");
                var image = document.getElementById("image");
                image.src = canvas.toDataURL(mime);
            }
            document.querySelector(".qz-window-content-close").onclick=function (){
                document.querySelector(".qzwindow").style.display="none";
            }
            //点击签字
            let qz = document.querySelectorAll(".goqz");
            for (let i = 0; i < qz.length; i++) {
                qz[i].onclick=async function () {
                    document.getElementById("addOrder").style.display="none";
                    document.getElementById("myOrder").style.display="none";
                    document.getElementById("myOrderContent").style.display="none";
                    document.getElementById("qzContent").style.display="block";
                    document.getElementById("qzContentTitle").innerHTML =
                        '  <th class="applicationSticky">物料号</th>\n' +
                        '  <th class="applicationSticky">物料名称</th>\n' +
                        '  <th class="applicationSticky">未签字数量</th>'

                    //查询需要确认签字的物料信息
                    await axios({
                        method:"post",
                        url:"log/selectQzProduct",
                        data:data[i].id
                    }).then(function (resp){
                        let formdata="";
                        let contentData = resp.data;
                        for (let j = 0; j < contentData.length; j++) {
                            formdata+= '  <tr  class=zd bgcolor="white" >\n' +
                                '                    <td class="applicationSticky" ><b>' + contentData[j].material_number + '</b></td>\n' +
                                '                    <td class="applicationSticky" ><b>' + contentData[j].name + '</b></td>\n' +
                                '                    <td class="applicationSticky" >'+contentData[j].log_number+'</td>\n' +
                                '                </tr>'
                        }


                        document.getElementById("qzTableBody").innerHTML = formdata;
                    })


                    document.getElementById("confirmQz").onclick=function (){
                        //弹出对话框填写二级密码
                        let s = prompt("请输入二级密码进行验证！");

                        if (s!==null){
                            //等于null是取消操作
                            if (s.length>0){
                                function two(s){
                                    axios({
                                        method: "post",
                                        url: "user/secondaryPasswordExamine",
                                        data: s
                                    }).then(async function (resp) {
                                        let b = resp.data;
                                        if (b) {

                                            // 将画布导出为PNG格式的图片
                                            let canvas = document.getElementById("myCanvas");

                                            var ctx = canvas.getContext("2d");
                                            const img = new Image();
                                            img.src = '/dataVault/images/secondary.PNG';



                                            axios({
                                                method:"post",
                                                url:"log/updateQz?appFormId="+data[i].id+"",
                                                data:img.src
                                            }).then(function (resp){
                                                let da=resp.data;
                                                if (da==="success"){
                                                    alert("签字成功！");
                                                    location.reload();
                                                }
                                                else {
                                                    alert("签字失败！请联系相关人员！")
                                                }
                                            })
                                        } else {
                                            let s = prompt("二级密码验证失败，请重新输入！");
                                            if (s.length > 0) {
                                                two(s);
                                            }

                                        }
                                    })
                                }
                                two(s);
                            }
                        }
                    }

                    document.getElementById("qzComeback").onclick=function (){
                        document.getElementById("addOrder").style.display="none";
                        document.getElementById("myOrder").style.display="block";
                        document.getElementById("myOrderContent").style.display="none";
                        document.getElementById("qzContent").style.display="none";
                    }









                }
            }


        } else {
            document.getElementById("myTableBody").innerHTML = '<div style="width: 100%;height: auto;text-align: center;color: red">您还未提交任何申请！</div>';
        }
    })
}




document.getElementById("myApplication").onclick=function (){
    //点击我的申请单要先检查是否已经登录
    axios({
        method:"post",
        url:"user/selectLoginId"
    }).then(function (resp){
        if (resp.data==="fail"){
            alert("您尚未登录！")
        }else {
           let userId= resp.data;
            document.querySelector(".window").style.display="block";
            document.getElementById("addOrder").style.display="none";
            document.getElementById("myOrder").style.display="block";
            document.getElementById("myOrderContent").style.display="none";
            document.getElementById("qzContent").style.display="none";

            //第一页
            selectMyApp(1,userId);

            //首页
            document.getElementById("firstPage1").onclick = function () {
                //获取当前页
                let innerHTML = document.getElementById("currentPage1").innerHTML;
                if (Number(innerHTML) === Number(1)) {
                    alert("这已经是第一页了！")
                } else if (Number(innerHTML) === Number(0)) {
                    alert("还未添加任何信息！")
                } else {
                    selectMyApp(1,userId);

                }

            }
            //尾页
            document.getElementById("endPage1").onclick = function () {
                //获取共有几页
                let innerHTML = document.getElementById("pageSum1").innerHTML;
                //获取当前页
                let innerHTML1 = document.getElementById("currentPage1").innerHTML;
                if (Number(innerHTML1) === Number(innerHTML) && Number(innerHTML) !== Number(0)) {
                    alert("这已经是最后一页了！")
                } else if (Number(innerHTML1) === Number(0)) {
                    alert("还未添加任何信息！")
                } else {
                    selectMyApp(innerHTML,userId);

                }

            }
            //上一页
            document.getElementById("lastPage1").onclick = function () {
                //获取当前页
                let innerHTML = document.getElementById("currentPage1").innerHTML;
                if (Number(innerHTML) > 0 && Number(innerHTML) !== 1) {
                    selectMyApp(Number(innerHTML) - 1,userId);
                } else if (Number(innerHTML) === Number(1)) {
                    alert("这已经是首页了!")
                } else if (Number(innerHTML) === Number(0)) {
                    alert("还未添加任何信息！")
                } else {
                    alert("系统错误，请寻找相关人员！")
                }


            }
            //下一页
            document.getElementById("nextPage1").onclick = function () {
                //获取总页数和当前页
                let innerHTML = document.getElementById("currentPage1").innerHTML;
                let innerHTML2 = document.getElementById("pageSum1").innerHTML;
                if (Number(innerHTML) > 0 && Number(innerHTML) < Number(innerHTML2)) {
                    selectMyApp(Number(innerHTML) + Number(1),userId)
                } else if (Number(innerHTML) === Number(innerHTML2) && Number(innerHTML2) !== Number(0)) {
                    alert("这已经是最后一页了！")
                } else if (Number(innerHTML) === Number(0)) {
                    alert("还未添加任何信息！")
                } else {
                    alert("系统错误，请联系相关人员！")
                }
            }

            //输入的页码
            document.getElementById("jump1").onclick = function () {
                //获取输入的页码
                let value = document.getElementById("jumpValue1").value;
                //获取当前页码
                let innerHTML = document.getElementById("currentPage1").innerHTML;
                //获取总页码
                let innerHTML1 = document.getElementById("pageSum1").innerHTML;
                //判断value是否为正整数
                var pattern = /^[1-9]\d*$/;

                let b = pattern.test(value);
                if (b) {
                    //如果是真的，那就符合正整数，然后再比较是否与当前页相同，是否大于总页数
                    if (Number(value) > Number(innerHTML1)) {
                        alert("您输入的数据不符合规则！")
                    } else if (Number(value) === Number(innerHTML)) {
                        alert("您已经处在当前页面了！")
                    } else if (Number(innerHTML) === Number(0)) {
                        alert("还未添加任何信息！")
                    } else {
                        selectMyApp(value,userId);
                    }

                } else {
                    alert("您输入的数据不符合页码规则！")
                }


            }

            //根据日期筛选
            document.getElementById("between").onchange = function (){
                selectMyApp(1,userId);
            }
            document.getElementById("and").onchange =function (){
                selectMyApp(1,userId);
            }
        }
    })


}
//清除所有物品按钮
document.getElementById("deleteAll").onclick=function (){
    if (window.confirm("您确定要清除所有物品吗？")){
        document.getElementById("orderContent").innerHTML="";
        document.getElementById("productNumber").innerHTML=0;
        return true;
    }else {
        return false;
    }
}







//给物料号添加点击方法
document.getElementById("tableBody").onmouseenter=function (){
    //获取该区域内所有的物料号信息
    let elementById = document.getElementById("tableBody");
    let sticky2 = elementById.querySelectorAll(".materialNumber");
    for (let i = 0; i < sticky2.length; i++) {
        sticky2[i].onclick=function (){
            let wdSmallOne = document.querySelector(".window-small-one");
            document.querySelector(".window-small-content-close-one").onclick=function (){
                wdSmallOne.style.display="none";
            }
            wdSmallOne.style.display="block";
            let status = document.querySelector(".shStatus");
            status.innerHTML="";
            //获取物料号
            let wlh = sticky2[i].innerHTML;
            //根据物料号查询该产品的信息
            axios({
                method:"post",
                url:"product/selectByMaterialNumber",
                data:wlh
            }).then(function (resp){
            let productAttribute=resp.data;
            if (productAttribute.length>0){
                let formdata='<div  class="PN">名称:</div>\n' +
                    '            <div CLASS="PNI">\n' +
                    '                <div>'+productAttribute[0].name+'</div>\n' +
                    '            </label></div>\n'+
                    '<div  class="PN">物料号:</div>\n' +
                    '            <div CLASS="PNI">\n' +
                    '                <div>'+productAttribute[0].material_number+'</div>\n' +
                    '            </div>\n'+
                    '<div  class="PN">描述信息:</div>\n' +
                    '            <div CLASS="PNI">\n' +
                    '                <div>'+productAttribute[0].description+'</div>\n' +
                    '            </div>\n'+
                    '<div  class="PN">图片:</div>\n' +
                    '            <div CLASS="PNI">\n' +
                    '                <img class="ylPic" style="width: 60%;margin: auto" src='+productAttribute[0].url+'  alt="图片未加载" title="点击查看大图">\n' +
                    '            </div>\n'


                for (let j = 0; j < productAttribute.length; j++) {
                    formdata+='<div  class="PN">'+productAttribute[j].attributeName+'</div>\n' +
                        '            <div CLASS="PNI">\n' +
                        '                <div>'+productAttribute[j].content+'</div>\n' +
                        '            </div>'
                }
              status.innerHTML=formdata;
                let ylPic = document.querySelectorAll(".ylPic");
                for (let j = 0; j < ylPic.length; j++) {
                    ylPic[j].onclick=function (){
                        let src = ylPic[j].querySelector("img").src;
                        const img = new Image();
                        img.src = src;
                        const newWin = window.open("", "_blank");
                        newWin.document.write(img.outerHTML);
                        newWin.document.title = "预览图";
                        newWin.document.close();

                    }
                }
            }

            })
        }
    }
}



