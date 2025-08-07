---
sticker: emoji//270f-fe0f
---

``` js title="getCommaFormatter - 세 자리마다 콤마가 포함된 숫자 형식으로 변환"
function getCommaFormatter(prefixSelector) {
	const input = prefixSelector;
	if(!input) return;
	
	input.on("input",function(){
		let val = this.value.replace(/[^0-9]/g, '');
		if(val === '') return this.value = '';
		this.value = Number(val).toLocaleString();
	});
}
```


