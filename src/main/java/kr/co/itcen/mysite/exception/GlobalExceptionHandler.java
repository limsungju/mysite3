package kr.co.itcen.mysite.exception;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
	
	// Controller, Dao, Service에서는 기술침투 x, 시스템 외적인 부분에선 o
	// Exception이 나면 이곳에서 받는다.
	@ExceptionHandler(Exception.class)
	public void handlerException(HttpServletRequest request, HttpServletResponse response, Exception e) throws ServletException, IOException {
		
		// 1. 로깅
		
		
		// 2. 안내 페이지
		request.getRequestDispatcher("/WEB-INF/views/error/exception.jsp").forward(request, response);
	}
}
