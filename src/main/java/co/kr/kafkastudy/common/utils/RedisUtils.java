package co.kr.kafkastudy.common.utils;

import co.kr.kafkastudy.model.redis.CreateKey;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Slf4j
public class RedisUtils {

    @Autowired
    private RedisTemplate redisTemplate;

    public void createKeyInfo(String key , String value, long score){
        /* REDIS KEY에 맵핑된 데이터 순서대로 넣기 */
        log.info("[RedisUtils.createHashTypeKey] START {}" , key);
        try {
            ZSetOperations zSetOperations = redisTemplate.opsForZSet();
            zSetOperations.add(key, value, score);
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<CreateKey> getKeyInfo(String key) {
        ObjectMapper objectMapper = new ObjectMapper(); // linkedHashMap으로 저장된 redis 값들을 List로 변환해줌
        ZSetOperations<String, CreateKey> zSetOps = redisTemplate.opsForZSet();
//        List<CreateKey> result = objectMapper.convertValue(
//                Objects.requireNonNull(zSetOps.reverseRange(key, 0, -1))
//                ,new TypeReference<List<CreateKey>>() {});
        List<CreateKey> result = new ArrayList<>(Objects.requireNonNull(zSetOps.reverseRange(key,0,-1)));
        return result;
    }

    // sorted set (opsForZSet)
    public void setSortedSetOps(String key, List<CreateKey> values){
        for(CreateKey v : values){
            redisTemplate.opsForZSet().add(key, v.getValue(), v.getScore());
        }
    }

//    public Set getSortedSetOps(String key){
//        Long len = redisTemplate.opsForZSet().size(key);
//        return len == 0 ? new HashSet<String>() : redisTemplate.opsForZSet().range(key, 0, len-1);
//    }

    public boolean deleteKey(String key) {
        boolean isDelete = redisTemplate.delete(key);
        return isDelete;
    }
}
