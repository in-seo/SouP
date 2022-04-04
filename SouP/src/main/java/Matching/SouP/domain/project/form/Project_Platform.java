package Matching.SouP.domain.project.form;

public enum Project_Platform {
    WEB("웹"), APP("앱"), NONE("미정");

    private final String description;

    Project_Platform(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
