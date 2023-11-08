//package com.example.LanguageAppMongoDb.resource;

// works with photo upload
//import lombok.*;
//import org.springframework.web.multipart.MultipartFile;
//
//@Getter
//@Setter
//@NoArgsConstructor
//@AllArgsConstructor
//@ToString
//public class ProfileRequest {
//    private String name;
//    private String nativeLanguage;
//    private String languageToLearn;
//    private String about;
//    private String email;
//
//
//    private MultipartFile image;
//    private String imagePath;
//
//    private String imageUrl;
//
//
//    public static ProfileRequestBuilder customBuilder() {
//        return new ProfileRequestBuilder();
//    }
//
//    public static class ProfileRequestBuilder {
//        private String name;
//        private String nativeLanguage;
//        private String languageToLearn;
//        private String about;
//        private String email;
//        private MultipartFile image;
//        private String imagePath;
//
//        private String imageUrl;
//
//        ProfileRequestBuilder() {
//        }
//
//        public ProfileRequestBuilder name(String name) {
//            this.name = name;
//            return this;
//        }
//
//        public ProfileRequestBuilder imageUrl(String imageUrl) {
//            this.imageUrl = imageUrl;
//            return this;
//        }
//
//        // ... other setter methods for properties
//
//        public ProfileRequestBuilder imagePath(String imagePath) {
//            this.imagePath = imagePath;
//            return this;
//        }
//
//        public ProfileRequest build() {
//            return new ProfileRequest(name, nativeLanguage, languageToLearn, about, email, image, imagePath, imageUrl);
//        }
//    }
//}

package com.example.LanguageAppMongoDb.resource;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ProfileRequest {
    private String name;
    private String nativeLanguage;
    private String languageToLearn;
    private String about;
    private String email;
    private String imagePath;
    private String imageUrl;

    public static ProfileRequestBuilder customBuilder() {
        return new ProfileRequestBuilder();
    }

    public static class ProfileRequestBuilder {
        private String name;
        private String nativeLanguage;
        private String languageToLearn;
        private String about;
        private String email;
        private String imagePath;
        private String imageUrl;

        ProfileRequestBuilder() {
        }

        public ProfileRequestBuilder name(String name) {
            this.name = name;
            return this;
        }

        public ProfileRequestBuilder imageUrl(String imageUrl) {
            this.imageUrl = imageUrl;
            return this;
        }

        public ProfileRequestBuilder imagePath(String imagePath) {
            this.imagePath = imagePath;
            return this;
        }

        public ProfileRequest build() {
            return new ProfileRequest(name, nativeLanguage, languageToLearn, about, email, imagePath, imageUrl);
        }
    }
}
