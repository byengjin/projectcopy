package com.lyj.securitydomo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 게시글과 댓글 수를 포함하는 DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostReplyCountDTO {
    private Long postId;         // 게시글 ID
    private String title;        // 게시글 제목
    private String author;       // 작성자
    private Long replyCount;      // 댓글 수
    private boolean isVisible;
    private String thumbnailLink;  // Add this field if it's missing


    public PostReplyCountDTO(Long postId, String title, String authorUsername, Long replyCount, Boolean isVisible, String thumbnailLink) {
        this.postId = postId;
        this.title = title;
        this.author = authorUsername;
        this.replyCount = replyCount;
        this.isVisible = isVisible;// 공개 여부
        this.thumbnailLink = thumbnailLink;
    }
}
