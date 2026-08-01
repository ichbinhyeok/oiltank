# OilTankRoute 최종 사업 판정 — 2026-08-01

## 결론

도메인은 버리지 않는다. 다만 `buried oil tank` 철거 글을 계속 늘리는 사업도 하지 않는다.

사이트의 중심을 **Residential Heating Oil Tank Planner**로 바꾼다.

사용자 흐름:

`탱크 식별 → 크기·용량 확인 → 게이지·잔량 계산 → 노후·누유 위험 판정 → 교체·제거 계획 → 적합 업체 연결`

일 100 organic clicks는 이제 수학적으로 불가능한 목표는 아니지만, 기본 시나리오도 아니다. utility + replacement + removal + leak 전체가 성공했을 때의 상단 목표다.

월 $1,000은 일 100클릭 그 자체보다 **월 40개 승인 리드**로 관리한다.

## 확인된 검색 수요

### 기존 상업 검색군

- raw 월간 검색량 범위: 7,300~73,000
- 로그 중간 시나리오: 약 23,122
- 핵심 검색군: replacement, installation, basement/above-ground removal, leak/cleanup, tank sweep

### 새로 발견한 utility 검색군

Google Ads Keyword Planner 미국 기준 50개를 검증했다.

- 1k~10k: 6개
- 100~1k: 24개
- 10~100: 17개
- 측정 불가: 3개
- raw 범위: 월 8,570~85,700
- 로그 중간 시나리오: 약 27,100

1k~10k 검색어:

- heating oil tank
- 275 gallon oil tank
- 275 gallon heating oil tank
- oil tank sizes
- oil tank dimensions
- oil tank gauge

단, 이 검색어들은 의미가 많이 겹친다. 중복과 검색결과 내 무클릭을 제외하면 utility의 실질 organic-click pool은 대략 월 7,300~14,100으로 본다.

## 일 100클릭 판정

상업 + utility의 raw 중간값은 월 50,222 searches다. 그러나 검색어 중복, 광고, 무클릭, 순위 차이를 빼야 한다.

- 중간 수요에서 모든 핵심군이 평균 3위: 약 61 clicks/day
- 높은 수요에서 평균 3위: 약 194 clicks/day
- 높은 수요에서 평균 4위: 약 80 clicks/day

따라서 일 100은 **높은 수요가 실제로 존재하고 여러 핵심군에서 상위 3위권을 얻을 때** 가능하다. 현재 상태에서 예측값으로 쓰면 안 되고, 성공 상단 목표로만 둔다.

## 제품 구조

50개 검색어를 50개 글로 만들지 않는다. 한 데이터 모델을 공유하는 5~6개 task URL로 만든다.

1. `/heating-oil-tank/`
   - 제품 허브와 탱크 식별 시작점
2. `/heating-oil-tank-sizes-dimensions/`
   - sizes와 dimensions를 한 URL에서 소유
3. `/275-gallon-oil-tank/`
   - 275-gallon 관련 두 head term과 모델별 고유 규격
4. `/oil-tank-gauge-calculator/`
   - 게이지, 잔량, 안전 주입량, 사용자가 입력한 소비량 기준 잔여 일수
5. `/oil-tank-capacity-calculator/`
   - 형상과 치수로 용량 범위 추정
6. `/oil-tank-replacement-planner/`
   - 앞 단계 입력값을 이어받아 수리·모니터링·교체·제거 판단 및 견적 준비

상업 페이지는 별도로 basement removal, replacement, leak/cleanup의 실제 의도를 담당한다. 같은 표와 FAQ를 여러 URL에 복사하지 않는다.

## 수익 모델

Utility 방문자는 대부분 당장 철거가 필요하지 않다. 따라서 utility만으로 월 40개를 기대하지 않는다.

보수적 base 모델:

| 유입원 | 월 방문 | 승인 전환율 | 승인 리드 |
|---|---:|---:|---:|
| Utility 도구 | 2,000 | 0.55% | 11 |
| Removal/leak money pages | 1,000 | 2.0% | 20 |
| 검사관·중개인 등 추천 | 150 | 6.0% | 9 |
| 합계 | 3,150 | — | 40 |

40 approved leads × $25 = $1,000/month.

이 계산은 검증 전 가설이지 실적이 아니다. 특히 추천 유입은 SEO 외 별도 확보가 필요하다.

## 리드 품질 장치

모든 방문자에게 철거 견적 CTA를 보여주지 않는다.

- 정상 게이지·연료 부족: 정보만 제공
- 20년 이상, 녹, 불안정, 실내 스며듦: inspection/replacement CTA
- 냄새, 젖은 토양, 확인된 누유: leak/remediation CTA와 공식 안전 안내
- 주택 매매 중 underground/unknown tank: sweep/removal evaluation CTA

결과를 보기 위해 이메일을 강제하지 않는다. 위험 또는 거래 의도가 확인된 결과에서만 업체 연결을 제안한다.

## 90일 제한 실험

- v1 개발 상한: 40시간
- 먼저 만드는 것: identifier/sizes, gauge calculator, risk/replacement routing
- 대규모 도시 페이지와 얇은 키워드 글 금지
- 이미지 인식, 실시간 날씨 소비예측, 방대한 모델 DB는 v1 제외

검증 순서:

1. 검색 노출과 순위가 생기는가
2. 사용자가 실제로 도구를 시작하고 완료하는가
3. 위험·교체 의도 사용자가 CTA를 누르는가
4. 제출이 플랫폼에서 승인되는가

## 중단 기준

- asset당 10,000 impressions 후 organic CTR <2.5%: 제목·의도 1회 수정; 추가 30일에도 실패하면 확장 중단
- 1,000 visits 후 tool start <20% 또는 completion <55%: UX 1회 수정; 다음 500 visits에도 실패하면 도구 실패
- 2,000 utility visits 후 submit <0.8% 또는 approved/visit <0.35%: utility 신규 개발 중단
- 첫 40 submissions 이후 approval <50%: qualification 수정 후에도 개선되지 않으면 해당 플랫폼 routing 중단
- 6개월 후 utility approved leads <8/month: 유지보수만
- 6개월 후 <4/month: 정적 표로 축소
- 전체 180일 후 approved leads <40/month: $1,000 사업 가설 실패

## 최종 의사결정

**진행한다. 단, 블로그 확장이 아니라 한 개의 유틸리티 제품과 세 개의 상업 경로를 검증하는 제한 실험으로 진행한다.**

사이트가 살 수 있는 이유는 큰 철거 키워드 때문이 아니라, `275 gallon / sizes / dimensions / gauge`라는 반복 사용형 검색 수요와 `replacement / removal / leak`이라는 수익 의도를 한 세션에서 연결할 수 있기 때문이다.

첫 성공 기준은 일 100이 아니다. 다음 세 단계다.

1. utility 핵심군 월 10,000 impressions + 500 clicks
2. 전체 organic 1,000 clicks/month
3. approved leads 8 → 20 → 40/month

이 증거가 쌓인 뒤에만 일 100을 현실적인 forecast로 승격한다.

