package com.tripPlanner.project.domain.board;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/planner")
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;

    // BoardService에서 공개된 플래너와 썸네일 이미지를 함께 가져와서 DTO 리스트로 반환
    @GetMapping("/board")
    public ResponseEntity<Page<BoardDto>> getPlanners(
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "8") int size,
            @RequestParam(value = "area", required = false) String area,
            @RequestParam(value = "myCourses", required = false) Boolean myCourses,
            @RequestParam(value = "userId", required = false) String userId) {

        Pageable pageable = PageRequest.of(page - 1, size);
        Page<BoardDto> planners = boardService.getPlanners(userId, area, myCourses, pageable);
        return ResponseEntity.ok(planners);
    }

    // 메인페이지에서 요청하는 플래너 리스트
    @GetMapping("/list")
    public Page<BoardDto> getPlannerList(@PageableDefault(size = 10) Pageable pageable) {
        return boardService.getPlannersForBoard(pageable);
    }

    @GetMapping("/plans/total")
    public ResponseEntity<Integer> getTotalPlans(){
        int totalPlans = boardService.getTotalPlans();
        return ResponseEntity.ok(totalPlans);
    }

}
