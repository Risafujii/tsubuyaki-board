package com.example.tsubuyaki.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public interface PostRepository extends JpaRepository<PostEntity, Long> {

    List<PostEntity> findTop50ByDeletedAtIsNullOrderByCreatedAtDescIdDesc();

    List<PostEntity> findTop50ByDeletedAtIsNullAndBodyContainingOrderByCreatedAtDescIdDesc(String keyword);

    List<PostEntity> findByDeletedAtIsNullAndBodyContainingIgnoreCase(String keyword, Pageable pageable);

    List<PostEntity> findByDeletedAtIsNullAndAuthorContainingIgnoreCase(String keyword, Pageable pageable);

    default List<PostEntity> searchActiveByKeyword(String keyword, Pageable pageable) {
        return Stream.concat(
                        findByDeletedAtIsNullAndBodyContainingIgnoreCase(keyword, pageable).stream(),
                        findByDeletedAtIsNullAndAuthorContainingIgnoreCase(keyword, pageable).stream())
                .collect(Collectors.toMap(
                        PostEntity::getId,
                        Function.identity(),
                        (first, second) -> first,
                        LinkedHashMap::new))
                .values()
                .stream()
                .sorted(Comparator
                        .comparing(PostEntity::getCreatedAt)
                        .reversed()
                        .thenComparing(Comparator.comparing(PostEntity::getId).reversed()))
                .limit(pageable.getPageSize())
                .toList();
    }

    List<PostEntity> findTop50ByDeletedAtIsNullAndTagsNameOrderByCreatedAtDescIdDesc(String tagName);

    Optional<PostEntity> findByIdAndDeletedAtIsNull(Long id);
}
