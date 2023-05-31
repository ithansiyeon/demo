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

    public void deleteSummernoteFile(Long idx) {
        Board board = boardRepository.findById(idx).get();
        Pattern pattern = Pattern.compile("<img[^>]*src=[\"']?([^>\"']+)[\"']?[^>]*>");
        Matcher matcher = pattern.matcher(board.getContent());
        System.out.println("matcher.find() = " + matcher.find());
        System.out.println("matcher.group(0) = " + matcher.group(0));
//        Matcher matcher = pattern.matcher(board.getContent());
        System.out.println("matcher.group(0) = " + matcher.group(1));
    }
}
