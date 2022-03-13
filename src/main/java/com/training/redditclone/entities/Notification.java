package com.training.redditclone.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long notificationId;
    @Enumerated(EnumType.STRING)
    private NotificationType notificationType;
    private String notificationTitle;
    private String description;
    @ManyToOne(cascade = CascadeType.ALL)
    private User user;
    private Instant createdAt;
}
