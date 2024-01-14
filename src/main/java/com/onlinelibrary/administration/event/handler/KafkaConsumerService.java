package com.onlinelibrary.administration.event.handler;


import com.onlinelibrary.administration.entity.Book;
import com.onlinelibrary.administration.event.model.BookChangeModel;
import com.onlinelibrary.administration.repository.redis.BookRedisRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaConsumerService {
    private static final Logger logger = LoggerFactory.getLogger(KafkaConsumerService.class);
    private static final String TOPIC_NAME = "bookTopic";
    private static final String BOOK_GROUP = "bookGroup";
    private final BookRedisRepository redisRepository;

    public KafkaConsumerService(BookRedisRepository redisRepository) {
        this.redisRepository = redisRepository;
    }

    @KafkaListener(topics = TOPIC_NAME, groupId = BOOK_GROUP)
    public void loggerSink(BookChangeModel bookChangeModel) {
        logger.debug("Received a message of type {}", bookChangeModel.getType());

        Book book = bookChangeModel.getBook();

        switch (bookChangeModel.getAction()) {
            case "UPDATE":
                logger.debug("Received an UPDATE event from the book service for book id {}", book.getId());
                if (redisRepository.findById(book.getId()).isPresent()) {
                    logger.debug("The book with id {} is present in Redis. Updating Redis...", book.getId());
                    redisRepository.save(book);
                } else {
                    logger.debug("The book with id {} is not present in Redis. No need to update Redis", book.getId());
                }
                break;
            case "DELETE":
                logger.debug("Received a DELETE event from the book service for book id {}", book.getId());
                if (redisRepository.findById(book.getId()).isPresent()) {
                    logger.debug("The book with id {} is present in Redis. Updating Redis...", book.getId());
                    redisRepository.deleteById(book.getId());
                } else {
                    logger.debug("The book with id {} is not present in Redis. No need to update Redis", book.getId());
                }
                break;
            default:
                logger.error("Received an UNKNOWN event from the book service of type {}", bookChangeModel.getType());
                break;
        }
    }
}
