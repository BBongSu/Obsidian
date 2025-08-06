---
sticker: emoji//270f-fe0f
---
``` js title="[select2] DB Data select2 attr에 추가"

/*
	bpp_por_user(임직원관리 테이블)에서 id는 seq값, text는 직원명 전제로 진행.
	그 외 데이터(이이디, 전화번호, 이메일 등)는 data-info 별명으로 attr에 추가.
*/

selectListApi(
	"/portal/por/user/selectListBppPorUser", // Controller명
	{}, // 전달할 jsonData 
	function(data){
		
		/*
			List<Map<String,Object>>로 data return
			변수명 : rows
		*/
		
		let options = data.rows.map(item => ({
			id: item.ppuSeq,
			text: item.ppuNm,
			ppuId: item.ppuId,
			ppuPhoneNo: item.ppuPhoneNo,
			ppuEml: item.ppuEml,
			ppuStat: item.ppuStat,
			ppoNm: item.ppoNm,
			ppgNtrpNm: item.ppgNtrpNm,
			ppuPstn: item.ppuPstn,
			ppuRegDt: item.ppuRegDt
		}));
		
		$("#[select2Id]").empty();

		options.forEach(option => {
			let newOption = new Option(option.text, option.id, false, false);
			$(newOption).attr("data-info",JSON.stringify(option));
			$("#[select2Id]").append(newOption);
		});
		
		$("#[select2Id]").val(null).trigger('change');
	}
);
```
``` js title="[select2] Button Click data-info 값 출력"

$("#[btnId]").on("click", function(){
	const selectedOption = $("#[select2Id] option:selected");
	const dataInfo = JSON.parse(selectedOption.attr('data-info'));

	console.log("dataInfo : ", dataInfo);
});

```
``` js title="[i18next] 다국어 지원 기능 플러그인"

/* 사이트 */
https://www.i18next.com/

```
