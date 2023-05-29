package com.example.demo.controller;

import com.example.demo.dto.BoardDto;
import com.example.demo.entity.Board;
import com.example.demo.service.BoardService;
import com.example.demo.utils.ExcelUtil;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.FilenameUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.demo.utils.ExcelUtil.*;

@Controller
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;
    private final ExcelUtil excelUtil = new ExcelUtil();

    private static int cnt = 0;

    @GetMapping("/board")
    public String boardList(Model model, @RequestParam(value = "page", required = false, defaultValue = "1") int page) {
        cnt+=1;
        if(cnt==1)
            boardService.createBoard();
        PageRequest pageRequest = PageRequest.of(page-1, 10, Sort.by(Sort.Direction.DESC, "id"));
        Page<Board> pageList = boardService.getBoardList(pageRequest);
        Page<BoardDto> boardList = pageList.map(BoardDto::new);
        model.addAttribute("startPage",Math.floor(boardList.getNumber() / boardList.getSize()) * boardList.getSize() + 1);
        model.addAttribute("boardList",boardList);
        model.addAttribute("count",pageList.getTotalElements());
        return "board/lists";
    }

    @GetMapping("/board/excel")
    public String boardExcel(Model model) {
        return "board/excel";
    }

    @PostMapping("/board/excel")
    public ResponseEntity<Map<String, Object>>boardExcelUpload(@RequestParam("file")MultipartFile file) throws IOException {
        String extension = FilenameUtils.getExtension(file.getOriginalFilename()); // 3

        if (!extension.equals("xlsx") && !extension.equals("xls")) {
            throw new IOException("엑셀파일만 업로드 해주세요.");
        }

        List<Map<String, Object>> maps = readExcel(file);
        List<Board> boards = convertToVOList(maps, Board.class);

        for (Board board : boards) {
            boardService.mergeBoard(board);
        }
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/board/download")
    public void excelDownload(HttpServletResponse response) throws Exception {
        List<BoardDto> boardList = boardService.getBoardList();
        multiSheetExcelFile(boardList, BoardDto.class, response, "test");
    }

    @GetMapping("/board/add")
    public String boardAdd() {
        return "/board/add";
    }

}
