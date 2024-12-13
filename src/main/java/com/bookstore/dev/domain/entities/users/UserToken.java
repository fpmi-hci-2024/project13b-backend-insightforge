package com.bookstore.dev.domain.entities.users;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

import static jakarta.persistence.FetchType.EAGER;
import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@Table(name = "users_tokens")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserToken {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(updatable = false, nullable = false)
    private long id;

    @Column(updatable = false, nullable = false, unique = true)
    private String access;

    @Column(updatable = false, nullable = false, unique = true)
    private String refresh;

    @OneToOne(fetch = EAGER)
    @JoinColumn(name = "users_id", nullable = false)
    private User user;
    @Column(name = "created_date_time", columnDefinition = "TIMESTAMP", nullable = false)
    @CreatedDate
    private LocalDateTime createdDateTime;
    @Column(name = "expires_date_time", columnDefinition = "TIMESTAMP", nullable = false)
    private LocalDateTime expiresDateTime;
}
