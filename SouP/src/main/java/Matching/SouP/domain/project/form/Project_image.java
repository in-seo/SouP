//package StudyMatching.cloneWeb.domain.project.form;
//
//import StudyMatching.cloneWeb.domain.project.ProjectInfo;
//import lombok.Getter;
//import lombok.Setter;
//import lombok.ToString;
//
//import javax.persistence.*;
//
//@Entity
//@Getter @Setter
//@ToString
//public class Project_image {
//
//    @Id
//    @GeneratedValue
//    @Column(name = "image_id")
//    private Long id;
//
//    @OneToOne(mappedBy = "image")
//    private ProjectInfo project;
//
//    private String uuid;
//    private String fileName;
//    private String contentType;
//
//    public Project_image(String uuid, String fileName, String contentType) {
//        this.uuid = uuid;
//        this.fileName = fileName;
//        this.contentType = contentType;
//    }
//
//    public Project_image() {
//
//    }
//}
