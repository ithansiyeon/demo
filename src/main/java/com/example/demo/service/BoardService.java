package com.example.demo.service;

import com.example.demo.dto.BoardDto;
import com.example.demo.dto.BoardEditForm;
import com.example.demo.dto.BoardUpdateForm;
import com.example.demo.entity.Board;
import com.example.demo.entity.BoardFile;
import com.example.demo.repository.BoardFileRepository;
import com.example.demo.repository.BoardRepository;
import com.example.demo.utils.UploadFile;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BoardService {
    private final EntityManager em;
    private final BoardRepository boardRepository;
    private final ModelMapper mapper;
    private final BoardFileRepository boardFileRepository;

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
    public Board updateBoard(Long itemId, BoardUpdateForm form) {
        Board board = boardRepository.save(Board.builder().name(form.getName()).writer(form.getWriter()).content(form.getContent()).is_top(form.getIs_top()).id(itemId).build());
        return board;
    }

    @Transactional(readOnly = false)
    public void deleteBoard(Long itemId) {
        boardRepository.deleteById(itemId);
    }

    @Transactional(readOnly = false)
    public void boardViewCount(Long itemId) {
        Board board = boardRepository.findById(itemId).get();
        board.viewCountUp(board);
    }

    public void createBoardFile(BoardFile boardFile) {
        boardFileRepository.save(boardFile);
    }

    public List<UploadFile> getBoardFileIdx(Long itemId) {
        return boardFileRepository.findByBoardId(itemId).stream().map(o->new UploadFile(o)).collect(Collectors.toList());
    }

    public void deleteFileBoard(String s) {
        boardFileRepository.deleteByStoreFileName(s);
    }
}
