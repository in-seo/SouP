package Matching.SouP.domain.post;

public enum Source {
    OKKY("OKKY"), INFLEARN("인프런"), HOLA("홀라"), CAMPICK("캠퍼스픽"), SOUP("SOUP");

    private String krName;

    Source(String krName) {
        this.krName = krName;
    }

    public String getKrName() {
        return krName;
    }
}
