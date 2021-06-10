package com.bezkoder.springjwt.controllers;

import com.bezkoder.springjwt.exception.ResourceNotFoundException;
import com.bezkoder.springjwt.models.Item;
import com.bezkoder.springjwt.repository.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/v1")
public class ItemController {
    @Autowired
    private ItemRepository itemRepository;

    @GetMapping("/items")
    @PreAuthorize("hasRole('USER')  or hasRole('ADMIN')")
    public List<Item> getAllClass() {
        return itemRepository.findAll();
    }

    @GetMapping("/items/{itemId}")
    @PreAuthorize("hasRole('USER')  or hasRole('ADMIN')")
    public Optional<Item> getItemById(@PathVariable int itemId) {
        Optional<Item> result = itemRepository.findById(itemId);
        return result;
    }

    @GetMapping("/items/name")
    @PreAuthorize("hasRole('USER')  or hasRole('ADMIN')")
    public List<Item> getItemByName(String name) {
        List<Item> result = itemRepository.findByName(name);
        return result;
    }

    @PostMapping("/items")
    @PreAuthorize("hasRole('USER')  or hasRole('ADMIN')")
    public Item createItem(@RequestBody Item item) {
        return itemRepository.save(item);
    }

    @PutMapping("/items/{itemId}")
    // @PreAuthorize("hasRole('USER')  or hasRole('ADMIN')")
    public Item updateItem(@PathVariable int itemId, @RequestBody(required = false) Item itemRequest) {
        return itemRepository.findById(itemId).map(itemNew -> {
            itemNew.setName(itemRequest.getName());
            itemNew.setType(itemRequest.getType());
            itemNew.setPrice(itemRequest.getPrice());
            itemNew.setStatus(itemRequest.getStatus());

            return itemRepository.save(itemNew);
        }).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        //}).orElseThrow(() -> new ResourceNotFoundException("ItemId " + itemId + " not found"));
    }


    @DeleteMapping("/items/{itemId}")
    @PreAuthorize("hasRole('USER')  or hasRole('ADMIN')")
    public ResponseEntity<?> deleteItem(@PathVariable int itemId) {
        return itemRepository.findById(itemId).map(itemDelete -> {
            itemRepository.delete(itemDelete);
            return ResponseEntity.ok().build();
        }).orElseThrow(() -> new ResourceNotFoundException("ItemId " + itemId + " not found"));
    }
}
