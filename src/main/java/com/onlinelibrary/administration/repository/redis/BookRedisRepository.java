package com.onlinelibrary.administration.repository.redis;

import com.onlinelibrary.administration.entity.Book;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRedisRepository extends CrudRepository<Book, Long> {
}
