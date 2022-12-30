package top.yinzsw.blog;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.MimeType;
import org.springframework.util.MimeTypeUtils;

@SpringBootTest
class BlogApplicationTests {

    @Test
    void contextLoads() {

    }

    public static void main(String[] args) {
//        File file = new File("/temp\\\\\\\\////","///ss/g");
//        System.out.println(file.getAbsolutePath());
//        System.out.println(file.getPath());
//
//        Path ss = Paths.get("", "ss");
//        System.out.println(ss.getFileName());
//        System.out.println(ss.toString());
//
//        UriComponents build = UriComponentsBuilder
//                .fromHttpUrl("https://localhost")
//                .path("xs")
//                .path("/tt\\dd.jpg")
//                .build();
//        System.out.println(build.toUriString());
//        System.out.println(UriComponentsBuilder.fromPath("/dsada").path("//ss").build());


        MimeType mimeType1 = MimeTypeUtils.parseMimeType("*/*");
        MimeType mimeType2 = MimeTypeUtils.parseMimeType("*/*");
        System.out.println(mimeType1.includes(mimeType2));
    }
}
