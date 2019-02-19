package cafe.jjdev.springboard.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cafe.jjdev.springboard.mapper.BoardMapper;
import cafe.jjdev.springboard.vo.Board;

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
		
		int startPageNum=1;
		int lastPageNum = Row_Per_Page; //마지막 페이지
		if(currentPage> (Row_Per_Page/2)) { //Row_Per_Page를 2로 나누었을때 currentPage보다 작은경우 if문 실행
			startPageNum= currentPage -((lastPageNum/2)-1); //
			lastPageNum += (startPageNum-1);
		}
		currentPage = (currentPage-1)*Row_Per_Page;
		Map<String, Integer> map = new HashMap<String, Integer>();
		map.put("currentPage", currentPage); //attribute처럼 "current"변수를 선언한다
		map.put("rowPerPage", Row_Per_Page);
		//2
		int boardCount = boardMapper.selectBoardCount();
		int lastPage = (int)(Math.ceil(boardCount/Row_Per_Page)); //boardCount의 개수를 Row_Per_Page로 나눈값을 lastpage변수에 담는다
		
		if(currentPage >= (lastPage-4)) { //lastPage-4가 currentPage보다 작거나 같으면 if문 실행
			lastPageNum = lastPage;
		}
		
		Map<String, Object> returnMap = new HashMap<String, Object>();
		
		returnMap.put("list", boardMapper.selectBoardList(map));
		returnMap.put("boardCount", boardCount);	
		returnMap.put("lastPage", lastPage);
		returnMap.put("lastPageNum", lastPageNum);
		returnMap.put("currentPage", currentPage);
		
		return returnMap;
	}
	//보드 입력메서드
	public int addBoard(Board board) {
		return boardMapper.insertBoard(board);
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