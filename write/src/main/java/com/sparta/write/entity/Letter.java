package com.sparta.write.entity;

import com.sparta.write.dto.LetterRequestDto;
import com.sparta.write.dto.WriteRequestDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
public class Letter {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private String content;

    @ManyToOne
    @JoinColumn(name = "Write_ID", nullable = false)
    private Write write;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public Letter(LetterRequestDto letterRequestDto, Write write,User user) {
        this.content = letterRequestDto.getContent();
        this.write = write;
        this.user = user;
    }

    public void update(LetterRequestDto letterRequestDto) {
        this.content = letterRequestDto.getContent();
    }



}
