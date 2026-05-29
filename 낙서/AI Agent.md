AI Agent 도입

1. 프로젝트 기술 스택 확인

	- Backend : Spring Boot (Java)
	- Frontend : JSP
	- DB : MariaDB
	- 구조 : MVC (Controller - Service - Mapper)

2. AI Agent 도입 방식 선택

	- 방식 A : 외부 API 연동 (가장 빠름. 추천 [Bizpack Bankend (Spring Controller)] - [AI API 호출 (OpenAI / Gemini)] - [결과 반환 (JSP 화면 표출)])

		- Java에서 OpenAI/Gemini API 호출
		- pom.xml 의존성 추가
			<dependency>
		    		<groupId>com.theokanning.openai-gpt3-java</groupId>
				<artifactId>service</artifactId>
				<version>0.18.2</version>
			</dependency>
	
	- 방식 B : LangChain4j (Java Native Agent 프레임워크)
		
		- Spring Boot와 가장 잘 맞는 Java용 AI 프레임워크
		- pom.xml 의존성 추가
			<!-- pom.xml -->
			<dependency>
			    <groupId>dev.langchain4j</groupId>
			    <artifactId>langchain4j-spring-boot-starter</artifactId>
			    <version>0.35.0</version>
			</dependency>
			<dependency>
			    <groupId>dev.langchain4j</groupId>
			    <artifactId>langchain4j-open-ai</artifactId>
			    <version>0.35.0</version>
			</dependency>

3. Bizpack에 적용 가능한 구체적인 시나리오

	- 재고 이상 감지 Agent
	// BppInvStkOutService.java 에 AI 분석 추가
	@Service
	public class BppInvStkAiService {
    
    		@Inject
		private AiServices aiAgent;
    
		public String analyzeStockAlert(List<BppInvSto> stockList) {
        	// 재고 데이터를 AI에게 전달 → 이상 패턴 감지
        	String prompt = "다음 재고 현황을 분석하고 " +
                       "부족 위험 품목을 알려줘: " + stockList.toString();
        	return aiAgent.analyze(prompt);
    		}
	}

	- 지출 결의서 자동 분류 Agent
	지출 내역 입력 → AI가 유형 자동 추천 "회식비 50,000원" → AI → "운영비용 > 접대비" 추천

	- 매출/재고 리포트 자동 생성
	DB 데이터 → AI Agent → 자연어 리포트 생성 "이번 달 재고 출고가 전월 대비 15% 증가했으며..."

4. 단계별 도입 로드맵

	1단계 (1~2주)
	└── API Key 발급 (OpenAI or Google Gemini)
	└── Spring Boot에 AI API 연동 테스트
	└── 간단한 챗봇 엔드포인트 생성

	2단계 (2~4주)
	└── LangChain4j 도입
	└── DB 데이터를 AI 컨텍스트로 제공 (RAG 구성)
	└── 특정 업무 자동화 1~2개 구현

	3단계 (1~2개월)
	└── 멀티스텝 Agent 구현
	└── Tool 등록 (재고조회, 결의서생성 등 Bizpack API를 Agent 도구로 등록)
	└── UI에 AI 채팅창 추가

5. 가장 빠른 시작 방법
	
	# application.properties
	gemini.api.key=YOUR_API_KEY
	gemini.model=gemini-1.5-flash

	@RestController
	@RequestMapping("/portal/ai")
	public class AiController {
    
    	@PostMapping("/analyze")
    	public ResponseEntity<String> analyze(@RequestBody Map<String, String> req) {
        		// Gemini API 호출
			String result = geminiService.chat(req.get("prompt"));
        		return ResponseEntity.ok(result);
		}
	}

수불현황

	- 재고자산이나 물품의 입고(들어옴)와 출고(나감) 과정을 체계적으로 기록하고 관리하는 것

		- "어떤 거래처"에서 "어떤 제품/부품"이 "어떤 창고에" "언제" "몇 개" "나갔고" "들어왔다"
		- 

[입고처리] insertBppInvStoIn() 완료
  ├── bppInvStoMapper.insertBppInvStoIn()       ← 입고처리 저장
  ├── updateBppInvProAddQty()                    ← 현재재고 +
  ├── updateBppInvStkInPisibBgngDt()            ← 입고시작일 기록
  └── updateBppInvStkInPisibCmptnDt()           ← 전체완료 시 완료일 기록
                                                   ↑ 여기서 수불현황 INSERT 추가

[출고처리] insertBppInvStoOut() 완료
  ├── bppInvStoMapper.insertBppInvStoOut()       ← 출고처리 저장
  ├── updateBppInvProSubQty()                    ← 현재재고 -
  ├── updateBppInvStkOutPisobBgngDt()           ← 출고시작일 기록
  └── updateBppInvStkOutPisobCmptnDt()          ← 전체완료 시 완료일 기록
                                                   ↑ 여기서 수불현황 INSERT 추가
		