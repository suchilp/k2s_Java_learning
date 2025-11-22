package org.example;

import java.util.Comparator;

public class DesignationComparator implements Comparator<EmployeeDemo> {
    @Override
    public int compare(EmployeeDemo o1, EmployeeDemo o2) {
        return o1.designation.compareTo(o2.designation);
    }
}
