package com.bs.model.dto;

import java.io.Serializable;
import java.sql.Date;
import java.util.Objects;

public class Employee implements Serializable {
	private static final long serialVersionUID = 5308135442685031512L;
	
	private String empId;
	private String empName;
	private String empNo;
	private String email;
	private String phone;
	private String deptCode;
	private String jobCode;
	private String salLevel;
	private int salary;
	private double bonus;
	private String managerId;
	private Date hireDate;
	private Date entDate;
	private char entYn;
	private Department department;
	private Job job;
	
	public Employee(Builder builder) {
		super();
		this.empId = builder.empId;
		this.empName = builder.empName;
		this.empNo = builder.empNo;
		this.email = builder.email;
		this.phone = builder.phone;
		this.deptCode = builder.deptCode;
		this.jobCode = builder.jobCode;
		this.salLevel = builder.salLevel;
		this.salary = builder.salary;
		this.bonus = builder.bonus;
		this.managerId = builder.managerId;
		this.hireDate = builder.hireDate;
		this.entDate = builder.entDate;
		this.entYn = builder.entYn;
		this.department = builder.department;
		this.job = builder.job;
	}
	
	public static class Builder {
		private String empId;
		private String empName;
		private String empNo;
		private String email;
		private String phone;
		private String deptCode;
		private String jobCode;
		private String salLevel;
		private int salary;
		private double bonus;
		private String managerId;
		private Date hireDate;
		private Date entDate;
		private char entYn;
		private Department department;
		private Job job;
		
		public Builder() {}
		
		public Builder empId(String empId) {
			this.empId = empId;
			return this;
		}
		
		public Builder empName(String empName) {
			this.empName = empName;
			return this;
		}
		
		public Builder empNo(String empNo) {
			this.empNo = empNo;
			return this;
		}
		
		public Builder email(String email) {
			this.email = email;
			return this;
		}
		
		public Builder phone(String phone) {
			this.phone = phone;
			return this;
		}
		
		public Builder deptCode(String deptCode) {
			this.deptCode = deptCode;
			return this;
		}
		
		public Builder jobCode(String jobCode) {
			this.jobCode = jobCode;
			return this;
		}
		
		public Builder salLevel(String salLevel) {
			this.salLevel = salLevel;
			return this;
		}
		
		public Builder salary(int salary) {
			this.salary = salary;
			return this;
		}
		
		public Builder bonus(double bonus) {
			this.bonus = bonus;
			return this;
		}
		
		public Builder managerId(String managerId) {
			if (managerId == null) {
				this.managerId = "없음";
				return this;
			}
			
			this.managerId = managerId;
			return this;
		}
		
		public Builder hireDate(Date hireDate) {
			this.hireDate = hireDate;
			return this;
		}
		
		public Builder entDate(Date entDate) {
			this.entDate = entDate;
			return this;
		}
		
		public Builder entYn(char entYn) {
			this.entYn = entYn;
			return this;
		}
		
		public Builder department(Department department) {
			this.department = department;
			return this;
		}
		
		public Builder job(Job job) {
			this.job = job;
			return this;
		}
		
		public Employee build() {
			return new Employee(this);
		}
	}

	public String getEmpId() {
		return empId;
	}

	public void setEmpId(String empId) {
		this.empId = empId;
	}

	public String getEmpName() {
		return empName;
	}

	public void setEmpName(String empName) {
		this.empName = empName;
	}

	public String getEmpNo() {
		return empNo;
	}

	public void setEmpNo(String empNo) {
		this.empNo = empNo;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getDeptCode() {
		return deptCode;
	}

	public void setDeptCode(String deptCode) {
		this.deptCode = deptCode;
	}

	public String getJobCode() {
		return jobCode;
	}

	public void setJobCode(String jobCode) {
		this.jobCode = jobCode;
	}

	public String getSalLevel() {
		return salLevel;
	}

	public void setSalLevel(String salLevel) {
		this.salLevel = salLevel;
	}

	public int getSalary() {
		return salary;
	}

	public void setSalary(int salary) {
		this.salary = salary;
	}

	public double getBonus() {
		return bonus;
	}

	public void setBonus(double bonus) {
		this.bonus = bonus;
	}

	public String getManagerId() {
		return managerId;
	}

	public void setManagerId(String managerId) {
		this.managerId = managerId;
	}

	public Date getHireDate() {
		return hireDate;
	}

	public void setHireDate(Date hireDate) {
		this.hireDate = hireDate;
	}

	public Date getEntDate() {
		return entDate;
	}

	public void setEntDate(Date entDate) {
		this.entDate = entDate;
	}

	public char getEntYn() {
		return entYn;
	}

	public void setEntYn(char entYn) {
		this.entYn = entYn;
	}

	public Department getDepartment() {
		return department;
	}

	public void setDepartment(Department department) {
		this.department = department;
	}

	public Job getJob() {
		return job;
	}

	public void setJob(Job job) {
		this.job = job;
	}

	@Override
	public int hashCode() {
		return Objects.hash(bonus, department, deptCode, email, empId, empName, empNo, entDate, entYn, hireDate, job,
				jobCode, managerId, phone, salLevel, salary);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Employee other = (Employee) obj;
		return Double.doubleToLongBits(bonus) == Double.doubleToLongBits(other.bonus)
				&& Objects.equals(department, other.department) && Objects.equals(deptCode, other.deptCode)
				&& Objects.equals(email, other.email) && Objects.equals(empId, other.empId)
				&& Objects.equals(empName, other.empName) && Objects.equals(empNo, other.empNo)
				&& Objects.equals(entDate, other.entDate) && entYn == other.entYn
				&& Objects.equals(hireDate, other.hireDate) && Objects.equals(job, other.job)
				&& Objects.equals(jobCode, other.jobCode) && Objects.equals(managerId, other.managerId)
				&& Objects.equals(phone, other.phone) && Objects.equals(salLevel, other.salLevel)
				&& salary == other.salary;
	}

	@Override
	public String toString() {
		return "Employee [empId=" + empId + ", empName=" + empName + ", empNo=" + empNo + ", email=" + email
				+ ", phone=" + phone + ", deptCode=" + deptCode + ", jobCode=" + jobCode + ", salLevel=" + salLevel
				+ ", salary=" + salary + ", bonus=" + bonus + ", managerId=" + managerId + ", hireDate=" + hireDate
				+ ", entDate=" + entDate + ", entYn=" + entYn + ", department=" + department + ", job=" + job + "]";
	}
}
