


//禁用数量框
document.getElementById("number").disabled=true;
document.getElementById("updateNumber").disabled=true;

async function ifLog(productId,vault){
 	let s;
	await axios({
		method:"post",
		url:"log/selectIfLog?vault="+vault+"",
		data:productId
	}).then(function (resp){
		s= resp.data;
	})
	 return s;
}




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

function setTreeData(data, str = 0) {
	return data.filter((item) => {
		if (item.parentId === str) {
			item.child = setTreeData(data, item.id)
			return true
		}
	})
}


let basicname = document.querySelectorAll(".basicName");

let basicname1 = document.querySelectorAll(".basicName1");

axios({
	method:"post",
	url:"basicAttribute/selectAll"
}).then(function (resp){
	let datas=resp.data;
	for (let i = 0; i < basicname.length; i++) {
		basicname[i].innerHTML=datas[i].name;
		basicname1[i].innerHTML=datas[i].name;
	}
})
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

//启用权限

//增改属性信息编码权限
async function addAndUpdateAttributeCodeQx(){
	let promise = userFunction(userId);
	let promiseBoolean=false;
	await promise.then( async function (resp) {

		for (let j = 0; j < resp.length; j++) {
			if (resp[j].function_two_id === 52 && resp[j].open_status === 1) {
				promiseBoolean = true;

				break;
			}
		}

	})
	return promiseBoolean;
}

//彻底删除权限

















function debounce(fn, delay) {
	let time = null;//time用来控制事件的触发
	return function () {
		if (time !== null) {
			clearTimeout(time);
		}
		time = setTimeout(() => {
			fn.call(this);
			//利用call(),让this的指针从指向window 转成指向input
		}, delay)
	}
}




// console.log(generateCode(3,2225,list))











		//显示物料信息



		//寻找该分类下的物料数据;这里的number就是该分类的id，里面的物料的parentId就是number
		axios({
			method:"post",
			url:"finProduct/selectAbandonedFinProduct?currentPage="+1+"",

		}).then(function (resp)
		{
			let data=resp.data.rows;
			let formdata="";
			let totalCounts=resp.data.totalCount;




			if (data.length>0){
				//当前分类下存在物料

				document.getElementById("pageDiv1").style.display="block"
				for (let i = 0; i < data.length; i++) {
					formdata+=' <div style="width: 100%;height: auto;display: flex;text-align: center;">\n' +
						'                                <div style="width: 34.5%;margin: auto; border: white solid 1px"><b>'+data[i].name+'</b></div>\n' +
						'                                <div style="width: 40%;margin: auto  ; border: white solid 1px"><b>'+data[i].materialNumber+'</b></div>\n' +
						'                                <div style="width: 25%;margin: auto;   border: white solid 1px"><a class="updateProduct" href="javascript:void(0)">查看</a>&nbsp;<a class="deleteProduct"  href="javascript:void(0)">删除</a></div>\n' +
						'                            </div>'
				}



				document.getElementById("content").innerHTML=formdata;
				document.getElementById("pageSum1").innerText=Math.ceil(Number(totalCounts)/20);
				document.getElementById("currentPage1").innerText=1;




				function page(pages){
					//调用axios
					axios({
						method:"post",
						url:"finProduct/selectAbandonedFinProduct?currentPage="+pages+"",

					}).then(function (resp)
					{
						let data=resp.data.rows;
						let formdata="";
						for (let i = 0; i < data.length; i++) {
							formdata+=' <div style="width: 100%;height: auto;display: flex;text-align: center;">\n' +
								'                                <div style="width: 34.5%;margin: auto; border: white solid 1px"><b>'+data[i].name+'</b></div>\n' +
								'                                <div style="width: 40%;margin: auto  ; border: white solid 1px"><b>'+data[i].materialNumber+'</b></div>\n' +
								'                                <div style="width: 25%;margin: auto;   border: white solid 1px"><a class="updateProduct" href="javascript:void(0)">修改</a>&nbsp;<a class="deleteProduct"  href="javascript:void(0)">删除</a></div>\n' +
								'                            </div>'
						}

						document.getElementById("currentPage1").innerText=pages;
						document.getElementById("content").innerHTML=formdata;
						//点击修改物料显示窗口
						let updateP = document.querySelectorAll(".updateProduct");
						for (let i = 0; i < updateP.length; i++) 	{
							updateP[i].onclick=function (){


								//隐藏添加数据。显示更细数据
								document.querySelector(".window").style.display="block";
								document.getElementById("addBlock").style.display="none";
								document.getElementById("updateBlock").style.display="block";
								document.getElementById("addAttribute").style.display="none";
								document.getElementById("updateAttribute").style.display="none";
								document.getElementById("updateAttribute2").style.display="none";
								//获取数据进行回显
								axios({
									method:"post",
									url:"finProduct/selectByIdAsProduct",
									data:data[i].id
								}).then(function (resp)
								{
									let datas = resp.data;
									let sortId=datas[0].parentId;
									// //设置数量框的禁用
									// let promise = ifLog(datas[0].id,datas[0].vault);
									// promise.then(function (resp){
									// 	if (resp===true){
									// 		document.getElementById("updateNumber").disabled=true;
									// 	}
									// })


									//自带属性
									document.getElementById("UpdateMaterialNumber").disabled=true;
									document.getElementById("updateName").value = datas[0].name;
									document.getElementById("url1").value = datas[0].url;
									document.getElementById("UpdateMaterialNumber").value = datas[0].materialNumber;
									document.getElementById("updateBrand").value = datas[0].brand;
									document.getElementById("updateDescription").value = datas[0].description;
									document.getElementById("updateNumber").value=datas[0].number;

									document.getElementById("produceId").innerHTML=datas[0].materialNumber.substring(0,7);
									document.getElementById("updateSortNumberA").innerHTML = datas[0].materialNumber.substring(0,7)
									document.getElementById("updateSerialNumber").innerHTML = datas[0].materialNumber.substring(16)

									let s = datas[0].materialNumber.substring(7,16);
									for (let j = 0; j < s.length; j++) {
										let m = Number(j)+Number(1);
										document.getElementById("upmdiv"+m+"").innerHTML=s[j];
									}


									//查询后添加的属性
									axios({
										method:"post",
										url:"finAttribute/selectAbandonedFinAttribute",
										data:data[i].id
									}).then(function (resp){
										let bdatas=resp.data;
										let selectAttributeUpdate="";
										for (let k = 0; k < bdatas.length; k++) {
											if (bdatas[k].unit.length>0){
												selectAttributeUpdate += '    <div style="width:738px ;height: auto;display: flex;">\n' +
													'                    <div  style="width: 100px;height: auto; border-right: 1px solid #d6e0ef;border-bottom: 1px solid #d6e0ef;text-align: center;display: flex">\n' +
													'                        <div style="width: fit-content;height: auto;align-self: center;margin: auto">' + bdatas[k].name + '  <br>(单位:'+bdatas[k].unit+')</div>\n' +
													'                    </div>\n' +
													'                    <div class="qwe" style="width:294px ;height: auto;border-right: 1px solid #d6e0ef;border-bottom: 1px solid #d6e0ef;">\n' +
													'                        &nbsp;<input autocomplete="off" maxlength="80" style="width: 90%;height: 20px;" type="text" name="" id=upInput' + bdatas[k].id + ' value="'+bdatas[k].content+'" disabled> &nbsp;<b id="bb'+bdatas[k].id+'" style="display: none">*</b>\n' +
													' <div id="inputUpValue'+bdatas[k].id+'" style="display: none;position: absolute;top: 100%;left: 3px; padding: 1px 2px; width:90%;min-width: 200px; height:auto; z-index:9999;overflow: hidden;background: #e0e0e0;border-top: none;"> </div>\n'+
													'                    </div>\n' +
													'                    <div  style="width: 50px;height: auto; border-right: 1px solid #d6e0ef;border-bottom: 1px solid #d6e0ef;text-align: center;display: flex">\n' +
													'                        <div style="width: fit-content;height: auto;align-self: center;margin: auto">编码</div>\n' +
													'                    </div>\n' +
													'                    <div class="qwe" style="width:294px ;height: auto;border-right: 1px solid #d6e0ef;border-bottom: 1px solid #d6e0ef;">\n' +
													'                        &nbsp;<input autocomplete="off" maxlength="80" style="width: 90%;height: 20px;" type="text" name="" id=upCode' + bdatas[k].id + ' value="" disabled > &nbsp;\n' +
													'                    </div>\n' +
													'                </div>'
											}else {
												selectAttributeUpdate += '    <div style="width:738px ;height: auto;display: flex;">\n' +
													'                    <div  style="width: 100px;height: auto; border-right: 1px solid #d6e0ef;border-bottom: 1px solid #d6e0ef;text-align: center;display: flex">\n' +
													'                        <div style="width: fit-content;height: auto;align-self: center;margin: auto">' + bdatas[k].name + '</div>\n' +
													'                    </div>\n' +
													'                    <div class="qwe" style="width:294px ;height: auto;border-right: 1px solid #d6e0ef;border-bottom: 1px solid #d6e0ef;">\n' +
													'                        &nbsp;<input autocomplete="off" maxlength="80" style="width: 90%;height: 20px;" type="text" name="" id=upInput' + bdatas[k].id + ' value="'+bdatas[k].content+'" disabled> &nbsp;<b id="bb'+bdatas[k].id+'" style="display: none">*</b>\n' +
													' <div  id="inputUpValue'+bdatas[k].id+'" style="display: none;position: absolute;top: 100%;left: 3px; padding: 1px 2px; width:90%;min-width: 200px; height:auto; z-index:9999;overflow: hidden;background: #e0e0e0;border-top: none;"> </div>\n'+
													'                    </div>\n' +
													'                    <div  style="width: 50px;height: auto; border-right: 1px solid #d6e0ef;border-bottom: 1px solid #d6e0ef;text-align: center;display: flex">\n' +
													'                        <div style="width: fit-content;height: auto;align-self: center;margin: auto">编码</div>\n' +
													'                    </div>\n' +
													'                    <div class="qwe" style="width:294px ;height: auto;border-right: 1px solid #d6e0ef;border-bottom: 1px solid #d6e0ef;">\n' +
													'                        &nbsp;<input autocomplete="off" maxlength="80" style="width: 90%;height: 20px;" type="text" name="" id=upCode' + bdatas[k].id + ' value="" disabled> &nbsp;\n' +
													'                    </div>\n' +
													'                </div>'
											}

										}
										document.getElementById("selectAttributeUpdate").innerHTML=selectAttributeUpdate;
									})











								})

								//恢复使用
								document.getElementById("updateSubmit").onclick=function (){
									let promise = userFunction(userId);
									let promiseBoolean=false;
									promise.then(function (resp){
										for (let j = 0; j < resp.length; j++) {
											if (resp[j].function_two_id===45 && resp[j].open_status===1){
												promiseBoolean=true;
												break;
											}
										}

										if (promiseBoolean===true){
											if (window.confirm("您确定要重新启用当前物料吗？")){
												axios({
													method:"post",
													url:"finProduct/enableFinProduct",
													data:data[i].id
												}).then(function (resp){
													if (resp.data==="success"){
														alert("启用成功！");
														//重置信息进行显示修改后的信息
														page(pages);



													}else {
														alert("启用失败，请联系相关人员！");
													}
												})
												return true;
											}else {
												return false;
											}
										}
										else {
											alert("您暂未获取启用的权限！")
										}

									})
								}

							}
						}

						//删除物料信息
						let deleteP = document.querySelectorAll(".deleteProduct");
						for (let i = 0; i < deleteP.length; i++) {
							deleteP[i].onclick=function (){
								let promise = userFunction(userId);
								let promiseBoolean=false;
								promise.then(function (resp){
									for (let j = 0; j < resp.length; j++) {
										if (resp[j].function_two_id===46 && resp[j].open_status===1){
											promiseBoolean=true;
											break;
										}
									}

									if (promiseBoolean===true){
										if (window.confirm("删除后将无法恢复，您确定要继续吗？")){
											axios({
												method:"post",
												url:"finProduct/deleteReally",
												data:data[i].id
											}).then(function (resp){
												if (resp.data==="success"){
													alert("删除成功！");
													//重置信息进行显示修改后的信息
													page(pages);

												}else {
													alert("删除失败，请联系相关人员！");
												}
											})
											return true;
										}else {
											return false;
										}
									}
									else {
										alert("您暂未获得删除权限！")
									}

								})




							}
						}
					})
				}
				//下一页
				document.getElementById("nextPage1").onclick=function (){
					//获取当前页
					let currentPage = document.getElementById("currentPage1").innerText;
					//获取总页数
					let pageSum = document.getElementById("pageSum1").innerText;
					//比较当前页是否为最后一页
					if (0<Number(currentPage)&&Number(currentPage)<Number(pageSum)){
						let nextPage=Number(currentPage)+1;
						//调用方法
						page(nextPage);

					}else if (Number(currentPage)===Number(pageSum)){
						alert("这已经是最后一页了！")
					}else {
						alert("系统错误,请联系相关人员！")
					}


				}
				//尾页
				document.getElementById("endPage1").onclick=function (){
					//获取当前页
					let currentPage = document.getElementById("currentPage1").innerText;
					//获取总页数
					let pageSum = document.getElementById("pageSum1").innerText;
					if (0<Number(currentPage)&&Number(currentPage)<Number(pageSum)){
						//调用方法
							page(pageSum);

					}else if (Number(currentPage)===Number(pageSum)){
						alert("这已经是最后一页了！")
					}else {
						alert("系统错误,请联系相关人员！")
					}
				}
				//上一页
				document.getElementById("lastPage1").onclick=function (){
					//获取当前页
					let currentPage = document.getElementById("currentPage1").innerText;
					//获取总页数
					let pageSum = document.getElementById("pageSum1").innerText;
					if (Number(currentPage)>1&& Number(currentPage)<=Number(pageSum)){
						let lastPage=currentPage-1;

						//调用方法
						page(lastPage);
					}else if (Number(currentPage)===1){
						alert("这已经是第一页了！")
					}else{
						alert("系统错误,请联系相关人员！")
					}


				}

				//首页
				document.getElementById("firstPage1").onclick=function (){
					//获取当前页
					let currentPage = document.getElementById("currentPage1").innerText;
					if (Number(currentPage)>0&&Number(currentPage)!==1){
						//调用方法
						page(1);
					}else if (Number(currentPage)===1){
						alert("这已经是第一页了！")
					}else {
						alert("系统错误,请联系相关人员！")
					}
				}

				//输入页码跳转
				document.getElementById("jump1").onclick=function (){
					//获取用户输入的数据
					let jump = document.getElementById("jumpValue1").value;
					//获取当前页码
					let currentPage = document.getElementById("currentPage1").innerText;
					//获取总页数
					let pageSum = document.getElementById("pageSum1").innerText;
					if (Number(jump)>0&&Number(jump)<=Number(pageSum)&&Number(jump)!==Number(currentPage)){
						//调用方法
						page(jump);
					}else if (Number(jump)===Number(currentPage)){
						alert("您已经在当前页了！")
					}else if (Number(jump)<=0||Number(jump)>Number(pageSum)){
						alert("您输入的数据不合法！")
					}
				}






				//点击修改物料显示窗口
				let updateP = document.querySelectorAll(".updateProduct");
				for (let i = 0; i < updateP.length; i++) 	{
					updateP[i].onclick=function (){


						//隐藏添加数据。显示更细数据
						document.querySelector(".window").style.display="block";
						document.getElementById("addBlock").style.display="none";
						document.getElementById("updateBlock").style.display="block";
						document.getElementById("addAttribute").style.display="none";
						document.getElementById("updateAttribute").style.display="none";
						document.getElementById("updateAttribute2").style.display="none";
						//获取数据进行回显
						axios({
							method:"post",
							url:"finProduct/selectByIdAsProduct",
							data:data[i].id
						}).then(function (resp)
						{
							let datas = resp.data;




							//自带属性
							document.getElementById("UpdateMaterialNumber").disabled=true;
							document.getElementById("updateName").value = datas[0].name;
							document.getElementById("url1").value = datas[0].url;
							document.getElementById("UpdateMaterialNumber").value = datas[0].materialNumber;
							document.getElementById("updateBrand").value = datas[0].brand;
							document.getElementById("updateDescription").value = datas[0].description;
							document.getElementById("updateNumber").value=datas[0].number;

							document.getElementById("produceId").innerHTML=datas[0].materialNumber.substring(0,7);
							document.getElementById("updateSortNumberA").innerHTML = datas[0].materialNumber.substring(0,7)
							document.getElementById("updateSerialNumber").innerHTML = datas[0].materialNumber.substring(16)

							let s = datas[0].materialNumber.substring(7,16);
							for (let j = 0; j < s.length; j++) {
								let m = Number(j)+Number(1);
								document.getElementById("upmdiv"+m+"").innerHTML=s[j];
							}


							//查询后添加的属性
							axios({
								method:"post",
								url:"finAttribute/selectAbandonedFinAttribute",
								data:data[i].id
							}).then(function (resp){
								let bdatas=resp.data;
								let selectAttributeUpdate="";
								for (let k = 0; k < bdatas.length; k++) {
									if (bdatas[k].unit.length>0){
										selectAttributeUpdate += '    <div style="width:738px ;height: auto;display: flex;">\n' +
											'                    <div  style="width: 100px;height: auto; border-right: 1px solid #d6e0ef;border-bottom: 1px solid #d6e0ef;text-align: center;display: flex">\n' +
											'                        <div style="width: fit-content;height: auto;align-self: center;margin: auto">' + bdatas[k].name + '  <br>(单位:'+bdatas[k].unit+')</div>\n' +
											'                    </div>\n' +
											'                    <div class="qwe" style="width:294px ;height: auto;border-right: 1px solid #d6e0ef;border-bottom: 1px solid #d6e0ef;">\n' +
											'                        &nbsp;<input autocomplete="off" maxlength="80" style="width: 90%;height: 20px;" type="text" name="" id=upInput' + bdatas[k].id + ' value="'+bdatas[k].content+'" disabled> &nbsp;<b id="bb'+bdatas[k].id+'" style="display: none">*</b>\n' +
											' <div id="inputUpValue'+bdatas[k].id+'" style="display: none;position: absolute;top: 100%;left: 3px; padding: 1px 2px; width:90%;min-width: 200px; height:auto; z-index:9999;overflow: hidden;background: #e0e0e0;border-top: none;"> </div>\n'+
											'                    </div>\n' +
											'                    <div  style="width: 50px;height: auto; border-right: 1px solid #d6e0ef;border-bottom: 1px solid #d6e0ef;text-align: center;display: flex">\n' +
											'                        <div style="width: fit-content;height: auto;align-self: center;margin: auto">编码</div>\n' +
											'                    </div>\n' +
											'                    <div class="qwe" style="width:294px ;height: auto;border-right: 1px solid #d6e0ef;border-bottom: 1px solid #d6e0ef;">\n' +
											'                        &nbsp;<input autocomplete="off" maxlength="80" style="width: 90%;height: 20px;" type="text" name="" id=upCode' + bdatas[k].id + ' value="" disabled > &nbsp;\n' +
											'                    </div>\n' +
											'                </div>'
									}else {
										selectAttributeUpdate += '    <div style="width:738px ;height: auto;display: flex;">\n' +
											'                    <div  style="width: 100px;height: auto; border-right: 1px solid #d6e0ef;border-bottom: 1px solid #d6e0ef;text-align: center;display: flex">\n' +
											'                        <div style="width: fit-content;height: auto;align-self: center;margin: auto">' + bdatas[k].name + '</div>\n' +
											'                    </div>\n' +
											'                    <div class="qwe" style="width:294px ;height: auto;border-right: 1px solid #d6e0ef;border-bottom: 1px solid #d6e0ef;">\n' +
											'                        &nbsp;<input autocomplete="off" maxlength="80" style="width: 90%;height: 20px;" type="text" name="" id=upInput' + bdatas[k].id + ' value="'+bdatas[k].content+'" disabled> &nbsp;<b id="bb'+bdatas[k].id+'" style="display: none">*</b>\n' +
											' <div  id="inputUpValue'+bdatas[k].id+'" style="display: none;position: absolute;top: 100%;left: 3px; padding: 1px 2px; width:90%;min-width: 200px; height:auto; z-index:9999;overflow: hidden;background: #e0e0e0;border-top: none;"> </div>\n'+
											'                    </div>\n' +
											'                    <div  style="width: 50px;height: auto; border-right: 1px solid #d6e0ef;border-bottom: 1px solid #d6e0ef;text-align: center;display: flex">\n' +
											'                        <div style="width: fit-content;height: auto;align-self: center;margin: auto">编码</div>\n' +
											'                    </div>\n' +
											'                    <div class="qwe" style="width:294px ;height: auto;border-right: 1px solid #d6e0ef;border-bottom: 1px solid #d6e0ef;">\n' +
											'                        &nbsp;<input autocomplete="off" maxlength="80" style="width: 90%;height: 20px;" type="text" name="" id=upCode' + bdatas[k].id + ' value="" disabled> &nbsp;\n' +
											'                    </div>\n' +
											'                </div>'
									}

								}
								document.getElementById("selectAttributeUpdate").innerHTML=selectAttributeUpdate;
							})











						})

						//恢复使用
						document.getElementById("updateSubmit").onclick=function (){
							let promise = userFunction(userId);
							let promiseBoolean=false;
							promise.then(function (resp){
								for (let j = 0; j < resp.length; j++) {
									if (resp[j].function_two_id===45 && resp[j].open_status===1){
										promiseBoolean=true;
										break;
									}
								}

								if (promiseBoolean===true){
									if (window.confirm("您确定要重新启用当前物料吗？")){
										axios({
											method:"post",
											url:"finProduct/enableFinProduct",
											data:data[i].id
										}).then(function (resp){
											if (resp.data==="success"){
												alert("启用成功！");
												//重置信息进行显示修改后的信息
												window.location.reload();

											}else {
												alert("启用失败，请联系相关人员！");
											}
										})
										return true;
									}else {
										return false;
									}
								}
								else {
									alert("您暂未获取启用的权限！")
								}

							})
						}

					}
				}

				//删除物料信息
				let deleteP = document.querySelectorAll(".deleteProduct");
				for (let i = 0; i < deleteP.length; i++) {
					deleteP[i].onclick=function (){
						let promise = userFunction(userId);
						let promiseBoolean=false;
						promise.then(function (resp){
							for (let j = 0; j < resp.length; j++) {
								if (resp[j].function_two_id===46 && resp[j].open_status===1){
									promiseBoolean=true;
									break;
								}
							}

							if (promiseBoolean===true){
								if (window.confirm("删除后将无法恢复，您确定要继续吗？")){
									axios({
										method:"post",
										url:"finProduct/deleteReally",
										data:data[i].id
									}).then(function (resp){
										if (resp.data==="success"){
											alert("删除成功！");
											//重置信息进行显示修改后的信息
											let currentPage = document.getElementById("currentPage1").innerText;
											page(currentPage)

										}else {
											alert("删除失败，请联系相关人员！");
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



			}
			else {
				//当前分类下没有物料
				document.getElementById("pageDiv1").style.display="none"
				document.getElementById("content").innerHTML='<div style="width: 100%;text-align: center;color: red"><b>此分类中没有任何物料！</b></div>';




			}
		})

		//搜索功能
//模糊查询一级弃用的物料号或者物料名称
		document.getElementById("search").onclick=function (){
			//获取搜索框中的数据
			let value = document.getElementById("searchContent").value;
			if (value.length>0){
				function  searchRe(){

					axios({
						method:"post",
						url:"finProduct/searchAbandoned",
						data:value
					}).then(function (resp){
						let data=resp.data;
						let formdata="";
						if (data.length>0){
							for (let i = 0; i < data.length; i++) {
								formdata+=' <div style="width: 100%;height: auto;display: flex;text-align: center;">\n' +
									'                                <div style="width: 34.5%;margin: auto; border: white solid 1px"><b>'+data[i].name+'</b></div>\n' +
									'                                <div style="width: 40%;margin: auto  ; border: white solid 1px"><b>'+data[i].materialNumber+'</b></div>\n' +
									'                                <div style="width: 25%;margin: auto;   border: white solid 1px"><a class="updateProduct" href="javascript:void(0)">查看</a>&nbsp;<a class="deleteProduct"  href="javascript:void(0)">删除</a></div>\n' +
									'                            </div>'
							}

						}
						else {
							formdata='<div style="width: 100%;height: auto;color: red;text-align: center">暂未查询到相关内容！</div>'
						}
						document.getElementById("content").innerHTML=formdata;
						document.getElementById("pageDiv1").style.display="none"






						//点击修改物料显示窗口
						let updateP = document.querySelectorAll(".updateProduct");
						for (let i = 0; i < updateP.length; i++) 	{
							updateP[i].onclick=function (){


								//隐藏添加数据。显示更细数据
								document.querySelector(".window").style.display="block";
								document.getElementById("addBlock").style.display="none";
								document.getElementById("updateBlock").style.display="block";
								document.getElementById("addAttribute").style.display="none";
								document.getElementById("updateAttribute").style.display="none";
								document.getElementById("updateAttribute2").style.display="none";
								//获取数据进行回显
								axios({
									method:"post",
									url:"finProduct/selectByIdAsProduct",
									data:data[i].id
								}).then(function (resp)
								{
									let datas = resp.data;




									//自带属性
									document.getElementById("UpdateMaterialNumber").disabled=true;
									document.getElementById("updateName").value = datas[0].name;
									document.getElementById("url1").value = datas[0].url;
									document.getElementById("UpdateMaterialNumber").value = datas[0].materialNumber;
									document.getElementById("updateBrand").value = datas[0].brand;
									document.getElementById("updateDescription").value = datas[0].description;
									document.getElementById("updateNumber").value=datas[0].number;

									document.getElementById("produceId").innerHTML=datas[0].materialNumber.substring(0,7);
									document.getElementById("updateSortNumberA").innerHTML = datas[0].materialNumber.substring(0,7)
									document.getElementById("updateSerialNumber").innerHTML = datas[0].materialNumber.substring(16)

									let s = datas[0].materialNumber.substring(7,16);
									for (let j = 0; j < s.length; j++) {
										let m = Number(j)+Number(1);
										document.getElementById("upmdiv"+m+"").innerHTML=s[j];
									}


									//查询后添加的属性
									axios({
										method:"post",
										url:"finAttribute/selectAbandonedFinAttribute",
										data:data[i].id
									}).then(function (resp){
										let bdatas=resp.data;
										let selectAttributeUpdate="";
										for (let k = 0; k < bdatas.length; k++) {
											if (bdatas[k].unit.length>0){
												selectAttributeUpdate += '    <div style="width:738px ;height: auto;display: flex;">\n' +
													'                    <div  style="width: 100px;height: auto; border-right: 1px solid #d6e0ef;border-bottom: 1px solid #d6e0ef;text-align: center;display: flex">\n' +
													'                        <div style="width: fit-content;height: auto;align-self: center;margin: auto">' + bdatas[k].name + '  <br>(单位:'+bdatas[k].unit+')</div>\n' +
													'                    </div>\n' +
													'                    <div class="qwe" style="width:294px ;height: auto;border-right: 1px solid #d6e0ef;border-bottom: 1px solid #d6e0ef;">\n' +
													'                        &nbsp;<input autocomplete="off" maxlength="80" style="width: 90%;height: 20px;" type="text" name="" id=upInput' + bdatas[k].id + ' value="'+bdatas[k].content+'" disabled> &nbsp;<b id="bb'+bdatas[k].id+'" style="display: none">*</b>\n' +
													' <div id="inputUpValue'+bdatas[k].id+'" style="display: none;position: absolute;top: 100%;left: 3px; padding: 1px 2px; width:90%;min-width: 200px; height:auto; z-index:9999;overflow: hidden;background: #e0e0e0;border-top: none;"> </div>\n'+
													'                    </div>\n' +
													'                    <div  style="width: 50px;height: auto; border-right: 1px solid #d6e0ef;border-bottom: 1px solid #d6e0ef;text-align: center;display: flex">\n' +
													'                        <div style="width: fit-content;height: auto;align-self: center;margin: auto">编码</div>\n' +
													'                    </div>\n' +
													'                    <div class="qwe" style="width:294px ;height: auto;border-right: 1px solid #d6e0ef;border-bottom: 1px solid #d6e0ef;">\n' +
													'                        &nbsp;<input autocomplete="off" maxlength="80" style="width: 90%;height: 20px;" type="text" name="" id=upCode' + bdatas[k].id + ' value="" disabled > &nbsp;\n' +
													'                    </div>\n' +
													'                </div>'
											}else {
												selectAttributeUpdate += '    <div style="width:738px ;height: auto;display: flex;">\n' +
													'                    <div  style="width: 100px;height: auto; border-right: 1px solid #d6e0ef;border-bottom: 1px solid #d6e0ef;text-align: center;display: flex">\n' +
													'                        <div style="width: fit-content;height: auto;align-self: center;margin: auto">' + bdatas[k].name + '</div>\n' +
													'                    </div>\n' +
													'                    <div class="qwe" style="width:294px ;height: auto;border-right: 1px solid #d6e0ef;border-bottom: 1px solid #d6e0ef;">\n' +
													'                        &nbsp;<input autocomplete="off" maxlength="80" style="width: 90%;height: 20px;" type="text" name="" id=upInput' + bdatas[k].id + ' value="'+bdatas[k].content+'" disabled> &nbsp;<b id="bb'+bdatas[k].id+'" style="display: none">*</b>\n' +
													' <div  id="inputUpValue'+bdatas[k].id+'" style="display: none;position: absolute;top: 100%;left: 3px; padding: 1px 2px; width:90%;min-width: 200px; height:auto; z-index:9999;overflow: hidden;background: #e0e0e0;border-top: none;"> </div>\n'+
													'                    </div>\n' +
													'                    <div  style="width: 50px;height: auto; border-right: 1px solid #d6e0ef;border-bottom: 1px solid #d6e0ef;text-align: center;display: flex">\n' +
													'                        <div style="width: fit-content;height: auto;align-self: center;margin: auto">编码</div>\n' +
													'                    </div>\n' +
													'                    <div class="qwe" style="width:294px ;height: auto;border-right: 1px solid #d6e0ef;border-bottom: 1px solid #d6e0ef;">\n' +
													'                        &nbsp;<input autocomplete="off" maxlength="80" style="width: 90%;height: 20px;" type="text" name="" id=upCode' + bdatas[k].id + ' value="" disabled> &nbsp;\n' +
													'                    </div>\n' +
													'                </div>'
											}

										}
										document.getElementById("selectAttributeUpdate").innerHTML=selectAttributeUpdate;
									})











								})

								//恢复使用
								document.getElementById("updateSubmit").onclick=function (){
									let promise = userFunction(userId);
									let promiseBoolean=false;
									promise.then(function (resp){
										for (let j = 0; j < resp.length; j++) {
											if (resp[j].function_two_id===45 && resp[j].open_status===1){
												promiseBoolean=true;
												break;
											}
										}

										if (promiseBoolean===true){
											if (window.confirm("您确定要重新启用当前物料吗？")){
												axios({
													method:"post",
													url:"finProduct/enableFinProduct",
													data:data[i].id
												}).then(function (resp){
													if (resp.data==="success"){
														alert("启用成功！");
														//重置信息进行显示修改后的信息
														window.location.reload();

													}else {
														alert("启用失败，请联系相关人员！");
													}
												})
												return true;
											}else {
												return false;
											}
										}
										else {
											alert("您暂未获取启用的权限！")
										}

									})
								}

							}
						}

						//删除物料信息
						let deleteP = document.querySelectorAll(".deleteProduct");
						for (let i = 0; i < deleteP.length; i++) {
							deleteP[i].onclick=function (){
								let promise = userFunction(userId);
								let promiseBoolean=false;
								promise.then(function (resp){
									for (let j = 0; j < resp.length; j++) {
										if (resp[j].function_two_id===46 && resp[j].open_status===1){
											promiseBoolean=true;
											break;
										}
									}

									if (promiseBoolean===true){
										if (window.confirm("删除后将无法恢复，您确定要继续吗？")){
											axios({
												method:"post",
												url:"finProduct/deleteReally",
												data:data[i].id
											}).then(function (resp){
												if (resp.data==="success"){
													alert("删除成功！");
													//重置信息进行显示修改后的信息
													searchRe();

												}else {
													alert("删除失败，请联系相关人员！");
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
					})
				}

				searchRe();

			}
			else {
				alert("请输入搜索内容！")
			}
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













