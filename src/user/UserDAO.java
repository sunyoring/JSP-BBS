package user;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class UserDAO { //DAO : �����ͺ��̽� ���� ��ü, �����ͺ��̽����� ������ �ҷ����ų� ���� �� ����Ѵ�.

	private Connection conn; //�����ͺ��̽��� ����
	private PreparedStatement pstmt;
	private ResultSet rs; //������ ��� ��ü
	
	public UserDAO() { //mySQL�� �����ϴ� �κ�
		try {
			String dbURL = "jdbc:mysql://localhost:3306/BBS"; //3306��Ʈ ������ BBS�� �����ϴ� �ּ�
			String dbID="root";
			String dbPassword="root";
			Class.forName("com.mysql.jdbc.Driver");//mysql�� ������ �� �� �ְ��ϴ� ���̺귯��
			conn = DriverManager.getConnection(dbURL,dbID,dbPassword); //dbURL�� ID��Password�� �̿��� ����.
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	public int login(String userID, String userPassword) { //�α��� �Լ�
		String SQL = "SELECT userPassword FROM USER WHERE userID = ? "; //������ ���̽��� �Է��� ��ɾ� ���� 
															//USER���̺��� �ش� ������� ��й�ȣ�� �����´�.
		try {
			pstmt = conn.prepareStatement(SQL); //SQL������ ����.
			pstmt.setString(1, userID);
			rs = pstmt.executeQuery();
			if (rs.next()) { //�����ϴ� ���̵� �ִٸ�?
				if(rs.getString(1).equals(userPassword)) { //��й�ȣ�� �´ٸ� ?
					return 1; //�α��� ����
				}
				else
					return 0; //��й�ȣ ����ġ
			}
			return -1;//���̵����
			
		}catch (Exception e){
			e.printStackTrace();
		}
		return -2; //�����ͺ��̽� ������ �ǹ�
	}
}
