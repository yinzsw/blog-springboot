package top.yinzsw.blog.model.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * IP 信息模型
 *
 * @author yinzsW
 * @since 22/12/15
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class IpInfoDTO {
    private String status;

    @JsonAlias("t")
    private String timestamp;

    @JsonAlias("set_cache_time")
    private String cacheTime;

    private List<Detail> data;

    @JsonIgnore
    public String getFirstLocation() {
        if (CollectionUtils.isEmpty(this.data)) {
            return null;
        }
        return this.data.get(0).location;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Detail {
        @JsonAlias("ExtendedLocation")
        private String extendedLocation;

        @JsonAlias("origip")
        private String originIp;

        @JsonAlias("resourceid")
        private String resourceId;

        @JsonAlias("titlecont")
        private String title;

        private String location;
    }
}
