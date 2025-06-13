package com.ecommerce.backend.controller;

import com.ecommerce.backend.model.ContactMessage;
import com.ecommerce.backend.repository.ContactMessageRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/contact")
public class ContactController {

    private final ContactMessageRepository repository;

    public ContactController(ContactMessageRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public ResponseEntity<List<ContactMessage>> getAllMessages() {
        return ResponseEntity.ok(repository.findAll());
    }

    @PostMapping
    public ResponseEntity<String> saveMessage(@RequestBody ContactMessage message) {
        repository.save(message);
        return ResponseEntity.ok("Mesaj başarıyla alındı!");
    }
}
