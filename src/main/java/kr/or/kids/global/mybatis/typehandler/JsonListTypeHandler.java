package kr.or.kids.global.mybatis.typehandler;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;

public class JsonListTypeHandler<T> extends BaseTypeHandler<List<T>> {

    private static final ObjectMapper mapper = new ObjectMapper()
            // SQL JSON 키가 prdct_nm 같은 snake_case 이므로 VO camelCase로 자동 매핑
            .setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE);

    private final TypeReference<List<T>> typeRef;

    public JsonListTypeHandler(TypeReference<List<T>> typeRef) {
        this.typeRef = typeRef;
    }

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, List<T> parameter, JdbcType jdbcType)
            throws SQLException {
        try {
            ps.setString(i, mapper.writeValueAsString(parameter));
        } catch (Exception e) {
            throw new SQLException("Failed to write JSON", e);
        }
    }

    @Override
    public List<T> getNullableResult(ResultSet rs, String columnName) throws SQLException {
        return parse(rs.getString(columnName));
    }

    @Override
    public List<T> getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        return parse(rs.getString(columnIndex));
    }

    @Override
    public List<T> getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        return parse(cs.getString(columnIndex));
    }

    private List<T> parse(String json) throws SQLException {
        if (json == null || json.isBlank()) return Collections.emptyList();
        try {
            return mapper.readValue(json, typeRef);
        } catch (Exception e) {
            throw new SQLException("Failed to parse JSON: " + json, e);
        }
    }
}
