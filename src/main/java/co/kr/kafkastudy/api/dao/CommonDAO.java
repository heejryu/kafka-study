package co.kr.kafkastudy.api.dao;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CommonDAO {

    @Autowired
    private SqlSessionTemplate sqlSession;

    public int selectCount(String queryId, Object parameterObject){
        return sqlSession.selectOne(queryId, parameterObject);
    }

    public <E> List<E> selectList(String queryId, Object parameterObject){
        return sqlSession.selectList(queryId, parameterObject);
    }

    public <E> List<E> selectList(String queryId){
        return sqlSession.selectList(queryId);
    }

    public <T> T select(String queryId, int parameterInt){
        return sqlSession.selectOne(queryId, parameterInt);
    }

    public <T> T select(String queryId, Object parameterObject){
        return sqlSession.selectOne(queryId, parameterObject);
    }

    public <T> T select(String queryId){
        return sqlSession.selectOne(queryId);
    }

    public int insert(String queryId, Object parameterObject){
        return sqlSession.insert(queryId, parameterObject);
    }

    public int insert(String queryId){
        return sqlSession.insert(queryId);
    }

    public int update(String queryId, Object parameterObject){
        return sqlSession.update(queryId, parameterObject);
    }

    public int update(String queryId){
        return sqlSession.update(queryId);
    }

    public int delete(String queryId, Object parameterObject){
        return sqlSession.delete(queryId, parameterObject);
    }

    public int delete(String queryId){
        return sqlSession.delete(queryId);
    }

}


