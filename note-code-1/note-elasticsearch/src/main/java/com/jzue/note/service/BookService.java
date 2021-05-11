package com.jzue.note.service;

import com.jzue.note.entity.Book;
import com.jzue.note.repository.BookRepository;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @Author: junzexue
 * @Date: 2019/7/19 上午1:13
 * @Description:
 **/
@RestController
@Slf4j
public class BookService {

    @Resource
    BookRepository bookRepository;

    @GetMapping("findById")
    public void findById(@RequestParam("id") Long id){
        bookRepository.findById(id);
    }

    @PostMapping("save")
    public void saveBook(){
        Book book=new Book();
        book.setId(1L);
        book.setBookName("xujeunze");
        Book save = bookRepository.save(book);

    }

}
