package com.jzue.note.repository;

import com.jzue.note.entity.Book;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * @author  junzexue
 * @date  2019/7/19 上午12:51
 **/
public interface BookRepository extends ElasticsearchRepository<Book,Long> {
}
