package jp.or.ja_kyousai.batch.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

@Component
@Mapper
public interface MembersMapper {

    /*
    @Insert({
            "<script>",
            "INSERT INTO MEMBERS(id, name, value) VALUES(#{id},#{name},#{value})",
            "</script>"
    })*/

    void truncate();

    void insert(@Param("id") String id, @Param("name") String name, @Param("value") String value);

    void delete(@Param("id") String id);

}
