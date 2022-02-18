package ru.sharipov.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.sharipov.dto.SaveUserRequestDto;
import ru.sharipov.exception.ResourceNotFoundException;
import ru.sharipov.model.User;
import ru.sharipov.service.DbUserService;

@RestController
@RequestMapping("/api/user")
public class UserApiController {
    private final DbUserService dbUserService;

    public UserApiController(DbUserService dbUserService) {
        this.dbUserService = dbUserService;
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<?> handleException() {
        return ResponseEntity.notFound().build();
    }

    @GetMapping("{id}")
    public User findById(@PathVariable("id") Long id) {
        return dbUserService.findById(id).orElse(null);
    }

    @PostMapping
    public User save(@RequestBody SaveUserRequestDto requestDto) {
        return dbUserService.save(new User(requestDto.getName(), requestDto.getLogin(), requestDto.getPassword()));
    }

}
