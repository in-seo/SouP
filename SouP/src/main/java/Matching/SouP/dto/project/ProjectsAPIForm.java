package Matching.SouP.dto.project;

import lombok.Getter;

@Getter
public class ProjectsAPIForm {
    private String title;
    private String userName;
    private Long id;

    public ProjectsAPIForm(String title, String userName, Long id) {
        this.title = title;
        this.userName = userName;
        this.id = id;
    }

    protected ProjectsAPIForm(){}
}
