package com.example.demo.service;

import com.example.demo.dto.BoardDto;
import com.example.demo.dto.BoardEditForm;
import com.example.demo.dto.BoardUpdateForm;
import com.example.demo.entity.Board;
import com.example.demo.repository.BoardRepository;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BoardService {
    private final EntityManager em;
    private final BoardRepository boardRepository;
    private final ModelMapper mapper;

    public Page<BoardDto> getBoardList(Pageable pageRequest) {
        return boardRepository.findBoardCustom(pageRequest).map(BoardDto::new);
    }

    @Transactional(readOnly = false)
    public void createBoard() {
        for(int i=0;i<110;i++) {
            Board board = Board.builder().name("board"+ (i + 1)).writer("test"+ (i + 1)).content("aaaaaa").build();
            boardRepository.save(board);
        }
    }

    public void mergeBoard(Board board) {
        Board findBoard = boardRepository.findByName(board.getName());
//        if(findBoard == null) {
//            em.persist(board);
//            boardRepository.save()
//        } else {
//            em.merge(board);
//        }
        boardRepository.save(board);
    }

    public List<BoardDto> getBoardList() {
        return boardRepository.findAll().stream().map(BoardDto::new).collect(Collectors.toList());
    }

    @Transactional(readOnly = false)
    public Long insertBoard(Board board) {
        boardRepository.save(board);
        return board.getId();
    }

    public BoardEditForm getBoardIdx(Long idx) {
        Board board = boardRepository.findById(idx).get();
        //mapper를 통해서도 값 넣어 줄 수는 있음
        return mapper.map(board, BoardEditForm.class);
    }

    @Transactional(readOnly = false)
    public Long updateBoard(Long itemId, BoardUpdateForm form) {
        Board board = boardRepository.save(Board.builder().name(form.getName()).writer(form.getWriter()).content(form.getContent()).id(itemId).build());
        return board.getId();
    }

    public void deleteSummernoteFile(Long idx, BoardUpdateForm form) {
        Board originalBoard = boardRepository.findById(idx).get();
        Pattern pattern = Pattern.compile("<img[^>]*src=[\"']?([^>\"']+)[\"']?[^>]*>");
        Matcher matcher = pattern.matcher(originalBoard.getContent());
        List<String> originalImagePaths = new ArrayList<>();
        List<String> updatedImagePaths = new ArrayList<>();

        // 매칭된 모든 결과를 가져와서 각각의 리스트에 추가
        while (matcher.find()) {
            String src = matcher.group(1);
            originalImagePaths.add(src);
        }

        matcher = pattern.matcher(form.getContent());
        while (matcher.find()) {
            String src = matcher.group(1);
            updatedImagePaths.add(src);
        }

        for (String originalImagePath : originalImagePaths) {
            String[] paths = originalImagePath.split("/");
            String imageName = paths[paths.length - 1];
            if (!updatedImagePaths.contains(originalImagePath)) {
                File imageFile = new File("/Users/siyeon/Desktop/summernote_image/" + imageName);
                // 파일이 존재하는지 확인
                if (imageFile.exists()) {
                    System.out.println("File exists: " + originalImagePath);
                    // 파일 삭제
                    if (imageFile.delete()) {
                        System.out.println("File deleted successfully: " + originalImagePath);
                    } else {
                        System.out.println("Failed to delete the file: " + originalImagePath);
                    }
                } else {
                    System.out.println("File does not exist: " + originalImagePath);
                }
            }
        }
    }
}
