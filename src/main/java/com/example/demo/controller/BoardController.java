package com.example.demo.controller;

import com.example.demo.dto.*;
import com.example.demo.entity.Board;
import com.example.demo.entity.BoardFile;
import com.example.demo.service.BoardService;
import com.example.demo.utils.FileUtil;
import com.example.demo.utils.UploadFile;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.util.UriUtils;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.demo.utils.ExcelUtil.*;

@Controller
@RequiredArgsConstructor
@Slf4j
public class BoardController {

    private final BoardService boardService;
    private final FileUtil fileStore;

    @GetMapping("/board")
    public String boardList(Model model, @ModelAttribute BoardListSearchCond searchCond, @RequestParam(value = "page", required = false, defaultValue = "1") int page) {
        PageRequest pageRequest = PageRequest.of(page - 1, 10, Sort.by("id").descending());
        Page<BoardDto> boardList = boardService.getBoardList(searchCond, pageRequest);

        model.addAttribute("searchCond", searchCond);
        model.addAttribute("startPage", Math.floor(boardList.getNumber() / boardList.getSize()) * boardList.getSize() + 1);
        model.addAttribute("boardList", boardList);
        model.addAttribute("count", boardList.getTotalElements());
        return "board/lists";
    }

    @GetMapping("/board/excel")
    public String boardExcel(Model model) {
        return "board/excel";
    }

    @PostMapping("/board/excel")
    public ResponseEntity<Map<String, Object>> boardExcelUpload(@RequestParam("file") MultipartFile file) throws IOException {
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
    public String boardAdd(@Validated @ModelAttribute("item") BoardSaveForm form, BindingResult bindingResult, RedirectAttributes redirectAttributes) throws IOException {
        if (bindingResult.hasErrors()) {
            log.info("errors={}", bindingResult);
            return "board/add";
        }
        List<UploadFile> uploadFiles = fileStore.storeFiles(form.getFile());
        Long idx = boardService.insertBoard(uploadFiles, form);
        redirectAttributes.addAttribute("itemId", idx);
        return "redirect:/board/edit/{itemId}";
    }

    @GetMapping("/board/view/{itemId}")
    public String boardView(@PathVariable Long itemId, Model model, HttpServletRequest req, HttpServletResponse res) {
        Cookie(itemId, req, res);
        BoardViewForm item = boardService.getBoardIdx(itemId);
        List<UploadFile> uploadFileList = boardService.getBoardFileIdx(itemId);
        model.addAttribute("fileList", uploadFileList);
        model.addAttribute("item", item);
        return "board/view";
    }

    @GetMapping("/board/edit/{itemId}")
    public String boardEdit(@PathVariable Long itemId, Model model) {
        BoardViewForm item = boardService.getBoardIdx(itemId);
        List<UploadFile> uploadFileList = boardService.getBoardFileIdx(itemId);
        model.addAttribute("fileList", uploadFileList);
        model.addAttribute("item", item);
        return "board/edit";
    }

    @PostMapping("/board/edit/{itemId}")
    public String boardEdit(@PathVariable Long itemId, @Validated @ModelAttribute("item") BoardUpdateForm form, BindingResult bindingResult, RedirectAttributes redirectAttributes) throws IOException {
        if (bindingResult.hasErrors()) {
            log.info("errors={}", bindingResult);
            return "board/edit";
        }

        Board board = boardService.updateBoard(itemId, form);
        Long idx = board.getId();

        List<MultipartFile> fileList = form.getFile();
        List<Long> fileId = form.getFileId();
        List<String> fileName = form.getFileName();

        for (int i = 0; i < fileList.size(); i++) {
            if ((!fileList.get(i).isEmpty() || fileName.get(i).isEmpty()) && fileId.get(i)!=null) {
                String storeFileName = boardService.getBoardFileName(fileId.get(i));
                boolean isDelete = fileStore.deleteFile(storeFileName);
                if (isDelete) {
                    boardService.deleteFileBoard(fileId.get(i));
                }
            }
        }

        List<UploadFile> uploadFiles = fileStore.storeFiles(form.getFile());

        for (UploadFile uploadFile : uploadFiles) {
            BoardFile boardFile = BoardFile.builder().uploadFileName(uploadFile.getUploadFileName()).storeFileName(uploadFile.getStoreFileName()).board(board).build();
            boardService.createBoardFile(boardFile);
        }
        redirectAttributes.addAttribute("itemId", idx);
        return "redirect:/board/view/{itemId}";
    }

    @GetMapping("/download/{itemId}")
    public ResponseEntity<Resource> downloadAttach(@PathVariable Long itemId) throws MalformedURLException {
        BoardFile boardFile = boardService.getBoardFile(itemId);
        String storeFileName = boardFile.getStoreFileName();
        String uploadFileName = boardFile.getUploadFileName();
        UrlResource resource = new UrlResource("file:" + fileStore.getFullPath(storeFileName));

        log.info("uploadFileName={}", uploadFileName);

        String encodedUploadFileName = UriUtils.encode(uploadFileName, StandardCharsets.UTF_8);
        String contentDisposition = "attachment; filename=\"" + encodedUploadFileName + "\"";

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, contentDisposition)
                .body(resource);
    }

    @GetMapping("/board/delete/{itemId}")
    public String boardDelete(@PathVariable Long itemId) {
        boardService.deleteBoard(itemId);
        return "redirect:/board";
    }

    @GetMapping("/board/{itemId}/comments")
    public String boardComment(@PathVariable Long itemId, Model model) throws JsonProcessingException {
        List<CommentDto> comments = boardService.getComment(itemId);
//        ResponseEntity<String>
//        ObjectMapper objectMapper = new ObjectMapper();
//        objectMapper.registerModule(new JavaTimeModule());
//        String json = objectMapper.writeValueAsString(comments);
//        return ResponseEntity.ok(json);
        model.addAttribute("commentList",comments);
        return "board/view :: #commentTable";
    }

    @ResponseBody
    @PostMapping("/board/{itemId}/comments")
    public ResponseEntity<String> savedComment(@PathVariable Long itemId, CommentDto commentDto) {
        boardService.saveComment(itemId, commentDto);
        return new ResponseEntity<>("ok", HttpStatus.OK);
    }

    @GetMapping("/board/{itemId}/comments/{id}")
    public ResponseEntity<String> getComment(@PathVariable Long itemId, @PathVariable Long id) throws JsonProcessingException {
        CommentDto commentDto = boardService.getCommentId(id);
        ObjectMapper objectMapper = new ObjectMapper();
        return ResponseEntity.ok(objectMapper.writeValueAsString(commentDto));
    }

    /*@ResponseBody
    @PostMapping("/board/{itemId}/comments")
    public ResponseEntity<String> updateComment(@PathVariable Long itemId, CommentDto commentDto) {
        System.out.println("commentDto = " + commentDto.toString());
        boardService.updateComment(commentDto);
        return new ResponseEntity<>("ok", HttpStatus.OK);
    }*/

    /**
     * 쿠기 만드는 메소드
     *
     * @param itemId
     * @param req
     * @param res
     */

    public void Cookie(Long itemId, HttpServletRequest req, HttpServletResponse res) {
        Cookie oldCookie = null;
        Cookie[] cookies = req.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("boardView")) {
                    oldCookie = cookie;
                    break;
                }
            }
        }
        //요청에 Cookie가 없고 글을 조회한다면 [게시글ID]의 값을 추가하여 Cookie생성 (기간은 하루로 설정)
        if (oldCookie != null) {
            if (!oldCookie.getValue().contains("[" + itemId.toString() + "]")) {
                boardService.boardViewCount(itemId);
                oldCookie.setValue(oldCookie.getValue() + "_[" + itemId + "]");
                oldCookie.setPath("/board/view");
                oldCookie.setMaxAge(60 * 60 * 24);
                res.addCookie(oldCookie);
            }
        } else {
            //요청에 Cookie가 있고 글을 조회한 기록이 있다면 pass 없다면 Cookie에 [게시글ID] 붙이기
            boardService.boardViewCount(itemId);
            Cookie newCookie = new Cookie("boardView", "[" + itemId + "]");
            newCookie.setPath("/board/view");
            newCookie.setMaxAge(60 * 60 * 24);
            res.addCookie(newCookie);
        }
    }

    @GetMapping("/common/message")
    // 사용자에게 메시지를 전달하고, 페이지를 리다이렉트 한다.
    private String showMessageAndRedirect(final MessageDto params, Model model) {
        params.setMessage("hello");
        model.addAttribute("params", params);
        return "common/messageRedirect";
    }

}