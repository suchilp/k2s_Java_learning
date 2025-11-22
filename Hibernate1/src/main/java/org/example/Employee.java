package org.example;

public class Employee  implements  Comparable<Employee>{
    @Override
    public String toString() {
        return "Employee{" +
                "empName='" + empName + '\'' +
                ", empId=" + empId +
                ", designation='" + designation + '\'' +
                '}';
    }

    String empName;
    int empId;
    String designation;
    public String getEmpName() {
        return empName;
    }

    public void setEmpName(String empName) {
        this.empName = empName;
    }

    public int getEmpId() {
        return empId;
    }

    public void setEmpId(int empId) {
        this.empId = empId;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }


    @Override
    public int compareTo(Employee o) {
        return Integer.compare(o.empId,this.empId);
    }
}
