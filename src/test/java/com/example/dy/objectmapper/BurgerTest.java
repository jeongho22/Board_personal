package com.example.dy.objectmapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;



// json         -> readValue()              dto
//              <- writeValueAsString()


class BurgerTest {
    @Test
    public void 자바_객체를_JSON으로_변환() throws JsonProcessingException {
        // 준비
        ObjectMapper objectMapper = new ObjectMapper();
        List<String> ingredients = Arrays.asList("통새우 패티", "순쇠고기 패티", "토마토", "스파이시 어니언 소스"); // 재료들...
        Burger burger = new Burger("맥도날드 슈비버거", 5500, ingredients);
        // 수행
        String json = objectMapper.writeValueAsString(burger); // json 만드는 writeValueAsString()
        // 예상
        String expected = "{\"name\":\"맥도날드 슈비버거\",\"price\":5500,\"ingredients\":[\"통새우 패티\",\"순쇠고기 패티\",\"토마토\",\"스파이시 어니언 소스\"]}";
        // 검증
        assertEquals(expected, json);
        JsonNode jsonNode = objectMapper.readTree(json); // 제이슨 노드를 가지고 예쁘게 출력가능
        System.out.println(jsonNode.toPrettyString());
    }

//    @Test
//    public void JSON을_자바_객체로_변환() throws JsonProcessingException {
//        // 준비
//        ObjectMapper objectMapper = new ObjectMapper();
//        String json = "{\"name\":\"맥도날드 슈비버거\",\"price\":5500,\"ingredients\":[\"통새우 패티\",\"순쇠고기 패티\",\"토마토\",\"스파이시 어니언 소스\"]}";
//        // 수행
//        Burger burger = objectMapper.readValue(json, Burger.class); // 객체로 만드는 readValue()
//        // 예상
//        List<String> ingredients = Arrays.asList("통새우 패티", "순쇠고기 패티", "토마토", "스파이시 어니언 소스");
//        Burger expected = new Burger("맥도날드 슈비버거", 5500, ingredients);
//        // 검증
//        assertEquals(expected.toString(), burger.toString());
//        JsonNode jsonNode = objectMapper.readTree(json);
//        System.out.println(jsonNode.toPrettyString()); // json 출력
//        System.out.println(burger.toString());         // 자바 출력
//    }

    @Test
    public void JSON을_자바_객체로_변환() throws JsonProcessingException {
        // 준비
        ObjectMapper objectMapper = new ObjectMapper();
        /*
        {
          "name" : "맥도날드 슈비버거",
          "price" : 5500,
          "ingredients" : [ "통새우 패티", "순쇠고기 패티", "토마토", "스파이시 어니언 소스" ]
        }
        */
        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("name", "맥도날드 슈비버거");
        objectNode.put("price", 5500);

//        "ingredients" : [ "통새우 패티", "순쇠고기 패티", "토마토", "스파이시 어니언 소스" ]
        ArrayNode arrayNode = objectMapper.createArrayNode();
        arrayNode.add("통새우 패티");
        arrayNode.add("순쇠고기 패티");
        arrayNode.add("토마토");
        arrayNode.add("스파이시 어니언 소스");
        objectNode.set("ingredients", arrayNode); // 뒤에 노드를 넣는경우에는 put보다는 set 메서드를 쓰는것을 권장한다.
//         변환
        String json = objectNode.toString();

        // 수행
        Burger burger = objectMapper.readValue(json, Burger.class);
        // 예상
        List<String> ingredients = Arrays.asList("통새우 패티", "순쇠고기 패티", "토마토", "스파이시 어니언 소스");
        Burger expected = new Burger("맥도날드 슈비버거", 5500, ingredients);
        // 검증
        assertEquals(expected.toString(), burger.toString());
        JsonNode jsonNode = objectMapper.readTree(json);
        System.out.println(jsonNode.toPrettyString());
        System.out.println(burger.toString());
    }

}