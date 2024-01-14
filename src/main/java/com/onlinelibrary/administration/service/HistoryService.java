package com.onlinelibrary.administration.service;

import com.onlinelibrary.administration.entity.History;
import com.onlinelibrary.administration.repository.HistoryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class HistoryService {
    private static final Logger logger = LoggerFactory.getLogger(HistoryService.class);
    private final HistoryRepository historyRepository;

    public HistoryService(HistoryRepository historyRepository) {
        this.historyRepository = historyRepository;
    }

    public void createHistory(History history) {
        logger.debug("Creating History");
        historyRepository.save(history);
    }
}
