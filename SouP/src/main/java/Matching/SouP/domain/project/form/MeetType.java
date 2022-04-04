package Matching.SouP.domain.project.form;

public enum MeetType {
    NONE("상관없음"), ON("온라인"), OFF("오프라인");

    private final String description;

    MeetType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
