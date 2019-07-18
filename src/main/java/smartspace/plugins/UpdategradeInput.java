package smartspace.plugins;

import com.google.common.collect.Sets;

public class UpdategradeInput {
	private String studentId;
	private int newGrade;
	
	public UpdategradeInput() {
		
	}
	
	public String getStudentId() {
		return studentId;
	}
	
	public void setStudentId(String studentId) {
		this.studentId = studentId;
	}
	
	public int getNewGrade() {
		return newGrade;
	}
	
	public void setNewGrade(int newGrade) {
		this.newGrade = newGrade;
	}
}
