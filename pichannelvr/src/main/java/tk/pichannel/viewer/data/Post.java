package tk.pichannel.viewer.data;

import tk.pichannel.Utilities;

/**
 * Created by HamnLee on 2016/10/27.
 */

public class Post {

    private String id;
    private String postUnixtimestampeOriginal;
    private String userId;
    private String imageFileName;
    private String imageSrc;
    private String text;

    public Post(String id, String postUnixtimestampeOriginal, String userId, String imageFileName, String imageSrc, String text) {
        this.id = id;
        this.postUnixtimestampeOriginal = postUnixtimestampeOriginal;
        this.userId = userId;
        this.imageFileName = imageFileName;
        this.imageSrc = imageSrc;
        this.text = text;
    }

    public String getId() {
        return id;
    }

    public String getPostUnixtimestampeOriginal() {
        return postUnixtimestampeOriginal;
    }

    public String getUserId() {
        return userId;
    }

    public String getImageSrc() {
        return imageSrc;
    }

    public String getImageFileName() {
        return imageFileName;

    }

    public String getText() {
        return text;
    }


    public static class Builder {

        private String id;
        private String postUnixtimestampeOriginal;
        private String userId;
        private String imageFileName;
        private String imageSrc;
        private String text;

        public Post build() {
            return new Post(id,postUnixtimestampeOriginal,userId,imageFileName,imageSrc,text);
        }

        public Builder id (String id) {
            this.id = id;
            return this;
        }

        public Builder userId (String userId) {
            this.userId = userId;
            return this;
        }

        public Builder postUnixtimestampeOriginal(String postUnixtimestampeOriginal) {
            this.postUnixtimestampeOriginal = postUnixtimestampeOriginal;
            return this;
        }

        public Builder imageSrc(String imageSrc) {
            this.imageSrc = imageSrc;
            this.imageFileName = Utilities.getImageFolderNameAsStringByURL(imageSrc)
                    + Utilities.getImageFileNameAsStringByURL(imageSrc);
            return this;
        }

        public Builder text(String text) {
            this.text = text;
            return this;
        }
    }
}
