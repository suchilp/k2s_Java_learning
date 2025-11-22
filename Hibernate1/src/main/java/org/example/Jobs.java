package org.example;

public class Jobs {

    public String getJobsName() {
        return jobsName;
    }

    public void setJobsName(String jobsName) {
        this.jobsName = jobsName;
    }

    public int getJobId() {
        return jobId;
    }

    public void setJobId(int jobId) {
        this.jobId = jobId;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }


    @Override
    public String toString() {
        return "Jobs{" +
                "jobsName='" + jobsName + '\'' +
                ", jobId=" + jobId +
                ", designation='" + designation + '\'' +
                '}';
    }

    private String jobsName;
    private int jobId;
    private String designation;
}
