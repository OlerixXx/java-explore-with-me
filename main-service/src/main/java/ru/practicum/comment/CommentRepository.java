package ru.practicum.comment;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.comment.model.Comment;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findAllByEventId(Long eventId, Pageable page);

    @Query(value = "SELECT EXISTS(SELECT 1 FROM comments WHERE id = :commentId AND commentator_id = :userId)", nativeQuery = true)
    boolean ownerMatchesUserId(Long userId, Long commentId);
}