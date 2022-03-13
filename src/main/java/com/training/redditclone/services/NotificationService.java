package com.training.redditclone.services;

import com.training.redditclone.entities.Notification;
import com.training.redditclone.entities.NotificationType;
import com.training.redditclone.entities.User;
import com.training.redditclone.exceptions.NotificationNotFoundException;
import com.training.redditclone.repositories.NotificationRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@Service
@AllArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;

    public Notification generateNotification(NotificationType notificationType, String message, User user){
        Notification notification = new Notification();
        notification.setNotificationType(notificationType);
        notification.setDescription(message);
        notification.setCreatedAt(Instant.now());
        notification.setUser(user);
        return notificationRepository.save(notification);
    }

    @Transactional(readOnly = true)
    public List<Notification> getAllNotifications(){
        return notificationRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Notification getNotification(Long id){
        return notificationRepository.findById(id)
                .orElseThrow(() -> new NotificationNotFoundException(id));
    }

    public void deleteNotification(Long id){
        notificationRepository.deleteById(id);
    }
}
