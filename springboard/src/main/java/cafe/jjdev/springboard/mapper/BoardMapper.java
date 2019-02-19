package cafe.jjdev.springboard.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import cafe.jjdev.springboard.vo.Board;
@Mapper
public interface BoardMapper {
	Board selectBoard(int boardNo);
	
	List<Board> selectBoardList(Map<String, Integer> map);
	
	int selectBoardCount();
	
	int insertBoard(Board board);
	
	int deleteBoard(Board board);
	
	int updateBoard(Board board);
	
	int deleteBoardForm(Board board);
}