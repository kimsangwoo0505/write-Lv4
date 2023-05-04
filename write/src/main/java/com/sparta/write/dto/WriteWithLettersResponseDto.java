package com.sparta.write.dto;

import lombok.Getter;

import java.util.List;

@Getter
public class WriteWithLettersResponseDto {
    private WriteResponseDto write;
    private List<LetterResponseDto> letters;

    public WriteWithLettersResponseDto(WriteResponseDto write, List<LetterResponseDto> letters) {
        this.write = write;
        this.letters = letters;
    }

}
