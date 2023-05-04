package com.sparta.write.dto;


import com.sparta.write.entity.Letter;
import com.sparta.write.entity.Write;
import lombok.Getter;

@Getter
public class LetterResponseDto {

    private Long id;
    private String content;
//    private WriteResponseDto write; // 게시글 정보를 포함하는 필드 추가

    public LetterResponseDto(Letter letter) {
        this.id = letter.getId();
        this.content = letter.getContent();
//        this.write = new WriteResponseDto(letter.getWrite());
    }

}
