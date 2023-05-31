package com.example.demo.controller;

import com.example.demo.dto.BoardDto;
import com.example.demo.dto.BoardEditForm;
import com.example.demo.dto.BoardSaveForm;
import com.example.demo.dto.BoardUpdateForm;
import com.example.demo.entity.Board;
import com.example.demo.service.BoardService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.demo.utils.ExcelUtil.*;

@Controller
@RequiredArgsConstructor
@Slf4j
public class BoardController {

    private final BoardService boardService;

    private static int cnt = 0;

    @GetMapping("/board")
    public String boardList(Model model, @RequestParam(value = "page", required = false, defaultValue = "1") int page) {
       /* cnt+=1;
        if(cnt==1)
            boardService.createBoard();*/
        PageRequest pageRequest = PageRequest.of(page-1, 10, Sort.by("id").descending());
        Page<BoardDto> boardList = boardService.getBoardList(pageRequest);
        model.addAttribute("startPage",Math.floor(boardList.getNumber() / boardList.getSize()) * boardList.getSize() + 1);
        model.addAttribute("boardList",boardList);
        model.addAttribute("count",boardList.getTotalElements());
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
    public String boardAdd(Model model) {
        model.addAttribute("item", new BoardSaveForm());
        return "board/add";
    }

    @PostMapping("/board/add")
    public String boardAdd(@Validated @ModelAttribute("item")BoardSaveForm form, BindingResult bindingResult, RedirectAttributes redirectAttributes) throws IllegalAccessException, InstantiationException, InvocationTargetException, NoSuchMethodException {
        if(bindingResult.hasErrors()) {
            log.info("errors={}",bindingResult);
            return "/board/add";
        }
        Board board = Board.builder().name(form.getName()).writer(form.getWriter()).content(form.getContent()).build();
        Long idx = boardService.insertBoard(board);
        redirectAttributes.addAttribute("itemId", idx);
        return "redirect:/board/edit/{itemId}";
    }

    @GetMapping("/board/edit/{itemId}")
    public String boardEdit(@PathVariable Long itemId, Model model) {
        BoardEditForm item = boardService.getBoardIdx(itemId);
        model.addAttribute("item", item);
        return "board/edit";
    }

    @PostMapping("/board/edit/{itemId}")
    public String boardEdit(@PathVariable Long itemId,  @Validated @ModelAttribute("item") BoardUpdateForm form, RedirectAttributes redirectAttributes) {
        boardService.deleteSummernoteFile(itemId);
        Long idx = boardService.updateBoard(itemId, form);
        redirectAttributes.addAttribute("itemId",idx);
        return "redirect:/board/edit/{itemId}";
    }

}