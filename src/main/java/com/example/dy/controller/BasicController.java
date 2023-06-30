//package com.example.dy.controller;
//
//import com.fasterxml.jackson.core.JsonProcessingException;
//import lombok.Data;
//import org.springframework.stereotype.Component;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//
//import javax.servlet.http.HttpSession;
//import java.time.LocalDateTime;
//import java.util.*;
//
//@Controller
//@RequestMapping("/basic")
//public class BasicController {
//
//    @GetMapping("text-basic")
//    public String textBasic(Model model) {
//        model.addAttribute("data", "Hello <b>Spring</b>");
//        return "basic/text-basic";
//    }
//
//    @GetMapping("/variable")
//    public String variable(Model model) {
//        User userA = new User("userA", 10);
//        User userB = new User("userB", 20);
//
//        //User userA = new User("userA", 10);: "userA"라는 이름과 10이라는 나이를 가진 User 객체를 생성하고
//        // 이를 userA라는 변수에 할당합니다.
//
//        //User userB = new User("userB", 20);: "userB"라는 이름과 20이라는 나이를 가진 User 객체를 생성하고
//        // 이를 userB라는 변수에 할당합니다.
//
//        List<User> list = new ArrayList<>(Arrays.asList(userA, userB));
//
//        // userA와 userB를 원소로 하는 리스트를 생성합니다. 이 리스트는 User 객체를 원소로 가질 수 있으며,
//        // ArrayList로 구현되어 있습니다. ArrayList는 가변 크기의 배열로, 원소의 추가, 삭제가 자유롭습니다.
//
//        Map<String, User> map = new HashMap<>();
//
//        //String을 키로, User를 값으로 가지는 맵을 생성합니다. 이 맵은 HashMap으로 구현되어 있습니다.
//        //HashMap은 키-값 쌍을 저장하는 데 사용되는 자료구조로, 키를 통해 빠르게 값을 조회할 수 있습니다.
//
//
//        map.put("userA", userA);
//        map.put("userB", userB);
//
//        //map.put("userB", userB);: 위에서 생성한 userA와 userB 객체를 각각 "userA"와 "userB"라는 키와 함께 맵에 저장합니다.
//
//
//        model.addAttribute("user", userA);
//        model.addAttribute("users", list);
//        model.addAttribute("userMap", map);
//
//        return "basic/variable";
//    }
//
//    @Data
//    static class User{
//        private String username;
//        private int age;
//
//        public User(String username, int age) {
//            this.username = username;
//            this.age = age;
//        }
//    }
//
////    @Data: 이것은 Lombok 라이브러리의 어노테이션입니다.
////    이 어노테이션을 클래스에 사용하면,
////    Lombok이 이 클래스에 대해 getter와 setter 메서드, equals(), hashCode(), toString() 등의 메서드를 자동으로 생성해줍니다.
////    이렇게 함으로써 클래스를 간결하게 유지하면서 필요한 메서드를 제공할 수 있습니다.
////    이 User 클래스는 username과 age 필드에 대해 getter와 setter가 자동으로 생성됩니다.
//
//
//
////    static class User: 이는 User라는 이름의 중첩 클래스를 정의하는 부분입니다.
////    static이 붙어 있으므로 이 클래스는 바깥 클래스인 BasicController의 인스턴스 없이도 생성할 수 있습니다.
//
//
////
////    private String username;, private int age;: 이는 User 클래스가 가지는 두 개의 필드를 정의하는 부분입니다.
////    하나는 문자열 타입의 username이고, 다른 하나는 정수 타입의 age입니다.
////    private 키워드가 붙어 있으므로 이 필드들은 User 클래스 외부에서는 직접 접근할 수 없습니다.
////    대신 getter와 setter를 통해 접근할 수 있습니다.
//
//
////
////    public User(String username, int age): 이는 User 클래스의 생성자를 정의하는 부분입니다.
////    이 생성자는 username과 age 두 개의 파라미터를 받아서 User 객체를 생성합니다.
////    생성자 내부에서는 this.username = username;, this.age = age;라는 코드를 통해
////    User 객체의 username과 age 필드를 초기화합니다. this는 현재 생성되는 User 객체를 가리키는 참조입니다.
//
//
//
//    @GetMapping("/basic-objects")
//    public String basicObject(HttpSession httpSession) {
//
//        // 이 메서드는 HttpSession 객체를 파라미터로 받아 세션에 데이터를 저장하고 뷰 이름을 반환합니다.
//        // HttpSession은 클라이언트와 서버 간에 상태 정보를 유지하기 위해 사용되는 인터페이스입니다.
//
//        httpSession.setAttribute("sessionData", "Hello Session");
//
//        // 세션에 "sessionData"라는 이름으로 "Hello Session"이라는 값을 저장합니다.
//        // 이 값은 같은 사용자의 다른 요청에서도 사용할 수 있습니다.
//
//
//        return "basic/basic-objects";
//    }
//
//    @Component("helloBean")
//
//    //이 어노테이션은 Spring에게 HelloBean 클래스를 빈으로 등록하라는 지시를 합니다.
//
//    static class HelloBean{
//        //HelloBean이라는 이름의 중첩 클래스를 정의하는 부분입니다.
//        // 이 클래스는 hello 메서드를 가지고 있습니다.
//        public String hello(String data) {
//            return "Hello " + data;
//        }
//    }
//
//    @GetMapping("/date")
//    public String date(Model model) {
//        model.addAttribute("localDateTime", LocalDateTime.now());
//        return "basic/date";
//    }
//
//    @GetMapping("/link")
//    public String link(Model model) {
//        model.addAttribute("param1", "data1");
//        model.addAttribute("param2", "data2");
//        return "basic/link";
//    }
//    @GetMapping("/literal")
//    public String literal(Model model) {
//        model.addAttribute("data", "Spring!");
//        return "basic/literal";
//    }
//
//    @GetMapping("/operation")
//    public String operation(Model model) {
//        model.addAttribute("nullData", null);
//        model.addAttribute("data", "Spring!");
//
//        return "basic/operation";
//    }
//
//    @GetMapping("/attribute")
//    public String attribute() {
//        return "basic/attribute";
//    }
//
//    @GetMapping("/each")
//    public String each(Model model) {
//        addUsers(model);
//        return "basic/each";
//    }
//
//    private void addUsers(Model model) {
//        List<User> users = Arrays.asList(new User("userA", 10),
//                new User("userB", 20),
//                new User("userC", 30));
//
//        model.addAttribute("users", users);
//    }
//
//    @GetMapping("/condition")
//    public String condition(Model model) {
//        addUsers(model);
//        return "basic/condition";
//    }
//    //위에서 작성했던 메서드니 중복에 유의하자
//    private void addUser(Model model) {
//        List<User> users = Arrays.asList(new User("userA", 10),
//                new User("userB", 20),
//                new User("userC", 30));
//
//        model.addAttribute("users", users);
//    }
//
//    @GetMapping("/comments")
//    public String comments(Model model) {
//        model.addAttribute("data", "Spring!");
//        return "basic/comments";
//    }
//    @GetMapping("/block")
//    public String block(Model model) {
//        addUsers(model);
//        return "basic/block";
//    }
//
//    @GetMapping("/javascript")
//    public String javascript(Model model) throws JsonProcessingException {
//        model.addAttribute("user", new User("userA", 10));
//        addUsers(model);
//
//        return "basic/javascript";
//    }
//
//
//
//}