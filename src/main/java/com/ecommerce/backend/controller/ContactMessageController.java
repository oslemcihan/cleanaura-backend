package com.ecommerce.backend.controller;

import com.ecommerce.backend.model.ContactMessage;
import com.ecommerce.backend.repository.ContactMessageRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/messages")
public class ContactMessageController {

    private final ContactMessageRepository messageRepository;

    public ContactMessageController(ContactMessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<ContactMessage>> getAll() {
        return ResponseEntity.ok(messageRepository.findAll());
    }
}
