package com.example.dy.Domain.constant;

import lombok.Getter;


public enum SearchType {
    TITLE("제목"),
    CONTENT("본문");

    @Getter private final String description;

    SearchType(String description) {
        this.description = description;
    }

}