package com.nhnnext.nextagram;

public class ArticleDTO {

    private final int articleNumber;
    private final String title;
    private final String writer;
    private final String id;
    private final String content;
    private final String writeDate;
    private final String imgName;

    public ArticleDTO(int articleNumber, String title, String writer, String id, String content, String writeDate, String imgName) {
        this.articleNumber = articleNumber;
        this.title = title;
        this.writer = writer;
        this.id = id;
        this.content = content;
        this.writeDate = writeDate;
        this.imgName = imgName;
    }

    public int getArticleNumber() {
        return articleNumber;
    }

    public String getTitle() {
        return title;
    }

    public String getWriter() {
        return writer;
    }

    public String getId() {
        return id;
    }

    public String getContent() {
        return content;
    }

    public String getWriteDate() {
        return writeDate;
    }

    public String getImgName() {
        return imgName;
    }
}
