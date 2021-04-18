package bbs;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class BbsDAO { // DAO : 데이터베이스 접근 객체, 데이터베이스에서 정보를 불러오거나 넣을 때 사용한다.

	private Connection conn; // 데이터베이스에 접근
	private ResultSet rs; // 정보를 담는 객체

	public BbsDAO() { // mySQL에 접속하는 부분
		try {
			String dbURL = "jdbc:mysql://localhost:3306/BBS"; // 3306포트 서버의 BBS에 접속하는 주소
			String dbID = "root";
			String dbPassword = "root";
			Class.forName("com.mysql.jdbc.Driver");// mysql에 접속해 줄 수 있게하는 라이브러리
			conn = DriverManager.getConnection(dbURL, dbID, dbPassword); // dbURL에 ID와Password를 이용해 접속.
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String getDate() { // 현재 시간을 가져오는 함수
		String SQL = "SELECT NOW()";// 현재 시간을 가져오는 mySQL문장
		try {
			PreparedStatement pstmt = conn.prepareStatement(SQL); // SQL문장으로 실행
			rs = pstmt.executeQuery(); // 실행결과를 가져옴.
			if (rs.next()) { // 결과가 있다면 현재의 날짜를 반환해줌.
				return rs.getString(1);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";// 데이터베이스 오류
	}

	public int getNext() { // bbsID를 구하는 함수
		String SQL = "SELECT bbsID FROM BBS ORDER BY bbsID DESC";// 가장 마지막에 쓰인 번호를 가져온 후 +1을 해서 현재bbsID에 값을 부여
		try {
			PreparedStatement pstmt = conn.prepareStatement(SQL); // SQL문장으로 실행
			rs = pstmt.executeQuery(); // 실행결과를 가져옴.
			if (rs.next()) { // 결과가 있다면 현재의 날짜를 반환해줌.
				return rs.getInt(1) + 1;
			}
			return 1; // 첫번째 게시물인 경우
		} catch (Exception e) {
			e.printStackTrace();
		}
		return -1;// 데이터베이스 오류
	}

	public int write(String bbsTitle, String userID, String bbsContent) { // 글쓰기 함수
		String SQL = "INSERT INTO BBS VALUES (?,?,?,?,?,?)";// BBS테이블에 6개의 인자를 들어갈 수 있게 함.
		// INSERT는 성공한 경우의 0이상의 결과를 반환하고 실패한 경우 -1을 반환한다.
		try {
			PreparedStatement pstmt = conn.prepareStatement(SQL); // SQL문장으로 실행
			pstmt.setInt(1, getNext());
			pstmt.setString(2, bbsTitle);
			pstmt.setString(3, userID);
			pstmt.setString(4, getDate());
			pstmt.setString(5, bbsContent);
			pstmt.setInt(6, 1);
			return pstmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return -1;// 데이터베이스 오류
	}

	public ArrayList<Bbs> getList(int pageNumber) {
		String SQL = "SELECT * FROM BBS WHERE bbsID < ? AND bbsAvailable = 1 ORDER BY bbsID DESC LIMIT 10";
		// 특정 숫자보다 작을 때 삭제가 되지않은 게시글 10개만 가져온다.
		ArrayList<Bbs> list = new ArrayList<Bbs>();
		try {
			PreparedStatement pstmt = conn.prepareStatement(SQL); // SQL문장으로 실행
			pstmt.setInt(1, getNext() - (pageNumber - 1) * 10);
			rs = pstmt.executeQuery(); // 실행결과를 가져옴.
			while (rs.next()) {
				Bbs bbs = new Bbs();
				bbs.setBbsID(rs.getInt(1));
				bbs.setBbsTitle(rs.getString(2));
				bbs.setUserID(rs.getString(3));
				bbs.setBbsDate(rs.getString(4));
				bbs.setBbsContent(rs.getString(5));
				bbs.setBbsAvailable(rs.getInt(1));
				list.add(bbs);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	public boolean nextPage(int pageNumber) {
		String SQL = "SELECT * FROM BBS WHERE bbsID < ? AND bbsAvailable = 1";
		// 특정 숫자보다 작을 때 삭제가 되지않은 게시글 10개만 가져온다. try {
		try {
			PreparedStatement pstmt = conn.prepareStatement(SQL); // SQL문장으로 실행
			pstmt.setInt(1, getNext() - (pageNumber - 1) * 10);
			rs = pstmt.executeQuery(); // 실행결과를 가져옴.
			if (rs.next()) {
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	public Bbs getBbs(int bbsID) {
		String SQL = "SELECT * FROM BBS WHERE bbsID = ? ";
		// 특정 숫자보다 작을 때 삭제가 되지않은 게시글 10개만 가져온다. try {
		try {
			PreparedStatement pstmt = conn.prepareStatement(SQL); // SQL문장으로 실행
			pstmt.setInt(1, bbsID);
			rs = pstmt.executeQuery(); // 실행결과를 가져옴.
			if (rs.next()) {
				Bbs bbs = new Bbs();
				bbs.setBbsID(rs.getInt(1));
				bbs.setBbsTitle(rs.getString(2));
				bbs.setUserID(rs.getString(3));
				bbs.setBbsDate(rs.getString(4));
				bbs.setBbsContent(rs.getString(5));
				bbs.setBbsAvailable(rs.getInt(1));
				return bbs;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
