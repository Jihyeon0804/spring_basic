package com.beyond.basic.b2_board.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class LogController {
    // logback 객체 만드는 방법 1.
    // LoggerFactory.getLogger(클래스명.class);
//    private static final Logger logger = LoggerFactory.getLogger(LogController.class);

    @GetMapping("/log/test")
    public String logTest() {
        // 기존의 system.out.print 는 spring 에서는 잘 사용되지 않음
        // 이유 1) 성능 떨어짐
        // 이유 2) 로그 분류 작업 불가
        System.out.println("hello world");

        // 가장 많이 사용되는 로그 라이브러리 : logback
        // 로그 레벨 (프로젝트 차원의 설정) : trace < debug < info < error
        /*
        logger.trace("$$$$$$$$$ trace 로그");
        logger.debug("$$$$$$$$$ debug 로그");
        logger.info("@@@@@@@@@@ info 로그");
        logger.error("%%%%%%% error 로그");
        try {
            logger.info("에러 테스트 시작");
            throw new RuntimeException("에러 테스트");
        } catch (RuntimeException e) {
            // + 보다 , 로 두 메시지를 이어서 출력하는 것이 성능에 더 좋음
            logger.error("#####################에러 메시지 : ", e);
        }
         */

//        @Slf4j 선언 시, log 라는 변수로 logback 객체 사용 가능
        log.info("slfj4 테스트");

        try {
            log.info("에러 테스트 시작");
            throw new RuntimeException("에러 테스트");
        } catch (RuntimeException e) {
            // + 보다 , 로 두 메시지를 이어서 출력하는 것이 성능에 더 좋음
            log.error("#####################에러 메시지 : ", e);
//            e.printStackTrace();                  // 성능 문제로 logback 쓰는 것이 더 좋음
        }



        return "ok";
    }
}
