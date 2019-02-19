package cafe.jjdev.springboard.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import cafe.jjdev.springboard.service.BoardService;
import cafe.jjdev.springboard.vo.Board;

@Controller
public class BoardController {
	 @Autowired
	    private BoardService boardService;
	 	// Spring에서는 매개변수에 들어간 객체는 자동으로 빈객체가 생성된다 단,Annotation이 붙어있어야함
	    // ex) boardAdd(Board board) --> Board board = new Board();
	 
	    // 리스트 요청
	 	//@RequestMapping(value="/boardList", method = RequestMethod.GET)
	    @GetMapping(value="/boardList")
	    public String boardList(Model model, @RequestParam(value="currentPage", required=false, defaultValue="1") int currentPage) {
		
	    	Map<String, Object> returnMap = boardService.selectBoardList(currentPage);
	    	model.addAttribute("map", returnMap); //request객체 대신 model객체에 값 셋팅
	    	model.addAttribute("currentPage", currentPage);
	        return "boardList";
	    }
	    // 입력(액션) 요청
	    @RequestMapping()
	    public String boardAdd(Board board) {
	    	boardService.addBoard(board);
	        return "redirect:/boardList"; // 글입력후 "/boardList"로 리다이렉트(재요청)
	    }
	    // 입력페이지(form) 요청
	    @RequestMapping(value="/boardAdd", method = RequestMethod.GET)
	    public String boardAdd() {
	        System.out.println("boardAdd 폼 요청");
	        return "boardAdd";
	    }
	    //삭제 액션
	    @PostMapping("boardDelete")
	    public String boardRemove(Board board) {
	    	System.out.println(board.getBoardNo()+"<--no");
	    	System.out.println(board.getBoardPw()+"<--pw");
	  	  int result = boardService.removeBoard(board);
	  	  System.out.println("쿼리문 실행 결과 --> " + result);
	  	  return "redirect:/boardList";
	    }
		
	    //삭제 폼
	    @GetMapping("boardDelete")
		public String removeBoardForm(Board board) {
			return "boardRemove";
		}
	    //수정 폼
	    @GetMapping("/boardModify")
		public String boardModify(@RequestParam(value="boardNo") int boardNo, Model model) {
			Board board = boardService.getBoard(boardNo);
			model.addAttribute("board",board);
			return "boardModify";
		}
	    //수정 액션
	    @RequestMapping("/boardModify")
	    public String boardModify(Board board) {
	    	boardService.modifyBoard(board);
			return "redirect:/boardList";
	    }
	}
