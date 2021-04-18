package bbs;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class BbsDAO { // DAO : �����ͺ��̽� ���� ��ü, �����ͺ��̽����� ������ �ҷ����ų� ���� �� ����Ѵ�.

	private Connection conn; // �����ͺ��̽��� ����
	private ResultSet rs; // ������ ��� ��ü

	public BbsDAO() { // mySQL�� �����ϴ� �κ�
		try {
			String dbURL = "jdbc:mysql://localhost:3306/BBS"; // 3306��Ʈ ������ BBS�� �����ϴ� �ּ�
			String dbID = "root";
			String dbPassword = "root";
			Class.forName("com.mysql.jdbc.Driver");// mysql�� ������ �� �� �ְ��ϴ� ���̺귯��
			conn = DriverManager.getConnection(dbURL, dbID, dbPassword); // dbURL�� ID��Password�� �̿��� ����.
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String getDate() { // ���� �ð��� �������� �Լ�
		String SQL = "SELECT NOW()";// ���� �ð��� �������� mySQL����
		try {
			PreparedStatement pstmt = conn.prepareStatement(SQL); // SQL�������� ����
			rs = pstmt.executeQuery(); // �������� ������.
			if (rs.next()) { // ����� �ִٸ� ������ ��¥�� ��ȯ����.
				return rs.getString(1);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";// �����ͺ��̽� ����
	}

	public int getNext() { // bbsID�� ���ϴ� �Լ�
		String SQL = "SELECT bbsID FROM BBS ORDER BY bbsID DESC";// ���� �������� ���� ��ȣ�� ������ �� +1�� �ؼ� ����bbsID�� ���� �ο�
		try {
			PreparedStatement pstmt = conn.prepareStatement(SQL); // SQL�������� ����
			rs = pstmt.executeQuery(); // �������� ������.
			if (rs.next()) { // ����� �ִٸ� ������ ��¥�� ��ȯ����.
				return rs.getInt(1) + 1;
			}
			return 1; // ù��° �Խù��� ���
		} catch (Exception e) {
			e.printStackTrace();
		}
		return -1;// �����ͺ��̽� ����
	}

	public int write(String bbsTitle, String userID, String bbsContent) { // �۾��� �Լ�
		String SQL = "INSERT INTO BBS VALUES (?,?,?,?,?,?)";// BBS���̺� 6���� ���ڸ� �� �� �ְ� ��.
		// INSERT�� ������ ����� 0�̻��� ����� ��ȯ�ϰ� ������ ��� -1�� ��ȯ�Ѵ�.
		try {
			PreparedStatement pstmt = conn.prepareStatement(SQL); // SQL�������� ����
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
		return -1;// �����ͺ��̽� ����
	}

	public ArrayList<Bbs> getList(int pageNumber) {
		String SQL = "SELECT * FROM BBS WHERE bbsID < ? AND bbsAvailable = 1 ORDER BY bbsID DESC LIMIT 10";
		// Ư�� ���ں��� ���� �� ������ �������� �Խñ� 10���� �����´�.
		ArrayList<Bbs> list = new ArrayList<Bbs>();
		try {
			PreparedStatement pstmt = conn.prepareStatement(SQL); // SQL�������� ����
			pstmt.setInt(1, getNext() - (pageNumber - 1) * 10);
			rs = pstmt.executeQuery(); // �������� ������.
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
		// Ư�� ���ں��� ���� �� ������ �������� �Խñ� 10���� �����´�. try {
		try {
			PreparedStatement pstmt = conn.prepareStatement(SQL); // SQL�������� ����
			pstmt.setInt(1, getNext() - (pageNumber - 1) * 10);
			rs = pstmt.executeQuery(); // �������� ������.
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
		// Ư�� ���ں��� ���� �� ������ �������� �Խñ� 10���� �����´�. try {
		try {
			PreparedStatement pstmt = conn.prepareStatement(SQL); // SQL�������� ����
			pstmt.setInt(1, bbsID);
			rs = pstmt.executeQuery(); // �������� ������.
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
