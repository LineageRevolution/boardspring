package cafe.jjdev.springboard.service;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import cafe.jjdev.springboard.mapper.BoardMapper;
import cafe.jjdev.springboard.vo.Board;
import cafe.jjdev.springboard.vo.BoardRequest;
import cafe.jjdev.springboard.vo.Boardfile;

@Service
@Transactional
public class BoardService {
	@Autowired
	private BoardMapper boardMapper;
	//수정을 위해서 특정 board 조회하는 메서드
	public Board getBoard(int boardNo) { 
		return boardMapper.selectBoard(boardNo);
	}
	//보드리스트의 갯수 가져오는 메서드
	public int getBoardCount() {
		return boardMapper.selectBoardCount();
	}
	//전체 리스트가져오는 메서드
	public Map<String, Object> selectBoardList(int currentPage){//
		// 1 paging code 작성
		final int Row_Per_Page = 10; //한 페이지에 넣어줄 행의 갯수를 설정 final로 선언하기 때문에 변경 불가
		
		int startNum = (currentPage-1)*Row_Per_Page;
		
		Map<String, Integer> map = new HashMap<String, Integer>();
		map.put("currentPage", startNum); //attribute처럼 "current"변수를 선언한다
		map.put("rowPerPage", Row_Per_Page);
		
		//2
		int boardCount = boardMapper.selectBoardCount();
		int lastPage = (int)(Math.ceil(boardCount/Row_Per_Page)); //boardCount의 개수를 Row_Per_Page로 나눈값을 lastpage변수에 담는다
		
				
		Map<String, Object> returnMap = new HashMap<String, Object>();
		
		returnMap.put("list", boardMapper.selectBoardList(map));
		returnMap.put("boardCount", boardCount);	
		returnMap.put("lastPage", lastPage);
		returnMap.put("currentPage", currentPage);
		
		return returnMap;
	}
	//보드 입력메서드
	public void addBoard(BoardRequest boardRequest, String path) {
		//1
		Board board = new Board();
		board.setBoardTitle(boardRequest.getBoardTitle());
		board.setBoardPw(boardRequest.getBoardPw());
		board.setBoardUser(boardRequest.getBoardUser());
		board.setBoardContent(boardRequest.getBoardContent());
		System.out.println("addBoard메서드 실행"+board);
		
		boardMapper.insertBoard(board);
		
		//2
		
		List<MultipartFile> files = boardRequest.getFiles();
		for(MultipartFile f : files) {
			// f-> boardfile
			Boardfile boardfile= new Boardfile();
			boardfile.setBoardNo(board.getBoardNo());
			boardfile.setFileSize(f.getSize());
			boardfile.setFileType(f.getContentType());
			
			String originalFilename = f.getOriginalFilename();
			int i = originalFilename.lastIndexOf(".");
			
			String ext = originalFilename.substring(i+1);
			boardfile.setFileExt(ext);
			String fileName = UUID.randomUUID().toString();
			boardfile.setFileName(fileName);	
			
			boardMapper.insertBoardFile(boardfile);
			// 전체작업이 롤백되면 파일삭제작업 직접해야함
			// 3  파일저장
			try {
				f.transferTo(new File(path+"/"+fileName+"."+ext));
			} catch (IllegalStateException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}		
	}
	//보드 삭제메서드
	public int removeBoard(Board board) {
		return boardMapper.deleteBoard(board);
	}
	//보드 수정액션 메서드
	public int modifyBoard(Board board) {
		return boardMapper.updateBoard(board);
	}
}