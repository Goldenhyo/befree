package com.project.befree.controller.advice;

import com.project.befree.util.CustomJWTException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;
import java.util.NoSuchElementException;

// @RestController 전용 예외처리 담당하는 어노테이션 (클래스레벨에 부착)
@RestControllerAdvice
public class CustomControllerAdvice {
    // 발생할 수 잇는 예외들에 대한 처리는 메서드로 구현
    // 이때 각 메서드가 예외발생시 실행되는 핸들러라는 뜻으로 @ExceptionHandler 어노테이션 부착
    @ExceptionHandler(NoSuchElementException.class)
    protected ResponseEntity<?> notExist(NoSuchElementException e) {
        String msg = e.getMessage();
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("msg", msg));
        // 예외 발생시 에러 메세지를 리턴해줄건데, RestController 이므로 메세지를 JSON 형태로 리턴해줘야함
        // 객체로 메세지 형성해서 body 안에 넣어야 하는데, 메세지용 클래스를 굳이 생성하지 않고, key=value 형태의 Map 이용
        // (Map.of("msg", msg, "msg2", msg2 ...)
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<?> handleIllegalArgsException(MethodArgumentNotValidException e) {
        String msg = e.getMessage();
        return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(Map.of("msg", msg));
    }

    // 우리가 만든 예외 발생시 실행될 핸들러 추가
    @ExceptionHandler(CustomJWTException.class)
    protected ResponseEntity<?> handleJWTException(CustomJWTException e) {
        String msg = e.getMessage();
        return ResponseEntity.ok().body(Map.of("error", msg));
    }
}
