package Matching.SouP.domain.project.form;

public enum Project_Place {
    SEOUL("서울/경기"),OTHER("이외");

    private final String description;

    Project_Place(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
