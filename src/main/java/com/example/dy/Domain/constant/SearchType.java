package com.example.dy.Domain.constant;

import lombok.Getter;


public enum SearchType {

    ALL("전체"),
    TITLE("제목"),
    CONTENT("본문");

    @Getter private final String description;

    SearchType(String description) {
        this.description = description;
    }

}