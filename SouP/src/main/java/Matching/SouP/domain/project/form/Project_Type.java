package Matching.SouP.domain.project.form;

public enum Project_Type {
    PLATFORM("플랫폼"),GAME("게임"),SERVICE("서비스 제공"),CLONE("클론코딩"),ETC("기타");
    private final String description;

    Project_Type(String description) {
        this.description = description;
    }
    public String getDescription(){
        return description;
    }
}
