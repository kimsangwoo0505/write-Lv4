package com.sparta.write.controller;

import com.sparta.write.dto.*;
import com.sparta.write.entity.Letter;
import com.sparta.write.service.LetterService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class LetterController {

    private final LetterService letterService;

    @PostMapping("/write/{writeId}/letter")//등록부분
    public  LetterResponseDto createLetter(@PathVariable Long writeId,@RequestBody LetterRequestDto letterRequestDto, HttpServletRequest request)
    {
        LetterResponseDto letter = letterService.createLetter(writeId, letterRequestDto, request);
        return letter;
    }



    @PutMapping("/write/{writeId}/letter/{id}")//수정
    public LetterResponseDto updateLetter(@PathVariable Long writeId,@PathVariable Long id, @RequestBody LetterRequestDto letterRequestDto, HttpServletRequest request){
        return letterService.updateLetter(writeId,id, letterRequestDto, request);//필요한 값을 넣어줌(id, requestDto)
    }


    @DeleteMapping("/write/{writeId}/letter/{id}")//삭제
    public ResponseDto deleteLetter(@PathVariable Long writeId,@PathVariable Long id, HttpServletRequest request){
//        boolean result = writeService.deleteWrite(id, request);
//        letterService.deleteLetter(writeId,id, request);
        return letterService.deleteLetter(writeId,id, request);
    }

}
