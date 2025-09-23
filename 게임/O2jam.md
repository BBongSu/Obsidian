---
sticker: emoji//1f3b9
---
## ⚠️ 문제


> **json 파일을 bms 파일로 변환**

- 8-kamui-shd.json → 8-kamui-shd.bms (050_ordeal_5k.bms 소스 참고)

---
## 🚀 예시

``` javascript title="BMS"

#00101:01 

/*
	#001 - 마디 번호 (오투잼은 001~299)
	01 - 키음번호
	: - 구분자
	01 - 문자열 길이 (2개씩 1묶음 => 각 1/(묶음 개수) 간격으로 위치 지정)
*/

```
``` javascript title="JSON (ez2dj)"

"headerData": {
	"tpm": 192,			// 프리빗 세팅
	"bpm": 210,			// 곡 BPM
	"trackCount": 64,		// trackList name,notes의 묶음 개수
	"duration": 123.42800903320312,	// 곡 시간 길이
	"endTick": 20736		// position 최대값
}

"soundList": [
	{
		"position" : 192,	// ?
		"id" : "d0",		// 키음을 등록한 순서
		"vol" : 127,		// ?
		"pan" : 64,		// ?
		"length" : 6		// ?
	}
]

"trackList": [	
	{
		"name": "",
		"notes": [
			{
				"position" : 192,	// ?
				"id" : 1,		// soundList 배열의 인덱스를 가리킴
				"vol" : 115,	// ?
				"pan" : 64,		// ?
				"length" : 6		// ?
			}
		]			
	}
]

```

---

## 🗒️ 참고 내용

> **참고**

- 그림1.png - BMS 예시 코드
- 그림2.png - BMS 예시 코드 결과 이미지 (노트툴)
- 그림3.png - BMS 문자열 설명 이미지
- 36진수로 구성

> **URL**

- https://github.com/ben-rnd/EZ2Tools/tree/master/ez2ChartConverter ez2dj→bms 컨버트 소스정보
- https://hitkey.nekokan.dyndns.info/cmds.htm 븜스 노트툴(bmse) 개발자 네코칸의 사이트
- https://github.com/MsrLab-org/osu2bms osu→bms 컨버트 소스정보
- https://github.com/mashimycota/om2bms?tab=readme-ov-file osu→bms 컨버트 소스정보
- https://github.com/Estrol/O2Game/blob/master/Game/src/Data/bms.hpp bms 소스정보