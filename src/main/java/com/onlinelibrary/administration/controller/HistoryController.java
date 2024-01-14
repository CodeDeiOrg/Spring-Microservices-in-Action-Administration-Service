package com.onlinelibrary.administration.controller;

import com.onlinelibrary.administration.entity.History;
import com.onlinelibrary.administration.service.HistoryService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/history")
public class HistoryController {
    private final HistoryService historyService;

    public HistoryController(HistoryService historyService) {
        this.historyService = historyService;
    }

    @PostMapping(value = {"/secure"})
    public void saveHistory(@RequestBody History history) {
        historyService.createHistory(history);
    }
}
