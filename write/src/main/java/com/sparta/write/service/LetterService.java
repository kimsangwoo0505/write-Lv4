package com.sparta.write.service;

import com.sparta.write.dto.*;
import com.sparta.write.entity.Letter;
import com.sparta.write.entity.User;
import com.sparta.write.entity.UserRoleEnum;
import com.sparta.write.entity.Write;
import com.sparta.write.jwt.JwtUtil;
import com.sparta.write.repository.LetterRepository;
import com.sparta.write.repository.UserRepository;
import com.sparta.write.repository.WriteRepository;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@Service
@RequiredArgsConstructor
public class LetterService {

    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;
    private final LetterRepository letterRepository;
    private final WriteRepository writeRepository;
    private static final String ADMIN_TOKEN = "AAABnvxRVklrnYxKZ0aHgTBcXukeZygoC";

    @Transactional//등록 부분(?)
    public LetterResponseDto createLetter(Long writeId,LetterRequestDto letterRequestDto, HttpServletRequest request)
    {
        // Request에서 Token 가져오기
        String token = jwtUtil.resolveToken(request);
        Claims claims;

        // 토큰이 있는 경우에만 관심상품 추가 가능
        if (token != null) {
            if (jwtUtil.validateToken(token)) {
                // 토큰에서 사용자 정보 가져오기
                claims = jwtUtil.getUserInfoFromToken(token);
            } else {
                throw new IllegalArgumentException("Token Error");
            }

            // 토큰에서 가져온 사용자 정보를 사용하여 DB 조회
            User user = userRepository.findByUsername(claims.getSubject()).orElseThrow(
                    () -> new IllegalArgumentException("사용자가 존재하지 않습니다.")
            );

            // 1) 게시글을 조회합니다.
            Write write = writeRepository.findById(writeId)
                    .orElseThrow(() -> new NullPointerException("해당 게시글이 존재하지 않습니다."));

            Letter letter=letterRepository.save(new Letter(letterRequestDto, write,user));

//            return new LetterResponseDto(letter);
            return new LetterResponseDto(letter);

        } else {
            return null;

        }
    }

    @Transactional//수정 부분(?)
    public LetterResponseDto updateLetter(Long writeId,Long id, LetterRequestDto letterRequestDto, HttpServletRequest request) {
        // Request에서 Token 가져오기
        String token = jwtUtil.resolveToken(request);
        Claims claims;

        // 토큰이 있는 경우에만 관심상품 최저가 업데이트 가능
        if (token != null) {
            // Token 검증
            if (jwtUtil.validateToken(token)) {
                // 토큰에서 사용자 정보 가져오기
                claims = jwtUtil.getUserInfoFromToken(token);
            } else {
                throw new IllegalArgumentException("Token Error");
            }

            // 토큰에서 가져온 사용자 정보를 사용하여 DB 조회
            User user = userRepository.findByUsername(claims.getSubject()).orElseThrow(
                    () -> new IllegalArgumentException("사용자가 존재하지 않습니다.")
            );

            // 1) 게시글을 조회합니다.
            Write write = writeRepository.findById(writeId)
                    .orElseThrow(() -> new NullPointerException("해당 게시글이 존재하지 않습니다."));


//            Letter letter = letterRepository.findByIdAndUser_Username(id, user.getUsername()).orElseThrow(
//                    () -> new NullPointerException("해당 댓글이 존재하지 않습니다.")
//            );

//            letter.update(letterRequestDto);
            Letter letter;//if문 밖에서 선언하기(?)

            UserRoleEnum userRoleEnum = user.getRole();
            if (userRoleEnum == UserRoleEnum.USER){
                letter = letterRepository.findByIdAndUser_Username(id, user.getUsername()).orElseThrow(
                        () -> new NullPointerException("해당 댓글이 존재하지 않습니다.")
                );
            }else{
                letter = letterRepository.findById(id).orElseThrow(
                        () -> new NullPointerException("해당 댓글이 존재하지 않습니다.")
                );
            }

            letter.update(letterRequestDto);
            return new LetterResponseDto(letter);

        } else {
            return null;
        }
    }//jwt 구현하기 20(from ProductController)

    @Transactional//삭제 부분(?)
    public ResponseDto deleteLetter(Long writeId,Long id, HttpServletRequest request) {
        // Request에서 Token 가져오기
        String token = jwtUtil.resolveToken(request);
        Claims claims;

        // 토큰이 있는 경우에만 관심상품 최저가 업데이트 가능
        if (token != null) {
            // Token 검증
            if (jwtUtil.validateToken(token)) {
                // 토큰에서 사용자 정보 가져오기
                claims = jwtUtil.getUserInfoFromToken(token);
            } else {
                throw new IllegalArgumentException("Token Error");
            }

            // 토큰에서 가져온 사용자 정보를 사용하여 DB 조회
            User user = userRepository.findByUsername(claims.getSubject()).orElseThrow(
                    () -> new IllegalArgumentException("사용자가 존재하지 않습니다.")
            );

            // 1) 게시글을 조회합니다.
            Write write = writeRepository.findById(writeId)
                    .orElseThrow(() -> new NullPointerException("해당 게시글이 존재하지 않습니다."));


//            Letter letter = letterRepository.findByIdAndUser_Username(id, user.getUsername()).orElseThrow(
//                    () -> new NullPointerException("해당 댓글은 존재하지 않습니다.")
//            );


            Letter letter;//if문 밖에서 선언하기(?)

            UserRoleEnum userRoleEnum = user.getRole();
            if (userRoleEnum == UserRoleEnum.USER){
                letter = letterRepository.findByIdAndUser_Username(id, user.getUsername()).orElseThrow(
                        () -> new NullPointerException("해당 댓글이 존재하지 않습니다.")
                );
            }else{
                letter = letterRepository.findById(id).orElseThrow(
                        () -> new NullPointerException("해당 댓글이 존재하지 않습니다.")
                );
            }


            letterRepository.delete(letter);

            return new ResponseDto("게시글 삭제 성공", HttpStatus.OK);

        } else {
            return new ResponseDto("게시글 삭제 실패", HttpStatus.BAD_REQUEST);
        }
    }


}
