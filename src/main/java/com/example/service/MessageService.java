package com.example.service;
import com.example.entity.Message;
import com.example.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class MessageService {
    @Autowired
    private MessageRepository messageRepository;
    public Message createMessage(Message message) {
        if (message == null || message.getMessageText() == null || message.getMessageText().trim().isEmpty()) {
            return null;}
        if (message.getMessageText().length() > 255) {
            return null;}
        return messageRepository.save(message); }
    public List<Message> getAllMessages() {
        return messageRepository.findAll();}
    public Optional<Message> getMessageById(Integer messageId) {
        return messageRepository.findById(messageId);}
    public List<Message> getMessagesByUser(Integer postedBy) {
        return messageRepository.findByPostedBy(postedBy);
    }
    public Message updateMessage(Integer messageId, String newText) {
        if (newText == null || newText.trim().isEmpty() || newText.length() > 255) {
            return null;
        }
        Optional<Message> existingMessage = messageRepository.findById(messageId);
        if (existingMessage.isPresent()) {
            Message message = existingMessage.get();
            message.setMessageText(newText);
            return messageRepository.save(message); }
        return null; }
    public boolean deleteMessage(Integer messageId) {
        if (messageRepository.existsById(messageId)) {
            messageRepository.deleteById(messageId);
            return true; }
        return false;}






}
