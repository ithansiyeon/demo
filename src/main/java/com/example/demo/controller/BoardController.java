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
import java.util.Base64;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
    public String boardEdit(@PathVariable Long itemId, @Validated @ModelAttribute("item") BoardUpdateForm form, RedirectAttributes redirectAttributes) throws IOException {
        boardService.deleteSummernoteFile(itemId, form);
//        boardService.copyImageFiles(form);
        System.out.println("form.getContent() = " + form.getContent());
        Pattern imgPattern = Pattern.compile("(?i)< *[img][^\\>]*[src] *= *[\"\']{0,1}([^\"\'\\ >]*)");
        Matcher captured = imgPattern.matcher(form.getContent());
        String imgSrcPath = "";
        while(captured.find()){
            imgSrcPath = captured.group(1);  // 글 내용의 이미지들 중 첫번째 이미지만 저장
            if(!imgSrcPath.contains("/temp/summernoteImage")) {
                decoder(imgSrcPath, getFileExtensionFromBase64(imgSrcPath), "/Users/siyeon/Desktop/temp/summernote_image/aaaaaaa.jpg");
                form.setContent(form.getContent().replace(imgSrcPath, "/temp/summernoteImage/aaaaaaa.jpg"));
                System.out.println("form.getContent().replaceAll(imgSrcPath,\"/temp/summernoteImage/aaaaaaa.jpg\") = " + form.getContent().replaceAll(imgSrcPath, "/temp/summernoteImage/aaaaaaa.jpg"));
                System.out.println("imgSrcPath = " + imgSrcPath);
                System.out.println("form.getContent() = " + form.getContent());
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

    public static void decoder(String base64String, String postFix, String targetFilePath){

        try {
            String[] parts = base64String.split(",");
            String mimeType = parts[0].split(":")[1].split(";")[0]; // MIME 타입 추출
            String base64Data = parts[1]; // Base64 데이터 추출

            byte[] decodedBytes = Base64.getDecoder().decode(base64Data); // Base64 디코딩

            ByteArrayInputStream bis = new ByteArrayInputStream(decodedBytes);
            BufferedImage bufferedImage = ImageIO.read(bis);

            ImageIO.write(bufferedImage, mimeType, new File(targetFilePath)); // 파일로 저장

            System.out.println("File conversion completed.");
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