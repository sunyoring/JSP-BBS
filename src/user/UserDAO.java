package user;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class UserDAO { //DAO : 데이터베이스 접근 객체, 데이터베이스에서 정보를 불러오거나 넣을 때 사용한다.

	private Connection conn; //데이터베이스에 접근
	private PreparedStatement pstmt;
	private ResultSet rs; //정보를 담는 객체
	
	public UserDAO() { //mySQL에 접속하는 부분
		try {
			String dbURL = "jdbc:mysql://localhost:3306/BBS"; //3306포트 서버의 BBS에 접속하는 주소
			String dbID="root";
			String dbPassword="root";
			Class.forName("com.mysql.jdbc.Driver");//mysql에 접속해 줄 수 있게하는 라이브러리
			conn = DriverManager.getConnection(dbURL,dbID,dbPassword); //dbURL에 ID와Password를 이용해 접속.
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	public int login(String userID, String userPassword) { //로그인 함수
		String SQL = "SELECT userPassword FROM USER WHERE userID = ? "; //데이터 베이스에 입력할 명령어 문장 
															//USER테이블에서 해당 사용자의 비밀번호를 가져온다.
		try {
			pstmt = conn.prepareStatement(SQL); //SQL문장을 삽입.
			pstmt.setString(1, userID);
			rs = pstmt.executeQuery();
			if (rs.next()) { //존재하는 아이디가 있다면?
				if(rs.getString(1).equals(userPassword)) { //비밀번호가 맞다면 ?
					return 1; //로그인 성공
				}
				else
					return 0; //비밀번호 불일치
			}
			return -1;//아이디없음
			
		}catch (Exception e){
			e.printStackTrace();
		}
		return -2; //데이터베이스 오류를 의미
	}
}
