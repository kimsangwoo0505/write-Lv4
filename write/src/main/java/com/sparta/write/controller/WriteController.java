package com.sparta.write.controller;

import com.sparta.write.dto.*;
import com.sparta.write.entity.Write;
import com.sparta.write.security.UserDetailsImpl;
import com.sparta.write.service.LetterService;
import com.sparta.write.service.WriteService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;


@RestController
@RequiredArgsConstructor
public class WriteController {

    private final WriteService writeService;
    private final LetterService letterService;



    @GetMapping("/")//메인 페이지 반환
    public ModelAndView home() {
        return new ModelAndView("index");
    }


    @PostMapping("/api/write")//등록 부분(?)
    public WriteResponseDto createWrite(@RequestBody WriteRequestDto requestDto,  @AuthenticationPrincipal UserDetailsImpl userDetails){

        return writeService.createWrite(requestDto, userDetails.getUser());
    }



    @GetMapping("/api/write")//전체 조회//주소가 똑같아도 방식(get,post)달라서 주소 같아도 됨(?)
    public List<WriteWithLettersResponseDto> getWrites(){
        return writeService.getWrites();
    }




    @GetMapping("/api/write/{id}")//특정 게시물 조회
    public WriteResponseDto getWrite(@PathVariable Long id) {
        return writeService.getWrite(id);
    }




    // 회원이 등록한 게시글 내 모든 댓글 조회
    @GetMapping("/api/write/{writeId}/letter")
    public WriteWithLettersResponseDto getLetterInWrite(
            @PathVariable Long writeId,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {


        return writeService.getLetterInWrite(
                writeId,
                userDetails.getUser()
        );
    }

    @PutMapping("/api/write/{id}")//수정
    public WriteResponseDto updateWrite(@PathVariable Long id,@RequestBody WriteRequestDto requestDto,@AuthenticationPrincipal UserDetailsImpl userDetails){
        return writeService.update(id, requestDto, userDetails.getUser());//필요한 값을 넣어줌(id, requestDto)
    }

    @DeleteMapping("/api/write/{id}")//삭제
    public ResponseDto deleteWrite(@PathVariable Long id, @AuthenticationPrincipal UserDetailsImpl userDetails){
//        boolean result = writeService.deleteWrite(id, request);
//        writeService.deleteWrite(id, request);
        return writeService.deleteWrite(id, userDetails.getUser());
    }

}
