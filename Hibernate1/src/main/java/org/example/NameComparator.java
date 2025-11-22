package org.example;

import java.util.Comparator;

public class NameComparator implements Comparator<EmployeeDemo> {
    @Override
    public int compare(EmployeeDemo o1, EmployeeDemo o2) {
        return o1.empName.compareTo(o2.empName);
    }
}
