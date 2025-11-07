package com.example.controller;
import com.example.entity.Account;
import com.example.entity.Message;
import com.example.service.AccountService;
import com.example.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@RestController
public class SocialMediaController {

    @Autowired
    private AccountService accountService;

    @Autowired
    private MessageService messageService;

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody Account account) {
        if (account.getUsername() == null || account.getUsername().isBlank()
                || account.getPassword() == null || account.getPassword().length() < 4) {
            return ResponseEntity.badRequest().build();
        }
        if (accountService.getAccountByUsername(account.getUsername()) != null) {
            return ResponseEntity.status(409).build(); // use 409 Conflict instead of 400
        }
        Account created = accountService.createAccount(account);
        return ResponseEntity.ok(created);}
    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody Account account) {
        Account valid = accountService.validateLogin(account.getUsername(), account.getPassword());
        if (valid == null) {
            return ResponseEntity.status(401).build();}
        return ResponseEntity.ok(valid);}
    @PostMapping("/messages")
    public ResponseEntity<?> createMessage(@RequestBody Message message) {
        if (message.getMessageText() == null || message.getMessageText().isBlank()
                || message.getMessageText().length() > 255) {
            return ResponseEntity.badRequest().build();
        }
        Optional<Account> userOpt = accountService.getAccountById(message.getPostedBy());
        if (userOpt.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        Message created = messageService.createMessage(message);
        return ResponseEntity.ok(created);
    }
    @GetMapping("/messages")
    public ResponseEntity<List<Message>> getAllMessages() {
        return ResponseEntity.ok(messageService.getAllMessages());
    }
    @GetMapping("/messages/{messageId}")
    public ResponseEntity<?> getMessageById(@PathVariable int messageId) {
        Optional<Message> found = messageService.getMessageById(messageId);
        if (found.isPresent()) {
            return ResponseEntity.ok(found.get());
        } else {
            return ResponseEntity.ok().body(null);}
        }
    @DeleteMapping("/messages/{messageId}")
    public ResponseEntity<?> deleteMessage(@PathVariable int messageId) {
        boolean deleted = messageService.deleteMessage(messageId);
        if (deleted) {
            return ResponseEntity.ok(1);}
        return ResponseEntity.ok().body(null); }
    @PatchMapping("/messages/{messageId}")
    public ResponseEntity<?> updateMessage(@PathVariable int messageId, @RequestBody Message message) {
        if (message.getMessageText() == null || message.getMessageText().isBlank()
                || message.getMessageText().length() > 255) {
            return ResponseEntity.badRequest().build();}
        Message updated = messageService.updateMessage(messageId, message.getMessageText());
        if (updated == null) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(1);}
    @GetMapping("/accounts/{accountId}/messages")
    public ResponseEntity<List<Message>> getMessagesByUser(@PathVariable int accountId) {
        return ResponseEntity.ok(messageService.getMessagesByUser(accountId));}
}
