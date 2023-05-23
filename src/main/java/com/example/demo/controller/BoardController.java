package com.example.demo.controller;

import com.example.demo.dto.BoardDto;
import com.example.demo.entity.Board;
import com.example.demo.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;
    private static int cnt = 0;

    @GetMapping("/board")
    public String boardList(Model model, @RequestParam(value = "page", required = false, defaultValue = "1") int page) {
        cnt+=1;
        if(cnt==1)
            boardService.createBoard();
        PageRequest pageRequest = PageRequest.of(page-1, 10, Sort.by(Sort.Direction.DESC, "id"));
        Page<Board> pageList = boardService.getBoardList(pageRequest);
        Page<BoardDto> boardList = pageList.map(BoardDto::new);
        model.addAttribute("boardList",boardList);
        model.addAttribute("count",pageList.getTotalElements());
        return "board/lists";
    }

//    @GetMapping("/board/excel")
//    public String boardExcel(Model model) {
//        return "board/excel";
//    }
}
