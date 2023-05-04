package com.sparta.write.repository;

import com.sparta.write.entity.Letter;
import com.sparta.write.entity.Write;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LetterRepository extends JpaRepository<Letter, Long> {

    Optional<Letter> findByIdAndUser_Username(Long id, String username);
//    Optional<Letter> findByIdAndUsername(Long id, String username);

    List<Letter> findAllByUser_IdAndWrite_Id(Long userId, Long writeId);

    List<Letter> findAllByWrite_Id(Long writeId);
    Optional<Letter> findById(Long id);
}
