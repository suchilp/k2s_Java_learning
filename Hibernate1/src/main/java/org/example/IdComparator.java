package org.example;

import java.util.Comparator;

public class IdComparator implements Comparator<EmployeeDemo> {
    @Override
    public int compare(EmployeeDemo o1, EmployeeDemo o2) {
        return Integer.compare(o1.empId,o2.empId);
    }
}
