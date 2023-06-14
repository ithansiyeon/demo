package com.example.demo.service;

import com.example.demo.dto.*;
import com.example.demo.entity.Board;
import com.example.demo.entity.BoardFile;
import com.example.demo.entity.Comment;
import com.example.demo.repository.BoardFileRepository;
import com.example.demo.repository.BoardRepository;
import com.example.demo.repository.CommentRepository;
import com.example.demo.utils.UploadFile;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
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
    private final CommentRepository commentRepository;

    public Page<BoardDto> getBoardList(BoardListSearchCond searchCond, Pageable pageRequest) {
        return boardRepository.findBoardCustom(searchCond,pageRequest).map(BoardDto::new);
    }

    public void mergeBoard(Board board) {
        boardRepository.save(board);
    }

    public List<BoardDto> getBoardList() {
        return boardRepository.findAll().stream().map(BoardDto::new).collect(Collectors.toList());
    }

    @Transactional(readOnly = false)
    public Long insertBoard(List<UploadFile> uploadFiles, BoardSaveForm form) {
        Board board = Board.builder().name(form.getName()).writer(form.getWriter()).content(form.getContent()).is_top(form.getIs_top() == true ? "Y" : "N").build();
        List<BoardFile> boardFiles = new ArrayList<>();
        for (UploadFile uploadFile : uploadFiles) {
            BoardFile boardFile = BoardFile.builder().uploadFileName(uploadFile.getUploadFileName()).storeFileName(uploadFile.getStoreFileName()).board(board).build();
            boardFiles.add(boardFile);
            createBoardFile(boardFile);
        }
        boardRepository.save(board);
        return board.getId();
    }

    public BoardViewForm getBoardIdx(Long idx) {
        Board board = boardRepository.findById(idx).get();
        //mapper를 통해서도 값 넣어 줄 수는 있음
//        return mapper.map(board, BoardEditForm.class);
        return BoardViewForm.builder().id(board.getId()).name(board.getName()).registerDate(board.getRegisterDate()).content(board.getContent()).writer(board.getWriter()).is_top(board.getIs_top() == "Y" ? true:false).build();
    }

    @Transactional(readOnly = false)
    public Board updateBoard(Long itemId, BoardUpdateForm form) {
        Board board = boardRepository.save(Board.builder().name(form.getName()).content(form.getContent()).writer("홍길동").is_top(form.getIs_top() == true ? "Y":"N").id(itemId).build());
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

    public BoardFile getBoardFile(Long fileId) {
        return boardFileRepository.findById(fileId).get();
    }

    public String getBoardFileName(Long fileId) {
        return boardFileRepository.findById(fileId).get().getStoreFileName();
    }

    @Transactional(readOnly = false)
    public void deleteFileBoard(Long fileId) {
        boardFileRepository.deleteById(fileId);
    }

    public List<CommentDto> getComment(Long itemId) {
        List<Comment> comments = commentRepository.findByBoardId(itemId);
        return comments.stream().map(o -> mapper.map(o, CommentDto.class)).collect(Collectors.toList());
    }

    public CommentDto getCommentId(Long id) {
        Comment comment = commentRepository.findById(id).get();
        return mapper.map(comment, CommentDto.class);
    }

    @Transactional(readOnly = false)
    public void saveComment(Long itemId, CommentDto commentDto) {
        Comment comment = null;
        Board board = boardRepository.findById(itemId).get();
        if(commentDto.getId() != null) {
            comment = Comment.builder().content(commentDto.getContent()).writer(commentDto.getWriter()).board(board).build();
        } else {
            System.out.println("aaaaaaa");
            comment = Comment.builder().id(commentDto.getId()).content(commentDto.getContent()).writer(commentDto.getWriter()).board(board).build();
        }
        commentRepository.save(comment);
    }
}
