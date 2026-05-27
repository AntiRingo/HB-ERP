
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

//点击查看物料信息后显示图片信息相关功能
function images(datas,i){
	//删除所有的图片信息
	let res = document.querySelectorAll(".responsive");
	for ( let j = 0; j < res.length; j++) {
		res[j].remove();
	}
	if (datas[0].url.length>0){

		let pic='<div class="responsive">\n' +
			'  <div class="img" style="display: flex">\n' +
			'    <a class="ylPic" style="cursor: pointer">\n' +
			'      <img style="width: 50px;height: 33px" src='+datas[0].url+' alt="图片未加载" title="点击预览图片" >\n' +
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



//添加时查询唯一标志位的数据是否重复
 async function uniqueAdd(nameId,uniqueValue){
	//nameId:属性id;uniqueValue:要添加的唯一标志位数据
	let formdata={
		parentId:nameId,
		content:uniqueValue
	}
	let s=false;
	await axios({
		method:"post",
		url:"attribute/selectUniqueContent",
		data:formdata

	}).then(function (resp){
		if (resp.data===true){
			s=resp.data;

		}
	})

	return s;

}

//添加时查询唯一标志位的数据是否重复
async function uniqueUpdate(nameId,uniqueValue,productId){
	//nameId:属性id;uniqueValue:要添加的唯一标志位数据
	let formdata={
		parentId:nameId,
		content:uniqueValue,
		productId:productId
	}
	let s=false;
	await axios({
		method:"post",
		url:"attribute/selectUniqueContentUpdate",
		data:formdata

	}).then(function (resp){
		if (resp.data===true){
			s=resp.data;

		}
	})

	return s;

}

//在映射中的流水码功能
async function serialCode(arr,sortId){


	var content={
		id:"",
		parentId:"",
		productId:"",
		content:"",
	}
	let list=[];
	let k=0;

	for (let i = 0; i < arr.length; i++) {
		//根据attNameId查询function
		await axios({
			method:"post",
			url:"attributeFunction/selectByAttNameId",
			data:arr[i].attNameId
		}).then(function (resp){
			let datas=resp.data;
			if (datas.length>0){
				if (datas[0].serialCode!==1){
					content.parentId=arr[i].attNameId;
					content.content=document.getElementById("input"+arr[i].attNameId+"").value.trim();
					list[k]=content;
					k++;
					content={
						id:"",
						parentId:"",
						productId:"",
						content:"",
					}
				}

			}
		})


	}
	//得到了映射中除了映射流水码以外的所有content数据，查询有这些数据的物料
	for (let i = 0; i < arr.length; i++) {
		await axios({
			method:"post",
			url:"attributeFunction/selectByAttNameId",
			data:arr[i].attNameId
		}).then(async function (resp){

			let ableCode=resp.data;
			let serialCode = ableCode[0].serialCode;
			if (serialCode===1){
				await axios({
					method:"post",
					url:"attribute/selectPidByContentByPid?sortId="+sortId+"&attNameId="+arr[i].attNameId+"",
					data:list
				}).then(function (resp)
				{
					let attributeContent = resp.data[0];
					let maxContent = attributeContent.content;

					//得到了相同数据的最大id的映射流水码

					if (maxContent===null){
						//不存在，第一个
						//长度

						let length = arr[i].length;
						let str="";
						for (let j = 0; j < length; j++) {
							str+="0"
						}
						let result = parseInt(str,10);
						// 如果你想保留结果中的所有前导零，可以将其转换回字符串并手动处理
						let formattedResult = result.toString().padStart(str.toString().length, '0');

						let beginLocation = arr[i].beginLocation;
						let endLocation = arr[i].endLocation;
						let nineNumber = document.getElementById("nineNumber").innerHTML;
						//替换
						let next = nineNumber.substring(0,beginLocation-1)+formattedResult+nineNumber.substring(endLocation);



						document.getElementById("nineNumber").innerHTML = next ;
						document.getElementById("input"+arr[i].attNameId+"").value=formattedResult;
						document.getElementById("code"+arr[i].attNameId+"").value=formattedResult;
						for (let i = 0; i < 9; i++) {
							let s=Number(i)+Number(1)
							document.getElementById("mdiv"+s+"").innerHTML=next.substring(i,s);
						}
						let sortCode = document.getElementById("produceId").innerHTML;
						document.getElementById("materialNumber").value=sortCode+next+"00";


					}
					else {
						//存在
						//长度

						let length = arr[i].length;
						let str=maxContent;
						// let result = parseInt(str,10)+parseInt("1",10)
						//从数据库中查到了最后添加的就用这个，然后查询重复不重复，如果重复，就用最后两位默认流水码，如果最后两位默认流水码到99，那就得到的数据库中最后的流水码+1
						let result = parseInt(str,10);




						// 如果你想保留结果中的所有前导零，可以将其转换回字符串并手动处理
						let formattedResult = result.toString().padStart(str.toString().length, '0');
						if (formattedResult.length===length){
							let beginLocation = arr[i].beginLocation;
							let endLocation = arr[i].endLocation;
							let nineNumber = document.getElementById("nineNumber").innerHTML;
							//替换
							let next = nineNumber.substring(0,beginLocation-1)+formattedResult+nineNumber.substring(endLocation);

							document.getElementById("nineNumber").innerHTML = next ;
							document.getElementById("input"+arr[i].attNameId+"").value=formattedResult;
							document.getElementById("code"+arr[i].attNameId+"").value=formattedResult;
							for (let i = 0; i < 9; i++) {
								let s=Number(i)+Number(1)
								document.getElementById("mdiv"+s+"").innerHTML=next.substring(i,s);
							}
							let sortCode = document.getElementById("produceId").innerHTML;
							document.getElementById("materialNumber").value=sortCode+next+"00";
						}

					}


				})
			}
		})

	}



}

//在映射中流水码+1
async function serialCodeAddOne(arr,sortId){
	let boolean = true;//返回false就表示映射中的流水码用完了


	var content={
		id:"",
		parentId:"",
		productId:"",
		content:"",
	}
	let lists=[];
	let k=0;

	for (let i = 0; i < arr.length; i++) {
		//根据attNameId查询function
		await axios({
			method:"post",
			url:"attributeFunction/selectByAttNameId",
			data:arr[i].attNameId
		}).then(function (resp){
			let datas=resp.data;
			if (datas.length>0){
				if (datas[0].serialCode!==1){
					content.parentId=arr[i].attNameId;
					content.content=document.getElementById("input"+arr[i].attNameId+"").value.trim();
					lists[k]=content;
					k++;
					content={
						id:"",
						parentId:"",
						productId:"",
						content:"",
					}
				}

			}
		})


	}
	//得到了映射中除了映射流水码以外的所有content数据，查询有这些数据的物料
	for (let i = 0; i < arr.length; i++) {
		await axios({
			method:"post",
			url:"attributeFunction/selectByAttNameId",
			data:arr[i].attNameId
		}).then(async function (resp){

			let ableCode=resp.data;
			let serialCode = ableCode[0].serialCode;
			if (serialCode===1){
				await axios({
					method:"post",
					url:"attribute/selectPidByContentByPid?sortId="+sortId+"&attNameId="+arr[i].attNameId+"",
					data:lists
				}).then(function (resp)
				{
					let attributeContent = resp.data[0];
					let maxContent = attributeContent.content;
					//得到了相同数据的最大id的映射流水码

					//存在
					//长度
					let length = arr[i].length;
					let str=maxContent;
					let result = parseInt(str,10)+parseInt("1",10)
					//从数据库中查到了最后添加的就用这个，然后查询重复不重复，如果重复，就用最后两位默认流水码，如果最后两位默认流水码到99，那就得到的数据库中最后的流水码+1
					// 如果你想保留结果中的所有前导零，可以将其转换回字符串并手动处理
					let formattedResult = result.toString().padStart(str.toString().length, '0');
					if (formattedResult.length===length){
						let beginLocation = arr[i].beginLocation;
						let endLocation = arr[i].endLocation;
						let nineNumber = document.getElementById("nineNumber").innerHTML;
						//替换
						let next = nineNumber.substring(0,beginLocation-1)+formattedResult+nineNumber.substring(endLocation);

						document.getElementById("nineNumber").innerHTML = next ;
						document.getElementById("input"+arr[i].attNameId+"").value=formattedResult;
						document.getElementById("code"+arr[i].attNameId+"").value=formattedResult;
						for (let i = 0; i < 9; i++) {
							let s=Number(i)+Number(1)
							document.getElementById("mdiv"+s+"").innerHTML=next.substring(i,s);
						}
						let sortCode = document.getElementById("produceId").innerHTML;
						document.getElementById("materialNumber").value=sortCode+next+"00";
					}
					else if (formattedResult.length>length){
						//映射中的流水码用完了，要用后两位流水码
						boolean = false;



					}


				})
			}
		})

	}
	return boolean;

}

//验证是否有流水码功能存在
async function ifSerialExist(arr){

	let boolean=false;//如果返回的数true就证明开启了流水码功能
	for (let i = 0; i < arr.length; i++) {
		await axios({
			method:"post",
			url:"attributeFunction/selectByAttNameId",
			data:arr[i].attNameId
		}).then(function (resp){
			let ableCode=resp.data;
			let serialCode = ableCode[0].serialCode;
			if (serialCode===1){
				//开启了流水码功能
				boolean=true;
			}
		})
	}
	return boolean;
}

//验证物料号是否重复
async function ifMaterialNumberExist(arr){
	let boolean = false;
	await axios({
		method:"post",
		url:"product/selectMN",
		data:arr
	}).then(function (resp){
		boolean= resp.data;
	})
	return boolean;
}

//查询该分类下最后一个添加物料的末尾两位流水码
async function selectLastEndSerialCode(sortId){
	let string = "";
	await axios({
		method:"post",
		url:"product/selectMaterialNumber",
		data:sortId
	}).then(function (resp){
		let datas=resp.data;
		string= datas[0].materialNumber;

	})
	return string;
}

//上面是添加时用到的，下面是修改时候用到的

//获取当前映射中的流水码使用到了哪里
async function serialCodeUpdate(arr,sortId,list){
//传递数组，数组是attributeFunction
//查询是否开启了流水码功能

	var content={
		id:"",
		parentId:"",
		productId:"",
		content:"",
	}
	let lists=[];
	let k=0;
	for (let i = 0; i < list.length; i++) {
		await axios({
			method:"post",
			url:"attributeFunction/selectByAttNameId",
			data:list[i].parentId
		}).then(async function (resp)
		{
			let ableCode=resp.data;
			let serialCode = ableCode[0].serialCode;
			for (let j = 0; j < arr.length; j++) {
				if (serialCode!==1 && arr[j].att_name_id===list[i].parentId){
					content.parentId=list[i].parentId;
					content.content=document.getElementById("upInput"+list[i].id+"").value.trim();
					lists[k]=content;
					k++;
					content={
						id:"",
						parentId:"",
						productId:"",
						content:"",
					}
				}
			}


		})
	}


	for (let i = 0; i < arr.length; i++) {
		await axios({
			method:"post",
			url:"attributeFunction/selectByAttNameId",
			data:arr[i].att_name_id
		}).then(async function (resp){

			let ableCode=resp.data;
			let serialCode = ableCode[0].serialCode;
			if (serialCode===1){
				await axios({
					method:"post",
					url:"attribute/selectPidByContentByPid?sortId="+sortId+"&attNameId="+arr[i].att_name_id+"",
					data:lists
				}).then(function (resp)
				{
					let attributeContent = resp.data[0];



					//得到了相同数据的最大id的映射流水码

					if (attributeContent===undefined){
						//不存在，第一个
						//长度

						let length = arr[i].length;
						let str="";
						for (let j = 0; j < length; j++) {
							str+="0"
						}
						let result = parseInt(str,10);
						// 如果你想保留结果中的所有前导零，可以将其转换回字符串并手动处理
						let formattedResult = result.toString().padStart(str.toString().length, '0');

						let beginLocation = arr[i].begin_location;
						let endLocation = arr[i].end_location;
						let nineNumber = document.getElementById("nineNumber").innerHTML;
						//替换
						let next = nineNumber.substring(0,beginLocation-1)+formattedResult+nineNumber.substring(endLocation);



						document.getElementById("nineNumber").innerHTML = next ;
						for (let j = 0; j < list.length; j++) {

							if (list[j].parentId===arr[i].att_name_id){
								document.getElementById("upInput"+list[j].id+"").value=formattedResult;
								document.getElementById("upCode"+list[j].id+"").value=formattedResult;
							}
						}
						for (let i = 0; i < 9; i++) {
							let s=Number(i)+Number(1)
							document.getElementById("upmdiv"+s+"").innerHTML=next.substring(i,s);
						}
						let sortCode = document.getElementById("produceId").innerHTML;
						document.getElementById("UpdateMaterialNumber").value=sortCode+next+"00";



					}
					else {
						//存在
						//长度
						let maxContent = attributeContent.content;
						let length = arr[i].length;
						let str=maxContent;
						// let result = parseInt(str,10)+parseInt("1",10)
						//从数据库中查到了最后添加的就用这个，然后查询重复不重复，如果重复，就用最后两位默认流水码，如果最后两位默认流水码到99，那就得到的数据库中最后的流水码+1
						let result = parseInt(str,10);




						// 如果你想保留结果中的所有前导零，可以将其转换回字符串并手动处理
						let formattedResult = result.toString().padStart(str.toString().length, '0');
						if (formattedResult.length===length){
							let beginLocation = arr[i].begin_location;
							let endLocation = arr[i].end_location;
							let nineNumber = document.getElementById("nineNumber").innerHTML;
							//替换
							let next = nineNumber.substring(0,beginLocation-1)+formattedResult+nineNumber.substring(endLocation);


							document.getElementById("nineNumber").innerHTML = next ;
							for (let j = 0; j < list.length; j++) {

								if (list[j].parentId===arr[i].att_name_id){
									document.getElementById("upInput"+list[j].id+"").value=formattedResult;
									document.getElementById("upCode"+list[j].id+"").value=formattedResult;
								}
							}
							for (let i = 0; i < 9; i++) {
								let s=Number(i)+Number(1)
								document.getElementById("upmdiv"+s+"").innerHTML=next.substring(i,s);
							}
							let sortCode = document.getElementById("produceId").innerHTML;
							document.getElementById("UpdateMaterialNumber").value=sortCode+next+"00";
						}







					}



				})
			}
		})

	}

}


//在映射中流水码+1
async function serialCodeAddOneUpdate(arr,sortId,list){
let boolean = true;//返回false就表示映射中的流水码用完了
//传递数组，数组是attributeFunction
//查询是否开启了流水码功能

	var content={
		id:"",
		parentId:"",
		productId:"",
		content:"",
	}
	let lists=[];
	let k=0;
for (let i = 0; i < list.length; i++) {
	await axios({
		method:"post",
		url:"attributeFunction/selectByAttNameId",
		data:list[i].parentId
	}).then(async function (resp)
	{
		let ableCode=resp.data;
		let serialCode = ableCode[0].serialCode;
		for (let j = 0; j < arr.length; j++) {
			if (serialCode!==1 && arr[j].att_name_id===list[i].parentId){
				content.parentId=list[i].parentId;
				content.content=document.getElementById("upInput"+list[i].id+"").value.trim();
				lists[k]=content;
				k++;
				content={
					id:"",
					parentId:"",
					productId:"",
					content:"",
				}
			}
		}


	})
}


	for (let i = 0; i < arr.length; i++) {
		await axios({
			method:"post",
			url:"attributeFunction/selectByAttNameId",
			data:arr[i].att_name_id
		}).then(async function (resp){

			let ableCode=resp.data;
			let serialCode = ableCode[0].serialCode;
			if (serialCode===1){
				await axios({
					method:"post",
					url:"attribute/selectPidByContentByPid?sortId="+sortId+"&attNameId="+arr[i].att_name_id+"",
					data:lists
				}).then(function (resp)
				{
					let attributeContent = resp.data[0];
					let maxContent = attributeContent.content;


					//存在
					//长度
					let length = arr[i].length;
					let str=maxContent;
					let result = parseInt(str,10)+parseInt("1",10)
					//从数据库中查到了最后添加的就用这个，然后查询重复不重复，如果重复，就用最后两位默认流水码，如果最后两位默认流水码到99，那就得到的数据库中最后的流水码+1

					// 如果你想保留结果中的所有前导零，可以将其转换回字符串并手动处理
					let formattedResult = result.toString().padStart(str.toString().length, '0');
					if (formattedResult.length===length){
						let beginLocation = arr[i].begin_location;
						let endLocation = arr[i].end_location;
						let nineNumber = document.getElementById("nineNumber").innerHTML;

						//替换
						let next = nineNumber.substring(0,beginLocation-1)+formattedResult+nineNumber.substring(endLocation);

						document.getElementById("nineNumber").innerHTML = next ;
						// console.log(list);
						// console.log(arr)
						for (let j = 0; j < list.length; j++) {

							if (list[j].parentId===arr[i].att_name_id){
								document.getElementById("upInput"+list[j].id+"").value=formattedResult;
								document.getElementById("upCode"+list[j].id+"").value=formattedResult;
							}
						}
						for (let i = 0; i < 9; i++) {
							let s=Number(i)+Number(1)
							document.getElementById("upmdiv"+s+"").innerHTML=next.substring(i,s);
						}

						let sortCode = document.getElementById("produceId").innerHTML;
						document.getElementById("UpdateMaterialNumber").value=sortCode+next+"00";
					}
					else if (formattedResult.length>length){
						//映射中的流水码用完了，要用后两位流水码
						boolean = false;

					}

				})
			}
		})

	}

return boolean;
}

//验证物料号是否重复
async function ifMaterialNumberExistUpdate(arr){
	let boolean = false;
	await axios({
		method:"post",
		url:"product/selectUpMN",
		data:arr
	}).then(function (resp){
		boolean= resp.data;
	})
	return boolean;
}
//验证是否有流水码功能存在
async function ifSerialExistUpdate(arr){

	let boolean=false;//如果返回的数true就证明开启了流水码功能
	for (let i = 0; i < arr.length; i++) {
		await axios({
			method:"post",
			url:"attributeFunction/selectByAttNameId",
			data:arr[i].att_name_id
		}).then(function (resp){
			let ableCode=resp.data;
			let serialCode = ableCode[0].serialCode;
			if (serialCode===1){
				//开启了流水码功能
				boolean=true;
			}
		})
	}
	return boolean;
}






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

function enableProduct(id,number,n){
	let promise = userFunction(userId);
	let promiseBoolean=false;
	promise.then(function (resp){
		for (let j = 0; j < resp.length; j++) {
			if (resp[j].function_two_id===43 && resp[j].open_status===1){
				promiseBoolean=true;
				break;
			}
		}

		if (promiseBoolean===true){
			if (window.confirm("您确定要重新启用当前物料吗？")){
				axios({
					method:"post",
					url:"product/enableProduct",
					data:id
				}).then(function (resp){
					if (resp.data==="success"){
						alert("启用成功！");
						//重置信息进行显示修改后的信息
						select(number,n);
						let wd = document.querySelector(".window");
						wd.style.display="none";
						//输入框设置为空
						let inputs = document.querySelectorAll("#addBlock input");
						for (let i = 0; i < inputs.length; i++) {
							inputs[i].value="";
						}

						document.getElementById("description").value="";
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
// let promise = userFunction(userId);
// promise.then(function (resp){
// 	console.log(resp)
// })

//修改权限
async function updateQx(){
	let promise = userFunction(userId);
	let promiseBoolean=false;
	await promise.then( async function (resp) {

		 for (let j = 0; j < resp.length; j++) {
			if (resp[j].function_two_id === 2 && resp[j].open_status === 1) {
				promiseBoolean = true;

				break;
			}
		}

	})
	return promiseBoolean;
}





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


//更新时判断基础属性值填写是否为空
function updateIfBaseExist(){
	let value = document.getElementById("updateName").value;
	let value1 = document.getElementById("UpdateMaterialNumber").value;
	// let value2 = document.getElementById("updateBrand").value;
	let value3 = document.getElementById("updateDescription").value;
	// let value4 = document.getElementById("updateNumber").value;
	if (value.length===0||value1.length===0||value3.length===0){
		return false;
	}else {
		return true;
	}
}

//单位换算
function power(str){
	let number= str.replace(/[fpnum%KMGT]/,'');
	let power = str.replace(/([-]?[0-9]+[.][0-9]+)|([-]?[0-9]*)/,'');


	if (power.length>0){
		let num;
		if (power==="f"){
			num=0.000000000000001;
		}else if (power==="p"){
			num=0.000000000001;
		}else if (power==="n"){
			num=0.000000001;
		}else if (power==="u"){
			num=0.000001;
		}else if (power==="m"){
			num=0.001;
		}else if (power==="%"){
			num=0.01;
		}else if (power==="K"){
			num=1000;
		}else if (power==="M"){
			num=1000000;
		}else if (power==="G"){
			num=1000000000;
		}else if (power==="T"){
			num=1000000000000;
		}

		return Number(num)*Number(number);
	}else {
		return number
	}

}

//添加物料
function add(number,n){

	document.getElementById("submit").onclick=function (){
		if (window.confirm("您确定要添加这条物料信息吗？")){
			//获取物料数据
			let formdata={
				id:"",
				parentId:number,
				name:"",
				url:"",
				materialNumber:"",
				brand:"",
				description:"",
				unit:"",
				priceUnit:""
				// number:""
			}
			let ylPc = document.querySelector(".ylPic");
			formdata.name = document.getElementById("name").value;
			if (ylPc){
				formdata.url =ylPc.querySelector("img").src;
			}

			formdata.materialNumber=document.getElementById("materialNumber").value;
			formdata.brand=0;
			formdata.description=document.getElementById("description").value;
			formdata.unit = document.getElementById("unit").value;
			//获取筛选框中的内容
			let priceUnitSelect = document.getElementById("priceUnit");
			let priceUnitIndex = priceUnitSelect.selectedIndex;//获取select的索引
			//获取选中的值
			formdata.priceUnit=priceUnitSelect.options[priceUnitIndex].value;

			// formdata.number = document.getElementById("number").value;
			//判断数据是否为空
			let value = document.getElementById("name").value;
			let value2 = document.getElementById("materialNumber").value;
			// let value3 = document.getElementById("brand").value;
			let value4 = document.getElementById("description").value;
			// let value5 = document.getElementById("number").value;
			//查询映射中的属性编码是否为空

			if (value.length===0||value2.length===0||value4.length===0){
				alert("数据未填写完整！")

			}
			else {



				//提交
				function submit(){

					axios({
						method:"post",
						url:"product/add",
						data:formdata
					}).then(function (resp){
						//返回刚才添加的物料的id作为attributecontent表中的productid
						let productid=resp.data;
						//判断有没有后添加的属性
						let innerHTML = document.getElementById("selectAttributeAdd").innerHTML;
						if (innerHTML!==""){
							if(productid){
								//失败时，删除添加成功的数据数据
								function deleteFailProduct(){
									axios({
										method:"post",
										url:"product/deleteReally",
										data:productid
									}).then(function (resp){
										if (resp.data==="success"){
											alert("添加失败，请联系相关人员！")
										}else {
											alert("系统错误，请联系相关人员！")
										}
									})
								}
								var datass=[];
								let m;
								async function belong(number1,m){

									if (m>0){
										//获取当前分类属于哪些大类里
										axios({
											method:"post",
											url:"sort/selectOfSort",
											data:number1
										}).then(function (resp){
											var datas = resp.data;
											datass=datas.concat(datass);
											m--;
											belong(datas[0].parentId,m)
										})

									}else if (m===0) {

										//查询公共属性分类
										axios({
											method:"post",
											url:"sort/selectId0"
										}).then(async function (resp){
											let ggdata=resp.data;
											datass=ggdata.concat(datass);
											var list;
											var sdata=[];

											for (let i = 0; i < datass.length; i++) {


												await	axios({
													method:"post",
													url:"attribute/selectAttributeName",
													data:datass[i].id
												}).then( async function (resp){
													let datas=resp.data;

													if (datas.length>0){
														//获取所有数据
														sdata=datas.concat(sdata);
														// console.log(list)

													}


												})
											}

											//解析所有数据
											// for (let i = 0; i < sdata.length; i++) {

											if (sdata.length>0){
												for (let j = 0; j < sdata.length; j++) {
													var content={
														id:"",
														parentId:"",
														productId:productid,
														content:"",
													}
													list=[sdata.length]
													for (let i = 0; i < sdata.length; i++) {
														content.parentId=sdata[i].id;
														content.content=document.getElementById("input"+sdata[i].id+"").value.trim();
														list[i]=content;
														content={
															id:"",
															parentId:"",
															productId:productid,
															content:"",
														}
													}

												}
												//判断在映射中的数据不为空
												let exist=true;
												//获取当前分类映射中的属性信息
												axios({
													method:"post",
													url:"mapping/selectBySortId",
													data:number
												}).then(function (resp){
													let mappingdatas=resp.data;
													for (let i = 0; i < list.length; i++) {
														for (let j = 0; j < mappingdatas.length; j++) {
															if (Number(mappingdatas[j].attNameId)===Number(list[i].parentId)){

																//相等证明是在映射中的
																if (list[i].content.length===0||document.getElementById("code"+mappingdatas[j].attNameId+"").value.length===0){
																	exist=false;

																}
															}
															if (exist===false){

																break;
															}
														}
														if (exist===false){
															break;
														}

													}
													if (exist===true){

														//数据不为空，list是后来添加的属性的数据。先判断哪些开启了自动编码功能，然后再判断是否符合值是数字和英文的组合

														axios({
															method:"post",
															url:"attributeFunction/selectAutoCode",
														}).then(async function (resp){
															let functionDatas=resp.data;
															let autoLength=0;

															//判断b,如果是true则可以进行添加编码的操作，如果不是则不进行添加操作

															await axios({
																method:"post",
																url:"attribute/addAttributeContent",
																data:list,
															}).then(async function (resp){

																if (resp.data==="success"){
																	let codeList=[autoLength];
																	let codeDatas={
																		id:"",
																		attNameId:"",
																		code:"",
																		attValue:""
																	}
																	let ccc=true;
																	let nums=0;
																	// console.log(list)
																	// console.log(functionDatas)

																	for (let i = 0; i < list.length; i++) {

																		for (let j = 0; j < functionDatas.length; j++) {
																			if (Number(list[i].parentId)===Number(functionDatas[j].att_name_id)){

																				//获取编码的长度
																				let content1 = list[i].content;
																				let length=functionDatas[j].length;
																				// console.log("输入的长度："+content1.length)
																				// console.log("规定的编码长度"+length)
																				//判断两个的长度

																				if (content1.length>0){

																					codeDatas.attNameId=list[i].parentId;
																					codeDatas.code=document.getElementById("code"+list[i].parentId+"").value;
																					codeDatas.attValue=list[i].content;
																					//查询是否在当前分类的映射中
																					await axios({
																						method:"post",
																						url:"mapping/selectBySortId",
																						data:number
																					}).then(async function (resp){
																						let map=resp.data;
																						for (let k = 0; k < map.length; k++) {
																							let attNameId = map[k].attNameId;
																							if (Number(attNameId)===Number(codeDatas.attNameId)){
																								//判断属性值是否重复
																								await axios({
																									method:"post",
																									url:"attributeValue/selectCode",
																									data:codeDatas
																								}).then(function (resp){
																									if (resp.data.length===0){

																										codeList[nums]=codeDatas;
																										// console.log(codeList[nums])
																										nums++;
																										codeDatas={
																											id:"",
																											attNameId:"",
																											code:"",
																											attValue:""
																										}
																									}
																								})
																							}
																						}
																					})



																				}

																			}
																			if (ccc===false){
																				break;
																			}
																		}

																	}


																	//判断ccc
																	// console.log(ccc)
																	if (ccc===true){
																		let ddd=true;
																		//添加编码


																		if (codeList[0]!==0){


																			//判断是否有编码值为空
																			for (let i = 0; i < codeList.length; i++) {
																				let code=codeList[i].code;
																				if (code.length===0){
																					ddd=false;
																					break;
																				}

																			}

																			if (ddd===true){
																				axios({
																					method:"post",
																					url:"attributeValue/addCodeAuto",
																					data:codeList
																				}).then(function (resp){
																					if (resp.data==="success"){
																						alert("添加成功！");
																						select(number,n);
																						// console.log(n)
																						// console.log(number)
																						let wd = document.querySelector(".window");
																						wd.style.display="none";
																						//输入框设置为空
																						let inputs = document.querySelectorAll("#addBlock input");
																						for (let i = 0; i < inputs.length; i++) {
																							inputs[i].value="";
																						}

																						document.getElementById("description").value="";
																					}else {
																						deleteFailProduct();

																					}
																				})
																			}
																			else {

																				axios({
																					method:"post",
																					url:"product/deleteReally",
																					data:productid
																				}).then(function (resp){
																					if (resp.data==="success"){
																						alert("编码值不能为空！")
																					}else {
																						alert("系统错误，请联系相关人员！")
																					}
																				})
																			}


																		}
																		else {

																			alert("添加成功！");
																			select(number,n);
																			// console.log(n)
																			// console.log(number)
																			let wd = document.querySelector(".window");
																			wd.style.display="none";
																			//输入框设置为空
																			let inputs = document.querySelectorAll("#addBlock input");
																			for (let i = 0; i < inputs.length; i++) {
																				inputs[i].value="";
																			}

																			document.getElementById("description").value="";
																		}

																	}

																}
																else {
																	//添加失败，删除原来添加的product
																	deleteFailProduct();
																}

															})


														})





													}
													else {

														//删除已经添加的
														axios({
															method:"post",
															url:"product/deleteReally",
															data:productid
														}).then(function (resp){
															if (resp.data==="success"){
																alert("数据未填写完整！");
															}else {
																alert("系统错误，请联系相关人员！")
															}
														})
													}
												})





											}


										})




									}

								}

								axios({
									method:"post",
									url:"sort/selectOtherLevel",
									data:number
								}).then(async function (resp){
									let datas=resp.data;

									if (datas.length!==0){
										//如果能查到证明不是最后一层分类
										let level=datas[0].level
										m=level-1;
										await belong(number,m);
									}else {
										//查不到证明是最后一层分类直接当做id去查自己是几级分类
										axios({
											method:"post",
											url:"sort/selectOfSort",
											data:number
										}).then(async function (resp){
											let datas=resp.data;

											m=datas[0].level;
											await belong(number,m);
										})
									}
								})



							}
							else {
								alert(productid+"添加失败！");


							}
						}else {


							alert("添加成功！");
							select(number,n);
							let wd = document.querySelector(".window");
							wd.style.display="none";
							//输入框设置为空
							let inputs = document.querySelectorAll("#addBlock input");
							for (let i = 0; i < inputs.length; i++) {
								inputs[i].value="";
							}

							document.getElementById("description").value="";
						}





					})
				}
				//检查是否是同一个物料
				async function submit1 (){



					//先判断是否有后添加的属性
					let innerHTML = document.getElementById("selectAttributeAdd").innerHTML;
					if (innerHTML!==""){
						//有后添加的属性，获取所有的填写的后来添加的属性的内容
						var datass=[];
						let m;
						async function belong(number1,m){

							if (m>0){
								//获取当前分类属于哪些大类里
								axios({
									method:"post",
									url:"sort/selectOfSort",
									data:number1
								}).then(function (resp){
									var datas = resp.data;
									datass=datas.concat(datass);
									m--;
									belong(datas[0].parentId,m)
								})

							}else if (m===0) {

								var list;
								var sdata=[];

								for (let i = 0; i < datass.length; i++) {


									await	axios({
										method:"post",
										url:"attribute/selectAttributeName",
										data:datass[i].id
									}).then( async function (resp){
										let datas=resp.data;

										if (datas.length>0){
											//获取所有数据
											sdata=datas.concat(sdata);
											// console.log(list)

										}


									})
								}
								//获取公共属性
								await axios({
									method:"post",
									url:"attribute/selectPublic"
								}).then(function (resp){
									let datas=resp.data;
									//获取所有数据
									sdata=datas.concat(sdata);
								})

								//解析所有数据
								// for (let i = 0; i < sdata.length; i++) {
								if (sdata.length>0){
									for (let j = 0; j < sdata.length; j++) {
										var content={
											id:"",
											parentId:"",
											productId:"",
											content:"",
										}
										list=[sdata.length]
										for (let i = 0; i < sdata.length; i++) {
											content.parentId=sdata[i].id;
											content.content=document.getElementById("input"+sdata[i].id+"").value.trim();
											list[i]=content;
											content={
												id:"",
												parentId:"",
												productId:"",
												content:"",
											}
										}

									}

										//数据都填写完整了，判断物料名是否重复
									//判断唯一标识的数据是否重复
									let unique=false;
									let dj=0;
									for (let i = 0; i < list.length; i++) {
										let parentId = list[i].parentId;
										let content1 = list[i].content;
										let b = await uniqueAdd(parentId,content1);
										if (b===true){
											unique=true;
											dj=i;
											break;
										}

									}
									if (unique===true){
										//查询属性名
										axios({
											method:"post",
											url:"attribute/selectById",
											data:list[dj].parentId
										}).then(function (resp){
											let datas=resp.data;
											alert(""+datas[0].name+"开启了唯一标识，"+list[dj].content+"已存在！")
										})

									}
									else {
										axios({
											method:"post",
											url:"product/selectAddNameIfExist",
											data:formdata
										}).then(async function (resp){
											if (resp.data===true){
												//有重复的,那就判断后面添加的属性是否有相同的，对比该分类下的所有物料，对比parentId和Content
												let d=0;
												let e=true;
												await 	axios({
													method:"post",
													url:"product/selectAllInSortDeleteSign",//已弃用的和未弃用的都查询
													data:formdata
												}).then(async function (resp){
													let datas=resp.data;
													//查询已经删除的物料信息
													for (let j = 0; j < datas.length; j++) {

														d=0;
														for (let k = 0; k < list.length; k++) {
															list[k].productId=datas[j].id;
															await axios({
																method:"post",
																url:"attributeFunction/selectByAttNameId",
																data:list[k].parentId
															}).then(async function (resp){
																let functionCode=resp.data;
																let serialCode = functionCode[0].serialCode
																if (Number(serialCode)===Number(1)){
																	//开启了流水码功能
																	d++;
																}
																else {
																	await axios({
																		method:"post",
																		url:"attribute/selectAttributeContentIfExist",
																		data:list[k]
																	}).then(function (resp){
																		if (resp.data===true){
																			d++;
																		}
																	})
																}
															})



														}

														if (Number(d)===Number(list.length)){
															e=false;
															break;
														}



													}

													if (e===false){
														//查询该物料是否是已弃用的
														let productId=list[0].productId;
														await axios({
															method:"post",
															url:"product/selectById",
															data:productId

														}).then( async function (resp){
															let datas=resp.data;
															if (datas[0].deleteSign===1){
																//与弃用物料信息相同，提示是否重新启用
																if (window.confirm("系统检测到该物料与已弃用的物料信息相同，是否启用该物料？")){
																	//检测是否有重新启用的权限
																	enableProduct(datas[0].id,number,n)
																	return true;
																}
																else {
																	return false;
																}

															}
															else {
																alert("该物料已存在，请核对后在操作！")
															}
														})


													}else {
														submit();
													}
												})
											}
											else {
												//没有重复的，可以执行添加操作
												submit();
											}
										})

									}







								}




							}

						}

						axios({
							method:"post",
							url:"sort/selectOtherLevel",
							data:number
						}).then(function (resp){
							let datas=resp.data;

							if (datas.length!==0){
								//如果能查到证明不是最后一层分类
								let level=datas[0].level
								m=level-1;
								belong(number,m);
							}else {
								//查不到证明是最后一层分类直接当做id去查自己是几级分类
								axios({
									method:"post",
									url:"sort/selectOfSort",
									data:number
								}).then(function (resp){
									let datas=resp.data;

									m=datas[0].level;
									belong(number,m);
								})
							}
						})



					}
					else {
						//判断物料名称是否重复

					await 	axios({
							method:"post",
							url:"product/selectAddNameIfExist",
							data:formdata
						}).then(async function (resp){
							if (resp.data===true){
								//有重复的
								alert("该物料已存在，请核对后再操作！")
							}else {
								submit();
							}
						})
					}
				}

				//自动生成一组数据所两两组合的所有结果
				let str=[{num:"0"},{num:"1"},{num:"2"},{num:"3"},{num:"4"},{num:"5"},{num:"6"},{num:"7"},{num:"8"},{num:"9"}]
				let ls1=str[0].num;
				let ls2;
				let lsSum=[];
				let lsD=[{}];
				function lsNumber1(q){
					if (q < str.length) {
						for (let i = 0; i < str.length; i++) {

							ls2 = str[i].num;
							ls1 = str[q].num
							if (String(ls2) === String(str[str.length - 1].num)) {
								q++;

								let sum=(ls1+ls2)
								lsD=[{num:""+sum+""}];
								lsSum=lsD.concat(lsSum);
								lsD={};
								lsNumber1(q);


							} else {
								let sum=(ls1+ls2)
								lsD=[{num:""+sum+""}];
								lsSum=lsD.concat(lsSum);
								lsD={};

							}
						}
					}
					else if (q===str.length){


						//得到了所有的集合
						//判断物料号是否重复
						axios({
							method:"post",
							url:"product/selectMN",
							data:formdata
						}).then(async function (resp){
							if (resp.data===true){
								//物料号重复了


								if (window.confirm("系统检测到要使用流水码，是否要进行添加？")){

									//得到映射
									await axios({
										method:"post",
										url:"mapping/selectBySortId",
										data:formdata.parentId
									}).then(async function (resp){
										let mappingData=resp.data;
										//先验证是否开启了流水码功能
										let b = await ifSerialExist(mappingData);

										if (b===true){
											//开启了流水码功能，查询该分类下插入的最后一个流水码
											await serialCode(mappingData,formdata.parentId);
											//然后查询重复不重复，如果重复，就用最后两位默认流水码，如果最后两位默认流水码到99，那就得到的数据库中最后的流水码+1
											formdata.materialNumber=document.getElementById("materialNumber").value;

											let b1 = await ifMaterialNumberExist(formdata);
											if (b1===true){
												// //获取最后添加的物料的物料号
												// let s = await selectLastEndSerialCode(formdata.parentId);
												// if (s.length>0){
												// 	let s1 = s.substring(s.length-2);
												// 	let result = parseInt(s1,10)+parseInt("1",10)
												// 	// 如果你想保留结果中的所有前导零，可以将其转换回字符串并手动处理
												// 	let number= result.toString().padStart(str.toString().length, '0');
												//
												// 	if (number.length===s1.length){
												// 		//替换最后两位
												// 		let value5 = document.getElementById("materialNumber").value;
												// 		let newStr = value5.substring(0, value5.length - 2) + number;
												// 		formdata.materialNumber = newStr;
												// 	}
												//
												// }
												//查询物料号是否重复
												// let b2 = await ifMaterialNumberExist(formdata);
												// if (b2===true){
													//重复
													//物料号依然重复,使用默认两位流水号
													async function sub(s) {

														if (Number(s)>=0) {
															//后两位流水码更换
															let value5 = document.getElementById("materialNumber").value;
															//替换最后两位
															let newStr = value5.substring(0, value5.length - 2) + lsSum[s].num;

															formdata.materialNumber = newStr;


															axios({
																method: "post",
																url: "product/selectMN",
																data: formdata
															}).then(function (resp) {
																if (resp.data === false) {
																	//不重复了，执行添加操作
																	submit1();
																	// s=97;
																	// sub(s)

																} else {
																	s--;
																	sub(s);
																}

															})

														}
														else {

															//末尾两位流水号用完了还重复，那就映射中的流水号+1
															let b2 = await serialCodeAddOne(mappingData, formdata.parentId);
															if (b2===true ){
																await sub(lsSum.length - 1);
															}
															else {


																//映射中的流水码用完了，默认最后两位流水码用完要报警
																function sub1(s){

																	if (Number(s)>=0){
																		//后两位流水码更换
																		let value5 = document.getElementById("materialNumber").value;
																		//替换最后两位
																		let newStr = value5.substring(0, value5.length - 2) + lsSum[s].num;

																		formdata.materialNumber=newStr;


																		axios({
																			method:"post",
																			url:"product/selectMN",
																			data:formdata
																		}).then(function (resp){
																			if (resp.data===false){
																				//不重复了，执行添加操作
																				submit1();
																				// s=97;
																				// sub(s)

																			}else {
																				s--;
																				sub1(s);
																			}

																		})

																	}
																	else {
																		alert("流水码已用完！请联系相关人员！")
																	}

																}
																sub1(lsSum.length-1);
															}
														}

													}
													await sub(lsSum.length - 1);


											}
											else {
												//物料号不重复
												await submit1();
											}
										}
										else {

											//查询物料号是否重复
											let b2 = await ifMaterialNumberExist(formdata);
											if (b2===true){
												function sub1(s){

													if (Number(s)>=0){
														//后两位流水码更换
														let value5 = document.getElementById("materialNumber").value;
														//替换最后两位
														let newStr = value5.substring(0, value5.length - 2) + lsSum[s].num;

														formdata.materialNumber=newStr;


														axios({
															method:"post",
															url:"product/selectMN",
															data:formdata
														}).then(function (resp){
															if (resp.data===false){
																//不重复了，执行添加操作
																submit1();
																// s=0;
																// sub(s)

															}else {
																s--;
																sub1(s);
															}

														})

													}
													else {
														alert("流水码已用完！请联系相关人员！")
													}

												}
												sub1(lsSum.length-1);
											}
											else {
												await submit1();
											}

										}






									})



									return true;
								}
								else {
									return false;
								}

							}
							else {
								//代表不重复
								await submit1();
							}
						})



					}

				}

				lsNumber1(0);


			}

		}


	}

}


function addblack(s){
	for (let i = 1; i < 10; i++) {
		document.getElementById("mdiv"+i+"").style.color="black";
	}
	let trim = document.getElementById("input"+s+"").value.trim();

	if (trim.length===0){
		document.getElementById("input"+s+"").value="";
	}
}
function upblack(s){
	for (let i = 1; i < 10; i++) {
		document.getElementById("upmdiv"+i+"").style.color="black";

	}
	let trim = document.getElementById("upInput"+s+"").value.trim();
	if (trim.length===0){
		document.getElementById("upInput"+s+"").value="";
	}
}

//更新先变黑
function black(){
	for (let i = 1; i < 10; i++) {
		document.getElementById("upmdiv"+i+"").style.border="1px solid black";

	}
}

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

//顺序编码
let list = ["0","1","2","3","4","5","6","7","8","9","A","B","C","D","E","F","G","H","J","K","L","M","N","P","Q","R","S","T","U","V","W","X","Y","Z"];

function generateCode(length,number,list,cs) {
	//length是编码的长度，number是第几个数

		if (length>0){

			//求shang
			let s = Math.floor(number/(list.length ** (length-1))) ;
			// console.log(number)
			// console.log(list.length ** (length-1))
			// console.log(number/(list.length ** (length-1)))
			// console.log(s)
			//求余数
			let ys = number%(list.length ** (length-1));
			// console.log(ys)
			cs=cs+list[s];
			return  generateCode(length-1,ys,list,cs);


	}else {


			return cs;

		}






}










let lsn="00"
//查询该分类中后添加的属性（物料添加时使用）
function selectAttributeAdd(number,n){
	var datass=[];
	let m;
	var forver=number;
	async function belong(number,m){

		if (m>0){
			//获取当前分类属于哪些大类里
			axios({
				method:"post",
				url:"sort/selectOfSort",
				data:number
			}).then(function (resp){
				var datas = resp.data;
				datass=datas.concat(datass);
				m--;
				belong(datas[0].parentId,m)
			})

		}else if (m===0) {
			let formdata=""
			//生成分类编码
			let sortCodeSum="";
			for (let i = 0; i < datass.length; i++) {
				sortCodeSum+=datass[i].code
			}

			//查询所有编码分级编码规则，获取总长度
			let number=0;
			axios({
				method:"post",
				url:"code/selectAll"
			}).then(function (resp){
				let datas=resp.data;
				if (datas.length>0){
					for (let i = 0; i <datas.length ; i++) {
						number+=Number(datas[i].length)
					}
					// console.log("总长度为"+number);
					// console.log("现有长度"+sortCodeSum.length)
					let x=number-sortCodeSum.length;

					let s="";
					for (let i = 0; i < Number(x); i++) {
						s+="0"
					}
					document.getElementById("produceId").innerHTML=sortCodeSum+s;
					document.getElementById("nineNumber").innerHTML="000000000";
					let nineNum = document.getElementById("nineNumber").innerHTML;
					//先将空的编码显示
					document.getElementById("materialNumber").value=sortCodeSum+s+nineNum+lsn;
					document.getElementById("sortNumberA").innerHTML=sortCodeSum+s;
					document.getElementById("serialNumber").innerHTML=lsn;
				}
			})


			//查询公共属性分类
			axios({
				method:"post",
				url:"sort/selectId0"
			}).then(async function (resp){
				let ggdatas=resp.data;
				datass=ggdatas.concat(datass);
				let bj=[];
				for (let i = 0; i < datass.length; i++) {
					await	axios({
						method:"post",
						url:"attribute/selectAttributeName",
						data:datass[i].id
					}).then(async function (resp){
						let datas=resp.data;
						bj=datas.concat(bj);
						if (datas.length>0){

							for (let j = 0; j < datas.length; j++) {
								await axios({
									method:"post",
									url:"attributeValue/selectByAttNameId",
									data:datas[j].id
								}).then(async function (resp){
									if (resp.data.length>0){
										if (datas[j].unit.length>0){
											formdata+='   <div style="width:738px ;height: auto;display: flex;">\n' +
												'                           <div  style="width: 100px;height: auto; border-right: 1px solid #d6e0ef;border-bottom: 1px solid #d6e0ef;text-align: center;display: flex">\n' +
												'                               <div style="width: fit-content;height: auto;align-self: center;margin: auto">'+datas[j].name+'<br>(单位:'+datas[j].unit+')</div>\n' +
												'                           </div>\n' +
												'                           <div class="qwe"  style="width:294px ;position:relative; height: auto;border-right: 1px solid #d6e0ef;border-bottom: 1px solid #d6e0ef;">\n' +
												'                               &nbsp;<input maxlength="80" style="width: 90%;height: 20px;" type="text" name="" placeholder="" id=input'+datas[j].id+' value="" > &nbsp;<b id="b'+datas[j].id+'" style="display: none">*</b>\n' +
												'        <div id="inputValue'+datas[j].id+'" style="display: none;position: absolute;top: 100%;left: 3px; padding: 1px 2px; width:90%;min-width: 200px; height:auto; z-index:9999;overflow: hidden;background: #e0e0e0;border-top: none;"> </div>\n' +
												'                           </div>\n' +
												'                           <div  style="width: 50px;height: auto; border-right: 1px solid #d6e0ef;border-bottom: 1px solid #d6e0ef;text-align: center;display: flex">\n' +
												'                            <div style="width: fit-content;height: auto;align-self: center;margin: auto">编码</div>\n' +
												'                        </div>\n' +
												'                        <div class="qwe"  style="width:294px ;position:relative; height: auto;border-right: 1px solid #d6e0ef;border-bottom: 1px solid #d6e0ef;">\n' +
												'                            &nbsp;<input maxlength="80" style="width: 90%;height: 20px;" type="text" name="" disabled id=code'+datas[j].id+' value="" > &nbsp;\n' +
												'                        </div>\n' +
												'                       </div>'

										}else {
											formdata+='   <div style="width:738px ;height: auto;display: flex;">\n' +
												'                           <div  style="width: 100px;height: auto; border-right: 1px solid #d6e0ef;border-bottom: 1px solid #d6e0ef;text-align: center;display: flex">\n' +
												'                               <div style="width: fit-content;height: auto;align-self: center;margin: auto">'+datas[j].name+'<br></div>\n' +
												'                           </div>\n' +
												'                           <div class="qwe"  style="width:294px ;position:relative; height: auto;border-right: 1px solid #d6e0ef;border-bottom: 1px solid #d6e0ef;">\n' +
												'                               &nbsp;<input maxlength="80" style="width: 90%;height: 20px;" type="text" name="" placeholder="" id=input'+datas[j].id+' value="" > &nbsp;<b id="b'+datas[j].id+'" style="display: none">*</b>\n' +
												'        <div id="inputValue'+datas[j].id+'" style="display: none;position: absolute;top: 100%;left: 3px; padding: 1px 2px; width:90%;min-width: 200px; height:auto; z-index:9999;overflow: hidden;background: #e0e0e0;border-top: none;"> </div>\n' +
												'                           </div>\n' +
												'                           <div  style="width: 50px;height: auto; border-right: 1px solid #d6e0ef;border-bottom: 1px solid #d6e0ef;text-align: center;display: flex">\n' +
												'                            <div style="width: fit-content;height: auto;align-self: center;margin: auto">编码</div>\n' +
												'                        </div>\n' +
												'                        <div class="qwe"  style="width:294px ;position:relative; height: auto;border-right: 1px solid #d6e0ef;border-bottom: 1px solid #d6e0ef;">\n' +
												'                            &nbsp;<input maxlength="80" style="width: 90%;height: 20px;" type="text" name="" disabled id=code'+datas[j].id+' value="" > &nbsp;\n' +
												'                        </div>\n' +
												'                       </div>'
										}
									}else {
										if (datas[j].unit.length>0){
											formdata+='   <div style="width:738px ;height: auto;display: flex;">\n' +
												'                           <div  style="width: 100px;height: auto; border-right: 1px solid #d6e0ef;border-bottom: 1px solid #d6e0ef;text-align: center;display: flex">\n' +
												'                               <div style="width: fit-content;height: auto;align-self: center;margin: auto">'+datas[j].name+'<br>(单位:'+datas[j].unit+')</div>\n' +
												'                           </div>\n' +
												'                           <div class="qwe"  style="width:294px ;position:relative; height: auto;border-right: 1px solid #d6e0ef;border-bottom: 1px solid #d6e0ef;">\n' +
												'                               &nbsp;<input maxlength="80" style="width: 90%;height: 20px;" type="text" name=""  id=input'+datas[j].id+' value="" > &nbsp;<b id="b'+datas[j].id+'" style="display: none">*</b>\n' +
												'        <div id="inputValue'+datas[j].id+'" style="display: none;position: absolute;top: 100%;left: 3px; padding: 1px 2px; width:90%;min-width: 200px; height:auto; z-index:9999;overflow: hidden;background: #e0e0e0;border-top: none;"> </div>\n' +
												'                           </div>\n' +
												'                           <div  style="width:50px;height: auto; border-right: 1px solid #d6e0ef;border-bottom: 1px solid #d6e0ef;text-align: center;display: flex">\n' +
												'                            <div style="width: fit-content;height: auto;align-self: center;margin: auto">编码</div>\n' +
												'                        </div>\n' +
												'                        <div class="qwe"  style="width:294px ;position:relative; height: auto;border-right: 1px solid #d6e0ef;border-bottom: 1px solid #d6e0ef;">\n' +
												'                            &nbsp;<input maxlength="80" style="width: 90%;height: 20px;" type="text" name="" disabled id=code'+datas[j].id+' value="" > &nbsp;\n' +
												'                        </div>\n' +
												'                       </div>'

										}else {
											formdata+='   <div style="width:738px ;height: auto;display: flex;">\n' +
												'                           <div  style="width: 100px;height: auto; border-right: 1px solid #d6e0ef;border-bottom: 1px solid #d6e0ef;text-align: center;display: flex">\n' +
												'                               <div style="width: fit-content;height: auto;align-self: center;margin: auto">'+datas[j].name+'<br></div>\n' +
												'                           </div>\n' +
												'                           <div class="qwe"  style="width:294px ;position:relative; height: auto;border-right: 1px solid #d6e0ef;border-bottom: 1px solid #d6e0ef;">\n' +
												'                               &nbsp;<input maxlength="80" style="width: 90%;height: 20px;" type="text" name="" id=input'+datas[j].id+' value="" > &nbsp;<b id="b'+datas[j].id+'" style="display: none">*</b>\n' +
												'        <div id="inputValue'+datas[j].id+'" style="display: none;position: absolute;top: 100%;left: 3px; padding: 1px 2px; width:90%;min-width: 200px; height:auto; z-index:9999;overflow: hidden;background: #e0e0e0;border-top: none;"> </div>\n' +
												'                           </div>\n' +
												'                           <div  style="width: 50px;height: auto; border-right: 1px solid #d6e0ef;border-bottom: 1px solid #d6e0ef;text-align: center;display: flex">\n' +
												'                            <div style="width: fit-content;height: auto;align-self: center;margin: auto">编码</div>\n' +
												'                        </div>\n' +
												'                        <div class="qwe"  style="width:294px ;position:relative; height: auto;border-right: 1px solid #d6e0ef;border-bottom: 1px solid #d6e0ef;">\n' +
												'                            &nbsp;<input maxlength="80" style="width: 90%;height: 20px;" type="text" name="" disabled id=code'+datas[j].id+' value="" > &nbsp;\n' +
												'                        </div>\n' +
												'                       </div>'
										}
									}
								})


							}
						}


					})
				}
				document.getElementById("selectAttributeAdd").innerHTML=formdata;


				//得到映射
				await axios({
					method:"post",
					url:"mapping/selectBySortId",
					data:forver
				}).then( async function (resp){
					let ysdatas=resp.data;

					for (let k = 0; k <ysdatas.length ; k++) {
						// let b = await serialCode(ysdatas[k], forver);
						// if (b===false){
						// 	alert("设置的流水码已用完，请联系相关人员");
						// 	let element = document.querySelector(".window");
						// 	element.style.display="none";
						// 	break;
						// }
						axios({
							method:"post",
							url:"attributeFunction/selectByAttNameId",
							data:ysdatas[k].attNameId
						}).then(function (resp)
						{
							let ableCode=resp.data;
							let serialCode = ableCode[0].serialCode;
							if (serialCode===1){

								let length = ysdatas[k].length;
								let str="";
								for (let i = 0; i < length; i++) {
									str+="0"
								}
								//开启了流水码功能，限制输入和编码
								document.getElementById("input"+ysdatas[k].attNameId+"").disabled=true;
								document.getElementById("code"+ysdatas[k].attNameId+"").disabled=true;
								document.getElementById("input"+ysdatas[k].attNameId+"").value=str;
								document.getElementById("code"+ysdatas[k].attNameId+"").value=str;

							}
						})






						//查询该属性下面是否有属性值
						await axios({
							method:"post",
							url:"attributeValue/selectIfCode",
							data:ysdatas[k].attNameId
						}).then(function (resp){
							let datas=resp.data;
							if (datas===true){
								document.getElementById("input"+ysdatas[k].attNameId+"").placeholder="请输入查询！"
							}
						})
						if (document.getElementById("input"+ysdatas[k].attNameId+"")){
							document.getElementById("b"+ysdatas[k].attNameId+"").style.color="red";
							document.getElementById("b"+ysdatas[k].attNameId+"").style.display="";
							document.getElementById("input"+ysdatas[k].attNameId+"").onfocus = function (){
								for (let i = ysdatas[k].beginLocation; i <=ysdatas[k].endLocation ; i++) {
									document.getElementById("mdiv"+i+"").style.color="red";
									for (let j = 0; j < ysdatas.length; j++) {
										if(k!==j){
											for (let l = ysdatas[j].beginLocation; l <= ysdatas[j].endLocation ; l++) {
												document.getElementById("mdiv"+l+"").style.color="black";
											}
										}
									}
								}

							}

							let num=ysdatas[k].attNameId;
							let input = document.getElementById("input"+ysdatas[k].attNameId+"");
							input.addEventListener('input', debounce(function () {
								let value = document.getElementById("input"+num+"").value.trim();
								document.getElementById("code"+num+"").value="";
								if (value.length>0){
									axios({
										method:"post",
										url:"attributeValue/selectByAttNameId",
										data:num
									}).then(function (resp)
									{

										function addUseFunction(){
											//查询是否是范围
											axios({
												method:"post",
												url:"attributeFunction/selectByAttNameId",
												data:num
											}).then(function (resp){
												let rangeDatas=resp.data;
												if (rangeDatas[0].range===0){
													//0表示是值
													//模糊查询
													axios({
														method:"post",
														url:"attributeValue/inputLX?attNameId="+num+"",
														data:value
													}).then(function (resp){
														let lxDatas=resp.data;
														//自动添加编码以及不添加自动编码功能的主体代码
														function autoAndNo(value,num){

															//得到了输入框的值，先判断能不能找到这个值
															let formdata={
																attNameId:num,
																attValue:value
															}
															//查询这个值是否存在
															axios({
																method:"post",
																url:"attributeValue/selectValueExistAdd",
																data:formdata
															}).then(function (resp){
																let attValueBoolean=resp.data;
																if (attValueBoolean===true){
																	//查看该属性是否在映射中
																	axios({
																		method:"post",
																		url:"mapping/selectMapping",
																		data:forver //分类id
																	}).then(function (resp){
																		let mappingData=resp.data;
																		let map=false;
																		let index=0;
																		for (let j = 0; j < mappingData.length; j++) {
																			if (Number(num)===mappingData[j].att_name_id){
																				map=true;
																				index=j;
																				break;
																			}

																		}

																		if (map===true){
																			//在映射中，根据长度和起始位置改编物料编码
																			//值已经存在，查询这个值的编码
																			let value1 = document.getElementById("input"+num+"").value.trim();
																			let data={
																				attNameId:num,
																				attValue:value1
																			}
																			axios({
																				method:"post",
																				url:"attributeValue/selectCode",
																				data:data
																			}).then(function (resp){
																				document.getElementById("code"+num+"").value=resp.data[0].code;
																				let nine = document.getElementById("nineNumber").innerHTML;
																				let str = nine;
																				//判断输入的长度和编码长度

																				let replaceStr =resp.data[0].code;//要替换的字符串

																				let startIndex = mappingData[index].begin_location;

																				let endIndex = mappingData[index].end_location;

																				let newStr = str.slice(0, startIndex-1) + replaceStr + str.slice(endIndex);

																				document.getElementById("nineNumber").innerHTML=newStr;
																				//获取分类编码
																				let sortId = document.getElementById("produceId").innerHTML;

																				document.getElementById("materialNumber").value=sortId+newStr+lsn;
																				for (let j = 0; j < newStr.length; j++) {
																					let m=Number(j)+Number(1);
																					document.getElementById("mdiv"+m+"").innerHTML=newStr[j];
																				}
																			})



																		}

																		else {
																			//不在映射中
																			let value1 = document.getElementById("input"+num+"").value.trim();
																			let data={
																				attNameId:num,
																				attValue:value1
																			}
																			axios({
																				method:"post",
																				url:"attributeValue/selectCode",
																				data:data
																			}).then(function (resp){
																				document.getElementById("code"+num+"").value=resp.data[0].code;
																			})
																		}

																	})

																}
																else {

																	//该值不存在,查询是否开启了值作为编码的功能
																	axios({
																		method:"post",
																		url:"attributeFunction/selectIfAutoCode",
																		data:num
																	}).then(function (resp){
																		if (resp.data===true){
																			//开启了值作为编码的功能
																			//先判断输入值的格式是否符合做编码的格式，然后判断长度是否符合编码的长度，然后再判断内容是否重复
																			var pattern = /^[0-9A-Za-z]+$/;
																			if (pattern.test(value)===true){
																				//符合标准，判断长度
																				axios({
																					method:"post",
																					url:"attribute/selectById",
																					data:num
																				}).then(function (resp){
																					let datas=resp.data;
																					if (Number(datas[0].length)>=Number(value.length)){
																						//长度符合，判断作为编码是否重复
																						let codeData={
																							attNameId:"",
																							attValue:"",
																							code:""
																						}
																						if (Number(datas[0].length)===Number(value.length)){
																							codeData={
																								attNameId:num,
																								attValue:value,
																								code:value
																							}
																						}else {
																							let jnum=Number(datas[0].length)-Number(value.length);
																							let str="";
																							for (let j = 0; j < jnum; j++) {
																								str+="0"
																							}
																							codeData={
																								attNameId:num,
																								attValue:value,
																								code:str+value
																							}
																						}

																						//查看该属性是否在映射中
																						axios({
																							method:"post",
																							url:"mapping/selectMapping",
																							data:forver //分类id
																						}).then(function (resp){
																							let mappingData=resp.data;
																							let map=false;
																							let index=0;
																							for (let j = 0; j < mappingData.length; j++) {
																								if (Number(num)===mappingData[j].att_name_id){
																									map=true;
																									index=j;
																									break;
																								}

																							}

																							if (map===true){
																								//在映射中，根据长度和起始位置改编物料编码
																								//查看这个值是否已经被使用了
																								let data={
																									attNameId:num,
																									attValue:document.getElementById("input"+num+"").value.trim()

																								}
																								axios({
																									method:"post",
																									url:"attributeValue/selectValueExistAdd",
																									data:data
																								}).then(function (resp){
																									if (resp.data===true){
																										//值已经存在，查询这个值的编码
																										let value1 = document.getElementById("input"+num+"").value.trim();
																										let data={
																											attNameId:num,
																											attValue:value1
																										}
																										axios({
																											method:"post",
																											url:"attributeValue/selectCode",
																											data:data
																										}).then(function (resp){
																											document.getElementById("code"+num+"").value=resp.data[0].code;
																										})
																									}else {
																										//值不存在.查询该编码是否已经存在
																										axios({
																											method:"post",
																											url:"attributeValue/selectCodeExist",
																											data:codeData
																										}).then(function (resp){
																											if (resp.data===true){
																												//编码存在，提示并清空输入框
																												document.getElementById("input"+num+"").value="";
																												document.getElementById("code"+num+"").value="";
																												alert("该编码已存在，不能使用该值作为编码使用！")
																											}
																											else {
																												//编码不存在

																												//得到了属性值的编码
																												let nine = document.getElementById("nineNumber").innerHTML;
																												let str = nine;
																												//判断输入的长度和编码长度

																												let replaceStr =codeData.code;//要替换的字符串
																												document.getElementById("code"+num+"").value=codeData.code;
																												let startIndex = mappingData[index].begin_location;


																												let endIndex = mappingData[index].end_location;
																												let newStr = str.slice(0, startIndex-1) + replaceStr + str.slice(endIndex);

																												document.getElementById("nineNumber").innerHTML=newStr;
																												//获取分类编码
																												let sortId = document.getElementById("produceId").innerHTML;
																												document.getElementById("materialNumber").value=sortId+newStr+lsn;
																												for (let j = 0; j < newStr.length; j++) {

																													let m=Number(j)+Number(1);
																													document.getElementById("mdiv"+m+"").innerHTML=newStr[j];
																												}
																											}
																										})


																									}
																								})

																							}else {
																								//不在映射中,将编码在编码框中显示
																								document.getElementById("code"+num+"").value=codeData.code;
																							}

																						})



																					}else {
																						let value1 = document.getElementById("input"+num+"").value.trim();
																						if(value1.length>0){
																							alert("输入值的长度与编码长度不符，请核对后在进行操作！");
																						}
																						document.getElementById("input"+num+"").value="";
																						document.getElementById("code"+num+"").value="";


																					}
																				})
																			}else {
																				let value2 = document.getElementById("input"+num+"").value.trim();
																				if(value2.length>0){
																					alert("输入值的格式不适合作为编码，请核对后再进行操作！");
																				}
																				document.getElementById("input"+num+"").value="";
																				document.getElementById("code"+num+"").value="";
																			}
																		}
																		else {
																			//未开启值作为编码的功能,查看是否有编码表存在,查询是否开启了顺序编码的功能
																			axios({
																				method:"post",
																				url:"attributeFunction/selectIfOrderCode",
																				data:num
																			}).then(function (resp){
																				if (resp.data===true){
																					//开启了顺序编码的功能
																					//获取长度
																					axios({
																						method:"post",
																						url:"attribute/selectById",
																						data:num
																					}).then(function (resp){
																						let datas=resp.data;
																						let length = datas[0].length;
																						//查询该属性下有多少个编码
																						axios({
																							method:"post",
																							url:"attributeValue/selectByAttNameId",
																							data:num
																						}).then(function (resp){
																							let CodeNumber=resp.data.length;
																							let number= Number(CodeNumber)+Number(1);
																							let generateCode1 = generateCode(length,number,list,"");


																							function orderNoRepeat(generateCode1){
																								//调用方法获取编码

																								//验证编码是否重复
																								let codeData={
																									attNameId:num,
																									code:generateCode1,
																								}
																								axios({
																									method:"post",
																									url:"attributeValue/selectCodeExistAdd",
																									data:codeData
																								}).then(function (resp){
																									if (resp.data===true){
																										//存在
																										number++;



																										let generateCode1 = generateCode(length,number,list,"");
																										orderNoRepeat(generateCode1);

																									}else if (resp.data===false){
																										//不存在
																										//查看该属性是否在映射中
																										axios({
																											method:"post",
																											url:"mapping/selectMapping",
																											data:forver //分类id
																										}).then(function (resp){
																											let mappingData=resp.data;
																											let map=false;
																											let index=0;
																											for (let j = 0; j < mappingData.length; j++) {
																												if (Number(num)===mappingData[j].att_name_id){
																													map=true;
																													index=j;
																													break;
																												}

																											}
																											if (map===true){
																												//在映射中
																												//值不存在
																												//得到了属性值的编码
																												let nine = document.getElementById("nineNumber").innerHTML;
																												let str = nine;
																												//判断输入的长度和编码长度

																												let replaceStr =codeData.code;//要替换的字符串
																												document.getElementById("code"+num+"").value=codeData.code;
																												let startIndex = mappingData[index].begin_location;


																												let endIndex = mappingData[index].end_location;

																												let newStr = str.slice(0, startIndex-1) + replaceStr + str.slice(endIndex);


																												document.getElementById("nineNumber").innerHTML=newStr;
																												//获取分类编码
																												let sortId = document.getElementById("produceId").innerHTML;
																												document.getElementById("materialNumber").value=sortId+newStr+lsn;
																												for (let j = 0; j < newStr.length; j++) {

																													let m=Number(j)+Number(1);
																													document.getElementById("mdiv"+m+"").innerHTML=newStr[j];
																												}
																											}
																											else {
																												//不在映射中,将编码在编码框中显示
																												document.getElementById("code"+num+"").value=codeData.code;
																											}
																										})


																									}
																								})
																							}
																							orderNoRepeat(generateCode1);


																						})
																					})
																				}
																				else {

																					//未开启值作为编码，也未开启顺序编码
																					axios({
																						method:"post",
																						url:"attributeValue/selectByAttNameId",
																						data:num
																					}).then(function (resp){
																						if (resp.data.length>0){
																							//有编码表
																							document.getElementById("input"+num+"").value="";
																							document.getElementById("code"+num+"").value="";
																						}else {
																							//没有编码表
																							document.getElementById("code"+num+"").value="";
																						}
																					})
																				}
																			})



																		}
																	})
																}
															})
														}
														if (lxDatas.length>0){
															var formdata="";
															if (lxDatas.length>5){
																for (let i = 0; i < 5; i++) {
																	formdata+="<div class='LX"+num+"' style='width: 100%;height: auto;margin: auto'><pre>"+lxDatas[i].att_value+"</pre></div>"
																}
															}else if (lxDatas.length===1){
																formdata+="<div class='LX"+num+"' style='width: 100%;height: auto;margin: auto'><pre>"+lxDatas[0].att_value+"</pre></div>"
															}
															else{
																for (let i = 0; i <lxDatas.length; i++) {
																	formdata+="<div class='LX"+num+"' style='width: 100%;height: auto;margin: auto'><pre>"+lxDatas[i].att_value+"</pre></div>"
																}
															}

															document.getElementById("inputValue"+num+"").innerHTML=formdata;


															//绑定触发变色
															let lxs = document.querySelectorAll(".LX"+num+"");
															for (let j = 0; j < lxs.length; j++) {
																lxs[j].onmouseover=function (){
																	lxs[j].style.backgroundColor="white"
																}
																lxs[j].onmouseout=function (){
																	lxs[j].style.backgroundColor="#e0e0e0"
																}

															}

															document.getElementById("inputValue"+num+"").style.display="";

															//先去除所有的绑定方法
															document.getElementById("inputValue"+num+"").onmouseout=null;
															document.getElementById("inputValue"+num+"").onmouseover=null;
															document.getElementById("input"+num+"").onblur=null;

															//失去光标要执行的方法
															document.getElementById("input"+num+"").onblur=function (){
																//没有动作，失去光标，获取输入的值
																let value1 = document.getElementById("input"+num+"").value.trim();
																if (value1.length>0){
																	autoAndNo(value1,num);
																}
																else {
																	document.getElementById("input"+num+"").value="";
																	//查看该属性是否在映射中
																	axios({
																		method:"post",
																		url:"mapping/selectMapping",
																		data:forver //分类id
																	}).then(function (resp) {
																		let mappingData = resp.data;
																		let map = false;
																		let index = 0;
																		for (let j = 0; j < mappingData.length; j++) {
																			if (Number(num) === mappingData[j].att_name_id) {
																				map = true;
																				index = j;
																				break;
																			}

																		}

																		if (map === true) {
																			//在映射中，根据长度和起始位置改编物料编码

																			let nine = document.getElementById("nineNumber").innerHTML;
																			let str = nine;
																			//判断输入的长度和编码长度
																			let replaceStr ="";
																			for (let j = 0; j < mappingData[index].length; j++) {
																				replaceStr+="0";
																			}

																			//要替换的字符串

																			let startIndex = mappingData[index].begin_location;

																			let endIndex = mappingData[index].end_location;

																			let newStr = str.slice(0, startIndex - 1) + replaceStr + str.slice(endIndex);

																			document.getElementById("nineNumber").innerHTML = newStr;
																			//获取分类编码
																			let sortId = document.getElementById("produceId").innerHTML;

																			document.getElementById("materialNumber").value = sortId + newStr + lsn;
																			for (let j = 0; j < newStr.length; j++) {
																				let m = Number(j) + Number(1);
																				document.getElementById("mdiv" + m + "").innerHTML = newStr[j];
																			}



																		}
																	})
																}

																document.getElementById("inputValue"+num+"").style.display="none";


															}
															//因为点击会默认执行失去光标的方法，所以鼠标移动到这里先将失去光标的方法置空，移出后再添加回来
															document.getElementById("inputValue"+num+"").onmouseover=function (){
																document.getElementById("input"+num+"").onblur=null;
															}
															document.getElementById("inputValue"+num+"").onmouseout=function (){
																document.getElementById("input"+num+"").onblur=function (){
																	//没有动作，失去光标，获取输入的值
																	let value1 = document.getElementById("input"+num+"").value.trim();
																	if (value1.length>0){
																		autoAndNo(value1,num);
																	}
																	else {
																		document.getElementById("input"+num+"").value="";
																		//查看该属性是否在映射中
																		axios({
																			method:"post",
																			url:"mapping/selectMapping",
																			data:forver //分类id
																		}).then(function (resp) {
																			let mappingData = resp.data;
																			let map = false;
																			let index = 0;
																			for (let j = 0; j < mappingData.length; j++) {
																				if (Number(num) === mappingData[j].att_name_id) {
																					map = true;
																					index = j;
																					break;
																				}

																			}

																			if (map === true) {
																				//在映射中，根据长度和起始位置改编物料编码

																				let nine = document.getElementById("nineNumber").innerHTML;
																				let str = nine;
																				//判断输入的长度和编码长度
																				let replaceStr ="";
																				for (let j = 0; j < mappingData[index].length; j++) {
																					replaceStr+="0";
																				}

																				//要替换的字符串

																				let startIndex = mappingData[index].begin_location;

																				let endIndex = mappingData[index].end_location;

																				let newStr = str.slice(0, startIndex - 1) + replaceStr + str.slice(endIndex);

																				document.getElementById("nineNumber").innerHTML = newStr;
																				//获取分类编码
																				let sortId = document.getElementById("produceId").innerHTML;

																				document.getElementById("materialNumber").value = sortId + newStr + lsn;
																				for (let j = 0; j < newStr.length; j++) {
																					let m = Number(j) + Number(1);
																					document.getElementById("mdiv" + m + "").innerHTML = newStr[j];
																				}



																			}
																		})
																	}

																	document.getElementById("inputValue"+num+"").style.display="none";


																}
															}

															//添加点击查询到的结果执行方法
															lxs = document.querySelectorAll(".LX"+num+"");
															for (let j = 0; j < lxs.length; j++) {
																lxs[j].onclick=function (){
																	//将点击的值放在输入框中
																	document.getElementById("input"+num+"").value=lxDatas[j].att_value;
																	autoAndNo(lxDatas[j].att_value,num);
																	document.getElementById("inputValue"+num+"").style.display="none";


																}
															}


														}
														else {
															//先去除所有的绑定方法
															document.getElementById("inputValue"+num+"").onmouseout=null;
															document.getElementById("inputValue"+num+"").onmouseover=null;
															document.getElementById("input"+num+"").onblur=null;
															document.getElementById("inputValue"+num+"").style.display="";
															document.getElementById("inputValue"+num+"").innerHTML="<div class='LX"+num+"' style='width: 100%;height: auto;margin: auto;color: red'>没有相关数据!</div>"

															//失去光标要执行的方法
															document.getElementById("input"+num+"").onblur=function (){
																//没有动作，失去光标，获取输入的值
																let value1 = document.getElementById("input"+num+"").value.trim();
																if (value1.length>0){
																	autoAndNo(value1,num);
																}
																else {
																	document.getElementById("input"+num+"").value="";
																	//查看该属性是否在映射中
																	axios({
																		method:"post",
																		url:"mapping/selectMapping",
																		data:forver //分类id
																	}).then(function (resp) {
																		let mappingData = resp.data;
																		let map = false;
																		let index = 0;
																		for (let j = 0; j < mappingData.length; j++) {
																			if (Number(num) === mappingData[j].att_name_id) {
																				map = true;
																				index = j;
																				break;
																			}

																		}

																		if (map === true) {
																			//在映射中，根据长度和起始位置改编物料编码

																			let nine = document.getElementById("nineNumber").innerHTML;
																			let str = nine;
																			//判断输入的长度和编码长度
																			let replaceStr ="";
																			for (let j = 0; j < mappingData[index].length; j++) {
																				replaceStr+="0";
																			}

																			//要替换的字符串

																			let startIndex = mappingData[index].begin_location;

																			let endIndex = mappingData[index].end_location;

																			let newStr = str.slice(0, startIndex - 1) + replaceStr + str.slice(endIndex);

																			document.getElementById("nineNumber").innerHTML = newStr;
																			//获取分类编码
																			let sortId = document.getElementById("produceId").innerHTML;

																			document.getElementById("materialNumber").value = sortId + newStr + lsn;
																			for (let j = 0; j < newStr.length; j++) {
																				let m = Number(j) + Number(1);
																				document.getElementById("mdiv" + m + "").innerHTML = newStr[j];
																			}



																		}
																	})
																}

																document.getElementById("inputValue"+num+"").style.display="none";

															}

														}
													})
												}
												else if (rangeDatas[0].range===1){

													//判断输入的值是否符合数据库中的范围
													function compare (str,value){

														//  console.log("范围"+ str)
														// console.log("传入的值"+value)
														if (value.length>0){
															var pattern = /^\[.*\]$/;
															var patterns= /^\<.*\]$/;
															var pattern1= /^\<.*\>$/;
															var patterns1= /^\[.*\>$/;

															if (pattern.test(str)===true){
																str=str.split("[").join("");
																str=str.split("]").join("");
																// str=str.replace(/[a-zA-Z]/g, '');
																let strings = str.split("~");

																//判断输入的数值是否符合这个范围
																if (Number(power(value))>=Number(power(strings[0])) && Number(power(value))<=Number(power(strings[1]))){
																	return true;
																}else {
																	return false;
																}

															}
															else if (patterns.test(str)===true){

																str=str.split("<").join("");
																str=str.split("]").join("");
																// str=str.replace(/[a-zA-Z]/g, '');
																let strings = str.split("~");

																// console.log("开始"+ strings[0]);
																// console.log("结束"+strings[1])

																//判断输入的数值是否符合这个范围
																if (Number(power(value))>Number(power(strings[0])) && Number(power(value))<=Number(power(strings[1]))){
																	return true;
																}else {
																	return false;
																}
															}
															else if (pattern1.test(str)===true){

																str=str.split("<").join("");
																str=str.split(">").join("");
																// str=str.replace(/[a-zA-Z]/g, '');
																let strings = str.split("~");

																// console.log("开始"+ strings[0]);
																// console.log("结束"+strings[1])

																//判断输入的数值是否符合这个范围
																if (Number(power(value))>Number(power(strings[0])) && Number(power(value))<Number(power(strings[1]))){
																	return true;
																}else {
																	return false;
																}
															}
															else if (patterns1.test(str)===true){

																str=str.split("[").join("");
																str=str.split(">").join("");
																// str=str.replace(/[a-zA-Z]/g, '');
																let strings = str.split("~");

																// console.log("开始"+ strings[0]);
																// console.log("结束"+strings[1])

																//判断输入的数值是否符合这个范围
																if (Number(power(value))>=Number(power(strings[0])) && Number(power(value))<Number(power(strings[1]))){
																	return true;
																}else {
																	return false;
																}
															}

															else if (pattern.test(str)===false && patterns.test(str)===false && pattern1.test(str)===false && patterns1.test(str)===false) {
																alert("数据有问题，不符合格式，请联系相关人员！");

															}
														}else {
															return false;
														}


													}
													//1表示是范围,获取所有的范围值
													axios({
														method:"post",
														url:"attributeValue/selectByAttNameId",
														data:num
													}).then(function (resp){
														let datas=resp.data;
														let b;

														for (let i = 0; i < datas.length; i++) {
															b = compare(datas[i].attValue,value);
															if (b===true){
																document.getElementById("inputValue"+num+"").innerHTML="<div id='LX"+num+"' style='width: 100%;height: auto;margin: auto;'><pre>"+datas[i].attValue+"</pre></div>"
																document.getElementById("inputValue"+num+"").style.display="";
																function blurAndClick (){


																	document.getElementById("inputValue"+num+"").style.display="none"
																	let code = datas[i].code;
																	document.getElementById("code"+num).value=code;


																	axios({
																		method:"post",
																		url:"mapping/selectBySortId",
																		data:forver
																	}).then(function (resp){
																		let ysdatas=resp.data;//属性值
																		for (let k = 0; k <ysdatas.length ; k++) {
																			if (ysdatas[k].attNameId === num){
																				axios({
																					method:"post",
																					url:"attributeValue/selectByAttNameId",
																					data:ysdatas[k].attNameId
																				}).then(function (resp){
																					let datas=resp.data;
																					if (datas.length>0){
																						//有属性
																						//得到了属性值的编码
																						let nine = document.getElementById("nineNumber").innerHTML;
																						let str = nine

																						let replaceStr =code;//要替换的字符串
																						let startIndex = ysdatas[k].beginLocation;

																						// document.getElementById("div"+ysdatas[k].attNameId+"").innerHTML=replaceStr;
																						let endIndex = ysdatas[k].endLocation;

																						let newStr = str.slice(0, startIndex-1) + replaceStr + str.slice(endIndex);

																						document.getElementById("nineNumber").innerHTML=newStr;
																						//获取分类编码
																						let sortId = document.getElementById("produceId").innerHTML;
																						document.getElementById("materialNumber").value=sortId+newStr+lsn;
																						//是范围类型的属性值
																						for (let j = 0; j < newStr.length; j++) {
																							let m=Number(j)+Number(1);
																							document.getElementById("mdiv"+m+"").innerHTML=newStr[j];
																						}


																					}else {
																						//无属性用0代替
																					}
																				})
																			}
																			else {

																			}
																		}
																	})



																}
																document.getElementById("LX"+num+"").onclick=blurAndClick;
																let trim = document.getElementById("input"+num+"").value.trim();
																if (trim.length>0){
																	document.getElementById("input"+num+"").onblur=blurAndClick;
																}
																else {
																	document.getElementById("input"+num+"").value="";
																}

																break;
															}

														}

														if (b===false) {
															document.getElementById("inputValue"+num+"").style.display="";
															document.getElementById("inputValue"+num+"").innerHTML="<div id='LX"+num+"' style='width: 100%;height: auto;margin: auto;color: red'>没有相关数据!</div>"
															document.getElementById("LX"+num+"").onclick=function (){
																document.getElementById("inputValue"+num+"").style.display="none";
																document.getElementById("input"+num+"").value="";
															}
															document.getElementById("input"+num+"").onblur=function (){
																document.getElementById("inputValue"+num+"").style.display="none";
																document.getElementById("input"+num+"").value="";


															}
														}

													})


												}
											})
										}
										if (resp.data.length>0){
											addUseFunction();
										}else {
											addUseFunction();
										}
									})
								}
								else {

									document.getElementById("code"+num+"").value="";
								}



							}, 500));
							input.addEventListener('blur',function(){
								addblack(num)
							} )




						}
						for (let i = ysdatas[k].beginLocation; i <=ysdatas[k].endLocation ; i++) {
							document.getElementById("mdiv"+i+"").style.border="#1890ff solid 1px"
						}
					}




				})
			})





		}

	}



	axios({
		method:"post",
		url:"sort/selectOtherLevel",
		data:number
	}).then(function (resp){
		let datas=resp.data;

		if (datas.length!==0){
			//如果能查到证明不是最后一层分类
			let level=datas[0].level
			m=level-1;
			belong(number,m);
		}else {
			//查不到证明是最后一层分类直接当做id去查自己是几级分类
			axios({
				method:"post",
				url:"sort/selectOfSort",
				data:number
			}).then(function (resp){
				let datas=resp.data;

				m=datas[0].level;
				belong(number,m);
			})
		}
	})



}


//点击树形图发生的事件
function select (number,n){
	var foreverNumber=number;

	function click(){
		//点击哪个背景颜色改变
		let lTree = document.querySelectorAll(".l_tree_branch");
		for (let i = 0; i < lTree.length; i++) {
			if (i===n){
				lTree[i].style.background="#b4d5e1"
			}else {
				lTree[i].style.background=""
			}
		}
		//显示物料信息
		let right = document.querySelector(".right");
		right.style.display="block";
		//根据分类id查询分类信息
		axios({
			method:"post",
			url:"sort/selectOfSort",
			data:number
		}).then(function (resp){
			let datas=resp.data;
			document.getElementById("nameProduct").innerHTML=datas[0].name;
		})

		//寻找该分类下的物料数据;这里的number就是该分类的id，里面的物料的parentId就是number
		axios({
			method:"post",
			url:"product/selectByPid?currentPage=1",
			data:number
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
						'                                <div style="width: 25%;margin: auto;   border: white solid 1px"><a class="updateProduct" href="javascript:void(0)">修改</a>&nbsp;<a class="deleteProduct"  href="javascript:void(0)">删除</a></div>\n' +
						'                            </div>'
				}



				document.getElementById("content").innerHTML=formdata;
				document.getElementById("pageSum1").innerText=Math.ceil(Number(totalCounts)/20);
				document.getElementById("currentPage1").innerText=1;

				//判断输入的值是否符合数据库中的范围
				function compare (str,value){

					//  console.log("范围"+ str)
					// console.log("传入的值"+value)
					if (value.length>0){
						var pattern = /^\[.*\]$/;
						var patterns= /^\<.*\]$/;
						var pattern1= /^\<.*\>$/;
						var patterns1= /^\[.*\>$/;

						if (pattern.test(str)===true){
							str=str.split("[").join("");
							str=str.split("]").join("");
							// str=str.replace(/[a-zA-Z]/g, '');
							let strings = str.split("~");

							//判断输入的数值是否符合这个范围
							if (Number(power(value))>=Number(power(strings[0])) && Number(power(value))<=Number(power(strings[1]))){
								return true;
							}else {
								return false;
							}

						}
						else if (patterns.test(str)===true){

							str=str.split("<").join("");
							str=str.split("]").join("");
							// str=str.replace(/[a-zA-Z]/g, '');
							let strings = str.split("~");

							// console.log("开始"+ strings[0]);
							// console.log("结束"+strings[1])

							//判断输入的数值是否符合这个范围
							if (Number(power(value))>Number(power(strings[0])) && Number(power(value))<=Number(power(strings[1]))){
								return true;
							}else {
								return false;
							}
						}
						else if (pattern1.test(str)===true){

							str=str.split("<").join("");
							str=str.split(">").join("");
							// str=str.replace(/[a-zA-Z]/g, '');
							let strings = str.split("~");

							// console.log("开始"+ strings[0]);
							// console.log("结束"+strings[1])

							//判断输入的数值是否符合这个范围
							if (Number(power(value))>Number(power(strings[0])) && Number(power(value))<Number(power(strings[1]))){
								return true;
							}else {
								return false;
							}
						}
						else if (patterns1.test(str)===true){

							str=str.split("[").join("");
							str=str.split(">").join("");
							// str=str.replace(/[a-zA-Z]/g, '');
							let strings = str.split("~");

							// console.log("开始"+ strings[0]);
							// console.log("结束"+strings[1])

							//判断输入的数值是否符合这个范围
							if (Number(power(value))>=Number(power(strings[0])) && Number(power(value))<Number(power(strings[1]))){
								return true;
							}else {
								return false;
							}
						}

						else if (pattern.test(str)===false && patterns.test(str)===false && pattern1.test(str)===false && patterns1.test(str)===false) {
							alert("数据有问题，不符合格式，请联系相关人员！");

						}
					}else {
						return false;
					}


				}


				function page(pages){
					//调用axios
					axios({
						method:"post",
						url:"product/selectByPid?currentPage="+pages+"",
						data:number
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

								for (let j = 1; j <= 9; j++) {
									document.getElementById("upmdiv"+j+"").style.color="black";
								}

								black();
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
									url:"product/selectById",
									data:data[i].id
								}).then(function (resp)
								{
									let datas = resp.data;
									let sortId=datas[0].parentId;



									//自带属性
									document.getElementById("UpdateMaterialNumber").disabled=true;
									document.getElementById("updateName").value = datas[0].name;
									images(datas,1);

									document.getElementById("UpdateMaterialNumber").value = datas[0].materialNumber;
									document.getElementById("updateBrand").value = datas[0].brand;
									document.getElementById("updateDescription").value = datas[0].description;
									document.getElementById("updateNumber").value=datas[0].number;
									document.getElementById("updateUnit").value=datas[0].unit;
									let priceUnit = datas[0].priceUnit;
									let priceUnitSelect = document.getElementById("updatePriceUnit");




									document.getElementById("produceId").innerHTML=datas[0].materialNumber.substring(0,7);
									document.getElementById("nineNumber").innerHTML=datas[0].materialNumber.substring(7,16);
									let s = datas[0].materialNumber.substring(7,16);
									for (let j = 0; j < s.length; j++) {
										let m = Number(j)+Number(1);
										document.getElementById("upmdiv"+m+"").innerHTML=s[j];
									}




									//这是查询最后一层的属性，现在要做的是查询出来该层上级所有的层数

									var datass=[];
									let m;
									async function belong2(number,m){

										if (m>0){
											//获取当前分类属于哪些大类里
											axios({
												method:"post",
												url:"sort/selectOfSort",
												data:number
											}).then(function (resp){
												var datas = resp.data;
												datass=datas.concat(datass);
												m--;
												belong2(datas[0].parentId,m)
											})

										}else if (m===0) {
											//得到了自己上面的所有分类内容，然后根据分类id去查询该分类下的属性内容

											let selectAttributeUpdate="";
											var mdata=[];
											var sortAttribute=[];

											function selectSortAttribute(sd1,sd2){
												if (sd2>0){
													axios({
														method:"post",
														url:"attribute/selectAttributeNameByParentId",
														data:datass[sd1].id
													}).then(function (resp){
														let adatas=resp.data;
														sortAttribute=adatas.concat(sortAttribute);
														sd1--;
														if (sd1>=0){
															sd2--;
															selectSortAttribute(sd1,sd2);
														}else {
															sd2--;
															selectSortAttribute(0,sd2);
														}
													})
												}
												else if (sd2===0){

													//查询公共属性
													axios({
														method:"post",
														url:"attribute/selectPublic"
													}).then(function (resp){
														let ggdatas=resp.data;
														sortAttribute=ggdatas.concat(sortAttribute);
														//得到了所有的分类属性信息
														if (sortAttribute.length>0){

															//如果该产品的上面分类有属性信息
															//获取到了分类的属性信息，然后用分类属性名的id作为分类内容的parentid以及物料的id去查询物料所有的属性内容

															function selectAttributeContent(ac1,ac2){
																if (ac2>0){
																	axios({
																		method:"post",
																		url:"attribute/selectBeforeContentUpdate?id="+datas[0].id+"",
																		data:sortAttribute[ac1].id,
																	}).then(async function (resp){
																		let bdatas=resp.data;
																		if (bdatas.length > 0) {
																			mdata=bdatas.concat(mdata)
																			for (let k = 0; k < bdatas.length; k++) {
																				if (bdatas[k].unit.length>0){
																					selectAttributeUpdate += '    <div style="width:738px ;height: auto;display: flex;">\n' +
																						'                    <div  style="width: 100px;height: auto; border-right: 1px solid #d6e0ef;border-bottom: 1px solid #d6e0ef;text-align: center;display: flex">\n' +
																						'                        <div style="width: fit-content;height: auto;align-self: center;margin: auto">' + bdatas[k].name + '  <br>(单位:'+bdatas[k].unit+')</div>\n' +
																						'                    </div>\n' +
																						'                    <div class="qwe" style="width:294px ;height: auto;border-right: 1px solid #d6e0ef;border-bottom: 1px solid #d6e0ef;">\n' +
																						'                        &nbsp;<input autocomplete="off" maxlength="80" style="width: 90%;height: 20px;" type="text" name="" id=upInput' + bdatas[k].id + ' value=""> &nbsp;<b id="bb'+bdatas[k].id+'" style="display: none">*</b>\n' +
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
																						'                        &nbsp;<input autocomplete="off" maxlength="80" style="width: 90%;height: 20px;" type="text" name="" id=upInput' + bdatas[k].id + ' value=""> &nbsp;<b id="bb'+bdatas[k].id+'" style="display: none">*</b>\n' +
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

																			ac1++;
																			ac2--;
																			selectAttributeContent(ac1,ac2);

																		}else {
																			ac1++;

																			ac2--;
																			selectAttributeContent(ac1,ac2);

																		}

																	})
																}
																else if (ac2===0){

																	document.getElementById("selectAttributeUpdate").innerHTML=selectAttributeUpdate;


																	//得到了该产品的所有后添加的分类属性

																	if (mdata.length>0){

																		function lastAttribute(la1,la2){
																			if (la2>0){
																				if (mdata[la1].content!==undefined){
																					document.getElementById("upInput"+mdata[la1].id+"").value=mdata[la1].content;
																				}else {
																					document.getElementById("upInput"+mdata[la1].id+"").value="";
																				}

																				if (mdata[la1].content!==undefined){
																					if (mdata[la1].content.length>0){


																						//根据属性id和分类id去查询编码
																						let formdata={
																							attNameId:mdata[la1].parentId,
																							attValue:mdata[la1].content
																						}
																						//查询是否是范围
																						axios({
																							method:"post",
																							url:"attributeFunction/selectByAttNameId",
																							data:formdata.attNameId
																						}).then(function (resp){
																							let functionDatas=resp.data;
																							if (functionDatas[0].range===1){
																								//如果是范围，获取所有的范围
																								axios({
																									method:"post",
																									url:"attributeValue/selectByAttNameId",
																									data:formdata.attNameId
																								}).then(function (resp){
																									let datas=resp.data;
																									let m=0;
																									for (let j = 0; j < datas.length; j++) {
																										let b = compare(datas[j].attValue,formdata.attValue);

																										if (b){
																											m=j;
																											break
																										}
																									}
																									let rangeDatas={
																										attNameId:mdata[la1].parentId,
																										attValue:datas[m].attValue
																									}
																									axios({
																										method:"post",
																										url:"attributeValue/selectCode",
																										data:rangeDatas
																									}).then(function (resp){
																										if (resp.data.length>0){
																											document.getElementById("upCode"+mdata[la1].id+"").value=resp.data[0].code;
																										}

																										la1--;
																										if (la1>=0){
																											la2--;
																											lastAttribute(la1,la2);
																										}else {
																											la2--;
																											lastAttribute(0,la2);
																										}
																									})
																								})

																							}else if (functionDatas[0].range===0){
																								axios({
																									method:"post",
																									url:"attributeValue/selectCode",
																									data:formdata
																								}).then(function (resp){
																									if (resp.data.length>0){
																										document.getElementById("upCode"+mdata[la1].id+"").value=resp.data[0].code;
																									}

																									la1--;
																									if (la1>=0){
																										la2--;
																										lastAttribute(la1,la2);
																									}else {
																										la2--;
																										lastAttribute(0,la2);
																									}
																								})
																							}
																						})

																					}

																					else {
																						la1--;
																						if (la1>=0){
																							la2--;
																							lastAttribute(la1,la2);
																						}else {
																							la2--;
																							lastAttribute(0,la2);
																						}
																					}
																				}else {
																					la1--;
																					if (la1>=0){
																						la2--;
																						lastAttribute(la1,la2);
																					}else {
																						la2--;
																						lastAttribute(0,la2);
																					}
																				}


																			}
																			else if (la2===0) {
																				axios({
																					method:"post",
																					url:"mapping/selectMapping",
																					data:sortId
																				}).then(function (resp){
																					let datas=resp.data;
																					if (datas.length>0){
																						let formdatas="";
																						for (let k = 0; k <datas.length ; k++) {
																							for (let j = 0; j < mdata.length; j++) {
																								if (Number(datas[k].att_name_id)===Number(mdata[j].parentId)){
																									for (let l = datas[k].begin_location; l <=datas[k].end_location ; l++) {
																										document.getElementById("upmdiv"+l+"").style.border="#1890ff solid 1px"
																									}


																								}
																							}

																						}

																						let value2 = document.getElementById("UpdateMaterialNumber").value;
																						document.getElementById("updateSortNumberA").innerHTML=value2.substring(0,7);
																						document.getElementById("updateSerialNumber").innerHTML=value2.substring(16,18);
																						for (let k = 0; k <datas.length ; k++) {
																							for (let j = 0; j < mdata.length; j++) {
																								if (Number(datas[k].att_name_id) === Number(mdata[j].parentId)) {
																									document.getElementById("upInput"+mdata[j].id+"").onfocus=function (){
																										for (let l = datas[k].begin_location; l <=datas[k].end_location ; l++) {
																											document.getElementById("upmdiv"+l+"").style.color="red"
																										}

																										for (let l = 0; l < datas.length; l++) {
																											if (Number(datas[l].att_name_id)!==Number(mdata[j].parentId)){
																												for (let o =datas[l].begin_location; o <= datas[l].end_location; o++) {
																													document.getElementById("upmdiv"+o+"").style.color="black"
																												}
																											}
																										}



																									}
																									//查询流水码的功能
																									//查询是否开启了流水码功能
																									axios({
																										method:"post",
																										url:"attributeFunction/selectByAttNameId",
																										data:datas[k].att_name_id
																									}).then(function (resp)
																									{
																										let ableCode=resp.data;
																										let serialCode = ableCode[0].serialCode;
																										if (serialCode===1){
																											//开启了流水码功能，限制输入和编码
																											document.getElementById("upInput"+mdata[j].id+"").disabled=true;
																											document.getElementById("upCode"+mdata[j].id+"").value=document.getElementById("upInput" + mdata[j].id + "").value;

																										}
																									})

																									let input = document.getElementById("upInput"+mdata[j].id+"");
																									//查询该属性下面是否有属性值
																									axios({
																										method:"post",
																										url:"attributeValue/selectIfCode",
																										data:mdata[j].parentId
																									}).then(function (resp){
																										let datas=resp.data;
																										if (datas===true){
																											input.placeholder="请输入查询！"
																										}
																									})


																									let num=mdata[j].parentId;
																									input.addEventListener('input', debounce(function () {
																										let value = input.value.trim();
																										document.getElementById("upCode"+mdata[j].id+"").value="";
																										if (value.length>0){
																											axios({
																												method:"post",
																												url:"attributeValue/selectByAttNameId",
																												data:mdata[j].parentId
																											}).then(function (resp)
																											{
																												if (resp.data.length>0){
																													//查询是否是范围
																													axios({
																														method:"post",
																														url:"attributeFunction/selectByAttNameId",
																														data:num
																													}).then(function (resp){
																														let rangeDatas=resp.data;
																														function autoAndNo(value,num,s){
																															//得到了输入框的值，先判断能不能找到这个值
																															let formdata={
																																attNameId:num,
																																attValue:value
																															}
																															//查询这个值是否存在
																															axios({
																																method:"post",
																																url:"attributeValue/selectValueExistAdd",
																																data:formdata
																															}).then(function (resp){
																																let attValueBoolean=resp.data;
																																if (attValueBoolean===true){
																																	//该值存在
																																	//查看该属性是否在映射中
																																	axios({
																																		method:"post",
																																		url:"mapping/selectMapping",
																																		data:sortId//分类id
																																	}).then(function (resp){
																																		let mappingData=resp.data;
																																		let map=false;
																																		let index=0;
																																		for (let j = 0; j < mappingData.length; j++) {
																																			if (Number(num)===mappingData[j].att_name_id){
																																				map=true;
																																				index=j;
																																				break;
																																			}

																																		}

																																		if (map===true){
																																			//在映射中，根据长度和起始位置改编物料编码
																																			//值已经存在，查询这个值的编码
																																			let value1 = document.getElementById("upInput"+s+"").value.trim();
																																			let data={
																																				attNameId:num,
																																				attValue:value1
																																			}
																																			axios({
																																				method:"post",
																																				url:"attributeValue/selectCode",
																																				data:data
																																			}).then(function (resp){
																																				document.getElementById("upCode"+s+"").value=resp.data[0].code;
																																				let nine = document.getElementById("nineNumber").innerHTML;
																																				let str = nine;
																																				//判断输入的长度和编码长度

																																				let replaceStr =resp.data[0].code;//要替换的字符串
																																				// document.getElementById("updiv"+s+"").innerHTML=replaceStr;
																																				let startIndex = mappingData[index].begin_location;


																																				let endIndex = mappingData[index].end_location;

																																				let newStr = str.slice(0, startIndex-1) + replaceStr + str.slice(endIndex);

																																				document.getElementById("nineNumber").innerHTML=newStr;
																																				//获取分类编码
																																				let sortId = document.getElementById("produceId").innerHTML;
																																				document.getElementById("UpdateMaterialNumber").value=sortId+newStr+lsn;
																																				for (let j = 0; j < newStr.length; j++) {
																																					let m=Number(j)+Number(1);
																																					document.getElementById("upmdiv"+m+"").innerHTML=newStr[j];
																																				}
																																			})



																																		}else {
																																			//不在映射中
																																			//值已经存在，查询这个值的编码
																																			let value1 = document.getElementById("upInput"+s+"").value.trim();
																																			let data={
																																				attNameId:num,
																																				attValue:value1
																																			}
																																			axios({
																																				method:"post",
																																				url:"attributeValue/selectCode",
																																				data:data
																																			}).then(function (resp){
																																				document.getElementById("upCode"+s+"").value=resp.data[0].code;
																																			})
																																		}

																																	})

																																}
																																else {
																																	//该值不存在,查询是否开启了值作为编码的功能
																																	axios({
																																		method:"post",
																																		url:"attributeFunction/selectIfAutoCode",
																																		data:num
																																	}).then(function (resp){
																																		if (resp.data===true){
																																			//开启了值作为编码的功能
																																			//先判断输入值的格式是否符合做编码的格式，然后判断长度是否符合编码的长度，然后再判断内容是否重复
																																			var pattern = /^[0-9A-Za-z]+$/;
																																			if (pattern.test(value)===true){
																																				//符合标准，判断长度
																																				axios({
																																					method:"post",
																																					url:"attribute/selectById",
																																					data:num
																																				}).then(function (resp){
																																					let datas=resp.data;
																																					if (Number(datas[0].length)>=Number(value.length)){
																																						//长度符合，判断作为编码是否重复
																																						let codeData={
																																							attNameId:"",
																																							attValue:"",
																																							code:""
																																						}
																																						if (Number(datas[0].length)===Number(value.length)){
																																							codeData={
																																								attNameId:num,
																																								attValue:value,
																																								code:value
																																							}
																																						}else {
																																							let jnum=Number(datas[0].length)-Number(value.length);
																																							let str="";
																																							for (let j = 0; j < jnum; j++) {
																																								str+="0"
																																							}
																																							codeData={
																																								attNameId:num,
																																								attValue:value,
																																								code:str+value
																																							}
																																						}

																																						//查看该属性是否在映射中
																																						axios({
																																							method:"post",
																																							url:"mapping/selectMapping",
																																							data:sortId //分类id
																																						}).then(function (resp){
																																							let mappingData=resp.data;
																																							let map=false;
																																							let index=0;
																																							for (let j = 0; j < mappingData.length; j++) {
																																								if (Number(num)===mappingData[j].att_name_id){
																																									map=true;
																																									index=j;
																																									break;
																																								}

																																							}

																																							if (map===true){
																																								//在映射中，根据长度和起始位置改编物料编码
																																								//查看这个值是否已经被使用了
																																								let data={
																																									attNameId:num,
																																									attValue:document.getElementById("upInput"+s+"").value.trim()

																																								}
																																								axios({
																																									method:"post",
																																									url:"attributeValue/selectValueExistAdd",
																																									data:data
																																								}).then(function (resp){
																																									if (resp.data===true){
																																										//值已经存在，查询这个值的编码
																																										let value1 = document.getElementById("upInput"+s+"").value.trim();
																																										let data={
																																											attNameId:num,
																																											attValue:value1
																																										}
																																										axios({
																																											method:"post",
																																											url:"attributeValue/selectCode",
																																											data:data
																																										}).then(function (resp){
																																											document.getElementById("upCode"+s+"").value=resp.data[0].code;
																																										})
																																									}else {

																																										//值不存在.查询该编码是否已经存在
																																										axios({
																																											method:"post",
																																											url:"attributeValue/selectCodeExist",
																																											data:codeData
																																										}).then(function (resp){
																																											if (resp.data===true){
																																												//编码存在，提示并清空输入框
																																												document.getElementById("upInput"+s+"").value="";
																																												document.getElementById("upCode"+s+"").value="";
																																												alert("该编码已存在，不能使用该值作为编码使用！")
																																											}
																																											else {
																																												//编码不存在
																																												//得到了属性值的编码
																																												let nine = document.getElementById("nineNumber").innerHTML;
																																												let str = nine;
																																												//判断输入的长度和编码长度

																																												let replaceStr =codeData.code;//要替换的字符串

																																												document.getElementById("upCode"+s+"").value=codeData.code;
																																												// document.getElementById("updiv"+s+"").innerHTML=replaceStr;
																																												let startIndex = mappingData[index].begin_location;


																																												let endIndex = mappingData[index].end_location;

																																												let newStr = str.slice(0, startIndex-1) + replaceStr + str.slice(endIndex);

																																												document.getElementById("nineNumber").innerHTML=newStr;
																																												//获取分类编码
																																												let sortId = document.getElementById("produceId").innerHTML;
																																												document.getElementById("UpdateMaterialNumber").value=sortId+newStr+lsn;
																																												for (let j = 0; j < newStr.length; j++) {
																																													let m=Number(j)+Number(1);
																																													document.getElementById("upmdiv"+m+"").innerHTML=newStr[j];
																																												}
																																											}
																																										})

																																									}
																																								})

																																							}else {
																																								//不在映射中
																																								//查看这个值是否已经被使用了
																																								let data={
																																									attNameId:num,
																																									attValue:document.getElementById("upInput"+s+"").value.trim()

																																								}
																																								axios({
																																									method:"post",
																																									url:"attributeValue/selectValueExistAdd",
																																									data:data
																																								}).then(function (resp){
																																									if (resp.data===true){
																																										//值已经存在，不在映射中，查询编码并显示
																																										axios({
																																											method:"post",
																																											url:"attributeValue/selectValueExistAdd",
																																											data:data
																																										}).then(function (resp){
																																											document.getElementById("upCode"+s+"").value=resp.data[0].code;
																																										})
																																									}else {
																																										//值不存在，将编码显示
																																										document.getElementById("upCode"+s+"").value=codeData.code;
																																									}
																																								})

																																							}

																																						})



																																					}else {
																																						let value1 = document.getElementById("upInput"+s+"").value.trim();
																																						if(value1.length>0){
																																							alert("输入值的长度与编码长度不符，请核对后在进行操作！");
																																						}
																																						document.getElementById("upInput"+s+"").value="";
																																						document.getElementById("upCode"+s+"").value="";


																																					}
																																				})
																																			}else {
																																				let value2 = document.getElementById("upInput"+s+"").value.trim();
																																				if(value2.length>0){
																																					alert("输入值的格式不适合作为编码，请核对后再进行操作！");
																																				}
																																				document.getElementById("upInput"+s+"").value="";
																																				document.getElementById("upCode"+s+"").value="";
																																			}
																																		}
																																		else {
																																			//该值不存在,查询是否开启了值作为编码的功能
																																			axios({
																																				method:"post",
																																				url:"attributeFunction/selectIfAutoCode",
																																				data:num
																																			}).then(function (resp){
																																				if (resp.data===true){
																																					//开启了值作为编码的功能
																																					//先判断输入值的格式是否符合做编码的格式，然后判断长度是否符合编码的长度，然后再判断内容是否重复
																																					var pattern = /^[0-9A-Za-z]+$/;
																																					if (pattern.test(value)===true){
																																						//符合标准，判断长度
																																						axios({
																																							method:"post",
																																							url:"attribute/selectById",
																																							data:num
																																						}).then(function (resp){
																																							let datas=resp.data;
																																							if (Number(datas[0].length)>=Number(value.length)){
																																								//长度符合，判断作为编码是否重复
																																								let codeData={
																																									attNameId:"",
																																									attValue:"",
																																									code:""
																																								}
																																								if (Number(datas[0].length)===Number(value.length)){
																																									codeData={
																																										attNameId:num,
																																										attValue:value,
																																										code:value
																																									}
																																								}else {
																																									let jnum=Number(datas[0].length)-Number(value.length);
																																									let str="";
																																									for (let j = 0; j < jnum; j++) {
																																										str+="0"
																																									}
																																									codeData={
																																										attNameId:num,
																																										attValue:value,
																																										code:str+value
																																									}
																																								}

																																								//查看该属性是否在映射中
																																								axios({
																																									method:"post",
																																									url:"mapping/selectMapping",
																																									data:sortId //分类id
																																								}).then(function (resp){
																																									let mappingData=resp.data;
																																									let map=false;
																																									let index=0;
																																									for (let j = 0; j < mappingData.length; j++) {
																																										if (Number(num)===mappingData[j].att_name_id){
																																											map=true;
																																											index=j;
																																											break;
																																										}

																																									}

																																									if (map===true){
																																										//在映射中，根据长度和起始位置改编物料编码
																																										//查看这个值是否已经被使用了
																																										let data={
																																											attNameId:num,
																																											attValue:document.getElementById("upInput"+s+"").value.trim()

																																										}
																																										axios({
																																											method:"post",
																																											url:"attributeValue/selectValueExistAdd",
																																											data:data
																																										}).then(function (resp){
																																											if (resp.data===true){
																																												//值已经存在，查询这个值的编码
																																												let value1 = document.getElementById("upInput"+s+"").value.trim();
																																												let data={
																																													attNameId:num,
																																													attValue:value1
																																												}
																																												axios({
																																													method:"post",
																																													url:"attributeValue/selectCode",
																																													data:data
																																												}).then(function (resp){
																																													document.getElementById("upCode"+s+"").value=resp.data[0].code;
																																												})
																																											}else {
																																												//值不存在.查询该编码是否已经存在
																																												axios({
																																													method:"post",
																																													url:"attributeValue/selectCodeExist",
																																													data:codeData
																																												}).then(function (resp){
																																													if (resp.data===true){
																																														//编码存在，提示并清空输入框
																																														document.getElementById("upInput"+s+"").value="";
																																														document.getElementById("upCode"+s+"").value="";
																																														alert("该编码已存在，不能使用该值作为编码使用！")
																																													}
																																													else {
																																														//编码不存在
																																														//得到了属性值的编码
																																														let nine = document.getElementById("nineNumber").innerHTML;
																																														let str = nine;
																																														//判断输入的长度和编码长度

																																														let replaceStr =codeData.code;//要替换的字符串
																																														document.getElementById("upCode"+s+"").value=codeData.code;
																																														let startIndex = mappingData[index].begin_location;


																																														let endIndex = mappingData[index].end_location;
																																														let newStr = str.slice(0, startIndex-1) + replaceStr + str.slice(endIndex);

																																														document.getElementById("nineNumber").innerHTML=newStr;

																																														//获取分类编码
																																														let sortId = document.getElementById("produceId").innerHTML;
																																														document.getElementById("UpdateMaterialNumber").value=sortId+newStr+lsn;
																																														for (let j = 0; j < newStr.length; j++) {
																																															let m=Number(j)+Number(1);
																																															document.getElementById("upmdiv"+m+"").innerHTML=newStr[j];
																																														}
																																													}
																																												})

																																											}
																																										})

																																									}
																																									else {
																																										//不在映射中
																																										//查看这个值是否已经被使用了
																																										let data={
																																											attNameId:num,
																																											attValue:document.getElementById("upInput"+s+"").value.trim()

																																										}
																																										axios({
																																											method:"post",
																																											url:"attributeValue/selectValueExistAdd",
																																											data:data
																																										}).then(function (resp){
																																											if (resp.data===true){
																																												//值已经存在，不在映射中，查询编码并显示
																																												axios({
																																													method:"post",
																																													url:"aAttributeValue/selectValueExistAdd",
																																													data:data
																																												}).then(function (resp){
																																													document.getElementById("upCode"+s+"").value=resp.data[0].code;
																																												})
																																											}else {
																																												//值不存在，将编码显示
																																												document.getElementById("upCode"+s+"").value=codeData.code;
																																											}
																																										})
																																									}

																																								})



																																							}
																																							else {
																																								let value1 = document.getElementById("upInput"+s+"").value;
																																								if(value1.length>0){
																																									alert("输入值的长度与编码长度不符，请核对后在进行操作！");
																																								}
																																								document.getElementById("upInput"+s+"").value="";
																																								document.getElementById("upCode"+s+"").value="";


																																							}
																																						})
																																					}else {
																																						let value2 = document.getElementById("upInput"+s+"").value;
																																						if(value2.length>0){
																																							alert("输入值的格式不适合作为编码，请核对后再进行操作！");
																																						}
																																						document.getElementById("upInput"+s+"").value="";
																																						document.getElementById("upCode"+s+"").value="";
																																					}
																																				}
																																				else {
																																					//未开启值作为编码的功能,查看是否有编码表存在,查询是否开启了顺序编码的功能
																																					axios({
																																						method:"post",
																																						url:"attributeFunction/selectIfOrderCode",
																																						data:num
																																					}).then(function (resp){
																																						if (resp.data===true){
																																							//开启了顺序编码的功能
																																							//获取长度
																																							axios({
																																								method:"post",
																																								url:"attribute/selectById",
																																								data:num
																																							}).then(function (resp){
																																								let datas=resp.data;
																																								let length = datas[0].length;
																																								//查询该属性下有多少个编码
																																								axios({
																																									method:"post",
																																									url:"attributeValue/selectByAttNameId",
																																									data:num
																																								}).then(function (resp){
																																									let CodeNumber=resp.data.length;
																																									let number= Number(CodeNumber)+Number(1);
																																									let generateCode1 = generateCode(length,number,list,"");
																																									function orderNoRepeat(generateCode1){
																																										//调用方法获取编码

																																										//验证编码是否重复
																																										let codeData={
																																											attNameId:num,
																																											code:generateCode1,
																																										}
																																										axios({
																																											method:"post",
																																											url:"attributeValue/selectCodeExistAdd",
																																											data:codeData
																																										}).then(function (resp){
																																											if (resp.data===true){
																																												//存在
																																												number++;
																																												let generateCode1 = generateCode(length,number,list,"");
																																												orderNoRepeat(generateCode1);

																																											}else if (resp.data===false){
																																												//不存在
																																												//查看该属性是否在映射中
																																												axios({
																																													method:"post",
																																													url:"mapping/selectMapping",
																																													data:sortId //分类id
																																												}).then(function (resp){
																																													let mappingData=resp.data;
																																													let map=false;
																																													let index=0;
																																													for (let j = 0; j < mappingData.length; j++) {
																																														if (Number(num)===mappingData[j].att_name_id){
																																															map=true;
																																															index=j;
																																															break;
																																														}

																																													}
																																													if (map===true){
																																														//值不存在
																																														//得到了属性值的编码
																																														let nine = document.getElementById("nineNumber").innerHTML;
																																														let str = nine;
																																														//判断输入的长度和编码长度

																																														let replaceStr =codeData.code;//要替换的字符串

																																														document.getElementById("upCode"+s+"").value=codeData.code;
																																														// document.getElementById("updiv"+s+"").innerHTML=replaceStr;
																																														let startIndex = mappingData[index].begin_location;


																																														let endIndex = mappingData[index].end_location;

																																														let newStr = str.slice(0, startIndex-1) + replaceStr + str.slice(endIndex);

																																														document.getElementById("nineNumber").innerHTML=newStr;
																																														//获取分类编码
																																														let sortId = document.getElementById("produceId").innerHTML;
																																														document.getElementById("UpdateMaterialNumber").value=sortId+newStr+lsn;
																																														for (let j = 0; j < newStr.length; j++) {
																																															let m=Number(j)+Number(1);
																																															document.getElementById("upmdiv"+m+"").innerHTML=newStr[j];
																																														}
																																													}
																																													else {
																																														//不在映射中
																																														//查看这个值是否已经被使用了
																																														let data={
																																															attNameId:num,
																																															attValue:document.getElementById("upInput"+s+"").value.trim()

																																														}
																																														axios({
																																															method:"post",
																																															url:"attributeValue/selectValueExistAdd",
																																															data:data
																																														}).then(function (resp){
																																															if (resp.data===true){
																																																//值已经存在，不在映射中，查询编码并显示
																																																axios({
																																																	method:"post",
																																																	url:"attributeValue/selectValueExistAdd",
																																																	data:data
																																																}).then(function (resp){
																																																	document.getElementById("upCode"+s+"").value=resp.data[0].code;
																																																})
																																															}else {
																																																//值不存在，将编码显示
																																																document.getElementById("upCode"+s+"").value=codeData.code;
																																															}
																																														})
																																													}
																																												})


																																											}
																																										})
																																									}
																																									orderNoRepeat(generateCode1);


																																								})
																																							})
																																						}
																																						else {
																																							//未开启值作为编码，也未开启顺序编码
																																							axios({
																																								method:"post",
																																								url:"attributeValue/selectByAttNameId",
																																								data:num
																																							}).then(function (resp){
																																								if (resp.data.length>0){
																																									//有编码表
																																									document.getElementById("upInput"+s+"").value="";
																																									document.getElementById("upCode"+s+"").value="";
																																								}else {
																																									//没有编码表
																																									document.getElementById("upCode"+s+"").value="";
																																								}
																																							})
																																						}
																																					})



																																				}
																																			})
																																		}
																																	})
																																}
																															})
																														}
																														if (rangeDatas[0].range===0){
																															//0表示是值
																															//模糊查询
																															axios({
																																method:"post",
																																url:"attributeValue/inputLX?attNameId="+num+"",
																																data:value
																															}).then(function (resp){
																																let lxDatas=resp.data;


																																if (lxDatas.length>0){
																																	var formdata="";
																																	if (lxDatas.length>5){
																																		for (let i = 0; i < 5; i++) {
																																			formdata+="<div class='ULX"+num+"' style='width: 100%;height: auto;margin: auto'><pre>"+lxDatas[i].att_value+"</pre></div>"
																																		}
																																	}else if (lxDatas.length===1){
																																		formdata+="<div class='ULX"+num+"' style='width: 100%;height: auto;margin: auto'><pre>"+lxDatas[0].att_value+"</pre></div>"
																																	}
																																	else{
																																		for (let i = 0; i <lxDatas.length; i++) {
																																			formdata+="<div class='ULX"+num+"' style='width: 100%;height: auto;margin: auto'><pre>"+lxDatas[i].att_value+"</pre></div>"
																																		}
																																	}

																																	document.getElementById("inputUpValue"+mdata[j].id+"").innerHTML=formdata;


																																	//绑定触发变色
																																	let lxs = document.querySelectorAll(".ULX"+num+"");
																																	for (let j = 0; j < lxs.length; j++) {
																																		lxs[j].onmouseover=function (){
																																			lxs[j].style.backgroundColor="white"
																																		}
																																		lxs[j].onmouseout=function (){
																																			lxs[j].style.backgroundColor="#e0e0e0"
																																		}

																																	}

																																	document.getElementById("inputUpValue"+mdata[j].id+"").style.display="";


																																	//先去除所有的绑定方法
																																	document.getElementById("inputUpValue"+mdata[j].id+"").onmouseout=null;
																																	document.getElementById("inputUpValue"+mdata[j].id+"").onmouseover=null;
																																	document.getElementById("upInput"+mdata[j].id+"").onblur=null;

																																	//失去光标要执行的方法
																																	document.getElementById("upInput"+mdata[j].id+"").onblur=function (){
																																		//没有动作，失去光标，获取输入的值
																																		let value1 = document.getElementById("upInput"+mdata[j].id+"").value.trim();
																																		if (value1.length>0){
																																			autoAndNo(value1,num,mdata[j].id);
																																		}
																																		else {
																																			document.getElementById("upInput"+mdata[j].id+"").value="";
																																			//查看该属性是否在映射中
																																			axios({
																																				method:"post",
																																				url:"mapping/selectMapping",
																																				data:sortId //分类id
																																			}).then(function (resp) {
																																				let mappingData = resp.data;
																																				let map = false;
																																				let index = 0;
																																				for (let j = 0; j < mappingData.length; j++) {
																																					if (Number(num) === mappingData[j].att_name_id) {
																																						map = true;
																																						index = j;
																																						break;
																																					}

																																				}

																																				if (map === true) {
																																					//在映射中，根据长度和起始位置改编物料编码

																																					let nine = document.getElementById("nineNumber").innerHTML;
																																					let str = nine;
																																					//判断输入的长度和编码长度
																																					let replaceStr ="";
																																					for (let j = 0; j < mappingData[index].length; j++) {
																																						replaceStr+="0";
																																					}

																																					// document.getElementById("updiv"+s+"").innerHTML=replaceStr;
																																					let startIndex = mappingData[index].begin_location;


																																					let endIndex = mappingData[index].end_location;

																																					let newStr = str.slice(0, startIndex-1) + replaceStr + str.slice(endIndex);

																																					document.getElementById("nineNumber").innerHTML=newStr;
																																					//获取分类编码
																																					let sortId = document.getElementById("produceId").innerHTML;
																																					document.getElementById("UpdateMaterialNumber").value=sortId+newStr+lsn;
																																					for (let j = 0; j < newStr.length; j++) {
																																						let m=Number(j)+Number(1);
																																						document.getElementById("upmdiv"+m+"").innerHTML=newStr[j];
																																					}



																																				}
																																			})
																																		}

																																		document.getElementById("inputUpValue"+mdata[j].id+"").style.display="none";

																																	}
																																	//因为点击会默认执行失去光标的方法，所以鼠标移动到这里先将失去光标的方法置空，移出后再添加回来
																																	document.getElementById("inputUpValue"+mdata[j].id+"").onmouseover=function (){
																																		document.getElementById("upInput"+mdata[j].id+"").onblur=null;
																																	}
																																	document.getElementById("inputUpValue"+mdata[j].id+"").onmouseout=function (){
																																		document.getElementById("upInput"+mdata[j].id+"").onblur=function (){
																																			//没有动作，失去光标，获取输入的值
																																			let value1 = document.getElementById("upInput"+mdata[j].id+"").value.trim();
																																			if (value1.length>0){
																																				autoAndNo(value1,num,mdata[j].id);
																																			}
																																			else {
																																				document.getElementById("upInput"+mdata[j].id+"").value="";
																																				//查看该属性是否在映射中
																																				axios({
																																					method:"post",
																																					url:"mapping/selectMapping",
																																					data:sortId //分类id
																																				}).then(function (resp) {
																																					let mappingData = resp.data;
																																					let map = false;
																																					let index = 0;
																																					for (let j = 0; j < mappingData.length; j++) {
																																						if (Number(num) === mappingData[j].att_name_id) {
																																							map = true;
																																							index = j;
																																							break;
																																						}

																																					}

																																					if (map === true) {
																																						//在映射中，根据长度和起始位置改编物料编码

																																						let nine = document.getElementById("nineNumber").innerHTML;
																																						let str = nine;
																																						//判断输入的长度和编码长度
																																						let replaceStr ="";
																																						for (let j = 0; j < mappingData[index].length; j++) {
																																							replaceStr+="0";
																																						}

																																						// document.getElementById("updiv"+s+"").innerHTML=replaceStr;
																																						let startIndex = mappingData[index].begin_location;


																																						let endIndex = mappingData[index].end_location;

																																						let newStr = str.slice(0, startIndex-1) + replaceStr + str.slice(endIndex);

																																						document.getElementById("nineNumber").innerHTML=newStr;
																																						//获取分类编码
																																						let sortId = document.getElementById("produceId").innerHTML;
																																						document.getElementById("UpdateMaterialNumber").value=sortId+newStr+lsn;
																																						for (let j = 0; j < newStr.length; j++) {
																																							let m=Number(j)+Number(1);
																																							document.getElementById("upmdiv"+m+"").innerHTML=newStr[j];
																																						}



																																					}
																																				})
																																			}

																																			document.getElementById("inputUpValue"+mdata[j].id+"").style.display="none";

																																		}
																																	}

																																	//添加点击查询到的结果执行方法
																																	lxs = document.querySelectorAll(".ULX"+num+"");

																																	// for (let j = 0; j < lxs.length; j++) {
																																	// 	lxs[j].onclick=function (){
																																	// 		//将点击的值放在输入框中
																																	// 		document.getElementById("upInput"+mdata[j].id+"").value=lxDatas[j].att_value;
																																	// 		autoAndNo(lxDatas[j].att_value,num,mdata[j].id);
																																	// 		document.getElementById("inputUpValue"+mdata[j].id+"").style.display="none";
																																	//
																																	//
																																	// 	}
																																	// }

																																	for (let l = 0; l < lxs.length; l++) {
																																		lxs[l].onclick=function (){
																																			//将点击的值放在输入框中
																																			document.getElementById("upInput"+mdata[j].id+"").value=lxDatas[l].att_value;
																																			autoAndNo(lxDatas[l].att_value,num,mdata[j].id);
																																			document.getElementById("inputUpValue"+mdata[j].id+"").style.display="none";
																																		}
																																	}


																																}
																																else {

																																	//先去除所有的绑定方法
																																	document.getElementById("inputUpValue"+mdata[j].id+"").onmouseout=null;
																																	document.getElementById("inputUpValue"+mdata[j].id+"").onmouseover=null;
																																	document.getElementById("upInput"+mdata[j].id+"").onblur=null;
																																	document.getElementById("inputUpValue"+mdata[j].id+"").style.display="";
																																	document.getElementById("inputUpValue"+mdata[j].id+"").innerHTML="<div class='ULX"+num+"' style='width: 100%;height: auto;margin: auto;color: red'>没有相关数据!</div>"

																																	//失去光标要执行的方法
																																	document.getElementById("upInput"+mdata[j].id+"").onblur=function (){
																																		//没有动作，失去光标，获取输入的值
																																		let value1 = document.getElementById("upInput"+mdata[j].id+"").value.trim();
																																		if (value1.length>0){
																																			autoAndNo(value1,num,mdata[j].id);
																																		}
																																		else {
																																			document.getElementById("upInput"+mdata[j].id+"").value="";
																																			//查看该属性是否在映射中
																																			axios({
																																				method:"post",
																																				url:"mapping/selectMapping",
																																				data:sortId //分类id
																																			}).then(function (resp) {
																																				let mappingData = resp.data;
																																				let map = false;
																																				let index = 0;
																																				for (let j = 0; j < mappingData.length; j++) {
																																					if (Number(num) === mappingData[j].att_name_id) {
																																						map = true;
																																						index = j;
																																						break;
																																					}

																																				}

																																				if (map === true) {
																																					//在映射中，根据长度和起始位置改编物料编码

																																					let nine = document.getElementById("nineNumber").innerHTML;
																																					let str = nine;
																																					//判断输入的长度和编码长度
																																					let replaceStr ="";
																																					for (let j = 0; j < mappingData[index].length; j++) {
																																						replaceStr+="0";
																																					}

																																					// document.getElementById("updiv"+s+"").innerHTML=replaceStr;
																																					let startIndex = mappingData[index].begin_location;


																																					let endIndex = mappingData[index].end_location;

																																					let newStr = str.slice(0, startIndex-1) + replaceStr + str.slice(endIndex);

																																					document.getElementById("nineNumber").innerHTML=newStr;
																																					//获取分类编码
																																					let sortId = document.getElementById("produceId").innerHTML;
																																					document.getElementById("UpdateMaterialNumber").value=sortId+newStr+lsn;
																																					for (let j = 0; j < newStr.length; j++) {
																																						let m=Number(j)+Number(1);
																																						document.getElementById("upmdiv"+m+"").innerHTML=newStr[j];
																																					}



																																				}
																																			})
																																		}
																																		document.getElementById("inputUpValue"+mdata[j].id+"").style.display="none";

																																	}

																																}
																															})
																														}
																														else if (rangeDatas[0].range===1){

																															//判断输入的值是否符合数据库中的范围
																															function compare (str,value){

																																//  console.log("范围"+ str)
																																// console.log("传入的值"+value)
																																if (value.length>0){
																																	var pattern = /^\[.*\]$/;
																																	var patterns= /^\<.*\]$/;
																																	var pattern1= /^\<.*\>$/;
																																	var patterns1= /^\[.*\>$/;

																																	if (pattern.test(str)===true){
																																		str=str.split("[").join("");
																																		str=str.split("]").join("");
																																		// str=str.replace(/[a-zA-Z]/g, '');
																																		let strings = str.split("~");

																																		//判断输入的数值是否符合这个范围
																																		if (Number(power(value))>=Number(power(strings[0])) && Number(power(value))<=Number(power(strings[1]))){
																																			return true;
																																		}else {
																																			return false;
																																		}

																																	}
																																	else if (patterns.test(str)===true){

																																		str=str.split("<").join("");
																																		str=str.split("]").join("");
																																		// str=str.replace(/[a-zA-Z]/g, '');
																																		let strings = str.split("~");

																																		// console.log("开始"+ strings[0]);
																																		// console.log("结束"+strings[1])

																																		//判断输入的数值是否符合这个范围
																																		if (Number(power(value))>Number(power(strings[0])) && Number(power(value))<=Number(power(strings[1]))){
																																			return true;
																																		}else {
																																			return false;
																																		}
																																	}
																																	else if (pattern1.test(str)===true){

																																		str=str.split("<").join("");
																																		str=str.split(">").join("");
																																		// str=str.replace(/[a-zA-Z]/g, '');
																																		let strings = str.split("~");

																																		// console.log("开始"+ strings[0]);
																																		// console.log("结束"+strings[1])

																																		//判断输入的数值是否符合这个范围
																																		if (Number(power(value))>Number(power(strings[0])) && Number(power(value))<Number(power(strings[1]))){
																																			return true;
																																		}else {
																																			return false;
																																		}
																																	}
																																	else if (patterns1.test(str)===true){

																																		str=str.split("[").join("");
																																		str=str.split(">").join("");
																																		// str=str.replace(/[a-zA-Z]/g, '');
																																		let strings = str.split("~");

																																		// console.log("开始"+ strings[0]);
																																		// console.log("结束"+strings[1])

																																		//判断输入的数值是否符合这个范围
																																		if (Number(power(value))>=Number(power(strings[0])) && Number(power(value))<Number(power(strings[1]))){
																																			return true;
																																		}else {
																																			return false;
																																		}
																																	}

																																	else if (pattern.test(str)===false && patterns.test(str)===false && pattern1.test(str)===false && patterns1.test(str)===false) {
																																		alert("数据有问题，不符合格式，请联系相关人员！");

																																	}
																																}else {
																																	return false;
																																}


																															}
																															//1表示是范围,获取所有的范围值
																															axios({
																																method:"post",
																																url:"attributeValue/selectByAttNameId",
																																data:num
																															}).then(function (resp){
																																let datas=resp.data;
																																let b;
																																let num2=mdata[j].id
																																for (let i = 0; i < datas.length; i++) {
																																	b = compare(datas[i].attValue,value);
																																	if (b===true){
																																		document.getElementById("inputUpValue"+num2+"").innerHTML="<div id='LX"+num+"' style='width: 100%;height: auto;margin: auto;'><pre>"+datas[i].attValue+"</pre></div>"
																																		document.getElementById("inputUpValue"+num2+"").style.display="";
																																		function blurAndClick (){

																																			document.getElementById("inputUpValue"+num2+"").style.display="none";
																																			document.getElementById("upCode"+num2+"").value=datas[i].code;
																																			// document.getElementById("updiv"+num2+"").innerHTML=datas[i].code;
																																			//查询这个属性是否在映射中
																																			axios({
																																				method:"post",
																																				url:"mapping/selectMapping",
																																				data:sortId//分类id
																																			}).then(function (resp){
																																				let mappingData=resp.data;
																																				let map=false;
																																				let index=0;
																																				for (let j = 0; j < mappingData.length; j++) {
																																					if (Number(num)===mappingData[j].att_name_id){
																																						map=true;
																																						index=j;
																																						break;
																																					}

																																				}

																																				if (map===true){
																																					//存在映射中
																																					let nine = document.getElementById("nineNumber").innerHTML;
																																					let str = nine;
																																					//判断输入的长度和编码长度

																																					let replaceStr =datas[i].code;//要替换的字符串
																																					let startIndex = mappingData[index].begin_location;

																																					let endIndex = mappingData[index].end_location;

																																					let newStr = str.slice(0, startIndex-1) + replaceStr + str.slice(endIndex);

																																					document.getElementById("nineNumber").innerHTML=newStr;
																																					//获取分类编码
																																					let sortId = document.getElementById("produceId").innerHTML;
																																					document.getElementById("UpdateMaterialNumber").value=sortId+newStr+lsn;
																																					for (let j = 0; j < newStr.length; j++) {
																																						let m=Number(j)+Number(1);
																																						document.getElementById("upmdiv"+m+"").innerHTML=newStr[j];
																																					}
																																				}

																																			})


																																		}
																																		document.getElementById("LX"+num+"").onclick=blurAndClick;
																																		let trim = document.getElementById("upInput"+num2+"").value.trim();
																																		if (trim.length>0){
																																			document.getElementById("upInput"+num2+"").onblur=blurAndClick;
																																		}
																																		else {
																																			document.getElementById("upInput"+num2+"").value="";
																																		}

																																		break;
																																	}

																																}

																																if (b===false) {
																																	document.getElementById("inputUpValue"+num2+"").style.display="";
																																	document.getElementById("inputUpValue"+num2+"").innerHTML="<div id='LX"+num+"' style='width: 100%;height: auto;margin: auto;color: red'>没有相关数据!</div>"
																																	document.getElementById("LX"+num+"").onclick=function (){
																																		document.getElementById("inputUpValue"+num2+"").style.display="none";
																																		document.getElementById("upInput"+num2+"").value="";
																																	}
																																	document.getElementById("upInput"+num2+"").onblur=function (){
																																		document.getElementById("inputUpValue"+num2+"").style.display="none";
																																		document.getElementById("upInput"+num2+"").value="";

																																	}
																																}

																															})


																														}
																													})
																												}
																											})
																										}
																										else {

																											document.getElementById("upCode"+mdata[j].id+"").value="";
																										}



																									}, 500));
																									input.addEventListener('blur',function() {upblack(mdata[j].id)});
																								}
																							}
																						}

																						//查询是否存在出入库日志
																						let promise = ifLog(data[i].id,data[i].vault);
																						promise.then(function (resp){
																							if (resp===true){
																								//存在，不允许修改
																								for (let j = 0; j < datas.length; j++) {
																									for (let k = 0; k < mdata.length; k++) {
																										if (Number(datas[j].att_name_id)===Number(mdata[k].parentId)){
																											document.getElementById("upInput"+mdata[k].id+"").disabled=true;
																											document.getElementById("updateUnit").disabled=true;
																											document.getElementById("bb"+mdata[k].id+"").style.color="red";
																											document.getElementById("bb"+mdata[k].id+"").style.display="";

																										}
																									}
																								}
																							}else {
																								//不存在，允许修改
																								for (let j = 0; j < datas.length; j++) {
																									for (let k = 0; k < mdata.length; k++) {
																										if (Number(datas[j].att_name_id)===Number(mdata[k].parentId)){
																											document.getElementById("bb"+mdata[k].id+"").style.color="red";
																											document.getElementById("bb"+mdata[k].id+"").style.display="";

																										}
																									}
																								}
																							}
																						})



																					}
																				})




																				//点击更新,循环完了绑定更新事件，更新完基础的再更新后添加的
																				document.getElementById("updateSubmit").onclick=function (){

																					let booleanPromise = updateQx();
																					booleanPromise.then(async function (resp){
																						if (resp===true){
																							//获取数据
																							let formdata={
																								id:datas[0].id,
																								parentId:sortId,
																								name:"",
																								url:"",
																								materialNumber:"",
																								brand:"",
																								description:"",
																								number:"",
																								unit:"",
																								priceUnit:""
																							}

																							formdata.name=document.getElementById("updateName").value;
																							let ylPc = document.querySelector(".ylPic");
																							if (ylPc){
																								formdata.url =ylPc.querySelector("img").src;
																							}
																							formdata.materialNumber= document.getElementById("UpdateMaterialNumber").value;
																							formdata.brand= document.getElementById("updateBrand").value;
																							formdata.description= document.getElementById("updateDescription").value;
																							formdata.unit=document.getElementById("updateUnit").value;
																							// formdata.number = document.getElementById("updateNumber").value;
																							//获取筛选框中的内容
																							let priceUnitSelect = document.getElementById("updatePriceUnit");
																							let priceUnitIndex = priceUnitSelect.selectedIndex;//获取select的索引
																							//获取选中的值
																							formdata.priceUnit=priceUnitSelect.options[priceUnitIndex].value;
																							let b = updateIfBaseExist();
																							if (b===true){
																								//判断后添加的是否为空


																								var content={
																									id:"",
																									parentId:"",
																									productId:"",
																									content:"",
																									autoCode:"",
																								}

																								var list=[mdata.length]


																								for (let i = 0; i < mdata.length; i++) {

																									content.id=mdata[i].id;
																									content.content=document.getElementById("upInput"+mdata[i].id+"").value.trim();
																									content.parentId=mdata[i].parentId
																									list[i]=content;
																									content={
																										id:"",
																										parentId:"",
																										productId:"",
																										content:"",
																										autoCode: "",
																									}
																								}
																								//判断开启了唯一标识的是否重复
																								let unique=false;
																								let dj=0;
																								for (let j = 0; j <list.length ; j++) {
																									let parentId = list[j].parentId;
																									let productId = datas[0].id;
																									let content1 = list[j].content;
																									let b1 = await uniqueUpdate(parentId,content1,productId);
																									if (b1===true){
																										unique=true;
																										dj=j;
																										break;
																									}
																								}
																								if (unique===true){
																									//查询属性名
																									axios({
																										method:"post",
																										url:"attribute/selectById",
																										data:list[dj].parentId
																									}).then(function (resp){
																										let datas=resp.data;
																										alert(""+datas[0].name+"开启了唯一标识，"+list[dj].content+"已存在！")
																									})
																								}
																								else {
																									let l=true;
																									//不在映射中的可以为空
																									axios({
																										method:"post",
																										url:"mapping/selectMapping",
																										data:sortId
																									}).then(function (resp)
																									{
																										let mappingdatas= resp.data;
																										for (let j = 0; j < list.length; j++) {
																											for (let k = 0; k < mappingdatas.length; k++) {
																												if (Number(list[j].parentId)===Number(mappingdatas[k].att_name_id)){
																													let trim = list[j].content;
																													if (trim.length===0 ||document.getElementById("upCode"+list[j].id+"").value.length===0) {
																														l=false;
																													}
																													if (l===false){
																														break;
																													}
																												}
																											}

																										}



																										function update(threeData){
																											//更新操作
																											axios({
																												method:"post",
																												url:"product/updateById",
																												data:formdata,
																											}).then(function (resp){
																												if (resp.data==="success"){
																													//提交多条数据
																													axios({
																														method:"post",
																														url:"attribute/updateAttributeContent",
																														data:list,
																													}).then(function (resp){

																														if (resp.data==="success"){
																															//更新成功，下面进行编码的添加，先判断哪些属性不在映射中就要查询该分类下的映射
																															if (threeData.length>0){
																																//执行添加编码的操作
																																axios({
																																	method:"post",
																																	url:"attributeValue/addCodeAuto",
																																	data:threeData
																																}).then(function (resp){
																																	if (resp.data==="success"){

																																		alert("更新成功！");
																																		select(foreverNumber,n);
																																		let wd = document.querySelector(".window");
																																		wd.style.display="none";
																																	}else {
																																		alert("更新成功，但添加编码失败，请联系先关人员！")
																																	}
																																})
																															}else {
																																alert("更新成功！");
																																select(foreverNumber,n);
																																let wd = document.querySelector(".window");
																																wd.style.display="none";
																															}



																														}else {
																															alert("更新失败，请联系相关人员！")
																														}

																													})



																												}else {
																													alert("修改失败，请联系相关人员！")
																												}
																											})
																										}
																										function ifyiy(threeData){
																											for (let i = 0; i < mdata.length; i++) {

																												content.id=mdata[i].id;
																												content.content=document.getElementById("upInput"+mdata[i].id+"").value.trim();
																												content.parentId=mdata[i].parentId
																												list[i]=content;
																												content={
																													id:"",
																													parentId:"",
																													productId:"",
																													content:"",
																												}
																											}

																											for (let j = 0; j < list.length; j++) {
																												for (let k = 0; k < mappingdatas.length; k++) {
																													if (Number(list[j].parentId)===Number(mappingdatas[k].att_name_id)){
																														let trim = list[j].content.trim();
																														if (trim.length===0||document.getElementById("upCode"+list[j].id+"").value.length===0){
																															l=false;
																														}
																														if (l===false){
																															break;
																														}
																													}
																												}

																											}
																											//自动生成一组数据所两两组合的所有结果
																											let str=[{num:"0"},{num:"1"},{num:"2"},{num:"3"},{num:"4"},{num:"5"},{num:"6"},{num:"7"},{num:"8"},{num:"9"}]
																											let ls1=str[0].num;
																											let ls2;
																											let lsSum=[];
																											let lsD=[{}];
																											async function lsNumber3(q) {
																												if (q < str.length) {
																													for (let i = 0; i < str.length; i++) {

																														ls2 = str[i].num;
																														ls1 = str[q].num
																														if (String(ls2) === String(str[str.length - 1].num)) {
																															q++;

																															let sum = (ls1 + ls2)
																															lsD = [{num: "" + sum + ""}];
																															lsSum = lsD.concat(lsSum);
																															lsD = {};
																															await lsNumber3(q);


																														} else {
																															let sum = (ls1 + ls2)
																															lsD = [{num: "" + sum + ""}];
																															lsSum = lsD.concat(lsSum);
																															lsD = {};

																														}
																													}
																												}
																												else if (q === str.length) {
																													//查询是否开启了映射编码的属性
																													let b1 = await ifSerialExistUpdate(mappingdatas);
																													if (b1 === true) {
																														//开启了映射编码表的功能，查询当前属性使用的映射中编码用到哪里了
																														await serialCodeUpdate(mappingdatas, formdata.parentId, list);
																														formdata.materialNumber = document.getElementById("UpdateMaterialNumber").value;
																														//查询是否重复
																														let b4 = await ifMaterialNumberExistUpdate(formdata);
																														if (b4 === true) {
																															//重复，使用后两位流水码
																															if (window.confirm("系统检测到要使用流水码，是否要进行添加？")) {
																																async function sub2(s) {

																																	if (Number(s) >=0) {
																																		//后两位流水码更换
																																		let value5 = document.getElementById("UpdateMaterialNumber").value;
																																		//替换最后两位
																																		let newStr = value5.substring(0, value5.length - 2) + lsSum[s].num;


																																		formdata.materialNumber = newStr;


																																		axios({
																																			method: "post",
																																			url: "product/selectUpMN",
																																			data: formdata
																																		}).then(async function (resp) {
																																			if (resp.data === false) {
																																				//不重复了，执行添加操作
																																				await update(threeData)


																																			} else {
																																				s--;
																																				await sub2(s);
																																			}

																																		})

																																	} else {
																																		//默认流水码用完了，映射流水码+1
																																		let b2 = await serialCodeAddOneUpdate(mappingdatas, formdata.parentId, list);
																																		if (b2 === true) {
																																			await sub2(lsSum.length - 1);

																																		} else {
																																			//映射中流水码用完了，使用后两位
																																			//映射中的流水码用完了，默认最后两位流水码用完要报警
																																			function sub1(s) {

																																				if (Number(s) >=0) {
																																					//后两位流水码更换
																																					let value5 = document.getElementById("materialNumber").value;
																																					//替换最后两位
																																					let newStr = value5.substring(0, value5.length - 2) + lsSum[s].num;

																																					formdata.materialNumber = newStr;



																																					axios({
																																						method: "post",
																																						url: "product/selectUpMN",
																																						data: formdata
																																					}).then(async function (resp) {
																																						if (resp.data === false) {
																																							//不重复了，执行添加操作
																																							await update(threeData)


																																						} else {
																																							s--;
																																							sub1(s);
																																						}

																																					})

																																				} else {
																																					alert("流水码已用完！请联系相关人员！")
																																				}

																																			}

																																			sub1(lsSum.length - 1);

																																		}

																																	}

																																}

																																await sub2(lsSum.length - 1);
																																return true;
																															}
																															else {
																																return false;
																															}

																														} else {
																															//不重复，直接提交
																															await ifyiy(threeData);
																														}

																													}
																													else {
																														//未开启映射编码的属性
																														//得到了所有的集合
																														//判断物料号是否重复
																														axios({
																															method: "post",
																															url: "product/selectUpMN",
																															data: formdata
																														}).then(async function (resp) {
																															if (resp.data === true) {

																																if (window.confirm("系统检测到要使用流水码，是否要进行添加？")) {

																																	async function sub(s) {

																																		if (Number(s) >=0) {
																																			//后两位流水码更换
																																			let value5 = document.getElementById("UpdateMaterialNumber").value;
																																			//替换最后两位
																																			let newStr = value5.substring(0, value5.length - 2) + lsSum[s].num;

																																			formdata.materialNumber = newStr;


																																			axios({
																																				method: "post",
																																				url: "product/selectUpMN",
																																				data: formdata
																																			}).then(function (resp) {
																																				if (resp.data === false) {
																																					//不重复了，执行添加操作
																																					update(threeData);


																																				} else {
																																					s--;
																																					sub(s);
																																				}

																																			})

																																		} else {

																																			alert("流水码已用完！请联系相关人员！")

																																		}

																																	}

																																	await sub(lsSum.length - 1);
																																	return true;
																																} else {
																																	return false;
																																}


																															} else {
																																//代表不重复
																																await update(threeData)
																															}
																														})
																													}




																												}

																											}
																											//判断是否是同一个物料.1。先判断物料名称是否重复
																											axios({
																												method:"post",
																												url:"product/selectNameIfExist",
																												data:formdata
																											}).then(async function (resp){
																												let d=0;
																												let e=true;
																												if (resp.data===true){
																													//存在当前名称,查询后来添加的属性是否有重复的
																													//查询当前分类下的产品id
																													await 	axios({
																														method:"post",
																														url:"product/selectAllInSortDeleteSignUpdate",
																														data:formdata
																													}).then(async function (resp){
																														let datas=resp.data;
																														for (let j = 0; j < datas.length; j++) {
																															if (Number(formdata.id)!==Number(datas[j].id)){
																																d=0;

																																for (let k = 0; k < list.length; k++) {
																																	list[k].productId=datas[j].id;
																																	await axios({
																																		method:"post",
																																		url:"attributeFunction/selectByAttNameId",
																																		data:list[k].parentId
																																	}).then(async function (resp){
																																		let functionCode=resp.data;
																																		let serialCode = functionCode[0].serialCode
																																		if (Number(serialCode)===Number(1)){
																																			//开启了流水码功能
																																			d++;
																																		}
																																		else {
																																			await axios({
																																				method:"post",
																																				url:"attribute/selectAttributeContentIfExist",
																																				data:list[k]
																																			}).then(function (resp){
																																				if (resp.data===true){
																																					d++;
																																				}
																																			})
																																		}
																																	})



																																}
																																if (Number(d)===Number(list.length)){
																																	e=false;
																																	break;
																																}
																															}


																														}
																													})

																													if (e===false){
																														let productId=list[0].productId;
																														await axios({
																															method:"post",
																															url:"product/selectById",
																															data:productId
																														}).then(function (resp){
																															let datas = resp.data;
																															if (datas[0].deleteSign===1){
																																//与弃用物料信息相同，提示
																																alert("检测到要修改的物料已被弃用，如要修改请联系相关人员！")

																															}
																															else {

																																alert("该物料已存在，请核对后再操作！")
																															}
																														})
																													}
																													else if (e===true) {


																														await lsNumber3(0);

																													}


																												}
																												else if (resp.data===false){
//自动生成一组数据所两两组合的所有结果


																													await lsNumber3(0);
																												}
																											})
																										}

																										if (l===true){
																											//值作为编码的判断
																											axios({
																												method:"post",
																												url:"mapping/selectMapping",
																												data:sortId
																											}).then(async function (resp){
																												// let mappingDatas=resp.data;
																												let datasOne=[];
																												let datasThree=[];


																												for (let j = 0; j < list.length; j++) {


																													datasOne.push(list[j])


																												}
																												//得到datas,查看是否有开启值作为编码的功能
																												let datasTwo=[];
																												if (datasOne.length>0){
																													for (let j = 0; j < datasOne.length; j++) {
																														await axios({
																															method:"post",
																															url:"attributeFunction/selectByAttNameId",
																															data:datasOne[j].parentId
																														}).then(function (resp){
																															if (resp.data[0].autoCode===1 || resp.data[0].autoCode===2 ){
																																datasOne[j].autoCode=resp.data[0].autoCode;
																																datasTwo.push(datasOne[j])
																															}
																														})
																													}

																													if (datasTwo.length>0){
																														//判断格式是否有不符和作为编码的

																														var pattern = /^[0-9A-Za-z]+$/;
																														let gs=true;
																														for (let j = 0; j < datasTwo.length; j++) {
																															//判断编码是否存在
																															if (datasTwo[j].content.length>0){
																																let ifformdata={
																																	attNameId:datasTwo[j].parentId,
																																	attValue:datasTwo[j].content
																																}
																																await axios({
																																	method:"post",
																																	url:"attributeValue/selectCode",
																																	data:ifformdata
																																}).then(function (resp){
																																	let ifdatas=resp.data;
																																	if (ifdatas.length>0){
																																		//存在编码，使用的是已存在的，不判断
																																	}else {
																																		if (datasTwo[j].autoCode===1){
																																			if (pattern.test(datasTwo[j].content)===false && datasTwo[j].content.length>0){
																																				alert(datasTwo[j].content+"的格式不适合作为编码的标准，请核对后再试！");
																																				gs=false
																																			}
																																		}

																																	}
																																})
																															}
																															if (gs===false){
																																break;
																															}
																														}
																														if (gs===true){
																															//都符合作为编码的标准，然后去查询是否有长度
																															let le=true;

																															for (let j = 0; j < datasTwo.length; j++) {
																																let ifformdata={
																																	attNameId:datasTwo[j].parentId,
																																	attValue:datasTwo[j].content
																																}
																																await axios({
																																	method:"post",
																																	url:"attributeValue/selectCode",
																																	data:ifformdata
																																}).then(async function (resp){
																																	let ifdatas=resp.data;
																																	if (ifdatas.length>0){
																																		//存在编码，使用的是已存在的，不判断
																																	}
																																	else{
																																		await axios({
																																			method:"post",
																																			url:"attribute/selectById",
																																			data:datasTwo[j].parentId
																																		}).then(async function (resp){
																																			let lengthTwo=resp.data[0].length
																																			if (lengthTwo===0){
																																				alert("值为"+datasTwo[j].content+"的属性还未设置编码长度，请设置后再进行操作！");
																																				le=false;
																																			}else {
																																				//判断长度是否符合
																																				if (datasTwo[j].content.length>0){
																																					if ((resp.data[0].length<datasTwo[j].content.length)&&datasTwo[j].autoCode===1){
																																						alert(datasTwo[j].content+"的长度大于编码长度，请核对后在进行操作！");
																																						le=false;
																																					}
																																					else {
																																						//长度合适，查询这个属性值是否已经有编码了(是否存在)
																																						let formdata={
																																							attNameId:datasTwo[j].parentId,
																																							attValue:datasTwo[j].content
																																						}
																																						await axios({
																																							method:"post",
																																							url:"attributeValue/selectValueExistAdd",
																																							data:formdata
																																						}).then(function (resp){
																																							if (resp.data===false){
																																								//值不重复,将值作为编码
																																								let codeData
																																								codeData={
																																									attNameId:datasTwo[j].parentId,
																																									attValue:datasTwo[j].content,
																																									code:document.getElementById("upCode"+datasTwo[j].id+"").value,
																																								}


																																								datasThree.push(codeData)


																																							}
																																						})
																																					}
																																				}


																																			}
																																		})
																																	}
																																})


																																if (le===false){
																																	break;
																																}
																															}

																															if (le===true){
																																//得到了最后的编码合集，进行添加
																																ifyiy(datasThree);
																															}

																														}

																													}
																													else {
																														//有不在映射中的，但是没有开启值作为编码的功能，不需要添加编码
																														ifyiy(datasThree);

																													}
																												}
																												else {
																													//全在映射中，直接更新内容即可，不需要添加编码

																													ifyiy(datasThree);
																												}

																											})
																										}
																										else {
																											alert("数据未填写完整！")
																										}



																									})
																								}




																							}
																							else {
																								alert("数据未填写完整！")
																							}
																						}
																						else {
																							alert("您暂未获得修改权限!")
																						}
																					})


																				}


																			}



																		}

																		lastAttribute(mdata.length-1,mdata.length)
																	}
																	else {

																		//点击修改数据，表示没有后添加的，直接更新基础的
																		document.getElementById("updateSubmit").onclick=function (){
																			let booleanPromise = updateQx();
																			booleanPromise.then(function (resp){
																				if (resp===true){
																					let b = updateIfBaseExist();
																					if (b===true){
																						//获取数据
																						let formdata={
																							id:datas[0].id,
																							parentId:sortId,
																							name:"",
																							url:"",
																							materialNumber:"",
																							brand:"",
																							description:"",
																							number:"",
																							unit:"",
																							priceUnit:""
																						}

																						formdata.name=document.getElementById("updateName").value;
																						let ylPc = document.querySelector(".ylPic");
																						if (ylPc){
																							formdata.url =ylPc.querySelector("img").src;
																						}
																						formdata.materialNumber= document.getElementById("UpdateMaterialNumber").value;
																						formdata.brand= document.getElementById("updateBrand").value;
																						formdata.description= document.getElementById("updateDescription").value;
																						formdata.unit=document.getElementById("updateUnit").value;
																						// formdata.number = document.getElementById("updateNumber").value;
																						//获取筛选框中的内容
																						let priceUnitSelect = document.getElementById("updatePriceUnit");
																						let priceUnitIndex = priceUnitSelect.selectedIndex;//获取select的索引
																						//获取选中的值
																						formdata.priceUnit=priceUnitSelect.options[priceUnitIndex].value;
																						axios({
																							method:"post",
																							url:"product/selectNameIfExist",
																							data:formdata
																						}).then(function (resp){
																							if(resp.data===true){
																								alert("该物料已存在，请核对后再操作！")
																							}else {
																								//更新
																								axios({
																									method:"post",
																									url:"product/updateById",
																									data:formdata,
																								}).then(function (resp){
																									if (resp.data==="success"){
																										alert("修改成功！");
																										//重置信息进行显示修改后的信息
																										select(foreverNumber,n);

																										//关闭窗口
																										let wd = document.querySelector(".window");
																										wd.style.display="none";
																										//输入框设置为空
																										let inputs = document.querySelectorAll("#updateBlock input");
																										for (let i = 0; i < inputs.length; i++) {
																											inputs[i].value="";
																										}

																										document.getElementById("updateDescription").value="";


																									}else {
																										alert("修改失败，请联系相关人员！")
																									}
																								})
																							}
																						})


																					}
																					else {
																						alert("数据未填写完整！")
																					}
																				}
																				else {
																					alert("您暂未获得修改权限！")
																				}
																			})



																		}

																	}


																}


															}
															selectAttributeContent(0,sortAttribute.length)

														}
														else {
															//该产品的下面分类信息没有属性信息，就直接添加更新功能
															document.getElementById("updateSubmit").onclick=function (){
																let booleanPromise = updateQx();
																booleanPromise.then(function (resp){
																	if (resp===true){
																		let b = updateIfBaseExist();
																		if (b===true){
																			//获取数据
																			let formdata={
																				id:datas[0].id,
																				parentId:sortId,
																				name:"",
																				url:"",
																				materialNumber:"",
																				brand:"",
																				description:"",
																				number:"",
																				unit:"",
																				priceUnit:""
																			}

																			formdata.name=document.getElementById("updateName").value;
																			let ylPc = document.querySelector(".ylPic");
																			if (ylPc){
																				formdata.url =ylPc.querySelector("img").src;
																			}
																			formdata.materialNumber= document.getElementById("UpdateMaterialNumber").value;
																			formdata.brand= document.getElementById("updateBrand").value;
																			formdata.description= document.getElementById("updateDescription").value;
																			formdata.unit=document.getElementById("updateUnit").value;
																			// formdata.number = document.getElementById("updateNumber").value;
																			//获取筛选框中的内容
																			let priceUnitSelect = document.getElementById("updatePriceUnit");
																			let priceUnitIndex = priceUnitSelect.selectedIndex;//获取select的索引
																			//获取选中的值
																			formdata.priceUnit=priceUnitSelect.options[priceUnitIndex].value;

																			axios({
																				method:"post",
																				url:"product/selectNameIfExist",
																				data:formdata
																			}).then(function (resp){
																				if (resp.data===true){
																					alert("该物料已存在，请核对后再操作！")
																				}else {
																					//更新
																					axios({
																						method:"post",
																						url:"product/updateById",
																						data:formdata,
																					}).then(function (resp){
																						if (resp.data==="success"){

																							alert("修改成功！");
																							//重置信息进行显示修改后的信息
																							select(foreverNumber,n);

																							//关闭窗口
																							let wd = document.querySelector(".window");
																							wd.style.display="none";
																							//输入框设置为空
																							let inputs = document.querySelectorAll("#updateBlock input");
																							for (let i = 0; i < inputs.length; i++) {
																								inputs[i].value="";
																							}

																							document.getElementById("updateDescription").value="";


																						}else {
																							alert("修改失败，请联系相关人员！")
																						}
																					})
																				}
																			})

																		}
																		else {
																			alert("数据未填写完整！")
																		}
																	}
																	else {
																		alert("您暂未获得修改权限!")
																	}
																})



															}
														}
													})


												}
											}
											selectSortAttribute(datass.length-1,datass.length)


										}

									}


									//查询层数
									axios({
										method:"post",
										url:"sort/selectOtherLevel",
										data:number
									}).then(function (resp){
										let datas=resp.data;

										if (datas.length!==0){
											//如果能查到证明不是最后一层分类
											let level=datas[0].level
											m=level-1;
											belong2(number,m);
										}else {
											//查不到证明是最后一层分类直接当做id去查自己是几级分类
											axios({
												method:"post",
												url:"sort/selectOfSort",
												data:number
											}).then(function (resp){
												let datas=resp.data;

												m=datas[0].level;
												belong2(number,m);
											})
										}
									})

								})

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
										if (resp[j].function_two_id===3 && resp[j].open_status===1){
											promiseBoolean=true;
											break;
										}
									}

									if (promiseBoolean===true){
										//检测是否存在库存
										axios({
											method:"post",
											url:"product/selectById",
											data:data[i].id
										}).then(function (resp){
											let kc = resp.data;
											let kcNumber = kc[0].number;
											if (Number(kcNumber)>0 || kcNumber===undefined){
												alert("该物料存在库存，禁止删除！")
											}
											else {
												if (window.confirm("您真的要删除这个物料吗？")){
													axios({
														method:"post",
														url:"product/deleteById",
														data:data[i].id
													}).then(function (resp){
														if (resp.data==="success"){
															alert("删除成功！");
															//重置信息进行显示修改后的信息
															select(number,n);
														}else {
															alert("删除失败，请联系相关人员！");
														}
													})
													return true;
												}
												else {
													return false;
												}
											}
										})


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


				add(number,n);
				// addAttribute(number,n);
				// updateAttribute(number,n);
				//点击添加物料显示窗口
				document.getElementById("add").onclick=function (){
					//删除所有的图片信息
					let res = document.querySelectorAll(".responsive");
					for ( let j = 0; j < res.length; j++) {
						res[j].remove();
					}
					//检测是否有权限
					let promise = userFunction(userId);
					let promiseBoolean=false;
					promise.then(function (resp){
						for (let i = 0; i < resp.length; i++) {
							if (resp[i].function_two_id===1 && resp[i].open_status===1){
								promiseBoolean=true;
								break;

							}


						}
						if (promiseBoolean===true){
							for (let i = 1; i < 9; i++) {
								document.getElementById("mdiv"+i+"").style.color="black"
							}
							//先判断当前分类下是否有映射
							axios({
								method:"post",
								url:"mapping/selectBySortId",
								data:number
							}).then(async function (resp){
								let mappingDatas=resp.data;
								//判断是否有映射存在
								if (mappingDatas.length>0){


									let nm=true;
									let fw = true;
									let idd=0;
									let iddd=0;
									axios({
										method:"post",
										url:"attributeFunction/selectAllAttributeFunction",
										data:number
									}).then(async function (resp){
										let autoDatas=resp.data;//得到了当前分类的映射下的所有在一映射中的属性信息
										for (let i = 0; i < autoDatas.length; i++) {
											if (autoDatas[i].autoCode===0 && autoDatas[i].range===0 && autoDatas[i].serialCode ===0 ){
												//在映射中，是值的，值作为编码功能以及顺序编码功能未开启的，查询是否有属性值添加
												await 	axios({
													method:"post",
													url:"attributeValue/selectIfCode",
													data:autoDatas[i].attNameId
												}).then(function (resp){
													if (resp.data===false){
														//不存在编码，提示不能进行添加
														nm=false;
														idd=autoDatas[i].attNameId;
													}
												})
											}
											else if (autoDatas[i].autoCode ===0 && autoDatas[i].range===1){
												//范围的
												await 	axios({
													method:"post",
													url:"attributeValue/selectIfCode",
													data:autoDatas[i].attNameId
												}).then(function (resp){
													if (resp.data===false){
														//不存在编码，提示不能进行添加
														fw=false;
														iddd=autoDatas[i].attNameId;
													}
												})

											}
											if (nm===false || fw===false){
												break;
											}
										}

										if (nm===true&&fw===true){
											//存在映射，检查是否为9
											let num=0;
											for (let i = 0; i < mappingDatas.length; i++) {

												num+=Number(mappingDatas[i].length);
											}

											if (Number(num)===9){

												document.querySelector(".window").style.display="block";
												document.getElementById("addBlock").style.display="block";
												document.getElementById("updateBlock").style.display="none";
												document.getElementById("addAttribute").style.display="none";
												document.getElementById("updateAttribute").style.display="none";
												document.getElementById("updateAttribute2").style.display="none";
												selectAttributeAdd(number,n);
												document.getElementById("materialNumber").value="";

											}
											else {

												//查询当前分类下是否有物料存在
												axios({
													method:"post",
													url:"product/selectByParentId",
													data:number
												}).then(function (resp){
													if (resp.data===true){
														//有物料
														document.querySelector(".window").style.display="block";
														document.getElementById("addBlock").style.display="block";
														document.getElementById("updateBlock").style.display="none";
														document.getElementById("addAttribute").style.display="none";
														document.getElementById("updateAttribute").style.display="none";
														document.getElementById("updateAttribute2").style.display="none";
														selectAttributeAdd(number,n);
														document.getElementById("materialNumber").value="";
													}else if (resp.data===false){
														//检测用户权限

														axios({
															method:"post",
															url:"user/selectName"
														}).then(function (resp){
															let user = resp.data;
															axios({
																method:"post",
																url:"userFunction/selectFunction2",
																data:user.id
															}).then(function (resp){
																if (resp.data===true){
																	if (window.confirm("检测到映射表中占位不全，是否进行修改？")){



																		axios({
																			method:"post",
																			url:"sort/selectSortById",
																			data:number
																		}).then(function (resp){
																			let datas=resp.data;
																			window.open("attributeAdd.html?parentId="+datas[0].id+"&content="+datas[0].name+"&level="+datas[0].level+"&userId="+userId+"")

																		})
																		return true;
																	}else {

																		document.querySelector(".window").style.display="block";
																		document.getElementById("addBlock").style.display="block";
																		document.getElementById("updateBlock").style.display="none";
																		document.getElementById("addAttribute").style.display="none";
																		document.getElementById("updateAttribute").style.display="none";
																		document.getElementById("updateAttribute2").style.display="none";
																		selectAttributeAdd(number,n);
																		document.getElementById("materialNumber").value="";
																		return false;
																	}
																}else {
																	document.querySelector(".window").style.display="block";
																	document.getElementById("addBlock").style.display="block";
																	document.getElementById("updateBlock").style.display="none";
																	document.getElementById("addAttribute").style.display="none";
																	document.getElementById("updateAttribute").style.display="none";
																	document.getElementById("updateAttribute2").style.display="none";
																	selectAttributeAdd(number,n);
																	document.getElementById("materialNumber").value="";
																}
															})
														})


													}
												})


											}
										}
										else {

											if (nm===false){
												//根据idd查询属性名
												axios({
													method:"post",
													url:"attribute/selectById",
													data:idd
												}).then(function (resp){
													let datas=resp.data;
													alert("当前映射中,"+datas[0].name+"还未添加编码以及未开启值作为编码功能的属性，请先添加编码或者打开值作为编码的功能再进行操作！")
												})
											}else if (fw===false){
												//根据idd查询属性名
												axios({
													method:"post",
													url:"attribute/selectById",
													data:iddd
												}).then(function (resp){
													let datas=resp.data;
													alert("当前映射中,"+datas[0].name+"还未添加任何属性值以及编码，请添加后再试！")
												})
											}

										}

									})




								}
								else {
									alert("不存在映射，请先添加映射在进行物料添加操作!");
									//查询分类的数据
									axios({
										method:"post",
										url:"sort/selectSortById",
										data:number
									}).then(function (resp){
										let datas=resp.data;
										window.open("attributeAdd.html?parentId="+datas[0].id+"&content="+datas[0].name+"&level="+datas[0].level+"&userId="+userId+"")

									})



								}
							})
						}
						else {
							alert("您未获得添加物料的权限！")
						}
					})





				}


				//点击修改物料显示窗口
				let updateP = document.querySelectorAll(".updateProduct");
				for (let i = 0; i < updateP.length; i++) 	{
					updateP[i].onclick=async function () {

						for (let j = 1; j <= 9; j++) {
							document.getElementById("upmdiv" + j + "").style.color = "black";
						}

						black();
						//隐藏添加数据。显示更细数据
						document.querySelector(".window").style.display = "block";
						document.getElementById("addBlock").style.display = "none";
						document.getElementById("updateBlock").style.display = "block";
						document.getElementById("addAttribute").style.display = "none";
						document.getElementById("updateAttribute").style.display = "none";
						document.getElementById("updateAttribute2").style.display = "none";
						//获取数据进行回显
						await axios({
							method: "post",
							url: "product/selectById",
							data: data[i].id
						}).then(async function (resp) {
							let datas = resp.data;
							let sortId = datas[0].parentId;
							//设置数量框的禁用
							// let promise = ifLog(datas[0].id,datas[0].vault);
							// promise.then(function (resp){
							// 	if (resp===true){
							// 		document.getElementById("updateNumber").disabled=true;
							// 	}
							// })
							//自带属性
							document.getElementById("UpdateMaterialNumber").disabled = true;
							document.getElementById("updateName").value = datas[0].name;
						images(datas,1);
							document.getElementById("UpdateMaterialNumber").value = datas[0].materialNumber;
							document.getElementById("updateBrand").value = datas[0].brand;
							document.getElementById("updateDescription").value = datas[0].description;
							document.getElementById("updateNumber").value = datas[0].number;
							document.getElementById("updateUnit").value = datas[0].unit;


							document.getElementById("produceId").innerHTML = datas[0].materialNumber.substring(0, 7);
							document.getElementById("nineNumber").innerHTML = datas[0].materialNumber.substring(7, 16);
							let s = datas[0].materialNumber.substring(7, 16);
							for (let j = 0; j < s.length; j++) {
								let m = Number(j) + Number(1);
								document.getElementById("upmdiv" + m + "").innerHTML = s[j];
							}


							//这是查询最后一层的属性，现在要做的是查询出来该层 所有的层数

							var datass = [];
							let m;

							async function belong2(number, m) {

								if (m > 0) {
									//获取当前分类属于哪些大类里
									axios({
										method: "post",
										url: "sort/selectOfSort",
										data: number
									}).then(function (resp) {
										var datas = resp.data;
										datass = datas.concat(datass);
										m--;
										belong2(datas[0].parentId, m)
									})

								} else if (m === 0) {
									//得到了自己上面的所有分类内容，然后根据分类id去查询该分类下的属性内容

									let selectAttributeUpdate = "";
									var mdata = [];
									var sortAttribute = [];

									function selectSortAttribute(sd1, sd2) {
										if (sd2 > 0) {
											axios({
												method: "post",
												url: "attribute/selectAttributeNameByParentId",
												data: datass[sd1].id
											}).then(function (resp) {
												let adatas = resp.data;
												sortAttribute = adatas.concat(sortAttribute);
												sd1--;
												if (sd1 >= 0) {
													sd2--;
													selectSortAttribute(sd1, sd2);
												} else {
													sd2--;
													selectSortAttribute(0, sd2);
												}
											})
										} else if (sd2 === 0) {

											//查询公共属性
											axios({
												method: "post",
												url: "attribute/selectPublic"
											}).then(function (resp) {
												let ggdatas = resp.data;
												sortAttribute = ggdatas.concat(sortAttribute);
												//得到了所有的分类属性信息
												if (sortAttribute.length > 0) {

													//如果该产品的上面分类有属性信息
													//获取到了分类的属性信息，然后用分类属性名的id作为分类内容的parentid以及物料的id去查询物料所有的属性内容

													function selectAttributeContent(ac1, ac2) {
														if (ac2 > 0) {
															axios({
																method: "post",
																url: "attribute/selectBeforeContentUpdate?id=" + datas[0].id + "",
																data: sortAttribute[ac1].id,
															}).then(async function (resp) {
																let bdatas = resp.data;
																if (bdatas.length > 0) {
																	mdata = bdatas.concat(mdata)
																	for (let k = 0; k < bdatas.length; k++) {
																		if (bdatas[k].unit.length > 0) {
																			selectAttributeUpdate += '    <div style="width:738px ;height: auto;display: flex;">\n' +
																				'                    <div  style="width: 100px;height: auto; border-right: 1px solid #d6e0ef;border-bottom: 1px solid #d6e0ef;text-align: center;display: flex">\n' +
																				'                        <div style="width: fit-content;height: auto;align-self: center;margin: auto">' + bdatas[k].name + '  <br>(单位:' + bdatas[k].unit + ')</div>\n' +
																				'                    </div>\n' +
																				'                    <div class="qwe" style="width:294px ;height: auto;border-right: 1px solid #d6e0ef;border-bottom: 1px solid #d6e0ef;">\n' +
																				'                        &nbsp;<input autocomplete="off" maxlength="80" style="width: 90%;height: 20px;" type="text" name="" id=upInput' + bdatas[k].id + ' value=""> &nbsp;<b id="bb' + bdatas[k].id + '" style="display: none">*</b>\n' +
																				' <div id="inputUpValue' + bdatas[k].id + '" style="display: none;position: absolute;top: 100%;left: 3px; padding: 1px 2px; width:90%;min-width: 200px; height:auto; z-index:9999;overflow: hidden;background: #e0e0e0;border-top: none;"> </div>\n' +
																				'                    </div>\n' +
																				'                    <div  style="width: 50px;height: auto; border-right: 1px solid #d6e0ef;border-bottom: 1px solid #d6e0ef;text-align: center;display: flex">\n' +
																				'                        <div style="width: fit-content;height: auto;align-self: center;margin: auto">编码</div>\n' +
																				'                    </div>\n' +
																				'                    <div class="qwe" style="width:294px ;height: auto;border-right: 1px solid #d6e0ef;border-bottom: 1px solid #d6e0ef;">\n' +
																				'                        &nbsp;<input autocomplete="off" maxlength="80" style="width: 90%;height: 20px;" type="text" name="" id=upCode' + bdatas[k].id + ' value="" disabled > &nbsp;\n' +
																				'                    </div>\n' +
																				'                </div>'
																		} else {
																			selectAttributeUpdate += '    <div style="width:738px ;height: auto;display: flex;">\n' +
																				'                    <div  style="width: 100px;height: auto; border-right: 1px solid #d6e0ef;border-bottom: 1px solid #d6e0ef;text-align: center;display: flex">\n' +
																				'                        <div style="width: fit-content;height: auto;align-self: center;margin: auto">' + bdatas[k].name + '</div>\n' +
																				'                    </div>\n' +
																				'                    <div class="qwe" style="width:294px ;height: auto;border-right: 1px solid #d6e0ef;border-bottom: 1px solid #d6e0ef;">\n' +
																				'                        &nbsp;<input autocomplete="off" maxlength="80" style="width: 90%;height: 20px;" type="text" name="" id=upInput' + bdatas[k].id + ' value=""> &nbsp;<b id="bb' + bdatas[k].id + '" style="display: none">*</b>\n' +
																				' <div  id="inputUpValue' + bdatas[k].id + '" style="display: none;position: absolute;top: 100%;left: 3px; padding: 1px 2px; width:90%;min-width: 200px; height:auto; z-index:9999;overflow: hidden;background: #e0e0e0;border-top: none;"> </div>\n' +
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

																	ac1++;
																	ac2--;
																	selectAttributeContent(ac1, ac2);

																} else {
																	ac1++;

																	ac2--;
																	selectAttributeContent(ac1, ac2);

																}

															})
														} else if (ac2 === 0) {

															document.getElementById("selectAttributeUpdate").innerHTML = selectAttributeUpdate;

															//得到了该产品的所有后添加的分类属性

															if (mdata.length > 0) {

																function lastAttribute(la1, la2) {
																	if (la2 > 0) {
																		if (mdata[la1].content !== undefined) {
																			document.getElementById("upInput" + mdata[la1].id + "").value = mdata[la1].content;
																		} else {
																			document.getElementById("upInput" + mdata[la1].id + "").value = "";
																		}

																		if (mdata[la1].content !== undefined) {
																			if (mdata[la1].content.length > 0) {


																				//根据属性id和分类id去查询编码
																				let formdata = {
																					attNameId: mdata[la1].parentId,
																					attValue: mdata[la1].content
																				}
																				//查询是否是范围
																				axios({
																					method: "post",
																					url: "attributeFunction/selectByAttNameId",
																					data: formdata.attNameId
																				}).then(function (resp) {
																					let functionDatas = resp.data;
																					if (functionDatas[0].range === 1) {
																						//如果是范围，获取所有的范围
																						axios({
																							method: "post",
																							url: "attributeValue/selectByAttNameId",
																							data: formdata.attNameId
																						}).then(function (resp) {
																							let datas = resp.data;
																							let m = 0;
																							for (let j = 0; j < datas.length; j++) {
																								let b = compare(datas[j].attValue, formdata.attValue);

																								if (b) {
																									m = j;
																									break
																								}
																							}
																							let rangeDatas = {
																								attNameId: mdata[la1].parentId,
																								attValue: datas[m].attValue
																							}
																							axios({
																								method: "post",
																								url: "attributeValue/selectCode",
																								data: rangeDatas
																							}).then(function (resp) {
																								if (resp.data.length > 0) {
																									document.getElementById("upCode" + mdata[la1].id + "").value = resp.data[0].code;
																								}

																								la1--;
																								if (la1 >= 0) {
																									la2--;
																									lastAttribute(la1, la2);
																								} else {
																									la2--;
																									lastAttribute(0, la2);
																								}
																							})
																						})

																					} else if (functionDatas[0].range === 0) {
																						axios({
																							method: "post",
																							url: "attributeValue/selectCode",
																							data: formdata
																						}).then(function (resp) {
																							if (resp.data.length > 0) {
																								document.getElementById("upCode" + mdata[la1].id + "").value = resp.data[0].code;
																							}

																							la1--;
																							if (la1 >= 0) {
																								la2--;
																								lastAttribute(la1, la2);
																							} else {
																								la2--;
																								lastAttribute(0, la2);
																							}
																						})
																					}
																				})

																			} else {
																				la1--;
																				if (la1 >= 0) {
																					la2--;
																					lastAttribute(la1, la2);
																				} else {
																					la2--;
																					lastAttribute(0, la2);
																				}
																			}
																		} else {
																			la1--;
																			if (la1 >= 0) {
																				la2--;
																				lastAttribute(la1, la2);
																			} else {
																				la2--;
																				lastAttribute(0, la2);
																			}
																		}


																	} else if (la2 === 0) {
																		axios({
																			method: "post",
																			url: "mapping/selectMapping",
																			data: sortId
																		}).then(function (resp) {
																			let datas = resp.data;
																			if (datas.length > 0) {
																				let formdatas = "";
																				for (let k = 0; k < datas.length; k++) {
																					for (let j = 0; j < mdata.length; j++) {
																						if (Number(datas[k].att_name_id) === Number(mdata[j].parentId)) {
																							for (let l = datas[k].begin_location; l <= datas[k].end_location; l++) {
																								document.getElementById("upmdiv" + l + "").style.border = "#1890ff solid 1px"
																							}


																						}
																					}

																				}

																				let value2 = document.getElementById("UpdateMaterialNumber").value;
																				document.getElementById("updateSortNumberA").innerHTML = value2.substring(0, 7);
																				document.getElementById("updateSerialNumber").innerHTML = value2.substring(16, 18);
																				for (let k = 0; k < datas.length; k++) {
																					for (let j = 0; j < mdata.length; j++) {
																						if (Number(datas[k].att_name_id) === Number(mdata[j].parentId)) {
																							document.getElementById("upInput" + mdata[j].id + "").onfocus = function () {
																								for (let l = datas[k].begin_location; l <= datas[k].end_location; l++) {
																									document.getElementById("upmdiv" + l + "").style.color = "red"
																								}

																								for (let l = 0; l < datas.length; l++) {
																									if (Number(datas[l].att_name_id) !== Number(mdata[j].parentId)) {
																										for (let o = datas[l].begin_location; o <= datas[l].end_location; o++) {
																											document.getElementById("upmdiv" + o + "").style.color = "black"
																										}
																									}
																								}

																							}
																							//查询流水码的功能
																							//查询是否开启了流水码功能
																							axios({
																								method: "post",
																								url: "attributeFunction/selectByAttNameId",
																								data: datas[k].att_name_id
																							}).then(function (resp) {
																								let ableCode = resp.data;
																								let serialCode = ableCode[0].serialCode;
																								if (serialCode === 1) {
																									//开启了流水码功能，限制输入和编码
																									document.getElementById("upInput" + mdata[j].id + "").disabled = true;
																									document.getElementById("upCode" + mdata[j].id + "").value = document.getElementById("upInput" + mdata[j].id + "").value;

																								}
																							})
																							let input = document.getElementById("upInput" + mdata[j].id + "");
																							//查询该属性下面是否有属性值
																							axios({
																								method: "post",
																								url: "attributeValue/selectIfCode",
																								data: mdata[j].parentId
																							}).then(function (resp) {
																								let datas = resp.data;
																								if (datas === true) {
																									input.placeholder = "请输入查询！"
																								}
																							})


																							let num = mdata[j].parentId;
																							input.addEventListener('input', debounce(function () {
																								let value = input.value.trim();
																								document.getElementById("upCode" + mdata[j].id + "").value = "";
																								if (value.length > 0) {
																									axios({
																										method: "post",
																										url: "attributeValue/selectByAttNameId",
																										data: mdata[j].parentId
																									}).then(function (resp) {
																										if (resp.data.length > 0) {
																											//查询是否是范围
																											axios({
																												method: "post",
																												url: "attributeFunction/selectByAttNameId",
																												data: num
																											}).then(function (resp) {
																												let rangeDatas = resp.data;

																												function autoAndNo(value, num, s) {
																													//得到了输入框的值，先判断能不能找到这个值
																													let formdata = {
																														attNameId: num,
																														attValue: value
																													}
																													//查询这个值是否存在
																													axios({
																														method: "post",
																														url: "attributeValue/selectValueExistAdd",
																														data: formdata
																													}).then(function (resp) {
																														let attValueBoolean = resp.data;
																														if (attValueBoolean === true) {
																															//该值存在
																															//查看该属性是否在映射中
																															axios({
																																method: "post",
																																url: "mapping/selectMapping",
																																data: sortId//分类id
																															}).then(function (resp) {
																																let mappingData = resp.data;
																																let map = false;
																																let index = 0;
																																for (let j = 0; j < mappingData.length; j++) {
																																	if (Number(num) === mappingData[j].att_name_id) {
																																		map = true;
																																		index = j;
																																		break;
																																	}

																																}

																																if (map === true) {
																																	//在映射中，根据长度和起始位置改编物料编码
																																	//值已经存在，查询这个值的编码
																																	let value1 = document.getElementById("upInput" + s + "").value.trim();
																																	let data = {
																																		attNameId: num,
																																		attValue: value1
																																	}
																																	axios({
																																		method: "post",
																																		url: "attributeValue/selectCode",
																																		data: data
																																	}).then(function (resp) {
																																		document.getElementById("upCode" + s + "").value = resp.data[0].code;
																																		let nine = document.getElementById("nineNumber").innerHTML;
																																		let str = nine;
																																		//判断输入的长度和编码长度

																																		let replaceStr = resp.data[0].code;//要替换的字符串
																																		// document.getElementById("updiv"+s+"").innerHTML=replaceStr;
																																		let startIndex = mappingData[index].begin_location;


																																		let endIndex = mappingData[index].end_location;

																																		let newStr = str.slice(0, startIndex - 1) + replaceStr + str.slice(endIndex);

																																		document.getElementById("nineNumber").innerHTML = newStr;
																																		//获取分类编码
																																		let sortId = document.getElementById("produceId").innerHTML;
																																		document.getElementById("UpdateMaterialNumber").value = sortId + newStr + lsn;
																																		for (let j = 0; j < newStr.length; j++) {
																																			let m = Number(j) + Number(1);
																																			document.getElementById("upmdiv" + m + "").innerHTML = newStr[j];
																																		}
																																	})


																																} else {
																																	//不在映射中
																																	//值已经存在，查询这个值的编码
																																	let value1 = document.getElementById("upInput" + s + "").value.trim();
																																	let data = {
																																		attNameId: num,
																																		attValue: value1
																																	}
																																	axios({
																																		method: "post",
																																		url: "attributeValue/selectCode",
																																		data: data
																																	}).then(function (resp) {
																																		document.getElementById("upCode" + s + "").value = resp.data[0].code;
																																	})
																																}

																															})

																														} else {
																															//该值不存在,查询是否开启了值作为编码的功能
																															axios({
																																method: "post",
																																url: "attributeFunction/selectIfAutoCode",
																																data: num
																															}).then(function (resp) {
																																if (resp.data === true) {
																																	//开启了值作为编码的功能
																																	//先判断输入值的格式是否符合做编码的格式，然后判断长度是否符合编码的长度，然后再判断内容是否重复
																																	var pattern = /^[0-9A-Za-z]+$/;
																																	if (pattern.test(value) === true) {
																																		//符合标准，判断长度
																																		axios({
																																			method: "post",
																																			url: "attribute/selectById",
																																			data: num
																																		}).then(function (resp) {
																																			let datas = resp.data;
																																			if (Number(datas[0].length) >= Number(value.length)) {
																																				//长度符合，判断作为编码是否重复
																																				let codeData = {
																																					attNameId: "",
																																					attValue: "",
																																					code: ""
																																				}
																																				if (Number(datas[0].length) === Number(value.length)) {
																																					codeData = {
																																						attNameId: num,
																																						attValue: value,
																																						code: value
																																					}
																																				} else {
																																					let jnum = Number(datas[0].length) - Number(value.length);
																																					let str = "";
																																					for (let j = 0; j < jnum; j++) {
																																						str += "0"
																																					}
																																					codeData = {
																																						attNameId: num,
																																						attValue: value,
																																						code: str + value
																																					}
																																				}

																																				//查看该属性是否在映射中
																																				axios({
																																					method: "post",
																																					url: "mapping/selectMapping",
																																					data: sortId //分类id
																																				}).then(function (resp) {
																																					let mappingData = resp.data;
																																					let map = false;
																																					let index = 0;
																																					for (let j = 0; j < mappingData.length; j++) {
																																						if (Number(num) === mappingData[j].att_name_id) {
																																							map = true;
																																							index = j;
																																							break;
																																						}

																																					}

																																					if (map === true) {
																																						//在映射中，根据长度和起始位置改编物料编码
																																						//查看这个值是否已经被使用了
																																						let data = {
																																							attNameId: num,
																																							attValue: document.getElementById("upInput" + s + "").value.trim()

																																						}
																																						axios({
																																							method: "post",
																																							url: "attributeValue/selectValueExistAdd",
																																							data: data
																																						}).then(function (resp) {
																																							if (resp.data === true) {
																																								//值已经存在，查询这个值的编码
																																								let value1 = document.getElementById("upInput" + s + "").value.trim();
																																								let data = {
																																									attNameId: num,
																																									attValue: value1
																																								}
																																								axios({
																																									method: "post",
																																									url: "attributeValue/selectCode",
																																									data: data
																																								}).then(function (resp) {
																																									document.getElementById("upCode" + s + "").value = resp.data[0].code;
																																								})
																																							} else {
																																								//值不存在.查询该编码是否已经存在
																																								axios({
																																									method: "post",
																																									url: "attributeValue/selectCodeExist",
																																									data: codeData
																																								}).then(function (resp) {
																																									if (resp.data === true) {
																																										//编码存在，提示并清空输入框
																																										document.getElementById("upInput" + s + "").value = "";
																																										document.getElementById("upCode" + s + "").value = "";
																																										alert("该编码已存在，不能使用该值作为编码使用！")
																																									} else {
																																										//编码不存在
																																										//得到了属性值的编码
																																										let nine = document.getElementById("nineNumber").innerHTML;
																																										let str = nine;
																																										//判断输入的长度和编码长度

																																										let replaceStr = codeData.code;//要替换的字符串

																																										document.getElementById("upCode" + s + "").value = codeData.code;
																																										// document.getElementById("updiv"+s+"").innerHTML=replaceStr;
																																										let startIndex = mappingData[index].begin_location;


																																										let endIndex = mappingData[index].end_location;

																																										let newStr = str.slice(0, startIndex - 1) + replaceStr + str.slice(endIndex);

																																										document.getElementById("nineNumber").innerHTML = newStr;
																																										//获取分类编码
																																										let sortId = document.getElementById("produceId").innerHTML;
																																										document.getElementById("UpdateMaterialNumber").value = sortId + newStr + lsn;
																																										for (let j = 0; j < newStr.length; j++) {
																																											let m = Number(j) + Number(1);
																																											document.getElementById("upmdiv" + m + "").innerHTML = newStr[j];
																																										}
																																									}
																																								})

																																							}
																																						})

																																					} else {
																																						//不在映射中
																																						//查看这个值是否已经被使用了
																																						let data = {
																																							attNameId: num,
																																							attValue: document.getElementById("upInput" + s + "").value

																																						}
																																						axios({
																																							method: "post",
																																							url: "attributeValue/selectValueExistAdd",
																																							data: data
																																						}).then(function (resp) {
																																							if (resp.data === true) {
																																								//值已经存在，不在映射中，查询编码并显示
																																								axios({
																																									method: "post",
																																									url: "attributeValue/selectValueExistAdd",
																																									data: data
																																								}).then(function (resp) {
																																									document.getElementById("upCode" + s + "").value = resp.data[0].code;
																																								})
																																							} else {
																																								//值不存在，将编码显示
																																								document.getElementById("upCode" + s + "").value = codeData.code;
																																							}
																																						})

																																					}

																																				})


																																			} else {
																																				let value1 = document.getElementById("upInput" + s + "").value.trim();
																																				if (value1.length > 0) {
																																					alert("输入值的长度与编码长度不符，请核对后在进行操作！");
																																				}
																																				document.getElementById("upInput" + s + "").value = "";
																																				document.getElementById("upCode" + s + "").value = "";


																																			}
																																		})
																																	} else {
																																		let value2 = document.getElementById("upInput" + s + "").value.trim();
																																		if (value2.length > 0) {
																																			alert("输入值的格式不适合作为编码，请核对后再进行操作！");
																																		}
																																		document.getElementById("upInput" + s + "").value = "";
																																		document.getElementById("upCode" + s + "").value = "";
																																	}
																																} else {
																																	//该值不存在,查询是否开启了值作为编码的功能
																																	axios({
																																		method: "post",
																																		url: "attributeFunction/selectIfAutoCode",
																																		data: num
																																	}).then(function (resp) {
																																		if (resp.data === true) {
																																			//开启了值作为编码的功能
																																			//先判断输入值的格式是否符合做编码的格式，然后判断长度是否符合编码的长度，然后再判断内容是否重复
																																			var pattern = /^[0-9A-Za-z]+$/;
																																			if (pattern.test(value) === true) {
																																				//符合标准，判断长度
																																				axios({
																																					method: "post",
																																					url: "attribute/selectById",
																																					data: num
																																				}).then(function (resp) {
																																					let datas = resp.data;
																																					if (Number(datas[0].length) >= Number(value.length)) {
																																						//长度符合，判断作为编码是否重复
																																						let codeData = {
																																							attNameId: "",
																																							attValue: "",
																																							code: ""
																																						}
																																						if (Number(datas[0].length) === Number(value.length)) {
																																							codeData = {
																																								attNameId: num,
																																								attValue: value,
																																								code: value
																																							}
																																						} else {
																																							let jnum = Number(datas[0].length) - Number(value.length);
																																							let str = "";
																																							for (let j = 0; j < jnum; j++) {
																																								str += "0"
																																							}
																																							codeData = {
																																								attNameId: num,
																																								attValue: value,
																																								code: str + value
																																							}
																																						}

																																						//查看该属性是否在映射中
																																						axios({
																																							method: "post",
																																							url: "mapping/selectMapping",
																																							data: sortId //分类id
																																						}).then(function (resp) {
																																							let mappingData = resp.data;
																																							let map = false;
																																							let index = 0;
																																							for (let j = 0; j < mappingData.length; j++) {
																																								if (Number(num) === mappingData[j].att_name_id) {
																																									map = true;
																																									index = j;
																																									break;
																																								}

																																							}

																																							if (map === true) {
																																								//在映射中，根据长度和起始位置改编物料编码
																																								//查看这个值是否已经被使用了
																																								let data = {
																																									attNameId: num,
																																									attValue: document.getElementById("upInput" + s + "").value.trim()

																																								}
																																								axios({
																																									method: "post",
																																									url: "attributeValue/selectValueExistAdd",
																																									data: data
																																								}).then(function (resp) {
																																									if (resp.data === true) {
																																										//值已经存在，查询这个值的编码
																																										let value1 = document.getElementById("upInput" + s + "").value.trim();
																																										let data = {
																																											attNameId: num,
																																											attValue: value1
																																										}
																																										axios({
																																											method: "post",
																																											url: "attributeValue/selectCode",
																																											data: data
																																										}).then(function (resp) {
																																											document.getElementById("upCode" + s + "").value = resp.data[0].code;
																																										})
																																									} else {
																																										//值不存在.查询该编码是否已经存在
																																										axios({
																																											method: "post",
																																											url: "attributeValue/selectCodeExist",
																																											data: codeData
																																										}).then(function (resp) {
																																											if (resp.data === true) {
																																												//编码存在，提示并清空输入框
																																												document.getElementById("upInput" + s + "").value = "";
																																												document.getElementById("upCode" + s + "").value = "";
																																												alert("该编码已存在，不能使用该值作为编码使用！")
																																											} else {
																																												//编码不存在
																																												//得到了属性值的编码
																																												let nine = document.getElementById("nineNumber").innerHTML;
																																												let str = nine;
																																												//判断输入的长度和编码长度

																																												let replaceStr = codeData.code;//要替换的字符串
																																												document.getElementById("upCode" + s + "").value = codeData.code;
																																												let startIndex = mappingData[index].begin_location;


																																												let endIndex = mappingData[index].end_location;
																																												let newStr = str.slice(0, startIndex - 1) + replaceStr + str.slice(endIndex);

																																												document.getElementById("nineNumber").innerHTML = newStr;

																																												//获取分类编码
																																												let sortId = document.getElementById("produceId").innerHTML;
																																												document.getElementById("UpdateMaterialNumber").value = sortId + newStr + lsn;
																																												for (let j = 0; j < newStr.length; j++) {
																																													let m = Number(j) + Number(1);
																																													document.getElementById("upmdiv" + m + "").innerHTML = newStr[j];
																																												}
																																											}
																																										})

																																									}
																																								})

																																							} else {
																																								//不在映射中
																																								//查看这个值是否已经被使用了
																																								let data = {
																																									attNameId: num,
																																									attValue: document.getElementById("upInput" + s + "").value.trim()

																																								}
																																								axios({
																																									method: "post",
																																									url: "attributeValue/selectValueExistAdd",
																																									data: data
																																								}).then(function (resp) {
																																									if (resp.data === true) {
																																										//值已经存在，不在映射中，查询编码并显示
																																										axios({
																																											method: "post",
																																											url: "aAttributeValue/selectValueExistAdd",
																																											data: data
																																										}).then(function (resp) {
																																											document.getElementById("upCode" + s + "").value = resp.data[0].code;
																																										})
																																									} else {
																																										//值不存在，将编码显示
																																										document.getElementById("upCode" + s + "").value = codeData.code;
																																									}
																																								})
																																							}

																																						})


																																					} else {
																																						let value1 = document.getElementById("upInput" + s + "").value;
																																						if (value1.length > 0) {
																																							alert("输入值的长度与编码长度不符，请核对后在进行操作！");
																																						}
																																						document.getElementById("upInput" + s + "").value = "";
																																						document.getElementById("upCode" + s + "").value = "";


																																					}
																																				})
																																			} else {
																																				let value2 = document.getElementById("upInput" + s + "").value;
																																				if (value2.length > 0) {
																																					alert("输入值的格式不适合作为编码，请核对后再进行操作！");
																																				}
																																				document.getElementById("upInput" + s + "").value = "";
																																				document.getElementById("upCode" + s + "").value = "";
																																			}
																																		} else {
																																			//未开启值作为编码的功能,查看是否有编码表存在,查询是否开启了顺序编码的功能
																																			axios({
																																				method: "post",
																																				url: "attributeFunction/selectIfOrderCode",
																																				data: num
																																			}).then(function (resp) {
																																				if (resp.data === true) {
																																					//开启了顺序编码的功能
																																					//获取长度
																																					axios({
																																						method: "post",
																																						url: "attribute/selectById",
																																						data: num
																																					}).then(function (resp) {
																																						let datas = resp.data;
																																						let length = datas[0].length;
																																						//查询该属性下有多少个编码
																																						axios({
																																							method: "post",
																																							url: "attributeValue/selectByAttNameId",
																																							data: num
																																						}).then(function (resp) {
																																							let CodeNumber = resp.data.length;
																																							let number = Number(CodeNumber) + Number(1);
																																							let generateCode1 = generateCode(length, number, list, "");

																																							function orderNoRepeat(generateCode1) {
																																								//调用方法获取编码

																																								//验证编码是否重复
																																								let codeData = {
																																									attNameId: num,
																																									code: generateCode1,
																																								}
																																								axios({
																																									method: "post",
																																									url: "attributeValue/selectCodeExistAdd",
																																									data: codeData
																																								}).then(function (resp) {
																																									if (resp.data === true) {
																																										//存在
																																										number++;
																																										let generateCode1 = generateCode(length, number, list, "");
																																										orderNoRepeat(generateCode1);

																																									} else if (resp.data === false) {
																																										//不存在
																																										//查看该属性是否在映射中
																																										axios({
																																											method: "post",
																																											url: "mapping/selectMapping",
																																											data: sortId //分类id
																																										}).then(function (resp) {
																																											let mappingData = resp.data;
																																											let map = false;
																																											let index = 0;
																																											for (let j = 0; j < mappingData.length; j++) {
																																												if (Number(num) === mappingData[j].att_name_id) {
																																													map = true;
																																													index = j;
																																													break;
																																												}

																																											}
																																											if (map === true) {
																																												//值不存在
																																												//得到了属性值的编码
																																												let nine = document.getElementById("nineNumber").innerHTML;
																																												let str = nine;
																																												//判断输入的长度和编码长度

																																												let replaceStr = codeData.code;//要替换的字符串

																																												document.getElementById("upCode" + s + "").value = codeData.code;
																																												// document.getElementById("updiv"+s+"").innerHTML=replaceStr;
																																												let startIndex = mappingData[index].begin_location;


																																												let endIndex = mappingData[index].end_location;

																																												let newStr = str.slice(0, startIndex - 1) + replaceStr + str.slice(endIndex);

																																												document.getElementById("nineNumber").innerHTML = newStr;
																																												//获取分类编码
																																												let sortId = document.getElementById("produceId").innerHTML;
																																												document.getElementById("UpdateMaterialNumber").value = sortId + newStr + lsn;
																																												for (let j = 0; j < newStr.length; j++) {
																																													let m = Number(j) + Number(1);
																																													document.getElementById("upmdiv" + m + "").innerHTML = newStr[j];
																																												}
																																											} else {
																																												//不在映射中
																																												//查看这个值是否已经被使用了
																																												let data = {
																																													attNameId: num,
																																													attValue: document.getElementById("upInput" + s + "").value.trim()

																																												}
																																												axios({
																																													method: "post",
																																													url: "attributeValue/selectValueExistAdd",
																																													data: data
																																												}).then(function (resp) {
																																													if (resp.data === true) {
																																														//值已经存在，不在映射中，查询编码并显示
																																														axios({
																																															method: "post",
																																															url: "attributeValue/selectValueExistAdd",
																																															data: data
																																														}).then(function (resp) {
																																															document.getElementById("upCode" + s + "").value = resp.data[0].code;
																																														})
																																													} else {
																																														//值不存在，将编码显示
																																														document.getElementById("upCode" + s + "").value = codeData.code;
																																													}
																																												})
																																											}
																																										})


																																									}
																																								})
																																							}

																																							orderNoRepeat(generateCode1);


																																						})
																																					})
																																				} else {
																																					//未开启值作为编码，也未开启顺序编码
																																					axios({
																																						method: "post",
																																						url: "attributeValue/selectByAttNameId",
																																						data: num
																																					}).then(function (resp) {
																																						if (resp.data.length > 0) {
																																							//有编码表
																																							document.getElementById("upInput" + s + "").value = "";
																																							document.getElementById("upCode" + s + "").value = "";
																																						} else {
																																							//没有编码表
																																							document.getElementById("upCode" + s + "").value = "";
																																						}
																																					})
																																				}
																																			})


																																		}
																																	})
																																}
																															})
																														}
																													})
																												}

																												if (rangeDatas[0].range === 0) {
																													//0表示是值
																													//模糊查询
																													axios({
																														method: "post",
																														url: "attributeValue/inputLX?attNameId=" + num + "",
																														data: value
																													}).then(function (resp) {
																														let lxDatas = resp.data;


																														if (lxDatas.length > 0) {
																															var formdata = "";
																															if (lxDatas.length > 5) {
																																for (let i = 0; i < 5; i++) {
																																	formdata += "<div class='ULX" + num + "' style='width: 100%;height: auto;margin: auto'><pre>" + lxDatas[i].att_value + "</pre></div>"
																																}
																															} else if (lxDatas.length === 1) {
																																formdata += "<div class='ULX" + num + "' style='width: 100%;height: auto;margin: auto'><pre>" + lxDatas[0].att_value + "</pre></div>"
																															} else {
																																for (let i = 0; i < lxDatas.length; i++) {
																																	formdata += "<div class='ULX" + num + "' style='width: 100%;height: auto;margin: auto'><pre>" + lxDatas[i].att_value + "</pre></div>"
																																}
																															}

																															document.getElementById("inputUpValue" + mdata[j].id + "").innerHTML = formdata;


																															//绑定触发变色
																															let lxs = document.querySelectorAll(".ULX" + num + "");
																															for (let j = 0; j < lxs.length; j++) {
																																lxs[j].onmouseover = function () {
																																	lxs[j].style.backgroundColor = "white"
																																}
																																lxs[j].onmouseout = function () {
																																	lxs[j].style.backgroundColor = "#e0e0e0"
																																}

																															}

																															document.getElementById("inputUpValue" + mdata[j].id + "").style.display = "";


																															//先去除所有的绑定方法
																															document.getElementById("inputUpValue" + mdata[j].id + "").onmouseout = null;
																															document.getElementById("inputUpValue" + mdata[j].id + "").onmouseover = null;
																															document.getElementById("upInput" + mdata[j].id + "").onblur = null;

																															//失去光标要执行的方法
																															document.getElementById("upInput" + mdata[j].id + "").onblur = function () {
																																//没有动作，失去光标，获取输入的值
																																let value1 = document.getElementById("upInput" + mdata[j].id + "").value.trim();
																																if (value1.length > 0) {
																																	autoAndNo(value1, num, mdata[j].id);
																																} else {
																																	document.getElementById("upInput" + mdata[j].id + "").value = "";
																																	//查看该属性是否在映射中
																																	axios({
																																		method: "post",
																																		url: "mapping/selectMapping",
																																		data: sortId //分类id
																																	}).then(function (resp) {
																																		let mappingData = resp.data;
																																		let map = false;
																																		let index = 0;
																																		for (let j = 0; j < mappingData.length; j++) {
																																			if (Number(num) === mappingData[j].att_name_id) {
																																				map = true;
																																				index = j;
																																				break;
																																			}

																																		}

																																		if (map === true) {
																																			//在映射中，根据长度和起始位置改编物料编码

																																			let nine = document.getElementById("nineNumber").innerHTML;
																																			let str = nine;
																																			//判断输入的长度和编码长度
																																			let replaceStr = "";
																																			for (let j = 0; j < mappingData[index].length; j++) {
																																				replaceStr += "0";
																																			}

																																			// document.getElementById("updiv"+s+"").innerHTML=replaceStr;
																																			let startIndex = mappingData[index].begin_location;


																																			let endIndex = mappingData[index].end_location;

																																			let newStr = str.slice(0, startIndex - 1) + replaceStr + str.slice(endIndex);

																																			document.getElementById("nineNumber").innerHTML = newStr;
																																			//获取分类编码
																																			let sortId = document.getElementById("produceId").innerHTML;
																																			document.getElementById("UpdateMaterialNumber").value = sortId + newStr + lsn;
																																			for (let j = 0; j < newStr.length; j++) {
																																				let m = Number(j) + Number(1);
																																				document.getElementById("upmdiv" + m + "").innerHTML = newStr[j];
																																			}


																																		}
																																	})
																																}
																																document.getElementById("inputUpValue" + mdata[j].id + "").style.display = "none";

																															}
																															//因为点击会默认执行失去光标的方法，所以鼠标移动到这里先将失去光标的方法置空，移出后再添加回来
																															document.getElementById("inputUpValue" + mdata[j].id + "").onmouseover = function () {
																																document.getElementById("upInput" + mdata[j].id + "").onblur = null;
																															}
																															document.getElementById("inputUpValue" + mdata[j].id + "").onmouseout = function () {
																																document.getElementById("upInput" + mdata[j].id + "").onblur = function () {
																																	//没有动作，失去光标，获取输入的值
																																	let value1 = document.getElementById("upInput" + mdata[j].id + "").value.trim();
																																	if (value.length > 0) {
																																		autoAndNo(value1, num, mdata[j].id);
																																	} else {
																																		document.getElementById("upInput" + mdata[j].id + "").value = "";
																																		//查看该属性是否在映射中
																																		axios({
																																			method: "post",
																																			url: "mapping/selectMapping",
																																			data: sortId //分类id
																																		}).then(function (resp) {
																																			let mappingData = resp.data;
																																			let map = false;
																																			let index = 0;
																																			for (let j = 0; j < mappingData.length; j++) {
																																				if (Number(num) === mappingData[j].att_name_id) {
																																					map = true;
																																					index = j;
																																					break;
																																				}

																																			}

																																			if (map === true) {
																																				//在映射中，根据长度和起始位置改编物料编码

																																				let nine = document.getElementById("nineNumber").innerHTML;
																																				let str = nine;
																																				//判断输入的长度和编码长度
																																				let replaceStr = "";
																																				for (let j = 0; j < mappingData[index].length; j++) {
																																					replaceStr += "0";
																																				}

																																				// document.getElementById("updiv"+s+"").innerHTML=replaceStr;
																																				let startIndex = mappingData[index].begin_location;


																																				let endIndex = mappingData[index].end_location;

																																				let newStr = str.slice(0, startIndex - 1) + replaceStr + str.slice(endIndex);

																																				document.getElementById("nineNumber").innerHTML = newStr;
																																				//获取分类编码
																																				let sortId = document.getElementById("produceId").innerHTML;
																																				document.getElementById("UpdateMaterialNumber").value = sortId + newStr + lsn;
																																				for (let j = 0; j < newStr.length; j++) {
																																					let m = Number(j) + Number(1);
																																					document.getElementById("upmdiv" + m + "").innerHTML = newStr[j];
																																				}


																																			}
																																		})
																																	}
																																	document.getElementById("inputUpValue" + mdata[j].id + "").style.display = "none";

																																}
																															}

																															//添加点击查询到的结果执行方法
																															lxs = document.querySelectorAll(".ULX" + num + "");
																															// for (let j = 0; j < lxs.length; j++) {
																															// 	lxs[j].onclick=function (){
																															// 		//将点击的值放在输入框中
																															// 		document.getElementById("upInput"+mdata[j].id+"").value=lxDatas[j].att_value;
																															// 		autoAndNo(lxDatas[j].att_value,num,mdata[j].id);
																															// 		document.getElementById("inputUpValue"+mdata[j].id+"").style.display="none";
																															//
																															//
																															// 	}
																															// }

																															for (let l = 0; l < lxs.length; l++) {
																																lxs[l].onclick = function () {
																																	//将点击的值放在输入框中
																																	document.getElementById("upInput" + mdata[j].id + "").value = lxDatas[l].att_value;
																																	autoAndNo(lxDatas[l].att_value, num, mdata[j].id);
																																	document.getElementById("inputUpValue" + mdata[j].id + "").style.display = "none";
																																}
																															}


																														} else {

																															//先去除所有的绑定方法
																															document.getElementById("inputUpValue" + mdata[j].id + "").onmouseout = null;
																															document.getElementById("inputUpValue" + mdata[j].id + "").onmouseover = null;
																															document.getElementById("upInput" + mdata[j].id + "").onblur = null;
																															document.getElementById("inputUpValue" + mdata[j].id + "").style.display = "";
																															document.getElementById("inputUpValue" + mdata[j].id + "").innerHTML = "<div class='ULX" + num + "' style='width: 100%;height: auto;margin: auto;color: red'>没有相关数据!</div>"

																															//失去光标要执行的方法
																															document.getElementById("upInput" + mdata[j].id + "").onblur = function () {
																																//没有动作，失去光标，获取输入的值
																																let value1 = document.getElementById("upInput" + mdata[j].id + "").value.trim();
																																if (value1.length > 0) {
																																	autoAndNo(value1, num, mdata[j].id);
																																} else {
																																	document.getElementById("upInput" + mdata[j].id + "").value = "";
																																	//查看该属性是否在映射中
																																	axios({
																																		method: "post",
																																		url: "mapping/selectMapping",
																																		data: sortId //分类id
																																	}).then(function (resp) {
																																		let mappingData = resp.data;
																																		let map = false;
																																		let index = 0;
																																		for (let j = 0; j < mappingData.length; j++) {
																																			if (Number(num) === mappingData[j].att_name_id) {
																																				map = true;
																																				index = j;
																																				break;
																																			}

																																		}

																																		if (map === true) {
																																			//在映射中，根据长度和起始位置改编物料编码

																																			let nine = document.getElementById("nineNumber").innerHTML;
																																			let str = nine;
																																			//判断输入的长度和编码长度
																																			let replaceStr = "";
																																			for (let j = 0; j < mappingData[index].length; j++) {
																																				replaceStr += "0";
																																			}

																																			// document.getElementById("updiv"+s+"").innerHTML=replaceStr;
																																			let startIndex = mappingData[index].begin_location;


																																			let endIndex = mappingData[index].end_location;

																																			let newStr = str.slice(0, startIndex - 1) + replaceStr + str.slice(endIndex);

																																			document.getElementById("nineNumber").innerHTML = newStr;
																																			//获取分类编码
																																			let sortId = document.getElementById("produceId").innerHTML;
																																			document.getElementById("UpdateMaterialNumber").value = sortId + newStr + lsn;
																																			for (let j = 0; j < newStr.length; j++) {
																																				let m = Number(j) + Number(1);
																																				document.getElementById("upmdiv" + m + "").innerHTML = newStr[j];
																																			}


																																		}
																																	})
																																}

																																document.getElementById("inputUpValue" + mdata[j].id + "").style.display = "none";

																															}

																														}
																													})
																												} else if (rangeDatas[0].range === 1) {

																													//判断输入的值是否符合数据库中的范围
																													function compare(str, value) {

																														//  console.log("范围"+ str)
																														// console.log("传入的值"+value)
																														if (value.length > 0) {
																															var pattern = /^\[.*\]$/;
																															var patterns = /^\<.*\]$/;
																															var pattern1 = /^\<.*\>$/;
																															var patterns1 = /^\[.*\>$/;

																															if (pattern.test(str) === true) {
																																str = str.split("[").join("");
																																str = str.split("]").join("");
																																// str=str.replace(/[a-zA-Z]/g, '');
																																let strings = str.split("~");

																																//判断输入的数值是否符合这个范围
																																if (Number(power(value)) >= Number(power(strings[0])) && Number(power(value)) <= Number(power(strings[1]))) {
																																	return true;
																																} else {
																																	return false;
																																}

																															} else if (patterns.test(str) === true) {

																																str = str.split("<").join("");
																																str = str.split("]").join("");
																																// str=str.replace(/[a-zA-Z]/g, '');
																																let strings = str.split("~");

																																// console.log("开始"+ strings[0]);
																																// console.log("结束"+strings[1])

																																//判断输入的数值是否符合这个范围
																																if (Number(power(value)) > Number(power(strings[0])) && Number(power(value)) <= Number(power(strings[1]))) {
																																	return true;
																																} else {
																																	return false;
																																}
																															} else if (pattern1.test(str) === true) {

																																str = str.split("<").join("");
																																str = str.split(">").join("");
																																// str=str.replace(/[a-zA-Z]/g, '');
																																let strings = str.split("~");

																																// console.log("开始"+ strings[0]);
																																// console.log("结束"+strings[1])

																																//判断输入的数值是否符合这个范围
																																if (Number(power(value)) > Number(power(strings[0])) && Number(power(value)) < Number(power(strings[1]))) {
																																	return true;
																																} else {
																																	return false;
																																}
																															} else if (patterns1.test(str) === true) {

																																str = str.split("[").join("");
																																str = str.split(">").join("");
																																// str=str.replace(/[a-zA-Z]/g, '');
																																let strings = str.split("~");

																																// console.log("开始"+ strings[0]);
																																// console.log("结束"+strings[1])

																																//判断输入的数值是否符合这个范围
																																if (Number(power(value)) >= Number(power(strings[0])) && Number(power(value)) < Number(power(strings[1]))) {
																																	return true;
																																} else {
																																	return false;
																																}
																															} else if (pattern.test(str) === false && patterns.test(str) === false && pattern1.test(str) === false && patterns1.test(str) === false) {
																																alert("数据有问题，不符合格式，请联系相关人员！");

																															}
																														} else {
																															return false;
																														}


																													}

																													//1表示是范围,获取所有的范围值
																													axios({
																														method: "post",
																														url: "attributeValue/selectByAttNameId",
																														data: num
																													}).then(function (resp) {
																														let datas = resp.data;
																														let b;
																														let num2 = mdata[j].id
																														for (let i = 0; i < datas.length; i++) {
																															b = compare(datas[i].attValue, value);
																															if (b === true) {
																																document.getElementById("inputUpValue" + num2 + "").innerHTML = "<div id='LX" + num + "' style='width: 100%;height: auto;margin: auto;'><pre>" + datas[i].attValue + "</pre></div>"
																																document.getElementById("inputUpValue" + num2 + "").style.display = "";

																																function blurAndClick() {

																																	document.getElementById("inputUpValue" + num2 + "").style.display = "none";
																																	document.getElementById("upCode" + num2 + "").value = datas[i].code;
																																	// document.getElementById("updiv"+num2+"").innerHTML=datas[i].code;
																																	//查询这个属性是否在映射中
																																	axios({
																																		method: "post",
																																		url: "mapping/selectMapping",
																																		data: sortId//分类id
																																	}).then(function (resp) {
																																		let mappingData = resp.data;
																																		let map = false;
																																		let index = 0;
																																		for (let j = 0; j < mappingData.length; j++) {
																																			if (Number(num) === mappingData[j].att_name_id) {
																																				map = true;
																																				index = j;
																																				break;
																																			}

																																		}

																																		if (map === true) {
																																			//存在映射中
																																			let nine = document.getElementById("nineNumber").innerHTML;
																																			let str = nine;
																																			//判断输入的长度和编码长度

																																			let replaceStr = datas[i].code;//要替换的字符串
																																			let startIndex = mappingData[index].begin_location;

																																			let endIndex = mappingData[index].end_location;

																																			let newStr = str.slice(0, startIndex - 1) + replaceStr + str.slice(endIndex);

																																			document.getElementById("nineNumber").innerHTML = newStr;
																																			//获取分类编码
																																			let sortId = document.getElementById("produceId").innerHTML;
																																			document.getElementById("UpdateMaterialNumber").value = sortId + newStr + lsn;
																																			for (let j = 0; j < newStr.length; j++) {
																																				let m = Number(j) + Number(1);
																																				document.getElementById("upmdiv" + m + "").innerHTML = newStr[j];
																																			}
																																		}

																																	})


																																}

																																document.getElementById("LX" + num + "").onclick = blurAndClick;
																																let trim = document.getElementById("upInput" + num2 + "").value.trim();
																																if (trim.length > 0) {
																																	document.getElementById("upInput" + num2 + "").onblur = blurAndClick;
																																} else {
																																	document.getElementById("upInput" + num2 + "").value = "";
																																}

																																break;
																															}

																														}

																														if (b === false) {
																															document.getElementById("inputUpValue" + num2 + "").style.display = "";
																															document.getElementById("inputUpValue" + num2 + "").innerHTML = "<div id='LX" + num + "' style='width: 100%;height: auto;margin: auto;color: red'>没有相关数据!</div>"
																															document.getElementById("LX" + num + "").onclick = function () {
																																document.getElementById("inputUpValue" + num2 + "").style.display = "none";
																																document.getElementById("upInput" + num2 + "").value = "";
																															}
																															document.getElementById("upInput" + num2 + "").onblur = function () {
																																document.getElementById("inputUpValue" + num2 + "").style.display = "none";
																																document.getElementById("upInput" + num2 + "").value = "";

																															}
																														}

																													})


																												}
																											})
																										}
																									})
																								} else {

																									document.getElementById("upCode" + mdata[j].id + "").value = "";
																								}


																							}, 500));
																							input.addEventListener('blur', function () {
																								upblack(mdata[j].id)
																							});
																						}
																					}


																				}


																				//查询是否存在出入库日志
																				let promise = ifLog(data[i].id, data[i].vault);
																				promise.then(function (resp) {
																					if (resp === true) {
																						//存在，不允许修改
																						for (let j = 0; j < datas.length; j++) {
																							for (let k = 0; k < mdata.length; k++) {
																								if (Number(datas[j].att_name_id) === Number(mdata[k].parentId)) {
																									document.getElementById("upInput" + mdata[k].id + "").disabled = true;
																									document.getElementById("updateUnit").disabled = true;
																									document.getElementById("bb" + mdata[k].id + "").style.color = "red";
																									document.getElementById("bb" + mdata[k].id + "").style.display = "";

																								}
																							}
																						}
																					} else {
																						//不存在，允许修改
																						for (let j = 0; j < datas.length; j++) {
																							for (let k = 0; k < mdata.length; k++) {
																								if (Number(datas[j].att_name_id) === Number(mdata[k].parentId)) {
																									document.getElementById("bb" + mdata[k].id + "").style.color = "red";
																									document.getElementById("bb" + mdata[k].id + "").style.display = "";

																								}
																							}
																						}
																					}
																				})


																			}
																		})


																		//点击更新,循环完了绑定更新事件，更新完基础的再更新后添加的
																		document.getElementById("updateSubmit").onclick = function () {

																			let booleanPromise = updateQx();
																			booleanPromise.then(async function (resp) {
																				if (resp === true) {
																					//获取数据
																					let formdata = {
																						id: datas[0].id,
																						parentId: sortId,
																						name: "",
																						url: "",
																						materialNumber: "",
																						brand: "",
																						description: "",
																						number: "",
																						unit: "",
																						priceUnit: ""
																					}

																					formdata.name = document.getElementById("updateName").value;
																					let ylPc = document.querySelector(".ylPic");
																					if (ylPc) {
																						formdata.url = ylPc.querySelector("img").src;
																					}
																					formdata.materialNumber = document.getElementById("UpdateMaterialNumber").value;
																					formdata.brand = document.getElementById("updateBrand").value;
																					formdata.description = document.getElementById("updateDescription").value;
																					formdata.unit = document.getElementById("updateUnit").value;
																					// formdata.number = document.getElementById("updateNumber").value;
																					//获取筛选框中的内容
																					let priceUnitSelect = document.getElementById("updatePriceUnit");
																					let priceUnitIndex = priceUnitSelect.selectedIndex;//获取select的索引
																					//获取选中的值
																					formdata.priceUnit = priceUnitSelect.options[priceUnitIndex].value;

																					let b = updateIfBaseExist();
																					if (b === true) {
																						//判断后添加的是否为空


																						var content = {
																							id: "",
																							parentId: "",
																							productId: "",
																							content: "",
																						}

																						var list = [mdata.length]


																						for (let i = 0; i < mdata.length; i++) {

																							content.id = mdata[i].id;
																							content.content = document.getElementById("upInput" + mdata[i].id + "").value.trim();
																							content.parentId = mdata[i].parentId
																							list[i] = content;
																							content = {
																								id: "",
																								parentId: "",
																								productId: "",
																								content: "",
																							}
																						}

																						//判断开启了唯一标识的是否重复
																						let unique = false;
																						let dj = 0;
																						for (let j = 0; j < list.length; j++) {
																							let parentId = list[j].parentId;
																							let productId = datas[0].id;
																							let content1 = list[j].content;
																							let b1 = await uniqueUpdate(parentId, content1, productId);
																							if (b1 === true) {
																								unique = true;
																								dj = j;
																								break;
																							}
																						}
																						if (unique === true) {
																							//查询属性名
																							axios({
																								method: "post",
																								url: "attribute/selectById",
																								data: list[dj].parentId
																							}).then(function (resp) {
																								let datas = resp.data;
																								alert("" + datas[0].name + "开启了唯一标识，" + list[dj].content + "已存在！")
																							})
																						} else {
																							let l = true;
																							//不在映射中的可以为空
																							axios({
																								method: "post",
																								url: "mapping/selectMapping",
																								data: sortId
																							}).then(function (resp) {
																								let mappingdatas = resp.data;

																								for (let j = 0; j < list.length; j++) {
																									for (let k = 0; k < mappingdatas.length; k++) {
																										if (Number(list[j].parentId) === Number(mappingdatas[k].att_name_id)) {
																											let trim = list[j].content.trim();
																											if (trim.length === 0 || document.getElementById("upCode" + list[j].id + "").value.length === 0) {
																												l = false;
																											}
																											if (l === false) {
																												break;
																											}
																										}
																									}

																								}


																								function update(threeData) {
																									var content = {
																										id: "",
																										parentId: "",
																										productId: "",
																										content: "",
																									}

																									var list = [mdata.length]


																									for (let i = 0; i < mdata.length; i++) {

																										content.id = mdata[i].id;
																										content.content = document.getElementById("upInput" + mdata[i].id + "").value.trim();
																										content.parentId = mdata[i].parentId
																										list[i] = content;
																										content = {
																											id: "",
																											parentId: "",
																											productId: "",
																											content: "",
																										}
																									}


																									//更新操作
																									axios({
																										method: "post",
																										url: "product/updateById",
																										data: formdata,
																									}).then(function (resp) {
																										if (resp.data === "success") {

																											//提交多条数据
																											axios({
																												method: "post",
																												url: "attribute/updateAttributeContent",
																												data: list,
																											}).then(function (resp) {

																												if (resp.data === "success") {
																													//更新成功，下面进行编码的添加，先判断哪些属性不在映射中就要查询该分类下的映射
																													if (threeData.length > 0) {
																														//执行添加编码的操作
																														axios({
																															method: "post",
																															url: "attributeValue/addCodeAuto",
																															data: threeData
																														}).then(function (resp) {
																															if (resp.data === "success") {

																																alert("更新成功！");
																																select(foreverNumber, n);
																																let wd = document.querySelector(".window");
																																wd.style.display = "none";
																															} else {
																																alert("更新成功，但添加编码失败，请联系先关人员！")
																															}
																														})
																													} else {
																														alert("更新成功！");
																														select(foreverNumber, n);
																														let wd = document.querySelector(".window");
																														wd.style.display = "none";
																													}


																												} else {
																													alert("更新失败，请联系相关人员！")
																												}

																											})


																										} else {
																											alert("修改失败，请联系相关人员！")
																										}
																									})
																								}

																								function ifyiy(threeData) {
																									for (let i = 0; i < mdata.length; i++) {

																										content.id = mdata[i].id;
																										content.content = document.getElementById("upInput" + mdata[i].id + "").value.trim();
																										content.parentId = mdata[i].parentId
																										list[i] = content;
																										content = {
																											id: "",
																											parentId: "",
																											productId: "",
																											content: "",
																										}
																									}

																									for (let j = 0; j < list.length; j++) {
																										for (let k = 0; k < mappingdatas.length; k++) {
																											if (Number(list[j].parentId) === Number(mappingdatas[k].att_name_id)) {
																												let trim = list[j].content.trim();
																												if (trim.length === 0 || document.getElementById("upCode" + list[j].id + "").value.length === 0) {
																													l = false;
																												}
																												if (l === false) {
																													break;
																												}
																											}
																										}

																									}
																									//自动生成一组数据所两两组合的所有结果
																									let str = [{num: "0"}, {num: "1"}, {num: "2"}, {num: "3"}, {num: "4"}, {num: "5"}, {num: "6"}, {num: "7"}, {num: "8"}, {num: "9"}]
																									let ls1 = str[0].num;
																									let ls2;
																									let lsSum = [];
																									let lsD = [{}];

																									async function lsNumber3(q) {
																										if (q < str.length) {
																											for (let i = 0; i < str.length; i++) {

																												ls2 = str[i].num;
																												ls1 = str[q].num
																												if (String(ls2) === String(str[str.length - 1].num)) {
																													q++;

																													let sum = (ls1 + ls2)
																													lsD = [{num: "" + sum + ""}];
																													lsSum = lsD.concat(lsSum);
																													lsD = {};
																													await lsNumber3(q);


																												} else {
																													let sum = (ls1 + ls2)
																													lsD = [{num: "" + sum + ""}];
																													lsSum = lsD.concat(lsSum);
																													lsD = {};

																												}
																											}
																										} else if (q === str.length) {

																											//查询是否开启了映射编码的属性
																											let b1 = await ifSerialExistUpdate(mappingdatas);
																											if (b1 === true) {
																												//开启了映射编码表的功能，查询当前属性使用的映射中编码用到哪里了
																												//查询物料号是否改变，如果没改变直接更新
																												let value = document.getElementById("UpdateMaterialNumber").value;
																												let materialNumber = datas[0].materialNumber;
																												if (value === materialNumber) {
																													await update(threeData);
																												} else {
																													await serialCodeUpdate(mappingdatas, formdata.parentId, list);
																													formdata.materialNumber = document.getElementById("UpdateMaterialNumber").value;
																													//查询是否重复
																													let b4 = await ifMaterialNumberExistUpdate(formdata);

																													if (b4 === true) {
																														//重复，使用后两位流水码
																														if (window.confirm("系统检测到要使用流水码，是否要进行添加？")) {
																															async function sub2(s) {

																																if (Number(s) >= 0) {
																																	//后两位流水码更换
																																	let value5 = document.getElementById("UpdateMaterialNumber").value;
																																	//替换最后两位
																																	let newStr = value5.substring(0, value5.length - 2) + lsSum[s].num;


																																	formdata.materialNumber = newStr;


																																	axios({
																																		method: "post",
																																		url: "product/selectUpMN",
																																		data: formdata
																																	}).then(async function (resp) {
																																		if (resp.data === false) {
																																			//不重复了，执行添加操作
																																			await update(threeData)


																																		} else {
																																			s--;
																																			await sub2(s);
																																		}

																																	})

																																} else {
																																	//默认流水码用完了，映射流水码+1
																																	let b2 = await serialCodeAddOneUpdate(mappingdatas, formdata.parentId, list);
																																	if (b2 === true) {
																																		await sub2(lsSum.length - 1);

																																	} else {
																																		//映射中流水码用完了，使用后两位
																																		//映射中的流水码用完了，默认最后两位流水码用完要报警
																																		function sub1(s) {

																																			if (Number(s) >= 0) {
																																				//后两位流水码更换
																																				let value5 = document.getElementById("materialNumber").value;
																																				//替换最后两位
																																				let newStr = value5.substring(0, value5.length - 2) + lsSum[s].num;

																																				formdata.materialNumber = newStr;


																																				axios({
																																					method: "post",
																																					url: "product/selectUpMN",
																																					data: formdata
																																				}).then(async function (resp) {
																																					if (resp.data === false) {
																																						//不重复了，执行添加操作
																																						await update(threeData)


																																					} else {
																																						s--;
																																						sub1(s);
																																					}

																																				})

																																			} else {
																																				alert("流水码已用完！请联系相关人员！")
																																			}

																																		}

																																		sub1(lsSum.length - 1);

																																	}

																																}

																															}

																															await sub2(lsSum.length - 1);
																															return true;
																														} else {
																															return false;
																														}

																													} else {
																														//不重复，直接提交
																														await update(threeData);
																													}
																												}


																											} else {
																												//未开启映射编码的属性
																												//得到了所有的集合
																												//判断物料号是否重复
																												axios({
																													method: "post",
																													url: "product/selectUpMN",
																													data: formdata
																												}).then(async function (resp) {
																													if (resp.data === true) {

																														if (window.confirm("系统检测到要使用流水码，是否要进行添加？")) {

																															async function sub(s) {

																																if (Number(s) >= 0) {
																																	//后两位流水码更换
																																	let value5 = document.getElementById("UpdateMaterialNumber").value;
																																	//替换最后两位
																																	let newStr = value5.substring(0, value5.length - 2) + lsSum[s].num;

																																	formdata.materialNumber = newStr;


																																	axios({
																																		method: "post",
																																		url: "product/selectUpMN",
																																		data: formdata
																																	}).then(function (resp) {
																																		if (resp.data === false) {
																																			//不重复了，执行添加操作
																																			update(threeData);


																																		} else {
																																			s--;
																																			sub(s);
																																		}

																																	})

																																} else {

																																	alert("流水码已用完！请联系相关人员！")

																																}

																															}

																															await sub(lsSum.length - 1);
																															return true;
																														} else {
																															return false;
																														}


																													} else {
																														//代表不重复
																														await update(threeData)
																													}
																												})
																											}


																										}

																									}

																									//判断是否是同一个物料.1。先判断物料名称是否重复
																									axios({
																										method: "post",
																										url: "product/selectNameIfExist",
																										data: formdata
																									}).then(async function (resp) {
																										let d = 0;
																										let e = true;
																										if (resp.data === true) {
																											//存在当前名称,查询后来添加的属性是否有重复的
																											//查询当前分类下的产品id
																											await axios({
																												method: "post",
																												url: "product/selectAllInSortDeleteSignUpdate",
																												data: formdata
																											}).then(async function (resp) {
																												let datas = resp.data;
																												for (let j = 0; j < datas.length; j++) {
																													if (Number(formdata.id) !== Number(datas[j].id)) {
																														d = 0;

																														for (let k = 0; k < list.length; k++) {
																															list[k].productId = datas[j].id;
																															await axios({
																																method: "post",
																																url: "attributeFunction/selectByAttNameId",
																																data: list[k].parentId
																															}).then(async function (resp) {
																																let functionCode = resp.data;
																																let serialCode = functionCode[0].serialCode
																																if (Number(serialCode) === Number(1)) {
																																	//开启了流水码功能
																																	d++;
																																} else {
																																	await axios({
																																		method: "post",
																																		url: "attribute/selectAttributeContentIfExist",
																																		data: list[k]
																																	}).then(function (resp) {
																																		if (resp.data === true) {
																																			d++;
																																		}
																																	})
																																}
																															})


																														}
																														if (Number(d) === Number(list.length)) {
																															e = false;
																															break;
																														}
																													}


																												}
																											})

																											if (e === false) {
																												let productId = list[0].productId;
																												await axios({
																													method: "post",
																													url: "product/selectById",
																													data: productId
																												}).then(function (resp) {
																													let datas = resp.data;
																													if (datas[0].deleteSign === 1) {
																														//与弃用物料信息相同，提示
																														alert("检测到要修改的物料已被弃用，如要修改请联系相关人员！")

																													} else {

																														alert("该物料已存在，请核对后再操作！")
																													}
																												})
																											} else if (e === true) {


																												await lsNumber3(0);

																											}


																										} else if (resp.data === false) {
//自动生成一组数据所两两组合的所有结果


																											await lsNumber3(0);
																										}
																									})
																								}


																								if (l === true) {
																									//值作为编码的判断
																									axios({
																										method: "post",
																										url: "mapping/selectMapping",
																										data: sortId
																									}).then(async function (resp) {
																										// let mappingDatas=resp.data;
																										let datasOne = [];
																										let datasThree = [];


																										for (let j = 0; j < list.length; j++) {


																											datasOne.push(list[j])


																										}
																										//得到datas,查看是否有开启值作为编码的功能
																										let datasTwo = [];
																										if (datasOne.length > 0) {
																											for (let j = 0; j < datasOne.length; j++) {
																												await axios({
																													method: "post",
																													url: "attributeFunction/selectByAttNameId",
																													data: datasOne[j].parentId
																												}).then(function (resp) {
																													if (resp.data[0].autoCode === 1 || resp.data[0].autoCode === 2) {
																														datasOne[j].autoCode = resp.data[0].autoCode;
																														datasTwo.push(datasOne[j])
																													}
																												})
																											}

																											if (datasTwo.length > 0) {
																												//判断格式是否有不符和作为编码的

																												var pattern = /^[0-9A-Za-z]+$/;
																												let gs = true;
																												for (let j = 0; j < datasTwo.length; j++) {
																													//判断编码是否存在
																													if (datasTwo[j].content.length > 0) {
																														let ifformdata = {
																															attNameId: datasTwo[j].parentId,
																															attValue: datasTwo[j].content
																														}
																														await axios({
																															method: "post",
																															url: "attributeValue/selectCode",
																															data: ifformdata
																														}).then(function (resp) {
																															let ifdatas = resp.data;
																															if (ifdatas.length > 0) {
																																//存在编码，使用的是已存在的，不判断
																															} else {
																																if (datasTwo[j].autoCode === 1) {
																																	if (pattern.test(datasTwo[j].content) === false && datasTwo[j].content.length > 0) {
																																		alert(datasTwo[j].content + "的格式不适合作为编码的标准，请核对后再试！");
																																		gs = false
																																	}
																																}

																															}
																														})
																													}
																													if (gs === false) {
																														break;
																													}
																												}
																												if (gs === true) {
																													//都符合作为编码的标准，然后去查询是否有长度
																													let le = true;

																													for (let j = 0; j < datasTwo.length; j++) {
																														let ifformdata = {
																															attNameId: datasTwo[j].parentId,
																															attValue: datasTwo[j].content
																														}
																														await axios({
																															method: "post",
																															url: "attributeValue/selectCode",
																															data: ifformdata
																														}).then(async function (resp) {
																															let ifdatas = resp.data;
																															if (ifdatas.length > 0) {
																																//存在编码，使用的是已存在的，不判断
																															} else {
																																await axios({
																																	method: "post",
																																	url: "attribute/selectById",
																																	data: datasTwo[j].parentId
																																}).then(async function (resp) {
																																	let lengthTwo = resp.data[0].length
																																	if (lengthTwo === 0) {
																																		alert("值为" + datasTwo[j].content + "的属性还未设置编码长度，请设置后再进行操作！");
																																		le = false;
																																	} else {
																																		//判断长度是否符合
																																		if (datasTwo[j].content.length > 0) {
																																			if ((resp.data[0].length < datasTwo[j].content.length) && datasTwo[j].autoCode === 1) {
																																				alert(datasTwo[j].content + "的长度大于编码长度，请核对后在进行操作！");
																																				le = false;
																																			} else {
																																				//长度合适，查询这个属性值是否已经有编码了(是否存在)
																																				let formdata = {
																																					attNameId: datasTwo[j].parentId,
																																					attValue: datasTwo[j].content
																																				}
																																				await axios({
																																					method: "post",
																																					url: "attributeValue/selectValueExistAdd",
																																					data: formdata
																																				}).then(function (resp) {
																																					if (resp.data === false) {
																																						//值不重复,将值作为编码
																																						let codeData
																																						codeData = {
																																							attNameId: datasTwo[j].parentId,
																																							attValue: datasTwo[j].content,
																																							code: document.getElementById("upCode" + datasTwo[j].id + "").value,
																																						}


																																						datasThree.push(codeData)


																																					}
																																				})
																																			}
																																		}


																																	}
																																})
																															}
																														})


																														if (le === false) {
																															break;
																														}
																													}

																													if (le === true) {
																														//得到了最后的编码合集，进行添加
																														ifyiy(datasThree);
																													}

																												}

																											} else {
																												//有不在映射中的，但是没有开启值作为编码的功能，不需要添加编码
																												ifyiy(datasThree);

																											}
																										} else {
																											//全在映射中，直接更新内容即可，不需要添加编码

																											ifyiy(datasThree);
																										}

																									})
																								}
																								else {
																									alert("数据未填写完整！")
																								}


																							})
																						}


																					} else {
																						alert("数据未填写完整！")
																					}
																				} else {
																					alert("您暂未获得修改权限！")
																				}
																			})


																		}


																	}


																}

																lastAttribute(mdata.length - 1, mdata.length)
															} else {

																//点击修改数据，表示没有后添加的，直接更新基础的
																document.getElementById("updateSubmit").onclick = function () {
																	let booleanPromise = updateQx();
																	booleanPromise.then(function (resp) {
																		if (resp === true) {

																			let b = updateIfBaseExist();
																			if (b === true) {
																				//获取数据
																				let formdata = {
																					id: datas[0].id,
																					parentId: sortId,
																					name: "",
																					url: "",
																					materialNumber: "",
																					brand: "",
																					description: "",
																					number: "",
																					unit: "",
																					priceUnit: ""
																				}

																				formdata.name = document.getElementById("updateName").value;
																				let ylPc = document.querySelector(".ylPic");
																				if (ylPc) {
																					formdata.url = ylPc.querySelector("img").src;
																				}
																				formdata.materialNumber = document.getElementById("UpdateMaterialNumber").value;
																				formdata.brand = document.getElementById("updateBrand").value;
																				formdata.description = document.getElementById("updateDescription").value;
																				formdata.unit = document.getElementById("updateUnit").value;
																				// formdata.number = document.getElementById("updateNumber").value;
																				//获取筛选框中的内容
																				let priceUnitSelect = document.getElementById("updatePriceUnit");
																				let priceUnitIndex = priceUnitSelect.selectedIndex;//获取select的索引
																				//获取选中的值
																				formdata.priceUnit = priceUnitSelect.options[priceUnitIndex].value;
																				axios({
																					method: "post",
																					url: "product/selectNameIfExist",
																					data: formdata
																				}).then(function (resp) {
																					if (resp.data === true) {
																						alert("该物料已存在，请核对后再操作！")
																					} else {
																						//更新
																						axios({
																							method: "post",
																							url: "product/updateById",
																							data: formdata,
																						}).then(function (resp) {
																							if (resp.data === "success") {

																								alert("修改成功！");
																								//重置信息进行显示修改后的信息
																								select(foreverNumber, n);

																								//关闭窗口
																								let wd = document.querySelector(".window");
																								wd.style.display = "none";
																								//输入框设置为空
																								let inputs = document.querySelectorAll("#updateBlock input");
																								for (let i = 0; i < inputs.length; i++) {
																									inputs[i].value = "";
																								}

																								document.getElementById("updateDescription").value = "";


																							} else {
																								alert("修改失败，请联系相关人员！")
																							}
																						})
																					}
																				})


																			} else {
																				alert("数据未填写完整！")
																			}
																		} else {
																			alert("您暂未获得修改权限！")
																		}
																	})


																}

															}


														}


													}

													selectAttributeContent(0, sortAttribute.length)

												} else {
													//该产品的下面分类信息没有属性信息，就直接添加更新功能
													document.getElementById("updateSubmit").onclick = function () {
														let booleanPromise = updateQx();
														booleanPromise.then(function (resp) {
															if (resp === true) {
																let b = updateIfBaseExist();
																if (b === true) {
																	//获取数据
																	let formdata = {
																		id: datas[0].id,
																		parentId: sortId,
																		name: "",
																		url: "",
																		materialNumber: "",
																		brand: "",
																		description: "",
																		number: "",
																		unit: "",
																		priceUnit: ""
																	}

																	formdata.name = document.getElementById("updateName").value;
																	let ylPc = document.querySelector(".ylPic");
																	if (ylPc) {
																		formdata.url = ylPc.querySelector("img").src;
																	}
																	formdata.materialNumber = document.getElementById("UpdateMaterialNumber").value;
																	formdata.brand = document.getElementById("updateBrand").value;
																	formdata.description = document.getElementById("updateDescription").value;
																	formdata.unit = document.getElementById("updateUnit").value;
																	// formdata.number = document.getElementById("updateNumber").value;
																	//获取筛选框中的内容
																	let priceUnitSelect = document.getElementById("updatePriceUnit");
																	let priceUnitIndex = priceUnitSelect.selectedIndex;//获取select的索引
																	//获取选中的值
																	formdata.priceUnit = priceUnitSelect.options[priceUnitIndex].value;

																	axios({
																		method: "post",
																		url: "product/selectNameIfExist",
																		data: formdata
																	}).then(function (resp) {
																		if (resp.data === true) {
																			alert("该物料已存在，请核对后再操作！")
																		} else {
																			//更新
																			axios({
																				method: "post",
																				url: "product/updateById",
																				data: formdata,
																			}).then(function (resp) {
																				if (resp.data === "success") {

																					alert("修改成功！");
																					//重置信息进行显示修改后的信息
																					select(foreverNumber, n);

																					//关闭窗口
																					let wd = document.querySelector(".window");
																					wd.style.display = "none";
																					//输入框设置为空
																					let inputs = document.querySelectorAll("#updateBlock input");
																					for (let i = 0; i < inputs.length; i++) {
																						inputs[i].value = "";
																					}

																					document.getElementById("updateDescription").value = "";


																				} else {
																					alert("修改失败，请联系相关人员！")
																				}
																			})
																		}
																	})

																} else {
																	alert("数据未填写完整！")
																}
															} else {
																alert("您暂未获得修改权限！")
															}
														})


													}
												}
											})


										}
									}

									selectSortAttribute(datass.length - 1, datass.length)


								}

							}


							//查询层数
							axios({
								method: "post",
								url: "sort/selectOtherLevel",
								data: number
							}).then(function (resp) {
								let datas = resp.data;

								if (datas.length !== 0) {
									//如果能查到证明不是最后一层分类
									let level = datas[0].level
									m = level - 1;
									belong2(number, m);
								} else {
									//查不到证明是最后一层分类直接当做id去查自己是几级分类
									axios({
										method: "post",
										url: "sort/selectOfSort",
										data: number
									}).then(function (resp) {
										let datas = resp.data;

										m = datas[0].level;
										belong2(number, m);
									})
								}
							})

						})


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
								if (resp[j].function_two_id===3 && resp[j].open_status===1){
									promiseBoolean=true;
									break;
								}
							}

							if (promiseBoolean===true){
								//检测是否存在库存
								axios({
									method:"post",
									url:"product/selectById",
									data:data[i].id
								}).then(function (resp){
									let kc = resp.data;
									let kcNumber = kc[0].number;
									if (Number(kcNumber)>0 || kcNumber===undefined){
										alert("该物料存在库存，禁止删除！")
									}
									else {
										if (window.confirm("您真的要删除这个物料吗？")){
											axios({
												method:"post",
												url:"product/deleteById",
												data:data[i].id
											}).then(function (resp){
												if (resp.data==="success"){
													alert("删除成功！");
													//重置信息进行显示修改后的信息
													select(number,n);
												}else {
													alert("删除失败，请联系相关人员！");
												}
											})
											return true;
										}
										else {
											return false;
										}
									}
								})

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
				add(number,n);
				// addAttribute(number,n);
				// updateAttribute(number,n);
				//点击添加物料显示窗口

				document.getElementById("add").onclick=function (){
//删除所有的图片信息
					let res = document.querySelectorAll(".responsive");
					for ( let j = 0; j < res.length; j++) {
						res[j].remove();
					}
					let promise = userFunction(userId);
					let promiseBoolean = false;
					promise.then(function (resp){
						for (let i = 0; i < resp.length; i++) {
							if (resp[i].function_two_id===1 && resp[i].open_status===1){
								promiseBoolean=true;
								break;

							}

						}
						if (promiseBoolean===true){
							for (let i = 1; i < 9; i++) {
								document.getElementById("mdiv"+i+"").style.color="black"
							}
							//先判断当前分类下是否有映射
							axios({
								method:"post",
								url:"mapping/selectBySortId",
								data:number
							}).then(function (resp){
								let mappingDatas=resp.data;
								//判断是否有映射存在
								if (mappingDatas.length>0){

									let nm=true;
									let fw=true;
									let idd=0;
									axios({
										method:"post",
										url:"attributeFunction/selectAllAttributeFunction",
										data:number
									}).then(async function (resp){
										let autoDatas=resp.data;//得到了当前分类的映射下的所有在一映射中的属性信息
										for (let i = 0; i < autoDatas.length; i++) {
											if (autoDatas[i].autoCode===0 && autoDatas[i].range===0  && autoDatas[i].serialCode ===0){
												//在映射中，是值的，值作为编码功能未开启的，查询是否有属性值添加
												await 	axios({
													method:"post",
													url:"attributeValue/selectIfCode",
													data:autoDatas[i].attNameId
												}).then(function (resp){
													if (resp.data===false){
														//不存在编码，提示不能进行添加
														nm=false;
														idd=autoDatas[i].attNameId;
													}
												})
											}
											else if (autoDatas[i].autoCode===0 && autoDatas[i].range===1){
												//范围的
												await 	axios({
													method:"post",
													url:"attributeValue/selectIfCode",
													data:autoDatas[i].attNameId
												}).then(function (resp){
													if (resp.data===false){
														//不存在编码，提示不能进行添加
														fw=false;
														iddd=autoDatas[i].attNameId;
													}
												})
											}
											if (nm===false||fw===false){
												break;
											}
										}

										if (nm===true&&fw===true){
											//存在映射，检查是否为9
											let num=0;
											for (let i = 0; i < mappingDatas.length; i++) {

												num+=Number(mappingDatas[i].length);
											}

											if (Number(num)===9){
												document.querySelector(".window").style.display="block";
												document.getElementById("addBlock").style.display="block";
												document.getElementById("updateBlock").style.display="none";
												document.getElementById("addAttribute").style.display="none";
												document.getElementById("updateAttribute").style.display="none";
												document.getElementById("updateAttribute2").style.display="none";
												selectAttributeAdd(number,n);
												document.getElementById("materialNumber").value="";
											}else {
												//查询当前分类下是否有物料存在
												axios({
													method:"post",
													url:"product/selectByParentId",
													data:number
												}).then(function (resp){
													if (resp.data===true){
														//有物料
														document.querySelector(".window").style.display="block";
														document.getElementById("addBlock").style.display="block";
														document.getElementById("updateBlock").style.display="none";
														document.getElementById("addAttribute").style.display="none";
														document.getElementById("updateAttribute").style.display="none";
														document.getElementById("updateAttribute2").style.display="none";
														selectAttributeAdd(number,n);
														document.getElementById("materialNumber").value="";
													}else if (resp.data===false) {
														//没有物料

														axios({
															method:"post",
															url:"user/selectName"
														}).then(function (resp){
															let user = resp.data;
															axios({
																method:"post",
																url:"userFunction/selectFunction2",
																data:user.id
															}).then(function (resp){
																if (resp.data===true){
																	if (window.confirm("检测到映射表中占位不全，是否进行修改？")){
																		axios({
																			method:"post",
																			url:"sort/selectSortById",
																			data:number
																		}).then(function (resp){
																			let datas=resp.data;
																			window.open("attributeAdd.html?parentId="+datas[0].id+"&content="+datas[0].name+"&level="+datas[0].level+"&userId="+userId+"")

																		})
																		return true;
																	}else {

																		document.querySelector(".window").style.display="block";
																		document.getElementById("addBlock").style.display="block";
																		document.getElementById("updateBlock").style.display="none";
																		document.getElementById("addAttribute").style.display="none";
																		document.getElementById("updateAttribute").style.display="none";
																		document.getElementById("updateAttribute2").style.display="none";
																		selectAttributeAdd(number,n);
																		document.getElementById("materialNumber").value="";
																		return false;
																	}
																}else {
																	document.querySelector(".window").style.display="block";
																	document.getElementById("addBlock").style.display="block";
																	document.getElementById("updateBlock").style.display="none";
																	document.getElementById("addAttribute").style.display="none";
																	document.getElementById("updateAttribute").style.display="none";
																	document.getElementById("updateAttribute2").style.display="none";
																	selectAttributeAdd(number,n);
																	document.getElementById("materialNumber").value="";
																}
															})
														})

													}
												})


											}
										}
										else {
											
											if (nm===false){
												//根据idd查询属性名
												axios({
													method:"post",
													url:"attribute/selectById",
													data:idd
												}).then(function (resp){
													let datas=resp.data;

													alert("当前映射中,"+datas[0].name+"还未添加编码以及未开启值作为编码功能的属性，请先添加编码或者打开值作为编码的功能再进行操作！")
												})
											}
											else if (fw===false){
												//根据idd查询属性名
												axios({
													method:"post",
													url:"attribute/selectById",
													data:iddd
												}).then(function (resp){
													let datas=resp.data;

													alert("当前映射中,"+datas[0].name+"还未添加任何属性值以及编码，请添加后再试！")
												})
											}


										}
									})




								}
								else {
									alert("不存在映射，请先添加映射在进行物料添加操作!");
									//查询分类的数据
									axios({
										method:"post",
										url:"sort/selectSortById",
										data:number
									}).then(function (resp){
										let datas=resp.data;
										window.open("attributeAdd.html?parentId="+datas[0].id+"&content="+datas[0].name+"&level="+datas[0].level+"&userId="+userId+"")

									})



								}
							})
						}
						else {
							alert("您暂未获得添加物料的权限！")
						}
					})



				}



			}
		})

		//搜索功能
		document.getElementById("search").onclick=function (){
			//获取搜索框中的数据
			let value = document.getElementById("searchContent").value;
			if (value.length>0){
				function  searchRe(){

					axios({
						method:"post",
						url:"product/searchProduct?sortId=0&all=lingjian",
						data:value
					}).then(function (resp){
						let data=resp.data;
						let formdata="";
						if (data.length>0){
							for (let i = 0; i < data.length; i++) {
								formdata+=' <div style="width: 100%;height: auto;display: flex;text-align: center;">\n' +
									'                                <div style="width: 34.5%;margin: auto; border: white solid 1px"><b>'+data[i].name+'</b></div>\n' +
									'                                <div style="width: 40%;margin: auto  ; border: white solid 1px"><b>'+data[i].materialNumber+'</b></div>\n' +
									'                                <div style="width: 25%;margin: auto;   border: white solid 1px"><a class="updateProduct" href="javascript:void(0)">修改</a>&nbsp;<a class="deleteProduct"  href="javascript:void(0)">删除</a></div>\n' +
									'                            </div>'
							}

						}
						else {
							formdata='<div style="width: 100%;height: auto;color: red;text-align: center">暂未查询到相关内容！</div>'
						}
						document.getElementById("content").innerHTML=formdata;
						document.getElementById("pageDiv1").style.display="none"
						document.getElementById("add").onclick=function (){
							//删除所有的图片信息
							let res = document.querySelectorAll(".responsive");
							for ( let j = 0; j < res.length; j++) {
								res[j].remove();
							}
							let promise = userFunction(userId);
							let promiseBoolean=false;
							promise.then(function (resp){
								for (let i = 0; i < resp.length; i++) {
									if (resp[i].function_two_id===1 && resp[i].open_status===1){
										promiseBoolean=true;

									}

								}
								if (promiseBoolean===true){
									for (let i = 1; i < 9; i++) {
										document.getElementById("mdiv"+i+"").style.color="black"
									}
									//先判断当前分类下是否有映射
									axios({
										method:"post",
										url:"mapping/selectBySortId",
										data:number
									}).then(async function (resp){
										let mappingDatas=resp.data;
										//判断是否有映射存在
										if (mappingDatas.length>0){


											let nm=true;
											let fw=true;
											let idd=0;
											let iddd=0;
											axios({
												method:"post",
												url:"attributeFunction/selectAllAttributeFunction",
												data:number
											}).then(async function (resp){
												let autoDatas=resp.data;//得到了当前分类的映射下的所有在一映射中的属性信息
												for (let i = 0; i < autoDatas.length; i++) {
													if (autoDatas[i].autoCode===0 && autoDatas[i].range===0  && autoDatas[i].serialCode ===0){
														//在映射中，是值的，值作为编码功能未开启的，查询是否有属性值添加
														await 	axios({
															method:"post",
															url:"attributeValue/selectIfCode",
															data:autoDatas[i].attNameId
														}).then(function (resp){
															if (resp.data===false){
																//不存在编码，提示不能进行添加
																nm=false;
																idd=autoDatas[i].attNameId;
															}
														})
													}
													else if (autoDatas[i].autoCode===0 && autoDatas[i].range===1){
														//范围的
														await 	axios({
															method:"post",
															url:"attributeValue/selectIfCode",
															data:autoDatas[i].attNameId
														}).then(function (resp){
															if (resp.data===false){
																//不存在编码，提示不能进行添加
																fw=false;
																iddd=autoDatas[i].attNameId;
															}
														})

													}
													if (nm===false||fw===false){
														break;
													}
												}

												if (nm===true&&fw===true){
													//存在映射，检查是否为9
													let num=0;
													for (let i = 0; i < mappingDatas.length; i++) {

														num+=Number(mappingDatas[i].length);
													}

													if (Number(num)===9){

														document.querySelector(".window").style.display="block";
														document.getElementById("addBlock").style.display="block";
														document.getElementById("updateBlock").style.display="none";
														document.getElementById("addAttribute").style.display="none";
														document.getElementById("updateAttribute").style.display="none";
														document.getElementById("updateAttribute2").style.display="none";
														selectAttributeAdd(number,n);
														document.getElementById("materialNumber").value="";

													}
													else {

														//查询当前分类下是否有物料存在
														axios({
															method:"post",
															url:"product/selectByParentId",
															data:number
														}).then(function (resp){
															if (resp.data===true){
																//有物料
																document.querySelector(".window").style.display="block";
																document.getElementById("addBlock").style.display="block";
																document.getElementById("updateBlock").style.display="none";
																document.getElementById("addAttribute").style.display="none";
																document.getElementById("updateAttribute").style.display="none";
																document.getElementById("updateAttribute2").style.display="none";
																selectAttributeAdd(number,n);
																document.getElementById("materialNumber").value="";
															}else if (resp.data===false){
																//检测用户权限

																axios({
																	method:"post",
																	url:"user/selectName"
																}).then(function (resp){
																	let user = resp.data;
																	axios({
																		method:"post",
																		url:"userFunction/selectFunction2",
																		data:user.id
																	}).then(function (resp){
																		if (resp.data===true){
																			if (window.confirm("检测到映射表中占位不全，是否进行修改？")){



																				axios({
																					method:"post",
																					url:"sort/selectSortById",
																					data:number
																				}).then(function (resp){
																					let datas=resp.data;
																					window.open("attributeAdd.html?parentId="+datas[0].id+"&content="+datas[0].name+"&level="+datas[0].level+"&&userId="+userId+"")

																				})
																				return true;
																			}else {

																				document.querySelector(".window").style.display="block";
																				document.getElementById("addBlock").style.display="block";
																				document.getElementById("updateBlock").style.display="none";
																				document.getElementById("addAttribute").style.display="none";
																				document.getElementById("updateAttribute").style.display="none";
																				document.getElementById("updateAttribute2").style.display="none";
																				selectAttributeAdd(number,n);
																				document.getElementById("materialNumber").value="";
																				return false;
																			}
																		}else {
																			document.querySelector(".window").style.display="block";
																			document.getElementById("addBlock").style.display="block";
																			document.getElementById("updateBlock").style.display="none";
																			document.getElementById("addAttribute").style.display="none";
																			document.getElementById("updateAttribute").style.display="none";
																			document.getElementById("updateAttribute2").style.display="none";
																			selectAttributeAdd(number,n);
																			document.getElementById("materialNumber").value="";
																		}
																	})
																})


															}
														})


													}
												}
												else {
													
													if (nm===false){
														//根据idd查询属性名
														axios({
															method:"post",
															url:"attribute/selectById",
															data:idd
														}).then(function (resp){
															let datas=resp.data;
															alert("当前映射中,"+datas[0].name+"还未添加编码以及未开启值作为编码功能的属性，请先添加编码或者打开值作为编码的功能再进行操作！")

														})
													}
													else if (fw===false){
														//根据idd查询属性名
														axios({
															method:"post",
															url:"attribute/selectById",
															data:iddd
														}).then(function (resp){
															let datas=resp.data;
															alert("当前映射中,"+datas[0].name+"还未添加任何属性值以及编码，请添加后再试！")

														})
													}

												}

											})




										}
										else {
											alert("不存在映射，请先添加映射在进行物料添加操作!");
											//查询分类的数据
											axios({
												method:"post",
												url:"sort/selectSortById",
												data:number
											}).then(function (resp){
												let datas=resp.data;
												window.open("attributeAdd.html?parentId="+datas[0].id+"&content="+datas[0].name+"&level="+datas[0].level+"&userId="+userId+"")

											})



										}
									})
								}
								else {
									alert("您暂未获得添加物料的权限！")
								}
							})




						}


						//点击修改物料显示窗口
						let updateP = document.querySelectorAll(".updateProduct");
						for (let i = 0; i < updateP.length; i++) 	{
							updateP[i].onclick=function (){

								for (let j = 1; j <= 9; j++) {
									document.getElementById("upmdiv"+j+"").style.color="black";
								}

								black();
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
									url:"product/selectById",
									data:data[i].id
								}).then(function (resp)
								{
									let datas = resp.data;
									let sortId=datas[0].parentId;
									//设置数量框的禁用

									//自带属性
									document.getElementById("UpdateMaterialNumber").disabled=true;
									document.getElementById("updateName").value = datas[0].name;
									images(datas,1);
									document.getElementById("UpdateMaterialNumber").value = datas[0].materialNumber;
									document.getElementById("updateBrand").value = datas[0].brand;
									document.getElementById("updateDescription").value = datas[0].description;
									document.getElementById("updateNumber").value=datas[0].number;
									document.getElementById("updateUnit").value=datas[0].unit;


									document.getElementById("produceId").innerHTML=datas[0].materialNumber.substring(0,7);
									document.getElementById("nineNumber").innerHTML=datas[0].materialNumber.substring(7,16);
									let s = datas[0].materialNumber.substring(7,16);
									for (let j = 0; j < s.length; j++) {
										let m = Number(j)+Number(1);
										document.getElementById("upmdiv"+m+"").innerHTML=s[j];
									}




									//这是查询最后一层的属性，现在要做的是查询出来该层上级所有的层数

									var datass=[];
									let m;
									function compare (str,value){

										//  console.log("范围"+ str)
										// console.log("传入的值"+value)
										if (value.length>0){
											var pattern = /^\[.*\]$/;
											var patterns= /^\<.*\]$/;
											var pattern1= /^\<.*\>$/;
											var patterns1= /^\[.*\>$/;

											if (pattern.test(str)===true){
												str=str.split("[").join("");
												str=str.split("]").join("");
												// str=str.replace(/[a-zA-Z]/g, '');
												let strings = str.split("~");

												//判断输入的数值是否符合这个范围
												if (Number(power(value))>=Number(power(strings[0])) && Number(power(value))<=Number(power(strings[1]))){
													return true;
												}else {
													return false;
												}

											}
											else if (patterns.test(str)===true){

												str=str.split("<").join("");
												str=str.split("]").join("");
												// str=str.replace(/[a-zA-Z]/g, '');
												let strings = str.split("~");

												// console.log("开始"+ strings[0]);
												// console.log("结束"+strings[1])

												//判断输入的数值是否符合这个范围
												if (Number(power(value))>Number(power(strings[0])) && Number(power(value))<=Number(power(strings[1]))){
													return true;
												}else {
													return false;
												}
											}
											else if (pattern1.test(str)===true){

												str=str.split("<").join("");
												str=str.split(">").join("");
												// str=str.replace(/[a-zA-Z]/g, '');
												let strings = str.split("~");

												// console.log("开始"+ strings[0]);
												// console.log("结束"+strings[1])

												//判断输入的数值是否符合这个范围
												if (Number(power(value))>Number(power(strings[0])) && Number(power(value))<Number(power(strings[1]))){
													return true;
												}else {
													return false;
												}
											}
											else if (patterns1.test(str)===true){

												str=str.split("[").join("");
												str=str.split(">").join("");
												// str=str.replace(/[a-zA-Z]/g, '');
												let strings = str.split("~");

												// console.log("开始"+ strings[0]);
												// console.log("结束"+strings[1])

												//判断输入的数值是否符合这个范围
												if (Number(power(value))>=Number(power(strings[0])) && Number(power(value))<Number(power(strings[1]))){
													return true;
												}else {
													return false;
												}
											}

											else if (pattern.test(str)===false && patterns.test(str)===false && pattern1.test(str)===false && patterns1.test(str)===false) {
												alert("数据有问题，不符合格式，请联系相关人员！");

											}
										}else {
											return false;
										}


									}
									async function belong2(number,m){

										if (m>0){
											//获取当前分类属于哪些大类里
											axios({
												method:"post",
												url:"sort/selectOfSort",
												data:number
											}).then(function (resp){
												var datas = resp.data;
												datass=datas.concat(datass);
												m--;
												belong2(datas[0].parentId,m)
											})

										}else if (m===0) {
											//得到了自己上面的所有分类内容，然后根据分类id去查询该分类下的属性内容

											let selectAttributeUpdate="";
											var mdata=[];
											var sortAttribute=[];

											function selectSortAttribute(sd1,sd2){
												if (sd2>0){
													axios({
														method:"post",
														url:"attribute/selectAttributeNameByParentId",
														data:datass[sd1].id
													}).then(function (resp){
														let adatas=resp.data;
														sortAttribute=adatas.concat(sortAttribute);
														sd1--;
														if (sd1>=0){
															sd2--;
															selectSortAttribute(sd1,sd2);
														}else {
															sd2--;
															selectSortAttribute(0,sd2);
														}
													})
												}else if (sd2===0){

													//查询公共属性
													axios({
														method:"post",
														url:"attribute/selectPublic"
													}).then(function (resp){
														let ggdatas=resp.data;
														sortAttribute=ggdatas.concat(sortAttribute);
														//得到了所有的分类属性信息
														if (sortAttribute.length>0){

															//如果该产品的上面分类有属性信息
															//获取到了分类的属性信息，然后用分类属性名的id作为分类内容的parentid以及物料的id去查询物料所有的属性内容

															function selectAttributeContent(ac1,ac2){
																if (ac2>0){
																	axios({
																		method:"post",
																		url:"attribute/selectBeforeContentUpdate?id="+datas[0].id+"",
																		data:sortAttribute[ac1].id,
																	}).then(async function (resp){
																		let bdatas=resp.data;
																		if (bdatas.length > 0) {
																			mdata=bdatas.concat(mdata)
																			for (let k = 0; k < bdatas.length; k++) {
																				if (bdatas[k].unit.length>0){
																					selectAttributeUpdate += '    <div style="width:738px ;height: auto;display: flex;">\n' +
																						'                    <div  style="width: 100px;height: auto; border-right: 1px solid #d6e0ef;border-bottom: 1px solid #d6e0ef;text-align: center;display: flex">\n' +
																						'                        <div style="width: fit-content;height: auto;align-self: center;margin: auto">' + bdatas[k].name + '  <br>(单位:'+bdatas[k].unit+')</div>\n' +
																						'                    </div>\n' +
																						'                    <div class="qwe" style="width:294px ;height: auto;border-right: 1px solid #d6e0ef;border-bottom: 1px solid #d6e0ef;">\n' +
																						'                        &nbsp;<input autocomplete="off" maxlength="80" style="width: 90%;height: 20px;" type="text" name="" id=upInput' + bdatas[k].id + ' value=""> &nbsp;<b id="bb'+bdatas[k].id+'" style="display: none">*</b>\n' +
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
																						'                        &nbsp;<input autocomplete="off" maxlength="80" style="width: 90%;height: 20px;" type="text" name="" id=upInput' + bdatas[k].id + ' value=""> &nbsp;<b id="bb'+bdatas[k].id+'" style="display: none">*</b>\n' +
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

																			ac1++;
																			ac2--;
																			selectAttributeContent(ac1,ac2);

																		}else {
																			ac1++;

																			ac2--;
																			selectAttributeContent(ac1,ac2);

																		}

																	})
																}
																else if (ac2===0){

																	document.getElementById("selectAttributeUpdate").innerHTML=selectAttributeUpdate;

																	//得到了该产品的所有后添加的分类属性

																	if (mdata.length>0){

																		function lastAttribute(la1,la2){
																			if (la2>0){
																				if (mdata[la1].content!==undefined){
																					document.getElementById("upInput"+mdata[la1].id+"").value=mdata[la1].content;
																				}else {
																					document.getElementById("upInput"+mdata[la1].id+"").value="";
																				}

																				if (mdata[la1].content!==undefined){
																					if (mdata[la1].content.length>0){


																						//根据属性id和分类id去查询编码
																						let formdata={
																							attNameId:mdata[la1].parentId,
																							attValue:mdata[la1].content
																						}
																						//查询是否是范围
																						axios({
																							method:"post",
																							url:"attributeFunction/selectByAttNameId",
																							data:formdata.attNameId
																						}).then(function (resp){
																							let functionDatas=resp.data;
																							if (functionDatas[0].range===1){
																								//如果是范围，获取所有的范围
																								axios({
																									method:"post",
																									url:"attributeValue/selectByAttNameId",
																									data:formdata.attNameId
																								}).then(function (resp){
																									let datas=resp.data;
																									let m=0;
																									for (let j = 0; j < datas.length; j++) {
																										let b = compare(datas[j].attValue,formdata.attValue);

																										if (b){
																											m=j;
																											break
																										}
																									}
																									let rangeDatas={
																										attNameId:mdata[la1].parentId,
																										attValue:datas[m].attValue
																									}
																									axios({
																										method:"post",
																										url:"attributeValue/selectCode",
																										data:rangeDatas
																									}).then(function (resp){
																										if (resp.data.length>0){
																											document.getElementById("upCode"+mdata[la1].id+"").value=resp.data[0].code;
																										}

																										la1--;
																										if (la1>=0){
																											la2--;
																											lastAttribute(la1,la2);
																										}else {
																											la2--;
																											lastAttribute(0,la2);
																										}
																									})
																								})

																							}else if (functionDatas[0].range===0){
																								axios({
																									method:"post",
																									url:"attributeValue/selectCode",
																									data:formdata
																								}).then(function (resp){
																									if (resp.data.length>0){
																										document.getElementById("upCode"+mdata[la1].id+"").value=resp.data[0].code;
																									}

																									la1--;
																									if (la1>=0){
																										la2--;
																										lastAttribute(la1,la2);
																									}else {
																										la2--;
																										lastAttribute(0,la2);
																									}
																								})
																							}
																						})

																					}

																					else {
																						la1--;
																						if (la1>=0){
																							la2--;
																							lastAttribute(la1,la2);
																						}else {
																							la2--;
																							lastAttribute(0,la2);
																						}
																					}
																				}else {
																					la1--;
																					if (la1>=0){
																						la2--;
																						lastAttribute(la1,la2);
																					}else {
																						la2--;
																						lastAttribute(0,la2);
																					}
																				}


																			}else if (la2===0) {
																				axios({
																					method:"post",
																					url:"mapping/selectMapping",
																					data:sortId
																				}).then(function (resp){
																					let datas=resp.data;
																					if (datas.length>0){
																						let formdatas="";
																						for (let k = 0; k <datas.length ; k++) {
																							for (let j = 0; j < mdata.length; j++) {
																								if (Number(datas[k].att_name_id)===Number(mdata[j].parentId)){
																									for (let l = datas[k].begin_location; l <=datas[k].end_location ; l++) {
																										document.getElementById("upmdiv"+l+"").style.border="#1890ff solid 1px"
																									}


																								}
																							}

																						}

																						let value2 = document.getElementById("UpdateMaterialNumber").value;
																						document.getElementById("updateSortNumberA").innerHTML=value2.substring(0,7);
																						document.getElementById("updateSerialNumber").innerHTML=value2.substring(16,18);
																						for (let k = 0; k <datas.length ; k++) {
																							for (let j = 0; j < mdata.length; j++) {
																								if (Number(datas[k].att_name_id) === Number(mdata[j].parentId)) {
																									document.getElementById("upInput"+mdata[j].id+"").onfocus=function (){
																										for (let l = datas[k].begin_location; l <=datas[k].end_location ; l++) {
																											document.getElementById("upmdiv"+l+"").style.color="red"
																										}

																										for (let l = 0; l < datas.length; l++) {
																											if (Number(datas[l].att_name_id)!==Number(mdata[j].parentId)){
																												for (let o =datas[l].begin_location; o <= datas[l].end_location; o++) {
																													document.getElementById("upmdiv"+o+"").style.color="black"
																												}
																											}
																										}

																									}
																									//查询流水码的功能
																									//查询是否开启了流水码功能
																									axios({
																										method:"post",
																										url:"attributeFunction/selectByAttNameId",
																										data:datas[k].att_name_id
																									}).then(function (resp)
																									{
																										let ableCode=resp.data;
																										let serialCode = ableCode[0].serialCode;
																										if (serialCode===1){
																											//开启了流水码功能，限制输入和编码
																											document.getElementById("upInput"+mdata[j].id+"").disabled=true;
																											document.getElementById("upCode"+mdata[j].id+"").value=document.getElementById("upInput" + mdata[j].id + "").value;

																										}
																									})
																									let input = document.getElementById("upInput"+mdata[j].id+"");
																									//查询该属性下面是否有属性值
																									axios({
																										method:"post",
																										url:"attributeValue/selectIfCode",
																										data:mdata[j].parentId
																									}).then(function (resp){
																										let datas=resp.data;
																										if (datas===true){
																											input.placeholder="请输入查询！"
																										}
																									})


																									let num=mdata[j].parentId;
																									input.addEventListener('input', debounce(function () {
																										document.getElementById("upCode"+mdata[j].id+"").value="";
																										let value = input.value.trim();
																										if (value.length>0){
																											axios({
																												method:"post",
																												url:"attributeValue/selectByAttNameId",
																												data:mdata[j].parentId
																											}).then(function (resp)
																											{
																												if (resp.data.length>0){
																													//查询是否是范围
																													axios({
																														method:"post",
																														url:"attributeFunction/selectByAttNameId",
																														data:num
																													}).then(function (resp){
																														let rangeDatas=resp.data;
																														function autoAndNo(value,num,s){
																															//得到了输入框的值，先判断能不能找到这个值
																															let formdata={
																																attNameId:num,
																																attValue:value
																															}
																															//查询这个值是否存在
																															axios({
																																method:"post",
																																url:"attributeValue/selectValueExistAdd",
																																data:formdata
																															}).then(function (resp){
																																let attValueBoolean=resp.data;
																																if (attValueBoolean===true){
																																	//该值存在
																																	//查看该属性是否在映射中
																																	axios({
																																		method:"post",
																																		url:"mapping/selectMapping",
																																		data:sortId//分类id
																																	}).then(function (resp){
																																		let mappingData=resp.data;
																																		let map=false;
																																		let index=0;
																																		for (let j = 0; j < mappingData.length; j++) {
																																			if (Number(num)===mappingData[j].att_name_id){
																																				map=true;
																																				index=j;
																																				break;
																																			}

																																		}

																																		if (map===true){
																																			//在映射中，根据长度和起始位置改编物料编码
																																			//值已经存在，查询这个值的编码
																																			let value1 = document.getElementById("upInput"+s+"").value.trim();
																																			let data={
																																				attNameId:num,
																																				attValue:value1
																																			}
																																			axios({
																																				method:"post",
																																				url:"attributeValue/selectCode",
																																				data:data
																																			}).then(function (resp){
																																				document.getElementById("upCode"+s+"").value=resp.data[0].code;
																																				let nine = document.getElementById("nineNumber").innerHTML;
																																				let str = nine;
																																				//判断输入的长度和编码长度

																																				let replaceStr =resp.data[0].code;//要替换的字符串
																																				// document.getElementById("updiv"+s+"").innerHTML=replaceStr;
																																				let startIndex = mappingData[index].begin_location;


																																				let endIndex = mappingData[index].end_location;

																																				let newStr = str.slice(0, startIndex-1) + replaceStr + str.slice(endIndex);

																																				document.getElementById("nineNumber").innerHTML=newStr;
																																				//获取分类编码
																																				let sortId = document.getElementById("produceId").innerHTML;
																																				document.getElementById("UpdateMaterialNumber").value=sortId+newStr+lsn;
																																				for (let j = 0; j < newStr.length; j++) {
																																					let m=Number(j)+Number(1);
																																					document.getElementById("upmdiv"+m+"").innerHTML=newStr[j];
																																				}
																																			})



																																		}else {
																																			//不在映射中
																																			//值已经存在，查询这个值的编码
																																			let value1 = document.getElementById("upInput"+s+"").value.trim();
																																			let data={
																																				attNameId:num,
																																				attValue:value1
																																			}
																																			axios({
																																				method:"post",
																																				url:"attributeValue/selectCode",
																																				data:data
																																			}).then(function (resp){
																																				document.getElementById("upCode"+s+"").value=resp.data[0].code;
																																			})
																																		}

																																	})

																																}
																																else {
																																	//该值不存在,查询是否开启了值作为编码的功能
																																	axios({
																																		method:"post",
																																		url:"attributeFunction/selectIfAutoCode",
																																		data:num
																																	}).then(function (resp){
																																		if (resp.data===true){
																																			//开启了值作为编码的功能
																																			//先判断输入值的格式是否符合做编码的格式，然后判断长度是否符合编码的长度，然后再判断内容是否重复
																																			var pattern = /^[0-9A-Za-z]+$/;
																																			if (pattern.test(value)===true){
																																				//符合标准，判断长度
																																				axios({
																																					method:"post",
																																					url:"attribute/selectById",
																																					data:num
																																				}).then(function (resp){
																																					let datas=resp.data;
																																					if (Number(datas[0].length)>=Number(value.length)){
																																						//长度符合，判断作为编码是否重复
																																						let codeData={
																																							attNameId:"",
																																							attValue:"",
																																							code:""
																																						}
																																						if (Number(datas[0].length)===Number(value.length)){
																																							codeData={
																																								attNameId:num,
																																								attValue:value,
																																								code:value
																																							}
																																						}else {
																																							let jnum=Number(datas[0].length)-Number(value.length);
																																							let str="";
																																							for (let j = 0; j < jnum; j++) {
																																								str+="0"
																																							}
																																							codeData={
																																								attNameId:num,
																																								attValue:value,
																																								code:str+value
																																							}
																																						}

																																						//查看该属性是否在映射中
																																						axios({
																																							method:"post",
																																							url:"mapping/selectMapping",
																																							data:sortId //分类id
																																						}).then(function (resp){
																																							let mappingData=resp.data;
																																							let map=false;
																																							let index=0;
																																							for (let j = 0; j < mappingData.length; j++) {
																																								if (Number(num)===mappingData[j].att_name_id){
																																									map=true;
																																									index=j;
																																									break;
																																								}

																																							}

																																							if (map===true){
																																								//在映射中，根据长度和起始位置改编物料编码
																																								//查看这个值是否已经被使用了
																																								let data={
																																									attNameId:num,
																																									attValue:document.getElementById("upInput"+s+"").value.trim()

																																								}
																																								axios({
																																									method:"post",
																																									url:"attributeValue/selectValueExistAdd",
																																									data:data
																																								}).then(function (resp){
																																									if (resp.data===true){
																																										//值已经存在，查询这个值的编码
																																										let value1 = document.getElementById("upInput"+s+"").value.trim();
																																										let data={
																																											attNameId:num,
																																											attValue:value1
																																										}
																																										axios({
																																											method:"post",
																																											url:"attributeValue/selectCode",
																																											data:data
																																										}).then(function (resp){
																																											document.getElementById("upCode"+s+"").value=resp.data[0].code;
																																										})
																																									}else {
																																										//值不存在.查询该编码是否已经存在
																																										axios({
																																											method:"post",
																																											url:"attributeValue/selectCodeExist",
																																											data:codeData
																																										}).then(function (resp){
																																											if (resp.data===true){
																																												//编码存在，提示并清空输入框
																																												document.getElementById("upInput"+s+"").value="";
																																												document.getElementById("upCode"+s+"").value="";
																																												alert("该编码已存在，不能使用该值作为编码使用！")
																																											}
																																											else {
																																												//编码不存在
																																												//得到了属性值的编码
																																												let nine = document.getElementById("nineNumber").innerHTML;
																																												let str = nine;
																																												//判断输入的长度和编码长度

																																												let replaceStr =codeData.code;//要替换的字符串

																																												document.getElementById("upCode"+s+"").value=codeData.code;
																																												// document.getElementById("updiv"+s+"").innerHTML=replaceStr;
																																												let startIndex = mappingData[index].begin_location;


																																												let endIndex = mappingData[index].end_location;

																																												let newStr = str.slice(0, startIndex-1) + replaceStr + str.slice(endIndex);

																																												document.getElementById("nineNumber").innerHTML=newStr;
																																												//获取分类编码
																																												let sortId = document.getElementById("produceId").innerHTML;
																																												document.getElementById("UpdateMaterialNumber").value=sortId+newStr+lsn;
																																												for (let j = 0; j < newStr.length; j++) {
																																													let m=Number(j)+Number(1);
																																													document.getElementById("upmdiv"+m+"").innerHTML=newStr[j];
																																												}
																																											}
																																										})

																																									}
																																								})

																																							}else {
																																								//不在映射中
																																								//查看这个值是否已经被使用了
																																								let data={
																																									attNameId:num,
																																									attValue:document.getElementById("upInput"+s+"").value

																																								}
																																								axios({
																																									method:"post",
																																									url:"attributeValue/selectValueExistAdd",
																																									data:data
																																								}).then(function (resp){
																																									if (resp.data===true){
																																										//值已经存在，不在映射中，查询编码并显示
																																										axios({
																																											method:"post",
																																											url:"attributeValue/selectValueExistAdd",
																																											data:data
																																										}).then(function (resp){
																																											document.getElementById("upCode"+s+"").value=resp.data[0].code;
																																										})
																																									}else {
																																										//值不存在，将编码显示
																																										document.getElementById("upCode"+s+"").value=codeData.code;
																																									}
																																								})

																																							}

																																						})



																																					}else {
																																						let value1 = document.getElementById("upInput"+s+"").value.trim();
																																						if(value1.length>0){
																																							alert("输入值的长度与编码长度不符，请核对后在进行操作！");
																																						}
																																						document.getElementById("upInput"+s+"").value="";
																																						document.getElementById("upCode"+s+"").value="";


																																					}
																																				})
																																			}else {
																																				let value2 = document.getElementById("upInput"+s+"").value;
																																				if(value2.length>0){
																																					alert("输入值的格式不适合作为编码，请核对后再进行操作！");
																																				}
																																				document.getElementById("upInput"+s+"").value="";
																																				document.getElementById("upCode"+s+"").value="";
																																			}
																																		}
																																		else {
																																			//该值不存在,查询是否开启了值作为编码的功能
																																			axios({
																																				method:"post",
																																				url:"attributeFunction/selectIfAutoCode",
																																				data:num
																																			}).then(function (resp){
																																				if (resp.data===true){
																																					//开启了值作为编码的功能
																																					//先判断输入值的格式是否符合做编码的格式，然后判断长度是否符合编码的长度，然后再判断内容是否重复
																																					var pattern = /^[0-9A-Za-z]+$/;
																																					if (pattern.test(value)===true){
																																						//符合标准，判断长度
																																						axios({
																																							method:"post",
																																							url:"attribute/selectById",
																																							data:num
																																						}).then(function (resp){
																																							let datas=resp.data;
																																							if (Number(datas[0].length)>=Number(value.length)){
																																								//长度符合，判断作为编码是否重复
																																								let codeData={
																																									attNameId:"",
																																									attValue:"",
																																									code:""
																																								}
																																								if (Number(datas[0].length)===Number(value.length)){
																																									codeData={
																																										attNameId:num,
																																										attValue:value,
																																										code:value
																																									}
																																								}else {
																																									let jnum=Number(datas[0].length)-Number(value.length);
																																									let str="";
																																									for (let j = 0; j < jnum; j++) {
																																										str+="0"
																																									}
																																									codeData={
																																										attNameId:num,
																																										attValue:value,
																																										code:str+value
																																									}
																																								}

																																								//查看该属性是否在映射中
																																								axios({
																																									method:"post",
																																									url:"mapping/selectMapping",
																																									data:sortId //分类id
																																								}).then(function (resp){
																																									let mappingData=resp.data;
																																									let map=false;
																																									let index=0;
																																									for (let j = 0; j < mappingData.length; j++) {
																																										if (Number(num)===mappingData[j].att_name_id){
																																											map=true;
																																											index=j;
																																											break;
																																										}

																																									}

																																									if (map===true){
																																										//在映射中，根据长度和起始位置改编物料编码
																																										//查看这个值是否已经被使用了
																																										let data={
																																											attNameId:num,
																																											attValue:document.getElementById("upInput"+s+"").value.trim()

																																										}
																																										axios({
																																											method:"post",
																																											url:"attributeValue/selectValueExistAdd",
																																											data:data
																																										}).then(function (resp){
																																											if (resp.data===true){
																																												//值已经存在，查询这个值的编码
																																												let value1 = document.getElementById("upInput"+s+"").value.trim();
																																												let data={
																																													attNameId:num,
																																													attValue:value1
																																												}
																																												axios({
																																													method:"post",
																																													url:"attributeValue/selectCode",
																																													data:data
																																												}).then(function (resp){
																																													document.getElementById("upCode"+s+"").value=resp.data[0].code;
																																												})
																																											}else {

																																												//值不存在.查询该编码是否已经存在
																																												axios({
																																													method:"post",
																																													url:"attributeValue/selectCodeExist",
																																													data:codeData
																																												}).then(function (resp){
																																													if (resp.data===true){
																																														//编码存在，提示并清空输入框
																																														document.getElementById("upInput"+s+"").value="";
																																														document.getElementById("upCode"+s+"").value="";
																																														alert("该编码已存在，不能使用该值作为编码使用！")
																																													}
																																													else {
																																														//编码不存在
																																														//得到了属性值的编码
																																														let nine = document.getElementById("nineNumber").innerHTML;
																																														let str = nine;
																																														//判断输入的长度和编码长度

																																														let replaceStr =codeData.code;//要替换的字符串
																																														document.getElementById("upCode"+s+"").value=codeData.code;
																																														let startIndex = mappingData[index].begin_location;


																																														let endIndex = mappingData[index].end_location;
																																														let newStr = str.slice(0, startIndex-1) + replaceStr + str.slice(endIndex);

																																														document.getElementById("nineNumber").innerHTML=newStr;

																																														//获取分类编码
																																														let sortId = document.getElementById("produceId").innerHTML;
																																														document.getElementById("UpdateMaterialNumber").value=sortId+newStr+lsn;
																																														for (let j = 0; j < newStr.length; j++) {
																																															let m=Number(j)+Number(1);
																																															document.getElementById("upmdiv"+m+"").innerHTML=newStr[j];
																																														}
																																													}
																																												})

																																											}
																																										})

																																									}
																																									else {
																																										//不在映射中
																																										//查看这个值是否已经被使用了
																																										let data={
																																											attNameId:num,
																																											attValue:document.getElementById("upInput"+s+"").value.trim()

																																										}
																																										axios({
																																											method:"post",
																																											url:"attributeValue/selectValueExistAdd",
																																											data:data
																																										}).then(function (resp){
																																											if (resp.data===true){
																																												//值已经存在，不在映射中，查询编码并显示
																																												axios({
																																													method:"post",
																																													url:"aAttributeValue/selectValueExistAdd",
																																													data:data
																																												}).then(function (resp){
																																													document.getElementById("upCode"+s+"").value=resp.data[0].code;
																																												})
																																											}else {
																																												//值不存在，将编码显示
																																												document.getElementById("upCode"+s+"").value=codeData.code;
																																											}
																																										})
																																									}

																																								})



																																							}
																																							else {
																																								let value1 = document.getElementById("upInput"+s+"").value;
																																								if(value1.length>0){
																																									alert("输入值的长度与编码长度不符，请核对后在进行操作！");
																																								}
																																								document.getElementById("upInput"+s+"").value="";
																																								document.getElementById("upCode"+s+"").value="";


																																							}
																																						})
																																					}else {
																																						let value2 = document.getElementById("upInput"+s+"").value;
																																						if(value2.length>0){
																																							alert("输入值的格式不适合作为编码，请核对后再进行操作！");
																																						}
																																						document.getElementById("upInput"+s+"").value="";
																																						document.getElementById("upCode"+s+"").value="";
																																					}
																																				}
																																				else {
																																					//未开启值作为编码的功能,查看是否有编码表存在,查询是否开启了顺序编码的功能
																																					axios({
																																						method:"post",
																																						url:"attributeFunction/selectIfOrderCode",
																																						data:num
																																					}).then(function (resp){
																																						if (resp.data===true){
																																							//开启了顺序编码的功能
																																							//获取长度
																																							axios({
																																								method:"post",
																																								url:"attribute/selectById",
																																								data:num
																																							}).then(function (resp){
																																								let datas=resp.data;
																																								let length = datas[0].length;
																																								//查询该属性下有多少个编码
																																								axios({
																																									method:"post",
																																									url:"attributeValue/selectByAttNameId",
																																									data:num
																																								}).then(function (resp){
																																									let CodeNumber=resp.data.length;
																																									let number= Number(CodeNumber)+Number(1);
																																									let generateCode1 = generateCode(length,number,list,"");
																																									function orderNoRepeat(generateCode1){
																																										//调用方法获取编码

																																										//验证编码是否重复
																																										let codeData={
																																											attNameId:num,
																																											code:generateCode1,
																																										}
																																										axios({
																																											method:"post",
																																											url:"attributeValue/selectCodeExistAdd",
																																											data:codeData
																																										}).then(function (resp){
																																											if (resp.data===true){
																																												//存在
																																												number++;
																																												let generateCode1 = generateCode(length,number,list,"");
																																												orderNoRepeat(generateCode1);

																																											}else if (resp.data===false){
																																												//不存在
																																												//查看该属性是否在映射中
																																												axios({
																																													method:"post",
																																													url:"mapping/selectMapping",
																																													data:sortId //分类id
																																												}).then(function (resp){
																																													let mappingData=resp.data;
																																													let map=false;
																																													let index=0;
																																													for (let j = 0; j < mappingData.length; j++) {
																																														if (Number(num)===mappingData[j].att_name_id){
																																															map=true;
																																															index=j;
																																															break;
																																														}

																																													}
																																													if (map===true){
																																														//值不存在
																																														//得到了属性值的编码
																																														let nine = document.getElementById("nineNumber").innerHTML;
																																														let str = nine;
																																														//判断输入的长度和编码长度

																																														let replaceStr =codeData.code;//要替换的字符串

																																														document.getElementById("upCode"+s+"").value=codeData.code;
																																														// document.getElementById("updiv"+s+"").innerHTML=replaceStr;
																																														let startIndex = mappingData[index].begin_location;


																																														let endIndex = mappingData[index].end_location;

																																														let newStr = str.slice(0, startIndex-1) + replaceStr + str.slice(endIndex);

																																														document.getElementById("nineNumber").innerHTML=newStr;
																																														//获取分类编码
																																														let sortId = document.getElementById("produceId").innerHTML;
																																														document.getElementById("UpdateMaterialNumber").value=sortId+newStr+lsn;
																																														for (let j = 0; j < newStr.length; j++) {
																																															let m=Number(j)+Number(1);
																																															document.getElementById("upmdiv"+m+"").innerHTML=newStr[j];
																																														}
																																													}
																																													else {
																																														//不在映射中
																																														//查看这个值是否已经被使用了
																																														let data={
																																															attNameId:num,
																																															attValue:document.getElementById("upInput"+s+"").value.trim()

																																														}
																																														axios({
																																															method:"post",
																																															url:"attributeValue/selectValueExistAdd",
																																															data:data
																																														}).then(function (resp){
																																															if (resp.data===true){
																																																//值已经存在，不在映射中，查询编码并显示
																																																axios({
																																																	method:"post",
																																																	url:"attributeValue/selectValueExistAdd",
																																																	data:data
																																																}).then(function (resp){
																																																	document.getElementById("upCode"+s+"").value=resp.data[0].code;
																																																})
																																															}else {
																																																//值不存在，将编码显示
																																																document.getElementById("upCode"+s+"").value=codeData.code;
																																															}
																																														})
																																													}
																																												})


																																											}
																																										})
																																									}
																																									orderNoRepeat(generateCode1);


																																								})
																																							})
																																						}
																																						else {
																																							//未开启值作为编码，也未开启顺序编码
																																							axios({
																																								method:"post",
																																								url:"attributeValue/selectByAttNameId",
																																								data:num
																																							}).then(function (resp){
																																								if (resp.data.length>0){
																																									//有编码表
																																									document.getElementById("upInput"+s+"").value="";
																																									document.getElementById("upCode"+s+"").value="";
																																								}else {
																																									//没有编码表
																																									document.getElementById("upCode"+s+"").value="";
																																								}
																																							})
																																						}
																																					})



																																				}
																																			})
																																		}
																																	})
																																}
																															})
																														}
																														if (rangeDatas[0].range===0){
																															//0表示是值
																															//模糊查询
																															axios({
																																method:"post",
																																url:"attributeValue/inputLX?attNameId="+num+"",
																																data:value
																															}).then(function (resp){
																																let lxDatas=resp.data;


																																if (lxDatas.length>0){
																																	var formdata="";
																																	if (lxDatas.length>5){
																																		for (let i = 0; i < 5; i++) {
																																			formdata+="<div class='ULX"+num+"' style='width: 100%;height: auto;margin: auto'><pre>"+lxDatas[i].att_value+"</pre></div>"
																																		}
																																	}else if (lxDatas.length===1){
																																		formdata+="<div class='ULX"+num+"' style='width: 100%;height: auto;margin: auto'><pre>"+lxDatas[0].att_value+"</pre></div>"
																																	}
																																	else{
																																		for (let i = 0; i <lxDatas.length; i++) {
																																			formdata+="<div class='ULX"+num+"' style='width: 100%;height: auto;margin: auto'><pre>"+lxDatas[i].att_value+"</pre></div>"
																																		}
																																	}

																																	document.getElementById("inputUpValue"+mdata[j].id+"").innerHTML=formdata;


																																	//绑定触发变色
																																	let lxs = document.querySelectorAll(".ULX"+num+"");
																																	for (let j = 0; j < lxs.length; j++) {
																																		lxs[j].onmouseover=function (){
																																			lxs[j].style.backgroundColor="white"
																																		}
																																		lxs[j].onmouseout=function (){
																																			lxs[j].style.backgroundColor="#e0e0e0"
																																		}

																																	}

																																	document.getElementById("inputUpValue"+mdata[j].id+"").style.display="";


																																	//先去除所有的绑定方法
																																	document.getElementById("inputUpValue"+mdata[j].id+"").onmouseout=null;
																																	document.getElementById("inputUpValue"+mdata[j].id+"").onmouseover=null;
																																	document.getElementById("upInput"+mdata[j].id+"").onblur=null;

																																	//失去光标要执行的方法
																																	document.getElementById("upInput"+mdata[j].id+"").onblur=function (){
																																		//没有动作，失去光标，获取输入的值
																																		let value1 = document.getElementById("upInput"+mdata[j].id+"").value.trim();
																																		if (value1.length>0){
																																			autoAndNo(value1,num,mdata[j].id);
																																		}
																																		else {
																																			document.getElementById("upInput"+mdata[j].id+"").value="";
																																			//查看该属性是否在映射中
																																			axios({
																																				method:"post",
																																				url:"mapping/selectMapping",
																																				data:sortId //分类id
																																			}).then(function (resp) {
																																				let mappingData = resp.data;
																																				let map = false;
																																				let index = 0;
																																				for (let j = 0; j < mappingData.length; j++) {
																																					if (Number(num) === mappingData[j].att_name_id) {
																																						map = true;
																																						index = j;
																																						break;
																																					}

																																				}

																																				if (map === true) {
																																					//在映射中，根据长度和起始位置改编物料编码

																																					let nine = document.getElementById("nineNumber").innerHTML;
																																					let str = nine;
																																					//判断输入的长度和编码长度
																																					let replaceStr ="";
																																					for (let j = 0; j < mappingData[index].length; j++) {
																																						replaceStr+="0";
																																					}

																																					// document.getElementById("updiv"+s+"").innerHTML=replaceStr;
																																					let startIndex = mappingData[index].begin_location;


																																					let endIndex = mappingData[index].end_location;

																																					let newStr = str.slice(0, startIndex-1) + replaceStr + str.slice(endIndex);

																																					document.getElementById("nineNumber").innerHTML=newStr;
																																					//获取分类编码
																																					let sortId = document.getElementById("produceId").innerHTML;
																																					document.getElementById("UpdateMaterialNumber").value=sortId+newStr+lsn;
																																					for (let j = 0; j < newStr.length; j++) {
																																						let m=Number(j)+Number(1);
																																						document.getElementById("upmdiv"+m+"").innerHTML=newStr[j];
																																					}



																																				}
																																			})
																																		}
																																		document.getElementById("inputUpValue"+mdata[j].id+"").style.display="none";

																																	}
																																	//因为点击会默认执行失去光标的方法，所以鼠标移动到这里先将失去光标的方法置空，移出后再添加回来
																																	document.getElementById("inputUpValue"+mdata[j].id+"").onmouseover=function (){
																																		document.getElementById("upInput"+mdata[j].id+"").onblur=null;
																																	}
																																	document.getElementById("inputUpValue"+mdata[j].id+"").onmouseout=function (){
																																		document.getElementById("upInput"+mdata[j].id+"").onblur=function (){
																																			//没有动作，失去光标，获取输入的值
																																			let value1 = document.getElementById("upInput"+mdata[j].id+"").value.trim();
																																			if (value1.length>0){
																																				autoAndNo(value1,num,mdata[j].id);
																																			}
																																			else {
																																				document.getElementById("upInput"+mdata[j].id+"").value="";
																																				//查看该属性是否在映射中
																																				axios({
																																					method:"post",
																																					url:"mapping/selectMapping",
																																					data:sortId //分类id
																																				}).then(function (resp) {
																																					let mappingData = resp.data;
																																					let map = false;
																																					let index = 0;
																																					for (let j = 0; j < mappingData.length; j++) {
																																						if (Number(num) === mappingData[j].att_name_id) {
																																							map = true;
																																							index = j;
																																							break;
																																						}

																																					}

																																					if (map === true) {
																																						//在映射中，根据长度和起始位置改编物料编码

																																						let nine = document.getElementById("nineNumber").innerHTML;
																																						let str = nine;
																																						//判断输入的长度和编码长度
																																						let replaceStr ="";
																																						for (let j = 0; j < mappingData[index].length; j++) {
																																							replaceStr+="0";
																																						}

																																						// document.getElementById("updiv"+s+"").innerHTML=replaceStr;
																																						let startIndex = mappingData[index].begin_location;


																																						let endIndex = mappingData[index].end_location;

																																						let newStr = str.slice(0, startIndex-1) + replaceStr + str.slice(endIndex);

																																						document.getElementById("nineNumber").innerHTML=newStr;
																																						//获取分类编码
																																						let sortId = document.getElementById("produceId").innerHTML;
																																						document.getElementById("UpdateMaterialNumber").value=sortId+newStr+lsn;
																																						for (let j = 0; j < newStr.length; j++) {
																																							let m=Number(j)+Number(1);
																																							document.getElementById("upmdiv"+m+"").innerHTML=newStr[j];
																																						}



																																					}
																																				})
																																			}
																																			document.getElementById("inputUpValue"+mdata[j].id+"").style.display="none";

																																		}
																																	}

																																	//添加点击查询到的结果执行方法
																																	lxs = document.querySelectorAll(".ULX"+num+"");
																																	// for (let j = 0; j < lxs.length; j++) {
																																	// 	lxs[j].onclick=function (){
																																	// 		//将点击的值放在输入框中
																																	// 		document.getElementById("upInput"+mdata[j].id+"").value=lxDatas[j].att_value;
																																	// 		autoAndNo(lxDatas[j].att_value,num,mdata[j].id);
																																	// 		document.getElementById("inputUpValue"+mdata[j].id+"").style.display="none";
																																	//
																																	//
																																	// 	}
																																	// }

																																	for (let l = 0; l < lxs.length; l++) {
																																		lxs[l].onclick=function (){
																																			//将点击的值放在输入框中
																																			document.getElementById("upInput"+mdata[j].id+"").value=lxDatas[l].att_value;
																																			autoAndNo(lxDatas[l].att_value,num,mdata[j].id);
																																			document.getElementById("inputUpValue"+mdata[j].id+"").style.display="none";
																																		}
																																	}


																																}
																																else {

																																	//先去除所有的绑定方法
																																	document.getElementById("inputUpValue"+mdata[j].id+"").onmouseout=null;
																																	document.getElementById("inputUpValue"+mdata[j].id+"").onmouseover=null;
																																	document.getElementById("upInput"+mdata[j].id+"").onblur=null;
																																	document.getElementById("inputUpValue"+mdata[j].id+"").style.display="";
																																	document.getElementById("inputUpValue"+mdata[j].id+"").innerHTML="<div class='ULX"+num+"' style='width: 100%;height: auto;margin: auto;color: red'>没有相关数据!</div>"

																																	//失去光标要执行的方法
																																	document.getElementById("upInput"+mdata[j].id+"").onblur=function (){
																																		//没有动作，失去光标，获取输入的值
																																		let value1 = document.getElementById("upInput"+mdata[j].id+"").value.trim();
																																		if (value1.length>0){
																																			autoAndNo(value1,num,mdata[j].id);
																																		}
																																		else {
																																			document.getElementById("upInput"+mdata[j].id+"").value="";
																																			//查看该属性是否在映射中
																																			axios({
																																				method:"post",
																																				url:"mapping/selectMapping",
																																				data:sortId //分类id
																																			}).then(function (resp) {
																																				let mappingData = resp.data;
																																				let map = false;
																																				let index = 0;
																																				for (let j = 0; j < mappingData.length; j++) {
																																					if (Number(num) === mappingData[j].att_name_id) {
																																						map = true;
																																						index = j;
																																						break;
																																					}

																																				}

																																				if (map === true) {
																																					//在映射中，根据长度和起始位置改编物料编码

																																					let nine = document.getElementById("nineNumber").innerHTML;
																																					let str = nine;
																																					//判断输入的长度和编码长度
																																					let replaceStr ="";
																																					for (let j = 0; j < mappingData[index].length; j++) {
																																						replaceStr+="0";
																																					}

																																					// document.getElementById("updiv"+s+"").innerHTML=replaceStr;
																																					let startIndex = mappingData[index].begin_location;


																																					let endIndex = mappingData[index].end_location;

																																					let newStr = str.slice(0, startIndex-1) + replaceStr + str.slice(endIndex);

																																					document.getElementById("nineNumber").innerHTML=newStr;
																																					//获取分类编码
																																					let sortId = document.getElementById("produceId").innerHTML;
																																					document.getElementById("UpdateMaterialNumber").value=sortId+newStr+lsn;
																																					for (let j = 0; j < newStr.length; j++) {
																																						let m=Number(j)+Number(1);
																																						document.getElementById("upmdiv"+m+"").innerHTML=newStr[j];
																																					}



																																				}
																																			})
																																		}
																																		document.getElementById("inputUpValue"+mdata[j].id+"").style.display="none";

																																	}

																																}
																															})
																														}
																														else if (rangeDatas[0].range===1){

																															//判断输入的值是否符合数据库中的范围
																															function compare (str,value){

																																//  console.log("范围"+ str)
																																// console.log("传入的值"+value)
																																if (value.length>0){
																																	var pattern = /^\[.*\]$/;
																																	var patterns= /^\<.*\]$/;
																																	var pattern1= /^\<.*\>$/;
																																	var patterns1= /^\[.*\>$/;

																																	if (pattern.test(str)===true){
																																		str=str.split("[").join("");
																																		str=str.split("]").join("");
																																		// str=str.replace(/[a-zA-Z]/g, '');
																																		let strings = str.split("~");

																																		//判断输入的数值是否符合这个范围
																																		if (Number(power(value))>=Number(power(strings[0])) && Number(power(value))<=Number(power(strings[1]))){
																																			return true;
																																		}else {
																																			return false;
																																		}

																																	}
																																	else if (patterns.test(str)===true){

																																		str=str.split("<").join("");
																																		str=str.split("]").join("");
																																		// str=str.replace(/[a-zA-Z]/g, '');
																																		let strings = str.split("~");

																																		// console.log("开始"+ strings[0]);
																																		// console.log("结束"+strings[1])

																																		//判断输入的数值是否符合这个范围
																																		if (Number(power(value))>Number(power(strings[0])) && Number(power(value))<=Number(power(strings[1]))){
																																			return true;
																																		}else {
																																			return false;
																																		}
																																	}
																																	else if (pattern1.test(str)===true){

																																		str=str.split("<").join("");
																																		str=str.split(">").join("");
																																		// str=str.replace(/[a-zA-Z]/g, '');
																																		let strings = str.split("~");

																																		// console.log("开始"+ strings[0]);
																																		// console.log("结束"+strings[1])

																																		//判断输入的数值是否符合这个范围
																																		if (Number(power(value))>Number(power(strings[0])) && Number(power(value))<Number(power(strings[1]))){
																																			return true;
																	``																	}else {
																																			return false;
																																		}
																																	}
																																	else if (patterns1.test(str)===true){

																																		str=str.split("[").join("");
																																		str=str.split(">").join("");
																																		// str=str.replace(/[a-zA-Z]/g, '');
																																		let strings = str.split("~");

																																		// console.log("开始"+ strings[0]);
																																		// console.log("结束"+strings[1])

																																		//判断输入的数值是否符合这个范围
																																		if (Number(power(value))>=Number(power(strings[0])) && Number(power(value))<Number(power(strings[1]))){
																																			return true;
																																		}else {
																																			return false;
																																		}
																																	}

																																	else if (pattern.test(str)===false && patterns.test(str)===false && pattern1.test(str)===false && patterns1.test(str)===false) {
																																		alert("数据有问题，不符合格式，请联系相关人员！");

																																	}
																																}else {
																																	return false;
																																}


																															}
																															//1表示是范围,获取所有的范围值
																															axios({
																																method:"post",
																																url:"attributeValue/selectByAttNameId",
																																data:num
																															}).then(function (resp){
																																let datas=resp.data;
																																let b;
																																let num2=mdata[j].id
																																for (let i = 0; i < datas.length; i++) {
																																	b = compare(datas[i].attValue,value);
																																	if (b===true){
																																		document.getElementById("inputUpValue"+num2+"").innerHTML="<div id='LX"+num+"' style='width: 100%;height: auto;margin: auto;'><pre>"+datas[i].attValue+"</pre></div>"
																																		document.getElementById("inputUpValue"+num2+"").style.display="";
																																		function blurAndClick (){

																																			document.getElementById("inputUpValue"+num2+"").style.display="none";
																																			document.getElementById("upCode"+num2+"").value=datas[i].code;
																																			// document.getElementById("updiv"+num2+"").innerHTML=datas[i].code;
																																			//查询这个属性是否在映射中
																																			axios({
																																				method:"post",
																																				url:"mapping/selectMapping",
																																				data:sortId//分类id
																																			}).then(function (resp){
																																				let mappingData=resp.data;
																																				let map=false;
																																				let index=0;
																																				for (let j = 0; j < mappingData.length; j++) {
																																					if (Number(num)===mappingData[j].att_name_id){
																																						map=true;
																																						index=j;
																																						break;
																																					}

																																				}

																																				if (map===true){
																																					//存在映射中
																																					let nine = document.getElementById("nineNumber").innerHTML;
																																					let str = nine;
																																					//判断输入的长度和编码长度

																																					let replaceStr =datas[i].code;//要替换的字符串
																																					let startIndex = mappingData[index].begin_location;

																																					let endIndex = mappingData[index].end_location;

																																					let newStr = str.slice(0, startIndex-1) + replaceStr + str.slice(endIndex);

																																					document.getElementById("nineNumber").innerHTML=newStr;
																																					//获取分类编码
																																					let sortId = document.getElementById("produceId").innerHTML;
																																					document.getElementById("UpdateMaterialNumber").value=sortId+newStr+lsn;
																																					for (let j = 0; j < newStr.length; j++) {
																																						let m=Number(j)+Number(1);
																																						document.getElementById("upmdiv"+m+"").innerHTML=newStr[j];
																																					}
																																				}

																																			})


																																		}
																																		document.getElementById("LX"+num+"").onclick=blurAndClick;
																																		let trim = document.getElementById("upInput"+num2+"").value.trim();
																																		if (trim.length>0){
																																			document.getElementById("upInput"+num2+"").onblur=blurAndClick;
																																		}
																																		else {
																																			document.getElementById("upInput"+num2+"").value="";
																																		}
																																		break;
																																	}

																																}

																																if (b===false) {
																																	document.getElementById("inputUpValue"+num2+"").style.display="";
																																	document.getElementById("inputUpValue"+num2+"").innerHTML="<div id='LX"+num+"' style='width: 100%;height: auto;margin: auto;color: red'>没有相关数据!</div>"
																																	document.getElementById("LX"+num+"").onclick=function (){
																																		document.getElementById("inputUpValue"+num2+"").style.display="none";
																																		document.getElementById("upInput"+num2+"").value="";
																																	}
																																	document.getElementById("upInput"+num2+"").onblur=function (){
																																		document.getElementById("inputUpValue"+num2+"").style.display="none";
																																		document.getElementById("upInput"+num2+"").value="";

																																	}
																																}

																															})


																														}
																													})
																												}
																											})

																										}
																										else {

																											document.getElementById("upCode"+mdata[j].id+"").value="";
																										}

																									}, 500));
																									input.addEventListener('blur',function() {upblack(mdata[j].id)});
																									
																								}
																							}
																						}


																						//查询是否存在出入库日志
																						let promise = ifLog(data[i].id,data[i].vault);
																						promise.then(function (resp){
																							if (resp===true){
																								//存在，不允许修改
																								for (let j = 0; j < datas.length; j++) {
																									for (let k = 0; k < mdata.length; k++) {
																										if (Number(datas[j].att_name_id)===Number(mdata[k].parentId)){
																											document.getElementById("upInput"+mdata[k].id+"").disabled=true;
																											document.getElementById("updateUnit").disabled=true;
																											document.getElementById("bb"+mdata[k].id+"").style.color="red";
																											document.getElementById("bb"+mdata[k].id+"").style.display="";

																										}
																									}
																								}
																							}else {
																								//不存在，允许修改
																								for (let j = 0; j < datas.length; j++) {
																									for (let k = 0; k < mdata.length; k++) {
																										if (Number(datas[j].att_name_id)===Number(mdata[k].parentId)){
																											document.getElementById("bb"+mdata[k].id+"").style.color="red";
																											document.getElementById("bb"+mdata[k].id+"").style.display="";

																										}
																									}
																								}
																							}
																						})



																					}
																				})




																				//点击更新,循环完了绑定更新事件，更新完基础的再更新后添加的
																				document.getElementById("updateSubmit").onclick=function (){
																					let booleanPromise = updateQx();
																					booleanPromise.then(async function (resp){
																						if (resp===true){
																							//获取数据
																							let formdata={
																								id:datas[0].id,
																								parentId:sortId,
																								name:"",
																								url:"",
																								materialNumber:"",
																								brand:"",
																								description:"",
																								number:"",
																								unit:"",
																								priceUnit:""
																							}

																							formdata.name=document.getElementById("updateName").value;
																							let ylPc = document.querySelector(".ylPic");
																							if (ylPc){
																								formdata.url =ylPc.querySelector("img").src;
																							}
																							formdata.materialNumber= document.getElementById("UpdateMaterialNumber").value;
																							formdata.brand= document.getElementById("updateBrand").value;
																							formdata.description= document.getElementById("updateDescription").value;
																							formdata.unit=document.getElementById("updateUnit").value;
																							// formdata.number = document.getElementById("updateNumber").value;
																							//获取筛选框中的内容
																							let priceUnitSelect = document.getElementById("updatePriceUnit");
																							let priceUnitIndex = priceUnitSelect.selectedIndex;//获取select的索引
																							//获取选中的值
																							formdata.priceUnit=priceUnitSelect.options[priceUnitIndex].value;
																							let b = updateIfBaseExist();
																							if (b===true){
																								//判断后添加的是否为空


																								var content={
																									id:"",
																									parentId:"",
																									productId:"",
																									content:"",
																								}

																								var list=[mdata.length]


																								for (let i = 0; i < mdata.length; i++) {

																									content.id=mdata[i].id;
																									content.content=document.getElementById("upInput"+mdata[i].id+"").value.trim();
																									content.parentId=mdata[i].parentId
																									list[i]=content;
																									content={
																										id:"",
																										parentId:"",
																										productId:"",
																										content:"",
																									}
																								}

																								//判断开启了唯一标识的是否重复
																								let unique=false;
																								let dj=0;
																								for (let j = 0; j <list.length ; j++) {
																									let parentId = list[j].parentId;
																									let productId = datas[0].id;
																									let content1 = list[j].content;
																									let b1 = await uniqueUpdate(parentId,content1,productId);
																									if (b1===true){
																										unique=true;
																										dj=j;
																										break;
																									}
																								}
																								if (unique===true){
																									//查询属性名
																									axios({
																										method:"post",
																										url:"attribute/selectById",
																										data:list[dj].parentId
																									}).then(function (resp){
																										let datas=resp.data;
																										alert(""+datas[0].name+"开启了唯一标识，"+list[dj].content+"已存在！")
																									})
																								}
																								else {
																									let l=true;
																									//不在映射中的可以为空
																									axios({
																										method:"post",
																										url:"mapping/selectMapping",
																										data:sortId
																									}).then(function (resp)
																									{
																										let mappingdatas= resp.data;
																										for (let j = 0; j < list.length; j++) {
																											for (let k = 0; k < mappingdatas.length; k++) {
																												if (Number(list[j].parentId)===Number(mappingdatas[k].att_name_id)){
																													let trim = list[j].content.trim();
																													if (trim.length===0||document.getElementById("upCode"+list[j].id+"").value.length===0){
																														l=false;
																													}
																													if (l===false){
																														break;
																													}
																												}
																											}

																										}



																										function update(threeData){
																											//更新操作
																											axios({
																												method:"post",
																												url:"product/updateById",
																												data:formdata,
																											}).then(function (resp){
																												if (resp.data==="success"){
																													//提交多条数据
																													axios({
																														method:"post",
																														url:"attribute/updateAttributeContent",
																														data:list,
																													}).then(function (resp)
																													{

																														if (resp.data==="success"){
																															//更新成功，下面进行编码的添加，先判断哪些属性不在映射中就要查询该分类下的映射
																															if (threeData.length>0){
																																//执行添加编码的操作
																																axios({
																																	method:"post",
																																	url:"attributeValue/addCodeAuto",
																																	data:threeData
																																}).then(function (resp){
																																	if (resp.data==="success"){

																																		alert("更新成功！");
																																		searchRe();
																																		let wd = document.querySelector(".window");
																																		wd.style.display="none";
																																	}else {
																																		alert("更新成功，但添加编码失败，请联系先关人员！")
																																	}
																																})
																															}else {
																																alert("更新成功！");
																																searchRe();
																																let wd = document.querySelector(".window");
																																wd.style.display="none";
																															}



																														}else {
																															alert("更新失败，请联系相关人员！")
																														}

																													})



																												}else {
																													alert("修改失败，请联系相关人员！")
																												}
																											})
																										}
																										function ifyiy(threeData){
																											for (let i = 0; i < mdata.length; i++) {

																												content.id=mdata[i].id;
																												content.content=document.getElementById("upInput"+mdata[i].id+"").value.trim();
																												content.parentId=mdata[i].parentId
																												list[i]=content;
																												content={
																													id:"",
																													parentId:"",
																													productId:"",
																													content:"",
																												}
																											}

																											for (let j = 0; j < list.length; j++) {
																												for (let k = 0; k < mappingdatas.length; k++) {
																													if (Number(list[j].parentId)===Number(mappingdatas[k].att_name_id)){
																														let trim = list[j].content.trim();
																														if (trim.length===0||document.getElementById("upCode"+list[j].id+"").value.length===0){
																															l=false;
																														}
																														if (l===false){
																															break;
																														}
																													}
																												}

																											}
																											//自动生成一组数据所两两组合的所有结果
																											let str=[{num:"0"},{num:"1"},{num:"2"},{num:"3"},{num:"4"},{num:"5"},{num:"6"},{num:"7"},{num:"8"},{num:"9"}]
																											let ls1=str[0].num;
																											let ls2;
																											let lsSum=[];
																											let lsD=[{}];
																											async function lsNumber3(q) {
																												if (q < str.length) {
																													for (let i = 0; i < str.length; i++) {

																														ls2 = str[i].num;
																														ls1 = str[q].num
																														if (String(ls2) === String(str[str.length - 1].num)) {
																															q++;

																															let sum = (ls1 + ls2)
																															lsD = [{num: "" + sum + ""}];
																															lsSum = lsD.concat(lsSum);
																															lsD = {};
																															lsNumber3(q);


																														} else {
																															let sum = (ls1 + ls2)
																															lsD = [{num: "" + sum + ""}];
																															lsSum = lsD.concat(lsSum);
																															lsD = {};

																														}
																													}
																												} else if (q === str.length) {

																													//查询是否开启了映射编码的属性
																													let b1 = await ifSerialExistUpdate(mappingdatas);
																													if (b1 === true) {
																														//开启了映射编码表的功能，查询当前属性使用的映射中编码用到哪里了
																														await serialCodeUpdate(mappingdatas, formdata.parentId, list);
																														formdata.materialNumber = document.getElementById("UpdateMaterialNumber").value;
																														//查询是否重复
																														let b4 = await ifMaterialNumberExistUpdate(formdata);
																														if (b4 === true) {
																															//重复，使用后两位流水码
																															if (window.confirm("系统检测到要使用流水码，是否要进行添加？")) {
																																async function sub2(s) {

																																	if (Number(s) >=0) {
																																		//后两位流水码更换
																																		let value5 = document.getElementById("UpdateMaterialNumber").value;
																																		//替换最后两位
																																		let newStr = value5.substring(0, value5.length - 2) + lsSum[s].num;


																																		formdata.materialNumber = newStr;


																																		axios({
																																			method: "post",
																																			url: "product/selectUpMN",
																																			data: formdata
																																		}).then(async function (resp) {
																																			if (resp.data === false) {
																																				//不重复了，执行添加操作
																																				await update(threeData)


																																			} else {
																																				s--;
																																				await sub2(s);
																																			}

																																		})

																																	} else {
																																		//默认流水码用完了，映射流水码+1
																																		let b2 = await serialCodeAddOneUpdate(mappingdatas, formdata.parentId, list);
																																		if (b2 === true) {
																																			await sub2(lsSum.length - 1);

																																		} else {
																																			//映射中流水码用完了，使用后两位
																																			//映射中的流水码用完了，默认最后两位流水码用完要报警
																																			function sub1(s) {

																																				if (Number(s) >=0) {
																																					//后两位流水码更换
																																					let value5 = document.getElementById("materialNumber").value;
																																					//替换最后两位
																																					let newStr = value5.substring(0, value5.length - 2) + lsSum[s].num;

																																					formdata.materialNumber = newStr;



																																					axios({
																																						method: "post",
																																						url: "product/selectUpMN",
																																						data: formdata
																																					}).then(async function (resp) {
																																						if (resp.data === false) {
																																							//不重复了，执行添加操作
																																							await update(threeData)


																																						} else {
																																							s--;
																																							sub1(s);
																																						}

																																					})

																																				} else {
																																					alert("流水码已用完！请联系相关人员！")
																																				}

																																			}

																																			sub1(lsSum.length - 1);

																																		}

																																	}

																																}

																																await sub2(lsSum.length - 1);
																																return true;
																															}
																															else {
																																return false;
																															}

																														} else {
																															//不重复，直接提交
																															await ifyiy(threeData);
																														}

																													}
																													else {
																														//未开启映射编码的属性
																														//得到了所有的集合
																														//判断物料号是否重复
																														axios({
																															method: "post",
																															url: "product/selectUpMN",
																															data: formdata
																														}).then(async function (resp) {
																															if (resp.data === true) {

																																if (window.confirm("系统检测到要使用流水码，是否要进行添加？")) {

																																	async function sub(s) {

																																		if (Number(s) >=0) {
																																			//后两位流水码更换
																																			let value5 = document.getElementById("UpdateMaterialNumber").value;
																																			//替换最后两位
																																			let newStr = value5.substring(0, value5.length - 2) + lsSum[s].num;

																																			formdata.materialNumber = newStr;


																																			axios({
																																				method: "post",
																																				url: "product/selectUpMN",
																																				data: formdata
																																			}).then(function (resp) {
																																				if (resp.data === false) {
																																					//不重复了，执行添加操作
																																					update(threeData);


																																				} else {
																																					s--;
																																					sub(s);
																																				}

																																			})

																																		} else {

																																			alert("流水码已用完！请联系相关人员！")

																																		}

																																	}

																																	await sub(lsSum.length - 1);
																																	return true;
																																} else {
																																	return false;
																																}


																															} else {
																																//代表不重复
																																await update(threeData)
																															}
																														})
																													}
																												}

																											}
																											//判断是否是同一个物料.1。先判断物料名称是否重复
																											axios({
																												method:"post",
																												url:"product/selectNameIfExist",
																												data:formdata
																											}).then(async function (resp){
																												let d=0;
																												let e=true;
																												if (resp.data===true){
																													//存在当前名称,查询后来添加的属性是否有重复的
																													//查询当前分类下的产品id
																													await 	axios({
																														method:"post",
																														url:"product/selectAllInSortDeleteSignUpdate",
																														data:formdata
																													}).then(async function (resp){
																														let datas=resp.data;
																														for (let j = 0; j < datas.length; j++) {
																															if (Number(formdata.id)!==Number(datas[j].id)){
																																d=0;

																																for (let k = 0; k < list.length; k++) {
																																	list[k].productId=datas[j].id;
																																	await axios({
																																		method:"post",
																																		url:"attributeFunction/selectByAttNameId",
																																		data:list[k].parentId
																																	}).then(async function (resp){
																																		let functionCode=resp.data;
																																		let serialCode = functionCode[0].serialCode
																																		if (Number(serialCode)===Number(1)){
																																			//开启了流水码功能
																																			d++;
																																		}
																																		else {
																																			await axios({
																																				method:"post",
																																				url:"attribute/selectAttributeContentIfExist",
																																				data:list[k]
																																			}).then(function (resp){
																																				if (resp.data===true){
																																					d++;
																																				}
																																			})
																																		}
																																	})



																																}
																																if (Number(d)===Number(list.length)){
																																	e=false;
																																	break;
																																}
																															}


																														}
																													})

																													if (e===false){
																														let productId=list[0].productId;
																														await axios({
																															method:"post",
																															url:"product/selectById",
																															data:productId
																														}).then(function (resp){
																															let datas = resp.data;
																															if (datas[0].deleteSign===1){
																																//与弃用物料信息相同，提示
																																alert("检测到要修改的物料已被弃用，如要修改请联系相关人员！")

																															}
																															else {

																																alert("该物料已存在，请核对后再操作！")
																															}
																														})
																													}
																													else if (e===true) {


																														lsNumber3(0);

																													}


																												}
																												else if (resp.data===false){
//自动生成一组数据所两两组合的所有结果


																													lsNumber3(0);
																												}
																											})
																										}


																										if (l===true){
																											//值作为编码的判断
																											axios({
																												method:"post",
																												url:"mapping/selectMapping",
																												data:sortId
																											}).then(async function (resp){
																												// let mappingDatas=resp.data;
																												let datasOne=[];
																												let datasThree=[];


																												for (let j = 0; j < list.length; j++) {


																													datasOne.push(list[j])


																												}
																												//得到datas,查看是否有开启值作为编码的功能
																												let datasTwo=[];
																												if (datasOne.length>0){
																													for (let j = 0; j < datasOne.length; j++) {
																														await axios({
																															method:"post",
																															url:"attributeFunction/selectByAttNameId",
																															data:datasOne[j].parentId
																														}).then(function (resp){
																															if (resp.data[0].autoCode===1 || resp.data[0].autoCode===2 ){
																																datasOne[j].autoCode=resp.data[0].autoCode;
																																datasTwo.push(datasOne[j])
																															}
																														})
																													}

																													if (datasTwo.length>0){
																														//判断格式是否有不符和作为编码的

																														var pattern = /^[0-9A-Za-z]+$/;
																														let gs=true;
																														for (let j = 0; j < datasTwo.length; j++) {
																															//判断编码是否存在
																															if (datasTwo[j].content.length>0){
																																let ifformdata={
																																	attNameId:datasTwo[j].parentId,
																																	attValue:datasTwo[j].content
																																}
																																await axios({
																																	method:"post",
																																	url:"attributeValue/selectCode",
																																	data:ifformdata
																																}).then(function (resp){
																																	let ifdatas=resp.data;
																																	if (ifdatas.length>0){
																																		//存在编码，使用的是已存在的，不判断
																																	}else {
																																		if (datasTwo[j].autoCode===1){
																																			if (pattern.test(datasTwo[j].content)===false && datasTwo[j].content.length>0){
																																				alert(datasTwo[j].content+"的格式不适合作为编码的标准，请核对后再试！");
																																				gs=false
																																			}
																																		}

																																	}
																																})
																															}
																															if (gs===false){
																																break;
																															}
																														}
																														if (gs===true){
																															//都符合作为编码的标准，然后去查询是否有长度
																															let le=true;

																															for (let j = 0; j < datasTwo.length; j++) {
																																let ifformdata={
																																	attNameId:datasTwo[j].parentId,
																																	attValue:datasTwo[j].content
																																}
																																await axios({
																																	method:"post",
																																	url:"attributeValue/selectCode",
																																	data:ifformdata
																																}).then(async function (resp){
																																	let ifdatas=resp.data;
																																	if (ifdatas.length>0){
																																		//存在编码，使用的是已存在的，不判断
																																	}else{
																																		await axios({
																																			method:"post",
																																			url:"attribute/selectById",
																																			data:datasTwo[j].parentId
																																		}).then(async function (resp){
																																			let lengthTwo=resp.data[0].length
																																			if (lengthTwo===0){
																																				alert("值为"+datasTwo[j].content+"的属性还未设置编码长度，请设置后再进行操作！");
																																				le=false;
																																			}else {
																																				//判断长度是否符合
																																				if (datasTwo[j].content.length>0){
																																					if ((resp.data[0].length<datasTwo[j].content.length)&&datasTwo[j].autoCode===1){
																																						alert(datasTwo[j].content+"的长度大于编码长度，请核对后在进行操作！");
																																						le=false;
																																					}
																																					else {
																																						//长度合适，查询这个属性值是否已经有编码了(是否存在)
																																						let formdata={
																																							attNameId:datasTwo[j].parentId,
																																							attValue:datasTwo[j].content
																																						}
																																						await axios({
																																							method:"post",
																																							url:"attributeValue/selectValueExistAdd",
																																							data:formdata
																																						}).then(function (resp){
																																							if (resp.data===false){
																																								//值不重复,将值作为编码
																																								let codeData
																																								codeData={
																																									attNameId:datasTwo[j].parentId,
																																									attValue:datasTwo[j].content,
																																									code:document.getElementById("upCode"+datasTwo[j].id+"").value,
																																								}


																																								datasThree.push(codeData)


																																							}
																																						})
																																					}
																																				}


																																			}
																																		})
																																	}
																																})


																																if (le===false){
																																	break;
																																}
																															}

																															if (le===true){
																																//得到了最后的编码合集，进行添加
																																ifyiy(datasThree);
																															}

																														}

																													}
																													else {
																														//有不在映射中的，但是没有开启值作为编码的功能，不需要添加编码
																														ifyiy(datasThree);

																													}
																												}
																												else {
																													//全在映射中，直接更新内容即可，不需要添加编码

																													ifyiy(datasThree);
																												}

																											})
																										}else {
																											alert("数据未填写完整！")
																										}




																									})
																								}


																							}
																							else {
																								alert("数据未填写完整！")
																							}
																						}
																						else {
																							alert("您暂未获得修改权限！")
																						}
																					})



																				}


																			}



																		}

																		lastAttribute(mdata.length-1,mdata.length)
																	}
																	else {

																		//点击修改数据，表示没有后添加的，直接更新基础的
																		document.getElementById("updateSubmit").onclick=function (){
																			let booleanPromise = updateQx();
																			booleanPromise.then(function (resp){
																				if (resp===true){
																					let b = updateIfBaseExist();
																					if (b===true){
																						//获取数据
																						let formdata={
																							id:datas[0].id,
																							parentId:sortId,
																							name:"",
																							url:"",
																							materialNumber:"",
																							brand:"",
																							description:"",
																							number:"",
																							unit:"",
																							priceUnit:""
																						}

																						formdata.name=document.getElementById("updateName").value;
																						let ylPc = document.querySelector(".ylPic");
																						if (ylPc){
																							formdata.url =ylPc.querySelector("img").src;
																						}
																						formdata.materialNumber= document.getElementById("UpdateMaterialNumber").value;
																						formdata.brand= document.getElementById("updateBrand").value;
																						formdata.description= document.getElementById("updateDescription").value;
																						formdata.unit=document.getElementById("updateUnit").value;
																						// formdata.number = document.getElementById("updateNumber").value;
																						//获取筛选框中的内容
																						let priceUnitSelect = document.getElementById("updatePriceUnit");
																						let priceUnitIndex = priceUnitSelect.selectedIndex;//获取select的索引
																						//获取选中的值
																						formdata.priceUnit=priceUnitSelect.options[priceUnitIndex].value;
																						axios({
																							method:"post",
																							url:"product/selectNameIfExist",
																							data:formdata
																						}).then(function (resp){
																							if(resp.data===true){
																								alert("该物料已存在，请核对后再操作！")
																							}else {
																								//更新
																								axios({
																									method:"post",
																									url:"product/updateById",
																									data:formdata,
																								}).then(function (resp){
																									if (resp.data==="success"){

																										alert("修改成功！");
																										//重置信息进行显示修改后的信息
																										searchRe();
																										//关闭窗口
																										let wd = document.querySelector(".window");
																										wd.style.display="none";
																										//输入框设置为空
																										let inputs = document.querySelectorAll("#updateBlock input");
																										for (let i = 0; i < inputs.length; i++) {
																											inputs[i].value="";
																										}

																										document.getElementById("updateDescription").value="";


																									}else {
																										alert("修改失败，请联系相关人员！")
																									}
																								})
																							}
																						})


																					}else {
																						alert("数据未填写完整！")
																					}
																				}
																				else {
																					alert("您暂未获得修改权限！")
																				}
																			})




																		}

																	}


																}


															}
															selectAttributeContent(0,sortAttribute.length)

														}
														else {
															//该产品的下面分类信息没有属性信息，就直接添加更新功能
															document.getElementById("updateSubmit").onclick=function (){
																let booleanPromise = updateQx();
																booleanPromise.then(function (resp){
																	if (resp===true){
																		let b = updateIfBaseExist();
																		if (b===true){
																			//获取数据
																			let formdata={
																				id:datas[0].id,
																				parentId:sortId,
																				name:"",
																				url:"",
																				materialNumber:"",
																				brand:"",
																				description:"",
																				number:"",
																				unit:"",
																				priceUnit:""
																			}

																			formdata.name=document.getElementById("updateName").value;
																			let ylPc = document.querySelector(".ylPic");
																			if (ylPc){
																				formdata.url =ylPc.querySelector("img").src;
																			}
																			formdata.materialNumber= document.getElementById("UpdateMaterialNumber").value;
																			formdata.brand= document.getElementById("updateBrand").value;
																			formdata.description= document.getElementById("updateDescription").value;
																			formdata.unit=document.getElementById("updateUnit").value;
																			// formdata.number = document.getElementById("updateNumber").value;
																			//获取筛选框中的内容
																			let priceUnitSelect = document.getElementById("updatePriceUnit");
																			let priceUnitIndex = priceUnitSelect.selectedIndex;//获取select的索引
																			//获取选中的值
																			formdata.priceUnit=priceUnitSelect.options[priceUnitIndex].value;
																			axios({
																				method:"post",
																				url:"product/selectNameIfExist",
																				data:formdata
																			}).then(function (resp){
																				if (resp.data===true){
																					alert("该物料已存在，请核对后再操作！")
																				}else {
																					//更新
																					axios({
																						method:"post",
																						url:"product/updateById",
																						data:formdata,
																					}).then(function (resp){
																						if (resp.data==="success"){

																							alert("修改成功！");
																							//重置信息进行显示修改后的信息
																							searchRe();

																							//关闭窗口
																							let wd = document.querySelector(".window");
																							wd.style.display="none";
																							//输入框设置为空
																							let inputs = document.querySelectorAll("#updateBlock input");
																							for (let i = 0; i < inputs.length; i++) {
																								inputs[i].value="";
																							}

																							document.getElementById("updateDescription").value="";


																						}else {
																							alert("修改失败，请联系相关人员！")
																						}
																					})
																				}
																			})

																		}else {
																			alert("数据未填写完整！")
																		}
																	}
																	else {
																		alert("您暂未获得修改权限！")
																	}
																})



															}
														}
													})


												}
											}
											selectSortAttribute(datass.length-1,datass.length)


										}

									}


									//查询层数
									axios({
										method:"post",
										url:"sort/selectOtherLevel",
										data:data[i].parentId
									}).then(function (resp){
										let datas=resp.data;

										if (datas.length!==0){
											//如果能查到证明不是最后一层分类
											let level=datas[0].level
											m=level-1;
											belong2(data[i].parentId,m);
										}else {
											//查不到证明是最后一层分类直接当做id去查自己是几级分类
											axios({
												method:"post",
												url:"sort/selectOfSort",
												data:data[i].parentId
											}).then(function (resp){
												let datas=resp.data;

												m=datas[0].level;
												belong2(data[i].parentId,m);
											})
										}
									})

								})


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
										if (resp[j].function_two_id===3 && resp[j].open_status===1){
											promiseBoolean=true;
											break;
										}
									}

									if (promiseBoolean===true){
										//检测是否存在库存
										axios({
											method:"post",
											url:"product/selectById",
											data:data[i].id
										}).then(function (resp){
											let kc = resp.data;
											let kcNumber = kc[0].number;
											if (Number(kcNumber)>0 || kcNumber===undefined){
												alert("该物料存在库存，禁止删除！")
											}
											else {
												if (window.confirm("您真的要删除这个物料吗？")){
													axios({
														method:"post",
														url:"product/deleteById",
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
												}
												else {
													return false;
												}
											}
										})

									}
									else {
										alert("您暂未获得删除权限！")
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
	}
	//查询该分类中是否有分类
	axios({
		method: "post",
		url: "sort/selectExist",
		data:number
	}).then(function (resp){
		let datas=resp.data;
		if (datas === true){
			let tree = document.querySelectorAll(".l_tree");
			let ocs = document.querySelectorAll(".oc");

			// for (let j = 0; j < treeBranch.length; j++) {
			//
			// }
			if (ocs[n].innerHTML==="+"){
				tree[n+1].style.display="block";
				ocs[n].innerHTML="-"
			}else if (ocs[n].innerHTML==="-") {
				tree[n+1].style.display="none";
				ocs[n].innerHTML="+"
			}


			click();
		}
		else {

			click();

		}
	})

	height();

}

axios({
	method: "post",
	url: "sort/selectAll",
}).then(function (resp){
	let arr=resp.data;

	var data =setTreeData(arr)


	axios({
		method:"post",
		url:"sort/selectLast"
	}).then(function (resp){
		let  idData=resp.data;
		$(function(){


			//递归   自己调自己
			var n=-1;
			function tree(data){
				var str = "<ul class=l_tree>";

				for(var i=0;i<data.length;i++){

					str+='<li class="l_tree_branch"><span class="change" style="display: none">'+data[i].id+'' +
						'</span><span class="oc">+</span></span>'+'<a href="javascript:void(0)" '+n+++' onclick="select('+data[i].id+','+n+')" >'+data[i].name+'</a>'


					if(data[i].child){
						str+=tree(data[i].child);
					}
					str+="</li>";
				}
				str += "</ul>";
				return str;
			}
			$(".lists").html(tree(data));

			let changes = document.querySelectorAll(".change");
			let oc = document.querySelectorAll(".oc");
			for (let i = 0; i < idData.length; i++) {
				for (let j = 0; j < changes.length; j++) {

					if (idData[i].id=== Number(changes[j].innerHTML)){
						oc[j].innerHTML="-";
					}
				}

			}

			let trees = document.querySelectorAll(".l_tree");
			for (let i = 0; i < trees.length; i++) {
				if (i===0){
					trees[i].style.display="block";
				}else {
					trees[i].style.display="none"
				}
			}




		})
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

// //上传图片
//
//
// let uploads = document.querySelectorAll(".uploads");
//
// for (let i = 0; i < uploads.length; i++) {
// 	uploads[i].onclick=function (){
// 		document.getElementById('file'+i+'').click();
// 	}
//
//
// }
//
// //自动上传
// for (let i = 0; i < uploads.length; i++) {
// 	document.getElementById('file'+i+'').onchange=function (){
// 		let files = document.getElementById('file'+i+'').files[0];
// 		let formdata = new FormData();
// 		formdata.append('file'+i+'', files);
//
// 		axios({
// 			method:"post",
// 			url:"uploadServlet",
// 			data:formdata,
// 			headers: {'Content-Type': 'multipart/form-data'},
// 		}).then(function (resp){
// 			document.getElementById("url"+i+"").value=resp.data;
// 		})
// 	}
// }




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
			'  <div class="img" style="display: flex">\n' +
			'    <a class="ylPic" style="cursor: pointer">\n' +
			'      <img style="width: 50px;height: 33px" src='+baseResult+' alt="图片未加载" title="点击预览图片" >\n' +
			'    </a>\n' +
			'    <div class="desc" title="点击删除图片" ></div>\n' +
			'  </div>\n' +
			'</div>'

		if (i===0){
			//删除所有的图片信息
			let res = document.getElementById("addFp").parentElement.querySelectorAll(".responsive");
			for ( let j = 0; j < res.length; j++) {
				res[j].remove();
			}
			document.getElementById("addFp").insertAdjacentHTML('afterend',pic);

		}
		else if (i===1){
			//删除所有的图片信息
			let res = document.getElementById("upFp").parentElement.querySelectorAll(".responsive");
			for ( let j = 0; j < res.length; j++) {
				res[j].remove();
			}
			document.getElementById("upFp").insertAdjacentHTML('afterend',pic);

		}

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





