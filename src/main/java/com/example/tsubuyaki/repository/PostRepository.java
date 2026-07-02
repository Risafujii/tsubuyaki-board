package com.example.tsubuyaki.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<PostEntity, Long> {

    List<PostEntity> findTop50ByDeletedAtIsNullOrderByCreatedAtDescIdDesc();

    List<PostEntity> findTop50ByDeletedAtIsNullAndBodyContainingOrderByCreatedAtDescIdDesc(String keyword);

    List<PostEntity> findByDeletedAtIsNullAndBodyContainingIgnoreCaseOrDeletedAtIsNullAndAuthorContainingIgnoreCase(
            String bodyKeyword,
            String authorKeyword,
            Pageable pageable);

    List<PostEntity> findTop50ByDeletedAtIsNullAndTagsNameOrderByCreatedAtDescIdDesc(String tagName);

    Optional<PostEntity> findByIdAndDeletedAtIsNull(Long id);
}
