package com.example.myapplication;

public class JobItem_foratyourservice {

    private final String jobtitle;

    private final String company;
    private final String location;

    private final String website;

    /**
     * Constructs a person object with the specified name and age.
     *
     * @param jobtitle - name to be given to the job.
     * @param location -  job location.
     * @param website -  job link.
     *
     */
    public JobItem_foratyourservice(String jobtitle, String company, String location, String website) {
        this.jobtitle = jobtitle;
        this.location = location;
        this.website = website;
        this.company = company;
    }

    public String getCompany() {
        return company;
    }

    public String getJobtitle() {
        return jobtitle;
    }

    public String getLocation() {
        return location;
    }

    public String getWebsite() {
        return website;
    }
}
