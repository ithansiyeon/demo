package com.example.demo.controller;

import com.example.demo.dto.BoardDto;
import com.example.demo.dto.BoardEditForm;
import com.example.demo.dto.BoardSaveForm;
import com.example.demo.dto.BoardUpdateForm;
import com.example.demo.entity.Board;
import com.example.demo.service.BoardService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Value;
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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.example.demo.utils.CalendarUtil.getCurrentSimpleDate;
import static com.example.demo.utils.ExcelUtil.*;

@Controller
@RequiredArgsConstructor
@Slf4j
public class BoardController {

    private final BoardService boardService;

    @Value("${image.storage.tempDir}")
    private String imageStorageTempDir;

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
    public String boardEdit(@PathVariable Long itemId, Model model, HttpServletRequest req, HttpServletResponse res) {
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
                oldCookie.setPath("/board/edit");
                oldCookie.setMaxAge(60 * 60 * 24);
                res.addCookie(oldCookie);
            }
        } else {
            //요청에 Cookie가 있고 글을 조회한 기록이 있다면 pass 없다면 Cookie에 [게시글ID] 붙이기
            boardService.boardViewCount(itemId);
            Cookie newCookie = new Cookie("boardView", "[" + itemId + "]");
            newCookie.setPath("/board/edit");
            newCookie.setMaxAge(60 * 60 * 24);
            res.addCookie(newCookie);
        }
        BoardEditForm item = boardService.getBoardIdx(itemId);
        model.addAttribute("item", item);
        return "board/edit";
    }

    @PostMapping("/board/edit/{itemId}")
    public String boardEdit(@PathVariable Long itemId, @Validated @ModelAttribute("item") BoardUpdateForm form, RedirectAttributes redirectAttributes) throws IOException {
        boardService.deleteSummernoteFile(itemId, form);
//        boardService.copyImageFiles(form);
        Pattern imgPattern = Pattern.compile("(?i)< *[img][^\\>]*[src] *= *[\"\']{0,1}([^\"\'\\ >]*)");
        Matcher captured = imgPattern.matcher(form.getContent());
        String currentSimpleDate = getCurrentSimpleDate();
        File dir = new File(imageStorageTempDir+currentSimpleDate);
        if(!dir.exists()) {
            dir.mkdirs();
        }
        while(captured.find()){
            String imgSrcPath = captured.group(1);
            String extension = getFileExtensionFromBase64(imgSrcPath);
            if(!imgSrcPath.contains("/temp/summernoteImage/")) {
                String savedFileName = UUID.randomUUID() + "." + extension;
                decoder(imgSrcPath, imageStorageTempDir+currentSimpleDate+"/"+savedFileName);
                form.setContent(form.getContent().replace(imgSrcPath, "/temp/summernoteImage/"+currentSimpleDate+"/"+savedFileName));
            }
        }
        Long idx = boardService.updateBoard(itemId, form);
        redirectAttributes.addAttribute("itemId",idx);
        return "redirect:/board/edit/{itemId}";
    }

    @GetMapping("/board/delete/{itemId}")
    public String boardDelete(@PathVariable Long itemId) {
        boardService.deleteBoard(itemId);
        return "redirect:/board";
    }

    public static void decoder(String base64String, String targetFilePath){

        try {
            String[] parts = base64String.split(",");
            String base64Data = parts[1]; // Base64 데이터 추출
            byte[] decodedBytes = Base64.getDecoder().decode(base64Data); // Base64 디코딩
            OutputStream stream = new FileOutputStream(targetFilePath);
            stream.write(decodedBytes);
            stream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getFileExtensionFromBase64(String base64) {
        int extensionStartIndex = base64.indexOf('/') + 1;
        int extensionEndIndex = base64.indexOf(';');
        if (extensionStartIndex > 0 && extensionEndIndex > extensionStartIndex) {
            return base64.substring(extensionStartIndex, extensionEndIndex);
        } else {
            return null;
        }
    }


}