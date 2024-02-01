package co.kr.kafkastudy.repository;

import co.kr.kafkastudy.entity.SongSave;
import org.springframework.data.repository.CrudRepository;

public interface SongSaveRedisRepository extends CrudRepository<SongSave, String> {
}
