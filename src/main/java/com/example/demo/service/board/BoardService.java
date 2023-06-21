package com.example.demo.service.board;

import com.example.demo.dto.board.*;
import com.example.demo.entity.board.Board;
import com.example.demo.entity.board.BoardFile;
import com.example.demo.entity.board.Comment;
import com.example.demo.entity.board.CommentHeart;
import com.example.demo.repository.boardFile.BoardFileRepository;
import com.example.demo.repository.board.BoardRepository;
import com.example.demo.repository.commentHeart.CommentHeartRepository;
import com.example.demo.repository.comment.CommentRepository;
import com.example.demo.utils.FileUtil;
import com.example.demo.utils.UploadFile;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
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
    private final CommentHeartRepository commentHeartRepository;
    private final FileUtil fileStore;

    public Page<BoardDto> getBoardList(BoardListSearchCond searchCond, PageRequest pageRequest) {
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
        for (UploadFile uploadFile : uploadFiles) {
            BoardFile boardFile = BoardFile.builder().uploadFileName(uploadFile.getUploadFileName()).storeFileName(uploadFile.getStoreFileName()).board(board).build();
            createBoardFile(boardFile);
        }
        return boardRepository.save(board).getId();
    }

    public BoardViewForm getBoardByIdx(Long boardIdx) {
        Board board = boardRepository.findById(boardIdx).get();
        return BoardViewForm.builder().id(board.getId()).name(board.getName()).registerDate(board.getRegisterDate()).content(board.getContent()).writer(board.getWriter()).is_top(board.getIs_top() == "Y" ? true:false).build();
    }

    @Transactional(readOnly = false)
    public Board updateBoard(Long boardIdx, BoardUpdateForm form) {
        Board board = boardRepository.save(Board.builder().name(form.getName()).content(form.getContent()).writer("홍길동").is_top(form.getIs_top() == true ? "Y":"N").id(boardIdx).build());
        return board;
    }

    @Transactional(readOnly = false)
    public void deleteBoardById(Long boardIdx) {
        boardRepository.deleteById(boardIdx);
    }

    @Transactional(readOnly = false)
    public void boardViewCount(Long boardIdx) {
        Board board = boardRepository.findById(boardIdx).get();
        board.viewCountUp(board);
    }

    public void createBoardFile(BoardFile boardFile) {
        boardFileRepository.save(boardFile);
    }

    public List<UploadFile> getBoardFileIdx(Long boardIdx) {
        return boardFileRepository.findByboardId(boardIdx).stream().map(o->new UploadFile(o)).collect(Collectors.toList());
    }

    public BoardFile getBoardFile(Long fileIdx) {
        return boardFileRepository.findById(fileIdx).get();
    }

    public String getBoardFileName(Long fileId) {
        return boardFileRepository.findById(fileId).get().getStoreFileName();
    }

    @Transactional(readOnly = false)
    public void deleteFileBoard(Long fileId) {
        boardFileRepository.deleteById(fileId);
    }

    public List<CommentDto> getComment(Long boardIdx) {
        List<CommentDto> comments = commentRepository.findByBoardId(boardIdx);
        return comments;
    }

    public CommentDto getCommentByIdx(Long commentIdx) {
        Comment comment = commentRepository.findById(commentIdx).get();
        return mapper.map(comment, CommentDto.class);
    }

    @Transactional(readOnly = false)
    public void saveComment(Long boardIdx, CommentDto commentDto) {
        Board board = boardRepository.findById(boardIdx).get();
        Comment comment = Comment.builder().id(commentDto.getId()).content(commentDto.getContent()).writer(commentDto.getWriter()).board(board).build();
        commentRepository.save(comment);
    }

    @Transactional(readOnly = false)
    public void deleteCommentByIdx(Long commentIdx) {
        commentRepository.deleteById(commentIdx);
    }

    public void createCommentHeart(Long commentIdx, Long commentHeartId, String isHeart) {
        if (isHeart.equals("N")) {
            commentHeartRepository.deleteByCommentId(commentIdx,"홍길동");
        } else {
            Comment comment = commentRepository.findById(commentIdx).get();
            commentHeartRepository.save(CommentHeart.builder().id(commentHeartId).isLike(isHeart).writer("홍길동").comment(comment).build());
        }
    }

    public void deleteBoardFile(List<MultipartFile> fileList, List<Long> fileId, List<String> fileName) {
        for (int i = 0; i < fileList.size(); i++) {
            if ((!fileList.get(i).isEmpty() || fileName.get(i).isEmpty()) && fileId.get(i)!=null) {
                String storeFileName = getBoardFileName(fileId.get(i));
                boolean isDelete = fileStore.deleteFile(storeFileName);
                if (isDelete) {
                    deleteFileBoard(fileId.get(i));
                }
            }
        }
    }

    public void createBoardFile(BoardUpdateForm form, Board board) throws IOException {
        List<UploadFile> uploadFiles = fileStore.storeFiles(form.getFile());

        for (UploadFile uploadFile : uploadFiles) {
            BoardFile boardFile = BoardFile.builder().uploadFileName(uploadFile.getUploadFileName()).storeFileName(uploadFile.getStoreFileName()).board(board).build();
            createBoardFile(boardFile);
        }
    }

    @Transactional(readOnly = false)
    public void updateFixedIsTop(ArrayList<BoardDto> tableData) {
        for (BoardDto board : tableData) {
            Board findBoard = boardRepository.findById(board.getId()).get();
            findBoard.changeIsTop(board.getIs_top());
        }
    }
}
