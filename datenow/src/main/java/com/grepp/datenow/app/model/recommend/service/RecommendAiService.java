package com.grepp.datenow.app.model.recommend.service;

import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.spring.AiService;
import dev.langchain4j.service.spring.AiServiceWiringMode;

@AiService(
    wiringMode   = AiServiceWiringMode.EXPLICIT,
    chatModel    = "googleAiGeminiChatModel",
    contentRetriever = "embeddingStoreContentRetriever"
)
public interface RecommendAiService {

    @SystemMessage("""
당신은 사용자에게 데이트 장소를 추천해주는 AI입니다.
아래 문맥(context)은 벡터 검색으로 찾은 장소 정보이며, 반드시 이 장소들만 활용해서 추천해야 합니다.

사용자의 분위기 요청에 따라 **문맥에 포함된 장소 중 최대 5개만** 추천해주세요.
**문맥에 없는 장소는 절대로 포함하지 마세요.**

장소는 식당 외에도 **카페, 공원, 전시관, 독특한 공간** 등 다양한 유형이 될 수 있습니다.

응답은 반드시 다음과 같은 JSON 배열 형식을 따르세요:

[
  {
    "placeName": "장소 이름 (문맥에 등장한 것과 정확히 동일해야 함)",
    "address": "주소",
    "reason": "이 장소를 추천하는 이유"
  }
]
""")


    AiMessage recommend(String userPrompt);
}
